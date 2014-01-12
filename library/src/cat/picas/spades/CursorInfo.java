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

public class CursorInfo {

	private boolean[] mHasTables;
	private int[][] mColumnIndexes;

	protected CursorInfo(boolean[] hasTables, int[][] columnIndexes) {
		mHasTables = hasTables;
		mColumnIndexes = columnIndexes;
	}

	public boolean hasTable(Table table) {
		return mHasTables[table.index];
	}

	public boolean hasTable(int tableIndex) {
		return mHasTables[tableIndex];
	}

	public boolean hasColumn(Column column) {
		return mColumnIndexes[column.getTable().index][column.index] != -1;
	}

	public boolean hasColumn(int tableIndex, int columnIndex) {
		return mColumnIndexes[tableIndex][columnIndex] != -1;
	}

	public int getColumnIndex(Column column) {
		return mColumnIndexes[column.getTable().index][column.index];
	}

	public int getColumnIndex(int tableIndex, int columnIndex) {
		return mColumnIndexes[tableIndex][columnIndex];
	}

}
