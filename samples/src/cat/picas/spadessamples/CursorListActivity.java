package cat.picas.spadessamples;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cat.picas.spades.query.Query;
import cat.picas.spadessamples.adapter.PersonCursorAdapter;
import cat.picas.spadessamples.model.ContactPointDao;
import cat.picas.spadessamples.model.DatabaseHelper;
import cat.picas.spadessamples.model.PersonDao;

public class CursorListActivity extends ListActivity {

	private DatabaseHelper mHelper;
	private Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mHelper = new DatabaseHelper(this);
		SQLiteDatabase db = mHelper.getReadableDatabase();

		Query query = new Query(PersonDao.TABLE)
				.leftJoin(ContactPointDao.TABLE, "%s = %s", ContactPointDao.PERSON_ID, PersonDao.ID)
				.groupBy(PersonDao.ID);
		mCursor = query.execute(db);

		setListAdapter(new PersonCursorAdapter(this, mCursor, query.getCursorInfo()));
		getListView().setOnItemClickListener(mListItemClickListener );
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mCursor.close();
		mHelper.close();
	}

	private final OnItemClickListener mListItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(CursorListActivity.this, PersonDetailActivity.class);
			intent.setData(Uri.parse(String.valueOf(id)));
			startActivity(intent);
		}
	};

}
