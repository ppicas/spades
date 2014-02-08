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

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cat.picas.spadessamples.R;
import cat.picas.spadessamples.model.ContactPoint;
import cat.picas.spadessamples.model.Person;

public class PersonArrayAdapter extends ArrayAdapter<Person> {

	public PersonArrayAdapter(Context context) {
		super(context, R.layout.list_item_person, R.id.name);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);

		Person person = getItem(position);

		List<ContactPoint> contactPoints = person.contactPoints.getList();
		ContactPoint contactPoint = !contactPoints.isEmpty() ? contactPoints.get(0) : null;

		TextView nameView = (TextView) view.findViewById(R.id.name);
		TextView contactPointView = (TextView) view.findViewById(R.id.contact_point);
		ImageView hasSpouseView = (ImageView) view.findViewById(R.id.has_spouse);

		nameView.setText(person.getName());
		contactPointView.setText(getContactPoint(contactPoint));
		hasSpouseView.setImageResource((!person.spouse.isNull())
				? android.R.drawable.checkbox_on_background
				: android.R.drawable.checkbox_off_background);

		return view;
	}

	@Override
	public long getItemId(int position) {
		Person person = getItem(position);
		return person != null ? person.getEntityId() : -1;
	}

	private CharSequence getContactPoint(ContactPoint cp) {
		if (cp == null) {
			return "";
		}

		return (cp.getEmail() != null) ? cp.getEmail() : cp.getPhone();
	}

}
