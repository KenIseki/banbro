package banbro.model.score;

/**
 * �y���Ɋ֘A����萔
 * �y�����\������v�f�͂��̃C���^�[�t�F�[�X��implements���邱��
 */
public interface Score {

	/**
	 * �����E�x���̎��
	 */
	public enum NoteType {
		WHOLE, HALF, QUARTER, EIGHTH, _16TH;
	}

	/**
	 * �O�A��
	 */
	public enum Tuplet {
		NONE, START, CONTINUE, STOP;
	}

	/**
	 * �_�̌���
	 */
	public enum Stem {
		NONE, UP, DOWN;
	}

	/**
	 * �A�t
	 */
	public enum Beam {
		NONE, BEGIN, CONTINUE, END, FORWARD_HOOK, BACKWARD_HOOK;
	}

	/**
	 * ���ߐ��̌`
	 */
	public enum BarType {
		/** �ʏ�̏��ߐ� */ SINGLE,
		/** ��d�̏��ߐ� */ DOUBLE,
		/** �I�~�� */ END;
	}

	public static final int KEY_NONE = Integer.MAX_VALUE;

}
