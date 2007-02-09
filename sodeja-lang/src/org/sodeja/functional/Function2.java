package org.sodeja.functional;

import java.util.HashMap;
import java.util.Map;

public interface Function2<R, P1, P2> {
    public R execute(P1 p1, P2 p2);

    public static final class Utils {
    	public static <R, P1, P2> Function2<R, P1, P2> memorize(final Function2<R, P1, P2> functor) {
    		final Map<Pair<P1, P2>, R> cache = new HashMap<Pair<P1, P2>, R>();
    		return new Function2<R, P1, P2>() {
				public R execute(P1 p1, P2 p2) {
					Pair<P1, P2> pair = new Pair<P1, P2>(p1, p2);
					R result = cache.get(pair);
					if(result != null) {
						return result;
					}
					result = functor.execute(p1, p2);
					cache.put(pair, result);
					return result;
				}
    		};
    	}

    	public static <R, P1, P2> Function1<R, P2> curryFirst(final Function2<R, P1, P2> functor, final P1 p1) {
    		return new Function1<R, P2>() {
				public R execute(P2 p2) {
					return functor.execute(p1, p2);
				}
    		};
    	}

    	public static <R, P1, P2> Function1<R, P1> currySecond(final Function2<R, P1, P2> functor, final P2 p2) {
    		return new Function1<R, P1>() {
				public R execute(P1 p1) {
					return functor.execute(p1, p2);
				}
    		};
    	}
    	
//        public static void main(String[] args) {
//        	Function2<String, String, String> adder = new Function2<String, String, String>() {
//				public String execute(String p1, String p2) {
//					return p1 + p2;
//				}
//        	};
//        	
//        	Function1<String, String> functor = curryFirst(adder, "alabala");
//        	String value = ArrayUtils.toString(ArrayUtils.map(
//        			ArrayUtils.asArray(" nica", " juji", " tuti", " fruti"), 
//        			functor));
//        	System.out.println(value);
//        	
//        	List<ActionListener> uihu = new ArrayList<ActionListener>();
//        	Function2<Void, List<ActionListener>, ActionEvent> propagator = new Function2<Void, List<ActionListener>, ActionEvent>() {
//				public Void execute(List<ActionListener> listeners, ActionEvent event) {
//					for(ActionListener listener : listeners) {
//						listener.actionPerformed(event);
//					}
//					return null;
//				}
//        	};
//        	Function1<Void, ActionEvent> props = curryFirst(propagator, uihu);
//        	
//        	// lots of code or some other place
//        	props.execute(new ActionEvent(null, 0, null));
//
//        	// lots of code or some other place
//        	props.execute(new ActionEvent(null, 0, null));
//        }
    }
}
