package com.idohoo.eclipse.plugin.rap.util;

public class StringUtil {

	public static boolean isEmpty(String s){
		if(s == null || "".equals(s)){
			return true;
		}
		return false;
	}
	
	public static boolean isNotEmpty(String s){
		return !isEmpty(s);
	}
	
	public static String FU(String s){
		return s.replaceFirst("^\\w", (s.charAt(0)+"").toUpperCase());
	}
}
