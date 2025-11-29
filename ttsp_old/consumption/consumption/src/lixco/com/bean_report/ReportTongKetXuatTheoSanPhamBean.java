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
import lixco.com.entity.ProductType;
import lixco.com.interfaces.IContractService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai1;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai2;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai3;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel;
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
public class ReportTongKetXuatTheoSanPhamBean extends AbstractBean {
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
	private Contract contractSearch;
	private int typep;//xuất khẩu hay nội địa
	private Date fromDateSearch;
	private Date toDateSearch;
	private FormatHandler formatHandler;
	@Override
	protected void initItem() {
		try{
			listIECategories=new ArrayList<>();
			iECategoriesService.selectAll(listIECategories);
			listProductType=new ArrayList<>();
			productTypeService.selectAll(listProductType);
			formatHandler=FormatHandler.getInstance();
			typep=-1;
			int month=ToolTimeCustomer.getMonthCurrent();
			int year=ToolTimeCustomer.getYearCurrent();
			fromDateSearch=ToolTimeCustomer.getDateMinCustomer(month, year);
			toDateSearch=ToolTimeCustomer.getDateMaxCustomer(month, year);
		}catch (Exception e) {
			logger.error("ReportTongKetXuatTheoSanPhamBean.initItem:"+e.getMessage(),e);
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

	@Override
	protected Logger getLogger() {
		return logger;
	}
	public void reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("product_type_id", productTypeSearch==null ? 0 : productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0: contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ?0 :iECategoriesSearch.getId());
			json.addProperty("typep", typep);
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai1> list=new ArrayList<>();
			reportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/report_tk_xuat_theo_san_pham/tongketxuattheosanphamtongkettieuthuloai1.jasper");
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
			logger.error("ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1:"+e.getMessage(),e);
		}
	}
	public void reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("product_type_id", productTypeSearch==null ? 0 : productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0: contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ?0 :iECategoriesSearch.getId());
			json.addProperty("typep", typep);
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai2> list=new ArrayList<>();
			reportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/report_tk_xuat_theo_san_pham/tongketxuattheosanphamtongkettieuthuloai2.jasper");
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
			logger.error("ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2:"+e.getMessage(),e);
		}
	}
	public void reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("product_type_id", productTypeSearch==null ? 0 : productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0: contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ?0 :iECategoriesSearch.getId());
			json.addProperty("typep", typep);
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai3> list=new ArrayList<>();
			reportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/report_tk_xuat_theo_san_pham/tongketxuattheosanphamtongkettieuthuloai3.jasper");
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
			logger.error("ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3:"+e.getMessage(),e);
		}
	}
	public void reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("product_type_id", productTypeSearch==null ? 0 : productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0: contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ?0 :iECategoriesSearch.getId());
			json.addProperty("typep", typep);
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel> list=new ArrayList<>();
			reportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "MÃ SP", "TÊN SẢN PHẨM","HSQD","QUI CÁCH","SỐ THÙNG","SỐ TIỀN","THUẾ TGGT"};
				results.add(title);
				for (TongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel it : list) {
					Object[] row = {it.getProduct_code(),it.getProduct_name(),it.getSpecification(),it.getFactor(),it.getBox_quantity(),it.getTotal_amount(),it.getTotal_tax_amount()};
					results.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("thsp_hopdong_");
		    	if(fromDateSearch !=null)
		    	   title2.append("tu_ngay "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	if(toDateSearch!=null)
		    	   title2.append("_den_ngay "+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
				ToolReport.printReportExcelRaw(results,title2.toString());
			}else{
				notify.warning("không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel:"+e.getMessage(),e);
		}
	}
	public void reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("product_type_id", productTypeSearch==null ? 0 : productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0: contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ?0 :iECategoriesSearch.getId());
			json.addProperty("typep", typep);
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel> list=new ArrayList<>();
			reportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "MÃ SP COM", "TÊN SẢN PHẨM COM","MÃ NX","LOẠI NHẬP XUẤT","SỐ LƯỢNG KG","SỐ LƯỢNG DVT","SỐ TIỀN","THUẾ TGGT"};
				results.add(title);
				for (TongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel it : list) {
					Object[] row = {it.getPcom_code(),it.getPcom_name(),it.getIe_categories_code(),it.getIe_categories_name(),it.getQuantity_kg(),it.getQuantity(),it.getTotal_amount(),it.getTotal_tax_amount()};
					results.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("thsp_com_");
		    	if(fromDateSearch !=null)
		    	   title2.append("tu_ngay "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	if(toDateSearch!=null)
		    	   title2.append("_den_ngay "+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
				ToolReport.printReportExcelRaw(results,title2.toString());
			}else{
				notify.warning("không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel:"+e.getMessage(),e);
		}
	}
	public void reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("product_type_id", productTypeSearch==null ? 0 : productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0: contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ?0 :iECategoriesSearch.getId());
			json.addProperty("typep", typep);
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel> list=new ArrayList<>();
			reportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = {"MÃ NX","LOẠI NHẬP XUẤT","MÃ SP","SẢN PHẨM","LOẠI SẢN PHẨM","SỐ LƯỢNG","SỐ LƯỢNG ĐVT","SỐ TIỀN","THUẾ TGGT"};
				results.add(title);
				for (TongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel it : list) {
					Object[] row = {it.getIe_categories_code(),it.getIe_categories_name(),it.getProduct_code(),it.getProduct_name(),it.getProduct_type_name(),it.getQuantity_kg(),it.getQuantity(),it.getTotal_amount(),it.getTotal_tax_amount()};
					results.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("bang_ke_chi_tiet_xuat_san_pham_");
		    	if(fromDateSearch !=null)
		    	   title2.append("tu_ngay "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	if(toDateSearch!=null)
		    	   title2.append("_den_ngay "+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
				ToolReport.printReportExcelRaw(results,title2.toString());
			}else{
				notify.warning("không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("ReportTongKetXuatTheoSanPhamBean.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel:"+e.getMessage(),e);
		}
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
	public Contract getContractSearch() {
		return contractSearch;
	}
	public void setContractSearch(Contract contractSearch) {
		this.contractSearch = contractSearch;
	}
	public int getTypep() {
		return typep;
	}
	public void setTypep(int typep) {
		this.typep = typep;
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
	public FormatHandler getFormatHandler() {
		return formatHandler;
	}
	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
	}
	

}
