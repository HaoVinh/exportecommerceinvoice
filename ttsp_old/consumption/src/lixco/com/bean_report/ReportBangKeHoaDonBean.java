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

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import com.google.gson.JsonObject;

import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Contract;
import lixco.com.entity.Customer;
import lixco.com.entity.IECategories;
import lixco.com.entity.Product;
import lixco.com.interfaces.IContractService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.BangKeHoaDon;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

@Named
@ViewScoped
public class ReportBangKeHoaDonBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private ICustomerService customerService;
	@Inject
	private IProductService productService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private IIECategoriesService iECategoriesService;
	@Inject
	private IReportService reportService;
	@Inject
	private IContractService contractService;
	private Date fromDateSearch;
	private Date toDateSearch;
	private Product productSearch;
	private Customer customerSearch;
	private Contract contractSearch;
	private IECategories iECategoriesSearch;
	private List<IECategories> listIECategories;
	private List<BangKeHoaDon> listBangKeHoaDon;
	private List<BangKeHoaDon> listBangKeHoaDonFilter;
	private FormatHandler formatHandler;
	
	@Override
	protected void initItem() {
		try{
			/*{from_date:'',to_date:'',product_id:0,customer_id:0,contract_id:0,ie_categories_id:0}*/
			listIECategories=new ArrayList<>();
		    iECategoriesService.selectAll(listIECategories);
		    formatHandler=FormatHandler.getInstance();
			int month=ToolTimeCustomer.getMonthCurrent();
			int year=ToolTimeCustomer.getYearCurrent();
			fromDateSearch=ToolTimeCustomer.getDateMinCustomer(month, year);
			toDateSearch=ToolTimeCustomer.getDateMaxCustomer(month, year);
		}catch (Exception e) {
			logger.error("ReportBangKeHoaDonBean.initItem:"+e.getMessage(),e);
		}
	}
	public List<Product> completeProduct(String text){
		try{
			List<Product> list=new ArrayList<Product>();
			productService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportBangKeHoaDonBean.completeProduct:"+e.getMessage(),e);
		}
		return null;
	}
	public List<Contract> completeContract(String text){
		try{
			List<Contract> list=new ArrayList<Contract>();
			contractService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportBangKeHoaDonBean.completeProduct:"+e.getMessage(),e);
		}
		return null;
	}
	public List<Customer> completeCustomer(String text){
		try{
			List<Customer> list=new ArrayList<>();
			customerService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportBangKeHoaDonBean.completeCustomer:"+e.getMessage(),e);
		}
		return null;
	}
	public void search(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			/*{from_date:'',to_date:'',product_id:0,customer_id:0,contract_id:0,ie_categories_id:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0: contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ? 0 :iECategoriesSearch.getId());
			listBangKeHoaDon=new ArrayList<>();
			reportService.reportBangKeHoaDon(JsonParserUtil.getGson().toJson(json),listBangKeHoaDon);
			if(listBangKeHoaDon.size()==0){
				notify.warning("Không có dữ liệu!");
			}
		}catch (Exception e) {
			logger.error("ReportBangKeHoaDonBean.search:"+e.getMessage(),e);
		}
		current.executeScript("PF('tablenc').clearFilters();");
	}
	public void reportBangKeHoaDon(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{/*{from_date:'',to_date:'',product_id:0,customer_id:0,contract_id:0,ie_categories_id:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0: contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ? 0 :iECategoriesSearch.getId());
			List<BangKeHoaDon> list=new ArrayList<BangKeHoaDon>();
			reportService.reportBangKeHoaDon(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/bang_ke_hoa_don/bang_ke_hoa_don.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
		    	importParam.put("logo", FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/gfx/lixco_logo.png"));
		    	StringBuilder title=new StringBuilder();
		    	title.append("BẢNG KÊ HÓA ĐƠN TỪ NGÀY "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	title.append(" ĐẾN NGÀY "+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
		    	importParam.put("title", title.toString());
		    	importParam.put("list_data", list);
		    	JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, new JREmptyDataSource());

				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
		        current.executeScript("PF('showImg').show();");
		        current.executeScript("utility.showPDFK('" + ba + "','')");
			}else{
				notify.warning("Không có dữ liệu!");
			}
			
		}catch (Exception e) {
			logger.error("ReportBangKeHoaDonBean.reportBangKeHoaDon:"+e.getMessage(),e);
		}
	}
	public void reportBangKeHoaDonExcel(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{from_date:'',to_date:'',product_id:0,customer_id:0,contract_id:0,ie_categories_id:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0: contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ? 0 :iECategoriesSearch.getId());
			List<BangKeHoaDon> list=new ArrayList<BangKeHoaDon>();
			reportService.reportBangKeHoaDon(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "MÃ NX", "KHÁCH HÀNG","ĐƠN VỊ","MAKH","ĐỊA CHỈ","SỐ CHỨNG TỪ","NGÀY HÓA ĐƠN","ID HÓA ĐƠN","SỐ TIỀN","THUẾ TGGT","TỔNG TIỀN",
						"DIỄN GIẢI","NHÂN VIÊN BÁN HÀNG","MÃ NV BÁN HÀNG" };
				results.add(title);
				for (BangKeHoaDon it : list) {
					Object[] row = {it.getIe_categories_code(),it.getCustomer_name(),it.getCompany_name(),it.getCustomer_code(),it.getAddress(),it.getVoucher_code(),it.getInvoice_date(),it.getInvoice_code(),
							it.getTotal_amount(),it.getTotal_tax_amount(),it.getTotal_amount_with_vat(),it.getIe_categories_name(),it.getCreated_by(),it.getCreated_by_id()};
					results.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("bang_ke_hoa_don_ ");
		    	if(fromDateSearch !=null)
		    	   title2.append("tu_ngay "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	if(toDateSearch!=null)
		    	   title2.append(" đến ngày "+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
				ToolReport.printReportExcelRaw(results,title2.toString());
			}else{
				notify.warning("Không có dữ liệu!");
			}
		}catch (Exception e) {
			logger.error("ReportBangKeHoaDonBean.reportBangKeHoaDonExcel:"+e.getMessage(),e);
		}
	}
	@Override
	protected Logger getLogger() {
		return logger;
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

	public Product getProductSearch() {
		return productSearch;
	}

	public void setProductSearch(Product productSearch) {
		this.productSearch = productSearch;
	}

	public Customer getCustomerSearch() {
		return customerSearch;
	}

	public void setCustomerSearch(Customer customerSearch) {
		this.customerSearch = customerSearch;
	}

	public Contract getContractSearch() {
		return contractSearch;
	}

	public void setContractSearch(Contract contractSearch) {
		this.contractSearch = contractSearch;
	}

	public IECategories getiECategoriesSearch() {
		return iECategoriesSearch;
	}

	public void setiECategoriesSearch(IECategories iECategoriesSearch) {
		this.iECategoriesSearch = iECategoriesSearch;
	}

	public List<IECategories> getListIECategories() {
		return listIECategories;
	}

	public void setListIECategories(List<IECategories> listIECategories) {
		this.listIECategories = listIECategories;
	}
	public List<BangKeHoaDon> getListBangKeHoaDon() {
		return listBangKeHoaDon;
	}
	public void setListBangKeHoaDon(List<BangKeHoaDon> listBangKeHoaDon) {
		this.listBangKeHoaDon = listBangKeHoaDon;
	}
	public FormatHandler getFormatHandler() {
		return formatHandler;
	}
	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
	}
	public List<BangKeHoaDon> getListBangKeHoaDonFilter() {
		return listBangKeHoaDonFilter;
	}
	public void setListBangKeHoaDonFilter(List<BangKeHoaDon> listBangKeHoaDonFilter) {
		this.listBangKeHoaDonFilter = listBangKeHoaDonFilter;
	}
}
