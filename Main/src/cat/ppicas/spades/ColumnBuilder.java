package cat.ppicas.spades;

import android.text.TextUtils;
import cat.ppicas.spades.map.ValueMapper;

public class ColumnBuilder {

	public enum Type {
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

	public enum DefaultValue {
		EMTPY("''"),
		ZERO("0"),
		ONE("1"),
		FALSE("0"),
		TRUE("1");

		private String mValue;

		private DefaultValue(String value) {
			mValue = value;
		}

		public String value() {
			return mValue;
		}
	}

	/*public static final String DEFAULT_EMTPY = "''";
	public static final String DEFAULT_ZERO = "0";
	public static final String DEFAULT_ONE = "1";
	public static final String DEFAULT_FALSE = "0";
	public static final String DEFAULT_TRUE = "1";*/

	// private Table<?> mTable;
	private String mName;
	private String mDefinition;
	private boolean mNotNull;
	private ValueMapper mValueMapper;

	private TableBuilder<?> mTableBuilder;

	protected ColumnBuilder(String columnName, Type columnType, TableBuilder<?> builder) {
		this(columnName, columnType.name(), builder);
	}

	protected ColumnBuilder(String columnName, String definition, TableBuilder<?> builder) {
		mName = columnName;
		mDefinition = definition;
		mTableBuilder = builder;
	}

	/*public ColumnBuilder(Table<?> table) {
		mTable = table;
	}

	public ColumnBuilder custom(String name, String definition) {
		mName = name;
		mDefinition = definition;
		return this;
	}

	public ColumnBuilder text(String name) {
		return custom(name, "TEXT");
	}

	public ColumnBuilder numeric(String name) {
		return custom(name, "NUMERIC");
	}

	public ColumnBuilder integer(String name) {
		return custom(name, "INTEGER");
	}

	public ColumnBuilder real(String name) {
		return custom(name, "REAL");
	}

	public ColumnBuilder auto(String fieldName) {
		// TODO Transform fieldName to name.
		return auto(fieldName, fieldName);
	}

	public ColumnBuilder auto(String name, String fieldName) {
		try {
			Field field = ReflectionUtils.getField(mTable.getEntity(), fieldName);;
			Class<?> type = field.getType();

			// TODO Refactor this to a ValueMapperFactory.
			if (type == Integer.TYPE || type == Integer.class) {
				integer(name);
				mValueMapper = new IntegerMapper(field);
			} else if (type == Long.TYPE || type == Long.class) {
				integer(name);
				mValueMapper = new LongMapper(field);
			} else if (type == Double.TYPE || type == Double.class) {
				real(name);
				mValueMapper = new DoubleMapper(field);
			} else if (type == Boolean.TYPE || type == Boolean.class) {
				integer(name);
				mValueMapper = new BooleanMapper(field);
			} else if (type == String.class) {
				text(name);
				mValueMapper = new StringMapper(field);
			} else if (type == Date.class) {
				integer(name);
				mValueMapper = new DateMapper(field);
			} else if (type == Related.class) {
				integer(name);
				mValueMapper = new RelatedMapper(field);
			} else {
				throw new IllegalArgumentException("Field type not supported");
			}

			return this;
		} catch (NoSuchFieldException e) {
			throw new IllegalArgumentException(e);
		}
	}*/

	public ColumnBuilder notNull() {
		mDefinition += " NOT NULL";
		mNotNull = true;
		return this;
	}

	public ColumnBuilder notNull(DefaultValue defaultVal) {
		return notNull(defaultVal.value());
	}

	public ColumnBuilder notNull(String defaultVal) {
		notNull();
		return defaultVal(defaultVal);
	}

	public ColumnBuilder defaultVal(DefaultValue val) {
		return defaultVal(val.value());
	}

	public ColumnBuilder defaultVal(String val) {
		mDefinition += " DEFAULT " + val;
		return this;
	}

	public ColumnBuilder foreignKey(Column column) {
		return foreignKey(column, OnDelete.CASCADE);
	}

	public ColumnBuilder foreignKey(Column column, OnDelete onDelete) {
		mDefinition += " REFERENCES " + column.getTable().getName() + "(" + column.name + ") "
				+ "ON DELETE " + onDelete.name().replace('_', ' ');
		return this;
	}

	public ColumnBuilder constraints(String... constraints) {
		mDefinition += " " + TextUtils.join(" ", constraints);
		return this;
	}

	public Column end() {
		int index = mTableBuilder.nextColumnIndex();
		Column column = new Column(index, mName, mDefinition, mNotNull, mValueMapper);
		mTableBuilder.addColumn(column);

		return column;
	}

	/*public Column end() {
		int index = mTable.nextColumnIndex();
		Column column = new Column(mTable, index, mName, mDefinition, mNotNull, mValueMapper);
		mTable.addColumn(column);
		return column;
	}*/

}
