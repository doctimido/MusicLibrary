package com.waves.library.utils;

/**  
 * @author Ian
 * 
 * leave this alone for now
 * This needs a lot more work to be robust
 * 
 */
public class ParseSong {
//	private String t;
//	private String a;
	
	ParseSong() {
//		t = title;
//		a = artist;
	}
	
	public static String featuring(String title, String artist) {
		String[] match = {"featuring", "feat.", "feat"};
		
		return parse(title, match);
	}
	
	private static String parse(String s, String[] match) {
		/** matching strings must be sorted in descending order of length */
		
		String[] br = {"(", ")", "[", "]", "{", "}"};
		int[] brIndex = new int[2];
		int brCount = 0;
		int matchIndex;
		int matchCount = 0;
		boolean done = false;
		
		int j;
		for (int i = 0; i<match.length; i++) {
			do{
				matchIndex = s.indexOf(match[i]);
			} while (!(matchIndex==-1));
			
			if (!(matchIndex == -1)) {
				j = 0;
				while (j<br.length && !done) {
					
					brIndex[j] = s.indexOf(br[j]);
					brIndex[j+1] = s.indexOf(br[j+1]);
					
					if (brIndex[j]<matchIndex && brIndex[j+1]>matchIndex) {
						
					}
					j = j+2;
				}
			}
		}
		
		return null;
	}
}
