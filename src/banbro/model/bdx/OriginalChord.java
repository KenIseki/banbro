package banbro.model.bdx;

import java.text.DecimalFormat;

public class OriginalChord extends Chord {

	private int _num;

	public OriginalChord() {
	}

	/**
	 * @param num 0以上
	 */
	public OriginalChord(int num) {
		if (num<0) {
			throw new IllegalArgumentException("オリジナルコード番号に負の数は使えません");
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
