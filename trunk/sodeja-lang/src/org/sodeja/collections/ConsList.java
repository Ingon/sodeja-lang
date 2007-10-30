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
	
	protected T head;
	protected ConsList<T> tail;
	
	protected ConsList() {
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
		return tail().get(index - 1);
	}

	@Override
	public int size() {
		return 1 + tail().size();
	}
	
	public T head() {
		return head;
	}
	
	public ConsList<T> tail() {
		return tail;
	}
	
	public void setHead(T head) {
		this.head = head;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
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

		@Override
		public boolean isEmpty() {
			return true;
		}
	}
}
