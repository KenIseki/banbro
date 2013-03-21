package banbro.model.bdx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import banbro.model.Clef;

public class Part implements Tone {

	/** 何も設定されていないパート */
	public static final Part NULL_PART = new Part(InstrumentType.NONE);

	// 楽器
	private InstrumentType _type;
	private BDXInstrument _instrument;
	private int _cloneNum;

	// 共通
	private int _partVolume;
	private int _pan;
	private List<StepValue<Integer>> _volume;
	private EnumMap<PlayLevel, Integer> _playLevel;
	private List<Note> _noteList;
	
	// SINGLE
	private List<StepValue<Integer>> _bass;
	private List<StepValue<Integer>> _button;
	private List<StepValue<Clef>> _clef;
	
	// GUITAR, PIANO
	private List<StepValue<List<Chord>>> _chordSetList;
	
	// PIANO
	private List<StepValue<Voicing>> _voicing;

	// 音色調整
	private int _attack;
	private int _decay;
	private int _sustain;
	private int _release;
	private VibratoShape _shape;
	private int _hold;
	private int _delay;
	private int _depth;
	private int _speed;
	private EffectType _effectType;
	private int _effectValue;

	/**
	 * @param type 演奏タイプ
	 * {@link InstrumentType#SINGLE},
	 * {@link InstrumentType#DRUMS},
	 * {@link InstrumentType#GUITAR},
	 * {@link InstrumentType#PIANO},
	 * {@link InstrumentType#NONE}
	 */
	public Part(InstrumentType type) {
		_instrument = BDXInstrument.NONE;
		_type = type;
		if (type==InstrumentType.SINGLE) {
			_bass = new ArrayList<StepValue<Integer>>();
			_button = new ArrayList<StepValue<Integer>>();
			_clef = new ArrayList<StepValue<Clef>>();
		}
		if (type==InstrumentType.GUITAR || type==InstrumentType.PIANO) {
			_chordSetList = new ArrayList<StepValue<List<Chord>>>();
		}
		if (type==InstrumentType.PIANO) {
			_voicing = new ArrayList<StepValue<Voicing>>();
		}
		if (type!=InstrumentType.NONE) {
			_playLevel = new EnumMap<PlayLevel, Integer>(PlayLevel.class);
			_volume = new ArrayList<StepValue<Integer>>();
			_noteList = new ArrayList<Note>();
		}
	}

	public BDXInstrument getInstrument() {
		return _instrument;
	}

