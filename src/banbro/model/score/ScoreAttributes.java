package banbro.model.score;

/**
 * 楽譜の先頭に書かれる要素
 * 音部記号、拍子、調
 * 途中で変わるときにも使われる
 */
public class ScoreAttributes implements Score {

	private int _divisions;
	private int _key;
	private int _time;
	private Clef _clef;

	private boolean _outputClef;
	private int _counter;

	public ScoreAttributes() {
		_divisions = 0;
		_key = KEY_NONE;
		_time = 0;
		_clef = Clef.NONE;
		_outputClef = false;
		_counter = 0;
	}

	/**
	 * MusicXML用
	 * @return 四分音符分解能
	 */
	public int getDivisions() {
		return _divisions;
	}

	/**
	 * MusicXML用
	 * @param divisions 四分音符分解能
	 */
	public void setDivisions(int divisions) {
		_divisions = divisions;
	}

	/**
	 * @return 調
	 */
	public int getKey() {
		return _key;
	}

	/**
	 * @param key 調
	 * 正の数でシャープの数、負の数でフラットの数
	 */
	public void setKey(int key) {
		_key = key;
	}

	/**
	 * 分母は常に4
	 * @return 拍子
	 */
	public int getTime() {
		return _time;
	}

	/**
	 * 分母は常に4
	 * @param time 拍子
	 */
	public void setTime(int time) {
		_time = time;
	}

	/**
	 * @return 音部記号
	 */
	public Clef getClef() {
		return _clef;
	}

	/**
	 * @param clef 音部記号
	 */
	public void setClef(Clef clef) {
		_clef = clef;
	}

	/**
	 * MusiXTeX用
	 * @return 音部記号を強制出力するならtrue
	 */
	public boolean isOutputClef() {
		return _outputClef;
	}

	/**
	 * MusiXTeX用
	 * @param output 音部記号を強制出力するならtrue
	 */
	public void setOutputClef(boolean output) {
		_outputClef = output;
	}

	/**
	 * @return 小節内の位置
	 */
	public int getCounter() {
		return _counter;
	}

	/**
	 * @param counter 小節内の位置
	 */
	public void setCounter(int counter) {
		_counter = counter;
	}

	public boolean isSetDivisions() {
		return _divisions>0;
	}

	public boolean isSetKey() {
		return _key!=KEY_NONE;
	}

	public boolean isSetTime() {
		return _time>0;
	}

	public boolean isSetClef() {
		return _clef!=Clef.NONE;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (isSetDivisions()) {
			sb.append("Divisions=" + getDivisions() + " ");
		}
		if (isSetTime()) {
			sb.append("Time=" + getTime() + " ");
		}
		if (isSetClef()) {
			sb.append("Clef=" + getClef() + " ");
		}
		if (isSetKey()) {
			sb.append("Key=" + getKey() + " ");
		}
		return sb.toString();
	}

}
