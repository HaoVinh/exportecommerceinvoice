package lixco.com.bean;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import lixco.com.entity.Carrier;
import lixco.com.entity.Customer;
import lixco.com.entity.IECategories;
import lixco.com.entity.KeHoachGiaoHang;
import lixco.com.entity.OrderDetail;
import lixco.com.entity.PromotionOrderDetail;
import lixco.com.interfaces.ICarrierService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IOrderDetailService;
import lixco.com.interfaces.IPromotionOrderDetailService;
import lixco.com.service.IECategoriesService;
import lixco.com.service.KeHoachGiaoHangService;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.ParamReportDetail;
import trong.lixco.com.service.AccountAPIService;
import trong.lixco.com.service.ParamReportDetailService;
import trong.lixco.com.util.KeHoachGiaoHangProccess;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.ToolReport;

@Named
@ViewScoped
public class KeHoachGiaoHangBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Getter
	@Setter
	Date ngaygh;
	@Getter
	@Setter
	Date ngaySearch;
	@Inject
	AccountAPIService accountAPIService;
	@Inject
	KeHoachGiaoHangService keHoachGiaoHangService;
	@Getter
	@Setter
	List<KeHoachGiaoHang> keHoachGiaoHangs;

	@Inject
	IOrderDetailService orderDetailService;
	@Getter
	@Setter
	boolean bd = true;
	@Inject
	ICustomerService customerService;
	@Inject
	ICarrierService carrierService;
	@Inject
	IIECategoriesService ieCategoriesService;

	public void capnhatdulieu() {
		keHoachGiaoHangs = orderDetailService.findBy(true, true, ngaygh);
		for (int i = 0; i < keHoachGiaoHangs.size(); i++) {
			if (keHoachGiaoHangs.get(i).getKhoiluongvc() == 0) {
				keHoachGiaoHangs.get(i).setKhoiluongvc(
						orderDetailService.khoiluongtongdonhang(keHoachGiaoHangs.get(i).getIdorder()));
			}
		}
		if (bd) {
			List<KeHoachGiaoHang> khghs = KeHoachGiaoHangProccess.dasxtheonhomhang("BD", accountAPIService, ngaygh);
			if (!khghs.isEmpty()) {
				for (int i = 0; i < khghs.size(); i++) {
					String makh = khghs.get(i).getCustomer() != null ? khghs.get(i).getCustomer().getCustomer_code()
							: null;
					String mavc = khghs.get(i).getCarrier() != null ? khghs.get(i).getCarrier().getCarrier_code()
							: null;
					String maxn = khghs.get(i).getIeCategories() != null ? khghs.get(i).getIeCategories().getCode()
							: null;
					if (makh != null) {
						Customer cus = customerService.selectByCode(makh);
						if (cus == null) {
							System.out.println("Không có khách hàng (" + makh + ") tại HCM.");
						}
						khghs.get(i).setCustomer(cus);

						Carrier carr = carrierService.selectByCode(mavc);
						if (carr == null) {
							System.out.println("Không có nhà vận chuyển (" + carr + ") tại HCM");
						}
						khghs.get(i).setCarrier(carr);

					}
					if (mavc != null) {
						Carrier carr = carrierService.selectByCode(mavc);
						if (carr == null) {
							System.out.println("Không có nhà vận chuyển (" + carr + ") tại HCM");
						}
						khghs.get(i).setCarrier(carr);

					}
				}
				keHoachGiaoHangs.addAll(khghs);
			}
		}
		String[] kenhGT = { "X", "A", "M", "P" };
		String[] kenhMT = { "1", "E", ")", "5", "-", "+", "&" };
		String[] kenhNB = { "BL146", "CO205", "CO400" };
		for (int i = 0; i < keHoachGiaoHangs.size(); i++) {
			String kenhKH = null;
			String maxn = keHoachGiaoHangs.get(i).getIeCategories() != null ? keHoachGiaoHangs.get(i).getIeCategories()
					.getCode() : null;
			String makh = keHoachGiaoHangs.get(i).getCustomer() != null ? keHoachGiaoHangs.get(i).getCustomer()
					.getCustomer_code() : null;

			if (maxn != null) {
				if (Arrays.asList(kenhGT).contains(maxn)) {
					kenhKH = "2. KÊNH GT";
				} else if (Arrays.asList(kenhMT).contains(maxn)) {
					kenhKH = "1. KÊNH MT";
				} else {
					if ("CO573".equals(makh)) {
						System.out.println("maxn: " + maxn);
					}
					kenhKH = "4. KÊNH KHÁC";
				}
			} else {
				if ("CO573".equals(makh)) {
					System.out.println("maxn: " + maxn + "-" + i);
				}
				kenhKH = "4. KÊNH KHÁC";
			}
			if (Arrays.asList(kenhNB).contains(makh)) {
				kenhKH = "3. KÊNH NỘI BỘ";
			}
			keHoachGiaoHangs.get(i).setKenhkhachhang(kenhKH);

		}
	}
	public int tongXe(String kenh) {
	    return keHoachGiaoHangs.stream()
	        .filter(i -> kenh.equals(i.getKenhkhachhang()))
	        .mapToInt(KeHoachGiaoHang::getSlxe)
	        .sum();
	}

	public double tongKhoiLuong(String kenh) {
	    return keHoachGiaoHangs.stream()
	        .filter(i -> kenh.equals(i.getKenhkhachhang()))
	        .mapToDouble(KeHoachGiaoHang::getKhoiluongvc)
	        .sum();
	}

	public static void main(String[] args) {
		String[] kenhMT = { "1", "E", ")", "5", "-", "+", "&" };
		String mxn = "E";
		if (Arrays.asList(kenhMT).contains(mxn)) {
			System.out.println("1. KÊNH MT");

		}
	}

	@Override
	protected void initItem() {
		ngaySearch = MyUtil.loaibogio(new Date());
		ngaygh = ngaySearch;
		search();
	}

	public void search() {
		if (ngaySearch != null) {
			keHoachGiaoHangs = keHoachGiaoHangService.search(ngaySearch);
		} else {
			warning("Chưa nhập dữ liệu giao hàng ngày.");
		}
	}

	public void saveOrUpdate() {
		if (keHoachGiaoHangs.size() != 0) {
			if (allowSave(null) && allowUpdate(null)) {
				boolean status = keHoachGiaoHangService.saveOrUpdate(keHoachGiaoHangs, getAccount().getMember()
						.getName());
				if (status) {
					search();
					success();
				} else {
					error("Không lưu được, kiểm tra log server.");
				}
			} else {
				warning("Tài khoản này không có quyền thực hiện.");
			}
		} else {
			warning("Không có dữ liệu.");
		}
	}

	@Inject
	ParamReportDetailService paramReportDetailService;

	public void inKHGH() {
		try {
			if (keHoachGiaoHangs.isEmpty()) {
				warning("Không có dữ liệu.");
			} else {
				executeScript("target='_blank';monitorDownload( showStatus , hideStatus)");
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/kehoachgiaohang.jasper");
				JRDataSource beanDataSource = new JRBeanCollectionDataSource(keHoachGiaoHangs);
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
				List<ParamReportDetail> listConfig = paramReportDetailService.findByParamReportName("thongtinchung");
				for (ParamReportDetail p : listConfig) {
					importParam.put(p.getKey(), p.getValue());
				}
				importParam.put("date_string", "Ngày " + MyUtil.chuyensangStr(ngaygh));
				JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, beanDataSource);
				FacesContext facesContext = FacesContext.getCurrentInstance();
				OutputStream outputStream = facesContext.getExternalContext().getResponseOutputStream();
				JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
				facesContext.responseComplete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void excelKHGH() {
		try {
			// xuat file excel.
			if (keHoachGiaoHangs.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "Kênh KH", "Mã KH", "Tên KH", "Số lượng xe", "Giờ dự kiến", "số lượng booking(kg)",
						"Đơn vị vận chuyển", "Ghi chú" };
				results.add(title);
				for (KeHoachGiaoHang it : keHoachGiaoHangs) {
					Object[] item = { it.getKenhkhachhang(),
							it.getCustomer() != null ? it.getCustomer().getCustomer_code() : "",
							it.getCustomer() != null ? it.getCustomer().getCustomer_name() : "", it.getSlxe(),
							MyUtil.chuyensangStrHHmm(it.getNgaygioxevao()), it.getKhoiluongvc(),
							it.getCarrier() != null ? it.getCarrier().getCarrier_name() : "", it.getGhichu() };
					results.add(item);
				}
				ToolReport.printReportExcelRawXLSX(results, "KHGiaoHang_" + MyUtil.chuyensangStrCode(ngaySearch));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
