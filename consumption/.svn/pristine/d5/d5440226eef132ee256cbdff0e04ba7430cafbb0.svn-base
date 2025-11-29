package trong.lixco.com.api;

import io.jsonwebtoken.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
public class KiemKhoServlet {
	@Inject
	private Logger logger;
	@Context
	private HttpServletRequest request;

	/*
	 * @API delete kiểm kho
	 * 
	 * @Link:http://192.168.0.203:8280/kiot/api/data/kiemKhos/id/{id}
	 */
	@DELETE
	@Path("kiemKhos/id/{id}")
	@Produces("application/json; charset=utf-8")
	public Response deleteKiemKhoById(@PathParam(DefinedName.PARAM_ID) Long id) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
//		try {
//			if (id != null && isNumeric(String.valueOf(id)) && id != 0) {
//				kiemKhoHanlder.processDeleteKiemKhoById(id, request, result, responseBuilder);
//			} else {
//				result.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
//				responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
//			}
//		} catch (Exception e) {
//			result.append(CommonModel.toJson(-1, MessagesAPI.MALFORMED_DATA));
//			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//			logger.error("KiemKhoServlet.findKiemKhoDetailByKiemKhoId:" + e.getMessage(), e);
//		}
		return responseBuilder.entity(result.toString()).build();
	}

	/*
	 * @API cập nhật kiểm kho
	 * 
	 * @Link:http://192.168.0.203:8280/kiot/api/data/kiemKhos/id/{id}
	 * 
	 * @Body:{ "id": 6, "date": "27/12/2022 12:43:47", "kiemKhoDetailAPIs": [ {
	 * "productID": 58405, "productCode": "037600138727", "productName":
	 * "Spam thịt heo hộp Classic 340g - Canned meat", "quantityCurrent": 15.0,
	 * "price": 120000.0 }, { "productID": 57271, "productCode":
	 * "8934669282274", "productName":
	 * "Lix On1 nước ủi quần áo hương biển xanh 500ml - Fresh ocean perfumed ironing spray"
	 * , "quantityCurrent": 10.0, "price": 50000.0 } ] }
	 */
	@PUT
	@Path("kiemKhos/id/{id}")
	@Produces("application/json; charset=utf-8")
	public Response updateKiemKhoById(@PathParam(DefinedName.PARAM_ID) Long id, String data)
			throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
//			Long branchId = settingBranchId(request);
//			if (id != null && isNumeric(String.valueOf(id)) && id != 0 && data != null && !data.equals("")) {
//				if (checkHaveBranch(branchId)) {
//					kiemKhoHanlder.processUpdateKiemKhoById(id, data, request, result, responseBuilder, branchId);
//				}else {
//					result.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
//					responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
//				}
//			} else {
//				result.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
//				responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
//			}
		} catch (Exception e) {
			result.append(CommonModel.toJson(-1, MessagesAPI.MALFORMED_DATA));
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			logger.error("KiemKhoServlet.findKiemKhoDetailByKiemKhoId:" + e.getMessage(), e);
		}
		return responseBuilder.entity(result.toString()).build();
	}

	/*
	 * @API lưu kiểm kho
	 * 
	 * @Link:http://192.168.0.203:8280/kiot/api/data/kiemKhos
	 * 
	 * @body: { "date": "27/12/2022 12:43:47", "kiemKhoDetailAPIs": [ {
	 * "productID": 58405, "productCode": "037600138727", "productName":
	 * "Spam thịt heo hộp Classic 340g - Canned meat", "quantityCurrent": 15.0,
	 * "price": 120000.0 }, { "productID": 57271, "productCode":
	 * "8934669282274", "productName":
	 * "Lix On1 nước ủi quần áo hương biển xanh 500ml - Fresh ocean perfumed ironing spray"
	 * , "quantityCurrent": 10.0, "price": 50000.0 } ] }
	 * 
	 * @header thêm BranchId
	 */
	@POST
	@Path("kiemKhos")
	@Produces("application/json; charset=utf-8")
	public Response saveKiemKho(String data) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
