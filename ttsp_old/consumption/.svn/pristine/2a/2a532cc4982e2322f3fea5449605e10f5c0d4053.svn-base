package lixco.com.bean_report;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import lixco.com.commom_ejb.MyMath;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.City;
import lixco.com.entity.Customer;
import lixco.com.entity.CustomerTypes;
import lixco.com.entity.IECategories;
import lixco.com.entity.InvenCongNo;
import lixco.com.entity.Product;
import lixco.com.entity.ProductBrand;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.ICityService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IInvenCongNoService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IReportKHService;
import lixco.com.reportInfo.BangKeHoaDon;
import lixco.com.reportInfo.ChiTietCongNo;
import lixco.com.reportInfo.CongNo;
import lixco.com.reportInfo.CongNoToiHanThanhToan;
import lixco.com.reportInfo.LoaiBaoCaoInSanPham;
import lixco.com.reportInfo.SoDuCongNo;
import lixco.com.reportInfo.TongKetTheoSanPham;
import lixco.com.reportInfo.TongKetTieuThuKhachHang;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.jboss.logging.Logger;
import org.joda.time.LocalDate;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;

import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

import com.google.gson.JsonObject;

@Named
@ViewScoped
public class InSanPhamTheoKHBean extends AbstractBean {
	private static final long serialVersionUID = -8870786800128625218L;
	@Getter
	@Setter
	private List<LoaiBaoCaoInSanPham> listLoaiBaoCaoInSanPham;
	@Getter
	@Setter
	private LoaiBaoCaoInSanPham baocaoSelect;
	@Getter
	@Setter
	private Date startDate;
	@Getter
	@Setter
	private Date endDate;
	@Getter
	@Setter
	private int month;
	@Getter
	@Setter
	private int year;
	@Getter
	@Setter
	private Product product;
	@Getter
	@Setter
	private ProductType productType;
	@Getter
	@Setter
	private ProductBrand productBrand;
	@Getter
	@Setter
	private Customer customer;
	@Getter
	@Setter
	private CustomerTypes customerTypes;
	@Getter
	@Setter
	private String customerStr;
	@Getter
	@Setter
	private IECategories ieCategories;
	@Getter
	@Setter
	private City city;
	@Getter
	@Setter
	private List<City> citieFilters;
	@Getter
	@Setter
	private boolean xuatexcel;

	@Getter
	boolean rdtungaydenngay;
	@Getter
	boolean rdthangnam;
	@Getter
	boolean bxepthutu;
	@Getter
	boolean bloaisanpham;
	@Getter
	boolean bxemsanpham;
	@Getter
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	IReportKHService reportKHService;
	@Getter
	@Setter
	private DualListModel<Customer> dualCustomers;
	@Inject
	ICityService cityService;
	@Inject
	ICustomerService customerService;

	@Override
	protected void initItem() {
		try {
			LocalDate today = new LocalDate();
			startDate = today.dayOfMonth().withMinimumValue().toDate();
			endDate = today.dayOfMonth().withMaximumValue().toDate();
			month = today.getMonthOfYear();
			year = today.getYear();
			dualCustomers = new DualListModel<>(new ArrayList<>(new ArrayList<Customer>()), new ArrayList<Customer>());
			danhsachcacbaocao();
			baocaoSelect = listLoaiBaoCaoInSanPham.get(0);
			ajaxChonBaoCao();
		} catch (Exception e) {
			logger.error("ReportInSanPhamBean.initItem:" + e.getMessage(), e);
		}

	}

	public void ajax_setDate() {
		LocalDate lc = new LocalDate();
		startDate = lc.withMonthOfYear(month).withYear(year).dayOfMonth().withMinimumValue().toDate();
		endDate = lc.withMonthOfYear(month).withYear(year).dayOfMonth().withMaximumValue().toDate();
	}

