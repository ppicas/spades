package cat.ppicas.spades.models.manual;

import cat.ppicas.spades.Related;
import cat.ppicas.spades.models.BaseBuilding;

public class Building extends BaseBuilding {

	private Related<Company> mCompany = new Related<Company>(CompanyDao.ID, CompanyDao.MAPPER);

	@Override
	public Related<Company> getCompany() {
		return mCompany;
	}

}
