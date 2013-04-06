package cat.ppicas.spades;

import static android.text.TextUtils.join;

import java.util.ArrayList;
import java.util.Arrays;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column.ColumnId;

public class Query {

	protected static NameMapper sMapper = new NameMapper();

	protected ArrayList<Table<?>> mSelectedTables = new ArrayList<Table<?>>();
	protected Table<?> mFromTable;
	protected ArrayList<String> mFromClauses = new ArrayList<String>();
	protected boolean mMagicColumns = false;
	protected boolean mAutoIds = true;
	protected boolean mAutoRowsId = true;
	protected boolean mAutoColumns = true;
	protected ArrayList<Column> mSelectedColumns = new ArrayList<Column>();
	protected ArrayList<String> mCustomColumns = new ArrayList<String>();
	protected ArrayList<String> mWhereClauses = new ArrayList<String>();
	protected ArrayList<String> mWhereArgs = new ArrayList<String>();
	protected ArrayList<String> mOrderBy = new ArrayList<String>();
	protected String mLimit;

	public static int[][] getCursorMappings(Cursor cursor) {
		MappingsBuilder builder = new MappingsBuilder();
		for (String name : cursor.getColumnNames()) {
			Column column = sMapper.parseColumnName(name);
			if (column != null) {
				builder.add(column);
			} else {
				builder.addOffset();
			}
		}

		return builder.build();
	}

	public static NameMapper getMapper() {
		return sMapper;
	}

	public static String expr(Column col, String exp) {
		return String.format("%s " + exp, new Object[] { sMapper.ref(col) });
	}

	public static String expr(String expr, Column... cols) {
		return String.format(expr, (Object[]) sMapper.refs(cols));
	}

	public static String and(Iterable<String> exprs) {
		return "(" + join(" AND ", exprs) + ")";
	}

	public static String and(String... exprs) {
		return "(" + join(" AND ", exprs) + ")";
	}

	public static String or(Iterable<String> exprs) {
		return "(" + join(" OR ", exprs) + ")";
	}

	public static String or(String... exprs) {
		return "(" + join(" OR ", exprs) + ")";
	}

	public Query(Table<?> fromTable) {
		from(fromTable);
	}

	public Query enableMagicColumns() {
		mMagicColumns = true;
		return this;
	}

	public Query disableAutoIds() {
		mAutoIds = false;
		return this;
	}

	public Query disableAutoRowsId() {
		mAutoRowsId = false;
		return this;
	}

	public Query select(Column... cols) {
		if (mAutoColumns) {
			mSelectedColumns.clear();
			mAutoColumns = false;
		}
		mSelectedColumns.addAll(Arrays.asList(cols));
		return this;
	}

	public Query select(String expr, Column... cols) {
		mCustomColumns.add(expr(expr, cols));
		return this;
	}

	public Query from(Table<?> table) {
		addSelectedTable(table);
		if (mFromTable != null) {
			throw new IllegalStateException("From clause already defined");
		}
		mFromTable = table;

		mFromClauses.add(table.getName() + " AS " + sMapper.alias(table));
		if (mAutoColumns) {
			mSelectedColumns.addAll(table.getColumns());
		}

		return this;
	}

	public Query leftJoin(Table<?> table, String onrExp, Column... onExprCols) {
		customJoin("LEFT", table, onrExp, onExprCols);
		return this;
	}

	public Query innerJoin(Table<?> table, String onExpr, Column... onExprCols) {
		customJoin("INNER", table, onExpr, onExprCols);
		return this;
	}

	public Query where(Column col, String expr) {
		mWhereClauses.add(expr("%s " + expr, col));
		return this;
	}

	public Query where(String expr, Column... cols) {
		mWhereClauses.add(expr(expr, cols));
		return this;
	}

	public Query params(String... params) {
		mWhereArgs.addAll(Arrays.asList(params));
		return this;
	}

	public Query orderBy(Column col, boolean asc) {
		mOrderBy.add(sMapper.ref(col) + " " + (asc ? "ASC" : "DESC"));
		return this;
	}

	public Query limit(int limit) {
		return limit(limit, 0);
	}

	public Query limit(int limit, int offset) {
		if (limit == 0) {
			mLimit = null;
		} else {
			mLimit = (offset > 0 ? limit + " OFFSET " + offset : String.valueOf(limit));
		}
		return this;
	}

	public boolean hasTable(Table<?> table) {
		return mSelectedTables.contains(table);
	}

