package banbro.model.bdx;

import java.util.HashMap;
import java.util.Map;

public class PianoNote extends SingleNote implements ChordNote {

	private static Map<Integer, PianoNote> _noteMap = new HashMap<Integer, PianoNote>();

	public static PianoNote getInstance(int n) {
		PianoNote note = _noteMap.get(n);
		if (note==null) {
			note = new PianoNote(n);
			_noteMap.put(n, note);
		}
		return note;
	}

	public static PianoNote getInstance(int n, boolean isExtend) {
		if (REST<n && n<EXTEND) {
			return getInstance(calcNoteNum(n, isExtend));
		} else {
			return getInstance(n);
		}
	}

	protected PianoNote(int n) {
		super(n);
	}

	@Override
	public InstrumentType getType() {
		return InstrumentType.PIANO;
	}

	@Override
	public int getButtonNum() {
		return getSingleNoteNum();
	}

	@Override
	public PianoNote clone() {
		return PianoNote.getInstance(getNoteNum());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PianoNote) {
			return super.equals(obj);
		}
		return false;
	}

}
