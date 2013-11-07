package cat.ppicas.spadessamples.adapter;

import cat.ppicas.spades.CursorInfo;
import cat.ppicas.spadessamples.R;
import cat.ppicas.spadessamples.model.Person;
import cat.ppicas.spadessamples.model.PersonDao;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
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

		TextView name = (TextView) view.findViewById(R.id.name);
		TextView contactPoint = (TextView) view.findViewById(R.id.contact_point);
		ImageView hasSpouse = (ImageView) view.findViewById(R.id.has_spouse);

		name.setText(person.getName());
		contactPoint.setText("TODO");
		hasSpouse.setImageResource((person.getSpouse().getKey() != null)
				? android.R.drawable.checkbox_on_background
				: android.R.drawable.checkbox_off_background);
	}

}
