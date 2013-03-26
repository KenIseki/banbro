package banbro.model.bdx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LyricLine {
	private List<StepValue<LyricNote>> _line;
	private String lyric;

	public LyricLine() {
		_line = new ArrayList<StepValue<LyricNote>>();
	}
	public void addNote(int step, LyricNote note) {
		lyric = null;
		if (_line.size()==1) {
			StepValue<LyricNote> sv = _line.get(0);
			String st = sv.getValue().getLyric();
			if (BDXUtil.getLyricLength(st)==0) {
				sv.getValue().setLyric(st + note.getLyric());
				sv.getValue().setLength(note.getLength());
				sv.setStep(step);
				return;
			}
		}
		_line.add(new StepValue<LyricNote>(step, note));
		Collections.sort(_line, StepValue.STEP_COMPARATOR);
	}
	public int getStep() {
		if (_line.isEmpty()) {
			return -1;
		}
		return _line.get(0).getStep();
	}
	public int getEndStep() {
		if (_line.isEmpty()) {
			return -1;
		}
		StepValue<LyricNote> sv = _line.get(_line.size()-1);
		return sv.getStep() + sv.getValue().getLength();
	}
	public int getEndNoteStep() {
		if (_line.isEmpty()) {
			return -1;
		}
		StepValue<LyricNote> sv = _line.get(_line.size()-1);
		return sv.getStep();
	}
	public int getEndNoteLength() {
		if (_line.isEmpty()) {
			return 0;
		}
		StepValue<LyricNote> sv = _line.get(_line.size()-1);
		return sv.getValue().getLength();
	}
	public void setEndNoteLength(int length) {
		if (_line.isEmpty()) {
			return;
		}
		StepValue<LyricNote> sv = _line.get(_line.size()-1);
		sv.getValue().setLength(length);
	}

	public String getLyric() {
		if (lyric==null) {
			StringBuilder sb = new StringBuilder();
			for (StepValue<LyricNote> sv : _line) {
				LyricNote note = sv.getValue();
				sb.append(note.getLyric());
			}
			lyric = sb.toString();
		}
		return lyric;
	}
	public int getLyricLength() {
		return getLyric().length();
	}
	public double getPosition(double step) {
		double position = 0.0;
		if (Double.compare(getEndStep(), step) <= 0) {
			position = getLyricLength();
		} else if (Double.compare(getStep(), step) < 0) {
			for (StepValue<LyricNote> sv : _line) {
				LyricNote note = sv.getValue();
				if (Double.compare(sv.getStep()+note.getLength(), step) < 0) {
					position += note.getLyric().length();
				} else if (Double.compare(sv.getStep(), step) < 0) {
					if (note.getLength()==0) {
						position += note.getLyric().length();
						continue;
					}
					int n = BDXUtil.getLyricLength(note.getLyric());
					Double step2 = step - sv.getStep();
					double pos2 = step2*n / note.getLength();
					position += pos2;
					for (char c : note.getLyric().toCharArray()) {
						if (BDXUtil.isLyricChar(c)) {
							if (pos2>=1.0) {
								pos2 -= 1.0;
							} else {
								break;
							}
						} else {
							position += 1.0;
						}
					}
					break;
				} else {
					break;
				}
			}
		}
		return position;
	}

	
	public LyricLine clone() {
		LyricLine line = new LyricLine();
		for (StepValue<LyricNote> sv : _line) {
			line.addNote(sv.getStep(), sv.getValue().clone());
		}
		return line;
	}

	@Override
	public String toString() {
		return _line.toString();
	}

}
