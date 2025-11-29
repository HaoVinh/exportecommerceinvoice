package trong.lixco.com.api;

import java.lang.reflect.Type;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;
import lixco.com.common.JsonParserUtil;
import lixco.com.entity.HeThong;
import lixco.com.entity.KhoaDuLieu;
import lixco.com.entityapi.DataAPI;
import lixco.com.entityapi.InvoiceAsyncDTO;
import lixco.com.entityapi.InvoiceDetailTempDTO;
import lixco.com.entityapi.InvoiceDetailTempJSON;
import lixco.com.entityapi.InvoiceTempAsyncDTO;
import lixco.com.service.HeThongService;
import lixco.com.service.InvoiceAPIService;
import lixco.com.service.KhoaDuLieuService;
import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.account.servicepublics.AccountServicePublic;
import trong.lixco.com.account.servicepublics.AccountServicePublicProxy;
import trong.lixco.com.account.servicepublics.Program;
import trong.lixco.com.account.servicepublics.Role;
import trong.lixco.com.apitaikhoan.AccountData;
import trong.lixco.com.apitaikhoan.AccountDataService;
import trong.lixco.com.entity.Branch;
import trong.lixco.com.entity.Employee;
import trong.lixco.com.info.LoginDTO;
import trong.lixco.com.service.BranchService;
import trong.lixco.com.servicepublic.EmployeeDTO;
import trong.lixco.com.servicepublic.EmployeeServicePublic;
import trong.lixco.com.servicepublic.EmployeeServicePublicProxy;

@Path("/data")
public class InvoiceTempServlet {
	@Inject
	private InvoiceTempHandler invoiceTempHandler;
	@Inject
	private Logger logger;
	@Context
	private HttpServletRequest servletRequest;

	@GET
	@Path("/invoicetemp")
	@Produces("application/json; charset=utf-8")
	public Response getInvoiceTemp(@QueryParam(DefinedName.PARAM_COMMAND) String cmd,
			@QueryParam(DefinedName.PARAM_SDATE) String fromDateStr,
			@QueryParam(DefinedName.PARAM_EDATE) String toDateStr, @QueryParam("codeNV") String codeNV)
			throws IOException, ServletException {

		String result = "";
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date ngayNhap = fromDateStr != null ? dateFormat.parse(fromDateStr) : null;
			Date toNgayNhap = toDateStr != null ? dateFormat.parse(toDateStr) : null;

			StringBuilder contentBuilder = new StringBuilder();

			if (cmd != null && fromDateStr != null && toDateStr != null) {
				invoiceTempHandler.process(cmd, responseBuilder, contentBuilder, ngayNhap, toNgayNhap, codeNV);
			} else {
				contentBuilder.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
			}

			result = contentBuilder.toString();
		} catch (Exception e) {
			logger.error("InvoiceTempServlet.getInvoiceTemp: " + e.getMessage(), e);
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return responseBuilder.entity(result).build();
	}

