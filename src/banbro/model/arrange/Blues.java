package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.Part;
import banbro.model.bdx.StepValue;

public class Blues extends Shuffle {

	public Blues() {
		super();
	}
	public Blues(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	protected void arrangePart(Part p) {
		switch (p.getInstrument()) {
		case MUSIC_BOX:
			BDXUtil.shiftNotes(p, 12);
		case PIANO:
		case E_PIANO:
		case HARPSICH_:
		case SYN_LEAD:
		case SQ_LEAD:
		case SYN_BELL:
		case NES:
		case P_VIOLIN:
			p.setInstrument(BDXInstrument.PIANO);
			break;
		case R_ORGAN:
		case P_ORGAN:
		case ACCORDION:
		case HARMONICA:
			p.setInstrument(BDXInstrument.HARMONICA);
			break;
		case VIBRAPH_:
		case MARIMBA:
		case HARP:
		case TIMPANI:
		case STEEL_DRM:
			p.setInstrument(BDXInstrument.VIBRAPH_);
			break;
		case S_GUITAR:
		case E_GUITAR:
		case C_GUITAR:
		case O_GUITAR:
		case D_GUITAR:
		case SHAMISEN:
		case BANJO:
			p.setInstrument(BDXInstrument.S_GUITAR);
			break;
		case F_GUITAR:
		case KOTO:
			p.setInstrument(BDXInstrument.F_GUITAR);
			break;
		case R_GUITAR:
		case ALTO_SAX:
		case CLARINET:
		case OBOE:
		case PAN_FLUTE:
		case SHAKUHA_:
			p.setInstrument(BDXInstrument.ALTO_SAX);
			break;
		case VIOLIN:
		case STRINGS:
		case PICCOLO:
		case FLUTE:
		case SOPR_SAX:
		case OCARINA:
			p.setInstrument(BDXInstrument.SOPR_SAX);
			break;
		case PICK_BASS:
		case SYN_BASS:
			p.setInstrument(BDXInstrument.PICK_BASS);
			break;
		case SLAP_BASS:
		case ACO_BASS:
		case TUBA:
			p.setInstrument(BDXInstrument.ACO_BASS);
			break;
		case DBL_BASS:
			break;
		case TRUMPET:
			break;
		case TROMBONE:
			break;
		case M_TRUMPET:
			break;
		case CHORUS:
		case HORN:
			p.setInstrument(BDXInstrument.BRASS);
			break;
		case R_DRUMS:
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
		if (p.getInstrument()==BDXInstrument.MUSIC_BOX) {
			BDXUtil.shiftNotes(p, 12);
		}
		p.setInstrument(BDXInstrument.HARMONICA);
	}

	@Override
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {
		arrangeTempo(tempo, tempoAverage, 85, 10.0, 300.0);
	}

}
