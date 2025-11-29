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
import lixco.com.common.ApiCallClient;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.SessionHelper;
import lixco.com.entity.City;
import lixco.com.entity.Customer;
import lixco.com.entity.CustomerChannel;
import lixco.com.entity.CustomerGroup;
import lixco.com.entity.CustomerTypes;
import lixco.com.entity.DongBo;
import lixco.com.entityapi.CityDTO;
import lixco.com.entityapi.CustomerAsyncDTO;
import lixco.com.entityapi.CustomerChannelDTO;
import lixco.com.entityapi.CustomerDTO;
import lixco.com.entityapi.CustomerGroupDTO;
import lixco.com.entityapi.CustomerTypesDTO;
import lixco.com.entityapi.DataAPI;
import lixco.com.interfaces.ICityService;
import lixco.com.interfaces.ICustomerChannelService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.ICustomerTypesService;
import lixco.com.interfaces.IProductBrandService;
import lixco.com.reqInfo.CityReqInfo;
import lixco.com.reqInfo.CustomerReqInfo;
import lixco.com.reqInfo.CustomerTypesReqInfo;
import lixco.com.service.CustomerGroupService;
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
import trong.lixco.com.service.AccountDatabaseService;
import trong.lixco.com.service.DongBoService;
import trong.lixco.com.util.MyStringUtil;
import trong.lixco.com.util.MyUtilExcel;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.StaticPath;
import trong.lixco.com.util.ToolReport;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Named
@ViewScoped
public class CustomerBean extends AbstractBean {
	private static final long serialVersionUID = 6248587419234517987L;
	@Inject
	private Logger logger;
	@Inject
	private ICustomerService customerService;
	@Inject
	private ICustomerTypesService customerTypesService;
	@Inject
	private ICityService cityService;
	@Inject
	private AccountDatabaseService accountDatabaseService;
	private Customer customerSelect;
	private Customer customerCrud;
	private List<Customer> listCustomer;
	private List<CustomerTypes> listCustomerTypes;
	private String customerCode;
	private String customerName;
	private String taxCode;
	private String cellPhone;
	private String homePhone;
	private CustomerTypes customerTypes;
	private Account account;
	@Getter
	DongBo dongBo;
	private final String NAMESYNC = "khachhang";

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
			customerCrud = new Customer();

		} catch (Exception e) {
			logger.error("CustomerBean.initItem:" + e.getMessage(), e);
		}
	}

	@Inject
	AccountAPIService accountAPIService;
	@Inject
	IProductBrandService productBrandService;
	@Inject
	DongBoService dongBoService;
	int solandangnhap;

	public void dongbo() {
		try {
			solandangnhap++;
			if (solandangnhap > 4) {
				noticeError("Token dăng nhâp lỗi, kiểm tra lại tài khoản liên kết.");
			} else {
				Gson gson = JsonParserUtil.getGson();
				String[] chinhanhs = { "BD" };
				CustomerAsyncDTO customerAsyncDTO = dongbodata();
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
					String datajson = gson.toJson(customerAsyncDTO);
					DataAPI dataAPI = new DataAPI(datajson);

					// Goi ham dong bo
					Call call = trong.lixco.com.api.CallAPI.getInstance(token).getMethodPost(
							path + "/api/data/dongbo/khachhang", gson.toJson(dataAPI));
					// Call call =
					// trong.lixco.com.api.CallAPI.getInstance(token).getMethodPost(
					// "http://192.168.0.226:63/consumption" +
					// "/api/data/dongbo/khachhang", gson.toJson(dataAPI));
					Response response = call.execute();

					String data = response.body().string();
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
				}
				if (!"".equals(notice.toString())) {
					notice("Đồng bộ " + notice.toString() + " thành công.");
					List<Long> ids = new ArrayList<Long>();
					for (int i = 0; i < customerAsyncDTO.getCustomerDTOs().size(); i++) {
						ids.add(customerAsyncDTO.getCustomerDTOs().get(i).getId());
					}
					customerService.updateCapNhat(ids);
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
			SecurityConfig.saveToken(accountAPIService, tokentime[0], Long.parseLong(tokentime[1]), chinhanh);
			dongbo();
		} else {
			noticeError("Tài khoản đăng nhập API " + chinhanh + " không đúng hoặc lỗi " + response.toString());
			return;
		}
	}

	@Inject
	ICustomerChannelService customerChannelService;
	@Inject
	CustomerGroupService customerGroupService;

	private CustomerAsyncDTO dongbodata() {
		// thanh pho KH
		List<City> cities = new ArrayList<City>();
		cityService.selectAll(cities);
		List<CityDTO> cityDTOs = new ArrayList<CityDTO>();
		for (int i = 0; i < cities.size(); i++) {
			CityDTO pdbDTO = new CityDTO(cities.get(i));
			cityDTOs.add(pdbDTO);
		}
		// kenh KH
		List<CustomerChannel> customerChannels = new ArrayList<CustomerChannel>();
		customerChannelService.selectAll(customerChannels);
		List<CustomerChannelDTO> customerChannelDTOs = new ArrayList<CustomerChannelDTO>();
		for (int i = 0; i < customerChannels.size(); i++) {
			CustomerChannelDTO pdbDTO = new CustomerChannelDTO(customerChannels.get(i));
			customerChannelDTOs.add(pdbDTO);
		}
		// nhom KH
		List<CustomerGroup> customerGroups = customerGroupService.selectAll();
		List<CustomerGroupDTO> customerGroupDTOs = new ArrayList<CustomerGroupDTO>();
		for (int i = 0; i < customerGroups.size(); i++) {
			CustomerGroupDTO pdbDTO = new CustomerGroupDTO(customerGroups.get(i));
			customerGroupDTOs.add(pdbDTO);
		}
		// Loai KH
		List<CustomerTypes> customerTypes = new ArrayList<CustomerTypes>();
		customerTypesService.selectAll(customerTypes);
		List<CustomerTypesDTO> customerTypesDTOs = new ArrayList<CustomerTypesDTO>();
		for (int i = 0; i < customerTypes.size(); i++) {
			CustomerTypesDTO pdbDTO = new CustomerTypesDTO(customerTypes.get(i));
			customerTypesDTOs.add(pdbDTO);
		}
		// Khach hang
		List<Customer> customers = customerService.findNotSync();
		List<CustomerDTO> customerDTOs = new ArrayList<CustomerDTO>();
		for (int i = 0; i < customers.size(); i++) {
			CustomerDTO pdDTO = new CustomerDTO(customers.get(i));
			customerDTOs.add(pdDTO);
		}
		CustomerAsyncDTO customerAsyncDTO = new CustomerAsyncDTO(cityDTOs, customerChannelDTOs, customerGroupDTOs,
				customerTypesDTOs, customerDTOs);
		return customerAsyncDTO;
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

//	private void callApiFoxCustomer(long customerId) {
//		PrimeFaces current = PrimeFaces.current();
//		try {
//			lixco.com.reqfox.Customer customer = customerService.getCustomerFoxPro(customerId);
//			AccountDatabase accountDatabase = accountDatabaseService.findByName("foxproapi");
//			if (customer != null) {
//				try {
//					Call call = ApiCallClient.getListObjectWithParam(accountDatabase.getAddressPublic(), "customer",
//							"sa", JsonParserUtil.getGson().toJson(customer));
//					Response response = call.execute();
//					String body = response.body().string();
//					JsonObject result = JsonParserUtil.getGson().fromJson(body, JsonObject.class);
//					if (response.isSuccessful() && result.get("err").getAsInt() == 0) {
//						lixco.com.reqfox.Customer customerResult = JsonParserUtil.getGson().fromJson(result.get("dt"),
//								lixco.com.reqfox.Customer.class);
//
//					} else {
//						String mesages = result.get("msg").getAsString();
//						current.executeScript("swaldesignclose2('Cảnh báo!', '" + mesages + "','warning');");
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					current.executeScript("swaldesignclose2('Cảnh báo!', 'Không thực hiện liên kết dữ liệu foxpro','warning');");
//				}
//			}
//		} catch (Exception e) {
//			logger.error("CustomerBean.callApiFoxCustomer:foxpro");
//		}
//	}
	@Getter
	@Setter
	private String stextStr;
	@Getter
	@Setter
	private City city;

	public void search() {
		try {
			listCustomer = new ArrayList<Customer>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("stextStr", MyStringUtil.replaceD(stextStr));
			jsonInfo.addProperty("customer_types_id", customerTypes != null ? customerTypes.getId() : 0);
			jsonInfo.addProperty("city_id", city != null ? city.getId() : 0);
			JsonObject json = new JsonObject();
			json.add("customer_info", jsonInfo);
			customerService.search(JsonParserUtil.getGson().toJson(json), listCustomer);
		} catch (Exception e) {
			logger.error("CustomerBean.search:" + e.getMessage(), e);
		}
	}

	public void exportExcel() {
		try {
			List<Customer> list = new ArrayList<>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("customer_code", customerCode);
			jsonInfo.addProperty("customer_name", customerName);
			jsonInfo.addProperty("tax_code", taxCode);
			jsonInfo.addProperty("cell_phone", cellPhone);
			jsonInfo.addProperty("home_phone", homePhone);
			jsonInfo.addProperty("customer_types_id", customerTypes != null ? customerTypes.getId() : 0);
			JsonObject json = new JsonObject();
			json.add("customer_info", jsonInfo);
			customerService.selectBy(JsonParserUtil.getGson().toJson(jsonInfo), list);
			if (list.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "MaKH", "tên khách hàng", "đơn vị", "địa chỉ", "IdLoaiKH", "tên loại khách hàng",
						"điện thoại di động", "điện thoại bàn", "Email", "fax", "city_id", "tên thành phố",
						"mã số thuế", "địa chỉ giao hàng", "nghỉ bán","Định danh cá nhân" };
				results.add(title);
				for (Customer p : list) {
					Object row[] = { p.getCustomer_code(), p.getCustomer_name(),
							Objects.toString(p.getCompany_name(), ""), Objects.toString(p.getAddress(), ""),
							p.getCustomer_types() == null ? "" : p.getCustomer_types().getId(),
							p.getCustomer_types() == null ? "" : p.getCustomer_types().getName(),
							Objects.toString(p.getCell_phone(), ""), Objects.toString(p.getHome_phone(), ""),
							Objects.toString(p.getEmail(), ""), Objects.toString(p.getFax(), ""),
							p.getCity() == null ? 0 : p.getCity().getId(),
							p.getCity() == null ? "" : p.getCity().getCity_name(),
							Objects.toString(p.getTax_code(), ""), Objects.toString(p.getLocation_delivery(), ""),
							p.isDisable(), p.getCitizenIDNumber() };
					results.add(row);
				}
				String titleEx = "dmkh";
				ToolReport.printReportExcelRaw(results, titleEx);
			}
		} catch (Exception e) {
			logger.error("ProductBean.exportExcel:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdate() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (customerCrud != null) {
				String customerCode = customerCrud.getCustomer_code();
				String customerName = customerCrud.getCustomer_name();
				CustomerTypes type = customerCrud.getCustomer_types();
				if (customerName != null && customerName != "" && type != null) {
					CustomerReqInfo t = new CustomerReqInfo(customerCrud);
					if (customerCrud.getId() == 0) {
						// check code đã tồn tại chưa
						if (allowSave(new Date())) {
							customerCrud.setCreated_date(new Date());
							customerCrud.setCreated_by(account.getMember().getName());
							if (customerCrud.getEmail() != null)
								customerCrud.setEmail(customerCrud.getEmail().trim());
							if (customerCrud.getTax_code() != null)
								customerCrud.setTax_code(customerCrud.getTax_code().trim());
							if (customerService.insert(t) != -1) {
								customerCrud = t.getCustomer();
								success();
								listCustomer.add(0, t.getCustomer());
							} else {
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Kiểm tra dữ liệu file log.','error',2000);");
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					} else {
						// check code update đã tồn tại chưa
						if (customerService.checkCustomerCode(customerCode, customerCrud.getId()) == 0) {
							if (allowUpdate(new Date())) {
								customerCrud.setLast_modifed_by(account.getMember().getName());
								customerCrud.setLast_modifed_date(new Date());
								if (customerCrud.getEmail() != null)
									customerCrud.setEmail(customerCrud.getEmail().trim());
								if (customerCrud.getTax_code() != null)
									customerCrud.setTax_code(customerCrud.getTax_code().trim());
								if (customerService.update(t) != -1) {
									success();
									listCustomer.set(listCustomer.indexOf(customerCrud), t.getCustomer());
									customerCrud = t.getCustomer().clone();
								} else {
									current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Lỗi hệ thống!','error',2000);");
								}
							} else {
								current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo', 'Mã khách hàng đã tồn tại!','warning',2000);");
						}
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo', 'Thông tin khách hàng không đầy đủ, điền đủ thông tin chứa(*)!','warning',2000);");
				}
			}
		} catch (Exception e) {
			logger.error("CustomerBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}

	public void showDialog() {
		PrimeFaces current = PrimeFaces.current();
		try {
			customerCrud = new Customer();
			current.executeScript("PF('dlg1').show();");
		} catch (Exception e) {
			logger.error("CustomerBean.showDialog:" + e.getMessage(), e);
		}
	}

	public void showDialogEdit() {
		PrimeFaces current = PrimeFaces.current();
		try {
			customerCrud = customerSelect;
			current.executeScript("PF('dlg1').show();");
		} catch (Exception e) {
			logger.error("CustomerBean.showDialogEdit:" + e.getMessage(), e);
		}
	}

	public void createdNew() {
		try {
			customerCrud = new Customer();
		} catch (Exception e) {
			logger.error("CustomerBean.createdNew:" + e.getMessage(), e);
		}
	}

	public void delete() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (customerSelect != null) {
				if (allowDelete(new Date())) {
					if (customerService.deleteById(customerSelect.getId()) != -1) {
						listCustomer.remove(customerSelect);
						success();
					} else {
						noticeError("KH đã tồn tại trong các phiếu, không xóa được");
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Chưa chọn KH để xóa!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("CustomerBean.delete:" + e.getMessage(), e);
		}
	}

	public List<CustomerTypes> autoComplete(String text) {
		try {
			List<CustomerTypes> list = new ArrayList<CustomerTypes>();
			customerTypesService.findLike(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("CustomerBean.autoComplete:" + e.getMessage(), e);
		}
		return null;
	}

	public List<City> completeCity(String text) {
		try {
			List<City> list = new ArrayList<City>();
			cityService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("CustomerBean.completeCity:" + e.getMessage(), e);
		}
		return null;
	}

	public void loadExcelDiaChi(FileUploadEvent event) {
		if (allowUpdate(new Date()) && allowSave(new Date())) {
			try {
				if (event.getFile() != null) {
					UploadedFile part = event.getFile();
					byte[] byteFile = event.getFile().getContent();
					List<Customer> listCustomerTemp = new ArrayList<Customer>();
					Workbook workBook = getWorkbook(new ByteArrayInputStream(byteFile), part.getFileName());
					Sheet firstSheet = workBook.getSheetAt(0);
					Iterator<Row> rows = firstSheet.iterator();
					while (rows.hasNext()) {
						rows.next();
						rows.remove();
						break;
					}
					while (rows.hasNext()) {
						Row row = rows.next();
						Iterator<Cell> cells = row.cellIterator();
						Customer lix = new Customer();
						lv2: while (cells.hasNext()) {
							Cell cell = cells.next();
							int columnIndex = cell.getColumnIndex();
							String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
							switch (columnIndex) {
							case 0:
								try {
									// mã khách hàng
									if (cellvalue != null && !"".equals(cellvalue)) {
										lix.setCustomer_code(cellvalue);
									} else {
										break lv2;
									}
								} catch (Exception e) {
								}
								break;

							case 1:
								try {
									// địa chỉ khách hàng
									lix.setAddress(cellvalue);
								} catch (Exception e) {
								}
								break;
							}
						}
						listCustomerTemp.add(lix);
					}
					workBook = null;// free
					int slcapnhatduoc=0;
					for (Customer it : listCustomerTemp) {
						CustomerReqInfo t = new CustomerReqInfo();
						customerService.selectByCode(it.getCustomer_code(), t);
						Customer cData = t.getCustomer();
						if (cData != null) {
							String dchicu=new String(cData.getAddress());
							cData.setLast_modifed_by(account.getMember().getName());
							cData.setLast_modifed_date(new Date());
							cData.setAddress(it.getAddress());
							if(cData.getAddressOld()==null){
								cData.setAddressOld(dchicu);
							}
							cData.setCapnhat(true);
							t.setCustomer(cData);
							customerService.update(t);
							slcapnhatduoc++;
						}
					}
					search();
					success("Cập nhật được "+slcapnhatduoc+" khách hàng");
				}
			} catch (Exception e) {
				logger.error("ProductTypeBean.loadExcel:" + e.getMessage(), e);
			}
		} else {
			warning("Tài khoản này không được phép thực hiện.");
		}
	}
	public void loadExcel(FileUploadEvent event) {
		if (allowUpdate(new Date()) && allowSave(new Date())) {
			Notify notify = new Notify(FacesContext.getCurrentInstance());
			try {
				if (event.getFile() != null) {
					UploadedFile part = event.getFile();
					byte[] byteFile = event.getFile().getContent();
					List<Customer> listCustomerTemp = new ArrayList<Customer>();
					Workbook workBook = getWorkbook(new ByteArrayInputStream(byteFile), part.getFileName());
					Sheet firstSheet = workBook.getSheetAt(0);
					Iterator<Row> rows = firstSheet.iterator();
					while (rows.hasNext()) {
						rows.next();
						rows.remove();
						break;
					}
					while (rows.hasNext()) {
						Row row = rows.next();
						Iterator<Cell> cells = row.cellIterator();
						Customer lix = new Customer();
						lv2: while (cells.hasNext()) {
							Cell cell = cells.next();
							int columnIndex = cell.getColumnIndex();
							String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
							switch (columnIndex) {
							case 0:
								try {
									// mã khách hàng
									if (cellvalue != null && !"".equals(cellvalue)) {
										lix.setCustomer_code(cellvalue);
									} else {
										break lv2;
									}
								} catch (Exception e) {
								}
								break;

							case 1:
								try {
									// tên khách hàng
									lix.setCustomer_name(cellvalue);
								} catch (Exception e) {
								}
								break;
							case 2:
								try {
									// đơn vị
									lix.setCompany_name(cellvalue);
								} catch (Exception e) {
								}
								break;
							case 3:
								try {
									// địa chỉ khách hàng
									lix.setAddress(cellvalue);
								} catch (Exception e) {
								}
								break;
							case 4:
								try {
									// mã loại khách hàng
									if (cellvalue != null && !"".equals(cellvalue)) {
										CustomerTypes ctf = customerTypesService.findByName(cellvalue);
										lix.setCustomer_types(ctf);
									}
								} catch (Exception e) {
								}
								break;
							case 5:
								try {
									// email
									lix.setEmail(cellvalue);
								} catch (Exception e) {
								}
								break;
							case 6:
								try {
									// ma so thue
									lix.setTax_code(cellvalue);
								} catch (Exception e) {
								}
								break;
							case 7:
								try {
									// nghi ban
									if ("true".equalsIgnoreCase(cellvalue)) {
										lix.setDisable(true);
									}

								} catch (Exception e) {
								}
								break;
							case 8:
								try {
									// khong in ten kh len hoa don
									if ("true".equalsIgnoreCase(cellvalue)) {
										lix.setNot_print_customer_name(true);
									}
								} catch (Exception e) {
								}
								break;
							}
						}
						listCustomerTemp.add(lix);
					}
					workBook = null;// free
					for (Customer it : listCustomerTemp) {
						CustomerReqInfo t = new CustomerReqInfo();
						customerService.selectByCode(it.getCustomer_code(), t);
						Customer p = t.getCustomer();
						t.setCustomer(it);
						if (p != null) {
							it.setId(p.getId());
							it.setLast_modifed_by(account.getMember().getName());
							it.setLast_modifed_date(new Date());
							customerService.update(t);
						} else {
							it.setCreated_by(account.getMember().getName());
							it.setCreated_date(new Date());
							customerService.insertAvaiCode(t);
						}
					}
					search();
					notify.success();
				}
			} catch (Exception e) {
				logger.error("ProductTypeBean.loadExcel:" + e.getMessage(), e);
			}
		} else {
			warning("Tài khoản này không được phép thực hiện.");
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

	public Customer getCustomerSelect() {
		return customerSelect;
	}

	public void setCustomerSelect(Customer customerSelect) {
		this.customerSelect = customerSelect;
	}

	public Customer getCustomerCrud() {
		return customerCrud;
	}

	public void setCustomerCrud(Customer customerCrud) {
		this.customerCrud = customerCrud;
	}

	public List<Customer> getListCustomer() {
		return listCustomer;
	}

	public void setListCustomer(List<Customer> listCustomer) {
		this.listCustomer = listCustomer;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public CustomerTypes getCustomerTypes() {
		return customerTypes;
	}

	public void setCustomerTypes(CustomerTypes customerTypes) {
		this.customerTypes = customerTypes;
	}

	public List<CustomerTypes> getListCustomerTypes() {
		return listCustomerTypes;
	}

	public void setListCustomerTypes(List<CustomerTypes> listCustomerTypes) {
		this.listCustomerTypes = listCustomerTypes;
	}
}
