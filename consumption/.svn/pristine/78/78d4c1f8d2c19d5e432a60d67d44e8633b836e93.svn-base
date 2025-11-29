package trong.lixco.com.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.CustomerChannel;
import lixco.com.entity.CustomerTypes;
import lixco.com.entity.Invoice;
import lixco.com.entityapi.InvoiceDTO3;
import lixco.com.entityapi.InvoiceDTO4;
import lixco.com.interfaces.IInvoiceService;
import lixco.com.service.InvoiceAPIService;

import org.jboss.logging.Logger;

import trong.lixco.com.util.MyStringUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class HoaDonHandler {
	@Inject
	private Logger logger;

	// Sau khi viết xong trong APIService thì sẽ thay invoiceService trong này
	// bằng invoiceService để lấy dữ liệu
	@Inject
	private InvoiceAPIService invoiceAPIService; // sử dụng APIService vừa viết

	// @Inject
	// private IInvoiceService invoiceService; // Lấy thông tin hóa đơn qua code

	// private List<InvoiceDTO3> listInvoice;

	public void process(String code, Date fromDate, ResponseBuilder respContent, StringBuilder contentBuilder) {
		int err = -1;
		try {
			err = getByCode(respContent, contentBuilder, code, fromDate);
		} catch (Exception e) {
			logger.error("HoaDonHandler.process:" + e.getMessage(), e);
		}
	}

	
	public int getByCode(ResponseBuilder respContent, StringBuilder contentBuilder, String code, Date fromDate) {
		int ret = -1;
		try {
			// Tìm danh sách hóa đơn theo điều kiện LIKE + fromDate
			List<Invoice> invoices = invoiceAPIService.findInvoicesByCode(code, fromDate);

			if (invoices == null || invoices.isEmpty()) {
				ret = -1;
				contentBuilder.append(CommonService.FormatResponse(ret, "Không tìm thấy hóa đơn", "data", null));
				respContent.status(Response.Status.NOT_FOUND);
			} else {
				ret = 0;

				// Chuyển danh sách hóa đơn sang danh sách DTO
				List<InvoiceDTO4> dtoList = invoices.stream().map(InvoiceDTO4::new).collect(Collectors.toList());

				// Format response JSON
				contentBuilder.append(CommonService.FormatResponse(ret, "", "data", dtoList));
				respContent.status(Response.Status.OK);
			}

		} catch (Exception e) {
			logger.error("HoaDonHandler.getByCode: " + e.getMessage(), e);
			respContent.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return ret;
	}

}
