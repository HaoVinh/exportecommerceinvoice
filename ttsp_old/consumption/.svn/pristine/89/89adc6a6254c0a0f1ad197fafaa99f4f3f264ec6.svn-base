package trong.lixco.com.api;

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

import org.jboss.logging.Logger;

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
	@GET
	@Path("productLixs/search/{containedStr}")
	@Produces("application/json; charset=utf-8")
	public Response findByContainedStr(
			@QueryParam(DefinedName.PARAM_PAGE_SIZE) int pageSize,
			@QueryParam(DefinedName.PARAM_PAGE_INDEX) int pageIndex) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			
//					productHanlder.processFindByContainedStr(containedStr, branchId, pageSize, pageIndex, request,
//							result, responseBuilder);
			
			
		} catch (Exception e) {
			result.append(CommonModel.toJson(-1, MessagesAPI.MALFORMED_DATA));
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			logger.error("ProductDataServlet.findByContainedStr:" + e.getMessage(), e);
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
