package banbro.model.bdx;

/**
 * オリジナルコード数の限界を超えたときに発生する。
 */
public class BDXOriginalChordOverflowException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BDXOriginalChordOverflowException() {
		super();
	}

	public BDXOriginalChordOverflowException(String message) {
		super(message);
	}

}
