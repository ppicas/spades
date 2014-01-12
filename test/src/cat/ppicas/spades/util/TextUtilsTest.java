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

package cat.ppicas.spades.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.Set;

import cat.ppicas.spades.util.TextUtils;

import junit.framework.TestCase;

public class TextUtilsTest extends TestCase {

	public void test__Should_split_words_from_identifier__Case_1() throws Exception {
		String identifier = "TWO_WORDS";
		String[] words = TextUtils.splitIdentifierWords(identifier );
		assertThat(words, arrayContainingInAnyOrder("two", "words"));
	}

	public void test__Should_split_words_from_identifier__Case_2() throws Exception {
		String identifier = "NOW_Three_Words";
		String[] words = TextUtils.splitIdentifierWords(identifier );
		assertThat(words, arrayContainingInAnyOrder("now", "three", "words"));
	}

	public void test__Should_split_words_from_identifier__Case_3() throws Exception {
		String identifier = "camelCasedWords";
		String[] words = TextUtils.splitIdentifierWords(identifier );
		assertThat(words, arrayContainingInAnyOrder("camel", "cased", "words"));
	}

	public void test__Should_split_words_from_identifier__Case_4() throws Exception {
		String identifier = "CamelCasedWords";
		String[] words = TextUtils.splitIdentifierWords(identifier );
		assertThat(words, arrayContainingInAnyOrder("camel", "cased", "words"));
	}

	public void test__Should_split_words_from_identifier__Case_5() throws Exception {
		String identifier = "HTMLContent";
		String[] words = TextUtils.splitIdentifierWords(identifier );
		assertThat(words, arrayContainingInAnyOrder("html", "content"));
	}

	public void test__Should_split_words_from_identifier__Case_6() throws Exception {
		String identifier = "contentHTML";
		String[] words = TextUtils.splitIdentifierWords(identifier );
		assertThat(words, arrayContainingInAnyOrder("content", "html"));
	}

	public void test__Should_split_words_from_identifier__Case_7() throws Exception {
		String identifier = "mixed_underscored_andCamelCased";
		String[] words = TextUtils.splitIdentifierWords(identifier );
		assertThat(words, arrayContainingInAnyOrder("mixed", "underscored", "andcamelcased"));
	}

	public void test__Should_generate_field_names_from_words__When_one_word() throws Exception {
		String[] words = givenWords("word");
		Set<String> fieldNames = TextUtils.generateFieldNames(words);
		assertThat(fieldNames, containsInAnyOrder("mWord", "word", "Word", "WORD"));
	}

	public void test__Should_generate_field_names_from_words__When_two_words() throws Exception {
		String[] words = givenWords("two", "words");
		Set<String> fieldNames = TextUtils.generateFieldNames(words);
		assertThat(fieldNames, containsInAnyOrder("mTwoWords", "twoWords", "TwoWords",
				"twowords", "two_words", "Two_Words", "TWO_WORDS"));
	}

	public void test__Should_generate_field_names_from_words__When_prefixed_with_is() throws Exception {
		String[] words = givenWords("is", "boolean");
		Set<String> fieldNames = TextUtils.generateFieldNames(words);
		assertThat(fieldNames, containsInAnyOrder("mIsBoolean", "isBoolean", "IsBoolean",
				"isboolean", "is_boolean", "Is_Boolean", "IS_BOOLEAN", "mBoolean",
				"boolean", "Boolean", "BOOLEAN"));
	}

	public void test__Should_generate_field_names_from_words__When_suffixed_with_id() throws Exception {
		String[] words = givenWords("object", "id");
		Set<String> fieldNames = TextUtils.generateFieldNames(words);
		assertThat(fieldNames, containsInAnyOrder("mObjectId", "objectId", "ObjectId",
				"objectid", "object_id", "Object_Id", "OBJECT_ID", "mObject",
				"object", "Object", "OBJECT"));
	}

	private String[] givenWords(String... words) {
		return words;
	}

}
