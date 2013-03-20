package banbro.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public abstract class EditTextDialog extends AbstractDialog {

	protected JTextField _textField;
	private JLabel _errorLabel;
	private JButton _commitButton;
	private JButton _closeButton;

	public EditTextDialog(Window owner, String title) {
		super(owner, title, true);
		checkTextField();
	}

	@Override
	protected Component createMainComponent() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		_textField = new JTextField("");
		_textField.setPreferredSize(new Dimension(100, _textField.getPreferredSize().height));
		_textField.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				return checkTextField();
			}
		});
		_textField.addKeyListener(new KeyListener() {
			private boolean _commit = false;
			private char _lastChar;
			@Override
			public void keyTyped(KeyEvent ev) {
				if (_lastChar==ev.getKeyChar()) {
					_commit = checkTextField();
				}
			}
			@Override
			public void keyReleased(KeyEvent ev) {
				_commit = checkTextField();
			}
			@Override
			public void keyPressed(KeyEvent ev) {
				_lastChar = ev.getKeyChar();
				if (ev.getKeyCode()==KeyEvent.VK_ENTER) {
					if (_commit==false) {
						ev.consume();
					} else {
						pushCommitButton();
					}
				}
			}
		});
		GridBagConstraints constraints = (GridBagConstraints) getDefaultConstraints(0, 0, 1, 1);
		panel.add(_textField, constraints);
		_errorLabel = new JLabel(" ");
		_errorLabel.setForeground(Color.RED);
		_errorLabel.setPreferredSize(new Dimension(100, _errorLabel.getPreferredSize().height));
		panel.add(_errorLabel, getDefaultConstraints(0, 1, 1, 1));
		return panel;
	}

	protected GridBagConstraints getDefaultConstraints(int x, int y, int w, int h) {
		GridBagConstraints constraints = ComponentUtil.createConstraints(x, y, w, h);
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5,5,5,5);
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.ipadx = 100;
		return constraints;
	}

	@Override
	protected List<JButton> createButtons() {
		List<JButton> buttonList = new ArrayList<JButton>();
		_commitButton = createButton("Œˆ’è");
		_closeButton = createButton("•Â‚¶‚é");
		_commitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pushCommitButton();
			}
		});
		_closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		buttonList.add(_commitButton);
		buttonList.add(_closeButton);
		return buttonList;
	}

	private void pushCommitButton() {
		if (checkTextField()==false) {
			close();
			return;
		}
		commit(_textField.getText());
		close();
	}

	protected abstract void commit(String text);

	protected boolean checkTextField() {
		String message = checkTextField(_textField.getText());
		boolean b = message==null;
		if (b) {
			_errorLabel.setText("");
		} else {
			_errorLabel.setText(message);
		}
		_commitButton.setEnabled(b);
		return b;
	}

	abstract protected String checkTextField(String text);

}
