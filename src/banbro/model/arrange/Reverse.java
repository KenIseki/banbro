package banbro.model.arrange;

import java.util.ArrayList;
import java.util.List;

import banbro.model.bdx.BDX;
import banbro.model.bdx.BinaryUtil;
import banbro.model.bdx.Chord;
import banbro.model.bdx.DrumNote;
import banbro.model.bdx.GuitarNote;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Note;
import banbro.model.bdx.Part;
import banbro.model.bdx.SingleNote;
import banbro.model.bdx.StepValue;
import banbro.model.bdx.Voicing;
import banbro.model.bdx.Guitar.Stroke;

public class Reverse extends AbstractArrange {

	private int _maxStep;

	public Reverse() {
		super();
	}
	public Reverse(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	public BDX doArrange(BDX bdx) {
		_maxStep = BDX.TIME_BASE * bdx.getTimeNum();
		return super.doArrange(bdx);
	}

	@Override
	protected void arrangeBDX(BDX bdx) {
		// テンポを逆にする
		bdx.setTempo(reverseStepValue(bdx.getTempo()));

		// 歌詞を逆にする
		List<String> newLyric = new ArrayList<String>();
		for (String page : bdx.getLyric()) {
			StringBuffer buf = new StringBuffer();
			buf.append(page);
			buf.reverse();
			String st = buf.toString();
			newLyric.add(0, st);
		}
		bdx.setLyric(newLyric);
		// 歌詞割り当てstepを逆にする 歌詞も逆順にする
		List<StepValue<String>> lyricTiming = bdx.getLyricTiming();
		if (lyricTiming.size()!=0) {
			List<StepValue<String>> newLyricTiming = new ArrayList<StepValue<String>>();
			for (int i=lyricTiming.size()-1; i>=0; i--) {
				StepValue<String> sv = lyricTiming.get(i);
				int step = sv.getStep();
				StringBuilder sb = new StringBuilder();
				sb.append(sv.getValue());
				sb.reverse();
				newLyricTiming.add(new StepValue<String>(_maxStep-step-1, sb.toString()));
			}
			bdx.setLyricTiming(newLyricTiming);
		}

		// 調stepを逆にする
		bdx.setKey(reverseStepValue(bdx.getKey()));

		// コード配置stepを逆にする
		List<StepValue<Chord>> chordTiming = reverseStepValue(bdx.getChordTiming());
		bdx.clearChordTiming();
		for (StepValue<Chord> sv : chordTiming) {
			bdx.addChordTiming(sv.getStep(), sv.getValue());
		}

		bdx.setLyricLines(null);  // 未対応
	}

	@Override
	protected void arrangePart(Part p) {
		// 音量stepを逆にする
		List<StepValue<Integer>> volume = reverseStepValue(p.getVolume());
		p.clearVolume();
		for (StepValue<Integer> sv : volume) {
			p.addVolume(sv.getStep(), sv.getValue());
		}
	}

	@Override
	protected void arrangeSinglePart(Part p) {
		// ベースstepを逆にする
		List<StepValue<Integer>> bass = reverseStepValue(p.getBass());
		p.clearBass();
		for (StepValue<Integer> sv : bass) {
			p.addBass(sv.getStep(), sv.getValue());
		}
		// ボタン割り当て
		List<StepValue<Integer>> button = reverseStepValue(p.getButton());
		p.clearButton();
		for (StepValue<Integer> sv : button) {
			p.addButton(sv.getStep(), sv.getValue());
		}
		// 音部stepを逆にする
		p.setClef(reverseStepValue(p.getClef()));
		reverseNotes(p);
	}

	@Override
	protected void arrangeDrumPart(Part p) {
		List<Note> noteList = p.getNoteList();
		int[] newUpperNoteList = new int[noteList.size()];
		int[] newLowerNoteList = new int[noteList.size()];
		for (int i=0; i<noteList.size(); i++) {
			DrumNote note = (DrumNote) noteList.get(i);
			newUpperNoteList[i] = note.getUpperNoteNum();
			newLowerNoteList[i] = note.getLowerNoteNum();
		}
		// 3連符フラグを拍の最後尾に移動する
		for (int i=3; i<noteList.size(); i+=4) {
			if (newUpperNoteList[i-3]==DrumNote.GROUP) {
				newUpperNoteList[i-3] = newUpperNoteList[i-2];
				newUpperNoteList[i-2] = newUpperNoteList[i-1];
				newUpperNoteList[i-1] = newUpperNoteList[i];
				newUpperNoteList[i] = DrumNote.GROUP;
			}
			if (newLowerNoteList[i-3]==DrumNote.GROUP) {
				newLowerNoteList[i-3] = newLowerNoteList[i-2];
				newLowerNoteList[i-2] = newLowerNoteList[i-1];
				newLowerNoteList[i-1] = newLowerNoteList[i];
				newLowerNoteList[i] = DrumNote.GROUP;
			}
		}
		// 逆順に読んで設定し直す
		int last = noteList.size()-1;
		for (int i=0; i<noteList.size(); i++) {
			DrumNote note = DrumNote.getInstanceWithNote(newUpperNoteList[last-i], newLowerNoteList[last-i]);
			p.setNote(i, note);
		}
	}

	@Override
	protected void arrangeGuitarPart(Part p) {
		// ストロークを逆にする
		List<Note> noteList = p.getNoteList();
		for (int i=0; i<noteList.size(); i++) {
			GuitarNote note = (GuitarNote) noteList.get(i);
			switch (note.getStroke()) {
			case DOWN:
				noteList.set(i, GuitarNote.getInstance(note.getButtonNum(), Stroke.UP, note.isExtend()));
				break;
			case UP:
				noteList.set(i, GuitarNote.getInstance(note.getButtonNum(), Stroke.DOWN, note.isExtend()));
				break;
			default:
				break;
			}
		}
		reverseNotes(p);
		reverseChordSet(p);
	}

	@Override
	protected void arrangePianoPart(Part p) {
		List<StepValue<Voicing>> voicing = reverseStepValue(p.getVoicing());
		p.clearVoicing();
		for (StepValue<Voicing> sv : voicing) {
			p.addVoicing(sv.getStep(), sv.getValue());
		}
		reverseNotes(p);
		reverseChordSet(p);
	}

	private <V> List<StepValue<V>> reverseStepValue(List<StepValue<V>> list) {
		if (_maxStep<=0) {
			return list;
		}
		List<StepValue<V>> newList = new ArrayList<StepValue<V>>();
		int nextStep = _maxStep;
		for (int i=list.size()-1; i>=0; i--) {
			StepValue<V> sv = list.get(i);
			newList.add(new StepValue<V>((_maxStep - nextStep), sv.getValue()));
			nextStep = sv.getStep();
		}
		return newList;
	}

	private void reverseNotes(Part p) {
		List<Note> noteList = p.getNoteList();
		Note[] newNoteList = new Note[noteList.size()];
		for (int i=0; i<noteList.size(); i++) {
			newNoteList[i] = noteList.get(i);
		}
		// アタック位置を逆にする
		for (int i=0; i<noteList.size(); i++) {
			SingleNote note = (SingleNote) noteList.get(i);
			if (note.isNote()) {
				int index = i;
				for (int j=i+1; j<noteList.size(); j++) {
					SingleNote note2 = (SingleNote) noteList.get(j);
					if (note2.isExtend()) {
						index = j;
					} else if (note2.isNote() || note2.isRest()) {
						break;
					}
				}
				if (i!=index) {
					newNoteList[i] = noteList.get(index);
					newNoteList[index] = noteList.get(i);
				}
			}
		}
		// 3連符フラグを拍の最後尾に移動する
		for (int i=3; i<newNoteList.length; i+=4) {
			SingleNote note = (SingleNote) newNoteList[i-3];
			if (note.isGroup()) {
				newNoteList[i-3] = newNoteList[i-2];
				newNoteList[i-2] = newNoteList[i-1];
				newNoteList[i-1] = newNoteList[i];
				newNoteList[i] = note;
			}
		}
		// 逆順に読んで設定し直す
		int last = newNoteList.length-1;
		for (int i=0; i<newNoteList.length; i++) {
			p.setNote(i, newNoteList[last-i]);
		}
		// アタックとリリース入れ替え
		int attack = p.getToneAttack();
		int release = p.getToneRelease();
		p.setToneAttack(release);
		p.setToneRelease(attack);
	}

	private void reverseChordSet(Part p) {
		List<StepValue<List<Chord>>> chordSet = reverseStepValue(p.getChordSet());
		p.clearChordSet();
		for (StepValue<List<Chord>> sv : chordSet) {
			p.addChordSet(sv.getStep(), sv.getValue());
		}
	}

	@Override
	public synchronized void arrangeBinary(List<Integer> binary, BDX bdx) {
		super.arrangeBinary(binary, bdx);
		// 全体：テンポ
		List<StepValue<Integer>> tempo = bdx.getTempo();
		assert(tempo.size()<=32);
		for (int i=0; i<tempo.size(); i++) {
			StepValue<Integer> sv = tempo.get(i);
			List<Integer> stepBinary = BinaryUtil.to2byteBinary(sv.getStep());
			List<Integer> tempoBinary = BinaryUtil.to2byteBinary(sv.getValue());
			binary.set(0x4248 + 4*i, stepBinary.get(0));
			binary.set(0x4249 + 4*i, stepBinary.get(1));
			binary.set(0x424A + 4*i, tempoBinary.get(0));
			binary.set(0x424B + 4*i, tempoBinary.get(1));
		}
		// 全体:コード配置
		List<StepValue<Chord>> chordTiming = bdx.getChordTiming();
		assert(chordTiming.size()<=255);
		binary.set(0x6918, chordTiming.size());
		for (int i=0; i<chordTiming.size(); i++) {
			StepValue<Chord> sv = chordTiming.get(i);
			List<Integer> stepBinary = BinaryUtil.to2byteBinary(sv.getStep());
			int[] chordBinary = BinaryUtil.getChordBinary(sv.getValue());
			assert(chordBinary.length==2);
			binary.set(0x691C + 4*i, stepBinary.get(0));
			binary.set(0x691D + 4*i, stepBinary.get(1));
			binary.set(0x691E + 4*i, chordBinary[0]);
			binary.set(0x691F + 4*i, chordBinary[1]);
		}
		// 全体:調
		int lastKey = 256;
		for (int i=0; i<512; i++) {  // 512拍
			int key = bdx.getKey(BDX.TIME_BASE*i);
			key += 0x88;
			if (key==lastKey) {
				key -= 0x80;
			} else {
				lastKey = key;
			}
			binary.set(0x7d18 + i, key);
		}
	}

	@Override
	protected void arrangePartBinary(List<Integer> binary, BDX bdx, int partNum) {
		super.arrangePartBinary(binary, bdx, partNum);
		Part p = bdx.getPart(partNum);
		final InstrumentType type = p.getType();
		if (type==InstrumentType.NONE) {
			return;
		}
		// パート:アタック、リリース
		if (type!=InstrumentType.DRUMS) {
			binary.set(0x01E8 + 12*partNum, BinaryUtil.AttackValue[p.getToneAttack()]);
			binary.set(0x01EB + 12*partNum, BinaryUtil.ReleaseValue[p.getToneRelease()]);
		}
		// 単音パート:音部
		if (type==InstrumentType.SINGLE) {
			int lastClef = -1;
			for (int i=0; i<512; i++) {
				int clef = p.getClef(BDX.TIME_BASE*i).getValue();
				if (clef!=lastClef) {
					lastClef = clef;
					clef += 0x80;
				}
				binary.set(0x6D18 + 512*partNum + i, clef);
			}
		}
		// ギターパート:ギターコードセット
		if (type==InstrumentType.GUITAR) {
			BinaryUtil.arrangeGuitarButtonBinary(binary, p);
		}
		if (type==InstrumentType.PIANO) {
			BinaryUtil.arrangePianoButtonBinary(binary, p);
		}
	}

}
