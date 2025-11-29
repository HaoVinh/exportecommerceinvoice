package lixco.com.bean_report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import lixco.com.reportInfo.ReportTongKetCongNoKhachHang;
import lixco.com.reportInfo.ReportTongKetCongNoKhachHang.ProductData;
import lixco.com.reportInfo.ReportTongKetCongNoKhachHang.ProductTypeData;
import lixco.com.reportInfo.TheoDoiDoanhThuKhachHang;
import lixco.com.reportInfo.TongKetTieuThuKhachHang;
import lixco.com.reportInfo.TongKetXuatTheoKhachHang;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;
@Named
@ViewScoped
public class ReportTongKetXuatTheoKhachHangBean extends AbstractBean {
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
			search();
		}catch (Exception e) {
			logger.error("ReportTongKetXuatTheoKhachHangBean.initItem:"+e.getMessage(),e);
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
	public void search(){
		try{
			
		}catch (Exception e) {
			logger.error("ReportInSanPhamBean.search:"+e.getMessage(),e);
		}
	}
	public void reportTongKetCongNo(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			List<TongKetXuatTheoKhachHang> listTongKetXuatTheoKhachHang=new ArrayList<>();
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:-1}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("product_type_id", productTypeSearch==null ? 0 : productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0: contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ?0 :iECategoriesSearch.getId());
			json.addProperty("typep", typep);
			reportService.reportTongKetCongNoKhachHang(JsonParserUtil.getGson().toJson(json), listTongKetXuatTheoKhachHang);
			Map<Long,ReportTongKetCongNoKhachHang> map=new LinkedHashMap<Long, ReportTongKetCongNoKhachHang>();
			for(TongKetXuatTheoKhachHang k:listTongKetXuatTheoKhachHang){
				if(map.containsKey(k.getCustomer_id())){
					ReportTongKetCongNoKhachHang t=map.get(k.getCustomer_id());
					t.setQuantity(BigDecimal.valueOf(k.getQuantity()).add(BigDecimal.valueOf(t.getQuantity())).doubleValue());
					t.setBox_quantity(BigDecimal.valueOf(k.getBox_quantity()).add(BigDecimal.valueOf(t.getBox_quantity())).doubleValue());
					t.setTotal_amount(BigDecimal.valueOf(k.getTotal_amount()).add(BigDecimal.valueOf(t.getTotal_amount())).doubleValue());
					t.setTotal_tax_amount(BigDecimal.valueOf(k.getTotal_tax_amount()).add(BigDecimal.valueOf(t.getTotal_tax_amount())).doubleValue());
					ProductData productData=new ProductData();
					t.getList_product_data().add(productData);
					productData.setProduct_id(k.getProduct_id());
					productData.setProduct_name(k.getProduct_name());
					productData.setQuantity(k.getQuantity());
					productData.setBox_quantity(k.getBox_quantity());
					productData.setTotal_amount(k.getTotal_amount());
					productData.setTotal_tax_amount(k.getTotal_tax_amount());
					List<ProductTypeData> listProductTypeData=t.getList_product_type_data();
					boolean kt=true;
					for(ProductTypeData pt:listProductTypeData){
						if(k.getProduct_type_id()==pt.getProduct_type_id()){
							pt.setQuantity(BigDecimal.valueOf(pt.getQuantity()).add(BigDecimal.valueOf(k.getQuantity())).doubleValue());
							pt.setBox_quantity(BigDecimal.valueOf(pt.getBox_quantity()).add(BigDecimal.valueOf(k.getBox_quantity())).doubleValue());
							pt.setTotal_amount(BigDecimal.valueOf(pt.getTotal_amount()).add(BigDecimal.valueOf(k.getTotal_amount())).doubleValue());
							kt=false;
							break;
						}
					}
					if(kt){
						ProductTypeData pt=new ProductTypeData();
						pt.setProduct_type_id(k.getProduct_type_id());
						pt.setProduct_type_name(k.getProduct_type_name());
						pt.setQuantity(k.getQuantity());
						pt.setBox_quantity(k.getBox_quantity());
						pt.setTotal_amount(k.getTotal_amount());
						listProductTypeData.add(pt);
					}
					
				}else{
					ReportTongKetCongNoKhachHang t=new ReportTongKetCongNoKhachHang();
					t.setCustomer_id(k.getCustomer_id());
					t.setCustomer_name(k.getCustomer_name());
					t.setQuantity(k.getQuantity());
					t.setBox_quantity(k.getBox_quantity());
					t.setTotal_amount(k.getTotal_amount());
					t.setTotal_tax_amount(k.getTotal_tax_amount());
					List<ProductData> listProductData=new ArrayList<>();
					ProductData  lv2=new ProductData();
					lv2.setProduct_id(k.getProduct_id());
					lv2.setProduct_name(k.getProduct_name());
					lv2.setQuantity(k.getQuantity());
					lv2.setBox_quantity(k.getBox_quantity());
					lv2.setTotal_amount(k.getTotal_amount());
					lv2.setTotal_tax_amount(k.getTotal_tax_amount());
					listProductData.add(lv2);
					t.setList_product_data(listProductData);
					List<ProductTypeData> listProductTypeData=new ArrayList<>();
					t.setList_product_type_data(listProductTypeData);
					ProductTypeData lv3=new ProductTypeData();
					lv3.setProduct_type_id(k.getProduct_type_id());
					lv3.setProduct_type_name(k.getProduct_type_name());
					lv3.setQuantity(k.getQuantity());
					lv3.setBox_quantity(k.getBox_quantity());
					lv3.setTotal_amount(k.getTotal_amount());
					listProductTypeData.add(lv3);
					map.put(k.getCustomer_id(), t);
				}
			}
			List<ReportTongKetCongNoKhachHang> listTongKetCongNoKhachHang=new ArrayList<>(map.values());
			if(listTongKetCongNoKhachHang !=null && listTongKetCongNoKhachHang.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/report_tk_xuat_theo_kh/reporttongketcongnokhachhang.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
		    	importParam.put("logo", FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/gfx/lixco_logo.png"));
		    	StringBuilder title=new StringBuilder();
		    	title.append("TỔNG KẾT CÔNG NỢ TỪ NGÀY "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	title.append(" ĐẾN NGÀY "+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
		    	importParam.put("title", title.toString());
		    	JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, new JRBeanCollectionDataSource(listTongKetCongNoKhachHang));

				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
		        current.executeScript("PF('showImg').show();");
		        current.executeScript("utility.showPDFK('" + ba + "','')");
			}else{
				notify.warning("Không có dữ liệu!");
			}
		}catch (Exception e) {
			logger.error("ReportInSanPhamBean.tongKetXuatTheoKhachHang:"+e.getMessage(),e);
		}
	}
	public void reportTongKetTieuThuSanPham(){
		PrimeFaces current=PrimeFaces.current();
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			List<TongKetTieuThuKhachHang> list=new ArrayList<>();
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:-1}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("product_type_id", productTypeSearch==null ? 0 : productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0: contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ?0 :iECategoriesSearch.getId());
			json.addProperty("typep", typep);
			reportService.reportTongKetTieuThuKhachHang(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/report_tk_xuat_theo_kh/tongkettieuthusanpham.jasper");
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
			logger.error("ReportInSanPhamBean.reportTongKetTieuThuSanPham:"+e.getMessage(),e);
		}
	}
	public void reportTongKetTieuThuSanPham2(){
		PrimeFaces current=PrimeFaces.current();
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			List<TongKetTieuThuKhachHang> list=new ArrayList<>();
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:-1}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("product_type_id", productTypeSearch==null ? 0 : productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0: contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ?0 :iECategoriesSearch.getId());
			json.addProperty("typep", typep);
			reportService.reportTongKetTieuThuKhachHangDVT(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/report_tk_xuat_theo_kh/tongkettieuthusanpham.jasper");
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
			logger.error("ReportInSanPhamBean.reportTongKetTieuThuSanPham:"+e.getMessage(),e);
		}
	}
	public void reportTheoDoiDoanhThuExcel(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			List<TheoDoiDoanhThuKhachHang> list=new ArrayList<>();
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:-1}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("product_type_id", productTypeSearch==null ? 0 : productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0: contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ?0 :iECategoriesSearch.getId());
			json.addProperty("typep", typep);
			reportService.reportTheoDoiDoanhThuKhachHang(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "MÃ KH", "TÊN KHÁCH HÀNG","SỐ LƯỢNG","SỐ LƯỢNG THÙNG","SỐ TIỀN" };
				results.add(title);
				for (TheoDoiDoanhThuKhachHang it : list) {
					Object[] row = {it.getCustomer_code(),it.getCustomer_name(),it.getQuantity(),it.getBox_quantity(),it.getTotal_amount()};
					results.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("theo_doi_doanh_thu_ ");
		    	if(fromDateSearch !=null)
		    	   title2.append("tu_ngay "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	if(toDateSearch!=null)
		    	   title2.append(" đến ngày "+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
				ToolReport.printReportExcelRaw(results,title2.toString());
			}else{
				notify.warning("không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("ReportInSanPhamBean.reportTongKetTieuThuSanPham:"+e.getMessage(),e);
		}
	}
	@Override
	protected Logger getLogger() {
		return logger;
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
