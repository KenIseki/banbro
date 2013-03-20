package banbro.util;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;

@SuppressWarnings("serial")
public abstract class OptionDialog extends AbstractDialog {

	private JButton _defaultButton;
	private JButton _okButton;
	private JButton _cancelButton;

	public OptionDialog(Window owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	@Override
	protected List<JButton> createButtons() {
		_defaultButton = createButton("既定値");
		_okButton = createButton("了解");
		_cancelButton = createButton("取消し");
		_defaultButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pushDefaultButton();
			}
		});
		_okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pushOkButton();
				close();
			}
		});
		_cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		_defaultButton.setToolTipText("既定値に戻します");
		_okButton.setToolTipText("現在の設定を保存してダイアログを閉じます");
		_cancelButton.setToolTipText("現在の設定を破棄してダイアログを閉じます");
		return Arrays.asList(_defaultButton, _okButton, _cancelButton);
	}

	/**
	 * 規定値ボタンが押されたときの処理
	 * ダイアログの内容を規定値に戻す
	 */
	protected abstract void pushDefaultButton();

	/**
	 * 了解ボタンが押されたときの処理
	 * ダイアログの内容を保存する
	 */
	protected abstract void pushOkButton();

	/**
	 * ダイアログの内容を変更したときに呼ぶ
	 */
	protected void update() {
		_okButton.setEnabled(check());
	}

	/**
	 * ダイアログの内容が妥当か？（了解ボタンを押しても良いか？）
	 * @return 妥当ならtrue デフォルトは常にtrue
	 */
	protected boolean check() {
		return true;
	}

}
