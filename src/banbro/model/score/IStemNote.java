package banbro.model.score;

import banbro.model.score.Score.Stem;

/**
 * �_�����鉹��
 * @see Stem
 */
public interface IStemNote extends IScoreNote {

	/**
	 * @return �_�̌���
	 */
	Stem getStem();

	/**
	 * @param stem �_�̌���
	 */
	void setStem(Stem stem);

	/**
	 * �_�̌��������߂�
	 */
	void updateStem();

}
