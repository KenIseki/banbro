package banbro.io.bdx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import banbro.io.FileIO;
import banbro.model.Clef;
import banbro.model.bdx.BDX;
import banbro.model.bdx.BDXDate;
import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BinaryUtil;
import banbro.model.bdx.Chord;
import banbro.model.bdx.EffectType;
import banbro.model.bdx.Guitar;
import banbro.model.bdx.GuitarChord;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Note;
import banbro.model.bdx.Part;
import banbro.model.bdx.Piano;
import banbro.model.bdx.PianoChord;
import banbro.model.bdx.PlayLevel;
import banbro.model.bdx.SingleNote;
import banbro.model.bdx.StepValue;
import banbro.model.bdx.VibratoShape;
import banbro.model.bdx.Voicing;
import banbro.model.bdx.Voicing.Spread;

public class BDXFileIO {

	public static final String EXTENSION = "bdx";
	public static final FileFilter FILE_FILTER =
			new FileNameExtensionFilter("�o���u��DX�y���t�@�C��(*." + EXTENSION + ")", EXTENSION);
	public static final int MODE_BDX = 0;  // ���@�ŉ\�Ȕ͈�
	public static final int MODE_BDX_FILE = 1;  // �o�C�i���t�@�C���Ƃ��ėL���Ȕ͈�
	public static final int MODE_BDXML = 2;  // BDXML�͈̔�

	public static List<Integer> readBDXBinary(File file) throws IOException {
		if (FileIO.isSupportedFile(file, EXTENSION)==false) {
			throw new IOException(file + "\"��bdx�t�@�C���ł͂���܂���B");
		}
		List<Integer> list = new ArrayList<Integer>();
		try (FileInputStream in = new FileInputStream(file);) {
			int ch;
			while ( (ch=in.read()) != -1 ) {
				list.add(ch);
			}
			boolean isBDX = list.size()>=0x8000;
			if (isBDX) {
				String st = BinaryUtil.byteToString(list.subList(0x0004, 0x0004+8));
				isBDX = st.equals("BBDX1234");
			}
			if (isBDX==false) {
				throw new IOException(file.getPath() + "\"��bdx�t�@�C���ł͂���܂���B");
			}
			in.close();
		} catch (IOException e) {
			throw e;
		}
		return list;
	}

