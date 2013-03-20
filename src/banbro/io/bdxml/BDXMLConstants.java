package banbro.io.bdxml;

/**
 * BDXML�̃^�O�Ƒ���
 */
public interface BDXMLConstants {

	public static final String BDXML_VERDION = "2.0";


	public static final String TAG_BDX = "BandBrothersDX";

	public static final String TAG_TimeSignature = "TimeSignature";
	public static final String TAG_Bars = "Bars";
	public static final String TAG_Master_Volume = "MasterVolume";
	public static final String TAG_Melody = "Melody";
	public static final String TAG_Title = "Title";
	public static final String TAG_Label = "Label";  // Title�̎q�v�f
	public static final String TAG_Create = "Create";
	public static final String TAG_Save = "Save";
	public static final String TAG_Submitter = "Submitter";
	public static final String TAG_Comment = "Comment";
	public static final String TAG_SerialID = "SerialID";
	
	// �p�[�g
	public static final String TAG_Empty_Part = "EmptyPart";
	public static final String TAG_Single_Part = "SinglePart";
	public static final String TAG_Guitar_Part = "GuitarPart";
	public static final String TAG_Piano_Part = "PianoPart";
	public static final String TAG_Drum_Part = "DrumPart";
	public static final String ATT_pan = "panpot";
	public static final String ATT_part_volume = "volume";
	// �y��
	public static final String TAG_Inst = "Instrument";
	public static final String ATT_inst_num = "number";
	public static final String ATT_inst_clone = "clone";
	public static final String TAG_Envelope = "Envelope";  //Envelope
	public static final String ATT_attack = "attack";
	public static final String ATT_decay = "decay";
	public static final String ATT_sustain = "sustain";
	public static final String ATT_release = "release";
	public static final String TAG_Vibrato = "Vibrato";  //Vibrato
	public static final String ATT_hold = "hold";
	public static final String ATT_delay = "delay";
	public static final String ATT_depth = "depth";
	public static final String ATT_speed = "speed";
	public static final String ATT_shape = "shape";
	public static final String TAG_Effects = "Effects";  //Effects
	public static final String ATT_effects_type = "type";

	// �y��
	public static final String TAG_Clef_Timing = "ClefTiming";
	public static final String TAG_Clef = "Clef";
	public static final String TAG_Expression_Timing = "ExpressionTiming";
	public static final String TAG_Expression = "Expression";
	

	public static final String TAG_Bass_Note_Timing = "BassNoteTiming";
	public static final String TAG_Button_Flag_Timing = "ButtonFlagTiming";
	public static final String TAG_Button_Flag = "ButtonFlag";

	public static final String TAG_Play_Level = "PlayLevel";
	public static final String ATT_level_beginner = "beginner";
	public static final String ATT_level_amateur = "amateur";
	public static final String ATT_level_pro = "pro";
	public static final String ATT_level_master = "master";

	// SingleNote
	public static final String TAG_Single_Note = "SingleNote";
	public static final String ATT_notenum = "n";

	// DrumNote
	public static final String TAG_Drum_Note = "DrumNote";
	public static final String ATT_drum_upper_note = "upper";
	public static final String ATT_drum_lower_note = "lower";

	// GuitarNote
	public static final String TAG_Guitar_Note = "GuitarNote";
	public static final String ATT_guitar_button = "button";
	public static final String ATT_guitar_stroke = "stroke";

	// PianoNote ������SingleNote�Ɠ���
	public static final String TAG_Piano_Note = "PianoNote";

	// Note����
	public static final String ATT_note_extend = "extend";
	public static final String ATT_note_group = "group";

	// �̎�
	public static final String TAG_Lyric = "Lyric";
	public static final String ATT_lyric_used = "used";
	public static final String TAG_Lyric_Page = "LyricPage";
	public static final String TAG_Lyric_Timing = "LyricTiming";
	public static final String TAG_Lyric_String = "LyricString";

	// �e���|
	public static final String TAG_Tempo_Timing = "TempoTiming";
	public static final String TAG_Tempo = "Tempo";
	// ��
	public static final String TAG_Key_Timing = "KeyTiming";
	public static final String TAG_Key = "Key";
	
	// �R�[�h�z�u
	public static final String TAG_Chord_Timing = "ChordTiming";
	// �R�[�h��
	public static final String TAG_Chord = "Chord";
	public static final String ATT_chord_root = "root";
	public static final String ATT_chord_flag = "flag";
	public static final String ATT_chord_name = "name";

	// �R�[�h�p�[�g
	public static final String TAG_Chord_Set_Timing = "ChordSetTiming";
	public static final String TAG_Chord_Set = "ChordSet";

	// �M�^�[�p�[�g�֘A
	public static final String TAG_Guitar = "Guitar";
	// �M�^�[�I���W�i���R�[�h
	public static final String TAG_Guitar_Original_Chord = "GuitarOriginalChord";
	public static final String ATT_guitar_original_chord_used = "used";
	public static final String TAG_Guitar_Chord = "GuitarChord";
	public static final String TAG_Guitar_Fret = "Fret";
	public static final String ATT_guitar_fret_num = "num";
	public static final String ATT_guitar_fret_mute = "mute";


	// �s�A�m�p�[�g�֘A
	public static final String TAG_Piano = "Piano";
	public static final String TAG_Voicing_Timing = "VoicingTiming";
	public static final String TAG_Voicing = "Voicing";
	public static final String TAG_Voicing_TopNote = "VoicingTopNote";
	public static final String TAG_Voicing_Notes = "VoicingNotes";
	public static final String TAG_Voicing_Spread = "VoicingSpread";
	// �s�A�m�I���W�i���R�[�h
	public static final String TAG_Piano_Original_Chord = "PianoOriginalChord";
	public static final String ATT_piano_original_chord_used = "used";
	public static final String TAG_Piano_Chord = "PianoChord";

	public static final String ATT_original_chord_name = "name";

	// Timing�֘A
	public static final String TAG_Step = "Step";
	
	// ���t
	public static final String TAG_Date = "BDXDate";
	public static final String ATT_year = "year";
	public static final String ATT_month = "month";
	public static final String ATT_day = "day";

	// ���X�g
	public static final String TAG_Part_List = "PartList";
	public static final String TAG_Single_Note_List = "SingleNoteList";
	public static final String TAG_Drum_Note_List = "DrumNoteList";
	public static final String TAG_Guitar_Note_List = "GuitarNoteList";
	public static final String TAG_Piano_Note_List = "PianoNoteList";
	public static final String TAG_Guitar_Original_Chord_List = "GuitarOriginalChordList";
	public static final String TAG_Piano_Original_Chord_List = "PianoOriginalChordList";
	public static final String TAG_Lyric_Page_List = "LyricPageList";
	public static final String TAG_Lyric_Timing_List = "LyricTimingList";

	// ���ꕶ��
	public static final String TAG_Char = "Char";
	public static final String ATT_code = "code";

}