	public String getPartName() {
		if (_instrument==BDXInstrument.NONE) {
			return _instrument.getName();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(_instrument.getName());
		if (_cloneNum>0) {
			String st = BinaryUtil.to2ByteString(String.valueOf(_cloneNum));
			sb.append(st);
		}
		switch (_type) {
		case GUITAR:
		case PIANO:
			sb.append("（");
			sb.append(_type.getTypeName());
			sb.append("）");
			break;
		default:
			break;
		}
		return sb.toString();
	}

	public int getCloneNum() {
		return _cloneNum;
	}

	public InstrumentType getType() {
		return _type;
	}

	public int getPlayLevel(PlayLevel level) {
		if (_type==InstrumentType.NONE) {
			throw new IllegalStateException();
		}
		Integer i = _playLevel.get(level);
		if (i!=null) {
			return i.intValue();
		}
		return 0;
	}

	public List<Note> getNoteList() {
		if (_type==InstrumentType.NONE) {
			throw new IllegalStateException();
		}
		return _noteList;
	}

	public int getPartVolume() {
		return _partVolume;
	}

	public int getPan() {
		return _pan;
	}

	public List<StepValue<Integer>> getVolume() {
		if (_type==InstrumentType.NONE) {
			throw new IllegalStateException();
		}
		return _volume;
	}

	public int getVolume(int step) {
		if (_type==InstrumentType.NONE) {
			throw new IllegalStateException();
		}
		return getValue(_volume, step);
	}

	public List<StepValue<Integer>> getBass() {
		if (_type!=InstrumentType.SINGLE) {
			throw new IllegalStateException();
		}
		return _bass;
	}

	public int getBass(int step) {
		if (_type!=InstrumentType.SINGLE) {
			throw new IllegalStateException();
		}
		return getValue(_bass, step);
	}

	public List<StepValue<Integer>> getButton() {
		if (_type!=InstrumentType.SINGLE) {
			throw new IllegalStateException();
		}
		return _button;
	}

	public int getButton(int step) {
		if (_type!=InstrumentType.SINGLE) {
			throw new IllegalStateException();
		}
		return getValue(_button, step);
	}

	public List<StepValue<Voicing>> getVoicing() {
		if (_type!=InstrumentType.PIANO) {
			throw new IllegalStateException();
		}
		return _voicing;
	}

	public Voicing getVoicing(int step) {
		if (_type!=InstrumentType.PIANO) {
			throw new IllegalStateException();
		}
		return BDXUtil.getValueWithStep(_voicing, step, true);
	}

	public List<StepValue<Clef>> getClef() {
		if (_type!=InstrumentType.SINGLE) {
			throw new IllegalStateException();
		}
		return _clef;
	}

	public Clef getClef(int step) {
		if (_type!=InstrumentType.SINGLE) {
			throw new IllegalStateException();
		}
		return BDXUtil.getValueWithStep(_clef, step, true);
	}

	private int getValue(List<StepValue<Integer>> list, int step) {
		Integer value = BDXUtil.getValueWithStep(list, step, true);
		if (value==null) {
			return 0;
		}
		return value;
	}

	@Override
	public int getToneAttack() {
		return _attack;
	}

	@Override
	public int getToneDecay() {
		return _decay;
	}

	@Override
	public int getToneSustain() {
		return _sustain;
	}

	@Override
	public int getToneRelease() {
		return _release;
	}

	@Override
	public VibratoShape getVibratoShape() {
		return _shape;
	}

	@Override
	public int getVibratoHold() {
		return _hold;
	}

	@Override
	public int getVibratoDelay() {
		return _delay;
	}

	@Override
	public int getVibratoDepth() {
		return _depth;
	}

	@Override
	public int getVibratoSpeed() {
		return _speed;
	}

	@Override
	public EffectType getToneEffectType() {
		return _effectType;
	}

	@Override
	public int getToneEffectValue() {
		return _effectValue;
	}

	public void setInstrument(BDXInstrument instrument) {
		if (instrument==BDXInstrument.NONE) {
			_type = InstrumentType.NONE;
		} else if (instrument.isDrums() && _type!=InstrumentType.DRUMS) {
			throw new IllegalArgumentException(instrument.getName() + " を " + _type.toString() + " に設定することはできません");
		}
		_instrument = instrument;
	}

	public void setCloneNum(int num) {
		if (_type==InstrumentType.NONE) {
			throw new IllegalStateException();
		}
		_cloneNum = num;
	}

	public void setPlayLevel(PlayLevel level, int value) {
		if (_type==InstrumentType.NONE) {
			throw new IllegalStateException();
		}
		_playLevel.put(level, value);
	}

	public void setNote(int i, Note n) {
		if (_type!=n.getType()) {
			throw new IllegalStateException();
		}
		if (0<=i && i<_noteList.size()) {
			_noteList.set(i, n);
		}
	}

	public void addNote(Note n) {
		if (_type!=n.getType()) {
			throw new IllegalStateException();
		}
		_noteList.add(n);
	}

	public void clearNotes() {
		if (_type==InstrumentType.NONE) {
			throw new IllegalStateException();
		}
		_noteList.clear();
	}

	public void setPartVolume(int v) {
		_partVolume = v;
	}

	public void setPan(int p) {
		_pan = p;
	}

	public void clearVolume() {
		if (_type==InstrumentType.NONE) {
			throw new IllegalStateException();
		}
		_volume.clear();
	}

	public void addVolume(int step, int volume) {
		if (_type==InstrumentType.NONE) {
			throw new IllegalStateException();
		}
		_volume.add(new StepValue<Integer>(step, volume));
	}

	public void clearBass() {
		if (_type!=InstrumentType.SINGLE) {
			throw new IllegalStateException();
		}
		_bass.clear();
	}

	public void addBass(int step, int bass) {
		if (_type!=InstrumentType.SINGLE) {
			throw new IllegalStateException();
		}
		_bass.add(new StepValue<Integer>(step, bass));
	}

	public void clearButton() {
		if (_type!=InstrumentType.SINGLE) {
			throw new IllegalStateException();
		}
		_button.clear();
	}

	public void addButton(int step, int button) {
		if (_type!=InstrumentType.SINGLE) {
			throw new IllegalStateException();
		}
		_button.add(new StepValue<Integer>(step, button));
	}

	public List<StepValue<List<Chord>>> getChordSet() {
		if (_type!=InstrumentType.GUITAR && _type!=InstrumentType.PIANO) {
			throw new IllegalStateException();
		}
		return _chordSetList;
	}

	public List<Chord> getChordSet(int step) {
		if (_type!=InstrumentType.GUITAR && _type!=InstrumentType.PIANO) {
			throw new IllegalStateException();
		}
		List<Chord> chordSet = BDXUtil.getValueWithStep(_chordSetList, step, true);
		if (chordSet==null) {
			return Collections.emptyList();
		}
		return chordSet;
	}
	
	public int[] getChordSetSteps() {
		if (_type!=InstrumentType.GUITAR && _type!=InstrumentType.PIANO) {
			throw new IllegalStateException();
		}
		int[] steps = new int[_chordSetList.size()];
		for (int i=0; i<steps.length; i++) {
			steps[i] = _chordSetList.get(i).getStep();
		}
		return steps;
	}

	public void addChordSet(int step, List<Chord> chordSet) {
		if (_type!=InstrumentType.GUITAR && _type!=InstrumentType.PIANO) {
			throw new IllegalStateException();
		}
		_chordSetList.add(new StepValue<List<Chord>>(step, chordSet));
	}

	public void addChord(int step, Chord c) {
		if (_type!=InstrumentType.GUITAR && _type!=InstrumentType.PIANO) {
			throw new IllegalStateException();
		}
		int index = BDXUtil.searchIndexWithStep(_chordSetList, step, true);
		if (index<0) {
			List<Chord> chordSet = new ArrayList<Chord>();
			chordSet.add(c);
			addChordSet(step, chordSet);
			return;
		}
		StepValue<List<Chord>> sv = _chordSetList.get(index);
		int st = sv.getStep();
		if (st==step) {
			List<Chord> chordSet = sv.getValue();
			chordSet.add(c);
		} else {
			List<Chord> chordSet = new ArrayList<Chord>();
			chordSet.add(c);
			addChordSet(step, chordSet);
			Collections.sort(_chordSetList);
		}
	}

	public void setChordSetSteps(int[] steps) {
		if (_type!=InstrumentType.GUITAR && _type!=InstrumentType.PIANO) {
			throw new IllegalStateException();
		}
		if (steps.length!=_chordSetList.size()) {
			return;
		}
		for (int i=0; i<steps.length; i++) {
			_chordSetList.get(i).setStep(steps[i]);
		}
	}

	public void clearChordSet() {
		if (_type!=InstrumentType.GUITAR && _type!=InstrumentType.PIANO) {
			throw new IllegalStateException();
		}
		_chordSetList.clear();
	}

	public void clearVoicing() {
		if (_type!=InstrumentType.PIANO) {
			throw new IllegalStateException();
		}
		_voicing.clear();
	}

	public void addVoicing(int step, Voicing v) {
		if (_type!=InstrumentType.PIANO) {
			throw new IllegalStateException();
		}
		_voicing.add(new StepValue<Voicing>(step, v));
	}

	public void setClef(List<StepValue<Clef>> clef) {
		if (_type!=InstrumentType.SINGLE) {
			throw new IllegalStateException();
		}
		_clef = clef;
	}

	public void addClef(int step, Clef clef) {
		if (_type!=InstrumentType.SINGLE) {
			throw new IllegalStateException();
		}
		_clef.add(new StepValue<Clef>(step, clef));
	}

	public void clearClef() {
		if (_type!=InstrumentType.SINGLE) {
			throw new IllegalStateException();
		}
		_clef.clear();
	}

	@Override
	public void setToneAttack(int attack) {
		_attack = attack;
	}

	@Override
	public void setToneDecay(int decay) {
		_decay = decay;
	}

	@Override
	public void setToneSustain(int sustain) {
		_sustain = sustain;
	}

	@Override
	public void setToneRelease(int release) {
		_release = release;
	}

	@Override
	public void setVibratoShape(VibratoShape shape) {
		_shape = shape;
	}

	@Override
	public void setVibratoHold(int hold) {
		_hold = hold;
	}

	@Override
	public void setVibratoDelay(int delay) {
		_delay = delay;
	}

	@Override
	public void setVibratoDepth(int depth) {
		_depth = depth;
	}

	@Override
	public void setVibratoSpeed(int speed) {
		_speed = speed;
	}

	@Override
	public void setToneEffectType(EffectType type) {
		_effectType = type;
	}

	@Override
	public void setToneEffectValue(int value) {
		_effectValue = value;
	}

	/**
	 * 無駄な情報を削除する
	 * @param maxStep
	 */
	public void trim(int maxStep) {
		if (_type==InstrumentType.NONE) {
			return;
		}
		if (_volume.isEmpty()) {
			_volume.add(new StepValue<Integer>(0, 99));
		} else {
			Collections.sort(_volume);
			StepValue<Integer> sv = _volume.get(0);
			sv.setStep(0);
			for (int i=_volume.size()-1; i>0; i--) {
				StepValue<Integer> step = _volume.get(i);
				if (step.getStep()>maxStep) {
					_volume.remove(i);
				}
			}
		}
		if (_type==InstrumentType.SINGLE) {
			if (_bass.isEmpty()) {
				_bass.add(new StepValue<Integer>(0, 24));
			} else {
				Collections.sort(_bass);
				StepValue<Integer> sv = _bass.get(0);
				sv.setStep(0);
				for (int i=_bass.size()-1; i>0; i--) {
					StepValue<Integer> step = _bass.get(i);
					if (step.getStep()>maxStep) {
						_bass.remove(i);
					} else {
						// 同じ値が連続しているなら削除
						StepValue<Integer> step2 = _bass.get(i-1);
						if (step.getValue()==step2.getValue()) {
							_bass.remove(i);
						}
					}
				}
			}
			if (_button.isEmpty()) {
				_button.add(new StepValue<Integer>(0, 0));
			} else {
				Collections.sort(_button);
				StepValue<Integer> sv = _button.get(0);
				sv.setStep(0);
				for (int i=_button.size()-1; i>0; i--) {
					StepValue<Integer> step = _button.get(i);
					if (step.getStep()>maxStep) {
						_button.remove(i);
					} else {
						// 同じ値が連続しているなら削除
						StepValue<Integer> step2 = _button.get(i-1);
						if (step.getValue()==step2.getValue()) {
							_button.remove(i);
						}
					}
				}
			}
			if (_clef.isEmpty()) {
				_clef.add(new StepValue<Clef>(0, Clef.G2));
			} else {
				Collections.sort(_clef);
				StepValue<Clef> sv = _clef.get(0);
				sv.setStep(0);
				for (int i=_clef.size()-1; i>0; i--) {
					StepValue<Clef> step = _clef.get(i);
					if (step.getStep()>maxStep) {
						_clef.remove(i);
					} else {
						// 同じ値が連続しているなら削除
						StepValue<Clef> step2 = _clef.get(i-1);
						if (step.getValue()==step2.getValue()) {
							_clef.remove(i);
						}
					}
				}
			}
		} else if (_type==InstrumentType.GUITAR || _type==InstrumentType.PIANO) {
			if (!_chordSetList.isEmpty()) {
				Collections.sort(_chordSetList);
				StepValue<List<Chord>> sv = _chordSetList.get(0);
				sv.setStep(0);
				for (int i=_chordSetList.size()-1; i>0; i--) {
					StepValue<List<Chord>> step = _chordSetList.get(i);
					if (step.getStep()>maxStep) {
						_chordSetList.remove(i);
					} else {
						// 同じ値が連続しているなら削除
						StepValue<List<Chord>> step2 = _chordSetList.get(i-1);
						if (step.getValue().equals(step2.getValue())) {
							_chordSetList.remove(i);
						}
					}
				}
			}
		}
		if (_type==InstrumentType.PIANO) {
			if (!_voicing.isEmpty()) {
				Collections.sort(_voicing);
				StepValue<Voicing> sv = _voicing.get(0);
				sv.setStep(0);
				for (int i=_voicing.size()-1; i>0; i--) {
					StepValue<Voicing> step = _voicing.get(i);
					if (step.getStep()>maxStep) {
						_voicing.remove(i);
					} else {
						// 同じ値が連続しているなら削除
						StepValue<Voicing> step2 = _voicing.get(i-1);
						if (step.getValue().equals(step2.getValue())) {
							_voicing.remove(i);
						}
					}
				}
			}
		}
	}

	public Part clone() {
		if (_type==InstrumentType.NONE) {
			return NULL_PART;
		}
		Part copy = new Part(_type);
		copy.setInstrument(_instrument);
		copy.setCloneNum(_cloneNum);
		for (PlayLevel level : _playLevel.keySet()) {
			copy.setPlayLevel(level, _playLevel.get(level));
		}
		for (int i=0; i<_noteList.size(); i++) {
			copy.addNote(_noteList.get(i).clone());
		}
		copy.setPartVolume(_partVolume);
		copy.setPan(_pan);
		for (StepValue<Integer> sv : _volume) {
			copy.addVolume(sv.getStep(), sv.getValue());
		}
		if (_type==InstrumentType.SINGLE) {
			for (StepValue<Integer> sv : _bass) {
				copy.addBass(sv.getStep(), sv.getValue());
			}
			for (StepValue<Integer> sv : _button) {
				copy.addButton(sv.getStep(), sv.getValue());
			}
			// clef
			List<StepValue<Clef>> clef = new ArrayList<StepValue<Clef>>();
			for (StepValue<Clef> sv : _clef) {
				clef.add(sv.clone());
			}
			copy.setClef(clef);
		}
		if (_type==InstrumentType.GUITAR || _type==InstrumentType.PIANO) {
			for (StepValue<List<Chord>> sv : _chordSetList) {
				copy.addChordSet(sv.getStep(), sv.getValue());
			}
		}
		if (_type==InstrumentType.PIANO) {
			for (StepValue<Voicing> sv : _voicing) {
				copy.addVoicing(sv.getStep(), sv.getValue());
			}
		}
		copy.setToneAttack(_attack);
		copy.setToneDecay(_decay);
		copy.setToneSustain(_sustain);
		copy.setToneRelease(_release);
		copy.setVibratoShape(_shape);
		copy.setVibratoHold(_hold);
		copy.setVibratoDelay(_delay);
		copy.setVibratoDepth(_depth);
		copy.setVibratoSpeed(_speed);
		copy.setToneEffectType(_effectType);
		copy.setToneEffectValue(_effectValue);
		return copy;
	}

	@Override
	public String toString() {
		return getPartName();
	}

}
