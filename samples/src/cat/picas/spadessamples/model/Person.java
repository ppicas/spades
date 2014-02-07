package cat.picas.spadessamples.model;

import java.util.Date;

import cat.picas.spades.Entity;
import cat.picas.spades.Related;
import cat.picas.spades.RelatedList;

public class Person implements Entity {

	public enum Gender {
		MALE,
		FEMALE
	}

	private Long mId;
	private String mName;
	private Date mBirthDate;
	private Gender mGender;
	private double mHeight;
	private int mWeight;

	private Related<Person> mSpouse = new Related<Person>(PersonDao.ID, PersonDao.MAPPER);

	private RelatedList<ContactPoint> contactPoints = new RelatedList<ContactPoint>(this,
			ContactPointDao.PERSON_ID, ContactPointDao.MAPPER);

	@Override
	public Long getEntityId() {
		return mId;
	}

	@Override
	public void setEntityId(Long id) {
		mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public Date getBirthDate() {
		return mBirthDate;
	}

	public void setBirthDate(Date birthDate) {
		mBirthDate = birthDate;
	}

	public Gender getGender() {
		return mGender;
	}

	public void setGender(Gender gender) {
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

	public RelatedList<ContactPoint> getContactPoints() {
		return contactPoints;
	}

}
