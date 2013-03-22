package banbro.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 1��1�̑g�ݍ��킹�B
 * ���v�f����щE�v�f�͏d�����Ȃ��B
 * �������A���E�̌^�������Ȃ�A����̂���v�f�������̗v�f�Əd�����Ă��悢�B
 * �i�������̂��݂������ƍl����΂悢�j
 * @param <L> ���v�f�̌^
 * @param <R> �E�v�f�̌^
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
			throw new IllegalStateException("���E�̗v�f�ɑ��Ⴊ����܂�");
		}
		return b;
	}

	public int size() {
		int size = _leftMap.size();
		if (size!=_rightMap.size()) {
			throw new IllegalStateException("���E�̗v�f�ɑ��Ⴊ����܂�");
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
