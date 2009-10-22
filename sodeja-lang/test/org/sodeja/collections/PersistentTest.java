package org.sodeja.collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class PersistentTest {
	public static void testGet() {
		PersistentMap<String, String> map = new PersistentMap<String, String>();
//		assertNull(map.get("a"));
		
		map.putValue("a", "b");
//		assertNull(map.get("a"));
		
		map = map.putValue("a", "b");
//		assertEquals("b", map.get("a"));

		map = map.putValue("c", "d");
//		assertEquals("d", map.get("c"));
	}

	public static void testMulty() {
		PersistentMap<String, String>[] maps = new PersistentMap[1000];
		maps[0] = new PersistentMap<String, String>();
		for(int i = 1;i < maps.length; i++) {
			maps[i] = maps[i - 1].putValue("a" + i, "b" + i);
			if(maps[i].size() != i) {
				System.out.println("Wrong size on: " + i);
			}
		}
		
		for(int i = 1; i < maps.length; i++) {
			PersistentMap<String, String> map = maps[i];
			for(int j = 1; j < i + 1; j++) {
				String key = "a" + j;
				String value = "b" + j;
				if(! value.equals(map.get(key))) {
					System.out.println("Missing or wrong value for key: " + key);
				}
			}
			for(int j = i + 1; j < maps.length; j++) {
				String key = "a" + j;
				String value = "b" + j;
				if(value.equals(map.get(key))) {
					System.out.println("Mutated original for key: " + key);
				}
			}
		}
		
		System.out.println(maps[maps.length - 1]);
	}
	
	public static void main(String[] args) throws Exception {
//		testMulty();
		testPerformance();
//		testIterator();
		testIteratorPerformance();
//		testPutAll();
		testPutAllPerformance();
	}

	private static final int GEN_SIZE = 200000;
	private static void testPerformance() throws Exception {
		System.out.println("Hash: " + testHash());
		System.out.println("Tree: " + testTree());
		System.out.println("Pers: " + testPersistent());
//		System.out.println("CPers: " + testCPersistent());
	}
	
	private static long testHash() {
		long start = System.currentTimeMillis();
		Set<String> set = new HashSet<String>();
		for(int i = 0; i < GEN_SIZE; i++) {
			set.add("data" + i);
		}
		for(int i = 0; i < GEN_SIZE; i++) {
			set.contains("data" + i);
		}
		for(int i = 0; i < GEN_SIZE; i++) {
			set.remove("data" + i);
		}
		long end = System.currentTimeMillis();
		return (end - start);
	}

	private static long testTree() {
		long start = System.currentTimeMillis();
		Set<String> set = new TreeSet<String>();
		for(int i = 0; i < GEN_SIZE; i++) {
			set.add("data" + i);
		}
		for(int i = 0; i < GEN_SIZE; i++) {
			set.contains("data" + i);
		}
		for(int i = 0; i < GEN_SIZE; i++) {
			set.remove("data" + i);
		}
		long end = System.currentTimeMillis();
		return (end - start);
	}
	
	private static long testPersistent() {
		long start = System.currentTimeMillis();
		PersistentSet<String> set = new PersistentSet<String>();
		for(int i = 0; i < GEN_SIZE; i++) {
			set = set.addValue("data" + i);
		}
		for(int i = 0; i < GEN_SIZE; i++) {
			set.contains("data" + i);
		}
		for(int i = 0; i < GEN_SIZE; i++) {
			set = set.removeValue("data" + i);
		}
		long end = System.currentTimeMillis();
		return (end - start);
	}
	
//	private static long testCPersistent() throws Exception {
//		long start = System.currentTimeMillis();
//		IPersistentSet set = PersistentHashSet.EMPTY;
//		for(int i = 0; i < GEN_SIZE; i++) {
//			set = (IPersistentSet) set.cons("data" + i);
//		}
//		for(int i = 0; i < GEN_SIZE; i++) {
//			set.contains("data" + i);
//		}
//		for(int i = 0; i < GEN_SIZE; i++) {
//			set = (IPersistentSet) set.disjoin("data" + i);
//		}
//		long end = System.currentTimeMillis();
//		return (end - start);
//	}
	
	private static void testIterator() {
		PersistentMap<String, String>[] maps = new PersistentMap[30];
		maps[0] = new PersistentMap<String, String>();
		for(int i = 1;i < maps.length; i++) {
			maps[i] = maps[i - 1].putValue("a" + i, "b" + i);
		}

		int sz = 0;
		PersistentMap<String, String> last = maps[maps.length - 1];
		for(Map.Entry<String, String> e : last.entrySet()) {
			sz++;
			System.out.println("K: " + e.getKey() + " E: " + e.getValue());
		}
		if(sz != maps.length - 1) {
			System.out.println("NOT SAME");
		}
	}

	private static void testIteratorPerformance() {
		System.out.println("iHash: " + testiHash());
		System.out.println("iTree: " + testiTree());
		System.out.println("iPers: " + testiPersistent());
	}
	
	private static long testiHash() {
		long start = System.currentTimeMillis();
		Set<String> set = new HashSet<String>();
		for(int i = 0; i < GEN_SIZE; i++) {
			set.add("data" + i);
		}
		Set<String> nset = new HashSet<String>();
		for(String s : set) {
			nset.add(s);
		}
		long end = System.currentTimeMillis();
		return (end - start);
	}
	
	private static long testiTree() {
		long start = System.currentTimeMillis();
		Set<String> set = new TreeSet<String>();
		for(int i = 0; i < GEN_SIZE; i++) {
			set.add("data" + i);
		}
		Set<String> nset = new TreeSet<String>();
		for(String s : set) {
			nset.add(s);
		}
		long end = System.currentTimeMillis();
		return (end - start);
	}
	
	private static long testiPersistent() {
		long start = System.currentTimeMillis();
		PersistentSet<String> set = new PersistentSet<String>();
		for(int i = 0; i < GEN_SIZE; i++) {
			set = set.addValue("data" + i);
		}
		PersistentSet<String> nset = new PersistentSet<String>();
		for(String s : set) {
			nset = nset.addValue(s);
		}
		long end = System.currentTimeMillis();
		return (end - start);
	}

	private static void testPutAll() {
		Set<String> set = new HashSet<String>();
		for(int i = 0; i < GEN_SIZE; i++) {
			set.add("data" + i);
		}
		
		long start = System.currentTimeMillis();
		PersistentSet<String> pset = new PersistentSet<String>();
		for(String s : set) {
			pset = pset.addValue(s);
		}
		long end = System.currentTimeMillis();
		System.out.println("Single add: " + (end - start));

		start = System.currentTimeMillis();
		PersistentSet<String> piset = new PersistentSet<String>();
		piset = piset.addAllValues(set);
		end = System.currentTimeMillis();
		System.out.println("Multy add: " + (end - start));
		
		System.out.println("EQ: " + (pset.size() == piset.size()));
	}

	private static void testPutAllPerformance() {
		Set<String> set = new HashSet<String>();
		for(int i = 0; i < GEN_SIZE; i++) {
			set.add("data" + i);
		}
		System.out.println("pHash: " + testpHash(set));
		System.out.println("pTree: " + testpTree(set));
		System.out.println("pPers: " + testpPersistent(set));
//		System.out.println("pCPers: " + testpCPersistent(set));
	}

	private static long testpHash(Set<String> set) {
		long start = System.currentTimeMillis();
		Set<String> hset = new HashSet<String>();
		hset.addAll(set);
		for(Iterator ite = hset.iterator(); ite.hasNext(); ) {
			ite.next();
		}
		long end = System.currentTimeMillis();
		return (end - start);
	}

	private static long testpTree(Set<String> set) {
		long start = System.currentTimeMillis();
		Set<String> hset = new TreeSet<String>();
		hset.addAll(set);
		for(Iterator ite = hset.iterator(); ite.hasNext(); ) {
			ite.next();
		}
		long end = System.currentTimeMillis();
		return (end - start);
	}

	private static long testpPersistent(Set<String> set) {
		long start = System.currentTimeMillis();
		PersistentSet<String> hset = new PersistentSet<String>();
		hset.addAllValues(set);
		for(Iterator ite = hset.iterator(); ite.hasNext(); ) {
			ite.next();
		}
		long end = System.currentTimeMillis();
		return (end - start);
	}

//	private static long testpCPersistent(Set<String> set) {
//		long start = System.currentTimeMillis();
//		PersistentHashSet hset = PersistentHashSet.create(set);
//		for(Iterator ite = hset.iterator(); ite.hasNext(); ) {
//			ite.next();
//		}
//		long end = System.currentTimeMillis();
//		return (end - start);
//	}
}
