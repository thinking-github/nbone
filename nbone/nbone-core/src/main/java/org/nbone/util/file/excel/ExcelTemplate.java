package org.nbone.util.file.excel;

import java.io.IOException;
import java.io.OutputStream;

/**
 * excel 基础信息模板接口
 * @author thinking
 *
 */
public interface ExcelTemplate<T> {
	
	
	public void export(OutputStream outputStream,T parameters)throws IOException;
	
	/**
	 * 获取sheet名称
	 * @return
	 */
	public String[] getSheetNames();
	
	/**
	 * 获取sheet大标题(合并单元格) eg: sheet1 = Captions[0]
	 * @return
	 */
	public String[] getCaptions();
	
	/**
	 *获取全部的sheet表头(多个sheet时)
	 * @return
	 */
	public String[][] getTitles();

}
