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
import lixco.com.interfaces.ICustomerService;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

@Path("datakhachhang")
public class KhachHangServlet {
	@Inject
	private Logger logger;
	@Context
	private HttpServletRequest request;
	@Inject
	ICustomerService customerService;

	@GET
	@Path("timkhachhang")
	@Produces("application/json; charset=utf-8")
	public Response findHangChamLuanChuyen(@QueryParam(DefinedName.PARAM_JSON) String paramJson) throws IOException,
			ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			JsonObject jo = JsonParserUtil.getGson().fromJson(paramJson, JsonObject.class);
			lixco.com.common.HolderParser makh = JsonParserUtil.getValueString(jo, "makh", null);
			lixco.com.common.HolderParser tenkh = JsonParserUtil.getValueString(jo, "tenkh", null);

			FormatHandler formatHandler = FormatHandler.getInstance();
			String makhStr=Objects.toString(makh.getValue());
			String tenkhStr=Objects.toString(tenkh.getValue());
			List<KhachHangData> khachHangDatas = customerService.complete2(makhStr,
					formatHandler.converViToEn(tenkhStr));
			if (khachHangDatas.size() != 0) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(khachHangDatas));
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
