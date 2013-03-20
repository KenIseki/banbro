package banbro.model.arrange;

import java.util.ArrayList;
import java.util.List;

import banbro.model.bdx.BDX;

public final class CompoundArrange extends AbstractArrange {
	
	private List<AbstractArrange> _arrangeList = new ArrayList<AbstractArrange>();

	public CompoundArrange() {
		super();
	}
	public CompoundArrange(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	public void addArrange(AbstractArrange arrange) {
		if (arrange instanceof CompoundArrange) {
			_arrangeList.addAll( ((CompoundArrange) arrange)._arrangeList );
			return;
		}
		_arrangeList.add(arrange);
	}

	@Override
	public synchronized void setOption(String optionName, Object optionValue) {
		for (AbstractArrange ar : _arrangeList) {
			ar.setOption(optionName, optionValue);
		}
	}

	@Override
	protected BDX doArrange(BDX bdx) {
		for (AbstractArrange ar : _arrangeList) {
			ar.setIsArrangeTempo(isArrangeTempo());
			ar.setIsBDX(isBDX());
			ar.setBDX(bdx);
			bdx = ar.arrange();
		}
		return bdx;
	}

	@Override
	public synchronized void arrangeBinary(List<Integer> binary, BDX bdx) {
		for (AbstractArrange ar : _arrangeList) {
			ar.arrangeBinary(binary, bdx);
		}
	}

}
