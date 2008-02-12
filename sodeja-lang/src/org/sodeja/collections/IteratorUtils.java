package org.sodeja.collections;

import java.util.Iterator;

import org.sodeja.functional.Function1;

public class IteratorUtils {
	private IteratorUtils() {
	}
	
	public static <P, R> Iterator<R> apply(final Iterator<P> orig, final Function1<R, P> functor) {
		return new Iterator<R>() {
			@Override
			public boolean hasNext() {
				return orig.hasNext();
			}

			@Override
			public R next() {
				return functor.execute(orig.next());
			}

			@Override
			public void remove() {
				orig.remove();
			}};
	}
	
	public static <P, R> Iterator<R> filter(final Iterator<P> orig, final Function1<Boolean, P> functor) {
		return new Iterator<R>() {
			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public R next() {
				return null;
			}

			@Override
			public void remove() {
			}};
	}
}
