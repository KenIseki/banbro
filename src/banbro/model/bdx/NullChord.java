package banbro.model.bdx;

/**
 * コードなし
 * N.C.でもない
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
