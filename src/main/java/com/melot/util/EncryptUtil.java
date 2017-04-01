package com.melot.util;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class EncryptUtil {
	
	private static char[] HEX_TAB_WEB = "s~0!e@5#c$8%r^6&".toCharArray();

	public static String slist_web(Map<String, Object> l)
	   {
	     if ((l == null) || (l.size() == 0))
	       return null;
	     try {
	       Set<String> ks = l.keySet();
	       
	       String[] kss = new String[ks.size()];
	       ks.toArray(kss);
	       Arrays.sort(kss, String.CASE_INSENSITIVE_ORDER);
	       
	 
	       String stos = "";
	       for (int i = 0; i < kss.length; i++) {
	         Object o = l.get(kss[i]);
	         if (o != null) {
	           stos = stos + o.toString();
	         }
	       }
	       if (stos.length() < 8) {
	         stos = stos + "0123456789012345";
	       }
	       
	       int[] s1 = new int[8];
	       for (int i = 0; i < stos.length() / 8 - 1; i++) {
	         for (int j = 0; j < 8; j++) {
	           if ((i + 1) * 8 + j < stos.length()) {
	             if (i == 0)
	               s1[j] = stos.charAt(i * 8 + j);
	             s1[j] ^= stos.charAt((i + 1) * 8 + j);
	           }
	         }
	       }
	       
	 
	       String s0 = "";
	       for (int i = 0; i < 8; i++) {
	         s0 = s0 + HEX_TAB_WEB[(s1[i] >>> 3 & 0xF)];
	         s0 = s0 + HEX_TAB_WEB[(s1[i] & 0xF)];
	       }
	       int sum = 0;
			for (int i = 0; i < s0.length(); i += 2) {
				sum += s0.charAt(i);
			}
		    char a = HEX_TAB_WEB[(sum % 16)];
		    char b = HEX_TAB_WEB[(sum % 13)];
		    s0 = s0 + a + b;
	       return s0;
	     } catch (Exception e) {
	       e.printStackTrace();
	     }
	     return null;
	   }
}