	public static BDX createBDX(List<Integer> binary) {
		if (binary==null || binary.isEmpty()) {
			return null;
		}
		BDX bdx = createHeaderBDX(binary);

		boolean isGuitar = false;
		boolean isPiano = false;
		for (int i=0; i<8; i++) {  // bdx�͍ő�8�p�[�g
			InstrumentType type = InstrumentType.valueOf(binary.get(0xCB + 16*i));
			int b = binary.get(0x00CA + 16*i);
			if (0x31<=b && b<=0x59) {
				b -= 0x30;
			} else if (0x5A<=b && b<=0x82) {
				b -= 0x59;
			} else if (0x8D<=b && b<=0x96) {
				b -= 0x0A;
			} else if (0x97<=b && b<=0xA0) {
				b -= 0x14;
			}
			BDXInstrument instrument = BDXInstrument.valueOf(b);
			if (type==InstrumentType.NONE || instrument==BDXInstrument.NONE) {
				bdx.addPart(Part.NULL_PART);
				continue;
			}
			Part p = new Part(type);
			p.setInstrument(instrument);

			// 0x00c8 �y��(16*8)
			//   c8:����(2)  cc:�p��  cd:�}�X�^�[�A�v��  ce:index  cf:�A�}�A�r�M�i�[  d0:0(8)
			//   ca:�y��@cb:���t�^�C�v(0:single, 1:drm, 2:guitar 3:piano)
			p.setPartVolume(BinaryUtil.byteToVolume(binary.get(0x00c8 + 16*i)));
			if (type==InstrumentType.GUITAR) {
				isGuitar = true;
				// 0x4B08 �M�^�[�{�^�����蓖��
				//    (�ʒu,FFFF,��,��,��,�E,FFFF,L��,L��,L��,L�E,FFFF)�̌J��Ԃ�(2*12)
				for (int bt=0; bt<32; bt++) {
					int step = binary.get(0x4B09 + 24*bt)*0x0100 + binary.get(0x4B08 + 24*bt);
					if (step==0xFFFF) {
						break;
					}
					List<Chord> button = new ArrayList<Chord>();
					for (int j=0; j<4; j++) {
						int root = binary.get(0x4B0D + 2*j + 24*bt) & 0x0F;
						int flag = ((binary.get(0x4B0D + 2*j + 24*bt) & 0xF0) >> 4);
						int name = binary.get(0x4B0C + 2*j + 24*bt);
						Chord chord = BinaryUtil.createChord(root, flag, name);
						button.add(chord);
					}
					for (int j=0; j<4; j++) {
						int root = binary.get(0x4B17 + 2*j + 24*bt) & 0x0F;
						int flag = ((binary.get(0x4B17 + 2*j + 24*bt) & 0xF0) >> 4);
						int name = binary.get(0x4B16 + 2*j + 24*bt);
						Chord chord = BinaryUtil.createChord(root, flag, name);
						button.add(chord);
					}
					p.addChordSet(step, button);
				}
			}
			if (type==InstrumentType.PIANO) {
				isPiano = true;
				// 0x6608 �s�A�m�{�^�����蓖��
				List<Chord> button = new ArrayList<Chord>();
				for (int bt=0; bt<32; bt++) {
					int root = binary.get(0x6609 + 2*bt) & 0x0F;
					int flag = ((binary.get(0x6609 + 2*bt) & 0xF0) >> 4);
					int name = binary.get(0x6608 + 2*bt);
					Chord chord = BinaryUtil.createChord(root, flag, name);
					button.add(chord);
				}
				p.addChordSet(0, button);
			}
			p.setCloneNum(binary.get(0x00CE + 16*i));
			int instLevel1, instLevel2;
			instLevel1 = binary.get(0x00CD + 16*i);
			instLevel2 = binary.get(0x00CF + 16*i);
			p.setPlayLevel(PlayLevel.BEGINNER, instLevel2%16);
			p.setPlayLevel(PlayLevel.AMATEUR, instLevel2/16);
			p.setPlayLevel(PlayLevel.PRO, instLevel1%16);
			p.setPlayLevel(PlayLevel.MASTER, instLevel1/16);
			p.setPan(BinaryUtil.byteToPan(binary.get(0x00CC + 16*i)));

			// 0x01e8 ���F����(12*8)
			//   e8:Attack  e9:Decay  ea:Sustain  eb:Release
			//   ec:SHAPE  ed:Hold  ee:Delay  F0:Depth  EF:Speed
			//   f2:EFFECTS(NONE,CROSS,ECHO,CHORUS)  f3:�G�t�F�N�^�[�l(0-20)
			p.setToneAttack(BinaryUtil.byteToAttack(binary.get(0x01E8 + 12*i)));
			p.setToneDecay(BinaryUtil.byteToDecay(binary.get(0x01E9 + 12*i)));
			p.setToneSustain(BinaryUtil.byteToSustain(binary.get(0x01EA + 12*i)));
			p.setToneRelease(BinaryUtil.byteToRelease(binary.get(0x01EB + 12*i)));
			p.setVibratoShape(VibratoShape.valueOf(binary.get(0x01EC + 12*i)));
			p.setVibratoHold(BinaryUtil.byteToHold(binary.get(0x01ED + 12*i)));
			p.setVibratoDelay(BinaryUtil.byteToDelay(binary.get(0x01EE + 12*i)));
			p.setVibratoDepth(BinaryUtil.byteToDepth(binary.get(0x01F0 + 12*i)));
			p.setVibratoSpeed(BinaryUtil.byteToSpeed(binary.get(0x01EF + 12*i)));
			p.setToneEffectType(EffectType.valueOf(binary.get(0x01F2 + 12*i)));
			p.setToneEffectValue(binary.get(0x01F3 + 12*i));

			// Single Note
			// 0x0248 ����(8*2048*8)
			//   00=�x��  48=5c  C8=5c�L�΂�  FF=3�A��
			//   18(2c)-6B(8b)
			//   �h������1-A���e�y��ɑΉ�(�㔼��+������)(BAYX��������LR)
			//   �s�A�m�̓{�^����(01-),�Ή���0x6608(2)
			//      DS  US  LDS  LUS    -01     -05
			//   S  05  07   19   1B   04,06   14,16
			//   H  09  0B   1D   1F   08,0A
			//   U  0D  0F   21   23   0C,0E
			//   M  11  13   25   27   10,12
			for (int j=0; j<4*bdx.getTimeNum(); j++) {
				Note n = Note.getInstance(binary.get(0x0248 + 2048*i + j), p.getType());
				p.addNote(n);
			}
			// Chord
			//   Chords, Rhythm, Original Chord, EFFECTS�Ȃ�
			// Piano
			//   Voicing, Top Note, Notes(3,4), Spread
			// Guitar
			//   Stroke, Sustain=0

			// 0x42c8 ���ʁA�L�[�A�I�N�^�[�u�͈́A�a���̕ω�(8*32*8)
			//   c8:�ʒu(2)
			//   ca:key(���{�^���̉�)�܂��̓{�C�V���O�ō���(1)  cb:�{�^�����蓖�ĕω��܂��̓{�C�V���O�����ƍL����(1)
			//   cc:����(1)  cd:�ύX�t���O(1)(0x01)
			int lastBass = -1;
			int lastButton = -1;
			Voicing lastVoicing = null;
			int lastVolume = -1;
			for (int j=0; j<32; j++) {
				int step = binary.get(0x42C9 + 256*i + 8*j)*0x0100 + binary.get(0x42C8 + 256*i + 8*j);
				int bass = binary.get(0x42CA + 256*i + 8*j);
				int button = binary.get(0x42CB + 256*i + 8*j);
				int volume = binary.get(0x42CC + 256*i + 8*j);
				int flag = binary.get(0x42CD + 256*i + 8*j);
				if (step==0xFFFF) {
					break;
				}
				if (type==InstrumentType.SINGLE) {
					if (bass!=lastBass) {
						p.addBass(step, bass);
						lastBass = bass;
					}
					if (button!=lastButton) {
						lastButton = button;
						if (button>=0x80) {
							button -= 0x100;
						}
						p.addButton(step, button);
					}
				} else if (type==InstrumentType.PIANO) {
					Voicing v = new Voicing(bass, button/3+3, Spread.valueOf(button%3));
					if (!v.equals(lastVoicing)) {
						p.addVoicing(step, v);
						lastVoicing = v;
					}
				}
				if (volume!=lastVolume || flag==0x00) {  // ���ʂ��ς�邩�A�ύX�t���O����
					p.addVolume(step, BinaryUtil.byteToVolume(volume));
					lastVolume = volume;
				}
			}
			
			if (type==InstrumentType.SINGLE) {
				// 0x6d18 �����ݒ�(4��*128����) 0x6D18+512n
				//   80:�g�� 81:�։� 82:�g��(1o)�@-80:�O�Ɠ���
				List<StepValue<Clef>> clefList = new ArrayList<StepValue<Clef>>();
				int lastClef = -1;
				for (int j=0; j<bdx.getTimeNum(); j++) {
					int step = 12*j;
					int clef = binary.get(0x6D18 + 512*i + j);
					if (clef>=0x80) {
						clef -= 0x80;
					}
					if (clef!=lastClef) {
						clefList.add(new StepValue<Clef>(step, Clef.valueOf(clef)));
						lastClef = clef;
					}
				}
				p.setClef(clefList);
			}

			bdx.addPart(p);
		}

		// 0x4248 �e���|(4*32)
		//   48:�ʒu(2) 16��������3�A1��12�A1����48
		//   4a:�l(2)  ex)0x1801=0x01*16*16+0x18=280
		List<StepValue<Integer>> tempoList = new ArrayList<StepValue<Integer>>();
		for (int i=0; i<32; i++) {
			int step = binary.get(0x4249 + 4*i)*0x0100 + binary.get(0x4248 + 4*i);
			int tempo = binary.get(0x424B + 4*i)*0x0100 + binary.get(0x424A + 4*i);
			if (step==0xFFFF) {
				break;
			}
			tempoList.add(new StepValue<Integer>(step, tempo));
		}
		bdx.setTempo(tempoList);

		if (isGuitar) {
			Guitar guitar = new Guitar();
			// 0x4AC8 �M�^�[�I���W�i���R�[�h(4*16)
			//  00 00 00 40  01000000 00000000 00000000 00000000  000000 ******
			//  10 42 08 61  01100001 00001000 01000010 00010000  000000 ------
			//  EF BD F7 5E  01011110 11110111 10111101 11101111  FFFFFF ******
			//  62 08 00 60  01100000 00000000 00001000 01100010  000232 -*****
			for (int i=0; i<16; i++) {
				int f6 = ((binary.get(0x4ACB + 4*i) & 0x1E) >> 1);
				int f5 = ((binary.get(0x4ACA + 4*i) & 0xF0) >> 4);
				int f4 = ((binary.get(0x4ACA + 4*i) & 0x07) << 1) + ((binary.get(0x4AC9 + 4*i) & 0x80) >> 7);
				int f3 = ((binary.get(0x4AC9 + 4*i) & 0x3C) >> 2);
				int f2 = ((binary.get(0x4AC9 + 4*i) & 0x01) << 3) + ((binary.get(0x4AC8 + 4*i) & 0xE0) >> 5);
				int f1 = binary.get(0x4AC8 + 4*i) & 0x0F;
				boolean m6 = (binary.get(0x4ACB + 4*i) & 0x20) != 0;
				boolean m5 = (binary.get(0x4ACB + 4*i) & 0x01) != 0;
				boolean m4 = (binary.get(0x4ACA + 4*i) & 0x08) != 0;
				boolean m3 = (binary.get(0x4AC9 + 4*i) & 0x40) != 0;
				boolean m2 = (binary.get(0x4AC9 + 4*i) & 0x02) != 0;
				boolean m1 = (binary.get(0x4AC8 + 4*i) & 0x10) != 0;
				boolean used = (binary.get(0x4ACB + 4*i) & 0x40) != 0;
				GuitarChord c = new GuitarChord(f6, f5, f4, f3, f2, f1, m6, m5, m4, m3, m2, m1);
				c.setUsed(used);
				guitar.addOriginalChord(c);
			}
			bdx.setGuitar(guitar);
		}

		// 0x4E08 �̎�(�ő�2048����)
		List<String> lyric = new ArrayList<String>();
		List<Integer> charEncodingList = binary.subList(0x4E08, 0x5608);
		assert(charEncodingList.size()==2048);
		int from = 0;
		int to = 2048;
		while (from<to) {
			List<Integer> lyricList = charEncodingList.subList(from, to);
			String st = BinaryUtil.byteToString(lyricList);
			if (st==null) {
				break;
			}
			lyric.add(st);
			int next = lyricList.indexOf(0x00);
			if (next<0) {
				break;
			}
			from += next + 1;
		}
		for (int i=lyric.size()-1; i>=0; i--) {
			String page = lyric.get(i);
			if (page.equals("\n\n") || page.equals("\n")) {
				lyric.remove(i);
			} else {
				break;
			}
		}
		bdx.setLyric(lyric);
		// 0x5608 �̎����蓖��(�ʒu������ł邾��) 0x7FFF �Ŋ��蓖�ďI��
		// ����ȕ���(���s�A�X�y�[�X�A���_�Ȃ�)��+0x4000
		// �ő�2048����
		List<StepValue<String>> timing = new ArrayList<StepValue<String>>();
		List<Integer> charList = new ArrayList<Integer>();
		int lastStep = 0;
		boolean kigou = false;
		for (int i=0; i<2048; i++) {
			int step = binary.get(0x5609 + 2*i)*0x0100 + binary.get(0x5608 + 2*i);
			if (step==0x3FFF) {
				break;
			}
			if (step==0x7FFF) {
				break;
			}
			if (step==0xFFFF) {
				break;
			}
			int charEncoding = charEncodingList.get(i);
			if (step>=0x4000) {
				step = step%0x4000;
			}
			if (charEncoding==0xDE || charEncoding==0xDF) {  // ���_�Ɣ����_
				step = lastStep;
			}
			// �����R�[�h�ł͂Ȃ������ɂ���
			if (step!=lastStep) {
				String str = BinaryUtil.byteToString(charList, false);
				if (str!=null) {
					timing.add(new StepValue<String>(lastStep, str));
				}
				charList.clear();
				if (kigou) {
					charList.add(0x80);
				}
				lastStep = step;
			}
			charList.add(charEncoding);
			kigou = (charEncoding==0x80);
		}
		String str = BinaryUtil.byteToString(charList, false);
		if (str!=null) {
			timing.add(new StepValue<String>(lastStep, str));
		}
		bdx.setLyricTiming(timing);

		if (isPiano) {
			Piano piano = new Piano();
			// 0x6648 �s�A�m�I���W�i���R�[�h(4*16)
			//   4,3,2,1
			// 0x7F1A �g�p�t���O(2)
			for (int i=0; i<16; i++) {
				SingleNote n1 = SingleNote.getInstance(binary.get(0x6648 + 4*i));
				SingleNote n2 = SingleNote.getInstance(binary.get(0x6649 + 4*i));
				SingleNote n3 = SingleNote.getInstance(binary.get(0x664A + 4*i));
				SingleNote n4 = SingleNote.getInstance(binary.get(0x664B + 4*i));
				boolean used = (binary.get(0x7F1A + i/8) & (0x01<<i%8)) != 0;
				PianoChord c = new PianoChord(n4, n3, n2, n1);
				c.setUsed(used);
				piano.addOriginalChord(c);
			}
			// 0x7F18 �{�C�V���O
			//   7F18:�ō���(1) 7F19:0-5(3��,3��,3�e,4��,4��,4�e)
			//   ex)48 00 03 00 00 00 3�� 0000
			//      48 04 0F 80 00 00 4�� 0100
			//      48 02 00 00 00 00 3�e 0010
			piano.setVoicing(binary.get(0x7F18), binary.get(0x7F19)/3+3, Spread.valueOf(binary.get(0x7F19)%3));
			bdx.setPiano(piano);
		}

		// 0x6918 �R�[�h�z�u
		//   6918:�R�[�h��(1)  691C:(�ʒu(2),�R�[�h(2))(4)
		//     1234
		//       12:00-0B  M  m  7  M7  m7  dim  m7b5  aug  sus4  7sus4  6  add9
		//       3:(0: 1:p 2:b) 4:root(0:C - 6:B)
		//     �I���W�i��  00FF-0FFF
		if (isGuitar || isPiano) {
			int chordNum = binary.get(0x6918);
			for (int i=0; i<chordNum; i++) {
				int step = binary.get(0x691D + 4*i)*0x0100 + binary.get(0x691C + 4*i);
				int root = binary.get(0x691F + 4*i) & 0x0F;
				int flag = ((binary.get(0x691F + 4*i) & 0xF0) >> 4);
				int name = binary.get(0x691E + 4*i);
				Chord chord = BinaryUtil.createChord(root, flag, name);
				bdx.addChordTiming(step, chord);
			}
		}

		// 0x7d18 �]���ݒ�(4*128)
		//   ��=�l-88  81:-7 88:0 8F:+7 -80:�O�Ɠ���
		List<StepValue<Integer>> keyList = new ArrayList<StepValue<Integer>>();
		int lastKey = 99;
		for (int i=0; i<bdx.getTimeNum(); i++) {
			int step = 12*i;
			int key = binary.get(0x7D18 + i);
			if (key<=0x80) {
				key += 0x80;
			}
			key -= 0x88;
			if (key!=lastKey) {
				keyList.add(new StepValue<Integer>(step, key));
				lastKey = key;
			}
		}
		bdx.setKey(keyList);

		bdx.trim();
		return bdx;
	}

