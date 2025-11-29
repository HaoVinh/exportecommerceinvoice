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
import lixco.com.entity.Customer;
import lixco.com.entity.IECategories;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.CongNo;
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
public class ReportCongNoBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private ICustomerService customerService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private IIECategoriesService iECategoriesService;
	@Inject
	private IReportService reportService;
	private Date fromDateSearch;
	private Date toDateSearch;
	private ProductType productTypeSearch;
	private List<ProductType> listProductType;
	private Customer customerSearch;
	private IECategories iECategoriesSearch;
	private List<IECategories> listIECategories;
	private List<CongNo> listCongNo;
	private List<CongNo> listCongNoFilter;
	private FormatHandler formatHandler;
	@Override
	protected void initItem() {
		try{
			listIECategories=new ArrayList<>();
			iECategoriesService.selectAll(listIECategories);
			listProductType=new ArrayList<>();
			productTypeService.selectAll(listProductType);
			formatHandler=FormatHandler.getInstance();
			int month=ToolTimeCustomer.getMonthCurrent();
			int year=ToolTimeCustomer.getYearCurrent();
			fromDateSearch=ToolTimeCustomer.getDateMinCustomer(month, year);
			toDateSearch=ToolTimeCustomer.getDateMaxCustomer(month, year);
		}catch (Exception e) {
			logger.error("ReportCongNoBean.initItem:"+e.getMessage(),e);
		}
	}
	public List<Customer> completeCustomer(String text){
		try{
			List<Customer> list=new ArrayList<>();
			customerService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportCongNoBean.completeCustomer:"+e.getMessage(),e);
		}
		return null;
	}
	public void search(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			/*{from_date:'',to_date:'',product_type_id:0,customer_id:0,ie_categories_id:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_type_id", productTypeSearch !=null ? productTypeSearch.getId() : 0);
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ? 0 :iECategoriesSearch.getId());
			listCongNo=new ArrayList<>();
			reportService.reportCongNo(JsonParserUtil.getGson().toJson(json),listCongNo);
			if(listCongNo.size()==0){
				notify.warning("Không có dữ liệu!");
			}
		}catch (Exception e) {
			logger.error("ReportCongNoBean.search:"+e.getMessage(),e);
		}
		current.executeScript("PF('tablenc').clearFilters();");
	}
	public void reportCongNo(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{/*{from_date:'',to_date:'',product_type_id:0,customer_id:0,ie_categories_id:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_type_id", productTypeSearch !=null ? productTypeSearch.getId() : 0);
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ? 0 :iECategoriesSearch.getId());
			List<CongNo> list=new ArrayList<>();
			reportService.reportCongNo(JsonParserUtil.getGson().toJson(json),list);
			if(list.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/cong_no/cong_no.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
		    	importParam.put("logo", FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/gfx/lixco_logo.png"));
		    	StringBuilder title=new StringBuilder();
		    	title.append("DOANH THU KHÁCH HÀNG TỪ NGÀY "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
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
			logger.error("ReportCongNoBean.reportCongNo:"+e.getMessage(),e);
		}
	}
	public void reportCongNoExcel(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_type_id", productTypeSearch !=null ? productTypeSearch.getId() : 0);
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ? 0 :iECategoriesSearch.getId());
			List<CongNo> list=new ArrayList<>();
			reportService.reportCongNo(JsonParserUtil.getGson().toJson(json),list);
			if(list.size()>0){
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = {"MAKH","TÊN KHÁCH HÀNG","THÀNH PHỐ","TỔNG TIỀN" };
				results.add(title);
				for (CongNo it : list) {
					Object[] row = {it.getCustomer_code(),it.getCustomer_name(),it.getCity_name(),it.getTotal_amount()};
					results.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("doanh_thu_khach_hang_ ");
		    	if(fromDateSearch !=null)
		    	   title2.append("tu_ngay "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	if(toDateSearch!=null)
		    	   title2.append(" đến ngày "+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
				ToolReport.printReportExcelRaw(results,title2.toString());
			}else{
				notify.warning("Không có dữ liệu!");
			}
		}catch (Exception e) {
			logger.error("ReportCongNoBean.reportCongNoExcel:"+e.getMessage(),e);
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

	public Customer getCustomerSearch() {
		return customerSearch;
	}

	public void setCustomerSearch(Customer customerSearch) {
		this.customerSearch = customerSearch;
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

	public List<CongNo> getListCongNo() {
		return listCongNo;
	}

	public void setListCongNo(List<CongNo> listCongNo) {
		this.listCongNo = listCongNo;
	}
	public List<CongNo> getListCongNoFilter() {
		return listCongNoFilter;
	}
	public void setListCongNoFilter(List<CongNo> listCongNoFilter) {
		this.listCongNoFilter = listCongNoFilter;
	}
	public FormatHandler getFormatHandler() {
		return formatHandler;
	}

	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
	}

}
