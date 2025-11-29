package lixco.com.bean;

import java.io.IOException;
import java.io.InputStream;
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
import javax.servlet.http.HttpServletRequest;

import lixco.com.commom_ejb.MyMath;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.SessionHelper;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Car;
import lixco.com.entity.Customer;
import lixco.com.entity.GoodsImportBreak;
import lixco.com.entity.GoodsImportBreakDetail;
import lixco.com.entity.GoodsReceiptNoteDetail;
import lixco.com.entity.Product;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IProductService;
import lixco.com.reportInfo.PhieuXuatKho;
import lixco.com.reqInfo.Message;
import lixco.com.service.GoodsImportBreakDetailService;
import lixco.com.service.GoodsImportBreakService;
import lixco.com.service.GoodsImportBreakTHService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;

import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.ParamReportDetail;
import trong.lixco.com.service.AccountDatabaseService;
import trong.lixco.com.service.ParamReportDetailService;
import trong.lixco.com.util.ConvertNumberToText;
import trong.lixco.com.util.MyStringUtil;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

import com.google.gson.JsonObject;

@Named
@ViewScoped
public class GoodsImportBreakBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private GoodsImportBreakService goodsImportBreakService;
	@Inject
	private GoodsImportBreakDetailService goodsImportBreakDetailService;
	@Inject
	private IProductService productService;
	@Inject
	private ICustomerService customerService;
	@Inject
	ParamReportDetailService paramReportDetailService;
	@Inject
	private AccountDatabaseService accountDatabaseService;
	@Getter
	@Setter
	private GoodsImportBreak goodsImportBreakCrud;
	@Getter
	@Setter
	private GoodsImportBreak goodsImportBreakSelect;
	@Getter
	@Setter
	private List<GoodsImportBreak> goodsImportBreaks;
	@Getter
	@Setter
	private GoodsImportBreakDetail goodsImportBreakDetailCrud;
	@Getter
	@Setter
	private List<GoodsImportBreakDetail> goodsImportBreakDetails;

	/* search phiếu nhập */
	@Getter
	@Setter
	private Date fromDateSearch;
	@Getter
	@Setter
	private Date toDateSearch;
	@Getter
	@Setter
	private Customer customerSearch;
	@Getter
	@Setter
	private FormatHandler formatHandler;
	@Getter
	@Setter
	private Account account;
	/* Thông tin search */
	@Getter
	@Setter
	private Product productSearch;
	@Getter
	@Setter
	private String batchCodeSearch;

	@Override
	protected void initItem() {
		try {
			formatHandler = FormatHandler.getInstance();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);
			createNew();
			createNewDetail();
			fromDateSearch = ToolTimeCustomer.plusMonthNow(-2);
			search();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addGoodsImportBreakDetail() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (goodsImportBreakCrud != null && goodsImportBreakDetailCrud != null) {
				if (allowSave(goodsImportBreakCrud.getImportDate())) {
					if (goodsImportBreakDetailCrud.getProduct() != null) {
						goodsImportBreakDetailCrud.setMinv(goodsImportBreakDetails.size() + 1);
						goodsImportBreakDetailCrud.setQuantity(1);
						goodsImportBreakDetailCrud.setCreated_by(account.getMember().getName());
						goodsImportBreakDetailCrud.setCreated_date(new Date());
						goodsImportBreakDetails.add(goodsImportBreakDetailCrud.clone());
					} else {
						if (goodsImportBreakDetailCrud.getProduct() == null) {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Chưa nhập sản phẩm để thêm vào!','warning',2000);");
						}
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
				}
				goodsImportBreakDetailCrud = new GoodsImportBreakDetail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			goodsImportBreakDetailCrud = new GoodsImportBreakDetail();
			logger.error("GoogsReceiptNoteBean.addGoodsImportBreakDetail:" + e.getMessage(), e);
		}
		current.executeScript("PF('tablect').clearFilters();");
	}

	public void ajaxt_total(GoodsImportBreakDetail item) {
		try {
			int index = 0;
			if (item.getId() == 0) {
				for (int i = 0; i < goodsImportBreakDetails.size(); i++) {
					if (goodsImportBreakDetails.get(i).getMinv() == item.getMinv()) {
						index = i;
						break;
					}
				}
			} else {
				index = goodsImportBreakDetails.indexOf(item);
			}
			if (index != -1)
				goodsImportBreakDetails.get(index).setTotal(
						goodsImportBreakDetails.get(index).getQuantity()
								* goodsImportBreakDetails.get(index).getPrice());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showDialogTongHop() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('dlgth').show();");
		} catch (Exception e) {
			logger.error("GoogsReceiptNoteBean.showDialogTongHop:" + e.getMessage(), e);
		}
	}

	public void deleteGoodsImportBreakDetail(GoodsImportBreakDetail f) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			if (f.getGoodsImportBreak() == null) {
				if (f.getId() == 0) {
					int index = -1;
					for (int i = 0; i < goodsImportBreakDetails.size(); i++) {
						if (goodsImportBreakDetails.get(i).getMinv() == f.getMinv()) {
							index = i;
							break;
						}
					}
					if (index != -1) {
						goodsImportBreakDetails.remove(index);
						notify.success();
					}
				}
			} else {
				if (allowDelete(f.getGoodsImportBreak().getImportDate())) {
					current.executeScript("PF('tablect').clearFilters();");

					int code = goodsImportBreakDetailService.deleteById(f.getId());
					if (code > 0) {
						notify.success();
						goodsImportBreakDetails.remove(f);
					} else {
						error("Lỗi khi xóa (kiểm tra log server).");
					}

				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Getter
	@Setter
	private String stextStr;

	public void search() {
		PrimeFaces current = PrimeFaces.current();
		try {
			goodsImportBreaks = new ArrayList<>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("stextStr", MyStringUtil.replaceD(stextStr));
			jsonInfo.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("customer_id", customerSearch == null ? 0 : customerSearch.getId());
			JsonObject json = new JsonObject();
			json.add("goodsImportBreak", jsonInfo);
			goodsImportBreakService.search(JsonParserUtil.getGson().toJson(json), goodsImportBreaks);
		} catch (Exception e) {
			e.printStackTrace();
		}
		current.executeScript("PF('tablect').clearFilters();");
	}

	public void onTabChange(TabChangeEvent event) {
		if ("Danh sách phiếu nhập".equals(event.getTab().getTitle())) {
			if (goodsImportBreaks == null || goodsImportBreaks.size() == 0) {
				search();
				PrimeFaces.current().ajax().update("menuformid:tabview1:tablesp");
			}
		}

	}

	public void createNew() {
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('tablect').clearFilters();");
			goodsImportBreakCrud = new GoodsImportBreak();
			goodsImportBreakCrud.setCreatedBy(account.getMember().getName());
			goodsImportBreakCrud.setImportDate(new Date());
			goodsImportBreakDetails = new ArrayList<>();
			goodsImportBreakDetailCrud = new GoodsImportBreakDetail();
		} catch (Exception e) {
			logger.error("GoogsReceiptNoteBean.createNew:" + e.getMessage(), e);
		}

	}

	public void createNewDetail() {
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('tablect').clearFilters();");
			goodsImportBreakDetailCrud = new GoodsImportBreakDetail();
		} catch (Exception e) {
			logger.error("GoogsReceiptNoteBean.createNewDetail:" + e.getMessage(), e);
		}
	}

	public List<Product> completeProduct(String text) {
		try {
			List<Product> list = new ArrayList<Product>();
			productService.complete(formatHandler.converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("GoogsReceiptNoteBean.completeProduct:" + e.getMessage(), e);
		}
		return null;
	}

	public List<Customer> completeCustomer(String text) {
		try {
			List<Customer> list = new ArrayList<Customer>();
			customerService.complete(formatHandler.converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("GoogsReceiptNoteBean.completeCustomer:" + e.getMessage(), e);
		}
		return null;
	}

	public void deleteGoodsImportBreak() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (allowDelete(goodsImportBreakSelect.getImportDate())) {
				current.executeScript("PF('tablect').clearFilters();");
				if (goodsImportBreakSelect != null && goodsImportBreakSelect.getId() != 0) {
					// delete detail
					int code = goodsImportBreakService.deleteById(goodsImportBreakSelect.getId());
					if (code >= 0) {
						success();
						goodsImportBreaks.remove(goodsImportBreakSelect);
						goodsImportBreakCrud = new GoodsImportBreak();
					} else {
						error("Lỗi khi xóa (kiểm tra log server).");
					}
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public long kiemtrasochungtu(Date ngay, String sochungtu, long id) {
		long socttrung = goodsImportBreakTHService.checkCode(ngay.getMonth() + 1, ngay.getYear() + 1900, sochungtu, id);
		return socttrung;
	}

	@Inject
	GoodsImportBreakTHService goodsImportBreakTHService;

	public void saveOrUpdateReceiptNote() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (goodsImportBreakCrud != null) {
				if (kiemtrasochungtu(goodsImportBreakCrud.getImportDate(), goodsImportBreakCrud.getImportCode(),
						goodsImportBreakCrud.getId()) != 0) {
					noticeError("Số chứng từ này đã tồn tại trong tháng.");
				} else {
					Message message = new Message();
					// check info
					Customer customer = goodsImportBreakCrud.getCustomer();
					Date importDate = goodsImportBreakCrud.getImportDate();
					if (goodsImportBreakCrud.getImportCode() != null
							&& !"".equals(goodsImportBreakCrud.getImportCode()) && customer != null
							&& importDate != null) {
						if (goodsImportBreakCrud.getId() == 0) {
							if (allowSave(goodsImportBreakCrud.getImportDate())) {
								int code = goodsImportBreakTHService.saveOrUpdateGoodsImportBreakService(
										goodsImportBreakCrud, goodsImportBreakDetails, message, getAccount()
												.getUserName());
								switch (code) {
								case -1:
									String m = message.getUser_message() + " \\n" + message.getInternal_message();
									current.executeScript("swaldesignclose('Xảy ra lỗi', '" + m + "','warning');");
									break;
								default:
									success();
									// tải lại
									goodsImportBreaks.add(0, goodsImportBreakCrud.clone());
									// load danh sách chi tiết
									goodsImportBreakDetails = goodsImportBreakDetailService
											.findByGoodsImportBreak(goodsImportBreakCrud.getId());

									break;
								}
							} else {
								current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
							}
						} else {
							GoodsImportBreak goodsImportBreakOld = goodsImportBreakService
									.findById(goodsImportBreakCrud.getId());
							if (goodsImportBreakOld != null && allowUpdate(goodsImportBreakOld.getImportDate())
									&& allowUpdate(goodsImportBreakCrud.getImportDate())) {
								int code = goodsImportBreakTHService.saveOrUpdateGoodsImportBreakService(
										goodsImportBreakCrud, goodsImportBreakDetails, message, getAccount()
												.getUserName());
								switch (code) {
								case -1:
									String m = message.getUser_message() + " \\n" + message.getInternal_message();
									current.executeScript("swaldesignclose('Xảy ra lỗi', '" + m + "','warning');");
									break;
								default:
									success();
									// tải lại
									goodsImportBreaks.set(goodsImportBreaks.indexOf(goodsImportBreakCrud),
											goodsImportBreakCrud.clone());
									// load danh sách chi tiết
									goodsImportBreakDetails = goodsImportBreakDetailService
											.findByGoodsImportBreak(goodsImportBreakCrud.getId());

									break;
								}
							} else {
								current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
							}
						}
					} else {
						warning("Nhập đầy đủ thông tin (*)");
					}
				}
			}
		} catch (Exception e) {
			logger.error("GoogsReceiptNoteBean.saveOrUpdateReceiptNote:" + e.getMessage(), e);
		}
		current.executeScript("PF('tablect').clearFilters();");
	}

	public void loadGoodsImportBreak() {
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('tablect').clearFilters();");
			if (goodsImportBreakSelect != null) {
				goodsImportBreakCrud = goodsImportBreakSelect.clone();
				goodsImportBreakDetails = goodsImportBreakDetailService.findByGoodsImportBreak(goodsImportBreakCrud
						.getId());
			}
		} catch (Exception e) {
			logger.error("GoogsReceiptNoteBean.loadGoodsImportBreak:" + e.getMessage(), e);
		}
	}

	public void nextOrPrev(int next) {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (goodsImportBreaks != null && goodsImportBreaks.size() > 0) {
				int index = goodsImportBreaks.indexOf(goodsImportBreakCrud);
				int size = goodsImportBreaks.size() - 1;
				if (index == -1) {
					goodsImportBreakSelect = goodsImportBreaks.get(0);
					goodsImportBreakCrud = goodsImportBreakSelect.clone();
				} else {
					switch (next) {
					case 1:
						if (index == size) {
							goodsImportBreakSelect = goodsImportBreaks.get(0);
							goodsImportBreakCrud = goodsImportBreakSelect.clone();
						} else {
							goodsImportBreakSelect = goodsImportBreaks.get(index + 1);
							goodsImportBreakCrud = goodsImportBreakSelect.clone();
						}
						break;

					default:
						if (index == 0) {
							goodsImportBreakSelect = goodsImportBreaks.get(size);
							goodsImportBreakCrud = goodsImportBreakSelect.clone();
						} else {
							goodsImportBreakSelect = goodsImportBreaks.get(index - 1);
							goodsImportBreakCrud = goodsImportBreakSelect.clone();
						}
						break;
					}
				}
				// load chi tiết phiếu nhập
				goodsImportBreakDetails = goodsImportBreakDetailService.findByGoodsImportBreak(goodsImportBreakCrud
						.getId());
			}
		} catch (Exception e) {
			logger.error("GoodsImportBreakBean.nextOrPrev:" + e.getMessage(), e);
		}
		// clear filter
		current.executeScript("PF('tablect').clearFilters();");
	}

	public void exportGoodsReceiptNew() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (goodsImportBreakCrud != null) {
				if ("N".equals(goodsImportBreakCrud.getLoaiPhieu())) {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/nhaphang/phieunhapspdoibe.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);

					List<ParamReportDetail> listConfig = paramReportDetailService
							.findByParamReportName("thongtinchung");
					for (ParamReportDetail p : listConfig) {
						importParam.put(p.getKey(), p.getValue());
					}

					importParam.put("importDate",
							ToolTimeCustomer.convertDateToString(goodsImportBreakCrud.getImportDate(), "dd/MM/yyyy"));
					importParam.put("importDateStr",
							ToolTimeCustomer.convertDateToStringNgayThangNam(goodsImportBreakCrud.getImportDate()));
					importParam.put("note", goodsImportBreakCrud.getNote());
					importParam.put("customer_name", goodsImportBreakCrud.getCustomer() != null ? goodsImportBreakCrud
							.getCustomer().getCustomer_name() : "");
					importParam.put("listDetail", goodsImportBreakDetails);
					importParam.put("importCode", goodsImportBreakCrud.getImportCode());
					importParam.put("soxe", goodsImportBreakCrud.getSoxe());
					double totalAmount = 0;
					for (GoodsImportBreakDetail i : goodsImportBreakDetails) {
						totalAmount += i.getTotal();
					}
					totalAmount = MyMath.round(totalAmount);
					importParam.put("words_total", ConvertNumberToText.docSo(totalAmount, "ĐỒNG"));
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JRBeanCollectionDataSource(goodsImportBreakDetails));
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("utility.printPDF('" + ba + "')");
				} else {
					exportPhieuXuatKho();
				}
			}
		} catch (Exception e) {
			logger.error("GoogsReceiptNoteBean.exportGoodsReceiptNew:" + e.getMessage(), e);
		}
		// clear filter
		current.executeScript("PF('tablect').clearFilters();");
	}

	private void exportPhieuXuatKho() {
		PrimeFaces current = PrimeFaces.current();
		try {
			String reportPath = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/resources/reports/invoice/phieuxuatkhodoibe.jasper");
			Map<String, Object> importParam = new HashMap<String, Object>();
			Locale locale = new Locale("vi", "VI");
			importParam.put(JRParameter.REPORT_LOCALE, locale);
			List<ParamReportDetail> listConfig = paramReportDetailService.findByParamReportName("thongtinchung");
			for (ParamReportDetail p : listConfig) {
				importParam.put(p.getKey(), p.getValue());
			}
			List<ParamReportDetail> listConfig2 = paramReportDetailService.findByParamReportName("phieuxuatkho");
			for (ParamReportDetail p : listConfig2) {
				importParam.put(p.getKey(), p.getValue());
			}
			importParam
					.put("logo",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/lixco_logo.png"));

			importParam.put("list_data", goodsImportBreakDetails);
			Date invoiceDate = goodsImportBreakCrud.getImportDate();
			importParam.put("voucher_code", goodsImportBreakCrud.getImportCode());
			int day = ToolTimeCustomer.getDayM(invoiceDate);
			int month = ToolTimeCustomer.getMonthM(invoiceDate);
			int year = ToolTimeCustomer.getYearM(invoiceDate);
			importParam.put("day", day);
			importParam.put("month", month);
			importParam.put("year", year);
			importParam.put("note", goodsImportBreakCrud.getNote());
			importParam.put("soxe", goodsImportBreakCrud.getSoxe());
			Customer customer = goodsImportBreakCrud.getCustomer();
			importParam.put("customer_name", customer != null ? customer.getCustomer_name() : "");
			// tổng thùng
			double totalQuantityExport = 0;
			double totalAmount = 0;
			for (GoodsImportBreakDetail i : goodsImportBreakDetails) {
				// số lượng và tổng tiền
				totalQuantityExport = BigDecimal.valueOf(totalQuantityExport).add(BigDecimal.valueOf(i.getQuantity()))
						.doubleValue();
				totalAmount = BigDecimal.valueOf(totalAmount).add(BigDecimal.valueOf(i.getTotal())).doubleValue();
			}
			// làm tròn
			totalAmount = MyMath.roundCustom(totalAmount, 3);
			importParam.put("total_quantity", totalQuantityExport);
			importParam.put("total_amount", totalAmount);
			importParam.put("words_total_amount", ConvertNumberToText.docSo(totalAmount, "ĐỒNG"));
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, new JREmptyDataSource());
			byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
			String ba = Base64.getEncoder().encodeToString(data);
			current.executeScript("utility.printPDF('" + ba + "')");
		} catch (Exception e) {
			logger.error("InvoiceBean.exportPhieuXuatKho:" + e.getMessage(), e);
		}
	}

	private Workbook getWorkbook(InputStream inputStream, String nameFile) throws IOException {
		Workbook workbook = null;
		if (nameFile.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else if (nameFile.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("The specified file is not Excel file");
		}

		return workbook;
	}

	public void showDialogUpload() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dlgup1').show();");
	}

	public void exportExcelTongHop() {
		try {
			List<Long> idReceipt = new ArrayList<Long>();
			int size = goodsImportBreaks.size();
			for (int i = 0; i < size; i++) {
				idReceipt.add(goodsImportBreaks.get(i).getId());
			}
			List<Object[]> datas = new ArrayList<>();
			goodsImportBreakService.selectByIdToExcel(idReceipt, datas);

			// xuat file excel.
			if (datas.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "ID", "Số CT", "Ngày", "Mã KH", "Tên KH", "X/N", "Số xe","Ghi chú", "IDCT", "Mã SP", "Tên SP",
						"Số lượng", "Số lượng (kg)", "Đơn giá", "Thành tiền", "Mã lô hàng", "Hệ số quy đổi" };
				results.add(title);
				for (Object[] it : datas) {
					try {
						results.add(it);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}

				ToolReport.printReportExcelRawXLSX(results, "phieunhapxuatdoibe");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("GoodsImportBreakBean.exportExcelTongHop:" + e.getMessage(), e);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