	public static BDX createHeaderBDX(List<Integer> binary) {
		BDX bdx = new BDX();
		// �s���l
		// 0x0010 �X���b�g�g�p�H(4)
		//  �g�p=0x80000001 ,���g�p=0x00000000
		// HMAC-SHA1,0x0018,20,76-DF-E5-29-A6-EE-�c,20�o�C�g�`�F�b�N�T���Abdx�ł�0
		// �̎��֘A?,0x002C,20,BC-BC-BB-DE-CF-A4-�c,�J���I�P�Ɋ֘A������H
		// �s��,0x0040,4,0x00000000,0x00000000�`0x00000044�̒l�H,
		// �s��,0x0044,4,0x000000FF,����0x000000FF
		
		// 
		bdx.setTime(binary.get(0x00AA));
		bdx.addTitle(BinaryUtil.to2ByteString( BinaryUtil.byteToString(binary.subList(0x0048, 0x0048+32)) ));
		bdx.addTitle(BinaryUtil.to2ByteString( BinaryUtil.byteToString(binary.subList(0x0068, 0x0068+32)) ));
		bdx.addTitle(BinaryUtil.to2ByteString( BinaryUtil.byteToString(binary.subList(0x0088, 0x0088+32)) ));

		// 0x00AB:���ߐ�+6  0x00AD:���ߐ�+1
		int bars1 = Math.max(1, binary.get(0x00AB)-6);
		int bars2 = Math.max(1, binary.get(0x00AD)-1);
		int bars = Math.min(bars1, bars2);
		bdx.setBars(bars);

		// 0x00AE ���C���y��(0-7)
		//   �����ꍇ��FF
		int main = binary.get(0x00AE);
		if (main!=0xFF) {
			bdx.setMelodyPartNum(main);
		}
		// 0x00AF �J���I�P�H 
		bdx.setLyrics(binary.get(0x00AF) == 0x01);
		
		// 0x00B4 �C�V�������ȂȂ�0�A����ȊO�Ȃ�1
		bdx.setContributed(binary.get(0x00B5)==0x01);
		bdx.setReceived(binary.get(0x00B6)==0x01);

		// 0x00B7 �}�X�^�[�{�����[��
		//   C0:0.5 00:1.0 40:1.5
		//   02:1.01  FF:0.99
		//   b>=C0  b-80��ϊ�����0.��t����  // b>=80
		//   b<=40  b��ϊ�����1.��t����  // b<=7F
		int mv = binary.get(0x00B7);
		if (mv<=0x7F) {
			bdx.setMasterVolume(100 + BinaryUtil.byteToVolume(mv));
		} else {
			bdx.setMasterVolume(BinaryUtil.byteToVolume(mv-0x80));
		}

		// 0x00B8 ��t�ԍ�(4)
		long serialId = 0;
		serialId += binary.get(0x00B8);
		serialId += binary.get(0x00B9) * 0x100;
		serialId += binary.get(0x00BA) * 0x10000;
		serialId += binary.get(0x00BB) * 0x1000000l;
		bdx.setSerialId(serialId);

		// 0x00bc �쐬�� �����N(3)
		bdx.setCreateDate(new BDXDate(binary.get(0x00BC), binary.get(0x00BD), binary.get(0x00BE)));

		// 0x00c0 �X�V�� �����N(3)
		bdx.setSaveDate(new BDXDate(binary.get(0x00C0), binary.get(0x00C1), binary.get(0x00C2)));

		// 0x014A ���e�Җ��̒���(2) �ő�156
		// 0x014C ���e�Җ�
		// ���e�Җ�����0x01E7�܂ŃR�����g
		int nameLength = binary.get(0x014A);
		if (nameLength>156) {
			nameLength = 156;
		}
		if (nameLength>0) {
			bdx.setSubmitter(BinaryUtil.to2ByteString(
					BinaryUtil.byteToString(binary.subList(0x014C, 0x014C+nameLength)) ));
		}
		if (156-nameLength>0) {
			bdx.setComment(BinaryUtil.byteToString(binary.subList(0x014C+nameLength, 0x01E7)));
		}
		return bdx;
	}

