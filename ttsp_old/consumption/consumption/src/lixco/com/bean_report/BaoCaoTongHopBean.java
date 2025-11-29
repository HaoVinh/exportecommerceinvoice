package lixco.com.bean_report;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Area;
import lixco.com.entity.City;
import lixco.com.entity.Contract;
import lixco.com.entity.Customer;
import lixco.com.entity.IECategories;
import lixco.com.entity.Product;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.IAreaService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.LoaiBaoCaoInSanPham;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai1;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai2;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai3;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel;
import lombok.Getter;
import lombok.Setter;
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
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

import com.google.gson.JsonObject;

@Named
@ViewScoped
public class BaoCaoTongHopBean extends AbstractBean {
	private static final long serialVersionUID = -8870786800128625218L;
	@Inject
	private Logger logger;

	@Override
	protected Logger getLogger() {
		return logger;
	}

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
	private int startMonth;
	@Getter
	@Setter
	private int endMonth;
	@Getter
	@Setter
	private int yearSE;
	@Getter
	@Setter
	private Product product;
	@Getter
	@Setter
	private List<ProductType> listProductType;
	@Getter
	@Setter
	private ProductType productType;
	@Getter
	@Setter
	private Customer customer;
	@Getter
	@Setter
	private Contract contract;
	@Getter
	@Setter
	private List<IECategories> listIeCategories;
	@Getter
	@Setter
	private IECategories ieCategories;
	@Getter
	@Setter
	private List<Area> listArea;
	@Getter
	@Setter
	private Area area;
	@Getter
	@Setter
	private City city;
	@Getter
	@Setter
	private String xepthutu;
	@Getter
	@Setter
	private int xemloaisp;// -1:tat ca; 0: xuat khau; 1:noi dia
	@Getter
	@Setter
	private boolean capnhattonkho;

	@Getter
	boolean rdtungaydenngay;
	@Getter
	boolean rdthangnam;
	@Getter
	boolean rdthangthangnam;
	@Getter
	boolean bsanpham;
	@Getter
	boolean bloaisanpham;
	@Getter
	boolean bkhachhang;
	@Getter
	boolean bhopdong;
	@Getter
	boolean bloainhapxuat;
	@Getter
	boolean bkhuvuc;
	@Getter
	boolean bthanhpho;
	@Getter
	boolean bxepthutu;
	@Getter
	boolean bxemsanpham;
	@Getter
	boolean bcapnhatton;
	@Getter
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private IIECategoriesService ieCategoriesService;
	@Inject
	private IAreaService areaService;
	@Inject
	IReportService reportService;

