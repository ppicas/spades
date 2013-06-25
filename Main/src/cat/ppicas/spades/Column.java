package cat.ppicas.spades;

import cat.ppicas.spades.map.ValueMapper;

public class Column {

	public static class ColumnId extends Column {

		protected ColumnId(int index) {
			super(index, "id", "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL", true, null);
		}

	}

	public final int index;
	public final String name;
	public final boolean notNull;
	public final ValueMapper valueMapper;

	private Table<?> mTable;
	private String mDefinition;

	protected Column(int index, String name, String definition, boolean notNull,
			ValueMapper valueMapper) {
		this.index = index;
		this.name = name;
		this.notNull = notNull;
		this.valueMapper = valueMapper;
		mDefinition = definition;
	}

	public Table<?> getTable() {
		if (mTable == null) {
			throw new IllegalStateException("This column is not associated with a Table");
		}
		return mTable;
	}

	public String getDefinition() {
		return name + " " + mDefinition;
	}

	@Override
	public String toString() {
		return name;
	}

	protected void setTable(Table<?> table) {
		if (mTable != null) {
			throw new IllegalStateException("This column is already associated with a Table");
		}
		mTable = table;
	}

}
