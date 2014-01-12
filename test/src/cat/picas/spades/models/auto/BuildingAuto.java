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

package cat.picas.spades.models.auto;

import cat.picas.spades.Related;
import cat.picas.spades.models.Building;

public class BuildingAuto extends Building {

	private Related<CompanyAuto> mCompany = new Related<CompanyAuto>(CompanyDao.ID, CompanyDao.MAPPER);

	@Override
	public Related<CompanyAuto> getCompany() {
		return mCompany;
	}

}
