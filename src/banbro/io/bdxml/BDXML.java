package banbro.io.bdxml;

import java.util.Arrays;
import java.util.List;

import banbro.io.DefaultXML;
import banbro.model.bdx.BDX;
import banbro.model.bdx.BDXDate;
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
import banbro.model.bdx.PlayLevel;
import banbro.model.bdx.SingleNote;
import banbro.model.bdx.StepValue;
import banbro.model.bdx.Voicing;
import banbro.model.score.Score.Clef;

public class BDXML extends DefaultXML implements BDXMLConstants {
	private static final char[] REPLACE_CHARS = new char[] {
		BinaryUtil.CHAR_NULL,
		BinaryUtil.CHAR_TRADE,
		BinaryUtil.CHAR_REG,
		BinaryUtil.CHAR_COPY,
		'\n'
	};
	static {
		Arrays.sort(REPLACE_CHARS);
	}

	private BDX _bdx;
	private String _software;

	public BDXML(BDX bdx, String software) {
		_bdx = bdx;
		_software = software;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append(header());
		if (_software!=null && _software.length()!=0) {
			sb.append(comment(_software));
		}

		sb.append(startTag(TAG_BDX, false));
		sb.append(attribute(ATT_MY_XML_version, BDXML_VERDION));
		sb.append(CLOSE);
		String value;

		// 曲名
		sb.append(startTag(TAG_Title));
		for (String st : _bdx.getTitleLabels()) {
			sb.append(textTag(TAG_Label, st));
		}
		sb.append(endTag(TAG_Title));

		// Create(作成開始日)
		BDXDate createDate = _bdx.getCreateDate();
		if (createDate!=null) {
			sb.append(startTag(TAG_Create));
			sb.append(toDateXML(createDate));
			sb.append(endTag(TAG_Create));
		}

		// Save(最終更新日)
		BDXDate saveDate = _bdx.getSaveDate();
		if (saveDate!=null) {
			sb.append(startTag(TAG_Save));
			sb.append(toDateXML(saveDate));
			sb.append(endTag(TAG_Save));
		}

		// 受付番号
		long id = _bdx.getSerialId();
		if (id!=0) {
			sb.append(textTag(TAG_SerialID, id));
		}
		// Submitter
		value = _bdx.getSubmitter();
		if (value!=null) {
			sb.append(textTag(TAG_Submitter, value));
		}
		// コメント
		value = _bdx.getComment();
		if (value!=null) {
			sb.append(textTag(TAG_Comment, value));
		}

		// 拍子
		sb.append(textTag(TAG_TimeSignature, _bdx.getTime()));
		// 小節数
		sb.append(textTag(TAG_Bars, _bdx.getBars()));
		// マスターボリューム
		sb.append(textTag(TAG_Master_Volume, _bdx.getMasterVolume()));
		// メロディパート
		int main = _bdx.getMelodyPartNum();
		if (main>=0) {
			sb.append(textTag(TAG_Melody, main));
		}

		// テンポ
		sb.append(toTimingXML(_bdx.getTempo(), TAG_Tempo_Timing, TAG_Tempo));
		// 調
		sb.append(toTimingXML(_bdx.getKey(), TAG_Key_Timing, TAG_Key));

		// 各パート
		sb.append(startTag(TAG_Part_List));
		for (int i=0; i<_bdx.getPartNum(); i++) {
			sb.append(toPartXml(_bdx.getPart(i)));
		}
		sb.append(endTag(TAG_Part_List));

		// 和音パート関連
		Guitar guitar = _bdx.getGuitar();
		Piano piano = _bdx.getPiano();
		if (guitar!=null) {
			// ギター
			sb.append(toGuitarXML(guitar));
		}
		if (piano!=null) {
			// ピアノ
			sb.append(toPianoXML(piano));
		}
		if (guitar!=null || piano!=null) {
			// コード配置
			sb.append(toChordTimingXML(_bdx.getChordTiming()));
		}

		// 歌詞、歌詞割り当て
		sb.append(toLyricXML(_bdx.getLyric(), _bdx.getLyricTiming(), _bdx.isLyrics()));

		sb.append(endTag(TAG_BDX));

		return sb.toString();
	}

