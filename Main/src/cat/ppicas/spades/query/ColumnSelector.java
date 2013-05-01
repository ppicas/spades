package cat.ppicas.spades.query;

import java.util.ArrayList;
import java.util.List;

import cat.ppicas.spades.Column;
import cat.ppicas.spades.Table;

class ColumnSelector {

	private List<Table<?>> mTables = new ArrayList<Table<?>>();
	private List<Column> mColumns = new ArrayList<Column>();
	private List<SelectedColumn> mSelected = new ArrayList<SelectedColumn>();
	private boolean mAutoEntitiesId = false;
	private boolean mAutoRowsId;

	public ColumnSelector() {
	}

	public void addTable(Table<?> table) {
		mTables.add(table);
	}

	public void selectColumn(Column column) {
		if (!mTables.contains(column.table)) {
			throw new IllegalArgumentException("The selected Column dosen't belongs to an added Table");
		}
		mColumns.add(column);
		mSelected.add(new SelectedColumn(column));
	}

	public void selectColumn(String expression, Column... columns) {
		mSelected.add(new SelectedColumn(expression, columns));
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

			if (mAutoEntitiesId) {
				addAutoTableIds(selected);
			}

			if (mAutoRowsId && !mTables.isEmpty() && (selected.isEmpty()
					|| selected.get(0).getColumn() != mTables.get(0).getColumnId())) {
				selected.add(0, new SelectedColumn(mTables.get(0).getColumnId()));
			}
		}


		return selected;
	}

	public void setAutoEntitiesId(boolean autoEntitiesId) {
		mAutoEntitiesId = autoEntitiesId;
	}

	public void setAutoRowsId(boolean autoRowsId) {
		mAutoRowsId = autoRowsId;
	}

	private void addAutoTableIds(List<SelectedColumn> selected) {
		List<SelectedColumn> addList = new ArrayList<SelectedColumn>();
		for (Table<?> table : mTables) {
			if (!mColumns.contains(table.getColumnId())) {
				addList.add(new SelectedColumn(table.getColumnId()));
			}
		}
		selected.addAll(0, addList);
	}

}
