package cat.ppicas.spades.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {

	public OpenHelper(Context context) {
		super(context, null, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		CompanyDao.TABLE.createTables(db);
		BuildingDao.TABLE.createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		CompanyDao.TABLE.upgradeTables(db, oldVersion, newVersion);
		BuildingDao.TABLE.upgradeTables(db, oldVersion, newVersion);
	}

}
