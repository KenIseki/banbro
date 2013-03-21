package banbro.model.score;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import banbro.model.Accidental;
import banbro.model.Clef;
import banbro.model.Pitch;
import banbro.model.bdx.BDX;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.Chord;
import banbro.model.bdx.ChordNote;
import banbro.model.bdx.DrumNote;
import banbro.model.bdx.GuitarNote;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.NonChord;
import banbro.model.bdx.Note;
import banbro.model.bdx.NullChord;
import banbro.model.bdx.Part;
import banbro.model.bdx.SingleNote;
import banbro.model.score.Score.BarType;
import banbro.model.score.Score.Beam;
import banbro.model.score.Score.NoteType;
import banbro.model.score.Score.Stem;
import banbro.model.score.Score.Tuplet;
import banbro.model.score.ScorePercNote.PercLine;

/**
 * 楽譜に関する便利メソッド集
 */
public class ScoreUtil {

	/**
	 * 五線譜に変換する
	 * 1要素が1小節
	 * @param part 単音、ギター、ピアノのいずれか
	 * @param bdx
	 * @param melody メロディパートならtrue。単音パートのみ。
	 * @param outputChord コード進行を出力するならtrue。メロディパートのみ
	 * @return
	 */
	public static List<List<Score>> singlePartToScore(Part part, BDX bdx, boolean melody, boolean outputChord) {
		InstrumentType type = part.getType();
		EnumSet<InstrumentType> typeSet = EnumSet.of(InstrumentType.SINGLE, InstrumentType.GUITAR, InstrumentType.PIANO); 
		if (typeSet.contains(type)) {
			throw new IllegalArgumentException(type.getTypeName() + " は五線譜に変換できません");
		}
		if (melody) {
			if (type!=InstrumentType.SINGLE) {
				throw new IllegalArgumentException(type.getTypeName() + " はメロディパートにはできません");
			}
			outputChord &= !bdx.getChordTiming().isEmpty();
		}
		List<Note> noteList = part.getNoteList();
		int time = bdx.getTime();
		int bars = Math.min(bdx.getBars(), noteList.size()/4/time);
		int lastKey = Score.KEY_NONE;
		Clef lastClef = Clef.NONE;
		ScoreSingleNote lastScoreNote = null;
		Chord lastChord = NullChord.getInstance();  //コード音符用
		Chord lastMeloChord = NullChord.getInstance();  //コード配置用
		Chord nowMeloChord = NullChord.getInstance();
		int nowCounter = 0;
		List<List<Score>> partScore = new ArrayList<List<Score>>();
		for (int measure=0; measure<bars; measure++) {  // 小節ループ
			Stack<Score> stack = new Stack<Score>();
			if (measure!=0) {
				ScoreBar bar = new ScoreBar();
				bar.setMeasureNum(measure);
				stack.push(bar);
			}
			int localCounter = 0;
			nowCounter = measure*time*BDX.TIME_BASE;
			for (int t=0; t<time; t++) {  // 拍ループ
				int nowKey = Score.KEY_NONE;
				Clef nowClef = Clef.NONE;
				if (type==InstrumentType.SINGLE) {  // 単音のみ、音部記号と調の変更チェック
					ScoreAttributes attributes = new ScoreAttributes();
					boolean clefChange = false;
					boolean keyChange = false;
					nowClef = part.getClef(nowCounter+localCounter);
					if (nowClef!=lastClef) {
						attributes.setClef(nowClef);
						if ((lastClef==Clef.G2 && nowClef==Clef.G2_8va) ||
								(lastClef==Clef.G2_8va && nowClef==Clef.G2)) {
							attributes.setOutputClef(true);
						}
						clefChange = true;
						lastClef = nowClef;
					}
					nowKey = bdx.getKey(nowCounter+localCounter);
					if (nowKey!=lastKey) {
						attributes.setKey(nowKey);
						keyChange = true;
						lastKey = nowKey;
					}
					if (keyChange || clefChange) {
						attributes.setCounter(localCounter);
						// 直前がBarなら、縦線は出力されるので削除する
						if (localCounter==0 && keyChange) {
							if (stack.isEmpty()==false) {
								stack.pop();
							}
						}
						stack.push(attributes);
					}
				}
				boolean renpu = false;
				int length = BDX.TIME_BASE/4;
				for (int i=0; i<4; i++) {  // 拍内の音符ループ
					SingleNote note = (SingleNote) noteList.get((measure*time + t)*4 + i);
					if (note.isGroup()) {
						renpu = true;
						length = BDX.TIME_BASE/3;
						continue;
					}
					if (melody && outputChord) {  // コード進行を表示する？
						nowMeloChord = BDXUtil.getValueWithStep(bdx.getChordTiming(), nowCounter+localCounter, true);
					}
					if (note.isNote()) {  // 発音開始
						ScorePitchNote pNote;
						if (note instanceof ChordNote) {  // コードのとき
							ScoreChordNote cNote = new ScoreChordNote();
							if (note instanceof GuitarNote) {  // ギターストローク
								cNote.setStroke(((GuitarNote)note).getStroke());
							}
							// コード名を歌詞として扱う
							List<Chord> chordSet = part.getChordSet(nowCounter+localCounter);
							Chord nowChord = NonChord.getInstance();
							int noteNum = ((ChordNote)note).getButtonNum() - 1;
							if (noteNum<chordSet.size()) {
								nowChord = chordSet.get(noteNum);
							}
							if (!lastChord.equals(nowChord)) {
								cNote.setLyric(nowChord.getChordName());
								lastChord = nowChord;
							}
							pNote = cNote;
						} else {  // 単音
							pNote = createPitchNote(note.getNoteNum(), nowKey, nowClef);
						}
						pNote.setDuration(length);
						pNote.setLocalCounter(localCounter);
						stack.push(pNote);
						lastScoreNote = pNote;
					} else if (note.isExtend()) {  // 発音継続
						assert(lastScoreNote!=null);
						assert(lastScoreNote instanceof ScorePitchNote);
						ScorePitchNote lastPNote = (ScorePitchNote) lastScoreNote;
						if ((renpu==false && i==0) || (renpu==true && i==1)  // 拍が変わる
								|| !lastMeloChord.equals(nowMeloChord)) { // コードが変わる
							lastPNote.setTieStart(true);
							ScorePitchNote pNote;
							if (note instanceof ChordNote) {  // コードのとき
								pNote = new ScoreChordNote();
							} else {
								pNote = createPitchNote(lastPNote.getNoteNum(), nowKey, nowClef);
							}
							pNote.setDuration(length);
							pNote.setLocalCounter(localCounter);
							pNote.setTieStop(true);
							stack.push(pNote);
							lastScoreNote = pNote;
						} else {
							lastPNote.setDuration(lastPNote.getDuration()+length);
						}
					} else if (note.isRest()) {  // 休符
						if (lastScoreNote!=null && (lastScoreNote instanceof ScoreRestNote)) {
							ScoreRestNote lastRNote = (ScoreRestNote) lastScoreNote;
							if ((renpu==false && i==0) || (renpu==true && i==1)  // 拍が変わる
									|| !lastMeloChord.equals(nowMeloChord)) { // コードが変わる
								ScoreRestNote rNote = createRestNote(localCounter, length, nowClef);
								rNote.setIsChord(note instanceof ChordNote);
								stack.push(rNote);
								lastScoreNote = rNote;
							} else {
								lastRNote.setDuration(lastRNote.getDuration()+length);
							}
						} else {
							ScoreRestNote rNote = createRestNote(localCounter, length, nowClef);
							rNote.setIsChord(note instanceof ChordNote);
							stack.push(rNote);
							lastScoreNote = rNote;
						}
					}
					if (!lastMeloChord.equals(nowMeloChord)) {  // コード進行
						lastScoreNote.setChord(nowMeloChord.getChordName());
						lastMeloChord = nowMeloChord;
					}

					localCounter += length;
				}  // 拍内の音符ループ
			}  // 拍ループ
			List<Score> scoreList = new ArrayList<Score>(stack);
			scoreList = updateScore(scoreList, time);
			for (int i=scoreList.size()-1; i>=0; i--) {
				Score score = scoreList.get(i);
				if (score instanceof ScoreSingleNote) {
					lastScoreNote = (ScoreSingleNote) score;
					break;
				} else if (score instanceof ScoreBeamNote) {
					ScoreBeamNote bNote = (ScoreBeamNote) score;
					lastScoreNote = bNote.getLastNote();
					break;
				}
			}
			partScore.add(scoreList);
		}  // 小節ループ
		// 終止線の追加
		List<Score> lastScoreList = partScore.get(partScore.size()-1);
		lastScoreList.add(new ScoreBar(BarType.END));
		// 棒の向き、歌詞表示位置
		int lyricLine = -7;
		for (int measure=0; measure<partScore.size(); measure++) {
			List<Score> scoreList = partScore.get(measure);
			for (int i=0; i<scoreList.size(); i++) {
				Score sc = scoreList.get(i);
				if (sc instanceof IStemNote) {
					IStemNote sNote = (IStemNote) sc;
					sNote.updateStem();
				}
				if (melody && (sc instanceof ScoreSingleNote)) {
					lyricLine = Math.min(lyricLine, ((ScoreSingleNote) sc).getLyricLine());
				}
			}
		}
		if (type==InstrumentType.GUITAR || type==InstrumentType.PIANO) {
			return partScore;
		}
		// 以降は単音のみ
		// 歌詞と臨時記号
		Clef nowClef = Clef.NONE;
		int nowKey = Score.KEY_NONE;
		Map<Integer, Accidental> acMap = new HashMap<Integer, Accidental>();
		nowCounter = 0;
		for (int measure=0; measure<partScore.size(); measure++) {
			List<Score> scoreList = partScore.get(measure);
			nowCounter = measure*time*BDX.TIME_BASE;
			acMap.clear();
			for (int i=0; i<scoreList.size(); i++) {
				Score sc = scoreList.get(i);
				if (sc instanceof ScoreSingleNote) {
					ScoreSingleNote sNote = (ScoreSingleNote) sc;
					if (melody) {
						int begin = nowCounter + sNote.getLocalCounter();
						int end = begin + sNote.getDuration();
						sNote.setLyric(bdx.getLyric(begin, end));
						sNote.setLyricLine(lyricLine);
					}
					if (sNote instanceof ScorePitchNote) {
						updateAccidental(nowKey, acMap, (ScorePitchNote)sNote);
					}
				} else if (sc instanceof ScoreBeamNote) {
					ScoreBeamNote bNote = (ScoreBeamNote) sc;
					for (ScoreSingleNote sNote : bNote.getNoteList()) {
						if (melody) {
							int begin = nowCounter + sNote.getLocalCounter();
							int end = begin + sNote.getDuration();
							sNote.setLyric(bdx.getLyric(begin, end));
							sNote.setLyricLine(lyricLine);
						}
						if (sNote instanceof ScorePitchNote) {
							updateAccidental(nowKey, acMap, (ScorePitchNote)sNote);
						}
					}
				} else if (sc instanceof ScoreAttributes) {
					ScoreAttributes at = (ScoreAttributes) sc;
					Clef clef = at.getClef();
					int key = at.getKey();
					if (clef!=Clef.NONE && clef!=nowClef) {
						nowClef = clef;
						acMap.clear();
					}
					if (key!=Score.KEY_NONE && key!=nowKey) {
						nowKey = key;
						acMap.clear();
					}
				}
			}
		}
		return partScore;
	}

