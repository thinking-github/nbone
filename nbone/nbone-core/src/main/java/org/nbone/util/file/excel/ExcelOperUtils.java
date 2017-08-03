package org.nbone.util.file.excel;

/**
 * 
 * @author thinking
 *
 */
public class ExcelOperUtils {
	
	/**
	 *  当第一个输入参数等于null和""返回 第二的参数output 否则返回input
	 * @param input
	 * @param output
	 * @return
	 * @throws Exception
	 */
	public static String nullToIndex2Param (Object input,String output) throws Exception {
		 if(input==null){
			 return output;
		 }
		 else{
			 if(input instanceof String){
				 if(input.toString().trim().equals("")){
					 return output;
				 }
				 
			 }
		 }
		return input+"";
	} 

}
