package banbro.io.gamepad;

import java.util.Arrays;
import java.util.EventObject;
import java.util.List;

public class GamepadButtonEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	public static final int Button_Y = 0;
	public static final int Button_X = 1;
	public static final int Button_B = 2;
	public static final int Button_A = 3;
	public static final int Button_L = 4;
	public static final int Button_R = 5;
	public static final int Button_Sel = 6;
	public static final int Button_Sta = 7;
	public static final int Button_U = 8;
	public static final int Button_D = 9;
	public static final int Button_H = 10;
	public static final int Button_M = 11;
	/**
	 * ‚x ‚w ‚a ‚` ‚k ‚q Select Start ª « © ¨
	 */
	private static final List<String> ButtonNames = Arrays.asList(new String[] {
			"‚x", "‚w", "‚a", "‚`", "‚k", "‚q", "Select", "Start", "ª", "«", "©", "¨"
	});
	public static final int Num = ButtonNames.size();

	private int _oldButton;
	private int _pressButton;
	private int _releaseButton;
	private int _newButton;

	/**
	 * •¡”“¯‚É•ÏX‚³‚ê‚é‰Â”\«‚ª‚ ‚é‚Ì‚ÅA‘S‚Äƒtƒ‰ƒO‚Å“n‚·
	 * i—áF‚k{‚`‚©‚ç‚q{‚`‚É•Ï‰»‚·‚éê‡j
	 * @param source
	 * @param oldButton ‚±‚ÌƒCƒxƒ“ƒg‚Ì‘O‚É‰Ÿ‚³‚ê‚Ä‚¢‚½ƒ{ƒ^ƒ“i—áF‚k{‚`j
	 * @param pressButton ‰Ÿ‚³‚ê‚½ƒ{ƒ^ƒ“i—áF‚qj
	 * @param releaseButton •ú‚³‚ê‚½ƒ{ƒ^ƒ“i—áF‚kj
	 * @param newButton Œ»İ‰Ÿ‚³‚ê‚Ä‚¢‚éƒ{ƒ^ƒ“i—áF‚q{‚`j
	 * @see EventObject#EventObject(Object)
	 */
	public GamepadButtonEvent(Object source, int oldButton, int pressButton, int releaseButton, int newButton) {
		super(source);
		_oldButton = oldButton;
		_pressButton = pressButton;
		_releaseButton = releaseButton;
		_newButton = newButton;
	}

	public int getOldButton() {
		return _oldButton;
	}

	public int getPressButton() {
		return _pressButton;
	}

	public int getReleaseButton() {
		return _releaseButton;
	}

	public int getNewButton() {
		return _newButton;
	}

	public static String getButtonName(int buttonCode) {
		if (buttonCode<0 || ButtonNames.size()<=buttonCode) {
			return "";
		}
		return ButtonNames.get(buttonCode);
	}

}
