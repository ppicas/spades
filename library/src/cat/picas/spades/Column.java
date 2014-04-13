/**
 * Copyright (C) 2013 Pau Picas Sans <pau.picas@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cat.picas.spades;

import cat.picas.spades.map.MappedField;

public class Column {

	public static class ColumnId extends Column {
		protected ColumnId(int index, String name, Table table) {
			super(index, name, table, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL", true);
		}

		@Override
		protected ColumnId alias(Table table) {
			return new ColumnId(index, name, table);
		}
	}

	public final int index;
	public final String name;

	private final Table mTable;
	private final String mDefinition;
	private final boolean mNotNull;

	private MappedField mMappedField;
	private boolean mIndexed;
	private boolean mIndexIsUnique;
	private boolean mIndexIsAscendant;

	protected Column(int index, String name, Table table, String definition, boolean notNull) {
		this.index = index;
		this.name = name;
		mTable = table;
		mDefinition = definition;
		mNotNull = notNull;
	}

	public Table getTable() {
		return mTable;
	}

	public String getDefinition() {
		return name + " " + mDefinition;
	}

	public boolean isNotNull() {
		return mNotNull;
	}

	public MappedField getMappedField() {
		return mMappedField;
	}

	public boolean isIndexed() {
		return mIndexed;
	}

	public boolean indexIsUnique() {
		return mIndexIsUnique;
	}

	public boolean indexIsAscendant() {
		return mIndexIsAscendant;
	}

	protected void setMappedField(MappedField mappedField) {
		mMappedField = mappedField;
	}

	protected void setIndexed(boolean unique, boolean ascendant) {
		mIndexed = true;
		mIndexIsUnique = unique;
		mIndexIsAscendant = ascendant;
	}

	protected Column alias(Table table) {
		Column column = new Column(index, name, table, mDefinition, mNotNull);
		column.mMappedField = mMappedField;
		column.mIndexed = mIndexed;
		column.mIndexIsUnique = mIndexIsUnique;
		column.mIndexIsAscendant = mIndexIsAscendant;

		return column;
	}

}
