package banbro.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public abstract class AbstractDialog extends JDialog {

	public AbstractDialog(Window owner, String title, boolean modal) {
		super(owner, title);
		setModal(modal);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		Component _mainComponent = createMainComponent();
		if (_mainComponent==null) {
			_mainComponent = new JPanel();
		}
		contentPane.add(_mainComponent, BorderLayout.CENTER);

		Box _box = Box.createHorizontalBox();
		_box.add(Box.createHorizontalGlue());
		List<JButton> buttonList = createButtons();
		int height = 0;
		if (buttonList!=null) {
			for (JButton button : buttonList) {
				height = Math.max(height, button.getPreferredSize().height);
				_box.add(button);
				_box.add(Box.createHorizontalStrut(5));
			}
		}
		_box.add(Box.createRigidArea(new Dimension(0, height+10)));
		contentPane.add(_box, BorderLayout.SOUTH);

		initSize();
		initLocation(owner.getBounds());

		AbstractAction act = new AbstractAction() {
			@Override public void actionPerformed(ActionEvent e) {
				close();
			}
		};
		InputMap imap = getRootPane().getInputMap(
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close-it");
		getRootPane().getActionMap().put("close-it", act);
	}

	/**
	 * ダイアログの中央部分を作成する
	 * @return
	 */
	protected abstract Component createMainComponent();

	/**
	 * ボタンを作成する
	 * ダイアログの下部に左から順に配置される
	 * @return
	 */
	protected abstract List<JButton> createButtons();

	protected JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setPreferredSize(new Dimension(100, button.getPreferredSize().height));
		return button;
	}

	/**
	 * サイズ調整
	 * デフォルトは適切なサイズにしてサイズ変更不可にする
	 */
	protected void initSize() {
		pack();
		setResizable(false);
	}

	/**
	 * 位置調整
	 * デフォルトはこのダイアログとrectの中央が重なるようにする
	 * @param rect 親ウインドウの長方形
	 */
	protected void initLocation(Rectangle rect) {
		Dimension size = getSize();
		int x = rect.x + rect.width/2;
		int y = rect.y + rect.height/2;
		x -= size.width/2;
		y -= size.height/2;
		x = Math.max(x, 0);
		y = Math.max(y, 0);
		setLocation(x, y);
	}

	protected void close() {
		dispose();
	}

}
