package org.sodeja.collections;

import java.util.List;

import org.sodeja.functional.Function1;

import junit.framework.TestCase;

public class IteratorUtilsTest extends TestCase {
	public void testApply() {
		List<Integer> vals = ListUtils.asList(1, 2, 3, 4);
		List<Integer> nvals = ListUtils.asList(IteratorUtils.apply(vals.iterator(), new Function1<Integer, Integer>() {
			@Override
			public Integer execute(Integer p) {
				return p * 2;
			}}));
		
		assertEquals(ListUtils.asList(2, 4, 6, 8), nvals);
	}

	public void testFilter() {
		List<Integer> vals = ListUtils.asList(1, 2, 3, 4);
		List<Integer> nvals = ListUtils.asList(IteratorUtils.filter(vals.iterator(), new Function1<Boolean, Integer>() {
			@Override
			public Boolean execute(Integer p) {
				return p < 3;
			}}));
		
		assertEquals(ListUtils.asList(1, 2), nvals);
	}
}
