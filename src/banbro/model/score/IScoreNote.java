package banbro.model.score;

/**
 * 長さがある音符
 */
public interface IScoreNote {

	/**
	 * @return 音符の長さ
	 */
	int getDuration();

	/**
	 * @param duration 音符の長さ
	 */
	void setDuration(int duration);

	/**
	 * @return 小節内の位置
	 */
	int getLocalCounter();

	/**
	 * @param counter 小節内の位置
	 */
	void setLocalCounter(int counter);

}
