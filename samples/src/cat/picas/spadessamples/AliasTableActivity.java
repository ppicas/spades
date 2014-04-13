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

package cat.picas.spadessamples;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import cat.picas.spades.query.Query;
import cat.picas.spadessamples.adapter.CouplesCursorAdapter;
import cat.picas.spadessamples.model.DatabaseHelper;
import cat.picas.spadessamples.model.PersonDao;

import static cat.picas.spadessamples.model.PersonDao.SpouseDao;
import static cat.picas.spadessamples.model.PersonDao.TABLE;

public class AliasTableActivity extends ListActivity {

	private DatabaseHelper mHelper;
	private Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mHelper = new DatabaseHelper(this);
		SQLiteDatabase db = mHelper.getReadableDatabase();

		PersonDao dao = new PersonDao(db);

		Query query = new Query(TABLE)
				.leftJoin(SpouseDao.TABLE, "%s = %s", SpouseDao.ID, PersonDao.SPOUSE_ID)
				.select(PersonDao.NAME, SpouseDao.NAME);

		mCursor = query.execute(db);

		setListAdapter(new CouplesCursorAdapter(this, mCursor, query.getCursorInfo()));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mCursor.close();
		mHelper.close();
	}

}