	/**
	 * ドラム譜に変換する
	 * 1要素が1小節
	 * @param part
	 * @param bdx
	 * @return
	 */
	public static List<List<Score>> drumPartToScore(Part part, BDX bdx) {
		InstrumentType type = part.getType();
		if (type!=InstrumentType.DRUMS) {
			throw new IllegalArgumentException(type.getTypeName() + " はドラム譜に変換できません");
		}
		List<Note> noteList = part.getNoteList();
		int time = bdx.getTime();
		int bars = Math.min(bdx.getBars(), noteList.size()/4/time);
		List<List<Score>> partScore = new ArrayList<List<Score>>();
		for (int measure=0; measure<bars; measure++) {  // 小節ループ
			List<Score> scoreList = new ArrayList<Score>();
			if (measure!=0) {
				ScoreBar bar = new ScoreBar();
				bar.setMeasureNum(measure);
				scoreList.add(bar);
			}
			int localCounterU = 0;
			int localCounterL = 0;
			List<Score> scoreU = new ArrayList<Score>();
			List<Score> scoreL = new ArrayList<Score>();
			for (int t=0; t<time; t++) {  // 拍ループ
				int offset = measure*time + t;
				int lengthU = BDX.TIME_BASE/4;
				int lengthL = BDX.TIME_BASE/4;
				for (int i=0; i<4; i++) {  // 拍内の音符ループ
					DrumNote note = (DrumNote) noteList.get(offset*4+i);
					if (note.getUpperNoteNum()==DrumNote.GROUP) {
						lengthU = BDX.TIME_BASE/3;
					} else {
						ScorePercNote pNote = new ScorePercNote(PercLine.UPPER);
						pNote.setNoteNum(note.getUpperNoteNum());
						pNote.setLocalCounter(localCounterU);
						pNote.setDuration(lengthU);
						scoreU.add(pNote);
						localCounterU += lengthU;
					}
					if (note.getLowerNoteNum()==DrumNote.GROUP) {
						lengthL = BDX.TIME_BASE/3;
					} else {
						ScorePercNote pNote = new ScorePercNote(PercLine.LOWER);
						pNote.setNoteNum(note.getLowerNoteNum());
						pNote.setLocalCounter(localCounterL);
						pNote.setDuration(lengthL);
						scoreL.add(pNote);
						localCounterL += lengthL;
					}
				}
			}
			scoreList.addAll(scoreU);
			scoreList.add(new ScoreBackMeasureBegin(time));
			scoreList.addAll(scoreL);
			updateType(scoreList);
			partScore.add(scoreList);
		}
		// 終止線の追加
		List<Score> lastScoreList = partScore.get(partScore.size()-1);
		lastScoreList.add(new ScoreBar(BarType.END));
		return partScore;
	}
	
