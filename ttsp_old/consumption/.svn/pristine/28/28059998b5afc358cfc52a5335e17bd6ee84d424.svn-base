package trong.lixco.com.api;

import io.jsonwebtoken.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

import lixco.com.commom_ejb.HolderParser;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.interfaces.IGoodsReceiptNoteService;
import lixco.com.interfaces.IInventoryService;
import lixco.com.interfaces.IProductService;
import lixco.com.reportInfo.HangChamLuanChuyen;
import lixco.com.reportInfo.TonKhoSanPham;
import lixco.com.reportInfo.TonKhoThang;
import lixco.com.reqInfo.ChiTietDuBaoTon;

import org.jboss.logging.Logger;

import trong.lixco.com.util.MyUtil;

import com.google.gson.JsonObject;

@Path("dataluanchuyen")
public class LuanChuyenServlet {
	@Inject
	private Logger logger;
	@Context
	private HttpServletRequest request;
	@Inject
	IProductService productService;
	@Inject
	IInventoryService inventoryService;
	@Inject
	IGoodsReceiptNoteService goodsReceiptNoteService;

	/*
	 * http://localhost:8280/kiot/api/data/productLixs/search/{containedStr}?
	 * branchId=80204&pageSize=50&pageIndex=1
	 */
	@GET
	@Path("hangchamluanchuyen")
	@Produces("application/json; charset=utf-8")
	public Response findHangChamLuanChuyen(@QueryParam(DefinedName.PARAM_JSON) String paramJson,
			@QueryParam(DefinedName.PARAM_CHINHANH) String chinhanh) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			JsonObject jo = JsonParserUtil.getGson().fromJson(paramJson, JsonObject.class);
			lixco.com.common.HolderParser ngaytinhton = JsonParserUtil.getValueString(jo, "ngaytinhton", null);
			List<HangChamLuanChuyen> hangChamLuanChuyens = new ArrayList<HangChamLuanChuyen>();
			Date ngay=MyUtil.chuyensangStrApiDate(Objects.toString(ngaytinhton.getValue()));
			int month = ToolTimeCustomer.getMonthM(ngay);
			int year = ToolTimeCustomer.getYearM(ngay);
			List<TonKhoSanPham> tonkhothangCNs = inventoryService.tonkhongay(ngay,  0, 0);
			
			Set<String> maspparams = new HashSet<String>();
			for (int i = 0; i < tonkhothangCNs.size(); i++) {
//				if (tonkhothangCNs.get(i).getKg_closing_balance() != 0) {
					HangChamLuanChuyen hcLuanChuyen = new HangChamLuanChuyen();
					hcLuanChuyen.setKho(chinhanh);
					hcLuanChuyen.setLoaisp(tonkhothangCNs.get(i).getProduct_type_name());
					hcLuanChuyen.setIdsp(tonkhothangCNs.get(i).getProduct_id());
					hcLuanChuyen.setMasp(tonkhothangCNs.get(i).getProduct_code());
					hcLuanChuyen.setTensp(tonkhothangCNs.get(i).getProduct_name());
					hcLuanChuyen.setNam(year);
					hcLuanChuyen.setThang(month);
					hcLuanChuyen.setSlton(tonkhothangCNs.get(i).getKg_closing_balance());
					maspparams.add(hcLuanChuyen.getMasp());
					hangChamLuanChuyens.add(hcLuanChuyen);
//				}
			}
			List<String> masps = new ArrayList<String>(maspparams);
			List<Object[]> ngaysxgannhats = goodsReceiptNoteService.ngaysxgannhat(masps);
			for (int i = 0; i < hangChamLuanChuyens.size(); i++) {
				for (int j = 0; j < ngaysxgannhats.size(); j++) {
					if (hangChamLuanChuyens.get(i).getMasp().equals(Objects.toString(ngaysxgannhats.get(j)[0], ""))) {
						hangChamLuanChuyens.get(i).setNgaysxgannhat((Date) ngaysxgannhats.get(j)[1]);
						break;
					}
				}
			}
			if (hangChamLuanChuyens.size() != 0) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(hangChamLuanChuyens));
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
	 * http://localhost:8280/kiot/api/data/productLixs/search/{containedStr}?
	 * branchId=80204&pageSize=50&pageIndex=1
	 */
	@GET
	@Path("chitietdubaoton")
	@Produces("application/json; charset=utf-8")
	public Response findChiTietDuBaoTon(@QueryParam(DefinedName.PARAM_SDATE) String sDate,
			@QueryParam(DefinedName.PARAM_EDATE) String eDate,
			@QueryParam(DefinedName.PARAM_TRANGTHAI) String bomaxnnoibo,
			@QueryParam(DefinedName.PARAM_CODE) String productCode,
			@QueryParam(DefinedName.PARAM_CHINHANH) String chinhanh) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			List<ChiTietDuBaoTon> results = new ArrayList<ChiTietDuBaoTon>();
			JsonObject json = new JsonObject();
			json.addProperty("exp_from_date",
					ToolTimeCustomer.convertDateToString(MyUtil.chuyensangStrApiDate(sDate), "dd/MM/yyyy"));
			json.addProperty("exp_to_date",
					ToolTimeCustomer.convertDateToString(MyUtil.chuyensangStrApiDate(eDate), "dd/MM/yyyy"));
			json.addProperty("product_code", productCode);
			json.addProperty("chinhanh", chinhanh);
			List<Object[]> list = new ArrayList<Object[]>();
			inventoryService.getListExpectedInventoryDetail2(JsonParserUtil.getGson().toJson(json), list,
					Boolean.getBoolean(bomaxnnoibo));
			if (list.size() > 0) {
				for (Object[] p : list) {
					double sldonhang_thung = Double.parseDouble(Objects.toString(p[5], "0"));
					double sldonhangdaxuat_thung = Double.parseDouble(Objects.toString(p[6], "0"));
					double slkhuyenmai_dvt = Double.parseDouble(Objects.toString(p[7], "0"));
					double slkhuyenmaidaxuat_dvt = Double.parseDouble(Objects.toString(p[8], "0"));
					if ((sldonhang_thung - sldonhangdaxuat_thung != 0 || slkhuyenmai_dvt - slkhuyenmaidaxuat_dvt != 0)) {
						ChiTietDuBaoTon item = new ChiTietDuBaoTon(Long.parseLong(Objects.toString(p[0])),
								Objects.toString(p[1], null), Objects.toString(p[2], null),
								Objects.toString(p[3], "0"), (Date) p[4], sldonhang_thung, sldonhangdaxuat_thung,
								slkhuyenmai_dvt, slkhuyenmaidaxuat_dvt,
								Double.parseDouble(Objects.toString(p[9], "0")), Double.parseDouble(Objects.toString(
										p[10], "0")), Objects.toString(p[11]), (Date) p[12],
								Objects.toString(p[13], ""));
						results.add(item);
					}
				}
				JsonObject jsonObject = new JsonObject();
				jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(results));
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
