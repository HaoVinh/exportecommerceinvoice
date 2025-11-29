package lixco.com.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import lixco.com.commom_ejb.MyMath;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.interfaces.IInventoryService;
import lixco.com.interfaces.IProductService;
import lixco.com.reqInfo.ChiTietDuBaoTon;
import lixco.com.reqInfo.ExpectedInventoryReqInfo;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Call;
import okhttp3.Response;

import org.jboss.logging.Logger;
import org.joda.time.LocalDate;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import trong.lixco.com.api.DefinedName;
import trong.lixco.com.api.SecurityConfig;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.AccountAPI;
import trong.lixco.com.service.AccountAPIService;
import trong.lixco.com.service.DongBoService;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.StaticPath;
import trong.lixco.com.util.ToolReport;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Named("expectedInventory2Bean")
@ViewScoped
public class ExpectedInventory2Bean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	private Date invDate;
	private Date expFromDate;
	private Date expToDate;
	@Inject
	private Logger logger;
	@Inject
	private IInventoryService inventoryService;
	private List<ExpectedInventoryReqInfo> results;
	private List<ExpectedInventoryReqInfo> listExpectedInventory;
	private List<ExpectedInventoryReqInfo> listExpectedInventoryFilter;
	private FormatHandler formatHandler;
	@Getter
	@Setter
	int month;
	@Getter
	@Setter
	int year;
	@Getter
	@Setter
	boolean bomaxnnoibo;
	@Getter
	@Setter
	boolean thieuhut;
	@Getter
	@Setter
	boolean thieudutru;
	@Getter
	@Setter
	boolean xuatdukien;
	LocalDate lc;

	@Override
	protected void initItem() {
		try {
			Date currentDate = new Date();
			invDate = ToolTimeCustomer.getFirstDateOfDay(currentDate);
			month = ToolTimeCustomer.getMonthM(currentDate);
			year = ToolTimeCustomer.getYearM(currentDate);
			lc = new LocalDate();
			formatHandler = FormatHandler.getInstance();
			ajax_setDate();

		} catch (Exception e) {
			logger.error("ExpectedInventoryBean.initItem:" + e.getMessage(), e);
		}
	}

	public void ajax_setDate() {
		expFromDate = lc.withMonthOfYear(month).withYear(year).dayOfMonth().withMinimumValue().toDate();
		expToDate = lc.withMonthOfYear(month).withYear(year).dayOfMonth().withMaximumValue().toDate();
	}

	public void ajax_setLocgiatri(String loailoc) {
		listExpectedInventory = new ArrayList<ExpectedInventoryReqInfo>();
		boolean vaoboloc = false;
		if (loailoc == null) {
			thieuhut = false;
			thieudutru = false;
			xuatdukien = false;
		} else {
			if ("thieuhut".equals(loailoc) && thieuhut) {
				vaoboloc = true;
				thieudutru = false;
				xuatdukien = false;
				if (results != null)
					for (int i = 0; i < results.size(); i++) {
						if (results.get(i).getExp_closing_quantityTH() < 0) {
							listExpectedInventory.add(results.get(i));
						}
					}

			} else if ("thieudutru".equals(loailoc) && thieudutru) {
				vaoboloc = true;
				thieuhut = false;
				xuatdukien = false;
				if (results != null)
					for (int i = 0; i < results.size(); i++) {
						double value = results.get(i).getExp_export_quantityTH();
						if (value == 0) {
							listExpectedInventory.add(results.get(i));
						}
					}
			} else if ("xuatdukien".equals(loailoc) && xuatdukien) {
				vaoboloc = true;
				thieuhut = false;
				thieudutru = false;
				if (results != null)
					for (int i = 0; i < results.size(); i++) {
						double value = results.get(i).getExp_export_quantityTH()
								+ (results.get(i).getTonghopdongTH() - results.get(i).getHdhopdongxuatkhauTH());
						if (value != 0) {
							listExpectedInventory.add(results.get(i));
						}
					}
			}
		}
		if (!vaoboloc) {
			listExpectedInventory.addAll(results);
		}
		executeScript("PF('tablenccn').clearFilters();");
	}
