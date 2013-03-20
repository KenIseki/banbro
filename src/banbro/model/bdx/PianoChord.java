package banbro.model.bdx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import banbro.model.bdx.Voicing.Spread;
import banbro.model.score.Score.Accidental;


public class PianoChord {

	private List<SingleNote> _notes;
	private boolean _used;
	private String _chordName;

	public PianoChord() {
		_notes = new ArrayList<SingleNote>();
	}

	/**
	 * @param chord 基本コード
	 * @param voicing
	 */
	public PianoChord(BaseChord chord, Voicing voicing) {
		_used = true;
		int root = chord.getRoot().getNoteNum();
		if (chord.getAccidental()==Accidental.SHARP) {
			root++;
		} else if (chord.getAccidental()==Accidental.FLAT) {
			root--;
			if (root<0) {
				root += 12;
			}
		}
		int[] n = new int[] {0, 4, 7, 0};
		switch (chord.getName()) {
		case _Maj:
			n = new int[] {0, 4, 7, 0};
			break;
		case _m:
			n = new int[] {0, 3, 7, 0};
			break;
		case _7:
			n = new int[] {0, 4, 10, 7};
			break;
		case _M7:
			n = new int[] {0, 4, 11, 7};
			break;
		case _m7:
			n = new int[] {0, 3, 10, 7};
			break;
		case _dim:
			n = new int[] {3, 6, 9, 12};
			break;
		case _m7b5:
			n = new int[] {3, 6, 10, 12};
			break;
		case _aug:
			n = new int[] {0, 4, 8, 0};
			break;
		case _sus4:
			n = new int[] {0, 5, 7, 0};
			break;
		case _7sus4:
			n = new int[] {0, 5, 10, 7};
			break;
		case _6:
			n = new int[] {4, 7, 9, 12};
			break;
		case _add9:
			n = new int[] {4, 7, 14, 12};
			break;
		default:
			throw new IllegalArgumentException();
		}
		_notes = calcNotes(root, n[0], n[1], n[2], n[3], voicing);
	}

	private List<SingleNote> calcNotes(int root, int n1, int n2, int n3, int n4, Voicing voicing) {
		List<SingleNote> notes = new ArrayList<SingleNote>();
		int top = voicing.getTopNote();
		int num = 0;
		num = (top/12)*12 + root + n1;
		while (num>top) {
			num -= 12;
		}
		notes.add(SingleNote.getInstance(num));
		num = (top/12)*12 + root + n2;
		while (num>top) {
			num -= 12;
		}
		notes.add(SingleNote.getInstance(num));
		num = (top/12)*12 + root + n3;
		while (num>top) {
			num -= 12;
		}
		notes.add(SingleNote.getInstance(num));
		Collections.sort(notes);
		if (voicing.getNotes()==3) {
			if (voicing.getSpread()==Spread.NORMAL) {
				num = notes.get(1).getNoteNum() - 12;
				notes.set(1, SingleNote.getInstance(num));
			}
			if (voicing.getSpread()==Spread.WIDE) {
				num = notes.get(0).getNoteNum() - 12;
				notes.set(0, SingleNote.getInstance(num));
				num = notes.get(1).getNoteNum() - 24;
				notes.set(1, SingleNote.getInstance(num));
			}
		} else {  // ノート数4
			if (n4>0) {
				num = (top/12)*12 + root + n4;
				while (num>top) {
					num -= 12;
				}
				notes.add(SingleNote.getInstance(num));
				Collections.sort(notes);
				if (voicing.getSpread()==Spread.NORMAL) {
					num = notes.get(2).getNoteNum() - 12;
					notes.set(2, SingleNote.getInstance(num));
				}
				if (voicing.getSpread()==Spread.WIDE) {
					num = notes.get(0).getNoteNum() - 12;
					notes.set(0, SingleNote.getInstance(num));
					num = notes.get(2).getNoteNum() - 12;
					notes.set(2, SingleNote.getInstance(num));
				}
			} else {  // オクターブ違いの音あり
				switch (voicing.getSpread()) {
				case NARROW:
					num = notes.get(2).getNoteNum() - 12;
					break;
				case NORMAL:
					num = notes.get(1).getNoteNum() - 12;
					notes.set(1, SingleNote.getInstance(num));
					num = notes.get(2).getNoteNum() - 12;
					break;
				case WIDE:
					num = notes.get(1).getNoteNum() - 12;
					notes.set(1, SingleNote.getInstance(num));
					num = notes.get(2).getNoteNum() - 24;
					break;
				default:
					num = 0;
					break;
				}
				notes.add(SingleNote.getInstance(num));
			}
		}
		Collections.sort(notes);
		return notes;
	}

	public PianoChord(SingleNote n4, SingleNote n3, SingleNote n2, SingleNote n1) {
		this();
		_notes.add(n4);
		_notes.add(n3);
		_notes.add(n2);
		_notes.add(n1);
		_used = true;
	}

	public List<SingleNote> getNotes() {
		return _notes;
	}

	public boolean isUsed() {
		return _used;
	}

	public void clearNotes() {
		_notes.clear();
	}

	public void addNote(SingleNote n) {
		_notes.add(n);
	}

	public void setUsed(boolean used) {
		_used = used;
	}

	public String getChordName() {
		return _chordName;
	}

	public void setChordName(String chordName) {
		_chordName = chordName;
	}

	public PianoChord clone() {
		PianoChord copy = new PianoChord();
		for (int i=0; i<_notes.size(); i++) {
			copy.addNote(_notes.get(i).clone());
		}
		copy.setChordName(_chordName);
		copy.setUsed(_used);
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PianoChord) {
			PianoChord chord = (PianoChord) obj;
			List<SingleNote> notes = chord.getNotes();
			if (_notes.size()!=notes.size()) {
				return false;
			}
			for (int i=0; i<_notes.size(); i++) {
				if (_notes.get(i).equals(notes.get(i))==false) {
					return false;
				}
			}
			return _used==chord.isUsed();
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (_used) {
			buf.append("[");
		} else {
			buf.append("(");
		}
		for (int i=0; i<_notes.size(); i++) {
			SingleNote note = _notes.get(i);
			buf.append(note.getSingleNoteNum());
			if (i<_notes.size()-1) {
				buf.append(" ");
			}
		}
		if (_used) {
			buf.append("]");
		} else {
			buf.append(")");
		}
		return buf.toString();
	}

}
