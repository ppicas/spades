package cat.ppicas.spades.models;

import cat.ppicas.spades.Table;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {

	private Table<?> mAutoCompanyTable;
	private Table<?> mAutoBuildingTable;
	private Table<?> mManualCompanyTable;
	private Table<?> mManualBuildingTable;

	public OpenHelper(Context context) {
		super(context, null, null, 1);

		mManualCompanyTable = cat.ppicas.spades.models.manual.CompanyDao.TABLE;
		mManualBuildingTable = cat.ppicas.spades.models.manual.BuildingDao.TABLE;

		mAutoCompanyTable = cat.ppicas.spades.models.auto.CompanyDao.TABLE;
		mAutoBuildingTable = cat.ppicas.spades.models.auto.BuildingDao.TABLE;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		mManualCompanyTable.createTables(db);
		mManualBuildingTable.createTables(db);

		mAutoCompanyTable.createTables(db);
		mAutoBuildingTable.createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		mManualCompanyTable.upgradeTables(db, oldVersion, newVersion);
		mManualBuildingTable.upgradeTables(db, oldVersion, newVersion);

		mAutoCompanyTable.upgradeTables(db, oldVersion, newVersion);
		mAutoBuildingTable.upgradeTables(db, oldVersion, newVersion);
	}

	@Override
	public void onConfigure(SQLiteDatabase db) {
		super.onConfigure(db);
		db.setForeignKeyConstraintsEnabled(true);
	}

}
