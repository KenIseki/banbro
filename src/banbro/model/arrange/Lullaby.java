package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDX;
import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Part;
import banbro.model.bdx.StepValue;

public class Lullaby extends AbstractArrange {

	public Lullaby() {
		super();
	}
	public Lullaby(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	protected void arrangeBDX(BDX bdx) {
		int volume = bdx.getMasterVolume();
		volume = volume * 70 / 100;
		bdx.setMasterVolume(volume);
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
			p.setInstrument(BDXInstrument.VIBRAPH_);
		}
	}

	@Override
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {
		arrangeTempo(tempo, tempoAverage, 60, 10.0, 75.0);
	}

}
