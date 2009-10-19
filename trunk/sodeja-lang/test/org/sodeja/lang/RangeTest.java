package org.sodeja.lang;

public class RangeTest {
	public static void main(String[] args) {
		for(Integer i : new Range(20, 50)) {
			System.out.println("I: " + i);
		}
	}
}
