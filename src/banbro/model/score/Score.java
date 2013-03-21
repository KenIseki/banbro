package banbro.model.score;

/**
 * 楽譜に関連する定数
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
	 * 小節線の形
	 */
	public enum BarType {
		/** 通常の小節線 */ SINGLE,
		/** 二重の小節線 */ DOUBLE,
		/** 終止線 */ END;
	}

	public static final int KEY_NONE = Integer.MAX_VALUE;

}
