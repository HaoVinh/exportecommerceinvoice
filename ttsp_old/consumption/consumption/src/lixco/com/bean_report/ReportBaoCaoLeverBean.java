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
import lixco.com.entity.Product;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.BaoCaoLever;
import net.sf.jasperreports.engine.JRDataSource;
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
public class ReportBaoCaoLeverBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IReportService reportService;
	@Inject
	private IProductService productService;
	private Date fromDateSearch;
	private Date toDateSearch;
	private Product productSearch;
	@Override
	protected void initItem() {
		try{
			int month=ToolTimeCustomer.getMonthCurrent();
			int year=ToolTimeCustomer.getYearCurrent();
			fromDateSearch=ToolTimeCustomer.getDateMinCustomer(month, year);
			toDateSearch=ToolTimeCustomer.getDateMaxCustomer(month, year);
		}catch (Exception e) {
			logger.error("ReportBaoCaoLeverBean.initItem:"+e.getMessage(),e);
		}
	}
	public List<Product> completeProduct(String text){
		try{
			List<Product> list=new ArrayList<Product>();
			productService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportBaoCaoLeverBean.completeProduct:"+e.getMessage(),e);
		}
		return null;
	}
	@Inject
	ParamReportDetailService paramReportDetailService;
	public void reportBaoCaoTonKhoLever(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
		try{
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date",ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id",productSearch==null ? 0:productSearch.getId());
			List<BaoCaoLever> list=new ArrayList<>();
			reportService.reportBaoCaoLever(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/bao_cao_lever/bao_cao_lever.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
		    	importParam.put("logo", FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/gfx/lixco_logo.png"));
		    	StringBuilder title=new StringBuilder();
		    	title.append("BÁO CÁO TỒN KHO LEVER ");
		    	if(fromDateSearch !=null){
		    		title.append("TỪ NGÀY "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	}
		    	if(toDateSearch !=null){
		    		title.append("ĐẾN NGÀY "+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
		    	}
		    	
		    	List<ParamReportDetail> listConfig = paramReportDetailService.findByParamReportName("thongtinchung");
				for (ParamReportDetail p : listConfig) {
					importParam.put(p.getKey(), p.getValue());
				}
		    	importParam.put("title", title.toString());
		    	JRDataSource beanDataSource = new JRBeanCollectionDataSource(list);
		    	JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam, beanDataSource);
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				String ba = Base64.getEncoder().encodeToString(data);
		        current.executeScript("PF('showImg').show();");
		        current.executeScript("utility.showPDFK('" + ba + "','')");
			}else{
				notify.warning("Không có dữ liệu");	
			}
		}catch (Exception e) {
			logger.error("ReportBaoCaoLeverBean.reportBaoCaoTonKhoLever:"+e.getMessage(),e);
		}
	}
	public void reportBaoCaoTonKhoLeverExcel(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date",ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id",productSearch==null ? 0:productSearch.getId());
			List<BaoCaoLever> list=new ArrayList<>();
			reportService.reportBaoCaoLever(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				List<Object[]> listResult=new ArrayList<>();
				Object[] title={"MASP","LEVER CODE","TÊN SẢN PHẨM","BRAND CODE","BRAND NAME","PCOM_CODE","PCOM_NAME","QUI CÁCH","HỆ SỐ QĐ","SỐ LƯỢNG ĐẦU(ĐVT)",
						"SỐ LƯỢNG ĐẦU(THÙNG)","SỐ LƯỢNG ĐẦU KG","SỐ LƯỢNG NHẬP(ĐVT)","SỐ LƯỢNG NHẬP(THÙNG)","SỐ LƯỢNG NHẬP KG","SỐ LƯỢNG XUẤT(ĐVT)","SỐ LƯỢNG XUẤT(THÙNG)",
						"SỐ LƯỢNG XUẤT KG","SỐ LƯỢNG CUỐI(ĐVT)","SỐ LƯỢNG CUỐI(THÙNG)","SỐ LƯỢNG CUỐI(KG"};
				listResult.add(title);
				for(BaoCaoLever p:list){
					Object[] row={p.getProduct_code(),p.getLever_code(),p.getProduct_name(),Objects.toString(p.getPbrand_code(),""),Objects.toString(p.getPbrand_name(),""),
							Objects.toString(p.getPcom_code(),""),Objects.toString(p.getPbrand_name(),""),p.getSpecification(),p.getFactor(),p.getUnit_opening_balance(),p.getBox_opening_balance(),
							p.getKg_opening_balance(),p.getUnit_import_quantity(),p.getBox_import_quantity(),p.getKg_import_quantity(),p.getUnit_export_quantity(),
							p.getBox_export_quantity(),p.getKg_export_quantity(),p.getUnit_closing_balance(),p.getBox_closing_balance(),p.getKg_closing_balance()};
					listResult.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("ton_san_pham_lever_");
		    	if(fromDateSearch !=null){
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
			logger.error("ReportBaoCaoLeverBean.reportBaoCaoTonKhoLeverExcel:"+e.getMessage(),e);
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
	public Product getProductSearch() {
		return productSearch;
	}
	public void setProductSearch(Product productSearch) {
		this.productSearch = productSearch;
	}
}
