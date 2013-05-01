package cat.ppicas.spades.query;

import cat.ppicas.spades.Column;

class SelectedColumn {

	private Column mColumn;
	private String mCustomExpression;
	private Column[] mCustomColumns;

	protected SelectedColumn(Column column) {
		mColumn = column;
	}

	protected SelectedColumn(String expression, Column... columns) {
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
