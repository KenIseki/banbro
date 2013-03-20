package banbro.util.table;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * ダブルクリックで列幅を自動調整するヘッダ
 */
class AutoResizeTableHeader extends JTableHeader {
	private static final long serialVersionUID = 1L;

	public AutoResizeTableHeader(TableColumnModel columnModel) {
		super(columnModel);
	}

	@Override
	protected void processMouseEvent(MouseEvent e) {
		if (e.getID()==MouseEvent.MOUSE_CLICKED && SwingUtilities.isLeftMouseButton(e)
				&& getCursor().getType()==Cursor.E_RESIZE_CURSOR) {
			// 矢印カーソルで左ボタンクリック
			if (e.getClickCount()==2) {
				Point pt = new Point(e.getX()-3, e.getY());  // 列幅変更の場合、3ピクセルずらされて考慮されている
				int columnIndex = columnAtPoint(pt);
				if (columnIndex >= 0) {
					JTable table = getTable();
					
					// ヘッダ部分
					TableColumn tc = table.getColumnModel().getColumn(columnIndex);
					TableCellRenderer renderer = tc.getHeaderRenderer();
					if (renderer==null) {
						renderer = getDefaultRenderer();
					}
					Object value = tc.getHeaderValue();
					Component c = renderer.getTableCellRendererComponent(table, value, false, false, 0, columnIndex);
					int max = c.getPreferredSize().width;
					
					// 行
					for (int rowIndex=0; rowIndex<table.getRowCount(); rowIndex++) {
						renderer = table.getCellRenderer(rowIndex, columnIndex);
						if (renderer==null) {
							renderer = getDefaultRenderer();
						}
						value = table.getValueAt(rowIndex, columnIndex);
						c = renderer.getTableCellRendererComponent(table, value, false, false, rowIndex, columnIndex);
						max = Math.max(max, c.getPreferredSize().width);
					}

					tc.setPreferredWidth(max+1);
					e.consume();
					return;
				}
			} else {
				return;
			}
		}
		super.processMouseEvent(e);
	}

}