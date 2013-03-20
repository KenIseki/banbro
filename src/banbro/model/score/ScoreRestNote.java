package banbro.model.score;

/**
 * ‹x•„
 */
public class ScoreRestNote extends ScoreSingleNote {

	private boolean _isChord;

	public ScoreRestNote() {
		super();
		_isChord = false;
	}

	public boolean isChord() {
		return _isChord;
	}

	public void setIsChord(boolean isChord) {
		_isChord = isChord;
	}

	@Override
	public int getNoteNum() {
		return 0;
	}

}
