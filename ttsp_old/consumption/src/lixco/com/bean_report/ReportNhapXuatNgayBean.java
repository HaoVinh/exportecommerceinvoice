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
import lixco.com.entity.Product;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.NhapXuatNgay;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.ParamReportDetail;
import trong.lixco.com.service.ParamReportDetailService;
import trong.lixco.com.util.Notify;

@Named
@ViewScoped
public class ReportNhapXuatNgayBean  extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IReportService reportService;
	@Inject
	private IProductService productService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private ICustomerService customerService;
	@Inject
	private IIECategoriesService iECategoriesService;
	private Date fromDateSearch;
	private Date toDateSearch;
	private Product productSearch;
	private ProductType productTypeSearch;
	private List<ProductType> listProductType;
	private Customer customerSearch;
	private IECategories iECategoriesSearch;
	private List<IECategories> listIECategories;
	@Override
	protected void initItem() {
		try{
			int month=ToolTimeCustomer.getMonthCurrent();
			int year=ToolTimeCustomer.getYearCurrent();
			fromDateSearch=ToolTimeCustomer.getDateMinCustomer(month, year);
			toDateSearch=ToolTimeCustomer.getDateMaxCustomer(month, year);
			listProductType=new ArrayList<ProductType>();
			listIECategories=new ArrayList<>();
			iECategoriesService.selectAll(listIECategories);
		}catch (Exception e) {
			logger.error("ReportNhapXuatNgayBean.initItem:"+e.getMessage(),e);
		}
	}
	@Inject
	ParamReportDetailService paramReportDetailService;
	public void reportBaoCaoNhapXuatSanPham(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			/*{from_date:'',to_date:'',product_id:0, product_type_id:0,customer_id:0,ie_categories_id:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch==null ? 0 :productSearch.getId());
			json.addProperty("product_type_id",productTypeSearch==null ? 0 :productTypeSearch.getId());
			json.addProperty("customer_id",customerSearch==null ? 0:customerSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ? 0:iECategoriesSearch.getId());
			List<NhapXuatNgay> list=new ArrayList<>();
			reportService.reportNhapXuatTheoNgay(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/in_nhap_xuat_ngay/bao_cao_nhap_xuat_san_pham.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
		    	List<ParamReportDetail> listConfig = paramReportDetailService.findByParamReportName("thongtinchung");
				for (ParamReportDetail p : listConfig) {
					importParam.put(p.getKey(), p.getValue());
				}
		    	StringBuilder title=new StringBuilder();
		    	title.append("BẢNG CÁO NHẬP XUẤT SẢN PHẨM ");
		    	if(fromDateSearch !=null){
		    		title.append("TỪ NGÀY "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	}
		    	if(toDateSearch !=null){
		    		title.append("ĐẾN NGÀY "+ToolTimeCustomer.convertDateToString(toDateSearch,"dd/MM/yyyy"));
		    	}
		    	importParam.put("title", title.toString());
		    	JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, new JRBeanCollectionDataSource(list));
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
		        current.executeScript("PF('showImg').show();");
		        current.executeScript("utility.showPDFK('" + ba + "','')");
			}else{
				notify.warning("Không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("ReportNhapXuatNgayBean.reportBaoCaoNhapXuatSanPham:"+e.getMessage(),e);
		}
	}
	public List<Customer> completeCustomer(String text){
		try{
			List<Customer> list=new ArrayList<>();
			customerService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportNhapXuatNgayBean.completeCustomer:"+e.getMessage(),e);
		}
		return null;
	}
	public List<Product> completeProduct(String text){
		try{
			List<Product> list=new ArrayList<>();
			productService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportNhapXuatNgayBean.completeProduct:"+e.getMessage(),e);
		}
		return null;
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

}
