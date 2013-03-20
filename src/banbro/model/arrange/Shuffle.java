package banbro.model.arrange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import banbro.model.bdx.BDX;
import banbro.model.bdx.BinaryUtil;
import banbro.model.bdx.Chord;
import banbro.model.bdx.DrumNote;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Note;
import banbro.model.bdx.Part;
import banbro.model.bdx.PlayLevel;
import banbro.model.bdx.SingleNote;
import banbro.model.bdx.StepValue;
import banbro.model.bdx.Voicing;
import banbro.model.score.Score.Clef;

public class Shuffle extends AbstractArrange {

	private static final int[] DEFAULT_SHUFFLE_VALUES = {
		 80,  // ����臒l
		 20,  // ��������
		  0,  // ��������
		 60,  // ��������
		 20,  // ��������
		 40,  // ��������
		 60,  // ��������
		 80,  // ��������
		  0,  // ��������
		 40,  // ��������
		  0,  // ��������
		 80,  // ��������
		 60,  // ��������
		 80,  // ��������
		 80,  // ��������
		100   // ��������
	};

	/**
	 * index=0:�S�̂̔���臒l
	 * index=1~15:�����̃p�^�[������臒l
	 * 臒l��0~100
	 * @return
	 */
	public static int[] getDefaultShuffleValues() {
		return Arrays.copyOf(DEFAULT_SHUFFLE_VALUES, DEFAULT_SHUFFLE_VALUES.length);
	}

	/** �u�ő�e���|�v�̍ŏ��l */
	public static final int MIN_MAX_TEMPO = 30;
	/** �u�ő�e���|�v�̍ő�l */
	public static final int MAX_MAX_TEMPO = 300;
	/** �u�ő�e���|�v�̃f�t�H���g */
	public static final int DEFAULT_MAX_TEMPO = 150;
	
	/** @see Shuffle#setOption(String, Object) */
	public static final String OPTION_SHUFFLE_MAX_TEMPO = "ShuffleMaxTempo";
	/** @see Shuffle#setOption(String, Object) */
	public static final String OPTION_SHUFFLE_VALUES = "ShuffleValues";

	private int _maxTempo;
	private int[] _shuffleValues;

	public Shuffle() {
		super();
		_maxTempo = DEFAULT_MAX_TEMPO;
		_shuffleValues = getDefaultShuffleValues();
	}
	public Shuffle(boolean isArrangeTempo, boolean isBDX) {
		this();
		setIsArrangeTempo(isArrangeTempo);
		setIsBDX(isBDX);
	}

	/**
	 * @param optionName {@link #OPTION_SHUFFLE_MAX_TEMPO}, {@link #OPTION_SHUFFLE_VALUES}
	 * @param optionValue
	 * {@link #OPTION_SHUFFLE_MAX_TEMPO}�̂Ƃ��A{@link #MIN_MAX_TEMPO}~{@link #MAX_MAX_TEMPO}�͈̔͂�int<br>
	 * {@link #OPTION_SHUFFLE_VALUES}�̂Ƃ��A{@link #getDefaultShuffleValues()}�Ɠ���������int[]
	 */
	@Override
	public synchronized void setOption(String optionName, Object optionValue) {
		if (optionName.equals(OPTION_SHUFFLE_MAX_TEMPO)) {
			int tempo = (int) optionValue;
			if (MIN_MAX_TEMPO<=tempo && tempo<=MAX_MAX_TEMPO) {
				_maxTempo = tempo;
			}
		} else if (optionName.equals(OPTION_SHUFFLE_VALUES)) {
			int[] values = (int[]) optionValue;
			if (_shuffleValues.length!=values.length) {
				return;
			}
			for (int i=0; i<_shuffleValues.length; i++) {
				if (0<=values[i] && values[i]<=100) {
					_shuffleValues[i] = values[i];
				}
			}
		}
	}

