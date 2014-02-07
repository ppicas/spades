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

import cat.picas.spades.models.auto.CompanyDao;
import cat.picas.spades.query.NameMapper;
import junit.framework.TestCase;

public class SqlHelperTest extends TestCase {

	private NameMapper mNameMapper;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mNameMapper = new NameMapper();
	}

	public void test__Should_create_a_SQL_expression_for_a_column() throws Exception {
		String expr = SqlHelper.expr(CompanyDao.NAME, "IS NULL");

		String expected = mNameMapper.ref(CompanyDao.NAME) + " IS NULL";
		assertEquals(expected, expr);
	}

	public void test__Should_create_a_SQL_expression_with_column_placeholders() throws Exception {
		String expr = SqlHelper.expr("%s %s %s", CompanyDao.NAME, CompanyDao.SIZE, CompanyDao.REGISTRATION);

		String expected = mNameMapper.ref(CompanyDao.NAME) + " "
				+ mNameMapper.ref(CompanyDao.SIZE) + " "
				+ mNameMapper.ref(CompanyDao.REGISTRATION);
		assertEquals(expected, expr);
	}

}
