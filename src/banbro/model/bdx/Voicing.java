package banbro.model.bdx;

public class Voicing {
	public enum Spread {
		NARROW(0, "��"), NORMAL(1, "��"), WIDE(2, "�e");
		private int _value;
		private String _name;
		private Spread(int value, String name) {
			_value = value;
			_name = name;
		}
		public int getValue() {
			return _value;
		}
		public String getName() {
			return _name;
		}
		public static Spread valueOf(int value) {
			for (Spread s : values()) {
				if (s.getValue()==value) {
					return s;
				}
			}
			return NARROW;
		}
	}
	
	/** �ō����̍ŏ��l */
	public static final int MIN_TOP_NOTE = 48;
	private int _topNote;
	private int _notes;
	private Spread _spread;

	/**
	 * �f�t�H���g�̃{�C�V���O
	 * �ō���:70 ����:3 �L����:��
	 */
	public Voicing() {
		_topNote = 70;
		_notes = 3;
		_spread = Spread.NARROW;
	}

	/**
	 * @param top {@link #MIN_TOP_NOTE}�ȏ�
	 * @param notes 3 or 4
	 * @param spread
	 */
	public Voicing(int top, int notes, Spread spread) {
		this();
		setTopNote(top);
		setNotes(notes);
		setSpread(spread);
	}

	public void setTopNote(int top) {
		if (top<MIN_TOP_NOTE || 127<top) {
			throw new IllegalArgumentException();
		}
		_topNote = top;
	}

	public void setNotes(int notes) {
		if ((notes==3 || notes==4) == false) {
			throw new IllegalArgumentException();
		}
		_notes = notes;
	}

	public void setSpread(Spread spread) {
		_spread = spread;
	}

	public int getTopNote() {
		return _topNote;
	}

	public int getNotes() {
		return _notes;
	}

	public Spread getSpread() {
		return _spread;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Voicing) {
			Voicing v = (Voicing) obj;
			return (_topNote==v.getTopNote() && _notes==v.getNotes() && _spread==v.getSpread());
		}
		return false;
	}

	public Voicing clone() {
		Voicing copy = new Voicing();
		copy.setTopNote(_topNote);
		copy.setNotes(_notes);
		copy.setSpread(_spread);
		return copy;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[�ō���=");
		sb.append(_topNote);
		sb.append(", ����=");
		sb.append(_notes);
		sb.append(", �L����=");
		sb.append(_spread.getName());
		sb.append("]");
		return sb.toString();
	}

}
