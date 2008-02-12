package org.sodeja.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
	
	private static final Object NOP = new Object();
	private static final Object END = new Object();
	
	public static <P> Iterator<P> filter(final Iterator<P> orig, final Function1<Boolean, P> functor) {
		return new Iterator<P>() {
			private Object value = NOP;
			
			@Override
			public boolean hasNext() {
				if(value == END) {
					return false;
				}
					
				if(value != NOP) {
					return true;
				}
				
				
				while(orig.hasNext()) {
					P temp = orig.next();
					if(functor.execute(temp)) {
						value = temp;
						return true;
					}
				}
				
				value = END;
				return false;
			}

			@SuppressWarnings("unchecked")
			@Override
			public P next() {
				if(value == END) {
					throw new NoSuchElementException();
				}
				
				if(value != NOP) {
					P temp = (P) value;
					value = NOP;
					return temp;
				}
				
				while(orig.hasNext()) {
					P temp = orig.next();
					if(functor.execute(temp)) {
						return temp;
					}
				}
				
				value = END;
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}};
	}
}
