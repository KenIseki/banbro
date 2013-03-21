package banbro.model.bdx;

import java.util.HashMap;
import java.util.Map;

import banbro.model.Stroke;

public class GuitarNote extends SingleNote implements ChordNote {

	private int _buttonNum;
	private Stroke _stroke;

	private static Map<Integer, GuitarNote> _noteMap = new HashMap<Integer, GuitarNote>();

	public static GuitarNote getInstance(int n) {
		GuitarNote note = _noteMap.get(n);
		if (note==null) {
			note = new GuitarNote(n);
			_noteMap.put(n, note);
		}
		return note;
	}

	public static GuitarNote getInstance(int buttonNum, Stroke stroke, boolean isExtend) {
		if (REST<buttonNum && buttonNum<EXTEND) {
			return getInstance(calcNoteNum(buttonNum, stroke, isExtend));
		} else {
			return getInstance(buttonNum);
		}
	}

	protected GuitarNote(int n) {
		super(n);
		_buttonNum = 0;
		_stroke = Stroke.NONE;
		int singleNotenum = getSingleNoteNum();
		if (singleNotenum!=REST) {
			_buttonNum = calcButtonNum(singleNotenum);
			_stroke = calcStroke(singleNotenum);
			assert(1<=_buttonNum && _buttonNum<=30);
			assert(_stroke!=Stroke.NONE);
		}
	}

	protected GuitarNote(int buttonNum, Stroke stroke, boolean isExtend) {
		super(0);
		_noteNum = calcNoteNum(buttonNum, stroke, isExtend);
		_buttonNum = buttonNum;
		_stroke = stroke;
	}

	@Override
	public InstrumentType getType() {
		return InstrumentType.GUITAR;
	}

	private int calcButtonNum(int n) {
		int buttonNum = n/4;
		if (buttonNum>5) {
			buttonNum--;
		}
		return buttonNum;
	}

	private Stroke calcStroke(int n) {
		int st = n%4;
		switch (st) {
		case 1:
			return Stroke.DOWN;
		case 3:
			return Stroke.UP;
		default:
			return Stroke.NONE;
		}
	}

	private static int calcNoteNum(int buttonNum, Stroke stroke, boolean isExtend) {
		assert(1<=buttonNum && buttonNum<=8);
		int n = 0;
		n += buttonNum * 4;
		if (buttonNum>=5) {
			n += 4;
		}
		switch (stroke) {
		case DOWN:
			n += 1;
			break;
		case UP:
			n += 3;
			break;
		default:
			break;
		}
		if (isExtend) {
			n += EXTEND;
		}
		return n;
	}

	public int getButtonNum() {
		return _buttonNum;
	}

	public Stroke getStroke() {
		return _stroke;
	}

	@Override
	public GuitarNote clone() {
		return GuitarNote.getInstance(getNoteNum());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GuitarNote) {
			return super.equals(obj);
		}
		return false;
	}

}
