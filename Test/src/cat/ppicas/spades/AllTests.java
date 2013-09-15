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