	@Override
	public BDX doArrange(BDX bdx) {
		// 1�����Ƃ̕��σe���|�Ƃ��̍ŏ��l���v�Z����
		// ���̓r���Ńe���|�ύX������ꍇ�� �e���|*�X�e�b�v ���i�[���Ă���
		List<StepValue<Integer>> tempoList = new ArrayList<StepValue<Integer>>(bdx.getTempo());
		tempoList.add(new StepValue<Integer>(BDX.TIME_BASE*bdx.getTimeNum(), 0));
		int min = 0xFFFF;
		int lastStep = 0;
		int lastTempo = 0;
		List<Integer> average = new ArrayList<Integer>();  // �l=1���̕��σe���|�Asize=����
		for (StepValue<Integer> sv : tempoList) {
			int step = sv.getStep();
			int tempo = lastTempo;
			int length = step - lastStep;
			if (lastStep%BDX.TIME_BASE!=0) {  // �O�̃e���|�ύX�����̓r��
				int n = (BDX.TIME_BASE-lastStep%BDX.TIME_BASE);  // 1���̎c���Step
				int ave = average.get(average.size()-1);
				if (length>=n) {
					ave += tempo * n;
					length -= n;
					ave /= BDX.TIME_BASE;
					min = Math.min(min, ave);
				} else {
					ave += tempo * length;
					length = 0;
				}
				average.set(average.size()-1, ave);
			}
			if (length>=BDX.TIME_BASE) {
				min = Math.min(min, tempo);
			}
			while (length>=BDX.TIME_BASE) {
				average.add(tempo);
				length -= BDX.TIME_BASE;
			}
			if (length!=0) {
				average.add(tempo * length);
			}
			lastStep = step;
			lastTempo = sv.getValue();
		}

		int extra = 0;
		List<Integer> offset = new ArrayList<Integer>();  // �����ɂ�肸��锏��
		List<Boolean> isHalf = new ArrayList<Boolean>();  // �����̔�
		// �����Ȃ甼������Ȃ�
		int maxMaXTempo = isBDX() ? Math.min(DEFAULT_MAX_TEMPO, _maxTempo) : _maxTempo;
		if (min<=maxMaXTempo) {
			// �e���|->(����,����臒l)
			Map<Integer, StepValue<Integer>> tempoMap = createTempoMap(bdx, average);

			// ����������2�{�ɐL�΂�����
			int maxTempo = 0;
			for (int i=0; i<average.size(); i++) {
				int tempo = average.get(i);
				StepValue<Integer> sv = tempoMap.get(tempo);
				boolean half = (sv!=null && sv.getValue()>=(100-_shuffleValues[0]) && tempo<=maxMaXTempo);
				if (half && tempo>maxTempo) {
					maxTempo = tempo;
				}
			}
			for (int i=0; i<average.size(); i++) {
				offset.add(extra);
				int tempo = average.get(i);
				boolean half = (tempo<=maxTempo);
				isHalf.add(half);
				if (half) {
					extra++;
				}
			}
		}
		int maxBars = isBDX() ? BDX.MAX_BARS : Integer.MAX_VALUE/BDX.TIME_BASE/bdx.getTime();

		// ����������2�{�ɂ���
		return super.doArrange(convertDoubleTempo(bdx, offset, isHalf, extra, maxBars));
	}

