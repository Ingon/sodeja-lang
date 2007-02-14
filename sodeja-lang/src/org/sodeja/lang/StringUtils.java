package org.sodeja.lang;

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
}
