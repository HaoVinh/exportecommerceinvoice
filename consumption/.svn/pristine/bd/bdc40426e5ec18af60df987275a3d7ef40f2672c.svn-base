package lixco.com.bean_report;

import java.util.ArrayList;
import java.util.Base64;
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
import lixco.com.entity.Product;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.LuyKeNhapXuat;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.ParamReportDetail;
import trong.lixco.com.service.ParamReportDetailService;

@Named
@ViewScoped
public class ReportLuyKeXuatNhapBean extends AbstractBean  {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IReportService reportService;
	@Inject
	private IIECategoriesService iEcategoriesService;
	@Inject
	private ICustomerService customerService;
	@Inject
	private IProductService productService;
	private int monthSearch;
	private int yearSearch;
	private Product productSearch;
	private Customer customerSearch;
	private IECategories iECategoriesSearch;
	private List<IECategories> listIECategories;
	private int typep=-1;

	@Override
	protected void initItem() {
		try{
			listIECategories=new ArrayList<>();
			iEcategoriesService.selectAll(listIECategories);
			monthSearch=ToolTimeCustomer.getMonthCurrent();
			yearSearch=ToolTimeCustomer.getYearCurrent();
		}catch (Exception e) {
			logger.error("ReportLuyKeXuatNhapBean.initItem:"+e.getMessage(),e);
		}
	}
	@Inject
	ParamReportDetailService paramReportDetailService;
	public void reportLuyKeXuatNhap(){
		PrimeFaces current=PrimeFaces.current();
		try{
			/*{month:0,year:0,product_id:0,customer_id:0,ie_categories_id:0,typep:-1}*/
			JsonObject json=new JsonObject();
			json.addProperty("month", monthSearch);
			json.addProperty("year", yearSearch);
			json.addProperty("product_id", productSearch==null ? 0 :productSearch.getId());
			json.addProperty("customer_id",customerSearch==null ?0  :customerSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ? 0:iECategoriesSearch.getId());
			json.addProperty("typep", typep);
			List<LuyKeNhapXuat> list=new ArrayList<LuyKeNhapXuat>();
			reportService.reportInLuyKeXuatNhap(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/luy_ke_nhap_xuat/luy_ke_nhap_xuat.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
		    	List<ParamReportDetail> listConfig = paramReportDetailService.findByParamReportName("thongtinchung");
				for (ParamReportDetail p : listConfig) {
					importParam.put(p.getKey(), p.getValue());
				}
		    	importParam.put("logo", FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/gfx/lixco_logo.png"));
		    	StringBuilder title=new StringBuilder();
		    	title.append("BÁO CÁO LŨY KẾ NHẬP XUẤT SẢN PHẨM THÁNG "+String.format("%02d",monthSearch)+"/"+String.format("%04d",yearSearch));
		    	importParam.put("title", title.toString());
		    	importParam.put("list_data", list);
		    	JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, new JREmptyDataSource());
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
		        current.executeScript("PF('showImg').show();");
		        current.executeScript("utility.showPDFK('" + ba + "','')");
			}
		}catch (Exception e) {
			logger.error("ReportLuyKeXuatNhapBean.reportLuyKeXuatNhap:"+e.getMessage(),e);
		}
	}
	public List<Customer> completeCustomer(String text){
		try{
			List<Customer> list=new ArrayList<>();
			customerService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportLuyKeXuatNhapBean.completeCustomer:"+e.getMessage(),e);
		}
		return null;
	}
	public List<Product> completeProduct(String text){
		try{
			List<Product> list=new ArrayList<>();
			productService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportLuyKeXuatNhapBean.completeProduct:"+e.getMessage(),e);
		}
		return null;
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	public int getMonthSearch() {
		return monthSearch;
	}
	public void setMonthSearch(int monthSearch) {
		this.monthSearch = monthSearch;
	}
	public int getYearSearch() {
		return yearSearch;
	}
	public void setYearSearch(int yearSearch) {
		this.yearSearch = yearSearch;
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
	public int getTypep() {
		return typep;
	}
	public void setTypep(int typep) {
		this.typep = typep;
	}

}
