package org.sodeja.math;

import junit.framework.TestCase;

public class RationalTest extends TestCase {
	public void testParse() {
		Rational rat = new Rational("5");
		assertEquals("5/1", rat.toString());
		assertEquals(5, rat.longValue());

		rat = new Rational("0.5");
		assertEquals("1/2", rat.toString());
		assertEquals(0.5, rat.doubleValue());

		rat = new Rational("0.005");
		assertEquals("1/200", rat.toString());
		
		rat = new Rational("1/2");
		assertEquals("1/2", rat.toString());
	}
	
	public void testAdd() {
		Rational rat1 = new Rational("0.5");
		Rational rat2 = new Rational("0.2");
		
		assertEquals("7/10", rat1.add(rat2).toString());
	}

	public void testSubstract() {
		Rational rat1 = new Rational("0.5");
		Rational rat2 = new Rational("0.2");
		
		assertEquals("3/10", rat1.substract(rat2).toString());
	}
	
	public void testMultiply() {
		Rational rat1 = new Rational("1.5");
		Rational rat2 = new Rational("0.4");
		
		assertEquals("3/5", rat1.multiply(rat2).toString());
	}

	public void testDivide() {
		Rational rat1 = new Rational("1.5");
		Rational rat2 = new Rational("0.4");
		
		assertEquals("15/4", rat1.divide(rat2).toString());
	}
}
