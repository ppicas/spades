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

package cat.picas.spades.models.manual;

import cat.picas.spades.RelatedInverse;
import cat.picas.spades.RelatedList;
import cat.picas.spades.SqlHelper;
import cat.picas.spades.models.Company;

public class CompanyManual extends Company {

	private final RelatedInverse<BuildingManual> mMainBuilding = new RelatedInverse<BuildingManual>(
            this, BuildingDao.COMPANY_ID, BuildingDao.MAPPER, SqlHelper.expr("%s", BuildingDao.IS_MAIN));

	private final RelatedList<BuildingManual> mBuildings = new RelatedList<BuildingManual>(
            this, BuildingDao.COMPANY_ID, BuildingDao.MAPPER);

	@Override
	public RelatedInverse<BuildingManual> getMainBuilding() {
		return mMainBuilding;
	}

	@Override
	public RelatedList<BuildingManual> getBuildings() {
		return mBuildings;
	}

}
