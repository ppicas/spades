package cat.ppicas.spades.models.auto;

import cat.ppicas.spades.Related;
import cat.ppicas.spades.models.Company;
import cat.ppicas.spades.query.Query;

public class CompanyAuto extends Company {

	private Related<BuildingAuto> mMainBuilding = new Related<BuildingAuto>(BuildingDao.COMPANY_ID,
			BuildingDao.MAPPER, Query.expr("%s", BuildingDao.IS_MAIN));

	@Override
	public Related<BuildingAuto> getMainBuilding() {
		return mMainBuilding;
	}

}
