package org.sodeja.generator;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.sodeja.collections.IteratorUtils;
import org.sodeja.functional.Maybe;
import static org.sodeja.functional.Maybe.*;

public class Generators2 {
	private Generators2() {
	}
	
	public static Generator2<Character> readerGenerator(final Reader reader) {
		return new Generator2<Character>(new GeneratorFunction<Character>() {
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
	
	public static <T> List<T> readFully(Generator2<T> generator) {
		List<T> vals = new ArrayList<T>();
		for(T t : IteratorUtils.wrapToIterable(generator)) {
			vals.add(t);
		}
		return vals;
	}
}
