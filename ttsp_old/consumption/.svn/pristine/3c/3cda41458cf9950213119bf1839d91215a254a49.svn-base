package lixco.com.bean_report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import lixco.com.entity.Product;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.ThongKeTheoThang;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

@Named
@ViewScoped
public class ReportThongKeTheoThangBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private ICustomerService customerService;
	@Inject
	private IProductService productService;
	@Inject
	private IIECategoriesService iECategoriesService;
	@Inject
	private IReportService reportService;
	private Date fromDateSearch;
	private Date toDateSearch;
	private Product productSearch;
	private Customer customerSearch;
	private IECategories iECategoriesSearch;
	private List<IECategories> listIECategories;
	private List<ThongKeTheoThang> listThongKeTheoThang;
	private List<ThongKeTheoThang> listThongKeTheoGioFilter;
	private FormatHandler formatHandler;
	@Override
	protected void initItem() {
		try{
			listIECategories=new ArrayList<>();
			iECategoriesService.selectAll(listIECategories);
			formatHandler=FormatHandler.getInstance();
			int month=ToolTimeCustomer.getMonthCurrent();
			int year=ToolTimeCustomer.getYearCurrent();
			fromDateSearch=ToolTimeCustomer.getDateMinCustomer(month, year);
			toDateSearch=ToolTimeCustomer.getDateMaxCustomer(month, year);
			
		}catch (Exception e) {
			logger.error("ReportThongKeTheoThangBean.initItem:"+e.getMessage(),e);
		}
	}
	public List<Customer> completeCustomer(String text){
		try{
			List<Customer> list=new ArrayList<>();
			customerService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportThongKeTheoThangBean.completeCustomer:"+e.getMessage(),e);
		}
		return null;
	}
	public List<Product> completeProduct(String text){
		try{
			List<Product> list=new ArrayList<>();
			productService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportThongKeTheoThangBean.completeProduct:"+e.getMessage(),e);
		}
		return null;
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
	public void search(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			/*{from_date:'',to_date:'',product_id:0,customer_id:0,ie_categories_id:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch==null ? 0 :productSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0: customerSearch.getId());
			json.addProperty("ie_categories_id",iECategoriesSearch==null ? 0 :iECategoriesSearch.getId());
			listThongKeTheoThang=new ArrayList<ThongKeTheoThang>();
			reportService.reportThongKeTheoThang(JsonParserUtil.getGson().toJson(json), listThongKeTheoThang);
			if(listThongKeTheoThang.size()==0){
				notify.warning("Không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("ReportThongKeTheoThangBean.search:"+e.getMessage(),e);
		}
		current.executeScript("PF('tablenc').clearFilters();");
	}
	public void reportThongKeSanLuongExcel(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{from_date:'',to_date:'',product_id:0,customer_id:0,ie_categories_id:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch==null ? 0 :productSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0: customerSearch.getId());
			json.addProperty("ie_categories_id",iECategoriesSearch==null ? 0 :iECategoriesSearch.getId());
			List<ThongKeTheoThang> list=new ArrayList<>();
			reportService.reportThongKeTheoThang(JsonParserUtil.getGson().toJson(json), list);
			if(list.size() >0){
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = {"MÃ LOẠI SP","TÊN LOẠI SẢN PHẨM","SẢN LƯỢNG(TẤN)","DOANH THU(TRIỆU ĐỒNG)" };
				results.add(title);
				for (ThongKeTheoThang it : list) {
					Object[] row = {it.getProduct_type_code(),it.getProduct_type_name(),it.getQuantity(),it.getTotal_amount()};
					results.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("thong_ke_ ");
		    	if(fromDateSearch !=null)
		    	   title2.append("tu_ngay "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	if(toDateSearch!=null)
		    	   title2.append(" đến ngày "+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
				ToolReport.printReportExcelRaw(results,title2.toString());
			}else{
				notify.warning("Không có dữ liệu!");
			}
		}catch (Exception e) {
			logger.error("ReportThongKeTheoThangBean.search:"+e.getMessage(),e);
		}
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

	public List<ThongKeTheoThang> getListThongKeTheoThang() {
		return listThongKeTheoThang;
	}

	public void setListThongKeTheoThang(List<ThongKeTheoThang> listThongKeTheoThang) {
		this.listThongKeTheoThang = listThongKeTheoThang;
	}

	public List<ThongKeTheoThang> getListThongKeTheoGioFilter() {
		return listThongKeTheoGioFilter;
	}

	public void setListThongKeTheoGioFilter(List<ThongKeTheoThang> listThongKeTheoGioFilter) {
		this.listThongKeTheoGioFilter = listThongKeTheoGioFilter;
	}

	public FormatHandler getFormatHandler() {
		return formatHandler;
	}

	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
	}
}
