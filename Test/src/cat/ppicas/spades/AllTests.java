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

package cat.ppicas.spades;

import cat.ppicas.spades.query.ColumnSelectorTest;
import cat.ppicas.spades.query.NameMapperTest;
import cat.ppicas.spades.util.ReflectionUtilsTest;
import cat.ppicas.spades.util.TextUtilsTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(AutoModelsIntegrationTest.class);
		suite.addTestSuite(ManualModelsIntegrationTest.class);
		suite.addTestSuite(ColumnSelectorTest.class);
		suite.addTestSuite(NameMapperTest.class);
		suite.addTestSuite(ReflectionUtilsTest.class);
		suite.addTestSuite(TextUtilsTest.class);
		//$JUnit-END$
		return suite;
	}

}
