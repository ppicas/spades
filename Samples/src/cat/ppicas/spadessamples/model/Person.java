package cat.ppicas.spadessamples.model;

import java.util.Date;

import cat.ppicas.spades.Entity;
import cat.ppicas.spades.Related;

public class Person implements Entity {

	public enum Gender {
		MALE,
		FEMALE
	}

	private long mId;
	private String mName;
	private Date mBirthDate;
	private Gender mGender;
	private double mHeight;
	private int mWeight;

	private Related<Person> mSpouse = new Related<Person>(PersonDao.ID, null);
	// TODO Related lists.
	// RelatedList<ContactPoint> contactPoints;

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

	Date getBirthDate() {
		return mBirthDate;
	}

	void setBirthDate(Date birthDate) {
		mBirthDate = birthDate;
	}

	Gender getGender() {
		return mGender;
	}

	void setGender(Gender gender) {
		mGender = gender;
	}

	public double getHeight() {
		return mHeight;
	}

	public void setHeight(double height) {
		mHeight = height;
	}

	public int getWeight() {
		return mWeight;
	}

	public void setWeight(int weight) {
		mWeight = weight;
	}

	public Related<Person> getSpouse() {
		return mSpouse;
	}

}