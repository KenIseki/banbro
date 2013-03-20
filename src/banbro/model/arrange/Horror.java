package banbro.model.arrange;

import java.util.List;

import banbro.model.bdx.BDX;
import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BaseChord;
import banbro.model.bdx.BinaryUtil;
import banbro.model.bdx.Chord;
import banbro.model.bdx.EffectType;
import banbro.model.bdx.Guitar;
import banbro.model.bdx.GuitarChord;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Part;
import banbro.model.bdx.Piano;
import banbro.model.bdx.PianoChord;
import banbro.model.bdx.SingleNote;
import banbro.model.bdx.StepValue;
import banbro.model.bdx.VibratoShape;
import banbro.model.score.Score.Accidental;
import banbro.model.score.Score.Pitch;

public class Horror extends ChangeKey {

	public Horror() {
		super();
	}
	public Horror(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	protected void arrangePart(Part p) {
		if (p.getType()==InstrumentType.NONE) {
			return;
		}
		if (p.getType()==InstrumentType.DRUMS) {
			p.setInstrument(BDXInstrument.NONE);
		} else {
			p.setToneAttack(4);
			p.setToneRelease(4);
			if (p.getVibratoShape()==VibratoShape.NONE) {
				p.setVibratoShape(VibratoShape.SIN);
				p.setVibratoHold(9);
				p.setVibratoDelay(9);
				p.setVibratoDepth(9);
				p.setVibratoSpeed(9);
			} else {
				p.setVibratoDepth(Math.max(p.getVibratoDepth(), 9));
			}
			if (p.getType()==InstrumentType.SINGLE) {
				p.setToneEffectType(EffectType.ECHO);
				p.setToneEffectValue(20);
			}
		}
	}

	@Override
	protected int calcNewNote(int n) {
		switch (n) {
		case 0:
		case 1:
			return 0;
		case 2:
		case 3:
			return 2;
		case 4:
		case 5:
			return 4;
		case 6:
		case 7:
			return 6;
		case 8:
		case 9:
			return 8;
		case 10:
		case 11:
			return 10;
		default:
			return n;
		}
	}

	@Override
	protected void arrangeGuitarPart(Part p) {
		arrangeChordNotes(p);
	}

	@Override
	protected void arrangePianoPart(Part p) {
		arrangeChordNotes(p);
	}

	private void arrangeChordNotes(Part p) {
		List<StepValue<List<Chord>>> csl = p.getChordSet();
		for (int j=0; j<csl.size(); j++) {
			List<Chord> cs = csl.get(j).getValue();
			for (int k=0; k<cs.size(); k++) {
				arrangeChordName(cs.get(k));
			}
		}
	}

	@Override
	protected void arrangeGuitar(Guitar g) {
		List<GuitarChord> chords = g.getOriginalChords();
		for (int i=0; i<chords.size(); i++) {
			GuitarChord chord = chords.get(i);
			arrangeGuitarChord(chord);
		}
	}

	private void arrangeGuitarChord(GuitarChord chord) {
		if (chord==null || chord.isUsed()==false) {
			return;
		}
		int[] baseNote = new GuitarChord().getNotes();
		int[] f = chord.getFret();
		boolean[] m = chord.getMute();
		assert(f.length==baseNote.length);
		assert(m.length==baseNote.length);
		int[] noteNum = new int[] {0, 0};
		int[] note = new int[baseNote.length];
		for (int j=0; j<baseNote.length; j++) {
			int n = baseNote[j] + f[j];
			note[j] = n;
			if (m[j]) {
				noteNum[n%2] += 1;  // ミュート音の重み
			} else {
				noteNum[n%2] += 4;  // 通常音の重み
			}
		}
		int index = -1;
		if (noteNum[0]==noteNum[1]) {
			for (int j=0; j<note.length; j++) {
				if (m[j]==false) {
					index = note[j] % 2;
					break;
				}
			}
			if (index<0) {
				index = note[0] % 2;
			}
		} else {
			if (noteNum[0]<noteNum[1]) {
				index = 1;
			} else {
				index = 0;
			}
		}
		for (int j=0; j<note.length; j++) {
			if (note[j]%2!=index) {
				if (baseNote[j]%2==index) {
					note[j]--;
				} else {
					note[j]++;
				}
			}
			f[j] = note[j] - baseNote[j];
		}
		for (int j=6; j>0; j--) {
			chord.setFret(j, f[6-j]);
			chord.setMute(j, m[6-j]);
		}
	}

	@Override
	protected void arrangePiano(Piano p) {
		List<PianoChord> chords = p.getOriginalChords();
		for (int i=0; i<chords.size(); i++) {
			PianoChord chord = chords.get(i);
			arrangePianoChord(chord);
		}
	}

	private void arrangePianoChord(PianoChord chord) {
		if (chord==null || chord.isUsed()==false) {
			return;
		}
		int[] noteNum = new int[] {0, 0};
		List<SingleNote> notes = chord.getNotes();
		int baseNote = 0xFF;
		for (int j=0; j<notes.size(); j++) {
			SingleNote note = notes.get(j);
			if (note.isNote()) {
				int n = note.getNoteNum();
				noteNum[n%2]++;
				if (n<baseNote) {
					baseNote = n;
				}
			}
		}
		if (noteNum[0]+noteNum[1]<=1) {
			return;
		}
		int index = -1;
		if (noteNum[0]==noteNum[1]) {
			index = baseNote % 2;
		} else {
			if (noteNum[0]<noteNum[1]) {
				index = 1;
			} else {
				index = 0;
			}
		}
		SingleNote[] newNotes = new SingleNote[notes.size()];
		for (int j=0; j<notes.size(); j++) {
			SingleNote note = notes.get(j);
			int n = SingleNote.REST;
			if (note.isNote()) {
				n = note.getNoteNum();
				if (n%2!=index) {
					if (n==baseNote+1) {
						n++;
					} else {
						n--;
					}
				}
			}
			newNotes[j] = SingleNote.getInstance(n);
		}
		
		chord.clearNotes();
		for (int j=0; j<newNotes.length; j++) {
			chord.addNote(newNotes[j]);
		}
	}

	private void arrangeChordName(Chord chord) {
		if ((chord instanceof BaseChord) == false) {
			return;
		}
		BaseChord c = (BaseChord) chord;
		Pitch root = c.getRoot();
		Accidental accidental = c.getAccidental();
		c.setChord(root, accidental, BaseChord.Name._aug);
	}

	@Override
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {
		arrangeTempo(tempo, tempoAverage, 80, 10.0, 95.0);
	}

	@Override
	public synchronized void arrangeBinary(List<Integer> binary, BDX bdx) {
		super.arrangeBinary(binary, bdx);
		// 音色調整
		// 0x01e8 音色調整(12*8)
		//   e8:Attack  e9:Decay  ea:Sustain  eb:Release
		//   ec:SHAPE  ed:Hold  ee:Delay  F0:Depth  EF:Speed
		//   f2:EFFECTS(NONE,CROSS,ECHO,CHORUS)  f3:エフェクター値(0-20)
		for (int i=0; i<8; i++) {
			Part p = bdx.getPart(i);
			final InstrumentType type = p.getType();
			if (type==InstrumentType.NONE) {
				continue;
			}
			if (type!=InstrumentType.DRUMS) {
				binary.set(0x01E8 + 12*i, BinaryUtil.AttackValue[p.getToneAttack()]);
				binary.set(0x01E9 + 12*i, BinaryUtil.DecayValue[p.getToneDecay()]);
				binary.set(0x01EA + 12*i, BinaryUtil.SustainValue[p.getToneSustain()]);
				binary.set(0x01EB + 12*i, BinaryUtil.ReleaseValue[p.getToneRelease()]);
				binary.set(0x01EC + 12*i, p.getVibratoShape().getValue());
				binary.set(0x01ED + 12*i, BinaryUtil.HoldValue[p.getVibratoHold()]);
				binary.set(0x01EE + 12*i, BinaryUtil.DelayValue[p.getVibratoDelay()]);
				binary.set(0x01F0 + 12*i, BinaryUtil.DepthValue[p.getVibratoDepth()]);
				binary.set(0x01EF + 12*i, BinaryUtil.SpeedValue[p.getVibratoSpeed()]);
			}
			if (type==InstrumentType.SINGLE) {
				binary.set(0x01F2 + 12*i, p.getToneEffectType().getTypeValue());
				binary.set(0x01F3 + 12*i, p.getToneEffectValue());
			}
			if (type==InstrumentType.GUITAR) {
				BinaryUtil.arrangeGuitarButtonBinary(binary, p);
			}
			if (type==InstrumentType.PIANO) {
				BinaryUtil.arrangePianoButtonBinary(binary, p);
			}
		}
		// ギターコード
		Guitar guitar = bdx.getGuitar();
		if (guitar!=null) {
			List<GuitarChord> gOriginal = guitar.getOriginalChords();
			assert(gOriginal.size()==16);
			for (int i=0; i<gOriginal.size(); i++) {
				GuitarChord chord = gOriginal.get(i);
				int[] fret = chord.getFret();
				boolean[] mute = chord.getMute();
				int[] gChordBinary = new int[] {0, 0, 0, 0};
				if (chord.isUsed()) {
					gChordBinary[3] += 0x40;
				}
				if (mute[0]) {
					gChordBinary[3] += 0x20;
				}
				if (mute[1]) {
					gChordBinary[3] += 0x01;
				}
				if (mute[2]) {
					gChordBinary[2] += 0x08;
				}
				if (mute[3]) {
					gChordBinary[1] += 0x40;
				}
				if (mute[4]) {
					gChordBinary[1] += 0x02;
				}
				if (mute[0]) {
					gChordBinary[0] += 0x10;
				}
				gChordBinary[3] += (fret[0] << 1);
				gChordBinary[2] += (fret[1] << 4);
				gChordBinary[2] += (fret[2] >> 1);
				gChordBinary[1] += ((fret[2] & 0x01) << 7);
				gChordBinary[1] += (fret[3] << 2);
				gChordBinary[1] += (fret[4] >> 3);
				gChordBinary[0] += ((fret[4] & 0x07) << 5);
				gChordBinary[0] += fret[5];
				for (int j=0; j<gChordBinary.length; j++) {
					binary.set(0x4AC8 + 4*i + j, gChordBinary[j]);
				}
			}
		}
		// ピアノコード
		Piano piano = bdx.getPiano();
		if (piano!=null) {
			// オリジナルコード
			List<PianoChord> pOriginal = piano.getOriginalChords();
			assert(pOriginal.size()==16);
			int used = 0;
			for (int i=0; i<pOriginal.size(); i++) {
				PianoChord chord = pOriginal.get(i);
				List<SingleNote> notes = chord.getNotes();
				assert(notes.size()==4);
				for (int j=0; j<notes.size(); j++) {
					binary.set(0x6648 + 4*i + j, notes.get(j).getNoteNum());
				}
				if (chord.isUsed()) {
					used += (0x01 << i);
				}
			}
			binary.set(0x7F1A, (used & 0x00FF));
			binary.set(0x7F1B, ((used & 0xFF00) >> 8));
		}
	}

}
