package cat.ppicas.spades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column.ColumnId;

public class Table<T extends Entity> {

	public final int index;

	private String mName;
	private Class<T> mEntity;
	private List<Column> mColumns = new ArrayList<Column>();
	private ColumnId mColumnId;
	private List<RelatedInverse> mRelatedInverses = new ArrayList<RelatedInverse>();

	/*public Table(String name, Class<T> cls) {
		index = Tables.getInstance().addTable(this);
		mName = name;
		mEntity = cls;
	}*/

	protected Table(int index, String name, Class<T> entityClass, List<Column> columns,
			ColumnId columnId, List<RelatedInverse> relatedInverses) {
		this.index = index;
		mName = name;
		mEntity = entityClass;
		mColumns.addAll(columns);
		mColumnId = columnId;
		mRelatedInverses.addAll(relatedInverses);
	}

	public String getName() {
		return mName;
	}

	public Class<T> getEntity() {
		return mEntity;
	}

	public List<Column> getColumns() {
		return Collections.unmodifiableList(mColumns);
	}

	public int getColumnsNumber() {
		return mColumns.size();
	}

	public List<RelatedInverse> getRelatedInverses() {
		return Collections.unmodifiableList(mRelatedInverses);
	}

	public ColumnId getColumnId() {
		return mColumnId;
	}

	/*public ColumnId columnId() {
		if (mColumnId != null) {
			throw new IllegalStateException("ColumnId is already defined");
		}
		mColumnId = new ColumnId(this, mColumns.size());
		mColumns.add(mColumnId);
		return mColumnId;
	}

	public ColumnBuilder column() {
		return new ColumnBuilder(this);
	}

	public void relatedInverse(String relatedFieldName, String keyValueFieldName) {
		try {
			Field relatedField = ReflectionUtils.getField(mEntity, relatedFieldName);
			Field keyValueField = ReflectionUtils.getField(mEntity, keyValueFieldName);
			mRelatedInverses.add(new RelatedInverse(relatedField, keyValueField));
		} catch (NoSuchFieldException e) {
			throw new IllegalArgumentException(e);
		}
	}*/

	public void createTables(SQLiteDatabase db) {
		String[] definitions = new String[mColumns.size()];
		int i = 0;
		for (Column column : mColumns) {
			definitions[i++] = column.getDefinition();
		}
		db.execSQL(SqlHelper.genCreateTable(getName(), definitions));
	}

	public void dropTables(SQLiteDatabase db) {
		db.execSQL(SqlHelper.genDropTable(getName()));
	}

	public void upgradeTables(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		createTables(db);
	}

	/*int nextColumnIndex() {
		return mColumns.size();
	}

	void addColumn(Column column) {
		mColumns.add(column);
	}*/

}
