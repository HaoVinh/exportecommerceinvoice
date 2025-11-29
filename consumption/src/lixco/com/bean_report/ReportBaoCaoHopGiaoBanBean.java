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
import java.util.Objects;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.BaoCaoHopGiaoBan;
import lixco.com.reportInfo.TheKhoSanPham;
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

import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

import com.google.gson.JsonObject;

@Named
@ViewScoped
public class ReportBaoCaoHopGiaoBanBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IReportService reportService;
	@Getter
	@Setter
	private Date fromDateSearch;
	@Getter
	@Setter
	private Date toDateSearch;

	@Override
	protected void initItem() {
		try {
			int month = ToolTimeCustomer.getMonthCurrent();
			int year = ToolTimeCustomer.getYearCurrent();
			fromDateSearch = ToolTimeCustomer.getDateMinCustomer(month, year);
			toDateSearch = ToolTimeCustomer.getDateMaxCustomer(month, year);
		} catch (Exception e) {
			logger.error("ReportBaoCaoHopGiaoBanBean.initItem:" + e.getMessage(), e);
		}
	}

	public void reportBaoCaoHopGiaoBan() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			List<BaoCaoHopGiaoBan> list = new ArrayList<>();
			reportService.reportBaoCaoHopGiaoBan(JsonParserUtil.getGson().toJson(json), list);
			if (list.size() > 0) {
				List<BaoCaoHopGiaoBan> listdl = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					if ("B".equals(list.get(i).getManhom())) {
						list.get(i).setTennhom("BỘT GIẶT");
					} else {
						list.get(i).setManhom("L");
						list.get(i).setTennhom("CHẤT TẨY RỬA DẠNG LỎNG");
					}

					if ("B".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("NHẬP SẢN XUẤT (GC)");
					} else if ("S".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("NHẬP SẢN XUẤT");
					}
					if ("X".equals(list.get(i).getNhapxuat())) {
						if (!"D".equals(list.get(i).getMaxuatnhap()) && !"T".equals(list.get(i).getMaxuatnhap())
								&& !"8".equals(list.get(i).getMaxuatnhap())) {
							list.get(i).setMaxuatnhap("I");
						}
					}
					if ("T".equals(list.get(i).getMaxuatnhap()) || "8".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("XUẤT KHẨU TRỰC TIẾP");
					} else if ("D".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("XUẤT TRẢ HÀNG GIA CÔNG");
					} else if ("I".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("BÁN NỘI ĐỊA");
					} else {
						// System.out.println(list.get(i).getNhapxuat() + "-" +
						// list.get(i).getMaxuatnhap() + "-"
						// + list.get(i).getLoaixuatnhap());
					}
					if (list.get(i).getSoluong() == 0)
						listdl.add(list.get(i));

				}
				list.removeAll(listdl);
				List<BaoCaoHopGiaoBan> results = new ArrayList<BaoCaoHopGiaoBan>();
				Map<String, List<BaoCaoHopGiaoBan>> listGroup = list.stream().collect(
						Collectors.groupingBy(BaoCaoHopGiaoBan::getNhapxuat, Collectors.toList()));
				for (String key : listGroup.keySet()) {
					List<BaoCaoHopGiaoBan> invs = listGroup.get(key);

					Map<String, List<BaoCaoHopGiaoBan>> listGroup0 = invs.stream().collect(
							Collectors.groupingBy(BaoCaoHopGiaoBan::getManhom, Collectors.toList()));
					for (String key0 : listGroup0.keySet()) {
						List<BaoCaoHopGiaoBan> invs0 = listGroup0.get(key0);

						Map<String, List<BaoCaoHopGiaoBan>> listGroup1 = invs0.stream().collect(
								Collectors.groupingBy(BaoCaoHopGiaoBan::getLoaixuatnhap, Collectors.toList()));
						for (String key1 : listGroup1.keySet()) {
							List<BaoCaoHopGiaoBan> invs1 = listGroup1.get(key1);
							double soluong = 0;
							for (int i = 0; i < invs1.size(); i++) {
								soluong += invs1.get(i).getSoluong();
							}
							if (invs1.size() != 0) {
								BaoCaoHopGiaoBan bc = new BaoCaoHopGiaoBan();
								bc.setNhapxuat(key);
								bc.setManhom(key0);
								bc.setTennhom(invs1.get(0).getTennhom());
								bc.setMaxuatnhap(invs1.get(0).getMaxuatnhap());
								bc.setLoaixuatnhap(key1);
								bc.setSoluong(soluong);
								bc.setDoanhthu(invs1.get(0).getDoanhthu());
								results.add(bc);
							}
						}
					}
				}

				Collections.sort(results, new Comparator<BaoCaoHopGiaoBan>() {
					public int compare(BaoCaoHopGiaoBan o1, BaoCaoHopGiaoBan o2) {
						int i = o1.getTennhom().compareTo(o2.getTennhom());
						if (i == 0)
							i = o1.getNhapxuat().compareTo(o2.getNhapxuat());
						if (i == 0)
							i = o1.getLoaixuatnhap().compareTo(o2.getLoaixuatnhap());
						return i;
					}
				});

				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/hopgiaoban/bao_cao_hop_giao_ban.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
				importParam.put(
						"logo",
						FacesContext.getCurrentInstance().getExternalContext()
								.getRealPath("/resources/gfx/lixco_logo.png"));
				StringBuilder title = new StringBuilder();
				title.append("TỔNG HỢP NHẬP XUẤT SẢN PHẨM\n");
				if (fromDateSearch != null) {
					title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
				}
				if (toDateSearch != null) {
					title.append(" ĐẾN NGÀY " + ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
				}
				importParam.put("title", title.toString());
				JRDataSource beanDataSource = new JRBeanCollectionDataSource(results);
				JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, beanDataSource);
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
				current.executeScript("PF('showImg').show();");
				current.executeScript("utility.showPDFK('" + ba + "','')");
			} else {
				notify.warning("Không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error("ReportBaoCaoHopGiaoBanBean.reportBaoCaoTonKhoLever:" + e.getMessage(), e);
		}
	}
	
	public void reportBaoCaoHopGiaoBanExcel() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			List<BaoCaoHopGiaoBan> list = new ArrayList<>();
			reportService.reportBaoCaoHopGiaoBan(JsonParserUtil.getGson().toJson(json), list);
			if (list.size() > 0) {
				List<BaoCaoHopGiaoBan> listdl = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					if ("B".equals(list.get(i).getManhom())) {
						list.get(i).setTennhom("BỘT GIẶT");
					} else {
						list.get(i).setManhom("L");
						list.get(i).setTennhom("CHẤT TẨY RỬA DẠNG LỎNG");
					}

					if ("B".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("NHẬP SẢN XUẤT (GC)");
					} else if ("S".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("NHẬP SẢN XUẤT");
					}
					if ("X".equals(list.get(i).getNhapxuat())) {
						if (!"D".equals(list.get(i).getMaxuatnhap()) && !"T".equals(list.get(i).getMaxuatnhap())
								&& !"8".equals(list.get(i).getMaxuatnhap())) {
							list.get(i).setMaxuatnhap("I");
						}
					}
					if ("T".equals(list.get(i).getMaxuatnhap()) || "8".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("XUẤT KHẨU TRỰC TIẾP");
					} else if ("D".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("XUẤT TRẢ HÀNG GIA CÔNG");
					} else if ("I".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("BÁN NỘI ĐỊA");
					} else {
						// System.out.println(list.get(i).getNhapxuat() + "-" +
						// list.get(i).getMaxuatnhap() + "-"
						// + list.get(i).getLoaixuatnhap());
					}
					if (list.get(i).getSoluong() == 0)
						listdl.add(list.get(i));

				}
				list.removeAll(listdl);
				List<BaoCaoHopGiaoBan> results = new ArrayList<BaoCaoHopGiaoBan>();
				Map<String, List<BaoCaoHopGiaoBan>> listGroup = list.stream().collect(
						Collectors.groupingBy(BaoCaoHopGiaoBan::getNhapxuat, Collectors.toList()));
				for (String key : listGroup.keySet()) {
					List<BaoCaoHopGiaoBan> invs = listGroup.get(key);

					Map<String, List<BaoCaoHopGiaoBan>> listGroup0 = invs.stream().collect(
							Collectors.groupingBy(BaoCaoHopGiaoBan::getManhom, Collectors.toList()));
					for (String key0 : listGroup0.keySet()) {
						List<BaoCaoHopGiaoBan> invs0 = listGroup0.get(key0);

						Map<String, List<BaoCaoHopGiaoBan>> listGroup1 = invs0.stream().collect(
								Collectors.groupingBy(BaoCaoHopGiaoBan::getLoaixuatnhap, Collectors.toList()));
						for (String key1 : listGroup1.keySet()) {
							List<BaoCaoHopGiaoBan> invs1 = listGroup1.get(key1);
							double soluong = 0;
							for (int i = 0; i < invs1.size(); i++) {
								soluong += invs1.get(i).getSoluong();
							}
							if (invs1.size() != 0) {
								BaoCaoHopGiaoBan bc = new BaoCaoHopGiaoBan();
								bc.setNhapxuat(key);
								bc.setManhom(key0);
								bc.setTennhom(invs1.get(0).getTennhom());
								bc.setMaxuatnhap(invs1.get(0).getMaxuatnhap());
								bc.setLoaixuatnhap(key1);
								bc.setSoluong(soluong);
								bc.setDoanhthu(invs1.get(0).getDoanhthu());
								results.add(bc);
							}
						}
					}
				}

				Collections.sort(results, new Comparator<BaoCaoHopGiaoBan>() {
					public int compare(BaoCaoHopGiaoBan o1, BaoCaoHopGiaoBan o2) {
						int i = o1.getTennhom().compareTo(o2.getTennhom());
						if (i == 0)
							i = o1.getNhapxuat().compareTo(o2.getNhapxuat());
						if (i == 0)
							i = o1.getLoaixuatnhap().compareTo(o2.getLoaixuatnhap());
						return i;
					}
				});
				List<Object[]> resultsExcel = new ArrayList<Object[]>();
				Object[] title = { "XN", "NHOM SP", "TEN NHOM ", "MA XN", "LOAI XN", "SO LUONG" };
				resultsExcel.add(title);

				for (BaoCaoHopGiaoBan p : results) {
					Object[] row = { p.getNhapxuat(), p.getManhom(),p.getTennhom(),p.getMaxuatnhap(),p.getLoaixuatnhap(),
							p.getSoluong()};
					resultsExcel.add(row);
				}
				StringBuilder title2 = new StringBuilder();
				title2.append("xuatnhapsanphamTH_");
				if (fromDateSearch != null) {
					title2.append(ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
				}
				if (toDateSearch != null) {
					title2.append("_" + ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
				}
				ToolReport.printReportExcelRaw(resultsExcel, title2.toString());
			} else {
				notify.warning("Không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error("ReportTheKhoSanPhamBean.reportTheKhoSanPhamExcel:" + e.getMessage(), e);
		}
	}
	
	public void reportBaoCaoHopGiaoBanChiTiet() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			List<BaoCaoHopGiaoBan> list = new ArrayList<>();
			reportService.reportBaoCaoHopGiaoBan(JsonParserUtil.getGson().toJson(json), list);
			if (list.size() > 0) {
				List<BaoCaoHopGiaoBan> listdl = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					

					if ("B".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("NHẬP SẢN XUẤT (GC)");
					} else if ("S".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("NHẬP SẢN XUẤT");
					}
					if ("X".equals(list.get(i).getNhapxuat())) {
						if (!"D".equals(list.get(i).getMaxuatnhap()) && !"T".equals(list.get(i).getMaxuatnhap())
								&& !"8".equals(list.get(i).getMaxuatnhap())) {
							list.get(i).setMaxuatnhap("I");
						}
					}
					if ("T".equals(list.get(i).getMaxuatnhap()) || "8".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("XUẤT KHẨU TRỰC TIẾP");
					} else if ("D".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("XUẤT TRẢ HÀNG GIA CÔNG");
					} else if ("I".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("BÁN NỘI ĐỊA");
					} else {
						// System.out.println(list.get(i).getNhapxuat() + "-" +
						// list.get(i).getMaxuatnhap() + "-"
						// + list.get(i).getLoaixuatnhap());
					}
					if (list.get(i).getSoluong() == 0)
						listdl.add(list.get(i));

				}
				list.removeAll(listdl);
				List<BaoCaoHopGiaoBan> results = new ArrayList<BaoCaoHopGiaoBan>();
				Map<String, List<BaoCaoHopGiaoBan>> listGroup = list.stream().collect(
						Collectors.groupingBy(BaoCaoHopGiaoBan::getNhapxuat, Collectors.toList()));
				for (String key : listGroup.keySet()) {
					List<BaoCaoHopGiaoBan> invs = listGroup.get(key);

					Map<String, List<BaoCaoHopGiaoBan>> listGroup0 = invs.stream().collect(
							Collectors.groupingBy(BaoCaoHopGiaoBan::getManhom, Collectors.toList()));
					for (String key0 : listGroup0.keySet()) {
						List<BaoCaoHopGiaoBan> invs0 = listGroup0.get(key0);

						Map<String, List<BaoCaoHopGiaoBan>> listGroup1 = invs0.stream().collect(
								Collectors.groupingBy(BaoCaoHopGiaoBan::getLoaixuatnhap, Collectors.toList()));
						for (String key1 : listGroup1.keySet()) {
							List<BaoCaoHopGiaoBan> invs1 = listGroup1.get(key1);
							double soluong = 0;
							for (int i = 0; i < invs1.size(); i++) {
								soluong += invs1.get(i).getSoluong();
							}
							if (invs1.size() != 0) {
								BaoCaoHopGiaoBan bc = new BaoCaoHopGiaoBan();
								bc.setNhapxuat(key);
								bc.setManhom(key0);
								bc.setTennhom(invs1.get(0).getTennhom());
								bc.setMaxuatnhap(invs1.get(0).getMaxuatnhap());
								bc.setLoaixuatnhap(key1);
								bc.setSoluong(soluong);
								bc.setDoanhthu(invs1.get(0).getDoanhthu());
								results.add(bc);
							}
						}
					}
				}

				Collections.sort(results, new Comparator<BaoCaoHopGiaoBan>() {
					public int compare(BaoCaoHopGiaoBan o1, BaoCaoHopGiaoBan o2) {
						int i = o1.getTennhom().compareTo(o2.getTennhom());
						if (i == 0)
							i = o1.getNhapxuat().compareTo(o2.getNhapxuat());
						if (i == 0)
							i = o1.getLoaixuatnhap().compareTo(o2.getLoaixuatnhap());
						return i;
					}
				});

				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/hopgiaoban/bao_cao_hop_giao_ban.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
				importParam.put(
						"logo",
						FacesContext.getCurrentInstance().getExternalContext()
								.getRealPath("/resources/gfx/lixco_logo.png"));
				StringBuilder title = new StringBuilder();
				title.append("TỔNG HỢP NHẬP XUẤT SẢN PHẨM \n");
				if (fromDateSearch != null) {
					title.append("TỪ NGÀY " + ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
				}
				if (toDateSearch != null) {
					title.append(" ĐẾN NGÀY " + ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
				}
				importParam.put("title", title.toString());
				JRDataSource beanDataSource = new JRBeanCollectionDataSource(results);
				JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, beanDataSource);
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
				current.executeScript("PF('showImg').show();");
				current.executeScript("utility.showPDFK('" + ba + "','')");
			} else {
				notify.warning("Không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error("ReportBaoCaoHopGiaoBanBean.reportBaoCaoTonKhoLever:" + e.getMessage(), e);
		}
	}
	public void reportBaoCaoHopGiaoBanChiTietExcel() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			List<BaoCaoHopGiaoBan> list = new ArrayList<>();
			reportService.reportBaoCaoHopGiaoBan(JsonParserUtil.getGson().toJson(json), list);
			if (list.size() > 0) {
				List<BaoCaoHopGiaoBan> listdl = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					

					if ("B".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("NHẬP SẢN XUẤT (GC)");
					} else if ("S".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("NHẬP SẢN XUẤT");
					}
					if ("X".equals(list.get(i).getNhapxuat())) {
						if (!"D".equals(list.get(i).getMaxuatnhap()) && !"T".equals(list.get(i).getMaxuatnhap())
								&& !"8".equals(list.get(i).getMaxuatnhap())) {
							list.get(i).setMaxuatnhap("I");
						}
					}
					if ("T".equals(list.get(i).getMaxuatnhap()) || "8".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("XUẤT KHẨU TRỰC TIẾP");
					} else if ("D".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("XUẤT TRẢ HÀNG GIA CÔNG");
					} else if ("I".equals(list.get(i).getMaxuatnhap())) {
						list.get(i).setLoaixuatnhap("BÁN NỘI ĐỊA");
					} else {
						// System.out.println(list.get(i).getNhapxuat() + "-" +
						// list.get(i).getMaxuatnhap() + "-"
						// + list.get(i).getLoaixuatnhap());
					}
					if (list.get(i).getSoluong() == 0)
						listdl.add(list.get(i));

				}
				list.removeAll(listdl);
				List<BaoCaoHopGiaoBan> results = new ArrayList<BaoCaoHopGiaoBan>();
				Map<String, List<BaoCaoHopGiaoBan>> listGroup = list.stream().collect(
						Collectors.groupingBy(BaoCaoHopGiaoBan::getNhapxuat, Collectors.toList()));
				for (String key : listGroup.keySet()) {
					List<BaoCaoHopGiaoBan> invs = listGroup.get(key);

					Map<String, List<BaoCaoHopGiaoBan>> listGroup0 = invs.stream().collect(
							Collectors.groupingBy(BaoCaoHopGiaoBan::getManhom, Collectors.toList()));
					for (String key0 : listGroup0.keySet()) {
						List<BaoCaoHopGiaoBan> invs0 = listGroup0.get(key0);

						Map<String, List<BaoCaoHopGiaoBan>> listGroup1 = invs0.stream().collect(
								Collectors.groupingBy(BaoCaoHopGiaoBan::getLoaixuatnhap, Collectors.toList()));
						for (String key1 : listGroup1.keySet()) {
							List<BaoCaoHopGiaoBan> invs1 = listGroup1.get(key1);
							double soluong = 0;
							for (int i = 0; i < invs1.size(); i++) {
								soluong += invs1.get(i).getSoluong();
							}
							if (invs1.size() != 0) {
								BaoCaoHopGiaoBan bc = new BaoCaoHopGiaoBan();
								bc.setNhapxuat(key);
								bc.setManhom(key0);
								bc.setTennhom(invs1.get(0).getTennhom());
								bc.setMaxuatnhap(invs1.get(0).getMaxuatnhap());
								bc.setLoaixuatnhap(key1);
								bc.setSoluong(soluong);
								bc.setDoanhthu(invs1.get(0).getDoanhthu());
								results.add(bc);
							}
						}
					}
				}

				Collections.sort(results, new Comparator<BaoCaoHopGiaoBan>() {
					public int compare(BaoCaoHopGiaoBan o1, BaoCaoHopGiaoBan o2) {
						int i = o1.getTennhom().compareTo(o2.getTennhom());
						if (i == 0)
							i = o1.getNhapxuat().compareTo(o2.getNhapxuat());
						if (i == 0)
							i = o1.getLoaixuatnhap().compareTo(o2.getLoaixuatnhap());
						return i;
					}
				});
				List<Object[]> resultsExcel = new ArrayList<Object[]>();
				Object[] title = { "XN", "NHOM SP", "TEN NHOM ", "MA XN", "LOAI XN", "SO LUONG" };
				resultsExcel.add(title);

				for (BaoCaoHopGiaoBan p : results) {
					Object[] row = { p.getNhapxuat(), p.getManhom(),p.getTennhom(),p.getMaxuatnhap(),p.getLoaixuatnhap(),
							p.getSoluong()};
					resultsExcel.add(row);
				}
				StringBuilder title2 = new StringBuilder();
				title2.append("xuatnhapsanphamCT_");
				if (fromDateSearch != null) {
					title2.append(ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
				}
				if (toDateSearch != null) {
					title2.append("_" + ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
				}
				ToolReport.printReportExcelRaw(resultsExcel, title2.toString());
			} else {
				notify.warning("Không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error("ReportTheKhoSanPhamBean.reportTheKhoSanPhamExcel:" + e.getMessage(), e);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
