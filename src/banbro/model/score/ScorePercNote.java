package banbro.model.score;

import banbro.model.bdx.DrumNote;

/**
 * �o���u���̑Ŋy��p�̉���
 * ���̏ォ���ɕ\������
 */
public class ScorePercNote extends ScoreSingleNote {

	/**
	 * �Ŋy�퉹���̕\���ʒu
	 */
	public enum PercLine {
		UPPER, LOWER
	}

	private PercLine _line;
	private int _noteNum;

	/**
	 * @param line
	 */
	public ScorePercNote(PercLine line) {
		_line = line;
	}

	public PercLine getLine() {
		return _line;
	}

	/**
	 * @param noteNum B,A,Y,X,U,S,H,M,L,R,REST
	 * @see DrumNote#getInstanceWithNote(int, int)
	 */
	public void setNoteNum(int noteNum) {
		_noteNum = noteNum;
	}

	/**
	 * @see DrumNote#getInstanceWithNote(int, int)
	 */
	@Override
	public int getNoteNum() {
		return _noteNum;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(_line.toString());
		sb.append("NoteNum=" + getNoteNum());
		sb.append("]");
		return sb.toString();
	}

}
