package ezvcard.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ezvcard.VCardDataType;
import ezvcard.ValidationWarnings;
import ezvcard.ValidationWarnings.WarningsGroup;
import ezvcard.types.VCardType;

/*
 Copyright (c) 2013, Michael Angstadt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met: 

 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer. 
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution. 

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies, 
 either expressed or implied, of the FreeBSD Project.
 */

/**
 * Utility classes for unit tests.
 * @author Michael Angstadt
 */
public class TestUtils {
	/**
	 * Asserts that a warnings list is a certain size.
	 * @param expectedSize the expected size of the warnings list
	 * @param warnings the warnings list
	 */
	public static void assertWarnings(int expectedSize, List<String> warnings) {
		assertEquals(warnings.toString(), expectedSize, warnings.size());
	}

	/**
	 * Asserts the sizes of each warnings list within a list of warnings lists.
	 * @param warningsLists the list of warnings lists
	 * @param expectedSizes the expected sizes of each warnings list
	 */
	public static void assertWarningsLists(List<List<String>> warningsLists, int... expectedSizes) {
		assertEquals(warningsLists.toString(), expectedSizes.length, warningsLists.size());

		for (int i = 0; i < expectedSizes.length; i++) {
			int expectedSize = expectedSizes[i];
			List<String> warnings = warningsLists.get(i);

			assertWarnings(expectedSize, warnings);
		}
	}

	/**
	 * Asserts that a validation warnings list is correct.
	 * @param warnings the warnings list
	 * @param expectedProperties the property objects that are expected to have
	 * warnings. The object should be added multiple times to this vararg
	 * parameter, depending on how many warnings it is expected to have (e.g. 3
	 * times for 3 warnings)
	 */
	public static void assertValidate(ValidationWarnings warnings, VCardType... expectedProperties) {
		Counts<VCardType> expectedCounts = new Counts<VCardType>();
		for (VCardType expectedProperty : expectedProperties) {
			expectedCounts.increment(expectedProperty);
		}

		Counts<Object> actualCounts = new Counts<Object>();
		for (WarningsGroup warning : warnings) {
			assertTrue(warning.getMessages().size() > 0);
			for (int i = 0; i < warning.getMessages().size(); i++) {
				VCardType property = warning.getProperty();
				actualCounts.increment(property);
			}
		}

		assertEquals(warnings.toString(), expectedCounts, actualCounts);
	}

	/**
	 * Asserts the value of an {@link Integer} object.
	 * @param expected the expected value
	 * @param actual the actual value
	 */
	public static void assertIntEquals(int expected, Integer actual) {
		assertEquals(Integer.valueOf(expected), actual);
	}

	/**
	 * Asserts the value of a single-valued jCard value.
	 * @param expectedDataType the expected data type
	 * @param expectedValue the expected value
	 * @param actualValue the actual jCard value
	 */
	public static void assertJCardValue(VCardDataType expectedDataType, String expectedValue, JCardValue actualValue) {
		assertEquals(expectedDataType, actualValue.getDataType());

		List<JsonValue> expected = Arrays.asList(new JsonValue(expectedValue));
		assertEquals(expected, actualValue.getValues());
	}

	/**
	 * Asserts the contents of a set.
	 * @param actualSet the actual set
	 * @param expectedElements the elements that are expected to be in the set
	 */
	public static <T> void assertSetEquals(Set<T> actualSet, T... expectedElements) {
		Set<T> expectedSet = new HashSet<T>(expectedElements.length);
		for (T expectedElement : expectedElements) {
			expectedSet.add(expectedElement);
		}
		assertEquals(expectedSet, actualSet);
	}

	/**
	 * Holds a list of test runs for a unit test.
	 */
	public static class Tests implements Iterable<Object[]> {
		private List<Object[]> tests = new ArrayList<Object[]>();

		/**
		 * Adds a test run.
		 * @param test the test data
		 * @return this
		 */
		public Tests add(Object... test) {
			tests.add(test);
			return this;
		}

		public Iterator<Object[]> iterator() {
			return tests.iterator();
		}
	}

	/**
	 * Keeps a count of how many identical instances of an object there are.
	 */
	private static class Counts<T> {
		private final Map<T, Integer> map = new HashMap<T, Integer>();

		public void increment(T t) {
			Integer value = getCount(t);
			map.put(t, value++);
		}

		public Integer getCount(T t) {
			Integer value = map.get(t);
			return (value == null) ? 0 : value;
		}

		@Override
		public int hashCode() {
			return map.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Counts<?> other = (Counts<?>) obj;
			return map.equals(other.map);
		}

		@Override
		public String toString() {
			return map.toString();
		}
	}

	private TestUtils() {
		//hide
	}
}
