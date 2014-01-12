package cat.picas.spadessamples;

import cat.picas.spadessamples.model.ContactPoint;
import cat.picas.spadessamples.model.DatabaseHelper;
import cat.picas.spadessamples.model.Person;
import cat.picas.spadessamples.model.PersonDao;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class PersonDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_detail);
		Person person = getPersonFromIntent();
		updateViews(person);
	}

	private Person getPersonFromIntent() {
		Intent intent = getIntent();
		Uri data = intent.getData();
		long id = data.isHierarchical() ? Long.valueOf(data.getLastPathSegment()) : -1;

		SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
		PersonDao dao = new PersonDao(db);
		Person person = dao.get(id);
		if (person != null) {
			person.getContactPoints().fetch(db);
		}
		db.close();

		return person;
	}

	private void updateViews(Person person) {
		TextView nameView = (TextView) findViewById(R.id.name);
		TextView infoView = (TextView) findViewById(R.id.info);

		nameView.setText(person.getName());

		StringBuilder info = new StringBuilder();
		info.append("Birth date: ").append(person.getBirthDate()).append("\n");
		info.append("Gender: ").append(person.getGender().name()).append("\n");
		info.append("Height: ").append(person.getHeight()).append("\n");
		info.append("Weight: ").append(person.getWeight()).append("\n");

		for (ContactPoint contact : person.getContactPoints().getList()) {
			info.append("\n");
			info.append("Contact '").append(contact.getName()).append("'\n");
			info.append("\tEmail: ").append(contact.getEmail()).append("\n");
			info.append("\tPhone: ").append(contact.getPhone()).append("\n");
		}

		infoView.setText(info);
	}

}
