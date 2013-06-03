package cat.ppicas.spades;

import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.models.Building;
import cat.ppicas.spades.models.Company;
import cat.ppicas.spades.models.manual.BuildingDao;
import cat.ppicas.spades.models.manual.BuildingManual;
import cat.ppicas.spades.models.manual.CompanyDao;
import cat.ppicas.spades.models.manual.CompanyManual;

public class ManualModelsIntegrationTest extends IntegrationBaseTest {

	@Override
	protected Dao<? extends Company> newCompanyDao(SQLiteDatabase db) {
		return new CompanyDao(db);
	}

	@Override
	protected Dao<? extends Building> newBuildingDao(SQLiteDatabase db) {
		return new BuildingDao(db);
	}

	@Override
	protected Company newCompany() {
		return new CompanyManual();
	}

	@Override
	protected Building newBuilding() {
		return new BuildingManual();
	}

}
