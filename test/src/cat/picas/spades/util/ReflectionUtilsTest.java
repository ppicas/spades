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

package cat.picas.spades.util;

import java.lang.reflect.Field;
import java.util.Arrays;

import junit.framework.TestCase;

public class ReflectionUtilsTest extends TestCase {

	public void test__Should_find_a_matching_field__When_field_exists_in_extended_class() throws Exception {
		Field field = ReflectionUtils.findField(Bar.class, "barField");
		assertEquals("barField", field.getName());
	}

	public void test__Should_find_a_matching_field__When_field_not_exists() throws Exception {
		Field field = ReflectionUtils.findField(Bar.class, "invalidField");
		assertNull(field);
	}

	public void test__Should_find_a_matching_from_fields__When_field_exists_in_the_passed_class() throws Exception {
		Field field = ReflectionUtils.findField(Bar.class,
				Arrays.asList("invalidField", "barField"));
		assertEquals("barField", field.getName());
	}

	public void test__Should_find_a_matching_from_fields__When_field_exists_in_extended_class() throws Exception {
		Field field = ReflectionUtils.findField(Bar.class,
				Arrays.asList("invalidField", "fooField"));
		assertEquals("fooField", field.getName());
	}

	public void test__Should_find_a_matching_from_fields__When_field_not_exists() throws Exception {
		Field field = ReflectionUtils.findField(Bar.class,
				Arrays.asList("invalidField", "invalidField2"));
		assertNull(field);
	}

	private static class Foo {
		@SuppressWarnings("unused")
		private int fooField;
	}

	private static class Bar extends Foo {
		@SuppressWarnings("unused")
		private int barField;

		@SuppressWarnings("unused")
		private int barField2;
	}

}
