package banbro.model.bdx;

public enum VibratoShape {
	// 00:‚È‚µ 01:sin 02:‹éŒ` 03:‹t‚Ì‚±‚¬‚è 04:‚Ì‚±‚¬‚è 05:ƒ‰ƒ“ƒ_ƒ€
	SIN(0x01, "ƒTƒCƒ“"),
	RECTANGLAR(0x02, "‹éŒ`"),
	SAWTOOTH(0x03, "ƒmƒRƒMƒŠ"),
	REVERSE_SAWTOOTH(0x04, "‹tƒmƒRƒMƒŠ"),
	RANDOM(0x05, "ƒ‰ƒ“ƒ_ƒ€"),
	NONE(0x00, "‚È‚µ");
	
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
