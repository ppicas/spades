package cat.ppicas.spades.models.auto;

import cat.ppicas.spades.Related;
import cat.ppicas.spades.models.BaseCompany;
import cat.ppicas.spades.query.Query;

public class Company extends BaseCompany {

	private Related<Building> mMainBuilding = new Related<Building>(BuildingDao.COMPANY_ID,
			BuildingDao.MAPPER, Query.expr("%s", BuildingDao.IS_MAIN));

	@Override
	public Related<Building> getMainBuilding() {
		return mMainBuilding;
	}

}
