package banbro.io.midi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

import banbro.model.Stroke;
import banbro.model.bdx.BDX;
import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BaseChord;
import banbro.model.bdx.BinaryUtil;
import banbro.model.bdx.Chord;
import banbro.model.bdx.DrumNote;
import banbro.model.bdx.Guitar;
import banbro.model.bdx.GuitarChord;
import banbro.model.bdx.GuitarNote;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Note;
import banbro.model.bdx.OriginalChord;
import banbro.model.bdx.Part;
import banbro.model.bdx.Piano;
import banbro.model.bdx.PianoChord;
import banbro.model.bdx.PianoNote;
import banbro.model.bdx.SingleNote;
import banbro.model.bdx.StepValue;
import banbro.model.bdx.VibratoShape;

public class Midi {
	public static final String EXTENSION = "mid";

	/** 打楽器専用チャンネル番号10 MIDIイベントでは9 */
	public static final int DRUM_CHANNEL = 10;
	/** 四分音符分解能 {@link BDX#TIME_BASE}より十分大きい倍数 */
	public static final int MIDI_TIME_BASE = 120;
	/** ギターストロークの音のズレ 5倍した値が24分音符の長さ（{@link #MIDI_TIME_BASE}/6）より小さいこと */
	public static final int STROKE_LENGTH = 2;
	/** 初期設定イベントのために空ける小節数 */
	public static final int MEASURE_SHIFT = 1;

	public static int toMidiInstNumber(BDXInstrument instrument, MidiOption option) {
		switch (instrument) {
		case PIANO: return 1;
		case E_PIANO: return 3;
		case R_ORGAN: return 19;
		case SYN_LEAD: return 82;
		case SYN_BELL: return 89;
		case P_ORGAN: return 20;
		case F_GUITAR: return 26;
		case E_GUITAR: return 27;
		case D_GUITAR: return 31;
		case R_GUITAR: return option.getRguitarNum();  // 代用
		case PICK_BASS: return 35;
		case SYN_BASS: return 39;
		case ACO_BASS: return 33;
		case STRINGS: return 49;
		case VIOLIN: return 41;
		case DBL_BASS: return 44;
		case HARP: return 47;
		case P_VIOLIN: return 46;
		case PICCOLO: return 73;
		case FLUTE: return 74;
		case CLARINET: return 72;
		case OBOE: return 69;
		case SOPR_SAX: return 65;
		case ALTO_SAX: return 66;
		case BRASS: return 62;
		case TRUMPET: return 57;
		case TROMBONE: return 58;
		case HORN: return 61;
		case TUBA: return 59;
		case HARMONICA: return 23;
		case PAN_FLUTE: return 76;
		case OCARINA: return 80;
		case VIBRAPH_: return 12;
		case MARIMBA: return 13;
		case TIMPANI: return 48;
		case STEEL_DRM: return 115;
		case CHORUS: return 53;
		case SHAMISEN: return 107;
		case KOTO: return 108;
		case SHAKUHA_: return 78;
		case NES: return option.getNesNum();  // 代用
		case R_DRUMS:
		case E_DRUMS:
		case S_DRUMS:
		case PERC_SET:
		case BONGO_SET:
		case CONGA_SET:
		case JPN_PERC_: return option.getDrumNum();
		case HARPSICH_: return 7;
		case ACCORDION: return 22;
		case M_TRUMPET: return 60;
		case MUSIC_BOX: return 11;
		case BANJO: return 106;
		case SQ_LEAD: return 81;
		case S_GUITAR: return 25;
		case C_GUITAR: return 28;
		case O_GUITAR: return 30;
		case SLAP_BASS: return 37;
		case NONE:
		default: return 0;
		}
	}

	public static int[] getPercNum(BDXInstrument instrument, MidiOption option) {
		int[] drumValue = null;
		switch (instrument) {
		case R_DRUMS:
			drumValue = option.getRDrum();
			break;
		case E_DRUMS:
			drumValue = option.getEDrum();
			break;
		case S_DRUMS:
			drumValue = option.getSDrum();
			break;
		case PERC_SET:
			drumValue = option.getPercSet();
			break;
		case BONGO_SET:
			drumValue = option.getBongoSet();
			break;
		case CONGA_SET:
			drumValue = option.getCongaSet();
			break;
		case JPN_PERC_:
			drumValue = option.getJpnPerc();
			break;
		default:
			break;
		}
		return drumValue;
	}

