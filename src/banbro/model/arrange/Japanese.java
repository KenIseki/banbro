package banbro.model.arrange;

import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.Part;

public class Japanese extends AbstractArrange {

	public Japanese() {
		super();
	}
	public Japanese(boolean isArrangeTempo, boolean isBDX) {
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
		case R_ORGAN:
		case P_ORGAN:
		case ACCORDION:
		case VIBRAPH_:
		case MARIMBA:
		case SYN_LEAD:
		case SYN_BELL:
		case NES:
		case P_VIOLIN:
		case HARP:
		case TIMPANI:
		case STEEL_DRM:
			p.setInstrument(BDXInstrument.KOTO);
			break;
		case HARMONICA:
		case SQ_LEAD:
		case CHORUS:
		case TRUMPET:
		case TROMBONE:
		case HORN:
		case BRASS:
		case M_TRUMPET:
		case PICCOLO:
		case FLUTE:
		case SOPR_SAX:
		case ALTO_SAX:
		case CLARINET:
		case OBOE:
		case PAN_FLUTE:
		case OCARINA:
			p.setInstrument(BDXInstrument.SHAKUHA_);
			break;
		case S_GUITAR:
		case F_GUITAR:
		case E_GUITAR:
		case C_GUITAR:
		case O_GUITAR:
		case D_GUITAR:
		case R_GUITAR:
		case VIOLIN:
		case STRINGS:
		case BANJO:
			p.setInstrument(BDXInstrument.SHAMISEN);
			break;
		case PICK_BASS:
		case SLAP_BASS:
		case SYN_BASS:
		case TUBA:
			p.setInstrument(BDXInstrument.ACO_BASS);
			break;
		case R_DRUMS:
		case E_DRUMS:
		case S_DRUMS:
		case PERC_SET:
		case BONGO_SET:
		case CONGA_SET:
			p.setInstrument(BDXInstrument.JPN_PERC_);
			break;
		default:
			break;
		}
	}

}
