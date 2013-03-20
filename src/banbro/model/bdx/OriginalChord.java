package banbro.model.bdx;

import java.text.DecimalFormat;

public class OriginalChord extends Chord {

	private int _num;

	public OriginalChord() {
	}

	/**
	 * @param num 0�ȏ�
	 */
	public OriginalChord(int num) {
		if (num<0) {
			throw new IllegalArgumentException("�I���W�i���R�[�h�ԍ��ɕ��̐��͎g���܂���");
		}
		_num = num;
	}

	public int getNum() {
		return _num;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("O");
		DecimalFormat f = new DecimalFormat("00");
		sb.append(f.format(_num+1));
		return sb.toString();
	}

}
