package cat.ppicas.spades.models;

import cat.ppicas.spades.Entity;

public abstract class Address implements Entity {

	private long mId;
	private String mStreet;
	private String mTown;
	private String mPostalCode;

	@Override
	public long getEntityId() {
		return mId;
	}

	@Override
	public void setEntityId(long id) {
		mId = id;
	}

	public String getStreet() {
		return mStreet;
	}

	public void setStreet(String street) {
		mStreet = street;
	}

	public String getTown() {
		return mTown;
	}

	public void setTown(String town) {
		mTown = town;
	}

	public String getPostalCode() {
		return mPostalCode;
	}

	public void setPostalCode(String postalCode) {
		mPostalCode = postalCode;
	}

}
