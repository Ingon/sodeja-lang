package org.sodeja.generator;

import static org.sodeja.functional.Maybe.just;
import static org.sodeja.functional.Maybe.nothing;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.sodeja.functional.Function1;
import org.sodeja.functional.Maybe;
import org.sodeja.functional.Predicate1;

public class Generators {
	public static Gen<Character> readerGenerator(final Reader reader) throws IOException {
		return new Gen<Character>(new GeneratorFunction<Character>() {
			@Override
			public Maybe<Character> execute() {
				try {
					int val = reader.read();
					if(val == -1) {
						return nothing();
					}
					return just((char) val);
				} catch(IOException exc) {
					throw new RuntimeException(exc);
				}
			}});
	}
	
	public static <T> List<T> readFully(Gen<T> generator) {
		List<T> vals = new ArrayList<T>();
		Gen<T> temp = generator;
		while(temp != null) {
			vals.add(temp.head());
			temp = temp.tail();
		}
		return vals;
	}
	
	public static <T> Gen<T> filter(final Gen<T> generator, final Predicate1<T> predicate) {
		return new Gen<T>(new GeneratorFunction<T>() {
			private Gen<T> internal = generator;
			
			@Override
			public Maybe<T> execute() {
				if(internal == null) {
					return nothing();
				}
				
				T value = internal.head();
				if(predicate.execute(value)) {
					internal = internal.tail();
					return just(value);
				}
				
				return execute();
			}});
	}
	
	public static <T, R> Gen<R> map(final Gen<T> generator, final Function1<R, T> functor) {
		return new Gen<R>(new GeneratorFunction<R>() {
			private Gen<T> internal = generator;
			
			@Override
			public Maybe<R> execute() {
				if(internal == null) {
					return nothing();
				}
				
				T value = internal.head();
				internal = internal.tail();
				
				return just(functor.execute(value));
			}});
	}
}
