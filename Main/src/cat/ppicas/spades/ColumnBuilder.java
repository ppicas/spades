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

	/*public ColumnBuilder auto(String name, String fieldName) {
		try {
			Field field = ReflectionUtils.getField(mTable.getEntity(), fieldName);;
			Class<?> type = field.getType();

			// TODO Refactor this to a MappedFieldFactory.
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
		return defaultValue(defaultVal);
	}

	public ColumnBuilder defaultValue(DefaultValue val) {
		return defaultValue(val.value());
	}

	public ColumnBuilder defaultValue(String val) {
		mDefinition += " DEFAULT " + val;
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
