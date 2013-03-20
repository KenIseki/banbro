package banbro.model.bdx;

public class Voicing {
	public enum Spread {
		NARROW(0, "密"), NORMAL(1, "並"), WIDE(2, "粗");
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
	
	/** 最高音の最小値 */
	public static final int MIN_TOP_NOTE = 48;
	private int _topNote;
	private int _notes;
	private Spread _spread;

	/**
	 * デフォルトのボイシング
	 * 最高音:70 音数:3 広がり:密
	 */
	public Voicing() {
		_topNote = 70;
		_notes = 3;
		_spread = Spread.NARROW;
	}

	/**
	 * @param top {@link #MIN_TOP_NOTE}以上
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
		sb.append("[最高音=");
		sb.append(_topNote);
		sb.append(", 音数=");
		sb.append(_notes);
		sb.append(", 広がり=");
		sb.append(_spread.getName());
		sb.append("]");
		return sb.toString();
	}

}
