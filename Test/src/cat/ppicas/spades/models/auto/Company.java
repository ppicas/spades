package cat.ppicas.spades.models.auto;

import java.util.Date;

import cat.ppicas.spades.Entity;
import cat.ppicas.spades.Related;
import cat.ppicas.spades.query.Query;

public class Company implements Entity {

	private long mId;
	private String mName = "";
	private int mFundationYear;
	private Date mRegistration;

	private Related<Building> mMainBuilding = new Related<Building>(BuildingDao.COMPANY_ID,
			BuildingDao.MAPPER, Query.expr("%s", BuildingDao.IS_MAIN));

	@Override
	public long getEntityId() {
		return mId;
	}

	@Override
	public void setEntityId(long id) {
		mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public int getFundationYear() {
		return mFundationYear;
	}

	public void setFundationYear(int fundationYear) {
		mFundationYear = fundationYear;
	}

	public Date getRegistration() {
		return mRegistration;
	}

	public void setRegistration(Date registration) {
		mRegistration = registration;
	}

	public Related<Building> getMainBuilding() {
		return mMainBuilding;
	}

}
