package lixco.com.bean_report;

import java.util.ArrayList;
import java.util.Base64;
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
import lixco.com.entity.Car;
import lixco.com.entity.CarType;
import lixco.com.entity.Customer;
import lixco.com.entity.Product;
import lixco.com.interfaces.ICarService;
import lixco.com.interfaces.ICarTypeService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.BangKeKhoiLuongVanChuyen;
import lixco.com.reportInfo.BangKeKhoiLuongVanChuyenTheoKhachHang;
import lixco.com.reportInfo.BangKeKhoiLuongVanChuyenTheoNVVC;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.ParamReportDetail;
import trong.lixco.com.service.ParamReportDetailService;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

@Named
@ViewScoped
public class ReportBangKeKhoiLuongVanChuyenBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IReportService reportService;
	@Inject
	private IProductService productService;
	@Inject
	private ICustomerService customerService;
	@Inject
	private ICarTypeService carTypeService;
	@Inject
	private ICarService carService;
	private int monthSearch;
	private int yearSearch;
	private Product productSearch;
	private Customer customerSearch;
	private CarType carTypeSearch;
	private List<CarType> listCarType;
	private Car carSearch;

	@Override
	protected void initItem() {
		try{
			listCarType=new ArrayList<>();
			carTypeService.selectAll(listCarType);
			monthSearch=ToolTimeCustomer.getMonthCurrent();
			yearSearch=ToolTimeCustomer.getYearCurrent();
		}catch (Exception e) {
			logger.error("BangKeKhoiLuongVanChuyenBean.initItem:"+e.getMessage(),e);
		}
	}
	public List<Product> completeProduct(String text){
		try{
			List<Product> list=new ArrayList<Product>();
			productService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("BangKeKhoiLuongVanChuyenBean.completeProduct:"+e.getMessage(),e);
		}
		return null;
	}
	public List<Customer> completeCustomer(String text){
		try{
			List<Customer> list=new ArrayList<>();
			customerService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("BangKeKhoiLuongVanChuyenBean.completeCustomer:"+e.getMessage(),e);
		}
		return null;
	}
	public List<Car> completeCar(String text){
		try{
			List<Car> list=new ArrayList<>();
			carService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("BangKeKhoiLuongVanChuyenBean.completeCar:"+e.getMessage(),e);
		}
		return null;
	}
	@Inject
	ParamReportDetailService paramReportDetailService;
	public void reportBangKeKhoiLuongVanChuyenThangTheoXe(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			/*{month:0,year:0,product_id:0,customer_id:0,car_type_id:0,car_id:0}*/
			List<BangKeKhoiLuongVanChuyen> list=new ArrayList<>();
			JsonObject json=new JsonObject();
			json.addProperty("month", monthSearch);
			json.addProperty("year", yearSearch);
			json.addProperty("product_id", productSearch==null ? 0:productSearch.getId());
			json.addProperty("customer_id", customerSearch==null? 0:customerSearch.getId());
			json.addProperty("car_type_id", carTypeSearch==null ? 0:carTypeSearch.getId());
			json.addProperty("car_id", carSearch==null ? 0 :carSearch.getId());
			reportService.reportBangKeKhoiLuongVanChuyen(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/bang_ke_khoi_luong_vc/bang_ke_khoi_luong_vc.jasper");
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
		    	title.append("BẢNG KÊ KHỐI LƯỢNG VẬN CHUYỂN ");
		    	if(monthSearch !=0 && yearSearch !=0){
		    		title.append("THÁNG "+String.format("%02d", monthSearch)+"/"+String.format("%04d", yearSearch));
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
			logger.error("BangKeKhoiLuongVanChuyenBean.reportBangKeKhoiLuongVanChuyenThangTheoXe:"+e.getMessage(),e);
		}
	}
	public void reportBangKeKhoiLuongVanChuyenThangTheoKhachHang(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			/*{month:0,year:0,product_id:0,customer_id:0,car_type_id:0,car_id:0}*/
			List<BangKeKhoiLuongVanChuyenTheoKhachHang> list=new ArrayList<>();
			JsonObject json=new JsonObject();
			json.addProperty("month", monthSearch);
			json.addProperty("year", yearSearch);
			json.addProperty("product_id", productSearch==null ? 0:productSearch.getId());
			json.addProperty("customer_id", customerSearch==null? 0:customerSearch.getId());
			json.addProperty("car_type_id", carTypeSearch==null ? 0:carTypeSearch.getId());
			json.addProperty("car_id", carSearch==null ? 0 :carSearch.getId());
			reportService.reportBangKeKhoiLuongVanChuyenTheoKhachHang(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				//prepare data report 
				List<BangKeKhoiLuongVanChuyenTheoKhachHang> listPrepare=new ArrayList<>();
				listPrepare.add(list.get(0));
				long id=list.get(0).getCustomer_id();
				if(list.size()>1){
					for(int i=1;i<list.size();i++){
						BangKeKhoiLuongVanChuyenTheoKhachHang item=list.get(i);
						if(id==item.getCustomer_id()){
							item.setCustomer_name("");
						}
						id=item.getCustomer_id();
						listPrepare.add(item);
					}
				}
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/bang_ke_khoi_luong_vc/bang_ke_khoi_luong_vc_theo_khach_hang.jasper");
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
		    	title.append("BẢNG KÊ KHỐI LƯỢNG VẬN CHUYỂN ");
		    	if(monthSearch !=0 && yearSearch !=0){
		    		title.append("THÁNG "+String.format("%02d", monthSearch)+"/"+String.format("%04d", yearSearch));
		    	}
		    	title.append(" (THEO KH)");
		    	importParam.put("title", title.toString());
		    	importParam.put("list_data", listPrepare);
		    	JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, new JREmptyDataSource());
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
		        current.executeScript("PF('showImg').show();");
		        current.executeScript("utility.showPDFK('" + ba + "','')");
			}else{
				notify.warning("Không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("BangKeKhoiLuongVanChuyenBean.reportBangKeKhoiLuongVanChuyenThangTheoKhachHang:"+e.getMessage(),e);
		}
	}
	public void reportBangKeKhoiLuongVanChuyenExcel(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{month:0,year:0,product_id:0,customer_id:0,car_type_id:0,car_id:0}*/
			List<BangKeKhoiLuongVanChuyen> list=new ArrayList<>();
			JsonObject json=new JsonObject();
			json.addProperty("month", monthSearch);
			json.addProperty("year", yearSearch);
			json.addProperty("product_id", productSearch==null ? 0:productSearch.getId());
			json.addProperty("customer_id", customerSearch==null? 0:customerSearch.getId());
			json.addProperty("car_type_id", carTypeSearch==null ? 0:carTypeSearch.getId());
			json.addProperty("car_id", carSearch==null ? 0 :carSearch.getId());
			reportService.reportBangKeKhoiLuongVanChuyen(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				List<Object[]> listResult=new ArrayList<>();
				Object[] title={"SỐ XE","NHÀ VC","NGÀY","MÃ KH","TÊN KHÁCH HÀNG","MÃ TP","THÀNH PHỐ","SỐ HÓA ĐƠN","LOẠI SẢN PHẨM","NHÓM","SỐ LƯỢNG"};
				listResult.add(title);
				for(BangKeKhoiLuongVanChuyen p:list){
					Object[] row={p.getLicense_plate(),p.getCarrier_name(),ToolTimeCustomer.convertDateToString(p.getInvoice_date(),"dd/MM/yyyy"),p.getCustomer_code(),
							p.getCustomer_name(),p.getCity_code(),p.getCity_name(),p.getInvoice_code(),
							p.getProduct_type_name(),p.getTypept()==1 ? "BỘT GIẶT" :"NTRL", p.getKg_quantity()};
					listResult.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("bang_ke_khoi_luong_van_chuyen");
				ToolReport.printReportExcelRaw(listResult,title2.toString());
			}else{
				notify.warning("Không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("BangKeKhoiLuongVanChuyenBean.reportBangKeKhoiLuongVanChuyenExcel:"+e.getMessage(),e);
		}
	}
	public void reportBangKeKhoiLuongVanChuyenTheoKhachHangExcel(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{month:0,year:0,product_id:0,customer_id:0,car_type_id:0,car_id:0}*/
			List<BangKeKhoiLuongVanChuyenTheoKhachHang> list=new ArrayList<>();
			JsonObject json=new JsonObject();
			json.addProperty("month", monthSearch);
			json.addProperty("year", yearSearch);
			json.addProperty("product_id", productSearch==null ? 0:productSearch.getId());
			json.addProperty("customer_id", customerSearch==null? 0:customerSearch.getId());
			json.addProperty("car_type_id", carTypeSearch==null ? 0:carTypeSearch.getId());
			json.addProperty("car_id", carSearch==null ? 0 :carSearch.getId());
			reportService.reportBangKeKhoiLuongVanChuyenTheoKhachHang(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				List<Object[]> listResult=new ArrayList<>();
				Object[] title={"MÃ KH","TÊN KHÁCH HÀNG","NHÓM","SỐ LƯỢNG"};
				listResult.add(title);
				for(BangKeKhoiLuongVanChuyenTheoKhachHang p:list){
					Object[] row={p.getCustomer_code(),p.getCustomer_name(),p.getTypept()==1 ? "BỘT GIẶT" :"NTRL", p.getKg_quantity()};
					listResult.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("bang_ke_khoi_luong_van_chuyen_kh");
				ToolReport.printReportExcelRaw(listResult,title2.toString());
			}else{
				notify.warning("Không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("BangKeKhoiLuongVanChuyenBean.reportBangKeKhoiLuongVanChuyenTheoKhachHangExcel:"+e.getMessage(),e);
		}
	}
	public void reportBangKeKhoiLuongVanChuyenTheoNVVCExcel(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{month:0,year:0,product_id:0,customer_id:0,car_type_id:0,car_id:0}*/
			List<BangKeKhoiLuongVanChuyenTheoNVVC> list=new ArrayList<>();
			JsonObject json=new JsonObject();
			json.addProperty("month", monthSearch);
			json.addProperty("year", yearSearch);
			json.addProperty("product_id", productSearch==null ? 0:productSearch.getId());
			json.addProperty("customer_id", customerSearch==null? 0:customerSearch.getId());
			json.addProperty("car_type_id", carTypeSearch==null ? 0:carTypeSearch.getId());
			json.addProperty("car_id", carSearch==null ? 0 :carSearch.getId());
			reportService.reportBangKeKhoiLuongVanChuyenTheoNVVC(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				List<Object[]> listResult=new ArrayList<>();
				Object[] title={"SỐ XE","SỐ HÓA ĐƠN","MÃ KH","TÊN KHÁCH HÀNG","NGÀY","MANVVC","TENNVVC","SỐ LƯỢNG"};
				listResult.add(title);
				for(BangKeKhoiLuongVanChuyenTheoNVVC p:list){
					Object[] row={Objects.toString(p.getLicense_plate(),""),Objects.toString(p.getInvoice_code(),""),Objects.toString(p.getCustomer_code()),Objects.toString(p.getCustomer_name(),""),ToolTimeCustomer.convertDateToString(p.getInvoice_date(),"dd/MM/yyyy"),
							Objects.toString(p.getCarrier_code(),""),Objects.toString(p.getCarrier_name(),""),Objects.toString(p.getKg_quantity(),"0")};
					listResult.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("bang_ke_khoi_luong_van_chuyen_nvvc");
				ToolReport.printReportExcelRaw(listResult,title2.toString());
			}else{
				notify.warning("Không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("BangKeKhoiLuongVanChuyenBean.reportBangKeKhoiLuongVanChuyenTheoNVVCExcel:"+e.getMessage(),e);
		}
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
	public CarType getCarTypeSearch() {
		return carTypeSearch;
	}
	public void setCarTypeSearch(CarType carTypeSearch) {
		this.carTypeSearch = carTypeSearch;
	}
	public List<CarType> getListCarType() {
		return listCarType;
	}
	public void setListCarType(List<CarType> listCarType) {
		this.listCarType = listCarType;
	}
	public Car getCarSearch() {
		return carSearch;
	}
	public void setCarSearch(Car carSearch) {
		this.carSearch = carSearch;
	}
}
