package banbro.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 1対1の組み合わせ。
 * 左要素および右要素は重複しない。
 * ただし、左右の型が同じなら、一方のある要素が他方の要素と重複してもよい。
 * （横方向のあみだくじと考えればよい）
 * @param <L> 左要素の型
 * @param <R> 右要素の型
 */
public class PairSet<L, R> {

	private Map<L, R> _leftMap;
	private Map<R, L> _rightMap;

	public PairSet() {
		_leftMap = new HashMap<>();
		_rightMap = new HashMap<>();
	}

	public void put(L left, R right) {
		removeLeft(left);
		removeRight(right);
		_leftMap.put(left, right);
		_rightMap.put(right, left);
	}

	public L getLeftValue(R right) {
		return _rightMap.get(right);
	}

	public R getRightValue(L left) {
		return _leftMap.get(left);
	}

	public R removeLeft(L left) {
		R r = getRightValue(left);
		if (r!=null) {
			_rightMap.remove(r);
		}
		_leftMap.remove(left);
		return r;
	}

	public L removeRight(R right) {
		L l = getLeftValue(right);
		if (l!=null) {
			_leftMap.remove(l);
		}
		_rightMap.remove(right);
		return l;
	}

	public void clear() {
		_leftMap.clear();
		_rightMap.clear();
	}

	public boolean isEmpty() {
		boolean b = _leftMap.isEmpty();
		if (b!=_rightMap.isEmpty()) {
			throw new IllegalStateException("左右の要素に相違があります");
		}
		return b;
	}

	public int size() {
		int size = _leftMap.size();
		if (size!=_rightMap.size()) {
			throw new IllegalStateException("左右の要素に相違があります");
		}
		return size;
	}

	public Set<L> getLeftSet() {
		return _leftMap.keySet();
	}

	public Set<R> getRightSet() {
		return _rightMap.keySet();
	}

}
