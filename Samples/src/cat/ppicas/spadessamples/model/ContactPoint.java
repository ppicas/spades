package cat.ppicas.spadessamples.model;

import cat.ppicas.spades.Entity;
import cat.ppicas.spades.Related;

public class ContactPoint implements Entity {

	private Long mId;
	private String mName;
	private String mEmail;
	private String mPhone;

	private Related<Person> mPerson = new Related<Person>(PersonDao.ID, PersonDao.MAPPER);

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

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		mEmail = email;
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		mPhone = phone;
	}

	public Related<Person> getPerson() {
		return mPerson;
	}

}
