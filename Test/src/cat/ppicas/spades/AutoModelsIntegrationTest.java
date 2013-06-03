package cat.ppicas.spades;

import android.database.sqlite.SQLiteDatabase;
import cat.ppicas.spades.models.Building;
import cat.ppicas.spades.models.Company;
import cat.ppicas.spades.models.auto.BuildingAuto;
import cat.ppicas.spades.models.auto.BuildingDao;
import cat.ppicas.spades.models.auto.CompanyAuto;
import cat.ppicas.spades.models.auto.CompanyDao;

public class AutoModelsIntegrationTest extends IntegrationBaseTest {

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
		return new CompanyAuto();
	}

	@Override
	protected Building newBuilding() {
		return new BuildingAuto();
	}

}
