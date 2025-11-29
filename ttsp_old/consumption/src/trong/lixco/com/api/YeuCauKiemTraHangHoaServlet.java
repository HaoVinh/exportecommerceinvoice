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
import lixco.com.entityapi.DataAPI;
import lixco.com.entityapi.YeuCauKiemTraHangDTO;
import lixco.com.service.YeuCauKiemTraHangTHService;
import trong.lixco.com.util.MyUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Path("data")
public class YeuCauKiemTraHangHoaServlet {
	@Context
	private HttpServletRequest request;
	@Inject
	YeuCauKiemTraHangTHService yeuCauKiemTraHangTHService;

	@GET
	@Path("danhsachphieuyeucau")
	@Produces("application/json; charset=utf-8")
	public Response findDanhSachPhieu(@QueryParam(DefinedName.PARAM_SDATE) String sDate,
			@QueryParam(DefinedName.PARAM_EDATE) String eDate) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			Date startDate = MyUtil.chuyensangStrApiDate(sDate);
			Date endDate = MyUtil.chuyensangStrApiDate(eDate);
			List<YeuCauKiemTraHangDTO> yeuCauKiemTraHangDTOs = yeuCauKiemTraHangTHService.searchAPI(startDate, endDate);
			if (yeuCauKiemTraHangDTOs.size() > 0) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(yeuCauKiemTraHangDTOs));
				responseBuilder.status(Response.Status.OK);
				result.append(jsonObject);
			} else {
				result.append(CommonModel.toJson(1, DefinedName.NO_CONTENT));
				responseBuilder.status(Response.Status.NO_CONTENT);
			}
		} catch (Exception e) {
			result.append("Xảy ra lỗi: " + e.getMessage());
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return responseBuilder.entity(result.toString()).build();
	}

	@POST
	@Path("luuketquaphieuyeucaukt")
	@Produces("application/json; charset=utf-8")
	public Response luuKetQuaPhieuYeuCauKiemTra(String data) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			if (data != null && !data.equals("")) {
				Gson gson = JsonParserUtil.getGson();
				YeuCauKiemTraHangDTO yeuCauKiemTraHangDTO = gson.fromJson(data, YeuCauKiemTraHangDTO.class);
				luuKetQuaKiemTra(yeuCauKiemTraHangDTO, result, responseBuilder);
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

	private void luuKetQuaKiemTra(YeuCauKiemTraHangDTO yeuCauKiemTraHangDTO, StringBuilder result,
			ResponseBuilder responseBuilder) {
		try {
			StringBuilder errors = new StringBuilder();
			int res = yeuCauKiemTraHangTHService.updateKetQuaKiemTra(yeuCauKiemTraHangDTO, errors);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("err", res);
			jsonObject.addProperty("msg", errors.toString());
			responseBuilder.status(Response.Status.OK);
			result.append(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			responseBuilder.status(Response.Status.BAD_REQUEST);
		}
	}
}
