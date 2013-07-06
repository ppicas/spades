package cat.ppicas.spades.text;

import java.util.Set;

import junit.framework.TestCase;

public class TextUtilsTest extends TestCase {

	public void test__Should_generate_field_names_from_column_name__Case_1() throws Exception {
		new SplitIndentifierIntoWordsTest()
				.identifier("TWO_WORDS")
				.words("two", "words")
				.test();
	}

	public void test__Should_generate_field_names_from_column_name__Case_2() throws Exception {
		new SplitIndentifierIntoWordsTest()
				.identifier("NOW_Three_Words")
				.words("now", "three", "words")
				.test();
	}

	public void test__Should_generate_field_names_from_column_name__Case_3() throws Exception {
		new SplitIndentifierIntoWordsTest()
				.identifier("camelCasedWords")
				.words("camel", "cased", "words")
				.test();
	}

	public void test__Should_generate_field_names_from_column_name__Case_4() throws Exception {
		new SplitIndentifierIntoWordsTest()
				.identifier("CamelCasedWords")
				.words("camel", "cased", "words")
				.test();
	}

	public void test__Should_generate_field_names_from_column_name__Case_5() throws Exception {
		new SplitIndentifierIntoWordsTest()
				.identifier("HTMLContent")
				.words("html", "content")
				.test();
	}

	public void test__Should_generate_field_names_from_column_name__Case_6() throws Exception {
		new SplitIndentifierIntoWordsTest()
				.identifier("contentHTML")
				.words("content", "html")
				.test();
	}

	public void test__Should_generate_field_names_from_column_name__Case_7() throws Exception {
		new SplitIndentifierIntoWordsTest()
				.identifier("mixed_underscored_andCamelCased")
				.words("mixed", "underscored", "andcamelcased")
				.test();
	}

	public void test__Should_generate_field_names_from_words__Case_1() throws Exception {
		String[] words = new String[] { "word" };
		Set<String> names = TextUtils.generateFieldNames(words);
		assertTrue(names.contains("word"));
		assertTrue(names.contains("mWord"));
	}

	/*public void test__Should_generate_field_names_from_column_name__When_one_word() throws Exception {
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
	}*/

	private class SplitIndentifierIntoWordsTest {
		private String mIdentifier;
		private String[] mWords;

		public SplitIndentifierIntoWordsTest identifier(String identifier) {
			mIdentifier = identifier;
			return this;
		}

		public SplitIndentifierIntoWordsTest words(String... words) {
			mWords = words;
			return this;
		}

		public void test() throws Exception {
			String[] words = TextUtils.splitIdentifierWords(mIdentifier);
			assertEquals(mWords.length, words.length);
			for (int i = 0; i < words.length; i++) {
				assertEquals(mWords[i], words[i]);
			}
		}
	}

}
