package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDX;
import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.Part;
import banbro.model.bdx.StepValue;

public class HardRock extends AbstractArrange {

	public HardRock() {
		super();
	}
	public HardRock(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	protected void arrangeBDX(BDX bdx) {
		int volume = bdx.getMasterVolume();
		volume = volume * 110 / 100;
		bdx.setMasterVolume(volume);
	}

	@Override
	protected void arrangePart(Part p) {
		switch (p.getInstrument()) {
		case MUSIC_BOX:
			BDXUtil.shiftNotes(p, 12);
		case PIANO:
		case E_PIANO:
		case HARPSICH_:
		case R_ORGAN:
		case P_ORGAN:
		case ACCORDION:
		case HARMONICA:
		case VIBRAPH_:
		case MARIMBA:
		case S_GUITAR:
		case F_GUITAR:
		case E_GUITAR:
		case C_GUITAR:
		case O_GUITAR:
		case SYN_LEAD:
		case SQ_LEAD:
		case SYN_BELL:
		case NES:
		case VIOLIN:
		case P_VIOLIN:
		case CHORUS:
		case HARP:
		case TIMPANI:
		case M_TRUMPET:
		case PICCOLO:
		case FLUTE:
		case SOPR_SAX:
		case CLARINET:
		case OBOE:
		case PAN_FLUTE:
		case OCARINA:
		case SHAKUHA_:
		case SHAMISEN:
		case KOTO:
		case BANJO:
		case STEEL_DRM:
			p.setInstrument(BDXInstrument.D_GUITAR);
			break;
		case SLAP_BASS:
		case ACO_BASS:
		case DBL_BASS:
		case TUBA:
			p.setInstrument(BDXInstrument.PICK_BASS);
			break;
		case STRINGS:
		case TRUMPET:
		case TROMBONE:
		case HORN:
		case BRASS:
		case ALTO_SAX:
			p.setInstrument(BDXInstrument.R_GUITAR);
			break;
		case R_DRUMS:
		case S_DRUMS:
		case PERC_SET:
		case BONGO_SET:
		case CONGA_SET:
		case JPN_PERC_:
			p.setInstrument(BDXInstrument.E_DRUMS);
			break;
		default:
			break;
		}
	}

	@Override
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {
		double min = 115.0;
		double max = 300.0;
		if (tempoAverage<=60) {
			min = 100.0;
		}
		int rate = 105;
		arrangeTempo(tempo, tempoAverage, rate, min, max);
	}

}
