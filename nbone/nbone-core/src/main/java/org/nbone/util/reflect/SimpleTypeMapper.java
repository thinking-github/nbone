/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.nbone.util.reflect;


import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.activation.DataHandler;

import org.nbone.constants.SimpleTypeConstant;

public class SimpleTypeMapper implements SimpleTypeConstant {
    
    public static Object getSimpleTypeObject(Class<?> parameter, String text) {
        String name = parameter.getName();

        if(name.equals(STRING)) {
            return text;
        } else  if (text == null || text.length() == 0) {
            return null;
        } else if (name.equals(INT)) {
            return new Integer(text);
        } else if (name.equals(BOOLEAN)) {
            return ConverterUtil.convertToBoolean(text);
        } else if (name.equals(BYTE)) {
            return new Byte(text);
        } else if (name.equals(DOUBLE)) {
            return new Double(text);
        } else if (name.equals(SHORT)) {
            return new Short(text);
        } else if (name.equals(LONG)) {
            return new Long(text);
        } else if (name.equals(FLOAT)) {
            return new Float(text);
        } else if (name.equals(CHAR)) {
            return text.toCharArray()[0];
        } else if (name.equals(W_INT)) {
            return new Integer(text);
        } else if (name.equals(W_BOOLEAN)) {
            return Boolean.valueOf(text);
        } else if (name.equals(W_BYTE)) {
            return new Byte(text);
        } else if (name.equals(W_DOUBLE)) {
            return new Double(text);
        } else if (name.equals(W_SHORT)) {
            return new Short(text);
        } else if (name.equals(W_LONG)) {
            return new Long(text);
        } else if (name.equals(W_FLOAT)) {
            return new Float(text);
        } else if (name.equals(W_CHAR)) {
            return text.toCharArray()[0];
        } else if (name.equals(W_CALENDAR)) {
            return makeCalendar(text);
        } else if (name.equals(W_DATE)) {
            return makeDate(text);
        }/*
         * return the correpsonding object for adding data type
         */
        else if(name.equals(BIG_DECIMAL)) {
        	return new java.math.BigDecimal(text);
        }
        else if(name.equals(BIG_INTEGER)) {
        	return new java.math.BigInteger(text);
        }
        else if(name.equals(W_URI)) {
            try {
                return new URI(text);
            } catch (URISyntaxException e) {
                throw new RuntimeException(" Invalid URI " + text, e);
            }
        } else {
            return null;
        }
    }


    public static boolean isSimpleType(Object obj) {
        String objClassName = obj.getClass().getName();
        return obj instanceof Calendar || obj instanceof Date || isSimpleType(objClassName);
    }

    public static boolean isSimpleType(Class<?> obj) {
        String objClassName = obj.getName();
        return isSimpleType(objClassName);
    }

    public static boolean isDataHandler(Class<?> obj) {
       return obj.isAssignableFrom(DataHandler.class) && !obj.equals(Object.class);
    }

    public static boolean isHashSet(Class<?> obj) {
        return java.util.HashSet.class.isAssignableFrom(obj);
    }


    public static boolean isCollection(Class<?> obj) {
        return java.util.Collection.class.isAssignableFrom(obj);
    }
    
    
    
    public static boolean isPrimitiveWithNumber(Class<?> clazz){
    	 String objClassName = clazz.getName();
		return isPrimitiveWithNumber(objClassName);
    }
    public static boolean isPrimitiveWithNumber(String objClassName){
    	  if(objClassName == null){
 	    	 return false;
 	     }
     	  if (objClassName.equals(INT)) {
              return true;
          } else if (objClassName.equals(LONG)) {
              return true;
          } else if (objClassName.equals(SHORT)) {
              return true;
          } else if (objClassName.equals(DOUBLE)) {
              return true;
          }  else if (objClassName.equals(FLOAT)) {
              return true;
          } else if (objClassName.equals(W_INT)) {
              return true;
          } else if (objClassName.equals(W_LONG)) {
              return true;
          } else if (objClassName.equals(W_SHORT)) {
              return true;
          } else if (objClassName.equals(W_DOUBLE)) {
              return true;
          } else if (objClassName.equals(W_FLOAT)) {
              return true;
          } 
     	 /*
           * consider BigDecimal, BigInteger, as simple type
           *  
           */
          else return objClassName.equals(BIG_DECIMAL)
                  || objClassName.equals(BIG_INTEGER);
    }
    
    public static boolean isPrimitiveWithString(Class<?> clazz){
    	if(clazz == null){
    		return false;
    	}
		return clazz== String.class || clazz== char.class || clazz== Character.class ;
   }
    
    public static boolean isPrimitive(String objClassName){
	     if(objClassName == null){
	    	 return false;
	     }
	     if(isPrimitiveWithNumber(objClassName)){
	    	return true;
	     }else{
	    	 if (objClassName.equals(STRING)) {
	             return true;
	         }  else if (objClassName.equals(BOOLEAN)) {
	             return true;
	         } else if (objClassName.equals(BYTE)) {
	             return true;
	         } else if (objClassName.equals(CHAR)) {
	             return true;
	         } else if (objClassName.equals(W_BOOLEAN)) {
	             return true;
	         } else if (objClassName.equals(W_BYTE)) {
	             return true;
	         }  else if (objClassName.equals(W_CHAR)) {
	             return true;
	         } 
	         else return false;
	     }
    	
    }
    public static boolean isSimpleType(String objClassName) {
    	if(isPrimitive(objClassName)){
    		return true;
    	}else{
    	  if (objClassName.equals(W_DATE)) {
              return true;
          } else if (objClassName.equals(W_URI)) {
              return true;
          } else if (objClassName.equals(W_CALENDAR)) {
              return true;
          } else if (objClassName.equals(W_Locale)) {
              return true;
          } else if (objClassName.equals(W_Class)) {
              return true;
          }
          else return false;
    	}
      
    }

    public static String getStringValue(Object obj) {
        if (obj instanceof Float ||
                obj instanceof Double) {
            double data;
            if (obj instanceof Float) {
                data = ((Float)obj).doubleValue();
            } else {
                data = (Double)obj;
            }
            if (Double.isNaN(data)) {
                return "NaN";
            } else if (data == Double.POSITIVE_INFINITY) {
                return "INF";
            } else if (data == Double.NEGATIVE_INFINITY) {
                return "-INF";
            } else {
                return obj.toString();
            }
        } else if (obj instanceof Calendar) {
            Calendar calendar = (Calendar) obj;
            return ConverterUtil.convertToString(calendar);
        } else if (obj instanceof Date) {
            SimpleDateFormat zulu = new SimpleDateFormat("yyyy-MM-dd");
            return zulu.format(obj);
        } else if (obj instanceof URI){
            return obj.toString();
        }
        return obj.toString();
    }

    public static Object makeCalendar(String source) {
        return ConverterUtil.convertToDateTime(source);
    }

    public static Object makeDate(String source) {
        return ConverterUtil.convertToDate(source);
    }
    
    
    

}
