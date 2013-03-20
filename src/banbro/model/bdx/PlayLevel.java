package banbro.model.bdx;

public enum PlayLevel {
	BEGINNER("ビギナー"),
	AMATEUR("アマ"),
	PRO("プロ"),
	MASTER("マスター"),
	;
	
	private String _name;
	private PlayLevel(String name) {
		_name = name;
	}
	public String getLevelName() {
		return _name;
	}

}
