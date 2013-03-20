package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDX;

public final class Original extends AbstractArrange {

	public Original() {
		super();
	}

	@Override
	public BDX doArrange(BDX bdx) {
		return bdx;
	}

	@Override
	public synchronized void arrangeBinary(List<Integer> binary, BDX bdx) {}

}
