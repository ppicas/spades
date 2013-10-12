package cat.ppicas.spadessamples;

import android.app.Activity;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import cat.ppicas.spades.query.Query;
import cat.ppicas.spadessamples.model.DatabaseHelper;
import cat.ppicas.spadessamples.model.PersonDao;

public class CursorListActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DatabaseHelper helper = new DatabaseHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		Query query = new Query(PersonDao.TABLE);
		Cursor cursor = query.execute(db);
		DatabaseUtils.dumpCursor(cursor);
		cursor.close();

		helper.close();
	}
}
