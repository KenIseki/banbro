package banbro.model.arrange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import banbro.model.bdx.BDX;
import banbro.model.bdx.BDXInstrument;
import banbro.model.bdx.BDXUtil;
import banbro.model.bdx.BinaryUtil;
import banbro.model.bdx.Guitar;
import banbro.model.bdx.InstrumentType;
import banbro.model.bdx.Note;
import banbro.model.bdx.Part;
import banbro.model.bdx.Piano;
import banbro.model.bdx.StepValue;
import banbro.model.bdx.Voicing;

/**
 * �A�����W���s���N���X
 * �S�ẴA�����W�͂��̃N���X���p�����č쐬����
 */
public abstract class AbstractArrange {

	private BDX _bdx;
	private boolean _isArrangeTempo;
	private boolean _isBDX;

	/**
	 * �T�u�N���X�͈����Ȃ��̃R���X�g���N�^��������
	 * @see ArrangeFactory#createInstance(PerformanceStyle)
	 */
	protected AbstractArrange() {
		this(true, true);
	}
	
	/**
	 * �A�����W�Ώۂ�BDX��{@link #setBDX(BDX)}�Œǉ�����
	 * ���̏���{@link #setOption(String, Object)}�Œǉ�����
	 * @param isArrangeTempo �e���|�ύX������Ȃ�true
	 * @param isBDX BDX�t�@�C���͈̔͂Ɏ��߂�Ȃ�true
	 * @return
	 * @see #setBDX(BDX)
	 * @see #setOption(String, Object)
	 */
	protected AbstractArrange(boolean isArrangeTempo, boolean isBDX) {
		_isArrangeTempo = isArrangeTempo;
		_isBDX = isBDX;
	}
	
	protected BDX getBDX() {
		return _bdx;
	}
	protected boolean isArrangeTempo() {
		return _isArrangeTempo;
	}
	protected boolean isBDX() {
		return _isBDX;
	}

	public synchronized void setBDX(BDX bdx) {
		_bdx = bdx;
	}
	public synchronized void setIsArrangeTempo(boolean isArrangeTempo) {
		_isArrangeTempo = isArrangeTempo;
	}
	public synchronized void setIsBDX(boolean isBDX) {
		_isBDX = isBDX;
	}

	/**
	 * �R���X�g���N�^�œn���Ȃ����̏���ǉ�����
	 * �f�t�H���g�͉������Ȃ�
	 * @param optionName
	 * @param optionValue
	 */
	public synchronized void setOption(String optionName, Object optionValue) {}

	/**
	 * �A�����W���s��
	 * 1��BDX�ɑ΂���1��̂݌Ăяo����
	 * @return �A�����W���BDX
	 * @see #setBDX(BDX)
	 */
	public synchronized final BDX arrange() {
		if (_bdx==null) {
			throw new IllegalStateException("���ɕϊ��ς݂�BDX������܂���B�V����BDX���Z�b�g���Ă��������B");
		}
		BDX bdx = doArrange(_bdx);
		_bdx = null;
		return bdx;
	}

	/**
	 * �A�����W�̗���
	 * @param bdx
	 * @return
	 */
	protected BDX doArrange(BDX bdx) {
		BDX copy = bdx.clone();
		arrangeBDX(copy);
		double tempoAverage = (bdx.getTimeNum()) * 60 / BDXUtil.calcPlayTime(bdx);
		if (isArrangeTempo() && !isBDX()) {
			arrangeTempo(copy.getTempo(), tempoAverage);
		}
		Guitar guitar = copy.getGuitar();
		if (guitar!=null) {
			arrangeGuitar(guitar);
		}
		Piano piano = copy.getPiano();
		if (piano!=null) {
			arrangePiano(piano);
		}
		Part mainPart = getMainPart(copy);
		for (int i=0; i<copy.getPartNum(); i++) {
			Part p = copy.getPart(i);
			if (p.getType()==InstrumentType.NONE) {
				continue;
			}
			if (p==mainPart) {
				arrangeMainPart(p);
			} else {
				arrangePart(p);
			}
			switch (p.getType()) {
			case SINGLE:
				arrangeSinglePart(p);
				break;
			case DRUMS:
				arrangeDrumPart(p);
				break;
			case GUITAR:
				arrangeGuitarPart(p);
				break;
			case PIANO:
				arrangePianoPart(p);
			default:
				continue;
			}
		}
		copy.trim();
		return copy;
	}

	/**
	 * �S�̂ɉe�����鍀�ڂ�ύX����
	 * �f�t�H���g�͉������Ȃ�
	 * @param bdx
	 */
	protected void arrangeBDX(BDX bdx) {}

