package org.sodeja.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

public class PersistentSet<E> extends AbstractSet<E> {
	private final PersistentMap<E, Object> backend;
	private static final Object INTERNAL_VALUE = new Object();
	
	public PersistentSet() {
		this.backend = new PersistentMap<E, Object>();
	}
	
	private PersistentSet(PersistentMap<E, Object> backend) {
		this.backend = backend;
	}
	
	@Override
	public Iterator<E> iterator() {
		return backend.keySet().iterator();
	}

	@Override
	public int size() {
		return backend.size();
	}

	public PersistentSet<E> addValue(E e) {
		return new PersistentSet<E>(backend.putValue(e, INTERNAL_VALUE));
	}

	// TODO innefective
	public PersistentSet<E> addAllValues(Collection<E> values) {
		PersistentMap<E, Object> c = backend;
		for(E e : values) {
			c = c.putValue(e, INTERNAL_VALUE);
		}
		return new PersistentSet<E>(c);
	}
	
	public PersistentSet<E> removeValue(E e) {
		return new PersistentSet<E>(backend.removeValue(e));
	}
}
