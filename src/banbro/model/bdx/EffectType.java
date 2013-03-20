package banbro.model.bdx;

public enum EffectType {
	NONE(0x01, "‚È‚µ"),
	CROSS(0x02, "CROSS"),
	ECHO(0x03, "ECHO"),
	CHORUS(0x04, "CHORUS"),
	SP(0x00, "“ÁŽê");
	
	private int _value;
	private String _name;
	private EffectType(int value, String name) {
		_value = value;
		_name = name;
	}
	public int getTypeValue() {
		return _value;
	}
	public String getTypeName() {
		return _name;
	}
	public static EffectType valueOf(int value) {
		for (EffectType type : values()) {
			if (type.getTypeValue()==value) {
				return type;
			}
		}
		return SP;
	}

}
