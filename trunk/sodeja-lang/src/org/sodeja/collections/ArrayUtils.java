package org.sodeja.collections;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sodeja.functional.Function1;
import org.sodeja.functional.Function2;
import org.sodeja.functional.Predicate1;
import org.sodeja.lang.StringUtils;


public final class ArrayUtils {
    private ArrayUtils() {
    }
	
    public static <T, R> R[] map(T[] source, R[] result, Function1<R, T> functor) {
        for (int i = 0; i < source.length; i++) {
            R executeResult = functor.execute(source[i]);
            if(result == null && executeResult != null) {
            	result = (R[]) ArrayUtils.createArray(executeResult, source.length);
            }
            if(result != null) {
            	result[i] = executeResult;
            }
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    public static <T, R> R[] map(T[] source, Function1<R, T> functor) {
        return map(source, (R[]) createArray(functor, source.length), functor);
    }
    
	public static <P> P foldr(P[] source, P initial, Function2<P, P, P> functor) {
		P result = initial;
		for(int i = source.length - 1;i >= 0;i--) {
			result = functor.execute(source[i], result);
		}
		return result;
	}
	
	public static <P> P foldl(P[] source, P initial, Function2<P, P, P> functor) {
		P result = initial;
		for(int i = 0;i < source.length;i++) {
			result = functor.execute(result, source[i]);
		}
		return result;
	}
    
    public static <T, R> Collection<R> mapToCollection(T[] source, Collection<R> result, Function1<R, T> functor) {
    	for(T t : source) {
    		result.add(functor.execute(t));
    	}
    	return result;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T[] filter(T[] source, Predicate1<T> functor) {
        List<T> result = new ArrayList<T>();
        for (T t : source) {
            if (functor.execute(t)) {
                result.add(t);
            }
        }
        return result.toArray((T[]) createArray(functor, result.size()));
    }

    public static <T> boolean exists(T[] source, Predicate1<T> functor) {
        for(T t : source) {
            if(functor.execute(t)) {
                return true;
            }
        }
        return false;
    }
    
    public static <T> T[] asArray(T... args) {
        return args;
    }
    
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    static Object[] createArray(Function1 functor, int length) {
    	return createArray(functor, 0, length);
    }

    static Object[] createArray(Object genericed, int position, int length) {
        ParameterizedType parameterizedType = ((ParameterizedType) genericed
		                .getClass().getGenericInterfaces()[0]);
		Type type = parameterizedType.getActualTypeArguments()[position];
		if(type instanceof Class) {
			return (Object[]) Array.newInstance((Class) type, length);
		}
		return null;
    }

    @SuppressWarnings("unchecked")
	static <T> T[] createArray(T component, int length) {
        if(component == null) {
            return null;
        }
        return (T[]) Array.newInstance(component.getClass(), length);
    }
    
    public static <T> String toString(T[] array) {
    	String[] vals = map(array, new Function1<String, T>() {
			public String execute(T p) {
				return StringUtils.nonNullValue(p);
			}});
    	StringBuilder builder = new StringBuilder();
    	builder.append("["); //$NON-NLS-1$
    	for(String str : vals) {
    		builder.append(str + ", "); //$NON-NLS-1$
    	}
    	builder.setLength(builder.length() - 2);
    	builder.append("]"); //$NON-NLS-1$
    	return builder.toString();
    }
}
