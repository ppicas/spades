package cat.ppicas.spades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column.ColumnId;

public class Table {

	public final int index;

	private String mName;
	private Class<? extends Entity> mEntity;
	private List<Column> mColumns = new ArrayList<Column>();
	private Map<String, Column> mColumnsNameMap = new HashMap<String, Column>();
	private ColumnId mColumnId;

	protected Table(int index, String name, Class<? extends Entity> entityClass) {
		this.index = index;
		mName = name;
		mEntity = entityClass;
	}

	public String getName() {
		return mName;
	}

	public Class<? extends Entity> getEntity() {
		return mEntity;
	}

	public Column getColumn(String name) {
		if (!mColumnsNameMap.containsKey(name)) {
			throw new NoSuchElementException("The column '" + name + "' is not defined");
		}

		return mColumnsNameMap.get(name);
	}

	public Column getColumn(int index) {
		if (index >= 0 && index < mColumns.size()) {
			return mColumns.get(index);
		} else {
			return null;
		}
	}

	public List<Column> getColumns() {
		return Collections.unmodifiableList(mColumns);
	}

	public int getColumnsNumber() {
		return mColumns.size();
	}

	public ColumnId getColumnId() {
		return mColumnId;
	}

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

	protected void addColumn(Column column) {
		mColumns.add(column);
		mColumnsNameMap.put(column.name, column);
	}

	protected int nextColumnIndex() {
		return mColumns.size();
	}

	protected void setColumnId(ColumnId columnId) {
		mColumnId = columnId;
	}

}
