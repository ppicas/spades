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

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cat.picas.spades.query.Query;
import cat.picas.spadessamples.adapter.PersonArrayAdapter;
import cat.picas.spadessamples.model.ContactPointDao;
import cat.picas.spadessamples.model.DatabaseHelper;
import cat.picas.spadessamples.model.Person;
import cat.picas.spadessamples.model.PersonDao;

public class ArrayListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DatabaseHelper helper = new DatabaseHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();
		PersonDao dao = new PersonDao(db);

		Query query = new Query(PersonDao.TABLE)
				.leftJoin(ContactPointDao.TABLE, "%s = %s", ContactPointDao.PERSON_ID, PersonDao.ID)
				.groupBy(PersonDao.ID);
		List<Person> people = dao.fetchAllWithRelated(query);

		helper.close();

		PersonArrayAdapter adapter = new PersonArrayAdapter(this);
		for (Person person : people) {
			adapter.add(person);
		}

		setListAdapter(adapter);
		getListView().setOnItemClickListener(mListItemClickListener );
	}

	private final OnItemClickListener mListItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(ArrayListActivity.this, PersonDetailActivity.class);
			intent.setData(Uri.parse(String.valueOf(id)));
			startActivity(intent);
		}
	};

}
