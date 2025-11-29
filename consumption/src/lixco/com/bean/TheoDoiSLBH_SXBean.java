package lixco.com.bean;

import java.math.BigDecimal;
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
import lixco.com.einvoice_data.EInvoiceV3;
import lixco.com.entity.KeHoachSL_BanHang;
import lixco.com.entity.KeHoachSL_SanXuat;
import lixco.com.entity.ProductKM;
import lixco.com.entity.TheoDoiSLBH_SX;
import lixco.com.hddt.InvoiceToJson;
import lixco.com.hddt.ThongBao;
import lixco.com.interfaces.IGoodsReceiptNoteDetailService;
import lixco.com.interfaces.IInventoryService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.PhieuXuatKho;
import lixco.com.reportInfo.SoLieuBaoCaoTongHop;
import lixco.com.service.KeHoachSL_BanHangService;
import lixco.com.service.KeHoachSL_SanXuatService;
import lixco.com.service.TheoDoiSLBH_SXService;
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
import org.primefaces.PrimeFaces;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.ParamReportDetail;
import trong.lixco.com.info.SLBH;
import trong.lixco.com.info.TheoDoiSLBH_SX_Data;
import trong.lixco.com.service.AccountAPIService;
import trong.lixco.com.util.ConvertNumberToText;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.TheoDoiSLBH_SXProccess;
import trong.lixco.com.util.ToolReport;

import com.google.gson.reflect.TypeToken;

import java.io.OutputStream;
import java.lang.reflect.Type;

