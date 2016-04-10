package org.nbone.util.temp;

import java.math.BigDecimal;
import java.text.DecimalFormat;
/**
 * 浮点数格式化公共类
 * 
 */
public final class NumberFormat {
	
	public static String format0 = "#";
	public static String format1 = "#.#";
	public static String format2 = "#.##";
	public static String format3 = "#.###";
	public static String format4 = "#.####";

	/**
	 * 格式化double,返回两位字符串
	 * @param bb
	 * @return
	 */
	public static String format(double bb) {
		String ss = "";
		if (bb == -9.999 || Double.isNaN(bb) || Double.isInfinite(bb)) {
			return ss;
		} else {
			DecimalFormat myFormat = new DecimalFormat();
			myFormat.applyPattern(format2);
			ss = myFormat.format(bb);
			
			myFormat = null;
		}
		return re_foramt(ss, format2);
	}
	/**
	 * 格式化double
	 * @param bb double数据
	 * @param formatStr 匹配模式字符串,见本类中常量定义
	 * @return 格式化double后的字符串
	 */
	public static String format(double bb,String formatStr) {
		String ss = "";
		if (formatStr==null || "".equals(formatStr.trim())) {
			formatStr = format2;
		}
		if (bb == -9.999 || Double.isNaN(bb) || Double.isInfinite(bb)) {
			return ss;
		} else {
			DecimalFormat myFormat = new DecimalFormat();
			myFormat.applyPattern(formatStr);
			ss = myFormat.format(bb);
			myFormat = null;
		}
		return re_foramt(ss, formatStr);
	}
	/**
	 * 格式化BigDecimal,返回两位字符串
	 * @param bb
	 * @return
	 */
	public static String format(BigDecimal bb){
		return format(bb,format2);
	}
	/**
	 * 格式化BigDecimal
	 * @param bb double数据
	 * @param formatStr 匹配模式字符串,见本类中常量定义
	 * @return 格式化double后的字符串
	 */
	public static String format(BigDecimal bb,String formatStr){
		String ss = "";
		if (formatStr==null || "".equals(formatStr.trim())) {
			formatStr = format2;
		}
		DecimalFormat myFormat = new DecimalFormat();
		myFormat.applyPattern(formatStr);
		ss = myFormat.format(bb);
		myFormat = null;
		return re_foramt(ss, formatStr);
	}
	
	/**
	 * 对数据进行格式化
	 * @param value 需要格式化的值
	 * @param value 小数点保留几位
	 * @return
	 */
	public static Double format(Object value,int formatLength){
		String style = ".";
		if(formatLength == 0){
			style = "";
		}
		for(int i = 0;i< formatLength;i++){
			style += "#";
				
		}
		DecimalFormat df = new DecimalFormat("#"+style);
		double temp = 0.0;
		try {
			temp = Double.parseDouble(value.toString());
		} catch (NumberFormatException e) {
			return Double.parseDouble("-1");
		}
		return new BigDecimal(df.format(temp)).doubleValue();
	}
	
	public static boolean availableInteger(Integer number){
		if(number!=Integer.valueOf("-1").intValue()){
			return true;
		}
		return false;
	}
	
	public static String re_foramt(String str , String formatNum){
		String [] sss = str.split("\\.");
		if(sss !=null && sss.length==2 ){
			for(int i=0;i<formatNum.split("\\.")[1].length()-sss[1].length();i++){
				str +="0";
			}
		}else{
			if(formatNum.split("\\.").length>1){
				String temp =".";
				for(int i=0;i<formatNum.split("\\.")[1].length();i++){
					
					temp +="0";
				}
				str = str+temp;
			}
			
		}
		return str;
	}
	public static void main(String[] args) {
		NumberFormat f = new NumberFormat();
		
		System.out.println("#".split("\\.").length);
	}
}
