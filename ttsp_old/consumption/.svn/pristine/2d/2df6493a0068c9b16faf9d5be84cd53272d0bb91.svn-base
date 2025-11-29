package trong.lixco.com.api;

import java.util.List;
import java.util.Objects;

import io.jsonwebtoken.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.entityapi.SanPhamData;
import lixco.com.interfaces.IProductService;
import lixco.com.service.ProductService;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

@Path("data")
public class ProductServlet {
	@Inject
	private Logger logger;
	@Context
	private HttpServletRequest request;

	/*
	 * http://localhost:8280/kiot/api/data/productLixs/search/{containedStr}?
	 * branchId=80204&pageSize=50&pageIndex=1
	 */
	@Inject
	IProductService productService;
	@GET
	@Path("productLixs/search/{containedStr}")
	@Produces("application/json; charset=utf-8")
	public Response findByContainedStr(@PathParam("containedStr") String code_u) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			responseBuilder = Response.status(Response.Status.OK);
			String product_code=productService.findByCodeU(code_u);
			result.append(product_code);
//					productHanlder.processFindByContainedStr(containedStr, branchId, pageSize, pageIndex, request,
//							result, responseBuilder);
			
			
		} catch (Exception e) {
			result.append(CommonModel.toJson(-1, MessagesAPI.MALFORMED_DATA));
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			logger.error("ProductDataServlet.findByContainedStr:" + e.getMessage(), e);
		}
		return responseBuilder.entity(result.toString()).build();
	}
	@GET
	@Path("timsanpham")
	@Produces("application/json; charset=utf-8")
	public Response timSanPham(@QueryParam(DefinedName.PARAM_JSON) String paramJson) throws IOException,
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
	/*
	 * @API cập nhật sản phẩm
	 * 
	 * @http://localhost:8280/kiot/api/data/productLixs/{id}
	 */
	@PUT
	@Path("productLixs/id/{id}")
	@Produces("application/json; charset=utf-8")
	public Response updateById(@PathParam(DefinedName.PARAM_ID) Long id, String data)
			throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			
		} catch (Exception e) {
			result.append(CommonModel.toJson(-1, MessagesAPI.MALFORMED_DATA));
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			logger.error("ProductDataServlet.updateById:" + e.getMessage(), e);
		}
		return responseBuilder.entity(result.toString()).build();
	}

	public boolean isNumeric(String strNum) {
		return strNum.matches("-?\\d+(\\.\\d+)?");
	}

	
}
