package lixco.com.bean_report;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.MyMath;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Product;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.IInventoryService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.TonKhoThang;
import lixco.com.reqInfo.InfoInventory;
import lixco.com.service.TonKhoThucTeService;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.jboss.logging.Logger;
import org.joda.time.LocalDate;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.TonKhoThucTe;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

import com.google.gson.JsonObject;

@Named
@ViewScoped
public class ReportTonKhoThangBean extends AbstractBean {
	private static final long serialVersionUID = -8870786800128625218L;
	@Inject
	private Logger logger;
	@Inject
	private IReportService reportService;
	@Inject
	private IProductService productService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private IInventoryService inventoryService;
	private int monthSearch;
	private int yearSearch;
	private Product productSearch;
	private ProductType productTypeSearch;
	private List<ProductType> listProductType;
	private int typep;
	private List<TonKhoThang> listTonKhoThang;
	private List<TonKhoThang> listTonKhoThangFilter;
	private FormatHandler formatHandler;
	private InfoInventory infoInventory;
	private String title = "";

	@Override
	protected void initItem() {
		try {
			monthSearch = ToolTimeCustomer.getMonthCurrent();
			yearSearch = ToolTimeCustomer.getYearCurrent();
			listProductType = new ArrayList<>();
			productTypeService.selectAll(listProductType);
			typep = -1;
			formatHandler = FormatHandler.getInstance();
			getInfo();
		} catch (Exception e) {
			logger.error("ReportTonKhoThangBean.initItem:" + e.getMessage(), e);
		}
	}

	public void getInfo() {
		try {
			infoInventory = null;
			title = "";
			InfoInventory temp = new InfoInventory();
			int code = inventoryService.infoInventory(monthSearch, yearSearch, temp);
			if (code == 0) {
				infoInventory = temp;
				title = "Tháng " + monthSearch + " năm " + yearSearch + " cập nhật tồn vào lúc "
						+ ToolTimeCustomer.convertDateToString(temp.getCreated_date(), "dd/MM/yyyy HH:mm:ss");
			} else {
				title = "Tháng " + monthSearch + " năm " + yearSearch + " chưa cập nhật tồn";
			}
		} catch (Exception e) {
			logger.error("ReportTonKhoThangBean.getInfo:" + e.getMessage(), e);
		}
	}

	public double[] sumByType(long productTypeId) {
		double[] arr = { 0, 0, 0, 0, 0, 0, 0, 0 };
		try {
			for (TonKhoThang p : listTonKhoThang) {
				if (p.getProduct_type_id() == productTypeId) {
					arr[0] += p.getUnit_opening_balance();
					arr[1] += p.getUnit_opening_balance() / p.getFactor();
					arr[2] += p.getKg_import_quantity();
					arr[3] += p.getUnit_import_quantity();
					arr[4] += p.getKg_export_quantity();
					arr[5] += p.getUnit_export_quantity();
					arr[6] += p.getKg_closing_balance();
					arr[7] += p.getKg_closing_balance() / p.getFactor();
				}
			}
			return arr;
		} catch (Exception e) {
			logger.error("ReportTonKhoThangBean.sumByType:" + e.getMessage(), e);
		}
		return arr;
	}

