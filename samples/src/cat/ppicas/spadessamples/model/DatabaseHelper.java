package cat.ppicas.spadessamples.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "database";
	private static final int DB_VERSION = 1;

	public static void deleteDatabase(Context context) {
		context.deleteDatabase(DB_NAME);
	}

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		PersonDao.TABLE.createTable(db);
		ContactPointDao.TABLE.createTable(db);
		FixturesHelper.createFixtures(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		PersonDao.TABLE.dropTable(db);
		ContactPointDao.TABLE.dropTable(db);
		onCreate(db);
	}

}
