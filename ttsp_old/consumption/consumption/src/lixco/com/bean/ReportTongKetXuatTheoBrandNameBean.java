package lixco.com.bean;

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
import lixco.com.entity.ProductType;
import lixco.com.interfaces.IContractService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.TongKetXuatTheoBrandName;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.util.Notify;

@Named
@ViewScoped
public class ReportTongKetXuatTheoBrandNameBean  extends AbstractBean {
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
	private Customer customerSearch;
	private Product productSearch;
	private IECategories iECategoriesSearch;
	private List<IECategories> listIECategories;
	private ProductType productTypeSearch;
	private List<ProductType> listProductType;
	private Date fromDateSearch;
	private Date toDateSearch;
	private Contract contractSearch;
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
			logger.error("ReportTongKetXuatTheoBrandNameBean.initItem"+e.getMessage(),e);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
	public void reportTongKetXuatTheoBrandName(){
		PrimeFaces current=PrimeFaces.current();
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch,"dd/MM/yyyy"));
			json.addProperty("product_id", productSearch==null ? 0 : productSearch.getId());
			json.addProperty("product_type_id", productTypeSearch==null ? 0 :productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id",contractSearch==null ? 0 :contractSearch.getId());
			List<TongKetXuatTheoBrandName> list=new ArrayList<>();
			reportService.reportTongKetXuatTheoBrandName(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/tk_xuat_theo_brand_name/tk_xuat_theo_brand_name.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
		    	importParam.put("logo", FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/gfx/lixco_logo.png"));
		    	importParam.put("list_data",list);
		    	StringBuilder title=new StringBuilder();
		    	title.append("TỔNG KẾT TT SP ");
		    	if(fromDateSearch !=null)
		    	   title.append("TỪ NGÀY "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	if(toDateSearch!=null)
		    	   title.append(" ĐẾN NGÀY "+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
		    	importParam.put("title", title.toString());
		    	JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, new JREmptyDataSource());
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
		        current.executeScript("PF('showImg').show();");
		        current.executeScript("utility.showPDFK('" + ba + "','')");
			}else{
				notify.warning("không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("ReportTongKetXuatTheoBrandNameBean.reportTongKetXuatTheoBrandName:"+e.getMessage(),e);
		}
	}
	public List<Product> completeProduct(String text){
		try{
			List<Product> list=new ArrayList<Product>();
			productService.completeByProductType(FormatHandler.getInstance().converViToEn(text),productTypeSearch==null ?0 :productTypeSearch.getId(), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportInSanPhamBean.completeProduct:"+e.getMessage(),e);
		}
		return null;
	}
	public List<Contract> completeContract(String text){
		try{
			List<Contract> list=new ArrayList<Contract>();
			contractService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportInSanPhamBean.completeProduct:"+e.getMessage(),e);
		}
		return null;
	}
	public List<Customer> completeCustomer(String text){
		try{
			List<Customer> list=new ArrayList<>();
			customerService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportInSanPhamBean.completeCustomer:"+e.getMessage(),e);
		}
		return null;
	}
	public Customer getCustomerSearch() {
		return customerSearch;
	}

	public void setCustomerSearch(Customer customerSearch) {
		this.customerSearch = customerSearch;
	}

	public Product getProductSearch() {
		return productSearch;
	}

	public void setProductSearch(Product productSearch) {
		this.productSearch = productSearch;
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

	public Contract getContractSearch() {
		return contractSearch;
	}

	public void setContractSearch(Contract contractSearch) {
		this.contractSearch = contractSearch;
	}

}
