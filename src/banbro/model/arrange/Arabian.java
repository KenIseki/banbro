package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.DrumNote;
import banbro.model.bdx.Note;
import banbro.model.bdx.Part;
import banbro.model.bdx.StepValue;

public class Arabian extends ChangeKey {

	public Arabian() {
		super();
	}
	public Arabian(boolean isArrangeTempo, boolean isBDX) {
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
		case ACCORDION:
		case VIBRAPH_:
		case MARIMBA:
		case S_GUITAR:
		case E_GUITAR:
		case C_GUITAR:
		case O_GUITAR:
		case D_GUITAR:
		case R_GUITAR:
		case SYN_LEAD:
		case SQ_LEAD:
		case NES:
		case P_VIOLIN:
		case HARP:
		case TIMPANI:
		case OCARINA:
		case SHAKUHA_:
		case SHAMISEN:
		case KOTO:
		case BANJO:
		case STEEL_DRM:
			p.setInstrument(BDXInstrument.F_GUITAR);
			break;
		case P_ORGAN:
		case SYN_BELL:
		case VIOLIN:
		case CHORUS:
		case TRUMPET:
		case TROMBONE:
		case HORN:
		case BRASS:
		case M_TRUMPET:
			p.setInstrument(BDXInstrument.STRINGS);
			break;
		case HARMONICA:
		case PICCOLO:
		case FLUTE:
		case SOPR_SAX:
		case ALTO_SAX:
		case CLARINET:
		case PAN_FLUTE:
			p.setInstrument(BDXInstrument.OBOE);
			break;
		case PICK_BASS:
		case SLAP_BASS:
		case SYN_BASS:
		case TUBA:
			p.setInstrument(BDXInstrument.ACO_BASS);
			break;
		case DBL_BASS:
			p.setInstrument(BDXInstrument.PICK_BASS);
			break;
		case R_DRUMS:
		case E_DRUMS:
		case S_DRUMS:
		case PERC_SET:
		case BONGO_SET:
		case JPN_PERC_:
			p.setInstrument(BDXInstrument.CONGA_SET);
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
			return 0;
		case 2:
			return 2;
		case 3:
		case 4:
			return 3;
		case 5:
		case 6:
			return 6;
		case 7:
			return 7;
		case 8:
		case 9:
			return 8;
		case 10:
		case 11:
			return 11;
		default:
			return n;
		}
	}

	@Override
	protected void arrangeDrumPart(Part p) {
		//ABYÅ´ÇÃÇ›
		List<Note> noteList = p.getNoteList();
		for (int i=0; i<noteList.size(); i++) {
			DrumNote note = (DrumNote) noteList.get(i);
			int noteU = note.getUpperNoteNum();
			int noteL = note.getLowerNoteNum();
			if (noteU==DrumNote.U || noteU==DrumNote.H || noteU==DrumNote.M ||
					noteU==DrumNote.L || noteU==DrumNote.R) {  // Å™Å©Å®LRî≤Ç´
				noteU = DrumNote.REST;
			}
			if (noteL==DrumNote.X) {  // Xî≤Ç´
				noteL = DrumNote.REST;
			}
			p.setNote(i, DrumNote.getInstanceWithNote(noteU, noteL));
		}
	}

	@Override
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {
		arrangeTempo(tempo, tempoAverage, 90, 10.0, 235.0);
	}

}