	/**
	 * パート
	 * @param part
	 * @return
	 */
	private String toPartXml(Part part) {
		InstrumentType type = part.getType();
		String partTag = "";

		switch (type) {
		case SINGLE:
			partTag = TAG_Single_Part;
			break;
		case DRUMS:
			partTag = TAG_Drum_Part;
			break;
		case GUITAR:
			partTag = TAG_Guitar_Part;
			break;
		case PIANO:
			partTag = TAG_Piano_Part;
			break;
		default:
			return emptyTag(TAG_Empty_Part);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(startTag(partTag, false));

		sb.append(attribute(ATT_part_volume, part.getPartVolume()));
		sb.append(attribute(ATT_pan, part.getPan()));
		sb.append(CLOSE);

		// 楽器関連
		sb.append(startTag(TAG_Inst, false));
		sb.append(attribute(ATT_inst_num, part.getInstrument().getNum()));
		sb.append(attribute(ATT_inst_clone, part.getCloneNum()));
		sb.append(CLOSE);
		// Envelope
		if (type==InstrumentType.SINGLE || type==InstrumentType.GUITAR || type==InstrumentType.PIANO) {
			sb.append(startTag(TAG_Envelope, false));
			sb.append(attribute(ATT_attack, part.getToneAttack()));
			sb.append(attribute(ATT_decay, part.getToneDecay()));
			sb.append(attribute(ATT_sustain, part.getToneSustain()));
			sb.append(attribute(ATT_release, part.getToneRelease()));
			sb.append(END_CLOSE);
		}
		// Vibrato
		if (type==InstrumentType.SINGLE || type==InstrumentType.GUITAR || type==InstrumentType.PIANO) {
			sb.append(startTag(TAG_Vibrato, false));
			sb.append(attribute(ATT_hold, part.getVibratoHold()));
			sb.append(attribute(ATT_delay, part.getVibratoDelay()));
			sb.append(attribute(ATT_depth, part.getVibratoDepth()));
			sb.append(attribute(ATT_speed, part.getVibratoSpeed()));
			sb.append(attribute(ATT_shape, part.getVibratoShape()));
			sb.append(END_CLOSE);
		}
		// Effects
		if (type==InstrumentType.SINGLE) {
			sb.append(startTag(TAG_Effects, false));
			sb.append(attribute(ATT_effects_type, part.getToneEffectType()));
			sb.append(CLOSE);
			sb.append(part.getToneEffectValue());
			sb.append(endTag(TAG_Effects));
		}
		sb.append(endTag(TAG_Inst));

		// 演奏レベル
		sb.append(startTag(TAG_Play_Level, false));
		sb.append(attribute(ATT_level_beginner, part.getPlayLevel(PlayLevel.BEGINNER)));
		sb.append(attribute(ATT_level_amateur, part.getPlayLevel(PlayLevel.AMATEUR)));
		sb.append(attribute(ATT_level_pro, part.getPlayLevel(PlayLevel.PRO)));
		sb.append(attribute(ATT_level_master, part.getPlayLevel(PlayLevel.MASTER)));
		sb.append(END_CLOSE);

		// 楽譜関連
		// clef
		if (type==InstrumentType.SINGLE) {
			sb.append(toTimingXML(part.getClef(), TAG_Clef_Timing, TAG_Clef));
		}
		// volume
		sb.append(toTimingXML(part.getVolume(), TAG_Expression_Timing, TAG_Expression));
		// 音符
		String noteListTag = "";
		switch (type) {
		case SINGLE:
			noteListTag = TAG_Single_Note_List;
			break;
		case DRUMS:
			noteListTag = TAG_Drum_Note_List;
			break;
		case GUITAR:
			noteListTag = TAG_Guitar_Note_List;
			break;
		case PIANO:
			noteListTag = TAG_Piano_Note_List;
			break;
		default:
			throw new IllegalStateException();
		}
		sb.append(startTag(noteListTag));
		List<Note> noteList = part.getNoteList();
		for (int i=0; i<noteList.size(); i++) {
			sb.append(toNoteXml(noteList.get(i), type));
		}
		sb.append(endTag(noteListTag));


		if (type==InstrumentType.SINGLE) {
			// bass
			List<StepValue<Integer>> bass = part.getBass();
			for (StepValue<Integer> sv : bass) {
				sb.append(startTag(TAG_Bass_Note_Timing));
				sb.append(textTag(TAG_Step, sv.getStep()));
				sb.append(toNoteXml(SingleNote.getInstance(sv.getValue()), InstrumentType.SINGLE));
				sb.append(endTag(TAG_Bass_Note_Timing));
			}
			// button
			sb.append(toTimingXML(part.getButton(), TAG_Button_Flag_Timing, TAG_Button_Flag));
		} else if (type==InstrumentType.GUITAR) {
			// コードセット
			int[] steps = part.getChordSetSteps();
			List<StepValue<List<Chord>>> chordSetList = part.getChordSet();
			sb.append(toChordSetXML(steps, chordSetList));
		} else if (type==InstrumentType.PIANO) {
			// コードセット
			int[] steps = part.getChordSetSteps();
			List<StepValue<List<Chord>>> chordSetList = part.getChordSet();
			sb.append(toChordSetXML(steps, chordSetList));
			// ボイシング変化
			List<StepValue<Voicing>> voicing = part.getVoicing();
			for (StepValue<Voicing> sv : voicing) {
				sb.append(startTag(TAG_Voicing_Timing));
				sb.append(textTag(TAG_Step, sv.getStep()));
				sb.append(toVoicingXML(sv.getValue()));
				sb.append(endTag(TAG_Voicing_Timing));
			}
		}

		sb.append(endTag(partTag));

		return sb.toString();
	}

	/**
	 * @param note
	 * @param type
	 * @return
	 */
	private String toNoteXml(Note note, InstrumentType type) {
		StringBuilder sb = new StringBuilder();
		if (type==InstrumentType.SINGLE) {
			sb.append(startTag(TAG_Single_Note, false));
			SingleNote singleNote = (SingleNote) note;
			if (singleNote.isGroup()) {
				sb.append(attribute(ATT_note_group, true));
			} else {
				int n = singleNote.getSingleNoteNum();
				if (n!=SingleNote.REST) {
					sb.append(attribute(ATT_notenum, n));
					if (singleNote.isExtend()) {
						sb.append(attribute(ATT_note_extend, true));
					}
				}
			}
			sb.append(END_CLOSE);
		} else if (type==InstrumentType.DRUMS) {
			DrumNote drumNote = (DrumNote) note;
			sb.append(startTag(TAG_Drum_Note, false));
			int upper = drumNote.getUpperNoteNum();
			int lower = drumNote.getLowerNoteNum();
			if (upper!=DrumNote.REST) {
				sb.append(attribute(ATT_drum_upper_note, upper));
			}
			if (lower!=DrumNote.REST) {
				sb.append(attribute(ATT_drum_lower_note, lower));
			}
			sb.append(END_CLOSE);
		} else if (type==InstrumentType.GUITAR) {
			GuitarNote guitarNote = (GuitarNote) note;
			sb.append(startTag(TAG_Guitar_Note, false));
			if (guitarNote.isGroup()) {
				sb.append(attribute(ATT_note_group, true));
			} else {
				int buttonNum = guitarNote.getButtonNum();
				int stroke = guitarNote.getStroke().getValue();
				if (buttonNum!=SingleNote.REST) {
					sb.append(attribute(ATT_guitar_button, buttonNum));
					sb.append(attribute(ATT_guitar_stroke, stroke));
					if (guitarNote.isExtend()) {
						sb.append(attribute(ATT_note_extend, true));
					}
				}
			}
			sb.append(END_CLOSE);
		} else if (type==InstrumentType.PIANO) {
			sb.append(startTag(TAG_Piano_Note, false));
			PianoNote pianoNote = (PianoNote) note;
			if (pianoNote.isGroup()) {
				sb.append(attribute(ATT_note_group, true));
			} else {
				int n = pianoNote.getSingleNoteNum();
				if (n!=SingleNote.REST) {
					sb.append(attribute(ATT_notenum, n));
					if (pianoNote.isExtend()) {
						sb.append(attribute(ATT_note_extend, true));
					}
				}
			}
			sb.append(END_CLOSE);
		}

		return sb.toString();
	}

	/**
	 * @param chord
	 * @return
	 */
	private String toChordXml(Chord chord) {
		StringBuilder sb = new StringBuilder();
		sb.append(startTag(TAG_Chord, false));
		if (chord instanceof BaseChord) {
			sb.append(attribute(ATT_chord_root, ((BaseChord) chord).getRoot()));
			sb.append(attribute(ATT_chord_flag, ((BaseChord) chord).getAccidental()));
			sb.append(attribute(ATT_chord_name, ((BaseChord) chord).getName()));
		} else if (chord instanceof OriginalChord) {
			sb.append(attribute(ATT_chord_root, 0xF));
			sb.append(attribute(ATT_chord_flag, 0xF));
			sb.append(attribute(ATT_chord_name, ((OriginalChord) chord).getNum()));
		}
		sb.append(CLOSE);
		sb.append(chord.toString());
		sb.append(endTag(TAG_Chord));
		return sb.toString();
	}

	/**
	 * @param chord
	 * @return
	 */
	private String toGuitarChordXml(GuitarChord chord) {
		StringBuilder sb = new StringBuilder();
		sb.append(startTag(TAG_Guitar_Chord));
		int[] fret = chord.getFret();
		boolean[] mute = chord.getMute();
		assert(fret.length==mute.length);
		for (int i=0; i<fret.length; i++) {
			sb.append(startTag(TAG_Guitar_Fret, false));
			sb.append(attribute(ATT_guitar_fret_num, fret[i]));
			sb.append(attribute(ATT_guitar_fret_mute, mute[i]));
			sb.append(END_CLOSE);
		}
		sb.append(endTag(TAG_Guitar_Chord));
		return sb.toString();
	}

	/**
	 * @param chord
	 * @return
	 */
	private String toPianoChordXml(PianoChord chord) {
		StringBuilder sb = new StringBuilder();
		sb.append(startTag(TAG_Piano_Chord));
		List<SingleNote> notes = chord.getNotes();
		for (int i=0; i<notes.size(); i++) {
			sb.append(toNoteXml(notes.get(i), InstrumentType.SINGLE));
		}
		sb.append(endTag(TAG_Piano_Chord));
		return sb.toString();
	}

	/**
	 * @param guitar
	 * @return
	 */
	private String toGuitarXML(Guitar guitar){
		StringBuilder sb = new StringBuilder();
		sb.append(startTag(TAG_Guitar));

		// オリジナルコード
		sb.append(startTag(TAG_Guitar_Original_Chord_List));
		List<GuitarChord> original = guitar.getOriginalChords();
		for (int i=0; i<original.size(); i++) {
			GuitarChord gc = original.get(i);
			sb.append(startTag(TAG_Guitar_Original_Chord, false));
			String name = gc.getChordName();
			if (name!=null) {
				sb.append(attribute(ATT_original_chord_name, name));
			}
			sb.append(attribute(ATT_guitar_original_chord_used, gc.isUsed()));
			sb.append(CLOSE);
			sb.append(toGuitarChordXml(gc));
			sb.append(endTag(TAG_Guitar_Original_Chord));
		}
		sb.append(endTag(TAG_Guitar_Original_Chord_List));

		sb.append(endTag(TAG_Guitar));
		return sb.toString();
	}

	private String toChordSetXML(int[] steps, List<StepValue<List<Chord>>> chordSetList) {
		assert(steps.length==chordSetList.size());
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<steps.length; i++) {
			sb.append(startTag(TAG_Chord_Set_Timing));
			sb.append(textTag(TAG_Step, steps[i]));
			sb.append(startTag(TAG_Chord_Set));
			List<Chord> chords = chordSetList.get(i).getValue();
			for (int j=0; j<chords.size(); j++) {
				sb.append(toChordXml(chords.get(j)));
			}
			sb.append(endTag(TAG_Chord_Set));
			sb.append(endTag(TAG_Chord_Set_Timing));
		}
		return sb.toString();
	}