	private static void updateAccidental(int nowKey,
			Map<Integer, Accidental> acMap, ScorePitchNote pNote) {
		int scoreLine = calcScoreLine(pNote);
		Clef clef = pNote.getClef();
		Accidental ac = Accidental.NONE;
		if (clef==Clef.G2 || clef==Clef.G2_8va) {
			scoreLine -= 1;
		} else if (clef==Clef.F4) {
			scoreLine += 1;
		}
		while (scoreLine<0) {
			scoreLine += 7;
		}
		scoreLine %= 7;
		switch (scoreLine) {
		case 0:
			if (nowKey<=-6) {
				ac = Accidental.FLAT;
			} else if (nowKey>=2) {
				ac = Accidental.SHARP;
			}
			break;
		case 1:
			if (nowKey<=-4) {
				ac = Accidental.FLAT;
			} else if (nowKey>=4) {
				ac = Accidental.SHARP;
			}
			break;
		case 2:
			if (nowKey<=-2) {
				ac = Accidental.FLAT;
			} else if (nowKey>=6) {
				ac = Accidental.SHARP;
			}
			break;
		case 3:
			if (nowKey<=-7) {
				ac = Accidental.FLAT;
			} else if (nowKey>=1) {
				ac = Accidental.SHARP;
			}
			break;
		case 4:
			if (nowKey<=-5) {
				ac = Accidental.FLAT;
			} else if (nowKey>=3) {
				ac = Accidental.SHARP;
			}
			break;
		case 5:
			if (nowKey<=-3) {
				ac = Accidental.FLAT;
			} else if (nowKey>=5) {
				ac = Accidental.SHARP;
			}
			break;
		case 6:
			if (nowKey<=-1) {
				ac = Accidental.FLAT;
			} else if (nowKey>=7) {
				ac = Accidental.SHARP;
			}
			break;
		default:
			break;
		}
		scoreLine = calcScoreLine(pNote);
		Accidental ac2 = acMap.get(scoreLine);
		if (ac2==null) {
			ac2 = Accidental.NONE;
		}
		if (ac==Accidental.SHARP || ac==Accidental.FLAT) {
			if (ac2==Accidental.NATURAL) {
				ac = Accidental.NATURAL;
			}
		} else {
			if (ac2!=Accidental.NONE) {
				ac = ac2;
			}
		}
		switch (pNote.getAlter()) {
		case 1:
			if (ac==Accidental.SHARP) {
				pNote.setAccidental(Accidental.NONE);
			} else {
				pNote.setAccidental(Accidental.SHARP);
				acMap.put(scoreLine, Accidental.SHARP);
			}
			break;
		case -1:
			if (ac==Accidental.FLAT) {
				pNote.setAccidental(Accidental.NONE);
			} else {
				pNote.setAccidental(Accidental.FLAT);
				acMap.put(scoreLine, Accidental.FLAT);
			}
			break;
		case 0:
		default:
			if (ac==Accidental.NONE || ac==Accidental.NATURAL) {
				pNote.setAccidental(Accidental.NONE);
			} else {
				pNote.setAccidental(Accidental.NATURAL);
				acMap.put(scoreLine, Accidental.NATURAL);
			}
			break;
		}
	}


