package org.sodeja.collections;

import junit.framework.TestCase;

public class PersistentTest extends TestCase {
	public void testGet() {
		PersistentMap<String, String> map = new PersistentMap<String, String>();
		assertNull(map.get("a"));
	}
}
