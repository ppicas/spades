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

package cat.picas.spades;

import android.os.Bundle;
import android.test.AndroidTestCase;

import cat.picas.spades.models.auto.BuildingDao;
import cat.picas.spades.models.auto.CompanyDao;

public class CursorInfoTest extends AndroidTestCase {
    public void test__Should_serialize_and_unserialize_all_fields() throws Exception {
        // Given
        CursorInfoBuilder builder = new CursorInfoBuilder();
        builder.add(CompanyDao.NAME);
        builder.add(CompanyDao.REGISTRATION);
        CursorInfo cursorInfo = builder.build();

        // When
        Bundle bundle = new Bundle();
        bundle.putParcelable("cursorInfo", cursorInfo);
        cursorInfo = bundle.getParcelable("cursorInfo");

        // Then
        assertTrue(cursorInfo.hasTable(CompanyDao.TABLE));
        assertFalse(cursorInfo.hasTable(BuildingDao.TABLE));
        // And
        assertTrue(cursorInfo.hasColumn(CompanyDao.NAME));
        assertTrue(cursorInfo.hasColumn(CompanyDao.REGISTRATION));
        assertFalse(cursorInfo.hasColumn(CompanyDao.ID));
        assertFalse(cursorInfo.hasColumn(BuildingDao.PHONE));
    }
}
