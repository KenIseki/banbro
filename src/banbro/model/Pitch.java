package banbro.model;

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
