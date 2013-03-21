package banbro.model;

/**
 * ‰¹•”‹L†
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