	/**
	 * @param localCounter 小節内の位置
	 * @param length 長さ
	 * @param clef 音部記号
	 * @return 休符
	 */
	public static ScoreRestNote createRestNote(int localCounter, int length, Clef clef) {
		ScoreRestNote rNote = new ScoreRestNote();
		rNote.setDuration(length);
		rNote.setClef(clef);
		rNote.setLocalCounter(localCounter);
		return rNote;
	}


	/**
	 * @param num ノート番号
	 * @param key 調
	 * @param clef 音部記号
	 * @return 音符
	 */
	public static ScorePitchNote createPitchNote(int num, int key, Clef clef) {
		ScorePitchNote pNote = new ScorePitchNote();
		int oct = num / 12;
		int pNum = num % 12;
		Pitch step = null;
		int alter = 0;
		Accidental accidental = Accidental.NONE;
		switch (pNum) {
		case 0:  // c, b+
			step = Pitch.C;
			if (key>=2) {
				if (key>=7) {  // b+_
					oct--;
					step = Pitch.B;
					alter = 1;
				} else {  // c+*
					accidental = Accidental.NATURAL;
				}
			} else if (key<=-6) {  // c-*
				accidental = Accidental.NATURAL;
			}
			// c__
			break;
		case 1:  // c+, d-
			if (key>=2) {  // c+_
				step = Pitch.C;
				alter = 1;
			} else if (key<=-4) {  // d-_
				step = Pitch.D;
				alter = -1;
			} else {  // c_+
				step = Pitch.C;
				alter = 1;
				accidental = Accidental.SHARP;
			}
			break;
		case 2:  // d
			step = Pitch.D;
			if (key>=4) {  // d+*
				accidental = Accidental.NATURAL;
			} else if (key<=-4) {  // d-*
				accidental = Accidental.NATURAL;
			}
			// d__
			break;
		case 3:  // d+, e-
			if (key>=4) {  // d+_
				step = Pitch.D;
				alter = 1;
			} else if (key<=-2) {  // e-_
				step = Pitch.E;
				alter = -1;
			} else {  // d_+
				step = Pitch.D;
				alter = 1;
				accidental = Accidental.SHARP;
			}
			break;
		case 4:  // e, f-
			step = Pitch.E;
			if (key>=6) {  // e+*
				accidental = Accidental.NATURAL;
			} else if (key<=-2) {
				if (key<=-7) {  // f-_
					step = Pitch.F;
					alter = -1;
				} else {  // e-*
					accidental = Accidental.NATURAL;
				}
			}
			// e__
			break;
		case 5:  // f, e+
			step = Pitch.F;
			if (key>=1) {
				if (key>=6) {  // e+_
					step = Pitch.E;
					alter = 1;
				} else {  // f+*
					accidental = Accidental.NATURAL;
				}
			} else if (key<=-7) {  // f-*
				accidental = Accidental.NATURAL;
			}
			// f__
			break;
		case 6:  // f+, g-
			if (key>=1) {  // f+_
				step = Pitch.F;
				alter = 1;
			} else if (key<=-5) {  // g-_
				step = Pitch.G;
				alter = -1;
			} else {  // f_+
				step = Pitch.F;
				alter = 1;
				accidental = Accidental.SHARP;
			}
			break;
		case 7:  // g
			step = Pitch.G;
			if (key>=3) {  // g+*
				accidental = Accidental.NATURAL;
			} else if (key<=-5) {  // g-*
				accidental = Accidental.NATURAL;
			}
			// g__
			break;
		case 8:  // g+, a-
			if (key>=3) {  // g+_
				step = Pitch.G;
				alter = 1;
			} else if (key<=-3) {  // a-_
				step = Pitch.A;
				alter = -1;
			} else {  // g_+
				step = Pitch.G;
				alter = 1;
				accidental = Accidental.SHARP;
			}
			break;
		case 9:  // a
			step = Pitch.A;
			if (key>=5) {  // a+*
				accidental = Accidental.NATURAL;
			} else if (key<=-3) {  // a-*
				accidental = Accidental.NATURAL;
			}
			// a__
			break;
		case 10:  // a+, b-
			if (key>=5) {  // a+_
				step = Pitch.A;
				alter = 1;
			} else if (key<=-1) {  // b-_
				step = Pitch.B;
				alter = -1;
			} else {  // a_+
				step = Pitch.A;
				alter = 1;
				accidental = Accidental.SHARP;
			}
			break;
		case 11:  // b, c-
			step = Pitch.B;
			if (key>=7) {  // b+*
				accidental = Accidental.NATURAL;
			} else if (key<=-1) {
				if (key<=-6) {  // c-_
					oct++;
					step = Pitch.C;
					alter = -1;
				} else {  // b-*
					accidental = Accidental.NATURAL;
				}
			}
			// b__
			break;
		default:
			break;
		}
		pNote.setOctave(oct);
		pNote.setStep(step);
		pNote.setAlter(alter);
		pNote.setAccidental(accidental);
		pNote.setClef(clef);
		return pNote;
	}