	private BDX convertDoubleTempo(BDX bdx, List<Integer> offset, List<Boolean> isHalf, int extra, int maxBars) {
		if (extra==0) {  // �����Ȃ�
			return bdx;
		}
		int exBars = (extra+bdx.getTime()-1) / bdx.getTime();
		if (bdx.getTime()*(bdx.getBars()+exBars) > maxBars) {
			// ���ߐ��I�[�o�[
			return bdx;
		}
		BDX copy = bdx.clone();
		copy.setBars(bdx.getBars() + exBars);
		copy.setTempo(calcTempo(bdx.getTempo(), offset, isHalf));
		copy.setKey(calcStepValue(bdx.getKey(), offset, isHalf));
		// �̎����蓖��
		List<StepValue<String>> lyricTiming = bdx.getLyricTiming();
		copy.clearLyricTiming();
		for (StepValue<String> sv : lyricTiming) {
			int step = calcNewStep(sv.getStep(), offset, isHalf);
			copy.addLyricTiming(step, sv.getValue());
		}
		// �R�[�h�z�u
		List<StepValue<Chord>> chordTiming = bdx.getChordTiming();
		copy.clearChordTiming();
		for (StepValue<Chord> sv : chordTiming) {
			copy.addChordTiming(calcNewStep(sv.getStep(), offset, isHalf), sv.getValue());
		}

		for (int partNum=0; partNum<bdx.getPartNum(); partNum++) {
			Part oldPart = bdx.getPart(partNum);
			InstrumentType type = oldPart.getType();
			if (type==InstrumentType.NONE) {
				continue;
			}
			Part newPart = new Part(type);
			List<Note> noteList = oldPart.getNoteList();
			for (int i=0; i<isHalf.size(); i++) {
				if (isHalf.get(i)) {
					// �����̒�����2�{�ɂ���
					if (type==InstrumentType.DRUMS) {
						int noteNum = 8;
						int[] upperNoteNum = new int[noteNum];
						int[] lowerNoteNum = new int[noteNum];
						Arrays.fill(upperNoteNum, DrumNote.REST);
						Arrays.fill(lowerNoteNum, DrumNote.REST);
						int upperNote = ((DrumNote) noteList.get(4*i)).getUpperNoteNum();
						int lowerNote = ((DrumNote) noteList.get(4*i)).getLowerNoteNum();
						if (upperNote==DrumNote.GROUP) {
							upperNoteNum[0] = DrumNote.GROUP;
							upperNoteNum[1] = ((DrumNote) noteList.get(4*i+1)).getUpperNoteNum();
							upperNoteNum[3] = ((DrumNote) noteList.get(4*i+2)).getUpperNoteNum();
							upperNoteNum[4] = DrumNote.GROUP;
							upperNoteNum[6] = ((DrumNote) noteList.get(4*i+3)).getUpperNoteNum();
						} else {
							for (int k=0; k<4; k++) {
								upperNoteNum[2*k] = ((DrumNote) noteList.get(4*i+k)).getUpperNoteNum();
							}
						}
						if (lowerNote==DrumNote.GROUP) {
							lowerNoteNum[0] = DrumNote.GROUP;
							lowerNoteNum[1] = ((DrumNote) noteList.get(4*i+1)).getLowerNoteNum();
							lowerNoteNum[3] = ((DrumNote) noteList.get(4*i+2)).getLowerNoteNum();
							lowerNoteNum[4] = DrumNote.GROUP;
							lowerNoteNum[6] = ((DrumNote) noteList.get(4*i+3)).getLowerNoteNum();
						} else {
							for (int k=0; k<4; k++) {
								lowerNoteNum[2*k] = ((DrumNote) noteList.get(4*i+k)).getLowerNoteNum();
							}
						}
						for (int k=0; k<noteNum; k++) {
							newPart.addNote(DrumNote.getInstanceWithNote(upperNoteNum[k], lowerNoteNum[k]));
						}
					} else {  // �h�����ȊO
						if (((SingleNote) noteList.get(4*i)).isGroup()) {  // 3�A
							newPart.addNote(Note.getInstance(SingleNote.GROUP, type));
							Note note = noteList.get(4*i+1);
							newPart.addNote(note);
							if (((SingleNote) note).isNote()) {
								newPart.addNote(Note.getInstance(note.getNoteNum()+SingleNote.EXTEND, type));
							} else {
								newPart.addNote(note);
							}
							note = noteList.get(4*i+2);
							newPart.addNote(note);
							newPart.addNote(Note.getInstance(SingleNote.GROUP, type));
							if (((SingleNote) note).isNote()) {
								newPart.addNote(Note.getInstance(note.getNoteNum()+SingleNote.EXTEND, type));
							} else {
								newPart.addNote(note);
							}
							note = noteList.get(4*i+3);
							newPart.addNote(note);
							if (((SingleNote) note).isNote()) {
								newPart.addNote(Note.getInstance(note.getNoteNum()+SingleNote.EXTEND, type));
							} else {
								newPart.addNote(note);
							}
						} else {
							for (int k=0; k<4; k++) {
								Note note = noteList.get(4*i+k);
								newPart.addNote(note);
								if (((SingleNote) note).isNote()) {
									newPart.addNote(Note.getInstance(note.getNoteNum()+SingleNote.EXTEND, type));
								} else {
									newPart.addNote(note);
								}
							}
						}
					}
				} else {  // �ʏ푬�x
					for (int k=0; k<4; k++) {
						newPart.addNote(noteList.get(4*i+k));
					}
				}
			}
			// �e�p�[�g��񍷂��ւ�
			// ����
			List<StepValue<Integer>> newVolume = calcStepValue(oldPart.getVolume(), offset, isHalf);
			for (StepValue<Integer> sv : newVolume) {
				newPart.addVolume(sv.getStep(), sv.getValue());
			}
			// �P���p�[�g�̂�
			if (oldPart.getType()==InstrumentType.SINGLE) {
				// �x�[�X
				List<StepValue<Integer>> newBass = calcStepValue(oldPart.getBass(), offset, isHalf);
				for (StepValue<Integer> sv : newBass) {
					newPart.addBass(sv.getStep(), sv.getValue());
				}
				// �{�^��
				List<StepValue<Integer>> newButton = calcStepValue(oldPart.getButton(), offset, isHalf);
				for (StepValue<Integer> sv : newButton) {
					newPart.addButton(sv.getStep(), sv.getValue());
				}
				// �����L��
				List<StepValue<Clef>> newClef = calcStepValue(oldPart.getClef(), offset, isHalf);
				for (StepValue<Clef> sv : newClef) {
					newPart.addClef(sv.getStep(), sv.getValue());
				}
			}
			if (oldPart.getType()==InstrumentType.GUITAR || oldPart.getType()==InstrumentType.PIANO) {
				// �R�[�h�Z�b�g
				List<StepValue<List<Chord>>> newChordSet = calcStepValue(oldPart.getChordSet(), offset, isHalf);
				for (StepValue<List<Chord>> sv : newChordSet) {
					newPart.addChordSet(sv.getStep(), sv.getValue());
				}
			}
			if (oldPart.getType()==InstrumentType.PIANO) {
				// �{�C�V���O
				List<StepValue<Voicing>> newVoicing = calcStepValue(oldPart.getVoicing(), offset, isHalf);
				for (StepValue<Voicing> sv : newVoicing) {
					newPart.addVoicing(sv.getStep(), sv.getValue());
				}
			}
			newPart = joinPartData(newPart, oldPart);
			copy.setPart(partNum, newPart);
		}
		return copy;
	}

