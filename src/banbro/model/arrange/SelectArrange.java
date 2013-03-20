package banbro.model.arrange;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import banbro.model.bdx.BDX;

/**
 * 何らかの条件で演奏イメージを選択してアレンジする
 */
public abstract class SelectArrange extends AbstractArrange {

	private ArrangeFactory _factory;
	private PerformanceStyle _style;
	private AbstractArrange _arrangeInstance;
	private Map<String, Object> _optionMap = new HashMap<String, Object>();

	/**
	 * @see AbstractArrange#AbstractArrange()
	 */
	protected SelectArrange() {
		super();
	}
	/**
	 * @param isArrangeTempo
	 * @param isBDX
	 * @see AbstractArrange#AbstractArrange(boolean, boolean)
	 */
	protected SelectArrange(boolean isArrangeTempo, boolean isBDX) {
		super(isArrangeTempo, isBDX);
	}

	@Override
	public synchronized void setIsArrangeTempo(boolean isArrangeTempo) {
		super.setIsArrangeTempo(isArrangeTempo);
		_factory = null;
	}

	@Override
	public synchronized void setIsBDX(boolean isBDX) {
		super.setIsBDX(isBDX);
		_factory = null;
	}

	@Override
	public synchronized void setBDX(BDX bdx) {
		_style = initStyle();
		_arrangeInstance = createArrangeInstance(_style);
		super.setBDX(bdx);
	}

	protected AbstractArrange createArrangeInstance(PerformanceStyle style) {
		if (_factory==null) {
			_factory = new ArrangeFactory(isArrangeTempo(), isBDX());
		}
		AbstractArrange ar = null;
		try {
			ar = _factory.createInstance(style);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
		for (String optionName : _optionMap.keySet()) {
			ar.setOption(optionName, _optionMap.get(optionName));
		}
		return ar;
	}

	/**
	 * @return 演奏イメージ
	 * ただし、{@link SelectArrange}を継承したクラスに対する演奏イメージは除く
	 */
	protected abstract PerformanceStyle initStyle();

	public PerformanceStyle getStyle() {
		if (_style==null) {
			_style = initStyle();
		}
		return _style;
	}

	public AbstractArrange getArrangeInstance() {
		if (_arrangeInstance==null) {
			_arrangeInstance = createArrangeInstance(getStyle());
		}
		return _arrangeInstance;
	}

	protected Object getOption(String optionName) {
		return _optionMap.get(optionName);
	}

	@Override
	public synchronized void setOption(String optionName, Object optionValue) {
		_optionMap.put(optionName, optionValue);
		if (_arrangeInstance!=null) {
			_arrangeInstance.setOption(optionName, optionValue);
		}
	}

	@Override
	protected BDX doArrange(BDX bdx) {
		getArrangeInstance().setBDX(bdx);
		return getArrangeInstance().arrange();
	}
	@Override
	public synchronized void arrangeBinary(List<Integer> binary, BDX bdx) {
		getArrangeInstance().arrangeBinary(binary, bdx);
	}

}