	/**
	 * @param piano
	 * @return
	 */
	private String toPianoXML(Piano piano){
		StringBuilder sb = new StringBuilder();
		sb.append(startTag(TAG_Piano));

		// ボイシング
		sb.append(toVoicingXML(piano.getVoicing()));

		// オリジナルコード
		sb.append(startTag(TAG_Piano_Original_Chord_List));
		List<PianoChord> original = piano.getOriginalChords();
		for (int i=0; i<original.size(); i++) {
			PianoChord pc = original.get(i);
			sb.append(startTag(TAG_Piano_Original_Chord, false));
			String name = pc.getChordName();
			if (name!=null) {
				sb.append(attribute(ATT_original_chord_name, name));
			}
			sb.append(attribute(ATT_piano_original_chord_used, pc.isUsed()));
			sb.append(CLOSE);
			sb.append(toPianoChordXml(pc));
			sb.append(endTag(TAG_Piano_Original_Chord));
		}
		sb.append(endTag(TAG_Piano_Original_Chord_List));

		sb.append(endTag(TAG_Piano));
		return sb.toString();
	}

	/**
	 * @param date
	 * @return
	 */
	private String toDateXML(BDXDate date){
		StringBuilder sb = new StringBuilder();
		sb.append(startTag(TAG_Date, false));
		sb.append(attribute(ATT_year, date.getYear()));
		sb.append(attribute(ATT_month, date.getMonth()));
		sb.append(attribute(ATT_day, date.getDay()));
		sb.append(END_CLOSE);
		return sb.toString();
	}

