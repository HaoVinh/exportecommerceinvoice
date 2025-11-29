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
import lixco.com.reqInfo.ChiTietDuBaoTon;
import lixco.com.reqInfo.ExpectedInventoryReqInfo;
import lixco.com.service.ProductService;

import org.jboss.logging.Logger;

import trong.lixco.com.util.MyUtil;

import com.google.gson.JsonObject;

@Path("data")
public class DuBaoTonServlet {
	@Inject
	private Logger logger;
	@Context
	private HttpServletRequest request;
	@Inject
	IProductService productService;
	@Inject
	IInventoryService inventoryService;

	/*
	 * http://localhost:8280/kiot/api/data/productLixs/search/{containedStr}?
	 * branchId=80204&pageSize=50&pageIndex=1
	 */
	@GET
	@Path("dubaoton")
	@Produces("application/json; charset=utf-8")
	public Response findDuBaoTon(@QueryParam(DefinedName.PARAM_NDATE) String nDate,
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
			List<Object[]> listOrder = inventoryService.getListExpectedOrder(JsonParserUtil.getGson().toJson(json),
					bomaxnnoibo);
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
								} else {
									// SL dự xuất BD
									item.setExp_export_quantityBD(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
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
							} else {
								// SL dự xuất BD
								item.setExp_export_quantityBD(Double.parseDouble(Objects.toString(listOrder.get(i)[4],
										"0")));
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
								} else {
									// SL dự xuất BD
									item.setExp_export_quantityBD(Double.parseDouble(Objects.toString(
											listOrder.get(i)[4], "0")));
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
							} else {
								// SL dự xuất BD
								item.setExp_export_quantityBD(Double.parseDouble(Objects.toString(listOrder.get(i)[4],
										"0")));
							}
							duBaoTons.add(item);
						}
					}
					results.addAll(duBaoTons);
				}

				// for (Object[] p : list) {
				// ExpectedInventoryReqInfo item = new ExpectedInventoryReqInfo(
				// Long.parseLong(Objects.toString(p[0])),
				// Objects.toString(p[1], null), Objects.toString(
				// p[2], null), Double.parseDouble(Objects.toString(p[3], "0")),
				// Double.parseDouble(Objects.toString(p[4], "0")),
				// Double.parseDouble(Objects.toString(p[5],
				// "0")), Double.parseDouble(Objects.toString(p[6], "0")),
				// Double.parseDouble(Objects
				// .toString(p[7], "0")),
				// Double.parseDouble(Objects.toString(p[8], "0")));
				// if (item.getInv_quantity() != 0 ||
				// item.getExp_export_quantity() != 0
				// || (item.getTonghopdong() - item.getHdhopdongxuatkhau()) !=
				// 0)
				// results.add(item);
				// }
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
						ExpectedInventoryReqInfo ex=results.get(i);
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

}
