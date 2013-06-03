package cat.ppicas.spades.models.manual;

import cat.ppicas.spades.Related;
import cat.ppicas.spades.models.Building;

public class BuildingManual extends Building {

	private Related<CompanyManual> mCompany = new Related<CompanyManual>(CompanyDao.ID, CompanyDao.MAPPER);

	@Override
	public Related<CompanyManual> getCompany() {
		return mCompany;
	}

}
