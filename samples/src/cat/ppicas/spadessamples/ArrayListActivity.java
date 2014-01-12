package cat.ppicas.spadessamples;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cat.ppicas.spades.query.Query;
import cat.ppicas.spadessamples.adapter.PersonArrayAdapter;
import cat.ppicas.spadessamples.model.ContactPointDao;
import cat.ppicas.spadessamples.model.DatabaseHelper;
import cat.ppicas.spadessamples.model.Person;
import cat.ppicas.spadessamples.model.PersonDao;

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
		adapter.addAll(people);

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
