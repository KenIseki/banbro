package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.Part;
import banbro.model.bdx.StepValue;

public class NewAge extends AbstractArrange {

	public NewAge() {
		super();
	}
	public NewAge(boolean isArrangeTempo, boolean isBDX) {
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
		case VIBRAPH_:
		case MARIMBA:
		case S_GUITAR:
		case F_GUITAR:
		case E_GUITAR:
		case C_GUITAR:
		case O_GUITAR:
		case SYN_BELL:
		case TIMPANI:
		case BANJO:
		case STEEL_DRM:
			p.setInstrument(BDXInstrument.HARP);
			break;
		case R_ORGAN:
		case ACCORDION:
		case D_GUITAR:
		case R_GUITAR:
		case SYN_LEAD:
		case STRINGS:
		case TRUMPET:
		case TROMBONE:
		case BRASS:
			p.setInstrument(BDXInstrument.CHORUS);
			break;
		case P_ORGAN:
		case M_TRUMPET:
			p.setInstrument(BDXInstrument.OBOE);
			break;
		case HARMONICA:
		case SQ_LEAD:
		case NES:
		case VIOLIN:
		case SOPR_SAX:
		case ALTO_SAX:
		case CLARINET:
			p.setInstrument(BDXInstrument.FLUTE);
			break;
		case PICK_BASS:
		case SYN_BASS:
			p.setInstrument(BDXInstrument.TUBA);
			break;
		case SLAP_BASS:
			p.setInstrument(BDXInstrument.ACO_BASS);
			break;
		case SHAKUHA_:
			p.setInstrument(BDXInstrument.PAN_FLUTE);
			break;
		case SHAMISEN:
		case KOTO:
			p.setInstrument(BDXInstrument.E_PIANO);
			break;
		case R_DRUMS:
		case E_DRUMS:
		case S_DRUMS:
		case PERC_SET:
		case CONGA_SET:
		case JPN_PERC_:
			p.setInstrument(BDXInstrument.BONGO_SET);
			break;
		default:
			break;
		}
	}

	@Override
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {
		arrangeTempo(tempo, tempoAverage, 80, 10.0, 300.0);
	}

}
