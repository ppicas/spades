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

package cat.picas.spadessamples.model.inheritance;

import cat.picas.spades.Entity;
import cat.picas.spades.Related;

public class Hotel implements Entity {

	public final Related<Place> place = new Related<Place>(PlaceDao.ID, PlaceDao.MAPPER);

	private Long mId;
	private int mTotalRooms;
	private int mAvailableRooms;

	@Override
	public Long getEntityId() {
		return mId;
	}

	@Override
	public void setEntityId(Long id) {
		mId = id;
	}

	public int getTotalRooms() {
		return mTotalRooms;
	}

	public void setTotalRooms(int totalRooms) {
		mTotalRooms = totalRooms;
	}

	public int getAvailableRooms() {
		return mAvailableRooms;
	}

	public void setAvailableRooms(int availableRooms) {
		mAvailableRooms = availableRooms;
	}

}
