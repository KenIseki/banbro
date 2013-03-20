package banbro.model.arrange;

import java.util.Calendar;

public abstract class ArrangeWithCalendar extends SelectArrange {
	private Calendar _calendar;

	/**
	 * @see AbstractArrange#AbstractArrange()
	 */
	protected ArrangeWithCalendar() {
		super();
	}
	/**
	 * @param isArrangeTempo
	 * @param isBDX
	 * @see AbstractArrange#AbstractArrange(boolean, boolean)
	 */
	protected ArrangeWithCalendar(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	protected PerformanceStyle initStyle() {
		_calendar = Calendar.getInstance();
		return getStyleWithCalendar(_calendar);
	}
	public Calendar getCalendar() {
		return _calendar;
	}

	protected abstract PerformanceStyle getStyleWithCalendar(Calendar calendar);

}
