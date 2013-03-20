package banbro.model.bdx;

import java.util.HashMap;
import java.util.Map;

public class SingleNote extends Note {

	/** 休符 */
	public static final int REST = 0x00;
	/** 直前の音を伸ばす */
	public static final int EXTEND = 0x80;
	/** 3連符フラグ */
	public static final int GROUP = 0xFF;

	private static Map<Integer, SingleNote> _noteMap = new HashMap<Integer, SingleNote>();

	private int _singleNoteNum;

	public static SingleNote getInstance(int n) {
		SingleNote note = _noteMap.get(n);
		if (note==null) {
			note = new SingleNote(n);
			_noteMap.put(n, note);
		}
		return note;
	}

	public static SingleNote getInstance(int n, boolean isExtend) {
		if (REST<n && n<EXTEND) {
			return getInstance(calcNoteNum(n, isExtend));
		} else {
			return getInstance(n);
		}
	}

	protected SingleNote(int n) {
		super(n);
		_singleNoteNum = n;
		if (isExtend()) {
			_singleNoteNum -= EXTEND;
			if (_singleNoteNum==REST) {
				_singleNoteNum = 127;
			}
		} else if (isGroup()) {
			_singleNoteNum = REST;
		}
	}

	@Override
	public InstrumentType getType() {
		return InstrumentType.SINGLE;
	}
	
	protected static int calcNoteNum(int n, boolean isExtend) {
		int num = n;
		if (isExtend) {
			if (n==127) {
				num = EXTEND;
			} else {
				num += EXTEND;
			}
		}
		return num;
	}

	public boolean isNote() {
		return (REST<getNoteNum() && getNoteNum()<EXTEND);
	}

	public boolean isRest() {
		return getNoteNum()==REST;
	}

	public boolean isExtend() {
		return (EXTEND<=getNoteNum() && getNoteNum()<GROUP);
	}

	public boolean isGroup() {
		return getNoteNum()==GROUP;
	}

	public int getSingleNoteNum() {
		return _singleNoteNum;
	}

	public SingleNote clone() {
		return SingleNote.getInstance(getNoteNum());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SingleNote) {
			return super.equals(obj);
		}
		return false;
	}

}
