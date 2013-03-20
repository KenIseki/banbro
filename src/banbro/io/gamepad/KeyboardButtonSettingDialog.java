package banbro.io.gamepad;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class KeyboardButtonSettingDialog extends ButtonSettingDialog {
	private static final long serialVersionUID = 1L;

	private static int[] buttons = {
		GamepadButtonEvent.Button_D,
		GamepadButtonEvent.Button_H,
		GamepadButtonEvent.Button_U,
		GamepadButtonEvent.Button_M,
		GamepadButtonEvent.Button_Y,
		GamepadButtonEvent.Button_B,
		GamepadButtonEvent.Button_A,
		GamepadButtonEvent.Button_X,
		GamepadButtonEvent.Button_L,
		GamepadButtonEvent.Button_R,
		GamepadButtonEvent.Button_Sel,
		GamepadButtonEvent.Button_Sta
	};

	private static int[] keys = {
		KeyEvent.VK_A,
		KeyEvent.VK_B,
		KeyEvent.VK_C,
		KeyEvent.VK_D,
		KeyEvent.VK_E,
		KeyEvent.VK_F,
		KeyEvent.VK_G,
		KeyEvent.VK_H,
		KeyEvent.VK_I,
		KeyEvent.VK_J,
		KeyEvent.VK_K,
		KeyEvent.VK_L,
		KeyEvent.VK_M,
		KeyEvent.VK_N,
		KeyEvent.VK_O,
		KeyEvent.VK_P,
		KeyEvent.VK_Q,
		KeyEvent.VK_R,
		KeyEvent.VK_S,
		KeyEvent.VK_T,
		KeyEvent.VK_U,
		KeyEvent.VK_V,
		KeyEvent.VK_W,
		KeyEvent.VK_X,
		KeyEvent.VK_Y,
		KeyEvent.VK_Z,
		KeyEvent.VK_1,
		KeyEvent.VK_2,
		KeyEvent.VK_3,
		KeyEvent.VK_4,
		KeyEvent.VK_5,
		KeyEvent.VK_6,
		KeyEvent.VK_7,
		KeyEvent.VK_8,
		KeyEvent.VK_9,
		KeyEvent.VK_0,
//		KeyEvent.VK_MINUS,
//		KeyEvent.VK_CIRCUMFLEX,
//		KeyEvent.VK_BACK_SLASH,
//		KeyEvent.VK_AT,
//		KeyEvent.VK_BRACELEFT,
//		KeyEvent.VK_SEMICOLON,
//		KeyEvent.VK_COLON,
//		KeyEvent.VK_BRACERIGHT,
//		KeyEvent.VK_COMMA,
//		KeyEvent.VK_PERIOD,
//		KeyEvent.VK_SLASH,
//		KeyEvent.VK_UNDERSCORE,
//		KeyEvent.VK_DOWN,
//		KeyEvent.VK_LEFT,
//		KeyEvent.VK_UP,
//		KeyEvent.VK_RIGHT,
	};

	private List<String> _keyList;

	public KeyboardButtonSettingDialog(Window owner) {
		super(owner, "キーボード設定");
	}

	@Override
	protected Component createMainComponent() {
		_keyList = new ArrayList<String>();
		for (int keyCode : keys) {
			_keyList.add(KeyEvent.getKeyText(keyCode));
		}
		Component component = super.createMainComponent();
		return component;
	}

	@Override
	protected JSpinner createSpinner(int b) {
		JSpinner spinner = new JSpinner();
		SpinnerListModel model = new SpinnerListModel(_keyList);
		spinner.setModel(model);
		((JSpinner.ListEditor)spinner.getEditor()).getTextField().setEditable(false);
		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				update();
			}
		});
		Dimension d = new Dimension(40, spinner.getPreferredSize().height);
		spinner.setMinimumSize(d);
		spinner.setPreferredSize(d);
		return spinner;
	}

	@Override
	public void readButtonSetting(Map<Integer, Integer> setting) {
		pushDefaultButton();
		for (int b : getButtons()) {
			Integer i = setting.get(b);
			if (i!=null) {
				int k = i.intValue();
				for (int keyCode : keys) {
					if (k==keyCode) {
						getSpinner(b).setValue(KeyEvent.getKeyText(keyCode));
						break;
					}
				}
			}
		}
	}

	@Override
	protected int[] getButtons() {
		return buttons;
	}

	@Override
	protected int calcButtonNum(Object value) {
		for (int keyCode : keys) {
			if (value.equals(KeyEvent.getKeyText(keyCode))) {
				return keyCode;
			}
		}
		return 0;
	}

	@Override
	protected void pushDefaultButton() {
		getSpinner(GamepadButtonEvent.Button_D).setValue(KeyEvent.getKeyText(KeyEvent.VK_C));
		getSpinner(GamepadButtonEvent.Button_H).setValue(KeyEvent.getKeyText(KeyEvent.VK_D));
		getSpinner(GamepadButtonEvent.Button_U).setValue(KeyEvent.getKeyText(KeyEvent.VK_R));
		getSpinner(GamepadButtonEvent.Button_M).setValue(KeyEvent.getKeyText(KeyEvent.VK_F));
		getSpinner(GamepadButtonEvent.Button_Y).setValue(KeyEvent.getKeyText(KeyEvent.VK_J));
		getSpinner(GamepadButtonEvent.Button_B).setValue(KeyEvent.getKeyText(KeyEvent.VK_M));
		getSpinner(GamepadButtonEvent.Button_A).setValue(KeyEvent.getKeyText(KeyEvent.VK_K));
		getSpinner(GamepadButtonEvent.Button_X).setValue(KeyEvent.getKeyText(KeyEvent.VK_I));
		getSpinner(GamepadButtonEvent.Button_L).setValue(KeyEvent.getKeyText(KeyEvent.VK_E));
		getSpinner(GamepadButtonEvent.Button_R).setValue(KeyEvent.getKeyText(KeyEvent.VK_O));
		getSpinner(GamepadButtonEvent.Button_Sel).setValue(KeyEvent.getKeyText(KeyEvent.VK_G));
		getSpinner(GamepadButtonEvent.Button_Sta).setValue(KeyEvent.getKeyText(KeyEvent.VK_H));
	}

}
