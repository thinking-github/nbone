package org.nbone.util.file.excel.jxl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletResponse;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.beanutils.BeanUtils;
import org.nbone.util.WebIOUtils;


/**
 * 导出EXCEL公共类
 * 
 * @author Thinking  2014-10-29
 * @serial 1.0
 */
public class ExcelUtils {
	
	/**
	 * 创建 Excel Workbook
	 * @param object 传入 File对象 或者是 OutputStream对象
	 * @return
	 */
	
	public static WritableWorkbook createWorkbook(Object object){
		
		WritableWorkbook wbook = null;
		try {
			if (object instanceof File) {
				File file = (File) object;
				// 建立excel文件
				wbook = Workbook.createWorkbook(file);
				
			} else if (object instanceof OutputStream) {
				OutputStream os = (OutputStream) object;
				 // 建立excel文件
				wbook = Workbook.createWorkbook(os);
			} else {
				throw new IllegalArgumentException("Please input File Object or OutputStream Object."); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return wbook;
	}
	
	/**
	 * 获取 Excel Workbook
	 * @param object 传入 File对象 或者是 OutputStream对象
	 * @return
	 */
	public static Workbook getWorkbook(Object object){
		Workbook wbook = null;
		try {
			if (object instanceof File) {
				File file = (File) object;
				// 获取excel文件
				wbook = Workbook.getWorkbook(file);
				
			} else if (object instanceof InputStream) {
				InputStream ins = (InputStream) object;
				 //获取excel文件
				wbook = Workbook.getWorkbook(ins);
			} else {
				throw new IllegalArgumentException("Please input File Object or InputStream Object."); 
			}
			
		}  catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
		return wbook;
	}
	
	public static void readExcelData(File file) throws Exception {
		/*
		 *  //从输入流创建Workbook
		 *  Workbook rwb = Workbook.getWorkbook(is);
		 * //获取第一张Sheet表 
		 * Sheet rs = rwb.getSheet(0); 
		 * // 获取Sheet表中的列数 
		 * int colNum = rs.getColumns(); 
		 * // 获取Sheet表中的行数 
		 * int rowNum = rs.getRows();
		 * 
		 */
		ExcelOper eo = new ExcelOper(file);
		int colNum = eo.getCols();
		int rowNum = eo.getRows();

	}
	/**
	 * 导出EXCEL文件
	 * @author Thinking  Mar 26, 2011 4:20:55 PM
	 * @param object 传入 File对象 或者是 OutputStream对象
	 * @param title EXCEL工作簿名称及导出的报表名称
	 * @param columns 表头信息数据列集合
	 * @param dataList 导出的数据
	 * @throws Exception
	 */
	public static void export(Object object, String title, List<ExcelColumn> columns,List<Object> dataList) 
			throws Exception {
		createExcelData(object, title, columns, dataList);
	}
	public static void export(ServletResponse response, String title, List<ExcelColumn> columns,List<Object> dataList) 
			throws Exception {
		OutputStream os = WebIOUtils.getExcelOutputStream(response, title);
		createExcelData(os, title, columns, dataList);
	}

	/**
	 * 创建EXCEL数据
	 * @author Thinking 
	 * @param output
	 * @param title EXCEL工作簿名称及导出的报表名称
	 * @param columns
	 * @param dataList
	 * @throws Exception
	 */
	private static void createExcelData(Object output, String title, List<ExcelColumn> columns, List<Object> dataList)
	        throws Exception {
		try {
			
				WritableWorkbook wbook = createWorkbook(output);
				WritableSheet wsheet = wbook.createSheet(title, 0); // sheet名称
				Label wlabel; // Excel表格的Cell
				ExcelColumn column;
		        FormatUtils.reinitialize();
				/**************** 添加标题 *************************/
				wsheet.mergeCells(0, 0, columns.size() - 1, 0); // 合并第一行单元格，加入公司名称
				// wsheet.addCell(new Label(0, 0, EcardConstants.COMPANY_NAME,
				// wcf_title));
				wsheet.mergeCells(0, 1, columns.size() - 1, 1); // 合并第二行单元格，加入报表名称
				wsheet.addCell(new Label(0, 1, title, FormatUtils.WCF_TITLE));
	
				// 根据原数据和map来创建Excel的表头
				for (int j = 0; j < columns.size(); j++) {
					column = columns.get(j);
					wlabel = new Label(j, 2, column.getColumnTitle(), FormatUtils.WCF_HEAD);
					wsheet.addCell(wlabel);
				}
				// 根据原数据和map来创建Excel的列名
				for (int i = 0; i < dataList.size(); i++) {
					Object obj = dataList.get(i);
					for (int j = 0; j < columns.size(); j++) {
						column = columns.get(j);
						wlabel = new Label(j, i + 3, BeanUtils.getProperty(obj, column.getColumnName()), FormatUtils.WCF_CONTENT);
						wsheet.setColumnView(j, column.getWidth());
						wsheet.addCell(wlabel);
					}
				}
				wbook.write(); // 写入文件
				wbook.close();
			
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
			WebIOUtils.close(output);
		}
		
	}



}
