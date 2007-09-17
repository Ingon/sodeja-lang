package org.sodeja.collections;

import java.util.ArrayList;
import java.util.List;

import org.sodeja.functional.Function1;
import org.sodeja.functional.IdentityFunction;
import org.sodeja.functional.Predicate1;

import junit.framework.TestCase;

public class ListUtilsTest extends TestCase {
	private static final Predicate1<Integer> BIGGER_THEN_FIVE = new Predicate1<Integer>() {
		public Boolean execute(Integer p) {
			return p > 5;
		}
	};

	public void testCollectItems() {
		List<Integer> expected = ListUtils.asList(2, 4, 6);
		
		List<Integer> prepare = ListUtils.asList(1, 2, 3);
		List<Integer> actual = ListUtils.collectItems(prepare, new Function1<Integer, Integer>() {
			public Integer execute(Integer p) {
				return p * 2;
			}});
		
		assertEquals(expected, actual);
	}
	
	public void testAsListEmpty() {
		List<Object> list = ListUtils.asList();
		assertNotNull(list);
		assertEquals(0, list.size());
	}
	
	public void testAsList() {
		List<Integer> expected = new ArrayList<Integer>();
		expected.add(1);
		expected.add(2);
		expected.add(3);
		
		List<Integer> actual = ListUtils.asList(1, 2, 3);
		
		assertEquals(expected, actual);
	}
	
	public void testAny() {
		assertFalse(ListUtils.any(ListUtils.asList(1, 2, 3, 4), BIGGER_THEN_FIVE));
		assertTrue(ListUtils.any(ListUtils.asList(1, 3, 5, 7), BIGGER_THEN_FIVE));
		assertTrue(ListUtils.any(ListUtils.asList(6, 7, 8, 9), BIGGER_THEN_FIVE));
	}
	
	public void testAll() {
		assertFalse(ListUtils.all(ListUtils.asList(1, 2, 3, 4), BIGGER_THEN_FIVE));
		assertFalse(ListUtils.all(ListUtils.asList(1, 3, 5, 7), BIGGER_THEN_FIVE));
		assertTrue(ListUtils.all(ListUtils.asList(6, 7, 8, 9), BIGGER_THEN_FIVE));
	}
	
	public void testSum() {
		assertEquals(1 + 2 + 3, ListUtils.sum(ListUtils.asList(1, 2, 3), new IdentityFunction<Integer>()));
	}
	
	public void testFlattern() {
		assertEquals(0, ListUtils.flattern(new ArrayList<List<Integer>>()).size());
		List<Integer> first = ListUtils.asList(1, 2);
		List<Integer> second = ListUtils.asList(3, 4);
		List<List<Integer>> req = ListUtils.asList(first, second);
		assertEquals(ListUtils.asList(1, 2, 3, 4), ListUtils.flattern(req));
	}
}
