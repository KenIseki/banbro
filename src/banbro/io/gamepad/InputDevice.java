package banbro.io.gamepad;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

public abstract class InputDevice {
	private final EventListenerList _listenerList = new EventListenerList();

	protected boolean _isRun;
	protected ButtonFlag _buttonFlag;

	private Map<Integer, Integer> _buttonSetting;  // �{�^����(�ԍ�)->�f�o�C�X�̃{�^���ԍ�
	private Map<Integer, Integer> _buttonMap;  // �f�o�C�X�̃{�^���ԍ�->�{�^����(�ԍ�)

	public InputDevice() {
		_buttonFlag = new ButtonFlag();
		_isRun = false;
		_buttonSetting = new HashMap<Integer, Integer>();
	}

	public void addGamepadButtonListener(GamepadButtonListener l) {
		_listenerList.add(GamepadButtonListener.class, l);
	}
	public void removeGamepadButtonListener(GamepadButtonListener l) {
		_listenerList.remove(GamepadButtonListener.class, l);
	}
	public void fireGamepadButtonEvent(final Object source, final int oldButton, final int newButton) {
		if (oldButton==newButton) {
			return;
		}
		// �����������Ȃ��悤�ɕʃX���b�h�ōs��
		Thread thread = new Thread() {
			public void run() {
				int pressButton = (~oldButton) & newButton;
				int releaseButton = oldButton & (~newButton);
				GamepadButtonEvent event = new GamepadButtonEvent(source, oldButton, pressButton, releaseButton, newButton);
				Object[] listeners = _listenerList.getListenerList();
				for(int i=listeners.length-2; i>=0; i-=2) {
					if (listeners[i]==GamepadButtonListener.class) {
						if (releaseButton!=0) {
							((GamepadButtonListener)listeners[i+1]).buttonReleased(event);
						}
						if (pressButton!=0) {
							((GamepadButtonListener)listeners[i+1]).buttonPressed(event);
						}
					}
				}
			};
		};
		thread.start();
	}

	/**
	 * @param n �{�^�����ɑΉ�����ԍ�
	 * @return �f�o�C�X�̃{�^���ԍ�
	 * @see GamepadButtonEvent
	 */
	public Integer getButtonSetting(int n) {
		return _buttonSetting.get(n);
	}
	/**
	 * �{�^����ݒ肷��B
	 * @param n �{�^�����ɑΉ�����ԍ�
	 * @param b �f�o�C�X�̃{�^���ԍ�
	 * @see GamepadButtonEvent
	 */
	public void setButtonSetting(int n, int b) {
		if (_isRun) {
			return;
		}
		_buttonSetting.put(n, b);
		_buttonMap = null;
	}

	protected Integer toButtonNum(int b) {
		if (_buttonMap==null) {
			_buttonMap = new HashMap<Integer, Integer>();
			for (Integer n : _buttonSetting.keySet()) {
				Integer b2 = _buttonSetting.get(n);
				_buttonMap.put(b2, n);
			}
		}
		return _buttonMap.get(b);
	}

	public void pollStart() {
		if (_isRun) {
			return;
		}
		pollRun();
	}

	abstract protected void pollRun();

	public void pollStop() {
		_isRun = false;
	}

}
