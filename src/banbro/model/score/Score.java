package banbro.model.score;

/**
 * 楽譜に関連する定数
 * 一部はBDXでも使用
 * 楽譜を構成する要素はこのインターフェースをimplementsすること
 */
public interface Score {

	/**
	 * 音符・休符の種類
	 */
	public enum NoteType {
		WHOLE, HALF, QUARTER, EIGHTH, _16TH;
	}

	/**
	 * 三連符
	 */
	public enum Tuplet {
		NONE, START, CONTINUE, STOP;
	}

	public enum Pitch {
		C(0, 0), D(1, 2), E(2, 4), F(3, 5), G(4, 7), A(5, 9), B(6, 11);
		private int _value;
		private int _noteNum;
		private Pitch(int value, int noteNum) {
			_value = value;
			_noteNum = noteNum;
		}
		public int getValue() {
			return _value;
		}
		public int getNoteNum() {
			return _noteNum;
		}
		public static Pitch valueOf(int value) {
			for (Pitch p : values()) {
				if (p.getValue()==value) {
					return p;
				}
			}
			return null;
		}
	}

	/**
	 * 調号、臨時記号
	 */
	public enum Accidental {
		NONE(0, ""), SHARP(1, "♯"), FLAT(2, "♭"), NATURAL(-1, "");
		private int _value;
		private String _text;
		private Accidental(int value, String text) {
			_value = value;
			_text = text;
		}
		public int getValue() {
			return _value;
		}
		public String getText() {
			return _text;
		}
		public static Accidental valueOf(int value) {
			for(Accidental ac : values()) {
				if (ac.getValue()==value) {
					return ac;
				}
			}
			return NONE;
		}
	}

	/**
	 * 棒の向き
	 */
	public enum Stem {
		NONE, UP, DOWN;
	}

	/**
	 * 連衡
	 */
	public enum Beam {
		NONE, BEGIN, CONTINUE, END, FORWARD_HOOK, BACKWARD_HOOK;
	}

	/**
	 * 音部記号
	 */
	public enum Clef {
		G2(0), F4(1), G2_8va(2), NONE(-1);
		private int _value;
		private Clef(int value) {
			_value = value;
		}
		public int getValue() {
			return _value;
		}
		public static Clef valueOf(int value) {
			for(Clef c : values()) {
				if (c.getValue()==value) {
					return c;
				}
			}
			return NONE;
		}
	}

	/**
	 * 小節線の形
	 */
	public enum BarType {
		/** 通常の小節線 */ SINGLE,
		/** 二重の小節線 */ DOUBLE,
		/** 終止線 */ END;
	}

	public static final int KEY_NONE = Integer.MAX_VALUE;

}