	private List<StepValue<Integer>> calcTempo(List<StepValue<Integer>> tempoList,
			List<Integer> offset, List<Boolean> isHalf) {
		int lastTempo = 0;
		int tempoIndex = 0;
		List<StepValue<Integer>> newTempo = new ArrayList<StepValue<Integer>>();
		for (int i=0; i<isHalf.size(); i++) {  // �e���|
			int step = BDX.TIME_BASE*i;
			int value = 0;
			for (int ti=tempoIndex; ti<tempoList.size(); ti++) {
				StepValue<Integer> sv = tempoList.get(ti);
				int st = sv.getStep() - step;
				if (st<=0) {
					value = sv.getValue();
				} else {
					tempoIndex = ti - 1;
					break;
				}
			}
			if (isHalf.get(i)) {
				value *= 2;
			}
			if (value!=lastTempo) {
				newTempo.add(new StepValue<Integer>(step+BDX.TIME_BASE*offset.get(i), value));
				lastTempo = value;
			}
			for (int ti=tempoIndex+1; ti<tempoList.size(); ti++) {
				StepValue<Integer> sv = tempoList.get(ti);
				int st = sv.getStep() - step;
				value = sv.getValue();
				if (0<st && st<BDX.TIME_BASE) {
					if (isHalf.get(i)) {
						st *= 2;
						value *= 2;
					}
					if (value!=lastTempo) {
						newTempo.add(new StepValue<Integer>(step+BDX.TIME_BASE*offset.get(i)+st, value));
						lastTempo = value;
					}
				} else {
					break;
				}
			}
		}
		return newTempo;
	}

	private <V> List<StepValue<V>> calcStepValue(List<StepValue<V>> stepList,
			List<Integer> offset, List<Boolean> isHalf) {
		List<StepValue<V>> newValue = new ArrayList<StepValue<V>>();
		for (StepValue<V> sv : stepList) {
			int step = calcNewStep(sv.getStep(), offset, isHalf);
			newValue.add(new StepValue<V>(step, sv.getValue()));
		}
		return newValue;
	}

	private int calcNewStep(int oldStep, List<Integer> offset, List<Boolean> isHalf) {
		int step = oldStep;
		int index = step / BDX.TIME_BASE;
		int st = step % BDX.TIME_BASE;
		if (isHalf.get(index)) {
			st *= 2;
		}
		return BDX.TIME_BASE*index + BDX.TIME_BASE*offset.get(index) + st;
	}

	private Map<Integer, StepValue<Integer>> createTempoMap(BDX bdx, List<Integer> average) {
		// �e���|->(����,����臒l)
		Map<Integer, StepValue<Integer>> retTempoMap = new HashMap<Integer, StepValue<Integer>>();
		// �e���|->(����,����臒l���v)
		Map<Integer, StepValue<Long>> tempoMap = new HashMap<Integer, StepValue<Long>>();
		for (int partNum=0; partNum<bdx.getPartNum(); partNum++) {
			Part p = bdx.getPart(partNum);
			if (p.getType()==InstrumentType.NONE) {
				continue;
			}
			List<Note> noteList = p.getNoteList();
			for (int i=0; i+3<noteList.size(); i+=4) {
				int tempo = average.get(i/4);
				Note note1 = noteList.get(i);
				Note note2 = noteList.get(i+1);
				Note note3 = noteList.get(i+2);
				Note note4 = noteList.get(i+3);
				int returnValue = calcHalfTempo(p.getType(), note1, note2, note3, note4);
				if (returnValue>=0) {
					StepValue<Long> sv = tempoMap.get(tempo);
					if (sv==null) {
						sv = new StepValue<Long>(1, (long) returnValue);
						tempoMap.put(tempo, sv);
					} else {
						sv.setStep(sv.getStep()+1);
						sv.setValue(sv.getValue()+returnValue);
					}
				}
			}
		}
		for (Iterator<Integer> ite=tempoMap.keySet().iterator(); ite.hasNext(); ) {
			int tempo = ite.next();
			StepValue<Long> sv = tempoMap.get(tempo);
			int step = sv.getStep();
			long value = sv.getValue();
			if (step!=0) {
				value /= step;
				retTempoMap.put(tempo, new StepValue<Integer>(step, (int) value));
			}
		}
		return retTempoMap;
	}