	public void search() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			infoInventory = null;
			/* {month:0, year:0, product_type_id:0,product_id:0,typep:-1} */
			JsonObject json = new JsonObject();
			json.addProperty("month", monthSearch);
			json.addProperty("year", yearSearch);
			json.addProperty("product_type_id", productTypeSearch == null ? 0 : productTypeSearch.getId());
			json.addProperty("product_id", productSearch == null ? 0 : productSearch.getId());
			json.addProperty("typep", typep);
			InfoInventory temp = new InfoInventory();
			int code = inventoryService.infoInventory(monthSearch, yearSearch, temp);
			listTonKhoThang = new ArrayList<>();
			LocalDate lc = new LocalDate();
			if (code == 0 && (lc.getMonthOfYear() != monthSearch || lc.getYear() != yearSearch)) {
				infoInventory = temp;
				inventoryService.getListInventory(JsonParserUtil.getGson().toJson(json), listTonKhoThang);
				title = "Tháng " + monthSearch + " năm " + yearSearch + " cập nhật tồn vào lúc "
						+ ToolTimeCustomer.convertDateToString(temp.getCreated_date(), "dd/MM/yyyy HH:mm:ss");
			} else {
				title = "Tháng " + monthSearch + " năm " + yearSearch + " chưa cập nhật tồn";
				reportService.reportTonKhoThang(JsonParserUtil.getGson().toJson(json), listTonKhoThang);
				if (listTonKhoThang.size() == 0) {
					notify.warning("Không có dữ liệu!");
				}
			}
			if (code == 0) {
				title = "Tháng " + monthSearch + " năm " + yearSearch + " cập nhật tồn vào lúc "
						+ ToolTimeCustomer.convertDateToString(temp.getCreated_date(), "dd/MM/yyyy HH:mm:ss");
			}

		} catch (Exception e) {
			logger.error("ReportTonKhoThangBean.search:" + e.getMessage(), e);
		}
		current.executeScript("PF('tablenc').clearFilters();");
	}
	

	public List<Product> completeProduct(String text) {
		try {
			List<Product> list = new ArrayList<>();
			productService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("ReportTonKhoThangBean.completeProduct:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Inject
	TonKhoThucTeService tonKhoThucTeService;

	public void capnhattonkhothucte() {
		try {
			infoInventory = null;
			Date now = new Date();
			int monthSearch = now.getMonth() + 1;
			int yearSearch = now.getYear() + 1900;
			JsonObject json = new JsonObject();
			json.addProperty("month", monthSearch);
			json.addProperty("year", yearSearch);
			json.addProperty("product_type_id", 0);
			json.addProperty("product_id", 0);
			json.addProperty("typep", -1);
			List<TonKhoThang> listTonKhoThang = new ArrayList<>();
			reportService.reportTonKhoThang(JsonParserUtil.getGson().toJson(json), listTonKhoThang);
			if (listTonKhoThang.size() > 0) {
				tonKhoThucTeService.saveOrUpdate(listTonKhoThang);
				notice("Đã cập nhật thành công.");
			} else {
				warning("Không có dữ liệu");
			}
		} catch (Exception e) {
			e.printStackTrace();
			noticeError(e.getMessage());
			logger.error("ReportTonKhoThangBean.reportTonKhoSanPhamTheoThang:" + e.getMessage(), e);
		}
	}
	public void capnhattonkhothucteExcel() {
		
		try {
			infoInventory = null;
			Date now = new Date();
			int monthSearch = now.getMonth() + 1;
			int yearSearch = now.getYear() + 1900;
			JsonObject json = new JsonObject();
			json.addProperty("month", monthSearch);
			json.addProperty("year", yearSearch);
			json.addProperty("product_type_id", 0);
			json.addProperty("product_id", 0);
			json.addProperty("typep", -1);
			List<Object[]> results = new ArrayList<Object[]>();
			Object[] title = { "MÃ SP", "SẢN PHẨM", "TỒN ĐẦU(Kg)", "TỒN ĐẦU(ĐVT)", "NHẬP(Kg)", "NHẬP(ĐVT)",
					"SL.XUẤT(Kg)", "SL.XUẤT(ĐVT)", "SL.CUỐI(Kg)", "SL.CUỐI(ĐVT)", "SL.TỒN (File tồn)" };
			results.add(title);
			List<TonKhoThang> listTonKhoThang = new ArrayList<>();
			reportService.reportTonKhoThang(JsonParserUtil.getGson().toJson(json), listTonKhoThang);
			for (int i = 0; i < listTonKhoThang.size(); i++) {
					TonKhoThucTe ttt = tonKhoThucTeService.findIdSP(listTonKhoThang.get(i).getProduct_id());
					
					Object[] row = {
							listTonKhoThang.get(i).getProduct_code(),
							listTonKhoThang.get(i).getProduct_name(),
							listTonKhoThang.get(i).getKg_opening_balance(),
							MyMath.round(listTonKhoThang.get(i).getKg_opening_balance()
									/ listTonKhoThang.get(i).getFactor()),
							listTonKhoThang.get(i).getKg_import_quantity(),
							MyMath.round(listTonKhoThang.get(i).getKg_import_quantity()
									/ listTonKhoThang.get(i).getFactor()),
							listTonKhoThang.get(i).getKg_export_quantity(),
							MyMath.round(listTonKhoThang.get(i).getKg_export_quantity()
									/ listTonKhoThang.get(i).getFactor()),
							listTonKhoThang.get(i).getKg_closing_balance(),
							MyMath.round(listTonKhoThang.get(i).getKg_closing_balance()
									/ listTonKhoThang.get(i).getFactor()), ttt!=null?ttt.getTon():0};
					results.add(row);
					
			}
			StringBuilder title2 = new StringBuilder();
			title2.append("tonsp_" + monthSearch + "_" + yearSearch);
			ToolReport.printReportExcelRaw(results, title2.toString());
		} catch (Exception e) {
			e.printStackTrace();
			noticeError(e.getMessage());
			logger.error("ReportTonKhoThangBean.reportTonKhoSanPhamTheoThang:" + e.getMessage(), e);
		}
	}

	public void reportTonKhoSanPhamTheoThang() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			search();
			List<TonKhoThang> deleteZeros = new ArrayList<TonKhoThang>();
			for (int i = 0; i < listTonKhoThang.size(); i++) {
				if (listTonKhoThang.get(i).getUnit_opening_balance() == 0
						&& listTonKhoThang.get(i).getKg_import_quantity() == 0
						&& listTonKhoThang.get(i).getUnit_export_quantity() == 0
						&& listTonKhoThang.get(i).getKg_closing_balance() == 0) {
					deleteZeros.add(listTonKhoThang.get(i));
				}
			}
			listTonKhoThang.removeAll(deleteZeros);
			if (listTonKhoThang.size() > 0) {
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/ton_kho_thang/ton_kho_thang.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
				importParam.put(
						"logo",
						FacesContext.getCurrentInstance().getExternalContext()
								.getRealPath("/resources/gfx/lixco_logo.png"));
				StringBuilder title = new StringBuilder();
				title.append("TỒN KHO SẢN PHẨM THÁNG " + monthSearch + " NĂM " + yearSearch);
				importParam.put("title", title.toString());
				importParam.put("list_data", listTonKhoThang);

				JasperPrint jasperPrint = JasperFillManager
						.fillReport(reportPath, importParam, new JREmptyDataSource());

				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
				current.executeScript("PF('showImg').show();");
				current.executeScript("utility.showPDFK('" + ba + "','')");
			} else {
				notify.warning("Không có dữ liệu");
			}
		} catch (Exception e) {
			e.printStackTrace();
			noticeError(e.getMessage());
			logger.error("ReportTonKhoThangBean.reportTonKhoSanPhamTheoThang:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdateInventory() {
		PrimeFaces current = PrimeFaces.current();
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (lock(monthSearch, yearSearch)) {
				notify.warning("Tháng đã khóa, không cập nhật được.");
			} else {
				if (kiemtracapnhattonkho()) {
					JsonObject json = new JsonObject();
					json.addProperty("month", monthSearch);
					json.addProperty("year", yearSearch);
					json.addProperty("typep", -1);
					listTonKhoThang = new ArrayList<TonKhoThang>();
					reportService.reportTonKhoThang(JsonParserUtil.getGson().toJson(json), listTonKhoThang);
					if (listTonKhoThang.size() > 0) {
						// cập nhật tồn kho
						int code = inventoryService.saveOrUpdateInventory(monthSearch, yearSearch, getAccount()
								.getMember().getName(), listTonKhoThang);
						if (code == 0) {
							current.executeScript("swaldesigntimer('Thông báo!', 'Cập nhật tồn kho thành không!','success',2000);");
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Cập nhật thất bại!','warning',2000);");
						}
					} else {
						notify.warning("Không có dữ liệu");
					}
					getInfo();
				} else {
					error("Ngày cập nhật tồn kho lớn hơn hoặc bằng ngày cuối tháng.");
				}
			}
		} catch (Exception e) {
			logger.error("ReportTonKhoThangBean.saveOrUpdateInventory:" + e.getMessage(), e);
		}
		current.executeScript("PF('tablenc').clearFilters();");
	}

	private boolean kiemtracapnhattonkho() {
		java.time.LocalDate today = java.time.LocalDate.now();
		int dayOfMonth = today.getDayOfMonth();
		int lengthOfMonth = today.lengthOfMonth();
		if (dayOfMonth == lengthOfMonth) {
			return true;
		} else {
			int monthNow = today.getMonthValue();
			int yearNow = today.getYear();
			if (yearSearch < yearNow) {
				return true;
			} else {
				if (yearSearch == yearNow && monthSearch < monthNow) {
					return true;
				}
			}
		}
		return false;
	}

	public void reportTonKhoSanPhamTheoThangExcel() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());

		try {
			search();
			List<TonKhoThang> deleteZeros = new ArrayList<TonKhoThang>();
			for (int i = 0; i < listTonKhoThang.size(); i++) {
				if (listTonKhoThang.get(i).getUnit_opening_balance() == 0
						&& listTonKhoThang.get(i).getKg_import_quantity() == 0
						&& listTonKhoThang.get(i).getUnit_export_quantity() == 0
						&& listTonKhoThang.get(i).getKg_closing_balance() == 0) {
					deleteZeros.add(listTonKhoThang.get(i));
				}

			}
			listTonKhoThang.removeAll(deleteZeros);
			if (listTonKhoThang.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "MÃ SP", "SẢN PHẨM", "TỒN ĐẦU(Kg)", "TỒN ĐẦU(ĐVT)", "NHẬP(Kg)", "NHẬP(ĐVT)",
						"SL.XUẤT(Kg)", "SL.XUẤT(ĐVT)", "SL.CUỐI(Kg)", "SL.CUỐI(ĐVT)","NHÃN HÀNG" };
				results.add(title);

				for (int i = 0; i < listTonKhoThang.size(); i++) {
					Object[] row = {
							listTonKhoThang.get(i).getProduct_code(),
							listTonKhoThang.get(i).getProduct_name(),
							listTonKhoThang.get(i).getKg_opening_balance(),
							MyMath.round(listTonKhoThang.get(i).getKg_opening_balance()
									/ listTonKhoThang.get(i).getFactor()),
							listTonKhoThang.get(i).getKg_import_quantity(),
							MyMath.round(listTonKhoThang.get(i).getKg_import_quantity()
									/ listTonKhoThang.get(i).getFactor()),
							listTonKhoThang.get(i).getKg_export_quantity(),
							MyMath.round(listTonKhoThang.get(i).getKg_export_quantity()
									/ listTonKhoThang.get(i).getFactor()),
							listTonKhoThang.get(i).getKg_closing_balance(),
							MyMath.round(listTonKhoThang.get(i).getKg_closing_balance()
									/ listTonKhoThang.get(i).getFactor()), listTonKhoThang.get(i).getNhanhang()};
					results.add(row);
				}

				StringBuilder title2 = new StringBuilder();
				title2.append("tonsp_" + monthSearch + "_" + yearSearch);
				ToolReport.printReportExcelRaw(results, title2.toString());
			} else {
				notify.warning("Không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error("ReportTonKhoThangBean.reportTonKhoSanPhamTheoThang:" + e.getMessage(), e);
		}

	}

	public int getMonthSearch() {
		return monthSearch;
	}

	public void setMonthSearch(int monthSearch) {
		this.monthSearch = monthSearch;
	}

	public int getYearSearch() {
		return yearSearch;
	}

	public void setYearSearch(int yearSearch) {
		this.yearSearch = yearSearch;
	}

	public Product getProductSearch() {
		return productSearch;
	}

	public void setProductSearch(Product productSearch) {
		this.productSearch = productSearch;
	}

	public ProductType getProductTypeSearch() {
		return productTypeSearch;
	}

	public void setProductTypeSearch(ProductType productTypeSearch) {
		this.productTypeSearch = productTypeSearch;
	}

	public List<ProductType> getListProductType() {
		return listProductType;
	}

	public void setListProductType(List<ProductType> listProductType) {
		this.listProductType = listProductType;
	}

	public int getTypep() {
		return typep;
	}

	public void setTypep(int typep) {
		this.typep = typep;
	}

	public List<TonKhoThang> getListTonKhoThang() {
		return listTonKhoThang;
	}

	public void setListTonKhoThang(List<TonKhoThang> listTonKhoThang) {
		this.listTonKhoThang = listTonKhoThang;
	}

	public List<TonKhoThang> getListTonKhoThangFilter() {
		return listTonKhoThangFilter;
	}

	public void setListTonKhoThangFilter(List<TonKhoThang> listTonKhoThangFilter) {
		this.listTonKhoThangFilter = listTonKhoThangFilter;
	}

	public FormatHandler getFormatHandler() {
		return formatHandler;
	}

	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
	}

	public InfoInventory getInfoInventory() {
		return infoInventory;
	}

	public void setInfoInventory(InfoInventory infoInventory) {
		this.infoInventory = infoInventory;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
