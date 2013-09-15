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

package cat.ppicas.spades.models.manual;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.Column.ColumnId;
import cat.ppicas.spades.Dao;
import cat.ppicas.spades.EntityMapper;
import cat.ppicas.spades.Table;
import cat.ppicas.spades.TableBuilder;
import cat.ppicas.spades.models.Company.CompanySize;

public class CompanyDao extends Dao<CompanyManual> {

	public static final Table TABLE = new TableBuilder("companies_manual", CompanyManual.class)
			.columnId("id")
			.columnText("name").notNull().end()
			.columnInteger("fundation_year").notNull().end()
			.columnInteger("registration").end()
			.columnText("size").end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();
	public static final Column NAME = TABLE.getColumn("name");
	public static final Column FUNDATION_YEAR = TABLE.getColumn("fundation_year");
	public static final Column REGISTRATION = TABLE.getColumn("registration");
	public static final Column SIZE = TABLE.getColumn("size");

	public static final EntityMapper<CompanyManual> MAPPER = new EntityMapper<CompanyManual>(TABLE) {

		@Override
		protected CompanyManual newInstance(Cursor cursor, int[][] mappings) {
			return new CompanyManual();
		}

		@Override
		protected void mapCursorValues(CompanyManual company, Cursor cursor, int[][] mappings,
				int tableIndex) {
			int[] maps = mappings[tableIndex];
			int index;

			index = maps[NAME.index];
			if (index != -1) {
				company.setName(cursor.getString(index));
			}
			index = maps[FUNDATION_YEAR.index];
			if (index != -1) {
				company.setFundationYear(cursor.getInt(index));
			}
			index = maps[REGISTRATION.index];
			if (index != -1) {
				company.setRegistration(cursor.isNull(index) ? null
						: new Date(cursor.getLong(index)));
			}
			index = maps[SIZE.index];
			if (index != -1) {
				company.setSize(cursor.isNull(index) ? null
						: CompanySize.valueOf(cursor.getString(index)));
			}

			company.getMainBuilding().setKey(company.getEntityId());
		}

		@Override
		protected void mapContentValues(CompanyManual company, ContentValues values) {
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
