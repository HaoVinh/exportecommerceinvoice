package trong.lixco.com.api;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

import lixco.com.entity.HeThong;
import lixco.com.entity.InvoiceTemp;
import lixco.com.entityapi.InvoiceAsyncDTO;
import lixco.com.entityapi.InvoiceDetailTempDTO;
import lixco.com.entityapi.InvoiceTempDTO;
import lixco.com.interfaces.IInvoiceService;
import lixco.com.service.InvoiceAPIService;
import lixco.com.service.InvoiceService;
import lixco.com.common.JsonParserUtil;
import lixco.com.entity.KhoaDuLieu;
import lixco.com.entityapi.DataAPI;
import lixco.com.service.HeThongService;
import lixco.com.service.KhoaDuLieuService;
import lombok.Getter;
import lombok.Setter;

public class InvoiceTempHandler {
	@Inject
	private Logger logger;
	@Inject
	InvoiceAPIService invoiceAPIService;
	private List<InvoiceTempDTO> invoiceTemps;

	public void process(String cmd, ResponseBuilder respContent, StringBuilder contentBuilder, Date ngayNhap,
			Date toNgayNhap) {
		int err = -1;
		try {
			if (cmd.equals("list_invoice_temps")) {
				err = searchInvoiceTemp(respContent, contentBuilder, ngayNhap, toNgayNhap);

			} else {
				respContent.status(Response.Status.NOT_ACCEPTABLE);
				contentBuilder.append(CommonService.FormatResponse(err, "Not support"));
			}
		} catch (Exception e) {
			logger.error("InvoiceTempHandler.process:" + e.getMessage(), e);
		}
	}
	
	public void process2(String cmd, ResponseBuilder respContent, StringBuilder contentBuilder,String data) {
		int err = -1;
		try {
			if (cmd.equals("list_invoice_detail_temps")) {
				err = searchInvoiceDetailTemp(respContent, contentBuilder,data);

			} else {
				respContent.status(Response.Status.NOT_ACCEPTABLE);
				contentBuilder.append(CommonService.FormatResponse(err, "Not support"));
			}
		} catch (Exception e) {
			logger.error("InvoiceTempHandler.process:" + e.getMessage(), e);
		}
	}


	public int searchInvoiceTemp(ResponseBuilder respContent, StringBuilder contentBuilder, Date ngayNhap,
			Date toNgayNhap) {
		int ret = -1;
		try {
			invoiceTemps = invoiceAPIService.searchInvoiceTemp(ngayNhap, toNgayNhap);
			ret = 0;
			contentBuilder.append(CommonService.FormatResponse(ret, "", "list_invoice_temps", invoiceTemps));
			respContent.status(Response.Status.OK);

		} catch (Exception e) {
			e.printStackTrace();
			respContent.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return ret;
	}
	public int searchInvoiceDetailTemp(ResponseBuilder respContent, StringBuilder contentBuilder, String data) {
	    int ret = -1;
	    try {
	        if (data == null || data.trim().isEmpty()) {
	            contentBuilder.append(CommonService.FormatResponse(ret, "Dữ liệu đầu vào không hợp lệ!"));
	            respContent.status(Response.Status.BAD_REQUEST);
	            return ret;
	        }

	        String[] dataArray = data.split(",");
	        if (dataArray.length < 2) {
	            contentBuilder.append(CommonService.FormatResponse(ret, "Tham số không hợp lệ!"));
	            respContent.status(Response.Status.BAD_REQUEST);
	            return ret;
	        }

	        Long idInvoice;
	        try {
	            idInvoice = Long.parseLong(dataArray[1].trim());
	        } catch (NumberFormatException e) {
	            contentBuilder.append(CommonService.FormatResponse(ret, "ID Invoice không hợp lệ!"));
	            respContent.status(Response.Status.BAD_REQUEST);
	            return ret;
	        }

	        List<InvoiceDetailTempDTO> invoiceDetailTemps = invoiceAPIService.searchInvoiceDetailTemp(idInvoice);
	        if (invoiceDetailTemps.isEmpty()) {
	            ret = -1;
	            contentBuilder.append(CommonService.FormatResponse(ret, "Không tìm thấy dữ liệu!"));
	            respContent.status(Response.Status.OK);
	        } else {
	            ret = 0;
	            contentBuilder.append(CommonService.FormatResponse(ret, "", "list_invoice_detail_temps", invoiceDetailTemps));
	            respContent.status(Response.Status.OK);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        contentBuilder.append(CommonService.FormatResponse(-1, "Lỗi hệ thống: " + e.getMessage()));
	        respContent.status(Response.Status.INTERNAL_SERVER_ERROR);
	    }
	    return ret;
	}
}