package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.DrumNote;
import banbro.model.bdx.Note;
import banbro.model.bdx.Part;
import banbro.model.bdx.StepValue;

public class Orchestra extends AbstractArrange {

	public Orchestra() {
		super();
	}
	public Orchestra(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	protected void arrangePart(Part p) {
		switch (p.getInstrument()) {
		case MUSIC_BOX:
			BDXUtil.shiftNotes(p, 12);
		case PIANO:
		case HARPSICH_:
		case BANJO:
			p.setInstrument(BDXInstrument.P_VIOLIN);
			break;
		case E_PIANO:
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
		case D_GUITAR:
		case R_GUITAR:
		case SYN_LEAD:
		case SQ_LEAD:
		case SYN_BELL:
		case NES:
		case BRASS:
		case PICCOLO:
		case FLUTE:
		case SOPR_SAX:
		case ALTO_SAX:
		case CLARINET:
		case PAN_FLUTE:
		case SHAKUHA_:
		case SHAMISEN:
		case STEEL_DRM:
			p.setInstrument(BDXInstrument.STRINGS);
			break;
		case PICK_BASS:
		case SLAP_BASS:
		case SYN_BASS:
			p.setInstrument(BDXInstrument.ACO_BASS);
			break;
		case CHORUS:
			p.setInstrument(BDXInstrument.HORN);
			break;
		case M_TRUMPET:
			p.setInstrument(BDXInstrument.TRUMPET);
			break;
		case OCARINA:
			p.setInstrument(BDXInstrument.FLUTE);
			break;
		case KOTO:
			p.setInstrument(BDXInstrument.HARP);
			break;
		case R_DRUMS:
		case E_DRUMS:
		case S_DRUMS:
		case BONGO_SET:
		case CONGA_SET:
		case JPN_PERC_:
			p.setInstrument(BDXInstrument.PERC_SET);
		default:
			break;
		}
	}

	@Override
	protected void arrangeDrumPart(Part p) {
		// UL”²‚«
		List<Note> noteList = p.getNoteList();
		for (int i=0; i<noteList.size(); i++) {
			DrumNote note = (DrumNote) noteList.get(i);
			int noteU = note.getUpperNoteNum();
			int noteL = note.getLowerNoteNum();
			if (noteU==DrumNote.U || noteU==DrumNote.L) {  // UL”²‚«
				noteU = DrumNote.REST;
			}
			p.setNote(i, DrumNote.getInstanceWithNote(noteU, noteL));
		}
	}

	@Override
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {
		arrangeTempo(tempo, tempoAverage, 90, 10.0, 140.0);
	}

}
