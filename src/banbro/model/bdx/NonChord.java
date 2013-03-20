package banbro.model.bdx;

/**
 * N.C.
 */
public final class NonChord extends Chord {
	private static final NonChord _instance = new NonChord();
	private NonChord() {
	}
	public static NonChord getInstance() {
		return _instance;
	}
	@Override
	public String getChordName() {
		return toString();
	}
	@Override
	public String toString() {
		return "N.C.";
	}
}
