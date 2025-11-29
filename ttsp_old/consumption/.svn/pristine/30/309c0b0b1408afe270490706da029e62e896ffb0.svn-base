package trong.lixco.com.api;

import java.util.ArrayList;
import java.util.Arrays;
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
import lixco.com.interfaces.IGoodsReceiptNoteDetailService;
import lixco.com.interfaces.IInventoryService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.SoLieuBaoCaoTongHop;
import lixco.com.reqInfo.ChiTietDuBaoTon;
import lixco.com.reqInfo.ExpectedInventoryReqInfo;
import lixco.com.reqInfo.ExpectedInventoryReqInfoSub;
import lixco.com.service.ProductService;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Call;

import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;

import trong.lixco.com.entity.AccountAPI;
import trong.lixco.com.service.AccountAPIService;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.StaticPath;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Path("data")
public class SLBH_SXServlet {
	@Inject
	private Logger logger;
	@Context
	private HttpServletRequest request;
	@Inject
	IProductService productService;
	@Inject
	IInventoryService inventoryService;
	@Inject
	IGoodsReceiptNoteDetailService goodsReceiptNoteDetailService;

	/*
	 * http://localhost:8280/kiot/api/data/productLixs/search/{containedStr}?
	 * branchId=80204&pageSize=50&pageIndex=1
	 */
	@GET
	@Path("dasxtheonhomhang")
	@Produces("application/json; charset=utf-8")
	public Response dasxtheonhomhang(@QueryParam(DefinedName.PARAM_SDATE) String sDate,
			@QueryParam(DefinedName.PARAM_EDATE) String eDate) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			Date startDate=MyUtil.chuyensangStrApiDate(sDate);
			Date endDate=MyUtil.chuyensangStrApiDate(eDate);
			List<Object[]> dasxtheonhomhang = goodsReceiptNoteDetailService.reportImpFromPX_Lix(startDate, endDate);
			if (dasxtheonhomhang.size() != 0) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(dasxtheonhomhang));
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
	@GET
	@Path("toncuoitheonhom")
	@Produces("application/json; charset=utf-8")
	public Response toncuoitheonhom(@QueryParam(DefinedName.PARAM_MONTH) String pmonth,
			@QueryParam(DefinedName.PARAM_YEAR) String pyear) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			List<Object[]> tondaus = inventoryService.toncuoitheonhom(Integer.parseInt(pmonth), Integer.parseInt(pyear));
			if (tondaus.size() != 0) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(tondaus));
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
	@Inject
	IReportService reportService;
	@GET
	@Path("xuathang")
	@Produces("application/json; charset=utf-8")
	public Response toncuoitheonhom(@QueryParam(DefinedName.PARAM_JSON2) String paramjson) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			List<Object[]> results= reportService.reportLaySoLieuBaoCaoTongHop2(paramjson);
			if (results.size() != 0) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(results));
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


	@GET
	@Path("dubaotonkenhkh")
	@Produces("application/json; charset=utf-8")
	public Response findDuBaoTonkenhkh(@QueryParam(DefinedName.PARAM_NDATE) String nDate,
			@QueryParam(DefinedName.PARAM_SDATE) String sDate, @QueryParam(DefinedName.PARAM_EDATE) String eDate,
			@QueryParam(DefinedName.PARAM_TRANGTHAI) String bomaxnnoibop,
			@QueryParam(DefinedName.PARAM_CHINHANH) String chinhanh) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			List<ExpectedInventoryReqInfo> results = new ArrayList<ExpectedInventoryReqInfo>();
			JsonObject json = new JsonObject();
			json.addProperty("cal_inv_date",
					ToolTimeCustomer.convertDateToString(MyUtil.chuyensangStrApiDate(nDate), "dd/MM/yyyy"));
			json.addProperty("exp_from_date",
					ToolTimeCustomer.convertDateToString(MyUtil.chuyensangStrApiDate(sDate), "dd/MM/yyyy"));
			json.addProperty("exp_to_date",
					ToolTimeCustomer.convertDateToString(MyUtil.chuyensangStrApiDate(eDate), "dd/MM/yyyy"));
			boolean bomaxnnoibo = "true".equals(bomaxnnoibop) ? true : false;
			List<Object[]> list = inventoryService.getListExpectedInventory2(JsonParserUtil.getGson().toJson(json),
					bomaxnnoibo);
			List<Object[]> listOrder = inventoryService.getListExpectedOrderKenhKH(JsonParserUtil.getGson()
					.toJson(json), bomaxnnoibo);
			List<ExpectedInventoryReqInfo> duBaoTons = new ArrayList<ExpectedInventoryReqInfo>();
			if (list.size() > 0) {
				if (!"HO CHI MINH".equals(chinhanh)) {
					for (Object[] p : list) {
						// tao du lieu BD
						ExpectedInventoryReqInfo item = new ExpectedInventoryReqInfo(Long.parseLong(Objects
								.toString(p[0])), Objects.toString(p[1], ""), Objects.toString(p[2], null),
								Double.parseDouble(Objects.toString(p[3], "0")), Double.parseDouble(Objects.toString(
										p[4], "0")), Double.parseDouble(Objects.toString(p[5], "0")),
								Double.parseDouble(Objects.toString(p[6], "0")), Double.parseDouble(Objects.toString(
										p[7], "0")), Double.parseDouble(Objects.toString(p[8], "0")), false,
								Long.parseLong(Objects.toString(p[0])));

						for (int i = 0; i < listOrder.size(); i++) {
							if (item.getProduct_code().equals(Objects.toString(listOrder.get(i)[2], null))) {
								if ("HO CHI MINH".equals(Objects.toString(listOrder.get(i)[0], ""))) {
									// SL dự xuất
									item.setExp_export_quantity(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
									if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
										// SL dự xuất GT
										item.setExp_export_quantityGT(Double.parseDouble(Objects.toString(
												listOrder.get(i)[4], "0")));
									} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
										// SL dự xuất KHAC
										item.setExp_export_quantityKHAC(Double.parseDouble(Objects.toString(
												listOrder.get(i)[4], "0")));
									}

								} else {
									// SL dự xuất BD
									item.setExp_export_quantityBD(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
									if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
										// SL dự xuất BD GT
										item.setExp_export_quantityBDGT(Double.parseDouble(Objects.toString(
												listOrder.get(i)[4], "0")));
									} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
										// SL dự xuất BD KHAC
										item.setExp_export_quantityBDKHAC(Double.parseDouble(Objects.toString(
												listOrder.get(i)[4], "0")));
									}
								}
								listOrder.get(i)[5] = 1;
								break;
							}
						}
						duBaoTons.add(item);
					}
					// kiem tra ds don hang chua co trong danh sach them vao
					for (int i = 0; i < listOrder.size(); i++) {
						if (Long.parseLong(Objects.toString(listOrder.get(i)[5])) == 0) {
							ExpectedInventoryReqInfo item = new ExpectedInventoryReqInfo(Long.parseLong(Objects
									.toString(listOrder.get(i)[1])), Objects.toString(listOrder.get(i)[2], ""),
									Objects.toString(listOrder.get(i)[3], null));
							if ("HO CHI MINH".equals(Objects.toString(listOrder.get(i)[0], ""))) {
								// SL dự xuất
								item.setExp_export_quantity(Double.parseDouble(Objects.toString(listOrder.get(i)[4],
										"0")));
								if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất GT
									item.setExp_export_quantityGT(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
								} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất KHAC
									item.setExp_export_quantityKHAC(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
								}
							} else {
								// SL dự xuất BD
								item.setExp_export_quantityBD(Double.parseDouble(Objects.toString(listOrder.get(i)[4],
										"0")));
								if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất GT
									item.setExp_export_quantityBDGT(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
								} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất KHAC
									item.setExp_export_quantityBDKHAC(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
								}
							}
							duBaoTons.add(item);
						}
					}
					results.addAll(duBaoTons);
				} else if ("HO CHI MINH".equals(chinhanh)) {
					for (Object[] p : list) {
						// tao du lieu BD
						ExpectedInventoryReqInfo item = new ExpectedInventoryReqInfo(Long.parseLong(Objects
								.toString(p[0])), Objects.toString(p[1], ""), Objects.toString(p[2], null),
								Double.parseDouble(Objects.toString(p[3], "0")), Double.parseDouble(Objects.toString(
										p[4], "0")), Double.parseDouble(Objects.toString(p[5], "0")),
								Double.parseDouble(Objects.toString(p[6], "0")), Double.parseDouble(Objects.toString(
										p[7], "0")), Double.parseDouble(Objects.toString(p[8], "0")));
						for (int i = 0; i < listOrder.size(); i++) {
							if (item.getProduct_code().equals(Objects.toString(listOrder.get(i)[2], null))) {
								if ("HO CHI MINH".equals(Objects.toString(listOrder.get(i)[0], ""))) {
									// SL dự xuất
									item.setExp_export_quantity(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
									if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
										// SL dự xuất GT
										item.setExp_export_quantityGT(Double.parseDouble(Objects.toString(
												listOrder.get(i)[4], "0")));
									} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
										// SL dự xuất KHAC
										item.setExp_export_quantityKHAC(Double.parseDouble(Objects.toString(
												listOrder.get(i)[4], "0")));
									}
								} else {
									// SL dự xuất BD
									item.setExp_export_quantityBD(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
									if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
										// SL dự xuất GT
										item.setExp_export_quantityBDGT(Double.parseDouble(Objects.toString(
												listOrder.get(i)[4], "0")));
									} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
										// SL dự xuất KHAC
										item.setExp_export_quantityBDKHAC(Double.parseDouble(Objects.toString(
												listOrder.get(i)[4], "0")));
									}
								}
								listOrder.get(i)[5] = 1;
								break;
							}
						}
						duBaoTons.add(item);
					}
					// kiem tra ds don hang chua co trong danh sach them vao
					for (int i = 0; i < listOrder.size(); i++) {
						if (Long.parseLong(Objects.toString(listOrder.get(i)[5])) == 0) {
							ExpectedInventoryReqInfo item = new ExpectedInventoryReqInfo(Long.parseLong(Objects
									.toString(listOrder.get(i)[1])), Objects.toString(listOrder.get(i)[2], ""),
									Objects.toString(listOrder.get(i)[3], null));
							if ("HO CHI MINH".equals(Objects.toString(listOrder.get(i)[0], ""))) {
								// SL dự xuất
								item.setExp_export_quantity(Double.parseDouble(Objects.toString(listOrder.get(i)[4],
										"0")));
								if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất GT
									item.setExp_export_quantityGT(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
								} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất KHAC
									item.setExp_export_quantityKHAC(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
								}
							} else {
								// SL dự xuất BD
								item.setExp_export_quantityBD(Double.parseDouble(Objects.toString(listOrder.get(i)[4],
										"0")));
								if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất GT
									item.setExp_export_quantityBDGT(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
								} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất KHAC
									item.setExp_export_quantityBDKHAC(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
								}
							}
							duBaoTons.add(item);
						}
					}
					results.addAll(duBaoTons);
				}
				for (int i = 0; i < results.size(); i++) {
					if ("CB032".equals(results.get(i).getProduct_code())) {
						ExpectedInventoryReqInfo ex = results.get(i);
						System.out.println();
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
			@QueryParam(DefinedName.PARAM_TRANGTHAI) String bomaxnnoibop,
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
			boolean bomaxnnoibo = "true".equals(bomaxnnoibop) ? true : false;
			inventoryService.getListExpectedInventoryDetail2(JsonParserUtil.getGson().toJson(json), list, bomaxnnoibo);
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

	// @GET
	// @Path("dubaotonlapkehoach")
	// @Produces("application/json; charset=utf-8")
	// public Response findDuBaoTonLapKH(@QueryParam(DefinedName.PARAM_NDATE)
	// String nDate,
	// @QueryParam(DefinedName.PARAM_SDATE) String sDate,
	// @QueryParam(DefinedName.PARAM_EDATE) String eDate,
	// @QueryParam(DefinedName.PARAM_TRANGTHAI) String bomaxnnoibop) throws
	// IOException, ServletException {
	// StringBuilder result = new StringBuilder();
	// ResponseBuilder responseBuilder =
	// Response.status(Response.Status.BAD_REQUEST);
	// try {
	// List<ExpectedInventoryReqInfoSub> results = new
	// ArrayList<ExpectedInventoryReqInfoSub>();
	// JsonObject json = new JsonObject();
	// json.addProperty("cal_inv_date",
	// ToolTimeCustomer.convertDateToString(MyUtil.chuyensangStrApiDate(nDate),
	// "dd/MM/yyyy"));
	// json.addProperty("exp_from_date",
	// ToolTimeCustomer.convertDateToString(MyUtil.chuyensangStrApiDate(sDate),
	// "dd/MM/yyyy"));
	// json.addProperty("exp_to_date",
	// ToolTimeCustomer.convertDateToString(MyUtil.chuyensangStrApiDate(eDate),
	// "dd/MM/yyyy"));
	// boolean bomaxnnoibo = "true".equals(bomaxnnoibop) ? true : false;
	// List<Object[]> list =
	// inventoryService.getListExpectedInventory2(JsonParserUtil.getGson().toJson(json),
	// bomaxnnoibo);
	// List<Object[]> listOrder =
	// inventoryService.getListExpectedOrder(JsonParserUtil.getGson().toJson(json),
	// bomaxnnoibo);
	// List<ExpectedInventoryReqInfoSub> duBaoTons = new
	// ArrayList<ExpectedInventoryReqInfoSub>();
	// if (list.size() > 0) {
	// for (Object[] p : list) {
	// ExpectedInventoryReqInfoSub item = new
	// ExpectedInventoryReqInfoSub(Long.parseLong(Objects
	// .toString(p[0])), Objects.toString(p[1], ""), Objects.toString(p[2],
	// null),
	// Double.parseDouble(Objects.toString(p[3], "0")),
	// Double.parseDouble(Objects.toString(p[4],
	// "0")), Double.parseDouble(Objects.toString(p[5], "0")),
	// Double.parseDouble(Objects
	// .toString(p[6], "0"))/30, Double.parseDouble(Objects.toString(p[7],
	// "0")),
	// Double.parseDouble(Objects.toString(p[8],
	// "0")),Double.parseDouble(Objects.toString(p[10], "0")));
	// for (int i = 0; i < listOrder.size(); i++) {
	// if (item.getProduct_code().equals(Objects.toString(listOrder.get(i)[2],
	// null))) {
	// item.setExp_export_quantity(Double.parseDouble(Objects.toString(listOrder.get(i)[4],
	// "0")));
	// listOrder.get(i)[5] = 1;
	// break;
	// }
	// }
	// duBaoTons.add(item);
	// }
	// // kiem tra ds don hang chua co trong danh sach them vao
	// for (int i = 0; i < listOrder.size(); i++) {
	// if (Long.parseLong(Objects.toString(listOrder.get(i)[5])) == 0) {
	// ExpectedInventoryReqInfoSub item = new
	// ExpectedInventoryReqInfoSub(Long.parseLong(Objects
	// .toString(listOrder.get(i)[1])), Objects.toString(listOrder.get(i)[2],
	// ""),
	// Objects.toString(listOrder.get(i)[3], null),Double.parseDouble(Objects
	// .toString(listOrder.get(i)[6])));
	// item.setExp_export_quantity(Double.parseDouble(Objects.toString(listOrder.get(i)[4],
	// "0")));
	// duBaoTons.add(item);
	// }
	// }
	// results.addAll(duBaoTons);
	// JsonObject jsonObject = new JsonObject();
	// jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(results));
	// responseBuilder.status(Response.Status.OK);
	// result.append(jsonObject);
	// } else {
	// result.append(CommonModel.toJson(1, DefinedName.NO_CONTENT));
	// responseBuilder.status(Response.Status.NO_CONTENT);
	// }
	//
	// } catch (Exception e) {
	// result.append("Xảy ra lỗi: " + e.getMessage());
	// responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
	// }
	// return responseBuilder.entity(result.toString()).build();
	// }
	// @GET
	// @Path("dubaotonlapkehoachchitiet")
	// @Produces("application/json; charset=utf-8")
	// public Response
	// findDuBaoTonLapKHChiTiet(@QueryParam(DefinedName.PARAM_NDATE) String
	// nDate,
	// @QueryParam(DefinedName.PARAM_SDATE) String sDate,
	// @QueryParam(DefinedName.PARAM_EDATE) String eDate,
	// @QueryParam(DefinedName.PARAM_TRANGTHAI) String bomaxnnoibop) throws
	// IOException, ServletException {
	// StringBuilder result = new StringBuilder();
	// ResponseBuilder responseBuilder =
	// Response.status(Response.Status.BAD_REQUEST);
	// try {
	// List<ExpectedInventoryReqInfoSub> results = new
	// ArrayList<ExpectedInventoryReqInfoSub>();
	// JsonObject json = new JsonObject();
	// json.addProperty("cal_inv_date",
	// ToolTimeCustomer.convertDateToString(MyUtil.chuyensangStrApiDate(nDate),
	// "dd/MM/yyyy"));
	// json.addProperty("exp_from_date",
	// ToolTimeCustomer.convertDateToString(MyUtil.chuyensangStrApiDate(sDate),
	// "dd/MM/yyyy"));
	// json.addProperty("exp_to_date",
	// ToolTimeCustomer.convertDateToString(MyUtil.chuyensangStrApiDate(eDate),
	// "dd/MM/yyyy"));
	// boolean bomaxnnoibo = "true".equals(bomaxnnoibop) ? true : false;
	// List<Object[]> list =
	// inventoryService.getListExpectedInventory2(JsonParserUtil.getGson().toJson(json),
	// bomaxnnoibo);
	// List<Object[]> listOrder =
	// inventoryService.getListExpectedOrder(JsonParserUtil.getGson().toJson(json),
	// bomaxnnoibo);
	// List<ExpectedInventoryReqInfoSub> duBaoTons = new
	// ArrayList<ExpectedInventoryReqInfoSub>();
	// if (list.size() > 0) {
	// for (Object[] p : list) {
	// // tao du lieu BD
	// ExpectedInventoryReqInfoSub item = new
	// ExpectedInventoryReqInfoSub(Long.parseLong(Objects
	// .toString(p[0])), Objects.toString(p[1], ""), Objects.toString(p[2],
	// null),
	// Double.parseDouble(Objects.toString(p[3], "0")),
	// Double.parseDouble(Objects.toString(p[4],
	// "0")), Double.parseDouble(Objects.toString(p[5], "0")),
	// Double.parseDouble(Objects
	// .toString(p[6], "0"))/30, Double.parseDouble(Objects.toString(p[7],
	// "0")),
	// Double.parseDouble(Objects.toString(p[8],
	// "0")),Double.parseDouble(Objects.toString(p[10], "0")));
	// for (int i = 0; i < listOrder.size(); i++) {
	// if (item.getProduct_code().equals(Objects.toString(listOrder.get(i)[2],
	// null))) {
	// item.setExp_export_quantity(Double.parseDouble(Objects.toString(listOrder.get(i)[4],
	// "0")));
	// listOrder.get(i)[5] = 1;
	// break;
	// }
	// }
	// duBaoTons.add(item);
	// }
	// // kiem tra ds don hang chua co trong danh sach them vao
	// for (int i = 0; i < listOrder.size(); i++) {
	// if (Long.parseLong(Objects.toString(listOrder.get(i)[5])) == 0) {
	// ExpectedInventoryReqInfoSub item = new
	// ExpectedInventoryReqInfoSub(Long.parseLong(Objects
	// .toString(listOrder.get(i)[1])), Objects.toString(listOrder.get(i)[2],
	// ""),
	// Objects.toString(listOrder.get(i)[3], null),Double.parseDouble(Objects
	// .toString(listOrder.get(i)[6])));
	// item.setExp_export_quantity(Double.parseDouble(Objects.toString(listOrder.get(i)[4],
	// "0")));
	// duBaoTons.add(item);
	// }
	// }
	// results.addAll(duBaoTons);
	// JsonObject jsonObject = new JsonObject();
	// jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(results));
	// responseBuilder.status(Response.Status.OK);
	// result.append(jsonObject);
	// } else {
	// result.append(CommonModel.toJson(1, DefinedName.NO_CONTENT));
	// responseBuilder.status(Response.Status.NO_CONTENT);
	// }
	//
	// } catch (Exception e) {
	// result.append("Xảy ra lỗi: " + e.getMessage());
	// responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
	// }
	// return responseBuilder.entity(result.toString()).build();
	// }

	boolean tonghopchinhanh = true;

	@GET
	@Path("dubaotonlapkehoach")
	@Produces("application/json; charset=utf-8")
	public Response findDuBaoTonLapKH(@QueryParam(DefinedName.PARAM_NDATE) String nDate,
			@QueryParam(DefinedName.PARAM_SDATE) String sDate, @QueryParam(DefinedName.PARAM_EDATE) String eDate,
			@QueryParam(DefinedName.PARAM_TRANGTHAI) String bomaxnnoibop,
			@QueryParam(DefinedName.PARAM_CHINHANH) String chinhanh) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);

		try {
			List<ExpectedInventoryReqInfo> results = new ArrayList<ExpectedInventoryReqInfo>();
			List<ExpectedInventoryReqInfo> duBaoTons = new ArrayList<ExpectedInventoryReqInfo>();

			JsonObject json = new JsonObject();
			Date invDate = MyUtil.chuyensangStrApiDate(nDate);
			Date expFromDate = MyUtil.chuyensangStrApiDate(sDate);
			Date expToDate = MyUtil.chuyensangStrApiDate(eDate);
			json.addProperty("cal_inv_date", ToolTimeCustomer.convertDateToString(invDate, "dd/MM/yyyy"));
			json.addProperty("exp_from_date", ToolTimeCustomer.convertDateToString(expFromDate, "dd/MM/yyyy"));
			json.addProperty("exp_to_date", ToolTimeCustomer.convertDateToString(expToDate, "dd/MM/yyyy"));
			boolean bomaxnnoibo = "true".equals(bomaxnnoibop) ? true : false;

			List<Object[]> list = inventoryService.getListExpectedInventory2(JsonParserUtil.getGson().toJson(json),
					bomaxnnoibo);
			List<Object[]> listOrder = inventoryService.getListExpectedOrder(JsonParserUtil.getGson().toJson(json),
					bomaxnnoibo);
			if ("HO CHI MINH".equals(chinhanh)) {
				/*
				 * Lấy dữ liệu tại HCM
				 */
				for (Object[] p : list) {
					ExpectedInventoryReqInfo item = new ExpectedInventoryReqInfo(
							Long.parseLong(Objects.toString(p[0])), Objects.toString(p[1], ""), Objects.toString(p[2],
									null), Double.parseDouble(Objects.toString(p[3], "0")), Double.parseDouble(Objects
									.toString(p[4], "0")), Double.parseDouble(Objects.toString(p[5], "0")),
							Double.parseDouble(Objects.toString(p[6], "0")), Double.parseDouble(Objects.toString(p[7],
									"0")), Double.parseDouble(Objects.toString(p[8], "0")));
					for (int i = 0; i < listOrder.size(); i++) {
						if (item.getProduct_code().equals(Objects.toString(listOrder.get(i)[2], null))) {
							if ("HO CHI MINH".equals(Objects.toString(listOrder.get(i)[0], ""))) {
								// SL dự xuất
								item.setExp_export_quantity(item.getExp_export_quantity()
										+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));

							} else {
								// SL dự xuất BD
								item.setExp_export_quantityBD(item.getExp_export_quantityBD()
										+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
							}
							listOrder.get(i)[5] = 1;
						}
					}
					duBaoTons.add(item);
				}
			} else if ("BINH DUONG".equals(chinhanh)) {
				for (Object[] p : list) {
					ExpectedInventoryReqInfo item = new ExpectedInventoryReqInfo(
							Long.parseLong(Objects.toString(p[0])), Objects.toString(p[1], ""), Objects.toString(p[2],
									null), Double.parseDouble(Objects.toString(p[3], "0")), Double.parseDouble(Objects
									.toString(p[4], "0")), Double.parseDouble(Objects.toString(p[5], "0")),
							Double.parseDouble(Objects.toString(p[6], "0")), Double.parseDouble(Objects.toString(p[7],
									"0")), Double.parseDouble(Objects.toString(p[8], "0")), false,
							Long.parseLong(Objects.toString(p[0])));
					for (int i = 0; i < listOrder.size(); i++) {
						if (item.getProduct_code().equals(Objects.toString(listOrder.get(i)[2], null))) {
							if ("HO CHI MINH".equals(Objects.toString(listOrder.get(i)[0], ""))) {
								// SL dự xuất
								item.setExp_export_quantity(Double.parseDouble(Objects.toString(listOrder.get(i)[4],
										"0")));
							} else {
								// SL dự xuất BD
								item.setExp_export_quantityBD(Double.parseDouble(Objects.toString(listOrder.get(i)[4],
										"0")));
							}
							listOrder.get(i)[5] = 1;
						}
					}
					duBaoTons.add(item);
				}
			}
			// kiem tra ds don hang chua co trong danh sach them vao
			for (int i = 0; i < listOrder.size(); i++) {
				if (Long.parseLong(Objects.toString(listOrder.get(i)[5])) == 0) {
					boolean status = false;
					for (int j = 0; j < duBaoTons.size(); j++) {
						if (Objects.toString(listOrder.get(i)[2], "").equals(duBaoTons.get(j).getProduct_code())) {
							if ("HO CHI MINH".equals(Objects.toString(listOrder.get(i)[0], ""))) {
								// SL dự xuất
								duBaoTons.get(j).setExp_export_quantity(
										duBaoTons.get(j).getExp_export_quantity()
												+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
							} else {
								// SL dự xuất BD
								duBaoTons.get(j).setExp_export_quantityBD(
										duBaoTons.get(j).getExp_export_quantityBD()
												+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
							}
							status = true;
							break;
						}
					}
					if (status == false) {
						ExpectedInventoryReqInfo item = new ExpectedInventoryReqInfo(Long.parseLong(Objects
								.toString(listOrder.get(i)[1])), Objects.toString(listOrder.get(i)[2], ""),
								Objects.toString(listOrder.get(i)[3], null));

						if ("HO CHI MINH".equals(Objects.toString(listOrder.get(i)[0], ""))) {
							// SL dự xuất
							item.setExp_export_quantity(Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
						} else {
							// SL dự xuất BD
							item.setExp_export_quantityBD(Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
						}
						duBaoTons.add(item);
					}
				}
			}
			/*
			 * Lay du lieu tai CN
			 */
			// for (int j = 0; j < duBaoTons.size(); j++) {
			// if ("KN009".equals(duBaoTons.get(j).getProduct_code())) {
			// System.out.println();
			// }
			// }
			if (tonghopchinhanh) {
				List<ExpectedInventoryReqInfo> duBaoTonCNBDs = dubaotonCN(chinhanh, invDate, expFromDate, expToDate,
						bomaxnnoibo, null);
				for (int i = 0; i < duBaoTonCNBDs.size(); i++) {
					boolean status = false;
					for (int j = 0; j < duBaoTons.size(); j++) {
						if (duBaoTonCNBDs.get(i).getProduct_code().equals(duBaoTons.get(j).getProduct_code())) {
							duBaoTons.get(j).setInv_quantity(
									duBaoTons.get(j).getInv_quantity() + duBaoTonCNBDs.get(i).getInv_quantity());
							duBaoTons.get(j).setExp_export_quantity(
									duBaoTons.get(j).getExp_export_quantity()
											+ duBaoTonCNBDs.get(i).getExp_export_quantity());
							duBaoTons.get(j).setAv30_quantity(
									duBaoTons.get(j).getAv30_quantity() + duBaoTonCNBDs.get(i).getAv30_quantity());
							duBaoTons.get(j).setHdhopdongxuatkhau(
									duBaoTons.get(j).getHdhopdongxuatkhau()
											+ duBaoTonCNBDs.get(i).getHdhopdongxuatkhau());
							duBaoTons.get(j).setTonghopdong(
									duBaoTons.get(j).getTonghopdong() + duBaoTonCNBDs.get(i).getTonghopdong());
							duBaoTons.get(j).setInv_quantityBD(
									duBaoTons.get(j).getInv_quantityBD() + duBaoTonCNBDs.get(i).getInv_quantityBD());
							duBaoTons.get(j).setExp_export_quantityBD(
									duBaoTons.get(j).getExp_export_quantityBD()
											+ duBaoTonCNBDs.get(i).getExp_export_quantityBD());
							duBaoTons.get(j).setAv30_quantityBD(
									duBaoTons.get(j).getAv30_quantityBD() + duBaoTonCNBDs.get(i).getAv30_quantityBD());
							duBaoTons.get(j).setHdhopdongxuatkhauBD(
									duBaoTons.get(j).getHdhopdongxuatkhauBD()
											+ duBaoTonCNBDs.get(i).getHdhopdongxuatkhauBD());
							duBaoTons.get(j).setTonghopdongBD(
									duBaoTons.get(j).getTonghopdongBD() + duBaoTonCNBDs.get(i).getTonghopdongBD());
							status = true;
							break;
						}
					}
					if (status == false) {
						duBaoTons.add(duBaoTonCNBDs.get(i));
					}
				}
			}
			for (int i = 0; i < duBaoTons.size(); i++) {
				// if("RT506".equals(duBaoTons.get(i).getProduct_code())){
				// System.out.println();
				// }
				duBaoTons.get(i).setExp_closing_quantity(
						duBaoTons.get(i).getInv_quantity() - duBaoTons.get(i).getExp_export_quantity()
								- (duBaoTons.get(i).getTonghopdong() - duBaoTons.get(i).getHdhopdongxuatkhau()));
				duBaoTons.get(i).setExp_closing_quantityBD(
						duBaoTons.get(i).getInv_quantityBD() - duBaoTons.get(i).getExp_export_quantityBD());

			}
			results.addAll(duBaoTons);
			if (results.size() != 0) {
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
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return responseBuilder.entity(result.toString()).build();
	}

	@Inject
	AccountAPIService accountAPIService;

	private List<ExpectedInventoryReqInfo> dubaotonCN(String cn, Date invDate, Date expFromDate, Date expToDate,
			boolean bomaxnnoibo, String kenhKH) {
		try {
			Gson gson = JsonParserUtil.getGson();
			String chinhanh = cn.equals("HO CHI MINH") ? "BD" : (cn.equals("BINH DUONG") ? "HCM" : "");

			String token = "";
			AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanh);
			if (accountAPI == null) {
				System.out.println("Không có tài khoản đăng nhập API.");
				return new ArrayList<ExpectedInventoryReqInfo>();
			}
			String path = null;
			if ("BD".equals(chinhanh)) {
				path = StaticPath.getPathBD();
			} else if ("HCM".equals(chinhanh)) {
				path = StaticPath.getPathHCM();
			}
			if (path != null) {
				String[] tokentime = new String[] { accountAPI.getToken(), accountAPI.getTimetoken() + "" };
				if (tokentime != null
						&& (tokentime[1] != null && new Date().getTime() < Long.parseLong(tokentime[1]) && tokentime[0] != null)) {
					token = tokentime[0];
				} else {
					dangnhapAPIdongbo(gson, path, chinhanh);
				}
				// Goi ham du bao ton tai chi nhanh
				List<String> params = new ArrayList<String>();
				params.add(DefinedName.PARAM_NDATE);
				params.add(DefinedName.PARAM_SDATE);
				params.add(DefinedName.PARAM_EDATE);
				params.add(DefinedName.PARAM_TRANGTHAI);
				params.add(DefinedName.PARAM_CHINHANH);
				List<String> values = new ArrayList<String>();
				values.add(MyUtil.chuyensangStrApi(invDate));
				values.add(MyUtil.chuyensangStrApi(expFromDate));
				values.add(MyUtil.chuyensangStrApi(expToDate));
				values.add(bomaxnnoibo + "");
				if ("BD".equals(chinhanh)) {
					values.add("BINH DUONG");
				} else if ("HCM".equals(chinhanh)) {
					values.add("HO CHI MINH");
				}
				String datachinhanh = "dubaoton";
				if (kenhKH != null && "kenhKH".equals(kenhKH)) {
					datachinhanh = "dubaotonkenhkh";
				}

				Call call = trong.lixco.com.api.CallAPI.getInstance(token).getMethodGet(
						path + "/api/data/" + datachinhanh, params, values);
				okhttp3.Response response = call.execute();
				if (response.isSuccessful()) {
					String data = response.body().string();
					if ("".equals(data)) {
						System.out.println("Không có dữ liệu " + chinhanh);
					} else {
						JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
						ExpectedInventoryReqInfo[] arrDetail = gson.fromJson(jsonObject.get("data"),
								ExpectedInventoryReqInfo[].class);
						List<ExpectedInventoryReqInfo> chiTietDuBaoTons = new ArrayList<>(Arrays.asList(arrDetail));
						return chiTietDuBaoTons;
					}
				} else {
					if (response.code() == 401) {
						dangnhapAPIdongbo(gson, path, chinhanh);
					} else {
						System.out.println("Xảy ra lỗi " + response.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ExpectedInventoryReqInfo>();
	}

	private void dangnhapAPIdongbo(Gson gson, String path, String chinhanh) throws IOException {
		try {
			AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanh);
			String[] tokentime = new String[2];
			Call call = trong.lixco.com.api.CallAPI.getInstance("").getMethodPost(path + "/api/account/login",
					gson.toJson(accountAPI));
			okhttp3.Response response = call.execute();
			if (response.isSuccessful()) {
				String data = response.body().string();
				JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
				tokentime[0] = jsonObject.get("access_token").getAsString();
				tokentime[1] = jsonObject.get("expires_in").getAsString();
				SecurityConfig.saveToken(accountAPIService, tokentime[0], Long.parseLong(tokentime[1]), chinhanh);
				System.out.println("Đã đăng nhập API " + chinhanh + ". Vui lòng thực hiện lại thao tác");
			} else {
				System.out.println("Tài khoản đăng nhập API " + chinhanh + " không đúng hoặc lỗi "
						+ response.toString());
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	//
	// @GET
	// @Path("dubaotonlapkehoachchitiet")
	// @Produces("application/json; charset=utf-8")
	// public Response findDuBaoTonLapKHChiTiet(@QueryParam("masp") String
	// productCode,
	// @QueryParam(DefinedName.PARAM_SDATE) String sDate,
	// @QueryParam(DefinedName.PARAM_EDATE) String eDate,
	// @QueryParam(DefinedName.PARAM_TRANGTHAI) String bomaxnnoibop,
	// @QueryParam(DefinedName.PARAM_CHINHANH) String chinhanh) throws
	// IOException, ServletException {
	// StringBuilder result = new StringBuilder();
	// ResponseBuilder responseBuilder =
	// Response.status(Response.Status.BAD_REQUEST);
	//
	//
	//
	// double tongsoluongdutru = 0;
	// try {
	// List<ChiTietDuBaoTon> chiTietDuBaoTons = new ArrayList<>();
	//
	// JsonObject json = new JsonObject();
	// Date expFromDate = MyUtil.chuyensangStrApiDate(sDate);
	// Date expToDate = MyUtil.chuyensangStrApiDate(eDate);
	// json.addProperty("exp_from_date",
	// ToolTimeCustomer.convertDateToString(expFromDate, "dd/MM/yyyy"));
	// json.addProperty("exp_to_date",
	// ToolTimeCustomer.convertDateToString(expToDate, "dd/MM/yyyy"));
	// json.addProperty("product_code", productCode);
	// json.addProperty("chinhanh", chinhanh);
	// boolean bomaxnnoibo = "true".equals(bomaxnnoibop) ? true : false;
	//
	// List<Object[]> list = new ArrayList<Object[]>();
	// inventoryService.getListExpectedInventoryDetail3(JsonParserUtil.getGson().toJson(json),
	// list, bomaxnnoibo);
	// String taichinhanh = chinhanh.equals("HO CHI MINH") ? "HCM"
	// : (chinhanh.equals("BINH DUONG") ? "BD" : "");
	// for (Object[] p : list) {
	// double sldonhang_thung = Double.parseDouble(Objects.toString(p[5], "0"));
	// double sldonhangdaxuat_thung = Double.parseDouble(Objects.toString(p[6],
	// "0"));
	// double slkhuyenmai_dvt = Double.parseDouble(Objects.toString(p[7], "0"));
	// double slkhuyenmaidaxuat_dvt = Double.parseDouble(Objects.toString(p[8],
	// "0"));
	//
	// if (sldonhang_thung - sldonhangdaxuat_thung != 0 || slkhuyenmai_dvt -
	// slkhuyenmaidaxuat_dvt != 0) {
	// ChiTietDuBaoTon item = new
	// ChiTietDuBaoTon(Long.parseLong(Objects.toString(p[0])),
	// Objects.toString(p[1], null), Objects.toString(p[2], null),
	// Objects.toString(p[3], "0"),
	// (Date) p[4], sldonhang_thung, sldonhangdaxuat_thung, slkhuyenmai_dvt,
	// slkhuyenmaidaxuat_dvt, Double.parseDouble(Objects.toString(p[9], "0")),
	// Double.parseDouble(Objects.toString(p[10], "0")),
	// Objects.toString(p[11]), (Date) p[12],
	// taichinhanh);
	// chiTietDuBaoTons.add(item);
	// }
	// }
	// if (tonghopchinhanh) {
	// List<ChiTietDuBaoTon> chiTietDuBaoTonsCN = kiemtradonhangCN(chinhanh,
	// productCode);
	// chiTietDuBaoTons.addAll(chiTietDuBaoTonsCN);
	// }
	// if (chiTietDuBaoTons.size() > 0) {
	// for (int i = 0; i < chiTietDuBaoTons.size(); i++) {
	// double soluongkgdt = ((chiTietDuBaoTons.get(i).getSldonhang_thung() -
	// chiTietDuBaoTons.get(i)
	// .getSldonhangdaxuat_thung()) * chiTietDuBaoTons.get(i).getFactor() *
	// chiTietDuBaoTons
	// .get(i).getSpecification())
	// + ((chiTietDuBaoTons.get(i).getSlkhuyenmai_dvt() -
	// chiTietDuBaoTons.get(i)
	// .getSlkhuyenmaidaxuat_dvt()) * chiTietDuBaoTons.get(i).getFactor());
	// tongsoluongdutru += soluongkgdt;
	// }
	// } else {
	// warning("Không có dữ liệu.");
	// }
	// } catch (Exception e) {
	// logger.error("ExpectedInventoryBean.search:" + e.getMessage(), e);
	// }
	//
	// }
}
