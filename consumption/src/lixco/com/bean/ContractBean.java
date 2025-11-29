package lixco.com.bean;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lixco.com.commom_ejb.MyMath;
import lixco.com.commom_ejb.MyUtilEJB;
import lixco.com.commom_ejb.PagingInfo;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.NavigationInfo;
import lixco.com.common.SessionHelper;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Contract;
import lixco.com.entity.ContractDetail;
import lixco.com.entity.ContractReqInfo;
import lixco.com.entity.Currency;
import lixco.com.entity.Customer;
import lixco.com.entity.Product;
import lixco.com.entity.ProductKM;
import lixco.com.entity.ProductType;
import lixco.com.entity.VoucherPayment;
import lixco.com.interfaces.IContractService;
import lixco.com.interfaces.ICurrencyService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IInvoiceDetailService;
import lixco.com.interfaces.IInvoiceService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IVoucherPaymentService;
import lixco.com.reqInfo.ContractDetailReqInfo;
import lixco.com.reqInfo.ContractLiquidationReqInfo;
import lixco.com.reqInfo.ProcessContract;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporterParameter;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;
import org.primefaces.PrimeFaces;

import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.ParamReportDetail;
import trong.lixco.com.info.InvoiceExcelMisa;
import trong.lixco.com.info.ThanhLyHopDong;
import trong.lixco.com.service.AccountDatabaseService;
import trong.lixco.com.service.ParamReportDetailService;
import trong.lixco.com.util.ConvertNumberToText;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.NumberWordConverter;
import trong.lixco.com.util.ToolReport;

import com.google.gson.JsonObject;

