package org.sodeja.lang;

import java.util.List;

import org.sodeja.collections.ListUtils;
import org.sodeja.functional.Function1;
import org.sodeja.functional.Function2;

public final class StringUtils {
	private StringUtils() {
	}
	
    public static boolean isEmpty(String txt) {
        return txt == null || txt.length() == 0;
    }
    
    public static boolean isTrimmedEmpty(String text) {
    	return isEmpty(text) || text.trim().length() == 0;
    }

    public static String capitalizeFirst(String str) {
        return capitalize(str, 0);
    }

    public static String capitalize(String str, int i) {
        char charAt = str.charAt(i);
        if (Character.isUpperCase(charAt)) {
            return str;
        }
        StringBuilder builder = new StringBuilder(str);
        builder.setCharAt(i, Character.toUpperCase(charAt));
        return builder.toString();
    }
    
    public static String nonNullValue(Object obj) {
    	if(obj == null) {
    		return ""; //$NON-NLS-1$
    	}
    	return String.valueOf(obj);
    }

	public static String getValue(Object value, String string) {
		if(value == null) {
			return string;
		}
		return String.valueOf(value);
	}
	
	public static <T> String appendWithSeparatorToString(List<T> vals, final String sep) {
		return appendWithSeparator(vals, sep, new Function1<String, T>() {
			@Override
			public String execute(T p) {
				return p.toString();
			}});
	}
	
	public static <T> String appendWithSeparator(List<T> vals, final String sep, Function1<String, T> functor) {
		List<String> strVals = ListUtils.map(vals, functor);
		return ListUtils.foldr(strVals, null, new Function2<String, String, String>() {
			@Override
			public String execute(String p1, String p2) {
				if(StringUtils.isEmpty(p2)) {
					return p1;
				}
				return p1 + sep + p2;
			}});
	}
}
