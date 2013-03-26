package banbro.model.bdx;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * BDXに関する便利メソッド集
 */
public class BDXUtil {

	/**
	 * 演奏時間を計算する
	 * @param bdx
	 * @return
	 */
	public static double calcPlayTime(BDX bdx) {
		return calcPlayTime(bdx.getTempo(), bdx.getTimeNum());
	}

	/**
	 * 演奏時間を計算する
	 * @param tempo テンポ
	 * @param timeNum 拍数
	 * @return
	 */
	public static double calcPlayTime(List<StepValue<Integer>> tempo, int timeNum) {
		double playTime = 0.0;
		if (tempo==null || tempo.isEmpty()) {
			return playTime;
		}
		int maxStep = BDX.TIME_BASE * timeNum;
		List<StepValue<Integer>> tempo2 = new ArrayList<StepValue<Integer>>();
		for (StepValue<Integer> sv : tempo) {
			if (sv.getValue()<maxStep) {
				tempo2.add(sv);
			}
		}
		tempo2.add(new StepValue<Integer>(maxStep, 0));
		int lastTempo = 1;
		int lastStep = 0;
		for (StepValue<Integer> sv : tempo2) {
			int step = sv.getStep() - lastStep;
			// 1拍 = 12step = 60/tempo sec
			// 1step = (60/tempo)/12 sec = 60/(12*tempo) sec
			playTime += 1.0 * step * 60 / (BDX.TIME_BASE * lastTempo);
			lastStep = sv.getStep();
			lastTempo = sv.getValue();
		}
		return playTime;
	}

	/**
	 * 演奏時間を文字列に変換する
	 * 小数点以下は四捨五入
	 * @param playTime
	 * @return hh:mm:ss
	 * @see #stringToPlayTime(String)
	 */
	public static String playTimeToString(double playTime) {
		StringBuilder sb = new StringBuilder();
		DecimalFormat format = new DecimalFormat("00");
		int time = (int)Math.round(playTime);
		int s = time%60;
		time = (time - s) / 60;
		int m = time%60;
		int h = (time - m) / 60;
		if (h!=0) {
			sb.append(h);
			sb.append(":");
			sb.append(format.format(m));
		} else {
			sb.append(m);
		}
		sb.append(":");
		sb.append(format.format(s));
		return sb.toString();
	}

	/**
	 * 文字列を時間に変換する
	 * @param st hh:mm:ss
	 * @return
	 * @see #playTimeToString(double)
	 */
	public static int stringToPlayTime(String st) {
		int time = 0;
		String[] timeSt = st.split(":");
		int[] times = new int[timeSt.length];
		for (int i=0; i<timeSt.length; i++) {
			try {
				times[i] = Integer.parseInt(timeSt[i]);
			} catch (NumberFormatException e) {
				times[i] = 0;
			}
		}
		if (times.length==2) {
			time += times[0] * 60;
			time += times[1];
		} else if (times.length==3) {
			time += times[0] * 3600;
			time += times[1] * 60;
			time += times[2];
		}
		return time;
	}

	/**
	 * ステップを文字列に変換する
	 * 小節:拍:分割番号/分割数
	 * @param step
	 * @param time 拍子
	 * @return
	 */
	public static String stepToString(int step, int time) {
		StringBuilder sb = new StringBuilder();
		int[] st = new int[4];  // [0]:[1]:[2]/[3]
		st[0] = step / (BDX.TIME_BASE*time);
		step -= st[0] * (BDX.TIME_BASE*time);
		st[1] = step / BDX.TIME_BASE;
		step -= st[1] * BDX.TIME_BASE;
		if (step%(BDX.TIME_BASE/4)==0) {
			st[2] = step / (BDX.TIME_BASE/4);
			st[3] = 4;
		} else if (step%(BDX.TIME_BASE/3)==0) {
			st[2] = step / (BDX.TIME_BASE/3);
			st[3] = 3;
		} else {
			st[2] = step;
			st[3] = BDX.TIME_BASE;
		}
		sb.append(st[0] + 1);
		sb.append(":");
		sb.append(st[1] + 1);
		sb.append(":");
		sb.append(st[2] + 1);
		sb.append("/");
		sb.append(st[3]);
		return sb.toString();
	}

	/**
	 * 各演奏レベルの音符の数を数える
	 * @param p
	 * @return
	 */
	public static EnumMap<PlayLevel, Integer> countNoteNum(Part p) {
		EnumMap<PlayLevel, Integer> map = new EnumMap<PlayLevel, Integer>(PlayLevel.class);
		for (PlayLevel level : PlayLevel.values()) {
			map.put(level, 0);
		}
		switch (p.getType()) {
		case SINGLE:
		case PIANO:
			countSingleNoteNum(p, map);
			break;
		case DRUMS:
			countDrumNoteNum(p, map);
			break;
		case GUITAR:
			countGuitarNoteNum(p, map);
			break;
		case NONE:
		default:
			break;
		}
		return map;
	}

