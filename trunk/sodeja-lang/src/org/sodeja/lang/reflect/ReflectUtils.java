package org.sodeja.lang.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.sodeja.collections.ArrayUtils;
import org.sodeja.collections.ListUtils;
import org.sodeja.functional.Function1;
import org.sodeja.lang.StringUtils;

public final class ReflectUtils {
	private ReflectUtils() {
	}
	
    public static final Class[] EMPTY_TYPES = new Class[0];
    public static final Object[] EMPTY_PARAMETERS = new Object[0];
    
    public static Object getUsingMethod(Object obj, String fieldName) {
    	Field fld = findFieldInHierarchy(obj, fieldName);
    	
    	String methodName = StringUtils.capitalizeFirst(fieldName);
    	if(fld.getType() == Boolean.class || fld.getType() == boolean.class) {
    		methodName = "is" + methodName;
    	} else {
    		methodName = "get" + methodName;
    	}
    	
        Method method = findMethodInHierarchy(obj, methodName, EMPTY_TYPES);
        return executeMethod(obj, method, EMPTY_PARAMETERS);
    }
    
    public static Object setUsingMethod(Object obj, String fieldName, Object newValue) {
    	Field fld = findFieldInHierarchy(obj, fieldName);
        String methodName = "set" + StringUtils.capitalizeFirst(fieldName); //$NON-NLS-1$
        Method method = findBestMethod(obj.getClass(), methodName, new Class[] {fld.getType()});
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

		for(Field field : fieldsIterable(obj.getClass())) {
			if(field.getName().equals(fieldName)) {
				return field;
			}
		}

		throw new ReflectUtilsException("Was unable to find required field"); //$NON-NLS-1$
	}

	public static Object getFieldValue(Object obj, Field fld) {
		boolean access = fld.isAccessible();
		if(! access) {
			fld.setAccessible(true);
		}

		try {
			return fld.get(obj);
		} catch (Exception e) {
			throw new ReflectUtilsException("Unable to get field " + fld.getName() + " value", e); //$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			fld.setAccessible(access);
		}
	}

	public static void setFieldValue(Object obj, Field fld, Object value) {
		boolean access = fld.isAccessible();
		if(! access) {
			fld.setAccessible(true);
		}

		try {
			fld.set(obj, value);
		} catch (Exception e) {
			throw new ReflectUtilsException("Unable to set field " + fld.getName() + " value", e); //$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			fld.setAccessible(access);
		}
	}
	
	public static Object executeMethod(Object obj, String methodName, Class[] paramTypes, Object[] params) {
		Method method = findMethodInHierarchy(obj, methodName, paramTypes);
		return executeMethod(obj, method, params);
	}
	
	public static Object executeMethod(Object obj, String methodName, Object[] params) {
		Class[] paramTypes = ArrayUtils.map(params, new Function1<Class, Object>() {
			public Class execute(Object p) {
				return p.getClass();
			}});
		
		return executeMethod(obj, methodName, paramTypes, params);
	}
	
	public static Method findMethodInHierarchy(Object obj, String methodName, Class... types) {
		if (obj == null) {
			return null;
		}

		for (Class clazz : hierarchyIterable(obj.getClass())) {
			Method method = findLocalMethodImpl(clazz, methodName, types);
			if(method == null) {
				continue;
			}
			
			return method;
		}

		throw new ReflectUtilsException("Was unable to find <" + methodName + "> method in <" + obj.getClass() + ">: "); //$NON-NLS-1$
	}
	
	public static Method findLocalMethod(Class clazz, String methodName, Class... types) {
		Method method = findLocalMethodImpl(clazz, methodName, types);
		
		if(method == null) {
			throw new ReflectUtilsException("Was unable to find required method"); //$NON-NLS-1$
		}
		
		return method;
	}
	
	private static Method findLocalMethodImpl(Class clazz, String methodName, Class... types) {
		try {
			return clazz.getMethod(methodName, types);
		} catch (Exception e) {
		}
		
		try {
			return clazz.getDeclaredMethod(methodName, types);
		} catch (Exception e) {
		}

		return null;
	}
	
	public static Method findBestMethod(Class clazz, String name, Class[] types) {
		Method bestMatch = null;
		Integer bestMatchValue = null;
		
		for(Method method : methodsIterable(clazz)) {
			if(! method.getName().equals(name)) {
				continue;
			}
			
			Integer matchValue = matchValue(method.getParameterTypes(), types);
			if(matchValue == null) {
				continue;
			}
			
			if(bestMatchValue == null) {
				bestMatch = method;
				bestMatchValue = matchValue;
				continue;
			}
			
			if(matchValue < bestMatchValue) {
				continue;
			}
			
			bestMatch = method;
			bestMatchValue = matchValue;
		}
		
		return bestMatch;
	}
	
	private static Integer matchValue(Class[] to, Class[] types) {
		if(to.length != types.length) {
			return null;
		}
		
		int value = 0;
		for(int i = 0, n = to.length;i < n;i++) {
			if(to[i] == types[i]) {
				value += 3;
				continue;
			}
			
			if(types[i] == null) {
				continue;
			}
			
			if(to[i].isPrimitive() && !types[i].isPrimitive()) {
				Class toWrap = getWrapperClass(to[i]);
				if(toWrap.equals(types[i])) {
					value += 2;
					continue;
				}
				
				return null;
			}
			
			if(! to[i].isPrimitive() && types[i].isPrimitive()) {
				Class typeWrap = getWrapperClass(types[i]);
				if(! to[i].isAssignableFrom(typeWrap)) {
					return null;
				}
				
				value += 1;
				continue;
			}
			
			if(! to[i].isAssignableFrom(types[i])) {
				return null;
			}
			
			value += 1;
		}
		
		return value;
	}
	
