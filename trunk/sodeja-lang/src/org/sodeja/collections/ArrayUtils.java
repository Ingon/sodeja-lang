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
import org.sodeja.lang.Range;
import org.sodeja.lang.StringUtils;


public final class ArrayUtils {
    private ArrayUtils() {
    }
	
    public static <T, R> R[] map(T[] source, R[] result, Function1<R, T> functor) {
        for (int i = 0; i < source.length; i++) {
            R executeResult = functor.execute(source[i]);
            if(result == null && executeResult != null) {
            	result = (R[]) ArrayUtils.createArray1(executeResult, source.length);
            }
            if(result != null) {
            	result[i] = executeResult;
            }
        }
        return result;
    }
    
    public static <T> T[] dup(T[] source) {
    	if(source.length == 0) {
    		return source;
    	}
    	T[] result = createArray1(source[0], source.length * 2);
    	for(Integer i : Range.of(source)) {
    		result[i * 2] = source[i];
    		result[i * 2 + 1] = source[i];
    	}
    	return result;
    }
    
    public static <T> T[][] unmapTuples(T[] source, int tupleSize) {
    	if(tupleSize == 0) {
    		throw new RuntimeException("Only tuples with size > 0");
    	}
    	if(source.length % tupleSize != 0) {
    		throw new RuntimeException("Expcted whole tuples");
    	}
    	
    	T[][] result = createArray1(source, tupleSize);
    	if(tupleSize == 1) {
    		result[0] = source;
    		return result;
    	}
    	
    	for(int i : Range.of(result)) {
    		if(source.length != 0) {
    			result[i] = createArray1(source[0], source.length / tupleSize);
    		} else {
    			result[i] = source;
    		}
    	}
    	
    	if(source.length == 0) {
    		return result;
    	}
    	
    	for(final int i : Range.of(result[0])) {
    		for(final int j : Range.of(tupleSize)) {
    			result[j][i] = source[i * tupleSize + j];
    		}
    	}
    	
    	return result;
    }
    
    @SuppressWarnings("unchecked")
    public static <T, R> R[] map(T[] source, Function1<R, T> functor) {
    	if(source == null) {
    		return null;
    	}
        return map(source, (R[]) createArray(functor, source.length), functor);
    }
    
	public static <R, P> R foldr(P[] source, R initial, Function2<R, P, R> functor) {
		R result = initial;
		for(int i = source.length - 1;i >= 0;i--) {
			result = functor.execute(source[i], result);
		}
		return result;
	}
	
	public static <R, P> R foldl(P[] source, R initial, Function2<R, R, P> functor) {
		R result = initial;
		for(int i = 0;i < source.length;i++) {
			result = functor.execute(result, source[i]);
		}
		return result;
	}
    
	public static <P> boolean any(P[] source, final Predicate1<P> functor) {
		return foldr(source, false, new Function2<Boolean, P, Boolean>() {
			public Boolean execute(P object, Boolean tempResult) {
				return tempResult | functor.execute(object);
			}});
	}
	
	public static <P> boolean all(P[] source, final Predicate1<P> functor) {
		return foldr(source, true, new Function2<Boolean, P, Boolean>() {
			public Boolean execute(P object, Boolean tempResult) {
				return tempResult & functor.execute(object);
			}});
	}

    public static <T> T find(T[] source, Predicate1<T> functor) {
        for (T t : source) {
            if (functor.execute(t)) {
                return t;
            }
        }
        return null;
    }
	
	public static <T> T[] append(T value, T[] source) {
		T[] result = (T[]) Array.newInstance(value.getClass(), source.length + 1);
		System.arraycopy(source, 0, result, 1, source.length);
		result[0] = value;
		return result;
	}

	public static <T> T[] append(T[] source, T value) {
		T[] result = (T[]) Array.newInstance(value.getClass(), source.length + 1);
		System.arraycopy(source, 0, result, 0, source.length);
		result[source.length] = value;
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

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }
    
    @SuppressWarnings("unchecked")
	static Object[] createArray(Function1 functor, int length) {
    	return createArray(functor, 0, length);
    }

    @SuppressWarnings("unchecked")
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
	static <T> T[] createArray1(T component, int length) {
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
    	builder.append("[");
    	for(String str : vals) {
    		builder.append(str + ", ");
    	}
    	builder.setLength(builder.length() - 2);
    	builder.append("]");
    	return builder.toString();
    }
}
