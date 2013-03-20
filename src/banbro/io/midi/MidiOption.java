package banbro.io.midi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class MidiOption {
	public static final int DEFAULT_NES_NUM = 88;
	public static final int DEFAULT_RGUITAR_NUM = 31;
	public static final int DEFAULT_DRUM_NUM = 1;
	public static final int DRUM_B = 0;
	public static final int DRUM_A = 1;
	public static final int DRUM_Y = 2;
	public static final int DRUM_X = 3;
	public static final int DRUM_U = 4;
	public static final int DRUM_D = 5;
	public static final int DRUM_H = 6;
	public static final int DRUM_M = 7;
	public static final int DRUM_L = 8;
	public static final int DRUM_R = 9;
	public static final int[] DEFAULT_R_DRUM = {35,40,37,54,42,45,41,48,46,57};
	public static final int[] DEFAULT_E_DRUM = {36,38,51,54,42,47,43,50,46,49};
	public static final int[] DEFAULT_S_DRUM = {32,40,30,54,44,47,43,50,46,57};
	public static final int[] DEFAULT_PERC_SET = {35,38,81,40,52,54,38,54,55,49};
	public static final int[] DEFAULT_BONGO_SET = {61,60,80,81,54,75,39,69,66,65};
	public static final int[] DEFAULT_CONGA_SET = {64,63,62,58,83,82,85,56,77,76};
	public static final int[] DEFAULT_JPN_PERC = {54,86,87,52,75,60,67,66,77,76};

	private static final String DEFAULT_MIDI_SETTING_FILE_NAME = "MidiSetting.ini";
	private static final String MIDI_ROCK_GUITAR_NUM = "RockGuitarNum";
	private static final String MIDI_NES_NUM = "NESNum";
	private static final String MIDI_ROCK_GUITAR_CHORD = "RockGuitarChord";
	private static final String MIDI_MUSIC_BOX_OCTAVE_UP = "MusicBoxOctaveUp";
	private static final String MIDI_OUTPUT_ENVELOPE = "OutputEnvelope";
	private static final String MIDI_OUTPUT_VIBRATO = "OutputVibrato";
	private static final String MIDI_OUTPUT_EFFECTS = "OutputEffects";
	private static final String MIDI_DRUM_NUM = "DrumNum";
	private static final String MIDI_R_DRUM = "RDrum";
	private static final String MIDI_E_DRUM = "EDrum";
	private static final String MIDI_S_DRUM = "SDrum";
	private static final String MIDI_PERC_SET = "PercSet";
	private static final String MIDI_BONGO_SET = "BongoSet";
	private static final String MIDI_CONGA_SET = "CongaSet";
	private static final String MIDI_JPN_PERC = "JpnPerc";

	private int _nesNum;
	private int _rguitarNum;
	private boolean _isRockGuitarChord;
	private boolean _isMusicBoxOctaveUp;  // true:オルゴールの音を1オクターブ上げる
	private boolean _outputEnvelope;
	private boolean _outputVibrato;
	private boolean _outputEffects;
	private int _drumNum;
	private int[] _rDrum;
	private int[] _eDrum;
	private int[] _sDrum;
	private int[] _percSet;
	private int[] _bongoSet;
	private int[] _congaSet;
	private int[] _jpnPerc;

	public MidiOption() {
		_nesNum = DEFAULT_NES_NUM;
		_rguitarNum = DEFAULT_RGUITAR_NUM;
		_drumNum = DEFAULT_DRUM_NUM;
		_rDrum = Arrays.copyOf(DEFAULT_R_DRUM, DEFAULT_R_DRUM.length);
		_eDrum = Arrays.copyOf(DEFAULT_E_DRUM, DEFAULT_E_DRUM.length);
		_sDrum = Arrays.copyOf(DEFAULT_S_DRUM, DEFAULT_S_DRUM.length);
		_percSet = Arrays.copyOf(DEFAULT_PERC_SET, DEFAULT_PERC_SET.length);
		_bongoSet = Arrays.copyOf(DEFAULT_BONGO_SET, DEFAULT_BONGO_SET.length);
		_congaSet = Arrays.copyOf(DEFAULT_CONGA_SET, DEFAULT_CONGA_SET.length);
		_jpnPerc = Arrays.copyOf(DEFAULT_JPN_PERC, DEFAULT_JPN_PERC.length);
		_isRockGuitarChord = true;
		_isMusicBoxOctaveUp = true;
		_outputEnvelope = true;
		_outputVibrato = true;
		_outputEffects = true;
	}

	public int getNesNum() {
		return _nesNum;
	}

	public int getRguitarNum() {
		return _rguitarNum;
	}
	
	public int getDrumNum() {
		return _drumNum;
	}

	public int[] getRDrum() {
		return _rDrum;
	}

	public int[] getEDrum() {
		return _eDrum;
	}

	public int[] getSDrum() {
		return _sDrum;
	}

	public int[] getPercSet() {
		return _percSet;
	}

	public int[] getBongoSet() {
		return _bongoSet;
	}

	public int[] getCongaSet() {
		return _congaSet;
	}

	public int[] getJpnPerc() {
		return _jpnPerc;
	}

	public int getRDrum(int button) {
		return _rDrum[button];
	}

	public int getEDrum(int button) {
		return _eDrum[button];
	}

	public int getSDrum(int button) {
		return _sDrum[button];
	}

	public int getPercSet(int button) {
		return _percSet[button];
	}

	public int getBongoSet(int button) {
		return _bongoSet[button];
	}

	public int getCongaSet(int button) {
		return _congaSet[button];
	}

	public int getJpnPerc(int button) {
		return _jpnPerc[button];
	}

	public boolean isRockGuitarChord() {
		return _isRockGuitarChord;
	}

	public boolean isMusicBoxOctaveUp() {
		return _isMusicBoxOctaveUp;
	}

	public boolean isOutputEnvelope() {
		return _outputEnvelope;
	}

	public boolean isOutputVibrato() {
		return _outputVibrato;
	}

	public boolean isOutputEffects() {
		return _outputEffects;
	}

	public void setNesNum(int num) {
		_nesNum = num;
	}

	public void setRguitarNum(int num) {
		_rguitarNum = num;
	}

	public void setDrumNum(int num) {
		_drumNum = num;
	}

	public void setRDrum(int button, int num) {
		_rDrum[button] = num;
	}

	public void setEDrum(int button, int num) {
		_eDrum[button] = num;
	}

	public void setSDrum(int button, int num) {
		_sDrum[button] = num;
	}

	public void setPercSet(int button, int num) {
		_percSet[button] = num;
	}

	public void setBongoSet(int button, int num) {
		_bongoSet[button] = num;
	}

	public void setCongaSet(int button, int num) {
		_congaSet[button] = num;
	}

	public void setJpnPerc(int button, int num) {
		_jpnPerc[button] = num;
	}

	public void setIsRockGuitarChord(boolean isRockGuitarChord) {
		_isRockGuitarChord = isRockGuitarChord;
	}

	public void setIsMusicBoxOctaveUp(boolean isMusicBoxOctaveUp) {
		_isMusicBoxOctaveUp = isMusicBoxOctaveUp;
	}
	public void setOutputEnvelope(boolean envelope) {
		_outputEnvelope = envelope;
	}

	public void setOutputVibrato(boolean vibrato) {
		_outputVibrato = vibrato;
	}

	public void setOutputEffects(boolean effects) {
		_outputEffects = effects;
	}

	public static MidiOption readMidiSetting() {
		return readMidiSetting(new File(DEFAULT_MIDI_SETTING_FILE_NAME));
	}

	public static MidiOption readMidiSetting(File file) {
		MidiOption option = new MidiOption();
		try (FileReader in = new FileReader(DEFAULT_MIDI_SETTING_FILE_NAME);
				BufferedReader br = new BufferedReader(in);
				) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] str = line.split("=");
				String pr = "";
				String value = "";
				if (str.length>=1) {
					pr = str[0];
				}
				if (str.length>=2) {
					value = str[1];
				}
				if (pr.equals(MIDI_ROCK_GUITAR_NUM)) {
					try {
						option.setRguitarNum(Integer.parseInt(value));
					} catch (NumberFormatException e) {
					}
				} else if (pr.equals(MIDI_NES_NUM)) {
					try {
						option.setNesNum(Integer.parseInt(value));
					} catch (NumberFormatException e) {
					}
				} else if (pr.equals(MIDI_ROCK_GUITAR_CHORD)) {
					option.setIsRockGuitarChord(Boolean.parseBoolean(value));
				} else if (pr.equals(MIDI_MUSIC_BOX_OCTAVE_UP)) {
					option.setIsMusicBoxOctaveUp(Boolean.parseBoolean(value));
				} else if (pr.equals(MIDI_OUTPUT_ENVELOPE)) {
					option.setOutputEnvelope(Boolean.parseBoolean(value));
				} else if (pr.equals(MIDI_OUTPUT_VIBRATO)) {
					option.setOutputVibrato(Boolean.parseBoolean(value));
				} else if (pr.equals(MIDI_OUTPUT_EFFECTS)) {
					option.setOutputEffects(Boolean.parseBoolean(value));
				} else if (pr.equals(MIDI_DRUM_NUM)) {
					try {
						option.setDrumNum(Integer.parseInt(value));
					} catch (NumberFormatException e) {
					}
				} else if (pr.equals(MIDI_R_DRUM)) {
					String[] drumValue = value.split(",");
					int length = Math.min(drumValue.length, MidiOption.DEFAULT_R_DRUM.length);
					for (int i=0; i<length; i++) {
						int newValue = MidiOption.DEFAULT_R_DRUM[i];
						try {
							newValue = Integer.parseInt(drumValue[i]);
						} catch (NumberFormatException e) {
						}
						option.setRDrum(i, newValue);
					}
				} else if (pr.equals(MIDI_E_DRUM)) {
					String[] drumValue = value.split(",");
					int length = Math.min(drumValue.length, MidiOption.DEFAULT_E_DRUM.length);
					for (int i=0; i<length; i++) {
						int newValue = MidiOption.DEFAULT_E_DRUM[i];
						try {
							newValue = Integer.parseInt(drumValue[i]);
						} catch (NumberFormatException e) {
						}
						option.setEDrum(i, newValue);
					}
				} else if (pr.equals(MIDI_S_DRUM)) {
					String[] drumValue = value.split(",");
					int length = Math.min(drumValue.length, MidiOption.DEFAULT_S_DRUM.length);
					for (int i=0; i<length; i++) {
						int newValue = MidiOption.DEFAULT_S_DRUM[i];
						try {
							newValue = Integer.parseInt(drumValue[i]);
						} catch (NumberFormatException e) {
						}
						option.setSDrum(i, newValue);
					}
				} else if (pr.equals(MIDI_PERC_SET)) {
					String[] drumValue = value.split(",");
					int length = Math.min(drumValue.length, MidiOption.DEFAULT_PERC_SET.length);
					for (int i=0; i<length; i++) {
						int newValue = MidiOption.DEFAULT_PERC_SET[i];
						try {
							newValue = Integer.parseInt(drumValue[i]);
						} catch (NumberFormatException e) {
						}
						option.setPercSet(i, newValue);
					}
				} else if (pr.equals(MIDI_BONGO_SET)) {
					String[] drumValue = value.split(",");
					int length = Math.min(drumValue.length, MidiOption.DEFAULT_BONGO_SET.length);
					for (int i=0; i<length; i++) {
						int newValue = MidiOption.DEFAULT_BONGO_SET[i];
						try {
							newValue = Integer.parseInt(drumValue[i]);
						} catch (NumberFormatException e) {
						}
						option.setBongoSet(i, newValue);
					}
				} else if (pr.equals(MIDI_CONGA_SET)) {
					String[] drumValue = value.split(",");
					int length = Math.min(drumValue.length, MidiOption.DEFAULT_CONGA_SET.length);
					for (int i=0; i<length; i++) {
						int newValue = MidiOption.DEFAULT_CONGA_SET[i];
						try {
							newValue = Integer.parseInt(drumValue[i]);
						} catch (NumberFormatException e) {
						}
						option.setCongaSet(i, newValue);
					}
				} else if (pr.equals(MIDI_JPN_PERC)) {
					String[] drumValue = value.split(",");
					int length = Math.min(drumValue.length, MidiOption.DEFAULT_JPN_PERC.length);
					for (int i=0; i<length; i++) {
						int newValue = MidiOption.DEFAULT_JPN_PERC[i];
						try {
							newValue = Integer.parseInt(drumValue[i]);
						} catch (NumberFormatException e) {
						}
						option.setJpnPerc(i, newValue);
					}
				}
			}
			br.close();
			in.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return option;
	}

	public void writeMidiSetting() {
		writeMidiSetting(new File(DEFAULT_MIDI_SETTING_FILE_NAME));
	}

	public void writeMidiSetting(File file) {
		try (FileOutputStream fos = new FileOutputStream(file);
				OutputStreamWriter osw = new OutputStreamWriter(fos);
				BufferedWriter bw = new BufferedWriter(osw);) {

			bw.write(MIDI_ROCK_GUITAR_NUM + "=" + getRguitarNum());
			bw.newLine();
			bw.write(MIDI_NES_NUM + "=" + getNesNum());
			bw.newLine();
			bw.write(MIDI_ROCK_GUITAR_CHORD + "=" + isRockGuitarChord());
			bw.newLine();
			bw.write(MIDI_MUSIC_BOX_OCTAVE_UP + "=" + isMusicBoxOctaveUp());
			bw.newLine();
			bw.write(MIDI_OUTPUT_ENVELOPE + "=" + isOutputEnvelope());
			bw.newLine();
			bw.write(MIDI_OUTPUT_VIBRATO + "=" + isOutputVibrato());
			bw.newLine();
			bw.write(MIDI_OUTPUT_EFFECTS + "=" + isOutputEffects());
			bw.newLine();
			bw.write(MIDI_DRUM_NUM + "=" + getDrumNum());
			bw.newLine();
			bw.write(MIDI_R_DRUM + "=" + toIntArrayString(getRDrum()));
			bw.newLine();
			bw.write(MIDI_E_DRUM + "=" + toIntArrayString(getEDrum()));
			bw.newLine();
			bw.write(MIDI_S_DRUM + "=" + toIntArrayString(getSDrum()));
			bw.newLine();
			bw.write(MIDI_PERC_SET + "=" + toIntArrayString(getPercSet()));
			bw.newLine();
			bw.write(MIDI_BONGO_SET + "=" + toIntArrayString(getBongoSet()));
			bw.newLine();
			bw.write(MIDI_CONGA_SET + "=" + toIntArrayString(getCongaSet()));
			bw.newLine();
			bw.write(MIDI_JPN_PERC + "=" + toIntArrayString(getJpnPerc()));
			bw.newLine();

			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String toIntArrayString(int[] array) {
		StringBuffer buf = new StringBuffer();
		for (int value : array) {
			buf.append(value);
			buf.append(",");
		}
		return buf.substring(0, buf.length()-1);
	}

}
