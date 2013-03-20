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
		_defaultButton = createButton("����l");
		_okButton = createButton("����");
		_cancelButton = createButton("�����");
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
		_defaultButton.setToolTipText("����l�ɖ߂��܂�");
		_okButton.setToolTipText("���݂̐ݒ��ۑ����ă_�C�A���O����܂�");
		_cancelButton.setToolTipText("���݂̐ݒ��j�����ă_�C�A���O����܂�");
		return Arrays.asList(_defaultButton, _okButton, _cancelButton);
	}

	/**
	 * �K��l�{�^���������ꂽ�Ƃ��̏���
	 * �_�C�A���O�̓��e���K��l�ɖ߂�
	 */
	protected abstract void pushDefaultButton();

	/**
	 * �����{�^���������ꂽ�Ƃ��̏���
	 * �_�C�A���O�̓��e��ۑ�����
	 */
	protected abstract void pushOkButton();

	/**
	 * �_�C�A���O�̓��e��ύX�����Ƃ��ɌĂ�
	 */
	protected void update() {
		_okButton.setEnabled(check());
	}

	/**
	 * �_�C�A���O�̓��e���Ó����H�i�����{�^���������Ă��ǂ����H�j
	 * @return �Ó��Ȃ�true �f�t�H���g�͏��true
	 */
	protected boolean check() {
		return true;
	}

}
