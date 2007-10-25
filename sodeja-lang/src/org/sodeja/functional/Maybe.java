package org.sodeja.functional;

public abstract class Maybe<T> {
	private static class Just<T> extends Maybe<T> {
		public final T value;
		public Just(T value) {
			this.value = value;
		}
		
		public T value() {
			return value;
		}

		@Override
		public boolean hasValue() {
			return true;
		}
	}
	
	private static class Nothing<T> extends Maybe<T> {
		@Override
		public T value() {
			throw new IllegalArgumentException("Cannot extract value from Nothing");
		}

		@Override
		public boolean hasValue() {
			return false;
		}
	}
	
	private Maybe() {
	}
	
	public abstract T value();
	
	public abstract boolean hasValue();
	
	public static <T> Maybe<T> just(T value) {
		return new Just<T>(value);
	}
	
	public static <T> Maybe<T> nothing() {
		return new Nothing<T>();
	}
}
