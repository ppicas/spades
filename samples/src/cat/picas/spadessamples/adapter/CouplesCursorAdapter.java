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

package cat.picas.spadessamples.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

import cat.picas.spades.CursorInfo;
import cat.picas.spadessamples.R;
import cat.picas.spadessamples.model.Person;
import cat.picas.spadessamples.model.PersonDao;

public class CouplesCursorAdapter extends ResourceCursorAdapter {

	private CursorInfo mCursorInfo;

	public CouplesCursorAdapter(Context context, Cursor c, CursorInfo cursorInfo) {
		super(context, R.layout.list_item_couple, c, 0);
		mCursorInfo = cursorInfo;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Person person = PersonDao.MAPPER.createFromCursor(cursor, mCursorInfo);
		Person couple = PersonDao.MAPPER.alias().createFromCursor(cursor, mCursorInfo);

		TextView nameView = (TextView) view.findViewById(R.id.name);
		TextView spouseNameView = (TextView) view.findViewById(R.id.spouse_name);

		nameView.setText(person.getName());
		spouseNameView.setText((couple != null) ? couple.getName() : "Not married");
	}

}
