package banbro.model.bdx;

/**
 * �X�e�b�v�ƒl�̑g
 * @param <V> �l�̌^
 */
public class StepValue<V> implements Comparable<StepValue<V>> {

	private int _step;
	private V _value;

	public StepValue() {
		this(0, null);
	}

	public StepValue(int step, V value) {
		_step = step;
		_value = value;
	}

	public int getStep() {
		return _step;
	}

	public void setStep(int step) {
		_step = step;
	}

	public V getValue() {
		return _value;
	}

	public void setValue(V value) {
		_value = value;
	}

	@Override
	public int compareTo(StepValue<V> o) {
		return _step - o.getStep();
	}

	public StepValue<V> clone() {
		return new StepValue<V>(_step, _value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[step=");
		sb.append(_step);
		sb.append(", value=");
		sb.append(_value.toString());
		sb.append("]");
		return sb.toString();
	}

}
