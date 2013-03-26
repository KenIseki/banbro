package banbro.model.bdx;

import banbro.model.Accidental;
import banbro.model.Pitch;

public class BaseChord extends Chord {
	public enum Name {
		_Maj(0, ""),
		_m(1, "m"),
		_7(2, "7"),
		_M7(3, "M7"),
		_m7(4, "m7"),
		_dim(5, "dim"),
		_m7b5(6, "m7ÅÛ5"),
		_aug(7, "aug"),
		_sus4(8, "sus4"),
		_7sus4(9, "7sus4"),
		_6(10, "6"),
		_add9(11, "add9"),
		;
		private int _value;
		private String _name;
		private Name(int value, String name) {
			_value = value;
			_name = name;
		}
		public int getValue() {
			return _value;
		}
		public String getString() {
			return _name;
		}
		public static Name valueOf(int value) {
			for (Name n : values()) {
				if (n.getValue()==value) {
					return n;
				}
			}
			return null;
		}
	}

	private Pitch _root;
	private Accidental _accidental;
	private Name _name;

	/**
	 * @param root
	 * @param accidental
	 * @param name
	 */
	public BaseChord(Pitch root, Accidental accidental, Name name) {
		setChord(root, accidental, name);
	}

	public Pitch getRoot() {
		return _root;
	}

	public Accidental getAccidental() {
		return _accidental;
	}

	public Name getName() {
		return _name;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(_root.name());
		sb.append(_accidental.getText());
		sb.append(_name.getString());
		return sb.toString();
	}

	public void setChord(Pitch root, Accidental accidental, Name name){
		if (root==null || accidental==null || name==null) {
			throw new IllegalArgumentException();
		}
		_root = root;
		_accidental = accidental;
		_name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BaseChord) {
			BaseChord c = (BaseChord)obj;
			return c.getRoot()==_root && c.getAccidental()==_accidental && c.getName()==_name;
		}
		return super.equals(obj);
	}

	public BaseChord clone() {
		return new BaseChord(_root, _accidental, _name);
	}

}