	@Override
	protected void initItem() {
		try {

			LocalDate today = new LocalDate();
			startDate = today.dayOfMonth().withMinimumValue().toDate();
			endDate = today.dayOfMonth().withMaximumValue().toDate();
			month = today.getMonthOfYear();
			year = today.getYear();

			startMonth = 1;
			endMonth = today.getMonthOfYear();
			year = today.getYear();

			

			listArea = new ArrayList<>();
			areaService.selectAll(listArea);

			listIeCategories = new ArrayList<>();
			ieCategoriesService.selectAll(listIeCategories);

			listProductType = new ArrayList<>();
			productTypeService.selectAll(listProductType);

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
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(1, "1.TK xuất khẩu theo sản phẩm"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(2, "2.TK xuất theo k/hàng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(3, "3.TK xuất theo thành phố"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(4, "4.TK xuất theo Brand Name"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(5, "5.Bảng kê hóa đơn"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(6, "6.Công nợ"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(7, "7.Chi tiết công nợ"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(8, "8.Thống kê theo tháng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(9, "9.Tồn kho tháng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(10, "10.Tồn kho sản phẩm"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(11, "11.Thẻ kho sản phẩm"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(12, "12.Doanh thu KH theo tháng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(13, "13.Bảng kê chi tiết hóa đơn"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(14, "14.Nhập xuất ngày"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(15, "15.Bảng kê lượng vận chuyển"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(16, "16.Bảng kê chi phí vc xe lix thuê", true));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(17, "17.Bảng kê chi tiết CP thuê xe v/c", true));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(18, "18.Xem doanh thu và sản lượng", true));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(19, "19.Bảng kê chi phí xe khách hàng", true));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(20, "20.Bảng kê đơn hàng", true));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(21, "21.Số liệu họp giao ban"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(22, "22.In chi phí vận chuyển", true));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(23, "23.Báo cáo lever"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(24, "24.Lấy số liệu BC tổng hợp"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(25, "25.TK sp chưa giao hàng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(26, "26.In lũy kế nhập xuất"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(27, "27.TK khách hàng xuất theo ngày", true));
		} catch (Exception e) {
		}
	}

	public void ajaxChonBaoCao() {
		try {
			if (baocaoSelect != null) {
				int code = baocaoSelect.getId();
				switch (code) {
				case 1:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = false;
					bloaisanpham = false;
					bkhachhang = false;
					bhopdong = false;
					bloainhapxuat = false;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 2:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = false;
					bloaisanpham = false;
					bkhachhang = false;
					bhopdong = false;
					bloainhapxuat = false;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = false;
					bxemsanpham = false;
					bcapnhatton = true;
					break;
				case 3:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = false;
					bloaisanpham = false;
					bkhachhang = false;
					bhopdong = false;
					bloainhapxuat = false;
					bkhuvuc = false;
					bthanhpho = false;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 4:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = false;
					bloaisanpham = false;
					bkhachhang = false;
					bhopdong = false;
					bloainhapxuat = false;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 5:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = false;
					bloaisanpham = true;
					bkhachhang = false;
					bhopdong = false;
					bloainhapxuat = false;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 6:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = true;
					bloaisanpham = true;
					bkhachhang = true;
					bhopdong = true;
					bloainhapxuat = false;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = false;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 7:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = true;
					bloaisanpham = false;
					bkhachhang = false;
					bhopdong = true;
					bloainhapxuat = false;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 8:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = false;
					bloaisanpham = true;
					bkhachhang = false;
					bhopdong = true;
					bloainhapxuat = false;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 9:
					rdtungaydenngay = false;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = true;
					bloaisanpham = true;
					bkhachhang = true;
					bhopdong = true;
					bloainhapxuat = true;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = false;
					break;
				case 10:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = false;
					bloaisanpham = false;
					bkhachhang = true;
					bhopdong = true;
					bloainhapxuat = true;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 11:
					rdtungaydenngay = false;
					rdthangnam = false;
					rdthangthangnam = true;

					bsanpham = false;
					bloaisanpham = false;
					bkhachhang = true;
					bhopdong = true;
					bloainhapxuat = true;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 12:
					rdtungaydenngay = false;
					rdthangnam = false;
					rdthangthangnam = true;

					bsanpham = false;
					bloaisanpham = false;
					bkhachhang = false;
					bhopdong = true;
					bloainhapxuat = false;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = false;
					bcapnhatton = true;
					break;
				case 13:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = false;
					bloaisanpham = true;
					bkhachhang = false;
					bhopdong = false;
					bloainhapxuat = false;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 14:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = false;
					bloaisanpham = false;
					bkhachhang = false;
					bhopdong = false;
					bloainhapxuat = false;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 15:
					rdtungaydenngay = false;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = false;
					bloaisanpham = true;
					bkhachhang = false;
					bhopdong = true;
					bloainhapxuat = true;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 21:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = true;
					bloaisanpham = true;
					bkhachhang = true;
					bhopdong = true;
					bloainhapxuat = true;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 23:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = false;
					bloaisanpham = true;
					bkhachhang = true;
					bhopdong = true;
					bloainhapxuat = true;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 24:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = true;
					bloaisanpham = true;
					bkhachhang = true;
					bhopdong = true;
					bloainhapxuat = true;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 25:
					rdtungaydenngay = true;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = false;
					bloaisanpham = true;
					bkhachhang = false;
					bhopdong = true;
					bloainhapxuat = false;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = true;
					bcapnhatton = true;
					break;
				case 26:
					rdtungaydenngay = false;
					rdthangnam = true;
					rdthangthangnam = false;

					bsanpham = false;
					bloaisanpham = true;
					bkhachhang = false;
					bhopdong = true;
					bloainhapxuat = false;
					bkhuvuc = true;
					bthanhpho = true;
					bxepthutu = true;
					bxemsanpham = false;
					bcapnhatton = true;
					break;
				}
			}
		} catch (Exception e) {
			logger.error("ReportInSanPhamBean.includeViewPage:" + e.getMessage(), e);
		}
	}

	public void thuchienbaocao() {
		if (baocaoSelect != null) {
			int code = baocaoSelect.getId();
			switch (code) {
			case 1:
				showDialog("dl1");
				break;
			}
		}
	}

	public void reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,xemloaisp:0}
			 */
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("product_id", product != null ? product.getId() : 0);
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("customer_id", customer == null ? 0 : customer.getId());
			json.addProperty("contract_id", contract == null ? 0 : contract.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			json.addProperty("xemloaisp", xemloaisp);
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai1> list = new ArrayList<>();
			reportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1(JsonParserUtil.getGson().toJson(json), list);
			if (list.size() > 0) {
				String reportPath = FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.getRealPath(
								"/resources/reports/reportinsanpham/report_tk_xuat_theo_san_pham/tongketxuattheosanphamtongkettieuthuloai1.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
				importParam.put(
						"logo",
						FacesContext.getCurrentInstance().getExternalContext()
								.getRealPath("/resources/gfx/lixco_logo.png"));
				importParam.put("list_data", list);
				StringBuilder title = new StringBuilder();
				title.append("TỔNG KẾT TT SP ");
				if (startDate != null)
					title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
				if (endDate != null)
					title.append(" ĐẾN NGÀY " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
				importParam.put("title", title.toString());
				JasperPrint jasperPrint = JasperFillManager
						.fillReport(reportPath, importParam, new JREmptyDataSource());
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
				current.executeScript("PF('showImg').show();");
				current.executeScript("utility.showPDFK('" + ba + "','')");
			} else {
				notify.warning("không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error(
					"ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1:"
							+ e.getMessage(), e);
		}
	}

	public void reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,xemloaisp:0}
			 */
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("product_id", product != null ? product.getId() : 0);
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("customer_id", customer == null ? 0 : customer.getId());
			json.addProperty("contract_id", contract == null ? 0 : contract.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			json.addProperty("xemloaisp", xemloaisp);
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai2> list = new ArrayList<>();
			reportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2(JsonParserUtil.getGson().toJson(json), list);
			if (list.size() > 0) {
				String reportPath = FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.getRealPath(
								"/resources/reports/reportinsanpham/report_tk_xuat_theo_san_pham/tongketxuattheosanphamtongkettieuthuloai2.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
				importParam.put(
						"logo",
						FacesContext.getCurrentInstance().getExternalContext()
								.getRealPath("/resources/gfx/lixco_logo.png"));
				importParam.put("list_data", list);
				StringBuilder title = new StringBuilder();
				title.append("TỔNG KẾT TT SP ");
				if (startDate != null)
					title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
				if (endDate != null)
					title.append(" ĐẾN NGÀY " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
				importParam.put("title", title.toString());
				JasperPrint jasperPrint = JasperFillManager
						.fillReport(reportPath, importParam, new JREmptyDataSource());
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
				current.executeScript("PF('showImg').show();");
				current.executeScript("utility.showPDFK('" + ba + "','')");
			} else {
				notify.warning("không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error(
					"ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2:"
							+ e.getMessage(), e);
		}
	}

	public void reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,xemloaisp:0}
			 */
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("product_id", product != null ? product.getId() : 0);
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("customer_id", customer == null ? 0 : customer.getId());
			json.addProperty("contract_id", contract == null ? 0 : contract.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			json.addProperty("xemloaisp", xemloaisp);
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai3> list = new ArrayList<>();
			reportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3(JsonParserUtil.getGson().toJson(json), list);
			if (list.size() > 0) {
				String reportPath = FacesContext
						.getCurrentInstance()
						.getExternalContext()
						.getRealPath(
								"/resources/reports/reportinsanpham/report_tk_xuat_theo_san_pham/tongketxuattheosanphamtongkettieuthuloai3.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
				importParam.put(
						"logo",
						FacesContext.getCurrentInstance().getExternalContext()
								.getRealPath("/resources/gfx/lixco_logo.png"));
				importParam.put("list_data", list);
				StringBuilder title = new StringBuilder();
				title.append("TỔNG KẾT TT SP ");
				if (startDate != null)
					title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
				if (endDate != null)
					title.append(" ĐẾN NGÀY " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
				importParam.put("title", title.toString());
				JasperPrint jasperPrint = JasperFillManager
						.fillReport(reportPath, importParam, new JREmptyDataSource());
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
				current.executeScript("PF('showImg').show();");
				current.executeScript("utility.showPDFK('" + ba + "','')");
			} else {
				notify.warning("không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error(
					"ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3:"
							+ e.getMessage(), e);
		}
	}

	public void reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,xemloaisp:0}
			 */
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("product_id", product != null ? product.getId() : 0);
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("customer_id", customer == null ? 0 : customer.getId());
			json.addProperty("contract_id", contract == null ? 0 : contract.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			json.addProperty("xemloaisp", xemloaisp);
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel> list = new ArrayList<>();
			reportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel(JsonParserUtil.getGson().toJson(json),
					list);
			if (list.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "MÃ SP", "TÊN SẢN PHẨM", "HSQD", "QUI CÁCH", "SỐ THÙNG", "SỐ TIỀN", "THUẾ TGGT" };
				results.add(title);
				for (TongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel it : list) {
					Object[] row = { it.getProduct_code(), it.getProduct_name(), it.getSpecification(), it.getFactor(),
							it.getSpecification(), it.getBox_quantity(), it.getTotal_amount(), it.getTotal_tax_amount() };
					results.add(row);
				}
				StringBuilder title2 = new StringBuilder();
				title2.append("thsp_hopdong_");
				if (startDate != null)
					title2.append("tu_ngay " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
				if (endDate != null)
					title2.append("_den_ngay " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
				ToolReport.printReportExcelRaw(results, title2.toString());
			} else {
				notify.warning("không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error(
					"ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel:"
							+ e.getMessage(), e);
		}
	}

	public void reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,xemloaisp:0}
			 */
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("product_id", product != null ? product.getId() : 0);
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("customer_id", customer == null ? 0 : customer.getId());
			json.addProperty("contract_id", contract == null ? 0 : contract.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			json.addProperty("xemloaisp", xemloaisp);
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel> list = new ArrayList<>();
			reportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel(JsonParserUtil.getGson().toJson(json),
					list);
			if (list.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "MÃ SP COM", "TÊN SẢN PHẨM COM", "MÃ NX", "LOẠI NHẬP XUẤT", "SỐ LƯỢNG KG",
						"SỐ LƯỢNG DVT", "THUẾ TGGT" };
				results.add(title);
				for (TongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel it : list) {
					Object[] row = { it.getPcom_code(), it.getPcom_name(), it.getIe_categories_code(),
							it.getIe_categories_name(), it.getQuantity_kg(), it.getQuantity(), it.getTotal_amount(),
							it.getTotal_tax_amount() };
					results.add(row);
				}
				StringBuilder title2 = new StringBuilder();
				title2.append("thsp_com_");
				if (startDate != null)
					title2.append("tu_ngay " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
				if (endDate != null)
					title2.append("_den_ngay " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
				ToolReport.printReportExcelRaw(results, title2.toString());
			} else {
				notify.warning("không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error(
					"ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel:"
							+ e.getMessage(), e);
		}
	}

	public void reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,xemloaisp:0}
			 */
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
			json.addProperty("product_id", product != null ? product.getId() : 0);
			json.addProperty("product_type_id", productType == null ? 0 : productType.getId());
			json.addProperty("customer_id", customer == null ? 0 : customer.getId());
			json.addProperty("contract_id", contract == null ? 0 : contract.getId());
			json.addProperty("ie_categories_id", ieCategories == null ? 0 : ieCategories.getId());
			json.addProperty("xemloaisp", xemloaisp);
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel> list = new ArrayList<>();
			reportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel(JsonParserUtil.getGson().toJson(json),
					list);
			if (list.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "MÃ NX", "LOẠI NHẬP XUẤT", "MÃ SP", "SẢN PHẨM", "LOẠI SẢN PHẨM", "SỐ LƯỢNG",
						"SỐ LƯỢNG ĐVT", "SỐ TIỀN", "THUẾ TGGT" };
				results.add(title);
				for (TongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel it : list) {
					Object[] row = { it.getIe_categories_code(), it.getIe_categories_name(), it.getProduct_code(),
							it.getProduct_name(), it.getProduct_type_name(), it.getQuantity_kg(), it.getQuantity(),
							it.getTotal_amount(), it.getTotal_tax_amount() };
					results.add(row);
				}
				StringBuilder title2 = new StringBuilder();
				title2.append("bang_ke_chi_tiet_xuat_san_pham_");
				if (startDate != null)
					title2.append("tu_ngay " + ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
				if (endDate != null)
					title2.append("_den_ngay " + ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
				ToolReport.printReportExcelRaw(results, title2.toString());
			} else {
				notify.warning("không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error(
					"ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel:"
							+ e.getMessage(), e);
		}
	}

}