	/**
	 * 1���̔���臒l�B���̐��ł��̔����v�Z�Ɋ܂߂Ȃ��B
	 * @param type
	 * @param n1
	 * @param n2
	 * @param n3
	 * @param n4
	 * @return
	 */
	private int calcHalfTempo(InstrumentType type, Note n1, Note n2, Note n3, Note n4) {
		boolean[] flag = new boolean[] {false, false, false, false};
		if (type==InstrumentType.DRUMS) {
			int[] upperNote = new int[] {
					((DrumNote)n1).getUpperNoteNum(),
					((DrumNote)n2).getUpperNoteNum(),
					((DrumNote)n3).getUpperNoteNum(),
					((DrumNote)n4).getUpperNoteNum()
			};
			int[] lowerNote = new int[] {
					((DrumNote)n1).getLowerNoteNum(),
					((DrumNote)n2).getLowerNoteNum(),
					((DrumNote)n3).getLowerNoteNum(),
					((DrumNote)n4).getLowerNoteNum()
			};
			if (n1.getNoteNum()==SingleNote.GROUP) {  // �㉺�ɎO�A��
				for (int i=0; i<flag.length-1; i++) {
					flag[i] = (upperNote[i+1]!=DrumNote.REST || lowerNote[i+1]!=DrumNote.REST);
				}
				if (flag[1] || flag[2]) {
					return -1;
				}
			} else if (upperNote[0]==DrumNote.GROUP) {
				for (int i=0; i<flag.length; i++) {
					flag[i] = (lowerNote[i]!=DrumNote.REST);
				}
				flag[0] = (flag[0] || upperNote[1]!=DrumNote.REST);
				if (flag[1] || flag[2] || flag[3]) {
					if (upperNote[2]!=DrumNote.REST) {
						if (flag[1]==false) {
							flag[1] = true;
						} else {
							flag[2] = true;
						}
					}
					if (upperNote[3]!=DrumNote.REST) {
						if (flag[3]==false) {
							flag[3] = true;
						} else {
							flag[2] = true;
						}
					}
				} else if (upperNote[2]!=DrumNote.REST || upperNote[3]!=DrumNote.REST) {
					return -1;
				}
			} else if (lowerNote[0]==DrumNote.GROUP) {
				for (int i=0; i<flag.length; i++) {
					flag[i] = (upperNote[i]!=DrumNote.REST);
				}
				flag[0] = (flag[0] || lowerNote[1]!=DrumNote.REST);
				if (flag[1] || flag[2] || flag[3]) {
					if (lowerNote[2]!=DrumNote.REST) {
						if (flag[1]==false) {
							flag[1] = true;
						} else {
							flag[2] = true;
						}
					}
					if (lowerNote[3]!=DrumNote.REST) {
						if (flag[3]==false) {
							flag[3] = true;
						} else {
							flag[2] = true;
						}
					}
				} else if (lowerNote[2]!=DrumNote.REST || lowerNote[3]!=DrumNote.REST) {
					return -1;
				}
			} else {
				for (int i=0; i<flag.length; i++) {
					flag[i] = (upperNote[i]!=DrumNote.REST || lowerNote[i]!=DrumNote.REST);
				}
			}
		} else {  // �h�����ȊO
			Note[] note = new Note[] {n1, n2, n3, n4};
			if (n1.getNoteNum()==SingleNote.GROUP) {
				for (int i=0; i<flag.length-1; i++) {
					flag[i] = (((SingleNote) note[i+1]).isNote());
				}
				if (flag[1] || flag[2]) {
					return -1;
				}
			} else {
				for (int i=0; i<flag.length; i++) {
					flag[i] = (((SingleNote) note[i]).isNote());
				}
			}
		}
		int num = 0;
		for (int i=0; i<flag.length; i++) {
			if (flag[i]) {
				num += (8 >> i);
			}
		}
		if (1<=num && num<=15) {
			return _shuffleValues[num];
		}
		return -1;
	}

