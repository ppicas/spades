package cat.ppicas.spades.query;

import cat.ppicas.spades.Column;

class SelectedColumn {

	private Column mColumn;
	private String mCustom;

	protected SelectedColumn(Column column) {
		mColumn = column;
	}

	protected SelectedColumn(String custom) {
		mCustom = custom;
	}

	public boolean isCustom() {
		return mCustom != null;
	}

	public Column getColumn() {
		return mColumn;
	}

	public String getCustom() {
		return mCustom;
	}

}
