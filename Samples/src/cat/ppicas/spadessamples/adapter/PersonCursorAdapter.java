package cat.ppicas.spadessamples.adapter;

import cat.ppicas.spades.CursorInfo;
import cat.ppicas.spadessamples.R;
import cat.ppicas.spadessamples.model.ContactPoint;
import cat.ppicas.spadessamples.model.ContactPointDao;
import cat.ppicas.spadessamples.model.Person;
import cat.ppicas.spadessamples.model.PersonDao;
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
