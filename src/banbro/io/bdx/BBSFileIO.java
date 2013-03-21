package banbro.io.bdx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import banbro.io.FileIO;
import banbro.model.Clef;
import banbro.model.bdx.BDX;
import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.BinaryUtil;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Note;
import banbro.model.bdx.Part;
import banbro.model.bdx.PlayLevel;

public class BBSFileIO {

	public static final String EXTENSION = "bbs";
	public static final FileFilter FILE_FILTER =
			new FileNameExtensionFilter("バンブラ楽譜ファイル(*." + EXTENSION + ")", EXTENSION);

	public static List<Integer> readBBSBinary(File file) throws IOException {
		if (FileIO.isSupportedFile(file, EXTENSION)==false) {
			throw new IOException(file.getPath() + "\"はbbsファイルではありません。");
		}
		List<Integer> list = new ArrayList<Integer>();
		try (FileInputStream in = new FileInputStream(file);) {
			int ch;
			while ( (ch=in.read()) != -1 ) {
				list.add(ch);
			}
			boolean isBBS = list.size()>=0x6000;
			if (isBBS) {
				String st = BinaryUtil.byteToString(list.subList(0x0000, 0x0000+8));
				isBBS = st.equals("BBRS_GAK");
			}
			if (isBBS==false) {
				throw new IOException(file.getPath() + "\"はbbsファイルではありません。");
			}
		} catch (IOException e) {
			throw e;
		}
		return list;
	}

	public static BDX createBDX(List<Integer> binary) {
		if (binary.isEmpty()) {
			return null;
		}
		int ad;
		BDX bdx = new BDX();
		bdx.setTime(4);  // 0x00A2 に入ってるけど4拍子のみ
		bdx.addTitle(BinaryUtil.to2ByteString( BinaryUtil.byteToString(binary.subList(0x0040, 0x0040+32)) ));
		bdx.addTitle(BinaryUtil.to2ByteString( BinaryUtil.byteToString(binary.subList(0x0060, 0x0060+32)) ));
		bdx.addTitle(BinaryUtil.to2ByteString( BinaryUtil.byteToString(binary.subList(0x0080, 0x0080+32)) ));
		int bars = binary.get(0x00A3)-6;
		boolean isExtra = false;
		if (binary.get(0x00A5)==0xFF) {
			isExtra = true;
		} else {
			bars = Math.max(bars, binary.get(0x00A5)-1);
			if (bars>128) {  // 128小節超え
				isExtra = true;
			}
		}
		bdx.setBars(bars);
		bdx.setMasterVolume(150);
		// テンポ
		ad = binary.get(0x00A0) + binary.get(0x00A1)*0x100 + 0x40;
		int lastStep = -1;
		int lastTempo = -1;
		while (ad+3<binary.size()) {
			int step = binary.get(ad) + binary.get(ad+1)*0x100;
			if (step<lastStep || step==0xFFFF) {
				break;
			}
			lastStep = step;
			int tempo = binary.get(ad+2) + binary.get(ad+3)*0x100;
			if (lastTempo!=tempo) {
				bdx.addTempo(step, tempo);
				lastTempo = tempo;
			}
			ad += 4;
		}
		// 転調
		if (isExtra) {
			bdx.addKey(0, 0);
		} else {
			ad = 0x5342;
			int lastKey = 0xFF;
			for (int m=0; m<128; m++) {  // 128小節まで
				int step = BDX.TIME_BASE*bdx.getTime()*m;
				int key = binary.get(ad+m);
				if (key>=0x80) {
					key = key - 0x100;
				}
				if (lastKey!=key) {
					bdx.addKey(step, key);
					lastKey = key;
				}
			}
		}


		for (int i=0; i<8; i++) {  // 最大8パート
			InstrumentType type = InstrumentType.valueOf(binary.get(0x00C3+16*i));
			BDXInstrument instrument = BDXInstrument.valueOf(binary.get(0x00C2+16*i));
			if (type==InstrumentType.NONE || instrument==BDXInstrument.NONE) {
				bdx.addPart(Part.NULL_PART);
				continue;
			}
			Part p = new Part(type);
			p.setInstrument(instrument);
			p.setPartVolume(85);
			if (type==InstrumentType.SINGLE) {
				p.setPan(BinaryUtil.byteToPan(binary.get(0x00C4+16*i)));
			}
			int playLevel = binary.get(0x00C5+16*i);
			int levelPro = Math.min(playLevel/16, 5);
			int levelAma = Math.min(playLevel%16, 5);
			p.setPlayLevel(PlayLevel.BEGINNER, (levelAma+1)/2);
			p.setPlayLevel(PlayLevel.AMATEUR, levelAma);
			p.setPlayLevel(PlayLevel.PRO, levelAma+(levelPro+1)/2);
			p.setPlayLevel(PlayLevel.MASTER, levelPro+levelAma);
			p.setCloneNum(binary.get(0x00C6+16*i));
			// 譜面
			p.clearNotes();
			ad = binary.get(0x00C0+16*i) + binary.get(0x00C1+16*i)*0x100 + 0x40;
			int length = 4*bdx.getTimeNum();
			for (int noteI=0; noteI<length; noteI++) {
				if (ad>=binary.size()) {
					break;
				}
				Note note = Note.getInstance(binary.get(ad), type);
				p.addNote(note);
				ad++;
			}
			// 拡張情報
			ad = binary.get(0x00CA+16*i) + binary.get(0x00CB+16*i)*0x100 + 0x40;
			lastStep = -1;
			int lastBass = -1;
			int lastButton = 0xFF;
			int lastVolume = -1;
			while (ad+7<binary.size()) {
				int step = binary.get(ad) + binary.get(ad+1)*0x100;
				if (step<lastStep || step==0xFFFF) {
					break;
				}
				lastStep = step;
				int bass = binary.get(ad+2);
				int button = binary.get(ad+3);
				if (button>=0x80) {
					button = button - 0x100;
				}
				int volume = binary.get(ad+4);
				if (lastBass!=bass) {
					if (type==InstrumentType.SINGLE) {
						p.addBass(step, bass);
					}
					lastBass = bass;
				}
				if (lastButton!=button) {
					if (type==InstrumentType.SINGLE) {
						p.addButton(step, button);
					}
					lastButton = button;
				}
				if (lastVolume!=volume) {
					p.addVolume(step, BinaryUtil.byteToVolume(volume));
					lastVolume = volume;
				}
				ad += 8;
			}
			// タッチ譜面（未対応）　0x00C8
			// 音部記号
			if (type==InstrumentType.SINGLE) {
				if (isExtra) {
					p.addClef(0, Clef.G2);
				} else {
					ad = 0x5570 + 128*i;
					Clef lastClef = null;
					for (int m=0; m<128; m++) {  // 128小節まで
						int step = BDX.TIME_BASE*bdx.getTime()*m;
						Clef clef = Clef.valueOf(binary.get(ad+m));
						if (clef!=lastClef) {
							p.addClef(step, clef);
							lastClef = clef;
						}
					}
				}
			}
			BDXUtil.setDefaultTone(p);
			bdx.addPart(p);
		}

		bdx.trim();
		return bdx;
	}

}
