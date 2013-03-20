package banbro.model.arrange;

import banbro.model.arrange.ShuffleDialog.ShuffleOption;

public class ArrangeOption implements ShuffleOption {

	private boolean _isArrangeTempo;  // true:演奏イメージによるテンポ変更をする
	private int _shuffleMaxTempo;
	private int[] _shuffleValues;

	public ArrangeOption() {
		_shuffleMaxTempo = Shuffle.DEFAULT_MAX_TEMPO;
		_shuffleValues = Shuffle.getDefaultShuffleValues();
	}

	public boolean isArrangeTempo(){
		return _isArrangeTempo;
	}
	public void setArrangeTempo(boolean b) {
		_isArrangeTempo = b;
	}

	@Override
	public int[] getShuffleValues() {
		return _shuffleValues;
	}

	@Override
	public int getShuffleMaxTempo() {
		return _shuffleMaxTempo;
	}

	@Override
	public void setShuffleMaxTempo(int tempo) {
		if (tempo<Shuffle.MIN_MAX_TEMPO || Shuffle.MAX_MAX_TEMPO<tempo) {
			return;
		}
		_shuffleMaxTempo = tempo;
	}

	@Override
	public int getShuffleValue(int noteFlag) {
		noteFlag &= 0b1111;
		return _shuffleValues[noteFlag];
	}

	@Override
	public void setShuffleValue(int noteFlag, int value) {
		noteFlag &= 0b1111;
		_shuffleValues[noteFlag] = value;
	}

}