	public static void writeBDXFile(BDX bdx, int mode) {
		if (mode>MODE_BDX_FILE || getBDXMode(bdx)>mode) {
			return;
		}
		List<Integer> bin = new ArrayList<Integer>();
		skipBinary(bin, 0x0004);  // �`�F�b�N�T��
		bin.addAll(BinaryUtil.stringToByte("BBDX12343000"));
		bin.addAll(BinaryUtil.to4byteBinary(0x80000001l));
		bin.addAll(BinaryUtil.to4byteBinary(0));
		skipBinary(bin, 0x002C);  // �`�F�b�N�T��
		skipBinary(bin, 0x0040, 0xFF);  // �̎��֘A�̕s���l
		bin.addAll(BinaryUtil.to4byteBinary(0));
		bin.addAll(BinaryUtil.to4byteBinary(0xFF000000l));
		List<String> titleLabels = bdx.getTitleLabels();
		for (String label : titleLabels) {
			if (label.length()==0) {
				bin.addAll(skipBinary(Arrays.asList(0x10), 32));
			} else {
				bin.addAll(skipBinary(BinaryUtil.stringToByte(label), 32));
			}
		}
		skipBinary(bin, 0x00AA);
		bin.add(bdx.getTime());
		bin.add(bdx.getBars()+6);
		bin.add(0);
		bin.add(bdx.getBars()+1);
		bin.add(bdx.getMelodyPartNum()<0 ? 0xFF : bdx.getMelodyPartNum());
		bin.add(bdx.isLyrics() ? 1 : 0);
		bin.addAll(BinaryUtil.to4byteBinary(0x20080116l));
		bin.add(1);
		bin.add(bdx.isContributed() ? 1 : 0);
		bin.add(bdx.isReceived() ? 1 : 0);
		int volume = bdx.getMasterVolume();
		assert(50<=volume && volume<=150);
		if (volume<100) {
			bin.add(BinaryUtil.VolumeValue[volume] + 0x80);
		} else {
			bin.add(BinaryUtil.VolumeValue[volume-100]);
		}
		bin.addAll(BinaryUtil.to4byteBinary(bdx.getSerialId()));
		BDXDate date;
		date = bdx.getCreateDate();
		bin.add(date.getYear());
		bin.add(date.getMonth());
		bin.add(date.getDay());
		bin.add(0);
		date = bdx.getCreateDate();
		bin.add(date.getYear());
		bin.add(date.getMonth());
		bin.add(date.getDay());
		bin.add(0);
		skipBinary(bin, 0x00C8);
		for (int i=0; i<8; i++) {
			Part p = bdx.getPart(i);
			InstrumentType type = p.getType();
			if (type!=InstrumentType.NONE) {
				bin.add(BinaryUtil.VolumeValue[p.getPartVolume()]);
				bin.add(0);
				int instNum = p.getInstrument().getNum();
				if (type==InstrumentType.GUITAR) {
					instNum += (instNum<0x83 ? 0x30 : 0x0A);
				} else if (type==InstrumentType.PIANO) {
					instNum += (instNum<0x83 ? 0x59 : 0x14);
				}
				bin.add(instNum);
				bin.add(type.intValue());
				for (int key : BinaryUtil.PanMap.keySet()) {
					if (BinaryUtil.PanMap.get(key)==p.getPan()) {
						bin.add(key);
						break;
					}
				}
				skipBinary(bin, 0x00CD + 16*i);
				bin.add((p.getPlayLevel(PlayLevel.MASTER)<<4) + p.getPlayLevel(PlayLevel.PRO));
				bin.add(p.getCloneNum());
				bin.add((p.getPlayLevel(PlayLevel.AMATEUR)<<4) + p.getPlayLevel(PlayLevel.BEGINNER));
			}
			skipBinary(bin, 0x00C8 + 16*(i+1));
		}
		skipBinary(bin, 0x014A);
		List<Integer> sub = BinaryUtil.stringToByte(bdx.getSubmitter());
		bin.addAll(BinaryUtil.to2byteBinary(sub.size()));
		bin.addAll(sub);
		skipBinary(bin, 0x01E8);
		for (int i=0; i<8; i++) {  // ���F����
			Part p = bdx.getPart(i);
			InstrumentType type = p.getType();
			if (type!=InstrumentType.NONE) {
				bin.add(BinaryUtil.AttackValue[p.getToneAttack()]);
				bin.add(BinaryUtil.DecayValue[p.getToneDecay()]);
				bin.add(BinaryUtil.SustainValue[p.getToneSustain()]);
				bin.add(BinaryUtil.ReleaseValue[p.getToneRelease()]);
				bin.add(p.getVibratoShape().getValue());
				bin.add(BinaryUtil.HoldValue[p.getVibratoHold()]);
				bin.add(BinaryUtil.DelayValue[p.getVibratoDelay()]);
				bin.add(BinaryUtil.SpeedValue[p.getVibratoSpeed()]);
				bin.add(BinaryUtil.DepthValue[p.getVibratoDepth()]);
				bin.add(0);
				bin.add(p.getToneEffectType().getTypeValue());
				bin.add(p.getToneEffectValue());
			}
			skipBinary(bin, 0x01E8 + 12*(i+1));
		}
		skipBinary(bin, 0x0248);
		for (int i=0; i<8; i++) {  // ����
			Part p = bdx.getPart(i);
			InstrumentType type = p.getType();
			if (type!=InstrumentType.NONE) {
				for (Note note : p.getNoteList()) {
					bin.add(note.getNoteNum());
				}
			}
			skipBinary(bin, 0x0248 + 2048*(i+1));
		}
		skipBinary(bin, 0x4248);
		for (StepValue<Integer> sv : bdx.getTempo()) {
			bin.addAll(BinaryUtil.to2byteBinary(sv.getStep()));
			bin.addAll(BinaryUtil.to2byteBinary(sv.getValue()));
		}
		skipBinary(bin, 0x42C8);
		for (int i=0; i<8; i++) {  // �g�����
			Part p = bdx.getPart(i);
			InstrumentType type = p.getType();
			if (type!=InstrumentType.NONE) {
				
			}
			skipBinary(bin, 0x42C8 + 256*(i+1));
		}
		skipBinary(bin, 0x4AC8);
		skipBinary(bin, 0x4B08);
		skipBinary(bin, 0x4E08);
		skipBinary(bin, 0x6608);
		skipBinary(bin, 0x6648);
		skipBinary(bin, 0x6918);
		skipBinary(bin, 0x6D18);
		for (int i=0; i<8; i++) {  // ����
			Part p = bdx.getPart(i);
			InstrumentType type = p.getType();
			int clef = 0;
			if (type==InstrumentType.SINGLE) {
				// TODO
			}
			skipBinary(bin, 0x6D18 + 512*(i+1), clef);
		}
		
		skipBinary(bin, 0x7D18);
		skipBinary(bin, 0x7F18);
		// TODO
		skipBinary(bin, 0x8000);
	}

