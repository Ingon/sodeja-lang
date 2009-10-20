package org.sodeja.collections;

import org.sodeja.lang.Range;

import junit.framework.TestCase;

public class ArrayUtilsTest extends TestCase {
	public void testDup() {
		String[] src = new String[0];
		String[] res = ArrayUtils.dup(src);
		
		assertEquals(0, res.length);
		
		src = new String[] {"a", "b", "c"};
		res = ArrayUtils.dup(src);
		
		assertEquals(src.length * 2, res.length);
		for(Integer i : Range.of(src)) {
			assertEquals(src[i], res[i * 2]);
			assertEquals(src[i], res[i * 2 + 1]);
		}
		
		String[][] dres = ArrayUtils.unmapTuples(res, 2);
		for(Integer i : Range.of(src)) {
			assertEquals(src[i], dres[0][i]);
			assertEquals(src[i], dres[1][i]);
		}
	}
}
