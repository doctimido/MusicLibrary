package com.waves.library.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * A container for static methods used in dealing with the library
 * 
 */
public class DbUtils {

	/**
	 * 
	 * @param A
	 *            - First string array
	 * @param B
	 *            - Second string array
	 * @return The string array (A,B), where A and B are also both string
	 *         arrays.
	 */
	public static String[] concat(String[] A, String[] B) {
		int aLen = A.length;
		int bLen = B.length;
		String[] C = new String[aLen + bLen];
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);
		return C;
	}

	/**
	 * 
	 * @param input
	 *            - String to be matched
	 * @param items
	 *            - Array of strings to test
	 * @return true if any of the "items" are in the string "input". This
	 *         performs a case insensitive pattern match.
	 */
	public static boolean matchesExtension(String input, String[] items) {
		Pattern p;
		Matcher m;

		input = input.substring(input.lastIndexOf('.'));

		for (int i = 0; i < items.length; i++) {
			p = Pattern.compile(items[i], Pattern.CASE_INSENSITIVE);
			m = p.matcher(input);
			if (m.matches())
				return m.matches();
		}
		return false;
	}

}
