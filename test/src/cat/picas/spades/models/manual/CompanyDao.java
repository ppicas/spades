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

package cat.picas.spades.models.manual;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.picas.spades.Column;
import cat.picas.spades.Column.ColumnId;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Dao;
import cat.picas.spades.EntityMapper;
import cat.picas.spades.Table;
import cat.picas.spades.Tables;
import cat.picas.spades.models.Company.CompanySize;
import cat.picas.spades.models.auto.BuildingDao;

public class CompanyDao extends Dao<CompanyManual> {

	public static final Table TABLE = Tables.newTable("companies_manual", CompanyManual.class);

	public static final ColumnId ID = TABLE.newColumnId("id");

	public static final Column NAME = TABLE.newColumnText("name").notNull().end();

	public static final Column FUNDATION_YEAR = TABLE.newColumnInteger("fundation_year")
			.notNull().indexed(false, true).end();

	public static final Column REGISTRATION = TABLE.newColumnInteger("registration").end();

	public static final Column SIZE = TABLE.newColumnText("size").end();

	public static final EntityMapper<CompanyManual> MAPPER = new EntityMapper<CompanyManual>(TABLE) {

		@Override
		public CompanyManual newInstance(Cursor cursor, CursorInfo cursorInfo) {
			return new CompanyManual();
		}

		@Override
		public void mapCursorValues(CompanyManual company, Cursor cursor, CursorInfo cursorInfo) {
			int index;

			index = cursorInfo.getColumnIndex(NAME);
			if (index != CursorInfo.INVALID_INDEX) {
				company.setName(cursor.getString(index));
			}
			index = cursorInfo.getColumnIndex(FUNDATION_YEAR);
			if (index != CursorInfo.INVALID_INDEX) {
				company.setFundationYear(cursor.getInt(index));
			}
			index = cursorInfo.getColumnIndex(REGISTRATION);
			if (index != CursorInfo.INVALID_INDEX) {
				company.setRegistration(cursor.isNull(index) ? null
						: new Date(cursor.getLong(index)));
			}
			index = cursorInfo.getColumnIndex(SIZE);
			if (index != CursorInfo.INVALID_INDEX) {
				company.setSize(cursor.isNull(index) ? null
						: CompanySize.valueOf(cursor.getString(index)));
			}

			index = cursorInfo.getColumnIndex(BuildingDao.IS_MAIN);
			if (index != CursorInfo.INVALID_INDEX && cursor.getInt(index) == 1) {
				company.getMainBuilding().fetch(cursor, cursorInfo);
			}
		}

		@Override
		public void mapContentValues(CompanyManual company, ContentValues values) {
			values.put(NAME.name, company.getName());
			values.put(FUNDATION_YEAR.name, company.getFundationYear());
			Date date = company.getRegistration();
			values.put(REGISTRATION.name, date != null ? date.getTime() : null);
			values.put(SIZE.name, company.getSize().name());
		}

	};

	public CompanyDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
