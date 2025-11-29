package trong.lixco.com.api;

import io.jsonwebtoken.io.IOException;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import lixco.com.common.JsonParserUtil;
import lixco.com.entity.HeThong;
import lixco.com.entity.KhoaDuLieu;
import lixco.com.entityapi.DataAPI;
import lixco.com.entityapi.InvoiceApiDTO;
import lixco.com.entityapi.InvoiceAsyncDTO;
import lixco.com.entityapi.InvoiceDTO2;
import lixco.com.entityapi.ProductKTDTO;
import lixco.com.interfaces.IInventoryService;
import lixco.com.service.HeThongService;
import lixco.com.service.InventoryService;
import lixco.com.service.InvoiceAPIService;
import lixco.com.service.KhoaDuLieuService;
import trong.lixco.com.util.MyUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Path("data")
public class InvoiceServlet {
	@Context
	private HttpServletRequest request;
	@Inject
	InvoiceAPIService invoiceAPIService;
	@Inject
	HeThongService heThongService;

	@GET
	@Path("dshoadon")
	@Produces("application/json; charset=utf-8")
	public Response dshoadon(@QueryParam(DefinedName.PARAM_SDATE) String sDate,
			@QueryParam(DefinedName.PARAM_EDATE) String eDate, @QueryParam("maxuatnhap") String maxuatnhap,
			@QueryParam("idloaibo") String idloaibo, @QueryParam("statusXN") String statusXN)
			throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			Date startDate = MyUtil.chuyensangStrApiDate(sDate);
			Date endDate = MyUtil.chuyensangStrApiDate(eDate);
//			String statusXN="1"; //0: lấy theo các mã xuất nhập này (IN); 1: loại bỏ mã xuất nhập này (NOT IN)
			List<InvoiceApiDTO> invoiceDTO2s = invoiceAPIService.search(startDate, endDate, maxuatnhap, idloaibo,
					statusXN);
			if (invoiceDTO2s.size() != 0) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(invoiceDTO2s));
				responseBuilder.status(Response.Status.OK);
				result.append(jsonObject);
			} else {
				result.append(CommonModel.toJson(1, DefinedName.NO_CONTENT));
				responseBuilder.status(Response.Status.NO_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.append("Xảy ra lỗi: " + e.getMessage());
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return responseBuilder.entity(result.toString()).build();
	}

	@POST
	@Path("taohoadon")
	@Produces("application/json; charset=utf-8")
	public Response ctkhuyenmai(String data) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			if (data != null && !data.equals("")) {
				Gson gson = JsonParserUtil.getGson();
				DataAPI dataAPI = gson.fromJson(data, DataAPI.class);
				InvoiceAsyncDTO invoiceAsyncDTO = gson.fromJson(dataAPI.getData(), InvoiceAsyncDTO.class);
				luuhoadon(invoiceAsyncDTO, result, responseBuilder);
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

	@Inject
	IInventoryService inventoryService;

	@GET
	@Path("dstonttsp")
	@Produces("application/json; charset=utf-8")
	public Response dshoadon(@QueryParam("thang") String thang, @QueryParam("nam") String nam,
			@QueryParam("masp") String masp) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			int thangApi = Integer.parseInt(thang);
			int namApi = Integer.parseInt(nam);
			List<ProductKTDTO> tons = inventoryService.selectByMonth(thangApi, namApi);
			if (tons.size() != 0) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(tons));
				responseBuilder.status(Response.Status.OK);
				result.append(jsonObject);
			} else {
				result.append(CommonModel.toJson(1, DefinedName.NO_CONTENT));
				responseBuilder.status(Response.Status.NO_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.append("Xảy ra lỗi: " + e.getMessage());
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return responseBuilder.entity(result.toString()).build();
	}

	private void luuhoadon(InvoiceAsyncDTO invoiceAsyncDTO, StringBuilder result, ResponseBuilder responseBuilder) {
		try {
			if (lock(new Date()) == false) {
				HeThong ht = heThongService.findById(1);
				StringBuilder errors = new StringBuilder();
				int res = invoiceAPIService.taoHoaDonAPI(invoiceAsyncDTO, errors, ht.isKiemton());
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