	/**
	 * 五線の中央が0。MusiXTeXのLineにするには+4する。
	 * @param pitchNote
	 * @return
	 */
	public static int calcUpperScoreLine(ScoreSingleNote note) {
		if (note instanceof ScorePitchNote) {
			ScorePitchNote pitchNote = (ScorePitchNote) note;
			if (pitchNote.getStem()==Stem.UP) {
				int line = 0;
				if (pitchNote.getBeam().isEmpty() || pitchNote.getBeam().get(0)==Beam.NONE) {
					line = calcPitchScoreLine((ScorePitchNote) pitchNote) + 7;
				} else {
					line = pitchNote.getBeamLine()+7;
					if (pitchNote.getBeam().size()==2) {
						line++;
					}
				}
				if (pitchNote.getTuplet()!=Tuplet.NONE) {
					line = Math.max(line, 4);
					line += 3;
				}
				return line;
			} else if (pitchNote.getStem()==Stem.DOWN) {
				int line = calcPitchScoreLine(pitchNote);
				if (pitchNote.getTuplet()!=Tuplet.NONE) {
					line = Math.max(line, 4);
					line += 4;
				}
				return line;
			} else {
				return calcPitchScoreLine(pitchNote);
			}
		}
		return 0;
	}

	/**
	 * @param note
	 * @return 五線の中央が0。MusiXTeXのLineにするには+4する。
	 */
	public static int calcScoreLine(ScoreSingleNote note) {
		if (note instanceof ScorePitchNote) {
			return calcPitchScoreLine((ScorePitchNote) note);
		}
		return 0;
	}

