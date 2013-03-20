package banbro.util.table;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * データに改行があれば、複数行にして表示する
 */
public class MultiLineCellRenderer extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	public MultiLineCellRenderer() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		removeAll();
		
		String[] lines = value.toString().split("\n", -1);
		if (lines.length==0) {
			lines = new String[] {""};
		}

		add(Box.createVerticalGlue());
		for (String line : lines) {
			JLabel label = new JLabel(line);
			label.setFont(table.getFont());
			add(label);
		}
		add(Box.createVerticalGlue());

		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		} else {
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		}
		if (hasFocus) {
			setBorder(BorderFactory.createLineBorder(table.getGridColor(), 1));
		} else {
			setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		}

		return this;
	}


}
