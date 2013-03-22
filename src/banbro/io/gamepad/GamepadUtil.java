package banbro.io.gamepad;

import java.util.Arrays;

public class GamepadUtil {

	/**
	 * @param buttonFlag
	 * @param buttonCode
	 * @return
	 * @see GamepadButtonEvent
	 */
	public static boolean isButtonPressingWithCode(int buttonFlag, int... buttonCode) {
		if (buttonCode==null || buttonCode.length==0) {
			return false;
		}
		for (int code : buttonCode) {
			if ((buttonFlag & 1<<code) == 0) {
				return false;
			}
		}
		return true;
	}
	public static boolean isButtonPressingWithFlag(int buttonFlag, int flag) {
		return (buttonFlag&flag)==flag;
	}

	public static int toButtonFlag(int... buttonCode) {
		if (buttonCode==null) {
			return 0;
		}
		int flag = 0;
		for (int code : buttonCode) {
			flag |= (1<<code);
		}
		return flag;
	}

	public static int[] toButtonCode(int buttonFlag) {
		int[] code = {};
		for (int i=0; i<32; i++) {
			if ((buttonFlag&(1<<i))!=0) {
				code = Arrays.copyOf(code, code.length+1);
				code[code.length-1] = i;
			}
		}
		return code;
	}

}
