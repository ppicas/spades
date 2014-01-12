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

import cat.picas.spades.RelatedInverse;
import cat.picas.spades.RelatedList;
import cat.picas.spades.SqlHelper;
import cat.picas.spades.models.Company;

public class CompanyAuto extends Company {

	private RelatedInverse<BuildingAuto> mMainBuilding = new RelatedInverse<BuildingAuto>(this,
			BuildingDao.COMPANY_ID, BuildingDao.MAPPER, SqlHelper.expr("%s", BuildingDao.IS_MAIN));

	private RelatedList<BuildingAuto> mBuildings = new RelatedList<BuildingAuto>(this,
			BuildingDao.COMPANY_ID, BuildingDao.MAPPER);

	@Override
	public RelatedInverse<BuildingAuto> getMainBuilding() {
		return mMainBuilding;
	}

	@Override
	public RelatedList<BuildingAuto> getBuildings() {
		return mBuildings;
	}

}
