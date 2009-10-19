package org.sodeja.lang;

import java.util.Iterator;
import java.util.List;

import org.sodeja.functional.Pair;

public class Range extends Pair<Integer, Integer> implements Iterable<Integer> {
	public Range(Integer from, Integer to) {
		super(from, to);
		if(from > to) {
			throw new RuntimeException("From must be smaller then to");
		}
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {
			private Integer current = null;
			
			@Override
			public boolean hasNext() {
				if(current == null) {
					current = first;
					return true;
				}
				
				Integer temp = current + 1;
				if(temp >= second) {
					return false;
				}
				
				current = temp;
				return true;
			}

			@Override
			public Integer next() {
				return current;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	public static <T> Range of(T[] arr) {
		return new Range(0, arr.length);
	}

	public static <T> Range of(List<T> arr) {
		return new Range(0, arr.size());
	}
}
