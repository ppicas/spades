package cat.ppicas.spades;

import java.util.ArrayList;
import java.util.Collection;
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
	private Map<String, Column> mColumns = new HashMap<String, Column>();
	private ColumnId mColumnId;
	private List<RelatedInverse> mRelatedInverses = new ArrayList<RelatedInverse>();

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
		if (!mColumns.containsKey(name)) {
			throw new NoSuchElementException("The column '" + name + "' is not defined");
		}

		return mColumns.get(name);
	}

	public List<Column> getColumns() {
		Collection<Column> values = mColumns.values();
		return Collections.unmodifiableList(new ArrayList<Column>(values));
	}

	public int getColumnsNumber() {
		return mColumns.size();
	}

	public ColumnId getColumnId() {
		return mColumnId;
	}

	public List<RelatedInverse> getRelatedInverses() {
		return Collections.unmodifiableList(mRelatedInverses);
	}

	public void createTables(SQLiteDatabase db) {
		String[] definitions = new String[mColumns.size()];
		int i = 0;
		for (Column column : mColumns.values()) {
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
		mColumns.put(column.name, column);
	}

	protected int nextColumnIndex() {
		return mColumns.size();
	}

	protected void setColumnId(ColumnId columnId) {
		mColumnId = columnId;
	}

	protected void addRelatedInverse(RelatedInverse relatedInverse) {
		mRelatedInverses.add(relatedInverse);
	}

}
