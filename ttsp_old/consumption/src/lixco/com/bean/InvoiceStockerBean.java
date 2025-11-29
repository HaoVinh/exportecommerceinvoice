package lixco.com.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.NavigationInfo;
import lixco.com.common.SessionHelper;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Batch;
import lixco.com.entity.Car;
import lixco.com.entity.Customer;
import lixco.com.entity.ExportBatch;
import lixco.com.entity.IECategories;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.interfaces.IBatchService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IInventoryService;
import lixco.com.interfaces.IInvoiceDetailService;
import lixco.com.interfaces.IInvoiceService;
import lixco.com.interfaces.IProcessLogicInvoiceService;
import lixco.com.reportInfo.LenhDieuDong;
import lixco.com.reportInfo.PhieuNhapKho;
import lixco.com.reportInfo.PhieuXuatKho;
import lixco.com.reportInfo.PhieuXuatKhoKiemVCNB;
import lixco.com.reqInfo.BatchReqInfo;
import lixco.com.reqInfo.InvoiceDetailReqInfo;
import lixco.com.reqInfo.InvoiceReqInfo;
import lixco.com.reqInfo.Message;
import lixco.com.reqInfo.OrderLixReqInfo;
import lixco.com.reqInfo.ReportTypeIEInVoice;
import lixco.com.reqInfo.WrapDataInvoiceDetail;
import lixco.com.reqInfo.WrapDelExportBatchReqInfo;
import lixco.com.reqInfo.WrapExportDataReqInfo;
import lixco.com.reqInfo.WrapInvoiceDetailReqInfo;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.util.ConvertNumberToText;
import trong.lixco.com.util.Notify;

