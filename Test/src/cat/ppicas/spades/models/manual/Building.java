package cat.ppicas.spades.models.manual;

import cat.ppicas.spades.Entity;
import cat.ppicas.spades.Related;

public class Building implements Entity {

	private long mId;
	private Related<Company> mCompany = new Related<Company>(CompanyDao.ID, CompanyDao.MAPPER);
	private String mAddress;
	private String mPhone;
	private int mFloors;
	private double mSurface;
	private boolean mMain;

	@Override
	public long getEntityId() {
		return mId;
	}

	@Override
	public void setEntityId(long id) {
		mId = id;
	}

	public Related<Company> getCompany() {
		return mCompany;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String address) {
		mAddress = address;
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		mPhone = phone;
	}

	public int getFloors() {
		return mFloors;
	}

	public void setFloors(int floors) {
		mFloors = floors;
	}

	public double getSurface() {
		return mSurface;
	}

	public void setSurface(double surface) {
		mSurface = surface;
	}

	public boolean isMain() {
		return mMain;
	}

	public void setMain(boolean main) {
		mMain = main;
	}

}
