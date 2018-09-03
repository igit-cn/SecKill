package com.proxy.utils;

import java.security.MessageDigest;
import java.util.Locale;


public class MD5Util {
	/**
	 * 将字符串采用MD5算法加密
	 * 
	 * @param psd 原字符串
	 * @return 加密后
	 * @throws Exception
	 */
	public static String digest(String psd) {
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(psd.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString().toUpperCase(Locale.getDefault());
		}
		catch(Exception e){
			
		}
		
		return "";
	}
}