	public void danhsachcacbaocao() {
		try {
			listLoaiBaoCaoInSanPham = new ArrayList<>();
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(0, "1.1.TK xuất theo sản phẩm (1)"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(1, "1.2.TK xuất theo sản phẩm (2)"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(2, "2.TK xuất theo k/hàng-đơn giá"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(3, "3.In bảng kê hóa đơn"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(4, "4.In công nợ"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(5, "5.Chi tiết công nợ"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(6, "6.Công nợ tới hạn thanh toán"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(7, "7.In số dư công nợ (1)"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(8, "8.In số dư công nợ (2)"));

		} catch (Exception e) {
		}
	}

	public void ajaxChonBaoCao() {
		try {
			if (baocaoSelect != null) {
				int code = baocaoSelect.getId();
				switch (code) {
				case 0:
					rdtungaydenngay = true;
					rdthangnam = true;
					bxepthutu = true;
					bloaisanpham = true;
					break;
				case 1:
					rdtungaydenngay = true;
					rdthangnam = true;
					bxepthutu = true;
					bloaisanpham = true;
					break;
				case 2:
					rdtungaydenngay = true;
					rdthangnam = true;
					bxepthutu = true;
					bloaisanpham = true;
					break;
				case 3:
					rdtungaydenngay = true;
					rdthangnam = true;
					bxepthutu = true;
					bloaisanpham = true;
					break;
				case 4:
					rdtungaydenngay = true;
					rdthangnam = true;
					bxepthutu = false;
					bloaisanpham = true;
					break;
				case 5:
					rdtungaydenngay = true;
					rdthangnam = true;
					bxepthutu = true;
					bloaisanpham = true;
					break;
				case 6:
					rdtungaydenngay = true;
					rdthangnam = true;
					bxepthutu = false;
					bloaisanpham = false;
					break;
				case 7:
					rdtungaydenngay = true;
					rdthangnam = true;
					bxepthutu = false;
					bloaisanpham = false;
					break;
				case 8:
					rdtungaydenngay = true;
					rdthangnam = true;
					bxepthutu = false;
					bloaisanpham = false;
					break;
				}
			}
		} catch (Exception e) {
			logger.error("ReportInSanPhamBean.includeViewPage:" + e.getMessage(), e);
		}
	}

	public void ajaxLocKH() {
		List<Customer> customers = new ArrayList<Customer>();
		if ((city != null && city.getId() != 0) || (customerTypes != null && customerTypes.getId() != 0)) {
			customers = customerService.selectAllByCity(city == null ? 0 : city.getId(), customerTypes == null ? 0
					: customerTypes.getId());
		}
		dualCustomers.setSource(customers);
	}

	public void thuchienbaocao() {
		if (baocaoSelect != null) {
			int code = baocaoSelect.getId();
			List<Customer> customerstg = dualCustomers.getTarget();
			switch (code) {
			case 0:
				inthongkexuattheosanpham(customerstg);
				break;
			case 1:
				inthongkexuattheosanpham2(customerstg);
				break;
			case 2:
				inthongkexuattheokhachhang(customerstg);
				break;
			case 3:
				bangkehoadon(customerstg);
				break;
			case 4:
				incongno(customerstg);
				break;
			case 5:
				chitietcongno(customerstg);
				break;
			case 6:
				congnotoihanthanhtoan(customerstg);
				break;
			case 7:
				soducongno1(customerstg);
				break;
			case 8:
				soducongno2(customerstg);
				break;
			}
		}
	}

	public void inthongkexuattheosanpham(List<Customer> customers) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}
			 */
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("product_brand_id", productBrand == null ? 0 : productBrand.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			List<TongKetTheoSanPham> list = new ArrayList<>();
			reportKHService.inthongketheosanpham(JsonParserUtil.getGson().toJson(json), list, customers);
			if (list.size() > 0) {
				if (xuatexcel) {
					List<Object[]> results = new ArrayList<Object[]>();
					Object[] title = { "LoaiSP", "SP(Brand)", "SP(COM-CODE)", "SP(COM-NAME)", "SL", "DonGia", "Thue",
							"TongCong" };
					results.add(title);
					for (TongKetTheoSanPham it : list) {
						Object[] row = { it.getProduct_type_name(), it.getProduct_brand_name(),
								it.getProduct_com_code(), it.getProduct_com_name(),
								MyMath.roundCustom(it.getQuantity(), 2), MyMath.roundCustom(it.getTotal_amount(), 2),
								MyMath.roundCustom(it.getTotal_tax_amount(), 2),
								MyMath.roundCustom(it.getTotal_amount_with_vat(), 2) };
						results.add(row);
					}
					StringBuilder title2 = new StringBuilder();
					title2.append("tk_xuattheosanpham_ ");
					title2.append(ToolTimeCustomer.convertDateToString(startDate, "dd_MM_yyyy "));
					title2.append(ToolTimeCustomer.convertDateToString(endDate, "dd_MM_yyyy"));
					ToolReport.printReportExcelRawXLSX(results, title2.toString());
				} else {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/reportinsanphamKH/tkxuattheosanpham1.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);
					importParam.put(
							"logo",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/lixco_logo.png"));
					importParam.put("list_data", list);
					StringBuilder title = new StringBuilder();
					title.append("TỔNG KẾT TIÊU THỤ SẢN PHẨM ");
					if (startDate != null)
						title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
					if (endDate != null)
						title.append(" ÐẾN NGÀY " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
					importParam.put("title", title.toString());
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JREmptyDataSource());
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					// current.executeScript("utility.printPDF('" + ba + "')");
					current.executeScript("PF('showImg').show();");
					current.executeScript("utility.showPDFK('" + ba + "','')");
				}
			} else {
				notify.warning("không có dữ liệu.");
			}
		} catch (Exception e) {
			logger.error(
					"ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1:"
							+ e.getMessage(), e);
		}
	}

	public void inthongkexuattheosanpham2(List<Customer> customers) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}
			 */
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("product_brand_id", productBrand == null ? 0 : productBrand.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			List<TongKetTheoSanPham> list = new ArrayList<>();
			reportKHService.inthongketheosanpham2(JsonParserUtil.getGson().toJson(json), list, customers);
			if (list.size() > 0) {
				if (xuatexcel) {
					List<Object[]> results = new ArrayList<Object[]>();
					Object[] title = { "LoaiSP", "SP(Brand)", "SP(COM-CODE)", "SP(COM-NAME)", "SL", "DonGia", "Thue",
							"TongCong", "LoaiNX" };
					results.add(title);
					for (TongKetTheoSanPham it : list) {
						Object[] row = { it.getProduct_type_name(), it.getProduct_brand_name(),
								it.getProduct_com_code(), it.getProduct_com_name(),
								MyMath.roundCustom(it.getQuantity(), 2), MyMath.roundCustom(it.getTotal_amount(), 2),
								MyMath.round(it.getTotal_tax_amount()), MyMath.round(it.getTotal_amount_with_vat()),
								it.getIe_categories_name() };
						results.add(row);
					}
					StringBuilder title2 = new StringBuilder();
					title2.append("tk_xuattheosanpham2_ ");
					title2.append(ToolTimeCustomer.convertDateToString(startDate, "dd_MM_yyyy "));
					title2.append(ToolTimeCustomer.convertDateToString(endDate, "dd_MM_yyyy"));
					ToolReport.printReportExcelRawXLSX(results, title2.toString());
				} else {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/reportinsanphamKH/tkxuattheosanpham2.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);
					importParam.put(
							"logo",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/lixco_logo.png"));
					importParam.put("list_data", list);
					StringBuilder title = new StringBuilder();
					title.append("TỔNG KẾT TIÊU THỤ SẢN PHẨM ");
					if (startDate != null)
						title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
					if (endDate != null)
						title.append(" ÐẾN NGÀY " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
					importParam.put("title", title.toString());
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JREmptyDataSource());
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("PF('showImg').show();");
					current.executeScript("utility.showPDFK('" + ba + "','')");
				}
			} else {
				notify.warning("không có dữ liệu.");
			}
		} catch (Exception e) {
			logger.error(
					"ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1:"
							+ e.getMessage(), e);
		}
	}

	public void inthongkexuattheokhachhang(List<Customer> customers) {
		PrimeFaces current = PrimeFaces.current();
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("product_brand_id", productBrand == null ? 0 : productBrand.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			List<TongKetTieuThuKhachHang> list = new ArrayList<>();

			reportKHService.inthongketheokhachhangdvt(JsonParserUtil.getGson().toJson(json), list, customers);
			if (list.size() > 0) {
				if (xuatexcel) {
					List<Object[]> results = new ArrayList<Object[]>();
					Object[] title = { "MaKH", "KhachHang", "LoaiSP", "MaSP", "TenSP", "SL", "DonGia", "TongTien" };
					results.add(title);
					for (TongKetTieuThuKhachHang it : list) {
						Object[] row = { it.getCustomer_code(), it.getCustomer_name(), it.getProduct_type_name(),
								it.getProduct_code_temp(), it.getProduct_name_temp(), MyMath.round(it.getQuantity()),
								MyMath.round(it.getUnit_price()), MyMath.round(it.getTotal_amount()) };
						results.add(row);
					}
					StringBuilder title2 = new StringBuilder();
					title2.append("tk_xuattheokhachhang_ ");
					title2.append(ToolTimeCustomer.convertDateToString(startDate, "dd_MM_yyyy "));
					title2.append(ToolTimeCustomer.convertDateToString(endDate, "dd_MM_yyyy"));
					ToolReport.printReportExcelRawXLSX(results, title2.toString());
				} else {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/reportinsanphamKH/tkxuattheokhachhang.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);
					importParam.put(
							"logo",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/lixco_logo.png"));
					importParam.put("list_data", list);
					StringBuilder title = new StringBuilder();
					title.append("TỔNG KẾT TIÊU THỤ SẢN PHẨM ");
					if (startDate != null)
						title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
					if (endDate != null)
						title.append(" ÐẾN NGÀY " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
					importParam.put("title", title.toString());
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JREmptyDataSource());
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("PF('showImg').show();");
					current.executeScript("utility.showPDFK('" + ba + "','')");
				}
			} else {
				notify.warning("không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error("ReportInSanPhamBean.reportTongKetTieuThuSanPham:" + e.getMessage(), e);
		}
	}

	public void bangkehoadon(List<Customer> customers) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("product_brand_id", productBrand == null ? 0 : productBrand.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			List<BangKeHoaDon> list = new ArrayList<BangKeHoaDon>();
			reportKHService.bangkehoadon(JsonParserUtil.getGson().toJson(json), list, customers);
			if (list.size() > 0) {
				if (xuatexcel) {
					List<Object[]> results = new ArrayList<Object[]>();
					Object[] title = { "SoPhieu", "Ngay", "MaKH", "KhachHang", "DienGiai", "ThanhTien", "Thue",
							"TongThanhToan", "SoPO" };
					results.add(title);
					for (BangKeHoaDon it : list) {
						Object[] row = { it.getVoucher_code(),
								ToolTimeCustomer.convertDateToString(it.getInvoice_date(), "dd/MM/yyyy "),
								it.getCustomer_code(), it.getCustomer_name(), it.getIe_categories_name(),
								MyMath.round(it.getTotal_amount()), MyMath.round(it.getTotal_tax_amount()),
								MyMath.round(it.getTotal_amount_with_vat()), it.getPo() };
						results.add(row);
					}
					StringBuilder title2 = new StringBuilder();
					title2.append("tk_xuattheokhachhang_ ");
					title2.append(ToolTimeCustomer.convertDateToString(startDate, "dd_MM_yyyy "));
					title2.append(ToolTimeCustomer.convertDateToString(endDate, "dd_MM_yyyy"));
					ToolReport.printReportExcelRawXLSX(results, title2.toString());
				} else {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/reportinsanphamKH/bangkehoadon.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);
					importParam.put(
							"logo",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/lixco_logo.png"));
					importParam.put("list_data", list);
					StringBuilder title = new StringBuilder();
					title.append("BẢNG KÊ HÓA ĐƠN ");
					if (startDate != null)
						title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
					if (endDate != null)
						title.append(" ÐẾN NGÀY " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
					importParam.put("title", title.toString());
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JREmptyDataSource());
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("PF('showImg').show();");
					current.executeScript("utility.showPDFK('" + ba + "','')");
				}
			} else {
				notify.warning("Không có dữ liệu!");
			}

		} catch (Exception e) {
			logger.error("ReportBangKeHoaDonBean.reportBangKeHoaDon:" + e.getMessage(), e);
		}
	}

	public void incongno(List<Customer> customers) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {/*
			 * {from_date:
			 * '',to_date:'',product_type_id:0,customer_id:0,ie_categories_id:0}
			 */
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("product_brand_id", productBrand == null ? 0 : productBrand.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			List<CongNo> list = new ArrayList<>();
			reportKHService.congno(JsonParserUtil.getGson().toJson(json), list, customers);
			if (list.size() > 0) {
				if (xuatexcel) {
					List<Object[]> results = new ArrayList<Object[]>();
					Object[] title = { "MaKH", "TenKH", "ThanhPho", "SoTien" };
					results.add(title);
					for (CongNo it : list) {
						Object[] row = { it.getCustomer_code(), it.getCustomer_name(), it.getCity_name(),
								MyMath.round(it.getTotal_amount()) };
						results.add(row);
					}
					StringBuilder title2 = new StringBuilder();
					title2.append("tk_congno_ ");
					title2.append(ToolTimeCustomer.convertDateToString(startDate, "dd_MM_yyyy "));
					title2.append(ToolTimeCustomer.convertDateToString(endDate, "dd_MM_yyyy"));
					ToolReport.printReportExcelRawXLSX(results, title2.toString());
				} else {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/reportinsanphamKH/congno.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);
					importParam.put(
							"logo",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/lixco_logo.png"));
					importParam.put("list_data", list);
					StringBuilder title = new StringBuilder();
					title.append("TỔNG KẾT CÔNG NỢ ");
					if (startDate != null)
						title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
					if (endDate != null)
						title.append(" ÐẾN NGÀY " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
					importParam.put("title", title.toString());
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JREmptyDataSource());
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("PF('showImg').show();");
					current.executeScript("utility.showPDFK('" + ba + "','')");
				}
			} else {
				notify.warning("Không có dữ liệu!");
			}

		} catch (Exception e) {
			logger.error("ReportCongNoBean.reportCongNo:" + e.getMessage(), e);
		}
	}

	public void chitietcongno(List<Customer> customers) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_type_id:0,customer_id:0,ie_categories_id:0}
			 */
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("now", ToolTimeCustomer.convertDateToString(new Date(), "dd/MM/yyyy"));
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("product_brand_id", productBrand == null ? 0 : productBrand.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			List<ChiTietCongNo> list = new ArrayList<ChiTietCongNo>();
			reportKHService.chitietcongno(JsonParserUtil.getGson().toJson(json), list, customers);
			if (list.size() > 0) {
				if (xuatexcel) {
					List<Object[]> results = new ArrayList<Object[]>();
					Object[] title = { "MaKH", "KhachHang", "LoaiNX", "Ngay", "SoCT", "MaSP", "SanPham", "SoLuong",
							"DonGia", "ThanhTien", "SoXe" };
					results.add(title);
					for (ChiTietCongNo it : list) {
						Object[] row = { it.getCustomer_code(), it.getCustomer_name(), it.getIe_categories_name(),
								ToolTimeCustomer.convertDateToString(it.getInvoice_date(), "dd/MM/yyyy"),
								it.getVoucher_code(), it.getProduct_code(), it.getProduct_name(),
								MyMath.roundCustom(it.getQuantity(), 2), MyMath.round(it.getUnit_price()),
								MyMath.round(it.getTotal_amount()), it.getLicense_plate() };
						results.add(row);
					}
					StringBuilder title2 = new StringBuilder();
					title2.append("tk_chitietcongno_ ");
					title2.append(ToolTimeCustomer.convertDateToString(startDate, "dd_MM_yyyy "));
					title2.append(ToolTimeCustomer.convertDateToString(endDate, "dd_MM_yyyy"));
					ToolReport.printReportExcelRawXLSX(results, title2.toString());
				} else {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/reportinsanphamKH/chitietcongno.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);
					importParam.put(
							"logo",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/lixco_logo.png"));
					StringBuilder title = new StringBuilder();
					title.append("CHI TIẾT CÔNG NỢ ");
					if (startDate != null)
						title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
					if (endDate != null)
						title.append(" ÐẾN NGÀY " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
					importParam.put("title", title.toString());
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JRBeanCollectionDataSource(list));
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("PF('showImg').show();");
					current.executeScript("utility.showPDFK('" + ba + "','')");
				}
			} else {
				notify.warning("Không có dữ liệu!");
			}
		} catch (Exception e) {
			logger.error("ReportChiTietCongNoBean.reportChiTietCongNo:" + e.getMessage(), e);
		}
	}

	public void congnotoihanthanhtoan(List<Customer> customers) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "yyyy-MM-dd"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "yyyy-MM-dd"));
			json.addProperty("now", ToolTimeCustomer.convertDateToString(new Date(), "yyyy-MM-dd"));
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("product_brand_id", productBrand == null ? 0 : productBrand.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			List<CongNoToiHanThanhToan> list = new ArrayList<CongNoToiHanThanhToan>();
			reportKHService.congnotoihanthanhtoan(JsonParserUtil.getGson().toJson(json), list, customers);
			if (list.size() > 0) {
				if (xuatexcel) {
					List<Object[]> results = new ArrayList<Object[]>();
					Object[] title = { "MaKH", "KhachHang", "MaLoaiNX", "LoaiNX", "SoDH", "SoCT", "Ngay", "ChuaToiHan",
							"ToiHan", "QuaHan", "SoPO" };
					results.add(title);
					for (CongNoToiHanThanhToan it : list) {
						Object[] row = { it.getCustomer_code(), it.getCustomer_name(), it.getMaloainhapxuat(),
								it.getLoainhapxuat(), it.getVoucher_code_dh(), it.getVoucher_code_iv(),
								ToolTimeCustomer.convertDateToString(it.getInvoice_date(), "dd/MM/yyyy"),
								MyMath.round(it.getChuatoihan()), MyMath.round(it.getToihan()),
								MyMath.round(it.getQuahan()), it.getPo() };
						results.add(row);
					}
					StringBuilder title2 = new StringBuilder();
					title2.append("tk_congnotoihan_ ");
					title2.append(ToolTimeCustomer.convertDateToString(startDate, "dd_MM_yyyy "));
					title2.append(ToolTimeCustomer.convertDateToString(endDate, "dd_MM_yyyy"));
					ToolReport.printReportExcelRawXLSX(results, title2.toString());
				} else {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/reportinsanphamKH/congnotoihanthanhtoan.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);
					importParam.put(
							"logo",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/lixco_logo.png"));
					StringBuilder title = new StringBuilder();
					title.append("CÔNG NỢ KHÁCH HÀNG CHƯA THANH TOÁN ");
					if (startDate != null)
						title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
					if (endDate != null)
						title.append(" ÐẾN NGÀY " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
					importParam.put("title", title.toString());
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JRBeanCollectionDataSource(list));
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("PF('showImg').show();");
					current.executeScript("utility.showPDFK('" + ba + "','')");
				}
			} else {
				notify.warning("Không có dữ liệu!");
			}
		} catch (Exception e) {
			logger.error("ReportChiTietCongNoBean.reportChiTietCongNo:" + e.getMessage(), e);
		}
	}

	@Inject
	IInvenCongNoService invenCongNoService;

	public void soducongno1(List<Customer> customers) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("product_brand_id", productBrand == null ? 0 : productBrand.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());

			// Cap nhat cong no
			int thang = 0, nam = 0;
			if (month == 1) {
				thang = 12;
				nam = year - 1;
			} else {
				thang = month - 1;
				nam = year;
			}
			List<SoDuCongNo> soDuCongNos = new ArrayList<SoDuCongNo>();
			for (int m = 0; m < customers.size(); m++) {
				List<SoDuCongNo> list = reportKHService.soducongno(JsonParserUtil.getGson().toJson(json), customers
						.get(m).getId());

				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setTotalNo(MyMath.round(list.get(i).getTotalNo()));
						list.get(i).setTotalCo(MyMath.round(list.get(i).getTotalCo()));
					}

					Map<String, List<SoDuCongNo>> datagroups1 = list.stream().collect(
							Collectors.groupingBy(p -> p.getCustomer_code(), Collectors.toList()));
					List<InvenCongNo> invenCongNos = new ArrayList<InvenCongNo>();
					for (String key : datagroups1.keySet()) {
						double tondauky = 0, no = 0, co = 0;
						List<SoDuCongNo> invs = datagroups1.get(key);
						for (int i = 0; i < invs.size(); i++) {
							no += invs.get(i).getTotalNo();
							co += invs.get(i).getTotalCo();
						}
						Customer cus = customerService.selectByCode(key);
						if (cus != null) {
							tondauky = invenCongNoService.getInvenFinal(cus.getId(), thang, nam);
							InvenCongNo invenCongNo = new InvenCongNo();
							invenCongNo.setCreated_by(getCreateByUser());
							invenCongNo.setCreated_date(new Date());
							invenCongNo.setCustomer(cus);
							invenCongNo.setMonth(month);
							invenCongNo.setYear(year);
							invenCongNo.setInvenFirst(MyMath.round(tondauky));
							invenCongNo.setInvenNo(MyMath.round(no));
							invenCongNo.setInvenCo(MyMath.round(co));
							invenCongNo.setInvenFinal(invenCongNo.getInvenFirst() + invenCongNo.getInvenNo()
									- invenCongNo.getInvenCo());
							invenCongNos.add(invenCongNo);
							for (int i = 0; i < list.size(); i++) {
								if (list.get(i).getCustomer_code().equals(key)) {
									list.get(i).setTotalFirst(invenCongNo.getInvenFirst());
									list.get(i).setTotalFinal(
											MyMath.round(list.get(i).getTotalFirst() + list.get(i).getTotalNo()
													- list.get(i).getTotalCo()));
								}
							}
						}

					}
					if (invenCongNos.size() != 0) {
						invenCongNoService.saveOrUpdateInvenCongNo(invenCongNos);
					}
					soDuCongNos.addAll(list);
				} else {
					Customer cus = customerService.findById(customers.get(m).getId());
					double tondauky = invenCongNoService.getInvenFinal(cus.getId(), thang, nam);
					InvenCongNo invenCongNo = new InvenCongNo();
					invenCongNo.setCreated_by(getCreateByUser());
					invenCongNo.setCreated_date(new Date());
					invenCongNo.setCustomer(cus);
					invenCongNo.setMonth(month);
					invenCongNo.setYear(year);
					invenCongNo.setInvenFirst(MyMath.round(tondauky));
					invenCongNo.setInvenNo(0);
					invenCongNo.setInvenCo(0);
					invenCongNo.setInvenFinal(invenCongNo.getInvenFirst() + invenCongNo.getInvenNo()
							- invenCongNo.getInvenCo());
					invenCongNoService.saveOrUpdateInvenCongNo(invenCongNo);
				}
			}
			if (soDuCongNos.size() == 0) {
				noticeError("Không có dữ liệu báo cáo.");
			} else {
				Collections.sort(soDuCongNos, new Comparator<SoDuCongNo>() {
					public int compare(SoDuCongNo s1, SoDuCongNo s2) {
						String s1name = s1.getCustomer_name();
						String s2name = s2.getCustomer_name();
						int value = s1name.compareTo(s2name);
						if (value == 0) {
							Date s1Date = s1.getDate();
							Date s2Date = s2.getDate();
							value = s1Date.compareTo(s2Date);
						}
						return value;
					}
				});

				// xuat excel/report
				if (xuatexcel) {
					List<Object[]> results = new ArrayList<Object[]>();
					Object[] title = { "MaKH", "KhachHang", "Ngay", "NoDK", "PSNo(HD)", "PSNo(ST)", "PSCo(UNC)",
							"PSCo(ST)", "NoCK", "GhiChu" };
					results.add(title);
					for (SoDuCongNo it : soDuCongNos) {
						Object[] row = { it.getCustomer_code(), it.getCustomer_name(),
								ToolTimeCustomer.convertDateToString(it.getDate(), "dd/MM/yyyy"), it.getTotalFirst(),
								it.getVoucher_code(), MyMath.roundCustom(it.getTotalNo(), 2), it.getUnc_code(),
								MyMath.roundCustom(it.getTotalCo(), 2), MyMath.roundCustom(it.getTotalFinal(), 2),
								it.getNote() };
						results.add(row);
					}
					StringBuilder title2 = new StringBuilder();
					title2.append("chitietcongno_ ");
					title2.append(ToolTimeCustomer.convertDateToString(startDate, "dd_MM_yyyy "));
					title2.append(ToolTimeCustomer.convertDateToString(endDate, "dd_MM_yyyy"));
					ToolReport.printReportExcelRawXLSX(results, title2.toString());
				} else {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/reportinsanphamKH/soducongno.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);
					importParam.put(
							"logo",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/lixco_logo.png"));
					StringBuilder title = new StringBuilder();
					title.append("CHI TIẾT CÔNG NỢ ");
					if (startDate != null)
						title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
					if (endDate != null)
						title.append(" ÐẾN NGÀY " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
					importParam.put("title", title.toString());
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JRBeanCollectionDataSource(soDuCongNos));
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("PF('showImg').show();");
					current.executeScript("utility.showPDFK('" + ba + "','')");
				}
			}
		} catch (Exception e) {
			logger.error("ReportChiTietCongNoBean.reportChiTietCongNo:" + e.getMessage(), e);
		}
	}

	public void soducongno2(List<Customer> customers) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("product_brand_id", productBrand == null ? 0 : productBrand.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			List<SoDuCongNo> list = new ArrayList<SoDuCongNo>();
			reportKHService.soducongno2(JsonParserUtil.getGson().toJson(json), list, customers);

			if (list.size() > 0) {
				int thang = 0, nam = 0;
				if (month == 1) {
					thang = 12;
					nam = year - 1;
				} else {
					thang = month - 1;
					nam = year;
				}
				List<SoDuCongNo> removes = new ArrayList<SoDuCongNo>();
				for (int i = 0; i < list.size(); i++) {
					Customer cus = customerService.selectByCode(list.get(i).getCustomer_code());
					if (cus != null) {
						double tondauky = invenCongNoService.getInvenFinal(cus.getId(), thang, nam);
						list.get(i).setTotalFirst(MyMath.round(tondauky));
					}
					list.get(i).setTotalNo(MyMath.round(list.get(i).getTotalNo()));
					list.get(i).setTotalCo(MyMath.round(list.get(i).getTotalCo()));
					list.get(i).setTotalFinal(
							MyMath.round(list.get(i).getTotalFirst() + list.get(i).getTotalNo()
									- list.get(i).getTotalCo()));

					if (list.get(i).getTotalFirst() == 0 && list.get(i).getTotalNo() == 0
							&& list.get(i).getTotalCo() == 0)
						removes.add(list.get(i));
				}
				if (removes.size() != 0)
					list.removeAll(removes);

				// xuat excel/report
				if (xuatexcel) {
					List<Object[]> results = new ArrayList<Object[]>();
					Object[] title = { "MaKH", "KhachHang", "TonDauKy", "PSNo(ST)", "PSCo(UNC)", "TonCuoiKy" };
					results.add(title);
					for (SoDuCongNo it : list) {
						Object[] row = { it.getCustomer_code(), it.getCustomer_name(), it.getTotalFirst(),
								it.getTotalNo(), it.getTotalCo(), it.getTotalFinal() };
						results.add(row);
					}
					StringBuilder title2 = new StringBuilder();
					title2.append("tonghopcongno_ ");
					title2.append(ToolTimeCustomer.convertDateToString(startDate, "dd_MM_yyyy "));
					title2.append(ToolTimeCustomer.convertDateToString(endDate, "dd_MM_yyyy"));
					ToolReport.printReportExcelRawXLSX(results, title2.toString());
				} else {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/reportinsanphamKH/soducongnotonghop.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);
					importParam.put(
							"logo",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/lixco_logo.png"));
					StringBuilder title = new StringBuilder();
					title.append("CÔNG NỢ KHÁCH HÀNG ");
					if (startDate != null)
						title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
					if (endDate != null)
						title.append(" ÐẾN NGÀY " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
					importParam.put("title", title.toString());
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JRBeanCollectionDataSource(list));
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("PF('showImg').show();");
					current.executeScript("utility.showPDFK('" + ba + "','')");
				}
			} else {
				notify.warning("Không có dữ liệu!");
			}
		} catch (Exception e) {
			logger.error("ReportChiTietCongNoBean.reportChiTietCongNo:" + e.getMessage(), e);
		}
	}

	@Inject
	private Logger logger;

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
