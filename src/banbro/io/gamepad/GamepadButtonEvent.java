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
	 * Ｙ Ｘ Ｂ Ａ Ｌ Ｒ Select Start ↑ ↓ ← →
	 */
	private static final List<String> ButtonNames = Arrays.asList(new String[] {
			"Ｙ", "Ｘ", "Ｂ", "Ａ", "Ｌ", "Ｒ", "Select", "Start", "↑", "↓", "←", "→"
	});
	public static final int Num = ButtonNames.size();

	private int _oldButton;
	private int _pressButton;
	private int _releaseButton;
	private int _newButton;

	/**
	 * 複数同時に変更される可能性があるので、全てフラグで渡す
	 * （例：Ｌ＋ＡからＲ＋Ａに変化する場合）
	 * @param source
	 * @param oldButton このイベントの前に押されていたボタン（例：Ｌ＋Ａ）
	 * @param pressButton 押されたボタン（例：Ｒ）
	 * @param releaseButton 放されたボタン（例：Ｌ）
	 * @param newButton 現在押されているボタン（例：Ｒ＋Ａ）
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
