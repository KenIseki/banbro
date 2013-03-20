package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.Part;
import banbro.model.bdx.StepValue;

public class Jazz extends Shuffle {

	public Jazz() {
		super();
	}
	public Jazz(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	protected void arrangePart(Part p) {
		switch (p.getInstrument()) {
		case E_PIANO:
		case HARPSICH_:
		case R_ORGAN:
		case P_ORGAN:
		case ACCORDION:
		case F_GUITAR:
		case SYN_LEAD:
		case SQ_LEAD:
		case SYN_BELL:
		case NES:
		case P_VIOLIN:
		case HARP:
		case SHAMISEN:
		case KOTO:
			p.setInstrument(BDXInstrument.PIANO);
			break;
		case HARMONICA:
		case D_GUITAR:
		case R_GUITAR:
		case VIOLIN:
		case STRINGS:
		case PICCOLO:
		case FLUTE:
		case SOPR_SAX:
		case CLARINET:
		case OBOE:
		case PAN_FLUTE:
		case OCARINA:
		case SHAKUHA_:
			p.setInstrument(BDXInstrument.ALTO_SAX);
			break;
		case MARIMBA:
		case TIMPANI:
		case BANJO:
		case STEEL_DRM:
			p.setInstrument(BDXInstrument.VIBRAPH_);
			break;
		case S_GUITAR:
		case C_GUITAR:
		case O_GUITAR:
			p.setInstrument(BDXInstrument.E_GUITAR);
			break;
		case PICK_BASS:
		case SLAP_BASS:
		case SYN_BASS:
		case TUBA:
			p.setInstrument(BDXInstrument.ACO_BASS);
			break;
		case CHORUS:
			p.setInstrument(BDXInstrument.BRASS);
			break;
		case TROMBONE:
			p.setInstrument(BDXInstrument.SOPR_SAX);
			break;
		case HORN:
			p.setInstrument(BDXInstrument.TRUMPET);
			break;
		case E_DRUMS:
		case S_DRUMS:
		case PERC_SET:
		case BONGO_SET:
		case CONGA_SET:
		case JPN_PERC_:
			p.setInstrument(BDXInstrument.R_DRUMS);
			break;
		default:
			break;
		}
	}

	@Override
	protected void arrangeMainPart(Part p) {
		p.setInstrument(BDXInstrument.SOPR_SAX);
	}

	@Override
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {
		arrangeTempo(tempo, tempoAverage, 95, 10.0, 300.0);
	}

}