	/**
	 * @param lyric
	 * @param timing
	 * @return
	 */
	private String toLyricXML(List<String> lyric, List<StepValue<String>> timing, boolean used){
		StringBuilder sb = new StringBuilder();
		sb.append(startTag(TAG_Lyric, false));
		sb.append(attribute(ATT_lyric_used, used));
		sb.append(CLOSE);

		sb.append(startTag(TAG_Lyric_Page_List));
		for (String page : lyric) {
			sb.append(textTag(TAG_Lyric_Page, page));
		}
		sb.append(endTag(TAG_Lyric_Page_List));

		// 歌詞割り当て
		sb.append(startTag(TAG_Lyric_Timing_List));
		sb.append(toTimingXML(timing, TAG_Lyric_Timing, TAG_Lyric_String));
		sb.append(endTag(TAG_Lyric_Timing_List));

		sb.append(endTag(TAG_Lyric));
		return sb.toString();
	}

	private <V> String toTimingXML(List<StepValue<V>> timing, String tag1, String tag2) {
		StringBuilder sb = new StringBuilder();
		for (StepValue<?> sv : timing) {
			sb.append(startTag(tag1));
			sb.append(textTag(TAG_Step, sv.getStep()));
			sb.append(textTag(tag2, timingValueToString(sv.getValue())));
			sb.append(endTag(tag1));
		}
		return sb.toString();
	}