@Named
@ViewScoped
public class ContractBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private IContractService contractService;
	@Inject
	private ICustomerService customerService;
	@Inject
	private Logger logger;
	@Inject
	private ICurrencyService currencyService;
	@Inject
	private IProductService productService;
	@Inject
	private IInvoiceService invoiceService;
	@Inject
	private IVoucherPaymentService voucherPaymentService;
	@Inject
	private AccountDatabaseService accountDatabaseService;
	private Contract contractCrud;
	private Contract contractSelect;
	private List<Contract> listContract;
	private ContractDetail contractDetailCrud;
	private ContractDetail contractDetailSelect;
	private List<ContractDetail> listContractDetail;
	private List<ContractDetail> listContractDetailFillter;
	private List<Currency> listCurrency;
	private List<ProcessContract> listProcessContract;
	/* search */
	private Customer customerSearch;
	private String voucherCodeSearch;
	private Date fromDateSearch;
	private Date toDateSearch;
	private int liquidatedSearch;
	private Account account;
	private FormatHandler formatHandler;
	/* foxpro */
	private Date fromDateFox;
	private Date toDateFox;

	@Override
	protected void initItem() {
		try {
			formatHandler = FormatHandler.getInstance();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);
			// init contract crud
			contractCrud = new Contract();
			contractCrud.setContract_date(new Date());
			int year = ToolTimeCustomer.getYearCurrent();
			fromDateSearch = ToolTimeCustomer.convertStringToDate("01/01/" + year, "dd/MM/yyyy");
			toDateSearch = ToolTimeCustomer.convertStringToDate("01/12/" + year, "dd/MM/yyyy");
			sDateTL = fromDateSearch;
			eDateTL = toDateSearch;
			liquidatedSearch = -1;
			search();
			listCurrency = new ArrayList<>();
			currencyService.selectAll(listCurrency);
			createNew();
		} catch (Exception e) {
			logger.error("ContractBean.initItem:" + e.getMessage(), e);
		}
	}

	public void ajax() {
		System.out.println("ajax");
	}

	public void showDialogFox() {
		PrimeFaces current = PrimeFaces.current();
		try {
			int month = ToolTimeCustomer.getMonthCurrent();
			int year = ToolTimeCustomer.getYearCurrent();
			fromDateFox = ToolTimeCustomer.getDateMinCustomer(month, year);
			toDateFox = ToolTimeCustomer.getDateMaxCustomer(month, year);
			current.executeScript("PF('dlgfox').show();");
		} catch (Exception e) {
			logger.error("ContractBean.showDialogFox:" + e.getMessage(), e);
		}
	}

	// public void saveOrUpdateApiFox() {
	// PrimeFaces current = PrimeFaces.current();
	// try {
	// JsonObject json = new JsonObject();
	// json.addProperty("from_date",
	// ToolTimeCustomer.convertDateToString(fromDateFox,
	// "dd/MM/yyyy HH:mm:ss"));
	// json.addProperty("to_date",
	// ToolTimeCustomer.convertDateToString(toDateFox, "dd/MM/yyyy HH:mm:ss"));
	// AccountDatabase accountDatabase =
	// accountDatabaseService.findByName("foxproapi");
	// try {
	// Call call =
	// ApiCallClient.getListObjectWithParam(accountDatabase.getAddressPublic(),
	// "contract",
	// "list", JsonParserUtil.getGson().toJson(json));
	// Response response = call.execute();
	// String body = response.body().string();
	// JsonObject result = JsonParserUtil.getGson().fromJson(body,
	// JsonObject.class);
	// if (response.isSuccessful() && result.get("err").getAsInt() == 0) {
	// List<lixco.com.reqfox.Contract> listResult =
	// JsonParserUtil.getGson().fromJson(
	// result.get("dt").getAsJsonObject().get("list_contract"),
	// new TypeToken<List<lixco.com.reqfox.Contract>>() {
	// }.getType());
	// if (contractService.saveOrUpdateFoxpro(listResult,
	// account.getMember().getName()) == 0) {
	// current.executeScript("swaldesigntimer('Thành công!', 'Nạp dữ liệu từ Foxpro thành công!','success',2000);");
	// } else {
	// current.executeScript("swaldesignclose2('Cảnh báo!', 'Nạp dữ liệu từ foxpro thất bại','warning');");
	// }
	// } else {
	// String mesages = result.get("msg").getAsString();
	// current.executeScript("swaldesignclose2('Cảnh báo!', '" + mesages +
	// "','warning');");
	// }
	// } catch (Exception e) {
	// current.executeScript("swaldesignclose2('Cảnh báo!', 'Không thực hiện liên kết dữ liệu foxpro','warning');");
	// }
	// } catch (Exception e) {
	// logger.error("PromotionProgramBean.saveOrUpdateApiFox:" + e.getMessage(),
	// e);
	// }
	// }

	public void liquidation() {
		try {
			if (contractCrud != null && contractCrud.getId() != 0) {
				List<Object[]> list = new ArrayList<>();
				checkProcessContract();
				// contractService.reportLiquidation(contractCrud.getId(),
				// list);
				if (listProcessContract.size() != 0) {
					List<ContractLiquidationReqInfo> listData = new ArrayList<>();
					double enTotalAmountMain = 0;
					double enTaxAmount = 0;
					double enTotalAmountWithVat = 0;
					double pcs = 0;
					double mts = 0;
					// for (Object[] p : list) {
					// // product_type_id, en_name, id, en_name, box_quantity,
					// // en_quantity, en_unit_price, total_amount, en_unit
					// enTotalAmount =
					// Double.parseDouble(Objects.toString(p[7]));
					// enTotalAmountMain += enTotalAmount;
					// // enTotalAmountWithVat = enTotalAmount;
					// String enUnit = Objects.toString(p[8]);
					// double enQuantity =
					// Double.parseDouble(Objects.toString(p[5], "0"));
					// ContractLiquidationReqInfo item = new
					// ContractLiquidationReqInfo(Long.parseLong(Objects
					// .toString(p[0], "0")), Objects.toString(p[1]),
					// Long.parseLong(Objects.toString(p[2],
					// "0")), Objects.toString(p[3]),
					// Double.parseDouble(Objects.toString(p[4], "0")),
					// enQuantity, Double.parseDouble(Objects.toString(p[6],
					// "0")), enTotalAmount, enUnit);
					// listData.add(item);
					//
					// if (enUnit.equals("MTS")) {
					// mts =
					// BigDecimal.valueOf(mts).add(BigDecimal.valueOf(enQuantity)).doubleValue();
					// } else {
					// pcs =
					// BigDecimal.valueOf(pcs).add(BigDecimal.valueOf(enQuantity)).doubleValue();
					// }
					// }
					// public ContractLiquidationReqInfo(long product_type_id,
					// String en_product_type_name, long product_id,
					// String en_product_name, Double box_quantity, double
					// en_quantity, double en_unit_price,double
					// en_total_amount,String en_unit) {
					// this.product_type_id = product_type_id;
					// this.en_product_type_name = en_product_type_name;
					// this.product_id = product_id;
					// this.en_product_name = en_product_name;
					// this.box_quantity = box_quantity;
					// this.en_quantity = en_quantity;
					// this.en_unit_price = en_unit_price;
					// this.en_total_amount=en_total_amount;
					// this.en_unit=en_unit;
					// }
					for (int i = 0; i < listProcessContract.size(); i++) {
						Product product = productService.findIdGetPType(listProcessContract.get(i).getContract_detail()
								.getProduct().getId());
						ProductType productType = product.getProduct_type();
						String enUnit = null;
						if (listProcessContract.get(i).getContract_detail().getProduct().getUnit()
								.equalsIgnoreCase("kg")) {
							enUnit = "MTS";
						} else {
							enUnit = "PCS";
						}
						ContractLiquidationReqInfo item = new ContractLiquidationReqInfo(
								productType != null ? productType.getId() : 0,
								productType != null ? productType.getEn_name() : "", listProcessContract.get(i)
										.getContract_detail().getProduct().getId(), listProcessContract.get(i)
										.getContract_detail().getProduct().getEn_name(), Math.round(listProcessContract
										.get(i).getDathuchien()
										/ listProcessContract.get(i).getContract_detail().getProduct().getFactor()
										/ listProcessContract.get(i).getContract_detail().getProduct()
												.getSpecification()), MyMath.roundCustom(listProcessContract.get(i)
										.getDathuchien()
										/ listProcessContract.get(i).getContract_detail().getProduct().getFactor(), 2),
								listProcessContract.get(i).getContract_detail().getUnit_price(), listProcessContract
										.get(i).getTiendathuchien(), enUnit);
						listData.add(item);
						if (enUnit.equals("MTS")) {
							mts += item.getEn_quantity();
							item.setEn_quantity(MyMath.roundCustom(item.getEn_quantity() / 1000, 5));
							item.setEn_unit_price(item.getEn_unit_price() * 1000);
						} else {
							pcs += item.getEn_quantity();
						}
						enTotalAmountMain += listProcessContract.get(i).getTiendathuchien();
					}

					enTotalAmountWithVat = enTotalAmountMain;
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/hopdong/thanhlyhopdong.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);
					importParam.put("voucher_code", contractCrud.getVoucher_code());
					importParam.put("contract_date",
							ToolTimeCustomer.convertDateToString(contractCrud.getContract_date(), "dd/MM/yyyy"));
					importParam.put("current_date", ToolTimeCustomer.convertDateToString(new Date(), "dd/MM/yyyy"));
					importParam.put("customer_name", contractCrud.getCustomer().getCustomer_name());
					importParam.put("en_total_amount", enTotalAmountMain);
					importParam.put("en_tax_amount", enTaxAmount);
					importParam.put("en_total_amount_with_vat", enTotalAmountWithVat);
					importParam.put("words_say", NumberWordConverter.getMoneyIntoWords(enTotalAmountWithVat));
					importParam.put("bang_chu", ConvertNumberToText.docSo(enTotalAmountWithVat, "USD"));
					importParam.put("list_data", listData);
					importParam.put("param_en_quantity", formatHandler.getNumberFormatEn(mts, 100) + "MTS \n +"
							+ formatHandler.getNumberFormat(pcs, 100) + "PCS");
					String printFileName = JasperFillManager.fillReportToFile(reportPath, importParam,
							new JRBeanCollectionDataSource(listData));

					JRDocxExporter exporter = new JRDocxExporter();
					exporter.setExporterInput(new SimpleExporterInput(printFileName));

					// Tạo tệp tin .doc mới

					FacesContext facesContext = FacesContext.getCurrentInstance();
					HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance()
							.getExternalContext().getResponse();
					String name = "thanhlyhopdong_" + contractCrud.getVoucher_code();
					httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + name + ".docx");
					ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
					exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(servletOutputStream));

					SimpleDocxReportConfiguration cf = new SimpleDocxReportConfiguration();
					cf.setFramesAsNestedTables(true);
					exporter.setConfiguration(cf);

					exporter.exportReport();
					facesContext.responseComplete();
					try {
						servletOutputStream.close();
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			logger.error("ContractBean.liquidation:" + e.getMessage(), e);
		}
	}

	Customer customerTL;
	Date sDateTL;
	Date eDateTL;
	@Inject
	IInvoiceDetailService invoiceDetailService;

	public void thanhlyhopdong2() {
		try {
			if (customerTL == null || sDateTL == null || eDateTL == null) {
				noticeError("Nhập đầy đủ thông tin khách hàng, từ ngày, đến ngày.");
			} else {

				List<Object[]> datas = invoiceDetailService.findByCustomer(customerTL, sDateTL, eDateTL);
				if (datas.size() == 0) {
					notice("Không có dữ liệu phát sinh.");
				} else {
					double tongtien = 0;
					double thue = 0;
					List<ThanhLyHopDong> thanhLyHopDongs = new ArrayList<ThanhLyHopDong>();
					for (int i = 0; i < datas.size(); i++) {
						ThanhLyHopDong tl = new ThanhLyHopDong(datas.get(i)[0].toString(), datas.get(i)[1].toString(),
								Double.parseDouble(datas.get(i)[2].toString()), Double.parseDouble(datas.get(i)[3]
										.toString()), Double.parseDouble(datas.get(i)[4].toString()),
								datas.get(i)[5].toString());
						thanhLyHopDongs.add(tl);
					}

					Object[] tongtien_thue = invoiceDetailService.findByCustomerTTThue(customerTL, sDateTL, eDateTL);
					if (tongtien_thue != null) {
						tongtien = (double) tongtien_thue[0];
						thue = (double) tongtien_thue[1];
					}

					Map<String, List<ThanhLyHopDong>> thanhlys = thanhLyHopDongs.stream().collect(
							Collectors.groupingBy(ThanhLyHopDong::getMasp, Collectors.toList()));

					FacesContext context = FacesContext.getCurrentInstance();
					// Lấy đường dẫn đầy đủ của tệp từ thư mục resources

					ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance()
							.getExternalContext().getContext();
					String path = servletContext
							.getRealPath("resources/upload/filemau/file_mau_thanh_ly_hop_dong.docx");
					InputStream input = new FileInputStream(path);
					XWPFDocument document = new XWPFDocument(input);
					//
					String pdanhsachsanpham = "productList";
					String ptongtien = "tongtienhang";
					String pvat = "thue";
					String ptongcong = "tongcong";
					String ptextTotalFinal = "tongthbangchu";
					
					String ptenkhachhang = "tenkhachhang";

					for (XWPFParagraph paragraph : document.getParagraphs()) {
						// Cài đặt khoảng cách tab
						for (int i = 0; i < paragraph.getRuns().size(); i++) {
							XWPFRun run = paragraph.getRuns().get(i);
							String text = run.getText(0);
							if (text != null && text.contains(pdanhsachsanpham)) {
								paragraph.removeRun(i);

								int m = 0;
								for (String key : thanhlys.keySet()) {
									m++;
									List<ThanhLyHopDong> thanhlysubs = thanhlys.get(key);
									XWPFRun newRun1 = paragraph.createRun();
									newRun1.addTab();
									newRun1.setFontSize(10);
									newRun1.setFontFamily("Times New Roman");
									newRun1.setText((m) + ". " + thanhlysubs.get(0).getTensp());
									newRun1.addBreak();
									newRun1.addBreak();
									for (int j = 0; j < thanhlysubs.size(); j++) {
										i++;
										XWPFRun newRun2 = paragraph.createRun();
										newRun2.setItalic(true);
										newRun2.setFontSize(10);
										newRun2.setText("");
										newRun2.setFontFamily("Times New Roman");
										newRun2.addTab();
										newRun2.addTab();
										newRun2.setText(MyUtil.dinhdangso(thanhlysubs.get(j).getSoluong()) + " "
												+ thanhlysubs.get(j).getDvt());
										newRun2.setText("     X     ");
										newRun2.addTab();
										newRun2.setText(MyUtil.dinhdangso(thanhlysubs.get(j).getDongia()));
										newRun2.setText("    =    ");
										newRun2.addTab();
										newRun2.setText(MyUtil.dinhdangso(thanhlysubs.get(j).getThanhtien()) + " đồng");
										newRun2.addBreak();
										i++;
									}
									XWPFRun newRun3 = paragraph.createRun();
									newRun3.addBreak();
								}
							} else if (text != null && text.contains(ptongtien)) {
								paragraph.removeRun(i);
								XWPFRun newRun1 = paragraph.createRun();
								newRun1.addTab();
								newRun1.setFontSize(12);
								newRun1.setFontFamily("Times New Roman");
								newRun1.setText("Tổng cộng : " + MyUtil.dinhdangso(tongtien) + " đồng");
								newRun1.addBreak();
								i++;

							} else if (text != null && text.contains(pvat)) {
								paragraph.removeRun(i);
								XWPFRun newRun1 = paragraph.createRun();
								newRun1.addTab();
								newRun1.setFontSize(12);
								newRun1.setFontFamily("Times New Roman");
								newRun1.setText("Thuế TGGT :    " + MyUtil.dinhdangso(thue) + " đồng");
								newRun1.addBreak();
								i++;

							} else if (text != null && text.contains(ptongcong)) {
								paragraph.removeRun(i);
								XWPFRun newRun1 = paragraph.createRun();
								newRun1.addTab();
								newRun1.setFontSize(12);
								newRun1.setFontFamily("Times New Roman");
								newRun1.setText("Tổng cộng tiền TT :  " + MyUtil.dinhdangso(tongtien + thue) + " đồng");
								newRun1.addBreak();
								i++;
							} else if (text != null && text.contains(ptextTotalFinal)) {
								paragraph.removeRun(i);
								XWPFRun newRun1 = paragraph.createRun();
								newRun1.addTab();
								newRun1.setFontSize(12);
								newRun1.setFontFamily("Times New Roman");
								String tiendocbangchu = "";
								try {
									tiendocbangchu = ConvertNumberToText.docSo(tongtien + thue, "ĐỒNG");
								} catch (Exception e) {
									// TODO: handle exception
								}
								newRun1.setText(tiendocbangchu);
								newRun1.addBreak();
								i++;
							} else if (text != null && text.contains(ptenkhachhang)) {
								paragraph.removeRun(i);
								XWPFRun newRun1 = paragraph.createRun();
								newRun1.addTab();
								newRun1.setFontSize(12);
								newRun1.setFontFamily("Times New Roman");
								String tiendocbangchu = customerTL!=null?customerTL.getCustomer_name():"";
								
								newRun1.setText(tiendocbangchu);
								newRun1.addBreak();
								i++;
							}
						}
					}
					// for (XWPFParagraph p : document.getParagraphs()) {
					// List<XWPFRun> runs = p.getRuns();
					// if (runs != null) {
					// for (XWPFRun r : runs) {
					//
					// String content = r.getText(0);
					// if (content != null && content.contains("${")) {
					// content = content.replace(ptenkhachhang,
					// customerTL.getCustomer_name());
					// content = content.replace(ptongtien,
					// MyUtil.dinhdangso(tongtien));
					// content = content.replace(pvat, MyUtil.dinhdangso(vat));
					// content = content.replace(ptongcong,
					// MyUtil.dinhdangso(tongtien + vat));
					// r.setText(content, 0);
					// }
					// }
					// }
					// }
					// int indexline = 0;
					// for (XWPFParagraph p : document.getParagraphs()) {
					// List<XWPFRun> runs = p.getRuns();
					// if (runs != null) {
					// StringBuffer lineText = new StringBuffer();
					// for (int i = 0; i < runs.size(); i++) {
					// lineText.append(runs.get(i).getText(0));
					// }
					// String content = lineText.toString();
					//
					// // if (content != null && content.contains("${")) {
					// content = content.replace(ptenkhachhang,
					// customerTL.getCustomer_name());
					// content = content.replace(ptongtien,
					// "Tổng cộng : "+MyUtil.dinhdangso(tongtien));
					// content = content.replace(pvat,
					// "Thuế TGGT :    "+MyUtil.dinhdangso(vat));
					// content = content.replace(ptongcong,
					// "Tổng cộng tiền TT :  "+MyUtil.dinhdangso(tongtien +
					// vat));
					//
					// // }
					// while (p.getRuns().size() > 0) {
					// p.removeRun(0);
					// }
					//
					// XWPFRun newRun = p.insertNewRun(indexline);
					// if (newRun != null) {
					// newRun.setText(content);
					// } else {
					// // Xử lý lỗi ở đây
					// }
					// indexline++;
					//
					// }
					//
					// }

					FacesContext facesContext = FacesContext.getCurrentInstance();
					HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance()
							.getExternalContext().getResponse();
					String name = "TLhopdong_" + customerTL.getCustomer_code() + "_"
							+ MyUtil.chuyensangStrCode(sDateTL) + "_" + MyUtil.chuyensangStrCode(eDateTL);
					// Thiết lập các thông tin trả về cho đối tượng
					// HttpServletResponse
					httpServletResponse
							.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
					httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + name + ".docx");
					// Ghi dữ liệu tài liệu vào đối tượng HttpServletResponse
					ServletOutputStream out = httpServletResponse.getOutputStream();
					document.write(out);
					out.flush();
					out.close();
					facesContext.responseComplete();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		//
		// if (contractCrud != null && contractCrud.getId() != 0) {
		// List<Object[]> list = new ArrayList<>();
		// contractService.reportLiquidation(contractCrud.getId(), list);
		// if (list.size() > 0) {
		// List<ContractLiquidationReqInfo> listData = new ArrayList<>();
		// double enTotalAmount = 0;
		// double enTaxAmount = 0;
		// double enTotalAmountWithVat = 0;
		// double pcs = 0;
		// double mts = 0;
		// for (Object[] p : list) {
		// // product_type_id, en_name, id, en_name, box_quantity,
		// // en_quantity, en_unit_price, total_amount, en_unit
		// enTotalAmount = Double.parseDouble(Objects.toString(p[7]));
		// enTotalAmountWithVat = enTotalAmount;
		// String enUnit = Objects.toString(p[8]);
		// double enQuantity = Double.parseDouble(Objects.toString(p[5], "0"));
		// ContractLiquidationReqInfo item = new
		// ContractLiquidationReqInfo(Long.parseLong(Objects
		// .toString(p[0], "0")), Objects.toString(p[1]),
		// Long.parseLong(Objects.toString(p[2],
		// "0")), Objects.toString(p[3]),
		// Double.parseDouble(Objects.toString(p[4], "0")),
		// enQuantity, Double.parseDouble(Objects.toString(p[6], "0")),
		// enTotalAmount, enUnit);
		// listData.add(item);
		// if (enUnit.equals("MTS")) {
		// mts =
		// BigDecimal.valueOf(mts).add(BigDecimal.valueOf(enQuantity)).doubleValue();
		// } else {
		// pcs =
		// BigDecimal.valueOf(pcs).add(BigDecimal.valueOf(enQuantity)).doubleValue();
		// }
		// }
		// String reportPath =
		// FacesContext.getCurrentInstance().getExternalContext()
		// .getRealPath("/resources/reports/hopdong/thanhlyhopdong.jasper");
		// Map<String, Object> importParam = new HashMap<String, Object>();
		// importParam.put("voucher_code", contractCrud.getVoucher_code());
		// importParam.put("contract_date",
		// ToolTimeCustomer.convertDateToString(contractCrud.getContract_date(),
		// "dd/MM/yyyy"));
		// importParam.put("current_date",
		// ToolTimeCustomer.convertDateToString(new Date(), "dd/MM/yyyy"));
		// importParam.put("customer_name",
		// contractCrud.getCustomer().getCustomer_name());
		// importParam.put("en_total_amount", enTotalAmount);
		// importParam.put("en_tax_amount", enTaxAmount);
		// importParam.put("en_total_amount_with_vat", enTotalAmountWithVat);
		// importParam.put("words_say",
		// NumberWordConverter.getMoneyIntoWords(enTotalAmountWithVat));
		// importParam.put("bang_chu",
		// ConvertNumberToText.docSo(enTotalAmountWithVat, "USD"));
		// importParam.put("list_data", listData);
		// importParam.put("param_en_quantity",
		// formatHandler.getNumberFormatEn(mts, 100) + "MTS \n +"
		// + formatHandler.getNumberFormat(pcs, 100) + "PCS");
		// String printFileName = JasperFillManager.fillReportToFile(reportPath,
		// importParam,
		// new JREmptyDataSource());
		//
		// JRDocxExporter exporter = new JRDocxExporter();
		// exporter.setExporterInput(new SimpleExporterInput(printFileName));
		//
		// // Tạo tệp tin .doc mới
		// XWPFDocument doc = new XWPFDocument();
		// FacesContext facesContext = FacesContext.getCurrentInstance();
		// HttpServletResponse httpServletResponse = (HttpServletResponse)
		// FacesContext.getCurrentInstance()
		// .getExternalContext().getResponse();
		// String name = "thanhlyhopdong_" + contractCrud.getVoucher_code();
		// httpServletResponse.addHeader("Content-disposition",
		// "attachment; filename=" + name + ".docx");
		// ServletOutputStream servletOutputStream =
		// httpServletResponse.getOutputStream();
		// exporter.setExporterOutput(new
		// SimpleOutputStreamExporterOutput(servletOutputStream));
		//
		// SimpleDocxReportConfiguration cf = new
		// SimpleDocxReportConfiguration();
		// cf.setFramesAsNestedTables(true);
		// exporter.setConfiguration(cf);
		//
		// exporter.exportReport();
		// facesContext.responseComplete();
		// try {
		// servletOutputStream.close();
		// } catch (Exception e) {
		// }
		// }
		// }
		// } catch (Exception e) {
		// logger.error("ContractBean.liquidation:" + e.getMessage(), e);
		// }
	}

	public void thanhlyhopdong2Excel() {
		try {
			List<Object[]> datas = invoiceDetailService.findByCustomer(customerTL, sDateTL, eDateTL);
			if (datas.size() == 0) {
				notice("Không có dữ liệu phát sinh.");
			} else {

				// xuat file excel.
				if (datas.size() > 0) {
					List<Object[]> results = new ArrayList<Object[]>();
					Object[] title = { "Mã SP", "Tên SP", "SL", "ĐG", "Tiền", "ĐVT" };
					results.add(title);
					for (Object[] it : datas) {
						results.add(it);
					}
					ToolReport.printReportExcelRawXLSX(results, "tlhopdong");
				}
			}
		} catch (Exception e) {
			logger.error("IEInvoiceBean.exportIEInvoiceListByContNoReport:" + e.getMessage(), e);
		}
	}

	public void nextOrPrev(int next) {
		PrimeFaces current = PrimeFaces.current();
		try {
			// clear filter
			current.executeScript("PF('tablect').clearFilters();");
			if (listContract != null && listContract.size() > 0) {
				int index = listContract.indexOf(contractCrud);
				int size = listContract.size() - 1;
				if (index == -1) {
					contractSelect = listContract.get(0);
					contractCrud = contractSelect.clone();
				} else {
					switch (next) {
					case 1:
						if (index == size) {
							contractSelect = listContract.get(0);
							contractCrud = contractSelect.clone();
						} else {
							contractSelect = listContract.get(index + 1);
							contractCrud = contractSelect.clone();
						}
						break;

					default:
						if (index == 0) {
							contractSelect = listContract.get(size);
							contractCrud = contractSelect.clone();
						} else {
							contractSelect = listContract.get(index - 1);
							contractCrud = contractSelect.clone();
						}
						break;
					}
				}
				// load chi tiết hợp đồng.
				listContractDetail = new ArrayList<>();
				contractService.selectContractDetailByContractId(contractSelect.getId(), listContractDetail);
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.nextOrPrev:" + e.getMessage(), e);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public void updateContractDetail(ContractDetail item) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (item != null && item.getId() != 0) {
				if (allowUpdate(new Date())) {
					item.setLast_modifed_by(account.getMember().getName());
					item.setLast_modifed_date(new Date());
					if (contractService.updateDetail(new ContractDetailReqInfo(item)) == 0) {
						notify.success("Thành công!");
					} else {
						notify.warning("Không cập nhật được trạng thái!");
					}
				} else {
					notify.warning("Tài khoản chưa được phân quyền!");
				}
			}
		} catch (Exception e) {
			logger.error("ContractBean.updateContractDetail:" + e.getMessage(), e);
		}
	}

	public void updateContract(Contract item) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (allowUpdate(new Date())) {
				item.setLast_modifed_by(account.getMember().getName());
				item.setLast_modifed_date(new Date());
				if (contractService.update(new ContractReqInfo(item)) == 0) {
					notify.success("Thành công!");
				} else {
					notify.warning("Không cập nhật được trạng thái!");
				}
			} else {
				notify.warning("Tài khoản chưa được phân quyền!");
			}
		} catch (Exception e) {
			logger.error("ContractBean.updateContract:" + e.getMessage(), e);
		}
	}

	public void deleteContract() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('tablect').clearFilters();");
			if (contractSelect != null && contractSelect.getId() != 0) {
				if (allowDelete(new Date())) {
					if (contractService.deleteById(contractSelect.getId()) > 0) {
						listContract.remove(contractSelect);
						notify.success("Xóa thành công!");
					} else {
						notify.warning("Xóa thất bại!");
					}
				} else {
					notify.warning("Tài khoản chưa được phân quyền!");
				}
			} else {
				notify.warning("Chưa chọn dòng để xóa!");
			}
		} catch (Exception e) {
			logger.error("ContractBean.deleteContract:" + e.getMessage(), e);
		}
	}

	public void deleteContactDetail(ContractDetail item) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('tablect').clearFilters();");
			if (item != null && item.getId() != 0) {
				if (allowDelete(new Date())) {
					if (contractService.deleteDetailById(item.getId()) > 0) {
						listContractDetail.remove(item);
						notify.success("Xóa thành công!");
					} else {
						notify.warning("Xóa thất bại!");
					}
				} else {
					notify.warning("Tài khoản chưa được phân quyền!");
				}
			} else {
				notify.warning("Chưa chọn dòng chi tiết để xóa!");
			}
		} catch (Exception e) {
			logger.error("ContractBean.deleteContactDetail:" + e.getMessage(), e);
		}
	}

	public void createNew() {
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('tablect').clearFilters();");
			contractCrud = new Contract();
			contractCrud.setVoucher_code(contractService.initVoucherCode());
			listContractDetail = new ArrayList<>();
		} catch (Exception e) {
			logger.error("ContractBean.createNew:" + e.getMessage(), e);
		}
	}

	public void exportExcel() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (contractCrud != null && contractCrud.getId() != 0 && listContractDetail != null
					&& listContractDetail.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "SOHD", "NGÀY HIỆU LỰC", "NGÀY HẾT HẠN", "MAKH", "TÊN KHÁCH HÀNG", "MASP",
						"TÊN SẢN PHẨM", "KHUYẾN MÃI", "CKMAI", "SỐ LƯỢNG HĐ", "SỐ LƯỢNG KG", "ĐÃ THỰC HIỆN", "CÒN LẠI",
						"ĐƠN GIÁ", "SỐ TIỀN", "LỢI NHUẬN" };
				results.add(title);
				String voucherCode = contractCrud.getVoucher_code();
				String effectiveDate = ToolTimeCustomer.convertDateToString(contractCrud.getEffective_date(),
						"dd/MM/yyyy");
				String expriryDate = ToolTimeCustomer.convertDateToString(contractCrud.getExpiry_date(), "dd/MM/yyyy");
				String customerCode = contractCrud.getCustomer().getCustomer_code();
				String customerName = contractCrud.getCustomer().getCustomer_name();
				for (ContractDetail it : listContractDetail) {
					Product product = it.getProduct();
					double soLuongKg = formatHandler.roundCus(it.getQuantity() * product.getFactor(), 1000);
					double soluongKgThucHien = 0;
					double soLuongKgConLai = BigDecimal.valueOf(soLuongKg)
							.subtract(BigDecimal.valueOf(soluongKgThucHien)).doubleValue();
					Object[] row = { voucherCode, effectiveDate, expriryDate, customerCode, customerName,
							product.getProduct_code(), product.getProduct_name(), Boolean.toString(it.isPromotion()),
							"", it.getQuantity(), soLuongKg, 0, soLuongKgConLai, it.getUnit_price(),
							it.getUnit_price() * it.getQuantity(), it.getProfit() };
					results.add(row);
				}
				String titleEx = "hop_dong_san_pham_" + voucherCode;
				ToolReport.printReportExcelRaw(results, titleEx);
			} else {
				notify.message("Không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error("ContractBean.exportExcel:" + e.getMessage(), e);
		}
	}

	public void exportExceltonghop() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (listContract != null && listContract.size() != 0) {
				List<ContractDetail> listContractDetails = new ArrayList<ContractDetail>();
				List<Long> idcontracts = new ArrayList<Long>();
				for (int i = 0; i < listContract.size(); i++) {
					idcontracts.add(listContract.get(i).getId());
				}
				contractService.selectContractDetailByContractIds(idcontracts, listContractDetails);
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "SOHD", "NGÀY HIỆU LỰC", "NGÀY HẾT HẠN", "MAKH", "TÊN KHÁCH HÀNG", "MASP",
						"TÊN SẢN PHẨM", "KHUYẾN MÃI", "CKMAI", "SỐ LƯỢNG HĐ", "SỐ LƯỢNG KG", "ĐÃ THỰC HIỆN", "CÒN LẠI",
						"ĐƠN GIÁ", "SỐ TIỀN" };
				results.add(title);

				for (ContractDetail it : listContractDetails) {
					String voucherCode = it.getContract().getVoucher_code();
					String effectiveDate = ToolTimeCustomer.convertDateToString(it.getContract().getEffective_date(),
							"dd/MM/yyyy");
					String expriryDate = ToolTimeCustomer.convertDateToString(it.getContract().getExpiry_date(),
							"dd/MM/yyyy");
					String customerCode = it.getContract().getCustomer().getCustomer_code();
					String customerName = it.getContract().getCustomer().getCustomer_name();

					Product product = it.getProduct();
					double soLuongKg = formatHandler.roundCus(it.getQuantity() * product.getFactor(), 1000);
					double soluongKgThucHien = 0;
					double soLuongKgConLai = BigDecimal.valueOf(soLuongKg)
							.subtract(BigDecimal.valueOf(soluongKgThucHien)).doubleValue();
					Object[] row = { voucherCode, effectiveDate, expriryDate, customerCode, customerName,
							product.getProduct_code(), product.getProduct_name(), Boolean.toString(it.isPromotion()),
							"", it.getQuantity(), soLuongKg, 0, soLuongKgConLai, it.getUnit_price(), it.getTotal() };
					results.add(row);
				}
				String titleEx = "hop_dong_san_pham_tonghop";
				ToolReport.printReportExcelRaw(results, titleEx);
			} else {
				notify.message("Không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error("ContractBean.exportExcel:" + e.getMessage(), e);
		}
	}

	public void copyContract() {
		try {
			if (contractCrud != null && contractCrud.getId() != 0 && listContractDetail != null
					&& listContractDetail.size() > 0) {
				contractCrud.setId(0);
				contractCrud.setContract_code(null);
				contractCrud.setVoucher_code(contractService.initVoucherCode());
				for (ContractDetail item : listContractDetail) {
					item.setId(0);
					item.setContract(null);
					item.setLast_modifed_by(null);
					item.setLast_modifed_date(null);
				}
			}
		} catch (Exception e) {
			logger.error("ContractBean.copyContract:" + e.getMessage(), e);
		}
	}

	public void tinhlaithanhtien() {
		List<ContractDetail> listContractDetails = new ArrayList<ContractDetail>();
		contractService.findAll(listContractDetails);
		for (int i = 0; i < listContractDetails.size(); i++) {
			double total = listContractDetails.get(i).getQuantity() * listContractDetails.get(i).getUnit_price();
			listContractDetails.get(i).setTotal(formatHandler.roundCus(total, 100));
			ContractDetailReqInfo ct = new ContractDetailReqInfo(listContractDetails.get(i));
			contractService.updateDetail(ct);
		}
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("swaldesigntimer('Thành công!', 'Đã tính lại thành tiền','success',2000);");

	}

	public void thanhtien() {
		double total = contractDetailCrud.getQuantity() * contractDetailCrud.getUnit_price();
		contractDetailCrud.setTotal(formatHandler.roundCus(total, 100));
	}

	public double sumContract() {
		double sum = 0;
		try {
			if (listContractDetail != null && listContractDetail.size() > 0) {
				for (ContractDetail item : listContractDetail) {
					sum += item.getTotal();
				}
			}
		} catch (Exception e) {
			logger.error("ContractBean.sumContract:" + e.getMessage(), e);
		}
		return sum;
	}

	public boolean filterContract(Object value, Object filter, Locale locale) {
		try {
			String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
			if (filterText == null || filterText.equals("")) {
				return true;
			}
			String productName = (String) value;
			return productName.contains(filterText);
		} catch (Exception e) {
			logger.error("ContractBean.filterContract:" + e.getMessage(), e);
		}
		return false;
	}

	public void showContract() {
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('tablect').clearFilters();");
			if (contractSelect != null) {
				contractCrud = contractSelect.clone();
				// load chi tiết hợp đồng.
				listContractDetail = new ArrayList<>();
				contractService.selectContractDetailByContractId(contractSelect.getId(), listContractDetail);
			}
		} catch (Exception e) {
			logger.error("ContractBean.showContract:" + e.getMessage(), e);
		}
	}

	public void search() {
		try {
			/*
			 * {contract:{customer_id:0,voucher_code:
			 * '',from_date:'',to_date:'',liquidated:-1},page:{page_index:0,
			 * page_size:0}}
			 */
			listContract = new ArrayList<>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("customer_id", customerSearch == null ? 0 : customerSearch.getId());
			jsonInfo.addProperty("voucher_code", voucherCodeSearch);
			jsonInfo.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("liquidated", liquidatedSearch);
			JsonObject json = new JsonObject();
			json.add("contract", jsonInfo);
			contractService.search(JsonParserUtil.getGson().toJson(json), listContract);
		} catch (Exception e) {
			logger.error("ContractBean.search:" + e.getMessage(), e);
		}
	}

	public List<Customer> completeCustomer(String text) {
		try {
			List<Customer> list = new ArrayList<Customer>();
			customerService.complete(formatHandler.converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("ContractBean.completeCustomer:" + e.getMessage(), e);
		}
		return null;
	}

	public List<Product> completeProduct(String text) {
		try {
			List<Product> list = new ArrayList<>();
			productService.complete3(formatHandler.converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("ContractBean.completeProduct:" + e.getMessage(), e);
		}
		return null;
	}

	public void saveOrUpdate() {
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('tablect').clearFilters();");
			if (contractCrud != null) {
				// kiểm tra dữ liệu có đầy đủ không
				Customer customer = contractCrud.getCustomer();
				Date contractDate = contractCrud.getContract_date();
				Date effectiveDate = contractCrud.getEffective_date();
				if (customer != null && effectiveDate != null && contractDate != null) {
					ContractReqInfo t = new ContractReqInfo(contractCrud);
					if (contractCrud.getId() == 0) {
						if (allowSave(new Date())) {
							contractCrud.setCreated_date(new Date());
							contractCrud.setCreated_by(account.getMember().getName());
							int code = contractService.insert(t);
							switch (code) {
							case 0:
								// nếu thành công thêm hợp đồng vào danh sách
								listContract.add(0, t.getContract());
								if (listContractDetail != null && listContractDetail.size() > 0) {
									// trường hợp copy phiếu
									ContractDetailReqInfo td = new ContractDetailReqInfo();
									for (ContractDetail d : listContractDetail) {
										if (d.getId() == 0) {
											td.setContract_detail(d);
											d.setContract(t.getContract());
											d.setCreated_by(account.getMember().getName());
											d.setCreated_date(new Date());
											d.setLast_modifed_by(null);
											d.setLast_modifed_date(null);
											contractService.insertDetail(td);
										}
									}
									current.executeScript("swaldesigntimer('Thành công!', 'Sao chép hợp đồng thành công!','success',2000);");
								} else {
									current.executeScript("swaldesigntimer('Thành công!', 'Tạo hợp đồng thành công!','success',2000);");
								}
								break;
							case -2:
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Số hợp đồng bị trùng!','warning',2000);");
								break;

							default:
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Không lưu được!','warning',2000);");
								break;
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
						}
					} else {
						if (allowUpdate(new Date())) {
							contractCrud.setLast_modifed_by(account.getMember().getName());
							contractCrud.setLast_modifed_date(new Date());
							int code = contractService.update(t);
							switch (code) {
							case 0:
								// nếu cập nhật thành công thì cập nhật lại danh
								// sách.
								listContract.set(listContract.indexOf(contractCrud), t.getContract());
								current.executeScript("swaldesigntimer('Thành công!', 'Cập nhật thành công!','success',2000);");
								break;
							case -2:
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Số hợp đồng bị trùng!','warning',2000);");
								break;

							default:
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Cập nhật thất bại!','warning',2000);");
								break;
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
						}
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!','Thông tin không đầy đủ!','warning',2000);");
				}
			}
		} catch (Exception e) {
			logger.error("ContractBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}

	@Inject
	ParamReportDetailService paramReportDetailService;

	public void debtTracking() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (contractCrud != null && contractCrud.getId() != 0) {
				JsonObject json = new JsonObject();
				json.addProperty("contract_id", contractCrud.getId());
				List<VoucherPayment> list = new ArrayList<>();
				voucherPaymentService.getVoucherPaymentBy(JsonParserUtil.getGson().toJson(json), list);
				if (list.size() > 0) {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/hopdong/theodoicongno.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);
					List<ParamReportDetail> listConfig = paramReportDetailService
							.findByParamReportName("thongtinchung");
					for (ParamReportDetail p : listConfig) {
						importParam.put(p.getKey(), p.getValue());
					}

					importParam.put(
							"logo",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/lixco_logo.png"));
					importParam.put("customer_name", contractCrud.getCustomer().getCustomer_name());
					importParam.put("voucher_code", contractCrud.getVoucher_code());
					importParam.put("total_quantity", sumContract());
					importParam.put("list_detail", list);
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JREmptyDataSource());
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("utility.printPDF('" + ba + "')");
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Chưa có ủy nhiệm chi nào','warning',2000);");
				}
			}
		} catch (Exception e) {
			logger.error("ContractBean.debtTracking:" + e.getMessage(), e);
		}
	}

	public void showDialogAddContractDetail() {
		PrimeFaces current = PrimeFaces.current();
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (contractCrud != null && contractCrud.getId() != 0) {
				contractDetailCrud = new ContractDetail();
				contractDetailCrud.setContract(contractCrud);
				current.executeScript("PF('dlg1').show();");
			} else {
				notify.warning("Hợp đồng không tồn tại!");
			}
		} catch (Exception e) {
			logger.error("ContractBean.showDialogAddContractDetail:" + e.getMessage(), e);
		}
	}

	public void showDialogEditContractDetail() {
		PrimeFaces current = PrimeFaces.current();
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (contractCrud != null && contractCrud.getId() != 0) {
				if (contractDetailSelect != null && contractSelect.getId() != 0) {
					contractDetailCrud = contractDetailSelect.clone();
					current.executeScript("PF('dlg1').show();");
				} else {
					notify.warning("Chưa chọn chi tiết hợp đồng để chỉnh sửa!");
				}
			} else {
				notify.warning("Hợp đồng không tồn tại!");
			}
		} catch (Exception e) {
			logger.error("ContractBean.showDialogEditContractDetail:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdateDetail() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('tablect').clearFilters();");
			if (contractDetailCrud != null) {
				// kiểm tra số liệu
				Product product = contractDetailCrud.getProduct();
				double quantity = contractDetailCrud.getQuantity();
				if (product != null && quantity != 0) {
					ContractDetailReqInfo t = new ContractDetailReqInfo(contractDetailCrud.clone());
					if (contractDetailCrud.getId() == 0) {
						if (allowSave(new Date())) {
							contractDetailCrud.setCreated_by(account.getMember().getName());
							contractDetailCrud.setCreated_date(new Date());
							if (contractService.insertDetail(t) == 0) {
								// thêm vào danh sách.
								listContractDetail.add(0, t.getContract_detail());
								notify.success("Lưu thành công!");
								// refesh lại dialog
								contractDetailCrud = new ContractDetail();
								contractDetailCrud.setContract(contractCrud);
							} else {
								notify.warning("Lưu thất bại!");
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
						}
					} else {
						if (allowUpdate(new Date())) {
							contractDetailCrud.setLast_modifed_by(account.getMember().getName());
							contractDetailCrud.setLast_modifed_date(new Date());
							if (contractService.updateDetail(t) == 0) {
								// cập nhật lại danh sách
								listContractDetail.set(listContractDetail.indexOf(t.getContract_detail()),
										t.getContract_detail());
								notify.success("Cập nhật thành công!");
							} else {
								notify.warning("Cập nhật thất bại!");
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
						}
					}
				} else {
					notify.warning("Thông tin không đầy đủ!");
				}
			}
		} catch (Exception e) {
			logger.error("ContractBean.saveOrUpdateDetail:" + e.getMessage(), e);
		}
	}

	@Getter
	private double tongsl;
	@Getter
	private double tongdathuchien;
	@Getter
	private double tongconlai;
	@Getter
	private double tongthanhtienTH;
	@Getter
	private double tongthanhtienCL;

	public void checkProcessContract() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			tongsl = 0;
			tongdathuchien = 0;
			tongconlai = 0;
			tongthanhtienTH = 0;
			listProcessContract = new ArrayList<>();
			if (contractCrud != null && contractCrud.getId() != 0 && listContractDetail != null
					&& listContractDetail.size() > 0) {
				for (ContractDetail d : listContractDetail) {
					Object[] objThucxuat = invoiceService.processingContractDetail(contractCrud.getId(), d.getProduct()
							.getId(), d.getUnit_price());
					ProcessContract pc;
					if (objThucxuat != null) {
						pc = new ProcessContract(d, (double) objThucxuat[0], (double) objThucxuat[1]);
					} else {
						pc = new ProcessContract(d);
					}
					listProcessContract.add(pc);
					tongsl += BigDecimal.valueOf(d.getQuantity())
							.multiply(BigDecimal.valueOf(d.getProduct().getFactor())).doubleValue();
					tongdathuchien += pc.getDathuchien();
					tongconlai += pc.getConlai();
					tongthanhtienTH += pc.getTiendathuchien();
					tongthanhtienCL += pc.getTienconlai();
				}

			} else {
				notify.warning("Không có dữ liệu!");
			}
		} catch (Exception e) {
			logger.error("ContractBean.checkProcessContract:" + e.getMessage(), e);
		}
	}

	public void printPDFProcessContract() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (listProcessContract != null && listProcessContract.size() > 0) {
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/hopdong/kiemtrathuchienhopdong.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
				importParam.put(
						"logo",
						FacesContext.getCurrentInstance().getExternalContext()
								.getRealPath("/resources/gfx/lixco_logo.png"));
				importParam.put("customer_name", contractCrud.getCustomer().getCustomer_name());
				importParam.put("title", "THEO DÕI HỢP ĐỒNG TIÊU THỤ SẢN PHẨM SỐ " + contractCrud.getVoucher_code());
				importParam.put("customer_code", contractCrud.getCustomer().getCustomer_code());
				importParam.put("list_data", listProcessContract);
				JasperPrint jasperPrint = JasperFillManager
						.fillReport(reportPath, importParam, new JREmptyDataSource());
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
				current.executeScript("utility.printPDF('" + ba + "')");
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo', 'Không có dữ liệu','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("ContractBean.printPDFProcessContract:" + e.getMessage(), e);
		}
	}

	public Contract getContractCrud() {
		return contractCrud;
	}

	public void setContractCrud(Contract contractCrud) {
		this.contractCrud = contractCrud;
	}

	public Contract getContractSelect() {
		return contractSelect;
	}

	public void setContractSelect(Contract contractSelect) {
		this.contractSelect = contractSelect;
	}

	public List<Contract> getListContract() {
		return listContract;
	}

	public void setListContract(List<Contract> listContract) {
		this.listContract = listContract;
	}

	public Customer getCustomerSearch() {
		return customerSearch;
	}

	public void setCustomerSearch(Customer customerSearch) {
		this.customerSearch = customerSearch;
	}

	public String getVoucherCodeSearch() {
		return voucherCodeSearch;
	}

	public void setVoucherCodeSearch(String voucherCodeSearch) {
		this.voucherCodeSearch = voucherCodeSearch;
	}

	public Date getFromDateSearch() {
		return fromDateSearch;
	}

	public void setFromDateSearch(Date fromDateSearch) {
		this.fromDateSearch = fromDateSearch;
	}

	public Date getToDateSearch() {
		return toDateSearch;
	}

	public void setToDateSearch(Date toDateSearch) {
		this.toDateSearch = toDateSearch;
	}

	public int getLiquidatedSearch() {
		return liquidatedSearch;
	}

	public void setLiquidatedSearch(int liquidatedSearch) {
		this.liquidatedSearch = liquidatedSearch;
	}

	public ContractDetail getContractDetailCrud() {
		return contractDetailCrud;
	}

	public void setContractDetailCrud(ContractDetail contractDetailCrud) {
		this.contractDetailCrud = contractDetailCrud;
	}

	public ContractDetail getContractDetailSelect() {
		return contractDetailSelect;
	}

	public void setContractDetailSelect(ContractDetail contractDetailSelect) {
		this.contractDetailSelect = contractDetailSelect;
	}

	public List<ContractDetail> getListContractDetail() {
		return listContractDetail;
	}

	public void setListContractDetail(List<ContractDetail> listContractDetail) {
		this.listContractDetail = listContractDetail;
	}

	public FormatHandler getFormatHandler() {
		return formatHandler;
	}

	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
	}

	public List<Currency> getListCurrency() {
		return listCurrency;
	}

	public void setListCurrency(List<Currency> listCurrency) {
		this.listCurrency = listCurrency;
	}

	public List<ContractDetail> getListContractDetailFillter() {
		return listContractDetailFillter;
	}

	public void setListContractDetailFillter(List<ContractDetail> listContractDetailFillter) {
		this.listContractDetailFillter = listContractDetailFillter;
	}

	public List<ProcessContract> getListProcessContract() {
		return listProcessContract;
	}

	public void setListProcessContract(List<ProcessContract> listProcessContract) {
		this.listProcessContract = listProcessContract;
	}

	public Date getFromDateFox() {
		return fromDateFox;
	}

	public void setFromDateFox(Date fromDateFox) {
		this.fromDateFox = fromDateFox;
	}

	public Date getToDateFox() {
		return toDateFox;
	}

	public void setToDateFox(Date toDateFox) {
		this.toDateFox = toDateFox;
	}

	public Customer getCustomerTL() {
		return customerTL;
	}

	public void setCustomerTL(Customer customerTL) {
		this.customerTL = customerTL;
	}

	public Date getsDateTL() {
		return sDateTL;
	}

	public void setsDateTL(Date sDateTL) {
		this.sDateTL = sDateTL;
	}

	public Date geteDateTL() {
		return eDateTL;
	}

	public void seteDateTL(Date eDateTL) {
		this.eDateTL = eDateTL;
	}

}
