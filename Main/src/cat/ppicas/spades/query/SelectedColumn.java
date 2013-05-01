package cat.ppicas.spades.query;

import cat.ppicas.spades.Column;

class SelectedColumn {

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

}
