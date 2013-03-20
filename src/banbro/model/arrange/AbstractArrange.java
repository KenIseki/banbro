package banbro.model.arrange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import banbro.model.bdx.BDX;
import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.BinaryUtil;
import banbro.model.bdx.Guitar;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Note;
import banbro.model.bdx.Part;
import banbro.model.bdx.Piano;
import banbro.model.bdx.StepValue;
import banbro.model.bdx.Voicing;

/**
 * アレンジを行うクラス
 * 全てのアレンジはこのクラスを継承して作成する
 */
public abstract class AbstractArrange {

	private BDX _bdx;
	private boolean _isArrangeTempo;
	private boolean _isBDX;

	/**
	 * サブクラスは引数なしのコンストラクタを持つこと
	 * @see ArrangeFactory#createInstance(PerformanceStyle)
	 */
	protected AbstractArrange() {
		this(true, true);
	}
	
	/**
	 * アレンジ対象のBDXは{@link #setBDX(BDX)}で追加する
	 * 他の情報は{@link #setOption(String, Object)}で追加する
	 * @param isArrangeTempo テンポ変更をするならtrue
	 * @param isBDX BDXファイルの範囲に収めるならtrue
	 * @return
	 * @see #setBDX(BDX)
	 * @see #setOption(String, Object)
	 */
	protected AbstractArrange(boolean isArrangeTempo, boolean isBDX) {
		_isArrangeTempo = isArrangeTempo;
		_isBDX = isBDX;
	}
	
	protected BDX getBDX() {
		return _bdx;
	}
	protected boolean isArrangeTempo() {
		return _isArrangeTempo;
	}
	protected boolean isBDX() {
		return _isBDX;
	}

	public synchronized void setBDX(BDX bdx) {
		_bdx = bdx;
	}
	public synchronized void setIsArrangeTempo(boolean isArrangeTempo) {
		_isArrangeTempo = isArrangeTempo;
	}
	public synchronized void setIsBDX(boolean isBDX) {
		_isBDX = isBDX;
	}

	/**
	 * コンストラクタで渡せない他の情報を追加する
	 * デフォルトは何もしない
	 * @param optionName
	 * @param optionValue
	 */
	public synchronized void setOption(String optionName, Object optionValue) {}

	/**
	 * アレンジを行う
	 * 1つのBDXに対して1回のみ呼び出し可
	 * @return アレンジ後のBDX
	 * @see #setBDX(BDX)
	 */
	public synchronized final BDX arrange() {
		if (_bdx==null) {
			throw new IllegalStateException("既に変換済みかBDXがありません。新しいBDXをセットしてください。");
		}
		BDX bdx = doArrange(_bdx);
		_bdx = null;
		return bdx;
	}

	/**
	 * アレンジの流れ
	 * @param bdx
	 * @return
	 */
	protected BDX doArrange(BDX bdx) {
		BDX copy = bdx.clone();
		arrangeBDX(copy);
		double tempoAverage = (bdx.getTimeNum()) * 60 / BDXUtil.calcPlayTime(bdx);
		if (isArrangeTempo() && !isBDX()) {
			arrangeTempo(copy.getTempo(), tempoAverage);
		}
		Guitar guitar = copy.getGuitar();
		if (guitar!=null) {
			arrangeGuitar(guitar);
		}
		Piano piano = copy.getPiano();
		if (piano!=null) {
			arrangePiano(piano);
		}
		Part mainPart = getMainPart(copy);
		for (int i=0; i<copy.getPartNum(); i++) {
			Part p = copy.getPart(i);
			if (p.getType()==InstrumentType.NONE) {
				continue;
			}
			if (p==mainPart) {
				arrangeMainPart(p);
			} else {
				arrangePart(p);
			}
			switch (p.getType()) {
			case SINGLE:
				arrangeSinglePart(p);
				break;
			case DRUMS:
				arrangeDrumPart(p);
				break;
			case GUITAR:
				arrangeGuitarPart(p);
				break;
			case PIANO:
				arrangePianoPart(p);
			default:
				continue;
			}
		}
		copy.trim();
		return copy;
	}

	/**
	 * 全体に影響する項目を変更する
	 * デフォルトは何もしない
	 * @param bdx
	 */
	protected void arrangeBDX(BDX bdx) {}

	/**
	 * 演奏イメージによるテンポ変更
	 * デフォルトは何もしない
	 * @param tempo 変更前のテンポ
	 * @param tempoAverage
	 */
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {}

