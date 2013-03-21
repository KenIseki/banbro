package banbro.model;

/**
 * ’²†A—Õ‹L†
 */
public enum Accidental {
	NONE(0, ""), SHARP(1, "ò"), FLAT(2, "ó"), NATURAL(-1, "");

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
