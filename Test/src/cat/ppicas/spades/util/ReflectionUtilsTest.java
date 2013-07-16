package cat.ppicas.spades.util;

import java.lang.reflect.Field;
import java.util.Arrays;

import junit.framework.TestCase;

public class ReflectionUtilsTest extends TestCase {

	public void test__Should_find_a_matching_field__When_field_exists_in_the_passed_class() throws Exception {
		Field field = ReflectionUtils.findField(Bar.class,
				Arrays.asList("invalidField", "barField"));
		assertEquals("barField", field.getName());
	}

	public void test__Should_find_a_matching_field__When_field_exists_in_extended_class() throws Exception {
		Field field = ReflectionUtils.findField(Bar.class,
				Arrays.asList("invalidField", "fooField"));
		assertEquals("fooField", field.getName());
	}

	public void test__Should_find_a_matching_field__When_the_order_names_is_important() throws Exception {
		Field field = ReflectionUtils.findField(Bar.class,
				Arrays.asList("fooField", "barField2", "barField"));
		assertEquals("fooField", field.getName());
	}

	public void test__Should_find_a_matching_field__When_field_not_exists() throws Exception {
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
