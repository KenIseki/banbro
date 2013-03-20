package banbro.model.arrange;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import banbro.model.bdx.BDX;

public class ArrangeFactory {
	
	private boolean _isArrangeTempo;
	private boolean _isBDX;
	
	public ArrangeFactory() {
		this(true, true);
	}
	/**
	 * @param isArrangeTempo
	 * @param isBDX
	 * @see AbstractArrange#AbstractArrange(BDX, boolean, boolean)
	 */
	public ArrangeFactory(boolean isArrangeTempo, boolean isBDX) {
		_isArrangeTempo = isArrangeTempo;
		_isBDX = isBDX;
	}

	/**
	 * @param style
	 * @return {@link PerformanceStyle#getStyleClass()}のインスタンス
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @see Class#getConstructor(Class...)
	 * @see Constructor#newInstance(Object...)
	 * @see AbstractArrange#AbstractArrange()
	 */
	public AbstractArrange createInstance(PerformanceStyle style)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		AbstractArrange arrange = null;
		arrange = style.getStyleClass().getConstructor().newInstance();
		arrange.setIsArrangeTempo(_isArrangeTempo);
		arrange.setIsBDX(_isBDX);
		return arrange;
	}

}
