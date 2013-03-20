package banbro.io.gamepad;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;

public class Gamepad extends InputDevice {
	private static final float EPSILON = 0.0001f;

	protected Controller _controller;

	public Gamepad(Controller controller) {
		super();
		if (controller!=null && controller.getType()!=Type.GAMEPAD) {
			throw new IllegalArgumentException();
		}
		_controller = controller;
		int[] buttons = {
				GamepadButtonEvent.Button_Y,
				GamepadButtonEvent.Button_X,
				GamepadButtonEvent.Button_B,
				GamepadButtonEvent.Button_A,
				GamepadButtonEvent.Button_L,
				GamepadButtonEvent.Button_R,
				GamepadButtonEvent.Button_Sel,
				GamepadButtonEvent.Button_Sta
		};
		for (int n : buttons) {  // �f�t�H���g�ݒ�
			setButtonSetting(n, n);
		}
	}

	@Override
	protected void pollRun() {
		if (isConnecting()==false) {
			return;
		}
		_isRun = true;
		Thread thread = new Thread() {
			@Override
			public void run() {
				_buttonFlag.setFlag(0);
				while (_isRun) {
					if (_controller.poll()) {
						int currentButtonFlag = getButtonFlag(_controller);
						fireGamepadButtonEvent(_controller, _buttonFlag.getFlag(), currentButtonFlag);
						_buttonFlag.setFlag(currentButtonFlag);
					} else {
						break;
					}
				}
			}// end run
		};// end thread
		thread.start();
	}

	protected int getButtonFlag(Controller c) {
		int currentButtonFlag = 0;
		Component[] comps = c.getComponents();
		for (Component comp : comps) {
			float val = comp.getPollData();
			if (comp.getIdentifier()==Identifier.Axis.X) {
				if (val<-EPSILON) {
					currentButtonFlag |= GamepadUtil.toButtonFlag(GamepadButtonEvent.Button_H);
				} else if (val>EPSILON) {
					currentButtonFlag |= GamepadUtil.toButtonFlag(GamepadButtonEvent.Button_M);
				}
			} else if (comp.getIdentifier()==Identifier.Axis.Y) {
				if (val<-EPSILON) {
					currentButtonFlag |= GamepadUtil.toButtonFlag(GamepadButtonEvent.Button_U);
				} else if (val>EPSILON) {
					currentButtonFlag |= GamepadUtil.toButtonFlag(GamepadButtonEvent.Button_D);
				}
			} else {
				if (Math.abs(val) > EPSILON) {
					String st = comp.getIdentifier().getName();
					try {
						int b = Integer.parseInt(st);
						Integer n = toButtonNum(b);
						if (n!=null) {
							currentButtonFlag |= GamepadUtil.toButtonFlag(n);
						}
					} catch (Exception e) {
					}
				}
			}
		}
		return currentButtonFlag;
	}

	public boolean isConnecting() {
		return _controller != null;
	}

}
