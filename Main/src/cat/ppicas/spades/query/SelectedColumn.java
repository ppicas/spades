package cat.ppicas.spades.query;

import android.provider.BaseColumns;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.Column.ColumnId;

class SelectedColumn {

	private NameMapper mMapper = Query.getMapper();
	private Column mColumn;
	private String mCustomExpression;
	private Column[] mCustomColumns;

	public SelectedColumn(Column column) {
		mColumn = column;
	}

	public SelectedColumn(String expression, Column... columns) {
		mCustomExpression = expression;
		mCustomColumns = columns;
	}

	public boolean isCustom() {
		return mCustomExpression != null;
	}

	public Column getColumn() {
		return mColumn;
	}

	public String getCustomExpression() {
		return mCustomExpression;
	}

	public Column[] getCustomColumns() {
		return mCustomColumns;
	}

	public String format(boolean magicColumn) {
		if (!isCustom()) {
			if (magicColumn) {
				return mMapper.ref(mColumn) + " AS " + mMapper.alias(mColumn);
			} else {
				return mMapper.ref(mColumn);
			}
		} else {
			return Query.expr(mCustomExpression, mCustomColumns);
		}
	}

	static class RowIdColumn extends SelectedColumn {

		public RowIdColumn(ColumnId column) {
			super("%s as " + BaseColumns._ID, column);
		}

	}

	static class CountColumn extends SelectedColumn {

		public CountColumn() {
			super("COUNT(*)");
		}

	}

}
