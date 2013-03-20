package banbro.model.arrange;

public class YonaNuki extends ChangeKey {

	public YonaNuki() {
		super();
	}
	public YonaNuki(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	protected int calcNewNote(int n) {
		switch (n) {
		case 0:
			return 0;
		case 1:
			return 0;
		case 2:
			return 2;
		case 3:
			return 2;
		case 4:
			return 4;
		case 5:
			return 4;
		case 6:
			return 7;
		case 7:
			return 7;
		case 8:
			return 7;
		case 9:
			return 9;
		case 10:
			return 9;
		case 11:
			return 12;
		default:
			return n;
		}
	}

}
