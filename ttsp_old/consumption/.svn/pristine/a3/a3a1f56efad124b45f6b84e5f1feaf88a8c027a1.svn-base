package trong.lixco.com.api;

import io.jsonwebtoken.io.IOException;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import lixco.com.common.JsonParserUtil;
import lixco.com.entityapi.GoodsReceiptNoteDTO;
import lixco.com.entityapi.InvoiceDTO2;
import lixco.com.service.GoodsReceiptNoteAPIService;
import lixco.com.service.HeThongService;
import trong.lixco.com.util.MyUtil;

import com.google.gson.JsonObject;

@Path("data/nhaphang")
public class GoodsReceiptNoteServlet {
	@Context
	private HttpServletRequest request;
	@Inject
	GoodsReceiptNoteAPIService goodsReceiptNoteAPIService;
	@Inject
	HeThongService heThongService;
	
	@GET
	@Path("dsphieunhap")
	@Produces("application/json; charset=utf-8")
	public Response dsPhieunhap(@QueryParam(DefinedName.PARAM_SDATE) String sDate,
			@QueryParam(DefinedName.PARAM_EDATE) String eDate, @QueryParam("maxuatnhap") String maxuatnhap,
			@QueryParam("idloaibo") String idloaibo) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			Date startDate=MyUtil.chuyensangStrApiDate(sDate);
			Date endDate=MyUtil.chuyensangStrApiDate(eDate);
			
			List<GoodsReceiptNoteDTO> goodsReceiptNoteDTOs=goodsReceiptNoteAPIService.search(startDate, endDate, maxuatnhap,idloaibo);
			if (goodsReceiptNoteDTOs.size() > 0) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(goodsReceiptNoteDTOs));
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
}
