package banbro.io.gamepad;

import java.util.EventListener;

public interface GamepadButtonListener extends EventListener {
	void buttonPressed(GamepadButtonEvent event);
	void buttonReleased(GamepadButtonEvent event);
}
