package banbro.util;

import java.awt.Component;

import javax.swing.JOptionPane;

public class ShowConfirmDialog implements Runnable {
	private Component _parent;
	private Object _message;
	private String _title;
	private int _optionType;
	private int _messageType;

	private int _returnOption;

	/**
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param optionType
	 * @param messageType
	 * @see JOptionPane#showConfirmDialog(Component, Object, String, int, int)
	 */
	public ShowConfirmDialog(Component parentComponent,
			Object message, String title,
			int optionType, int messageType) {
		_parent = parentComponent;
		_message = message;
		_title = title;
		_optionType = optionType;
		_messageType = messageType;
	}

	@Override
	public void run() {
		_returnOption = JOptionPane.showConfirmDialog(
				_parent,
				_message,_title,
				_optionType,_messageType);
	}

	public int getReturnOption() {
		return _returnOption;
	}

}