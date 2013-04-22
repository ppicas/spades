package cat.ppicas.spades;

import java.util.List;

import android.database.Cursor;

public class NameMapper {

	private static final String PREFIX = "__T";

	private Tables mTables = Tables.getInstance();

	public String alias(Table<?> table) {
		return PREFIX + table.index;
	}

	/**
	 * Generates an unique column alias representing this {@link Column}. This alias {@link String}
	 * will be unique from the rest of other {@link Column} possible alias. The format
	 * of this alias is the following: {@literal __<TABLE INDEX>_<COLUMN_INDEX>}. For
	 * example a generated alias can be __T4_1.
	 *
	 * @param The {@link Column} to generate the alias
	 * @return
	 */
	public String alias(Column col) {
		return PREFIX + col.table.index + "_" + col.index;
	}

	public String ref(Column col) {
		return PREFIX + col.table.index + "." + col.name;
	}

	public String[] refs(Column... cols) {
		String[] alias = new String[cols.length];
		for (int i = 0; i < cols.length; i++) {
			alias[i] = ref(cols[i]);
		}
		return alias;
	}

	/**
	 * Parse a column name proceeding from {@link Cursor} with the format generated
	 * by {@link #alias(Column)}. For example the column name __T2_3 will return the
	 * 3rd registered {@link Column} object of the 2nd registered {@link OldTable}.
	 *
	 * @param Cursor column name
	 * @return A {@link Column}
	 * @see #alias(Column)
	 */
	public Column parseColumnName(String name) {
		// TODO Compare performace with other techniques as regex parsing
		// or precalculated column aliases.
		if (!name.startsWith(PREFIX)) {
			return null;
		};
		name = name.substring(PREFIX.length());
		StringBuilder builder = new StringBuilder();
		int tableIndex = -1;
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isDigit(c)) {
				builder.append(c);
			} else if (c == '_') {
				tableIndex = Integer.valueOf(builder.toString());
				builder.setLength(0);
			} else {
				return null;
			}
		}
		int colIndex = Integer.valueOf(builder.toString());

		Table<?> table = mTables.getTable(tableIndex);
		if (table != null) {
			List<Column> columns = table.getColumns();
			if (colIndex >= 0 && colIndex < columns.size()) {
				return columns.get(colIndex);
			}
		}

		return null;
	}

}
