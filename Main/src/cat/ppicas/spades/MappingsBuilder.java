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

package cat.ppicas.spades;

import java.util.Arrays;

import android.database.Cursor;
import cat.ppicas.spades.query.NameMapper;

public class MappingsBuilder {

	private int mOffset;
	private Tables mTables;
	private int[][] mMappings;

	public static int[][] getCursorMappings(Cursor cursor) {
		MappingsBuilder builder = new MappingsBuilder();
		NameMapper mapper = new NameMapper();

		for (String name : cursor.getColumnNames()) {
			Column column = mapper.parseColumnName(name);
			if (column != null) {
				builder.add(column);
			} else {
				builder.addOffset();
			}
		}

		return builder.build();
	}

	public MappingsBuilder() {
		mOffset = 0;
		mTables = Tables.getInstance();
		mMappings = new int[mTables.size()][];
	}

	public MappingsBuilder addOffset() {
		mOffset++;
		return this;
	}

	public MappingsBuilder addOffset(int inc) {
		mOffset += inc;
		return this;
	}

	public MappingsBuilder add(Table table) {
		add(table.getColumns());
		return this;
	}

	public MappingsBuilder add(Column column) {
		Table table = column.getTable();
		if (mMappings[table.index] == null) {
			mMappings[table.index] = new int[table.getColumnsSize()];
			Arrays.fill(mMappings[table.index], -1);
		}

		if (mMappings[table.index][column.index] == -1) {
			mMappings[table.index][column.index] = mOffset++;
		}

		return this;
	}

	public MappingsBuilder add(Iterable<? extends Column> columns) {
		for (Column column : columns) {
			add(column);
		}
		return this;
	}

	public MappingsBuilder add(Column... columns) {
		for (Column column : columns) {
			add(column);
		}
		return this;
	}

	public int[][] build() {
		return mMappings;
	}

}
