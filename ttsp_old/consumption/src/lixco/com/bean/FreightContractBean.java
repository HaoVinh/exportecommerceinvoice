package lixco.com.bean;

import java.math.BigDecimal;
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
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DualListModel;

import com.google.gson.JsonObject;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.NavigationInfo;
import lixco.com.common.SessionHelper;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Car;
import lixco.com.entity.Customer;
import lixco.com.entity.FreightContract;
import lixco.com.entity.FreightContractDetail;
import lixco.com.entity.PaymentMethod;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.ICarService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IFreightContractService;
import lixco.com.interfaces.IPaymentMethodService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.reqInfo.FreightContractDetailReqInfo;
import lixco.com.reqInfo.FreightContractReqInfo;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.util.Notify;
@Named
@ViewScoped
public class FreightContractBean  extends AbstractBean  {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IFreightContractService freightContractService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private ICustomerService customerService;
	@Inject
	private IPaymentMethodService paymentMethodService;
	@Inject
	private ICarService carService;
	private FreightContract freightContractSelect;
	private FreightContract freightContractCrud;
	private List<FreightContract> listFreightContract;
	private FreightContractDetail freightContractDetailSelect;
	private FreightContractDetail freightContractDetailCrud;
	private List<FreightContractDetail> listFreightContractDetail;
	private List<ProductType> listProductType;
	/*Search*/
	private Date fromDateSearch;
	private Date toDateSearch;
	private Customer customerSearch;
	private String contractNoSearch;
	private Car carSearch;
	private PaymentMethod paymentMethodSearch;
	private List<PaymentMethod> listPaymentMethod;
	private int paymentSearch;
	/*Tổng hợp từ hóa đơn*/
	private DualListModel<String> modelInvoiceCode;
	private boolean change;
	
	
	private int pageSize;
	private List<Integer> listRowPerPage;
	private Account account;
	private FormatHandler formatHandler;
  	@Override
	protected void initItem() {
		try{
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);
			/*init FreightContract*/
			freightContractCrud=new FreightContract();
			freightContractDetailCrud=new FreightContractDetail();
			freightContractCrud.setContract_date(new Date());
			formatHandler=FormatHandler.getInstance();
			listPaymentMethod=new ArrayList<>();
			paymentMethodService.selectAll(listPaymentMethod);
			listProductType=new ArrayList<>();
			productTypeService.selectAll(listProductType);
			search();
			modelInvoiceCode=new DualListModel<>(new ArrayList<>(), new ArrayList<>());
		}catch (Exception e) {
			logger.error("FreightContractBean.initItem:"+e.getMessage(),e);
		}
	}
  	public void showDialogPickInvoice(){
  		PrimeFaces current=PrimeFaces.current();
  		try{
  			if(freightContractCrud !=null && freightContractCrud.getId() !=0){
  				modelInvoiceCode=new DualListModel<>(new ArrayList<>(), new ArrayList<>());
  				//lấy danh mã hóa đơn chứa hợp đồng vận chuyển này
  				freightContractService.getListInvoiceCode(freightContractCrud.getContract_date(),freightContractCrud.getCustomer().getId(),modelInvoiceCode.getSource());
  				 current.executeScript("PF('dlg2').show();");
  			}else{
  				current.executeScript(
						"swaldesigntimer('Cảnh báo', 'Hợp đồng không tồn tại','warning',2000);");
  			}
  		}catch (Exception e) {
  			logger.error("FreightContractBean.showDialogPickInvoice:"+e.getMessage(),e);
		}
  	}
  	public void createNew(){
		try{
			freightContractCrud=new FreightContract();
			listFreightContractDetail=new ArrayList<>();
		}catch (Exception e) {
			logger.error("FreightContractBean.createNew:"+e.getMessage(),e);
		}
	}
  	private void initRowPerPage() {
		try {
			listRowPerPage = new ArrayList<Integer>();
			listRowPerPage.add(90);
			listRowPerPage.add(180);
			listRowPerPage.add(240);
			pageSize = listRowPerPage.get(0);
		} catch (Exception e) {
			logger.error("FreightContractBean.initRowPerPage:" + e.getMessage(), e);
		}
	}
  
  	public void search(){
		try{
			/*{freight_contract_info:{contract_no:'',from_date:'',to_date:'',customer_id:0,car_id:0,payment_method_id:0,payment:-1}, page:{page_index:0, page_size:0}}*/
			listFreightContract=new ArrayList<>();
			JsonObject jsonInfo=new JsonObject();
			jsonInfo.addProperty("contract_code", contractNoSearch);
			jsonInfo.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch,"dd/MM/yyyy"));
			jsonInfo.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("customer_id", customerSearch==null ? 0: customerSearch.getId());
			jsonInfo.addProperty("car_id", carSearch==null ? 0 :carSearch.getId());
			jsonInfo.addProperty("payment_method_id", paymentMethodSearch==null ? 0 :paymentMethodSearch.getId());
			jsonInfo.addProperty("payment", paymentSearch);
			JsonObject json=new JsonObject();
			json.add("contract", jsonInfo);
			freightContractService.search(JsonParserUtil.getGson().toJson(json), listFreightContract);
		}catch(Exception e){
			logger.error("FreightContractBean.search:"+e.getMessage(),e);
		}
	}
  	
  	public void saveOrUpdate(){
		PrimeFaces current=PrimeFaces.current();
		try{
			if(freightContractCrud != null){
				//kiểm tra dữ liệu có đầy đủ không 
				String contractNo=freightContractCrud.getContract_no();
				Date contractDate=freightContractCrud.getContract_date();
				Customer customer=freightContractCrud.getCustomer();
				Car car=freightContractCrud.getCar();
				if(contractNo!=null && !"".equals(contractNo) && contractDate !=null && customer !=null && car !=null){
					FreightContractReqInfo t=new FreightContractReqInfo(freightContractCrud);
					if(freightContractCrud.getId()==0){
						if(allowSave(new Date())){
							freightContractCrud.setCreated_date(new Date());
							freightContractCrud.setCreated_by(account.getMember().getName());
							int code=freightContractService.insert(t);
							switch (code) {
							case 0:
								//nếu thành công thêm hợp đồng vào danh sách
								listFreightContract.add(0,t.getFreight_contract());
								current.executeScript(
											"swaldesigntimer('Thành công!', 'Tạo hợp đồng thành công!','success',2000);");
								break;
							default:
								current.executeScript(
										"swaldesigntimer('Xảy ra lỗi!', 'Không lưu được!','warning',2000);");
								break;
							}
						}else{
							current.executeScript(
									"swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
						}
					}else{
						if(allowUpdate(new Date())){
							freightContractCrud.setLast_modifed_by(account.getMember().getName());
							freightContractCrud.setLast_modifed_date(new Date());
							int code=freightContractService.update(t);
							switch (code) {
							case 0:
								//nếu cập nhật thành công thì cập nhật lại danh sách.
								listFreightContract.set(listFreightContract.indexOf(freightContractCrud),t.getFreight_contract());
								if(listFreightContractDetail !=null && listFreightContractDetail.size()>0 && change){
									//xóa tất cả chi tiết hợp đồng cũ
									freightContractService.deleteDetailByFreightContract(freightContractCrud.getId());
									FreightContractDetailReqInfo freightContractDetailReqInfo=new FreightContractDetailReqInfo();
									for(FreightContractDetail p:listFreightContractDetail){
										p.setFreight_contract(freightContractCrud);
										freightContractDetailReqInfo.setFreight_contract_detail(p);
										//lưu chi tiết mới vào 
										freightContractService.insertDetail(freightContractDetailReqInfo);
										
									}
									current.executeScript(
											"swaldesigntimer('Thành công!', 'Cập nhật thành công!','success',2000);");
									change=false;
								}
								break;
							default:
								current.executeScript(
										"swaldesigntimer('Xảy ra lỗi!', 'Cập nhật thất bại!','warning',2000);");
								break;
							}
						}else{
							current.executeScript(
									"swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
						}
					}
				}else{
					current.executeScript("swaldesigntimer('Cảnh báo!','Thông tin không đầy đủ!','warning',2000);");
				}
				
			}
		} catch (Exception e) {
			logger.error("FreightContractBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}
  	public void showDialogAddFreightContractDetail(){
 	   PrimeFaces current=PrimeFaces.current();
 	   Notify notify = new Notify(FacesContext.getCurrentInstance());
        try{
     	   if(freightContractCrud !=null && freightContractCrud.getId()!=0){
     		  freightContractDetailCrud=new FreightContractDetail();
     		  freightContractDetailCrud.setFreight_contract(freightContractCrud);
     	     current.executeScript("PF('dlg1').show();");
     	   }else{
     		   notify.warning("Hợp đồng không tồn tại!");
     	   }
 		}catch (Exception e) {
 			logger.error("FreightContractBean.showDialogAddContractDetail:" + e.getMessage(), e);
 		}
 	}
 	public void showDialogEditFreightContractDetail(){
 		PrimeFaces current=PrimeFaces.current();
 		Notify notify = new Notify(FacesContext.getCurrentInstance());
 		try{
 			if(freightContractCrud!=null && freightContractCrud.getId()!=0){
 				if(freightContractSelect !=null && freightContractSelect.getId() !=0){
 					  freightContractDetailCrud=freightContractDetailSelect.clone();
 					  current.executeScript("PF('dlg1').show();");
 				}else{
 					 notify.warning("Chưa chọn chi tiết hợp đồng để chỉnh sửa!");
 				}
 			}else{
 				notify.warning("Hợp đồng không tồn tại!");
 			}
 		}catch(Exception e){
 			logger.error("FreightContractBean.showDialogEditContractDetail:" + e.getMessage(), e);
 		}
 	}
 	public void calFreightContractByInvoice(){
 		PrimeFaces current=PrimeFaces.current();
 		Notify notify = new Notify(FacesContext.getCurrentInstance());
 		try{
 			if(allowUpdate(new Date())){
 				if(modelInvoiceCode !=null && modelInvoiceCode.getTarget() !=null && modelInvoiceCode.getTarget().size() >0){
 					//check đã tồn tại chi tiết chưa
 					listFreightContractDetail=new ArrayList<>();
 					freightContractService.getListDataFreightContractByInvoice(modelInvoiceCode.getTarget(), listFreightContractDetail);
 					//lấy danh sách xe trong hóa đơn
 					Car car=freightContractService.getCarByInvoice(modelInvoiceCode.getTarget());
 					if(car !=null){
 						freightContractCrud.setCar(car);
 					}
 					freightContractCrud.setNote(org.apache.commons.lang3.StringUtils.join(modelInvoiceCode.getTarget(), ","));
 					change=true;
 					notify.success();
 				}else{
 					notify.warning("Chưa chọn hóa đơn");
 				}
 			}else{
 				current.executeScript(
						"swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
 			}
 		}catch (Exception e) {
 			logger.error("FreightContractBean.calFreightContractByInvoice:" + e.getMessage(), e);
		}
 	}
 	public void saveOrUpdateDetail(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current=PrimeFaces.current();
        try{
			if(freightContractDetailCrud !=null){
				//kiểm tra số liệu
				ProductType productType=freightContractDetailCrud.getProduct_type();
				double quantity=freightContractDetailCrud.getQuantity();
				if(productType !=null && quantity !=0){
					FreightContractDetailReqInfo t=new FreightContractDetailReqInfo(freightContractDetailCrud.clone());
;					if(freightContractDetailCrud.getId()==0){
						if(allowSave(new Date())){
							freightContractDetailCrud.setCreated_by(account.getMember().getName());
							freightContractDetailCrud.setCreated_date(new Date());
							if(freightContractService.insertDetail(t)==0){
								//thêm vào danh sách.
								listFreightContractDetail.add(0, t.getFreight_contract_detail());
								notify.success("Lưu thành công!");
								//refesh lại dialog 
								freightContractDetailCrud=new FreightContractDetail();
								freightContractDetailCrud.setFreight_contract(freightContractCrud);
							}else{
								notify.warning("Lưu thất bại!");
							}
						}else{
							current.executeScript(
									"swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
						}
					}else{
						if(allowUpdate(new Date())){
							freightContractDetailCrud.setLast_modifed_by(account.getMember().getName());
							freightContractDetailCrud.setLast_modifed_date(new Date());
							if(freightContractService.updateDetail(t)==0){
								//cập nhật lại danh sách
								listFreightContractDetail.set(listFreightContractDetail.indexOf(freightContractDetailCrud),t.getFreight_contract_detail());
								notify.success("Cập nhật thành công!");
							}else{
								notify.warning("Cập nhật thất bại!");
							}
						}else{
							current.executeScript(
									"swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
						}
					}
				}else{
					notify.warning("Thông tin không đầy đủ!");
				}
			}
		}catch (Exception e) {
			logger.error("FreightContractBean.saveOrUpdateDetail:" + e.getMessage(), e);
		}
	}
  	public List<Customer> completeCustomer(String text){
  		try{
  			List<Customer> list=new ArrayList<>();
  			customerService.complete(FormatHandler.getInstance().converViToEn(text), list);
  			return list;
  		}catch (Exception e) {
  			logger.error("FreightContractBean.completeCustomer:"+e.getMessage(),e);
		}
		return null;
  	}
  	public List<Car> completeCar(String text){
  		try{
  			List<Car> list=new ArrayList<>();
  			carService.complete2(formatHandler.converViToEn(text), list);
  			return list;
  		}catch (Exception e) {
			logger.error("FreightContractBean.completeCar:"+e.getMessage(),e);
		}
  		return null;
  	}
  	public void reportBienBangVanChuyenHangHoa(){
  		PrimeFaces current=PrimeFaces.current();
  		try{
			String reportPath = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/resources/reports/hopdongvc/hopdongvc.jasper");
			Map<String, Object> importParam = new HashMap<String, Object>();
			Locale locale = new Locale("vi", "VI");
			importParam.put(JRParameter.REPORT_LOCALE, locale);
	    	FreightContract freightContract=freightContractCrud.clone();
	    	List<FreightContractDetail> listDetail=new ArrayList<>();
	    	for(FreightContractDetail f:listFreightContractDetail){
	    		listDetail.add(f.clone());
	    	}
	    	freightContract.setList_freight_contract_detail(listDetail);
	    	List<FreightContract> list=new ArrayList<>();
	    	list.add(freightContract);
	    	JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,new JRBeanCollectionDataSource(list));
			byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
			String ba = Base64.getEncoder().encodeToString(data);
			current.executeScript("utility.printPDF('" + ba + "')");
  		}catch (Exception e) {
  			logger.error("FreightContractBean.reportBienBangVanChuyenHangHoa:"+e.getMessage(),e);
		}
  	}
  	public void deleteDetail(FreightContractDetail item){
  		Notify notify = new Notify(FacesContext.getCurrentInstance());
  		PrimeFaces current=PrimeFaces.current();
  		try{
  			if(item !=null){
  				if(allowDelete(new Date())){
	  				if(item.getId()==0){
	  					listFreightContractDetail.remove(item);
	  					notify.success();
	  				}else{
	  					int code=freightContractService.deleteByFreightContractDetailId(item.getId());
	  					if(code>0){
	  						notify.success("Xóa thành công");
	  						listFreightContractDetail.remove(item);
	  					}else{
	  						notify.warning("Không xóa được");
	  					}
	  				}
  				}else{
  					current.executeScript(
							"swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
  				}
  			}
  			freightContractDetailSelect=null;
  		}catch (Exception e) {
  			logger.error("FreightContractBean.deleteDetail:"+e.getMessage(),e);
		}
  	}
  	public void deleteFreightContract(){
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try{
			if(freightContractSelect !=null && freightContractSelect.getId()!=0){
				if(allowDelete(new Date())){
					if(freightContractService.deleteById(freightContractSelect.getId())>0){
						listFreightContract.remove(freightContractSelect);
						notify.success("Xóa thành công!");
					}else{
						notify.warning("Xóa thất bại!");
					}
 				}else{
					notify.warning("Tài khoản chưa được phân quyền!");
				}
			}else{
				notify.warning("Chưa chọn dòng để xóa!");
			}
		}catch (Exception e) {
			logger.error("FreightContractBean.deleteFreightContract:"+e.getMessage(),e);
		}
	}
  	public double sumFreightContract(){
  		double sum=0;
  		try{
  			if(listFreightContractDetail !=null && listFreightContractDetail.size()>0){
				for(FreightContractDetail item:listFreightContractDetail){
					BigDecimal thanhtien=BigDecimal.valueOf(item.getQuantity()).multiply(BigDecimal.valueOf(item.getUnit_price()));
					sum=BigDecimal.valueOf(sum).add(thanhtien).doubleValue();
				}
				return sum;
			}
  		}catch (Exception e) {
  			logger.error("FreightContractBean.sumFreightContract:"+e.getMessage(),e);
		}
  		return 0;
  	}
  	public void showContract(){
		try{
			if(freightContractSelect!=null){
				freightContractCrud=freightContractSelect.clone();
				freightContractDetailCrud=null;
				freightContractDetailSelect=null;
				//load chi tiết hợp đồng.
				listFreightContractDetail=new ArrayList<>();
				freightContractService.selectFreightContractDetailByFreightContractId(freightContractSelect.getId(), listFreightContractDetail);
			}
		}catch (Exception e) {
			logger.error("ContractBean.showContract:"+e.getMessage(),e);
		}
	}
	@Override
	protected Logger getLogger() {
		return logger;
	}
	public FreightContract getFreightContractSelect() {
		return freightContractSelect;
	}
	public void setFreightContractSelect(FreightContract freightContractSelect) {
		this.freightContractSelect = freightContractSelect;
	}
	public FreightContract getFreightContractCrud() {
		return freightContractCrud;
	}
	public void setFreightContractCrud(FreightContract freightContractCrud) {
		this.freightContractCrud = freightContractCrud;
	}
	public List<FreightContract> getListFreightContract() {
		return listFreightContract;
	}
	public void setListFreightContract(List<FreightContract> listFreightContract) {
		this.listFreightContract = listFreightContract;
	}
	public FreightContractDetail getFreightContractDetailSelect() {
		return freightContractDetailSelect;
	}
	public void setFreightContractDetailSelect(FreightContractDetail freightContractDetailSelect) {
		this.freightContractDetailSelect = freightContractDetailSelect;
	}
	public FreightContractDetail getFreightContractDetailCrud() {
		return freightContractDetailCrud;
	}
	public void setFreightContractDetailCrud(FreightContractDetail freightContractDetailCrud) {
		this.freightContractDetailCrud = freightContractDetailCrud;
	}
	public List<FreightContractDetail> getListFreightContractDetail() {
		return listFreightContractDetail;
	}
	public void setListFreightContractDetail(List<FreightContractDetail> listFreightContractDetail) {
		this.listFreightContractDetail = listFreightContractDetail;
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
	public Customer getCustomerSearch() {
		return customerSearch;
	}
	public void setCustomerSearch(Customer customerSearch) {
		this.customerSearch = customerSearch;
	}
	public String getContractNoSearch() {
		return contractNoSearch;
	}
	public void setContractNoSearch(String contractNoSearch) {
		this.contractNoSearch = contractNoSearch;
	}
	public Car getCarSearch() {
		return carSearch;
	}
	public void setCarSearch(Car carSearch) {
		this.carSearch = carSearch;
	}
	public PaymentMethod getPaymentMethodSearch() {
		return paymentMethodSearch;
	}
	public void setPaymentMethodSearch(PaymentMethod paymentMethodSearch) {
		this.paymentMethodSearch = paymentMethodSearch;
	}
	public List<PaymentMethod> getListPaymentMethod() {
		return listPaymentMethod;
	}
	public void setListPaymentMethod(List<PaymentMethod> listPaymentMethod) {
		this.listPaymentMethod = listPaymentMethod;
	}
	public int getPaymentSearch() {
		return paymentSearch;
	}
	public void setPaymentSearch(int paymentSearch) {
		this.paymentSearch = paymentSearch;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List<Integer> getListRowPerPage() {
		return listRowPerPage;
	}
	public void setListRowPerPage(List<Integer> listRowPerPage) {
		this.listRowPerPage = listRowPerPage;
	}
	public FormatHandler getFormatHandler() {
		return formatHandler;
	}
	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
	}
	public DualListModel<String> getModelInvoiceCode() {
		return modelInvoiceCode;
	}
	public void setModelInvoiceCode(DualListModel<String> modelInvoiceCode) {
		this.modelInvoiceCode = modelInvoiceCode;
	}
	
}