	public static int getBDXMode(BDX bdx) {
		if (checkBDXFile(bdx, MODE_BDX).isEmpty()) {
			return MODE_BDX;
		} else if (checkBDXFile(bdx, MODE_BDX_FILE).isEmpty()) {
			return MODE_BDX_FILE;
		}
		return MODE_BDXML;
	}

	private static List<String> checkBDXFile(BDX bdx, int mode) {
		if (mode!=MODE_BDX && mode!=MODE_BDX_FILE) {
			throw new IllegalArgumentException();
		}
		List<String> message = new ArrayList<String>();
		if (mode==MODE_BDX && bdx.getBars()>120) {
			message.add("�y�����������܂�(120���߂܂�)");
		}
		if (mode==MODE_BDX_FILE && bdx.getTimeNum()>512) {
			message.add("�y�����������܂�(512���܂�)");
		}
		List<String> labels = bdx.getTitleLabels();
		if (labels.isEmpty()) {
			message.add("�Ȗ�������܂���");
		} else {
			int size = labels.size();
			if (size>3) {
				message.add("�Ȗ��̍s�����������܂�(3�s�܂�)");
			} else {
				if (mode==MODE_BDX) {
					int[] len = new int[] {0,0,0};
					for (int i=0; i<labels.size(); i++) {
						len[i] = labels.get(i).length();
					}
					int[] max = new int[3];
					if (size==1) {
						max[0] = 9;
						max[1] = len[1];
						max[2] = len[2];
					} else if (size==2) {
						max[0] = 10;
						max[1] = 8;
						max[2] = len[2];
					} else {// size==3
						max[0] = 11;
						max[1] = 9;
						max[2] = 7;
					}
					for (int i=0; i<len.length; i++) {
						if (len[i]>max[i]) {
							message.add("�Ȗ���" + (i+1) + "�s�ڂ̕��������������܂�(" + max[i] + "�����܂�)");
						}
					}
				} else if (mode==MODE_BDX_FILE) {
					for (int i=0; i<labels.size(); i++) {
						String label = labels.get(i);
						if (BinaryUtil.stringToByte(label).size()>32) {
							message.add("�Ȗ���" + (i+1) + "�s�ڂ�32byte�𒴂��Ă��܂�");
						}
					}
				}
			}
		}
		if (bdx.getPartNum()>8) {
			message.add("�p�[�g�����������܂�(8�p�[�g�܂�)");
		}
		List<Part> guitarParts = new ArrayList<Part>();
		List<Part> pianoParts = new ArrayList<Part>();
		for (int partNum=0; partNum<bdx.getPartNum(); partNum++) {
			Part p = bdx.getPart(partNum);
			InstrumentType type = p.getType();
			if (type==InstrumentType.NONE) {
				continue;
			}
			if (type==InstrumentType.GUITAR) {
				guitarParts.add(p);
				continue;
			}
			if (type==InstrumentType.PIANO) {
				pianoParts.add(p);
				continue;
			}
		}
		if (guitarParts.size()+pianoParts.size()>1) {
			message.add("�a���p�[�g���������܂�(1�p�[�g�܂�)");
		}
		// TODO
		return message;
	}

	private static List<Integer> skipBinary(List<Integer> bin, int size) {
		return skipBinary(bin, size, 0);
	}
	/**
	 * bin�̃T�C�Y��size�ɂȂ�܂�value�Ŗ��߂�
	 * @param bin
	 * @param size
	 * @param value
	 * @return bin
	 */
	private static List<Integer> skipBinary(List<Integer> bin, int size, int value) {
		int n = size - bin.size();
		if (n<0) {
			throw new IllegalArgumentException();
		}
		for (int i=0; i<n; i++) {
			bin.add(value);
		}
		return bin;
	}
	
}
