package lixco.com.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import com.google.gson.JsonObject;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.NavigationInfo;
import lixco.com.common.SessionHelper;
import lixco.com.entity.Car;
import lixco.com.entity.CarOwner;
import lixco.com.entity.CarType;
import lixco.com.entity.Carrier;
import lixco.com.interfaces.ICarrierService;
import lixco.com.reqInfo.CarrierReqInfo;
import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.util.ToolReport;

@Named
@ViewScoped
public class CarrierBean  extends AbstractBean  {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private ICarrierService carrierService;
	private Carrier carrierCrud;
	private Carrier carrierSelect;
	private List<Carrier> listCarrier;
	private String carrierCode;
	private String carrierName;
	private List<Integer> listRowPerPage;
	private Account account;
	@Override
	protected void initItem() {
		try{
			search();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);
			carrierCrud=new Carrier();
		}catch(Exception e){
			logger.error("CarrierBean.initItem:"+e.getMessage(),e);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
	public void search() {
		try {
			/*{ carrier_info:{carrier_code:'',carrier_name:''}, page:{page_index:0, page_size:0}}*/
			listCarrier = new ArrayList<>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("carrier_code", carrierCode);
			jsonInfo.addProperty("carrier_name", carrierName);
			JsonObject json = new JsonObject();
			json.add("carrier_info", jsonInfo);
			carrierService.search(JsonParserUtil.getGson().toJson(json),  listCarrier);
		} catch (Exception e) {
			logger.error("CarrierBean.search:" + e.getMessage(), e);
		}
	}


	public void exportExcel() {
		try {
			if (listCarrier.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "id", "ma", "tennguoivanchuyen"};
				results.add(title);
				for (Carrier p : listCarrier) {
					Object row[] = { p.getId(), p.getCarrier_code(), p.getCarrier_name()};
					results.add(row);
				}
				String titleEx = "dmnvc";
				ToolReport.printReportExcelRaw(results, titleEx);
			}
		} catch (Exception e) {
			logger.error("ProductBean.exportExcel:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdate() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (carrierCrud != null) {
				String name = carrierCrud.getCarrier_name();
				if (name != null && !"".equals(name)) {
					CarrierReqInfo t = new CarrierReqInfo(carrierCrud);
					if (carrierCrud.getId() == 0) {
						//check code đã tồn tại chưa
						if(allowSave(new Date())){
							carrierCrud.setCreated_date(new Date());
							carrierCrud.setCreated_by(account.getMember().getName());
							if(carrierService.insert(t)!=-1){
								current.executeScript("swaldesigntimer('Thành công!', 'Thêm thành công!','success',2000);");
								listCarrier.add(0,carrierCrud);
							}else{
								current.executeScript(
										"swaldesigntimer('Cảnh báo', 'Mã đã tồn tại!','warning',2000);");
							}
						}else{
							current.executeScript(
									"swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					}else{
						//check code update đã tồn tại chưa
						if(allowUpdate(new Date())){
							carrierCrud.setLast_modifed_by(account.getMember().getName());
							carrierCrud.setLast_modifed_date(new Date());
							if(carrierService.update(t)!=-1){
								current.executeScript(
										"swaldesigntimer('Thành công!', 'Cập nhật thành công!','success',2000);");
								listCarrier.set(listCarrier.indexOf(carrierCrud),t.getCarrier());
							}else{
								current.executeScript(
										"swaldesigntimer('Cảnh báo', 'Mã đã tồn tại!','warning',2000);");
							}
						}else{
							current.executeScript(
									"swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
						
					}
				} else {
					current.executeScript(
							"swaldesigntimer('Cảnh báo', 'Điền đủ thông tin chứa(*)!','warning',2000);");
				}
			}
		} catch (Exception e) {
			logger.error("CarrierBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}
	public void showEdit(){
		try{
			carrierCrud=carrierSelect.clone();
		}catch(Exception e){
			logger.error("CarrierBean.showEdit:"+e.getMessage(),e);
		}
	}
	public void createNew(){
		carrierCrud=new Carrier();
	}
	public void delete(){
		PrimeFaces current=PrimeFaces.current();
		try{
			if(carrierSelect !=null){
				if(allowDelete(new Date())){
					if(carrierService.deleteById(carrierSelect.getId())!=-1){
						current.executeScript(
								"swaldesigntimer('Thành công!', 'Xóa thành công!','success',2000);");
						listCarrier.remove(carrierSelect);
					}else{
						current.executeScript(
								"swaldesigntimer('Xảy ra lỗi!', 'Lỗi hệ thống!','error',2000);");
					}
				}else{
					current.executeScript(
							"swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
				}
			}else{
				current.executeScript(
						"swaldesigntimer('Cảnh báo!', 'Chưa chọn dòng để xóa!','warning',2000);");
			}
		}catch(Exception e){
			logger.error("CarrierBean.delete:"+e.getMessage(),e);
		}
	}

	public Carrier getCarrierCrud() {
		return carrierCrud;
	}

	public void setCarrierCrud(Carrier carrierCrud) {
		this.carrierCrud = carrierCrud;
	}

	public Carrier getCarrierSelect() {
		return carrierSelect;
	}

	public void setCarrierSelect(Carrier carrierSelect) {
		this.carrierSelect = carrierSelect;
	}

	public List<Carrier> getListCarrier() {
		return listCarrier;
	}

	public void setListCarrier(List<Carrier> listCarrier) {
		this.listCarrier = listCarrier;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public List<Integer> getListRowPerPage() {
		return listRowPerPage;
	}

	public void setListRowPerPage(List<Integer> listRowPerPage) {
		this.listRowPerPage = listRowPerPage;
	}
	
}
