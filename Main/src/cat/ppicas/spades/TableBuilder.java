package cat.ppicas.spades;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cat.ppicas.spades.ColumnBuilder.ColumnType;
import cat.ppicas.spades.map.MappedField;
import cat.ppicas.spades.map.MappedFieldFactory;
import cat.ppicas.spades.util.ReflectionUtils;
import cat.ppicas.spades.util.TextUtils;

public class TableBuilder {

	private String mTableName;
	private Class<? extends Entity> mEntityClass;

	private List<ColumnBuilder> mColumnBuilders = new ArrayList<ColumnBuilder>();
	private String mColumnIdName;
	private List<RelatedInverse> mRelatedInverses = new ArrayList<RelatedInverse>();

	public TableBuilder(String tableName, Class<? extends Entity> entityClass) {
		mTableName = tableName;
		mEntityClass = entityClass;
	}

	public TableBuilder columnId(String columnName) {
		if (mColumnIdName != null) {
			throw new IllegalStateException("ColumnId is already defined");
		}
		mColumnIdName = columnName;

		return this;
	}

	public ColumnBuilder columnText(String columnName) {
		return new ColumnBuilder(columnName, ColumnType.TEXT, this);
	}

	public ColumnBuilder columnNumeric(String columnName) {
		return new ColumnBuilder(columnName, ColumnType.NUMERIC, this);
	}

	public ColumnBuilder columnInteger(String columnName) {
		return new ColumnBuilder(columnName, ColumnType.INTEGER, this);
	}

	public ColumnBuilder columnReal(String columnName) {
		return new ColumnBuilder(columnName, ColumnType.REAL, this);
	}

	public ColumnBuilder columnAuto(String columnName) {
		String[] words = TextUtils.splitIdentifierWords(columnName);
		Set<String> fieldNames = TextUtils.generateFieldNames(words);
		Field field = ReflectionUtils.findField(mEntityClass, fieldNames);
		if (field == null) {
			throw new IllegalArgumentException(new NoSuchFieldException());
		}

		MappedFieldFactory factory = MappedFieldFactory.getInstance();
		MappedField mappedField = factory.createForField(field);

		return new ColumnBuilder(columnName, mappedField, this);
	}

	public ColumnBuilder columnAuto(String columnName, String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	public ColumnBuilder columnCustom(String columnName, String definition) {
		return new ColumnBuilder(columnName, definition, this);
	}

	public TableBuilder relatedInverse(String relatedFieldName, String keyValueFieldName) {
		try {
			Field relatedField = ReflectionUtils.getField(mEntityClass, relatedFieldName);
			Field keyValueField = ReflectionUtils.getField(mEntityClass, keyValueFieldName);
			mRelatedInverses.add(new RelatedInverse(relatedField, keyValueField));
		} catch (NoSuchFieldException e) {
			throw new IllegalArgumentException(e);
		}
		return this;
	}

	public Table build() {
		// TODO Validation.

		Tables tables = Tables.getInstance();

		Table table = new Table(tables.nextTableIndex(), mTableName, mEntityClass);
		tables.addTable(table);

		for (ColumnBuilder columnBuilder : mColumnBuilders) {
			table.addColumn(columnBuilder.build(table.nextColumnIndex(), table));
		}

		return table;
	}

	protected void addColumnBuilder(ColumnBuilder columnBuilder) {
		mColumnBuilders.add(columnBuilder);
	}

}
