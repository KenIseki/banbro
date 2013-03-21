package banbro.model.bdx;

import java.util.Arrays;

import banbro.model.Accidental;

public class GuitarChord {
	/**
	 * Œ·‚ð‰Ÿ‚³‚¦‚¸‚É’e‚­‚Æ–Â‚é‰¹
	 */
	private static final int[] NOTES = new int[] {40, 45, 50, 55, 59, 64};

	private int[] _fret;
	private boolean[] _mute;
	private boolean _used;

	private String _chordName;

	public GuitarChord() {
		_fret = new int[6];
		Arrays.fill(_fret, 0);
		_mute = new boolean[6];
		Arrays.fill(_mute, false);
	}

	/**
	 * @param chord Šî–{ƒR[ƒh
	 */
	public GuitarChord(BaseChord chord) {
		this();
		_used = true;
		int root = chord.getRoot().getNoteNum();
		if (chord.getAccidental()==Accidental.SHARP) {
			root++;
			if (root>12) {
				root -= 12;
			}
		} else if (chord.getAccidental()==Accidental.FLAT) {
			root--;
			if (root<0) {
				root += 12;
			}
		}
		switch (chord.getName()) {
		case _Maj:
			setChord_Maj(root);
			break;
		case _m:
			setChord_m(root);
			break;
		case _7:
			setChord_7(root);
			break;
		case _M7:
			setChord_M7(root);
			break;
		case _m7:
			setChord_m7(root);
			break;
		case _dim:
			setChord_dim(root);
			break;
		case _m7b5:
			setChord_m7b5(root);
			break;
		case _aug:
			setChord_aug(root);
			break;
		case _sus4:
			setChord_sus4(root);
			break;
		case _7sus4:
			setChord_7sus4(root);
			break;
		case _6:
			setChord_6(root);
			break;
		case _add9:
			setChord_add9(root);
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	private void setChord_Maj(int root) {
		switch (root) {
		case 0:
			_fret = new int[] {0,3,2,0,1,0};
			break;
		case 1:
			_fret = new int[] {4,4,6,6,6,4};
			break;
		case 2:
			_fret = new int[] {0,0,0,2,3,2};
			_mute[0] = true;
			break;
		case 3:
			_fret = new int[] {6,6,8,8,8,6};
			break;
		case 4:
			_fret = new int[] {0,2,2,1,0,0};
			break;
		case 5:
			_fret = new int[] {1,3,3,2,1,1};
			break;
		case 6:
			_fret = new int[] {2,4,4,3,2,2};
			break;
		case 7:
			_fret = new int[] {3,2,0,0,0,3};
			break;
		case 8:
			_fret = new int[] {4,6,6,5,4,4};
			break;
		case 9:
			_fret = new int[] {0,0,2,2,2,0};
			break;
		case 10:
			_fret = new int[] {1,1,3,3,3,1};
			break;
		case 11:
			_fret = new int[] {2,2,4,4,4,2};
			break;
		default:
			break;
		}
	}

	private void setChord_m(int root) {
		switch (root) {
		case 0:
			_fret = new int[] {3,3,5,5,4,3};
			break;
		case 1:
			_fret = new int[] {4,4,6,6,5,4};
			break;
		case 2:
			_fret = new int[] {0,0,0,2,3,1};
			_mute[0] = true;
			break;
		case 3:
			_fret = new int[] {6,6,8,8,7,6};
			break;
		case 4:
			_fret = new int[] {0,2,2,0,0,0};
			break;
		case 5:
			_fret = new int[] {1,3,3,1,1,1};
			break;
		case 6:
			_fret = new int[] {2,4,4,2,2,2};
			break;
		case 7:
			_fret = new int[] {3,5,5,3,3,3};
			break;
		case 8:
			_fret = new int[] {4,6,6,4,4,4};
			break;
		case 9:
			_fret = new int[] {0,0,2,2,1,0};
			break;
		case 10:
			_fret = new int[] {1,1,3,3,2,1};
			break;
		case 11:
			_fret = new int[] {2,2,4,4,3,2};
			break;
		default:
			break;
		}
	}

	private void setChord_7(int root) {
		switch (root) {
		case 0:
			_fret = new int[] {0,3,2,3,1,0};
			_mute[0] = true;
			break;
		case 1:
			_fret = new int[] {4,4,6,4,6,4};
			break;
		case 2:
			_fret = new int[] {0,0,0,2,1,2};
			_mute[0] = true;
			break;
		case 3:
			_fret = new int[] {6,6,8,6,8,6};
			break;
		case 4:
			_fret = new int[] {0,2,0,2,0,0};
			break;
		case 5:
			_fret = new int[] {1,3,1,2,1,1};
			break;
		case 6:
			_fret = new int[] {2,4,2,3,2,2};
			break;
		case 7:
			_fret = new int[] {3,2,0,0,0,1};
			break;
		case 8:
			_fret = new int[] {4,6,4,5,4,4};
			break;
		case 9:
			_fret = new int[] {0,0,2,0,2,0};
			break;
		case 10:
			_fret = new int[] {1,1,3,1,3,1};
			break;
		case 11:
			_fret = new int[] {0,2,1,2,0,2};
			_mute[0] = true;
			break;
		default:
			break;
		}
	}

	private void setChord_M7(int root) {
		switch (root) {
		case 0:
			_fret = new int[] {0,3,2,0,0,0};
			break;
		case 1:
			_fret = new int[] {4,4,6,5,6,4};
			break;
		case 2:
			_fret = new int[] {0,0,0,2,2,2};
			_mute[0] = true;
			break;
		case 3:
			_fret = new int[] {6,6,8,7,8,6};
			break;
		case 4:
			_fret = new int[] {0,2,1,1,0,0};
			break;
		case 5:
			_fret = new int[] {0,0,3,2,1,0};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 6:
			_fret = new int[] {2,4,3,3,2,2};
			break;
		case 7:
			_fret = new int[] {3,2,0,0,0,2};
			break;
		case 8:
			_fret = new int[] {4,6,5,5,4,4};
			break;
		case 9:
			_fret = new int[] {0,0,2,1,2,0};
			break;
		case 10:
			_fret = new int[] {1,1,3,2,3,1};
			break;
		case 11:
			_fret = new int[] {2,2,4,3,4,2};
			break;
		default:
			break;
		}
	}

	private void setChord_m7(int root) {
		switch (root) {
		case 0:
			_fret = new int[] {3,3,5,3,4,3};
			break;
		case 1:
			_fret = new int[] {4,4,6,4,5,4};
			break;
		case 2:
			_fret = new int[] {0,0,0,2,1,1};
			_mute[0] = true;
			break;
		case 3:
			_fret = new int[] {6,6,8,6,7,6};
			break;
		case 4:
			_fret = new int[] {0,2,0,0,0,0};
			break;
		case 5:
			_fret = new int[] {1,3,1,1,1,1};
			break;
		case 6:
			_fret = new int[] {4,4,6,4,5,4};
			break;
		case 7:
			_fret = new int[] {3,5,3,3,3,3};
			break;
		case 8:
			_fret = new int[] {4,6,4,4,4,4};
			break;
		case 9:
			_fret = new int[] {0,0,2,0,1,0};
			break;
		case 10:
			_fret = new int[] {1,1,3,1,2,1};
			break;
		case 11:
			_fret = new int[] {2,2,4,2,3,2};
			break;
		default:
			break;
		}
	}

	private void setChord_dim(int root) {
		switch (root) {
		case 0:
		case 3:
		case 6:
		case 9:
			_fret = new int[] {0,0,1,2,1,2};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 1:
		case 4:
		case 7:
		case 10:
			_fret = new int[] {0,0,2,3,2,3};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 2:
		case 5:
		case 8:
		case 11:
			_fret = new int[] {0,0,0,1,0,1};
			_mute[0] = true;
			_mute[1] = true;
			break;
		default:
			break;
		}
	}

	private void setChord_m7b5(int root) {
		switch (root) {
		case 0:
			_fret = new int[] {0,3,4,3,4,0};
			_mute[0] = true;
			_mute[5] = true;
			break;
		case 1:
			_fret = new int[] {0,4,5,4,5,0};
			_mute[0] = true;
			_mute[5] = true;
			break;
		case 2:
			_fret = new int[] {0,0,0,1,1,1};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 3:
			_fret = new int[] {0,0,1,2,2,2};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 4:
			_fret = new int[] {0,0,2,3,3,3};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 5:
			_fret = new int[] {0,0,3,4,4,4};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 6:
			_fret = new int[] {2,0,2,2,1,0};
			_mute[1] = true;
			break;
		case 7:
			_fret = new int[] {3,0,3,3,2,0};
			_mute[1] = true;
			_mute[5] = true;
			break;
		case 8:
			_fret = new int[] {4,0,4,4,3,0};
			_mute[1] = true;
			_mute[5] = true;
			break;
		case 9:
			_fret = new int[] {5,0,5,5,4,0};
			_mute[1] = true;
			_mute[5] = true;
			break;
		case 10:
			_fret = new int[] {0,1,2,1,2,0};
			_mute[0] = true;
			_mute[5] = true;
			break;
		case 11:
			_fret = new int[] {0,2,3,2,3,0};
			_mute[0] = true;
			_mute[5] = true;
			break;
		default:
			break;
		}
	}

	private void setChord_aug(int root) {
		switch (root) {
		case 0:
			_fret = new int[] {0,3,2,1,1,0};
			_mute[0] = true;
			_mute[5] = true;
			break;
		case 1:
			_fret = new int[] {0,4,3,2,2,0};
			_mute[0] = true;
			_mute[5] = true;
			break;
		case 2:
			_fret = new int[] {0,0,0,3,3,2};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 3:
			_fret = new int[] {0,6,5,4,4,0};
			_mute[0] = true;
			_mute[5] = true;
			break;
		case 4:
			_fret = new int[] {0,0,2,1,1,0};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 5:
			_fret = new int[] {0,0,3,2,2,1};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 6:
			_fret = new int[] {0,0,4,3,3,2};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 7:
			_fret = new int[] {0,0,5,4,4,3};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 8:
			_fret = new int[] {0,0,6,5,5,4};
			break;
		case 9:
			_fret = new int[] {0,0,3,2,2,1};
			_mute[0] = true;
			break;
		case 10:
			_fret = new int[] {0,0,4,3,3,2};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 11:
			_fret = new int[] {0,0,5,4,4,3};
			_mute[0] = true;
			_mute[1] = true;
			break;
		default:
			break;
		}
	}

	private void setChord_sus4(int root) {
		switch (root) {
		case 0:
			_fret = new int[] {3,3,5,5,6,3};
			break;
		case 1:
			_fret = new int[] {4,4,6,6,7,4};
			break;
		case 2:
			_fret = new int[] {0,0,0,2,3,3};
			_mute[0] = true;
			break;
		case 3:
			_fret = new int[] {6,6,8,8,9,6};
			break;
		case 4:
			_fret = new int[] {0,2,2,2,0,0};
			break;
		case 5:
			_fret = new int[] {1,3,3,3,1,1};
			break;
		case 6:
			_fret = new int[] {2,4,4,4,2,2};
			break;
		case 7:
			_fret = new int[] {3,5,5,5,3,3};
			break;
		case 8:
			_fret = new int[] {4,6,6,6,4,4};
			break;
		case 9:
			_fret = new int[] {0,0,2,2,3,0};
			break;
		case 10:
			_fret = new int[] {1,1,3,3,4,1};
			break;
		case 11:
			_fret = new int[] {2,2,4,4,5,2};
			break;
		default:
			break;
		}
	}

	private void setChord_7sus4(int root) {
		switch (root) {
		case 0:
			_fret = new int[] {3,3,5,3,6,3};
			break;
		case 1:
			_fret = new int[] {4,4,6,4,7,4};
			break;
		case 2:
			_fret = new int[] {0,0,0,2,1,3};
			_mute[0] = true;
			break;
		case 3:
			_fret = new int[] {6,6,8,6,9,6};
			break;
		case 4:
			_fret = new int[] {0,2,0,2,0,0};
			break;
		case 5:
			_fret = new int[] {1,3,1,3,1,1};
			break;
		case 6:
			_fret = new int[] {2,4,2,4,2,2};
			break;
		case 7:
			_fret = new int[] {3,5,3,5,3,3};
			break;
		case 8:
			_fret = new int[] {4,6,4,6,4,4};
			break;
		case 9:
			_fret = new int[] {0,0,2,0,3,0};
			break;
		case 10:
			_fret = new int[] {1,1,3,1,4,1};
			break;
		case 11:
			_fret = new int[] {2,2,4,2,5,2};
			break;
		default:
			break;
		}
	}

	private void setChord_6(int root) {
		switch (root) {
		case 0:
			_fret = new int[] {0,3,5,5,5,5};
			_mute[0] = true;
			break;
		case 1:
			_fret = new int[] {0,4,6,6,6,6};
			_mute[0] = true;
			break;
		case 2:
			_fret = new int[] {0,0,0,2,0,2};
			_mute[0] = true;
			break;
		case 3:
			_fret = new int[] {0,6,8,8,8,8};
			_mute[0] = true;
			break;
		case 4:
			_fret = new int[] {0,2,2,1,2,0};
			break;
		case 5:
			_fret = new int[] {1,3,0,2,3,0};
			_mute[5] = true;
			break;
		case 6:
			_fret = new int[] {2,0,1,3,2,0};
			_mute[1] = true;
			_mute[5] = true;
			break;
		case 7:
			_fret = new int[] {3,2,0,0,0,0};
			break;
		case 8:
			_fret = new int[] {4,0,3,5,4,0};
			_mute[1] = true;
			_mute[5] = true;
			break;
		case 9:
			_fret = new int[] {0,0,2,2,2,2};
			break;
		case 10:
			_fret = new int[] {0,1,3,3,3,3};
			_mute[0] = true;
			break;
		case 11:
			_fret = new int[] {0,2,4,4,4,4};
			_mute[0] = true;
			break;
		default:
			break;
		}
	}

	private void setChord_add9(int root) {
		switch (root) {
		case 0:
			_fret = new int[] {0,3,2,0,3,0};
			break;
		case 1:
			_fret = new int[] {4,4,6,6,4,4};
			break;
		case 2:
			_fret = new int[] {0,0,0,2,3,0};
			_mute[0] = true;
			break;
		case 3:
			_fret = new int[] {6,6,8,8,6,6};
			break;
		case 4:
			_fret = new int[] {0,2,2,1,0,2};
			break;
		case 5:
			_fret = new int[] {0,0,3,2,1,3};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 6:
			_fret = new int[] {0,0,4,3,2,4};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 7:
			_fret = new int[] {0,0,5,4,3,5};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 8:
			_fret = new int[] {0,0,6,5,4,6};
			_mute[0] = true;
			_mute[1] = true;
			break;
		case 9:
			_fret = new int[] {0,0,2,2,0,0};
			break;
		case 10:
			_fret = new int[] {1,1,3,3,1,1};
			break;
		case 11:
			_fret = new int[] {2,2,4,4,2,2};
			break;
		default:
			break;
		}
	}

	public GuitarChord(int f6, int f5, int f4, int f3, int f2, int f1,
			boolean m6, boolean m5, boolean m4, boolean m3, boolean m2, boolean m1) {
		_fret = new int[6];
		_fret[0] = f6;
		_fret[1] = f5;
		_fret[2] = f4;
		_fret[3] = f3;
		_fret[4] = f2;
		_fret[5] = f1;
		_mute = new boolean[6];
		_mute[0] = m6;
		_mute[1] = m5;
		_mute[2] = m4;
		_mute[3] = m3;
		_mute[4] = m2;
		_mute[5] = m1;
		_used = true;
	}

	public int[] getFret() {
		return Arrays.copyOf(_fret, _fret.length);
	}

	public boolean[] getMute() {
		return Arrays.copyOf(_mute, _mute.length);
	}

	public boolean isUsed() {
		return _used;
	}

	public int[] getNotes() {
		int[] notes = Arrays.copyOf(GuitarChord.NOTES, GuitarChord.NOTES.length);
		for (int i=0; i<notes.length; i++) {
			notes[i] += _fret[i];
		}
		return notes;
	}

	/**
	 * @param f 1`6
	 * @param num 0`15
	 */
	public void setFret(int f, int num) {
		assert(1<=f && f<=6);
		_fret[6-f] = num;
	}

	/**
	 * @param f 1`6
	 * @param mute
	 */
	public void setMute(int f, boolean mute) {
		assert(1<=f && f<=6);
		_mute[6-f] = mute;
	}

	public void setUsed(boolean used) {
		_used = used;
	}

	public String getChordName() {
		return _chordName;
	}

	public void setChordName(String chordName) {
		_chordName = chordName;
	}

	public GuitarChord clone() {
		GuitarChord copy = new GuitarChord(_fret[0], _fret[1], _fret[2], _fret[3], _fret[4], _fret[5],
				_mute[0], _mute[1], _mute[2], _mute[3], _mute[4], _mute[5]);
		copy.setChordName(_chordName);
		copy.setUsed(_used);
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GuitarChord) {
			GuitarChord chord = (GuitarChord) obj;
			if (Arrays.equals(_fret, chord.getFret())==false) {
				return false;
			}
			if (Arrays.equals(_mute, chord.getMute())==false) {
				return false;
			}
			return _used==chord.isUsed();
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (_used) {
			sb.append("[");
		} else {
			sb.append("(");
		}
		for (int i=0; i<6; i++) {
			sb.append(_fret[i]);
			if (_mute[i]) {
				sb.append("x");
			} else {
				sb.append("o");
			}
			if (i<5) {
				sb.append(" ");
			}
		}
		if (_used) {
			sb.append("]");
		} else {
			sb.append(")");
		}
		return sb.toString();
	}

}