	private static void countSingleNoteNum(Part p, EnumMap<PlayLevel, Integer> map) {
		int combo = 0;
		List<Note> notes = p.getNoteList();
		for (int i=0; i<notes.size(); i++) {
			SingleNote n = (SingleNote) notes.get(i);
			if (n.isNote()) {
				combo++;
			}
		}
		map.put(PlayLevel.BEGINNER, combo);
		map.put(PlayLevel.AMATEUR, combo);
		map.put(PlayLevel.PRO, combo);
		map.put(PlayLevel.MASTER, combo);
	}

	private static void countDrumNoteNum(Part p, EnumMap<PlayLevel, Integer> map) {
		int combo1 = 0;
		int combo2 = 0;
		List<Note> notes = p.getNoteList();
		int oneTimeNoteNum = 4;
		for (int i=oneTimeNoteNum-1; i<notes.size(); i+=oneTimeNoteNum) {
			DrumNote[] drumNotes = new DrumNote[oneTimeNoteNum];
			int[] upperNotes = new int[oneTimeNoteNum];
			int[] lowerNotes = new int[oneTimeNoteNum];
			for (int j=0; j<oneTimeNoteNum; j++) {
				drumNotes[j] = (DrumNote) notes.get(i-(oneTimeNoteNum-1)+j);
				upperNotes[j] = drumNotes[j].getUpperNoteNum();
				lowerNotes[j] = drumNotes[j].getLowerNoteNum();
				if (DrumNote.isNote(upperNotes[j])) {
					combo2++;
				}
				if (DrumNote.isNote(lowerNotes[j])) {
					combo1++;
					combo2++;
				}
			}
			boolean isUpperGroup = upperNotes[0]==DrumNote.GROUP;
			boolean isLowerGroup = lowerNotes[0]==DrumNote.GROUP;
			for (int j=0; j<oneTimeNoteNum; j++) {
				if (DrumNote.isNote(upperNotes[j])) {
					int upperTick;
					if (isUpperGroup) {
						upperTick = (j-1)*4;
					} else {
						upperTick = j*3;
					}
					int index = -1;
					switch (upperTick) {
					case 3:
						if (isLowerGroup) {
							index = 1;  // step=0
						} else {
							index = 1;  // step=3
						}
						break;
					case 6:
						if (isLowerGroup) {
							index = 2;  // step=4
						} else {
							index = 2;  // step=6
						}
						break;
					case 9:
						if (isLowerGroup) {
							index = 3;  // step=8
						} else {
							index = 3;  // step=9
						}
						break;
					case 0:
						if (isLowerGroup) {
							index = 1;  // step=0
						} else {
							index = 0;  // step=0
						}
						break;
					case 4:
						if (isLowerGroup) {
							index = 2;  // step=4
						} else {
							index = 1;  // step=3
						}
						break;
					case 8:
						if (isLowerGroup) {
							index = 3;  // step=8
						} else {
							index = 2;  // step=6
						}
						break;
					default:
						break;
					}
					if (index<0 || !DrumNote.isNote(lowerNotes[index])) {
						combo1++;
					}
				}
			}
		}
		map.put(PlayLevel.BEGINNER, combo1);
		map.put(PlayLevel.AMATEUR, combo1);
		map.put(PlayLevel.PRO, combo1);
		map.put(PlayLevel.MASTER, combo2);
	}

	private static void countGuitarNoteNum(Part p, EnumMap<PlayLevel, Integer> map) {
		int combo1 = 0;
		int combo2 = 0;
		int lastButton = -1;
		List<Note> notes = p.getNoteList();
		for (int i=0; i<notes.size(); i++) {
			GuitarNote n = (GuitarNote) notes.get(i);
			if (n.isNote()) {
				combo2++;
				int buttonNum = n.getButtonNum();
				if (buttonNum!=lastButton) {
					lastButton = buttonNum;
					combo1++;
				}
			}
		}
		map.put(PlayLevel.BEGINNER, combo1);
		map.put(PlayLevel.AMATEUR, combo2);
		map.put(PlayLevel.PRO, combo2);
		map.put(PlayLevel.MASTER, combo2);
	}

