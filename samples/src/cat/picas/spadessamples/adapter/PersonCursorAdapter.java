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

import cat.picas.spades.CursorInfo;
import cat.picas.spadessamples.R;
import cat.picas.spadessamples.model.ContactPoint;
import cat.picas.spadessamples.model.ContactPointDao;
import cat.picas.spadessamples.model.Person;
import cat.picas.spadessamples.model.PersonDao;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonCursorAdapter extends ResourceCursorAdapter {

	private CursorInfo mCursorInfo;

	public PersonCursorAdapter(Context context, Cursor c, CursorInfo cursorInfo) {
		super(context, R.layout.list_item_person, c, 0);
		mCursorInfo = cursorInfo;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Person person = PersonDao.MAPPER.createFromCursor(cursor, mCursorInfo);
		ContactPoint contactPoint = ContactPointDao.MAPPER.createFromCursor(cursor, mCursorInfo);

		TextView nameView = (TextView) view.findViewById(R.id.name);
		TextView contactPointView = (TextView) view.findViewById(R.id.contact_point);
		ImageView hasSpouseView = (ImageView) view.findViewById(R.id.has_spouse);

		nameView.setText(person.getName());
		contactPointView.setText(getContactPoint(contactPoint));
		hasSpouseView.setImageResource((!person.getSpouse().isNull())
				? android.R.drawable.checkbox_on_background
				: android.R.drawable.checkbox_off_background);
	}

	private CharSequence getContactPoint(ContactPoint cp) {
		if (cp == null) {
			return "";
		}

		return (cp.getEmail() != null) ? cp.getEmail() : cp.getPhone();
	}

}
