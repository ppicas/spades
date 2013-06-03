package cat.ppicas.spades.models.manual;

import cat.ppicas.spades.Related;
import cat.ppicas.spades.models.Company;
import cat.ppicas.spades.query.Query;

public class CompanyManual extends Company {

	private Related<BuildingManual> mMainBuilding = new Related<BuildingManual>(BuildingDao.COMPANY_ID,
			BuildingDao.MAPPER, Query.expr("%s", BuildingDao.IS_MAIN));

	@Override
	public Related<BuildingManual> getMainBuilding() {
		return mMainBuilding;
	}

}