	/**
	 * @param <V>
	 * @param list stepでソートされたリスト
	 * @param step
	 * @param last true:同じstepがある場合、最後の要素番号を返す。
	 * @return
	 */
	public static <V> int searchIndexWithStep(List<StepValue<V>> list, int step, boolean last) {
		if (list==null || list.isEmpty()) {
			return -1;
		}
		int left = 0;
		int right = list.size()-1;
		int index = 0;
		while (true) {
			index = (left+right)/2;
			int iStep = list.get(index).getStep();
			if (iStep==step) {
				if (last) {
					for (int i=index+1; i<list.size(); i++) {
						if (list.get(i).getStep()==step) {
							index = i;
						} else {
							break;
						}
					}
				} else {
					for (int i=index-1; i>=0; i--) {
						if (list.get(i).getStep()==step) {
							index = i;
						} else {
							break;
						}
					}
				}
				break;
			} else if (iStep<step) {
				left = index + 1;
			} else {
				right = index - 1;
			}
			if (left>right) {
				index = right;
				break;
			}
		}
		return index;
	}

	/**
	 * @param <V>
	 * @param list stepでソートされたリスト
	 * @param step
	 * @param last true:同じstepがある場合、最後の要素を返す。
	 * @return
	 */
	public static <V> V getValueWithStep(List<StepValue<V>> list, int step, boolean last) {
		int index = searchIndexWithStep(list, step, last);
		if (index<0) {
			return null;
		}
		return list.get(index).getValue();
	}


	/**
	 * @param c
	 * @return 歌詞表示可能な文字ならtrue
	 */
	public static boolean isLyricChar(char c) {
		return (c=='\u0000' || c=='\n' || c==' ' || c=='　') == false;
	}

	/**
	 * @param st
	 * @return 歌詞表示可能な文字数
	 */
	public static int getLyricLength(String st) {
		if (st==null) {
			return 0;
		}
		int length = 0;
		for (char c : st.toCharArray()) {
			if  (isLyricChar(c)) {
				length++;
			}
		}
		return length;
	}

	/**
	 * 歌詞表示用データを作成する
	 * バージョン2以降のみ
	 * @param bdx
	 */
	public static void createLyricLines(BDX bdx) {
		if (bdx.isLyrics()==false) {
			return;
		}
		if (bdx.getMejVer()<=1) {
			return;
		}
		List<StepValue<LyricLine>> lines = new ArrayList<StepValue<LyricLine>>();
		LyricLine line = new LyricLine();
		List<StepValue<String>> lyricTiming = bdx.getLyricTiming();
		for (int i=0; i<lyricTiming.size(); i++) {
			StepValue<String> sv = lyricTiming.get(i);
			int step = sv.getStep();
			String value = sv.getValue();
			List<String> strings = new ArrayList<String>();
			String st = "";
			boolean isLF = false;
			for (char c : value.toCharArray()) {
				isLF = (c==BinaryUtil.CHAR_NULL || c=='\n');
				if (isLF) {
					strings.add(st);
					st = "";
				} else {
					st += c;
				}
			}
			if (st.length()!=0) {
				strings.add(st);
			}
			int n = strings.size();
			if (n==0) {
				continue;
			}
			if (n>=2) {
				for (int j=0; j<strings.size()-1; j++) {
					line.addNote(step, new LyricNote(strings.get(j), 0));
					lines.add(new StepValue<LyricLine>(line.getStep(), line));
					line = new LyricLine();
				}
			}
			String lastSt = strings.get(strings.size()-1);
			LyricNote note = new LyricNote(lastSt);
			if (i+1<lyricTiming.size()) {
				note.setLength(Math.min(BDX.TIME_BASE, lyricTiming.get(i+1).getStep()-step));
			}
			line.addNote(step, note);
			if (isLF) {
				lines.add(new StepValue<LyricLine>(line.getStep(), line));
				line = new LyricLine();
			}
		}
		if (line.getStep()>=0) {
			lines.add(new StepValue<LyricLine>(line.getStep(), line));
		}
		for (int i=0; i<lines.size(); i++) {
			LyricLine line1 = lines.get(i).getValue();
			if (line1.getEndNoteLength()==0 && line1.getLyricLength()!=0) {
				int newLen = BDX.TIME_BASE;
				for (int j=i+1; j<lines.size(); j++) {
					LyricLine line2 = lines.get(j).getValue();
					if (line2.getLyricLength()!=0) {
						newLen = Math.min(newLen, line2.getStep()-line1.getEndNoteStep());
						break;
					}
				}
				line1.setEndNoteLength(newLen);
			}
		}
		bdx.setLyricLines(lines);
	}

