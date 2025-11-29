package trong.lixco.com.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

public class MyUtilPDF {

	public static byte[] mergePDF(List<byte[]> pdfFilesAsByteArray) throws DocumentException, IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		Document document = null;
		PdfCopy writer = null;
		for (byte[] pdfByteArray : pdfFilesAsByteArray) {

			try {
				PdfReader reader = new PdfReader(pdfByteArray);
				int numberOfPages = reader.getNumberOfPages();

				if (document == null) {
					document = new Document(reader.getPageSizeWithRotation(1));
					writer = new PdfCopy(document, outStream); // new
					document.open();
				}
				PdfImportedPage page;
				for (int i = 0; i < numberOfPages;) {
					++i;
					page = writer.getImportedPage(reader, i);
					writer.addPage(page);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		document.close();
		outStream.close();
		return outStream.toByteArray();

	}

}
