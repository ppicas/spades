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

package cat.ppicas.spades.query;

import java.util.concurrent.TimeUnit;

import cat.ppicas.spades.Column;
import cat.ppicas.spades.models.auto.BuildingDao;
import cat.ppicas.spades.models.auto.CompanyDao;
import junit.framework.TestCase;

public class NameMapperTest extends TestCase {

	private NameMapper mMapper;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mMapper = new NameMapper();
	}

	public void test__Should_return_the_column_represented_by_a_Magic_Alias__When_is_valid() throws Exception {
		String alias = mMapper.alias(CompanyDao.NAME);
		Column column = mMapper.parseColumnName(alias);
		assertEquals(CompanyDao.NAME, column);
	}

	public void test__Should_return_the_column_represented_by_a_Magic_Alias__When_is_invalid() throws Exception {
		Column column = mMapper.parseColumnName("INVALID");
		assertNull(column);
	}

	public void DISABLED__test__ParseColumnName_performance() throws Exception {
		Column[] columns = new Column[] {
				BuildingDao.ID,
				BuildingDao.COMPANY_ID,
				BuildingDao.ADDRESS,
				BuildingDao.PHONE,
				BuildingDao.FLOORS,
				BuildingDao.SURFACE };
		String[] aliases = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			aliases[i] = mMapper.alias(column);
		}

		long time = System.currentTimeMillis();
		int count = 0;
		while (System.currentTimeMillis() < time + TimeUnit.SECONDS.toMillis(5)) {
			for (int i = 0; i < aliases.length; i++) {
				String alias = aliases[i];
				Column column = mMapper.parseColumnName(alias);
				assertEquals(columns[i], column);
				count++;
			}
		}
		System.out.println("Count=" + count);
	}

}
