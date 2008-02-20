package org.sodeja.generator;

import java.io.StringReader;
import java.util.List;

import org.sodeja.collections.ListUtils;

import junit.framework.TestCase;

public class GeneratorTest extends TestCase {
	public void testGenerator() {
		List<Character> vals = Generators2.readFully(Generators2.readerGenerator(new StringReader("123")));
		assertEquals(ListUtils.asList('1', '2', '3'), vals);
	}
}
