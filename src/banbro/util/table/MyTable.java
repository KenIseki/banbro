package banbro.util.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class MyTable extends JTable {
	private static final long serialVersionUID = 1L;

	private Color _oddRowBackground = new Color(0xCC, 0xCC, 0xCC);  // äÔêîçsÇÃîwåiêF

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
