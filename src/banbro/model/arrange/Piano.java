package banbro.model.arrange;

import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Part;

public class Piano extends AbstractArrange {

	public Piano() {
		super();
	}
	public Piano(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	protected void arrangePart(Part p) {
		if (p.getType()==InstrumentType.NONE) {
			return;
		}
		if (p.getType()==InstrumentType.DRUMS) {
			p.setInstrument(BDXInstrument.NONE);
		} else {
			if (p.getInstrument()==BDXInstrument.MUSIC_BOX) {
				BDXUtil.shiftNotes(p, 12);
			}
			p.setInstrument(BDXInstrument.PIANO);
		}
	}

}
