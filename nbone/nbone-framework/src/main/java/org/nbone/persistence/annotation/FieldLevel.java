package org.nbone.persistence.annotation;

import java.util.HashMap;
import java.util.Map;


/**
 * 定义字段级别 值越小级别越高
 * @author thinking
 * @since 2016-08-08
 * @version 1.0.1
 */
public enum FieldLevel  {
	ID(0),
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGHT(8),
	NINE(9),
	ALL(10);
	
	/**
	 * ID为最核心字段 0
	 */
    public final static FieldLevel MIN_VALUE = ID;
    /**
     * 默认级别字段 5
     */
    public final static FieldLevel NORM_VALUE = FIVE;
    /**
     * 最小级别字段(一般用于大字段BLOB,CLOB)
     */
    public final static FieldLevel MAX_VALUE = ALL;
    
	
	private final int id;
	private static Map<Integer,FieldLevel> codeLookup = new HashMap<Integer,FieldLevel>();
	
    static {
	    for (FieldLevel level : FieldLevel.values()) {
	      codeLookup.put(level.id, level);
	    }
	  }
	
	private FieldLevel(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	
    public static FieldLevel forId(int id)  {
	    return codeLookup.get(id);
	}
    public static void main(String[] args) {
    	
    	System.out.println(FieldLevel.ALL .equals(FieldLevel.ALL));
    	System.out.println(FieldLevel.FIVE == FieldLevel.NORM_VALUE);
    	System.out.println(FieldLevel.ALL.compareTo(FieldLevel.ALL));
		
	}

}
