/**
 * Copyright (C) 2013 Pau Picas Sans <pau.picas@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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

	public final Related<Person> spouse = new Related<Person>(PersonDao.ID, PersonDao.MAPPER);

	public final RelatedList<ContactPoint> contactPoints = new RelatedList<ContactPoint>(this,
			ContactPointDao.PERSON_ID, ContactPointDao.MAPPER);

	private Long mId;
	private String mName;
	private Date mBirthDate;
	private Gender mGender;
	private double mHeight;
	private int mWeight;

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

}
