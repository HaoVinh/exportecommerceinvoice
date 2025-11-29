package lixco.com.bean_report;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import lixco.com.entity.Contract;
import lixco.com.entity.Customer;
import lixco.com.entity.IECategories;
import lixco.com.entity.Product;
import lixco.com.interfaces.IContractService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.BangKeChiTietHoaDon;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.ParamReportDetail;
import trong.lixco.com.service.ParamReportDetailService;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

@Named
@ViewScoped
public class ReportBangKeChiTietHoaDonBean  extends AbstractBean {
	private static final long serialVersionUID = -8870786800128625218L;
	@Inject
	private Logger logger;
	@Inject
	private IReportService reportService;
	@Inject
	private IProductService productService;
	@Inject
	private ICustomerService customerService;
	@Inject
	private IContractService contractService;
	@Inject
	private IIECategoriesService iECategoriesService;
	private Date fromDateSearch;
	private Date toDateSearch;
	private Product productSearch;
	private Customer customerSearch;
	private Contract contractSearch;
	private IECategories iECategoriesSearch;
	private List<IECategories> listIECategories;
	@Override
	protected void initItem() {
		try{
			int month=ToolTimeCustomer.getMonthCurrent();
			int year=ToolTimeCustomer.getYearCurrent();
			fromDateSearch=ToolTimeCustomer.getDateMinCustomer(month, year);
			toDateSearch=ToolTimeCustomer.getDateMaxCustomer(month, year);
			listIECategories=new ArrayList<>();
			iECategoriesService.selectAll(listIECategories);
		}catch (Exception e) {
			logger.error("ReportBangKeChiTietHoaDonBean.initItem:"+e.getMessage(),e);
		}
	}
	public void reportBangKeChiTietHoaDonExcel(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{from_date:'',to_date:'',product_id:0,customer_id:0,contract_id:0,ie_categories_id:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date",ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch,"dd/MM/yyyy"));
			json.addProperty("product_id", productSearch==null ? 0 : productSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0 :contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ? 0 :iECategoriesSearch.getId());
			List<BangKeChiTietHoaDon> list=new ArrayList<BangKeChiTietHoaDon>();
			reportService.reportBangKeChiTietHoaDon(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				List<Object[]> listResult=new ArrayList<>();
				Object[] title={"NGÀY","MÃ HÓA ĐƠN","SỐ ĐƠN HÀNG","SỐ XE","ITEM CODE","MASP","TÊN SẢN PHẨM","SỐ THÙNG","SỐ LƯỢNG","ĐƠN GIÁ","SỐ TIỀN","GHI CHÚ","MAKH","MAXN","TÊN KHÁCH HÀNG","LOẠI KHÁCH HÀNG","SỐ LƯỢNG KG"};
				listResult.add(title);
				for(BangKeChiTietHoaDon p:list){
					Object[] row={ToolTimeCustomer.convertDateToString(p.getInvoice_date(),"dd/MM/yyyy"),p.getInvoice_code(),p.getOrder_voucher(),p.getLicense_plate(),Objects.toString(p.getLever_code(),""),
							p.getProduct_code(),p.getProduct_name(),p.getBox_quantity(),p.getUnit_quantity(),p.getUnit_price(),p.getTotal_amount(),
							Objects.toString(p.getNote(),""),Objects.toString(p.getCustomer_code(),""),Objects.toString(p.getIe_categories_name(),""),Objects.toString(p.getCustomer_name(),""),
							Objects.toString(p.getCustomer_type_name(),""),p.getKg_quantity()};
					listResult.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("Liet_ke_phieu_xuat_");
		    	if(fromDateSearch != null){
		    		title2.append("tu_ngay_"+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	}
		    	if(toDateSearch !=null){
		    		title2.append("_den_ngay_"+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
		    	}
				ToolReport.printReportExcelRaw(listResult,title2.toString());
			}else{
				notify.warning("Không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("ReportBangKeChiTietHoaDonBean.reportBangKeChiTietHoaDonExcel:"+e.getMessage(),e);
		}
	}
	@Inject
	ParamReportDetailService paramReportDetailService;
	public void reportBangKeChiTietHoaDon(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			/*{from_date:'',to_date:'',product_id:0,customer_id:0,contract_id:0,ie_categories_id:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date",ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch,"dd/MM/yyyy"));
			json.addProperty("product_id", productSearch==null ? 0 : productSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("contract_id", contractSearch==null ? 0 :contractSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ? 0 :iECategoriesSearch.getId());
			List<BangKeChiTietHoaDon> list=new ArrayList<BangKeChiTietHoaDon>();
			reportService.reportBangKeChiTietHoaDon(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()==0){
				notify.warning("Không có dữ liệu");
			}else{
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/bk_chi_tiet_hoa_don/bk_chi_tiet_hoa_don.jasper");
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
		    	title.append("BẢNG KÊ CHI TIẾT HÓA ĐƠN ");
		    	if(fromDateSearch !=null){
		    		title.append("TỪ NGÀY "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	}
		    	if(toDateSearch !=null){
		    		title.append(" ĐẾN NGÀY "+ToolTimeCustomer.convertDateToString(toDateSearch,"dd/MM/yyyy"));
		    	}
		    	importParam.put("title", title.toString());
		    	importParam.put("list_data", list);
		    	JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, new JREmptyDataSource());
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
		        current.executeScript("PF('showImg').show();");
		        current.executeScript("utility.showPDFK('" + ba + "','')");
			}
		}catch (Exception e) {
			logger.error("ReportBangKeChiTietHoaDonBean.search:"+e.getMessage(),e);
		}
	}
	public List<Customer> completeCustomer(String text){
		try{
			List<Customer> list=new ArrayList<>();
			customerService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportBangKeChiTietHoaDonBean.completeCustomer:"+e.getMessage(),e);
		}
		return null;
	}
	public List<Contract> completeContract(String text){
		try{
			List<Contract> list=new ArrayList<>();
			contractService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportBangKeChiTietHoaDonBean.completeContract:"+e.getMessage(),e);
		}
		return null;
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
}
