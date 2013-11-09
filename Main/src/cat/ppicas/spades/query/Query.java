/**
 * Copyright (C) 2013 Pau Picas Sans <pau.picas@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package cat.ppicas.spades.query;

import static android.text.TextUtils.join;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.CursorInfo;
import cat.ppicas.spades.CursorInfoBuilder;
import cat.ppicas.spades.SqlHelper;
import cat.ppicas.spades.Table;

public class Query {

	private NameMapper mMapper = new NameMapper();
	private ColumnSelector mSelector = new ColumnSelector();
	private ArrayList<String> mFromClauses = new ArrayList<String>();
	private ArrayList<String> mWhereClauses = new ArrayList<String>();
	private ArrayList<String> mWhereArgs = new ArrayList<String>();
	private ArrayList<String> mOrderBy = new ArrayList<String>();
	private ArrayList<String> mGroupByClauses = new ArrayList<String>();
	private String mLimit;
	private boolean mDistinct;
	private boolean mMagicColumns = false;

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
		mFromClauses.add(table.name + " AS " + mMapper.alias(table));
		return this;
	}

	public Query leftJoin(Table table, String onExpr, Column... onExprCols) {
		customJoin("LEFT", table, onExpr, onExprCols);
		return this;
	}

	public Query innerJoin(Table table, String onExpr, Column... onExprCols) {
		customJoin("INNER", table, onExpr, onExprCols);
		return this;
	}

	public Query where(Column col, String expr) {
		mWhereClauses.add(SqlHelper.expr("%s " + expr, col));
		return this;
	}

	public Query where(String expr, Column... cols) {
		mWhereClauses.add(SqlHelper.expr(expr, cols));
		return this;
	}

	public Query params(String... params) {
		mWhereArgs.addAll(Arrays.asList(params));
		return this;
	}

	public Query groupBy(Column... cols) {
		for (Column col : cols) {
			mGroupByClauses.add(mMapper.ref(col));
		}
		return this;
	}

	public Query groupBy(String expr, Column... cols) {
		mGroupByClauses.add(SqlHelper.expr(expr, cols));
		return this;
	}

	public Query orderBy(Column col, boolean asc) {
		mOrderBy.add(mMapper.ref(col) + " " + (asc ? "ASC" : "DESC"));
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

	public CursorInfo getCursorInfo() {
		CursorInfoBuilder builder = new CursorInfoBuilder();
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
		String groupBy = mGroupByClauses.isEmpty() ? null : join(",", mGroupByClauses);
		String orderBy = mOrderBy.isEmpty() ? null : join(",", mOrderBy);

		return db.query(mDistinct, tables, columns, selection, selectionArgs, groupBy, null, orderBy, mLimit);
	}

	public int count(SQLiteDatabase db) {
		checkFromClause();

		String tables= join(" ", mFromClauses);
		String[] columns = prepareColumnsStrings(true);
		String selection = mWhereClauses.isEmpty() ? null : join(" AND ", mWhereClauses);
		String[] selectionArgs = mWhereArgs.isEmpty() ? null
				: (String[]) mWhereArgs.toArray(new String[mWhereArgs.size()]);
		String groupBy = mGroupByClauses.isEmpty() ? null : join(",", mGroupByClauses);

		int res = 0;
		Cursor cursor = db.query(tables, columns, selection, selectionArgs, groupBy, null, null);
		if (cursor.moveToFirst()) {
			res = cursor.getInt(0);
		}
		cursor.close();

		return res;
	}

	protected void customJoin(String type, Table table, String onExpr, Column... onExprCols) {
		checkFromClause();
		mSelector.addTable(table);
		mFromClauses.add(type + " JOIN " + table.name + " AS " + mMapper.alias(table) + " ON "
				+ SqlHelper.expr(onExpr, onExprCols));
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
