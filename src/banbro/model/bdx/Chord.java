package banbro.model.bdx;

public abstract class Chord {
	private String _chordName;

	public String getChordName() {
		if (_chordName!=null) {
			return _chordName;
		}
		String st = toString();
		return st;
	}

	public void setChordName(String chordName) {
		_chordName = chordName;
	}

}
