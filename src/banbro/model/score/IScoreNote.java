package banbro.model.score;

/**
 * ���������鉹��
 */
public interface IScoreNote {

	/**
	 * @return �����̒���
	 */
	int getDuration();

	/**
	 * @param duration �����̒���
	 */
	void setDuration(int duration);

	/**
	 * @return ���ߓ��̈ʒu
	 */
	int getLocalCounter();

	/**
	 * @param counter ���ߓ��̈ʒu
	 */
	void setLocalCounter(int counter);

}
