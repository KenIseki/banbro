package banbro.model.arrange;

import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Part;

public class NES extends AbstractArrange {

	public NES() {
		super();
	}
	public NES(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	protected void arrangePart(Part p) {
		if (p.getType()==InstrumentType.NONE) {
			return;
		}
		if (p.getType()==InstrumentType.DRUMS) {
			p.setInstrument(BDXInstrument.S_DRUMS);
		} else {
			if (p.getInstrument()==BDXInstrument.MUSIC_BOX) {
				BDXUtil.shiftNotes(p, 12);
			}
			p.setInstrument(BDXInstrument.NES);
		}
	}

}
