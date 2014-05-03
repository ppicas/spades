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

package cat.picas.spades.models.auto;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cat.picas.spades.AutoEntityMapper;
import cat.picas.spades.Column;
import cat.picas.spades.Column.ColumnId;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Dao;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.Tables;

public class CompanyDao extends Dao<CompanyAuto> {

	public static final Table TABLE = Tables.newTable("companies_auto", CompanyAuto.class);

	public static final ColumnId ID = TABLE.newColumnId("id");

	public static final Column NAME = TABLE.newColumnAuto("name", "mName").notNull().end();

	public static final Column FUNDATION_YEAR = TABLE.newColumnAuto("fundation_year",
			"mFundationYear").notNull().indexed(false, true).end();

	public static final Column REGISTRATION = TABLE.newColumnAuto("registration",
			"mRegistration").end();

	public static final Column SIZE = TABLE.newColumnAuto("size", "mSize").end();

	public static final EntityMapper<CompanyAuto> MAPPER = new AutoEntityMapper<CompanyAuto>(TABLE) {
		@Override
		public void mapCursorValues(CompanyAuto company, Cursor cursor, CursorInfo cursorInfo) {
			super.mapCursorValues(company, cursor, cursorInfo);

			int isMainIndex = cursorInfo.getColumnIndex(BuildingDao.IS_MAIN);
			if (isMainIndex != CursorInfo.INVALID_INDEX && cursor.getInt(isMainIndex) == 1) {
				company.getMainBuilding().fetch(cursor, cursorInfo);
			}
		}

	};

	public CompanyDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
