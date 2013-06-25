package cat.ppicas.spades;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cat.ppicas.spades.Column.ColumnId;
import cat.ppicas.spades.ColumnBuilder.Type;

public class TableBuilder<T extends Entity> {

	private String mTableName;
	private Class<T> mEntityClass;

	private List<Column> mColumns = new ArrayList<Column>();
	private ColumnId mColumnId;
	private List<RelatedInverse> mRelatedInverses = new ArrayList<RelatedInverse>();

	public TableBuilder(String tableName, Class<T> entityClass) {
		mTableName = tableName;
		mEntityClass = entityClass;
	}

	public ColumnId columnId() {
		if (mColumnId != null) {
			throw new IllegalStateException("ColumnId is already defined");
		}
		mColumnId = new ColumnId(mColumns.size());
		mColumns.add(mColumnId);

		return mColumnId;
	}

	public ColumnBuilder columnText(String columnName) {
		return new ColumnBuilder(columnName, Type.TEXT, this);
	}

	public ColumnBuilder columnNumeric(String columnName) {
		return new ColumnBuilder(columnName, Type.NUMERIC, this);
	}

	public ColumnBuilder columnInteger(String columnName) {
		return new ColumnBuilder(columnName, Type.INTEGER, this);
	}

	public ColumnBuilder columnReal(String columnName) {
		return new ColumnBuilder(columnName, Type.REAL, this);
	}

	public ColumnBuilder columnAuto(String columnName) {
		return new ColumnBuilder(columnName, Type.TEXT, this);
	}

	public ColumnBuilder columnCustom(String columnName, String definition) {
		return new ColumnBuilder(columnName, definition, this);
	}

	public void relatedInverse(String relatedFieldName, String keyValueFieldName) {
		try {
			Field relatedField = ReflectionUtils.getField(mEntityClass, relatedFieldName);
			Field keyValueField = ReflectionUtils.getField(mEntityClass, keyValueFieldName);
			mRelatedInverses.add(new RelatedInverse(relatedField, keyValueField));
		} catch (NoSuchFieldException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public Table<T> build() {
		// TODO Validation.

		Tables tables = Tables.getInstance();

		Table<T> table = new Table<T>(tables.nextTableIndex(), mTableName, mEntityClass, mColumns,
				mColumnId, mRelatedInverses);

		tables.addTable(table);
		for (Column column : mColumns) {
			column.setTable(table);
		}

		return table;
	}

	protected int nextColumnIndex() {
		return mColumns.size();
	}

	protected void addColumn(Column column) {
		mColumns.add(column);
	}

}
