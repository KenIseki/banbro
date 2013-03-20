package banbro.util.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class MyTableModel<V extends MyTableItem> extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
	
	private List<V> _list;

	public MyTableModel(String[] columnNames, List<V> list) {
		super(columnNames, 0);
		_list = new ArrayList<V>();
		for (V item : list) {
			addRow(item);
		}
		addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent event) {
				int type = event.getType();
				if (type==TableModelEvent.UPDATE) {
					int row = event.getLastRow();
					int column = event.getColumn();
					Object value = getValueAt(row, column);
					if (value==null) {
						value = "";
					}
					_list.get(row).setColumnValue(getColumnName(column), value);
				}
			}
		});
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Object object = getValueAt(0, columnIndex);
		if (object!=null) {
			return object.getClass();
		}
		return super.getColumnClass(columnIndex);
	}

	public void addRow(V item) {
		if (_list.contains(item)) {
			return;
		}
		_list.add(item);
		addRow(convertRowData(item));
	}

	public void insertRow(int row, V item) {
		if (row<0 || getRowCount()<row) {
			return;
		}
		_list.add(row, item);
		insertRow(row, convertRowData(item));
	}

	@Override
	public void removeRow(int row) {
		if (row<0 || _list.size()<=row) {
			return;
		}
		_list.remove(row);
		super.removeRow(row);
	}

	public void removeAll() {
		setRowCount(0);
		_list.clear();
	}

	private Object[] convertRowData(V item) {
		Object[] data = new Object[getColumnCount()];
		for (int i=0; i<data.length; i++) {
			String name = getColumnName(i);
			Object value = item.getColumnValue(name);
			if (value==null) {
				data[i] = "";
			} else {
				data[i] = value;
			}
		}		
		return data;
	}

	public V getRow(int row) {
		if (row<0 || _list.size()<=row) {
			return null;
		}
		return _list.get(row);
	}

	public void setValueAt(Object value, int row, String name) {
		int column = findColumn(name);
		if (column<0) {
			return;
		}
		setValueAt(value, row, column);
	}

	public void tableCellUpdate(int row) {
		V item = _list.get(row);
		for (int column=0; column<getColumnCount(); column++) {
			String name = getColumnName(column);
			setValueAt(item.getColumnValue(name), row, column);
		}
	}

	public void tableCellUpdate(int row, String... names) {
		V item = _list.get(row);
		for (String name : names) {
			int column = findColumn(name);
			if (column<0) {
				continue;
			}
			setValueAt(item.getColumnValue(name), row, column);
		}
	}

}
