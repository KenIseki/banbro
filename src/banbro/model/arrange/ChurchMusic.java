package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Part;
import banbro.model.bdx.StepValue;

public class ChurchMusic extends AbstractArrange {

	public ChurchMusic() {
		super();
	}
	public ChurchMusic(boolean isArrangeTempo, boolean isBDX) {
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
			p.setInstrument(BDXInstrument.P_ORGAN);
		}
	}

	@Override
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {
		arrangeTempo(tempo, tempoAverage, 85, 10.0, 300.0);
	}

}
