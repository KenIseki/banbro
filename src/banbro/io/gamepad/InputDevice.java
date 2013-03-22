package banbro.io.gamepad;

import javax.swing.event.EventListenerList;

import banbro.util.PairSet;

public abstract class InputDevice {
	private final EventListenerList _listenerList = new EventListenerList();

	protected boolean _isRun;
	protected ButtonFlag _buttonFlag;

	private PairSet<Integer, Integer> _buttonPairSet;  // �{�^����(�ԍ�)<->�f�o�C�X�̃{�^���ԍ�

	public InputDevice() {
		_buttonFlag = new ButtonFlag();
		_isRun = false;
		_buttonPairSet = new PairSet<>();
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
		return _buttonPairSet.getRightValue(n);
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
		_buttonPairSet.put(n, b);
	}

	protected Integer toButtonNum(int b) {
		return _buttonPairSet.getLeftValue(b);
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

	@Override
	protected void finalize() throws Throwable {
		pollStop();
		super.finalize();
	}

}
