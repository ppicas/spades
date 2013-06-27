package cat.ppicas.spades.query;

import static android.text.TextUtils.join;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.MappingsBuilder;
import cat.ppicas.spades.Table;

public class Query {

	protected static NameMapper sMapper = new NameMapper();

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

	protected ColumnSelector mSelector = new ColumnSelector();

	protected ArrayList<String> mFromClauses = new ArrayList<String>();
	protected ArrayList<String> mWhereClauses = new ArrayList<String>();
	protected ArrayList<String> mWhereArgs = new ArrayList<String>();
	protected ArrayList<String> mOrderBy = new ArrayList<String>();
	protected String mLimit;
	private boolean mDistinct;
	protected boolean mMagicColumns = false;

	public Query(Table fromTable) {
		mSelector.setAutoEntitiesId(true);
		mSelector.setAutoRowsId(true);
		from(fromTable);
	}

	public Query select(Table table) {
		for (Column column : table.getColumns()) {
			mSelector.selectColumn(column);
		}
		return this;
	}

	public Query select(Column... cols) {
		for (Column column : cols) {
			mSelector.selectColumn(column);
		}
		return this;
	}

	public Query select(String expr, Column... cols) {
		mSelector.selectColumn(expr, cols);
		return this;
	}

	public Query from(Table table) {
		if (mSelector.hasTables()) {
			throw new IllegalStateException("From clause already defined");
		}
		mSelector.addTable(table);
		mFromClauses.add(table.getName() + " AS " + sMapper.alias(table));
		return this;
	}

	public Query leftJoin(Table table, String onrExp, Column... onExprCols) {
		customJoin("LEFT", table, onrExp, onExprCols);
		return this;
	}

	public Query innerJoin(Table table, String onExpr, Column... onExprCols) {
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

	public Query distinct() {
		mDistinct = true;
		return this;
	}

	public Query enableMagicColumns() {
		mMagicColumns = true;
		return this;
	}

	public Query setDistinct(boolean state) {
		mDistinct = state;
		return this;
	}

	public Query setMagicColumns(boolean state) {
		mMagicColumns = state;
		return this;
	}

	public Query setAutoEntitiesId(boolean state) {
		mSelector.setAutoEntitiesId(state);
		return this;
	}

	public Query setAutoRowsId(boolean state) {
		mSelector.setAutoRowsId(state);
		return this;
	}

	public boolean isDistinct() {
		return mDistinct;
	}

	public boolean hasMagicColumns() {
		return mMagicColumns;
	}

	public boolean hasAutoEntitiesId() {
		return mSelector.isAutoEntitiesId();
	}

	public boolean hasAutoRowsId() {
		return mSelector.isAutoRowsId();
	}

	public boolean hasTable(Table table) {
		return mSelector.hasTable(table);
	}

	public int[][] getMappings() {
		MappingsBuilder builder = new MappingsBuilder();
		for (SelectedColumn selected : mSelector.getSelectedColumns()) {
			if (!selected.isCustom()) {
				builder.add(selected.getColumn());
			} else {
				builder.addOffset();
			}
		}
		return builder.build();
	}

	public Cursor execute(SQLiteDatabase db) {
		checkFromClause();

		String tables= join(" ", mFromClauses);
		String[] columns = prepareColumnsStrings(false);
		String selection = mWhereClauses.isEmpty() ? null : join(" AND ", mWhereClauses);
		String[] selectionArgs = mWhereArgs.isEmpty() ? null
				: (String[]) mWhereArgs.toArray(new String[mWhereArgs.size()]);
		String orderBy = mOrderBy.isEmpty() ? null : join(",", mOrderBy);

		return db.query(mDistinct, tables, columns, selection, selectionArgs, null, null, orderBy, mLimit);
	}

	public int count(SQLiteDatabase db) {
		checkFromClause();

		String tables= join(" ", mFromClauses);
		String[] columns = prepareColumnsStrings(true);
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

	protected void customJoin(String type, Table table, String onExpr, Column... onExprCols) {
		checkFromClause();
		mSelector.addTable(table);
		mFromClauses.add(type + " JOIN " + table.getName() + " AS " + sMapper.alias(table) + " ON "
				+ expr(onExpr, onExprCols));
	}

	protected void checkFromClause() {
		if (!mSelector.hasTables()) {
			throw new IllegalStateException("Missing from clause");
		}
	}

	protected String[] prepareColumnsStrings(boolean forCount) {
		List<SelectedColumn> selectedCols;
		if (forCount) {
			selectedCols = mSelector.getSelectedColumnsForCount();
		} else {
			selectedCols = mSelector.getSelectedColumns();
		}
		int i = 0;
		String[] columns = new String[selectedCols.size()];
		for (SelectedColumn selected : selectedCols) {
			columns[i++] = selected.format(mMagicColumns);
		}
		return columns;
	}

}