	/**
	 * テンポをrate%に変更する
	 * @param tempo 変更前のテンポ
	 * @param tempoAverage 平均テンポ
	 * @param rate 倍率%
	 * @param min 最小テンポ
	 * @param max 最大テンポ
	 * @see #arrangeTempo(List, BDX)
	 */
	protected final void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage, int rate, double min, double max) {
		if (isArrangeTempo()==false || isBDX()==true) {
			return;
		}
		if ((tempoAverage*rate/100) < min) {
			rate = (int)Math.ceil(min * 100 / tempoAverage);
		} else if ((tempoAverage*rate/100) > max) {
			rate = (int)Math.ceil(max * 100 / tempoAverage);
		}
		for (StepValue<Integer> sv : tempo) {
			int value = sv.getValue();
			if (value>1) {
				value = Math.max((value * rate / 100), 1);
				sv.setValue(value);
			}
		}
	}

	/**
	 * @param bdx
	 * @return メインパート（メロディパートまたは番号が最小の単音パート）
	 */
	protected final Part getMainPart(BDX bdx) {
		Part mainPart = null;
		mainPart = bdx.getPart(bdx.getMelodyPartNum());
		if (mainPart.getType()!=InstrumentType.SINGLE) {
			mainPart = null;
		}
		if (mainPart==null) {
			for (int i=0; i<bdx.getPartNum(); i++) {
				Part p = bdx.getPart(i);
				if (p.getType()==InstrumentType.SINGLE) {
					mainPart = p;
					break;
				}
			}
		}
		return mainPart;
	}

	/**
	 * 全演奏タイプに共通する、1パートの内容を変更する
	 * デフォルトは何もしない
	 * @param p
	 */
	protected void arrangePart(Part p) {}

	/**
	 * メインパートの内容を変更する
	 * デフォルトは{@link #arrangePart(Part)}
	 * @param p
	 */
	protected void arrangeMainPart(Part p) {
		arrangePart(p);
	}

	/**
	 * 単音パートの内容を変更する
	 * デフォルトは何もしない
	 * @param p
	 */
	protected void arrangeSinglePart(Part p) {}

	/**
	 * ギターコードパートの内容を変更する
	 * デフォルトは何もしない
	 * @param p
	 */
	protected void arrangeGuitarPart(Part p) {}

	/**
	 * ピアノ和音パートの内容を変更する
	 * デフォルトは何もしない
	 * @param p
	 */
	protected void arrangePianoPart(Part p) {}

	/**
	 * ドラムパートの内容を変更する
	 * デフォルトは何もしない
	 * @param p
	 */
	protected void arrangeDrumPart(Part p) {}

	/**
	 * ギターコードパートに影響する設定
	 * オリジナルコード
	 * デフォルトは何もしない
	 * @param g
	 */
	protected void arrangeGuitar(Guitar g) {}

	/**
	 * ピアノ和音パートに影響する設定
	 * オリジナルコード・ボイシング
	 * ただし、ここでボイシングを変更しても演奏に影響しない
	 * デフォルトは何もしない
	 * @param p
	 * @see #arrangePianoPart(Part)
	 */
	protected void arrangePiano(Piano p) {}

	/**
	 * binaryを書き換える
	 * @param binary 元のバイナリ
	 * @param bdx アレンジ後のbdx
	 * @see #arrange()
	 * @see #arrangePartBinary(List, BDX, int)
	 */
	public synchronized void arrangeBinary(List<Integer> binary, BDX bdx) {
		int b = 0;
		// マスターボリューム
		int volume = bdx.getMasterVolume();
		if (volume<100) {
			b = BinaryUtil.VolumeValue[volume] + 0x80;
		} else {
			b = BinaryUtil.VolumeValue[volume-100];
		}
		binary.set(0x00B7, b);
		if (bdx.isLyrics()) {
			// 全体:歌詞、歌詞割り当て
			List<String> lyric = bdx.getLyric();
			List<Integer> lyricBinary = new ArrayList<Integer>();
			for (String st : lyric) {
				lyricBinary.addAll(BinaryUtil.stringToByte(st));
				lyricBinary.add(0x00);
			}
			assert(lyricBinary.size()<=0x0800);  // 最大2048byte
			List<StepValue<String>> lyricTiming = bdx.getLyricTiming();
			int step = 0;
			int timingIndex = 0;
			boolean kigou = false;
			for (int i=0; i<lyricBinary.size(); i++) {
				b = lyricBinary.get(i);
				List<Integer> stepBinary;
				// 特殊な文字(改行、スペース、濁点など)は+0x4000
				// 0x80は記号フラグ
				if (BinaryUtil.isSpecialChar(b) && b!=0x80) {
					stepBinary = BinaryUtil.to2byteBinary(step + 0x4000);
					kigou = false;
				} else {
					if (kigou==false) {
						if (timingIndex>=lyricTiming.size()) {
							break;
						}
						step = lyricTiming.get(timingIndex).getStep();
						timingIndex++;
					}
					if (b==0x80) {
						stepBinary = BinaryUtil.to2byteBinary(step + 0x4000);
						kigou = true;
					} else {
						stepBinary = BinaryUtil.to2byteBinary(step);
						kigou = false;
					}
				}
				// 歌詞
				binary.set(0x4E08 + i, b);
				// 歌詞割り当て
				binary.set(0x5608 + 2*i, stepBinary.get(0));
				binary.set(0x5608 + 2*i + 1, stepBinary.get(1));
			}
		}
		for (int i=0; i<8; i++) {  // 8:パート数
			arrangePartBinary(binary, bdx, i);
		}
	}

	/**
	 * 1パートの部分だけ書き換える
	 * @param binary
	 * @param bdx
	 * @param partNum
	 * @see #arrangeBinary(List, BDX)
	 */
	protected void arrangePartBinary(List<Integer> binary, BDX bdx, int partNum) {
		Part p = bdx.getPart(partNum);
		int instNum = p.getInstrument().getNum();
		final InstrumentType type = p.getType();
		binary.set(0xCB + 16*partNum, type.intValue());
		if (type==InstrumentType.NONE || p.getInstrument()==BDXInstrument.NONE) {
			return;
		}
		// 楽器変更
		// ギターコードの場合+0x30、ピアノ和音の場合+0x59される
		// ただしDXで追加された楽器(0x83以降)はギターコードの場合+0x0A、ピアノ和音の場合+0x14される
		if (type==InstrumentType.GUITAR) {
			if (instNum<0x83) {
				instNum += 0x30;
			} else {
				instNum += 0x0A;
			}
		} else if (type==InstrumentType.PIANO) {
			if (instNum<0x83) {
				instNum += 0x59;
			} else {
				instNum += 0x14;
			}
		}
		binary.set(0x00CA + 16*partNum, instNum);
		// 音符
		List<Note> noteList = p.getNoteList();
		int noteSize = Math.min(noteList.size(), 2048);
		for (int j=0; j<noteSize; j++) {
			int noteNum = noteList.get(j).getNoteNum();
			binary.set(0x0248 + 2048*partNum + j, noteNum);
		}
		// Step
		Set<Integer> stepSet = new HashSet<Integer>();
		for (StepValue<Integer> sv : p.getVolume()) {
			stepSet.add(sv.getStep());
		}
		if (type==InstrumentType.SINGLE) {
			for (StepValue<Integer> sv : p.getBass()) {
				stepSet.add(sv.getStep());
			}
			for (StepValue<Integer> sv : p.getButton()) {
				stepSet.add(sv.getStep());
			}
		} else if (type==InstrumentType.PIANO) {
			for (StepValue<Voicing> sv : p.getVoicing()) {
				stepSet.add(sv.getStep());
			}
		}
		List<Integer> steps = new ArrayList<Integer>(stepSet);
		Collections.sort(steps);
		assert(steps.size()<=32);
		int lastVolume = -1;
		for (int i=0; i<steps.size(); i++) {
			int step = steps.get(i);
			List<Integer> stepBinary = BinaryUtil.to2byteBinary(step);
			binary.set(0x42C8 + 256*partNum + 8*i, stepBinary.get(0));
			binary.set(0x42C9 + 256*partNum + 8*i, stepBinary.get(1));
			// パート:音量
			int volume = p.getVolume(step);
			binary.set(0x42CC + 256*partNum + 8*i, BinaryUtil.VolumeValue[volume]);
			if (volume!=lastVolume) {
				binary.set(0x42CD + 256*partNum + 8*i, 0x00);
				lastVolume = volume;
			} else {
				binary.set(0x42CD + 256*partNum + 8*i, 0x01);
			}
			if (type==InstrumentType.SINGLE) {
				// 単音パート:ベース、ボタン
				binary.set(0x42CA + 256*partNum + 8*i, p.getBass(step));
				int button = p.getButton(step);
				if (button<0) {
					button += 0x100;
				}
				binary.set(0x42CB + 256*partNum + 8*i, button);
			} else if (type==InstrumentType.PIANO) {
				// ピアノパート:ボイシング
				Voicing voicing = p.getVoicing(step);
				binary.set(0x42CA + 256*partNum + 8*i, voicing.getTopNote());
				binary.set(0x42CB + 256*partNum + 8*i,
						(voicing.getNotes()-3)*3 + voicing.getSpread().getValue());
			} else {
				binary.set(0x42CA + 256*partNum + 8*i, 0x00);
				binary.set(0x42CB + 256*partNum + 8*i, 0x00);
			}
			binary.set(0x42CE + 256*partNum + 8*i, 0x00);
			binary.set(0x42CF + 256*partNum + 8*i, 0x00);
		}
	}

}