	/**
	 * ���t�C���[�W�ɂ��e���|�ύX
	 * �f�t�H���g�͉������Ȃ�
	 * @param tempo �ύX�O�̃e���|
	 * @param tempoAverage
	 */
	protected void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage) {}

	/**
	 * �e���|��rate%�ɕύX����
	 * @param tempo �ύX�O�̃e���|
	 * @param tempoAverage ���σe���|
	 * @param rate �{��%
	 * @param min �ŏ��e���|
	 * @param max �ő�e���|
	 * @see #arrangeTempo(List, BDX)
	 */
	protected final void arrangeTempo(List<StepValue<Integer>> tempo, double tempoAverage, int rate, double min, double max) {
		if (isArrangeTempo()==false || isBDX()==true) {
			return;
		}
		if ((tempoAverage*rate/100) < min) {
			rate = (int)Math.ceil(min * 100 / tempoAverage);
		} else if ((tempoAverage*rate/100) > max) {
			rate = (int)Math.ceil(max * 100 / tempoAverage);
		}
		for (StepValue<Integer> sv : tempo) {
			int value = sv.getValue();
			if (value>1) {
				value = Math.max((value * rate / 100), 1);
				sv.setValue(value);
			}
		}
	}

	/**
	 * @param bdx
	 * @return ���C���p�[�g�i�����f�B�p�[�g�܂��͔ԍ����ŏ��̒P���p�[�g�j
	 */
	protected final Part getMainPart(BDX bdx) {
		Part mainPart = null;
		mainPart = bdx.getPart(bdx.getMelodyPartNum());
		if (mainPart.getType()!=InstrumentType.SINGLE) {
			mainPart = null;
		}
		if (mainPart==null) {
			for (int i=0; i<bdx.getPartNum(); i++) {
				Part p = bdx.getPart(i);
				if (p.getType()==InstrumentType.SINGLE) {
					mainPart = p;
					break;
				}
			}
		}
		return mainPart;
	}

	/**
	 * �S���t�^�C�v�ɋ��ʂ���A1�p�[�g�̓��e��ύX����
	 * �f�t�H���g�͉������Ȃ�
	 * @param p
	 */
	protected void arrangePart(Part p) {}

	/**
	 * ���C���p�[�g�̓��e��ύX����
	 * �f�t�H���g��{@link #arrangePart(Part)}
	 * @param p
	 */
	protected void arrangeMainPart(Part p) {
		arrangePart(p);
	}

	/**
	 * �P���p�[�g�̓��e��ύX����
	 * �f�t�H���g�͉������Ȃ�
	 * @param p
	 */
	protected void arrangeSinglePart(Part p) {}

	/**
	 * �M�^�[�R�[�h�p�[�g�̓��e��ύX����
	 * �f�t�H���g�͉������Ȃ�
	 * @param p
	 */
	protected void arrangeGuitarPart(Part p) {}

	/**
	 * �s�A�m�a���p�[�g�̓��e��ύX����
	 * �f�t�H���g�͉������Ȃ�
	 * @param p
	 */
	protected void arrangePianoPart(Part p) {}

	/**
	 * �h�����p�[�g�̓��e��ύX����
	 * �f�t�H���g�͉������Ȃ�
	 * @param p
	 */
	protected void arrangeDrumPart(Part p) {}

	/**
	 * �M�^�[�R�[�h�p�[�g�ɉe������ݒ�
	 * �I���W�i���R�[�h
	 * �f�t�H���g�͉������Ȃ�
	 * @param g
	 */
	protected void arrangeGuitar(Guitar g) {}

	/**
	 * �s�A�m�a���p�[�g�ɉe������ݒ�
	 * �I���W�i���R�[�h�E�{�C�V���O
	 * �������A�����Ń{�C�V���O��ύX���Ă����t�ɉe�����Ȃ�
	 * �f�t�H���g�͉������Ȃ�
	 * @param p
	 * @see #arrangePianoPart(Part)
	 */
	protected void arrangePiano(Piano p) {}

	/**
	 * binary������������
	 * @param binary ���̃o�C�i��
	 * @param bdx �A�����W���bdx
	 * @see #arrange()
	 * @see #arrangePartBinary(List, BDX, int)
	 */
	public synchronized void arrangeBinary(List<Integer> binary, BDX bdx) {
		int b = 0;
		// �}�X�^�[�{�����[��
		int volume = bdx.getMasterVolume();
		if (volume<100) {
			b = BinaryUtil.VolumeValue[volume] + 0x80;
		} else {
			b = BinaryUtil.VolumeValue[volume-100];
		}
		binary.set(0x00B7, b);
		if (bdx.isLyrics()) {
			// �S��:�̎��A�̎����蓖��
			List<String> lyric = bdx.getLyric();
			List<Integer> lyricBinary = new ArrayList<Integer>();
			for (String st : lyric) {
				lyricBinary.addAll(BinaryUtil.stringToByte(st));
				lyricBinary.add(0x00);
			}
			assert(lyricBinary.size()<=0x0800);  // �ő�2048byte
			List<StepValue<String>> lyricTiming = bdx.getLyricTiming();
			int step = 0;
			int timingIndex = 0;
			boolean kigou = false;
			for (int i=0; i<lyricBinary.size(); i++) {
				b = lyricBinary.get(i);
				List<Integer> stepBinary;
				// ����ȕ���(���s�A�X�y�[�X�A���_�Ȃ�)��+0x4000
				// 0x80�͋L���t���O
				if (BinaryUtil.isSpecialChar(b) && b!=0x80) {
					stepBinary = BinaryUtil.to2byteBinary(step + 0x4000);
					kigou = false;
				} else {
					if (kigou==false) {
						if (timingIndex>=lyricTiming.size()) {
							break;
						}
						step = lyricTiming.get(timingIndex).getStep();
						timingIndex++;
					}
					if (b==0x80) {
						stepBinary = BinaryUtil.to2byteBinary(step + 0x4000);
						kigou = true;
					} else {
						stepBinary = BinaryUtil.to2byteBinary(step);
						kigou = false;
					}
				}
				// �̎�
				binary.set(0x4E08 + i, b);
				// �̎����蓖��
				binary.set(0x5608 + 2*i, stepBinary.get(0));
				binary.set(0x5608 + 2*i + 1, stepBinary.get(1));
			}
		}
		for (int i=0; i<8; i++) {  // 8:�p�[�g��
			arrangePartBinary(binary, bdx, i);
		}
	}

	/**
	 * 1�p�[�g�̕�����������������
	 * @param binary
	 * @param bdx
	 * @param partNum
	 * @see #arrangeBinary(List, BDX)
	 */
	protected void arrangePartBinary(List<Integer> binary, BDX bdx, int partNum) {
		Part p = bdx.getPart(partNum);
		int instNum = p.getInstrument().getNum();
		final InstrumentType type = p.getType();
		binary.set(0xCB + 16*partNum, type.intValue());
		if (type==InstrumentType.NONE || p.getInstrument()==BDXInstrument.NONE) {
			return;
		}
		// �y��ύX
		// �M�^�[�R�[�h�̏ꍇ+0x30�A�s�A�m�a���̏ꍇ+0x59�����
		// ������DX�Œǉ����ꂽ�y��(0x83�ȍ~)�̓M�^�[�R�[�h�̏ꍇ+0x0A�A�s�A�m�a���̏ꍇ+0x14�����
		if (type==InstrumentType.GUITAR) {
			if (instNum<0x83) {
				instNum += 0x30;
			} else {
				instNum += 0x0A;
			}
		} else if (type==InstrumentType.PIANO) {
			if (instNum<0x83) {
				instNum += 0x59;
			} else {
				instNum += 0x14;
			}
		}
		binary.set(0x00CA + 16*partNum, instNum);
		// ����
		List<Note> noteList = p.getNoteList();
		int noteSize = Math.min(noteList.size(), 2048);
		for (int j=0; j<noteSize; j++) {
			int noteNum = noteList.get(j).getNoteNum();
			binary.set(0x0248 + 2048*partNum + j, noteNum);
		}
		// Step
		Set<Integer> stepSet = new HashSet<Integer>();
		for (StepValue<Integer> sv : p.getVolume()) {
			stepSet.add(sv.getStep());
		}
		if (type==InstrumentType.SINGLE) {
			for (StepValue<Integer> sv : p.getBass()) {
				stepSet.add(sv.getStep());
			}
			for (StepValue<Integer> sv : p.getButton()) {
				stepSet.add(sv.getStep());
			}
		} else if (type==InstrumentType.PIANO) {
			for (StepValue<Voicing> sv : p.getVoicing()) {
				stepSet.add(sv.getStep());
			}
		}
		List<Integer> steps = new ArrayList<Integer>(stepSet);
		Collections.sort(steps);
		assert(steps.size()<=32);
		int lastVolume = -1;
		for (int i=0; i<steps.size(); i++) {
			int step = steps.get(i);
			List<Integer> stepBinary = BinaryUtil.to2byteBinary(step);
			binary.set(0x42C8 + 256*partNum + 8*i, stepBinary.get(0));
			binary.set(0x42C9 + 256*partNum + 8*i, stepBinary.get(1));
			// �p�[�g:����
			int volume = p.getVolume(step);
			binary.set(0x42CC + 256*partNum + 8*i, BinaryUtil.VolumeValue[volume]);
			if (volume!=lastVolume) {
				binary.set(0x42CD + 256*partNum + 8*i, 0x00);
				lastVolume = volume;
			} else {
				binary.set(0x42CD + 256*partNum + 8*i, 0x01);
			}
			if (type==InstrumentType.SINGLE) {
				// �P���p�[�g:�x�[�X�A�{�^��
				binary.set(0x42CA + 256*partNum + 8*i, p.getBass(step));
				int button = p.getButton(step);
				if (button<0) {
					button += 0x100;
				}
				binary.set(0x42CB + 256*partNum + 8*i, button);
			} else if (type==InstrumentType.PIANO) {
				// �s�A�m�p�[�g:�{�C�V���O
				Voicing voicing = p.getVoicing(step);
				binary.set(0x42CA + 256*partNum + 8*i, voicing.getTopNote());
				binary.set(0x42CB + 256*partNum + 8*i,
						(voicing.getNotes()-3)*3 + voicing.getSpread().getValue());
			} else {
				binary.set(0x42CA + 256*partNum + 8*i, 0x00);
				binary.set(0x42CB + 256*partNum + 8*i, 0x00);
			}
			binary.set(0x42CE + 256*partNum + 8*i, 0x00);
			binary.set(0x42CF + 256*partNum + 8*i, 0x00);
		}
	}

}
