package org.nbone.util.file.excel.jxl;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;


/**
 * 用于Excel单元格样式表
 * @author thinking  2014-10-23
 *
 */
public class FormatUtils {
	        //===================================字体=======================================
			//用于标题的字体
		    public  static WritableFont titleFont; 
			//定义表头字体
		    public  static WritableFont boldFont; 
			//定义正文字体
		    public  static WritableFont normalFont; 
		   
		    //=================================单元格样式===================================
		    // 用于标题
		    public  static WritableCellFormat WCF_TITLE;
		    // 用于表头
		    public  static WritableCellFormat WCF_HEAD;
			//用于正文,单元格全部细线
		    public  static WritableCellFormat WCF_CONTENT;
		    // 用于正文,左对齐
		    public  static WritableCellFormat WCF_CONTENT_LEFT;
		    // 用于正文,右对齐
		    public  static WritableCellFormat WCF_CONTENT_RIGHT;
		    
		    
		    //用于正文,单元格左侧中等线
		    public  static WritableCellFormat WCF_CONTENT1;
			//用于正文,单元格上侧中等线
		    public  static WritableCellFormat WCF_CONTENT2;
		    //用于正文,单元格右侧中等线
		    public  static WritableCellFormat WCF_CONTENT3;
		   //用于正文,单元格下侧中等线
		    public  static WritableCellFormat WCF_CONTENT4;
		    
		    
		   //用于正文,单元格左侧、下侧中等线
		    public  static WritableCellFormat WCF_CONTENT5;
		   //用于正文,单元格右侧、下侧中等线
		    public  static WritableCellFormat WCF_CONTENT6;
		/**
		 * 初始化单元格样式    
		 */
	    public static void initialize(){
	    	   
	    	 //===================================字体=======================================
			 //用于标题的字体
		     titleFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD); 
			 //定义表头字体
		     boldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD); 
			 //定义正文字体
		     normalFont = new WritableFont(WritableFont.ARIAL, 10); 
		   
		    //=================================单元格样式===================================
		     try {
		    	 // 用于标题
			     WCF_TITLE = new WritableCellFormat(titleFont);
				 WCF_TITLE.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
				 WCF_TITLE.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
				 WCF_TITLE.setAlignment(Alignment.CENTRE); // 水平对齐
				 WCF_TITLE.setWrap(false); // 是否换行
				 
				// 用于表头
			     WCF_HEAD = new WritableCellFormat(boldFont);
				 WCF_HEAD.setBorder(Border.ALL, BorderLineStyle.THIN); 
				 WCF_HEAD.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
				 WCF_HEAD.setAlignment(Alignment.CENTRE); // 水平对齐
				 WCF_HEAD.setWrap(false); // 是否换行
				 WCF_HEAD.setBackground(Colour.GRAY_50);
				 
				 
				//用于正文,单元格全部细线
			     WCF_CONTENT = new WritableCellFormat(normalFont);
				 WCF_CONTENT.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
				 WCF_CONTENT.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
				 WCF_CONTENT.setAlignment(Alignment.CENTRE);// 水平对齐
				 WCF_CONTENT.setWrap(false); // 是否换行
			     
			    // 用于正文,左对齐
			     WCF_CONTENT_LEFT = new WritableCellFormat(normalFont);
				 WCF_CONTENT_LEFT.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
				 WCF_CONTENT_LEFT.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
				 WCF_CONTENT_LEFT.setAlignment(Alignment.LEFT);// 水平对齐
				 WCF_CONTENT_LEFT.setWrap(false); // 是否换行
				 
				 // 用于正文,右对齐
			     WCF_CONTENT_RIGHT = new WritableCellFormat(normalFont);
			     WCF_CONTENT_RIGHT.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			     WCF_CONTENT_RIGHT.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
			     WCF_CONTENT_RIGHT.setAlignment(Alignment.RIGHT);// 水平对齐
			     WCF_CONTENT_RIGHT.setWrap(false); // 是否换行
			    
			    
			    //用于正文,单元格左侧中等线
			     WCF_CONTENT1 = new WritableCellFormat(normalFont);
				 WCF_CONTENT1.setBorder(Border.LEFT, BorderLineStyle.MEDIUM); // 线条
				 WCF_CONTENT1.setBorder(Border.RIGHT, BorderLineStyle.THIN); 
				 WCF_CONTENT1.setBorder(Border.TOP, BorderLineStyle.THIN); 
				 WCF_CONTENT1.setBorder(Border.BOTTOM, BorderLineStyle.THIN); 
				 WCF_CONTENT1.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
				 WCF_CONTENT1.setAlignment(Alignment.CENTRE);// 水平对齐
				 WCF_CONTENT1.setWrap(false); // 是否换行
			    
				//用于正文,单元格上侧中等线
			     WCF_CONTENT2 = new WritableCellFormat(normalFont);
				 WCF_CONTENT2.setBorder(Border.LEFT, BorderLineStyle.THIN); // 线条
				 WCF_CONTENT2.setBorder(Border.RIGHT, BorderLineStyle.THIN); 
				 WCF_CONTENT2.setBorder(Border.TOP, BorderLineStyle.MEDIUM); 
				 WCF_CONTENT2.setBorder(Border.BOTTOM, BorderLineStyle.THIN); 
				 WCF_CONTENT2.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
				 WCF_CONTENT2.setAlignment(Alignment.CENTRE);// 水平对齐
				 WCF_CONTENT2.setWrap(false); // 是否换行
				 
			    //用于正文,单元格右侧中等线
			     WCF_CONTENT3 = new WritableCellFormat(normalFont);
				 WCF_CONTENT3.setBorder(Border.LEFT, BorderLineStyle.THIN); // 线条
				 WCF_CONTENT3.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM); 
				 WCF_CONTENT3.setBorder(Border.TOP, BorderLineStyle.THIN); 
				 WCF_CONTENT3.setBorder(Border.BOTTOM, BorderLineStyle.THIN); 
				 WCF_CONTENT3.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
				 WCF_CONTENT3.setAlignment(Alignment.CENTRE);// 水平对齐
				 WCF_CONTENT3.setWrap(false); // 是否换行
			    
			   //用于正文,单元格下侧中等线
			     WCF_CONTENT4 = new WritableCellFormat(normalFont);
				 WCF_CONTENT4.setBorder(Border.LEFT, BorderLineStyle.THIN); // 线条
				 WCF_CONTENT4.setBorder(Border.RIGHT, BorderLineStyle.THIN); 
				 WCF_CONTENT4.setBorder(Border.TOP, BorderLineStyle.THIN); 
				 WCF_CONTENT4.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM); 
				 WCF_CONTENT4.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
				 WCF_CONTENT4.setAlignment(Alignment.CENTRE);// 水平对齐
				 WCF_CONTENT4.setWrap(false); // 是否换行
			    
			    
			   //用于正文,单元格左侧、下侧中等线
			    WCF_CONTENT5 = new WritableCellFormat(normalFont);
				WCF_CONTENT5.setBorder(Border.LEFT, BorderLineStyle.MEDIUM); // 线条
				WCF_CONTENT5.setBorder(Border.RIGHT, BorderLineStyle.THIN); 
				WCF_CONTENT5.setBorder(Border.TOP, BorderLineStyle.THIN); 
				WCF_CONTENT5.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM); 
				WCF_CONTENT5.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
				WCF_CONTENT5.setAlignment(Alignment.CENTRE);// 水平对齐
				WCF_CONTENT5.setWrap(false); // 是否换行
				
			   //用于正文,单元格右侧、下侧中等线
			    WCF_CONTENT6 = new WritableCellFormat(normalFont);
				WCF_CONTENT6.setBorder(Border.LEFT, BorderLineStyle.THIN); // 线条
				WCF_CONTENT6.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM); 
				WCF_CONTENT6.setBorder(Border.TOP, BorderLineStyle.THIN); 
				WCF_CONTENT6.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM); 
				WCF_CONTENT6.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
				WCF_CONTENT6.setAlignment(Alignment.CENTRE);// 水平对齐
				WCF_CONTENT6.setWrap(false); // 是否换行
				 
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    }
	    /**
	     * 重新初始化
	     * @see #initialize()
	     */
	    public static void reinitialize(){
	    	initialize();
	    }
		 static{
			 initialize();
		 }
	
	
}
