package banbro.model.score;

import banbro.model.score.Score.Stem;

/**
 * 棒がある音符
 * @see Stem
 */
public interface IStemNote extends IScoreNote {

	/**
	 * @return 棒の向き
	 */
	Stem getStem();

	/**
	 * @param stem 棒の向き
	 */
	void setStem(Stem stem);

	/**
	 * 棒の向きを決める
	 */
	void updateStem();

}
