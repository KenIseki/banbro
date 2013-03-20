package banbro.model.bdx;

/**
 * �R�[�h�Ȃ�
 * N.C.�ł��Ȃ�
 */
public final class NullChord extends Chord {
	private static final NullChord _instance = new NullChord();
	private NullChord() {
	}
	public static NullChord getInstance() {
		return _instance;
	}
	@Override
	public String getChordName() {
		return toString();
	}
	@Override
	public String toString() {
		return "Null Chord";
	}
}
