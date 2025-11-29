package lixco.com.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import lixco.com.commom_ejb.MyMath;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.interfaces.IInventoryService;
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

@Named("expectedInventoryBean")
@ViewScoped
public class ExpectedInventoryBean extends AbstractBean {
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
			if ("thieuhut".equals(loailoc)) {
				vaoboloc = true;
				thieudutru = false;
				xuatdukien = false;
				if (results != null)
					for (int i = 0; i < results.size(); i++) {
						if (results.get(i).getExp_closing_quantity() < 0) {
							listExpectedInventory.add(results.get(i));
						}
					}

			} else if ("thieudutru".equals(loailoc)) {
				vaoboloc = true;
				thieuhut = false;
				xuatdukien = false;
				if (results != null)
					for (int i = 0; i < results.size(); i++) {
						double value = results.get(i).getExp_export_quantity()
								+ (results.get(i).getTonghopdong() - results.get(i).getHdhopdongxuatkhau());
						if (value == 0) {
							listExpectedInventory.add(results.get(i));
						}
					}
			} else if ("xuatdukien".equals(loailoc)) {
				vaoboloc = true;
				thieuhut = false;
				thieudutru = false;
				if (results != null)
					for (int i = 0; i < results.size(); i++) {
						double value = results.get(i).getExp_export_quantity()
								+ (results.get(i).getTonghopdong() - results.get(i).getHdhopdongxuatkhau());
						if (value != 0) {
							listExpectedInventory.add(results.get(i));
						}
					}
			}
		}
		if (!vaoboloc) {
			listExpectedInventory.addAll(results);
		}
		executeScript("PF('tablenc').clearFilters();");
	}

	public void exportExcel() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (listExpectedInventory != null && listExpectedInventory.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "Mã SP", "Tên sản phẩm", "Tồn đầu", "Dự xuất", "Dự xuấtXK", "Dự tồn cuối",
						"Bình quân 30 ngày trước" };
				results.add(title);
				for (ExpectedInventoryReqInfo t : listExpectedInventory) {
					Object[] row = { t.getProduct_code(), t.getProduct_name(),
							MyMath.roundCustom(t.getInv_quantity(), 2),
							MyMath.roundCustom(t.getExp_export_quantity(), 2),
							MyMath.roundCustom(t.getTonghopdong() - t.getHdhopdongxuatkhau(), 2),
							MyMath.roundCustom(t.getExp_closing_quantity(), 2),
							MyMath.roundCustom(t.getAv30_quantity() / 30, 2) };
					results.add(row);
				}
				Object[] rowLast = {};
				results.add(rowLast);
				String titleEx = "du_bao_ton_kho";
				ToolReport.printReportExcelRaw(results, titleEx);
			} else {
				notify.message("Không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error("ExpectedInventoryBean.exportExcel:" + e.getMessage(), e);
		}
	}
	public void search() {
		PrimeFaces current = PrimeFaces.current();
		try {
			results = new ArrayList<ExpectedInventoryReqInfo>();
			listExpectedInventory = new ArrayList<>();
			/* {cal_inv_date:'',exp_from_date:'',exp_to_date:''} */
			JsonObject json = new JsonObject();
			json.addProperty("cal_inv_date", ToolTimeCustomer.convertDateToString(invDate, "dd/MM/yyyy"));
			json.addProperty("exp_from_date", ToolTimeCustomer.convertDateToString(expFromDate, "dd/MM/yyyy"));
			json.addProperty("exp_to_date", ToolTimeCustomer.convertDateToString(expToDate, "dd/MM/yyyy"));
			List<Object[]> list = new ArrayList<Object[]>();
			inventoryService.getListExpectedInventory(JsonParserUtil.getGson().toJson(json), list, bomaxnnoibo);
			if (list.size() > 0) {
				for (Object[] p : list) {
					ExpectedInventoryReqInfo item = new ExpectedInventoryReqInfo(
							Long.parseLong(Objects.toString(p[0])), Objects.toString(p[1], null), Objects.toString(
									p[2], null), Double.parseDouble(Objects.toString(p[3], "0")),
							Double.parseDouble(Objects.toString(p[4], "0")), Double.parseDouble(Objects.toString(p[5],
									"0")), Double.parseDouble(Objects.toString(p[6], "0")), Double.parseDouble(Objects
									.toString(p[7], "0")), Double.parseDouble(Objects.toString(p[8], "0")));
//					double a=Double.parseDouble(Objects.toString(p[9], "0"));
//					if("RC002".equals(item.getProduct_code()))
//						System.out.println(a);
					if (item.getInv_quantity() != 0 || item.getExp_export_quantity() != 0
							|| (item.getTonghopdong() - item.getHdhopdongxuatkhau()) != 0)
						
//					if(Math.abs(item.getInv_quantity())>0.01){
//						double a=Math.abs(item.getInv_quantity());
						results.add(item);
//					}
				}
				ajax_setLocgiatri(null);
			} else {
				current.executeScript("swaldesigntimer('ThÃ´ng bÃ¡o', 'KhÃ´ng cÃ³ dá»¯ liá»‡u','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("ExpectedInventoryBean.search:" + e.getMessage(), e);
		}
		current.executeScript("PF('tablenc').clearFilters();");
	}

	@Getter
	@Setter
	boolean tonghopchinhanh;

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
			List<Object[]> list = new ArrayList<Object[]>();
			inventoryService.getListExpectedInventory(JsonParserUtil.getGson().toJson(json), list, bomaxnnoibo);
			List<ExpectedInventoryReqInfo> duBaoTonCNs = new ArrayList<ExpectedInventoryReqInfo>();
			if (tonghopchinhanh) {
				duBaoTonCNs = dubaotonCN();
			}
			for (Object[] p : list) {
				ExpectedInventoryReqInfo item = new ExpectedInventoryReqInfo(Long.parseLong(Objects.toString(p[0])),
						Objects.toString(p[1], null), Objects.toString(p[2], null), Double.parseDouble(Objects
								.toString(p[3], "0")), Double.parseDouble(Objects.toString(p[4], "0")),
						Double.parseDouble(Objects.toString(p[5], "0")),
						Double.parseDouble(Objects.toString(p[6], "0")),
						Double.parseDouble(Objects.toString(p[7], "0")),
						Double.parseDouble(Objects.toString(p[8], "0")));
				if (item.getInv_quantity() != 0 || item.getExp_export_quantity() != 0
						|| (item.getTonghopdong() - item.getHdhopdongxuatkhau()) != 0)
					duBaoTons.add(item);
			}

			// gop danh sach du tru cac chi nhanh
			String chinhanh = getDatabase().equals("HO CHI MINH") ? "HCM" : (getDatabase().equals("BINH DUONG") ? "BD"
					: "");
			if ("HCM".equals(chinhanh)) {
				// cai dat so lieu BD
				// duBaoTonCNs: BD
				// duBaoTons: HCM
				for (int i = 0; i < duBaoTons.size(); i++) {
					for (int j = 0; j < duBaoTonCNs.size(); j++) {
						if (duBaoTonCNs.get(j).isDagoptemp() == false) {
							if (duBaoTons.get(i).getProduct_code().equals(duBaoTonCNs.get(j).getProduct_code())) {
								duBaoTons.get(i).setInv_quantityBD(duBaoTonCNs.get(j).getInv_quantity());
								duBaoTons.get(i).setExp_export_quantityBD(duBaoTonCNs.get(j).getExp_export_quantity());
								duBaoTons.get(i)
										.setExp_closing_quantityBD(duBaoTonCNs.get(j).getExp_closing_quantity());
								duBaoTons.get(i).setAv30_quantityBD(duBaoTonCNs.get(j).getAv30_quantity());
								duBaoTons.get(i).setHdhopdongxuatkhauBD(duBaoTonCNs.get(j).getHdhopdongxuatkhau());
								duBaoTons.get(i).setTonghopdongBD(duBaoTonCNs.get(j).getTonghopdong());
								duBaoTons.get(i).setProduct_id_cn(duBaoTonCNs.get(j).getProduct_id());
								duBaoTonCNs.get(j).setDagoptemp(true);
								break;
							}
						}
					}
					results.add(duBaoTons.get(i));
				}
				for (int i = 0; i < duBaoTonCNs.size(); i++) {
					if (duBaoTonCNs.get(i).isDagoptemp() == false) {
						ExpectedInventoryReqInfo item = new ExpectedInventoryReqInfo(0, duBaoTonCNs.get(i)
								.getProduct_code(), duBaoTonCNs.get(i).getProduct_name(), duBaoTonCNs.get(i)
								.getInv_quantity(), duBaoTonCNs.get(i).getExp_export_quantity(), duBaoTonCNs.get(i)
								.getExp_closing_quantity(), duBaoTonCNs.get(i).getAv30_quantity(), duBaoTonCNs.get(i)
								.getHdhopdongxuatkhau(), duBaoTonCNs.get(i).getTonghopdong(), true, duBaoTonCNs.get(i)
								.getProduct_id());
						results.add(item);
					}
				}
			} else if ("BD".equals(chinhanh)) {
				// cai dat so lieu BD
				// duBaoTonCNs: HCM
				// duBaoTons: BD
				for (int i = 0; i < duBaoTonCNs.size(); i++) {
					for (int j = 0; j < duBaoTons.size(); j++) {
						if (duBaoTons.get(j).isDagoptemp() == false) {
							if (duBaoTonCNs.get(i).getProduct_code().equals(duBaoTons.get(j).getProduct_code())) {
								duBaoTonCNs.get(i).setInv_quantityBD(duBaoTons.get(j).getInv_quantity());
								duBaoTonCNs.get(i).setExp_export_quantityBD(duBaoTons.get(j).getExp_export_quantity());
								duBaoTonCNs.get(i)
										.setExp_closing_quantityBD(duBaoTons.get(j).getExp_closing_quantity());
								duBaoTonCNs.get(i).setAv30_quantityBD(duBaoTons.get(j).getAv30_quantity());
								duBaoTonCNs.get(i).setHdhopdongxuatkhauBD(duBaoTons.get(j).getHdhopdongxuatkhau());
								duBaoTonCNs.get(i).setTonghopdongBD(duBaoTons.get(j).getTonghopdong());
								duBaoTonCNs.get(j).setDagoptemp(true);
								duBaoTonCNs.get(i).setProduct_id_cn(duBaoTonCNs.get(j).getProduct_id());
								break;
							}
						}
					}
					results.add(duBaoTonCNs.get(i));
				}
				for (int i = 0; i < duBaoTons.size(); i++) {
					if (duBaoTons.get(i).isDagoptemp() == false) {
						ExpectedInventoryReqInfo item = new ExpectedInventoryReqInfo(duBaoTons.get(i).getProduct_id(),
								duBaoTons.get(i).getProduct_code(), duBaoTons.get(i).getProduct_name(), duBaoTons
										.get(i).getInv_quantity(), duBaoTons.get(i).getExp_export_quantity(), duBaoTons
										.get(i).getExp_closing_quantity(), duBaoTons.get(i).getAv30_quantity(),
								duBaoTons.get(i).getHdhopdongxuatkhau(), duBaoTons.get(i).getTonghopdong(), true,
								duBaoTons.get(i).getProduct_id());
						results.add(item);
					}
				}
			} else {
				// Neu truong hop BN
				results.addAll(duBaoTons);
			}
			if (results.size() == 0) {
				current.executeScript("swaldesigntimer('Thông báo', 'Không có dữ liệu','warning',2000);");
			}
			ajax_setLocgiatri(null);
		} catch (Exception e) {
			logger.error("ExpectedInventoryBean.search:" + e.getMessage(), e);
		}
		current.executeScript("PF('tablenc').clearFilters();");
		current.executeScript("PF('tablenccn').clearFilters();");
	}

	@Getter
	List<ChiTietDuBaoTon> chiTietDuBaoTons;
	@Getter
	double tongsoluongdutru = 0;

	public void kiemtradonhang(ExpectedInventoryReqInfo itemparam) {
		PrimeFaces current = PrimeFaces.current();
		tongsoluongdutru = 0;
		try {
			chiTietDuBaoTons = new ArrayList<>();
			/* {cal_inv_date:'',exp_from_date:'',exp_to_date:''} */
			if (itemparam.getProduct_id() == 0) {
				warning("Không có dữ liệu chi tiết dự báo xuất.");
			} else {
				JsonObject json = new JsonObject();
				json.addProperty("exp_from_date", ToolTimeCustomer.convertDateToString(expFromDate, "dd/MM/yyyy"));
				json.addProperty("exp_to_date", ToolTimeCustomer.convertDateToString(expToDate, "dd/MM/yyyy"));
				json.addProperty("product_id", itemparam.getProduct_id());
				List<Object[]> list = new ArrayList<Object[]>();
				inventoryService.getListExpectedInventoryDetail(JsonParserUtil.getGson().toJson(json), list,
						bomaxnnoibo);
				if (list.size() > 0) {
					for (Object[] p : list) {
						double sldonhang_thung = Double.parseDouble(Objects.toString(p[5], "0"));
						double sldonhangdaxuat_thung = Double.parseDouble(Objects.toString(p[6], "0"));
						double slkhuyenmai_dvt = Double.parseDouble(Objects.toString(p[7], "0"));
						double slkhuyenmaidaxuat_dvt = Double.parseDouble(Objects.toString(p[8], "0"));
						
						if ((sldonhang_thung - sldonhangdaxuat_thung != 0 || slkhuyenmai_dvt - slkhuyenmaidaxuat_dvt != 0)
								&& Long.parseLong(Objects.toString(p[0])) == itemparam.getProduct_id()) {
							ChiTietDuBaoTon item = new ChiTietDuBaoTon(Long.parseLong(Objects.toString(p[0])),
									Objects.toString(p[1], null), Objects.toString(p[2], null), Objects.toString(p[3],
											"0"), (Date) p[4], sldonhang_thung, sldonhangdaxuat_thung, slkhuyenmai_dvt,
									slkhuyenmaidaxuat_dvt, Double.parseDouble(Objects.toString(p[9], "0")),
									Double.parseDouble(Objects.toString(p[10], "0")), Objects.toString(p[11]),
									(Date) p[12]);
							chiTietDuBaoTons.add(item);
						}
					}
					tongsoluongdutru = itemparam.getExp_export_quantity();
					showDialog("dlchitietdubaoton");
					updateform("idchitietdubaoton");
				} else {
					warning("Không có dữ liệu.");
				}
			}
		} catch (Exception e) {
			logger.error("ExpectedInventoryBean.search:" + e.getMessage(), e);
		}

	}

	public void kiemtradonhangCN(ExpectedInventoryReqInfo itemparam) {
		tongsoluongdutru = 0;
		try {
			chiTietDuBaoTons = new ArrayList<>();
			/* {cal_inv_date:'',exp_from_date:'',exp_to_date:''} */
			if (itemparam.getProduct_id_cn() == 0) {
				warning("Không có dữ liệu chi tiết dự báo xuất.");
			} else {
				chiTietDuBaoTons = chitietdubaotonCN(itemparam.getProduct_id_cn());

				if (chiTietDuBaoTons.size() > 0) {
					tongsoluongdutru = itemparam.getExp_export_quantityBD();
					showDialog("dlchitietdubaoton");
					updateform("idchitietdubaoton");
				} else {
					warning("Không có dữ liệu.");
				}
			}
		} catch (Exception e) {
			logger.error("ExpectedInventoryBean.search:" + e.getMessage(), e);
		}

	}

	@Inject
	AccountAPIService accountAPIService;
	@Inject
	DongBoService dongBoService;
	int solandangnhap;

	private List<ChiTietDuBaoTon> chitietdubaotonCN(long product_id_cn) {
		try {

			Gson gson = JsonParserUtil.getGson();
			String chinhanh = getDatabase().equals("HO CHI MINH") ? "BD" : (getDatabase().equals("BD") ? "HCM" : "");

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
				params.add(DefinedName.PARAM_ID);
				List<String> values = new ArrayList<String>();
				values.add(MyUtil.chuyensangStrApi(expFromDate));
				values.add(MyUtil.chuyensangStrApi(expToDate));
				values.add(bomaxnnoibo + "");
				values.add(product_id_cn + "");

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

	private List<ExpectedInventoryReqInfo> dubaotonCN() {
		try {
			solandangnhap++;
			if (solandangnhap > 4) {
				noticeError("Token đăng nhập lỗi, kiểm tra lại tài khoản liên kết.");
			} else {
				Gson gson = JsonParserUtil.getGson();
				String chinhanh = getDatabase().equals("HO CHI MINH") ? "BD"
						: (getDatabase().equals("BD") ? "HCM" : "");

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
					// Goi ham dong bo BD
					List<String> params = new ArrayList<String>();
					params.add(DefinedName.PARAM_NDATE);
					params.add(DefinedName.PARAM_SDATE);
					params.add(DefinedName.PARAM_EDATE);
					params.add(DefinedName.PARAM_TRANGTHAI);
					List<String> values = new ArrayList<String>();
					values.add(MyUtil.chuyensangStrApi(invDate));
					values.add(MyUtil.chuyensangStrApi(expFromDate));
					values.add(MyUtil.chuyensangStrApi(expToDate));
					values.add(bomaxnnoibo + "");

					Call call = trong.lixco.com.api.CallAPI.getInstance(token).getMethodGet(
							path + "/api/data/dubaoton", params, values);
					Response response = call.execute();
					if (response.isSuccessful()) {
						String data = response.body().string();
						JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
						ExpectedInventoryReqInfo[] arrDetail = gson.fromJson(jsonObject.get("data"),
								ExpectedInventoryReqInfo[].class);
						List<ExpectedInventoryReqInfo> chiTietDuBaoTons = new ArrayList<>(Arrays.asList(arrDetail));
						return chiTietDuBaoTons;
					} else {
						if (response.code() == 401) {
							dangnhapAPIdongbo(gson, path, chinhanh);
						} else {
							noticeError("Xảy ra lỗi " + response.toString());
						}
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
			dubaotonCN();
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