@Named
@ViewScoped
public class TheoDoiSLBH_SXBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Getter
	@Setter
	List<TheoDoiSLBH_SX_Data> theoDoiSLBH_SX_Datas;

	@Inject
	IGoodsReceiptNoteDetailService goodsReceiptNoteDetailService;
	@Getter
	@Setter
	Date ngaychotcapnhatdulieu;
	@Inject
	IInventoryService inventoryService;
	Gson gson;

	@Getter
	@Setter
	Date ngay;
	@Inject
	TheoDoiSLBH_SXService theoDoiSLBH_SXService;
	TheoDoiSLBH_SX theoDoiSLBH_SX;

	@Inject
	IReportService reportService;
	@Inject
	AccountAPIService accountAPIService;

	public void excelSLBH() {
		try {

			// xuat file excel.
			if (theoDoiSLBH_SX_Datas.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "Kênh", "Dự kiến SL bán", "Thực xuất", "Kế hoạch còn lại", "Ước còn lại",
						"Tồn kho đầu tháng", "Tồn kho cuối", "Kế hoạch sản xuất", "Đã sản xuất", "Kế hoạch SX còn lại" };
				results.add(title);
				for (TheoDoiSLBH_SX_Data it : theoDoiSLBH_SX_Datas) {
					Object[] item = { it.getKenh(), it.getDukienSLban(), it.getThucxuat(), it.getKhconlai(),
							it.getUocconlai(), it.getTonkhodauthang(), it.getTonkhocuoi(), it.getKehoachsx(),
							it.getDasx(), it.getKehoachsxconlai() };
					results.add(item);
				}
				ToolReport.printReportExcelRawXLSX(results, "sanluongBHSX_" + MyUtil.chuyensangStrCode(ngay));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void inSLBH() {
		try {
			executeScript("target='_blank';monitorDownload( showStatus , hideStatus)");
			String reportPath = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/resources/reports/slbh.jasper");
			JRDataSource beanDataSource = new JRBeanCollectionDataSource(theoDoiSLBH_SX_Datas);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("date_string", "Ngày " + MyUtil.chuyensangStr(ngay));
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, parameters, beanDataSource);
			FacesContext facesContext = FacesContext.getCurrentInstance();
			OutputStream outputStream = facesContext.getExternalContext().getResponseOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
			facesContext.responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("InvoiceBean.inSLBH:" + e.getMessage(), e);
		}
	}

	public void capnhatdulieu() {
		theoDoiSLBH_SX = new TheoDoiSLBH_SX();
		theoDoiSLBH_SX.setSLdate(ngaychotcapnhatdulieu);
		theoDoiSLBH_SX_Datas = new ArrayList<TheoDoiSLBH_SX_Data>();
		caidatDS_N();
		caidatDS_B();
		caidatDS_T();
		
		capnhatdulieuPN();
		capnhatdulieuPB();
		ngay = ngaychotcapnhatdulieu;
	}

	public void capnhatdulieuPN() {
		/*
		 * Cot da san xuat HCM (Bot giat, CTRL) -> LIX, OB, XK
		 */
		/*
		 * Bot giat phia nam
		 */
		if (ngaychotcapnhatdulieu == null) {
			warning("Chưa nhập ngày chốt dữ liệu.");
			return;
		}
		Date startDate = new Date(ngaychotcapnhatdulieu.getTime());
		startDate.setDate(1);
		Date endDate = ngaychotcapnhatdulieu;
		// {LIX,347,27}
		List<Object[]> dasxtheonhomhang = goodsReceiptNoteDetailService.reportImpFromPX_Lix(startDate, endDate);
		List<Object[]> dasxtheonhomhangBD = TheoDoiSLBH_SXProccess.dasxtheonhomhang("BD", accountAPIService, startDate,
				endDate);
		dasxtheonhomhang.addAll(dasxtheonhomhangBD);
		dasxtheonhomhang = TheoDoiSLBH_SXProccess.groupAndSumAsList(dasxtheonhomhang);

		int month = startDate.getMonth() + 1;
		int year = startDate.getYear() + 1900;
		if (month == 1) {
			month = 12;
			year = year - 1;
		} else {
			month = month - 1;
		}
		List<Object[]> tondaus = inventoryService.toncuoitheonhom(month, year);
		List<Object[]> tondauBD = TheoDoiSLBH_SXProccess.toncuoitheonhom("BD", accountAPIService, month, year);
		tondaus.addAll(tondauBD);
		tondaus = TheoDoiSLBH_SXProccess.groupAndSumAsList(tondaus);

		int monthNow = ngaychotcapnhatdulieu.getMonth() + 1;
		int yearNow = ngaychotcapnhatdulieu.getYear() + 1900;
		List<KeHoachSL_BanHang> khbhs = keHoachSL_BanHangService.search(yearNow);
		List<KeHoachSL_SanXuat> khsxs = keHoachSL_SanXuatService.search(yearNow);

		JsonObject json = new JsonObject();
		json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
		json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
		json.addProperty("company_code", getDatabase());

		String paramjson = JsonParserUtil.getGson().toJson(json);

		// List<SoLieuBaoCaoTongHop> xuathangs = new
		// ArrayList<SoLieuBaoCaoTongHop>();
		// reportService.reportLaySoLieuBaoCaoTongHop(paramjson, xuathangs,
		// true);
		// xuathangs.addAll(TheoDoiSLBH_SXProccess.xuathang("BD",
		// accountAPIService, paramjson));
		// List<Object[]> slbhs_HCM =
		// TheoDoiSLBH_SXProccess.tonghopsolieu(xuathangs);

		List<Object[]> slbhs_tho = reportService.reportLaySoLieuBaoCaoTongHop2(paramjson);
		slbhs_tho.addAll(TheoDoiSLBH_SXProccess.xuathang("BD", accountAPIService, paramjson));
		List<Object[]> slbhs_HCM_BD = TheoDoiSLBH_SXProccess.tonghopsolieu2(slbhs_tho);
		for (int i = 0; i < theoDoiSLBH_SX_Datas.size(); i++) {
			for (int j = 0; j < khbhs.size(); j++) {
				if ((theoDoiSLBH_SX_Datas.get(i).getKenhDT() == null && khbhs.get(j).getKenhDT() == null && theoDoiSLBH_SX_Datas
						.get(i).getKenh().equals(khbhs.get(j).getKenh()))
						|| (theoDoiSLBH_SX_Datas.get(i).getKenhDT() != null && theoDoiSLBH_SX_Datas.get(i).getKenhDT()
								.equals(khbhs.get(j).getKenhDT()))) {
					switch (monthNow) {
					case 1:
						theoDoiSLBH_SX_Datas.get(i).setDukienSLban(khbhs.get(j).getSl_1());
						break;
					case 2:
						theoDoiSLBH_SX_Datas.get(i).setDukienSLban(khbhs.get(j).getSl_2());
						break;
					case 3:
						theoDoiSLBH_SX_Datas.get(i).setDukienSLban(khbhs.get(j).getSl_3());
						break;
					case 4:
						theoDoiSLBH_SX_Datas.get(i).setDukienSLban(khbhs.get(j).getSl_4());
						break;
					case 5:
						theoDoiSLBH_SX_Datas.get(i).setDukienSLban(khbhs.get(j).getSl_5());
						break;
					case 6:
						theoDoiSLBH_SX_Datas.get(i).setDukienSLban(khbhs.get(j).getSl_6());
						break;
					case 7:
						theoDoiSLBH_SX_Datas.get(i).setDukienSLban(khbhs.get(j).getSl_7());
						break;
					case 8:
						theoDoiSLBH_SX_Datas.get(i).setDukienSLban(khbhs.get(j).getSl_8());
						break;
					case 9:
						theoDoiSLBH_SX_Datas.get(i).setDukienSLban(khbhs.get(j).getSl_9());
						break;
					case 10:
						theoDoiSLBH_SX_Datas.get(i).setDukienSLban(khbhs.get(j).getSl_10());
						break;
					case 11:
						theoDoiSLBH_SX_Datas.get(i).setDukienSLban(khbhs.get(j).getSl_11());
						break;
					case 12:
						theoDoiSLBH_SX_Datas.get(i).setDukienSLban(khbhs.get(j).getSl_12());
						break;
					default:
						break;
					}
				}
			}
			for (int j = 0; j < khsxs.size(); j++) {
				if ((theoDoiSLBH_SX_Datas.get(i).getKenhDT() == null && khsxs.get(j).getKenhDT() == null && theoDoiSLBH_SX_Datas
						.get(i).getKenh().equals(khsxs.get(j).getKenh()))
						|| (theoDoiSLBH_SX_Datas.get(i).getKenhDT() != null && theoDoiSLBH_SX_Datas.get(i).getKenhDT()
								.equals(khsxs.get(j).getKenhDT()))) {
					switch (monthNow) {
					case 1:
						theoDoiSLBH_SX_Datas.get(i).setKehoachsx(khsxs.get(j).getSl_1());
						break;
					case 2:
						theoDoiSLBH_SX_Datas.get(i).setKehoachsx(khsxs.get(j).getSl_2());
						break;
					case 3:
						theoDoiSLBH_SX_Datas.get(i).setKehoachsx(khsxs.get(j).getSl_3());
						break;
					case 4:
						theoDoiSLBH_SX_Datas.get(i).setKehoachsx(khsxs.get(j).getSl_4());
						break;
					case 5:
						theoDoiSLBH_SX_Datas.get(i).setKehoachsx(khsxs.get(j).getSl_5());
						break;
					case 6:
						theoDoiSLBH_SX_Datas.get(i).setKehoachsx(khsxs.get(j).getSl_6());
						break;
					case 7:
						theoDoiSLBH_SX_Datas.get(i).setKehoachsx(khsxs.get(j).getSl_7());
						break;
					case 8:
						theoDoiSLBH_SX_Datas.get(i).setKehoachsx(khsxs.get(j).getSl_8());
						break;
					case 9:
						theoDoiSLBH_SX_Datas.get(i).setKehoachsx(khsxs.get(j).getSl_9());
						break;
					case 10:
						theoDoiSLBH_SX_Datas.get(i).setKehoachsx(khsxs.get(j).getSl_10());
						break;
					case 11:
						theoDoiSLBH_SX_Datas.get(i).setKehoachsx(khsxs.get(j).getSl_11());
						break;
					case 12:
						theoDoiSLBH_SX_Datas.get(i).setKehoachsx(khsxs.get(j).getSl_12());
						break;
					default:
						break;
					}
				}
			}
			/*
			 * Bot phia nam
			 */
			if ("OB-MT B PN".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_HCM_BD.size(); j++) {
					if ("BỘT GIẶT".equals(slbhs_HCM_BD.get(j)[0])) {
						if ("OB-MT".equals(slbhs_HCM_BD.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_HCM_BD.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

				for (int j = 0; j < dasxtheonhomhang.size(); j++) {
					if ("Khác".equals(dasxtheonhomhang.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setDasx(Math.round(((double) dasxtheonhomhang.get(j)[1])));
						break;
					}
				}
				ajax_soluong(8, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(9, theoDoiSLBH_SX_Datas.get(i));
				for (int j = 0; j < tondaus.size(); j++) {
					if ("Khác".equals(tondaus.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setTonkhodauthang(Math.round((double) tondaus.get(j)[1]));
						break;
					}
				}
				ajax_soluong(5, theoDoiSLBH_SX_Datas.get(i));
			} else if ("LIX (GT+MT) B PN".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {

				for (int j = 0; j < dasxtheonhomhang.size(); j++) {
					if ("LIX".equals(dasxtheonhomhang.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setDasx(Math.round(((double) dasxtheonhomhang.get(j)[1])));
						break;
					}
				}
				ajax_soluong(8, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(9, theoDoiSLBH_SX_Datas.get(i));
				for (int j = 0; j < tondaus.size(); j++) {
					if ("LIX".equals(tondaus.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setTonkhodauthang(Math.round((double) tondaus.get(j)[1]));
						break;
					}
				}
				ajax_soluong(5, theoDoiSLBH_SX_Datas.get(i));
			} else if ("GT B PN".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_HCM_BD.size(); j++) {
					if ("BỘT GIẶT".equals(slbhs_HCM_BD.get(j)[0])) {
						if ("GT".equals(slbhs_HCM_BD.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_HCM_BD.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

			} else if ("MT B PN".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_HCM_BD.size(); j++) {
					if ("BỘT GIẶT".equals(slbhs_HCM_BD.get(j)[0])) {
						if ("MT".equals(slbhs_HCM_BD.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_HCM_BD.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

			} else if ("OB-MT B PN".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_HCM_BD.size(); j++) {
					if ("BỘT GIẶT".equals(slbhs_HCM_BD.get(j)[0])) {
						if ("OB-MT".equals(slbhs_HCM_BD.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_HCM_BD.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

			} else if ("XK B PN".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_HCM_BD.size(); j++) {
					if ("BỘT GIẶT".equals(slbhs_HCM_BD.get(j)[0])) {
						if ("XK".equals(slbhs_HCM_BD.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_HCM_BD.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));
				for (int j = 0; j < dasxtheonhomhang.size(); j++) {
					if ("XK".equals(dasxtheonhomhang.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setDasx(Math.round(((double) dasxtheonhomhang.get(j)[1])));
						break;
					}
				}
				ajax_soluong(8, theoDoiSLBH_SX_Datas.get(i));
				for (int j = 0; j < tondaus.size(); j++) {
					if ("XK".equals(tondaus.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setTonkhodauthang(Math.round((double) tondaus.get(j)[1]));
						break;
					}
				}
				ajax_soluong(5, theoDoiSLBH_SX_Datas.get(i));
			}
			/*
			 * Nuoc phia nam
			 */
			if ("OB-MT N PN".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_HCM_BD.size(); j++) {
					if ("NTRL".equals(slbhs_HCM_BD.get(j)[0])) {
						if ("OB-MT".equals(slbhs_HCM_BD.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_HCM_BD.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

				for (int j = 0; j < dasxtheonhomhang.size(); j++) {
					if ("Khác".equals(dasxtheonhomhang.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setDasx(Math.round(((double) dasxtheonhomhang.get(j)[2])));
						break;
					}
				}
				ajax_soluong(8, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(9, theoDoiSLBH_SX_Datas.get(i));
				for (int j = 0; j < tondaus.size(); j++) {
					if ("Khác".equals(tondaus.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setTonkhodauthang(Math.round((double) tondaus.get(j)[2]));
						break;
					}
				}
				ajax_soluong(5, theoDoiSLBH_SX_Datas.get(i));
			} else if ("LIX (GT+MT) N PN".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < dasxtheonhomhang.size(); j++) {
					if ("LIX".equals(dasxtheonhomhang.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setDasx(Math.round(((double) dasxtheonhomhang.get(j)[2])));
						break;
					}
				}
				ajax_soluong(8, theoDoiSLBH_SX_Datas.get(i));
				for (int j = 0; j < tondaus.size(); j++) {
					if ("LIX".equals(tondaus.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setTonkhodauthang(Math.round((double) tondaus.get(j)[2]));
						break;
					}
				}
				ajax_soluong(5, theoDoiSLBH_SX_Datas.get(i));
			} else if ("GT N PN".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_HCM_BD.size(); j++) {
					if ("NTRL".equals(slbhs_HCM_BD.get(j)[0])) {
						if ("GT".equals(slbhs_HCM_BD.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_HCM_BD.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

			} else if ("MT N PN".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_HCM_BD.size(); j++) {
					if ("NTRL".equals(slbhs_HCM_BD.get(j)[0])) {
						if ("MT".equals(slbhs_HCM_BD.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_HCM_BD.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

			} else if ("OB-MT N PN".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_HCM_BD.size(); j++) {
					if ("NTRL".equals(slbhs_HCM_BD.get(j)[0])) {
						if ("OB-MT".equals(slbhs_HCM_BD.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_HCM_BD.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

			} else if ("XK N PN".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_HCM_BD.size(); j++) {
					if ("NTRL".equals(slbhs_HCM_BD.get(j)[0])) {
						if ("XK".equals(slbhs_HCM_BD.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_HCM_BD.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

				for (int j = 0; j < dasxtheonhomhang.size(); j++) {
					if ("XK".equals(dasxtheonhomhang.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setDasx(Math.round(((double) dasxtheonhomhang.get(j)[2])));
						break;
					}
				}
				ajax_soluong(8, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(9, theoDoiSLBH_SX_Datas.get(i));
				for (int j = 0; j < tondaus.size(); j++) {
					if ("XK".equals(tondaus.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setTonkhodauthang(Math.round((double) tondaus.get(j)[2]));
						break;
					}
				}
				ajax_soluong(5, theoDoiSLBH_SX_Datas.get(i));
			}
			// ajax_soluong(1, theoDoiSLBH_SX_Datas.get(i));
			// ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
			// ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
			// ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));
			// ajax_soluong(5, theoDoiSLBH_SX_Datas.get(i));
			// ajax_soluong(6, theoDoiSLBH_SX_Datas.get(i));
			// ajax_soluong(7, theoDoiSLBH_SX_Datas.get(i));
			// ajax_soluong(8, theoDoiSLBH_SX_Datas.get(i));
			// ajax_soluong(9, theoDoiSLBH_SX_Datas.get(i));
		}

	}

	public void capnhatdulieuPB() {
		if (ngaychotcapnhatdulieu == null) {
			warning("Chưa nhập ngày chốt dữ liệu.");
			return;
		}
		Date startDate = new Date(ngaychotcapnhatdulieu.getTime());
		startDate.setDate(1);
		Date endDate = ngaychotcapnhatdulieu;
		// {LIX,347,27}
		List<Object[]> dasxtheonhomhangBN = TheoDoiSLBH_SXProccess.dasxtheonhomhang("BN", accountAPIService, startDate,
				endDate);
		dasxtheonhomhangBN = TheoDoiSLBH_SXProccess.groupAndSumAsList(dasxtheonhomhangBN);

		int month = startDate.getMonth() + 1;
		int year = startDate.getYear() + 1900;
		if (month == 1) {
			month = 12;
			year = year - 1;
		} else {
			month = month - 1;
		}
		List<Object[]> tondausBN = TheoDoiSLBH_SXProccess.toncuoitheonhom("BN", accountAPIService, month, year);
		tondausBN = TheoDoiSLBH_SXProccess.groupAndSumAsList(tondausBN);

		JsonObject json = new JsonObject();
		json.addProperty("from_date", ToolTimeCustomer.convertDateToString(startDate, "dd/MM/yyyy"));
		json.addProperty("to_date", ToolTimeCustomer.convertDateToString(endDate, "dd/MM/yyyy"));
		json.addProperty("company_code", getDatabase());

		String paramjson = JsonParserUtil.getGson().toJson(json);
		// List<SoLieuBaoCaoTongHop> xuathangs =
		// TheoDoiSLBH_SXProccess.xuathang("BN", accountAPIService, paramjson);
		// List<Object[]> slbhs_BN =
		// TheoDoiSLBH_SXProccess.tonghopsolieu(xuathangs);
		List<Object[]> slbhs_BN = TheoDoiSLBH_SXProccess.tonghopsolieu2(TheoDoiSLBH_SXProccess.xuathang("BN",
				accountAPIService, paramjson));
		for (int i = 0; i < theoDoiSLBH_SX_Datas.size(); i++) {
			/*
			 * Bot phia bac
			 */
			if ("OB-MT B PB".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_BN.size(); j++) {
					if ("BỘT GIẶT".equals(slbhs_BN.get(j)[0])) {
						if ("OB-MT".equals(slbhs_BN.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_BN.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

				for (int j = 0; j < dasxtheonhomhangBN.size(); j++) {
					if ("Khác".equals(dasxtheonhomhangBN.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setDasx(Math.round(((double) dasxtheonhomhangBN.get(j)[1])));
						break;
					}
				}
				ajax_soluong(8, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(9, theoDoiSLBH_SX_Datas.get(i));
				for (int j = 0; j < tondausBN.size(); j++) {
					if ("Khác".equals(tondausBN.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setTonkhodauthang(Math.round((double) tondausBN.get(j)[1]));
						break;
					}
				}
				ajax_soluong(5, theoDoiSLBH_SX_Datas.get(i));
			} else if ("LIX (GT+MT) B PB".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {

				for (int j = 0; j < dasxtheonhomhangBN.size(); j++) {
					if ("LIX".equals(dasxtheonhomhangBN.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setDasx(Math.round(((double) dasxtheonhomhangBN.get(j)[1])));
						break;
					}
				}
				ajax_soluong(8, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(9, theoDoiSLBH_SX_Datas.get(i));
				for (int j = 0; j < tondausBN.size(); j++) {
					if ("LIX".equals(tondausBN.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setTonkhodauthang(Math.round((double) tondausBN.get(j)[1]));
						break;
					}
				}
				ajax_soluong(5, theoDoiSLBH_SX_Datas.get(i));
			} else if ("GT B PB".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_BN.size(); j++) {
					if ("BỘT GIẶT".equals(slbhs_BN.get(j)[0])) {
						if ("GT".equals(slbhs_BN.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_BN.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

			} else if ("MT B PB".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_BN.size(); j++) {
					if ("BỘT GIẶT".equals(slbhs_BN.get(j)[0])) {
						if ("MT".equals(slbhs_BN.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_BN.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

			} else if ("OB-MT B PB".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_BN.size(); j++) {
					if ("BỘT GIẶT".equals(slbhs_BN.get(j)[0])) {
						if ("OB-MT".equals(slbhs_BN.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_BN.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

			} else if ("XK B PB".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {

				for (int j = 0; j < slbhs_BN.size(); j++) {
					if ("BỘT GIẶT".equals(slbhs_BN.get(j)[0])) {
						if ("XK".equals(slbhs_BN.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_BN.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

				for (int j = 0; j < dasxtheonhomhangBN.size(); j++) {
					if ("XK".equals(dasxtheonhomhangBN.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setDasx(Math.round(((double) dasxtheonhomhangBN.get(j)[1])));
						break;
					}
				}
				ajax_soluong(8, theoDoiSLBH_SX_Datas.get(i));
				// ajax_soluong(9, theoDoiSLBH_SX_Datas.get(i));
				for (int j = 0; j < tondausBN.size(); j++) {
					if ("XK".equals(tondausBN.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setTonkhodauthang(Math.round((double) tondausBN.get(j)[1]));
						break;
					}
				}
				ajax_soluong(5, theoDoiSLBH_SX_Datas.get(i));
			}
			/*
			 * Nuoc phia bac
			 */
			if ("OB-MT N PB".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_BN.size(); j++) {
					if ("NTRL".equals(slbhs_BN.get(j)[0])) {
						if ("OB-MT".equals(slbhs_BN.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_BN.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

				for (int j = 0; j < dasxtheonhomhangBN.size(); j++) {
					if ("Khác".equals(dasxtheonhomhangBN.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setDasx(Math.round(((double) dasxtheonhomhangBN.get(j)[2])));
						break;
					}
				}
				ajax_soluong(8, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(9, theoDoiSLBH_SX_Datas.get(i));
				for (int j = 0; j < tondausBN.size(); j++) {
					if ("Khác".equals(tondausBN.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setTonkhodauthang(Math.round((double) tondausBN.get(j)[2]));
						break;
					}
				}
				ajax_soluong(5, theoDoiSLBH_SX_Datas.get(i));
			} else if ("LIX (GT+MT) N PB".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < dasxtheonhomhangBN.size(); j++) {
					if ("LIX".equals(dasxtheonhomhangBN.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setDasx(Math.round(((double) dasxtheonhomhangBN.get(j)[2])));
						break;
					}
				}
				ajax_soluong(8, theoDoiSLBH_SX_Datas.get(i));
				// ajax_soluong(9, theoDoiSLBH_SX_Datas.get(i));
				for (int j = 0; j < tondausBN.size(); j++) {
					if ("LIX".equals(tondausBN.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setTonkhodauthang(Math.round((double) tondausBN.get(j)[2]));
						break;
					}
				}
				ajax_soluong(5, theoDoiSLBH_SX_Datas.get(i));
			} else if ("GT N PB".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_BN.size(); j++) {
					if ("NTRL".equals(slbhs_BN.get(j)[0])) {
						if ("GT".equals(slbhs_BN.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_BN.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

			} else if ("MT N PB".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_BN.size(); j++) {
					if ("NTRL".equals(slbhs_BN.get(j)[0])) {
						if ("MT".equals(slbhs_BN.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_BN.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

			} else if ("OB-MT N PB".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_BN.size(); j++) {
					if ("NTRL".equals(slbhs_BN.get(j)[0])) {
						if ("OB-MT".equals(slbhs_BN.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_BN.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

			} else if ("XK N PB".equals(theoDoiSLBH_SX_Datas.get(i).getKenhDT())) {
				for (int j = 0; j < slbhs_BN.size(); j++) {
					if ("NTRL".equals(slbhs_BN.get(j)[0])) {
						if ("XK".equals(slbhs_BN.get(j)[1])) {
							theoDoiSLBH_SX_Datas.get(i).setThucxuat(Math.round((double) slbhs_BN.get(j)[2]));
							double conlai = theoDoiSLBH_SX_Datas.get(i).getDukienSLban()
									- theoDoiSLBH_SX_Datas.get(i).getThucxuat();
							theoDoiSLBH_SX_Datas.get(i).setKhconlai(conlai);
							theoDoiSLBH_SX_Datas.get(i).setUocconlai(conlai);
							break;
						}
					}
				}
				ajax_soluong(2, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(3, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(4, theoDoiSLBH_SX_Datas.get(i));

				for (int j = 0; j < dasxtheonhomhangBN.size(); j++) {
					if ("XK".equals(dasxtheonhomhangBN.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setDasx(Math.round(((double) dasxtheonhomhangBN.get(j)[2])));
						break;
					}
				}
				ajax_soluong(8, theoDoiSLBH_SX_Datas.get(i));
				ajax_soluong(9, theoDoiSLBH_SX_Datas.get(i));
				for (int j = 0; j < tondausBN.size(); j++) {
					if ("XK".equals(tondausBN.get(j)[0])) {
						theoDoiSLBH_SX_Datas.get(i).setTonkhodauthang(Math.round((double) tondausBN.get(j)[2]));
						break;
					}
				}
				ajax_soluong(5, theoDoiSLBH_SX_Datas.get(i));
			}
		}
		// ajax_soluong(1, theoDoiSLBH_SX_Datas.get(27));
		// ajax_soluong(2, theoDoiSLBH_SX_Datas.get(27));
		// ajax_soluong(3, theoDoiSLBH_SX_Datas.get(27));
		// ajax_soluong(4, theoDoiSLBH_SX_Datas.get(27));
		// ajax_soluong(5, theoDoiSLBH_SX_Datas.get(27));
		// //
		// ajax_soluong(8, theoDoiSLBH_SX_Datas.get(27));
		// ajax_soluong(9, theoDoiSLBH_SX_Datas.get(27));
		for (int j = 1; j < 28; j++) {
			ajax_soluong(6, theoDoiSLBH_SX_Datas.get(j));
		}
		ajax_soluong(7, theoDoiSLBH_SX_Datas.get(27));
		// ajax_soluong(6, theoDoiSLBH_SX_Datas.get(21));
		// ajax_soluong(6, theoDoiSLBH_SX_Datas.get(27));
	}

	@Override
	protected void initItem() {
		gson = new Gson();
		ngay = MyUtil.loaibogio(new Date());
		ngaychotcapnhatdulieu = ngay;
		ngayCopy = ngay;
		yearSLBan = ngay.getYear() + 1900;
		yearSLSanXuat = ngay.getYear() + 1900;
		taidulieu();

	}

	public void taidulieu() {
		if (ngay != null) {
			theoDoiSLBH_SX = theoDoiSLBH_SXService.search(ngay);
			if (theoDoiSLBH_SX != null) {
				Type listType = new TypeToken<List<TheoDoiSLBH_SX_Data>>() {
				}.getType();
				theoDoiSLBH_SX_Datas = gson.fromJson(theoDoiSLBH_SX.getTheoDoiSLBH_SX_Data(), listType);
			} else {
				theoDoiSLBH_SX = new TheoDoiSLBH_SX();
				theoDoiSLBH_SX.setSLdate(ngay);
				theoDoiSLBH_SX_Datas = new ArrayList<TheoDoiSLBH_SX_Data>();
				caidatDS_N();
				caidatDS_B();
				caidatDS_T();
			}
		} else {
			warning("Chưa nhập dữ liệu ngày.");
		}

	}

	private void caidatDS_N() {
		TheoDoiSLBH_SX_Data td1 = new TheoDoiSLBH_SX_Data();
		td1.setKenh("PHÍA NAM");
		td1.setKenhDT("PHÍA NAM");
		td1.setThuockenh("TỔNG CỘNG");
		td1.setKenhLever(1);
		theoDoiSLBH_SX_Datas.add(td1);

		TheoDoiSLBH_SX_Data td2 = new TheoDoiSLBH_SX_Data();
		td2.setKenh("Tổng CTRL");
		td2.setKenhDT("Tổng CTRL PN");
		td2.setKenhLever(2);
		td2.setThuockenh("PHÍA NAM");
		theoDoiSLBH_SX_Datas.add(td2);

		TheoDoiSLBH_SX_Data td3 = new TheoDoiSLBH_SX_Data();
		td3.setKenh("LIX (GT+MT)");
		td3.setKenhDT("LIX (GT+MT) N PN");
		td3.setKenhLever(3);
		td3.setThuockenh("Tổng CTRL PN");
		theoDoiSLBH_SX_Datas.add(td3);

		TheoDoiSLBH_SX_Data td4 = new TheoDoiSLBH_SX_Data();
		td4.setKenh("GT");
		td4.setKenhDT("GT N PN");
		td4.setKenhLever(4);
		td4.setThuockenh("LIX (GT+MT) N PN");
		theoDoiSLBH_SX_Datas.add(td4);

		TheoDoiSLBH_SX_Data td5 = new TheoDoiSLBH_SX_Data();
		td5.setKenh("MT");
		td5.setKenhDT("MT N PN");
		td5.setKenhLever(4);
		td5.setThuockenh("LIX (GT+MT) N PN");
		theoDoiSLBH_SX_Datas.add(td5);

		TheoDoiSLBH_SX_Data td6 = new TheoDoiSLBH_SX_Data();
		td6.setKenh("OB-MT");
		td6.setKenhDT("OB-MT N PN");
		td6.setKenhLever(3);
		td6.setThuockenh("Tổng CTRL PN");
		theoDoiSLBH_SX_Datas.add(td6);

		TheoDoiSLBH_SX_Data td7 = new TheoDoiSLBH_SX_Data();
		td7.setKenh("XK");
		td7.setKenhDT("XK N PN");
		td7.setKenhLever(3);
		td7.setThuockenh("Tổng CTRL PN");
		theoDoiSLBH_SX_Datas.add(td7);

		TheoDoiSLBH_SX_Data td8 = new TheoDoiSLBH_SX_Data();
		td8.setKenh("Tổng Bột giặt");
		td8.setKenhDT("Tổng Bột giặt PN");
		td8.setKenhLever(2);
		td8.setThuockenh("PHÍA NAM");
		theoDoiSLBH_SX_Datas.add(td8);

		TheoDoiSLBH_SX_Data td9 = new TheoDoiSLBH_SX_Data();
		td9.setKenh("LIX (GT+MT)");
		td9.setKenhDT("LIX (GT+MT) B PN");
		td9.setKenhLever(3);
		td9.setThuockenh("Tổng Bột giặt PN");
		theoDoiSLBH_SX_Datas.add(td9);

		TheoDoiSLBH_SX_Data td10 = new TheoDoiSLBH_SX_Data();
		td10.setKenh("GT");
		td10.setKenhDT("GT B PN");
		td10.setKenhLever(4);
		td10.setThuockenh("LIX (GT+MT)");
		td10.setThuockenh("LIX (GT+MT) B PN");
		theoDoiSLBH_SX_Datas.add(td10);

		TheoDoiSLBH_SX_Data td11 = new TheoDoiSLBH_SX_Data();
		td11.setKenh("MT");
		td11.setKenhDT("MT B PN");
		td11.setKenhLever(4);
		td11.setThuockenh("LIX (GT+MT) B PN");
		theoDoiSLBH_SX_Datas.add(td11);

		TheoDoiSLBH_SX_Data td12 = new TheoDoiSLBH_SX_Data();
		td12.setKenh("OB-MT");
		td12.setKenhDT("OB-MT B PN");
		td12.setKenhLever(3);
		td12.setThuockenh("Tổng Bột giặt PN");
		theoDoiSLBH_SX_Datas.add(td12);

		TheoDoiSLBH_SX_Data td13 = new TheoDoiSLBH_SX_Data();
		td13.setKenh("XK");
		td13.setKenhDT("XK B PN");
		td13.setKenhLever(3);
		td13.setThuockenh("Tổng Bột giặt PN");
		theoDoiSLBH_SX_Datas.add(td13);
	}

	private void caidatDS_B() {
		TheoDoiSLBH_SX_Data td1 = new TheoDoiSLBH_SX_Data();
		td1.setKenh("PHÍA BẮC");
		td1.setKenhDT("PHÍA BẮC");
		td1.setKenhLever(1);
		td1.setThuockenh("TỔNG CỘNG");
		theoDoiSLBH_SX_Datas.add(td1);

		TheoDoiSLBH_SX_Data td2 = new TheoDoiSLBH_SX_Data();
		td2.setKenh("Tổng CTRL");
		td2.setKenhDT("Tổng CTRL PB");
		td2.setKenhLever(2);
		td2.setThuockenh("PHÍA BẮC");
		theoDoiSLBH_SX_Datas.add(td2);

		TheoDoiSLBH_SX_Data td2_ = new TheoDoiSLBH_SX_Data();
		td2_.setKenh("Nhập miền nam");
		td2_.setKenhDT("MN CTRL PB");
		td2_.setKenhLever(2);
		td2_.setDuocnhap(true);
		td2_.setThuockenh("MN");
		theoDoiSLBH_SX_Datas.add(td2_);

		TheoDoiSLBH_SX_Data td3 = new TheoDoiSLBH_SX_Data();
		td3.setKenh("LIX (GT+MT)");
		td3.setKenhDT("LIX (GT+MT) N PB");
		td3.setKenhLever(3);
		td3.setThuockenh("Tổng CTRL PB");
		theoDoiSLBH_SX_Datas.add(td3);

		TheoDoiSLBH_SX_Data td4 = new TheoDoiSLBH_SX_Data();
		td4.setKenh("GT");
		td4.setKenhDT("GT N PB");
		td4.setKenhLever(4);
		td4.setThuockenh("LIX (GT+MT) N PB");
		theoDoiSLBH_SX_Datas.add(td4);

		TheoDoiSLBH_SX_Data td5 = new TheoDoiSLBH_SX_Data();
		td5.setKenh("MT");
		td5.setKenhDT("MT N PB");
		td5.setKenhLever(4);
		td5.setThuockenh("LIX (GT+MT) N PB");
		theoDoiSLBH_SX_Datas.add(td5);

		TheoDoiSLBH_SX_Data td6 = new TheoDoiSLBH_SX_Data();
		td6.setKenh("OB-MT");
		td6.setKenhDT("OB-MT N PB");
		td6.setKenhLever(3);
		td6.setThuockenh("Tổng CTRL PB");
		theoDoiSLBH_SX_Datas.add(td6);

		TheoDoiSLBH_SX_Data td7 = new TheoDoiSLBH_SX_Data();
		td7.setKenh("XK");
		td7.setKenhDT("XK N PB");
		td7.setKenhLever(3);
		td7.setThuockenh("Tổng CTRL PB");
		theoDoiSLBH_SX_Datas.add(td7);

		TheoDoiSLBH_SX_Data td8 = new TheoDoiSLBH_SX_Data();
		td8.setKenh("Tổng Bột giặt");
		td8.setKenhDT("Tổng Bột giặt PB");
		td8.setKenhLever(2);
		td8.setThuockenh("PHÍA BẮC");
		theoDoiSLBH_SX_Datas.add(td8);

		TheoDoiSLBH_SX_Data td8_ = new TheoDoiSLBH_SX_Data();
		td8_.setKenh("Nhập miền nam");
		td8_.setKenhDT("MN B PB");
		td8_.setKenhLever(2);
		td8_.setDuocnhap(true);
		td8_.setThuockenh("MN");
		theoDoiSLBH_SX_Datas.add(td8_);

		TheoDoiSLBH_SX_Data td9 = new TheoDoiSLBH_SX_Data();
		td9.setKenh("LIX (GT+MT)");
		td9.setKenhDT("LIX (GT+MT) B PB");
		td9.setKenhLever(3);
		td9.setThuockenh("Tổng Bột giặt PB");
		theoDoiSLBH_SX_Datas.add(td9);

		TheoDoiSLBH_SX_Data td10 = new TheoDoiSLBH_SX_Data();
		td10.setKenh("GT");
		td10.setKenhDT("GT B PB");
		td10.setKenhLever(4);
		td10.setThuockenh("LIX (GT+MT)");
		td10.setThuockenh("LIX (GT+MT) B PB");
		theoDoiSLBH_SX_Datas.add(td10);

		TheoDoiSLBH_SX_Data td11 = new TheoDoiSLBH_SX_Data();
		td11.setKenh("MT");
		td11.setKenhDT("MT B PB");
		td11.setKenhLever(4);
		td11.setThuockenh("LIX (GT+MT) B PB");
		theoDoiSLBH_SX_Datas.add(td11);

		TheoDoiSLBH_SX_Data td12 = new TheoDoiSLBH_SX_Data();
		td12.setKenh("OB-MT");
		td12.setKenhDT("OB-MT B PB");
		td12.setKenhLever(3);
		td12.setThuockenh("Tổng Bột giặt PB");
		theoDoiSLBH_SX_Datas.add(td12);

		TheoDoiSLBH_SX_Data td13 = new TheoDoiSLBH_SX_Data();
		td13.setKenh("XK");
		td13.setKenhDT("XK B PB");
		td13.setKenhLever(3);
		td13.setThuockenh("Tổng Bột giặt PB");
		theoDoiSLBH_SX_Datas.add(td13);
	}

	private void caidatDS_T() {
		TheoDoiSLBH_SX_Data td15_t = new TheoDoiSLBH_SX_Data();
		td15_t.setKenh("TỔNG CỘNG");
		td15_t.setKenhDT("TỔNG CỘNG");
		td15_t.setKenhLever(0);
		theoDoiSLBH_SX_Datas.add(td15_t);

		TheoDoiSLBH_SX_Data td16_t = new TheoDoiSLBH_SX_Data();
		td16_t.setKenh("LIX (GT+MT)");
		td16_t.setKenhLever(2);
		theoDoiSLBH_SX_Datas.add(td16_t);
		TheoDoiSLBH_SX_Data td17_t = new TheoDoiSLBH_SX_Data();
		td17_t.setKenh("GT");
		td17_t.setKenhLever(3);
		theoDoiSLBH_SX_Datas.add(td17_t);
		TheoDoiSLBH_SX_Data td18_t = new TheoDoiSLBH_SX_Data();
		td18_t.setKenh("MT");
		td18_t.setKenhLever(3);
		theoDoiSLBH_SX_Datas.add(td18_t);
		TheoDoiSLBH_SX_Data td19_t = new TheoDoiSLBH_SX_Data();
		td19_t.setKenh("OB-MT");
		td19_t.setKenhLever(2);
		theoDoiSLBH_SX_Datas.add(td19_t);
		TheoDoiSLBH_SX_Data td20_t = new TheoDoiSLBH_SX_Data();
		td20_t.setKenh("XK");
		td20_t.setKenhLever(2);
		theoDoiSLBH_SX_Datas.add(td20_t);
	}

	public void ajax_soluong(int cot, TheoDoiSLBH_SX_Data itemprama) {
		int index = theoDoiSLBH_SX_Datas.indexOf(itemprama);
		if (index != -1) {
			switch (cot) {
			case 1:
				theoDoiSLBH_SX_Datas.get(index).setDukienSLban(itemprama.getDukienSLban());
				theoDoiSLBH_SX_Datas.get(index).setKhconlai(
						theoDoiSLBH_SX_Datas.get(index).getDukienSLban()
								- theoDoiSLBH_SX_Datas.get(index).getThucxuat());
				theoDoiSLBH_SX_Datas.get(index).setUocconlai(
						theoDoiSLBH_SX_Datas.get(index).getDukienSLban()
								- theoDoiSLBH_SX_Datas.get(index).getThucxuat());
				if (!"GT".equals(theoDoiSLBH_SX_Datas.get(index).getKenh())
						&& !"MT".equals(theoDoiSLBH_SX_Datas.get(index).getKenh())) {
					theoDoiSLBH_SX_Datas.get(index).setTonkhocuoi(
							theoDoiSLBH_SX_Datas.get(index).getTonkhodauthang()
									+ theoDoiSLBH_SX_Datas.get(index).getDasx()
									- theoDoiSLBH_SX_Datas.get(index).getThucxuat());
				}
				break;
			case 2:
				theoDoiSLBH_SX_Datas.get(index).setThucxuat(itemprama.getThucxuat());
				theoDoiSLBH_SX_Datas.get(index).setKhconlai(
						theoDoiSLBH_SX_Datas.get(index).getDukienSLban()
								- theoDoiSLBH_SX_Datas.get(index).getThucxuat());
				theoDoiSLBH_SX_Datas.get(index).setUocconlai(
						theoDoiSLBH_SX_Datas.get(index).getDukienSLban()
								- theoDoiSLBH_SX_Datas.get(index).getThucxuat());
				if (!"GT".equals(theoDoiSLBH_SX_Datas.get(index).getKenh())
						&& !"MT".equals(theoDoiSLBH_SX_Datas.get(index).getKenh())) {
					theoDoiSLBH_SX_Datas.get(index).setTonkhocuoi(
							theoDoiSLBH_SX_Datas.get(index).getTonkhodauthang()
									+ theoDoiSLBH_SX_Datas.get(index).getDasx()
									- theoDoiSLBH_SX_Datas.get(index).getThucxuat());
				}
				break;
			case 3:
				theoDoiSLBH_SX_Datas.get(index).setKhconlai(itemprama.getKhconlai());
				break;
			case 4:
				theoDoiSLBH_SX_Datas.get(index).setUocconlai(itemprama.getUocconlai());
				break;
			case 5:
				theoDoiSLBH_SX_Datas.get(index).setTonkhodauthang(itemprama.getTonkhodauthang());
				break;
			case 6:
				// theoDoiSLBH_SX_Datas.get(index).setTonkhocuoi(itemprama.getTonkhocuoi());
				if (!"GT".equals(theoDoiSLBH_SX_Datas.get(index).getKenh())
						&& !"MT".equals(theoDoiSLBH_SX_Datas.get(index).getKenh())) {
					theoDoiSLBH_SX_Datas.get(index).setTonkhocuoi(
							theoDoiSLBH_SX_Datas.get(index).getTonkhodauthang()
									+ theoDoiSLBH_SX_Datas.get(index).getDasx()
									- theoDoiSLBH_SX_Datas.get(index).getThucxuat());
				}
				break;
			case 7:
				theoDoiSLBH_SX_Datas.get(index).setKehoachsx(itemprama.getKehoachsx());
				theoDoiSLBH_SX_Datas.get(index).setKehoachsxconlai(itemprama.getKehoachsx() - itemprama.getDasx());
				break;
			case 8:
				theoDoiSLBH_SX_Datas.get(index).setDasx(itemprama.getDasx());
				theoDoiSLBH_SX_Datas.get(index).setKehoachsxconlai(itemprama.getKehoachsx() - itemprama.getDasx());
				if (!"GT".equals(theoDoiSLBH_SX_Datas.get(index).getKenh())
						&& !"MT".equals(theoDoiSLBH_SX_Datas.get(index).getKenh())) {
					theoDoiSLBH_SX_Datas.get(index).setTonkhocuoi(
							theoDoiSLBH_SX_Datas.get(index).getTonkhodauthang()
									+ theoDoiSLBH_SX_Datas.get(index).getDasx()
									- theoDoiSLBH_SX_Datas.get(index).getThucxuat());
				}
				break;
			case 9:
				theoDoiSLBH_SX_Datas.get(index).setKehoachsxconlai(itemprama.getKehoachsxconlai());
				break;
			}
		}

		TheoDoiSLBH_SX_Data[] itemprama_Kenhmoi = new TheoDoiSLBH_SX_Data[1];
		double total = theoDoiSLBH_SX_Datas.stream()
				.filter(item -> item.getThuockenh() != null && item.getThuockenh().equals(itemprama.getThuockenh()))
				.mapToDouble(item -> {
					switch (cot) {
					case 1:
						return item.getDukienSLban();

					case 2:
						return item.getThucxuat();
					case 3:
						return item.getKhconlai();
					case 4:
						return item.getUocconlai();
					case 5:
						return item.getTonkhodauthang();
						// case 6:
						// return item.getTonkhocuoi();
					case 7:
						return item.getKehoachsx();
					case 8:
						return item.getDasx();
					case 9:
						return item.getKehoachsxconlai();
					default:
						return 0;
					}
				}).sum();

		theoDoiSLBH_SX_Datas.forEach(item -> {
			if (item.getKenhDT() != null && item.getKenhDT().equals(itemprama.getThuockenh())) {
				switch (cot) {
				case 1:
					item.setDukienSLban(total);
					if (!"GT".equals(item.getKenh()) && !"MT".equals(item.getKenh())) {
						item.setTonkhocuoi(item.getTonkhodauthang() + item.getDasx() - item.getThucxuat());
					}
					break;
				case 2:
					item.setThucxuat(total);
					if (!"GT".equals(item.getKenh()) && !"MT".equals(item.getKenh())) {
						item.setTonkhocuoi(item.getTonkhodauthang() + item.getDasx() - item.getThucxuat());
					}
					break;
				case 3:
					item.setKhconlai(total);
					break;
				case 4:
					item.setUocconlai(total);
					break;
				case 5:
					item.setTonkhodauthang(total);
					break;
				case 6:
					// item.setTonkhocuoi(total);
				if (!"GT".equals(item.getKenh()) && !"MT".equals(item.getKenh())) {
					item.setTonkhocuoi(item.getTonkhodauthang() + item.getDasx() - item.getThucxuat());
				}
					break;
				case 7:
					item.setKehoachsx(total);
					break;
				case 8:
					item.setDasx(total);
					if (!"GT".equals(item.getKenh()) && !"MT".equals(item.getKenh())) {
						item.setTonkhocuoi(item.getTonkhodauthang() + item.getDasx() - item.getThucxuat());
					}
					break;
				case 9:
					item.setKehoachsxconlai(total);
					break;
				}
				itemprama_Kenhmoi[0] = item; // Lưu giá trị vào mảng
			}
		});
		/*
		 * Cài đặt tổng cộng
		 */
		TheoDoiSLBH_SX_Data finalItemprama_Kenhmoi = itemprama_Kenhmoi[0];
		if (finalItemprama_Kenhmoi != null && finalItemprama_Kenhmoi.getKenhLever() != 0) {
			ajax_soluong(cot, finalItemprama_Kenhmoi);
		} else {

			theoDoiSLBH_SX_Datas.forEach(item -> {
				// System.out.println("Cot: "+cot);
					switch (cot) {
					case 1:
						double totalGT1 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getDukienSLban).sum();
						double totalMT1 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getDukienSLban).sum();
						double totalOB_MT1 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getDukienSLban).sum();
						double totalXK1 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getDukienSLban).sum();

						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setDukienSLban(totalGT1);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setDukienSLban(totalMT1);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setDukienSLban(totalGT1 + totalMT1);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setDukienSLban(totalOB_MT1);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setDukienSLban(totalXK1);
						}
						break;
					case 2:
						double totalGT2 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getThucxuat).sum();
						double totalMT2 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getThucxuat).sum();
						double totalOB_MT2 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getThucxuat).sum();
						double totalXK2 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getThucxuat).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setThucxuat(totalGT2);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setThucxuat(totalMT2);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setThucxuat(totalGT2 + totalMT2);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setThucxuat(totalOB_MT2);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setThucxuat(totalXK2);
						}
						break;
					case 3:
						double totalGT3 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getKhconlai).sum();
						double totalMT3 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getKhconlai).sum();
						double totalOB_MT3 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getKhconlai).sum();
						double totalXK3 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getKhconlai).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setKhconlai(totalGT3);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setKhconlai(totalMT3);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setKhconlai(totalGT3 + totalMT3);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setKhconlai(totalOB_MT3);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setKhconlai(totalXK3);
						}
						break;
					case 4:
						double totalGT4 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getUocconlai).sum();
						double totalMT4 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getUocconlai).sum();
						double totalOB_MT4 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getUocconlai).sum();
						double totalXK4 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getUocconlai).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setUocconlai(totalGT4);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setUocconlai(totalMT4);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setUocconlai(totalGT4 + totalMT4);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setUocconlai(totalOB_MT4);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setUocconlai(totalXK4);
						}
						break;
					case 5:

						double totalGT_MT5 = theoDoiSLBH_SX_Datas
								.stream()
								.filter(itemsub -> itemsub.getKenh().equals("LIX (GT+MT)")
										&& itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getTonkhodauthang).sum();
						double totalOB_MT5 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getTonkhodauthang).sum();
						double totalXK5 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getTonkhodauthang).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setTonkhodauthang(totalGT_MT5);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setTonkhodauthang(totalOB_MT5);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setTonkhodauthang(totalXK5);
						}
						break;
					case 6:
						double totalGT_MT6 = theoDoiSLBH_SX_Datas
								.stream()
								.filter(itemsub -> itemsub.getKenh().equals("LIX (GT+MT)")
										&& itemsub.getKenhDT() != null).mapToDouble(TheoDoiSLBH_SX_Data::getTonkhocuoi)
								.sum();
						double totalOB_MT6 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getTonkhocuoi).sum();
						double totalXK6 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getTonkhocuoi).sum();

						if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {

							// System.out.println("totalGT_MT6: "+totalGT_MT6);
							item.setTonkhocuoi(totalGT_MT6);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {

							item.setTonkhocuoi(totalOB_MT6);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {

							// double a = 0;
							// for (int i = 0; i < theoDoiSLBH_SX_Datas.size();
							// i++) {
							// if (theoDoiSLBH_SX_Datas.get(i).getKenhDT() !=
							// null
							// &&
							// theoDoiSLBH_SX_Datas.get(i).getKenh().equals("XK"))
							// {
							// System.out.println("\t" +
							// theoDoiSLBH_SX_Datas.get(i).getTonkhocuoi());
							// a += theoDoiSLBH_SX_Datas.get(i).getTonkhocuoi();
							// }
							// }
							// System.out.println("\n\n\n");
							item.setTonkhocuoi(totalXK6);
						}
						break;
					case 7:
						double totalGT_MT7 = theoDoiSLBH_SX_Datas
								.stream()
								.filter(itemsub -> itemsub.getKenh().equals("LIX (GT+MT)")
										&& itemsub.getKenhDT() != null).mapToDouble(TheoDoiSLBH_SX_Data::getKehoachsx)
								.sum();
						double totalOB_MT7 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getKehoachsx).sum();
						double totalXK7 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getKehoachsx).sum();

						if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setKehoachsx(totalGT_MT7);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setKehoachsx(totalOB_MT7);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setKehoachsx(totalXK7);
						}
						break;
					case 8:
						double totalGT_MT8 = theoDoiSLBH_SX_Datas
								.stream()
								.filter(itemsub -> itemsub.getKenh().equals("LIX (GT+MT)")
										&& itemsub.getKenhDT() != null).mapToDouble(TheoDoiSLBH_SX_Data::getDasx).sum();
						double totalOB_MT8 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getDasx).sum();
						double totalXK8 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getDasx).sum();

						if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setDasx(totalGT_MT8);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setDasx(totalOB_MT8);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setDasx(totalXK8);
						}
						break;
					case 9:
						double totalGT_MT9 = theoDoiSLBH_SX_Datas
								.stream()
								.filter(itemsub -> itemsub.getKenh().equals("LIX (GT+MT)")
										&& itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getKehoachsxconlai).sum();
						double totalOB_MT9 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getKehoachsxconlai).sum();
						double totalXK9 = theoDoiSLBH_SX_Datas.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(TheoDoiSLBH_SX_Data::getKehoachsxconlai).sum();

						if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setKehoachsxconlai(totalGT_MT9);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setKehoachsxconlai(totalOB_MT9);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setKehoachsxconlai(totalXK9);
						}
						break;
					}

				});
		}
	}

	@Getter
	@Setter
	int yearSLBan;
	@Getter
	@Setter
	List<KeHoachSL_BanHang> keHoachSL_BanHangs;
	@Inject
	KeHoachSL_BanHangService keHoachSL_BanHangService;

	public void kehoachSLBanHang() {
		keHoachSL_BanHangs = keHoachSL_BanHangService.search(yearSLBan);
		if (keHoachSL_BanHangs.size() == 0) {
			kehoachSLBanHang_N();
			kehoachSLBanHang_B();
			kehoachSLBanHang_T();

		}

		updateform("idfdukienslban");
		showDialog("dldukienslban");
	}

	private void kehoachSLBanHang_N() {
		KeHoachSL_BanHang td1 = new KeHoachSL_BanHang();
		td1.setYear(yearSLBan);
		td1.setKenh("PHÍA NAM");
		td1.setKenhDT("PHÍA NAM");
		td1.setThuockenh("TỔNG CỘNG");
		td1.setKenhLever(1);
		keHoachSL_BanHangs.add(td1);

		KeHoachSL_BanHang td2 = new KeHoachSL_BanHang();
		td2.setYear(yearSLBan);
		td2.setKenh("Tổng CTRL");
		td2.setKenhDT("Tổng CTRL PN");
		td2.setKenhLever(2);
		td2.setThuockenh("PHÍA NAM");
		keHoachSL_BanHangs.add(td2);

		KeHoachSL_BanHang td3 = new KeHoachSL_BanHang();
		td3.setYear(yearSLBan);
		td3.setKenh("LIX (GT+MT)");
		td3.setKenhDT("LIX (GT+MT) N PN");
		td3.setKenhLever(3);
		td3.setThuockenh("Tổng CTRL PN");
		keHoachSL_BanHangs.add(td3);

		KeHoachSL_BanHang td4 = new KeHoachSL_BanHang();
		td4.setYear(yearSLBan);
		td4.setKenh("GT");
		td4.setKenhDT("GT N PN");
		td4.setKenhLever(4);
		td4.setThuockenh("LIX (GT+MT) N PN");
		keHoachSL_BanHangs.add(td4);

		KeHoachSL_BanHang td5 = new KeHoachSL_BanHang();
		td5.setYear(yearSLBan);
		td5.setKenh("MT");
		td5.setKenhDT("MT N PN");
		td5.setKenhLever(4);
		td5.setThuockenh("LIX (GT+MT) N PN");
		keHoachSL_BanHangs.add(td5);

		KeHoachSL_BanHang td6 = new KeHoachSL_BanHang();
		td6.setYear(yearSLBan);
		td6.setKenh("OB-MT");
		td6.setKenhDT("OB-MT N PN");
		td6.setKenhLever(3);
		td6.setThuockenh("Tổng CTRL PN");
		keHoachSL_BanHangs.add(td6);

		KeHoachSL_BanHang td7 = new KeHoachSL_BanHang();
		td7.setYear(yearSLBan);
		td7.setKenh("XK");
		td7.setKenhDT("XK N PN");
		td7.setKenhLever(3);
		td7.setThuockenh("Tổng CTRL PN");
		keHoachSL_BanHangs.add(td7);

		KeHoachSL_BanHang td8 = new KeHoachSL_BanHang();
		td8.setYear(yearSLBan);
		td8.setKenh("Tổng Bột giặt");
		td8.setKenhDT("Tổng Bột giặt PN");
		td8.setKenhLever(2);
		td8.setThuockenh("PHÍA NAM");
		keHoachSL_BanHangs.add(td8);

		KeHoachSL_BanHang td9 = new KeHoachSL_BanHang();
		td9.setYear(yearSLBan);
		td9.setKenh("LIX (GT+MT)");
		td9.setKenhDT("LIX (GT+MT) B PN");
		td9.setKenhLever(3);
		td9.setThuockenh("Tổng Bột giặt PN");
		keHoachSL_BanHangs.add(td9);

		KeHoachSL_BanHang td10 = new KeHoachSL_BanHang();
		td10.setYear(yearSLBan);
		td10.setKenh("GT");
		td10.setKenhDT("GT B PN");
		td10.setKenhLever(4);
		td10.setThuockenh("LIX (GT+MT)");
		td10.setThuockenh("LIX (GT+MT) B PN");
		keHoachSL_BanHangs.add(td10);

		KeHoachSL_BanHang td11 = new KeHoachSL_BanHang();
		td11.setYear(yearSLBan);
		td11.setKenh("MT");
		td11.setKenhDT("MT B PN");
		td11.setKenhLever(4);
		td11.setThuockenh("LIX (GT+MT) B PN");
		keHoachSL_BanHangs.add(td11);

		KeHoachSL_BanHang td12 = new KeHoachSL_BanHang();
		td12.setYear(yearSLBan);
		td12.setKenh("OB-MT");
		td12.setKenhDT("OB-MT B PN");
		td12.setKenhLever(3);
		td12.setThuockenh("Tổng Bột giặt PN");
		keHoachSL_BanHangs.add(td12);

		KeHoachSL_BanHang td13 = new KeHoachSL_BanHang();
		td13.setYear(yearSLBan);
		td13.setKenh("XK");
		td13.setKenhDT("XK B PN");
		td13.setKenhLever(3);
		td13.setThuockenh("Tổng Bột giặt PN");
		keHoachSL_BanHangs.add(td13);
	}

	private void kehoachSLBanHang_B() {
		KeHoachSL_BanHang td1 = new KeHoachSL_BanHang();
		td1.setYear(yearSLBan);
		td1.setKenh("PHÍA BẮC");
		td1.setKenhDT("PHÍA BẮC");
		td1.setKenhLever(1);
		td1.setThuockenh("TỔNG CỘNG");
		keHoachSL_BanHangs.add(td1);

		KeHoachSL_BanHang td2 = new KeHoachSL_BanHang();
		td2.setYear(yearSLBan);
		td2.setKenh("Tổng CTRL");
		td2.setKenhDT("Tổng CTRL PB");
		td2.setKenhLever(2);
		td2.setThuockenh("PHÍA BẮC");
		keHoachSL_BanHangs.add(td2);

		KeHoachSL_BanHang td3 = new KeHoachSL_BanHang();
		td3.setYear(yearSLBan);
		td3.setKenh("LIX (GT+MT)");
		td3.setKenhDT("LIX (GT+MT) N PB");
		td3.setKenhLever(3);
		td3.setThuockenh("Tổng CTRL PB");
		keHoachSL_BanHangs.add(td3);

		KeHoachSL_BanHang td4 = new KeHoachSL_BanHang();
		td4.setYear(yearSLBan);
		td4.setKenh("GT");
		td4.setKenhDT("GT N PB");
		td4.setKenhLever(4);
		td4.setThuockenh("LIX (GT+MT) N PB");
		keHoachSL_BanHangs.add(td4);

		KeHoachSL_BanHang td5 = new KeHoachSL_BanHang();
		td5.setYear(yearSLBan);
		td5.setKenh("MT");
		td5.setKenhDT("MT N PB");
		td5.setKenhLever(4);
		td5.setThuockenh("LIX (GT+MT) N PB");
		keHoachSL_BanHangs.add(td5);

		KeHoachSL_BanHang td6 = new KeHoachSL_BanHang();
		td6.setYear(yearSLBan);
		td6.setKenh("OB-MT");
		td6.setKenhDT("OB-MT N PB");
		td6.setKenhLever(3);
		td6.setThuockenh("Tổng CTRL PB");
		keHoachSL_BanHangs.add(td6);

		KeHoachSL_BanHang td7 = new KeHoachSL_BanHang();
		td7.setYear(yearSLBan);
		td7.setKenh("XK");
		td7.setKenhDT("XK N PB");
		td7.setKenhLever(3);
		td7.setThuockenh("Tổng CTRL PB");
		keHoachSL_BanHangs.add(td7);

		KeHoachSL_BanHang td8 = new KeHoachSL_BanHang();
		td8.setYear(yearSLBan);
		td8.setKenh("Tổng Bột giặt");
		td8.setKenhDT("Tổng Bột giặt PB");
		td8.setKenhLever(2);
		td8.setThuockenh("PHÍA BẮC");
		keHoachSL_BanHangs.add(td8);

		KeHoachSL_BanHang td9 = new KeHoachSL_BanHang();
		td9.setYear(yearSLBan);
		td9.setKenh("LIX (GT+MT)");
		td9.setKenhDT("LIX (GT+MT) B PB");
		td9.setKenhLever(3);
		td9.setThuockenh("Tổng Bột giặt PB");
		keHoachSL_BanHangs.add(td9);

		KeHoachSL_BanHang td10 = new KeHoachSL_BanHang();
		td10.setYear(yearSLBan);
		td10.setKenh("GT");
		td10.setKenhDT("GT B PB");
		td10.setKenhLever(4);
		td10.setThuockenh("LIX (GT+MT)");
		td10.setThuockenh("LIX (GT+MT) B PB");
		keHoachSL_BanHangs.add(td10);

		KeHoachSL_BanHang td11 = new KeHoachSL_BanHang();
		td11.setYear(yearSLBan);
		td11.setKenh("MT");
		td11.setKenhDT("MT B PB");
		td11.setKenhLever(4);
		td11.setThuockenh("LIX (GT+MT) B PB");
		keHoachSL_BanHangs.add(td11);

		KeHoachSL_BanHang td12 = new KeHoachSL_BanHang();
		td12.setYear(yearSLBan);
		td12.setKenh("OB-MT");
		td12.setKenhDT("OB-MT B PB");
		td12.setKenhLever(3);
		td12.setThuockenh("Tổng Bột giặt PB");
		keHoachSL_BanHangs.add(td12);

		KeHoachSL_BanHang td13 = new KeHoachSL_BanHang();
		td13.setYear(yearSLBan);
		td13.setKenh("XK");
		td13.setKenhDT("XK B PB");
		td13.setKenhLever(3);
		td13.setThuockenh("Tổng Bột giặt PB");
		keHoachSL_BanHangs.add(td13);
	}

	private void kehoachSLBanHang_T() {
		KeHoachSL_BanHang td15_t = new KeHoachSL_BanHang();
		td15_t.setYear(yearSLBan);
		td15_t.setKenh("TỔNG CỘNG");
		td15_t.setKenhDT("TỔNG CỘNG");
		td15_t.setKenhLever(0);
		keHoachSL_BanHangs.add(td15_t);

		KeHoachSL_BanHang td16_t = new KeHoachSL_BanHang();
		td16_t.setYear(yearSLBan);
		td16_t.setKenh("LIX (GT+MT)");
		td16_t.setKenhLever(2);
		keHoachSL_BanHangs.add(td16_t);
		KeHoachSL_BanHang td17_t = new KeHoachSL_BanHang();
		td17_t.setYear(yearSLBan);
		td17_t.setKenh("GT");
		td17_t.setKenhLever(3);
		keHoachSL_BanHangs.add(td17_t);
		KeHoachSL_BanHang td18_t = new KeHoachSL_BanHang();
		td18_t.setYear(yearSLBan);
		td18_t.setKenh("MT");
		td18_t.setKenhLever(3);
		keHoachSL_BanHangs.add(td18_t);
		KeHoachSL_BanHang td19_t = new KeHoachSL_BanHang();
		td19_t.setYear(yearSLBan);
		td19_t.setKenh("OB-MT");
		td19_t.setKenhLever(2);
		keHoachSL_BanHangs.add(td19_t);
		KeHoachSL_BanHang td20_t = new KeHoachSL_BanHang();
		td20_t.setYear(yearSLBan);
		td20_t.setKenh("XK");
		td20_t.setKenhLever(2);
		keHoachSL_BanHangs.add(td20_t);
	}

	public void ajax_kehoachSLBanHang(int cot, KeHoachSL_BanHang itemprama) {
		KeHoachSL_BanHang[] itemprama_Kenhmoi = new KeHoachSL_BanHang[1];
		// System.out.println("itemprama.getThuockenh(): "+itemprama.getThuockenh());
		double total = keHoachSL_BanHangs.stream()
				.filter(item -> item.getThuockenh() != null && item.getThuockenh().equals(itemprama.getThuockenh()))
				.mapToDouble(item -> {
					switch (cot) {
					case 1:
						return item.getSl_1();
					case 2:
						return item.getSl_2();
					case 3:
						return item.getSl_3();
					case 4:
						return item.getSl_4();
					case 5:
						return item.getSl_5();
					case 6:
						return item.getSl_6();
					case 7:
						return item.getSl_7();
					case 8:
						return item.getSl_8();
					case 9:
						return item.getSl_9();
					case 10:
						return item.getSl_10();
					case 11:
						return item.getSl_11();
					case 12:
						return item.getSl_12();
					default:
						return 0;
					}
				}).sum();
		keHoachSL_BanHangs.forEach(item -> {
			if (item.getKenhDT() != null && item.getKenhDT().equals(itemprama.getThuockenh())) {
				switch (cot) {
				case 1:
					item.setSl_1(total);
					break;
				case 2:
					item.setSl_2(total);
					break;
				case 3:
					item.setSl_3(total);
					break;
				case 4:
					item.setSl_4(total);
					break;
				case 5:
					item.setSl_5(total);
					break;
				case 6:
					item.setSl_6(total);
					break;
				case 7:
					item.setSl_7(total);
					break;
				case 8:
					item.setSl_8(total);
					break;
				case 9:
					item.setSl_9(total);
					break;
				case 10:
					item.setSl_10(total);
					break;
				case 11:
					item.setSl_11(total);
					break;
				case 12:
					item.setSl_12(total);
					break;

				}
				itemprama_Kenhmoi[0] = item;
			}
		});
		/*
		 * Cài đặt tổng cộng
		 */
		KeHoachSL_BanHang finalItemprama_Kenhmoi = itemprama_Kenhmoi[0];
		if (finalItemprama_Kenhmoi != null && finalItemprama_Kenhmoi.getKenhLever() != 0) {
			ajax_kehoachSLBanHang(cot, finalItemprama_Kenhmoi);
		} else {

			keHoachSL_BanHangs.forEach(item -> {
				// System.out.println("Cot: "+cot);
					switch (cot) {
					case 1:
						double totalGT1 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_1).sum();
						double totalMT1 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_1).sum();
						double totalOB_MT1 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_1).sum();
						double totalXK1 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_1).sum();

						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_1(totalGT1);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_1(totalMT1);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_1(totalGT1 + totalMT1);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_1(totalOB_MT1);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_1(totalXK1);
						}
						break;
					case 2:
						double totalGT2 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_2).sum();
						double totalMT2 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_2).sum();
						double totalOB_MT2 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_2).sum();
						double totalXK2 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_2).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_2(totalGT2);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_2(totalMT2);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_2(totalGT2 + totalMT2);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_2(totalOB_MT2);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_2(totalXK2);
						}
						break;
					case 3:
						double totalGT3 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_3).sum();
						double totalMT3 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_3).sum();
						double totalOB_MT3 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_3).sum();
						double totalXK3 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_3).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_3(totalGT3);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_3(totalMT3);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_3(totalGT3 + totalMT3);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_3(totalOB_MT3);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_3(totalXK3);
						}
						break;
					case 4:
						double totalGT4 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_4).sum();
						double totalMT4 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_4).sum();
						double totalOB_MT4 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_4).sum();
						double totalXK4 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_4).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_4(totalGT4);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_4(totalMT4);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_4(totalGT4 + totalMT4);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_4(totalOB_MT4);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_4(totalXK4);
						}
						break;

					case 5:
						double totalGT5 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_5).sum();
						double totalMT5 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_5).sum();
						double totalOB_MT5 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_5).sum();
						double totalXK5 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_5).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_5(totalGT5);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_5(totalMT5);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_5(totalGT5 + totalMT5);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_5(totalOB_MT5);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_5(totalXK5);
						}
						break;
					case 6:
						double totalGT6 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_6).sum();
						double totalMT6 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_6).sum();
						double totalOB_MT6 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_6).sum();
						double totalXK6 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_6).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_6(totalGT6);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_6(totalMT6);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_6(totalGT6 + totalMT6);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_6(totalOB_MT6);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_6(totalXK6);
						}
						break;
					case 7:
						double totalGT7 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_7).sum();
						double totalMT7 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_7).sum();
						double totalOB_MT7 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_7).sum();
						double totalXK7 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_7).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_7(totalGT7);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_7(totalMT7);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_7(totalGT7 + totalMT7);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_7(totalOB_MT7);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_7(totalXK7);
						}
						break;
					case 8:
						double totalGT8 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_8).sum();
						double totalMT8 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_8).sum();
						double totalOB_MT8 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_8).sum();
						double totalXK8 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_8).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_8(totalGT8);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_8(totalMT8);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_8(totalGT8 + totalMT8);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_8(totalOB_MT8);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_8(totalXK8);
						}
						break;
					case 9:
						double totalGT9 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_9).sum();
						double totalMT9 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_9).sum();
						double totalOB_MT9 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_9).sum();
						double totalXK9 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_9).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_9(totalGT9);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_9(totalMT9);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_9(totalGT9 + totalMT9);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_9(totalOB_MT9);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_9(totalXK9);
						}
						break;
					case 10:
						double totalGT10 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_10).sum();
						double totalMT10 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_10).sum();
						double totalOB_MT10 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_10).sum();
						double totalXK10 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_10).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_10(totalGT10);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_10(totalMT10);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_10(totalGT10 + totalMT10);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_10(totalOB_MT10);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_10(totalXK10);
						}
						break;
					case 11:
						double totalGT11 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_11).sum();
						double totalMT11 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_11).sum();
						double totalOB_MT11 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_11).sum();
						double totalXK11 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_11).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_11(totalGT11);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_11(totalMT11);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_11(totalGT11 + totalMT11);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_11(totalOB_MT11);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_11(totalXK11);
						}
						break;
					case 12:
						double totalGT12 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_12).sum();
						double totalMT12 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_12).sum();
						double totalOB_MT12 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_12).sum();
						double totalXK12 = keHoachSL_BanHangs.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_BanHang::getSl_12).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_12(totalGT12);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_12(totalMT12);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_12(totalGT12 + totalMT12);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_12(totalOB_MT12);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_12(totalXK12);
						}
						break;
					}

				});
		}
	}

	public void saveOrUpdate_KHSLBanHang() {
		keHoachSL_BanHangService.saveOrUpdateKeHoachSL_BanHang(keHoachSL_BanHangs);
		success();
	}

	@Getter
	@Setter
	int yearSLSanXuat;
	@Getter
	@Setter
	List<KeHoachSL_SanXuat> keHoachSL_SanXuats;
	@Inject
	KeHoachSL_SanXuatService keHoachSL_SanXuatService;

	public void kehoachSLSanXuat() {
		keHoachSL_SanXuats = keHoachSL_SanXuatService.search(yearSLSanXuat);
		if (keHoachSL_SanXuats.size() == 0) {
			kehoachSLSanXuat_N();
			kehoachSLSanXuat_B();
			kehoachSLSanXuat_T();

		}

		updateform("idfdukienslsanxuat");
		showDialog("dldukienslsanxuat");
	}

	private void kehoachSLSanXuat_N() {
		KeHoachSL_SanXuat td1 = new KeHoachSL_SanXuat();
		td1.setYear(yearSLBan);
		td1.setKenh("PHÍA NAM");
		td1.setKenhDT("PHÍA NAM");
		td1.setThuockenh("TỔNG CỘNG");
		td1.setKenhLever(1);
		keHoachSL_SanXuats.add(td1);

		KeHoachSL_SanXuat td2 = new KeHoachSL_SanXuat();
		td2.setYear(yearSLBan);
		td2.setKenh("Tổng CTRL");
		td2.setKenhDT("Tổng CTRL PN");
		td2.setKenhLever(2);
		td2.setThuockenh("PHÍA NAM");
		keHoachSL_SanXuats.add(td2);

		KeHoachSL_SanXuat td3 = new KeHoachSL_SanXuat();
		td3.setYear(yearSLBan);
		td3.setKenh("LIX (GT+MT)");
		td3.setKenhDT("LIX (GT+MT) N PN");
		td3.setKenhLever(3);
		td3.setThuockenh("Tổng CTRL PN");
		keHoachSL_SanXuats.add(td3);

		KeHoachSL_SanXuat td4 = new KeHoachSL_SanXuat();
		td4.setYear(yearSLBan);
		td4.setKenh("GT");
		td4.setKenhDT("GT N PN");
		td4.setKenhLever(4);
		td4.setThuockenh("LIX (GT+MT) N PN");
		keHoachSL_SanXuats.add(td4);

		KeHoachSL_SanXuat td5 = new KeHoachSL_SanXuat();
		td5.setYear(yearSLBan);
		td5.setKenh("MT");
		td5.setKenhDT("MT N PN");
		td5.setKenhLever(4);
		td5.setThuockenh("LIX (GT+MT) N PN");
		keHoachSL_SanXuats.add(td5);

		KeHoachSL_SanXuat td6 = new KeHoachSL_SanXuat();
		td6.setYear(yearSLBan);
		td6.setKenh("OB-MT");
		td6.setKenhDT("OB-MT N PN");
		td6.setKenhLever(3);
		td6.setThuockenh("Tổng CTRL PN");
		keHoachSL_SanXuats.add(td6);

		KeHoachSL_SanXuat td7 = new KeHoachSL_SanXuat();
		td7.setYear(yearSLBan);
		td7.setKenh("XK");
		td7.setKenhDT("XK N PN");
		td7.setKenhLever(3);
		td7.setThuockenh("Tổng CTRL PN");
		keHoachSL_SanXuats.add(td7);

		KeHoachSL_SanXuat td8 = new KeHoachSL_SanXuat();
		td8.setYear(yearSLBan);
		td8.setKenh("Tổng Bột giặt");
		td8.setKenhDT("Tổng Bột giặt PN");
		td8.setKenhLever(2);
		td8.setThuockenh("PHÍA NAM");
		keHoachSL_SanXuats.add(td8);

		KeHoachSL_SanXuat td9 = new KeHoachSL_SanXuat();
		td9.setYear(yearSLBan);
		td9.setKenh("LIX (GT+MT)");
		td9.setKenhDT("LIX (GT+MT) B PN");
		td9.setKenhLever(3);
		td9.setThuockenh("Tổng Bột giặt PN");
		keHoachSL_SanXuats.add(td9);

		KeHoachSL_SanXuat td10 = new KeHoachSL_SanXuat();
		td10.setYear(yearSLBan);
		td10.setKenh("GT");
		td10.setKenhDT("GT B PN");
		td10.setKenhLever(4);
		td10.setThuockenh("LIX (GT+MT)");
		td10.setThuockenh("LIX (GT+MT) B PN");
		keHoachSL_SanXuats.add(td10);

		KeHoachSL_SanXuat td11 = new KeHoachSL_SanXuat();
		td11.setYear(yearSLBan);
		td11.setKenh("MT");
		td11.setKenhDT("MT B PN");
		td11.setKenhLever(4);
		td11.setThuockenh("LIX (GT+MT) B PN");
		keHoachSL_SanXuats.add(td11);

		KeHoachSL_SanXuat td12 = new KeHoachSL_SanXuat();
		td12.setYear(yearSLBan);
		td12.setKenh("OB-MT");
		td12.setKenhDT("OB-MT B PN");
		td12.setKenhLever(3);
		td12.setThuockenh("Tổng Bột giặt PN");
		keHoachSL_SanXuats.add(td12);

		KeHoachSL_SanXuat td13 = new KeHoachSL_SanXuat();
		td13.setYear(yearSLBan);
		td13.setKenh("XK");
		td13.setKenhDT("XK B PN");
		td13.setKenhLever(3);
		td13.setThuockenh("Tổng Bột giặt PN");
		keHoachSL_SanXuats.add(td13);
	}

	private void kehoachSLSanXuat_B() {
		KeHoachSL_SanXuat td1 = new KeHoachSL_SanXuat();
		td1.setYear(yearSLBan);
		td1.setKenh("PHÍA BẮC");
		td1.setKenhDT("PHÍA BẮC");
		td1.setKenhLever(1);
		td1.setThuockenh("TỔNG CỘNG");
		keHoachSL_SanXuats.add(td1);

		KeHoachSL_SanXuat td2 = new KeHoachSL_SanXuat();
		td2.setYear(yearSLBan);
		td2.setKenh("Tổng CTRL");
		td2.setKenhDT("Tổng CTRL PB");
		td2.setKenhLever(2);
		td2.setThuockenh("PHÍA BẮC");
		keHoachSL_SanXuats.add(td2);

		KeHoachSL_SanXuat td3 = new KeHoachSL_SanXuat();
		td3.setYear(yearSLBan);
		td3.setKenh("LIX (GT+MT)");
		td3.setKenhDT("LIX (GT+MT) N PB");
		td3.setKenhLever(3);
		td3.setThuockenh("Tổng CTRL PB");
		keHoachSL_SanXuats.add(td3);

		KeHoachSL_SanXuat td4 = new KeHoachSL_SanXuat();
		td4.setYear(yearSLBan);
		td4.setKenh("GT");
		td4.setKenhDT("GT N PB");
		td4.setKenhLever(4);
		td4.setThuockenh("LIX (GT+MT) N PB");
		keHoachSL_SanXuats.add(td4);

		KeHoachSL_SanXuat td5 = new KeHoachSL_SanXuat();
		td5.setYear(yearSLBan);
		td5.setKenh("MT");
		td5.setKenhDT("MT N PB");
		td5.setKenhLever(4);
		td5.setThuockenh("LIX (GT+MT) N PB");
		keHoachSL_SanXuats.add(td5);

		KeHoachSL_SanXuat td6 = new KeHoachSL_SanXuat();
		td6.setYear(yearSLBan);
		td6.setKenh("OB-MT");
		td6.setKenhDT("OB-MT N PB");
		td6.setKenhLever(3);
		td6.setThuockenh("Tổng CTRL PB");
		keHoachSL_SanXuats.add(td6);

		KeHoachSL_SanXuat td7 = new KeHoachSL_SanXuat();
		td7.setYear(yearSLBan);
		td7.setKenh("XK");
		td7.setKenhDT("XK N PB");
		td7.setKenhLever(3);
		td7.setThuockenh("Tổng CTRL PB");
		keHoachSL_SanXuats.add(td7);

		KeHoachSL_SanXuat td8 = new KeHoachSL_SanXuat();
		td8.setYear(yearSLBan);
		td8.setKenh("Tổng Bột giặt");
		td8.setKenhDT("Tổng Bột giặt PB");
		td8.setKenhLever(2);
		td8.setThuockenh("PHÍA BẮC");
		keHoachSL_SanXuats.add(td8);

		KeHoachSL_SanXuat td9 = new KeHoachSL_SanXuat();
		td9.setYear(yearSLBan);
		td9.setKenh("LIX (GT+MT)");
		td9.setKenhDT("LIX (GT+MT) B PB");
		td9.setKenhLever(3);
		td9.setThuockenh("Tổng Bột giặt PB");
		keHoachSL_SanXuats.add(td9);

		KeHoachSL_SanXuat td10 = new KeHoachSL_SanXuat();
		td10.setYear(yearSLBan);
		td10.setKenh("GT");
		td10.setKenhDT("GT B PB");
		td10.setKenhLever(4);
		td10.setThuockenh("LIX (GT+MT)");
		td10.setThuockenh("LIX (GT+MT) B PB");
		keHoachSL_SanXuats.add(td10);

		KeHoachSL_SanXuat td11 = new KeHoachSL_SanXuat();
		td11.setYear(yearSLBan);
		td11.setKenh("MT");
		td11.setKenhDT("MT B PB");
		td11.setKenhLever(4);
		td11.setThuockenh("LIX (GT+MT) B PB");
		keHoachSL_SanXuats.add(td11);

		KeHoachSL_SanXuat td12 = new KeHoachSL_SanXuat();
		td12.setYear(yearSLBan);
		td12.setKenh("OB-MT");
		td12.setKenhDT("OB-MT B PB");
		td12.setKenhLever(3);
		td12.setThuockenh("Tổng Bột giặt PB");
		keHoachSL_SanXuats.add(td12);

		KeHoachSL_SanXuat td13 = new KeHoachSL_SanXuat();
		td13.setYear(yearSLBan);
		td13.setKenh("XK");
		td13.setKenhDT("XK B PB");
		td13.setKenhLever(3);
		td13.setThuockenh("Tổng Bột giặt PB");
		keHoachSL_SanXuats.add(td13);
	}

	private void kehoachSLSanXuat_T() {
		KeHoachSL_SanXuat td15_t = new KeHoachSL_SanXuat();
		td15_t.setYear(yearSLBan);
		td15_t.setKenh("TỔNG CỘNG");
		td15_t.setKenhDT("TỔNG CỘNG");
		td15_t.setKenhLever(0);
		keHoachSL_SanXuats.add(td15_t);

		KeHoachSL_SanXuat td16_t = new KeHoachSL_SanXuat();
		td16_t.setYear(yearSLBan);
		td16_t.setKenh("LIX (GT+MT)");
		td16_t.setKenhLever(2);
		keHoachSL_SanXuats.add(td16_t);

		KeHoachSL_SanXuat td17_t = new KeHoachSL_SanXuat();
		td17_t.setYear(yearSLBan);
		td17_t.setKenh("GT");
		td17_t.setKenhLever(3);
		keHoachSL_SanXuats.add(td17_t);

		KeHoachSL_SanXuat td18_t = new KeHoachSL_SanXuat();
		td18_t.setYear(yearSLBan);
		td18_t.setKenh("MT");
		td18_t.setKenhLever(3);
		keHoachSL_SanXuats.add(td18_t);

		KeHoachSL_SanXuat td19_t = new KeHoachSL_SanXuat();
		td19_t.setYear(yearSLBan);
		td19_t.setKenh("OB-MT");
		td19_t.setKenhLever(2);
		keHoachSL_SanXuats.add(td19_t);

		KeHoachSL_SanXuat td20_t = new KeHoachSL_SanXuat();
		td20_t.setYear(yearSLBan);
		td20_t.setKenh("XK");
		td20_t.setKenhLever(2);
		keHoachSL_SanXuats.add(td20_t);
	}

	public void ajax_kehoachSLSanXuat(int cot, KeHoachSL_SanXuat itemprama) {
		KeHoachSL_SanXuat[] itemprama_Kenhmoi = new KeHoachSL_SanXuat[1];
		double total = keHoachSL_SanXuats.stream()
				.filter(item -> item.getThuockenh() != null && item.getThuockenh().equals(itemprama.getThuockenh()))
				.mapToDouble(item -> {
					switch (cot) {
					case 1:
						return item.getSl_1();
					case 2:
						return item.getSl_2();
					case 3:
						return item.getSl_3();
					case 4:
						return item.getSl_4();
					case 5:
						return item.getSl_5();
					case 6:
						return item.getSl_6();
					case 7:
						return item.getSl_7();
					case 8:
						return item.getSl_8();
					case 9:
						return item.getSl_9();
					case 10:
						return item.getSl_10();
					case 11:
						return item.getSl_11();
					case 12:
						return item.getSl_12();
					default:
						return 0;
					}
				}).sum();
		keHoachSL_SanXuats.forEach(item -> {
			if (item.getKenhDT() != null && item.getKenhDT().equals(itemprama.getThuockenh())) {
				switch (cot) {
				case 1:
					item.setSl_1(total);
					break;
				case 2:
					item.setSl_2(total);
					break;
				case 3:
					item.setSl_3(total);
					break;
				case 4:
					item.setSl_4(total);
					break;
				case 5:
					item.setSl_5(total);
					break;
				case 6:
					item.setSl_6(total);
					break;
				case 7:
					item.setSl_7(total);
					break;
				case 8:
					item.setSl_8(total);
					break;
				case 9:
					item.setSl_9(total);
					break;
				case 10:
					item.setSl_10(total);
					break;
				case 11:
					item.setSl_11(total);
					break;
				case 12:
					item.setSl_12(total);
					break;

				}
				itemprama_Kenhmoi[0] = item;
			}
		});
		/*
		 * Cài đặt tổng cộng
		 */
		KeHoachSL_SanXuat finalItemprama_Kenhmoi = itemprama_Kenhmoi[0];
		if (finalItemprama_Kenhmoi != null && finalItemprama_Kenhmoi.getKenhLever() != 0) {
			ajax_kehoachSLSanXuat(cot, finalItemprama_Kenhmoi);
		} else {

			keHoachSL_SanXuats.forEach(item -> {
				// System.out.println("Cot: "+cot);
					switch (cot) {
					case 1:
						double totalGT1 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_1).sum();
						double totalMT1 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_1).sum();
						double totalOB_MT1 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_1).sum();
						double totalXK1 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_1).sum();

						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_1(totalGT1);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_1(totalMT1);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_1(totalGT1 + totalMT1);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_1(totalOB_MT1);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_1(totalXK1);
						}
						break;
					case 2:
						double totalGT2 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_2).sum();
						double totalMT2 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_2).sum();
						double totalOB_MT2 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_2).sum();
						double totalXK2 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_2).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_2(totalGT2);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_2(totalMT2);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_2(totalGT2 + totalMT2);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_2(totalOB_MT2);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_2(totalXK2);
						}
						break;
					case 3:
						double totalGT3 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_3).sum();
						double totalMT3 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_3).sum();
						double totalOB_MT3 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_3).sum();
						double totalXK3 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_3).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_3(totalGT3);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_3(totalMT3);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_3(totalGT3 + totalMT3);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_3(totalOB_MT3);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_3(totalXK3);
						}
						break;
					case 4:
						double totalGT4 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_4).sum();
						double totalMT4 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_4).sum();
						double totalOB_MT4 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_4).sum();
						double totalXK4 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_4).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_4(totalGT4);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_4(totalMT4);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_4(totalGT4 + totalMT4);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_4(totalOB_MT4);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_4(totalXK4);
						}
						break;

					case 5:
						double totalGT5 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_5).sum();
						double totalMT5 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_5).sum();
						double totalOB_MT5 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_5).sum();
						double totalXK5 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_5).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_5(totalGT5);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_5(totalMT5);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_5(totalGT5 + totalMT5);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_5(totalOB_MT5);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_5(totalXK5);
						}
						break;
					case 6:
						double totalGT6 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_6).sum();
						double totalMT6 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_6).sum();
						double totalOB_MT6 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_6).sum();
						double totalXK6 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_6).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_6(totalGT6);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_6(totalMT6);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_6(totalGT6 + totalMT6);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_6(totalOB_MT6);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_6(totalXK6);
						}
						break;
					case 7:
						double totalGT7 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_7).sum();
						double totalMT7 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_7).sum();
						double totalOB_MT7 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_7).sum();
						double totalXK7 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_7).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_7(totalGT7);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_7(totalMT7);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_7(totalGT7 + totalMT7);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_7(totalOB_MT7);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_7(totalXK7);
						}
						break;
					case 8:
						double totalGT8 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_8).sum();
						double totalMT8 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_8).sum();
						double totalOB_MT8 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_8).sum();
						double totalXK8 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_8).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_8(totalGT8);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_8(totalMT8);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_8(totalGT8 + totalMT8);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_8(totalOB_MT8);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_8(totalXK8);
						}
						break;
					case 9:
						double totalGT9 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_9).sum();
						double totalMT9 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_9).sum();
						double totalOB_MT9 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_9).sum();
						double totalXK9 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_9).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_9(totalGT9);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_9(totalMT9);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_9(totalGT9 + totalMT9);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_9(totalOB_MT9);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_9(totalXK9);
						}
						break;
					case 10:
						double totalGT10 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_10).sum();
						double totalMT10 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_10).sum();
						double totalOB_MT10 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_10).sum();
						double totalXK10 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_10).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_10(totalGT10);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_10(totalMT10);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_10(totalGT10 + totalMT10);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_10(totalOB_MT10);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_10(totalXK10);
						}
						break;
					case 11:
						double totalGT11 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_11).sum();
						double totalMT11 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_11).sum();
						double totalOB_MT11 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_11).sum();
						double totalXK11 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_11).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_11(totalGT11);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_11(totalMT11);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_11(totalGT11 + totalMT11);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_11(totalOB_MT11);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_11(totalXK11);
						}
						break;
					case 12:
						double totalGT12 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("GT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_12).sum();
						double totalMT12 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_12).sum();
						double totalOB_MT12 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("OB-MT") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_12).sum();
						double totalXK12 = keHoachSL_SanXuats.stream()
								.filter(itemsub -> itemsub.getKenh().equals("XK") && itemsub.getKenhDT() != null)
								.mapToDouble(KeHoachSL_SanXuat::getSl_12).sum();
						if (item.getKenhDT() == null && item.getKenh().equals("GT")) {
							item.setSl_12(totalGT12);
						} else if (item.getKenhDT() == null && item.getKenh().equals("MT")) {
							item.setSl_12(totalMT12);
						} else if (item.getKenhDT() == null && item.getKenh().equals("LIX (GT+MT)")) {
							item.setSl_12(totalGT12 + totalMT12);
						} else if (item.getKenhDT() == null && item.getKenh().equals("OB-MT")) {
							item.setSl_12(totalOB_MT12);
						} else if (item.getKenhDT() == null && item.getKenh().equals("XK")) {
							item.setSl_12(totalXK12);
						}
						break;
					}

				});
		}
	}

	public void saveOrUpdate_KHSLSanXuat() {
		keHoachSL_SanXuatService.saveOrUpdateKeHoachSL_SanXuat(keHoachSL_SanXuats);
		success();
	}

	public void saveOrUpdate() {
		if (allowSave(null) && allowUpdate(null, null)) {
			String data = gson.toJson(theoDoiSLBH_SX_Datas);
			theoDoiSLBH_SX.setTheoDoiSLBH_SX_Data(data);
			theoDoiSLBH_SX.setSLdate(ngay);
			theoDoiSLBH_SXService.saveOrUpdate(theoDoiSLBH_SX);
			success();
		} else {
			warning("Tài khoản này không có quyền thực hiện");
		}
	}

	@Getter
	@Setter
	Date ngayCopy;

	public void copy() {
		if (ngayCopy == null) {
			warning("Chưa nhập ngày copy");
			return;
		} else {
			TheoDoiSLBH_SX td = theoDoiSLBH_SXService.search(ngayCopy);
			if (td != null) {
				warning("Đã có dữ liệu ngày " + MyUtil.chuyensangddMMyyStr(ngayCopy));
				return;
			} else {
				theoDoiSLBH_SX.setId(0);
				theoDoiSLBH_SX.setSLdate(ngayCopy);
				theoDoiSLBH_SXService.saveOrUpdate(theoDoiSLBH_SX);
				success();
			}
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
