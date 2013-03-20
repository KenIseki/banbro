package banbro.model.bdx;

import java.util.HashMap;
import java.util.Map;

public class DrumNote extends Note {

	/** �x�� */
	public static final int REST = 0x0;
	/** �a�{�^�� */
	public static final int B = 0x1;
	/** �`�{�^�� */
	public static final int A = 0x2;
	/** �x�{�^�� */
	public static final int Y = 0x3;
	/** �w�{�^�� */
	public static final int X = 0x4;
	/** ���{�^�� */
	public static final int U = 0x5;
	/** ���{�^�� */
	public static final int S = 0x6;
	/** ���{�^�� */
	public static final int H = 0x7;
	/** ���{�^�� */
	public static final int M = 0x8;
	/** �k�{�^�� */
	public static final int L = 0x9;
	/** �q�{�^�� */
	public static final int R = 0xA;
	/** 3�A���t���O */
	public static final int GROUP = 0xF;

	private static Map<Integer, DrumNote> _noteMap = new HashMap<Integer, DrumNote>();

	public static DrumNote getInstance(int n) {
		DrumNote note = _noteMap.get(n);
		if (note==null) {
			note = new DrumNote(n);
			_noteMap.put(n, note);
		}
		return note;
	}

	/**
	 * @param upperNote U,S,H,M,L,R,REST,GROUP
	 * @param lowerNote B,A,Y,X,REST,GROUP
	 * @return
	 */
	public static DrumNote getInstanceWithNote(int upperNote, int lowerNote) {
		return getInstance(((upperNote&0xF)<<4) | (lowerNote&0xF));
	}

	private DrumNote(int n) {
		super(n);
	}

	@Override
	public InstrumentType getType() {
		return InstrumentType.DRUMS;
	}

	public int getUpperNoteNum() {
		return ((getNoteNum() & 0xF0) >> 4);
	}

	public int getLowerNoteNum() {
		return (getNoteNum() & 0x0F);
	}

	public static boolean isNote(int noteNum) {
		return (0x1<=noteNum && noteNum<=0xA);
	}

	@Override
	public DrumNote clone() {
		return DrumNote.getInstance(getNoteNum());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DrumNote) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int upperNote = getUpperNoteNum();
		switch (upperNote) {
		case U:
			sb.append("��");
			break;
		case S:
			sb.append("��");
			break;
		case H:
			sb.append("��");
			break;
		case M:
			sb.append("��");
			break;
		case L:
			sb.append("L");
			break;
		case R:
			sb.append("R");
			break;
		case GROUP:
			sb.append("3");
			break;
		case REST:
			sb.append("r");
			break;
		default:
			sb.append("Error");
			break;
		}
		sb.append("/");
		int lowerNote = getLowerNoteNum();
		switch (lowerNote) {
		case B:
			sb.append("B");
			break;
		case A:
			sb.append("A");
			break;
		case Y:
			sb.append("Y");
			break;
		case X:
			sb.append("X");
			break;
		case GROUP:
			sb.append("3");
			break;
		case REST:
			sb.append("r");
			break;
		default:
			sb.append("Error");
			break;
		}
		sb.append("]");
		return sb.toString();
	}

}
