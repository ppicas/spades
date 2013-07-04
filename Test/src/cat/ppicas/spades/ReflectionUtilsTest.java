package cat.ppicas.spades;

import java.util.Set;

import junit.framework.TestCase;

public class ReflectionUtilsTest extends TestCase {

	public void test__Should_generate_field_names_from_column_name__When_one_word() throws Exception {
		Set<String> names = ReflectionUtils.generateFieldNamesFromColumnName("one");
		assertTrue(names.contains("one"));
		assertTrue(names.contains("mOne"));
	}

	public void test__Should_generate_field_names_from_column_name__When_two_words() throws Exception {
		Set<String> names = ReflectionUtils.generateFieldNamesFromColumnName("TWO_WORDS");
		assertTrue(names.contains("two_words"));
		assertTrue(names.contains("twoWords"));
		assertTrue(names.contains("mTwoWords"));
	}

	public void test__Should_generate_field_names_from_column_name__When_two_words_capitalized() throws Exception {
		Set<String> names = ReflectionUtils.generateFieldNamesFromColumnName("twoWords");
		assertTrue(names.contains("twoWords"));
		assertTrue(names.contains("mTwoWords"));
	}

	public void test__Should_generate_field_names_from_column_name__When_has_prefix_is() throws Exception {
		Set<String> names = ReflectionUtils.generateFieldNamesFromColumnName("is_boolean");
		assertTrue(names.contains("is_boolean"));
		assertTrue(names.contains("isBoolean"));
		assertTrue(names.contains("mIsBoolean"));
		assertTrue(names.contains("boolean"));
		assertTrue(names.contains("mBoolean"));
	}

	public void test__Should_generate_field_names_from_column_name__When_has_suffix_id() throws Exception {
		Set<String> names = ReflectionUtils.generateFieldNamesFromColumnName("something_id");
		assertTrue(names.contains("something_id"));
		assertTrue(names.contains("somethingId"));
		assertTrue(names.contains("mSomethingId"));
		assertTrue(names.contains("something"));
		assertTrue(names.contains("mSomething"));
	}

}
