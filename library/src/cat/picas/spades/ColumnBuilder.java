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

import android.text.TextUtils;
import cat.picas.spades.map.MappedField;

public class ColumnBuilder {

	public enum ColumnType {
		TEXT,
		NUMERIC,
		INTEGER,
		REAL,
		NONE
	}

	public enum OnDelete {
		CASCADE,
		SET_NULL,
		SET_DEFAULT,
		RESTRICT
	}

	private String mName;
	private String mDefinition;
	private boolean mNotNull;
	private MappedField mMappedField;
	private TableBuilder mTableBuilder;
	private boolean mIndexed;
	private boolean mIndexIsUnique;
	private boolean mIndexIsAscendant;

	protected ColumnBuilder(String columnName, ColumnType columnType, TableBuilder builder) {
		this(columnName, columnType.name(), builder);
	}

	protected ColumnBuilder(String columnName, MappedField mappedField, TableBuilder builder) {
		this(columnName, mappedField.getColumnType(), builder);
		mMappedField = mappedField;
	}

	protected ColumnBuilder(String columnName, String definition, TableBuilder builder) {
		mName = columnName;
		mDefinition = definition;
		mTableBuilder = builder;
	}

	public ColumnBuilder notNull() {
		mDefinition += " NOT NULL";
		mNotNull = true;

		return this;
	}

	public ColumnBuilder defaultValue(String value) {
		return defaultValueExp("'" + value.replace("'", "''") + "'");
	}

	public ColumnBuilder defaultValue(Number value) {
		return defaultValueExp(value.toString());
	}

	public ColumnBuilder defaultValue(boolean value) {
		return defaultValueExp(value ? "1" : "0");
	}

	public ColumnBuilder defaultValue(Enum<?> value) {
		return defaultValueExp("'" + value.name() + "'");
	}

	public ColumnBuilder defaultValueExp(String expression) {
		mDefinition += " DEFAULT " + expression;
		return this;
	}

	public ColumnBuilder foreignKey(Column column) {
		return foreignKey(column, OnDelete.CASCADE);
	}

	public ColumnBuilder foreignKey(Column column, OnDelete onDelete) {
		mDefinition += " REFERENCES " + column.getTable().name + "(" + column.name + ") "
				+ "ON DELETE " + onDelete.name().replace('_', ' ');
		indexed(false, true);

		return this;
	}

	public ColumnBuilder indexed(boolean unique, boolean ascendant) {
		mIndexed = true;
		mIndexIsUnique = unique;
		mIndexIsAscendant = ascendant;

		return this;
	}

	public ColumnBuilder constraints(String... constraints) {
		mDefinition += " " + TextUtils.join(" ", constraints);
		return this;
	}

	public TableBuilder end() {
		mTableBuilder.addColumnBuilder(this);
		return mTableBuilder;
	}

	protected Column build(int index, Table table) {
		Column column = new Column(index, mName, table, mDefinition, mNotNull);
		if (mMappedField != null) {
			column.setMappedField(mMappedField);
		}
		if (mIndexed) {
			column.setIndexed(mIndexIsUnique, mIndexIsAscendant);
		}

		return column;
	}

}
