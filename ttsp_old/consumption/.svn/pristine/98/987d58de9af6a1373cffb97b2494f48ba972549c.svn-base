package lixco.com.bean_report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
import lixco.com.entity.Area;
import lixco.com.entity.City;
import lixco.com.entity.Customer;
import lixco.com.entity.IECategories;
import lixco.com.entity.Product;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.IAreaService;
import lixco.com.interfaces.ICityService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.TongKetXuatSanPhamTheoQuocGia;
import lixco.com.reportInfo.TongKetXuatTheoThanhPhoTongKetTieuThu;
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
public class ReportTongKetXuatTheoThanhPhoBean extends AbstractBean {
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
	private IAreaService areaService;
	@Inject
	private ICityService cityService;
	private Customer customerSearch;
	private Product productSearch;
	private IECategories iECategoriesSearch;
	private List<IECategories> listIECategories;
	private ProductType productTypeSearch;
	private List<ProductType> listProductType;
	private Date fromDateSearch;
	private Date toDateSearch;
	private Area areaSearch;
	private List<Area> listArea;
	private City citySearch;
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
			listArea=new ArrayList<>();
			areaService.selectAll(listArea);
		}catch (Exception e) {
			logger.error("ReportTongKetXuatTheoThanhPhoBean.initItem:"+e.getMessage(),e);
		}
	}
	@Override
	protected Logger getLogger() {
		return logger;
	}
	public void reportTongKetXuatTheoThanhPhoTongKetTieuThu(){
		PrimeFaces current=PrimeFaces.current();
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,ie_categories_id:0,area_id:0,city_id:0,typep:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("product_type_id", productTypeSearch==null ? 0 : productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ?0 :iECategoriesSearch.getId());
			json.addProperty("area_id", areaSearch==null ? 0 : areaSearch.getId());
			json.addProperty("city_id", citySearch==null ? 0:citySearch.getId());
			List<TongKetXuatTheoThanhPhoTongKetTieuThu> list=new ArrayList<TongKetXuatTheoThanhPhoTongKetTieuThu>();
			reportService.reportTongKetXuatTheoThanhPho(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				String reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/reportinsanpham/report_tk_xuat_theo_thanh_pho/tongketxuattheothanhpho.jasper");
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
			logger.error("ReportTongKetXuatTheoThanhPhoBean.reportTongKetXuatTheoThanhPho:"+e.getMessage(),e);
		}
	}
	public void reportTongKetXuatTheoKhuVuc(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,ie_categories_id:0,area_id:0,city_id:0,typep:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("product_type_id", productTypeSearch==null ? 0 : productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ?0 :iECategoriesSearch.getId());
			json.addProperty("area_id", areaSearch==null ? 0 : areaSearch.getId());
			json.addProperty("city_id", citySearch==null ? 0:citySearch.getId());
			List<TongKetXuatTheoThanhPhoTongKetTieuThu> list=new ArrayList<TongKetXuatTheoThanhPhoTongKetTieuThu>();
			reportService.reportTongKetXuatTheoThanhPho(JsonParserUtil.getGson().toJson(json), list);
			List<Object[]> listResult=new ArrayList<>();
			Object[] header={"MÃ KHU VỰC","MÃ THÀNH PHỐ","MÃ NX","MÃ BRAND","MASP","TÊN DỮ LIỆU","SỐ LƯỢNG","SỐ TIỀN USD","SỐ TIỀN","TỈ LỆ","SỐ THÙNG"};
			listResult.add(header);
			if(list.size()>0){
//				a1.area_name,c1.city_name,pb1.pbrand_name,p1.product_name 
				//group by area id
				Map<Long, List<TongKetXuatTheoThanhPhoTongKetTieuThu>> groupByAreaId=list.stream().collect(Collectors.groupingBy(TongKetXuatTheoThanhPhoTongKetTieuThu::getArea_id,Collectors.toList()));
				for (Map.Entry<Long, List<TongKetXuatTheoThanhPhoTongKetTieuThu>> entry : groupByAreaId.entrySet()) {
			        List<TongKetXuatTheoThanhPhoTongKetTieuThu> listGroupAreaId=entry.getValue();
			        Object[] rowArea=new Object[11];
			        rowArea[0]=listGroupAreaId.get(0).getArea_code();
			        rowArea[1]="";
			        rowArea[2]="";
			        rowArea[3]="";
			        rowArea[4]="";
			        rowArea[5]=listGroupAreaId.get(0).getArea_name();
			        rowArea[6]=0.0;//quantity area
			        rowArea[7]=0.0;//total foreign amount area 
			        rowArea[8]=0.0;//total amount area
			        rowArea[9]=0.0;//ratio
			        rowArea[10]=0.0;//box quantity area
			        for(TongKetXuatTheoThanhPhoTongKetTieuThu p:listGroupAreaId){
			        	rowArea[6]=BigDecimal.valueOf((Double)rowArea[6]).add(BigDecimal.valueOf(p.getQuantity())).doubleValue();
			        	rowArea[7]=BigDecimal.valueOf((Double)rowArea[7]).add(BigDecimal.valueOf(p.getTotal_foreign_amount())).doubleValue();
			        	rowArea[8]=BigDecimal.valueOf((Double)rowArea[8]).add(BigDecimal.valueOf(p.getTotal_amount())).doubleValue();
			        	rowArea[10]=BigDecimal.valueOf((Double)rowArea[10]).add(BigDecimal.valueOf(p.getBox_quantity())).doubleValue();
			        }
			        //add resultant 
			        listResult.add(rowArea);
			        //group by city id
			        Map<Long,List<TongKetXuatTheoThanhPhoTongKetTieuThu>> groupByCityId=listGroupAreaId.stream().collect(Collectors.groupingBy(TongKetXuatTheoThanhPhoTongKetTieuThu::getCity_id,Collectors.toList()));
			        for(Map.Entry<Long, List<TongKetXuatTheoThanhPhoTongKetTieuThu>> entry1: groupByCityId.entrySet()){
			        	 List<TongKetXuatTheoThanhPhoTongKetTieuThu> listGroupCityId=entry1.getValue();
			        	 Object[] rowCity=new Object[11];
			        	 rowCity[0]=rowArea[0];//area_code
			        	 rowCity[1]=listGroupCityId.get(0).getCity_code();//city_code
			        	 rowCity[2]="";//product_type_code
			        	 rowCity[3]="";//pbrand_code
			        	 rowCity[4]="";//product_code
			        	 rowCity[5]="* "+listGroupCityId.get(0).getCity_name();// tên thành phố
			        	 rowCity[6]=0.0;//quantity city
			        	 rowCity[7]=0.0;//total foreign amount city 
			        	 rowCity[8]=0.0;//total amount city
			        	 rowCity[9]=0.0;//ratio
			        	 rowCity[10]=0.0;//box quantity city
			        	 for(TongKetXuatTheoThanhPhoTongKetTieuThu p1:listGroupCityId){
			        		 rowCity[6]=BigDecimal.valueOf((Double)rowCity[6]).add(BigDecimal.valueOf(p1.getQuantity())).doubleValue();
			        		 rowCity[7]=BigDecimal.valueOf((Double)rowCity[7]).add(BigDecimal.valueOf(p1.getTotal_foreign_amount())).doubleValue();
			        		 rowCity[8]=BigDecimal.valueOf((Double)rowCity[8]).add(BigDecimal.valueOf(p1.getTotal_amount())).doubleValue();
			        		 rowCity[10]=BigDecimal.valueOf((Double)rowCity[10]).add(BigDecimal.valueOf(p1.getBox_quantity())).doubleValue();
			        	 }
			        	 listResult.add(rowCity);
			        	 //group by product_ty_id
			        	 Map<Long, List<TongKetXuatTheoThanhPhoTongKetTieuThu>> groupByProductTypeId=listGroupCityId.stream().collect(Collectors.groupingBy(TongKetXuatTheoThanhPhoTongKetTieuThu::getProduct_type_id,Collectors.toList()));
			        	 for(Map.Entry<Long,List<TongKetXuatTheoThanhPhoTongKetTieuThu>> entry2:groupByProductTypeId.entrySet()){
			        		 long keyProductTypeId=entry2.getKey();
			        		 List<TongKetXuatTheoThanhPhoTongKetTieuThu> listGroupProductTypeId=entry2.getValue();
			        		 Object[] rowProductTypeId=new Object[11];
			        		 rowProductTypeId[0]=rowArea[0];// area code
			        		 rowProductTypeId[1]=rowCity[1];//city_code
			        		 rowProductTypeId[2]=listGroupProductTypeId.get(0).getProduct_type_code();//product_type_code
			        		 rowProductTypeId[3]="";//pbrand_code
			        		 rowProductTypeId[4]="";//product_code
			        		 rowProductTypeId[5]="* * "+listGroupProductTypeId.get(0).getProduct_type_name();
			        		 rowProductTypeId[6]=0.0;//quantity product type
			        		 rowProductTypeId[7]=0.0;//total foreign amount product type 
			        		 rowProductTypeId[8]=0.0;//total amount product type
			        		 rowProductTypeId[9]=0.0;//ratio
			        		 rowProductTypeId[10]=0.0;//box quantity product type
				        	 for(TongKetXuatTheoThanhPhoTongKetTieuThu p2:listGroupCityId){
				        		 rowProductTypeId[6]=BigDecimal.valueOf((Double)rowProductTypeId[6]).add(BigDecimal.valueOf(p2.getQuantity())).doubleValue();
				        		 rowProductTypeId[7]=BigDecimal.valueOf((Double)rowProductTypeId[7]).add(BigDecimal.valueOf(p2.getTotal_foreign_amount())).doubleValue();
				        		 rowProductTypeId[8]=BigDecimal.valueOf((Double)rowProductTypeId[8]).add(BigDecimal.valueOf(p2.getTotal_amount())).doubleValue();
				        		 rowProductTypeId[10]=BigDecimal.valueOf((Double)rowProductTypeId[10]).add(BigDecimal.valueOf(p2.getBox_quantity())).doubleValue();
				        	 }
				        	 listResult.add(rowProductTypeId);
			        		 //group by brand
			        		 Map<Long,List<TongKetXuatTheoThanhPhoTongKetTieuThu>> groupByBrandId=listGroupProductTypeId.stream().collect(Collectors.groupingBy(TongKetXuatTheoThanhPhoTongKetTieuThu::getProduct_brand_id,Collectors.toList()));
			        		 for(Map.Entry<Long, List<TongKetXuatTheoThanhPhoTongKetTieuThu>> entry3:groupByBrandId.entrySet()){
			        			 long keyBrandId=entry3.getKey();
			        			 List<TongKetXuatTheoThanhPhoTongKetTieuThu> listGroupProductBrandId=entry3.getValue();
			        			 Object[] rowProductBrand=new Object[11];
			        			 rowProductBrand[0]=rowArea[0];// area code
			        			 rowProductBrand[1]=rowCity[1];//city_code
			        			 rowProductBrand[2]=listGroupProductBrandId.get(0).getProduct_type_code();//product_type_code
			        			 rowProductBrand[3]=listGroupProductBrandId.get(0).getProduct_brand_code();//pbrand_code
			        			 rowProductBrand[4]="";//product_code
			        			 rowProductBrand[5]="* * *"+listGroupProductBrandId.get(0).getProduct_brand_name();
			        			 rowProductBrand[6]=0.0;//quantity area
			        			 rowProductBrand[7]=0.0;//total foreign amount area 
			        			 rowProductBrand[8]=0.0;//total amount area
			        			 rowProductBrand[9]=0.0;//ratio
				        		 rowProductBrand[10]=0.0;//box quantity area
				        		 List<Object[]> listFinal=new ArrayList<>();
			        			 for(TongKetXuatTheoThanhPhoTongKetTieuThu p3:listGroupProductBrandId){
			        				 rowProductBrand[6]=BigDecimal.valueOf((Double)rowProductBrand[6]).add(BigDecimal.valueOf(p3.getQuantity())).doubleValue();
			        				 rowProductBrand[7]=BigDecimal.valueOf((Double)rowProductBrand[7]).add(BigDecimal.valueOf(p3.getTotal_foreign_amount())).doubleValue();
			        				 rowProductBrand[8]=BigDecimal.valueOf((Double)rowProductBrand[8]).add(BigDecimal.valueOf(p3.getTotal_amount())).doubleValue();
			        				 rowProductBrand[10]=BigDecimal.valueOf((Double)rowProductBrand[10]).add(BigDecimal.valueOf(p3.getBox_quantity())).doubleValue();
			        				 Object[] rowProduct=new Object[11];
			        				 rowProduct[0]=rowArea[0];// area code
			        				 rowProduct[1]=rowCity[1];//city_code
			        				 rowProduct[2]=rowProductBrand[2];////product_type_code
			        				 rowProduct[3]=rowProductBrand[3];//pbrand_code
			        				 rowProduct[4]=p3.getProduct_code();//product_code
			        				 rowProduct[5]="* * * *"+p3.getProduct_name();
			        				 rowProduct[6]=p3.getQuantity();//quantity product
			        				 rowProduct[7]=p3.getTotal_foreign_amount();//total foreign amount  prodcut
			        				 rowProduct[8]=p3.getTotal_amount();//total amount product
			        				 rowProduct[9]=0.0;//ratio product
			        				 rowProduct[10]=p3.getBox_quantity();//box quantity product
			        				 listFinal.add(rowProduct);
			        			 }
			        			 listResult.add(rowProductBrand);
			        			 //add list final 
			        			 listResult.addAll(listFinal);
			        		 }
			        		 
			        	 }
			        	 
			        }
			    }
				StringBuilder title2=new StringBuilder();
		    	title2.append("tong_ket_xuat_theo_khu_vuc");
		    	if(fromDateSearch !=null)
		    	   title2.append("tu_ngay "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	if(toDateSearch!=null)
		    	   title2.append("_den_ngay "+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
				ToolReport.printReportExcelRaw(listResult,title2.toString());
			}else{
				notify.warning("không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("ReportTongKetXuatTheoThanhPhoBean.reportTongKetXuatTheoKhuVuc:"+e.getMessage(),e);
		}
	}
	public void reportTongKetXuatTheoQuocGia(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			/*{from_date:'',to_date:'',product_id:0,product_type_id:0,customer_id:0,ie_categories_id:0,area_id:0,city_id:0,typep:0}*/
			JsonObject json=new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			json.addProperty("product_id", productSearch !=null ? productSearch.getId() : 0);
			json.addProperty("product_type_id", productTypeSearch==null ? 0 : productTypeSearch.getId());
			json.addProperty("customer_id", customerSearch==null ? 0 :customerSearch.getId());
			json.addProperty("ie_categories_id", iECategoriesSearch==null ?0 :iECategoriesSearch.getId());
			json.addProperty("area_id", areaSearch==null ? 0 : areaSearch.getId());
			json.addProperty("city_id", citySearch==null ? 0:citySearch.getId());
			List<TongKetXuatSanPhamTheoQuocGia> list=new ArrayList<>();
			reportService.reportTongKetXuatSanPhamTheoQuocGia(JsonParserUtil.getGson().toJson(json), list);
			if(list.size()>0){
				List<Object[]> listResult=new ArrayList<>();
				Object[] header={"MÃ QUỐC GIA","QUỐC GIA","SỐ LƯỢNG BG","TỔNG TIỀN BG","TỔNG TIỀN NGOẠI TỆ BG","SỐ LƯỢNG NTR","TỔNG TIỀN NTR","TỔNG TIỀN NGOẠI TỆ BG"};
				listResult.add(header);
				for(TongKetXuatSanPhamTheoQuocGia t:list){
					Object[] row={t.getCountry_code(),t.getCountry_name(),t.getQuantity_bg(),t.getTotal_amount_bg(),t.getTotal_foreign_amount_bg(),
							t.getQuantity_ntr(),t.getTotal_amount_ntr(),t.getTotal_foreign_amount_ntr()};
					listResult.add(row);
				}
				StringBuilder title2=new StringBuilder();
		    	title2.append("tong_ket_xuat_theo_quoc_gia");
		    	if(fromDateSearch !=null)
		    	   title2.append("tu_ngay "+ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
		    	if(toDateSearch!=null)
		    	   title2.append("_den_ngay "+ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
				ToolReport.printReportExcelRaw(listResult,title2.toString());
			}else{
				notify.warning("không có dữ liệu");
			}
		}catch (Exception e) {
			logger.error("ReportTongKetXuatTheoThanhPhoBean.reportTongKetXuatTheoQuocGia:"+e.getMessage(),e);
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
	public List<City> completeCity(String text){
		try{
			List<City> list=new ArrayList<>();
			if(areaSearch==null){
				cityService.complete(FormatHandler.getInstance().converViToEn(text), list);
			}else{
				cityService.complete(FormatHandler.getInstance().converViToEn(text), areaSearch.getId(), list);
			}
			return list;
		}catch (Exception e) {
			logger.error("ReportInSanPhamBean.completeCity:"+e.getMessage(),e);
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
	public Area getAreaSearch() {
		return areaSearch;
	}
	public void setAreaSearch(Area areaSearch) {
		this.areaSearch = areaSearch;
	}
	public City getCitySearch() {
		return citySearch;
	}
	public void setCitySearch(City citySearch) {
		this.citySearch = citySearch;
	}
	public List<Area> getListArea() {
		return listArea;
	}
	public void setListArea(List<Area> listArea) {
		this.listArea = listArea;
	}
	public ProductType getProductTypeSearch() {
		return productTypeSearch;
	}
	public void setProductTypeSearch(ProductType productTypeSearch) {
		this.productTypeSearch = productTypeSearch;
	}
	
}
