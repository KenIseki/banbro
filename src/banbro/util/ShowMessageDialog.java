package banbro.util;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ShowMessageDialog implements Runnable {
	private Component _parent;
	private Object _message;
	private String _title;
	private int _messageType;

	/**
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param messageType
	 * @see JOptionPane#showMessageDialog(Component, Object, String, int)
	 */
	public ShowMessageDialog(Component parentComponent,
			Object message, String title,
			int messageType) {
		_parent = parentComponent;
		_message = message;
		_title = title;
		_messageType = messageType;
	}

	@Override
	public void run() {
		JOptionPane.showMessageDialog(
				_parent,
				_message,_title,
				_messageType);
	}

	public static void openDialog(final ShowMessageDialog dialog) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					SwingUtilities.invokeAndWait(new Thread() {
						@Override
						public void run() {
							dialog.run();
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

}