	private Part joinPartData(Part newPart, Part oldPart) {
		Part copy = new Part(oldPart.getType());
		copy.setInstrument(oldPart.getInstrument());
		copy.setCloneNum(oldPart.getCloneNum());
		PlayLevel[] levels = {PlayLevel.BEGINNER, PlayLevel.AMATEUR, PlayLevel.PRO, PlayLevel.MASTER};
		for (PlayLevel level : levels) {
			copy.setPlayLevel(level, oldPart.getPlayLevel(level));
		}
		List<Note> noteList = newPart.getNoteList();
		for (int i=0; i<noteList.size(); i++) {
			Note n = noteList.get(i);
			if (n==null) {
				if (oldPart.getType()==InstrumentType.DRUMS) {
					n = DrumNote.getInstanceWithNote(DrumNote.REST, DrumNote.REST);
				} else {
					n = Note.getInstance(SingleNote.REST, oldPart.getType());
				}
			}
			copy.addNote(n);
		}
		copy.setPartVolume(oldPart.getPartVolume());
		copy.setPan(oldPart.getPan());
		List<StepValue<Integer>> volume = newPart.getVolume();
		for (StepValue<Integer> sv : volume) {
			copy.addVolume(sv.getStep(), sv.getValue());
		}
		if (oldPart.getType()==InstrumentType.SINGLE) {
			List<StepValue<Integer>> bass = newPart.getBass();
			for (StepValue<Integer> sv : bass) {
				copy.addBass(sv.getStep(), sv.getValue());
			}
			List<StepValue<Integer>> button = newPart.getButton();
			for (StepValue<Integer> sv : button) {
				copy.addButton(sv.getStep(), sv.getValue());
			}
			List<StepValue<Clef>> clef = newPart.getClef();
			for (StepValue<Clef> sv : clef) {
				copy.addClef(sv.getStep(), sv.getValue());
			}
		}
		if (oldPart.getType()==InstrumentType.GUITAR || oldPart.getType()==InstrumentType.PIANO) {
			List<StepValue<List<Chord>>> chordSet = newPart.getChordSet();
			for (StepValue<List<Chord>> sv : chordSet) {
				copy.addChordSet(sv.getStep(), sv.getValue());
			}
		}
		if (oldPart.getType()==InstrumentType.PIANO) {
			List<StepValue<Voicing>> voicing = newPart.getVoicing();
			for (StepValue<Voicing> sv : voicing) {
				copy.addVoicing(sv.getStep(), sv.getValue());
			}
		}
		copy.setToneAttack(oldPart.getToneAttack());
		copy.setToneDecay(oldPart.getToneDecay());
		copy.setToneSustain(oldPart.getToneSustain());
		copy.setToneRelease(oldPart.getToneRelease());
		copy.setVibratoShape(oldPart.getVibratoShape());
		copy.setVibratoHold(oldPart.getVibratoHold());
		copy.setVibratoDelay(oldPart.getVibratoDelay());
		copy.setVibratoDepth(oldPart.getVibratoDepth());
		copy.setVibratoSpeed(oldPart.getVibratoSpeed());
		copy.setToneEffectType(oldPart.getToneEffectType());
		copy.setToneEffectValue(oldPart.getToneEffectValue());
		return copy;
	}

	@Override
	protected void arrangeBDX(BDX bdx) {
		// �^�C�~���O�̃Y���𒲐�����
		List<Integer> timing = new ArrayList<Integer>();
		Part mainPart = getMainPart(bdx);
		if (mainPart!=null && mainPart.getType()==InstrumentType.SINGLE) {
			// �̎����蓖�Ă������
			if (bdx.isLyrics()) {
				timing.clear();
				List<StepValue<String>> lyricT = bdx.getLyricTiming();
				for (StepValue<String> sv : lyricT) {
					timing.add(sv.getStep());
				}
				timing = calcNewTiming(mainPart, timing, true);
				assert(timing.size()==lyricT.size());
				for (int i=0; i<timing.size(); i++) {
					lyricT.get(i).setStep(timing.get(i));
				}
			}
			// �R�[�h�z�u������
			timing.clear();
			List<StepValue<Chord>> chordT = bdx.getChordTiming();
			for (StepValue<Chord> sv : chordT) {
				timing.add(sv.getStep());
			}
			timing = calcNewTiming(mainPart, timing);
			assert(timing.size()==chordT.size());
			for (int i=0; i<timing.size(); i++) {
				chordT.get(i).setStep(timing.get(i));
			}
		}
		bdx.setLyricLines(null);  // ���Ή�
	}

