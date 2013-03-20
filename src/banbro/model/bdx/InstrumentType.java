package banbro.model.bdx;

public enum InstrumentType {
	SINGLE(0, "�P��"),
	DRUMS(1, "�h����"),
	GUITAR(2, "�M�^�[�R�[�h"),
	PIANO(3, "�s�A�m�a��"),
	NONE(-1, "None");
	
	private int _value;
	private String _name;
	private InstrumentType(int value, String name) {
		_value = value;
		_name = name;
	}
	public int intValue() {
		return _value;
	}
	public String getTypeName() {
		return _name;
	}
	public static InstrumentType valueOf(int value) {
		for (InstrumentType type : values()) {
			if (type.intValue()==value) {
				return type;
			}
		}
		return NONE;
	}

}
