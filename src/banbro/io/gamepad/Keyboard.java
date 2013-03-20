package banbro.io.gamepad;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard extends InputDevice implements KeyListener {

	private Component _component;

	public Keyboard(Component c) {
		if (c==null) {
			throw new IllegalArgumentException();
		}
		_component = c;
		setButtonSetting(GamepadButtonEvent.Button_D, KeyEvent.VK_C);
		setButtonSetting(GamepadButtonEvent.Button_H, KeyEvent.VK_D);
		setButtonSetting(GamepadButtonEvent.Button_U, KeyEvent.VK_R);
		setButtonSetting(GamepadButtonEvent.Button_M, KeyEvent.VK_F);
		setButtonSetting(GamepadButtonEvent.Button_Y, KeyEvent.VK_J);
		setButtonSetting(GamepadButtonEvent.Button_B, KeyEvent.VK_M);
		setButtonSetting(GamepadButtonEvent.Button_A, KeyEvent.VK_K);
		setButtonSetting(GamepadButtonEvent.Button_X, KeyEvent.VK_I);
		setButtonSetting(GamepadButtonEvent.Button_L, KeyEvent.VK_E);
		setButtonSetting(GamepadButtonEvent.Button_R, KeyEvent.VK_O);
		setButtonSetting(GamepadButtonEvent.Button_Sel, KeyEvent.VK_G);
		setButtonSetting(GamepadButtonEvent.Button_Sta, KeyEvent.VK_H);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int newButton = GamepadUtil.toButtonFlag(getButtonCode(e));
		newButton = ~newButton & _buttonFlag.getFlag();
		_buttonFlag.setFlag(newButton);
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int newButton = GamepadUtil.toButtonFlag(getButtonCode(e));
		newButton = newButton | _buttonFlag.getFlag();
		_buttonFlag.setFlag(newButton);
	}

	@Override
	protected void pollRun() {
		if (_component==null) {
			return;
		}
		_component.addKeyListener(this);
		_isRun = true;
		Thread thread = new Thread() {
			@Override
			public void run() {
				int lastButton = 0;
				while (_isRun) {
					int currentButtonFlag = _buttonFlag.getFlag();
					fireGamepadButtonEvent(_component, lastButton, currentButtonFlag);
					lastButton = currentButtonFlag;
				}
			}
		};
		thread.start();
	}

	private int getButtonCode(KeyEvent event) {
		Integer n = toButtonNum(event.getKeyCode());
		if (n==null) {
			return GamepadButtonEvent.Num;
		}
		return n.intValue();
	}

	@Override
	public void pollStop() {
		super.pollStop();
		if (_component==null) {
			return;
		}
		_component.removeKeyListener(this);
	}

}
