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

import android.provider.BaseColumns;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.SqlHelper;
import cat.ppicas.spades.Column.ColumnId;

class SelectedColumn {

	private NameMapper mMapper = new NameMapper();
	private Column mColumn;
	private String mCustomExpression;
	private Column[] mCustomColumns;

	public SelectedColumn(Column column) {
		mColumn = column;
	}

	public SelectedColumn(String expression, Column... columns) {
		mCustomExpression = expression;
		mCustomColumns = columns;
	}

	public boolean isCustom() {
		return mCustomExpression != null;
	}

	public Column getColumn() {
		return mColumn;
	}

	public String getCustomExpression() {
		return mCustomExpression;
	}

	public Column[] getCustomColumns() {
		return mCustomColumns;
	}

	public String format(boolean magicColumn) {
		if (!isCustom()) {
			if (magicColumn) {
				return mMapper.ref(mColumn) + " AS " + mMapper.alias(mColumn);
			} else {
				return mMapper.ref(mColumn);
			}
		} else {
			return SqlHelper.expr(mCustomExpression, mCustomColumns);
		}
	}

	static class RowIdColumn extends SelectedColumn {

		public RowIdColumn(ColumnId column) {
			super("%s as " + BaseColumns._ID, column);
		}

	}

	static class CountColumn extends SelectedColumn {

		public CountColumn() {
			super("COUNT(*)");
		}

	}

}
