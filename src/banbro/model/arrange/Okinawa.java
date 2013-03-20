package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.DrumNote;
import banbro.model.bdx.Note;
import banbro.model.bdx.Part;
import banbro.model.bdx.StepValue;

public class Okinawa extends ChangeKey {

	public Okinawa() {
		super();
	}
	public Okinawa(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	protected void arrangePart(Part p) {
		switch (p.getInstrument()) {
		case PIANO:
		case E_PIANO:
		case HARPSICH_:
		case R_ORGAN:
		case P_ORGAN:
		case ACCORDION:
		case SYN_LEAD:
		case SQ_LEAD:
		case SYN_BELL:
		case TIMPANI:
		case SHAMISEN:
		case KOTO:
		case BANJO:
			p.setInstrument(BDXInstrument.MARIMBA);
			break;
		case MUSIC_BOX:
			BDXUtil.shiftNotes(p, 12);
		case HARMONICA:
		case VIBRAPH_:
		case S_GUITAR:
		case F_GUITAR:
		case E_GUITAR:
		case C_GUITAR:
		case O_GUITAR:
		case D_GUITAR:
		case R_GUITAR:
		case NES:
		case P_VIOLIN:
		case HARP:
		case M_TRUMPET:
		case FLUTE:
		case SOPR_SAX:
		case ALTO_SAX:
		case CLARINET:
		case OBOE:
		case PAN_FLUTE:
		case OCARINA:
		case SHAKUHA_:
		case STEEL_DRM:
			p.setInstrument(BDXInstrument.SHAMISEN);
			break;
		case PICK_BASS:
		case SLAP_BASS:
		case SYN_BASS:
		case TUBA:
			p.setInstrument(BDXInstrument.ACO_BASS);
			break;
		case VIOLIN:
		case STRINGS:
		case TRUMPET:
		case TROMBONE:
		case HORN:
		case BRASS:
			p.setInstrument(BDXInstrument.CHORUS);
			break;
		case DBL_BASS:
			p.setInstrument(BDXInstrument.PICK_BASS);
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

	@Override
	protected int calcNewNote(int n) {
		switch (n) {
		case 0:
		case 1:
		case 2:
			return 0;
		case 3:
		case 4:
			return 4;
		case 5:
		case 6:
			return 5;
		case 7:
		case 8:
		case 9:
			return 7;
		case 10:
		case 11:
			return 11;
		default:
			return n;
		}
	}

	@Override
	protected void arrangeDrumPart(Part p) {
		//UXR”²‚«
		List<Note> noteList = p.getNoteList();
		for (int i=0; i<noteList.size(); i++) {
			DrumNote note = (DrumNote) noteList.get(i);
			int noteU = note.getUpperNoteNum();
			int noteL = note.getLowerNoteNum();
			if (noteU==DrumNote.U || noteU==DrumNote.R) {  // UR”²‚«
				noteU = DrumNote.REST;
			}
			if (noteL==DrumNote.X) {  // X”²‚«
				noteL = DrumNote.REST;
			}
			p.setNote(i, DrumNote.getInstanceWithNote(noteU, noteL));
		}
	}

	@Override
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {
		arrangeTempo(tempo, tempoAverage, 90, 10.0, 130.0);
	}

}
