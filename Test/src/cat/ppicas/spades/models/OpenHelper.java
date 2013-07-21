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

package cat.ppicas.spades.models;

import cat.ppicas.spades.Table;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {

	private Table mAutoCompanyTable;
	private Table mAutoBuildingTable;
	private Table mManualCompanyTable;
	private Table mManualBuildingTable;

	public OpenHelper(Context context) {
		super(context, null, null, 1);

		mManualCompanyTable = cat.ppicas.spades.models.manual.CompanyDao.TABLE;
		mManualBuildingTable = cat.ppicas.spades.models.manual.BuildingDao.TABLE;

		mAutoCompanyTable = cat.ppicas.spades.models.auto.CompanyDao.TABLE;
		mAutoBuildingTable = cat.ppicas.spades.models.auto.BuildingDao.TABLE;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("PRAGMA foreign_keys = ON;");

		mManualCompanyTable.createTables(db);
		mManualBuildingTable.createTables(db);

		mAutoCompanyTable.createTables(db);
		mAutoBuildingTable.createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		mManualCompanyTable.upgradeTables(db, oldVersion, newVersion);
		mManualBuildingTable.upgradeTables(db, oldVersion, newVersion);

		mAutoCompanyTable.upgradeTables(db, oldVersion, newVersion);
		mAutoBuildingTable.upgradeTables(db, oldVersion, newVersion);
	}

	/*@Override
	public void onConfigure(SQLiteDatabase db) {
		super.onConfigure(db);
		db.setForeignKeyConstraintsEnabled(true);
	}*/

}
