package banbro.model.score;

/**
 * MusiXTeX�p
 * �Ŋy��p�[�g�ŁA�㔼������͂�����A���߂̍ŏ��ɖ߂�B
 */
public class ScoreBackMeasureBegin implements Score {

	private int _time;

	/**
	 * @param time ���q
	 */
	public ScoreBackMeasureBegin(int time) {
		_time = time;
	}

	public int getTime() {
		return _time;
	}


}
