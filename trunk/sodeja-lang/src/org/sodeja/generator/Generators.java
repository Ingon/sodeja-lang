package org.sodeja.generator;

import java.io.IOException;
import java.io.Reader;

import org.sodeja.functional.Function0;

public class Generators {
	public static Generator<Character> readerGenerator(final Reader reader) throws IOException {
		int val = reader.read();
		if(val == -1) {
			return null;
		}
		Character value = Character.valueOf((char) val);
		return new Generator<Character>(value, new Function0<Character>() {
			@Override
			public Character execute() {
				try {
					return (char) reader.read();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}});
	}
}
