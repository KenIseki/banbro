package banbro.model;

/**
 * ストロークの方向
 */
public enum Stroke {
	NONE(0), DOWN(1), UP(-1);

	private int _value;

	private Stroke(int value) {
		_value = value;
	}
	public int getValue() {
		return _value;
	}
	public static Stroke valueOf(int value) {
		if (0<value) {
			return DOWN;
		} else if (value<0) {
			return UP;
		} else {
			return NONE;
		}
	}

}
