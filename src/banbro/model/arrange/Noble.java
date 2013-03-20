package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Part;
import banbro.model.bdx.StepValue;

public class Noble extends AbstractArrange {

	public Noble() {
		super();
	}
	public Noble(boolean isArrangeTempo, boolean isBDX) {
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
			p.setInstrument(BDXInstrument.HARPSICH_);
		}
	}

	@Override
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {
		arrangeTempo(tempo, tempoAverage, 90, 10.0, 160.0);
	}

}
