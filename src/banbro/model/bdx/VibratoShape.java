package banbro.model.bdx;

public enum VibratoShape {
	// 00:�Ȃ� 01:sin 02:��` 03:�t�̂����� 04:�̂����� 05:�����_��
	SIN(0x01, "�T�C��"),
	RECTANGLAR(0x02, "��`"),
	SAWTOOTH(0x03, "�m�R�M��"),
	REVERSE_SAWTOOTH(0x04, "�t�m�R�M��"),
	RANDOM(0x05, "�����_��"),
	NONE(0x00, "�Ȃ�");
	
	private int _value;
	private String _name;
	private VibratoShape(int value, String name) {
		_value = value;
		_name = name;
	}
	public int getValue() {
		return _value;
	}
	public String getShapeName() {
		return _name;
	}
	public static VibratoShape valueOf(int value) {
		for (VibratoShape shape : values()) {
			if (shape.getValue()==value) {
				return shape;
			}
		}
		return NONE;
	}

}
