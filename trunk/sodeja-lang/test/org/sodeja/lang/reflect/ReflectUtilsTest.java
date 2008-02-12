package org.sodeja.lang.reflect;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JFrame;

import junit.framework.TestCase;

public class ReflectUtilsTest extends TestCase {
	public void testFieldAccess() {
		T2 val = new T2();
		val.setVal1("alabala");
		val.setVal2(true);
		
		assertEquals("alabala", ReflectUtils.getFieldValue(val, "val1"));
		assertEquals(true, ReflectUtils.getFieldValue(val, "val2"));
		
		assertEquals("alabala", ReflectUtils.getUsingMethod(val, "val1"));
		assertEquals(true, ReflectUtils.getUsingMethod(val, "val2"));
		
		ReflectUtils.setFieldValue(val, "val1", "al");
		ReflectUtils.setFieldValue(val, "val2", false);

		assertEquals("al", ReflectUtils.getFieldValue(val, "val1"));
		assertEquals(false, ReflectUtils.getFieldValue(val, "val2"));
		
		assertEquals("al", ReflectUtils.getUsingMethod(val, "val1"));
		assertEquals(false, ReflectUtils.getUsingMethod(val, "val2"));

		ReflectUtils.setUsingMethod(val, "val1", "al1");
		ReflectUtils.setUsingMethod(val, "val2", true);

		assertEquals("al1", ReflectUtils.getFieldValue(val, "val1"));
		assertEquals(true, ReflectUtils.getFieldValue(val, "val2"));
		
		assertEquals("al1", ReflectUtils.getUsingMethod(val, "val1"));
		assertEquals(true, ReflectUtils.getUsingMethod(val, "val2"));
	}
	
	public void testHierarchyIterator() {
		System.out.println("THI: Limitless");
		for(Class clazz : ReflectUtils.hierarchyIterable(JFrame.class)) {
			System.out.println("Class: " + clazz.getName());
		}

		System.out.println("THI: Limit");
		for(Class clazz : ReflectUtils.hierarchyIterable(JFrame.class, Container.class)) {
			System.out.println("Class: " + clazz.getName());
		}
	}
	
	public void testFieldsIterator() {
		System.out.println("TFI: Limitless");
		for(Field fld : ReflectUtils.fieldsIterable(JFrame.class)) {
			System.out.println(fld.getDeclaringClass().getName() + ": " + fld.getName());
		}
		
		System.out.println("TFI: Limit");
		for(Field fld : ReflectUtils.fieldsIterable(JFrame.class, Container.class)) {
			System.out.println(fld.getDeclaringClass().getName() + ": " + fld.getName());
		}
	}
	
	public void testFindBestMethod() {
		Method method = ReflectUtils.findBestMethod(Component.class, "addFocusListener", new Class[] {FocusListener.class});
		System.out.println("Method: " + method);
		
		method = ReflectUtils.findBestMethod(Component.class, "addFocusListener", new Class[] {FocusAdapter.class});
		System.out.println("Method: " + method);

		method = ReflectUtils.findBestMethod(T1.class, "add", new Class[] {Integer.class});
		System.out.println("Method: " + method);

		method = ReflectUtils.findBestMethod(T1.class, "add", new Class[] {Double.class});
		System.out.println("Method: " + method);

		method = ReflectUtils.findBestMethod(T1.class, "add", new Class[] {Integer.class, Integer.class});
		System.out.println("Method: " + method);

		method = ReflectUtils.findBestMethod(T1.class, "add", new Class[] {null, Integer.class});
		System.out.println("Method: " + method);
		
		method = ReflectUtils.findBestMethod(T1.class, "add", new Class[] {int.class});
		System.out.println("Method: " + method);

		method = ReflectUtils.findBestMethod(T1.class, "add", new Class[] {double.class});
		System.out.println("Method: " + method);
	}
	
	private static class TempFocusListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {
		}

		@Override
		public void focusLost(FocusEvent e) {
		}
	}
	
	private static class T1 {
		public void add(int a) {
		}
		
		public void add(Number a) {
		}
		
		public void add(Integer a) {
		}
		
		public void add(Number a, Integer b) {
		}
		
		public void add(Integer a, Number b) {
		}
	}
	
	private static class T2 {
		private String val1;
		private boolean val2;
		
		public String getVal1() {
			return val1;
		}
		
		public void setVal1(String val1) {
			this.val1 = val1;
		}
		
		public boolean isVal2() {
			return val2;
		}
		
		public void setVal2(boolean val2) {
			this.val2 = val2;
		}
	}
}
