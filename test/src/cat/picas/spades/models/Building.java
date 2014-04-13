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
import cat.picas.spades.RelatedParent;

public abstract class Building implements Entity {

	private Long mId;
	private String mAddress;
	private String mPhone;
	private int mFloors;
	private double mSurface;
	private boolean mMain;

	public Building() {
		super();
	}

	@Override
	public Long getEntityId() {
		return mId;
	}

	@Override
	public void setEntityId(Long id) {
		mId = id;
	}

	public abstract RelatedParent<? extends Company> getCompany();

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