package lixco.com.bean_report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import lixco.com.commom_ejb.MyMath;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Product;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.TheKhoSanPham;
import lixco.com.reportInfo.TonKhoDauThang;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.ParamReportDetail;
import trong.lixco.com.service.ParamReportDetailService;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

import com.google.gson.JsonObject;

@Named
@ViewScoped
public class ReportTheKhoSanPhamBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IProductService productService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private IReportService reportService;
	private int fromMonthSearch;
	private int toMonthSearch;
	private int yearSearch;
	private ProductType productTypeSearch;
	private List<ProductType> listProductType;
	private Product productSearch;

	@Override
	protected void initItem() {
		try {
			listProductType = new ArrayList<>();
			productTypeService.selectAll(listProductType);
			fromMonthSearch = 1;
			toMonthSearch = ToolTimeCustomer.getMonthCurrent();
			yearSearch = ToolTimeCustomer.getYearCurrent();
		} catch (Exception e) {
			logger.error("ReportTheKhoSanPhamBean.initItem:" + e.getMessage(), e);
		}
	}

	public List<Product> completeProduct(String text) {
		try {
			List<Product> list = new ArrayList<>();
			productService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("ReportTheKhoSanPhamBean.completeProduct:" + e.getMessage(), e);
		}
		return null;
	}

	@Inject
	ParamReportDetailService paramReportDetailService;

	public void reportTheKhoSanPham() {
		PrimeFaces current = PrimeFaces.current();
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			/* {from_month:0,to_month:0,year:0,product_id:0,product_type_id:0} */
			JsonObject json = new JsonObject();
			json.addProperty("from_month", fromMonthSearch);
			json.addProperty("to_month", toMonthSearch);
			json.addProperty("year", yearSearch);
			json.addProperty("product_id", productSearch == null ? 0 : productSearch.getId());
			json.addProperty("product_type_id", productTypeSearch == null ? 0 : productTypeSearch.getId());
			List<TheKhoSanPham> list = new ArrayList<>();
			reportService.reportTheKhoSanPham(JsonParserUtil.getGson().toJson(json), list);
			
			if (list.size() > 0) {

//				for (int i = 0; i < list.size(); i++) {
//
//				}

				// add row missing
				// add row missing
				List<TheKhoSanPham> listFinal = new ArrayList<TheKhoSanPham>();
				// group by product id
				Map<Long, List<TheKhoSanPham>> mapGroupByProductId = list.stream().collect(
						Collectors.groupingBy(TheKhoSanPham::getProduct_id, Collectors.toList()));
				for (Entry<Long, List<TheKhoSanPham>> entry : mapGroupByProductId.entrySet()) {
					Long key = entry.getKey();
					List<TheKhoSanPham> listGroupByProductId = entry.getValue();
					// group by month
					Map<Integer, List<TheKhoSanPham>> mapGroupByMonth = listGroupByProductId.stream().collect(
							Collectors.groupingBy(TheKhoSanPham::getData_month, Collectors.toList()));
					double nhapCongDonTuThang1 = 0;
					double xuatCongDonTuThang1 = 0;
					for (Entry<Integer, List<TheKhoSanPham>> entry1 : mapGroupByMonth.entrySet()) {
						Integer key1 = entry1.getKey();
						List<TheKhoSanPham> listGroupByMonth = entry1.getValue();
						int thangtondau=key1;
						int namtondau=yearSearch;
						if(thangtondau==1){
							thangtondau=12;
							namtondau-=1;
						}else{
							thangtondau-=1;
						}
						TonKhoDauThang tk = reportService.reportTonKhoDauThang(thangtondau, namtondau, listGroupByMonth
								.get(0).getProduct_id());
						
						double nhapPhatSinhTrongThang = 0;
						double xuatPhatSinhTrongThang = 0;
						List<TheKhoSanPham> deletes = new ArrayList<TheKhoSanPham>();
						for (int i = 0; i < listGroupByMonth.size(); i++) {
							nhapPhatSinhTrongThang = BigDecimal.valueOf(nhapPhatSinhTrongThang)
									.add(BigDecimal.valueOf(listGroupByMonth.get(i).getKg_import_quantity()))
									.doubleValue();
							xuatPhatSinhTrongThang = BigDecimal.valueOf(xuatPhatSinhTrongThang)
									.add(BigDecimal.valueOf(listGroupByMonth.get(i).getKg_export_quantity()))
									.doubleValue();
							listGroupByMonth.get(i).setKg_ivn_quantityFirst(tk.getKg_inv_balance());

							int typeie = listGroupByMonth.get(i).getTypeie();
							if (typeie == 3) {
								deletes.add(listGroupByMonth.get(i));
							}

							// System.out.println(typeie+"\t"+list.get(i).getCustomer_code()
							// + "-" + list.get(i).getCustomer_name() + "\t"
							// + list.get(i).getKg_import_quantity() + "\t" +
							// list.get(i).getKg_export_quantity() + "\t"
							// + list.get(i).getKg_ivn_quantity());
							if (i == 0) {
								listGroupByMonth.get(i).setKg_ivn_quantity(
										MyMath.roundCustom(listGroupByMonth.get(i).getKg_ivn_quantityFirst()
												+ listGroupByMonth.get(i).getKg_import_quantity()
												- listGroupByMonth.get(i).getKg_export_quantity(), 2));
							} else if (i > 0) {
								listGroupByMonth.get(i).setKg_ivn_quantity(
										MyMath.roundCustom(listGroupByMonth.get(i - 1).getKg_ivn_quantity()
												+ listGroupByMonth.get(i).getKg_import_quantity()
												- listGroupByMonth.get(i).getKg_export_quantity(), 2));
							}
							// Kiểm tra tồn thẻ kho và tồn kho (file tồn) để
							// cộng trừ vào nhập xuất =>Tồn thẻ kho = tồn kho
							// (file tồn)
							// if (i + 1 == listGroupByMonth.size() && tk !=
							// null) {
							// double tonconlai = tk.getKg_inv_balance()
							// - listGroupByMonth.get(i).getKg_ivn_quantity();
							//
							// if (Math.abs(tonconlai) > 0
							// && Math.abs(tonconlai) <
							// listGroupByMonth.get(i).getFactor()) {
							// if
							// (listGroupByMonth.get(i).getKg_export_quantity()
							// > 0) {
							// listGroupByMonth.get(i).setKg_export_quantity(
							// listGroupByMonth.get(i).getKg_export_quantity() -
							// tonconlai);
							// } else {
							// listGroupByMonth.get(i).setKg_import_quantity(
							// listGroupByMonth.get(i).getKg_import_quantity() +
							// tonconlai);
							// }
							// listGroupByMonth.get(i).setKg_ivn_quantity(tk.getKg_inv_balance());
							// }
							// }

							// listGroupByMonth.get(i).setKg_ivn_quantityFirst(
							// listGroupByMonth.get(0).getKg_ivn_quantity());
						}
						listGroupByMonth.removeAll(deletes);
						if (listGroupByMonth.size() != 0) {
							List<TheKhoSanPham> removes = new ArrayList<TheKhoSanPham>();
							for (int i = 0; i < listGroupByMonth.size(); i++) {
								if ("".equals(listGroupByMonth.get(i).getCustomer_name())
										&& listGroupByMonth.get(i).getKg_import_quantity() == 0
										&& listGroupByMonth.get(i).getKg_export_quantity() == 0) {
									removes.add(listGroupByMonth.get(i));
								}
							}
							listGroupByMonth.removeAll(removes);// ton dau thang
							
						}

						nhapCongDonTuThang1 = BigDecimal.valueOf(nhapCongDonTuThang1)
								.add(BigDecimal.valueOf(nhapPhatSinhTrongThang)).doubleValue();
						xuatCongDonTuThang1 = BigDecimal.valueOf(xuatCongDonTuThang1)
								.add(BigDecimal.valueOf(xuatPhatSinhTrongThang)).doubleValue();
						listFinal.addAll(listGroupByMonth);
					}
				}
				//
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/the_kho_san_pham/the_kho_san_pham.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
				List<ParamReportDetail> listConfig = paramReportDetailService.findByParamReportName("thongtinchung");
				for (ParamReportDetail p : listConfig) {
					importParam.put(p.getKey(), p.getValue());
				}
				importParam.put(
						"logo",
						FacesContext.getCurrentInstance().getExternalContext()
								.getRealPath("/resources/gfx/lixco_logo.png"));
				importParam.put("startMonth", fromMonthSearch);
				StringBuilder title = new StringBuilder();
				title.append("THẺ KHO SẢN PHẨM TỪ THÁNG " + String.format("%02d", fromMonthSearch) + "/"
						+ String.format("%04d", yearSearch));
				title.append(" ĐẾN THÁNG " + String.format("%02d", toMonthSearch) + "/"
						+ String.format("%04d", yearSearch));
				importParam.put("title", title.toString());
				JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
						new JRBeanCollectionDataSource(listFinal));
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
				current.executeScript("PF('showImg').show();");
				current.executeScript("utility.showPDFK('" + ba + "','')");
			} else {
				notify.warning("Không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error("ReportTheKhoSanPhamBean.reportTheKhoSanPham:" + e.getMessage(), e);
		}
	}

	public void reportTheKhoSanPhamExcel() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			/* {from_month:0,to_month:0,year:0,product_id:0,product_type_id:0} */
			JsonObject json = new JsonObject();
			json.addProperty("from_month", fromMonthSearch);
			json.addProperty("to_month", toMonthSearch);
			json.addProperty("year", yearSearch);
			json.addProperty("product_id", productSearch == null ? 0 : productSearch.getId());
			json.addProperty("product_type_id", productTypeSearch == null ? 0 : productTypeSearch.getId());
			List<TheKhoSanPham> list = new ArrayList<>();
			reportService.reportTheKhoSanPham(JsonParserUtil.getGson().toJson(json), list);
			if (list.size() > 0) {
				// add row missing
				// add row missing
				List<TheKhoSanPham> listFinal = new ArrayList<TheKhoSanPham>();
				// group by product id
				Map<Long, List<TheKhoSanPham>> mapGroupByProductId = list.stream().collect(
						Collectors.groupingBy(TheKhoSanPham::getProduct_id, Collectors.toList()));
				for (Entry<Long, List<TheKhoSanPham>> entry : mapGroupByProductId.entrySet()) {
					Long key = entry.getKey();
					List<TheKhoSanPham> listGroupByProductId = entry.getValue();
					// group by month
					Map<Integer, List<TheKhoSanPham>> mapGroupByMonth = listGroupByProductId.stream().collect(
							Collectors.groupingBy(TheKhoSanPham::getData_month, Collectors.toList()));
					double nhapCongDonTuThang1 = 0;
					double xuatCongDonTuThang1 = 0;
					for (Entry<Integer, List<TheKhoSanPham>> entry1 : mapGroupByMonth.entrySet()) {
						Integer key1 = entry1.getKey();
						int thangtondau=key1;
						int namtondau=yearSearch;
						if(thangtondau==1){
							thangtondau=12;
							namtondau-=1;
						}else{
							thangtondau-=1;
						}
						List<TheKhoSanPham> listGroupByMonth = entry1.getValue();
						TonKhoDauThang tk = reportService.reportTonKhoDauThang(thangtondau, namtondau, listGroupByMonth
								.get(0).getProduct_id());
						TheKhoSanPham thkho=new TheKhoSanPham();
						thkho.setProduct_code("");
						thkho.setProduct_name("TỒN ĐẦU THÁNG "+key1);
						thkho.setKg_export_quantity(0.0);
						thkho.setKg_import_quantity(0.0);
						thkho.setKg_ivn_quantity(tk.getKg_inv_balance());
						thkho.setKg_ivn_quantityFirst(0.0);
						thkho.setKg_ivn_quantityLast(0.0);
						thkho.setKg_ivn_quantityLastNow(0.0);
						listGroupByMonth.add(0,thkho);
						double nhapPhatSinhTrongThang = 0;
						double xuatPhatSinhTrongThang = 0;
						List<TheKhoSanPham> deletes = new ArrayList<TheKhoSanPham>();
						for (int i = 0; i < listGroupByMonth.size(); i++) {
							nhapPhatSinhTrongThang = BigDecimal.valueOf(nhapPhatSinhTrongThang)
									.add(BigDecimal.valueOf(listGroupByMonth.get(i).getKg_import_quantity()))
									.doubleValue();
							xuatPhatSinhTrongThang = BigDecimal.valueOf(xuatPhatSinhTrongThang)
									.add(BigDecimal.valueOf(listGroupByMonth.get(i).getKg_export_quantity()))
									.doubleValue();
							listGroupByMonth.get(i).setKg_ivn_quantityFirst(tk.getKg_inv_balance());

							int typeie = listGroupByMonth.get(i).getTypeie();
							if (typeie == 3) {
								deletes.add(listGroupByMonth.get(i));
							}
							if (i > 0) {
								listGroupByMonth.get(i).setKg_ivn_quantity(
										MyMath.roundCustom(listGroupByMonth.get(i - 1).getKg_ivn_quantity()
												+ listGroupByMonth.get(i).getKg_import_quantity()
												- listGroupByMonth.get(i).getKg_export_quantity(), 2));
							}
							if (i + 1 == listGroupByMonth.size() && tk != null) {
								double tonconlai = tk.getKg_inv_balance()
										- listGroupByMonth.get(i).getKg_ivn_quantity();
								if (Math.abs(tonconlai) > 0
										&& Math.abs(tonconlai) < listGroupByMonth.get(i).getFactor()) {
									listGroupByMonth.get(i).setKg_export_quantity(
											listGroupByMonth.get(i).getKg_export_quantity() - tonconlai);
									listGroupByMonth.get(i).setKg_ivn_quantity(tk.getKg_inv_balance());
								}
							}

						}
						if (listGroupByMonth.size() != 0) {
//							 listGroupByMonth.remove(0);// ton dau thang
//							listGroupByMonth.get(0).setProduct_code("");
//							listGroupByMonth.get(0).setProduct_name("TỒN ĐẦU THÁNG");
							listGroupByMonth.removeAll(deletes);
						}

						nhapCongDonTuThang1 = BigDecimal.valueOf(nhapCongDonTuThang1)
								.add(BigDecimal.valueOf(nhapPhatSinhTrongThang)).doubleValue();
						xuatCongDonTuThang1 = BigDecimal.valueOf(xuatCongDonTuThang1)
								.add(BigDecimal.valueOf(xuatPhatSinhTrongThang)).doubleValue();
						listFinal.addAll(listGroupByMonth);
					}
				}

				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "MÃ SP", "TÊN SẢN PHẨM", "NGÀY ", "SỐ PHIẾU", "DIỄN GIẢI", "MÃ LÔ HÀNG", "NHẬP",
						"XUẤT", "TỒN", "NGƯỜI VC", "PO" };
				results.add(title);

				for (int i = 0; i < listFinal.size(); i++) {

					TheKhoSanPham p = listFinal.get(i);
					Object[] row = {
							p.getProduct_code(),
							p.getProduct_name(),
							Objects.toString(ToolTimeCustomer.convertDateToString(p.getData_date(), "dd/MM/yyyy"), ""),
							Objects.toString(p.getData_code(), ""),
							Objects.toString(p.getCustomer_name(), ""),
							Objects.toString(p.getBatch_code(), ""),
							MyMath.roundCustom(p.getKg_import_quantity(), 2),
							MyMath.roundCustom(p.getKg_export_quantity(), 2),
							i == 0 ? MyMath.roundCustom(p.getKg_ivn_quantityFirst(), 2) : MyMath.roundCustom(
									p.getKg_ivn_quantity(), 2), Objects.toString(p.getCarrierName(), ""),
							Objects.toString(p.getPoNo(), "") };
					results.add(row);
				}
				StringBuilder title2 = new StringBuilder();
				title2.append("the_kho_san_pham_");
				if (yearSearch != 0) {
					if (fromMonthSearch != 0)
						title2.append("từ tháng_" + String.format("%02d", fromMonthSearch) + "/"
								+ String.format("%04d", yearSearch));
					if (toMonthSearch != 0)
						title2.append("_đến_tháng_" + String.format("%02d", toMonthSearch) + "/"
								+ String.format("%04d", yearSearch));
				}
				ToolReport.printReportExcelRaw(results, title2.toString());
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

	public int getFromMonthSearch() {
		return fromMonthSearch;
	}

	public void setFromMonthSearch(int fromMonthSearch) {
		this.fromMonthSearch = fromMonthSearch;
	}

	public int getToMonthSearch() {
		return toMonthSearch;
	}

	public void setToMonthSearch(int toMonthSearch) {
		this.toMonthSearch = toMonthSearch;
	}

	public int getYearSearch() {
		return yearSearch;
	}

	public void setYearSearch(int yearSearch) {
		this.yearSearch = yearSearch;
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

	public Product getProductSearch() {
		return productSearch;
	}

	public void setProductSearch(Product productSearch) {
		this.productSearch = productSearch;
	}
}
