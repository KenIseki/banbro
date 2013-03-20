package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDX;
import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.EffectType;
import banbro.model.bdx.Part;
import banbro.model.bdx.StepValue;
import banbro.model.bdx.VibratoShape;

public class PowerSaving extends AbstractArrange {

	public PowerSaving() {
		super();
	}
	public PowerSaving(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	protected void arrangeBDX(BDX bdx) {
		int volume = bdx.getMasterVolume();
		volume = volume * 80 / 100;
		bdx.setMasterVolume(volume);
	}

	@Override
	protected void arrangePart(Part p) {
		switch (p.getType()) {
		case SINGLE:
			p.setToneEffectType(EffectType.NONE);
		case GUITAR:
		case PIANO:
			p.setVibratoShape(VibratoShape.NONE);
			break;
		default:
			break;
		}
		// 電気を使う楽器の使用を禁止する
		switch (p.getInstrument()) {
		case E_PIANO:
			p.setInstrument(BDXInstrument.PIANO);
			break;
		case R_ORGAN:
			p.setInstrument(BDXInstrument.ACCORDION);
			break;
		case E_GUITAR:
			p.setInstrument(BDXInstrument.C_GUITAR);
			break;
		case O_GUITAR:
			p.setInstrument(BDXInstrument.F_GUITAR);
			break;
		case D_GUITAR:
			p.setInstrument(BDXInstrument.F_GUITAR);
			break;
		case R_GUITAR:
			p.setInstrument(BDXInstrument.S_GUITAR);
			break;
		case PICK_BASS:
			p.setInstrument(BDXInstrument.ACO_BASS);
			break;
		case SYN_BASS:
			p.setInstrument(BDXInstrument.DBL_BASS);
			break;
		case SYN_LEAD:
			p.setInstrument(BDXInstrument.TROMBONE);
			break;
		case SQ_LEAD:
			p.setInstrument(BDXInstrument.FLUTE);
			break;
		case SYN_BELL:
			p.setInstrument(BDXInstrument.VIBRAPH_);
			break;
		case NES:
			p.setInstrument(BDXInstrument.OCARINA);
			break;
		case E_DRUMS:
		case S_DRUMS:
			p.setInstrument(BDXInstrument.R_DRUMS);
			break;
		default:
			break;
		}
	}

	@Override
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {
		arrangeTempo(tempo, tempoAverage, 120, 120.0, 300.0);
	}

}