	/**
	 * 現在のステップの歌詞表示を取得する
	 * バージョン2以降のみ
	 * @param bdx
	 * @param step
	 * @return
	 * @see #createLyricLines(BDX)
	 */
	public static LyricLine getLyricLine(BDX bdx, int step) {
		if (bdx.isLyrics()==false || step<0) {
			return null;
		}
		List<StepValue<LyricLine>> lines = bdx.getLyricLines();
		if (lines==null || lines.isEmpty()) {
			return null;
		}
		if (bdx.getMejVer()<=1) {
			return null;
		} else if (bdx.getMejVer()>=2) {
			final int LEN1 = BDX.TIME_BASE * 4;
			final int LEN2 = BDX.TIME_BASE * 8;
			int index = searchIndexWithStep(lines, step, false);
			if (index<0) {  // 曲の始まり
				StepValue<LyricLine> sv = lines.get(0);
				if (sv.getStep()-step<=LEN1) {
					return sv.getValue();
				} else {
					return null;
				}
			}
			for (; index>=0; index--) {  // 空行ではない行まで戻る
				StepValue<LyricLine> sv = lines.get(index);
				if (sv.getValue().getLyricLength()!=0) {
					break;
				}
			}
			if (index<0) {  // 空行で始まっている
				for (int i=0; i<lines.size(); i++) {
					StepValue<LyricLine> sv2 = lines.get(i);
					LyricLine line2 = sv2.getValue();
					if (line2.getLyricLength()!=0) {
						if (sv2.getStep()-step<=LEN1) {
							return line2;
						} else {
							return null;
						}
					}
				}
				return null;
			}
			StepValue<LyricLine> sv1 = lines.get(index);
			LyricLine line1 = sv1.getValue();
			LyricLine line2 = null;
			for (int i=index+1; i<lines.size(); i++) {
				StepValue<LyricLine> sv2 = lines.get(i);
				LyricLine line = sv2.getValue();
				if (line.getLyricLength()!=0) {
					line2 = line;
					break;
				}
			}
			int endStep = line1.getEndNoteStep();
			if (step-endStep<LEN2) {
				if (line2!=null) {
					int nextStep = line2.getStep();
					if (((nextStep-endStep)*3/4+endStep)<step && nextStep-step<=LEN1) {
						return line2;
					}
				}
				return line1;
			} else {
				if (line2!=null) {
					int nextStep = line2.getStep();
					if (nextStep-step<=LEN1) {
						return line2;
					}
				}
				return null;
			}
		}
		return null;
	}

	/**
	 * @param id
	 * @return 自作曲のidならtrue
	 * @see BDX#getSerialId()
	 */
	public static boolean isMyBDX(long id) {
		return (id<=0 || id>=3000000000l);
	}

	/**
	 * デフォルトの音色調整にする
	 * @param p
	 */
	public static void setDefaultTone(Part p) {
		// TODO 和音パートに対応
		switch (p.getType()) {
		case SINGLE:
			setPartTone(p, BinaryUtil.getDefaultSinglePartTone(p.getInstrument()));
			break;
		case GUITAR:
		case PIANO:
		default:
			break;
		}
	}

	private static void setPartTone(Part p, int[] tone) {
		p.setToneAttack(BinaryUtil.byteToAttack(tone[0]));
		p.setToneDecay(BinaryUtil.byteToDecay(tone[1]));
		p.setToneSustain(BinaryUtil.byteToSustain(tone[2]));
		p.setToneRelease(BinaryUtil.byteToRelease(tone[3]));
		p.setVibratoShape(VibratoShape.valueOf(tone[4]));
		p.setVibratoHold(BinaryUtil.byteToHold(tone[5]));
		p.setVibratoDelay(BinaryUtil.byteToDelay(tone[6]));
		p.setVibratoSpeed(BinaryUtil.byteToSpeed(tone[7]));
		p.setVibratoDepth(BinaryUtil.byteToDepth(tone[8]));
		p.setToneEffectType(EffectType.valueOf(tone[9]));
		p.setToneEffectValue(tone[10]);
	}
	
	/**
	 * 単音パートの音階をずらす
	 * 制限を超える場合は何もしない
	 * @param p
	 * @param shift
	 * @return 音階をずらしたならtrue
	 */
	public static boolean shiftNotes(Part p, int shift) {
		if (p.getType()!=InstrumentType.SINGLE) {
			return false;
		}
		List<SingleNote> newNotes = new ArrayList<SingleNote>();
		for (Note note : p.getNoteList()) {
			SingleNote sNote = (SingleNote) note;
			if (sNote.isRest() || sNote.isGroup()) {
				newNotes.add(sNote);
				continue;
			}
			int noteNum = sNote.getSingleNoteNum() + shift;
			if (noteNum<=0 || 127<noteNum) {
				return false;
			}
			newNotes.add(SingleNote.getInstance(noteNum, sNote.isExtend()));
		}
		p.clearNotes();
		for (SingleNote note : newNotes) {
			p.addNote(note);
		}
		return true;
	}

}
