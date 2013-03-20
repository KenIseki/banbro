package banbro.model.arrange;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import banbro.util.ComponentUtil;
import banbro.util.OptionDialog;

public class ArrangeOptionDialog extends OptionDialog {
	private static final long serialVersionUID = 1L;

	private JCheckBox _arrangeTempoCheckBox;
	private JButton _shuffleButton;
	
	private ArrangeOption _option;
	
	public ArrangeOptionDialog(Window owner, ArrangeOption option) {
		super(owner, "�A�����W�ݒ�", true);
		_option = option;
		_arrangeTempoCheckBox.setSelected(_option.isArrangeTempo());
	}

	@Override
	protected Component createMainComponent() {
		JPanel mainPanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		mainPanel.setLayout(layout);
		int y = 0;
		
		_arrangeTempoCheckBox = new JCheckBox("���t�C���[�W�ɂ��e���|�ύX������");
		mainPanel.add(_arrangeTempoCheckBox, getDefaultConstraints(0, y, 1, 1));
		y++;

		_shuffleButton = new JButton("�V���b�t���ݒ�");
		_shuffleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShuffleDialog dialog = new ShuffleDialog(ArrangeOptionDialog.this, _option);
				dialog.setVisible(true);
			}
		});
		mainPanel.add(_shuffleButton, getDefaultConstraints(0, y, 1, 1));
		y++;

		return mainPanel;
	}

	@Override
	protected void pushDefaultButton() {
		_arrangeTempoCheckBox.setSelected(true);
	}

	@Override
	protected void pushOkButton() {
		_option.setArrangeTempo(_arrangeTempoCheckBox.isSelected());
	}

	protected GridBagConstraints getDefaultConstraints(int x, int y, int w, int h) {
		GridBagConstraints constraints = ComponentUtil.createConstraints(x, y, w, h);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5,5,5,5);
		return constraints;
	}

}
