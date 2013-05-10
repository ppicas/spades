package cat.ppicas.spades;

import java.lang.reflect.Field;
import java.util.Date;

import android.text.TextUtils;

import cat.ppicas.spades.map.BooleanMapper;
import cat.ppicas.spades.map.DateMapper;
import cat.ppicas.spades.map.DoubleMapper;
import cat.ppicas.spades.map.IntegerMapper;
import cat.ppicas.spades.map.LongMapper;
import cat.ppicas.spades.map.RelatedMapper;
import cat.ppicas.spades.map.StringMapper;
import cat.ppicas.spades.map.ValueMapper;

public class ColumnBuilder {

	private Table<?> mTable;
	private String mName;
	private String mDefinition;
	private boolean mNotNull;
	private ValueMapper mValueMapper;

	public ColumnBuilder(Table<?> table) {
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
			Field field = mTable.getEntity().getDeclaredField(fieldName);
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
	}

	public ColumnBuilder notNull() {
		mDefinition += " NOT NULL";
		mNotNull = true;
		return this;
	}

	public ColumnBuilder notNull(String defaultVal) {
		mDefinition += " NOT NULL";
		mNotNull = true;
		return defaultVal(defaultVal);
	}

	public ColumnBuilder defaultVal(String val) {
		mDefinition += " DEFAULT " + val;
		return this;
	}

	public ColumnBuilder constraints(String... constraints) {
		mDefinition += " " + TextUtils.join(" ", constraints);
		return this;
	}

	public Column end() {
		int index = mTable.nextColumnIndex();
		Column column = new Column(mTable, index, mName, mDefinition, mNotNull, mValueMapper);
		mTable.addColumn(column);
		return column;
	}

}
