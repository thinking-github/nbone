package org.nbone.util.file.excel.poi;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.nbone.util.file.excel.ExcelTemplate;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 *  apache poi
 * 
 * @author thinking
 *
 */
public abstract class AbstractPoiExcelTemplate<T> implements ExcelTemplate<T> {
	

	/**
	 * 默认单元格宽度
	 */
	private static final int DEFAULT_COLUMN_WIDTH = 7000;
	/**
	 * excel工作薄
	 */
	protected Workbook workbook;
	/**
	 * excel sheet页
	 */
	protected List<Sheet> sheets = new ArrayList<Sheet>();
	/**
	 * 表头
	 */
	protected String[][] titles;
	/**
	 * 数据样式
	 */
	protected CellStyle captionRowSytle;
	/**
	 * 表头样式
	 */
	protected CellStyle titleRowStyle;
	/**
	 * 数据样式
	 */
	protected CellStyle bodyRowStyle;
	/**
	 */
	protected Map<Integer, Boolean> hasCaptionMap = new HashMap<Integer, Boolean>();
	/**
	 */
	protected int columnWidth = DEFAULT_COLUMN_WIDTH;
	/**
	 */
	protected T parameters;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.haier.openplatform.excel.ExcelExportService#doExport(java.io.OutputStream
	 * )
	 */
	@Override
	public void export(OutputStream outputStream, T parameters)throws IOException {
		String[] sheetNames = this.getSheetNames();
		Validate.notEmpty(sheetNames);
		Validate.notNull(outputStream);
		this.parameters = parameters;
		this.workbook = new SXSSFWorkbook(getRowAccessWindowSize());
		this.titles = this.getTitles();
		this.captionRowSytle = crateCaptionCellStyle();
		this.titleRowStyle = crateTitleCellStyle();
		this.bodyRowStyle = crateBodyCellStyle();
		this.afterCreateWorkBook();

		for (int i = 0; i < sheetNames.length; i++) {
			//build sheet 
			Sheet sheet = workbook.createSheet(sheetNames[i]);
			this.sheets.add(sheet);
			afterBuildSheet(i);
			// build title
			buildCaption(i);
			buildTitle(i);
			afterBuildTitle(i);
			
			//build body content
			buildBody(i);
			afterBuildBody(i);
		}
		this.workbook.write(outputStream);
	}

	/**
	 * 
	 * @param row 单元格行
	 * @param index 单元格索引
	 * @param cellValue
	 * @param cellStyle
	 */
	protected void createStyledCell(Row row, int index, String cellValue,CellStyle cellStyle) {
		Cell cell = row.createCell(index);
		cell.setCellValue(cellValue);
		cell.setCellStyle(cellStyle);
	}

	/**
	 */
	protected void afterCreateWorkBook() {
	}

	/**
	 * 
	 * @return
	 */
	protected CellStyle crateCaptionCellStyle() {
		Font font = workbook.createFont();
		font.setColor(Font.COLOR_NORMAL);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(false);
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		return cellStyle;
	}

	/**
	 * 表头单元格样式
	 * 
	 * @return
	 */
	protected CellStyle crateTitleCellStyle() {
		Font font = workbook.createFont();
		font.setColor(Font.COLOR_NORMAL);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(false);
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		short border = 1;
		setCellBorder(cellStyle, border, border, border, border);
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		return cellStyle;
	}

	/**
	 * 
	 * @param cellStyle
	 * @param top
	 * @param bottom
	 * @param left
	 * @param right
	 */
	protected void setCellBorder(CellStyle cellStyle, short top, short bottom,
			short left, short right) {
		cellStyle.setBorderBottom(bottom);
		cellStyle.setBorderLeft(left);
		cellStyle.setBorderRight(right);
		cellStyle.setBorderTop(top);
	}

	/**
	 * 创建默认的数据单元格样式,子类如果需要可以重写实现多样化
	 * 
	 * @return
	 */
	protected CellStyle crateBodyCellStyle() {
		Font font = workbook.createFont();
		// font.setColor(HSSFColor.BLUE_GREY.index);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(false);
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		short border = 1;
		setCellBorder(cellStyle, border, border, border, border);
		return cellStyle;
	}

	/**
	 * 
	 * @param sheetIndex
	 * @return
	 */
	protected Sheet getSheet(int sheetIndex) {
		return this.sheets.get(sheetIndex);
	}

	/**
	 * 
	 * @param sheetIndex
	 */
	protected void afterBuildSheet(int sheetIndex) {
	}

	/**
	 * 
	 * @param sheetIndex
	 */
	protected void buildCaption(int sheetIndex) {
		Sheet sheet = getSheet(sheetIndex);
		String[] captions = this.getCaptions();
		hasCaptionMap.put(sheetIndex, false);
		if (captions != null && captions.length >= sheetIndex + 1) {
			String caption = captions[sheetIndex];
			if (StringUtils.isNotBlank(caption)) {
				Row row = sheet.createRow(0);
				int lastColumn = calculateLastColumn(sheetIndex);
				CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0,0, lastColumn);
				sheet.addMergedRegion(cellRangeAddress);
				createStyledCell(row, 0, caption, this.captionRowSytle);
				hasCaptionMap.put(sheetIndex, true);
			}
		}
	}

	/**
	 * 
	 * @param sheetIndex
	 * @return
	 */
	protected int calculateLastColumn(int sheetIndex) {
		if (this.titles != null && sheetIndex <= this.titles.length - 1
				&& this.titles[sheetIndex] != null) {
			return this.titles[sheetIndex].length - 1;
		} else {
			return 1;
		}
	}

	/**
	 * 
	 * @param sheetIndex
	 */
	protected void buildTitle(int sheetIndex) {
		if (this.titles.length < sheetIndex + 1) {
			return;
		}
		String[] ts = this.titles[sheetIndex];
		if (ts == null) {
			return;
		}
		Sheet sheet = this.getSheet(sheetIndex);
		int titleStartIndex = this.getTitleStartIndex(sheetIndex);
		Row rowTitle = sheet.createRow(titleStartIndex);
		for (int i = 0; i < ts.length; i++) {
			sheet.setColumnWidth(i, columnWidth);
			createStyledCell(rowTitle, i, ts[i], this.titleRowStyle);
		}
	}

	/**
	 * 
	 * @param sheetIndex
	 * @return
	 */
	protected int getBodyStartIndex(int sheetIndex) {
		int captionRow = getTitleStartIndex(sheetIndex);
		int titleRow = 0;
		if (this.titles != null && this.titles.length >= sheetIndex + 1) {
			if (titles[sheetIndex] != null && titles[sheetIndex].length > 0) {
				titleRow = 1;
			}
		}
		return captionRow + titleRow;
	}

	protected int getTitleStartIndex(int sheetIndex) {
		return this.hasCaptionMap.get(sheetIndex) ? 1 : 0;
	}

	/**
	 * 
	 * @param sheetIndex
	 */
	protected void afterBuildTitle(int sheetIndex) {
	}

	/**
	 * 
	 * @param sheetIndex
	 */
	protected abstract void buildBody(int sheetIndex);

	/**
	 * 
	 * @param sheetIndex
	 */
	protected void afterBuildBody(int sheetIndex) {
	}

	
	@Override
	public String[] getCaptions() {
		return new String[] {};
	}

	public int getRowAccessWindowSize() {
		return 200;
	}
	
	public String toString(Object obj) {
		if(obj == null){
			return "";
		}
		return obj.toString();
	}

	

}
