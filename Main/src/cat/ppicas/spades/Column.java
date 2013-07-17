package cat.ppicas.spades;

import cat.ppicas.spades.map.MappedField;

public class Column {

	public static class ColumnId extends Column {
		protected ColumnId(int index, Table table, String name) {
			super(index, table, name, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL", true, null);
		}
	}

	public final int index;
	public final Table table;
	public final String name;
	public final boolean notNull;
	public final MappedField mappedField;

	private String mDefinition;

	protected Column(int index, Table table, String name, String definition, boolean notNull,
			MappedField mappedField) {
		this.index = index;
		this.table = table;
		this.name = name;
		this.notNull = notNull;
		this.mappedField = mappedField;
		mDefinition = definition;
	}

	public String getDefinition() {
		return name + " " + mDefinition;
	}

	@Override
	public String toString() {
		return name;
	}

}
