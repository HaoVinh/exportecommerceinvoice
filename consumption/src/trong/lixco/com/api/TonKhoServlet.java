package trong.lixco.com.api;

import java.util.ArrayList;
import java.util.Date;
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

import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Product;
import lixco.com.entityapi.ProductAsyncDTO;
import lixco.com.entityapi.ProductDTO;
import lixco.com.interfaces.IInventoryService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.TonKhoSanPham;
import lixco.com.reqInfo.ChiTietDuBaoTon;
import lixco.com.reqInfo.ExpectedInventoryReqInfo;
import lixco.com.service.ProductService;

import org.jboss.logging.Logger;

import trong.lixco.com.info.TonKhoTTSP;
import trong.lixco.com.util.MyUtil;

import com.google.gson.JsonObject;

@Path("data")
public class TonKhoServlet {
	@Inject
	private Logger logger;
	@Context
	private HttpServletRequest request;
	@Inject
	IProductService productService;
	@Inject
	IInventoryService inventoryService;
	@Inject
	IReportService reportService;

	@GET
	@Path("tonkhosanpham")
	@Produces("application/json; charset=utf-8")
	public Response findDuBaoTon(@QueryParam(DefinedName.PARAM_SDATE) String sDate,
			@QueryParam(DefinedName.PARAM_EDATE) String eDate) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			JsonObject json = new JsonObject();
			json.addProperty("from_date",
					ToolTimeCustomer.convertDateToString(MyUtil.chuyensangStrApiDate(sDate), "dd/MM/yyyy"));
			json.addProperty("to_date",
					ToolTimeCustomer.convertDateToString(MyUtil.chuyensangStrApiDate(eDate), "dd/MM/yyyy"));
			json.addProperty("product_id", 0);
			json.addProperty("product_type_id", 0);
			json.addProperty("typep", -1);
			/*
			 * <f:selectItem itemLabel="Tất cả" itemValue="-1" /> <f:selectItem
			 * itemLabel="Nội địa" itemValue="0" /> <f:selectItem
			 * itemLabel="Xuất khẩu" itemValue="1" />
			 */
			List<TonKhoSanPham> results = new ArrayList<TonKhoSanPham>();
			reportService.reportTonKhoSanPham(JsonParserUtil.getGson().toJson(json), results);
			if (results.size() > 0) {
				List<TonKhoTTSP> tonKhoTTSPs = new ArrayList<TonKhoTTSP>();
				for (int i = 0; i < results.size(); i++) {
					TonKhoTTSP tk = new TonKhoTTSP();
					tk.setProductCode(results.get(i).getProduct_code());
					tk.setFactor(results.get(i).getFactor());
					tk.setQuantity(results.get(i).getKg_closing_balance());// so luong ton KG
					tonKhoTTSPs.add(tk);
				}

				JsonObject jsonObject = new JsonObject();
				jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(tonKhoTTSPs));
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
