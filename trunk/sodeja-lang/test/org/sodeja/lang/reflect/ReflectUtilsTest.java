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
	
	public void testFindBestMatch() {
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
		public void add(Number a) {
		}
		
		public void add(Integer a) {
		}
		
		public void add(Number a, Integer b) {
		}
		
		public void add(Integer a, Number b) {
		}
	}
}