@Named
@ViewScoped
public class InvoiceStockerBean extends AbstractBean  {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IProcessLogicInvoiceService processLogicInvoiceService;
	@Inject
	private IInvoiceService invoiceService;
	@Inject
	private IInvoiceDetailService invoiceDetailService;
	@Inject
	private IBatchService batchService;
	@Inject
	private IIECategoriesService iECategoriesService;
	@Inject
	private ICustomerService customerService;
	private Invoice invoiceCrud;
	private Invoice invoiceSelect;
	private List<Invoice> listInvoice;
	private WrapInvoiceDetailReqInfo wrapInvoiceDetailCrud;
	private WrapInvoiceDetailReqInfo wrapInvoiceDetailSelect;
	private List<WrapInvoiceDetailReqInfo> listWrapInvoiceDetail;
	private List<WrapInvoiceDetailReqInfo> listWrapInvoiceDetailFilter;
	private FormatHandler formatHandler;
	private List<IECategories> listIECategories;
	/*search invoice*/
	private Date fromDateSearch;
	private Date toDateSearch;
	private Customer customerSearch;
	private String invoiceCodeSearch;
	private String voucherCodeSearch;
	private IECategories ieCategoriesSearch;
	private String poNoSearch;
	private String orderVoucherSearch;
	private String orderCodeSearch;
	private int paymentSearch;
	private int exportedSearch;
	private int pageSize;
	private NavigationInfo navigationInfo;
	private List<Integer> listRowPerPage;
	private Account account;
	/*Export lô hàng*/
	private WrapInvoiceDetailReqInfo wrapInvoiceDetailPick;
	private List<WrapExportDataReqInfo> listWrapExportData;
	/* report */
	private List<ReportTypeIEInVoice> listSelectReport;
	private ReportTypeIEInVoice reportTypeIEInVoiceSelect;
	@Override
	protected void initItem() {
		try{
			formatHandler=FormatHandler.getInstance();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);
			listIECategories=new ArrayList<IECategories>();
			iECategoriesService.selectAll(listIECategories);
			paymentSearch=-1;
			navigationInfo = new NavigationInfo();
			navigationInfo.setCurrentPage(1);
			initRowPerPage();
			navigationInfo.setLimit(pageSize);
			navigationInfo.setMaxIndices(5);
			int month=ToolTimeCustomer.getMonthCurrent();
			int year=ToolTimeCustomer.getYearCurrent();
			fromDateSearch=ToolTimeCustomer.getDateMinCustomer(month, year);
			toDateSearch=ToolTimeCustomer.getDateMaxCustomer(month, year);
			search();
			createdNew();
			/* init report */
			listSelectReport = new ArrayList<>();
//			listSelectReport.add(new ReportTypeIEInVoice(1, "In hóa đơn"));
//			listSelectReport.add(new ReportTypeIEInVoice(2, "Phiếu xuất kho_VCNB"));
			listSelectReport.add(new ReportTypeIEInVoice(3, "In phiếu xuất kho"));
			listSelectReport.add(new ReportTypeIEInVoice(4, "Lệnh điều động Lix"));
//			listSelectReport.add(new ReportTypeIEInVoice(5, "In phiếu xuất kho(lever)"));
//			listSelectReport.add(new ReportTypeIEInVoice(6, "In Hóa Đơn Xuất Khẩu(VNĐ)"));
//			listSelectReport.add(new ReportTypeIEInVoice(7, "In Lệnh điều động"));
			listSelectReport.add(new ReportTypeIEInVoice(8, "Phiếu nhập kho sp"));
			listSelectReport.add(new ReportTypeIEInVoice(9, "L.điều động Lix(nhiều phiếu)"));
		}catch(Exception e){
			logger.error("InvoiceBean.initItem:"+e.getMessage(),e);
		}
		
	}
	public void viewInventory(){
		try{
			if(invoiceCrud !=null){
				List<Long> listProductId=new ArrayList<>();
				for(WrapInvoiceDetailReqInfo w :listWrapInvoiceDetail){
					listProductId.add(w.getInvoice_detail().getProduct().getId());
				}
				List<Object[]> list=new ArrayList<>();
				invoiceService.getListInventory(listProductId, invoiceCrud.getDelivery_date(), list);
				if(list.size()>0){
					Map<Long, Double[]> map=new LinkedHashMap<Long, Double[]>();
					for(Object[] p:list){
						Double[] arr={Double.parseDouble(Objects.toString(p[1],"0")),Double.parseDouble(Objects.toString(p[2],"0"))};
						map.put(Long.parseLong(Objects.toString(p[0])),arr);
					}
					for(WrapInvoiceDetailReqInfo w:listWrapInvoiceDetail){
						long id=w.getInvoice_detail().getProduct().getId();
						if(map.containsKey(id)){
							w.setInv_quantity(map.get(id)[0]);
							w.setInv_quantity_cal(map.get(id)[1]);
						}
					}
				}
			}
		}catch (Exception e) {
			logger.error("InvoiceBean.viewInventory:"+e.getMessage(),e);
		}
	}
	public void processExported(){
		PrimeFaces current=PrimeFaces.current();
		try{
			if(allowUpdate(new Date())){
				if(invoiceCrud !=null && invoiceCrud.getId() !=0){
					Message message=new Message();
					int code=processLogicInvoiceService.processExported(invoiceCrud, message);
					if(code==-1){
						String m=message.getUser_message()+" \\n"+message.getInternal_message();
						current.executeScript(
								"swaldesignclose('Xảy ra lỗi', '"+m+"','warning');");
					}else{
						current.executeScript(
								"swaldesigntimer('Thành công', 'Đã xuất hàng thành công','success',2000);");
					}
					//load lại phiếu xuất 
					InvoiceReqInfo invoiceReqInfo=new InvoiceReqInfo();
					invoiceService.selectById(invoiceCrud.getId(), invoiceReqInfo);
					Invoice invoiceTemp=invoiceReqInfo.getInvoice();
					invoiceCrud=invoiceTemp.clone();
					int index=listInvoice.indexOf(invoiceTemp);
					if(index!=-1){
						listInvoice.set(index,invoiceTemp);
					}
				}
			}else{
				current.executeScript(
						"swaldesigntimer('Cảnh báo', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
			}
		}catch (Exception e) {
			logger.error("InvoiceStockerBean.processExported:"+e.getMessage(),e);
		}
	}
	public void nextOrPrev(int next){
		PrimeFaces current=PrimeFaces.current();
		try{
			current.executeScript("PF('tablect').clearFilters();");
			if(listInvoice !=null && listInvoice.size()>0){
				int index =listInvoice.indexOf(invoiceCrud);
				int size=listInvoice.size()-1;
				if(index==-1){
					invoiceSelect=listInvoice.get(0);
					invoiceCrud=invoiceSelect.clone();
				}else{
					switch (next) {
					case 1:
						if(index==size){
							invoiceSelect=listInvoice.get(0);
							invoiceCrud=invoiceSelect.clone();
						}else{
							invoiceSelect=listInvoice.get(index+1);
							invoiceCrud=invoiceSelect.clone();
						}
						break;
		
					default:
						if(index==0){
							invoiceSelect=listInvoice.get(size);
							invoiceCrud=invoiceSelect.clone();
						}else{
							invoiceSelect=listInvoice.get(index-1);
							invoiceCrud=invoiceSelect.clone();
						}
						break;
					}
				}
				//show thông tin chi tiết hóa đơn
				listWrapInvoiceDetail=new ArrayList<>();
				List<InvoiceDetail> listInvoiceDetail=new ArrayList<>();
				invoiceDetailService.selectByInvoice(invoiceCrud.getId(), listInvoiceDetail);
				for(InvoiceDetail dt:listInvoiceDetail){
					List<ExportBatch> list=new ArrayList<>();
					invoiceService.getListExportBatch(dt.getId(), list);
					WrapInvoiceDetailReqInfo w=new WrapInvoiceDetailReqInfo(dt, list);
					listWrapInvoiceDetail.add(w);
				}
				//tính tổng tiền hóa đơn
				sumInvoice();
			}
		}catch (Exception e) {
			logger.error("InvoiceBean.nextOrPrev:"+e.getMessage(),e);
		}
		//chạy thử nghiệm
		current.executeScript("setTimeout(utility.expandTable('.ui-row-toggler'),1000)");
	}
	public void processPrintPDFReport(){
		try{
			if (reportTypeIEInVoiceSelect != null) {
				long id = reportTypeIEInVoiceSelect.getId();
				if (id == 1) {
				} else if (id == 2) {
				} else if(id==3){
					exportPhieuXuatKho();
				}else if (id == 4) {
					exportLenhDieuDong();
				}else if(id==5){
				}else if (id == 6) {
				} else if (id == 8) {
					exportPhieuNhapKho();
				}else if(id==9){
					exportLenhDieuDongNhieuPhieu();
				}
			}
		}catch (Exception e) {
			logger.error("InvoiceBean.processPrintPDFReport:"+e.getMessage(),e);
		}
	}
	public void exportLenhDieuDongNhieuPhieu(){
		PrimeFaces current=PrimeFaces.current();
		try{
			if(invoiceCrud !=null && invoiceCrud.getId()!=0){
				//lệnh điều động
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/invoice/lenhdieudong_m.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
				Date invoiceDate=invoiceCrud.getInvoice_date();
				int day=ToolTimeCustomer.getDayM(invoiceDate);
				int month=ToolTimeCustomer.getMonthM(invoiceDate);
				int year=ToolTimeCustomer.getYearM(invoiceDate);
				importParam.put("title_day","TP. Hồ Chí Minh, Ngày "+String.format("%02d",day)+" Tháng "+String.format("%02d",month)+" Năm "+String.format("%04d",year));
				importParam.put("movement_commands_no", invoiceCrud.getMovement_commands_no());
				Customer customer = invoiceCrud.getCustomer();
				if (customer != null) {
					importParam.put("customer_name", customer.getCustomer_name());
					importParam.put("address",customer.getAddress());
				}
				JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, new JREmptyDataSource());
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				//phiếu xuất kho kiêm vcnb
				List<PhieuXuatKhoKiemVCNB> list=new ArrayList<>();
				/*{invoice_date:'',customer_id:0,movement_commands_no:'' }*/
				JsonObject json=new JsonObject();
				json.addProperty("invoice_date",ToolTimeCustomer.convertDateToString(invoiceCrud.getInvoice_date(),"dd/MM/yyyy"));
				json.addProperty("customer_id",customer==null ?0 :customer.getId());
				json.addProperty("movement_commands_no",invoiceCrud.getMovement_commands_no());
				invoiceService.reportPhieuXuatKhoKiemVCNB(JsonParserUtil.getGson().toJson(json), list);
				String reportPath2= FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/invoice/bangkephieuxuatkiemvcnb.jasper");
				Map<String, Object> importParam2 = new HashMap<String, Object>();
				importParam2.put("movement_commands_no", invoiceCrud.getMovement_commands_no());
				importParam2.put("list_data", list);
				JasperPrint jasperPrint2 = JasperFillManager.fillReport(reportPath2, importParam2, new JREmptyDataSource());
				byte[] data2 = JasperExportManager.exportReportToPdf(jasperPrint2);
				List<byte[]> listByte=new ArrayList<>();
				listByte.add(data);
				listByte.add(data2);
				String ba = Base64.getEncoder().encodeToString(mergePDF(listByte));
				current.executeScript("utility.printPDF('" + ba + "')");
			}else{
				current.executeScript("swaldesigntimer('Xảy ra lỗi','Hóa đơn không tồn tại','warning',2000)");
			}
		}catch(Exception e){
			logger.error("InvoiceBean.exportLenhDieuDongNhieuPhieu:"+e.getMessage(),e);
		}
	}
	private  byte[] mergePDF(List<byte[]> pdfFilesAsByteArray) throws DocumentException, IOException {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		Document document = null;
		PdfCopy writer = null;
		for (byte[] pdfByteArray : pdfFilesAsByteArray) {

			try {
				PdfReader reader = new PdfReader(pdfByteArray);
				int numberOfPages = reader.getNumberOfPages();

				if (document == null) {
					document = new Document(reader.getPageSizeWithRotation(1));
					writer = new PdfCopy(document, outStream); // new
					document.open();
				}
				PdfImportedPage page;
				for (int i = 0; i < numberOfPages;) {
					++i;
					page = writer.getImportedPage(reader, i);
					writer.addPage(page);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		document.close();
		outStream.close();
		return outStream.toByteArray();

	}
	public void exportLenhDieuDong(){
		PrimeFaces current=PrimeFaces.current();
		try{
			String reportPath = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/resources/reports/invoice/lenhdieudong.jasper");
			Map<String, Object> importParam = new HashMap<String, Object>();
			Locale locale = new Locale("vi", "VI");
			importParam.put(JRParameter.REPORT_LOCALE, locale);
			importParam.put("logo", FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/resources/gfx/lixco_logo.png"));
			List<LenhDieuDong> list = new ArrayList<>();
			invoiceService.reportLenhDieuDong(invoiceCrud.getId(), list);
			importParam.put("list_data", list);
			Date invoiceDate=invoiceCrud.getInvoice_date();
			importParam.put("voucher_code", invoiceCrud.getVoucher_code());
			int day=ToolTimeCustomer.getDayM(invoiceDate);
			int month=ToolTimeCustomer.getMonthM(invoiceDate);
			int year=ToolTimeCustomer.getYearM(invoiceDate);
			importParam.put("title_day","TP. Hồ Chí Minh, Ngày "+String.format("%02d",day)+" Tháng "+String.format("%02d",month)+" Năm "+String.format("%04d",year));
			importParam.put("movement_commands_no", invoiceCrud.getMovement_commands_no());
			Car car=invoiceCrud.getCar();
			importParam.put("license_plate", car==null ?"" :car.getLicense_plate());
			Customer customer = invoiceCrud.getCustomer();
			if (customer != null) {
				importParam.put("customer_name", customer.getCustomer_name());
				importParam.put("address",customer.getAddress());
			}
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, new JREmptyDataSource());
			byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
			String ba = Base64.getEncoder().encodeToString(data);
			current.executeScript("utility.printPDF('" + ba + "')");
		}catch (Exception e) {
			logger.error("InvoiceBean.exportLenhDieuDong:"+e.getMessage(),e);
		}
	}
	public void exportPhieuXuatKho(){
		PrimeFaces current=PrimeFaces.current();
		try{
			String reportPath = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/resources/reports/invoice/phieuxuatkho.jasper");
			Map<String, Object> importParam = new HashMap<String, Object>();
			Locale locale = new Locale("vi", "VI");
			importParam.put(JRParameter.REPORT_LOCALE, locale);
			importParam.put("logo", FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/resources/gfx/lixco_logo.png"));
			List<PhieuXuatKho> list = new ArrayList<>();
			invoiceService.reportPhieuXuatKho(invoiceCrud.getId(), list);
			importParam.put("list_data", list);
			Date invoiceDate=invoiceCrud.getInvoice_date();
			importParam.put("voucher_code", invoiceCrud.getVoucher_code());
			int day=ToolTimeCustomer.getDayM(invoiceDate);
			int month=ToolTimeCustomer.getMonthM(invoiceDate);
			int year=ToolTimeCustomer.getYearM(invoiceDate);
			importParam.put("day", day);importParam.put("month", month);importParam.put("year", year);
			importParam.put("warehouse_name", "CÔNG TY CỔ PHẦN BỘT GIẶT LIX");
			importParam.put("ie_reason",invoiceCrud.getIe_reason());
			Car car=invoiceCrud.getCar();
			importParam.put("license_plate", car==null ?"" :car.getLicense_plate());
			Customer customer = invoiceCrud.getCustomer();
			if (customer != null) {
				importParam.put("customer_name", customer.getCustomer_name());
			}
			// tổng thùng
			double totalQuantityExport = 0;
			double totalAmount = 0;
			for (PhieuXuatKho i : list) {
				// số lượng và tổng tiền
				totalQuantityExport=BigDecimal.valueOf(totalQuantityExport).add(BigDecimal.valueOf(i.getQuantity())).doubleValue();
				totalAmount=BigDecimal.valueOf(totalAmount).add(BigDecimal.valueOf(i.getTotal_amount())).doubleValue();
			}
			//làm tròn
			totalAmount=Math.round(totalAmount*1000)/1000;
			importParam.put("total_quantity", totalQuantityExport);
			importParam.put("total_amount", totalAmount);
			importParam.put("words_total_amount", ConvertNumberToText.docSo(totalAmount, "ĐỒNG"));
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, new JREmptyDataSource());
			byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
			String ba = Base64.getEncoder().encodeToString(data);
			current.executeScript("utility.printPDF('" + ba + "')");
		}catch (Exception e) {
			logger.error("InvoiceBean.exportPhieuXuatKho:"+e.getMessage(),e);
		}
	}
	public void exportPhieuNhapKho(){
		PrimeFaces current=PrimeFaces.current();
		try{
			String reportPath = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/resources/reports/invoice/phieunhapkho.jasper");
			Map<String, Object> importParam = new HashMap<String, Object>();
			Locale locale = new Locale("vi", "VI");
			importParam.put(JRParameter.REPORT_LOCALE, locale);
			List<PhieuNhapKho> list = new ArrayList<>();
			invoiceService.reportPhieuNhapKho(invoiceCrud.getId(), list);
			importParam.put("list_data", list);
			Date invoiceDate=invoiceCrud.getInvoice_date();
			importParam.put("voucher_code", invoiceCrud.getVoucher_code());
			importParam.put("invoice_date", invoiceDate);
			int day=ToolTimeCustomer.getDayM(invoiceDate);
			int month=ToolTimeCustomer.getMonthM(invoiceDate);
			int year=ToolTimeCustomer.getYearM(invoiceDate);
			importParam.put("day", String.format("%02d",day));importParam.put("month", String.format("%02d",month));importParam.put("year",String.format("%04d",year));
			importParam.put("warehouse_name", "CÔNG TY CỔ PHẦN BỘT GIẶT LIX");
			importParam.put("ie_categories_name",invoiceCrud.getIe_categories()==null ? "" :invoiceCrud.getIe_categories().getContent());
			Car car=invoiceCrud.getCar();
			importParam.put("license_plate", car==null ? "" :car.getLicense_plate());
			Customer customer = invoiceCrud.getCustomer();
			if (customer != null) {
				importParam.put("customer_name", customer.getCustomer_name());
			}
			// tổng thùng
			double totalAmount = 0;
			for (PhieuNhapKho i : list) {
				// số lượng và tổng tiền
				totalAmount=BigDecimal.valueOf(totalAmount).add(BigDecimal.valueOf(i.getTotal_amount())).doubleValue();
			}
			//làm tròn
			totalAmount=Math.round(totalAmount*1000)/1000;
			importParam.put("total_amount", totalAmount);
			importParam.put("words_total_amount", ConvertNumberToText.docSo(totalAmount, "ĐỒNG"));
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, new JREmptyDataSource());
			byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
			String ba = Base64.getEncoder().encodeToString(data);
			current.executeScript("utility.printPDF('" + ba + "')");
		}catch (Exception e) {
			logger.error("InvoiceBean.exportPhieuNhapKho:"+e.getMessage(),e);
		}
	}
	public void search(){
		try{
			/*{ invoice_info:{from_date:'',to_date:'',customer_id:0,invoice_code:'',voucher_code:'',ie_categories_id:0,po_no:'',order_code:'',order_voucher:'',payment:-1,status:-1}, page:{page_index:0, page_size:0}}*/
			navigationInfo.setLimit(pageSize);
			navigationInfo.setMaxIndices(5);
			listInvoice=new ArrayList<>();
			PagingInfo page=new PagingInfo();
			JsonObject jsonInfo=new JsonObject();
			jsonInfo.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("customer_id", customerSearch ==null ? 0 :customerSearch.getId());
			jsonInfo.addProperty("invoice_code", invoiceCodeSearch);
			jsonInfo.addProperty("voucher_code", voucherCodeSearch);
			jsonInfo.addProperty("ie_categories_id", ieCategoriesSearch==null ? 0 :ieCategoriesSearch.getId());
			jsonInfo.addProperty("po_no", poNoSearch);
			jsonInfo.addProperty("order_code",orderCodeSearch);
			jsonInfo.addProperty("order_voucher",orderVoucherSearch);
			jsonInfo.addProperty("payment", paymentSearch);
			jsonInfo.addProperty("exported",exportedSearch);
			JsonObject json=new JsonObject();
			json.add("invoice_info", jsonInfo);
			invoiceService.search(JsonParserUtil.getGson().toJson(json),  listInvoice);
			navigationInfo.setTotalRecords((int) page.getTotalRow());
			navigationInfo.setCurrentPage(1);
		}catch(Exception e){
			logger.error("InvoiceBean.search:"+e.getMessage(),e);
		}
	}
	private void initRowPerPage() {
		try {
			listRowPerPage = new ArrayList<Integer>();
			listRowPerPage.add(50);
			listRowPerPage.add(80);
			listRowPerPage.add(120);
			pageSize = listRowPerPage.get(0);
		} catch (Exception e) {
			logger.error("InvoiceBean.initRowPerPage:" + e.getMessage(), e);
		}
	}
	public void paginatorChange(int currentPage) {
		try {
			/*{ invoice_info:{from_date:'',to_date:'',customer_id:0,invoice_code:'',voucher_code:'',ie_categories_id:0,po_no:'',order_code:'',order_voucher:'',payment:-1,status:-1}, page:{page_index:0, page_size:0}}*/
			navigationInfo.setLimit(pageSize);
			navigationInfo.setMaxIndices(5);
			listInvoice = new ArrayList<>();
			PagingInfo page = new PagingInfo();
			// thông tin phân trang
			JsonObject jsonInfo=new JsonObject();
			jsonInfo.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("customer_id", customerSearch ==null ? 0 :customerSearch.getId());
			jsonInfo.addProperty("invoice_code", invoiceCodeSearch);
			jsonInfo.addProperty("voucher_code", voucherCodeSearch);
			jsonInfo.addProperty("ie_categories_id", ieCategoriesSearch==null ? 0 :ieCategoriesSearch.getId());
			jsonInfo.addProperty("po_no", poNoSearch);
			jsonInfo.addProperty("order_code",orderCodeSearch);
			jsonInfo.addProperty("order_voucher",orderVoucherSearch);
			jsonInfo.addProperty("payment", paymentSearch);
			
			JsonObject json=new JsonObject();
			json.add("invoice_info", jsonInfo);
			invoiceService.search(JsonParserUtil.getGson().toJson(json),  listInvoice);
			navigationInfo.setTotalRecords((int) page.getTotalRow());
			navigationInfo.setCurrentPage(currentPage);
		} catch (Exception e) {
			logger.error("InvoiceBean.paginatorChange:" + e.getMessage(), e);
		}
	}
	public void createdNew(){
		PrimeFaces current=PrimeFaces.current();
		try{
			invoiceCrud=new Invoice();
			//tạo mã đơn hàng
			invoiceCrud.setInvoice_date(ToolTimeCustomer.getFirstDateOfDay(new  Date()));
			invoiceCrud.setCreated_by(account.getMember().getName());
			listWrapInvoiceDetail=new ArrayList<>();
		}catch(Exception e){
			logger.error("InvoiceBean.createNew:" + e.getMessage(), e);
		}
		current.executeScript("PF('tablect').clearFilters();");
	}
	public void showEditInvoice(){
		PrimeFaces current=PrimeFaces.current();
		try{
			if(invoiceSelect !=null ){
				invoiceCrud=invoiceSelect.clone();
				//show thông tin chi tiết hóa đơn
				listWrapInvoiceDetail=new ArrayList<>();
				List<InvoiceDetail> listInvoiceDetail=new ArrayList<>();
				invoiceDetailService.selectByInvoice(invoiceCrud.getId(), listInvoiceDetail);
				for(InvoiceDetail dt:listInvoiceDetail){
					List<ExportBatch> list=new ArrayList<>();
					invoiceService.getListExportBatch(dt.getId(), list);
					WrapInvoiceDetailReqInfo w=new WrapInvoiceDetailReqInfo(dt, list);
					listWrapInvoiceDetail.add(w);
				}
				//tính tổng tiền hóa đơn
				sumInvoice();
			}
		}catch(Exception e){
			logger.error("InvoiceBean.showEditInvoice:" + e.getMessage(), e);
		}
		current.executeScript("PF('tablect').clearFilters();");
	}



	
	public void showDialogPrint() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (invoiceCrud != null && invoiceCrud.getId() != 0 && listWrapInvoiceDetail != null
					&& listWrapInvoiceDetail.size() > 0) {
				current.executeScript("PF('idlg2').show();");
			}else{
				current.executeScript(
						"swaldesigntimer('Cảnh báo', 'Hóa đơn không tồn tại!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("InvoiceBean.showDialogPrint:" + e.getMessage(), e);
		}
	}
	public double sumInvoice(){
		try{
			if(listWrapInvoiceDetail !=null && listWrapInvoiceDetail.size()>0){
				double total=0;
				for(WrapInvoiceDetailReqInfo w:listWrapInvoiceDetail){
					total=BigDecimal.valueOf(total).add(BigDecimal.valueOf(w.getInvoice_detail().getTotal())).doubleValue();
				}
				return total;
			}
		}catch(Exception e){
			logger.error("InvoiceBean.sumInvoice:" + e.getMessage(), e);
		}
		return 0;
	}
	public void exportPDF(){
		try{
			
		}catch (Exception e) {
			logger.error("InvoiceBean.exportPDF:" + e.getMessage(), e);
		}
	}
	public void destroyInvoice(){
		PrimeFaces current=PrimeFaces.current();
		try{
			current.executeScript(
					"swaldesigntimer('Cảnh báo!', 'Chức năng đang phát triển!','warning',2000);");
		}catch (Exception e) {
			logger.error("InvoiceBean.destroyInvoice:" + e.getMessage(), e);
		}
	}
	public void deleteExportBatch(ExportBatch item,List<ExportBatch> list){
		PrimeFaces current=PrimeFaces.current();
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			WrapDelExportBatchReqInfo t=new WrapDelExportBatchReqInfo(item.getId(), account.getMember().getId(), account.getMember().getName());
			Message message=new Message();
			int code=processLogicInvoiceService.deleteExportBatch(t, message);
			if(code==0){
				list.remove(item);
				notify.success("xóa lô hàng xuất thành công!");
			}else{
				//đưa ra mã lỗi
				String m=message.getUser_message()+" \\n"+message.getInternal_message();
				current.executeScript(
						"swaldesignclose('Xảy ra lỗi', '"+m+"','warning');");
			}
		}catch (Exception e) {
			logger.error("InvoiceBean.deleteExportBatch:" + e.getMessage(), e);
		}
	}
	public void showDialogExport(WrapInvoiceDetailReqInfo item){
		PrimeFaces current=PrimeFaces.current();
		try{
			wrapInvoiceDetailPick =item.clone();
			InvoiceDetail invoiceDetail=item.getInvoice_detail();
			listWrapExportData=new ArrayList<>();
			//lấy danh sách lô hàng đã xuất và chưa xuất(còn tồn) cho chi tiết này
			//lưu ý chỉ hiển thị lô hàng trạng thái đã hoàn thành
			List<ExportBatch> listExportBatch=new ArrayList<>();
			List<Batch> listBatch=new ArrayList<>();
			batchService.exportBatchByInvoiceDetail(invoiceDetail.getProduct().getId(), invoiceDetail.getId(), listBatch);
			for(Batch b:listBatch){
				ExportBatch e=new ExportBatch();
				e.setBatch(b);
				listExportBatch.add(e);
			}
			//prepare list wrap
			for(ExportBatch ex:listExportBatch){
				WrapExportDataReqInfo w=new WrapExportDataReqInfo();
				w.setExport_batch(ex);
				w.setQuantity_export(0);
				w.setSelect(ex.isSelect());
				listWrapExportData.add(w);
			}
			current.executeScript("PF('dlgexport').show();");
		}catch (Exception e) {
			logger.error("InvoiceBean.showDialogExport:" + e.getMessage(), e);
		}
	}
	public void selectExportBatch(WrapExportDataReqInfo t){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			Batch batch=t.getExport_batch().getBatch();
			InvoiceDetail invoiceDetailPick=wrapInvoiceDetailPick.getInvoice_detail();
			if(t.isSelect()){ 
				//load lại Batch hiện tại
				BatchReqInfo brq=new BatchReqInfo();
				batchService.selectById(batch.getId(), brq);
				batch=brq.getBatch();
				t.getExport_batch().setBatch(batch);
				//kiểm tra sản phẩm lô hàng có cài quy cách chưa.
				if(batch.getProduct().getSpecification()==0){
					current.executeScript(
							"swaldesignclose('Cảnh báo', 'Quy cách sản phẩm bị sai:'"+invoiceDetailPick.getProduct().getProduct_code()+",'warning');");
					t.setSelect(false);
					return;
				}
				//số lượng tồn của lô hàng
				double soTon=batch.getRemain_quantity();
				//số lượng yêu cầu và số lượng thực xuất chi tiết hóa đơn
				double quantity=invoiceDetailPick.getQuantity();
				double quantityReal=invoiceDetailPick.getReal_quantity();
				//số lượng yêu cầu còn lại chưa xuất
				double quantityEx=BigDecimal.valueOf(quantity).subtract(BigDecimal.valueOf(quantityReal)).doubleValue();
				if(quantityEx==0)
				{
					//đã đủ số lượng.
					t.setSelect(false);
					notify.warning("Đã xuất đủ số lượng!");
					return;
				}
				double soluongCal=BigDecimal.valueOf(quantityEx).subtract(BigDecimal.valueOf(soTon)).doubleValue();
				if(soluongCal >=0){
					t.setQuantity_export(soTon);
					//tăng số lượng thực xuất
					quantityReal=BigDecimal.valueOf(quantityReal).add(BigDecimal.valueOf(soTon)).doubleValue();
				}else{
					t.setQuantity_export(quantityEx);
					//tăng số lượng thực xuất
					quantityReal=BigDecimal.valueOf(quantityReal).add(BigDecimal.valueOf(quantityEx)).doubleValue();
				}
				invoiceDetailPick.setReal_quantity(quantityReal);
			}else{
				double quantityEx=t.getQuantity_export();
				double quantityReal=BigDecimal.valueOf(invoiceDetailPick.getReal_quantity()).subtract(BigDecimal.valueOf(quantityEx)).doubleValue();
				invoiceDetailPick.setReal_quantity(quantityReal);
				t.setQuantity_export(0);
			}
		}catch (Exception e) {
			logger.error("InvoiceBean.selectExportBatch:" + e.getMessage(), e);
			notify.message("Lỗi dữ liệu không hợp lệ");
			current.executeScript("PF('dlgexport').hide();");
		}
		PrimeFaces.current().ajax().update("menuformid:tabview1:tablect:"+listWrapInvoiceDetail.indexOf(wrapInvoiceDetailPick)+":tableInner");
	}
	public void changeQuantityExportBatch(WrapExportDataReqInfo item){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			InvoiceDetail invoiceDetailPick=wrapInvoiceDetailPick.getInvoice_detail();
			if(item.isSelect()){
				//load lại lô hàng
				BatchReqInfo brq=new BatchReqInfo();
				batchService.selectById(item.getExport_batch().getBatch().getId(),brq);
				Batch batch=brq.getBatch();
				item.getExport_batch().setBatch(batch);
				//tính số lượng đơn vị tính
				double quantityEdit=item.getQuantity_export();
				//nếu sản phẩm chưa cài quy cách thì thông báo.
				item.setQuantity_export(quantityEdit);
				//số lượng tồn của lô hàng
				double quantityTon=batch.getRemain_quantity();
				double sum=0;
				for(WrapExportDataReqInfo ex:listWrapExportData){
					if(ex.isSelect()){
					   sum=BigDecimal.valueOf(sum).add(BigDecimal.valueOf(ex.getQuantity_export())).doubleValue();
					}
				}
				//tính tổng lượng đã xuất trước đó (thực tế).
				List<ExportBatch> listExportBatchReal=wrapInvoiceDetailPick.getList_export_batch();
				double sumReal=0;
				for(ExportBatch r:listExportBatchReal){
					sumReal=BigDecimal.valueOf(sumReal).add(BigDecimal.valueOf(r.getQuantity())).doubleValue();
				}
				double totalQuantity=BigDecimal.valueOf(sum).add(BigDecimal.valueOf(sumReal)).doubleValue();
				if(quantityEdit>quantityTon){
					//trả lại trạng thái trước.
					item.setQuantity_export(0);
					invoiceDetailPick.setReal_quantity(sumReal);
					notify.warning("Số lượng xuất lớn hơn số lượng tồn.");
					return;
				}
				//nếu tổng xuất lớn hơn số lượng yêu cầu
				if( totalQuantity >invoiceDetailPick.getQuantity()){
					item.setQuantity_export(0);
					invoiceDetailPick.setReal_quantity(sumReal);
					notify.warning("Số lượng xuất lớn hơn số lượng yêu cầu.");
					return;
				}
				invoiceDetailPick.setReal_quantity(totalQuantity);
			}
		}catch (Exception e) {
			logger.error("InvoiceBean.changeQuantityExportBatch:" + e.getMessage(), e);
			notify.message("Lổi dữ liệu không hợp lệ");
			current.executeScript("PF('dlgexport').hide();");
		}
	}
	public void saveOrUpdateExportBatch(){
		PrimeFaces current=PrimeFaces.current();
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			if(listWrapExportData !=null && listWrapExportData.size()>0){
				InvoiceDetail invoiceDetailPick= wrapInvoiceDetailPick.getInvoice_detail();
				//prepare wrap  để lưu/ cập nhật dữ liệu.
				WrapDataInvoiceDetail  data=new WrapDataInvoiceDetail(invoiceDetailPick, new ArrayList<>(),account.getMember().getName());
				for(WrapExportDataReqInfo p:listWrapExportData){
					if(p.isSelect()){
						data.getList_wrap_export_data().add(p);
					}
				}
				//lưu lô hàng 
				Message message=new Message();
				int code=processLogicInvoiceService.saveListWrapExportData(data, message);
				if(code>=0){
					//load lại exportbatch trong bảng hiển thị
					List<ExportBatch> list=new ArrayList<>();
					invoiceService.getListExportBatch(invoiceDetailPick.getId(), list);
					InvoiceDetailReqInfo invoiceDetailReqInfo=new InvoiceDetailReqInfo();
					invoiceDetailService.selectById(invoiceDetailPick.getId(),invoiceDetailReqInfo);
					int index=listWrapInvoiceDetail.indexOf(wrapInvoiceDetailPick);
					if(index !=-1){
						WrapInvoiceDetailReqInfo main= listWrapInvoiceDetail.get(index);
						main.setInvoice_detail(invoiceDetailReqInfo.getInvoice_detail());
						main.setList_export_batch(list);
						//cập nhật lại phần tử chọn lựa cho dialog hiện thị
						wrapInvoiceDetailPick= main.clone();
					}
					//load danh sách lô hàng trên dialog
					//lấy danh sách lô hàng còn tồn cho chi tiết này
					//lưu ý chỉ hiển thị lô hàng trạng thái đã hoàn thành
					listWrapExportData=new ArrayList<>();
					List<ExportBatch> listExportBatch=new ArrayList<>();
					List<Batch> listBatch=new ArrayList<>();
					batchService.exportBatchByInvoiceDetail(invoiceDetailPick.getProduct().getId(),invoiceDetailPick.getId(), listBatch);
					for(Batch b:listBatch){
						ExportBatch e=new ExportBatch();
						e.setBatch(b);
						listExportBatch.add(e);
					}
					//prepare list wrap
					for(ExportBatch ex:listExportBatch){
						WrapExportDataReqInfo w=new WrapExportDataReqInfo();
						w.setExport_batch(ex);
						w.setQuantity_export(0);
						w.setSelect(ex.isSelect());
						listWrapExportData.add(w);
					}
					//update current row.
					notify.success("Lưu lô hàng thành công!");
				}else{
					String m=message.getUser_message()+" \\n"+message.getInternal_message();
					current.executeScript(
							"swaldesignclose('Xảy ra lỗi', '"+m+"','warning');");
				}
			}
		}catch (Exception e) {
			logger.error("InvoiceBean.saveOrUpdateExportBatch:" + e.getMessage(), e);
			current.executeScript("PF('dlgexport').hide();");
		}
		current.executeScript("PF('tablect').clearFilters();");
		PrimeFaces.current().ajax().update("menuformid:tabview1:tablect:"+listWrapInvoiceDetail.indexOf(wrapInvoiceDetailPick)+":tableInner");
		PrimeFaces.current().ajax().update("menuformid:tabview1:tablect:"+listWrapInvoiceDetail.indexOf(wrapInvoiceDetailPick)+":realExport");
	}
	public List<Customer> completeCustomer(String text){
		try{
			List<Customer> list=new ArrayList<>();
			customerService.complete(formatHandler.converViToEn(text), list);
			return list;
		}catch(Exception e){
			logger.error("InvoiceBean.completeCustomer:"+e.getMessage(),e);
		}
		return null;
	}
	@Override
	protected Logger getLogger() {
		return logger;
	}

	public Invoice getInvoiceCrud() {
		return invoiceCrud;
	}

	public void setInvoiceCrud(Invoice invoiceCrud) {
		this.invoiceCrud = invoiceCrud;
	}

	public Invoice getInvoiceSelect() {
		return invoiceSelect;
	}

	public void setInvoiceSelect(Invoice invoiceSelect) {
		this.invoiceSelect = invoiceSelect;
	}

	public List<Invoice> getListInvoice() {
		return listInvoice;
	}

	public void setListInvoice(List<Invoice> listInvoice) {
		this.listInvoice = listInvoice;
	}
	public WrapInvoiceDetailReqInfo getWrapInvoiceDetailCrud() {
		return wrapInvoiceDetailCrud;
	}
	public void setWrapInvoiceDetailCrud(WrapInvoiceDetailReqInfo wrapInvoiceDetailCrud) {
		this.wrapInvoiceDetailCrud = wrapInvoiceDetailCrud;
	}
	public WrapInvoiceDetailReqInfo getWrapInvoiceDetailSelect() {
		return wrapInvoiceDetailSelect;
	}
	public void setWrapInvoiceDetailSelect(WrapInvoiceDetailReqInfo wrapInvoiceDetailSelect) {
		this.wrapInvoiceDetailSelect = wrapInvoiceDetailSelect;
	}
	public List<WrapInvoiceDetailReqInfo> getListWrapInvoiceDetail() {
		return listWrapInvoiceDetail;
	}
	public void setListWrapInvoiceDetail(List<WrapInvoiceDetailReqInfo> listWrapInvoiceDetail) {
		this.listWrapInvoiceDetail = listWrapInvoiceDetail;
	}
	public FormatHandler getFormatHandler() {
		return formatHandler;
	}
	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
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
	public Customer getCustomerSearch() {
		return customerSearch;
	}
	public void setCustomerSearch(Customer customerSearch) {
		this.customerSearch = customerSearch;
	}
	public String getInvoiceCodeSearch() {
		return invoiceCodeSearch;
	}
	public void setInvoiceCodeSearch(String invoiceCodeSearch) {
		this.invoiceCodeSearch = invoiceCodeSearch;
	}
	public String getVoucherCodeSearch() {
		return voucherCodeSearch;
	}
	public void setVoucherCodeSearch(String voucherCodeSearch) {
		this.voucherCodeSearch = voucherCodeSearch;
	}
	public IECategories getIeCategoriesSearch() {
		return ieCategoriesSearch;
	}
	public void setIeCategoriesSearch(IECategories ieCategoriesSearch) {
		this.ieCategoriesSearch = ieCategoriesSearch;
	}
	public String getPoNoSearch() {
		return poNoSearch;
	}
	public void setPoNoSearch(String poNoSearch) {
		this.poNoSearch = poNoSearch;
	}
	public String getOrderVoucherSearch() {
		return orderVoucherSearch;
	}
	public void setOrderVoucherSearch(String orderVoucherSearch) {
		this.orderVoucherSearch = orderVoucherSearch;
	}
	public String getOrderCodeSearch() {
		return orderCodeSearch;
	}
	public void setOrderCodeSearch(String orderCodeSearch) {
		this.orderCodeSearch = orderCodeSearch;
	}
	public int getPaymentSearch() {
		return paymentSearch;
	}
	public void setPaymentSearch(int paymentSearch) {
		this.paymentSearch = paymentSearch;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public NavigationInfo getNavigationInfo() {
		return navigationInfo;
	}
	public void setNavigationInfo(NavigationInfo navigationInfo) {
		this.navigationInfo = navigationInfo;
	}
	public List<Integer> getListRowPerPage() {
		return listRowPerPage;
	}
	public void setListRowPerPage(List<Integer> listRowPerPage) {
		this.listRowPerPage = listRowPerPage;
	}
	public List<WrapInvoiceDetailReqInfo> getListWrapInvoiceDetailFilter() {
		return listWrapInvoiceDetailFilter;
	}
	public void setListWrapInvoiceDetailFilter(List<WrapInvoiceDetailReqInfo> listWrapInvoiceDetailFilter) {
		this.listWrapInvoiceDetailFilter = listWrapInvoiceDetailFilter;
	}
	public WrapInvoiceDetailReqInfo getWrapInvoiceDetailPick() {
		return wrapInvoiceDetailPick;
	}
	public void setWrapInvoiceDetailPick(WrapInvoiceDetailReqInfo wrapInvoiceDetailPick) {
		this.wrapInvoiceDetailPick = wrapInvoiceDetailPick;
	}
	public List<WrapExportDataReqInfo> getListWrapExportData() {
		return listWrapExportData;
	}
	public void setListWrapExportData(List<WrapExportDataReqInfo> listWrapExportData) {
		this.listWrapExportData = listWrapExportData;
	}
	public List<ReportTypeIEInVoice> getListSelectReport() {
		return listSelectReport;
	}
	public void setListSelectReport(List<ReportTypeIEInVoice> listSelectReport) {
		this.listSelectReport = listSelectReport;
	}
	public ReportTypeIEInVoice getReportTypeIEInVoiceSelect() {
		return reportTypeIEInVoiceSelect;
	}
	public void setReportTypeIEInVoiceSelect(ReportTypeIEInVoice reportTypeIEInVoiceSelect) {
		this.reportTypeIEInVoiceSelect = reportTypeIEInVoiceSelect;
	}
	public List<IECategories> getListIECategories() {
		return listIECategories;
	}
	public void setListIECategories(List<IECategories> listIECategories) {
		this.listIECategories = listIECategories;
	}
	public int getExportedSearch() {
		return exportedSearch;
	}
	public void setExportedSearch(int exportedSearch) {
		this.exportedSearch = exportedSearch;
	}
}
