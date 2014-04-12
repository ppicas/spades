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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cat.picas.spades.Column.ColumnId;
import cat.picas.spades.ColumnBuilder.ColumnType;
import cat.picas.spades.map.MappedField;
import cat.picas.spades.map.MappedFieldFactory;
import cat.picas.spades.util.ReflectionUtils;
import cat.picas.spades.util.TextUtils;

public class TableBuilder {

	private String mTableName;
	private Class<? extends Entity> mEntityClass;

	private List<ColumnBuilder> mColumnBuilders = new ArrayList<ColumnBuilder>();
	private String mColumnIdName;

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
		return columnAuto(columnName, field);
	}

	public ColumnBuilder columnAuto(String columnName, String fieldName) {
		Field field = ReflectionUtils.findField(mEntityClass, fieldName);
		return columnAuto(columnName, field);
	}

	public ColumnBuilder columnCustom(String columnName, String definition) {
		return new ColumnBuilder(columnName, definition, this);
	}

	public Table build() {
		if (mColumnIdName == null || mColumnIdName.isEmpty()) {
			throw new IllegalStateException("You must define a ColumnId");
		}

		Tables tables = Tables.getInstance();
		Table table = new Table(tables.nextTableIndex(), mTableName, mEntityClass);
		tables.addTable(table);

		ColumnId columnId = new ColumnId(table.nextColumnIndex(), mColumnIdName, table);
		table.addColumn(columnId);
		table.setColumnId(columnId);

		for (ColumnBuilder columnBuilder : mColumnBuilders) {
			table.addColumn(columnBuilder.build(table.nextColumnIndex(), table));
		}

		return table;
	}

	protected void addColumnBuilder(ColumnBuilder columnBuilder) {
		mColumnBuilders.add(columnBuilder);
	}

	protected String getTableName() {
		return mTableName;
	}

	protected boolean hasColumn(String columnName) {
		if (columnName.equals(mColumnIdName)) {
			return true;
		}

		for (ColumnBuilder columnBuilder : mColumnBuilders) {
			if (columnName.equals(columnBuilder.getName())) {
				return true;
			}
		}

		return false;
	}

	private ColumnBuilder columnAuto(String columnName, Field field) {
		if (field == null) {
			throw new IllegalArgumentException(new NoSuchFieldException());
		}

		MappedFieldFactory factory = MappedFieldFactory.getInstance();
		MappedField mappedField = factory.createForField(field);

		return new ColumnBuilder(columnName, mappedField, this);
	}
}
