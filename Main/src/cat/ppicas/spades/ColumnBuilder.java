package cat.ppicas.spades;

import android.text.TextUtils;
import cat.ppicas.spades.map.MappedField;

public class ColumnBuilder {

	public enum ColumnType {
		TEXT,
		NUMERIC,
		INTEGER,
		REAL,
		NONE
	}

	public enum OnDelete {
		CASCADE,
		SET_NULL,
		SET_DEFAULT,
		RESTRICT
	}

	private String mName;
	private String mDefinition;
	private boolean mNotNull;
	private MappedField mMappedField;

	private TableBuilder mTableBuilder;

	protected ColumnBuilder(String columnName, ColumnType columnType, TableBuilder builder) {
		this(columnName, columnType.name(), builder);
	}

	protected ColumnBuilder(String columnName, MappedField mappedField, TableBuilder builder) {
		this(columnName, mappedField.getColumnType(), builder);
		mMappedField = mappedField;
	}

	protected ColumnBuilder(String columnName, String definition, TableBuilder builder) {
		mName = columnName;
		mDefinition = definition;
		mTableBuilder = builder;
	}

	public ColumnBuilder notNull() {
		mDefinition += " NOT NULL";
		mNotNull = true;
		return this;
	}

	public ColumnBuilder defaultValue(String value) {
		// TODO Strip "'" chars from text.
		return defaultValueExp("'" + value + "'");
	}

	public ColumnBuilder defaultValue(Number value) {
		return defaultValueExp(value.toString());
	}

	public ColumnBuilder defaultValue(boolean value) {
		return defaultValueExp(value ? "1" : "0");
	}

	public ColumnBuilder defaultValueExp(String expression) {
		mDefinition += " DEFAULT " + expression;
		return this;
	}

	public ColumnBuilder foreignKey(Column column) {
		return foreignKey(column, OnDelete.CASCADE);
	}

	public ColumnBuilder foreignKey(Column column, OnDelete onDelete) {
		mDefinition += " REFERENCES " + column.table.getName() + "(" + column.name + ") "
				+ "ON DELETE " + onDelete.name().replace('_', ' ');
		return this;
	}

	public ColumnBuilder constraints(String... constraints) {
		mDefinition += " " + TextUtils.join(" ", constraints);
		return this;
	}

	public TableBuilder end() {
		mTableBuilder.addColumnBuilder(this);
		return mTableBuilder;
	}

	protected Column build(int index, Table table) {
		return new Column(index, table, mName, mDefinition, mNotNull, mMappedField);
	}

}
