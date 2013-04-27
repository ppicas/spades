package cat.ppicas.spades;

import cat.ppicas.spades.map.ValueMapper;

public class Column {

	public final Table<?> table;
	public final int index;
	public final String name;
	public final boolean notNull;
	public final ValueMapper valueMapper;

	private String mDefinition;

	Column(Table<?> table, int index, String name, String def, boolean notNull, ValueMapper valueMapper) {
		this.table = table;
		this.index = index;
		this.name = name;
		mDefinition = def;
		this.notNull = notNull;
		this.valueMapper = valueMapper;
	}

	public String getDefinition() {
		return name + " " + mDefinition;
	}

	@Override
	public String toString() {
		return name;
	}

	public static class ColumnId extends Column {

		ColumnId(Table<?> table, int index) {
			super(table, index, "_id", "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL", true, null);
		}

	}

}
