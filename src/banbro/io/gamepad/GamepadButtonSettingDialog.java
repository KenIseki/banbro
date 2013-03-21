package banbro.io.gamepad;

import java.awt.Window;
import java.util.Map;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GamepadButtonSettingDialog extends ButtonSettingDialog {
	private static final long serialVersionUID = 1L;

	private static int[] buttons = {
			GamepadButtonEvent.Button_Y,
			GamepadButtonEvent.Button_B,
			GamepadButtonEvent.Button_A,
			GamepadButtonEvent.Button_X,
			GamepadButtonEvent.Button_L,
			GamepadButtonEvent.Button_R,
			GamepadButtonEvent.Button_Sel,
			GamepadButtonEvent.Button_Sta
	};

	public GamepadButtonSettingDialog(Window owner) {
		super(owner, "ゲームパッド設定");
	}

	@Override
	protected int[] getButtons() {
		return buttons;
	}

	@Override
	protected JSpinner createSpinner(int b) {
		SpinnerNumberModel model = new SpinnerNumberModel(b,0,15,1);
		JSpinner spinner = new JSpinner(model);
		((JSpinner.NumberEditor)spinner.getEditor()).getTextField().setEditable(false);
		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				update();
			}
		});
		return spinner;
	}

	@Override
	public void readButtonSetting(Map<Integer, Integer> setting) {
		for (int b : getButtons()) {
			Integer i = setting.get(b);
			if (i!=null) {
				getSpinner(b).setValue(i);
			}
		}
		update();
	}

	@Override
	protected void pushDefaultButton() {
		for (int b : getButtons()) {
			getSpinner(b).setValue(b);
		}
	}

	@Override
	protected int calcButtonNum(Object value) {
		return ((Integer)value).intValue();
	}
	
}
