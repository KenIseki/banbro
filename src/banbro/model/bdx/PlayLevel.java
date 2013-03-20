package banbro.model.bdx;

public enum PlayLevel {
	BEGINNER("�r�M�i�["),
	AMATEUR("�A�}"),
	PRO("�v��"),
	MASTER("�}�X�^�["),
	;
	
	private String _name;
	private PlayLevel(String name) {
		_name = name;
	}
	public String getLevelName() {
		return _name;
	}

}
