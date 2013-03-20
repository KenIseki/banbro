package banbro.model.bdx;


public class Note implements Comparable<Note> {

	protected int _noteNum;

	protected Note(int n) {
		_noteNum = n;
	}

	public InstrumentType getType() {
		return InstrumentType.NONE;
	}

	public int getNoteNum() {
		return _noteNum;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Note) {
			return ((Note)obj).getNoteNum() == getNoteNum();
		}
		return false;
	}

	@Override
	public int compareTo(Note o) {
		return getNoteNum() - o.getNoteNum();
	}

	@Override
	public Note clone() {
		return new Note(_noteNum);
	}

	@Override
	public String toString() {
		return "note=" + _noteNum;
	}

	public static final Note getInstance(int n, InstrumentType type) {
		switch (type) {
		case SINGLE:
			return SingleNote.getInstance(n);
		case DRUMS:
			return DrumNote.getInstance(n);
		case GUITAR:
			return GuitarNote.getInstance(n);
		case PIANO:
			return PianoNote.getInstance(n);
		case NONE:
		default:
			return new Note(n);
		}
	}

}
