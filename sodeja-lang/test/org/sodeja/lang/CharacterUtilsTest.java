package org.sodeja.lang;

import junit.framework.TestCase;

public class CharacterUtilsTest extends TestCase {
	public void testConvertSingle() {
		assertEquals(0, CharacterUtils.convert('0'));
		assertEquals(1, CharacterUtils.convert('1'));
		assertEquals(2, CharacterUtils.convert('2'));
		assertEquals(3, CharacterUtils.convert('3'));
		assertEquals(4, CharacterUtils.convert('4'));
		assertEquals(5, CharacterUtils.convert('5'));
		assertEquals(6, CharacterUtils.convert('6'));
		assertEquals(7, CharacterUtils.convert('7'));
		assertEquals(8, CharacterUtils.convert('8'));
		assertEquals(9, CharacterUtils.convert('9'));
		
		assertEquals(10, CharacterUtils.convert('a'));
		assertEquals(11, CharacterUtils.convert('b'));
		assertEquals(12, CharacterUtils.convert('c'));
		assertEquals(13, CharacterUtils.convert('d'));
		assertEquals(14, CharacterUtils.convert('e'));
		assertEquals(15, CharacterUtils.convert('f'));

		assertEquals(10, CharacterUtils.convert('A'));
		assertEquals(11, CharacterUtils.convert('B'));
		assertEquals(12, CharacterUtils.convert('C'));
		assertEquals(13, CharacterUtils.convert('D'));
		assertEquals(14, CharacterUtils.convert('E'));
		assertEquals(15, CharacterUtils.convert('F'));
	}
	
	public void testIsHexadecimal() {
		assertTrue(CharacterUtils.isHexadecimal('0'));
		assertTrue(CharacterUtils.isHexadecimal('1'));
		assertTrue(CharacterUtils.isHexadecimal('2'));
		assertTrue(CharacterUtils.isHexadecimal('3'));
		assertTrue(CharacterUtils.isHexadecimal('4'));
		assertTrue(CharacterUtils.isHexadecimal('5'));
		assertTrue(CharacterUtils.isHexadecimal('6'));
		assertTrue(CharacterUtils.isHexadecimal('7'));
		assertTrue(CharacterUtils.isHexadecimal('8'));
		assertTrue(CharacterUtils.isHexadecimal('9'));
		
		assertTrue(CharacterUtils.isHexadecimal('a'));
		assertTrue(CharacterUtils.isHexadecimal('b'));
		assertTrue(CharacterUtils.isHexadecimal('c'));
		assertTrue(CharacterUtils.isHexadecimal('d'));
		assertTrue(CharacterUtils.isHexadecimal('e'));
		assertTrue(CharacterUtils.isHexadecimal('f'));

		assertTrue(CharacterUtils.isHexadecimal('A'));
		assertTrue(CharacterUtils.isHexadecimal('B'));
		assertTrue(CharacterUtils.isHexadecimal('C'));
		assertTrue(CharacterUtils.isHexadecimal('D'));
		assertTrue(CharacterUtils.isHexadecimal('E'));
		assertTrue(CharacterUtils.isHexadecimal('F'));
		
		assertFalse(CharacterUtils.isHexadecimal('j'));
	}
	
	public void testConvert() {
		assertEquals('\\', (char) CharacterUtils.convertToUnicode(new Character[] {'0', '0', '5', 'c'}));
		assertEquals('Z', (char) CharacterUtils.convertToUnicode(new Character[] {'0', '0', '5', 'a'}));
		assertEquals('m', (char) CharacterUtils.convertToUnicode(new Character[] {'0', '0', '6', 'D'}));
	}
}

