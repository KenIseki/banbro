package banbro.model.score;

public class ScorePitchNote extends ScoreSingleNote implements IStemNote {

	private Pitch _step;
	private int _alter;
	private int _octave;
	private Stem _stem;

	private boolean _tieStart;
	private boolean _tieStop;
	private Accidental _accidental;

	public ScorePitchNote() {
	}

	public Pitch getStep() {
		return _step;
	}
	public void setStep(Pitch step) {
		_step = step;
	}
	public int getAlter() {
		return _alter;
	}
	public void setAlter(int alter) {
		_alter = alter;
	}
	public int getOctave() {
		return _octave;
	}
	public void setOctave(int octave) {
		_octave = octave;
	}
	public Stem getStem() {
		return _stem;
	}
	public void setStem(Stem stem) {
		_stem = stem;
	}
	public boolean isTieStart() {
		return _tieStart;
	}
	public void setTieStart(boolean start) {
		_tieStart = start;
	}
	public boolean isTieStop() {
		return _tieStop;
	}
	public void setTieStop(boolean stop) {
		_tieStop = stop;
	}
	public Accidental getAccidental() {
		return _accidental;
	}
	public void setAccidental(Accidental accidental) {
		_accidental = accidental;
	}

	public void updateStem() {
		setStem(ScoreUtil.calcStem(this));
		int lyricLine = -7;
		int line = ScoreUtil.calcScoreLine(this);
		if (line<-6) {
			lyricLine = line - 1;
		}
		setLyricLine(lyricLine);
		if (getTuplet()!=Tuplet.NONE) {
			setTupletLine(ScoreUtil.calcUpperScoreLine(this));
		}
	}

	@Override
	public int getNoteNum() {
		int num = 12*getOctave();
		num += getStep().getNoteNum();
		num += getAlter();
		return num;
	}


}
