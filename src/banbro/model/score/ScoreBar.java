package banbro.model.score;

/**
 * ���ߐ�
 * @see BarType
 */
public class ScoreBar implements Score {

	private BarType _type;
	private int _measureNum;

	/**
	 * {@link BarType#SINGLE}�̏��ߐ�
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
	 * @param num ���ߔԍ� �y���̐擪�̏��ߔԍ���0
	 */
	public void setMeasureNum(int num) {
		if (num<0) {
			throw new IllegalArgumentException();
		}
		_measureNum = num;
	}

	/**
	 * @return ���ߔԍ� ���ݒ�̏ꍇ��-1��Ԃ�
	 */
	public int getMeasureNum() {
		return _measureNum;
	}


}