	/**
	 * @param note
	 * @return 五線の中央が0。MusiXTeXのLineにするには+4する。
	 */
	public static int calcPitchScoreLine(ScorePitchNote note) {
		Clef clef = note.getClef();
		int line = 7*note.getOctave() + note.getStep().getValue();
		switch (clef) {
		case G2_8va:  // o6b
			line -= 7*6 + Pitch.B.getValue();
			break;
		case G2:  // o5b
			line -= 7*5 + Pitch.B.getValue();
			break;
		case F4:  // o4d
			line -= 7*4 + Pitch.D.getValue();
			break;
		default:
			line -= 7*5 + Pitch.B.getValue();
			break;
		}
		return line;
	}

	public static Stem calcStem(ScorePitchNote note) {
		if (note.getType()==Score.NoteType.WHOLE) {
			return Stem.NONE;
		}
		int line = calcScoreLine(note);
		// 中央の場合は下向き
		if (line<0) {
			return Stem.UP;
		} else {
			return Stem.DOWN;
		}
	}

	/**
	 * 長さ、連衡、三連符の更新。
	 * @param list
	 * @param time
	 * @return
	 */
	private static List<Score> updateScore(List<Score> list, int time) {
		List<Score> newList = updateDuration(list, time);
		updateType(newList);
		newList = updateBeam(newList);
		newList = updateTuplet(newList);
		return newList;
	}

	private static List<Score> updateTuplet(List<Score> list) {
		List<Score> newList = new ArrayList<Score>();
		for (int i=0; i<list.size(); i++) {
			Score sc = list.get(i);
			if (sc instanceof ScoreSingleNote) {
				ScoreSingleNote sNote = (ScoreSingleNote) sc;
				if (sNote.getTuplet()==Tuplet.NONE) {
					newList.add(sNote);
					continue;
				}
				ScoreBeamNote bNote = new ScoreBeamNote(sNote);
				for (int j=i+1; j<list.size(); j++) {
					Score sc2 = list.get(j);
					ScoreSingleNote sNote2 = (ScoreSingleNote) sc2;
					bNote.addNote(sNote2);
					i++;
					if (sNote2.getTuplet()==Tuplet.STOP) {
						break;
					}
				}
				newList.add(bNote);
			} else {
				newList.add(sc);
			}
		}
		return newList;
	}

