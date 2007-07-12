package org.sodeja.collections;

import java.util.ArrayList;
import java.util.List;

import org.sodeja.functional.Function1;
import org.sodeja.functional.Predicate1;

import junit.framework.TestCase;

public class CollectionUtilsTest extends TestCase {
	public void testMap() {
		List<Integer> expected = ListUtils.asList(2, 4, 6);
		
		List<Integer> prepare = ListUtils.asList(1, 2, 3);
		List<Integer> actual = new ArrayList<Integer>();
		
		CollectionUtils.map(prepare, actual, new Function1<Integer, Integer>() {
			public Integer execute(Integer p) {
				return p * 2;
			}});
		
		assertEquals(expected, actual);
	}
	
	public void testFilter() {
		List<Integer> expected = ListUtils.asList(4, 5, 6);
		List<Integer> prepare = ListUtils.asList(1, 2, 3, 4, 5, 6);
		List<Integer> actual = new ArrayList<Integer>();
		
		CollectionUtils.filter(prepare, actual, new Predicate1<Integer>() {
			public Boolean execute(Integer p) {
				return p > 3;
			}});
		
		assertEquals(expected, actual);
	}
}
