package org.sodeja.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

public class PersistentSet<E> extends AbstractSet<E> {
	private final PersistentMap<E, Object> map;
	private static final Object INTERNAL_VALUE = new Object();
	
	public PersistentSet() {
		this.map = new PersistentMap<E, Object>();
	}
	
	private PersistentSet(PersistentMap<E, Object> map) {
		this.map = map;
	}
	
	@Override
	public Iterator<E> iterator() {
		return map.keySet().iterator();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	public PersistentSet<E> addValue(E e) {
		return new PersistentSet<E>(map.putValue(e, INTERNAL_VALUE));
	}

	// TODO innefective
	public PersistentSet<E> addAllValues(Collection<E> values) {
		PersistentMap<E, Object> c = map;
		for(E e : values) {
			c = c.putValue(e, INTERNAL_VALUE);
		}
		return new PersistentSet<E>(c);
	}
	
	public PersistentSet<E> removeValue(E e) {
		return new PersistentSet<E>(map.removeValue(e));
	}
}
