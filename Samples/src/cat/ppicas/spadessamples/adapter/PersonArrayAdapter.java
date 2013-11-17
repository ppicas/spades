package cat.ppicas.spadessamples.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cat.ppicas.spadessamples.R;
import cat.ppicas.spadessamples.model.ContactPoint;
import cat.ppicas.spadessamples.model.Person;

public class PersonArrayAdapter extends ArrayAdapter<Person> {

	public PersonArrayAdapter(Context context) {
		super(context, R.layout.list_item_person, R.id.name);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);

		Person person = getItem(position);

		List<ContactPoint> contactPoints = person.getContactPoints().getList();
		ContactPoint contactPoint = !contactPoints.isEmpty() ? contactPoints.get(0) : null;

		TextView nameView = (TextView) view.findViewById(R.id.name);
		TextView contactPointView = (TextView) view.findViewById(R.id.contact_point);
		ImageView hasSpouseView = (ImageView) view.findViewById(R.id.has_spouse);

		nameView.setText(person.getName());
		contactPointView.setText(getContactPoint(contactPoint));
		hasSpouseView.setImageResource((!person.getSpouse().isNull())
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
