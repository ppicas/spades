package cat.ppicas.spades.query;

import java.util.ArrayList;
import java.util.List;

import cat.ppicas.spades.Column;
import cat.ppicas.spades.Table;
import cat.ppicas.spades.query.SelectedColumn.CountColumn;
import cat.ppicas.spades.query.SelectedColumn.RowIdColumn;

class ColumnSelector {

	private List<Table<?>> mTables = new ArrayList<Table<?>>();
	private List<Column> mColumns = new ArrayList<Column>();
	private List<SelectedColumn> mSelected = new ArrayList<SelectedColumn>();
	private boolean mAutoEntitiesId = false;
	private boolean mAutoRowsId;

	public ColumnSelector() {
	}

	public void addTable(Table<?> table) {
		if (mTables.contains(table)) {
			throw new IllegalStateException("Table already selected");
		}
		mTables.add(table);
	}

	public boolean hasTable(Table<?> table) {
		return mTables.contains(table);
	}

	public boolean hasTables() {
		return !mTables.isEmpty();
	}

	public void selectColumn(Column column) {
		checkColumn(column);
		mColumns.add(column);
		mSelected.add(new SelectedColumn(column));
	}

	public void selectColumn(String expression, Column... columns) {
		for (Column column : columns) {
			checkColumn(column);
		}
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
			selected.addAll(mSelected);

			if (mAutoEntitiesId) {
				addAutoTableIds(selected);
			}
		}

		if (mAutoRowsId && !mTables.isEmpty()) {
			selected.add(new RowIdColumn(mTables.get(0).getColumnId()));
		}

		return selected;
	}

	public List<SelectedColumn> getSelectedColumnsForCount() {
		List<SelectedColumn> selected = new ArrayList<SelectedColumn>();

		selected.add(new CountColumn());
		for (SelectedColumn sel : mSelected) {
			if (sel.isCustom()) {
				selected.add(sel);
			}
		}

		return selected;
	}

	public boolean isAutoEntitiesId() {
		return mAutoEntitiesId;
	}

	public void setAutoEntitiesId(boolean autoEntitiesId) {
		mAutoEntitiesId = autoEntitiesId;
	}

	public boolean isAutoRowsId() {
		return mAutoRowsId;
	}

	public void setAutoRowsId(boolean autoRowsId) {
		mAutoRowsId = autoRowsId;
	}

	private void checkColumn(Column column) {
		if (!mTables.contains(column.getTable())) {
			throw new IllegalArgumentException("The selected Column dosen't belongs to a table "
					+ "added before");
		}
	}

	private void addAutoTableIds(List<SelectedColumn> selected) {
		List<SelectedColumn> addList = new ArrayList<SelectedColumn>();
		for (Table<?> table : mTables) {
			if (!mColumns.contains(table.getColumnId())) {
				addList.add(new SelectedColumn(table.getColumnId()));
			}
		}
		selected.addAll(addList);
	}

}
