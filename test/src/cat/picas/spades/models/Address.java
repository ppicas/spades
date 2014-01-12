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

import cat.picas.spades.Entity;

public abstract class Address implements Entity {

	private Long mId;
	private String mStreet;
	private String mTown;
	private String mPostalCode;

	@Override
	public Long getEntityId() {
		return mId;
	}

	@Override
	public void setEntityId(Long id) {
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
