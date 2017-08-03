/*
 * <p>Title: 方天服务平台系统</p>
 * <p>Description: 方天服务平台系统</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 江苏方天电力技术有限公司</p>
 */
package org.nbone.util.lang;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

/**
 * 随机数相关类
 */
public class MathUtil {
	/**
	 * 中文繁体数字
	 */
	private static final String[] HAN_DIGISTR_UPPER = new String[] { "零", "壹","贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	/**
	 * 大写数字
	 */
	private static final String[] HAN_DIGISTR_LOWER = new String[] { "○", "一","二", "三", "四", "五", "六", "七", "八", "九" };
	/**
	 * 中文数字单位
	 */
	private static final String[] HAN_DIVISTR = new String[] { "", "拾", "佰","仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟",
		"万", "拾", "佰", "仟","亿", "拾", "佰", "仟", "万","拾", "佰", "仟" };

	/**
	 * 产生一组不重复的随机数
	 * @param n 生成的个数
	 * @return int[] 返回结果
	 */
	public static int[] getUniqueRandoms(int n) {
		int[] num = new int[n];
		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < n; i++) {
			num[i] = Math.abs(r.nextInt());
		}
		return num;
	}
	
	/**
	 * 产生一组不重复的随机数
	 * @param n 生成的个数
	 * @param len 长度限制
	 * @return int[] 返回结果
	 */
	public static int[] getUniqueRandoms(int n, int len) {
		int[] num = new int[n];
		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < n; i++) {
			num[i] = Math.abs(r.nextInt() % digit(len));
		}
		return num;
	}
	
	/**
	 * 产生一个随机数功能
	 * @return int 返回结果
	 */
	public static int getRandom() {
		return Math.abs(new Random().nextInt());
	}

	/**
	 * 产生一个有长度限制的随机数功能
	* @param len 设置长度限制
	 * @return int 返回结果
	 */
	public static int getRandom(int len) {
		int maxvalue = digit(len);
		return Math.abs(new Random().nextInt(maxvalue));
	}
	
	/**
	 * @param n 设置长度限制
	 * @return int 返回随机数最大值
	 */
	private static int digit(int n) {
		int j = 1;
		for (int i = 0; i < n; i++) {
			j = 10 * j;
		}
		return j - 1;
	}
	/**
	 * 实现将传入整数字符串转化为中文大写数字格式并返回，输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零，如：902133011->玖亿零贰佰拾叁万叁仟零拾壹
	 * @param val 数字
	 * @return String 中文大写数字
	 */
	public static String numToStrUpper(int val) {
		return getIntToCnStr(HAN_DIGISTR_UPPER, String.valueOf(val));
	}

	/**
	 * 实现将传入整数字符串转化为中文简体数字格式并返回，输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零，如：902133011->九亿○二百十三万三千○十一
	 * @param val 数字
	 * @return String 中文简体数字
	 */
	public static String numToStrLower(int val) {
		return getIntToCnStr(HAN_DIGISTR_LOWER, String.valueOf(val));
	}
	
	/**
	 * 数字符串转化为中文大写数字人民币格式，如：902133011->￥玖亿零贰佰拾叁万叁仟零拾壹 元整
	 * @param val 小数数字
	 * @return String 中文大写数字人民币
	 */
	public static String numToRMBStrUpper(double val) {
		String signStr = "";
		String tailStr = "";
		long fraction;
		long integer;
		int jiao;
		int fen;

		if (val < 0) {
			val = -val;
			signStr = "负";
		}
		if (val > 99999999999999.999 || val < -99999999999999.999) {
			return "数值位数过大!";
		}
		// 四舍五入到分
		long temp = Math.round(val * 100);
		integer = temp / 100;
		fraction = temp % 100;
		jiao = (int) fraction / 10;
		fen = (int) fraction % 10;
		if (jiao == 0 && fen == 0) {
		    tailStr = "整";
		} else {
		    tailStr = HAN_DIGISTR_UPPER[jiao];
			if (jiao != 0) {
			    tailStr += "角";
			}
			// 零元后不写零几分
			if (integer == 0 && jiao == 0) { 
			    tailStr = "";
			}
			if (fen != 0) {
			    tailStr += HAN_DIGISTR_UPPER[fen] + "分";
			}
		}

		// 下一行可用于非正规金融场合，0.03只显示“叁分”而不是“零元叁分”
		// if( !integer ) return SignStr+TailStr;

		return "￥" + signStr+ getIntToCnStr(HAN_DIGISTR_UPPER, String.valueOf(integer)) + "元"+ tailStr;
	}
	
	/**
	 * 实现将传入整数字符串转化为中文简写数字人民币格式并返回，如：902133011->￥九亿○二百十三万三千○十一 元整
	 * @param val 小数数字
	 * @return String 为中文简写数字人民币
	 */
	public static String numToRMBStrLower(double val) {
		String signStr = "";
		String tailStr = "";
		long fraction;
		long integer;
		int jiao;
		int fen;

		if (val < 0) {
			val = -val;
			signStr = "负";
		}
		if (val > 99999999999999.999 || val < -99999999999999.999) {
			return "数值位数过大!";
		}
		// 四舍五入到分
		long temp = Math.round(val * 100);
		integer = temp / 100;
		fraction = temp % 100;
		jiao = (int) fraction / 10;
		fen = (int) fraction % 10;
		if (jiao == 0 && fen == 0) {
		    tailStr = "整";
		} else {
		    tailStr = HAN_DIGISTR_LOWER[jiao];
			if (jiao != 0) {
			    tailStr += "角";
			}
			// 零元后不写零几分
			if (integer == 0 && jiao == 0) { 
			    tailStr = "";
			}
			if (fen != 0) {
			    tailStr += HAN_DIGISTR_LOWER[fen] + "分";
			}
		}
		return "￥" + signStr+getIntToCnStr(HAN_DIGISTR_LOWER, String.valueOf(integer)) + "元"	+ tailStr;
	}

	/**
	 * 将实现对传入浮点小数60000000转化为货币格式显示, 如：￥60,000,000
	 * @param val 小数数字
	 * @param type 转换货币类型
	 * @return String 返回结果
	 */
	public static String formatMoney(double val, Locale type) {
		NumberFormat numFormat = NumberFormat.getCurrencyInstance(type);
		return numFormat.format(val);
	}
	
	/**
	 * 将实现对传入浮点小数转化为货币格式显示,默认本地转换格式
	 * @param val 小数数字
	 * @return String 返回结果
	 */
	public static String formatMoney(double val) {
		NumberFormat numFormat = NumberFormat.getCurrencyInstance();
		return numFormat.format(val);
	}
	/**
         * 数字字符串转化为中文大写数字格式(辅助类)
         * @param hanDigi 字符串数组
         * @param numStr 数字字符串
         * @return String 中文大写数字
         */
        private static String getIntToCnStr(String[] hanDigi, String numStr){
                String rMBStr = "";
                boolean lastzero = false;
                // 亿、万进位前有数值标记
                boolean hasvalue = false; 
                int len;
                int n;
                len = numStr.length();
                if (len > 15) {
                        return "数值过大!";
                }
                for (int i = len - 1; i >= 0; i--) {
                        if (numStr.charAt(len - i - 1) == ' ') {
                                continue;
                        }
                        n = numStr.charAt(len - i - 1) - '0';
                        if (n < 0 || n > 9) {
                                return "输入含非数字字符!";
                        }

                        if (n != 0) {
                                if (lastzero) {
                                        // 若干零后若跟非零值，只显示一个零
                                    rMBStr += hanDigi[0]; 
                                        // 除了亿万前的零不带到后面
                                        // if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) ) //
                                        // 如十进位前有零也不发壹音用此行
                                }
                                if (!(n == 1 && (i % 4) == 1 && i == len - 1)) { 
                                        // 十进位处于第一位不发壹音
                                    rMBStr += hanDigi[n];
                                }
                                // 非零值后加进位，个位为空
                                rMBStr += hanDigi[i]; 
                                // 置万进位前有值标记
                                hasvalue = true; 

                        } else {
                                if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue)) { 
                                        // 亿万之间必须有非零值方显示万
                                    // “亿”或“万”     
                                    rMBStr += hanDigi[i]; 
                                        hasvalue = false;
                                }
                        }
                        if ((i % 8) == 0 || (i % 8) == 4) {
                                hasvalue = false;
                        }
                        // 亿万前有零后不加零，如：拾万贰仟
                        lastzero = (n == 0) && (i % 4 != 0); 
                }

                if (rMBStr.length() == 0) {
                        // 输入空字符或"0"，返回"零"
                        return hanDigi[0]; 
                }
                return rMBStr;
        }
}