	@Override
	protected void arrangeSinglePart(Part p) {
		List<Integer> timing = new ArrayList<Integer>();

		timing.clear();
		List<StepValue<Integer>> bass = p.getBass();
		for (StepValue<Integer> sv : bass) {
			timing.add(sv.getStep());
		}
		timing = calcNewTiming(p, timing);
		assert(timing.size()==bass.size());
		for (int i=0; i<timing.size(); i++) {
			bass.get(i).setStep(timing.get(i));
		}

		timing.clear();
		List<StepValue<Integer>> button = p.getButton();
		for (StepValue<Integer> sv : button) {
			timing.add(sv.getStep());
		}
		timing = calcNewTiming(p, timing);
		assert(timing.size()==button.size());
		for (int i=0; i<timing.size(); i++) {
			button.get(i).setStep(timing.get(i));
		}

		shuffleSingleNotes(p);
	}

	private void shuffleSingleNotes(Part p) {
		// �P���E�M�^�[�R�[�h�E�s�A�m�a���̓��Y���̕ς���������
		List<Integer> timing = new ArrayList<Integer>();

		timing.clear();
		List<StepValue<Integer>> volume = p.getVolume();
		for (StepValue<Integer> sv : volume) {
			timing.add(sv.getStep());
		}
		timing = calcNewTiming(p, timing);
		assert(timing.size()==volume.size());
		for (int i=0; i<timing.size(); i++) {
			volume.get(i).setStep(timing.get(i));
		}

		List<Note> noteList = p.getNoteList();
		for (int i=0; i+3<noteList.size(); i+=4) {
			List<Note> list = new ArrayList<Note>();
			int flag = 0;
			for (int j=0; j<4; j++) {
				SingleNote n = (SingleNote) noteList.get(i+j);
				list.add(n);
				if (n.isNote()) {
					flag += (0x8 >> j);
				}
			}
			if (((SingleNote) list.get(0)).isGroup()) {
				continue;
			}
			list.remove(removeNoteIndex(flag));
			list.add(0, Note.getInstance(SingleNote.GROUP, p.getType()));
			for (int j=0; j<4; j++) {
				p.setNote(i+j, list.get(j));
			}
		}
	}

	private List<Integer> calcNewTiming(Part p, List<Integer> timing) {
		return calcNewTiming(p, timing, false);
	}

	private List<Integer> calcNewTiming(Part p, List<Integer> timing, boolean melody) {
		if (p.getType()==InstrumentType.NONE) {
			return timing;
		}
		List<Integer> newTiming = new ArrayList<Integer>();
		List<Note> noteList = p.getNoteList();
		for (int step : timing) {
			int noteIndex = ((step/3) / 4) * 4;  // step�������Ă��锏�́A�ŏ��̉�����index
			if (noteList.size()<=noteIndex || ((SingleNote) noteList.get(noteIndex)).isGroup()) {  // �O�A���Ȃ�ύX�Ȃ�
				newTiming.add(step);
				continue;
			}
			int localStep = step % 12;
			int newStep = step - localStep;
			int flag = 0;			
			for (int i=0; i<4; i++) {
				SingleNote n = (SingleNote) noteList.get(noteIndex+i);
				if (n.isNote()) {
					flag += (0x8 >> i);
				}
			}
			int index = removeNoteIndex(flag);
			// �̎����蓖�Ă̂݁A0,3,6,9->0,4,8�ȊO���������
			switch (index) {
			case 0:  // 0,1,2->0,0,0  3,4,5->0,1,2  6,7,8->4,5,6  9,a,b->8,9,a
				if (localStep<3) {
					localStep = 0;
				} else if (localStep<6) {
					localStep -= 3;
				} else if (localStep<9) {
					localStep -= 2;
				} else {
					localStep -= 1;
				}
				break;
			case 1:  // 0,1,2->0,1,2  3,4,5->3,3,3  6,7,8->4,5,6  9,a,b->8,9,a
				if (localStep<3) {
				} else if (localStep<6) {
					if (melody) {
						localStep = 3;
					} else {
						localStep = 4;
					}
				} else if (localStep<9) {
					localStep -= 2;
				} else {
					localStep -= 1;
				}
				break;
			case 2:  // 0,1,2->0,1,2  3,4,5->4,5,6  6,7,8->7,7,7  9,a,b->8,9,a
				if (localStep<3) {
				} else if (localStep<6) {
					localStep += 1;
				} else if (localStep<9) {
					if (melody) {
						localStep = 7;
					} else {
						localStep = 8;
					}
				} else {
					localStep -= 1;
				}
				break;
			case 3:  // 0,1,2->0,1,2  3,4,5->4,5,6  6,7,8->8,9,a  9,a,b->b,b,b
				if (localStep<3) {
				} else if (localStep<6) {
					localStep += 1;
				} else if (localStep<9) {
					if (melody) {
						localStep += 2;
					} else {
						localStep = 8;
					}
				} else {
					if (melody) {
						localStep = 11;
					} else {
						localStep = 8;
					}
				}
				break;
			default:
				break;
			}
			newStep += localStep;
			newTiming.add(newStep);
		}
		return newTiming;
	}

