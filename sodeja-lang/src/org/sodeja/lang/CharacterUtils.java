package org.sodeja.lang;

public class CharacterUtils {
	public static boolean isHexadecimal(Character ch) {
		if(ch >= '0' && ch <= '9') {
			return true;
		}
		
		Character lowCaseCh = Character.toLowerCase(ch);
		return  lowCaseCh >= 'a' && lowCaseCh <= 'f';
	}
	
	public static Character convertToUnicode(Character[] hexDigits) {
		byte[] converted = convert(hexDigits);
		int val = 0;
		for(int i = 0;i < converted.length;i++) {
			val <<= 4;
			val += converted[i];
		}
		return (char) val;
	}
	
	public static byte[] convert(Character[] hexDigits) {
		byte[] hex = new byte[hexDigits.length];
		for(int i = 0;i < hex.length;i++) {
			hex[i] = convert(hexDigits[i]);
		}
		return hex;
	}
	
	public static byte convert(Character hexDigit) {
		if(hexDigit >= '0' && hexDigit <= '9') {
			return (byte) (hexDigit - '0');
		}
		Character lowCaseHex = Character.toLowerCase(hexDigit);
		return (byte) (10 + (lowCaseHex - 'a'));
	}
}
