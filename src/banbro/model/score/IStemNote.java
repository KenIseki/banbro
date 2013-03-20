package banbro.model.score;

import banbro.model.score.Score.Stem;

/**
 * –_‚ª‚ ‚é‰¹•„
 * @see Stem
 */
public interface IStemNote extends IScoreNote {

	/**
	 * @return –_‚ÌŒü‚«
	 */
	Stem getStem();

	/**
	 * @param stem –_‚ÌŒü‚«
	 */
	void setStem(Stem stem);

	/**
	 * –_‚ÌŒü‚«‚ðŒˆ‚ß‚é
	 */
	void updateStem();

}