	private int removeNoteIndex(int flag) {
		switch (flag) {
		case 0b0111: // ��������
			return 0;
		case 0b0011: // ��������
		case 0b1011: // ��������
			return 1;
		case 0b0001: // ��������
		case 0b1001: // ��������
		case 0b0101: // ��������
		case 0b1101: // ��������
		case 0b1111: // ��������
			return 2;
		case 0b0000: // ��������
		case 0b1000: // ��������
		case 0b0100: // ��������
		case 0b0010: // ��������
		case 0b1100: // ��������
		case 0b1010: // ��������
		case 0b0110: // ��������
		case 0b1110: // ��������
		default:
			return 3;
		}
	}

	@Override
	protected void arrangeGuitarPart(Part p) {
		List<Integer> timing = new ArrayList<Integer>();

		timing.clear();
		List<StepValue<List<Chord>>> chordSet = p.getChordSet();
		for (StepValue<List<Chord>> sv : chordSet) {
			timing.add(sv.getStep());
		}
		timing = calcNewTiming(p, timing);
		assert(timing.size()==chordSet.size());
		for (int i=0; i<timing.size(); i++) {
			chordSet.get(i).setStep(timing.get(i));
		}

		shuffleSingleNotes(p);
	}

	@Override
	protected void arrangePianoPart(Part p) {
		List<Integer> timing = new ArrayList<Integer>();

		timing.clear();
		List<StepValue<Voicing>> voicing = p.getVoicing();
		for (StepValue<Voicing> sv : voicing) {
			timing.add(sv.getStep());
		}
		timing = calcNewTiming(p, timing);
		assert(timing.size()==voicing.size());
		for (int i=0; i<timing.size(); i++) {
			voicing.get(i).setStep(timing.get(i));
		}

		timing.clear();
		List<StepValue<List<Chord>>> chordSet = p.getChordSet();
		for (StepValue<List<Chord>> sv : chordSet) {
			timing.add(sv.getStep());
		}
		timing = calcNewTiming(p, timing);
		assert(timing.size()==chordSet.size());
		for (int i=0; i<timing.size(); i++) {
			chordSet.get(i).setStep(timing.get(i));
		}

		shuffleSingleNotes(p);
	}

	@Override
	protected void arrangeDrumPart(Part p) {
		List<Note> noteList = p.getNoteList();
		for (int i=0; i+3<noteList.size(); i+=4) {
			List<Integer> upperlist = new ArrayList<Integer>();
			List<Integer> lowerlist = new ArrayList<Integer>();
			int upperFlag = 0;
			int lowerFlag = 0;
			for (int j=0; j<4; j++) {
				DrumNote n = (DrumNote) noteList.get(i+j);
				int upper = n.getUpperNoteNum();
				int lower = n.getLowerNoteNum();
				upperlist.add(upper);
				lowerlist.add(lower);
				if (DrumNote.isNote(upper)) {
					upperFlag += (0x8 >> j);
				}
				if (DrumNote.isNote(lower)) {
					lowerFlag += (0x8 >> j);
				}
			}
			if (upperlist.get(0)!=DrumNote.GROUP) {
				upperlist.remove(removeNoteIndex(upperFlag));
				upperlist.add(0, DrumNote.GROUP);
			}
			if (lowerlist.get(0)!=DrumNote.GROUP) {
				lowerlist.remove(removeNoteIndex(lowerFlag));
				lowerlist.add(0, DrumNote.GROUP);
			}
			for (int j=0; j<4; j++) {
				DrumNote n = DrumNote.getInstanceWithNote(
						upperlist.get(j),
						lowerlist.get(j));
				p.setNote(i+j, n);
			}
		}
	}

	@Override
	protected void arrangePartBinary(List<Integer> binary, BDX bdx, int partNum) {
		super.arrangePartBinary(binary, bdx, partNum);
		Part p = bdx.getPart(partNum);
		if (p.getType()==InstrumentType.GUITAR) {
			BinaryUtil.arrangeGuitarButtonBinary(binary, p);
		}
	}

}
