package org.sodeja.collections;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;

public class ConsList<T> extends AbstractList<T> {
	
	public static <T> ConsList<T> createList(Collection<T> coll) {
		return createList(coll.iterator());
	}
	
	public static <T> ConsList<T> createList(Iterator<T> ite) {
		if(! ite.hasNext()) {
			return new NullConsList<T>();
		}
		
		return new ConsList<T>(ite.next(), createList(ite));
	}
	
	private T head;
	private ConsList<T> tail;
	
	private ConsList() {
	}
	
	public ConsList(T head) {
		this(head, new NullConsList<T>());
	}
	
	public ConsList(T head, ConsList<T> tail) {
		this.head = head;
		this.tail = tail;
	}
	
	@Override
	public T get(int index) {
		if(index == 0) {
			return head;
		}
		return tail.get(index - 1);
	}

	@Override
	public int size() {
		return 1 + tail.size();
	}
	
	public T getHead() {
		return head;
	}
	
	public ConsList<T> getTail() {
		return tail;
	}
	
	private static class NullConsList<T> extends ConsList<T> {
		@Override
		public T get(int index) {
			if(index >= 0) {
				throw new IndexOutOfBoundsException();
			}
			return null;
		}

		@Override
		public int size() {
			return 0;
		}
	}
}