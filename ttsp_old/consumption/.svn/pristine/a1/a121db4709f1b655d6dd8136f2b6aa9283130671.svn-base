package lixco.com.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import lixco.com.commom_ejb.MyUtilEJB;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.SessionHelper;
import lixco.com.entity.Customer;
import lixco.com.entity.CustomerTypes;
import lixco.com.entity.DeliveryPricing;
import lixco.com.entity.DongBo;
import lixco.com.entityapi.DataAPI;
import lixco.com.entityapi.DeliveryPricingAsyncDTO;
import lixco.com.entityapi.DeliveryPricingDTO;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.ICustomerTypesService;
import lixco.com.interfaces.IDeliveryPricingService;
import lixco.com.reqInfo.CustomerReqInfo;
import lixco.com.reqInfo.DeliveryPricingReqInfo;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Call;
import okhttp3.Response;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.api.SecurityConfig;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.convertfont.FontType;
import trong.lixco.com.convertfont.MyConvertFontFactory;
import trong.lixco.com.entity.AccountAPI;
import trong.lixco.com.service.AccountAPIService;
import trong.lixco.com.service.DongBoService;
import trong.lixco.com.util.MyStringUtil;
import trong.lixco.com.util.MyUtilExcel;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.StaticPath;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Named
@ViewScoped
public class DeliveryPricingBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IDeliveryPricingService deliveryPricingService;
	@Inject
	private ICustomerTypesService customerTypesService;
	@Inject
	private ICustomerService customerService;
	private DeliveryPricing deliveryPricingCrud;
	private DeliveryPricing deliveryPricingSelect;
	private List<DeliveryPricing> listDeliveryPricing;
	private List<CustomerTypes> listCustomerTypes;
	private Customer customerSearch;
	private String placeCodeSearch;
	private FormatHandler formatHandler;
	private Account account;
	
	private final String NAMESYNC = "dongiavc";
	@Getter
	DongBo dongBo;
	@Inject
	DongBoService dongBoService;
	
	
	
	
	@Override
	protected void initItem() {
		try {
			dongBo = dongBoService.findName(NAMESYNC);
			if (dongBo == null) {
				dongBo = new DongBo();
				dongBo.setName(NAMESYNC);
			}
			search();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);
			listCustomerTypes = new ArrayList<CustomerTypes>();
			customerTypesService.selectAll(listCustomerTypes);
			deliveryPricingCrud = new DeliveryPricing();
			formatHandler = FormatHandler.getInstance();
		} catch (Exception e) {
			logger.error("PlaceDeliveryBean.initItem:" + e.getMessage(), e);
		}
	}

	@Inject
	AccountAPIService accountAPIService;
	int solandangnhap;
	public void dongbo() {
		try {
			solandangnhap++;
			if (solandangnhap > 4) {
				noticeError("Token dăng nhâp lỗi, kiểm tra lại tài khoản liên kết.");
			} else {
				Gson gson = JsonParserUtil.getGson();
				String[] chinhanhs = { "BD" };
				DeliveryPricingAsyncDTO deliveryPricingAsyncDTO = dongbodata();
				StringBuffer message = new StringBuffer();
				StringBuffer notice = new StringBuffer();
				for (int i = 0; i < chinhanhs.length; i++) {
					String token = "";
					AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanhs[i]);
					if (accountAPI == null) {
						noticeError("Không có tài khoản đăng nhập API.");
						return;
					}
					String path = StaticPath.getPathBD();
					if ("BD".equals(chinhanhs[i])) {
						path = StaticPath.getPathBD();
					} else if ("BN".equals(chinhanhs[i])) {
						path = StaticPath.getPathBN();
					}
					
					String[] tokentime = new String[] { accountAPI.getToken(), accountAPI.getTimetoken() + "" };
					if (tokentime != null
							&& (tokentime[1] != null && new Date().getTime() < Long.parseLong(tokentime[1]) && tokentime[0] != null)) {
						token = tokentime[0];
					} else {
						dangnhapAPIdongbo(gson, path, chinhanhs[i]);
					}
					String datajson = gson.toJson(deliveryPricingAsyncDTO);
					DataAPI dataAPI = new DataAPI(datajson);

					// Goi ham dong bo BD
					Call call = trong.lixco.com.api.CallAPI.getInstance(token).getMethodPost(
							path + "/api/data/dongbo/dongiavc", gson.toJson(dataAPI));
					Response response = call.execute();
					String data = response.body().string();
					try {
						
						JsonObject jdata = gson.fromJson(data, JsonObject.class);
						String msg = jdata.get("msg").getAsString();
						message.append(msg);
						if (response.isSuccessful()) {
							notice.append(chinhanhs[i]);
							if (i == 0)
								notice.append(", ");
							notice("Đồng bộ " + chinhanhs[i] + " thành công.");
						} else {
							if (response.code() == 401) {
								dangnhapAPIdongbo(gson, path, chinhanhs[i]);
							} else {
								error(msg);
							}
						}
					} catch (Exception e) {
						error(data);
						executeScript("console.log('"+data+"');");
						e.printStackTrace();
					}
					
				}
				if (!"".equals(notice.toString())) {
					notice("Đồng bộ " + notice.toString() + " thành công.");
					List<Long> ids=new ArrayList<Long>();
					int size=deliveryPricingAsyncDTO.getDeliveryPricingDTOs().size();
					for (int i = 0; i < size; i++) {
						ids.add(deliveryPricingAsyncDTO.getDeliveryPricingDTOs().get(i).getId());
					}
					deliveryPricingService.updateCapNhat(ids);
				} else {
					noticeError("Kiểm tra lại dữ liệu chi nhánh.");
				}
				dongBo.setTime("Lần cuối: " + MyUtilEJB.chuyensangStrHH(new Date()));
				dongBo.setUserSync(getAccount().getUserName());
				dongBo.setMessages(message.toString());
				dongBoService.saveOrUpdate(dongBo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getLocalizedMessage());
		}

	}

	private void dangnhapAPIdongbo(Gson gson, String path, String chinhanh) throws IOException {
		AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanh);
		String[] tokentime = new String[2];
		Call call = trong.lixco.com.api.CallAPI.getInstance("").getMethodPost(path + "/api/account/login",
				gson.toJson(accountAPI));
		Response response = call.execute();
		if (response.isSuccessful()) {
			String data = response.body().string();
			JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
			tokentime[0] = jsonObject.get("access_token").getAsString();
			tokentime[1] = jsonObject.get("expires_in").getAsString();
			SecurityConfig.saveToken(accountAPIService, tokentime[0], Long.parseLong(tokentime[1]),chinhanh);
			dongbo();
		} else {
			noticeError("Tài khoản đăng nhập API " + chinhanh + " không đúng hoặc lỗi " + response.toString());
			return;
		}
	}

	private DeliveryPricingAsyncDTO dongbodata() {
		List<DeliveryPricing> deliveryPricings = deliveryPricingService.findNotSync();
		List<DeliveryPricingDTO> deliveryPricingDTOs = new ArrayList<DeliveryPricingDTO>();
		for (int i = 0; i < deliveryPricings.size(); i++) {
			DeliveryPricingDTO pdDTO = new DeliveryPricingDTO(deliveryPricings.get(i));
			deliveryPricingDTOs.add(pdDTO);
		}
		DeliveryPricingAsyncDTO deliveryPricingAsyncDTO = new DeliveryPricingAsyncDTO(deliveryPricingDTOs);
		return deliveryPricingAsyncDTO;
	}

	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	@Getter
	@Setter
	private String stextStr;

	public void search() {
		try {
			listDeliveryPricing = new ArrayList<>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("stextStr", MyStringUtil.replaceD(stextStr));
			JsonObject json = new JsonObject();
			json.add("delivery_pricing_info", jsonInfo);
			deliveryPricingService.search(JsonParserUtil.getGson().toJson(json), listDeliveryPricing);
		} catch (Exception e) {
			logger.error("PlaceDeliveryBean.search:" + e.getMessage(), e);
		}
	}

	

	public void saveOrUpdate() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (deliveryPricingCrud != null) {
				Customer cus = deliveryPricingCrud.getCustomer();
				// String placeArrived = deliveryPricingCrud.getPlace_arrived();
				String placeCode = deliveryPricingCrud.getPlace_code();
				if (cus != null && placeCode != null && !"".equals(placeCode)) {
					DeliveryPricingReqInfo t = new DeliveryPricingReqInfo(deliveryPricingCrud);
					if (deliveryPricingCrud.getId() == 0) {
						// check code đã tồn tại chưa
						if (allowSave(new Date())) {
							if (deliveryPricingService.checkCode(deliveryPricingCrud.getPlace_code(), 0) == 0) {
								deliveryPricingCrud.setCreated_date(new Date());
								deliveryPricingCrud.setCreated_by(account.getMember().getName());
								if (deliveryPricingService.insert(t) != -1) {
									success();
									listDeliveryPricing.add(0, deliveryPricingCrud.clone());
									createdNew();
								} else {
									current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Lỗi hệ thống!','error',2000);");
								}
							} else {
								error("Mã nơi vận chuyển đã tồn tại.");
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					} else {
						// check code update đã tồn tại chưa
						if (deliveryPricingService.checkCode(deliveryPricingCrud.getPlace_code(),
								deliveryPricingCrud.getId()) == 0) {
							if (allowUpdate(new Date())) {
								deliveryPricingCrud.setLast_modifed_by(account.getMember().getName());
								deliveryPricingCrud.setLast_modifed_date(new Date());
								if (deliveryPricingService.update(t) != -1) {
									success();
									listDeliveryPricing.set(listDeliveryPricing.indexOf(deliveryPricingCrud),
											t.getDelivery_pricing());
								} else {
									current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Lỗi hệ thống!','error',2000);");
								}
							} else {
								current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
							}
						} else {
							error("Mã nơi vận chuyển đã tồn tại.");
						}
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo', 'Thông tin không đầy đủ, điền đủ thông tin chứa(*)!','warning',2000);");
				}
			}
		} catch (Exception e) {
			logger.error("PlaceDeliveryBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}

	public void showDialog() {
		PrimeFaces current = PrimeFaces.current();
		try {
			deliveryPricingCrud = new DeliveryPricing();
			current.executeScript("PF('dlg1').show();");
		} catch (Exception e) {
			logger.error("PlaceDeliveryBean.showDialog:" + e.getMessage(), e);
		}
	}

	public void showEdit() {
		try {
			deliveryPricingCrud = deliveryPricingSelect.clone();
		} catch (Exception e) {
			logger.error("PlaceDeliveryBean.showDialogEdit:" + e.getMessage(), e);
		}
	}

	public void createdNew() {
		try {
			deliveryPricingCrud = new DeliveryPricing();
		} catch (Exception e) {
			logger.error("PlaceDeliveryBean.createdNew:" + e.getMessage(), e);
		}
	}

	public void delete() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (deliveryPricingCrud != null) {
				if (allowDelete(new Date())) {
					if (deliveryPricingService.deleteById(deliveryPricingCrud.getId()) != -1) {
						success();
						listDeliveryPricing.remove(deliveryPricingCrud);
					} else {
						current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Đơn giá đã được cài trong hóa đơn.','error',2000);");
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Chưa chọn dòng để xóa!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("PlaceDeliveryBean.delete:" + e.getMessage(), e);
		}
	}

	public void showDialogUpload() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('uploadpdffile').show();");
	}

	@Getter
	@Setter
	boolean fontvni = false;

	public void loadExcel(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<DeliveryPricing> listPlaceDeliveryTemp = new ArrayList<>();
				Workbook workBook = getWorkbook(new ByteArrayInputStream(byteFile), part.getFileName());
				Sheet firstSheet = workBook.getSheetAt(0);
				Iterator<Row> rows = firstSheet.iterator();
				while (rows.hasNext()) {
					rows.next();
					rows.remove();
					break;
				}
				lv1: while (rows.hasNext()) {
					Row row = rows.next();
					Iterator<Cell> cells = row.cellIterator();
					DeliveryPricing lix = new DeliveryPricing();
					while (cells.hasNext()) {
						Cell cell = cells.next();
						int columnIndex = cell.getColumnIndex();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						if (fontvni)
							cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);

						switch (columnIndex) {
						case 0:
							try {
								// mã khách hàng
								String makh = Objects.toString(cellvalue, null);
								if (makh != null && !"".equals(makh)) {
									CustomerReqInfo c = new CustomerReqInfo();
									customerService.selectByCode(makh, c);
									if (c.getCustomer() == null) {
										continue lv1;
									} else {
										lix.setCustomer(c.getCustomer());
									}
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// mã nơi vc
								String manvc = Objects.toString(cellvalue, null);
								if (manvc != null && !"".equals(manvc)) {
									lix.setPlace_code(manvc);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// địa chỉ
								String diachi = Objects.toString(cellvalue, null);
								lix.setAddress(diachi);
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// số km
								String km = Objects.toString(cellvalue, null);
								lix.setKm(Double.parseDouble(km));
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								// Đơn giá
								String unitPrice = Objects.toString(cellvalue, null);
								lix.setUnit_price(Double.parseDouble(unitPrice));
							} catch (Exception e) {
							}
							break;
						case 5:
							try {
								// Đơn giá k su dung
								String ksd = Objects.toString(cellvalue, null);
								lix.setDisable(Boolean.parseBoolean(ksd));
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// Nơi đến
								String placeArried = Objects.toString(cellvalue, null);
								if (placeArried != null && !"".equals(placeArried)) {
									lix.setPlace_arrived(placeArried);
								}
							} catch (Exception e) {
							}
							break;
						}
					}
					listPlaceDeliveryTemp.add(lix);
				}
				workBook = null;// free
				for (DeliveryPricing it : listPlaceDeliveryTemp) {
					DeliveryPricingReqInfo t = new DeliveryPricingReqInfo();
					deliveryPricingService.selectByPlaceCode(it.getPlace_code(), t);
					DeliveryPricing p = t.getDelivery_pricing();
					t.setDelivery_pricing(it);
					if (p != null) {
						it.setId(p.getId());
						it.setLast_modifed_by(account.getMember().getName());
						it.setLast_modifed_date(new Date());
						deliveryPricingService.update(t);
					} else {
						it.setCreated_by(account.getMember().getName());
						it.setCreated_date(new Date());
						deliveryPricingService.insert(t);
					}
				}
				search();
				notify.success();
			}
		} catch (Exception e) {
			logger.error("PlaceDeliveryBean.loadExcel:" + e.getMessage(), e);
		}
	}

	private Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
		Workbook workbook = null;
		if (excelFilePath.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else if (excelFilePath.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("The specified file is not Excel file");
		}

		return workbook;
	}


	public List<Customer> completeCustomerCrud(String text) {
		try {
			List<Customer> list = new ArrayList<>();
			customerService.complete(formatHandler.converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("PlaceDeliveryBean.completeCustomerSearch:" + e.getMessage(), e);
		}
		return null;
	}

	public DeliveryPricing getDeliveryPricingCrud() {
		return deliveryPricingCrud;
	}

	public void setDeliveryPricingCrud(DeliveryPricing deliveryPricingCrud) {
		this.deliveryPricingCrud = deliveryPricingCrud;
	}

	public DeliveryPricing getDeliveryPricingSelect() {
		return deliveryPricingSelect;
	}

	public void setDeliveryPricingSelect(DeliveryPricing deliveryPricingSelect) {
		this.deliveryPricingSelect = deliveryPricingSelect;
	}

	public List<DeliveryPricing> getListDeliveryPricing() {
		return listDeliveryPricing;
	}

	public void setListDeliveryPricing(List<DeliveryPricing> listDeliveryPricing) {
		this.listDeliveryPricing = listDeliveryPricing;
	}

	public List<CustomerTypes> getListCustomerTypes() {
		return listCustomerTypes;
	}

	public void setListCustomerTypes(List<CustomerTypes> listCustomerTypes) {
		this.listCustomerTypes = listCustomerTypes;
	}

	public Customer getCustomerSearch() {
		return customerSearch;
	}

	public void setCustomerSearch(Customer customerSearch) {
		this.customerSearch = customerSearch;
	}

	public String getPlaceCodeSearch() {
		return placeCodeSearch;
	}

	public void setPlaceCodeSearch(String placeCodeSearch) {
		this.placeCodeSearch = placeCodeSearch;
	}

	public FormatHandler getFormatHandler() {
		return formatHandler;
	}

	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
	}
}
