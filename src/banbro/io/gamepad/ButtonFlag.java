package banbro.io.gamepad;

public class ButtonFlag {
	private int _flag;

	public ButtonFlag() {
	}

	public void setFlag(int flag) {
		_flag = flag;
	}
	public int getFlag() {
		return _flag;
	}

	public boolean isButtonPressing(int... buttonCode) {
		return GamepadUtil.isButtonPressingWithCode(_flag, buttonCode);
	}

}
