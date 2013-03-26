package banbro.util.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class MyTable extends JTable {
	private static final long serialVersionUID = 1L;

	private Color _oddRowBackground = new Color(0xCC, 0xCC, 0xCC);  // ��s�̔w�i�F

	public Color getOddRowBackground() {
		return _oddRowBackground;
	}

	public void setOddRowBackground(Color bg) {
		_oddRowBackground = bg;
	}

	@Override
	protected JTableHeader createDefaultTableHeader() {
		return new AutoResizeTableHeader(getColumnModel());
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		int row = rowAtPoint(event.getPoint());
		int col = columnAtPoint(event.getPoint());
		if (row<getRowCount() && col<getColumnCount()) {
			Object value = getModel().getValueAt(row, col);
			if (value==null) {
				return null;
			} else {
				StringBuilder sb = new StringBuilder();
				for (char c : value.toString().toCharArray()) {
					switch (c) {
					case '&':
						sb.append("&amp;");
						break;
					case '<':
						sb.append("&lt;");
						break;
					case '>':
						sb.append("&gt;");
						break;
					case '"':
						sb.append("&quot;");
						break;
					case '\'':
						sb.append("&#39;");
						break;
					case '\n':
						sb.append("<br>");
						break;
					default:
						sb.append(c);
						break;
					}
				}
				if (sb.length()==0) {
					return null;
				}
				sb.insert(0, "<html>");
				sb.append("</html>");
				return sb.toString();
			}
		}
		return null;
	}

	@Override
	public void doLayout() {
		updateHeight(this);
		super.doLayout();
	}

	private void updateHeight(JTable table) {
		for (int row=0; row<table.getRowCount(); row++) {
			int h = 0;
			for (int col=0; col<table.getColumnCount(); col++) {
				Component c = table.prepareRenderer(table.getCellRenderer(row, col), row, col);
				h = Math.max(h, c.getPreferredSize().height);
			}
			if (h!=0) {
				table.setRowHeight(row, h);
			}
		}
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component c = super.prepareRenderer(renderer, row, column);
		if (!isCellSelected(row, column)) {
			if (row%2==1) {
				c.setBackground(getOddRowBackground());
			} else {
				c.setBackground(getBackground());
			}
		}
		return c;
	}

}
