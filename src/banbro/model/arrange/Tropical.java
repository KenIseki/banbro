package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.Part;
import banbro.model.bdx.StepValue;

public class Tropical extends AbstractArrange {

	public Tropical() {
		super();
	}
	public Tropical(boolean isArrangeTempo, boolean isBDX) {
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
			p.setInstrument(BDXInstrument.PIANO);
			break;
		case MUSIC_BOX:
			BDXUtil.shiftNotes(p, 12);
		case HARMONICA:
		case VIBRAPH_:
		case SYN_LEAD:
		case SQ_LEAD:
		case SYN_BELL:
		case NES:
		case VIOLIN:
		case CHORUS:
		case PICCOLO:
		case FLUTE:
		case SOPR_SAX:
		case ALTO_SAX:
		case CLARINET:
		case OBOE:
		case PAN_FLUTE:
		case OCARINA:
		case SHAKUHA_:
			p.setInstrument(BDXInstrument.STEEL_DRM);
			break;
		case S_GUITAR:
		case E_GUITAR:
		case O_GUITAR:
		case D_GUITAR:
		case R_GUITAR:
		case P_VIOLIN:
		case HARP:
			p.setInstrument(BDXInstrument.F_GUITAR);
			break;
		case SLAP_BASS:
		case SYN_BASS:
		case ACO_BASS:
		case DBL_BASS:
			p.setInstrument(BDXInstrument.PICK_BASS);
			break;
		case STRINGS:
			p.setInstrument(BDXInstrument.BRASS);
			break;
		case TIMPANI:
		case SHAMISEN:
		case KOTO:
		case BANJO:
			p.setInstrument(BDXInstrument.MARIMBA);
			break;
		case HORN:
			p.setInstrument(BDXInstrument.TROMBONE);
			break;
		case M_TRUMPET:
			p.setInstrument(BDXInstrument.TRUMPET);
			break;
		case R_DRUMS:
		case E_DRUMS:
		case S_DRUMS:
		case PERC_SET:
		case JPN_PERC_:
			p.setInstrument(BDXInstrument.BONGO_SET);
			break;
		default:
			break;
		}
	}

	@Override
	protected void arrangeMainPart(Part p) {
		p.setInstrument(BDXInstrument.STEEL_DRM);
	}

	@Override
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {
		arrangeTempo(tempo, tempoAverage, 105, 95.0, 300.0);
	}

}
