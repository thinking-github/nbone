package org.nbone.util.file.excel.jxl;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import org.nbone.util.DateUtil;

import jxl.BooleanCell;
import jxl.BooleanFormulaCell;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.ErrorCell;
import jxl.FormulaCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.NumberFormulaCell;
import jxl.Sheet;
import jxl.StringFormulaCell;
import jxl.Workbook;
import jxl.biff.EmptyCell;


/**
 * excel操作类    ExcelOperator
 * <p/>
 * 
 * @author <a href="mailto:holin@huijisoft.com">Holin Ding</a>
 * @version Date: 2013-2-1 下午9:18:05
 * @serial 1.0
 * @since 2013-2-1 下午9:18:05
 */

public class ExcelOper {
	private Workbook workbook;
	private Sheet currentSheet;

	public ExcelOper() {
	}

	/**
	 * 构造方法-通过文件生成 Workbook
	 * 
	 * @param file
	 */
	public ExcelOper(File file) {
		Workbook workbook = ExcelUtils.getWorkbook(file);
		if (workbook != null) {
			setWorkbook(workbook);
		}
	}

	/**
	 * 构造方法-通过输入流生成Workbook
	 * 
	 * @param ins
	 */
	public ExcelOper(InputStream ins) {
		Workbook workbook = ExcelUtils.getWorkbook(ins);
		if (workbook != null) {
			setWorkbook(workbook);
		}
	}

	/**
	 * 构造方法-通过Workbook生成一个Workbook
	 * 
	 * @param workbook
	 */
	public ExcelOper(Workbook workbook) {
		setWorkbook(workbook);
	}

	/**
	 * 设置当前的Workbook及当前的Sheet表为第一张Sheet表
	 * 
	 * @param workbook
	 */
	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
		this.currentSheet = workbook.getSheet(0);
	}

	/**
	 * 设置为指定的Sheet表
	 * 
	 * @param sheetNum
	 */
	public void setCurrentSheet(int sheetNum) {
		this.currentSheet = workbook.getSheet(sheetNum);
	}

	/**
	 * 根据当前行列的坐标获得该单元格内容
	 * 
	 * @param rowNum
	 * @param colNum
	 * @return
	 */
	public Cell getCell(int rowNum, int colNum) {
		return currentSheet.getCell(rowNum, colNum);
	}

	/**
	 * 获得指定行的一整行数据,返回单元格数组
	 * 
	 * @param rowNum
	 * @return
	 */
	public Cell[] getRowCells(int rowNum) {
		return currentSheet.getRow(rowNum);
	}

	/**
	 * 获得指定列的一整列数据,返回单元格数组
	 * 
	 * @param colNum
	 * @return
	 */
	public Cell[] getColCells(int colNum) {
		return currentSheet.getColumn(colNum);
	}

	/**
	 * 获得当前Sheet表的总行数
	 * 
	 * @return
	 */
	public int getRows() {
		return currentSheet.getRows();
	}

	/**
	 * 获得当前Sheet表的总列数
	 * 
	 * @return
	 */
	public int getCols() {
		return currentSheet.getColumns();
	}

	public EmptyCell getEmptyCell(int rowNum, int colNum) {
		Cell cell = getCell(rowNum, colNum);
		if (cell instanceof EmptyCell) {
			return (EmptyCell) cell;
		}

		return null;
	}

	public BooleanCell getBooleanCell(int rowNum, int colNum) {
		Cell cell = getCell(rowNum, colNum);
		if (cell instanceof BooleanCell) {
			return (BooleanCell) cell;
		}

		return null;
	}

	public BooleanFormulaCell getBooleanFormulaCell(int rowNum, int colNum) {
		Cell cell = getCell(rowNum, colNum);
		if (cell instanceof BooleanFormulaCell) {
			return (BooleanFormulaCell) cell;
		}

		return null;
	}

	public DateCell getDateCell(int rowNum, int colNum) {
		Cell cell = getCell(rowNum, colNum);
		if (cell instanceof DateCell) {
			return (DateCell) cell;
		}

		return null;
	}

	public ErrorCell getErrorCell(int rowNum, int colNum) {
		Cell cell = getCell(rowNum, colNum);
		if (cell instanceof ErrorCell) {
			return (ErrorCell) cell;
		}

		return null;
	}

	public FormulaCell getFormulaCell(int rowNum, int colNum) {
		Cell cell = getCell(rowNum, colNum);
		if (cell instanceof FormulaCell) {
			return (FormulaCell) cell;
		}

		return null;
	}

	public LabelCell getLabelCell(int rowNum, int colNum) {
		Cell cell = getCell(rowNum, colNum);
		if (cell instanceof LabelCell) {
			return (LabelCell) cell;
		}

		return null;
	}

	public NumberCell getNumberCell(int rowNum, int colNum) {
		Cell cell = getCell(rowNum, colNum);
		if (cell instanceof NumberCell) {
			return (NumberCell) cell;
		}

		return null;
	}

	public NumberFormulaCell getNumberFormulaCell(int rowNum, int colNum) {
		Cell cell = getCell(rowNum, colNum);
		if (cell instanceof NumberFormulaCell) {
			return (NumberFormulaCell) cell;
		}

		return null;
	}

	public StringFormulaCell getStringFormula(int rowNum, int colNum) {
		Cell cell = getCell(rowNum, colNum);
		if (cell instanceof StringFormulaCell) {
			return (StringFormulaCell) cell;
		}

		return null;
	}

	public Date getCellDate(int rowNum, int colNum) {
		DateCell dateCell = getDateCell(rowNum, colNum);
		return dateCell.getDate();
	}

	public String getCellStr(int rowNum, int colNum) {
		LabelCell labelCell = getLabelCell(rowNum, colNum);
		return labelCell.getContents();
	}

	public String getCellStrValue(int rowNum, int colNum) {
		String str = null;
		Cell cell = getCell(rowNum, colNum);
		if (cell.getType() == CellType.LABEL) {
            LabelCell labelcell = (LabelCell)cell;
            str = labelcell.getString().trim();
        } else if (cell.getType() == CellType.NUMBER){
        	str = cell.getContents();
        } else if (cell.getType() == CellType.DATE) {
            DateCell datcell = (DateCell)cell;
            str = DateUtil.formatDate(datcell.getDate(), "yyyy-MM-dd");
        } else {
        	str = cell.getContents().toString().trim();
        }
		return str;
	}
	
	public void close() {
		if (workbook != null) {
			workbook.close();
		}
	}

}
