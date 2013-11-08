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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.Column;
import cat.ppicas.spades.Column.ColumnId;
import cat.ppicas.spades.CursorInfo;
import cat.ppicas.spades.Dao;
import cat.ppicas.spades.EntityMapper;
import cat.ppicas.spades.Table;
import cat.ppicas.spades.TableBuilder;
import cat.ppicas.spades.models.auto.CompanyDao;

public class BuildingDao extends Dao<BuildingManual> {

	public static final Table TABLE = new TableBuilder("buildings_manual", BuildingManual.class)
			.columnId("id")
			.columnInteger("company_id").notNull().foreignKey(CompanyDao.ID).end()
			.columnText("address").notNull().defaultValue("").end()
			.columnText("phone").end()
			.columnInteger("floors").notNull().defaultValue(0).end()
			.columnReal("surface").notNull().defaultValue(0).end()
			.columnInteger("is_main").notNull().defaultValue(false).end()
			.build();

	public static final ColumnId ID = TABLE.getColumnId();
	public static final Column COMPANY_ID = TABLE.getColumn("company_id");
	public static final Column ADDRESS = TABLE.getColumn("address");
	public static final Column PHONE = TABLE.getColumn("phone");
	public static final Column FLOORS = TABLE.getColumn("floors");
	public static final Column SURFACE = TABLE.getColumn("surface");
	public static final Column IS_MAIN = TABLE.getColumn("is_main");

	public static final EntityMapper<BuildingManual> MAPPER = new EntityMapper<BuildingManual>(TABLE) {

		@Override
		protected BuildingManual newInstance(Cursor cursor, CursorInfo cursorInfo) {
			return new BuildingManual();
		}

		@Override
		protected void mapCursorValues(BuildingManual building, Cursor cursor, CursorInfo cursorInfo) {
			int index;

			index = cursorInfo.getColumnIndex(COMPANY_ID);
			if (index != -1) {
				building.getCompany().setRawValue(cursor.getLong(index));
			}
			index = cursorInfo.getColumnIndex(ADDRESS);
			if (index != -1) {
				building.setAddress(cursor.getString(index));
			}
			index = cursorInfo.getColumnIndex(PHONE);
			if (index != -1) {
				building.setPhone(cursor.isNull(index) ? null : cursor.getString(index));
			}
			index = cursorInfo.getColumnIndex(FLOORS);
			if (index != -1) {
				building.setFloors(cursor.getInt(index));
			}
			index = cursorInfo.getColumnIndex(SURFACE);
			if (index != -1) {
				building.setSurface(cursor.getDouble(index));
			}
			index = cursorInfo.getColumnIndex(IS_MAIN);
			if (index != -1) {
				building.setMain(cursor.getInt(index) == 1);
			}

			building.getCompany().fetch(cursor, cursorInfo);
		}

		@Override
		protected void mapContentValues(BuildingManual building, ContentValues values) {
			values.put(COMPANY_ID.name, building.getCompany().getRawValue());
			if (building.getAddress() != null) {
				values.put(ADDRESS.name, building.getAddress());
			}
			values.put(PHONE.name, building.getPhone());
			values.put(FLOORS.name, building.getFloors());
			values.put(SURFACE.name, building.getSurface());
			values.put(IS_MAIN.name, building.isMain());
		}

	};

	public BuildingDao(SQLiteDatabase db) {
		super(db, TABLE, MAPPER);
	}

}
