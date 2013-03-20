package banbro.model.score;

import java.util.ArrayList;
import java.util.List;

/**
 * 連衡でつながった複数の音符
 */
public class ScoreBeamNote implements Score, IStemNote {

	private Stem _stem;
	private List<ScoreSingleNote> _noteList;

	protected ScoreBeamNote() {
		_noteList = new ArrayList<ScoreSingleNote>();
	}

	public ScoreBeamNote(ScoreSingleNote firstNote) {
		this();
		addNote(firstNote);
	}

	public ScoreBeamNote(List<ScoreSingleNote> notes) {
		this();
		assert(!notes.isEmpty());
		for (ScoreSingleNote note : notes) {
			addNote(note);
		}
	}

	public void addNote(ScoreSingleNote note) {
		_noteList.add(note);
	}

	public List<ScoreSingleNote> getNoteList() {
		return _noteList;
	}

	public ScoreSingleNote getFirstNote() {
		return _noteList.get(0);
	}

	public ScoreSingleNote getLastNote() {
		return _noteList.get(_noteList.size()-1);
	}

	public int getDuration() {
		int d = 0;
		for (ScoreSingleNote note : _noteList) {
			d += note.getDuration();
		}
		return d;
	}

	@Override
	public void setDuration(int duration) {
		throw new IllegalStateException("連衡全体の長さは変更できません");
	}

	public int getLocalCounter() {
		return getFirstNote().getLocalCounter();
	}

	@Override
	public void setLocalCounter(int counter) {
		getFirstNote().setLocalCounter(counter);
	}

	@Override
	public Stem getStem() {
		return _stem;
	}

	@Override
	public void setStem(Stem stem) {
		_stem = stem;
		for (ScoreSingleNote note : _noteList) {
			if (note instanceof IStemNote) {
				((IStemNote) note).setStem(stem);
			}
		}
	}

	public void updateStem() {
		// 第3線からの距離の平均によって決定
		// 0なら後の♪が優先
		boolean flag = !getFirstNote().getBeam().isEmpty() && getFirstNote().getBeam().get(0)==Beam.NONE;
		Stem stem = Stem.NONE;
		int lineAve = 0;
		int lastLine = 0;
		int upperLine = Integer.MIN_VALUE;
		int lowerLine = Integer.MAX_VALUE;
		int firstBeams = 0;
		for (ScoreSingleNote sNote : _noteList) {
			if (sNote instanceof ScorePitchNote) {
				ScorePitchNote pNote = (ScorePitchNote) sNote;
				int line2 = ScoreUtil.calcScoreLine(pNote);
				if (upperLine<line2) {
					upperLine = line2;
				}
				if (line2<lowerLine) {
					lowerLine = line2;
				}
				lineAve += line2;
				lastLine = line2;
				if (firstBeams==0) {
					firstBeams = pNote.getBeam().size();
				}
			}
		}
		if (lineAve==0) {
			if (lastLine<0) {
				stem = Stem.UP;
			} else {
				stem = Stem.DOWN;
			}
		} else {
			if (lineAve<0) {
				stem = Stem.UP;
			} else {
				stem = Stem.DOWN;
			}
		}
		int tupletLine = 8;
		int beamLine = 0;
		if (flag) {
			for (ScoreSingleNote sNote : _noteList) {
				if (sNote instanceof ScorePitchNote) {
					((ScorePitchNote) sNote).updateStem();
				}
				int line2 = ScoreUtil.calcScoreLine(sNote);
				if (line2<0) {
					tupletLine = Math.max(tupletLine, line2+10);  // +7:音符の縦の長さ  +3:音符上部と三連符記号の距離
				} else {
					tupletLine = Math.max(tupletLine, line2+4);  // +4:音符上部と三連符記号の距離
				}
			}
		} else {
			setStem(stem);
			if (stem==Stem.DOWN) {
				beamLine = lowerLine;
				tupletLine = Math.max(tupletLine, upperLine+4);  // +4:音符上部と三連符記号の距離
			} else {
				beamLine = upperLine;
				tupletLine = Math.max(tupletLine, upperLine+10);  // +7:音符の縦の長さ  +3:音符上部と三連符記号の距離
			}
			for (ScoreSingleNote note : _noteList) {
				if (firstBeams==2) {
					if (stem==Stem.DOWN) {
						beamLine++;
					} else {
						beamLine--;
					}
				}
				note.setBeamLine(beamLine);
			}
		}
		if (getFirstNote().getTuplet()==Tuplet.NONE) {
			tupletLine = 0;
		}
		//
		if (stem==Stem.DOWN) {
			int lyricLine = -7;
			lyricLine = Math.min(-7, beamLine-7);
			for (ScoreSingleNote sNote : _noteList) {
				sNote.setLyricLine(lyricLine);
			}
		} else {
			for (ScoreSingleNote sNote : _noteList) {
				int lyricLine = -7;
				int line = ScoreUtil.calcScoreLine(sNote);
				if (line<-6) {
					lyricLine = line - 1;
				}
				sNote.setLyricLine(lyricLine);
			}
		}
		for (ScoreSingleNote sNote : _noteList) {
			if (sNote instanceof ScoreChordNote) {
				((ScoreChordNote) sNote).updateStem();
			} else {
				sNote.setTupletLine(tupletLine);
			}
		}

		// 連衡の傾き
		if (getFirstNote().getTuplet()!=Tuplet.NONE) {
			return;
		}
		int firstLine = 0;
		for (ScoreSingleNote sNote : _noteList) {
			if (sNote instanceof ScorePitchNote) {
				firstLine = ScoreUtil.calcScoreLine(sNote);
				break;
			}
		}
		int slope = 0;
		if (firstLine<lastLine) {
			slope = Math.min(4, (lastLine-firstLine)/(_noteList.size()-1));
			if (slope==0) {
				slope = 1;
			}
		} else if (firstLine>lastLine) {
			slope = Math.max(-4, (lastLine-firstLine)/(_noteList.size()-1));
			if (slope==0) {
				slope = -1;
			}
		}
		for (ScoreSingleNote sNote : _noteList) {
			if (sNote instanceof ScorePitchNote) {
				((ScorePitchNote) sNote).setSlope(slope);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<_noteList.size(); i++) {
			ScoreSingleNote note = _noteList.get(i);
			sb.append(note.toString());
			if (i<_noteList.size()-1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

}