	@GET
	@Path("/invoicedetailtemp")
	@Produces("application/json; charset=utf-8")
	public Response getInvoiceDetailTemp(@QueryParam(DefinedName.PARAM_COMMAND) String cmd,
			@QueryParam(DefinedName.PARAM_DATA) String data) {
		String result = "";
		ResponseBuilder responseBuilder = Response.status(Response.Status.OK);
		StringBuilder contentBuilder = new StringBuilder();

		try {
			if (cmd == null || data == null) {
				contentBuilder.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
				responseBuilder.status(Response.Status.BAD_REQUEST);
			} else {
				invoiceTempHandler.process2(cmd, responseBuilder, contentBuilder, data);
			}
			result = contentBuilder.toString();

		} catch (Exception e) {
			logger.error("InvoiceTempServlet.getInvoiceDetailTemp: " + e.getMessage(), e);
			contentBuilder.append(CommonModel.toJson(-1, "Lỗi hệ thống: " + e.getMessage()));
			result = contentBuilder.toString();
			responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return responseBuilder.entity(result).build();
	}

	@POST
	@Path("/saveinvoicetemp")
	@Produces("application/json; charset=utf-8")
	public Response saveInvoiceTemp(String data) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			if (data != null && !data.isEmpty()) {
				Gson gson = JsonParserUtil.getGson();
				DataAPI dataAPI = gson.fromJson(data, DataAPI.class);
				if (dataAPI != null && dataAPI.getData() != null) {
					InvoiceTempAsyncDTO invoiceAsyncDTO = gson.fromJson(dataAPI.getData(), InvoiceTempAsyncDTO.class);
					luuhoadon(invoiceAsyncDTO, result, responseBuilder);
				} else {
					result.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
					responseBuilder.status(Response.Status.BAD_REQUEST);
				}
			} else {
				result.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
				responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.append(CommonModel.toJson(-1, MessagesAPI.MALFORMED_DATA));
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return responseBuilder.entity(result.toString()).build();
	}

	@POST
	@Path("/saveinvoicedetailtemp")
	@Produces("application/json; charset=utf-8")
	public Response saveInvoiceDetailTemp(String data) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		Map<String, Object> responseMap = new HashMap<>();
		try {
			if (data != null && !data.isEmpty()) {
				Gson gson = JsonParserUtil.getGson();
				DataAPI dataAPI = gson.fromJson(data, DataAPI.class);
				if (dataAPI != null && dataAPI.getData() != null) {
//                    System.out.println("dataAPI.getData(): " + dataAPI.getData());
					// Parse dataAPI.getData() thành InvoiceDetailTempWrapper
					InvoiceDetailTempJSON wrapper = gson.fromJson(dataAPI.getData(), InvoiceDetailTempJSON.class);
					List<InvoiceDetailTempDTO> invoiceDetailTempDTOs = wrapper.getInvoiceDetailTemps();

//                    System.out.println("Parsed invoiceDetailTempDTOs: " + invoiceDetailTempDTOs);

					if (invoiceDetailTempDTOs != null && !invoiceDetailTempDTOs.isEmpty()) {
						int saveResult = invoiceAPIService.luuChiTietHoaDonTam(invoiceDetailTempDTOs, result);
						if (saveResult == 0) {
							responseMap.put("err", 0);
							responseMap.put("msg", "Lưu chi tiết hóa đơn tạm thành công!");
							responseMap.put("data", invoiceDetailTempDTOs);
							responseBuilder.status(Response.Status.OK);
						} else {
							responseMap.put("err", -1);
							responseMap.put("msg", result.toString());
							responseBuilder.status(Response.Status.BAD_REQUEST);
						}
					} else {
						responseMap.put("err", -1);
						responseMap.put("msg", DefinedName.RESP_MSG_INVALID_REQUEST);
						responseBuilder.status(Response.Status.BAD_REQUEST);
					}
				} else {
					responseMap.put("err", -1);
					responseMap.put("msg", DefinedName.RESP_MSG_INVALID_REQUEST);
					responseBuilder.status(Response.Status.BAD_REQUEST);
				}
			} else {
				responseMap.put("err", -1);
				responseMap.put("msg", DefinedName.RESP_MSG_INVALID_REQUEST);
				responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("err", -1);
			responseMap.put("msg", MessagesAPI.MALFORMED_DATA + ": " + e.getMessage());
			responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return responseBuilder.entity(new Gson().toJson(responseMap)).build();
	}

	@Inject
	InvoiceAPIService invoiceAPIService;
	@Inject
	HeThongService heThongService;

	private void luuhoadon(InvoiceTempAsyncDTO invoiceAsyncDTO, StringBuilder result, ResponseBuilder responseBuilder) {
		try {
			if (lock(new Date()) == false) {
				StringBuilder errors = new StringBuilder();
				int res = invoiceAPIService.taoHoaDonTempAPI(invoiceAsyncDTO, errors);
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("err", res);
				jsonObject.addProperty("msg", errors.toString());
				responseBuilder.status(Response.Status.OK);
				result.append(jsonObject);
			} else {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("err", -1);
				jsonObject.addProperty("msg", "Tháng đã khóa.");
				responseBuilder.status(Response.Status.OK);
				result.append(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseBuilder.status(Response.Status.BAD_REQUEST);
		}
	}

	@Inject
	KhoaDuLieuService khoaDuLieuService;

	public boolean lock(Date date) {
		try {
			KhoaDuLieu lt;
			int month = (date.getMonth() + 1);
			int year = (date.getYear() + 1900);
			lt = khoaDuLieuService.findByMonthYear(month, year);
			if (lt != null) {
				return lt.isKkhoa();
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

}