	private static List<Score> updateBeam(List<Score> list) {
		List<Score> newList = new ArrayList<Score>();
		for (int i=0; i<list.size(); i++) {
			Score sc = list.get(i);
			//　1拍に音符が2個以上かつ間に休符がないなら連衡
			//　三連符は、設定した連衡の数が三連符記号の長さになる
			//　三連符は以下のように設定する
			//　ノート数　　　2　　　　　　　3
			//　休符なし　連衡なし*1　連衡あり+連衡なし
			//　休符あり　連衡なし*1　連衡なし*2
			assert(!(sc instanceof ScorePercNote));
			if (sc instanceof ScoreSingleNote) {
				ScoreSingleNote pNote = (ScoreSingleNote) sc;
				boolean renpu = false;
				if (pNote.getTuplet()==Tuplet.START) {
					renpu = true;
				}
				if ((pNote instanceof ScoreRestNote) && !renpu) {  // 三連符でないなら休符は無視
					newList.add(sc);
					continue;
				}
				int length = pNote.getDuration();
				if (length>=BDX.TIME_BASE) {  // 長さが1拍を超える
					newList.add(sc);
					continue;
				}
				int lCounter = pNote.getLocalCounter() + length;
				if ((lCounter%BDX.TIME_BASE)==0) {  // 音符の終わりが拍の変わり目
					newList.add(sc);
					continue;
				}
				List<ScoreSingleNote> noteList = new ArrayList<ScoreSingleNote>();
				noteList.add(pNote);
				for (int j=i+1; j<list.size(); j++) {
					Score sc2 = list.get(j);
					assert(!(sc2 instanceof ScorePercNote));
					if ( (sc2 instanceof ScorePitchNote) || ((sc2 instanceof ScoreRestNote) && renpu) ) {
						ScoreSingleNote pNote2 = (ScoreSingleNote) sc2;
						noteList.add(pNote2);
						i++;
						lCounter += pNote2.getDuration();
						if ((lCounter%BDX.TIME_BASE)==0) {
							break;
						}
					} else {
						break;
					}
				}
				// 音符の数と長さによって連衡の形を変える
				int size = noteList.size();
				if (size==1) {
					newList.add(sc);
					continue;
				} else if (size==2) {
					if (noteList.get(0).getTuplet()==Tuplet.NONE) {
						List<Beam> beam1 = new ArrayList<Beam>();
						beam1.add(Beam.BEGIN);
						List<Beam> beam2 = new ArrayList<Beam>();
						beam2.add(Beam.END);
						NoteType type1 = noteList.get(0).getType();
						NoteType type2 = noteList.get(1).getType();
						if (type1==type2) {
							if (type1==NoteType._16TH) {
								beam1.add(Beam.BEGIN);
								beam2.add(Beam.END);
							}
						} else {
							if (type1==NoteType._16TH) {
								beam1.add(Beam.FORWARD_HOOK);
							} else {
								beam2.add(Beam.BACKWARD_HOOK);
							}
						}
						noteList.get(0).setBeams(beam1);
						noteList.get(1).setBeams(beam2);
					} else {    // 三連符、ノート数2
						for (ScoreSingleNote sNote2 : noteList) {
							List<Beam> beam = new ArrayList<Beam>();
							beam.add(Beam.NONE);
							sNote2.setBeams(beam);
						}
					}
				} else if (size==3) {
					List<Beam> beam1 = new ArrayList<Beam>();
					List<Beam> beam2 = new ArrayList<Beam>();
					List<Beam> beam3 = new ArrayList<Beam>();
					boolean isRest = false;
					for (ScoreSingleNote sNote2 : noteList) {
						isRest |= (sNote2 instanceof ScoreRestNote);
					}
					if (isRest) {  // (三連符)、休符あり
						beam1.add(Beam.NONE);
						beam2.add(Beam.NONE);
						beam3.add(Beam.NONE);
					} else {
						beam1.add(Beam.BEGIN);
						beam2.add(Beam.CONTINUE);
						beam3.add(Beam.END);
					}
					if (noteList.get(0).getTuplet()==Tuplet.NONE) {
						int eighth = -1;
						for (int j=0; j<size; j++) {
							if (noteList.get(j).getType()==Score.NoteType.EIGHTH) {
								eighth = j;
								break;
							}
						}
						switch (eighth) {
						case 0:
							beam2.add(Beam.BEGIN);
							beam3.add(Beam.END);
							break;
						case 1:
							beam1.add(Beam.FORWARD_HOOK);
							beam3.add(Beam.BACKWARD_HOOK);
							break;
						case 2:
							beam1.add(Beam.BEGIN);
							beam2.add(Beam.END);
							break;
						case -1:
							beam1.add(Beam.BEGIN);
							beam2.add(Beam.CONTINUE);
							beam3.add(Beam.END);
						default:
							break;
						}
					} else {  // 三連符、ノート数3
						beam1.add(Beam.NONE);
						beam2.add(Beam.NONE);
						beam3.add(Beam.NONE);
					}
					noteList.get(0).setBeams(beam1);
					noteList.get(1).setBeams(beam2);
					noteList.get(2).setBeams(beam3);
				} else if (size==4) {
					List<Beam> beam1 = new ArrayList<Beam>();
					beam1.add(Beam.BEGIN);
					beam1.add(Beam.BEGIN);
					List<Beam> beam2 = new ArrayList<Beam>();
					beam2.add(Beam.CONTINUE);
					beam2.add(Beam.CONTINUE);
					List<Beam> beam3 = new ArrayList<Beam>(beam2);
					List<Beam> beam4 = new ArrayList<Beam>();
					beam4.add(Beam.END);
					beam4.add(Beam.END);
					noteList.get(0).setBeams(beam1);
					noteList.get(1).setBeams(beam2);
					noteList.get(2).setBeams(beam3);
					noteList.get(3).setBeams(beam4);
				}
				ScoreBeamNote bNote = new ScoreBeamNote(noteList);
				newList.add(bNote);
			} else {
				newList.add(sc);
			}
		}
		return newList;
	}

