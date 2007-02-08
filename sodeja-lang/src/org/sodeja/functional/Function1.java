package org.sodeja.functional;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.sodeja.lang.reflect.ReflectUtils;

public interface Function1<R, P> {
    public R execute(P p);

    public static final class Utils {
    	public static <R, P> Function1<R, P> memorize(final Function1<R, P> functor) {
    		final Map<P, R> cache = new HashMap<P, R>();
    		return new Function1<R, P>() {
				public R execute(P p) {
					R result = cache.get(p);
					if(result != null) {
						return result;
					}
					result = functor.execute(p);
					cache.put(p, result);
					return result;
				}
    		};
    	}
    	
    	public static <R, P> Function0<R> curry(final Function1<R, P> functor, final P p) {
    		return new Function0<R>() {
				public R execute() {
					return functor.execute(p);
				}
    		};
    	}
    	
    	public static Function1 reflectedStatic(Class clazz, String name, Class type) {
    		final Method method = ReflectUtils.findLocalMethod(clazz, name, type);
    		return new Function1() {
				public Object execute(Object p) {
					return ReflectUtils.executeMethod(null, method, p);
				}
			};
    	}

    	public static Function1 reflectedDynamic(final Object object, String name, Class type) {
    		final Method method = ReflectUtils.findLocalMethod(object.getClass(), name, type);
    		return new Function1() {
				public Object execute(Object p) {
					return ReflectUtils.executeMethod(object, method, p);
				}
			};
    	}

//        private static Function1<Integer, Integer> fib;
//    	
//        public static void main(String[] args) {
//        	fib = new Function1<Integer, Integer>() {
//    			public Integer execute(Integer p) {
//    				if(p < 2) {
//    					return p;
//    				}
//    				return fib.execute(p - 1) + fib.execute(p - 2);
//    			}
//    		};
//        	
//			long time = System.currentTimeMillis();
//			for(int i = 0;i < 40;i++) {
//				System.out.print(fib.execute(i) + ", ");
//			}
//			System.out.println("\nTotal1: " + (System.currentTimeMillis() - time));
//			
//			fib = memorize(fib);
//			time = System.currentTimeMillis();
//			for(int i = 0;i < 20000;i++) {
//				System.out.print(fib.execute(i) + ", ");
//			}
//			System.out.println("\nTotal2: " + (System.currentTimeMillis() - time));
//        }
    }
}
