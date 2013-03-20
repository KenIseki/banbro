package banbro.model.score;

/**
 * MusiXTeX用
 * 打楽器パートで、上半分を入力した後、小節の最初に戻る。
 */
public class ScoreBackMeasureBegin implements Score {

	private int _time;

	/**
	 * @param time 拍子
	 */
	public ScoreBackMeasureBegin(int time) {
		_time = time;
	}

	public int getTime() {
		return _time;
	}


}