	private static void updateType(List<Score> list) {
		for (int i=0; i<list.size(); i++) {
			Score sc = list.get(i);
			if (sc instanceof ScoreSingleNote) {
				ScoreSingleNote sNote = (ScoreSingleNote) sc;
				int lCounter = sNote.getLocalCounter() % BDX.TIME_BASE;
				switch (sNote.getDuration()) {
				case 3:
					sNote.setType(NoteType._16TH);
					break;
				case 4:
					sNote.setType(NoteType.EIGHTH);
					switch (lCounter) {
					case 0:
						sNote.setTuplet(Tuplet.START);
						break;
					case 4:
						sNote.setTuplet(Tuplet.CONTINUE);
						break;
					case 8:
						sNote.setTuplet(Tuplet.STOP);
						break;
					default:
						sNote.setTuplet(Tuplet.CONTINUE);
					break;
					}
					break;
				case 8:
					sNote.setType(NoteType.QUARTER);
					switch (lCounter) {
					case 0:
						sNote.setTuplet(Tuplet.START);
						break;
					case 4:
						sNote.setTuplet(Tuplet.STOP);
						break;
					default:
						sNote.setTuplet(Tuplet.CONTINUE);
					break;
					}
					break;
				case 9:
					sNote.setDot(true);
				case 6:
					sNote.setType(NoteType.EIGHTH);
					break;
				case 18:
					sNote.setDot(true);
				case 12:
					sNote.setType(NoteType.QUARTER);
					break;
				case 36:
					sNote.setDot(true);
				case 24:
					sNote.setType(NoteType.HALF);
					break;
				case 48:
				default:
					sNote.setType(NoteType.WHOLE);
				break;
				}
			}
		}
	}


	/**
	 * 音符と休符を適切な長さにつなげる。
	 * @param list
	 * @param time
	 * @return
	 */
	private static List<Score> updateDuration(List<Score> list, int time) {
		List<Score> newList = new ArrayList<Score>();
		for (int i=0; i<list.size(); i++) {
			Score sc = list.get(i);
			if (sc instanceof ScorePitchNote) {
				ScorePitchNote pNote = (ScorePitchNote) sc;
				int lCounter = pNote.getLocalCounter();
				int len = pNote.getDuration();
				if ( (lCounter==0 || (lCounter==2*BDX.TIME_BASE && time==4)) &&
						len==BDX.TIME_BASE && pNote.isTieStart() ) {
					for (int j=i+1; j<list.size(); j++) {
						Score sc2 = list.get(j);
						if (sc2 instanceof ScorePitchNote) {
							ScorePitchNote pNote2 = (ScorePitchNote) sc2;
							if (pNote2.getChord()!=null) {
								break;
							}
							len += pNote2.getDuration();
							if (len%BDX.TIME_BASE==0 || len==BDX.TIME_BASE*3/2) {
								pNote.setDuration(len);
								pNote.setTieStart(pNote2.isTieStart());
								i++;
							} else {
								break;
							}
							if (!pNote2.isTieStart()) {
								break;
							}
						} else {
							break;
						}
					}
				}
				newList.add(pNote);
			} else if (sc instanceof ScoreRestNote) {
				ScoreRestNote rNote = (ScoreRestNote) sc;
				int lCounter = rNote.getLocalCounter();
				int len = rNote.getDuration();
				if ( (lCounter==0 || (lCounter==2*BDX.TIME_BASE && time==4)) &&
						len==BDX.TIME_BASE ) {
					for (int j=i+1; j<list.size(); j++) {
						Score sc2 = list.get(j);
						if (sc2 instanceof ScoreRestNote) {
							ScoreRestNote rNote2 = (ScoreRestNote) sc2;
							if (rNote2.getChord()!=null) {
								break;
							}
							len += rNote2.getDuration();
							if (len%BDX.TIME_BASE==0) {
								rNote.setDuration(len);
								i++;
							} else if (len==BDX.TIME_BASE*3/2) {
								rNote.setDuration(len);
								i++;
								break;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}
				newList.add(rNote);
				if (lCounter==0 && rNote.getDuration()==3*BDX.TIME_BASE && time==4) {
					rNote.setDuration(2*BDX.TIME_BASE);
					ScoreRestNote rNote2 = new ScoreRestNote();
					rNote2.setLocalCounter(lCounter+rNote.getDuration());
					rNote2.setDuration(BDX.TIME_BASE);
					newList.add(rNote2);
				}
			} else {
				newList.add(sc);
			}
		}
		return newList;
	}


}