	private static Class getWrapperClass(Class primitive) {
		if(primitive == byte.class) {
			return Byte.class;
		} else if(primitive == short.class) {
			return Short.class;
		} else if(primitive == int.class) {
			return Integer.class;
		} else if(primitive == long.class) {
			return Long.class;
		} else if(primitive == float.class) {
			return Float.class;
		} else if(primitive == double.class) {
			return Double.class;
		} else if(primitive == boolean.class) {
			return Boolean.class;
		} else if(primitive == char.class) {
			return Character.class;
		}
		
		return primitive;
	}
	
	public static Object executeMethod(Object obj, Method method, Object... params) {
		boolean access = method.isAccessible();
		if(! access) {
			method.setAccessible(true);
		}
		
		try {
			return method.invoke(obj, params);
		} catch(Exception exc) {
			throw new ReflectUtilsException("Unable to execute method " + method.getName(), exc); //$NON-NLS-1$
		} finally {
			if(! access) {
				method.setAccessible(access);
			}
		}
	}
	
	public static <R> R newInstance(Class<R> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Class resolveClass(String name) {
		try {
			return Class.forName(name);
		} catch(Exception exc) {
			throw new RuntimeException(exc);
		}
	}
		
	public static List<Class> loadHierarchy(Class clazz) {
		List<Class> result = ListUtils.asList(hierarchyIterator(clazz));
		Collections.reverse(result);
		return result;
	}
	
	public static Iterable<Class> hierarchyIterable(final Class clazzParam) {
		return new Iterable<Class>() {
			@Override
			public Iterator<Class> iterator() {
				return hierarchyIterator(clazzParam);
			}};
	}
	
	public static Iterable<Class> hierarchyIterable(final Class clazzParam, final Class limit) {
		return new Iterable<Class>() {
			@Override
			public Iterator<Class> iterator() {
				return hierarchyIterator(clazzParam, limit);
			}};
	}
	
	public static Iterator<Class> hierarchyIterator(final Class clazzParam) {
		return hierarchyIterator(clazzParam, null);
	}
	
	public static Iterator<Class> hierarchyIterator(final Class clazzParam, final Class limit) {
		return new OnClassIterator<Class>(clazzParam, limit, new Function1<Class[], Class>() {
			@Override
			public Class[] execute(Class p) {
				return new Class[] {p};
			}});
	}
	
	public static Iterable<Field> fieldsIterable(final Class clazzParam) {
		return new Iterable<Field>() {
			@Override
			public Iterator<Field> iterator() {
				return fieldsIterator(clazzParam);
			}};
	}
	
	public static Iterable<Field> fieldsIterable(final Class clazzParam, final Class limit) {
		return new Iterable<Field>() {
			@Override
			public Iterator<Field> iterator() {
				return fieldsIterator(clazzParam, limit);
			}};
	}
	
	public static Iterator<Field> fieldsIterator(final Class clazzParam) {
		return fieldsIterator(clazzParam, null);
	}
	
	public static Iterator<Field> fieldsIterator(final Class clazzParam, final Class limit) {
		return new OnClassIterator<Field>(clazzParam, limit, new Function1<Field[], Class>() {
			@Override
			public Field[] execute(Class p) {
				return p.getDeclaredFields();
			}});
	}

	public static Iterable<Method> methodsIterable(final Class clazzParam) {
		return new Iterable<Method>() {
			@Override
			public Iterator<Method> iterator() {
				return methodsIterator(clazzParam);
			}};
	}
	
	public static Iterable<Method> methodsIterable(final Class clazzParam, final Class limit) {
		return new Iterable<Method>() {
			@Override
			public Iterator<Method> iterator() {
				return methodsIterator(clazzParam, limit);
			}};
	}
	
	public static Iterator<Method> methodsIterator(final Class clazzParam) {
		return methodsIterator(clazzParam, null);
	}
	
	public static Iterator<Method> methodsIterator(final Class clazzParam, final Class limit) {
		return new OnClassIterator<Method>(clazzParam, limit, new Function1<Method[], Class>() {
			@Override
			public Method[] execute(Class p) {
				return p.getDeclaredMethods();
			}});
	}
	
	private static class OnClassIterator<T> implements Iterator<T> {
		private Class clazz;
		private Class limit;
		private Function1<T[], Class> accessor;
		
		private T[] contents;
		private int contentsIndex = 0;
		
		public OnClassIterator(Class clazzParam, Class limitParam, Function1<T[], Class> accessorParam) {
			clazz = clazzParam;
			limit = limitParam;
			
			accessor = accessorParam;
			
			contents = accessor.execute(clazz);
		}
		
		@Override
		public boolean hasNext() {
			if(contents.length > contentsIndex) {
				return true;
			}
			
			contentsIndex = 0;
			while(clazz != limit) {
				clazz = clazz.getSuperclass();
				if(clazz == null) {
					break;
				}
				
				contents = accessor.execute(clazz);
				if(contents.length != 0) {
					break;
				}
			}
			
			if(clazz == limit) {
				return false;
			}
			
			return true;
		}

		@Override
		public T next() {
			return contents[contentsIndex++];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("It is not possible to modify class on runtime");
		}
	}
}
