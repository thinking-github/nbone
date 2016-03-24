package org.nbone.constant;

import java.net.URI;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public interface SimpleTypeConstant {
    
    /**
     * primitive types 
     */
    public static final String SHORT = "short";
    public static final String INT = "int";
    public static final String LONG = "long";
    public static final String FLOAT = "float";
    public static final String DOUBLE = "double";
    public static final String CHAR = "char";
    public static final String BYTE = "byte";
    public static final String BOOLEAN = "boolean";
    /**
     * primitive types  Wrapper
     */
    public static final String W_SHORT = "java.lang.Short";
    public static final String W_INT = "java.lang.Integer";
    public static final String W_LONG = "java.lang.Long";
    public static final String W_FLOAT = "java.lang.Float";
    public static final String W_DOUBLE = "java.lang.Double";
    public static final String W_CHAR = "java.lang.Character";
    public static final String W_BYTE = "java.lang.Byte";
    public static final String W_BOOLEAN = "java.lang.Boolean";
    
    
    public static final String STRING = "java.lang.String";
    public static final String W_DATE = "java.util.Date";
    public static final String W_URI = URI.class.getName();
    
    public static final String W_Class = Class.class.getName();
    public static final String W_Locale = Locale.class.getName();
    public static final String W_CALENDAR = Calendar.class.getName();
    public static final String W_UUID = UUID.class.getName();

    /*
     * To support deserialize BigDecimal, BigInteger
     */
    public static final String BIG_DECIMAL = "java.math.BigDecimal";
    public static final String BIG_INTEGER = "java.math.BigInteger";
	
	

}