//			Long branchId = settingBranchId(request);
//			if (data != null && !data.equals("") && branchId != null && branchId != 0) {
//				if (checkHaveBranch(branchId)) {
//					kiemKhoHanlder.processSaveKiemKho(data, branchId, request, result, responseBuilder);
//				} else {
//					result.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
//					responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
//				}
//			} else {
//				result.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
//				responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
//			}
		} catch (Exception e) {
			result.append(CommonModel.toJson(-1, MessagesAPI.MALFORMED_DATA));
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return responseBuilder.entity(result.toString()).build();
	}

	// lấy dữ liệu danh sách kiểm kho
	/*
	 * @param: pagesize=123&currentItem=2
	 * localhost:8280/kiot/api/data/kiemKhos?branchId=hong1&pageSize=100&
	 * pageIndex=1&date=dd-MM-yyyy
	 */
	@GET
	@Path("kiemKhos")
	@Produces("application/json; charset=utf-8")
	public Response findKiemKhos(
			@QueryParam(DefinedName.PARAM_PAGE_SIZE) int pageSize,
			@QueryParam(DefinedName.PARAM_PAGE_INDEX) int pageIndex)
			throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
//			if (pageSize != 0 && pageIndex != 0 && branchId != null && branchId != 0) {
//				if (checkHaveBranch(branchId)) {
//					SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
//					Date dateSearch = null;
//					try {
//						dateSearch = ddMMyyyy.parse(date);
//					} catch (Exception e) {
//					}
//					// if (dateSearch != null) {
//					kiemKhoHanlder.processFindList(dateSearch, pageSize, pageIndex, branchId, request, result,
//							responseBuilder);
//					// } else {
//					// result.append(CommonModel.toJson(-1,
//					// MessagesAPI.MALFORMED_DATA));
//					// responseBuilder =
//					// Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//					// }
//				} else {
//					result.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
//					responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
//				}
//			} else {
//				result.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
//				responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
//			}
		} catch (Exception e) {
			result.append(CommonModel.toJson(-1, MessagesAPI.MALFORMED_DATA));
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			logger.error("KiemKhoServlet.findList:" + e.getMessage(), e);
		}
		return responseBuilder.entity(result.toString()).build();
	}

	// lấy dữ liệu danh sách chi tiết kiểm kho
	/*
	 * @param: id=123
	 * localhost:8280/kiot/api/data/kiemKhoDetails/kiemKhoId/123?branchId=80204&pageSize=50&
	 * pageIndex=1
	 */
	@GET
	@Path("kiemKhoDetails/kiemKhoId/{id}")
	@Produces("application/json; charset=utf-8")
	public Response findKiemKhoDetailsByKiemKhoId(
			@QueryParam(DefinedName.PARAM_PAGE_SIZE) int pageSize,
			@QueryParam(DefinedName.PARAM_PAGE_INDEX) int pageIndex) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
//			if (id != null && isNumeric(String.valueOf(id)) && id != 0 && pageSize != 0 && pageIndex != 0) {
//				if (checkHaveBranch(branchId)) {
//					kiemKhoHanlder.processFindKiemKhoDetails(id, pageSize, pageIndex, request, result, responseBuilder,
//							branchId);
//				}else {
//					result.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
//					responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
//				}
//			} else {
//				result.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
//				responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
//			}
		} catch (Exception e) {
			result.append(CommonModel.toJson(-1, MessagesAPI.MALFORMED_DATA));
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
			logger.error("KiemKhoServlet.findKiemKhoDetailByKiemKhoId:" + e.getMessage(), e);
		}
		return responseBuilder.entity(result.toString()).build();
	}

	public boolean isNumeric(String strNum) {
		return strNum.matches("-?\\d+(\\.\\d+)?");
	}

}
