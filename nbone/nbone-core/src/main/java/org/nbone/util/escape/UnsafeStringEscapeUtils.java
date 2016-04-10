package org.nbone.util.escape;


public abstract class UnsafeStringEscapeUtils {
	
	private final static UnsafeStringEscape stringEscape = new UnsafeStringEscape(); 
	
	
	public static String largeGrainedTagFilter(String value) {
		
       return stringEscape.largeGrainedTagFilter(value);
		
	}
	
	public static String smallGrainedTagFilter(String value) {
		
		return stringEscape.smallGrainedTagFilter(value);
	}
	
	public static String keywordFilter(String value) {
		
		return stringEscape.keywordFilter(value);
	}
	
	public static String escapeSqlFilter(String value) {
		 
		return stringEscape.escapeSqlFilter(value);
	}
	
}
