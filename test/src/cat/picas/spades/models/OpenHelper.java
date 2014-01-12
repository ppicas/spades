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

package cat.picas.spades.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cat.picas.spades.Table;

public class OpenHelper extends SQLiteOpenHelper {

	private Table mAutoCompanyTable;
	private Table mAutoBuildingTable;
	private Table mManualCompanyTable;
	private Table mManualBuildingTable;

	public OpenHelper(Context context) {
		super(context, null, null, 1);

		mManualCompanyTable = cat.picas.spades.models.manual.CompanyDao.TABLE;
		mManualBuildingTable = cat.picas.spades.models.manual.BuildingDao.TABLE;

		mAutoCompanyTable = cat.picas.spades.models.auto.CompanyDao.TABLE;
		mAutoBuildingTable = cat.picas.spades.models.auto.BuildingDao.TABLE;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("PRAGMA foreign_keys = ON;");
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		createTables(db);
	}

	/*@Override
	public void onConfigure(SQLiteDatabase db) {
		super.onConfigure(db);
		db.setForeignKeyConstraintsEnabled(true);
	}*/

	private void createTables(SQLiteDatabase db) {
		mManualCompanyTable.createTable(db);
		mManualBuildingTable.createTable(db);

		mAutoCompanyTable.createTable(db);
		mAutoBuildingTable.createTable(db);
	}

	private void dropTables(SQLiteDatabase db) {
		mManualCompanyTable.dropTable(db);
		mManualBuildingTable.dropTable(db);

		mAutoCompanyTable.dropTable(db);
		mAutoBuildingTable.dropTable(db);
	}

}