	private BDX _bdx;
	private MidiOption _option;
	private final int _offset;

	public Midi(BDX bdx, MidiOption option) {
		_bdx = bdx;
		_option = option;
		_offset = BDX.TIME_BASE * bdx.getTime() * MEASURE_SHIFT;
		assert(_offset>BDX.TIME_BASE);
	}

	public Sequence toSequence() {
		Sequence seq = null;
		try {
			seq = new Sequence(Sequence.PPQ, MIDI_TIME_BASE);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		
		Track track = seq.createTrack();
		try {
			track.add(new MidiEvent(getGMResetMessage(), 0));
			track.add(new MidiEvent(getGSResetMessage(), 1));
			track.add(new MidiEvent(getXGResetMessage(), 2));
			int volume = Math.min((127 * _bdx.getMasterVolume() / 150), 127);
			track.add(new MidiEvent(getMasterVolumetMessage((byte)volume), 10));
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		// テンポ設定
		track.add(toTempoEvent(120, 0));
		for (StepValue<Integer> sv : _bdx.getTempo()) {
			track.add(toTempoEvent(sv.getValue(), sv.getStep()+_offset));
		}
		// 休符で終わっても演奏し続けるようにわざと最後にイベントを入れる
		track.add(toTempoEvent(_bdx.getTempo().get(0).getValue(), (BDX.TIME_BASE*_bdx.getTime()*_bdx.getBars())+_offset));
		int channel = 0;
		int outputPartNum = 0;
		boolean isOutputDrum = false;
		for (int partNum=0; partNum<_bdx.getPartNum(); partNum++) {
			Part part = _bdx.getPart(partNum);
			if (channel>=16) {
				break;
			}
			InstrumentType type = part.getType();
			if (type==InstrumentType.NONE) {
				continue;
			}
			track = seq.createTrack();
			switch (type) {
			case SINGLE:
			case GUITAR:
			case PIANO:
				toSinglePartTrack(part, track, channel);
				channel++;
				break;
			case DRUMS:
				if (isOutputDrum==false) {
					isOutputDrum = true;
					track.add(toInstrumentEvent(_option.getDrumNum()-1, DRUM_CHANNEL-1));
					track.add(toMainVolumeEvent(99, DRUM_CHANNEL-1));
					track.add(toExpressionEvent(99, BDX.TIME_BASE, DRUM_CHANNEL-1));
				}
				toDrumPartTrack(part, track);
				break;
			default:
			}
			if (channel==DRUM_CHANNEL-1) {
				channel++;
			}
			
			outputPartNum++;
			if (outputPartNum>=16) {
				break;
			}
		}
		
		return seq;
	}

	private void toSinglePartTrack(Part part, Track track, int channel) {
		track.add(toInstrumentEvent(toMidiInstNumber(part.getInstrument(), _option)-1, channel));
		track.add(toMainVolumeEvent(part.getPartVolume(), channel));
		track.add(toPanEvent(part.getPan(), channel));
		addEnvelopeEvent(track, channel, part, !_option.isOutputEnvelope());
		boolean isVibrato = false;  // ビブラート効果がかかっているか
		int hold;  // これより長く音を伸ばしたらビブラートをかける
		if (_option.isOutputVibrato() && part.getVibratoShape()!=VibratoShape.NONE) {
			addVibratoEvent(track, channel, part, false);
			// hold最大値=全音符の長さ　本当はミリ秒単位の時間
			hold = (BDX.TIME_BASE*4) * BinaryUtil.HoldValue[part.getVibratoHold()] / 99;
			isVibrato = true;
		} else {
			addVibratoEvent(track, channel, part, true);
			hold = Integer.MAX_VALUE;
		}
		addEffectsEvent(track, channel, part, !(_option.isOutputEffects() && part.getType()==InstrumentType.SINGLE));
		for (StepValue<Integer> sv : part.getVolume()) {
			int step = sv.getStep();
			if (step==0) {
				step = BDX.TIME_BASE;
			} else {
				step += _offset;
			}
			track.add(toExpressionEvent(sv.getValue(), step, channel));
		}
		boolean isOctUp = part.getInstrument()==BDXInstrument.MUSIC_BOX && _option.isMusicBoxOctaveUp();
		boolean isChord = part.getInstrument()==BDXInstrument.R_GUITAR && _option.isRockGuitarChord();
		
		List<Note> noteList = part.getNoteList();
		boolean renpu = false;
		SingleNote lastNote = SingleNote.getInstance(SingleNote.REST);
		int step = 0;
		int start = 0;
		int length = 0;
		for (int i=0; i<noteList.size(); i++) {
			SingleNote note = (SingleNote) noteList.get(i);
			if (i%4==0) {
				renpu = false;
			}
			if (note.isGroup()) {
				renpu = true;
				continue;
			}
			if ((note.isNote() || note.isRest())) {
				//lastnote start length
				if (lastNote.getNoteNum()!=SingleNote.REST) {
					if (isVibrato) {
						addVibratoOnEvent(track, channel, part, start+_offset, false);
						isVibrato = false;
					}
					addSingleNoteEvent(part, track, channel, lastNote,
							start, length, isOctUp, isChord);
					if (length>hold) {
						addVibratoOnEvent(track, channel, part, start+_offset+hold, true);
						isVibrato = true;
					}
				}
				length = 0;
				start = step;
				lastNote = note;
			}
			if (renpu) {
				length += 4;
			} else {
				length += 3;
			}
			if (renpu) {
				step += 4;
			} else {
				step += 3;
			}
		}
		if (length!=0 && lastNote.getNoteNum()!=SingleNote.REST) {
			if (isVibrato) {
				addVibratoOnEvent(track, channel, part, start+_offset, false);
				isVibrato = false;
			}
			addSingleNoteEvent(part, track, channel, lastNote,
					start, length, isOctUp, isChord);
			if (length>hold) {
				addVibratoOnEvent(track, channel, part, start+_offset+hold, true);
				isVibrato = true;
			}
		}
	}

	private void addSingleNoteEvent(Part part, Track track, int channel,
			SingleNote note, int start, int length,
			boolean isOctUp, boolean isChord) {
		InstrumentType type = part.getType();
		if (type==InstrumentType.SINGLE) {
			int pitch = note.getSingleNoteNum();
			if (isOctUp) {
				pitch += 12;
			}
			addSingleNoteEvent(track, channel, pitch, start+_offset, length, isChord);
		} else if (type==InstrumentType.GUITAR) {
			GuitarNote gNote = (GuitarNote) note;
			GuitarChord guitarChord = getGuitarChord(part, start, gNote.getButtonNum(), _bdx.getGuitar());
			addGuitarNoteEvent(track, channel, guitarChord, gNote.getStroke(), start+_offset, length, isOctUp);
		} else if (type==InstrumentType.PIANO) {
			PianoNote pNote = (PianoNote) note;
			PianoChord pianoChord = getPianoChord(part, start, pNote.getButtonNum(), _bdx.getPiano());
			addPianoNoteEvent(track, channel, pianoChord, start+_offset, length, isOctUp);
		}
	}

	private void toDrumPartTrack(Part part, Track track) {
		int partV = part.getPartVolume();
		int stepU = 0;
		int stepL = 0;
		int lengthU = 0;
		int lengthL = 0;
		int[] drumSet = getPercNum(part.getInstrument(), _option);
		if (drumSet==null) {
			drumSet = _option.getRDrum();
		}
		List<Note> noteList = part.getNoteList();
		for (int i=0; i<noteList.size(); i++) {
			DrumNote note = (DrumNote) noteList.get(i);
			int upper = note.getUpperNoteNum();
			int lower = note.getLowerNoteNum();
			if (i%4==0) {
				lengthU = BDX.TIME_BASE / 4;
				lengthL = BDX.TIME_BASE / 4;
			}
			if (upper==DrumNote.GROUP) {
				lengthU = BDX.TIME_BASE / 3;
			} else {
				if (DrumNote.isNote(upper)) {
					for (MidiEvent event :
						toDrumNoteEvent(drumSet[upper-1], stepU+_offset, lengthU, partV, part.getVolume(stepU))) {
						track.add(event);
					}
				}
				stepU += lengthU;
			}
			if (lower==DrumNote.GROUP) {
				lengthL = BDX.TIME_BASE / 3;
			} else {
				if (DrumNote.isNote(lower)) {
					for (MidiEvent event :
						toDrumNoteEvent(drumSet[lower-1], stepL+_offset, lengthL, partV, part.getVolume(stepL))) {
						track.add(event);
					}
				}
				stepL += lengthL;
			}
		}
	}
	
	private GuitarChord getGuitarChord(Part part, int step, int buttonNum, Guitar guitar) {
		GuitarChord guitarChord = null;
		try {
			List<Chord> chords = part.getChordSet(step);
			Chord chord = chords.get(buttonNum-1);
			if (chord instanceof OriginalChord) {
				List<GuitarChord> original = guitar.getOriginalChords();
				guitarChord = original.get(((OriginalChord) chord).getNum());
			} else if (chord instanceof BaseChord) {
				guitarChord = new GuitarChord((BaseChord) chord);
			}
		} catch (Exception e) {
			return null;
		}
		return guitarChord;
	}

	private PianoChord getPianoChord(Part part, int step, int buttonNum, Piano piano) {
		PianoChord pianoChord = null;
		try {
			List<Chord> chords = part.getChordSet(step);
			Chord chord = chords.get(buttonNum-1);
			if (chord instanceof OriginalChord) {
				List<PianoChord> original = piano.getOriginalChords();
				pianoChord = original.get(((OriginalChord) chord).getNum());
			} else if (chord instanceof BaseChord) {
				pianoChord = new PianoChord((BaseChord) chord, part.getVoicing(step));
			}
		} catch (Exception e) {
			return null;
		}
		return pianoChord;
	}

	private MidiEvent toTempoEvent(int tempo, int step) {
		MetaMessage message = new MetaMessage();
		int len = 60*1000000/tempo;
		try {
			message.setMessage(0x51, new byte[]{(byte)(len/0x10000), (byte)(len%0x10000/0x100), (byte)(len%0x100)}, 3);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return new MidiEvent(message, bdxStepToMidiStep(step));
	}
	
	private MidiEvent toInstrumentEvent(int inst, int channel) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(ShortMessage.PROGRAM_CHANGE, channel, inst, 0);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return new MidiEvent(message, 10);
	}

	private MidiEvent toMainVolumeEvent(int volume, int channel) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 7, BinaryUtil.VolumeValue[volume]);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return new MidiEvent(message, 17);
	}

