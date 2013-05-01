package cat.ppicas.spades.query;

import java.util.ArrayList;
import java.util.List;

import cat.ppicas.spades.Column;
import cat.ppicas.spades.Table;

class ColumnSelector {

	private List<Table<?>> mTables = new ArrayList<Table<?>>();
	private List<SelectedColumn> mSelected = new ArrayList<SelectedColumn>();

	public ColumnSelector() {
	}

	public void addTable(Table<?> table) {
		mTables.add(table);
	}

	public List<SelectedColumn> getSelectedColumns() {
		List<SelectedColumn> selected = new ArrayList<SelectedColumn>();

		if (mSelected.isEmpty()) {
			for (Table<?> table : mTables) {
				for (Column column : table.getColumns()) {
					selected.add(new SelectedColumn(column));
				}
			}
		} else {
			selected = mSelected;
		}

		return selected;
	}

	public void selectColumn(Column name) {
		mSelected.add(new SelectedColumn(name));
	}

	public void setAutoTablesId(boolean b) {
	}

}
