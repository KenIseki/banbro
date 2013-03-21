package banbro.io.bdxml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import banbro.io.DefaultParser;
import banbro.io.DefaultXML;
import banbro.model.Clef;
import banbro.model.Stroke;
import banbro.model.bdx.BDX;
import banbro.model.bdx.BDXDate;
import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BinaryUtil;
import banbro.model.bdx.Chord;
import banbro.model.bdx.DrumNote;
import banbro.model.bdx.EffectType;
import banbro.model.bdx.Guitar;
import banbro.model.bdx.GuitarChord;
import banbro.model.bdx.GuitarNote;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Part;
import banbro.model.bdx.Piano;
import banbro.model.bdx.PianoChord;
import banbro.model.bdx.PianoNote;
import banbro.model.bdx.PlayLevel;
import banbro.model.bdx.SingleNote;
import banbro.model.bdx.StepValue;
import banbro.model.bdx.VibratoShape;
import banbro.model.bdx.Voicing;
import banbro.model.bdx.Voicing.Spread;

class BDXMLParser extends DefaultParser implements BDXMLConstants {

	protected BDX _bdx;
	protected int _mejVer;
	private int _step;
	private int _chordNum;
	private List<StepValue<Voicing>> _voiList;

	public BDXMLParser() {
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		_bdx = null;
		_mejVer = 0;
		_step = 0;
		_chordNum = 0;
		_voiList = new ArrayList<StepValue<Voicing>>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals(TAG_Char)) {
			int code = 0;
			try {
				code = Integer.parseInt(attributes.getValue(ATT_code), 16);
			} catch (NumberFormatException e) {
			}
			addText(String.valueOf(Character.toChars(code)));
		} else {
			super.startElement(uri, localName, qName, attributes);
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals(TAG_Char)) {
			return;
		}
		super.endElement(uri, localName, qName);
	}

	@Override
	protected Object createCurrentObject(String name, Attributes attributes, String parentTag, Object parentObj) {
		if (name.equals(TAG_Single_Note)) {
			String noteNumSt = attributes.getValue(ATT_notenum);
			int noteNum = SingleNote.REST;
			if (noteNumSt!=null) {
				noteNum = Integer.parseInt(noteNumSt);
			}
			if (noteNum!=SingleNote.REST) {
				String extendSt = attributes.getValue(ATT_note_extend);
				if (extendSt!=null && Boolean.parseBoolean(extendSt)) {
					if (noteNum==127) {
						noteNum = SingleNote.EXTEND;
					} else {
						noteNum += SingleNote.EXTEND;
					}
				}
			} else {
				String groupSt = attributes.getValue(ATT_note_group);
				if (groupSt!=null && Boolean.parseBoolean(groupSt)) {
					noteNum = SingleNote.GROUP;
				}
			}
			SingleNote note = SingleNote.getInstance(noteNum);
			if (parentTag.equals(TAG_Single_Note_List)) {
				Part p = (Part) parentObj;
				if (p.getType()==InstrumentType.SINGLE) {
					p.addNote(note);
				}
			} else if (parentTag.equals(TAG_Bass_Note_Timing)) {
				Part p = (Part) parentObj;
				if (p.getType()==InstrumentType.SINGLE) {
					p.addBass(_step, note.getSingleNoteNum());
				}
				_step = 0;
			} else if (parentTag.equals(TAG_Piano_Chord)) {
				PianoChord chord = (PianoChord) parentObj;
				chord.addNote(note);
			} else if (parentTag.equals(TAG_Voicing_TopNote)) {
				Voicing v = (Voicing) parentObj;
				v.setTopNote(note.getSingleNoteNum());
			}
		} else if (name.equals(TAG_Drum_Note)) {
			String upperSt = attributes.getValue(ATT_drum_upper_note);
			String lowerSt = attributes.getValue(ATT_drum_lower_note);
			int upper = DrumNote.REST;
			int lower = DrumNote.REST;
			if (upperSt!=null) {
				upper = Integer.parseInt(upperSt);
			}
			if (lowerSt!=null) {
				lower = Integer.parseInt(lowerSt);
			}
			DrumNote note = DrumNote.getInstanceWithNote(upper, lower);
			if (parentTag.equals(TAG_Drum_Note_List)) {
				Part p = (Part) parentObj;
				if (p.getType()==InstrumentType.DRUMS) {
					p.addNote(note);
				}
			}
		} else if (name.equals(TAG_Guitar_Note)) {
			String buttonSt = attributes.getValue(ATT_guitar_button);
			String strokeSt = attributes.getValue(ATT_guitar_stroke);
			String extendSt = attributes.getValue(ATT_note_extend);
			String groupSt = attributes.getValue(ATT_note_group);
			GuitarNote note = GuitarNote.getInstance(SingleNote.REST);
			boolean isExtend = false;
			if (extendSt!=null) {
				isExtend = Boolean.parseBoolean(extendSt);
			}
			if (buttonSt!=null && strokeSt!=null) {
				note = GuitarNote.getInstance(
						Integer.parseInt(buttonSt),
						Stroke.valueOf(Integer.parseInt(strokeSt)),
						isExtend);
			} else if (isExtend) {
				note = GuitarNote.getInstance(GuitarNote.EXTEND);
			}
			if (groupSt!=null && Boolean.parseBoolean(groupSt)) {
				note = GuitarNote.getInstance(GuitarNote.GROUP);
			}
			if (parentTag.equals(TAG_Guitar_Note_List)) {
				Part p = (Part) parentObj;
				if (p.getType()==InstrumentType.GUITAR) {
					p.addNote(note);
				}
			}
		} else if (name.equals(TAG_Piano_Note)) {
			String noteNumSt = attributes.getValue(ATT_notenum);
			int noteNum = SingleNote.REST;
			if (noteNumSt!=null) {
				noteNum = Integer.parseInt(noteNumSt);
			}
			if (noteNum!=SingleNote.REST) {
				String extendSt = attributes.getValue(ATT_note_extend);
				if (extendSt!=null && Boolean.parseBoolean(extendSt)) {
					if (noteNum==127) {
						noteNum = SingleNote.EXTEND;
					} else {
						noteNum += SingleNote.EXTEND;
					}
				}
			} else {
				String groupSt = attributes.getValue(ATT_note_group);
				if (groupSt!=null && Boolean.parseBoolean(groupSt)) {
					noteNum = SingleNote.GROUP;
				}
			}
			PianoNote note = PianoNote.getInstance(noteNum);
			if (parentTag.equals(TAG_Piano_Note_List)) {
				Part p = (Part) parentObj;
				if (p.getType()==InstrumentType.PIANO) {
					p.addNote(note);
				}
			}
		} else if (name.equals(TAG_Chord)) {
			int root = Integer.parseInt(attributes.getValue(ATT_chord_root));
			int flag = Integer.parseInt(attributes.getValue(ATT_chord_flag));
			int cName = Integer.parseInt(attributes.getValue(ATT_chord_name));
			Chord chord = BinaryUtil.createChord(root, flag, cName);
			if (parentTag.equals(TAG_Chord_Timing)) {
				_bdx.addChordTiming(_step, chord);
				_step = 0;
			} else if (parentTag.equals(TAG_Chord_Set)) {
				if (parentObj instanceof Part) {
					Part p = (Part) parentObj;
					if (p.getType()==InstrumentType.GUITAR) {
						p.addChord(_step, chord);
					} else if (p.getType()==InstrumentType.PIANO) {
						p.addChord(_step, chord);
					}
				}
			}
		} else if (name.equals(TAG_Guitar_Fret)) {
			GuitarChord chord = (GuitarChord) parentObj;
			int num = Integer.parseInt(attributes.getValue(ATT_guitar_fret_num));
			boolean mute = Boolean.parseBoolean(attributes.getValue(ATT_guitar_fret_mute));
			chord.setFret(_chordNum, num);
			chord.setMute(_chordNum, mute);
			_chordNum--;
		} else if (name.equals(TAG_Guitar_Original_Chord)) {
			Guitar guitar = (Guitar) parentObj;
			GuitarChord original = new GuitarChord();
			original.setUsed(Boolean.parseBoolean(attributes.getValue(ATT_guitar_original_chord_used)));
			guitar.addOriginalChord(original);
			return original;
		} else if (name.equals(TAG_Piano_Original_Chord)) {
			Piano piano = (Piano) parentObj;
			PianoChord original = new PianoChord();
			original.setUsed(Boolean.parseBoolean(attributes.getValue(ATT_piano_original_chord_used)));
			piano.addOriginalChord(original);
			return original;
		} else if (name.equals(TAG_Guitar_Chord)) {
			_chordNum = 6;
//		} else if (name.equals(TAG_Piano_Chord)) {
		} else if (name.equals(TAG_Voicing)) {
			Voicing v = new Voicing();
			return v;
		} else if (name.equals(TAG_Inst)) {
			Part p = (Part) parentObj;
			String value;
			value = attributes.getValue(ATT_inst_num);
			try {
				p.setInstrument(BDXInstrument.valueOf(Integer.parseInt(value)));
			} catch (NumberFormatException e) {
			}
			value = attributes.getValue(ATT_inst_clone);
			try {
				p.setCloneNum(Integer.parseInt(value));
			} catch (NumberFormatException e) {
			}
		} else if (name.equals(TAG_Play_Level)) {
			Part p = (Part) parentObj;
			int beginner = Integer.parseInt(attributes.getValue(ATT_level_beginner));
			int amateur = Integer.parseInt(attributes.getValue(ATT_level_amateur));
			int pro = Integer.parseInt(attributes.getValue(ATT_level_pro));
			int master = Integer.parseInt(attributes.getValue(ATT_level_master));
			p.setPlayLevel(PlayLevel.BEGINNER, beginner);
			p.setPlayLevel(PlayLevel.AMATEUR, amateur);
			p.setPlayLevel(PlayLevel.PRO, pro);
			p.setPlayLevel(PlayLevel.MASTER, master);
		} else if (name.equals(TAG_Single_Part)) {
			Part p = new Part(InstrumentType.SINGLE);
			String value;
			value = attributes.getValue(ATT_pan);
			if (value!=null) {
				p.setPan(Integer.parseInt(value));
			}
			value = attributes.getValue(ATT_part_volume);
			if (value!=null) {
				p.setPartVolume(Integer.parseInt(value));
			}
			_bdx.addPart(p);
			return p;
		} else if (name.equals(TAG_Drum_Part)) {
			Part p = new Part(InstrumentType.DRUMS);
			String value;
			value = attributes.getValue(ATT_part_volume);
			if (value!=null) {
				p.setPartVolume(Integer.parseInt(value));
			}
			_bdx.addPart(p);
			return p;
		} else if (name.equals(TAG_Guitar_Part)) {
			Part p = new Part(InstrumentType.GUITAR);
			String value;
			value = attributes.getValue(ATT_pan);
			if (value!=null) {
				p.setPan(Integer.parseInt(value));
			}
			value = attributes.getValue(ATT_part_volume);
			if (value!=null) {
				p.setPartVolume(Integer.parseInt(value));
			}
			_bdx.addPart(p);
			return p;
		} else if (name.equals(TAG_Piano_Part)) {
			Part p = new Part(InstrumentType.PIANO);
			String value;
			value = attributes.getValue(ATT_pan);
			if (value!=null) {
				p.setPan(Integer.parseInt(value));
			}
			value = attributes.getValue(ATT_part_volume);
			if (value!=null) {
				p.setPartVolume(Integer.parseInt(value));
			}
			_bdx.addPart(p);
			return p;
		} else if (name.equals(TAG_Empty_Part)) {
			Part p = Part.NULL_PART;
			_bdx.addPart(p);
			return p;
		} else if (name.equals(TAG_Envelope)) {
			Part p = (Part) parentObj;
			String value;
			value = attributes.getValue(ATT_attack);
			if (value!=null) {
				p.setToneAttack(Integer.parseInt(value));
			}
			value = attributes.getValue(ATT_decay);
			if (value!=null) {
				p.setToneDecay(Integer.parseInt(value));
			}
			value = attributes.getValue(ATT_sustain);
			if (value!=null) {
				p.setToneSustain(Integer.parseInt(value));
			}
			value = attributes.getValue(ATT_release);
			if (value!=null) {
				p.setToneRelease(Integer.parseInt(value));
			}
		} else if (name.equals(TAG_Vibrato)) {
			Part p = (Part) parentObj;
			String value;
			value = attributes.getValue(ATT_hold);
			if (value!=null) {
				p.setVibratoHold(Integer.parseInt(value));
			}
			value = attributes.getValue(ATT_delay);
			if (value!=null) {
				p.setVibratoDelay(Integer.parseInt(value));
			}
			value = attributes.getValue(ATT_depth);
			if (value!=null) {
				p.setVibratoDepth(Integer.parseInt(value));
			}
			value = attributes.getValue(ATT_speed);
			if (value!=null) {
				p.setVibratoSpeed(Integer.parseInt(value));
			}
			value = attributes.getValue(ATT_shape);
			if (value!=null) {
				p.setVibratoShape(VibratoShape.valueOf(Integer.parseInt(value)));
			}
		} else if (name.equals(TAG_Effects)) {
			Part p = (Part) parentObj;
			String value = attributes.getValue(ATT_effects_type);
			if (value!=null) {
				p.setToneEffectType(EffectType.valueOf(Integer.parseInt(value)));
			}
		} else if (name.equals(TAG_Voicing_Timing)) {
			_step = 0;
			Voicing voicing = new Voicing();
			return voicing;
		} else if (name.equals(TAG_Date)) {
			String value;
			value = attributes.getValue(ATT_year);
			int year = Integer.parseInt(value);
			value = attributes.getValue(ATT_month);
			int month = Integer.parseInt(value);
			value = attributes.getValue(ATT_day);
			int day = Integer.parseInt(value);
			BDXDate date = new BDXDate(year, month, day);
			if (parentTag.equals(TAG_Create)) {
				_bdx.setCreateDate(date);
			} else if (parentTag.equals(TAG_Save)) {
				_bdx.setSaveDate(date);
			}
		} else if (name.equals(TAG_Guitar)) {
			Guitar guitar = new Guitar();
			_bdx.setGuitar(guitar);
			return guitar;
		} else if (name.equals(TAG_Piano)) {
			Piano piano = new Piano();
			_bdx.setPiano(piano);
			return piano;
		} else if (name.equals(TAG_Lyric)) {
			_bdx.setLyrics(Boolean.parseBoolean(attributes.getValue(ATT_lyric_used)));
		} else if (name.equals(TAG_BDX)) {
			String version = attributes.getValue(DefaultXML.ATT_MY_XML_version);
			_mejVer = 0;
			if (version!=null) {
				String[] ver = version.split(".");
				if (ver.length!=0) {
					try {
						_mejVer = Integer.parseInt(ver[0]);
					} catch (NumberFormatException e) {
					}
				}
			}
			_bdx = new BDX(_mejVer);
			return _bdx;
		}
		return super.createCurrentObject(name, attributes, parentTag, parentObj);
	}

	@Override
	protected void updateCurrentObject(String parentTag, String tag, String text, Object parentObj, Object obj) {
		if (_mejVer<=1 && text!=null) {  // Version 2 ‚æ‚è‘O‚Ì“ÁŽê•¶Žš‚ð•ÏŠ·‚·‚é
			text = text.replaceAll("\\(LF\\)", "\n");
			text = text.replaceAll("\\(SPC\\)", "@");
			text = text.replaceAll("\\(TM\\)", "\u2122");
			text = text.replaceAll("\\(R\\)", "\u00ae");
			text = text.replaceAll("\\(C\\)", "\u00a9");
		}
		if (tag.equals(TAG_Step)) {
			_step = Integer.parseInt(text);
		} else if (tag.equals(TAG_Clef)) {
			Part p = (Part) obj;
			p.addClef(_step, Clef.valueOf(Integer.parseInt(text)));
			_step = 0;
		} else if (tag.equals(TAG_Expression)) {
			Part p = (Part) obj;
			p.addVolume(_step, Integer.parseInt(text));
			_step = 0;
		} else if (tag.equals(TAG_Button_Flag)) {
			Part p = (Part) obj;
			p.addButton(_step, Integer.parseInt(text));
			_step = 0;
		} else if (tag.equals(TAG_Effects)) {
			Part p = (Part) obj;
			p.setToneEffectValue(Integer.parseInt(text));
		} else if (tag.equals(TAG_Voicing_Notes)) {
			Voicing v = (Voicing) obj;
			v.setNotes(Integer.parseInt(text));
		} else if (tag.equals(TAG_Voicing_Spread)) {
			Voicing v = (Voicing) obj;
			int i = Integer.parseInt(text);
			v.setSpread(Spread.valueOf(i));
		} else if (tag.equals(TAG_Piano_Part)) {
			Part p = (Part) obj;
			if (p.getType()==InstrumentType.PIANO) {
				for (int i=0; i<_voiList.size(); i++) {
					StepValue<Voicing> sv = _voiList.get(i);
					p.addVoicing(sv.getStep(), sv.getValue());
				}
			}
			_voiList.clear();
		} else if (tag.equals(TAG_Chord_Set_Timing)) {
			_step = 0;
		} else if (tag.equals(TAG_Voicing)) {
			Voicing v = (Voicing) obj;
			if (parentTag.equals(TAG_Voicing_Timing)) {
				_voiList.add(new StepValue<Voicing>(_step, v));
				_step = 0;
			} else if (parentTag.equals(TAG_Piano)) {
				Piano piano = (Piano) parentObj;
				piano.setVoicing(v);
			}
		} else if (tag.equals(TAG_Lyric_String)) {
			_bdx.addLyricTiming(_step, text);
			_step = 0;
		} else if (tag.equals(TAG_Tempo)) {
			_bdx.addTempo(_step, Integer.parseInt(text));
			_step = 0;
		} else if (tag.equals(TAG_Key)) {
			_bdx.addKey(_step, Integer.parseInt(text));
			_step = 0;
		} else if (tag.equals(TAG_Lyric_Page)) {
			_bdx.addLyric(text);
		} else if (tag.equals(TAG_Label)) {
			_bdx.addTitle(text);
		} else if (tag.equals(TAG_Melody)) {
			_bdx.setMelodyPartNum(Integer.parseInt(text));
		} else if (tag.equals(TAG_Master_Volume)) {
			_bdx.setMasterVolume(Integer.parseInt(text));
		} else if (tag.equals(TAG_SerialID)) {
			_bdx.setSerialId(Long.parseLong(text));
		} else if (tag.equals(TAG_Bars)) {
			_bdx.setBars(Integer.parseInt(text));
		} else if (tag.equals(TAG_TimeSignature)) {
			_bdx.setTime(Integer.parseInt(text));
		} else if (tag.equals(TAG_Submitter)) {
			_bdx.setSubmitter(text);
		} else if (tag.equals(TAG_Comment)) {
			_bdx.setComment(text);
		}
	}

	public BDX getBdx() {
		return _bdx;
	}

}
