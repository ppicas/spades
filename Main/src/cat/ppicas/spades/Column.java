/**
 * Copyright (C) 2013 Pau Picas Sans <pau.picas@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cat.ppicas.spades;

import cat.ppicas.spades.map.MappedField;

public class Column {

	public static class ColumnId extends Column {
		protected ColumnId(int index, Table table, String name) {
			super(index, table, name, "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL", true, null);
		}
	}

	public final int index;
	public final Table table;
	public final String name;
	public final boolean notNull;
	public final MappedField mappedField;

	private String mDefinition;
	private boolean mIndexed;
	private boolean mIndexIsUnique;
	private boolean mIndexIsAscendant;

	protected Column(int index, Table table, String name, String definition, boolean notNull,
			MappedField mappedField) {
		this.index = index;
		this.table = table;
		this.name = name;
		this.notNull = notNull;
		this.mappedField = mappedField;
		mDefinition = definition;
	}

	public String getDefinition() {
		return name + " " + mDefinition;
	}

	@Override
	public String toString() {
		return name;
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

	protected void setIndexed(boolean unique, boolean ascendant) {
		mIndexed = true;
		mIndexIsUnique = unique;
		mIndexIsAscendant = ascendant;
	}

}