	private String timingValueToString(Object value) {
		if (value instanceof Clef) {
			return String.valueOf(((Clef) value).getValue());
		}
		return value.toString();
	}

	/**
	 * @param timing
	 * @return
	 */
	private String toChordTimingXML(List<StepValue<Chord>> timing){
		StringBuilder sb = new StringBuilder();
		for (StepValue<Chord> sv : timing) {
			sb.append(startTag(TAG_Chord_Timing));
			sb.append(textTag(TAG_Step, sv.getStep()));
			sb.append(toChordXml(sv.getValue()));
			sb.append(endTag(TAG_Chord_Timing));
		}
		return sb.toString();
	}

	/**
	 * @param top
	 * @param notes
	 * @param spread
	 * @return
	 */
	private String toVoicingXML(Voicing v){
		StringBuilder sb = new StringBuilder();
		int top = v.getTopNote();
		int notes = v.getNotes();
		int spread = v.getSpread().getValue();
		sb.append(startTag(TAG_Voicing));
		sb.append(startTag(TAG_Voicing_TopNote));
		sb.append(toNoteXml(SingleNote.getInstance(top), InstrumentType.SINGLE));
		sb.append(endTag(TAG_Voicing_TopNote));
		sb.append(textTag(TAG_Voicing_Notes, notes));
		sb.append(textTag(TAG_Voicing_Spread, spread));
		sb.append(endTag(TAG_Voicing));
		return sb.toString();
	}

	@Override
	protected String replaceText(String st) {
		String ret = super.replaceText(st);
		StringBuilder sb = new StringBuilder();
		for (char c : ret.toCharArray()) {
			if (Arrays.binarySearch(REPLACE_CHARS, c)>=0) {
				sb.append(startTag(TAG_Char, false));
				sb.append(attribute(ATT_code, Integer.toHexString((int)c)));
				sb.append(END_CLOSE);
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