@Inject
IProductService productService;
	public void exportExcel() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (listExpectedInventory != null && listExpectedInventory.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "Mã SP", "Tên sản phẩm", "Tồn đầu", "Dự xuất", "Dự xuấtXK", "Dự tồn cuối",
						"Bình quân 30 ngày trước", "Tồn đầu(BD)", "Dự xuất(BD)", "Dự tồn cuối(BD)",
						"Bình quân 30 ngày trước(BD)", "Tồn đầu(TH)", "Dự xuất(TH)", "Dự tồn cuối(TH)",
						"Bình quân 30 ngày trước(TH)","Nhãn hàng" };
				results.add(title);
				 List<String> productCodes = listExpectedInventory.stream()
			                .map(ExpectedInventoryReqInfo::getProduct_code)
			                .collect(Collectors.toList());
				List<Object[]> prdNhs = productService.findByCodeNhanHang(productCodes);
				for (ExpectedInventoryReqInfo t : listExpectedInventory) {
					String nhanhang = null;
					for (int i = 0; i < prdNhs.size(); i++) {
						if(prdNhs.get(i)[1]!=null&&t.getProduct_code().equals(prdNhs.get(i)[0].toString())){
								nhanhang=prdNhs.get(i)[1].toString();
							break;
						}
					}
					Object[] row = { t.getProduct_code(), t.getProduct_name(),
							MyMath.roundCustom(t.getInv_quantity(), 2),
							MyMath.roundCustom(t.getExp_export_quantity(), 2),
							MyMath.roundCustom(t.getTonghopdong() - t.getHdhopdongxuatkhau(), 2),
							MyMath.roundCustom(t.getExp_closing_quantity(), 2),
							MyMath.roundCustom(t.getAv30_quantity() / 30, 2),

							MyMath.roundCustom(t.getInv_quantityBD(), 2),
							MyMath.roundCustom(t.getExp_export_quantityBD(), 2),
							MyMath.roundCustom(t.getExp_closing_quantityBD(), 2),
							MyMath.roundCustom(t.getAv30_quantityBD() / 30, 2),

							MyMath.roundCustom(t.getInv_quantityTH(), 2),
							MyMath.roundCustom(t.getExp_export_quantityTH(), 2),
							MyMath.roundCustom(t.getExp_closing_quantityTH(), 2),
							MyMath.roundCustom(t.getAv30_quantityTH() / 30, 2),nhanhang };
					results.add(row);
				}
				Object[] rowLast = {};
				results.add(rowLast);
				String titleEx = "du_bao_ton_kho_TH";
				ToolReport.printReportExcelRaw(results, titleEx);
			} else {
				notify.message("Không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error("ExpectedInventoryBean.exportExcel:" + e.getMessage(), e);
		}
	}

	public void exportExcelKenhKH() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			search2KenhKH();
			if (listExpectedInventory != null && listExpectedInventory.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "Mã SP", "Tên sản phẩm", "Tồn đầu", "Dự xuất(GT)", "Dự xuất(Khác)",
						"Dự xuất(chung)", "Dự xuấtXK", "Dự tồn cuối", "Bình quân 30 ngày trước", "Tồn đầu(BD)",
						"Dự xuất(BD-GT)", "Dự xuất(BD-Khác)", "Dự xuất(BD-chung)", "Dự tồn cuối(BD)",
						"Bình quân 30 ngày trước(BD)", "Tồn đầu(TH)", "Dự xuất(TH)", "Dự tồn cuối(TH)",
						"Bình quân 30 ngày trước(TH)","Nhãn hàng" };
				results.add(title);
				
				 List<String> productCodes = listExpectedInventory.stream()
			                .map(ExpectedInventoryReqInfo::getProduct_code)
			                .collect(Collectors.toList());
				List<Object[]> prdNhs = productService.findByCodeNhanHang(productCodes);
				for (ExpectedInventoryReqInfo t : listExpectedInventory) {
					
					String nhanhang = null;
					for (int i = 0; i < prdNhs.size(); i++) {
						if(prdNhs.get(i)[1]!=null&&t.getProduct_code().equals(prdNhs.get(i)[0].toString())){
							nhanhang=prdNhs.get(i)[1].toString();
							break;
						}
					}
					Object[] row = {
							t.getProduct_code(),
							t.getProduct_name(),
							MyMath.roundCustom(t.getInv_quantity(), 2),
							MyMath.roundCustom(t.getExp_export_quantityGT(), 2),// dự
																				// xuất
							MyMath.roundCustom(t.getExp_export_quantityKHAC(), 2),// dự
																					// xuất
							MyMath.roundCustom(t.getExp_export_quantity(), 2),// dự
																				// xuất
							MyMath.roundCustom(t.getTonghopdong() - t.getHdhopdongxuatkhau(), 2),// du
																									// xuat
																									// xuat
																									// khau

							MyMath.roundCustom(t.getExp_closing_quantity(), 2),
							MyMath.roundCustom(t.getAv30_quantity() / 30, 2),

							MyMath.roundCustom(t.getInv_quantityBD(), 2),
							
							MyMath.roundCustom(t.getExp_export_quantityBDGT(), 2),// dự
																					// xuất
							MyMath.roundCustom(t.getExp_export_quantityBDKHAC(), 2),// dự
																					// xuất
							MyMath.roundCustom(t.getExp_export_quantityBD(), 2),
							MyMath.roundCustom(t.getExp_closing_quantityBD(), 2),
							MyMath.roundCustom(t.getAv30_quantityBD() / 30, 2),

							MyMath.roundCustom(t.getInv_quantityTH(), 2),
							MyMath.roundCustom(t.getExp_export_quantityTH(), 2),
							MyMath.roundCustom(t.getExp_closing_quantityTH(), 2),
							MyMath.roundCustom(t.getAv30_quantityTH() / 30, 2),nhanhang };
					results.add(row);
				}
				Object[] rowLast = {};
				results.add(rowLast);
				String titleEx = "du_bao_ton_kho_TH";
				ToolReport.printReportExcelRaw(results, titleEx);
			} else {
				notify.message("Không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error("ExpectedInventoryBean.exportExcel:" + e.getMessage(), e);
		}
	}

	public void search2KenhKH() {
		PrimeFaces current = PrimeFaces.current();
		try {
			results = new ArrayList<ExpectedInventoryReqInfo>();
			listExpectedInventory = new ArrayList<>();
			List<ExpectedInventoryReqInfo> duBaoTons = new ArrayList<ExpectedInventoryReqInfo>();
			/* {cal_inv_date:'',exp_from_date:'',exp_to_date:''} */
			JsonObject json = new JsonObject();
			json.addProperty("cal_inv_date", ToolTimeCustomer.convertDateToString(invDate, "dd/MM/yyyy"));
			json.addProperty("exp_from_date", ToolTimeCustomer.convertDateToString(expFromDate, "dd/MM/yyyy"));
			json.addProperty("exp_to_date", ToolTimeCustomer.convertDateToString(expToDate, "dd/MM/yyyy"));
			List<Object[]> list = inventoryService.getListExpectedInventory2(JsonParserUtil.getGson().toJson(json),
					bomaxnnoibo);
			List<Object[]> listOrder = inventoryService.getListExpectedOrderKenhKH(JsonParserUtil.getGson()
					.toJson(json), bomaxnnoibo);
			// Lấy tồn kho tại HCM
			String chinhanh = getDatabase();
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
								if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất GT
									item.setExp_export_quantityGT(item.getExp_export_quantityGT()
											+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất KHAC
									item.setExp_export_quantityKHAC(item.getExp_export_quantityKHAC()
											+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								}
							} else {
								// SL dự xuất BD
								item.setExp_export_quantityBD(item.getExp_export_quantityBD()
										+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất BD GT
									item.setExp_export_quantityBDGT(item.getExp_export_quantityBDGT()
											+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất BD KHAC
									item.setExp_export_quantityBDKHAC(item.getExp_export_quantityBDKHAC()
											+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								}
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
								if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất GT
									item.setExp_export_quantityGT(Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất KHAC
									item.setExp_export_quantityKHAC(Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								}
							} else {
								// SL dự xuất BD
								item.setExp_export_quantityBD(Double.parseDouble(Objects.toString(listOrder.get(i)[4],
										"0")));
								if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất BD GT
									item.setExp_export_quantityBDGT(Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất BD KHAC
									item.setExp_export_quantityBDKHAC(Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								}
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
								if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất GT
									duBaoTons.get(j).setExp_export_quantityGT(
											duBaoTons.get(j).getExp_export_quantityGT()
													+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất KHAC
									duBaoTons.get(j).setExp_export_quantityKHAC(
											duBaoTons.get(j).getExp_export_quantityKHAC()
													+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								}
							} else {
								// SL dự xuất BD
								duBaoTons.get(j).setExp_export_quantityBD(
										duBaoTons.get(j).getExp_export_quantityBD()
												+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất BD GT
									duBaoTons.get(j).setExp_export_quantityBDGT(
											duBaoTons.get(j).getExp_export_quantityBDGT()
													+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
									// SL dự xuất BD KHAC
									duBaoTons.get(j).setExp_export_quantityBDKHAC(
											duBaoTons.get(j).getExp_export_quantityBDKHAC()
													+ Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
								}

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
							if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
								// SL dự xuất BD GT
								item.setExp_export_quantityGT(Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
							} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
								// SL dự xuất BD KHAC
								item.setExp_export_quantityKHAC( Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
							}
						} else {
							// SL dự xuất BD
							item.setExp_export_quantityBD(Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
							if ("GT".equals(Objects.toString(listOrder.get(i)[6], ""))) {
								// SL dự xuất BD GT
								item.setExp_export_quantityBDGT(Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
							} else if ("KHAC".equals(Objects.toString(listOrder.get(i)[6], ""))) {
								// SL dự xuất BD KHAC
								item.setExp_export_quantityBDKHAC( Double.parseDouble(Objects.toString(listOrder.get(i)[4], "0")));
							}
						}
						duBaoTons.add(item);
					}
				}
			}
			/*
			 * Lay du lieu tai CN
			 */
			if (tonghopchinhanh) {
				List<ExpectedInventoryReqInfo> duBaoTonCNBDs = dubaotonCN("kenhKH");
				for (int i = 0; i < duBaoTonCNBDs.size(); i++) {
					boolean status = false;
					for (int j = 0; j < duBaoTons.size(); j++) {
						if (duBaoTonCNBDs.get(i).getProduct_code().equals(duBaoTons.get(j).getProduct_code())) {
							duBaoTons.get(j).setInv_quantity(
									duBaoTons.get(j).getInv_quantity() + duBaoTonCNBDs.get(i).getInv_quantity());
							duBaoTons.get(j).setExp_export_quantity(
									duBaoTons.get(j).getExp_export_quantity()
											+ duBaoTonCNBDs.get(i).getExp_export_quantity());

							duBaoTons.get(j).setExp_export_quantityGT(
									duBaoTons.get(j).getExp_export_quantityGT()
											+ duBaoTonCNBDs.get(i).getExp_export_quantityGT());
							duBaoTons.get(j).setExp_export_quantityKHAC(
									duBaoTons.get(j).getExp_export_quantityKHAC()
											+ duBaoTonCNBDs.get(i).getExp_export_quantityKHAC());

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

							duBaoTons.get(j).setExp_export_quantityBDGT(
									duBaoTons.get(j).getExp_export_quantityBDGT()
											+ duBaoTonCNBDs.get(i).getExp_export_quantityBDGT());
							duBaoTons.get(j).setExp_export_quantityBDKHAC(
									duBaoTons.get(j).getExp_export_quantityBDKHAC()
											+ duBaoTonCNBDs.get(i).getExp_export_quantityBDKHAC());

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
				duBaoTons.get(i).setExp_closing_quantity(
						duBaoTons.get(i).getInv_quantity() - duBaoTons.get(i).getExp_export_quantity()
								- (duBaoTons.get(i).getTonghopdong() - duBaoTons.get(i).getHdhopdongxuatkhau()));
				duBaoTons.get(i).setExp_closing_quantityBD(
						duBaoTons.get(i).getInv_quantityBD() - duBaoTons.get(i).getExp_export_quantityBD());

			}
			results.addAll(duBaoTons);
			ajax_setLocgiatri(null);
			updateform("menuformid,idboloc");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ExpectedInventoryBean.search:" + e.getMessage(), e);
		}
		current.executeScript("PF('tablenccn').clearFilters();");
	}

	@Getter
	@Setter
	boolean tonghopchinhanh = true;

	public void search2() {
		PrimeFaces current = PrimeFaces.current();
		try {
			results = new ArrayList<ExpectedInventoryReqInfo>();
			listExpectedInventory = new ArrayList<>();
			List<ExpectedInventoryReqInfo> duBaoTons = new ArrayList<ExpectedInventoryReqInfo>();
			/* {cal_inv_date:'',exp_from_date:'',exp_to_date:''} */
			JsonObject json = new JsonObject();
			json.addProperty("cal_inv_date", ToolTimeCustomer.convertDateToString(invDate, "dd/MM/yyyy"));
			json.addProperty("exp_from_date", ToolTimeCustomer.convertDateToString(expFromDate, "dd/MM/yyyy"));
			json.addProperty("exp_to_date", ToolTimeCustomer.convertDateToString(expToDate, "dd/MM/yyyy"));
			List<Object[]> list = inventoryService.getListExpectedInventory2(JsonParserUtil.getGson().toJson(json),
					bomaxnnoibo);
			List<Object[]> listOrder = inventoryService.getListExpectedOrder(JsonParserUtil.getGson().toJson(json),
					bomaxnnoibo);
			// Lấy tồn kho tại HCM
			String chinhanh = getDatabase();
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
				List<ExpectedInventoryReqInfo> duBaoTonCNBDs = dubaotonCN(null);
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
//				if("RT506".equals(duBaoTons.get(i).getProduct_code())){
//					System.out.println();
//				}
				duBaoTons.get(i).setExp_closing_quantity(
						duBaoTons.get(i).getInv_quantity() - duBaoTons.get(i).getExp_export_quantity()
								- (duBaoTons.get(i).getTonghopdong() - duBaoTons.get(i).getHdhopdongxuatkhau()));
				duBaoTons.get(i).setExp_closing_quantityBD(
						duBaoTons.get(i).getInv_quantityBD() - duBaoTons.get(i).getExp_export_quantityBD());

			}
			results.addAll(duBaoTons);
			ajax_setLocgiatri(null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ExpectedInventoryBean.search:" + e.getMessage(), e);
		}
		current.executeScript("PF('tablenccn').clearFilters();");
	}

	@Getter
	List<ChiTietDuBaoTon> chiTietDuBaoTons;
	@Getter
	double tongsoluongdutru = 0;

	public void kiemtradonhang(String chinhanh, String productCode) {
		tongsoluongdutru = 0;
		try {
			chiTietDuBaoTons = new ArrayList<>();

			JsonObject json = new JsonObject();
			json.addProperty("exp_from_date", ToolTimeCustomer.convertDateToString(expFromDate, "dd/MM/yyyy"));
			json.addProperty("exp_to_date", ToolTimeCustomer.convertDateToString(expToDate, "dd/MM/yyyy"));
			json.addProperty("product_code", productCode);
			json.addProperty("chinhanh", chinhanh);
			List<Object[]> list = new ArrayList<Object[]>();
			inventoryService.getListExpectedInventoryDetail2(JsonParserUtil.getGson().toJson(json), list, bomaxnnoibo);
			String taichinhanh = getDatabase().equals("HO CHI MINH") ? "HCM"
					: (getDatabase().equals("BINH DUONG") ? "BD" : "");
			for (Object[] p : list) {
				double sldonhang_thung = Double.parseDouble(Objects.toString(p[5], "0"));
				double sldonhangdaxuat_thung = Double.parseDouble(Objects.toString(p[6], "0"));
				double slkhuyenmai_dvt = Double.parseDouble(Objects.toString(p[7], "0"));
				double slkhuyenmaidaxuat_dvt = Double.parseDouble(Objects.toString(p[8], "0"));

				if (sldonhang_thung - sldonhangdaxuat_thung != 0 || slkhuyenmai_dvt - slkhuyenmaidaxuat_dvt != 0) {
					ChiTietDuBaoTon item = new ChiTietDuBaoTon(Long.parseLong(Objects.toString(p[0])),
							Objects.toString(p[1], null), Objects.toString(p[2], null), Objects.toString(p[3], "0"),
							(Date) p[4], sldonhang_thung, sldonhangdaxuat_thung, slkhuyenmai_dvt,
							slkhuyenmaidaxuat_dvt, Double.parseDouble(Objects.toString(p[9], "0")),
							Double.parseDouble(Objects.toString(p[10], "0")), Objects.toString(p[11]), (Date) p[12],
							taichinhanh);
					chiTietDuBaoTons.add(item);
				}
			}
			if (tonghopchinhanh) {
				List<ChiTietDuBaoTon> chiTietDuBaoTonsCN = kiemtradonhangCN(chinhanh, productCode);
				chiTietDuBaoTons.addAll(chiTietDuBaoTonsCN);
			}
			if (chiTietDuBaoTons.size() > 0) {
				for (int i = 0; i < chiTietDuBaoTons.size(); i++) {
					double soluongkgdt = ((chiTietDuBaoTons.get(i).getSldonhang_thung() - chiTietDuBaoTons.get(i)
							.getSldonhangdaxuat_thung()) * chiTietDuBaoTons.get(i).getFactor() * chiTietDuBaoTons
							.get(i).getSpecification())
							+ ((chiTietDuBaoTons.get(i).getSlkhuyenmai_dvt() - chiTietDuBaoTons.get(i)
									.getSlkhuyenmaidaxuat_dvt()) * chiTietDuBaoTons.get(i).getFactor());
					tongsoluongdutru += soluongkgdt;
				}
				showDialog("dlchitietdubaoton");
				updateform("idchitietdubaoton");
			} else {
				warning("Không có dữ liệu.");
			}
		} catch (Exception e) {
			logger.error("ExpectedInventoryBean.search:" + e.getMessage(), e);
		}

	}

	private List<ChiTietDuBaoTon> kiemtradonhangCN(String chinhanh, String productCode) {
		try {
			List<ChiTietDuBaoTon> chiTietDuBaoTons = chitietdubaotonCN(chinhanh, productCode);
			if (chiTietDuBaoTons != null) {
				String taichinhanh = getDatabase().equals("HO CHI MINH") ? "BD"
						: (getDatabase().equals("BINH DUONG") ? "HCM" : "");
				for (int i = 0; i < chiTietDuBaoTons.size(); i++) {
					chiTietDuBaoTons.get(i).setBrand(taichinhanh);
				}
			}
			return chiTietDuBaoTons;
		} catch (Exception e) {
			logger.error("ExpectedInventoryBean.search:" + e.getMessage(), e);
		}
		return new ArrayList<ChiTietDuBaoTon>();
	}

	@Inject
	AccountAPIService accountAPIService;
	@Inject
	DongBoService dongBoService;

	private List<ChiTietDuBaoTon> chitietdubaotonCN(String chinhanhdl, String productCode) {
		try {

			Gson gson = JsonParserUtil.getGson();
			String chinhanh = getDatabase().equals("HO CHI MINH") ? "BD" : (getDatabase().equals("BINH DUONG") ? "HCM"
					: "");

			String token = "";
			AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanh);
			if (accountAPI == null) {
				noticeError("Không có tài khoản đăng nhập API.");
				return new ArrayList<ChiTietDuBaoTon>();
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
					noticeError("Chưa có token truy cập.");
				}
				// Goi ham dong bo BD
				List<String> params = new ArrayList<String>();
				params.add(DefinedName.PARAM_SDATE);
				params.add(DefinedName.PARAM_EDATE);
				params.add(DefinedName.PARAM_TRANGTHAI);
				params.add(DefinedName.PARAM_CODE);
				params.add(DefinedName.PARAM_CHINHANH);
				List<String> values = new ArrayList<String>();
				values.add(MyUtil.chuyensangStrApi(expFromDate));
				values.add(MyUtil.chuyensangStrApi(expToDate));
				values.add(bomaxnnoibo + "");
				values.add(productCode + "");
				values.add(chinhanhdl);

				Call call = trong.lixco.com.api.CallAPI.getInstance(token).getMethodGet(
						path + "/api/data/chitietdubaoton", params, values);

				Response response = call.execute();
				if (response.isSuccessful()) {
					String data = response.body().string();
					JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
					if (jsonObject != null) {
						ChiTietDuBaoTon[] arrDetail = gson.fromJson(jsonObject.get("data"), ChiTietDuBaoTon[].class);
						List<ChiTietDuBaoTon> chiTietDuBaoTons = new ArrayList<>(Arrays.asList(arrDetail));
						return chiTietDuBaoTons;
					}
					return new ArrayList<ChiTietDuBaoTon>();
				} else {

					noticeError("Xảy ra lỗi " + response.toString());

				}
			} else {
				noticeError("Chưa cài link truy cập chi nhánh.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getLocalizedMessage());
		}
		return new ArrayList<ChiTietDuBaoTon>();
	}

	private List<ExpectedInventoryReqInfo> dubaotonCN(String kenhKH) {
		try {
			Gson gson = JsonParserUtil.getGson();
			String chinhanh = getDatabase().equals("HO CHI MINH") ? "BD" : (getDatabase().equals("BINH DUONG") ? "HCM"
					: "");

			String token = "";
			AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanh);
			if (accountAPI == null) {
				noticeError("Không có tài khoản đăng nhập API.");
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
				Response response = call.execute();
				if (response.isSuccessful()) {
					String data = response.body().string();
					if ("".equals(data)) {
						warning("Không có dữ liệu " + chinhanh);
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
						noticeError("Xảy ra lỗi " + response.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getLocalizedMessage());
		}
		return new ArrayList<ExpectedInventoryReqInfo>();
	}

	private void dangnhapAPIdongbo(Gson gson, String path, String chinhanh) throws IOException {
		AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanh);
		String[] tokentime = new String[2];
		Call call = trong.lixco.com.api.CallAPI.getInstance("").getMethodPost(path + "/api/account/login",
				gson.toJson(accountAPI));
		Response response = call.execute();
		if (response.isSuccessful()) {
			String data = response.body().string();
			JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
			tokentime[0] = jsonObject.get("access_token").getAsString();
			tokentime[1] = jsonObject.get("expires_in").getAsString();
			SecurityConfig.saveToken(accountAPIService, tokentime[0], Long.parseLong(tokentime[1]), chinhanh);
			noticeError("Đã đăng nhập API " + chinhanh + ". Vui lòng thực hiện lại thao tác");
		} else {
			noticeError("Tài khoản đăng nhập API " + chinhanh + " không đúng hoặc lỗi " + response.toString());
			return;
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public Date getInvDate() {
		return invDate;
	}

	public void setInvDate(Date invDate) {
		this.invDate = invDate;
	}

	public Date getExpFromDate() {
		return expFromDate;
	}

	public void setExpFromDate(Date expFromDate) {
		this.expFromDate = expFromDate;
	}

	public Date getExpToDate() {
		return expToDate;
	}

	public void setExpToDate(Date expToDate) {
		this.expToDate = expToDate;
	}

	public List<ExpectedInventoryReqInfo> getListExpectedInventory() {
		return listExpectedInventory;
	}

	public List<ExpectedInventoryReqInfo> getListExpectedInventoryFilter() {
		return listExpectedInventoryFilter;
	}

	public void setListExpectedInventoryFilter(List<ExpectedInventoryReqInfo> listExpectedInventoryFilter) {
		this.listExpectedInventoryFilter = listExpectedInventoryFilter;
	}

	public FormatHandler getFormatHandler() {
		return formatHandler;
	}

	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
	}
}
