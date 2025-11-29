package trong.lixco.com.api;

import io.jsonwebtoken.io.IOException;

import java.util.List;
import java.util.Objects;

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

import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.entityapi.KhachHangData;
import lixco.com.entityapi.SanPhamData;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IProductService;

import com.google.gson.JsonObject;

@Path("datasanpham")
public class SanPhamServlet {
	@Context
	private HttpServletRequest request;
	@Inject
	IProductService productService;

	@GET
	@Path("timsanpham")
	@Produces("application/json; charset=utf-8")
	public Response findHangChamLuanChuyen(@QueryParam(DefinedName.PARAM_JSON) String paramJson) throws IOException,
			ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			JsonObject jo = JsonParserUtil.getGson().fromJson(paramJson, JsonObject.class);
			lixco.com.common.HolderParser masp = JsonParserUtil.getValueString(jo, "masp", null);
			lixco.com.common.HolderParser tensp = JsonParserUtil.getValueString(jo, "tensp", null);

			FormatHandler formatHandler = FormatHandler.getInstance();
			List<SanPhamData> sanPhamDatas = productService.complete2(Objects.toString(masp.getValue()),
					formatHandler.converViToEn(Objects.toString(tensp.getValue())));
			if (sanPhamDatas.size() != 0) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(sanPhamDatas));
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

}
