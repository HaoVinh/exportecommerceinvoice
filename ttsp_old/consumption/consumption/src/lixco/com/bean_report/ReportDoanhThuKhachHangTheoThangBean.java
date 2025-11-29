package lixco.com.bean_report;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import lixco.com.entity.ProductType;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.DoanhThuKhachHangTheoThang;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

@Named
@ViewScoped
public class ReportDoanhThuKhachHangTheoThangBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IReportService reportService;
	@Inject
	private ICustomerService customerService;
	@Inject
	private IProductService productService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private IIECategoriesService iECategoriesService;
	private int fromMonthSearch;
	private int toMonthSearch;
	private int yearSearch;
	private Product productSearch;
	private ProductType productTypeSearch;
	private Customer customerSearch;
	private int typep=-1;
	private IECategories iECategoriesSearch;
	private List<ProductType> listProductType;
	private List<IECategories> listCategories;
	private FormatHandler formatHandler;
	private List<DoanhThuKhachHangTheoThang> listDoanhThuKhachHangTheoThang;
	private List<DoanhThuKhachHangTheoThang> listDoanhThuKhachHangTheoThangFilter;
	
	@Override
	protected void initItem() {
		try{
			listCategories=new ArrayList<IECategories>();
			iECategoriesService.selectAll(listCategories);
			listProductType=new ArrayList<>();
			productTypeService.selectAll(listProductType);
			formatHandler=FormatHandler.getInstance();
			fromMonthSearch=1;
			toMonthSearch=ToolTimeCustomer.getMonthCurrent();
			yearSearch=ToolTimeCustomer.getYearCurrent();
		}catch (Exception e) {
			logger.error("ReportDoanhThuKhachHangTheoThangBean.initItem:"+e.getMessage(),e);
		}
	}
	public List<Customer> completeCustomer(String text){
		try{
			List<Customer> list=new ArrayList<>();
			customerService.complete(formatHandler.converViToEn(text), list);
		}catch (Exception e) {
			logger.error("ReportDoanhThuKhachHangTheoThangBean.completeCustomer:"+e.getMessage(),e);
		}
		return null;
	}
	public List<Product> completeProduct(String text){
		try{
			List<Product> list=new ArrayList<>();
			productService.complete(formatHandler.converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportDoanhThuKhachHangTheoThangBean.completeProduct:"+e.getMessage(),e);
		}
		return null;
	}
	public void search(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			/*{from_month:0,to_month:0,year:0,product_id:0,product_type_id:0,customer_id:0,ie_categories_id:0,typep:-1}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_month",fromMonthSearch);
			json.addProperty("to_month", toMonthSearch);
			json.addProperty("year", yearSearch);
			json.addProperty("product_id", productSearch==null ? 0 :productSearch.getId());
			json.addProperty("product_type_id", productTypeSearch==null ? 0:productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0: customerSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ? 0 :iECategoriesSearch.getId());
			json.addProperty("typep", typep);
			listDoanhThuKhachHangTheoThang=new ArrayList<>();
			reportService.reportDoanhThuTheoThang(JsonParserUtil.getGson().toJson(json), listDoanhThuKhachHangTheoThang);
			if(listDoanhThuKhachHangTheoThang.size()==0){
				notify.warning("Không có dữ liệu!");
			}
		}catch (Exception e) {
			logger.error("ReportDoanhThuKhachHangTheoThangBean.search:"+e.getMessage(),e);
		}
		current.executeScript("PF('tablenc').clearFilters();");
	}
	public void reportDoanhThuKhachHangExcel(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{from_month:0,to_month:0,year:0,product_id:0,product_type_id:0,customer_id:0,ie_categories_id:0,typep:-1}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_month",fromMonthSearch);
			json.addProperty("to_month", toMonthSearch);
			json.addProperty("year", yearSearch);
			json.addProperty("product_id", productSearch==null ? 0 :productSearch.getId());
			json.addProperty("product_type_id", productTypeSearch==null ? 0:productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0: customerSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ? 0 :iECategoriesSearch.getId());
			json.addProperty("typep", typep);
			listDoanhThuKhachHangTheoThang=new ArrayList<>();
			reportService.reportDoanhThuTheoThang(JsonParserUtil.getGson().toJson(json), listDoanhThuKhachHangTheoThang);
			if(listDoanhThuKhachHangTheoThang.size()>0){
				List<Object[]> listResult=new ArrayList<>();
				Object[] title={"MAKH","TÊN KHÁCH HÀNG","THÁNG 01","THÁNG 02","THÁNG 03","THÁNG 04","THÁNG 05","THÁNG 06","THÁNG 07","THÁNG 08","THÁNG 09","THÁNG 10","THÁNG 11","THÁNG 12"};
				listResult.add(title);
				for(DoanhThuKhachHangTheoThang p:listDoanhThuKhachHangTheoThang){
					Object[] row={Objects.toString(p.getCustomer_code(),""),Objects.toString(p.getCustomer_name(),""),p.getJanuary_total_amount(),p.getFebruary_total_amount(),p.getMarch_total_amount(),
							p.getApril_total_amount(),p.getMay_total_amount(),p.getJune_total_amount(),p.getJuly_total_amount(),p.getAugust_total_amount(),p.getSeptember_total_amount(),
							p.getOctober_total_amount(),p.getNovember_total_amount(),p.getDecember_total_amount()};
					listResult.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("doanh_thu_khach_hang_theo_thang");
		    	if(yearSearch !=0){
			    	if(fromMonthSearch !=0)
			    	   title2.append("từ tháng_"+String.format("%02d", fromMonthSearch)+"/"+String.format("%04d",yearSearch));
			    	if(toMonthSearch != 0)
			    		title2.append("_đến_tháng_"+String.format("%02d",toMonthSearch)+"/"+String.format("%04d",yearSearch));
		    	}
				ToolReport.printReportExcelRaw(listResult,title2.toString());
			}else{
				notify.warning("Không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("ReportDoanhThuKhachHangTheoThangBean.reportDoanhThuKhachHangExcel:"+e.getMessage(),e);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public Product getProductSearch() {
		return productSearch;
	}

	public void setProductSearch(Product productSearch) {
		this.productSearch = productSearch;
	}

	public ProductType getProductTypeSearch() {
		return productTypeSearch;
	}

	public void setProductTypeSearch(ProductType productTypeSearch) {
		this.productTypeSearch = productTypeSearch;
	}

	public Customer getCustomerSearch() {
		return customerSearch;
	}

	public void setCustomerSearch(Customer customerSearch) {
		this.customerSearch = customerSearch;
	}

	public int getTypep() {
		return typep;
	}

	public void setTypep(int typep) {
		this.typep = typep;
	}

	public IECategories getiECategoriesSearch() {
		return iECategoriesSearch;
	}

	public void setiECategoriesSearch(IECategories iECategoriesSearch) {
		this.iECategoriesSearch = iECategoriesSearch;
	}

	public List<ProductType> getListProductType() {
		return listProductType;
	}

	public void setListProductType(List<ProductType> listProductType) {
		this.listProductType = listProductType;
	}

	public List<IECategories> getListCategories() {
		return listCategories;
	}

	public void setListCategories(List<IECategories> listCategories) {
		this.listCategories = listCategories;
	}
	public FormatHandler getFormatHandler() {
		return formatHandler;
	}
	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
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
	public List<DoanhThuKhachHangTheoThang> getListDoanhThuKhachHangTheoThang() {
		return listDoanhThuKhachHangTheoThang;
	}
	public void setListDoanhThuKhachHangTheoThang(List<DoanhThuKhachHangTheoThang> listDoanhThuKhachHangTheoThang) {
		this.listDoanhThuKhachHangTheoThang = listDoanhThuKhachHangTheoThang;
	}
	public List<DoanhThuKhachHangTheoThang> getListDoanhThuKhachHangTheoThangFilter() {
		return listDoanhThuKhachHangTheoThangFilter;
	}
	public void setListDoanhThuKhachHangTheoThangFilter(
			List<DoanhThuKhachHangTheoThang> listDoanhThuKhachHangTheoThangFilter) {
		this.listDoanhThuKhachHangTheoThangFilter = listDoanhThuKhachHangTheoThangFilter;
	}
}
