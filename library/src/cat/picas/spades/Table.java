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

package cat.picas.spades;

import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cat.picas.spades.Column.ColumnId;

public class Table {

	public final int index;
	public final String name;

	private final Class<? extends Entity> mEntity;
	private final List<Column> mColumns = new ArrayList<Column>();
	private final Map<String, Column> mColumnsNameMap = new HashMap<String, Column>();
	private ColumnId mColumnId;
	private Table mDefaultAlias;
	private SparseArray<Table> mAliases;

	protected Table(int index, String name, Class<? extends Entity> entityClass) {
		this.index = index;
		this.name = name;
		mEntity = entityClass;
	}

	public Class<? extends Entity> getEntity() {
		return mEntity;
	}

	public Column getColumn(String name) {
		if (!mColumnsNameMap.containsKey(name)) {
			throw new IllegalArgumentException("The column '" + name + "' is not defined");
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

	public int getColumnsSize() {
		return mColumns.size();
	}

	public ColumnId getColumnId() {
		return mColumnId;
	}

	public Table alias() {
		if (mDefaultAlias == null) {
			mDefaultAlias = newAlias();
		}
		return mDefaultAlias;
	}

	public Table alias(int aliasId) {
		if (mAliases == null) {
			mAliases = new SparseArray<Table>();
		}
		Table table = mAliases.get(aliasId);
		if (table == null) {
			table = newAlias();
		}
		return table;
	}

	public void createTable(SQLiteDatabase db) {
		String[] definitions = new String[mColumns.size()];
		int i = 0;
		for (Column column : mColumns) {
			definitions[i++] = column.getDefinition();
		}
		db.execSQL(SqlHelper.createTable(name, definitions));

		int num = 1;
		for (Column column : mColumns) {
			if (column.isIndexed()) {
				String indexName = generateIndexName(num++);
				String colDef = column.name + (column.indexIsAscendant() ? " ASC" : " DESC");
				db.execSQL(SqlHelper.createIndex(indexName, column.indexIsUnique(), name, colDef));
			}
		}
	}

	public void dropTable(SQLiteDatabase db) {
		db.execSQL(SqlHelper.dropTable(name));

		int num = 1;
		for (Column column : mColumns) {
			if (column.isIndexed()) {
				db.execSQL(SqlHelper.dropIndex(generateIndexName(num++)));
			}
		}
	}

	@Override
	public String toString() {
		return name;
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

	private String generateIndexName(int num) {
		StringBuilder indexName = new  StringBuilder(name.length() + 10 + 3);
		indexName.append(name).append("_auto_idx_").append(num);
		return indexName.toString();
	}

	private Table newAlias() {
		Tables tables = Tables.getInstance();
		Table table = new Table(tables.nextTableIndex(), name, mEntity);
		table.setColumnId(mColumnId.clone(table, mColumnId.index));
		for (Column column : mColumns) {
			table.addColumn(column.clone(table, column.index));
		}
		tables.addTable(table, false);
		return table;
	}

}
