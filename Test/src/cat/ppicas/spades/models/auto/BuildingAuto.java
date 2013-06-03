package cat.ppicas.spades.models.auto;

import cat.ppicas.spades.Related;
import cat.ppicas.spades.models.Building;

public class BuildingAuto extends Building {

	private Related<CompanyAuto> mCompany = new Related<CompanyAuto>(CompanyDao.ID, CompanyDao.MAPPER);

	@Override
	public Related<CompanyAuto> getCompany() {
		return mCompany;
	}

}
