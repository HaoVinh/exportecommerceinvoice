package trong.lixco.com.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MyUtilExcel {

	public static Object getCellValue(Cell cell) {

		if (cell.getCellType().equals(CellType.STRING)) {
			return cell.getStringCellValue();
		} else if (cell.getCellType().equals(CellType.BOOLEAN)) {
			return cell.getBooleanCellValue();
		} else if (cell.getCellType().equals(CellType.NUMERIC)) {
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				return cell.getNumericCellValue();
			}
		} else if (cell.getCellType().equals(CellType.FORMULA)) {
			switch (cell.getCachedFormulaResultType()) {
			case NUMERIC:
				return cell.getNumericCellValue();
			case STRING:
				return cell.getStringCellValue();
			}
		}
		return null;
	}

	public static String getCellValueMaSP(Cell cell) {

		if (cell.getCellType().equals(CellType.STRING)) {
			return cell.getStringCellValue();
		} else if (cell.getCellType().equals(CellType.NUMERIC)) {
			Object obj= cell.getNumericCellValue();
			return (int)Double.parseDouble(Objects.toString(obj, "0"))+"";
		} else if (cell.getCellType().equals(CellType.FORMULA)) {
			switch (cell.getCachedFormulaResultType()) {
			case NUMERIC:
				return (int)Double.parseDouble(Objects.toString(cell.getNumericCellValue(), "0"))+"";
			case STRING:
				return cell.getStringCellValue();
			}
		}
		return null;
	}

	public static Object getCellValue(Cell cell, boolean StrReturn) {

		if (cell.getCellType().equals(CellType.STRING)) {
			return cell.getStringCellValue();
		} else if (cell.getCellType().equals(CellType.BOOLEAN)) {
			return cell.getBooleanCellValue();
		} else if (cell.getCellType().equals(CellType.NUMERIC)) {
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				if (StrReturn) {
					return Objects.toString((long) cell.getNumericCellValue(), "");
				} else {
					return cell.getNumericCellValue();
				}
			}
		} else if (cell.getCellType().equals(CellType.FORMULA)) {
			switch (cell.getCachedFormulaResultType()) {
			case NUMERIC:
				if (StrReturn) {
					return Objects.toString((long) cell.getNumericCellValue(), "");
				} else {
					return cell.getNumericCellValue();
				}
			case STRING:
				return cell.getStringCellValue();
			}
		}
		return null;
	}

	public static String getCellValueString(Cell cell) {
		DataFormatter formatter = new DataFormatter();
		return formatter.formatCellValue(cell);

	}

	public static Object getCellValue(Workbook workbook, Cell cell) {
		// Tạo đối tượng FormulaEvaluator từ Workbook
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		// Tính toán giá trị của ô nếu nó có kiểu dữ liệu là công thức
		if (cell.getCellType() == CellType.FORMULA) {
			try {
				CellValue cellValue = evaluator.evaluate(cell);
				switch (cellValue.getCellType()) {
				case BOOLEAN:
					boolean b = cellValue.getBooleanValue();
					return b;
				case NUMERIC:
					double d = cellValue.getNumberValue();
					return d;
				case STRING:
					String s = cellValue.getStringValue();
					return s;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (cell.getCellType() == CellType.NUMERIC) {
			double d = cell.getNumericCellValue();
			return d;
		} else if (cell.getCellType() == CellType.STRING) {
			String s = cell.getStringCellValue();
			return s;
		}
		return null;
	}

	public static Workbook getWorkbook(InputStream inputStream, String nameFile) throws IOException {
		Workbook workbook = null;
		if (nameFile.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else if (nameFile.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("The specified file is not Excel file");
		}

		return workbook;
	}
}
