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
	private ArrayList<Column> mColumns = new ArrayList<Column>();
	private ColumnId mColumnId;

	public Table(String name, Class<T> cls) {
		index = Tables.getInstance().addTable(this);
		mName = name;
		mEntity = cls;
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

	public ColumnId getColumnId() {
		return mColumnId;
	}

	public ColumnId columnId() {
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

	public void updateTables(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		createTables(db);
	}

	public int nextColumnIndex() {
		return mColumns.size();
	}

	public void addColumn(Column column) {
		mColumns.add(column);
	}

}
