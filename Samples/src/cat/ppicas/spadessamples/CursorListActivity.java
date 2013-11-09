package cat.ppicas.spadessamples;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import cat.ppicas.spades.query.Query;
import cat.ppicas.spadessamples.adapter.PersonCursorAdapter;
import cat.ppicas.spadessamples.model.ContactPointDao;
import cat.ppicas.spadessamples.model.DatabaseHelper;
import cat.ppicas.spadessamples.model.PersonDao;

public class CursorListActivity extends ListActivity {

	private Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DatabaseHelper helper = new DatabaseHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		Query query = new Query(PersonDao.TABLE)
				.leftJoin(ContactPointDao.TABLE, "%s = %s", ContactPointDao.PERSON_ID, PersonDao.ID)
				.groupBy(PersonDao.ID);
		mCursor = query.execute(db);
		DatabaseUtils.dumpCursor(mCursor);

		helper.close();

		setListAdapter(new PersonCursorAdapter(this, mCursor, query.getCursorInfo()));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mCursor != null) {
			mCursor.close();
		}
	}

}
