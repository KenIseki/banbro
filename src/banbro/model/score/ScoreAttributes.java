package banbro.model.score;

/**
 * �y���̐擪�ɏ������v�f
 * �����L���A���q�A��
 * �r���ŕς��Ƃ��ɂ��g����
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
	 * MusicXML�p
	 * @return �l����������\
	 */
	public int getDivisions() {
		return _divisions;
	}

	/**
	 * MusicXML�p
	 * @param divisions �l����������\
	 */
	public void setDivisions(int divisions) {
		_divisions = divisions;
	}

	/**
	 * @return ��
	 */
	public int getKey() {
		return _key;
	}

	/**
	 * @param key ��
	 * ���̐��ŃV���[�v�̐��A���̐��Ńt���b�g�̐�
	 */
	public void setKey(int key) {
		_key = key;
	}

	/**
	 * ����͏��4
	 * @return ���q
	 */
	public int getTime() {
		return _time;
	}

	/**
	 * ����͏��4
	 * @param time ���q
	 */
	public void setTime(int time) {
		_time = time;
	}

	/**
	 * @return �����L��
	 */
	public Clef getClef() {
		return _clef;
	}

	/**
	 * @param clef �����L��
	 */
	public void setClef(Clef clef) {
		_clef = clef;
	}

	/**
	 * MusiXTeX�p
	 * @return �����L���������o�͂���Ȃ�true
	 */
	public boolean isOutputClef() {
		return _outputClef;
	}

	/**
	 * MusiXTeX�p
	 * @param output �����L���������o�͂���Ȃ�true
	 */
	public void setOutputClef(boolean output) {
		_outputClef = output;
	}

	/**
	 * @return ���ߓ��̈ʒu
	 */
	public int getCounter() {
		return _counter;
	}

	/**
	 * @param counter ���ߓ��̈ʒu
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
