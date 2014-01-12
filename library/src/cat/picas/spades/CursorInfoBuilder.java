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

import java.util.Arrays;

public class CursorInfoBuilder {

	private final Tables mTables = Tables.getInstance();
	private int mOffset = 0;
	private boolean[] mHasTables;
	private int[][] mColumnIndexes;

	public CursorInfoBuilder() {
		mHasTables = new boolean[mTables.size()];
		Arrays.fill(mHasTables, false);

		mColumnIndexes = new int[mTables.size()][];
		for (Table table : mTables.getTables()) {
			mColumnIndexes[table.index] = new int[table.getColumnsSize()];
			Arrays.fill(mColumnIndexes[table.index], -1);
		}
	}

	public CursorInfoBuilder addOffset() {
		mOffset++;
		return this;
	}

	public CursorInfoBuilder addOffset(int inc) {
		mOffset += inc;
		return this;
	}

	public CursorInfoBuilder add(Table table) {
		add(table.getColumns());
		return this;
	}

	public CursorInfoBuilder add(Column column) {
		Table table = column.getTable();
		if (mColumnIndexes[table.index][column.index] == -1) {
			mHasTables[table.index] = true;
			mColumnIndexes[table.index][column.index] = mOffset++;
		}

		return this;
	}

	public CursorInfoBuilder add(Iterable<? extends Column> columns) {
		for (Column column : columns) {
			add(column);
		}
		return this;
	}

	public CursorInfoBuilder add(Column... columns) {
		for (Column column : columns) {
			add(column);
		}
		return this;
	}

	public CursorInfo build() {
		return new CursorInfo(mHasTables, mColumnIndexes);
	}

}
