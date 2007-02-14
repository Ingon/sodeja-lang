package org.sodeja.lang.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.sodeja.lang.StringUtils;

public final class ReflectUtils {
	private ReflectUtils() {
	}
	
    public static final Class[] EMPTY_TYPES = new Class[0];
    public static final Object[] EMPTY_PARAMETERS = new Object[0];
    
    public static Object getUsingMethod(Object obj, String fieldName) {
        String methodName = "get" + StringUtils.capitalizeFirst(fieldName); //$NON-NLS-1$
        Method method = findMethodInHierarchy(obj, methodName, EMPTY_TYPES);
        return executeMethod(obj, method, EMPTY_PARAMETERS);
    }
    
    public static Object setUsingMethod(Object obj, String fieldName, Object newValue) {
        String methodName = "set" + StringUtils.capitalizeFirst(fieldName); //$NON-NLS-1$
        Method method = findMethodInHierarchy(obj, methodName, new Class[] {newValue.getClass()});
        return executeMethod(obj, method, new Object[] {newValue});
    }

	public static Object getFieldValue(Object obj, String fieldName) {
		Field fld = findFieldInHierarchy(obj, fieldName);
		return getFieldValue(obj, fld);
	}
	
	public static void setFieldValue(Object obj, String fieldName, Object value) {
		Field fld = findFieldInHierarchy(obj, fieldName);
		setFieldValue(obj, fld, value);
	}
	
	public static Field findFieldInHierarchy(Object obj, String fieldName) {
		if (obj == null) {
			return null;
		}

		for (Class clazz = obj.getClass(); clazz != null; clazz = clazz
				.getSuperclass()) {
			try {
				return clazz.getField(fieldName);
			} catch (Exception e) {
			}

			try {
				return clazz.getDeclaredField(fieldName);
			} catch (Exception e) {
			}
		}

		throw new ReflectUtilsException(
				"Was unable to find required field"); //$NON-NLS-1$
	}

	public static Object getFieldValue(Object obj, Field fld) {
		if (!fld.isAccessible()) {
			fld.setAccessible(true);
		}

		try {
			return fld.get(obj);
		} catch (Exception e) {
			throw new ReflectUtilsException("Unable to get field " + fld.getName() + " value", e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public static void setFieldValue(Object obj, Field fld, Object value) {
		if (!fld.isAccessible()) {
			fld.setAccessible(true);
		}

		try {
			fld.set(obj, value);
		} catch (Exception e) {
			throw new ReflectUtilsException("Unable to set field " + fld.getName() + " value", e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	public static Object executeMethod(Object obj, String methodName, Class[] paramTypes, Object[] params) {
		Method method = findMethodInHierarchy(obj, methodName, paramTypes);
		return executeMethod(obj, method, params);
	}
	
	public static Method findMethodInHierarchy(Object obj, String methodName, Class... types) {
		if (obj == null) {
			return null;
		}

		for (Class clazz = obj.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
			try {
				return clazz.getMethod(methodName, types);
			} catch (Exception e) {
			}

			try {
				return clazz.getDeclaredMethod(methodName, types);
			} catch (Exception e) {
			}
		}

		throw new ReflectUtilsException(
				"Was unable to find required method"); //$NON-NLS-1$
	}
	
	public static Method findLocalMethod(Class clazz, String methodName, Class... types) {
		try {
			return clazz.getMethod(methodName, types);
		} catch (Exception e) {
		}
		
		try {
			return clazz.getDeclaredMethod(methodName, types);
		} catch (Exception e) {
		}
		
		throw new ReflectUtilsException(
			"Was unable to find required method"); //$NON-NLS-1$
	}
	
	public static Object executeMethod(Object obj, Method method, Object... params) {
		if(! method.isAccessible()) {
			method.setAccessible(true);
		}
		
		try {
			return method.invoke(obj, params);
		} catch(Exception exc) {
			throw new ReflectUtilsException("Unable to execute method " + method.getName(), exc); //$NON-NLS-1$
		}
	}
}