	public int[][] getMappings() {
		// We need to offset the position the size of custom columns plus one because
		// first column is reserved for '_id'.
		return new MappingsBuilder()
				.addOffset((mAutoRowsId ? 1 : 0) + mCustomColumns.size())
				.add(getAutoColumnIdList())
				.add(mSelectedColumns)
				.build();
	}

	public void setMagicColumns(boolean state) {
		mMagicColumns = state;
	}

	public void setAutoIds(boolean state) {
		mAutoIds = state;
	}

	public void setAutoRowsId(boolean state) {
		mAutoRowsId = state;
	}

	public Cursor execute(SQLiteDatabase db) {
		checkFromClause();

		String tables= join(" ", mFromClauses);
		String[] columns = prepareColumnStrings(false);
		String selection = mWhereClauses.isEmpty() ? null : join(" AND ", mWhereClauses);
		String[] selectionArgs = mWhereArgs.isEmpty() ? null
				: (String[]) mWhereArgs.toArray(new String[mWhereArgs.size()]);
		String orderBy = mOrderBy.isEmpty() ? null : join(",", mOrderBy);

		return db.query(tables, columns, selection, selectionArgs, null, null, orderBy, mLimit);
	}

	public int count(SQLiteDatabase db) {
		checkFromClause();

		String tables= join(" ", mFromClauses);
		String[] columns = prepareColumnStrings(true);
		String selection = mWhereClauses.isEmpty() ? null : join(" AND ", mWhereClauses);
		String[] selectionArgs = mWhereArgs.isEmpty() ? null
				: (String[]) mWhereArgs.toArray(new String[mWhereArgs.size()]);

		int res = 0;
		Cursor cursor = db.query(tables, columns, selection, selectionArgs, null, null, null);
		if (cursor.moveToFirst()) {
			res = cursor.getInt(0);
		}
		cursor.close();

		return res;
	}

	protected void customJoin(String type, Table<?> table, String onExpr, Column... onExprCols) {
		addSelectedTable(table);
		checkFromClause();

		mFromClauses.add(type + " JOIN " + table.getName() + " AS " + sMapper.alias(table) + " ON "
				+ expr(onExpr, onExprCols));
		if (mAutoColumns) {
			mSelectedColumns.addAll(table.getColumns());
		}
	}

	protected void addSelectedTable(Table<?> table) {
		if (mSelectedTables.contains(table)) {
			throw new IllegalStateException("Table already selected");
		}
		mSelectedTables.add(table);
	}

	protected void checkFromClause() {
		if (mFromTable == null) {
			throw new IllegalStateException("Missing from clause");
		}
	}

	protected String[] prepareColumnStrings(boolean forCount) {
		String[] columns;

		if (forCount) {
			columns = new String[1 + mCustomColumns.size()];
			columns[0] = "COUNT(*)";

			for (int i = 0; i < mCustomColumns.size(); i++) {
				columns[i + 1] = mCustomColumns.get(i);
			}
		} else {
			ArrayList<ColumnId> autoColumnIdList = getAutoColumnIdList();

			int rowsIdSize = (mAutoRowsId ? 1 : 0);
			columns = new String[rowsIdSize + mCustomColumns.size() + autoColumnIdList.size()
					+ mSelectedColumns.size()];
			if (mAutoRowsId) {
				columns[0] = sMapper.ref(mFromTable.getColumnId()) + " AS _id";
			}

			int i = rowsIdSize;
			for (String column : mCustomColumns) {
				columns[i++] = column;
			}
			for (ColumnId column : autoColumnIdList) {
				if (mMagicColumns) {
					columns[i++] = sMapper.ref(column) + " AS " + sMapper.alias(column);
				} else {
					columns[i++] = sMapper.ref(column);
				}
			}
			for (Column column : mSelectedColumns) {
				if (mMagicColumns) {
					columns[i++] = sMapper.ref(column) + " AS " + sMapper.alias(column);
				} else {
					columns[i++] = sMapper.ref(column);
				}
			}
		}

		return columns;
	}

	protected ArrayList<ColumnId> getAutoColumnIdList() {
		ArrayList<ColumnId> list = new ArrayList<ColumnId>();

		if (mAutoIds) {
			for (Table<?> table : mSelectedTables) {
				ColumnId colId = table.getColumnId();
				if (table == mFromTable && mAutoRowsId && !mMagicColumns) {
					// Skip because this column will be redundant with the first auto
					// row id column.
					continue;
				}
				if (!mSelectedColumns.contains(colId)) {
					list.add(colId);
				}
			}
		}

		return list;
	}

}
