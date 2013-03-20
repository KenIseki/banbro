package banbro.model.score;

/**
 * ¬ßü
 * @see BarType
 */
public class ScoreBar implements Score {

	private BarType _type;
	private int _measureNum;

	/**
	 * {@link BarType#SINGLE}‚Ì¬ßü
	 */
	public ScoreBar() {
		this(BarType.SINGLE);
	}

	public ScoreBar(BarType type) {
		_type = type;
		_measureNum = -1;
	}

	public void setType(BarType type) {
		_type = type;
	}

	public BarType getType() {
		return _type;
	}

	/**
	 * @param num ¬ß”Ô† Šy•ˆ‚Ìæ“ª‚Ì¬ß”Ô†‚Í0
	 */
	public void setMeasureNum(int num) {
		if (num<0) {
			throw new IllegalArgumentException();
		}
		_measureNum = num;
	}

	/**
	 * @return ¬ß”Ô† –¢İ’è‚Ìê‡‚Í-1‚ğ•Ô‚·
	 */
	public int getMeasureNum() {
		return _measureNum;
	}


}
