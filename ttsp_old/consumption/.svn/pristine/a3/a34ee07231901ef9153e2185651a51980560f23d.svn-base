package trong.lixco.com.util;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.primefaces.PrimeFaces;
//import com.google.zxing.Writer;

//import net.sf.jasperreports.engine.export.JExcelApiExporter;

public class ToolReport {
	public static void PrintReportPDF(Map<String, Object> importParam, JRDataSource beanDataSource, String reportPath) {
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, beanDataSource);
			FacesContext facesContext = FacesContext.getCurrentInstance();
			OutputStream outputStream = facesContext.getExternalContext().getResponseOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
			facesContext.responseComplete();
		} catch (JRException | IOException e) {
			e.printStackTrace();
		}
	}

	// public static void printReportExcelPattern(Map<String, Object>
	// importParam,JRDataSource beanDataSource,String reportPath){
	// try {
	// String printFileName = JasperFillManager.fillReportToFile(reportPath,
	// importParam, beanDataSource);
	// if (printFileName != null) {
	// JExcelApiExporter exporter = new JExcelApiExporter();
	//
	// exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME,
	// printFileName);
	// FacesContext facesContext = FacesContext.getCurrentInstance();
	// HttpServletResponse httpServletResponse = (HttpServletResponse)
	// FacesContext.getCurrentInstance()
	// .getExternalContext().getResponse();
	// httpServletResponse.addHeader("Content-disposition",
	// "attachment; filename=" + "excel" + ".xls");
	// ServletOutputStream servletOutputStream =
	// httpServletResponse.getOutputStream();
	// exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
	// servletOutputStream);
	// exporter.exportReport();
	// facesContext.responseComplete();
	// }
	// } catch (JRException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	public static void printReportExcelCSV(List<Object[]> listObj, String fileName) {
		// try{
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.execute("target='_blank';monitorDownload( showStatus , hideStatus)");
		// HttpServletResponse httpServletResponse = (HttpServletResponse)
		// FacesContext
		// .getCurrentInstance().getExternalContext().getResponse();
		// httpServletResponse.setCharacterEncoding("utf8");
		// httpServletResponse.addHeader("Content-disposition",
		// "attachment; filename=" + fileName
		// + ".txt");
		// ServletOutputStream servletOutputStream =
		// httpServletResponse.getOutputStream();
		// try (java.io.Writer w = new OutputStreamWriter(servletOutputStream,
		// "UTF-8")) {
		// String a="";
		// for(Object[] obj :listObj){
		// a+=obj[0]+";"+obj[1]+";"+obj[2]+";"+obj[3]+";"+obj[4]+"\n";
		//
		// }
		// w.write(a);
		// } //
		// // String a="";
		// // PrintWriter p = new PrintWriter(servletOutputStream);
		// //
		// // p.print(a);
		// FacesContext.getCurrentInstance().responseComplete();
		//
		// }catch(Exception e){
		// e.printStackTrace();
		// }
	}

	public static void printReportExcelRaw(List<Object[]> listObj, String fileName) {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("target='_blank';monitorDownload( showStatus , hideStatus)");
			HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance()
					.getExternalContext().getResponse();
			httpServletResponse.reset();
			httpServletResponse.setHeader("Content-Type", "application/vnd.ms-excel");
			httpServletResponse.setCharacterEncoding("utf8");
			httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
			ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
			// Create blank workbook
			HSSFWorkbook workbook = new HSSFWorkbook();
			// Create a blank sheet
			HSSFSheet spreadsheet = workbook.createSheet("Sheet1");

			// Create row object
			Row row;
			// This data needs to be written (Object[])
			Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
			for (int i = 0; i < listObj.size(); i++) {
				empinfo.put((i + 1) + "", listObj.get(i));
			}
			// Iterate over data and write to sheet
			Set<String> keyid = empinfo.keySet();
			int rowid = 0;
			SimpleDateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat fdtime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			for (String key : keyid) {
				row = spreadsheet.createRow(rowid++);
				Object[] objectArr = empinfo.get(key);
				int cellid = 0;
				for (Object obj : objectArr) {
					Cell cell = row.createCell(cellid++);
					// cell.setCellValue(obj + "");
					if (obj != null && obj.getClass().equals(Double.class)) {
						cell.setCellValue((double) obj);
					} else if (obj != null && obj.getClass().equals(BigDecimal.class)) {
						cell.setCellValue((double) Double.parseDouble(Objects.toString(obj)));
					} else if (obj != null && (obj.getClass().equals(Date.class)|| obj.getClass().equals(java.sql.Date.class))) {
						cell.setCellValue(fd.format((Date) obj));
					} else if (obj != null && obj.getClass().equals(Timestamp.class)) {
						cell.setCellValue(fdtime.format((Timestamp) obj));
					} else if (obj != null && obj.getClass().equals(Long.class)) {
						cell.setCellValue((long) obj);
					} else if (obj != null && obj.getClass().equals(Integer.class)) {
						cell.setCellValue((int) obj);
					} else if (obj != null && obj.getClass().equals(BigInteger.class)) {
						cell.setCellValue((int) Integer.parseInt(Objects.toString(obj)));
					} else {
						cell.setCellValue(obj + "");
					}
				}
			}

			// Write the workbook in file system
			workbook.write(servletOutputStream);
			FacesContext.getCurrentInstance().responseComplete();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printReportExcelRawXLSX(List<Object[]> listObj, String fileName) {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("target='_blank';monitorDownload( showStatus , hideStatus)");
			HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance()
					.getExternalContext().getResponse();
			httpServletResponse.reset();
			httpServletResponse.setHeader("Content-Type", "application/vnd.ms-excel");
			httpServletResponse.setCharacterEncoding("utf8");
			httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
			ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

			SXSSFWorkbook workbook = new SXSSFWorkbook();
			SXSSFSheet spreadsheet = (SXSSFSheet) workbook.createSheet("Sheet1");

			// Create row object
			Row row;
			// This data needs to be written (Object[])
			Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
			for (int i = 0; i < listObj.size(); i++) {
				empinfo.put((i + 1) + "", listObj.get(i));
			}
			// Iterate over data and write to sheet
			Set<String> keyid = empinfo.keySet();
			int rowid = 0;
			SimpleDateFormat fd = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat fdtime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			
			CellStyle cellStyleDate = workbook.createCellStyle();
			CreationHelper createHelper = workbook.getCreationHelper();
			cellStyleDate.setDataFormat(
			createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			
			for (String key : keyid) {
				row = spreadsheet.createRow(rowid++);
				Object[] objectArr = empinfo.get(key);
				int cellid = 0;
				for (Object obj : objectArr) {
					Cell cell = row.createCell(cellid++);
					// cell.setCellValue(obj + "");
					if (obj != null && obj.getClass().equals(Double.class)) {
						cell.setCellValue((double) obj);
					} else if (obj != null && obj.getClass().equals(BigDecimal.class)) {
						cell.setCellValue((double) Double.parseDouble(Objects.toString(obj)));
					} else if (obj != null && obj.getClass().equals(Date.class)) {
						cell.setCellValue(fd.format((Date) obj));
						cell.setCellStyle(cellStyleDate);
					} else if (obj != null && obj.getClass().equals(java.sql.Date.class)) {
						cell.setCellValue((java.sql.Date)obj);
						cell.setCellStyle(cellStyleDate);
					} else if (obj != null && obj.getClass().equals(Timestamp.class)) {
						cell.setCellValue(fdtime.format((Timestamp) obj));
					} else if (obj != null && obj.getClass().equals(Long.class)) {
						cell.setCellValue((long) obj);
					} else if (obj != null && obj.getClass().equals(Integer.class)) {
						cell.setCellValue((int) obj);
					} else if (obj != null && obj.getClass().equals(BigInteger.class)) {
						cell.setCellValue((int) Integer.parseInt(Objects.toString(obj)));
					} else {
						cell.setCellValue(obj + "");
					}
				}
			}

			// Write the workbook in file system
			workbook.write(servletOutputStream);
			FacesContext.getCurrentInstance().responseComplete();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static byte[] printReportExcelRawToByte(List<Object[]> listObj, String fileName) {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("target='_blank';monitorDownload( showStatus , hideStatus)");
			HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance()
					.getExternalContext().getResponse();
			httpServletResponse.reset();
			httpServletResponse.setHeader("Content-Type", "application/vnd.ms-excel");
			httpServletResponse.setCharacterEncoding("utf8");
			httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
			ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
			// Create blank workbook
			HSSFWorkbook workbook = new HSSFWorkbook();
			// Create a blank sheet
			HSSFSheet spreadsheet = workbook.createSheet("Sheet1");

			// Create row object
			Row row;
			// This data needs to be written (Object[])
			Map<String, Object[]> empinfo = new LinkedHashMap<String, Object[]>();
			for (int i = 0; i < listObj.size(); i++) {
				empinfo.put((i + 1) + "", listObj.get(i));
			}
			// Iterate over data and write to sheet
			Set<String> keyid = empinfo.keySet();
			int rowid = 0;
			SimpleDateFormat fd = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat fdtime = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			for (String key : keyid) {
				row = spreadsheet.createRow(rowid++);
				Object[] objectArr = empinfo.get(key);
				int cellid = 0;
				for (Object obj : objectArr) {
					Cell cell = row.createCell(cellid++);
					cell.setCellValue(obj + "");
					if (obj != null && obj.getClass().equals(Double.class)) {
						cell.setCellValue((double) obj);
					} else if (obj != null && obj.getClass().equals(Date.class)) {
						cell.setCellValue(fd.format((Date) obj));
					} else if (obj != null && obj.getClass().equals(Timestamp.class)) {
						cell.setCellValue(fdtime.format((Timestamp) obj));
					} else {
						cell.setCellValue(obj + "");
					}
				}
			}

			// Write the workbook in file system
			workbook.write(servletOutputStream);
			servletOutputStream.flush();
			servletOutputStream.close();
			return workbook.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
