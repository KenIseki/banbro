package banbro.model.bdx;

public class BDXDate implements Comparable<BDXDate> {
	public static final int DEFAULT_YEAR = 2000;

	private int _year;
	private int _month;
	private int _day;

	public BDXDate() {
		this(0,1,1);
	}

	/**
	 * @param year 0Å`99
	 * @param month 1Å`12
	 * @param day 1Å`31
	 */
	public BDXDate(int year, int month, int day) {
		_year = year;
		_month = month;
		_day = day;
		assert(0<=year && year<=99);
		assert(0<=month && month<=12);
		assert(0<=day && day<=31);
	}

	public int getYear() {
		return _year;
	}

	public int getMonth() {
		return _month;
	}

	public int getDay() {
		return _day;
	}

	public void setYear(int year) {
		_year = year;
	}

	public void setMonth(int month) {
		_month = month;
	}

	public void setDay(int day) {
		_day = day;
	}

	@Override
	public int compareTo(BDXDate o) {
		int c = 0;
		c = _year - o.getYear();
		if (c!=0) {
			return c;
		}
		c = _month - o.getMonth();
		if (c!=0) {
			return c;
		}
		c = _day - o.getDay();
		return c;
	}

	public BDXDate clone() {
		return new BDXDate(_year, _month, _day);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(DEFAULT_YEAR + _year);
		sb.append("/");
		if (_month<10) {
			sb.append("0");
		}
		sb.append(_month);
		sb.append("/");
		if (_day<10) {
			sb.append("0");
		}
		sb.append(_day);
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BDXDate) {
			BDXDate date = (BDXDate) obj;
			return date.getYear()==getYear() && date.getMonth()==getMonth() && date.getDay()==getDay();
		}
		return false;
	}

}
