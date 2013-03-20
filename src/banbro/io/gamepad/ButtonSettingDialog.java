package banbro.io.gamepad;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import banbro.util.OptionDialog;
import banbro.util.ComponentUtil;

@SuppressWarnings("serial")
abstract class ButtonSettingDialog extends OptionDialog {

	// 各ボタンにデバイスのボタン番号を設定する
	// ボタン名(番号)->デバイスのボタン番号
	private Map<Integer, JSpinner> _buttonSpinner;
	private JLabel _label;
	private Map<Integer, Integer> _buttonSetting;

	public ButtonSettingDialog(Window owner, String title) {
		super(owner, title, true);
	}

	/**
	 * －０１２３４５６７８９０１２
	 * <br>０Ｌ＊　　　　　　　　　＊Ｒ
	 * <br>１　　　＊　　　　　＊
	 * <br>２　　　Ｕ　　　　　Ｘ
	 * <br>３　＊Ｈ　Ｍ＊　＊Ｙ　Ａ＊
	 * <br>４　　　Ｄ　　　　　Ｂ
	 * <br>５　　　＊　　　　　＊
	 * <br>６　　ＳＥＬ＊　＊ＳＴＡ
	 * @see banbro.util.OptionDialog#createMainComponent()
	 */
	@Override
	protected Component createMainComponent() {
		JPanel panel = new JPanel(new GridBagLayout());
		
		panel.add(new JLabel(GamepadButtonEvent.getButtonName(GamepadButtonEvent.Button_L)), getDefaultConstraints(0, 0, 1, 1));
		panel.add(new JLabel(GamepadButtonEvent.getButtonName(GamepadButtonEvent.Button_R)), getDefaultConstraints(12, 0, 1, 1));
		panel.add(new JLabel(GamepadButtonEvent.getButtonName(GamepadButtonEvent.Button_U)), getDefaultConstraints(3, 2, 1, 1));
		panel.add(new JLabel(GamepadButtonEvent.getButtonName(GamepadButtonEvent.Button_X)), getDefaultConstraints(9, 2, 1, 1));
		panel.add(new JLabel(GamepadButtonEvent.getButtonName(GamepadButtonEvent.Button_H)), getDefaultConstraints(2, 3, 1, 1));
		panel.add(new JLabel(GamepadButtonEvent.getButtonName(GamepadButtonEvent.Button_M)), getDefaultConstraints(4, 3, 1, 1));
		panel.add(new JLabel("　"), getDefaultConstraints(6, 3, 1, 1));
		panel.add(new JLabel(GamepadButtonEvent.getButtonName(GamepadButtonEvent.Button_Y)), getDefaultConstraints(8, 3, 1, 1));
		panel.add(new JLabel(GamepadButtonEvent.getButtonName(GamepadButtonEvent.Button_A)), getDefaultConstraints(10, 3, 1, 1));
		panel.add(new JLabel(GamepadButtonEvent.getButtonName(GamepadButtonEvent.Button_D)), getDefaultConstraints(3, 4, 1, 1));
		panel.add(new JLabel(GamepadButtonEvent.getButtonName(GamepadButtonEvent.Button_B)), getDefaultConstraints(9, 4, 1, 1));
		panel.add(new JLabel(GamepadButtonEvent.getButtonName(GamepadButtonEvent.Button_Sel)), getDefaultConstraints(3, 6, 2, 1));
		panel.add(new JLabel(GamepadButtonEvent.getButtonName(GamepadButtonEvent.Button_Sta)), getDefaultConstraints(8, 6, 2, 1));

		_buttonSpinner = new HashMap<Integer, JSpinner>();
		for (int b : getButtons()) {
			_buttonSpinner.put(b, createSpinner(b));
		}
		
		Map<Integer, GridBagConstraints> spinnerConst = new HashMap<Integer, GridBagConstraints>();
		spinnerConst.put(GamepadButtonEvent.Button_L, getDefaultConstraints(1, 0, 1, 1));
		spinnerConst.put(GamepadButtonEvent.Button_R, getDefaultConstraints(11, 0, 1, 1));
		spinnerConst.put(GamepadButtonEvent.Button_U, getDefaultConstraints(3, 1, 1, 1));
		spinnerConst.put(GamepadButtonEvent.Button_X, getDefaultConstraints(9, 1, 1, 1));
		spinnerConst.put(GamepadButtonEvent.Button_H, getDefaultConstraints(1, 3, 1, 1));
		spinnerConst.put(GamepadButtonEvent.Button_M, getDefaultConstraints(5, 3, 1, 1));
		spinnerConst.put(GamepadButtonEvent.Button_Y, getDefaultConstraints(7, 3, 1, 1));
		spinnerConst.put(GamepadButtonEvent.Button_A, getDefaultConstraints(11, 3, 1, 1));
		spinnerConst.put(GamepadButtonEvent.Button_D, getDefaultConstraints(3, 5, 1, 1));
		spinnerConst.put(GamepadButtonEvent.Button_B, getDefaultConstraints(9, 5, 1, 1));
		spinnerConst.put(GamepadButtonEvent.Button_Sel, getDefaultConstraints(5, 6, 1, 1));
		spinnerConst.put(GamepadButtonEvent.Button_Sta, getDefaultConstraints(7, 6, 1, 1));
		
		for (int b : getButtons()) {
			panel.add(getSpinner(b), spinnerConst.get(b));
		}
		
		_label = new JLabel(" ");
		_label.setForeground(Color.RED);
		GridBagConstraints constraints = ComponentUtil.createConstraints(0, 7, 13, 1);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10,5,10,5);
		panel.add(_label, constraints);
		return panel;
	}

	protected GridBagConstraints getDefaultConstraints(int x, int y, int w, int h) {
		GridBagConstraints constraints = ComponentUtil.createConstraints(x, y, w, h);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(5,5,5,5);
		return constraints;
	}

	abstract protected JSpinner createSpinner(int b);

	abstract public void readButtonSetting(Map<Integer, Integer> setting);

	protected JSpinner getSpinner(int b) {
		return _buttonSpinner.get(b);
	}

	@Override
	protected boolean check() {
		// ボタン番号の重複チェック
		Set<Object> set = new HashSet<Object>();
		for (int i : getButtons()) {
			JSpinner spinner = getSpinner(i);
			set.add(spinner.getValue());
		}
		if (getButtons().length!=set.size()) {
			_label.setText("割り当てが重複しています");
			return false;
		}
		_label.setText(" ");
		return true;
	}

	abstract protected int[] getButtons();

	@Override
	protected void pushOkButton() {
		_buttonSetting = new HashMap<Integer, Integer>();
		for (int b : getButtons()) {
			Object value = getSpinner(b).getValue();
			_buttonSetting.put(b, calcButtonNum(value));
		}
	}
	
	abstract protected int calcButtonNum(Object value);

	@Override
	protected void close() {
		_buttonSpinner.clear();
		super.close();
	}

	public Map<Integer, Integer> getButtonSetting() {
		return _buttonSetting;
	}

}
