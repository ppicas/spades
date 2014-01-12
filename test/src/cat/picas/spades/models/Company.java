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

package cat.picas.spades.models;

import java.util.Date;

import cat.picas.spades.Entity;
import cat.picas.spades.RelatedInverse;
import cat.picas.spades.RelatedList;

public abstract class Company implements Entity {

	public static enum CompanySize {
		SMALL, MEDIUM, LARGE
	}

	private Long mId;
	private String mName = "";
	private int mFundationYear;
	private Date mRegistration;
	private CompanySize mSize;

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

	public CompanySize getSize() {
		return mSize;
	}

	public void setSize(CompanySize size) {
		mSize = size;
	}

	public abstract RelatedInverse<? extends Building> getMainBuilding();

	public abstract RelatedList<? extends Building> getBuildings();

}