package cat.ppicas.spades.models;

import cat.ppicas.spades.Entity;

public class Building implements Entity {

	private long mId;
	private long mCompanyId;
	private String mAddress;
	private String mPhone;
	private int mFloors;
	private double mSurface;

	@Override
	public long getEntityId() {
		return mId;
	}

	@Override
	public void setEntityId(long id) {
		mId = id;
	}

	public long getCompanyId() {
		return mCompanyId;
	}

	public void setCompanyId(long companyId) {
		mCompanyId = companyId;
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

}