	private MidiEvent toPanEvent(int pan, int channel) {
		ShortMessage message = new ShortMessage();
		int midPanValue = 0x40;
		try {
			for (int k : BinaryUtil.PanMap.keySet()) {
				int value = BinaryUtil.PanMap.get(k);
				if (value==Math.abs(pan)) {
					if (pan<0) {
						midPanValue -= value;
					} else {
						midPanValue += value;
					}
					break;
				}
			}
			message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 10, midPanValue);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return new MidiEvent(message, 20);
	}

	private MidiEvent toExpressionEvent(int volume, int step, int channel) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 11, BinaryUtil.VolumeValue[volume]);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return new MidiEvent(message, bdxStepToMidiStep(step)-1);
	}

	private void addSingleNoteEvent(Track track, int channel,
			int pitch, int step, int length,
			boolean isChord) {
		if (isChord) {
			for (MidiEvent event : toSingleNoteEvent(pitch, step, length, 0, 114, channel)) {
				track.add(event);
			}
			for (MidiEvent event : toSingleNoteEvent(pitch+7, step, length, 2, 50, channel)) {
				track.add(event);
			}
		} else {
			for (MidiEvent event : toSingleNoteEvent(pitch, step, length, channel)) {
				track.add(event);
			}
		}
	}

	private MidiEvent[] toSingleNoteEvent(int pitch, int step, int length, int channel) {
		return toSingleNoteEvent(pitch, step, length, 0, 127, channel);
	}

	private MidiEvent[] toSingleNoteEvent(int pitch, int step, int length, int timing, int v, int channel) {
		MidiEvent[] events = new MidiEvent[2];
		try {
			ShortMessage onMessage = new ShortMessage();
			onMessage.setMessage(ShortMessage.NOTE_ON, channel, pitch, v);
			events[0] = new MidiEvent(onMessage, bdxStepToMidiStep(step)+timing);
			ShortMessage offMessage = new ShortMessage();
			offMessage.setMessage(ShortMessage.NOTE_OFF, channel, pitch, 0);
			events[1] = new MidiEvent(offMessage, bdxStepToMidiStep(step+length)-2);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return events;
	}

	private void addGuitarNoteEvent(Track track, int channel,
			GuitarChord guitarChord, Stroke stroke, int step, int length, boolean isOctUp) {
		if (guitarChord==null) {
			return;
		}
		int[] notes = guitarChord.getNotes();
		boolean[] mute = guitarChord.getMute();
		if (isOctUp) {
			for (int i=0; i<notes.length; i++) {
				notes[i] += 12;
			}
		}
		for (int i=0; i<notes.length; i++) {
			for (int j=i+1; j<notes.length; j++) {
				if (notes[i]==notes[j]) {
					if (mute[i]) {
						notes[i] = 0;
					} else {
						notes[j] = 0;
					}
				}
			}
		}
		int st = stroke.getValue() * STROKE_LENGTH;
		int[] timing = new int[6];
		if (st<0) {
			timing = new int[] {-5*st, -4*st, -3*st, -2*st, -st, 0};
		} else {
			timing = new int[] {0, st, 2*st, 3*st, 4*st, 5*st};
		}
		for (int i=0; i<notes.length; i++) {
			if (notes[i]<=0) {
				continue;
			}
			int v = mute[i] ? 0x3F : 0x7F;
			int muteTime = mute[i] ? BDX.TIME_BASE/6 : length;  // ミュートは24分音符
			for (MidiEvent event : toSingleNoteEvent(notes[i], step, muteTime, timing[i], v, channel)) {
				track.add(event);
			}
		}
	}

	private void addPianoNoteEvent(Track track, int channel,
			PianoChord pianoChord, int step, int length, boolean isOctUp) {
		if (pianoChord==null) {
			return;
		}
		Set<Integer> noteSet = new HashSet<Integer>();
		for (SingleNote note : pianoChord.getNotes()) {
			int n = note.getSingleNoteNum();
			if (n==SingleNote.REST || noteSet.contains(n)) {
				continue;
			}
			noteSet.add(n);
			for (MidiEvent event : toSingleNoteEvent(n, step, length, channel)) {
				track.add(event);
			}
		}
	}

	private void addEnvelopeEvent(Track track, int channel, Part part, boolean reset) {
		/*
		 * 72 リリース
		 * 73 アタック
		 * 75 ディケイ
		 */
		ShortMessage message;
		try {
			if (reset) {
//				message = new ShortMessage();
//				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 72, 64);
//				track.add(new MidiEvent(message, 0));
//				message = new ShortMessage();
//				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 73, 64);
//				track.add(new MidiEvent(message, 0));
//				message = new ShortMessage();
//				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 75, 64);
//				track.add(new MidiEvent(message, 0));
			} else {
				message = new ShortMessage();
				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 72, 127-(127*BinaryUtil.ReleaseValue[part.getToneRelease()]/99));
				track.add(new MidiEvent(message, 82));
				message = new ShortMessage();
				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 73, 127-(127*BinaryUtil.AttackValue[part.getToneAttack()]/99));
				track.add(new MidiEvent(message, 83));
				message = new ShortMessage();
				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 75, 127-(127*BinaryUtil.DecayValue[part.getToneRelease()]/99));
				track.add(new MidiEvent(message, 85));
			}
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	private void addVibratoEvent(Track track, int channel, Part part, boolean reset) {
		/*
		 * 1 ビブラート　かけ具合
		 * 76 ビブラート　速さ
		 * 77 ビブラート　深さ
		 * 78 ビブラート　開始時間
		 */
		if (part.getVibratoShape()==VibratoShape.NONE) {
			reset = true;
		}
		ShortMessage message;
		try {
			if (reset) {
//				message = new ShortMessage();
//				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 76, 64);
//				track.add(new MidiEvent(message, 0));
//				message = new ShortMessage();
//				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 77, 64);
//				track.add(new MidiEvent(message, 0));
//				message = new ShortMessage();
//				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 78, 64);
//				track.add(new MidiEvent(message, 0));
			} else {
				message = new ShortMessage();
				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 76, 127*BinaryUtil.SpeedValue[part.getVibratoSpeed()]/99);
				track.add(new MidiEvent(message, 86));
				message = new ShortMessage();
				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 77, 127*BinaryUtil.DepthValue[part.getVibratoDepth()]/99);
				track.add(new MidiEvent(message, 87));
				message = new ShortMessage();
				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 78, 127*BinaryUtil.DelayValue[part.getVibratoDelay()]/99);
				track.add(new MidiEvent(message, 88));
			}
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	private void addVibratoOnEvent(Track track, int channel, Part part, int step, boolean on) {
		ShortMessage message;
		int value = on ? 127 : 0;
		try {
			message = new ShortMessage();
			message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 1, value);
			track.add(new MidiEvent(message, bdxStepToMidiStep(step)-1));
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	private void addEffectsEvent(Track track, int channel, Part part, boolean reset) {
		/*
		 * 91 リバーブエコー
		 * 93 コーラス
		 */
		ShortMessage message;
		int effectValue = 127 * part.getToneEffectValue() / 20;
		try {
			if (reset) {
//				message = new ShortMessage();
//				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 91, 0);
//				track.add(new MidiEvent(message, 0));
//				message = new ShortMessage();
//				message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 93, 0);
//				track.add(new MidiEvent(message, 0));
			} else {
				switch (part.getToneEffectType()) {
				case ECHO:
					message = new ShortMessage();
					message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 91, effectValue);
					track.add(new MidiEvent(message, 101));
					break;
				case CHORUS:
					message = new ShortMessage();
					message.setMessage(ShortMessage.CONTROL_CHANGE, channel, 93, effectValue);
					track.add(new MidiEvent(message, 103));
					break;
				default:
					break;
				}
			}
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	private MidiEvent[] toDrumNoteEvent(int pitch, int step, int length, int partV, int exp) {
		MidiEvent[] events = new MidiEvent[2];
		try {
			// 16129:127*127
			int v = 127 * BinaryUtil.VolumeValue[partV] * BinaryUtil.VolumeValue[exp] / 16129;
			ShortMessage onMessage = new ShortMessage();
			onMessage.setMessage(ShortMessage.NOTE_ON, DRUM_CHANNEL-1, pitch, v);
			events[0] = new MidiEvent(onMessage, bdxStepToMidiStep(step));
			ShortMessage offMessage = new ShortMessage();
			offMessage.setMessage(ShortMessage.NOTE_OFF, DRUM_CHANNEL-1, pitch, 0);
			events[1] = new MidiEvent(offMessage, bdxStepToMidiStep(step+length)-3);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return events;
	}

	public static long bdxStepToMidiStep(int step) {
		return (long) step * (MIDI_TIME_BASE/BDX.TIME_BASE);
	}

	/**
	 * GMリセット　F0　7E　7F　09　01　F7 6byte
	 * @return
	 * @throws InvalidMidiDataException
	 */
	public static MidiMessage getGMResetMessage() throws InvalidMidiDataException {
		return new SysexMessage(new byte[] {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7}, 6);
	}
	/**
	 * GSリセット　F0 41 20 42 12 40 00 7F 00 41 F7 11byte
	 * @return
	 * @throws InvalidMidiDataException
	 */
	public static MidiMessage getGSResetMessage() throws InvalidMidiDataException {
		return new SysexMessage(new byte[] {(byte)0xF0, 0x41, 0x20, 0x42, 0x12, 0x40, 0x00, 0x7F, 0x00, 0x41, (byte)0xF7}, 11);
	}
	/**
	 * XGリセット　F0 43 10 4C 00 00 7E 00 F7 9byte
	 * @return
	 * @throws InvalidMidiDataException
	 */
	public static MidiMessage getXGResetMessage() throws InvalidMidiDataException {
		return new SysexMessage(new byte[] {(byte)0xF0, 0x43, 0x10, 0x4C, 0x00, 0x00, 0x7E, 0x00, (byte)0xF7}, 9);
	}
	/**
	 * マスターボリューム　F0 7F 7F 04 01 00 ** F7 8byte
	 * @param volume 0~127
	 * @return
	 * @throws InvalidMidiDataException
	 */
	public static MidiMessage getMasterVolumetMessage(byte volume) throws InvalidMidiDataException {
		return new SysexMessage(new byte[] {(byte)0xF0, 0x7F, 0x7F, 0x04, 0x01, 0x00, volume, (byte)0xF7}, 8);
	}
	
}
