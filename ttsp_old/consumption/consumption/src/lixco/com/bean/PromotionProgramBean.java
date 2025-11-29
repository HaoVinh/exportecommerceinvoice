package lixco.com.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.file.UploadedFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import lixco.com.commom_ejb.MyUtilEJB;
import lixco.com.commom_ejb.PagingInfo;
import lixco.com.common.ApiCallClient;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.NavigationInfo;
import lixco.com.common.SessionHelper;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Customer;
import lixco.com.entity.CustomerPricingProgram;
import lixco.com.entity.CustomerPromotionProgram;
import lixco.com.entity.CustomerTypes;
import lixco.com.entity.DongBo;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.PricingProgramDetail;
import lixco.com.entity.Product;
import lixco.com.entity.PromotionProgram;
import lixco.com.entity.PromotionProgramDetail;
import lixco.com.entityapi.CustomerPricingProgramDTO;
import lixco.com.entityapi.CustomerPromotionProgramDTO;
import lixco.com.entityapi.DataAPI;
import lixco.com.entityapi.PricingProgramAsyncDTO;
import lixco.com.entityapi.PricingProgramDTO;
import lixco.com.entityapi.PricingProgramDetailDTO;
import lixco.com.entityapi.PromotionProgramAsyncDTO;
import lixco.com.entityapi.PromotionProgramDTO;
import lixco.com.entityapi.PromotionProgramDetailDTO;
import lixco.com.interfaces.ICustomerChannelService;
import lixco.com.interfaces.ICustomerPromotionProgramService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.ICustomerTypesService;
import lixco.com.interfaces.IProductBrandService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IPromotionProgramDetailService;
import lixco.com.interfaces.IPromotionProgramService;
import lixco.com.reqInfo.CustomerPromotionProgramReqInfo;
import lixco.com.reqInfo.CustomerReqInfo;
import lixco.com.reqInfo.PricingProgramDetailReqInfo;
import lixco.com.reqInfo.ProductReqInfo;
import lixco.com.reqInfo.PromotionProgramDetailReqInfo;
import lixco.com.reqInfo.PromotionProgramReqInfo;
import lixco.com.reqfox.WrapDataPromotionProgram;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Call;
import okhttp3.Response;
import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.api.SecurityConfig;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.AccountAPI;
import trong.lixco.com.entity.AccountDatabase;
import trong.lixco.com.service.AccountAPIService;
import trong.lixco.com.service.AccountDatabaseService;
import trong.lixco.com.service.DongBoService;
import trong.lixco.com.util.MyUtilExcel;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.StaticPath;
import trong.lixco.com.util.ToolReport;

@Named
@ViewScoped
public class PromotionProgramBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IPromotionProgramService promotionProgramService;
	@Inject
	private IPromotionProgramDetailService promotionProgramDetailService;
	@Inject
	private IProductService productService;
	@Inject
	private ICustomerPromotionProgramService customerPromotionProgramService;
	@Inject
	private ICustomerTypesService customerTypesService;
	@Inject
	private ICustomerService customerService;
	@Inject
	private AccountDatabaseService accountDatabaseService;
	private PromotionProgram promotionProgramCrud;
	private PromotionProgram promotionProgramSelect;
	private List<PromotionProgram> listPromotionProgram;
	private PromotionProgramDetail promotionProgramDetailCrud;
	private PromotionProgramDetail promotionProgramDetailSelect;
	@Getter
	@Setter
	private List<PromotionProgramDetail> listPromotionProgramDetailFilter;
	private List<PromotionProgramDetail> listPromotionProgramDetail;
	private List<CustomerTypes> listCustomerTypes;
	private Date fromDate;
	private Date toDate;
	private String programCode;
	private Product product;
	private Product promotionProduct;
	// search 2
	private Product product2;
	private Product promotionProduct2;
	private Account account;
	private FormatHandler formatHandler;

	/* search 3 */
	@Getter
	@Setter
	private String promotionProgramCodeSearch3;
	@Getter
	@Setter
	private CustomerTypes customerTypesSearch3;
	@Getter
	@Setter
	private Customer customerSearch3;
	@Getter
	@Setter
	private Product product3;
	@Getter
	@Setter
	private Product promotionProduct3;
	@Getter
	@Setter
	private Date fromDateSearch3;
	@Getter
	@Setter
	private Date toDateSearch3;

	/* Danh sách khách hàng đã cài đặt chương trình khuyến mãi */
	private List<CustomerPromotionProgram> listCustomerPromotionProgramSet;
	private CustomerPromotionProgram customerPromotionProgramSetCrud;
	@Getter
	@Setter
	private List<CustomerPromotionProgram> customerPromotionProgramSetFilter;
	private CustomerPromotionProgram customerPromotionProgramSetSelect;
	private Customer customerSearchSet;
	private CustomerTypes customerTypesSearchSet;
	private CustomerTypes customerTypesSet;
	/* Cài đặt nhiều khách hàng */
	private List<CustomerPromotionProgram> listCustomerPromotionProgram;
	private CustomerPromotionProgram customerPromotionProgramSelect;
	private CustomerPromotionProgram customerPromotionProgramCrud;
	private CustomerTypes customerTypesSearch;
	private Customer customerSearch;
	private String promotionProgramCodeSearch;
	private Date fromDateSearch;
	private Date toDateSearch;
	private CustomerTypes customerTypesSetting;
	private DualListModel<Customer> modelCustomerSetting;
	private PromotionProgram promotionProgramSetting;
	private String textSearch;
	/* upload data */
	private boolean rewrite;
	/* foxpro */
	private Date fromDateFox;
	private Date toDateFox;

	@Getter
	DongBo dongBo;
	private final String NAMESYNC = "ctkhuyenmai";

	@Override
	protected void initItem() {
		try {
			dongBo = dongBoService.findName(NAMESYNC);
			if (dongBo == null) {
				dongBo = new DongBo();
				dongBo.setName(NAMESYNC);
			}
			listCustomerTypes = new ArrayList<>();
			customerTypesService.selectAll(listCustomerTypes);
			// paging customer set
			listCustomerPromotionProgramSet = new ArrayList<>();

			formatHandler = FormatHandler.getInstance();
			int month = ToolTimeCustomer.getMonthCurrent();
			int year = ToolTimeCustomer.getYearCurrent();
			fromDate = ToolTimeCustomer.getDateMinCustomer(month, year);
			search();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);
			createdNew();
			/* innit setting */
			listCustomerTypes = new ArrayList<CustomerTypes>();
			customerTypesService.selectAll(listCustomerTypes);
			modelCustomerSetting = new DualListModel<>(new ArrayList<Customer>(), new ArrayList<Customer>());
			fromDateSearch = ToolTimeCustomer.getDateMinCustomer(month, year);
			fromDateSearch3 = ToolTimeCustomer.getDateMinCustomer(month, year);
			searchCustomerPromotionProgram();
		} catch (Exception e) {
			logger.error("PromotionProgramBean.initItem:" + e.getMessage(), e);
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
				PromotionProgramAsyncDTO programAsyncDTO = dongbodata();
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
					String datajson = gson.toJson(programAsyncDTO);
					DataAPI dataAPI = new DataAPI(datajson);

					// Goi ham dong bo
					Call call = trong.lixco.com.api.CallAPI.getInstance(token).getMethodPost(
							path + "/api/data/dongbo/ctkhuyenmai", gson.toJson(dataAPI));
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
					for (int i = 0; i < programAsyncDTO.getPromotionProgramDTOs().size(); i++) {
						ids.add(programAsyncDTO.getPromotionProgramDTOs().get(i).getId());
					}
					promotionProgramService.updateCapNhat(ids);
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

	private PromotionProgramAsyncDTO dongbodata() {
		List<PromotionProgram> promotionPrograms = promotionProgramService.findNotSync();
		List<PromotionProgramDTO> promotionProgramDTOs = new ArrayList<PromotionProgramDTO>();

		for (int i = 0; i < promotionPrograms.size(); i++) {
			PromotionProgramDTO pdbDTO = new PromotionProgramDTO(promotionPrograms.get(i));
			List<PromotionProgramDetail> promotionProgramDetails = promotionProgramDetailService
					.findAllByPromotionProgram(promotionPrograms.get(i).getId());
			List<PromotionProgramDetailDTO> promotionProgramDetailDTOs = new ArrayList<PromotionProgramDetailDTO>();
			for (int j = 0; j < promotionProgramDetails.size(); j++) {
				PromotionProgramDetailDTO pdbDetailDTO = new PromotionProgramDetailDTO(promotionProgramDetails.get(j));
				promotionProgramDetailDTOs.add(pdbDetailDTO);
			}
			pdbDTO.setProgramDetailDTOs(promotionProgramDetailDTOs);
			List<CustomerPromotionProgram> customerPromotionPrograms = customerPromotionProgramService
					.findAllByPromotionProgram(promotionPrograms.get(i).getId());

			List<CustomerPromotionProgramDTO> customerPromotionProgramDTOs = new ArrayList<CustomerPromotionProgramDTO>();
			for (int j = 0; j < customerPromotionPrograms.size(); j++) {
				CustomerPromotionProgramDTO pdbCusProgramDTO = new CustomerPromotionProgramDTO(
						customerPromotionPrograms.get(j));
				customerPromotionProgramDTOs.add(pdbCusProgramDTO);
			}
			pdbDTO.setCustomerPromotionProgramDTOs(customerPromotionProgramDTOs);
			promotionProgramDTOs.add(pdbDTO);

		}
		PromotionProgramAsyncDTO programAsyncDTO = new PromotionProgramAsyncDTO(promotionProgramDTOs);
		return programAsyncDTO;
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public void showDialogFox() {
		PrimeFaces current = PrimeFaces.current();
		try {
			int month = ToolTimeCustomer.getMonthCurrent();
			int year = ToolTimeCustomer.getYearCurrent();
			fromDateFox = ToolTimeCustomer.getDateMinCustomer(month, year);
			toDateFox = ToolTimeCustomer.getDateMaxCustomer(month, year);
			current.executeScript("PF('dlgfox').show();");
		} catch (Exception e) {
			logger.error("PromotionProgramBean.showDialogFox:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdateApiFox() {
		PrimeFaces current = PrimeFaces.current();
		try {
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateFox, "dd/MM/yyyy HH:mm:ss"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateFox, "dd/MM/yyyy HH:mm:ss"));
			AccountDatabase accountDatabase = accountDatabaseService.findByName("foxproapi");
			try {
				Call call = ApiCallClient.getListObjectWithParam(accountDatabase.getAddressPublic(), "program",
						"promotion", JsonParserUtil.getGson().toJson(json));
				Response response = call.execute();
				String body = response.body().string();
				JsonObject result = JsonParserUtil.getGson().fromJson(body, JsonObject.class);
				if (response.isSuccessful() && result.get("err").getAsInt() == 0) {
					List<WrapDataPromotionProgram> listResult = JsonParserUtil.getGson().fromJson(
							result.get("dt").getAsJsonObject().get("list_promotion_program"),
							new TypeToken<List<WrapDataPromotionProgram>>() {
							}.getType());
					if (promotionProgramService.saveOrUpdate(listResult, account.getMember().getName()) == 0) {
						current.executeScript("swaldesignclose2('Thành công!', 'Nạp dữ liệu từ Foxpro thành công!','success',2000);");
					} else {
						current.executeScript("swaldesignclose2('Cảnh báo!', 'Nạp dữ liệu từ foxpro thất bại','warning');");
					}
				} else {
					String mesages = result.get("msg").getAsString();
					current.executeScript("swaldesignclose2('Cảnh báo!', '" + mesages + "','warning');");
				}
			} catch (Exception e) {
				current.executeScript("swaldesignclose2('Cảnh báo!', 'Không thực hiện liên kết dữ liệu foxpro','warning');");
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.saveOrUpdateApiFox:" + e.getMessage(), e);
		}
	}

	public void nextOrPrev(int next) {
		try {
			if (listPromotionProgram != null && listPromotionProgram.size() > 0) {
				int index = listPromotionProgram.indexOf(promotionProgramCrud);
				int size = listPromotionProgram.size() - 1;
				if (index == -1) {
					promotionProgramSelect = listPromotionProgram.get(0);
					promotionProgramCrud = promotionProgramSelect.clone();
				} else {
					switch (next) {
					case 1:
						if (index == size) {
							promotionProgramSelect = listPromotionProgram.get(0);
							promotionProgramCrud = promotionProgramSelect.clone();
						} else {
							promotionProgramSelect = listPromotionProgram.get(index + 1);
							promotionProgramCrud = promotionProgramSelect.clone();
						}
						break;

					default:
						if (index == 0) {
							promotionProgramSelect = listPromotionProgram.get(size);
							promotionProgramCrud = promotionProgramSelect.clone();
						} else {
							promotionProgramSelect = listPromotionProgram.get(index - 1);
							promotionProgramCrud = promotionProgramSelect.clone();
						}
						break;
					}
				}
				promotionProgramCrud = promotionProgramSelect.clone();
				promotionProgramDetailCrud = new PromotionProgramDetail();
				// init detail
				listPromotionProgramDetail = new ArrayList<PromotionProgramDetail>();
				product2 = null;
				search2();
				// init danh sách khách hàng đã được cài đặt.
				listCustomerPromotionProgramSet = new ArrayList<>();
				customerPromotionProgramService.selectAll(promotionProgramSelect.getId(),
						listCustomerPromotionProgramSet);
			}
		} catch (Exception e) {
			logger.error("promotionProgramBean.nextOrPrev:" + e.getMessage(), e);
		}
	}

	public void search() {
		try {/*
			 * { promotion_program_info:{from_date:
			 * '',to_date:'',program_code:'',product_id:0,promotion_product_id:0,disable:0},
			 * page:{page_index:0, page_size:0}}
			 */
			listPromotionProgram = new ArrayList<PromotionProgram>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDate, "dd/MM/yyyy HH:mm:ss"));
			jsonInfo.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDate, "dd/MM/yyyy HH:mm:ss"));
			jsonInfo.addProperty("program_code", programCode);
			jsonInfo.addProperty("product_id", product != null ? product.getId() : 0);
			jsonInfo.addProperty("promotion_product_id", promotionProduct != null ? promotionProduct.getId() : 0);
			jsonInfo.addProperty("disable", 0);// chỉ lấy những phiếu còn sử
												// dụng

			JsonObject json = new JsonObject();
			json.add("promotion_program_info", jsonInfo);
			promotionProgramService.search(JsonParserUtil.getGson().toJson(json), listPromotionProgram);
		} catch (Exception e) {
			logger.error("PromotionProgramBean.search:" + e.getMessage(), e);
		}
	}

	public void search2() {
		try {/*
			 * { promotion_program_detail_info:{program_id:0,product_id:0,
			 * promotion_product_id:0}, page:{page_index:0, page_size:0}}
			 */
			listPromotionProgramDetail = new ArrayList<PromotionProgramDetail>();
			PagingInfo page = new PagingInfo();
			if (promotionProgramCrud != null && promotionProgramCrud.getId() != 0) {
				// thông tin phân trang
				JsonObject jsonInfo = new JsonObject();
				jsonInfo.addProperty("program_id", promotionProgramCrud != null ? promotionProgramCrud.getId() : 0);
				jsonInfo.addProperty("product_id", product2 != null ? product2.getId() : 0);
				jsonInfo.addProperty("promotion_product_id", promotionProduct2 != null ? promotionProduct2.getId() : 0);
				JsonObject json = new JsonObject();
				json.add("promotion_program_detail_info", jsonInfo);
				promotionProgramDetailService.search(JsonParserUtil.getGson().toJson(json), page,
						listPromotionProgramDetail);
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.search2:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdate() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (promotionProgramCrud != null) {
				String programCode = promotionProgramCrud.getProgram_code();
				Date effectiveDate = promotionProgramCrud.getEffective_date();
				Date expiryDate = promotionProgramCrud.getExpiry_date();
				if (programCode != null && programCode != "" && effectiveDate != null
						&& (expiryDate == null || effectiveDate.getTime() <= expiryDate.getTime())) {
					PromotionProgramReqInfo t = new PromotionProgramReqInfo(promotionProgramCrud);
					if (promotionProgramCrud.getId() == 0) {
						if (allowSave(new Date())) {
							promotionProgramCrud.setCreated_date(new Date());
							promotionProgramCrud.setCreated_by(account.getMember().getName());

							if (promotionProgramService.insert(t) != -1) {
								// kiểm tra thử có phải lưu dữ liệu copy hay
								// không
								if (listPromotionProgramDetail != null && listPromotionProgramDetail.size() > 0
										&& listPromotionProgramDetail.get(0).getId() == 0) {
									// đúng
									for (PromotionProgramDetail pd : listPromotionProgramDetail) {
										pd.setPromotion_program(t.getPromotion_program());
										promotionProgramDetailService.insert(new PromotionProgramDetailReqInfo(pd));
									}
									success("Sao chép thành công.");
									search();
								} else {
									success();
									listPromotionProgram.add(0, promotionProgramCrud);
								}
							} else {
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Thêm thất bại!','error',2000);");
							}

						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					} else {
						if (allowUpdate(new Date())) {
							promotionProgramCrud.setLast_modifed_date(new Date());
							promotionProgramCrud.setLast_modifed_by(account.getMember().getName());
							if (promotionProgramService.update(t) == 0) {
								success();
								listPromotionProgram.set(listPromotionProgram.indexOf(promotionProgramCrud),
										promotionProgramCrud.clone());
							} else {
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Cập nhật thất bại!','error',2000);");
							}

						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo', 'Thông tin không đầy đủ hoặc ngày hiệu lực và ngày kết thúc không hợp lệ!','warning',2500);");
				}
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}

	public void showEdit() {
		try {
			promotionProgramCrud = promotionProgramSelect.clone();
			promotionProgramDetailCrud = new PromotionProgramDetail();
			// init detail
			listPromotionProgramDetail = new ArrayList<PromotionProgramDetail>();
			product2 = null;
			promotionProduct2 = null;
			search2();
			listCustomerPromotionProgramSet = new ArrayList<>();
			customerPromotionProgramService.selectAll(promotionProgramCrud.getId(), listCustomerPromotionProgramSet);
		} catch (Exception e) {
			logger.error("PromotionProgramBean.showDialogEdit:" + e.getMessage(), e);
		}
	}

	public void createdNew() {
		try {
			promotionProgramCrud = new PromotionProgram();
			String code = promotionProgramService.initPromotionProgramCode();
			promotionProgramCrud.setProgram_code(code);
			promotionProgramCrud.setEffective_date(new Date());
			promotionProgramDetailCrud = new PromotionProgramDetail();
			listPromotionProgramDetail = new ArrayList<PromotionProgramDetail>();
		} catch (Exception e) {
			logger.error("PromotionProgramBean.createdNew:" + e.getMessage(), e);
		}
	}

	public void delete() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (promotionProgramSelect != null) {
				if (allowDelete(new Date())) {
					if (promotionProgramService.deleteById(promotionProgramSelect.getId()) > 0) {
						success();
						executeScript("PF('datatb').clearFilters();PF('datatbct').clearFilters()");
						listPromotionProgram.remove(promotionProgramSelect);
						createdNew();
					} else {
						current.executeScript("swaldesigntimer('Cảnh báo!', 'Không xóa được!','warning',2000);");
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Chưa chọn dòng để xóa!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.delete:" + e.getMessage(), e);
		}
	}

	public void showDialogDetail() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (promotionProgramCrud != null && promotionProgramCrud.getId() != 0) {
				promotionProgramDetailCrud = new PromotionProgramDetail();
				promotionProgramDetailCrud.setPromotion_form(1);
				promotionProgramDetailCrud.setPromotion_program(promotionProgramCrud);
				current.executeScript("PF('dlg1').show();");
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Chương trình khuyến mãi không tồn tại, vui lòng chọn một chương trình khuyến mãi!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.showDialogDetail:" + e.getMessage(), e);
		}
	}

	public void showDialogEditDetail() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (promotionProgramDetailSelect != null) {
				promotionProgramDetailCrud = promotionProgramDetailSelect.clone();
				current.executeScript("PF('dlg1').show();");
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.showDialogEditDetail:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdateDetail() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (promotionProgramDetailCrud != null && promotionProgramCrud != null && promotionProgramCrud.getId() != 0) {
				Product product = promotionProgramDetailCrud.getProduct();
				Product promotionProduct = promotionProgramDetailCrud.getPromotion_product();
				int promotionForm = promotionProgramDetailCrud.getPromotion_form();
				if (product != null && promotionProduct != null && promotionForm != 0) {
					PromotionProgramDetailReqInfo t = new PromotionProgramDetailReqInfo(promotionProgramDetailCrud);
					if (promotionProgramDetailCrud.getId() == 0) {
						if (allowSave(new Date())) {
							promotionProgramDetailCrud.setCreated_date(new Date());
							promotionProgramDetailCrud.setCreated_by(account.getMember().getName());
							int check = promotionProgramDetailService.insert(t);
							if (check == 0) {
								current.executeScript("swaldesigntimer('Thành công!', 'Thêm thành công!','success',2000);");
								listPromotionProgramDetail.add(0, promotionProgramDetailCrud);
								createNewDetail();
							} else if (check > 0) {
								current.executeScript("swaldesigntimer('Cảnh báo!', 'Chi tiết chương trình khuyến mãi đã tồn tại !','warning',2000);");
							} else {
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Lỗi hệ thống!','error',2000);");
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					} else {
						if (allowUpdate(new Date())) {
							int check = promotionProgramDetailService.update(t);
							if (check == 0) {
								current.executeScript("swaldesigntimer('Thành công!', 'Cập nhật thành công!','success',2000);");
								listPromotionProgramDetail.set(
										listPromotionProgramDetail.indexOf(promotionProgramDetailCrud),
										promotionProgramDetailCrud.clone());
							} else if (check > 0) {
								current.executeScript("swaldesigntimer('Cảnh báo!', 'Chi tiết chương trình khuyến mãi đã tồn tại !','warning',2000);");
							} else {
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Lỗi hệ thống!','error',2000);");
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo', 'Thông tin chi tiết chương trình khuyến mãi không đầy đủ, điền đủ thông tin chứa(*)!','warning',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Thông tin không đầy đủ hoặc chương trình khuyến mãi không tồn tại!','error',2000);");
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.saveOrUpdateDetail:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdateCustomerSet() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (customerPromotionProgramSetCrud != null) {
				Customer customer = customerPromotionProgramSetCrud.getCustomer();
				if (customer != null) {
					CustomerPromotionProgramReqInfo t = new CustomerPromotionProgramReqInfo(
							customerPromotionProgramSetCrud);
					if (customerPromotionProgramSetCrud.getId() == 0) {
						if (allowSave(new Date())) {
							customerPromotionProgramSetCrud.setCreated_by(account.getMember().getName());
							customerPromotionProgramSetCrud.setCreated_date(new Date());
							int check = customerPromotionProgramService.insert(t);
							switch (check) {
							case 0:
								current.executeScript("swaldesigntimer('Thành công!', 'Thêm thành công!','success',2000);");
								listCustomerPromotionProgramSet.add(0, t.getCustomer_promotion_program());
								// reset dialog
								showDialogCustomerPromotionProgramSet();
								break;
							case -2:
								current.executeScript("swaldesigntimer('Cảnh báo!', 'Khách hàng này đã được cài đặt trước đó!','warning',2000);");
								break;
							default:
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Thêm thất bại!','error',2000);");
								break;
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					} else {
						if (allowUpdate(new Date())) {
							customerPromotionProgramSetCrud.setLast_modifed_by(account.getMember().getName());
							customerPromotionProgramSetCrud.setLast_modifed_date(new Date());
							int check = customerPromotionProgramService.update(t);
							switch (check) {
							case 0:
								current.executeScript("swaldesigntimer('Thành công!', 'Cập nhật thành công!','success',2000);");
								listCustomerPromotionProgramSet.set(
										listCustomerPromotionProgramSet.indexOf(customerPromotionProgramSetCrud),
										customerPromotionProgramSetCrud.clone());
								break;
							case -2:
								current.executeScript("swaldesigntimer('Cảnh báo!', 'Khách hàng này đã được cài đặt trước đó!','warning',2000);");
								break;
							default:
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Cập nhật thất bại!','error',2000);");
								break;
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Thông tin không đầy đủ, điền đầy đủ thông tin chứa(*)','warning',2000);");
				}
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.saveOrUpdateCustomerSet:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdateCustomerPromotionProgram() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (customerPromotionProgramCrud != null) {
				PromotionProgram ct = customerPromotionProgramCrud.getPromotion_program();
				Customer cs = customerPromotionProgramCrud.getCustomer();
				Date eff = customerPromotionProgramCrud.getEffective_date();
				if (ct != null && cs != null && eff != null) {
					// wrap containner
					CustomerPromotionProgramReqInfo t = new CustomerPromotionProgramReqInfo(
							customerPromotionProgramCrud);
					int chk = 0;
					if (customerPromotionProgramCrud.getId() == 0) {
						if (allowSave(new Date())) {
							customerPromotionProgramCrud.setCreated_by(account.getMember().getName());
							customerPromotionProgramCrud.setCreated_date(new Date());
							chk = customerPromotionProgramService.insert(t);
							switch (chk) {
							case 0:
								current.executeScript("swaldesigntimer('Thành công!', 'Thành công!','success',2000);");
								listCustomerPromotionProgram.add(0, customerPromotionProgramCrud);
								customerPromotionProgramCrud = new CustomerPromotionProgram();
								break;
							case -2:
								current.executeScript("swaldesigntimer('Cảnh báo', 'Trùng dữ liệu, chương trình đơn giá này đã được áp dụng cho khách hàng trên!','warning',2500);");
								break;

							default:
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Thêm thất bại!','error',2000);");
								break;
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					} else {
						if (allowUpdate(new Date())) {
							customerPromotionProgramCrud.setLast_modifed_by(account.getMember().getName());
							customerPromotionProgramCrud.setLast_modifed_date(new Date());
							chk = customerPromotionProgramService.update(t);
							switch (chk) {
							case 0:
								current.executeScript("swaldesigntimer('Thành công!', 'Cập nhật thành công!','success',2000);");
								listCustomerPromotionProgram.set(
										listCustomerPromotionProgram.indexOf(customerPromotionProgramCrud),
										customerPromotionProgramCrud.clone());
								break;
							case -2:
								current.executeScript("swaldesigntimer('Cảnh báo', 'Trùng dữ liệu, chương trình đơn giá này đã được áp dụng cho khách hàng trên!','warning',2500);");
								break;

							default:
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Cập nhật thất bại!','error',2000);");
								break;
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo', 'Thông tin không đầy đủ, điền đủ đầy thông tin chứa(*)','warning',2500);");
				}
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.saveOrUpdateCustomerPromotionProgram:" + e.getMessage(), e);
		}
	}

	private void createNewDetail() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (promotionProgramCrud != null && promotionProgramCrud.getId() != 0) {
				promotionProgramDetailCrud = new PromotionProgramDetail();
				promotionProgramDetailCrud.setPromotion_program(promotionProgramCrud);
				promotionProgramDetailCrud.setPromotion_form(1);
			} else {
				current.executeScript("PF('dlg1').hide();");
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.createNewDetail:" + e.getMessage(), e);
		}
	}

	public void deleteDetail() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (promotionProgramDetailSelect != null) {
				if (allowDelete(new Date())) {
					if (promotionProgramDetailService.deleteById(promotionProgramDetailSelect.getId()) > 0) {
						success();
						executeScript("PF('datatb').clearFilters();PF('datatbct').clearFilters()");
						listPromotionProgramDetail.remove(promotionProgramDetailSelect);
					} else {
						error("Không xóa được.");
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo', 'Chọn một dòng chi tiết để xóa!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.deleteDetail:" + e.getMessage(), e);
		}
	}

	public void showDialogUpload() {
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('dlgup1').show();");
		} catch (Exception e) {
			logger.error("PromotionProgramBean.showDialogUpload:" + e.getMessage(), e);
		}
	}

	public void showDialogUploadChitiet() {
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('dlgup1chitiet').show();");
		} catch (Exception e) {
			logger.error("PromotionProgramBean.dlgup1chitiet:" + e.getMessage(), e);
		}
	}

	@Getter
	@Setter
	boolean rewritechitiet;

	public void loadExcelCTKMChitiet(FileUploadEvent event) {
		if (promotionProgramCrud == null || promotionProgramCrud.getId() == 0) {
			noticeError("Chưa lưu chương trình khuyến mãi.");
		} else {
			if (!allowSave(null) || !allowUpdate(null)) {
				noticeError("Tài khoản không được cài đặt.");
			} else {
				Notify notify = new Notify(FacesContext.getCurrentInstance());
				try {
					UploadedFile part = event.getFile();
					if (part != null) {
						Workbook workBook = getWorkbook(new ByteArrayInputStream(part.getContent()), part.getFileName());
						Sheet firstSheet = workBook.getSheetAt(0);
						Iterator<Row> rows = firstSheet.iterator();
						Map<String, PromotionProgram> map = new LinkedHashMap<String, PromotionProgram>();
						while (rows.hasNext()) {
							rows.next();
							rows.remove();
							break;
						}
						List<PromotionProgramDetail> details = new ArrayList<PromotionProgramDetail>();
						lv1: while (rows.hasNext()) {
							Row row = rows.next();
							Iterator<Cell> cells = row.cellIterator();
							PromotionProgramDetail detail = new PromotionProgramDetail();
							while (cells.hasNext()) {
								Cell cell = cells.next();
								int columnIndex = cell.getColumnIndex();
								switch (columnIndex) {
								case 0:
									try {
										String masp = Objects.toString(MyUtilExcel.getCellValue(cell), null);
										try {
											if (masp.contains(".0")) {
												double maspIsNumber = Double.parseDouble(masp);
												masp = ((int) maspIsNumber) + "";
											}
										} catch (Exception e) {
										}
										if (masp != null && !"".equals(masp)) {
											ProductReqInfo pinfo = new ProductReqInfo();
											productService.selectByCode(masp, pinfo);
											if (pinfo.getProduct() != null) {
												detail.setProduct(pinfo.getProduct());
											} else {
												continue lv1;
											}
										} else {
											continue lv1;
										}
									} catch (Exception e) {
									}
									break;
								case 1:
									try {
										String maspkm = Objects.toString(MyUtilExcel.getCellValue(cell), null);
										if (maspkm != null && !"".equals(maspkm)) {
											ProductReqInfo pinfo = new ProductReqInfo();
											productService.selectByCode(maspkm, pinfo);
											if (pinfo.getProduct() != null) {
												detail.setPromotion_product(pinfo.getProduct());
											} else {
												continue lv1;
											}
										} else {
											continue lv1;
										}
									} catch (Exception e) {
									}
									break;
								case 2:
									try {
										String sothung = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
										detail.setBox_quatity(Double.parseDouble(sothung));
									} catch (Exception e) {
									}
									break;
								case 3:
									try {
										// Số lượng khuyến mãi (tính đvt) dùng
										// cột này
										String sodvsp = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
										detail.setSpecification(Double.parseDouble(sodvsp));
									} catch (Exception e) {
									}
									break;
								case 4:
									try {
										// Đã bỏ (không còn dử dụng cột này)
										String soluongkm = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
										detail.setPromotion_quantity(Double.parseDouble(soluongkm));
									} catch (Exception e) {
									}
									break;
								case 5:
									try {
										String hthuckm = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
										detail.setPromotion_form((int) Double.parseDouble(hthuckm));
									} catch (Exception e) {
									}
									break;
								}
							}
							detail.setPromotion_program(promotionProgramCrud);
							details.add(detail);
						}
						if (rewritechitiet)
							// xóa tất cả chi tiết chương trình đơn giá.
							promotionProgramDetailService.deleteAll(promotionProgramCrud.getId());
						for (PromotionProgramDetail pd : details) {
							pd.setPromotion_program(promotionProgramCrud);
							pd.setCreated_date(new Date());
							pd.setCreated_by(account.getMember().getName());
							// kiểm tra đã tồn tại sản phẩm trong chuong
							// trình đơn giá đó chưa.
							// Product product = pd.getProduct();
							// Product promotionProduct =
							// pd.getPromotion_product();
							// int check =
							// promotionProgramDetailService.checkExist(product
							// != null ? product.getId() : 0,
							// promotionProduct != null ?
							// promotionProduct.getId() : 0, pd.getId(), pd
							// .getPromotion_program().getId(),
							// pd.getPromotion_form());
							// if (check == 0) {
							promotionProgramDetailService.insert(new PromotionProgramDetailReqInfo(pd));
							// } else {
							// promotionProgramDetailService.updateByPredicate(pd.getProduct().getId(),
							// pd
							// .getPromotion_product().getId(),
							// pd.getPromotion_form(), promotionProgramCrud
							// .getId(), pd.getBox_quatity(),
							// pd.getPromotion_quantity());
							// }
						}
						search2();
						notify.success();
					}
				} catch (Exception e) {
					logger.error("PricingProgramBean.loadExcelCTDG:" + e.getMessage(), e);
				}
			}
		}
	}

	public void loadExcelCTKM(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			UploadedFile part = event.getFile();
			if (part != null) {
				Workbook workBook = getWorkbook(new ByteArrayInputStream(part.getContent()), part.getFileName());
				Sheet firstSheet = workBook.getSheetAt(0);
				Iterator<Row> rows = firstSheet.iterator();
				Map<String, PromotionProgram> map = new LinkedHashMap<String, PromotionProgram>();
				while (rows.hasNext()) {
					rows.next();
					rows.remove();
					break;
				}
				lv1: while (rows.hasNext()) {
					Row row = rows.next();
					Iterator<Cell> cells = row.cellIterator();
					String mactkm = null;
					PromotionProgramDetail detail = new PromotionProgramDetail();
					while (cells.hasNext()) {
						Cell cell = cells.next();
						int columnIndex = cell.getColumnIndex();

						switch (columnIndex) {
						case 0:
							try {
								mactkm = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								if (mactkm == null || !"".equals(mactkm)) {
									if (!map.containsKey(mactkm)) {
										PromotionProgram program = new PromotionProgram();
										program.setList_promotion_program_detail(new ArrayList<>());
										map.put(mactkm, program);
									}
									map.get(mactkm).setProgram_code(mactkm);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								String ngayHL = Objects.toString(MyUtilExcel.getCellValue(cell));
								if (ngayHL != null && !"".equals(ngayHL)) {
									map.get(mactkm).setEffective_date(
											ToolTimeCustomer.convertStringToDate(ngayHL, "dd/MM/yyyy"));
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								String ngayKT = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								map.get(mactkm).setExpiry_date(
										ToolTimeCustomer.convertStringToDate(ngayKT, "dd/MM/yyyy"));
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								String masp = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								try {
									double maspIsNumber = Double.parseDouble(masp);
									masp = ((int) maspIsNumber) + "";
								} catch (Exception e) {
								}
								if (masp != null && !"".equals(masp)) {
									ProductReqInfo pinfo = new ProductReqInfo();
									productService.selectByCode(masp, pinfo);
									if (pinfo.getProduct() != null) {
										detail.setProduct(pinfo.getProduct());
									} else {
										continue lv1;
									}
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								String maspkm = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								if (maspkm != null && !"".equals(maspkm)) {
									ProductReqInfo pinfo = new ProductReqInfo();
									productService.selectByCode(maspkm, pinfo);
									if (pinfo.getProduct() != null) {
										detail.setPromotion_product(pinfo.getProduct());
									} else {
										continue lv1;
									}
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 5:
							try {
								String sothung = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								detail.setBox_quatity(Double.parseDouble(sothung));
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// Số lượng khuyến mãi (tính đvt) dùng cột này
								String sodvsp = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								detail.setSpecification(Double.parseDouble(sodvsp));
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								// Đã bỏ (không còn dử dụng cột này)
								String soluongkm = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								detail.setPromotion_quantity(Double.parseDouble(soluongkm));
							} catch (Exception e) {
							}
							break;
						case 8:
							try {
								String hthuckm = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								detail.setPromotion_form((int) Double.parseDouble(hthuckm));
								map.get(mactkm).getList_promotion_program_detail().add(detail);
							} catch (Exception e) {
							}
							break;
						}

					}
				}
				for (Map.Entry<String, PromotionProgram> entry : map.entrySet()) {
					String key = entry.getKey();
					PromotionProgram value = entry.getValue();
					// check key đã tồn tại chưa.
					PromotionProgramReqInfo ppr = new PromotionProgramReqInfo();
					promotionProgramService.selectByCode(key, ppr);
					PromotionProgram program = ppr.getPromotion_program();
					List<PromotionProgramDetail> listDetail = value.getList_promotion_program_detail();
					if (program != null) {
						program.setEffective_date(value.getEffective_date());
						program.setExpiry_date(value.getExpiry_date());
						// cập nhật lại chương trình khuyến mãi.
						promotionProgramService.update(new PromotionProgramReqInfo(program));
						if (rewrite) {
							// xóa tất cả chi tiết chương trình khuyến mãi.
							promotionProgramDetailService.deleteAll(program.getId());
							for (PromotionProgramDetail pd : listDetail) {
								pd.setPromotion_program(program);
								pd.setCreated_date(new Date());
								pd.setCreated_by(account.getMember().getName());
								// kiểm tra đã tồn tại sản phẩm trong chương
								// trình khuyến mãi đó chưa.
								if (promotionProgramDetailService.checkExist(pd.getProduct().getId(), pd
										.getPromotion_product().getId(), 0, program.getId(), pd.getPromotion_form()) == 0) {
									promotionProgramDetailService.insert(new PromotionProgramDetailReqInfo(pd));
								} else {
									List<PromotionProgramDetail> listTemp = new ArrayList<>();
									/*
									 * {product_id:0,promotion_product_id:0,
									 * promotion_program_id:0,promotion_form:0}
									 */
									JsonObject json = new JsonObject();
									json.addProperty("product_id", pd.getProduct().getId());
									json.addProperty("promotion_product_id", pd.getPromotion_product().getId());
									json.addProperty("promotion_program_id", program.getId());
									json.addProperty("promotion_form", pd.getPromotion_form());
									promotionProgramDetailService.selectBy(JsonParserUtil.getGson().toJson(json),
											listTemp);
									for (PromotionProgramDetail v : listTemp) {
										v.setLast_modifed_by(account.getMember().getName());
										v.setLast_modifed_date(new Date());
										v.setBox_quatity(pd.getBox_quatity());
										v.setSpecification(pd.getSpecification());
										v.setPromotion_quantity(pd.getPromotion_quantity());
										v.setPromotion_form(pd.getPromotion_form());
										promotionProgramDetailService.update(new PromotionProgramDetailReqInfo(v));

									}
								}
							}
						} else {
							for (PromotionProgramDetail pd : listDetail) {
								pd.setPromotion_program(program);
								pd.setCreated_date(new Date());
								pd.setCreated_by(account.getMember().getName());
								if (promotionProgramDetailService.checkExist(pd.getProduct().getId(), pd
										.getPromotion_product().getId(), 0, program.getId(), pd.getPromotion_form()) == 0) {
									promotionProgramDetailService.insert(new PromotionProgramDetailReqInfo(pd));
								} else {
									// cập nhật chi tiết
									List<PromotionProgramDetail> listTemp = new ArrayList<>();
									/*
									 * {product_id:0,promotion_product_id:0,
									 * promotion_program_id:0,promotion_form:0}
									 */
									JsonObject json = new JsonObject();
									json.addProperty("product_id", pd.getProduct().getId());
									json.addProperty("promotion_product_id", pd.getPromotion_product().getId());
									json.addProperty("promotion_program_id", program.getId());
									json.addProperty("promotion_form", pd.getPromotion_form());
									promotionProgramDetailService.selectBy(JsonParserUtil.getGson().toJson(json),
											listTemp);
									for (PromotionProgramDetail v : listTemp) {
										v.setLast_modifed_by(account.getMember().getName());
										v.setLast_modifed_date(new Date());
										v.setBox_quatity(pd.getBox_quatity());
										v.setSpecification(pd.getSpecification());
										v.setPromotion_quantity(pd.getPromotion_quantity());
										v.setPromotion_form(pd.getPromotion_form());
										promotionProgramDetailService.update(new PromotionProgramDetailReqInfo(v));

									}
								}
							}
						}
					} else {
						// lưu chương trình khuyến mãi.
						value.setList_promotion_program_detail(null);
						value.setCreated_date(new Date());
						value.setCreated_by(account.getMember().getName());
						promotionProgramService.insert(new PromotionProgramReqInfo(value));
						for (PromotionProgramDetail pd : listDetail) {
							pd.setPromotion_program(value);
							pd.setCreated_date(new Date());
							pd.setCreated_by(account.getMember().getName());
							if (promotionProgramDetailService.checkExist(pd.getProduct().getId(), pd
									.getPromotion_product().getId(), 0, value.getId(), pd.getPromotion_form()) == 0) {
								promotionProgramDetailService.insert(new PromotionProgramDetailReqInfo(pd));
							} else {
								// cập nhật lại chi tiết
								List<PromotionProgramDetail> listTemp = new ArrayList<>();
								/*
								 * {product_id:0,promotion_product_id:0,
								 * promotion_program_id:0,promotion_form:0}
								 */
								JsonObject json = new JsonObject();
								json.addProperty("product_id", pd.getProduct().getId());
								json.addProperty("promotion_product_id", pd.getPromotion_product().getId());
								json.addProperty("promotion_program_id", value.getId());
								json.addProperty("promotion_form", pd.getPromotion_form());
								promotionProgramDetailService.selectBy(JsonParserUtil.getGson().toJson(json), listTemp);
								for (PromotionProgramDetail v : listTemp) {
									v.setLast_modifed_by(account.getMember().getName());
									v.setLast_modifed_date(new Date());
									v.setBox_quatity(pd.getBox_quatity());
									v.setSpecification(pd.getSpecification());
									v.setPromotion_quantity(pd.getPromotion_quantity());
									v.setPromotion_form(pd.getPromotion_form());
									promotionProgramDetailService.update(new PromotionProgramDetailReqInfo(v));

								}
							}
						}
					}
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("PricingProgramBean.loadExcelCTDG:" + e.getMessage(), e);
		}
	}

	public void loadExcelCDCTKM(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			UploadedFile part = event.getFile();
			if (part != null) {
				Workbook workBook = getWorkbook(new ByteArrayInputStream(part.getContent()), part.getFileName());
				Sheet firstSheet = workBook.getSheetAt(0);
				Iterator<Row> rows = firstSheet.iterator();
				List<CustomerPromotionProgram> listCustomerPromotionProgram = new ArrayList<>();
				while (rows.hasNext()) {
					rows.next();
					rows.remove();
					break;
				}
				lv1: while (rows.hasNext()) {
					Row row = rows.next();
					Iterator<Cell> cells = row.cellIterator();
					CustomerPromotionProgram lix = new CustomerPromotionProgram();
					while (cells.hasNext()) {
						Cell cell = cells.next();
						int columnIndex = cell.getColumnIndex();

						switch (columnIndex) {
						case 0:
							try {
								String mact = Objects.toString(MyUtilExcel.getCellValue(cell));
								if (mact != null && !"".equals(mact)) {
									PromotionProgramReqInfo tp = new PromotionProgramReqInfo();
									promotionProgramService.selectByCode(mact, tp);
									if (tp.getPromotion_program() != null) {
										lix.setPromotion_program(tp.getPromotion_program());
									} else {
										// Thông báo mã không hợp lệ
										current.executeScript("swaldesigntimer('Cảnh báo!', 'Mã chương trình:" + mact
												+ " không tồn tại','error',2000);");
										return;
									}
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// ma khach hang
								String makh = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								if (makh == null || !"".equals(makh)) {
									CustomerReqInfo c = new CustomerReqInfo();
									customerService.selectByCode(makh, c);
									if (c.getCustomer() != null) {
										lix.setCustomer(c.getCustomer());
									} else {
										// Thông báo mã không hợp lệ
										current.executeScript("swaldesigntimer('Cảnh báo!', 'Mã khách hàng:" + makh
												+ " không tồn tại','error',2000);");
										return;
									}
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 2:
							try {

								Date ngayBD = null;
								if (MyUtilExcel.getCellValue(workBook, cell).getClass().equals(Double.class)) {
									ngayBD = DateUtil.getJavaDate((double) MyUtilExcel.getCellValue(workBook, cell));
								} else {
									ngayBD = (Date) MyUtilExcel.getCellValue(workBook, cell);
								}
								lix.setEffective_date(ngayBD);

							} catch (Exception e) {
							}
							break;
						case 3:
							try {

								Date ngayKT = null;
								if (MyUtilExcel.getCellValue(workBook, cell).getClass().equals(Double.class)) {
									ngayKT = DateUtil.getJavaDate((double) MyUtilExcel.getCellValue(workBook, cell));
								} else {
									ngayKT = (Date) MyUtilExcel.getCellValue(workBook, cell);
								}
								lix.setExpiry_date(ngayKT);

							} catch (Exception e) {
							}
							break;
						}

					}
					listCustomerPromotionProgram.add(lix);
				}
				for (CustomerPromotionProgram it : listCustomerPromotionProgram) {
					it.setCreated_by(account.getMember().getName());
					it.setCreated_date(new Date());
					if (customerPromotionProgramService.insert(new CustomerPromotionProgramReqInfo(it)) != -2) {
						// silent
					}
				}
				notify.success();

			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.loadExcelCDCTKM:" + e.getMessage(), e);
		}
	}

	public void capnhatlaingay() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (allowUpdate(new Date())) {
				List<Long> ids = new ArrayList<Long>();
				for (int i = 0; i < listCustomerPromotionProgramSet.size(); i++) {
					ids.add(listCustomerPromotionProgramSet.get(i).getId());
				}
				if (ids.size() != 0) {
					customerPromotionProgramService.caphnhatlaingay(ids, promotionProgramCrud.getEffective_date(),
							promotionProgramCrud.getExpiry_date());

					listCustomerPromotionProgramSet = new ArrayList<>();
					customerPromotionProgramService.selectAll(promotionProgramCrud.getId(),
							listCustomerPromotionProgramSet);
					executeScript("PF('datatb').clearFilters()");
					success();
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
			}

		} catch (Exception e) {
			logger.error("PricingProgramBean.capnhatlaingay:" + e.getMessage(), e);
		}
	}

	private Workbook getWorkbook(InputStream inputStream, String nameFile) throws IOException {
		Workbook workbook = null;
		if (nameFile.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else if (nameFile.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("The specified file is not Excel file");
		}

		return workbook;
	}

	public List<Product> completeProduct(String text) {
		try {
			List<Product> list = new ArrayList<Product>();
			productService.findLike(FormatHandler.getInstance().converViToEn(text), 120, list);
			return list;
		} catch (Exception e) {
			logger.error("PromotionProgramBean.completeProduct:" + e.getMessage(), e);
		}
		return null;
	}

	public List<PromotionProgram> completePromotionProgram(String text) {
		try {
			List<PromotionProgram> list = new ArrayList<PromotionProgram>();
			promotionProgramService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("PromotionProgramBean.completePromotionProgram:" + e.getMessage(), e);
		}
		return null;
	}

	public void changeDataDialogCDKH() {
		try {
			PromotionProgram pg = customerPromotionProgramCrud.getPromotion_program();
			if (pg != null) {
				customerPromotionProgramCrud.setEffective_date(pg.getEffective_date());
				customerPromotionProgramCrud.setExpiry_date(pg.getExpiry_date());
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.changeDataDialogCDKH:" + e.getMessage(), e);
		}
	}

	public void showDialogEditCustomerPromotionProgramSet() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (promotionProgramCrud != null && promotionProgramCrud.getId() != 0
					&& customerPromotionProgramSetSelect != null) {
				customerPromotionProgramSetCrud = customerPromotionProgramSetSelect.clone();
				current.executeScript("PF('dlgSet').show();");
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.showDialogEditCustomerPromotionProgramSet:" + e.getMessage(), e);
		}
	}

	public void deleteCustomerPromotionProgramSet() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (customerPromotionProgramSetSelect != null && customerPromotionProgramSetSelect.getId() != 0) {
				if (allowDelete(new Date())) {
					if (customerPromotionProgramService.deleteById(customerPromotionProgramSetSelect.getId()) > 0) {
						success();
						executeScript("PF('datatb').clearFilters();PF('datatbct').clearFilters()");
						listCustomerPromotionProgramSet.remove(customerPromotionProgramSetSelect);
					} else {
						current.executeScript("swaldesigntimer('Cảnh báo!', 'Không xóa được!','warning',2000);");
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không được phân quyền để làm việc này!','warning',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Chưa chọn khách hàng để xóa!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.deleteCustomerPromotionProgramSet:" + e.getMessage(), e);
		}
	}

	public void showDialogCustomerPromotionProgramSet() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (promotionProgramCrud != null && promotionProgramCrud.getId() != 0) {
				customerTypesSet = null;
				customerPromotionProgramSetCrud = new CustomerPromotionProgram();
				customerPromotionProgramSetCrud.setPromotion_program(promotionProgramCrud);
				customerPromotionProgramSetCrud.setEffective_date(promotionProgramCrud.getEffective_date());
				customerPromotionProgramSetCrud.setExpiry_date(promotionProgramCrud.getExpiry_date());
				current.executeScript("PF('dlgSet').show();");
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.showDialogCustomerPromotionProgramSet:" + e.getMessage(), e);
		}
	}

	public List<Customer> completeCustomer(String text) {
		try {
			List<Customer> list = new ArrayList<>();
			if (customerTypesSearchSet != null) {
				customerService.complete(FormatHandler.getInstance().converViToEn(text), customerTypesSearchSet, list);
			} else {
				customerService.complete(FormatHandler.getInstance().converViToEn(text), list);
			}
			return list;
		} catch (Exception e) {
			logger.error("PromotionProgramBean.completeCustomer:" + e.getMessage(), e);
		}
		return null;
	}

	public List<Customer> completeCustomerSet(String text) {
		try {
			List<Customer> list = new ArrayList<>();
			if (customerTypesSet != null) {
				customerService.complete(FormatHandler.getInstance().converViToEn(text), customerTypesSet, list);
			} else {
				customerService.complete(FormatHandler.getInstance().converViToEn(text), list);
			}
			return list;
		} catch (Exception e) {
			logger.error("PromotionProgramBean.completeCustomerSet:" + e.getMessage(), e);
		}
		return null;
	}

	public List<Customer> completeCustomerSetting(String text) {
		try {
			List<Customer> list = new ArrayList<>();
			if (customerTypesSearch != null) {
				customerService.complete(FormatHandler.getInstance().converViToEn(text), customerTypesSearch, list);
			} else {
				customerService.complete(FormatHandler.getInstance().converViToEn(text), list);
			}
			return list;
		} catch (Exception e) {
			logger.error("PromotionProgramBean.completeCustomerSetting:" + e.getMessage(), e);
		}
		return null;
	}

	public List<Customer> completeCustomerCommon(String text) {
		try {
			List<Customer> list = new ArrayList<>();
			customerService.complete(formatHandler.converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("PricingProgramBean.completeCustomer:" + e.getMessage(), e);
		}
		return null;
	}

	public void resetSearchSet() {
		customerSearchSet = new Customer();
	}

	public void searchCustomerSet() {
		try {
			listCustomerPromotionProgramSet = new ArrayList<>();
			if (promotionProgramCrud != null && promotionProgramCrud.getId() != 0) {
				/*
				 * {
				 * customer_promotion_program_info:{customer_types_id:0,customer_id
				 * :0,program_code:'',from_date:'',to_date:'',disable:-1},
				 * page:{page_index:0, page_size:0}}
				 */
				JsonObject json = new JsonObject();
				JsonObject jsonInfo = new JsonObject();
				jsonInfo.addProperty("customer_types_id",
						customerTypesSearchSet == null ? 0 : customerTypesSearchSet.getId());
				jsonInfo.addProperty("customer_id", customerSearchSet == null ? 0 : customerSearchSet.getId());
				jsonInfo.addProperty("program_code", promotionProgramCrud.getProgram_code());
				jsonInfo.addProperty("disable", -1);
				json.add("customer_promotion_program_info", jsonInfo);
				customerPromotionProgramService.search(JsonParserUtil.getGson().toJson(json),
						listCustomerPromotionProgramSet);
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.searchCustomerSet:" + e.getMessage(), e);
		}
	}

	public void paginatorChangeSet(int currentPageSet) {
		try {
			listCustomerPromotionProgramSet = new ArrayList<>();
			/*
			 * {
			 * customer_promotion_program_info:{customer_types_id:0,customer_id
			 * :0,program_code:'',from_date:'',to_date:'',disable:-1},
			 * page:{page_index:0, page_size:0}}
			 */
			JsonObject json = new JsonObject();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("customer_types_id",
					customerTypesSearchSet == null ? 0 : customerTypesSearchSet.getId());
			jsonInfo.addProperty("customer_id", customerSearchSet == null ? 0 : customerSearchSet.getId());
			jsonInfo.addProperty("program_code", promotionProgramCrud.getProgram_code());
			jsonInfo.addProperty("disable", -1);
			json.add("customer_promotion_program_info", jsonInfo);
			customerPromotionProgramService.search(JsonParserUtil.getGson().toJson(json),
					listCustomerPromotionProgramSet);
		} catch (Exception e) {
			logger.error("PromotionProgramBean.paginatorChange2:" + e.getMessage(), e);
		}
	}

	public void searchCustomerPromotionProgram() {
		try {
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("customer_types_id", customerTypesSearch3 == null ? 0 : customerTypesSearch3.getId());
			jsonInfo.addProperty("customer_id", customerSearch3 == null ? 0 : customerSearch3.getId());

			jsonInfo.addProperty("program_code", promotionProgramCodeSearch3);
			jsonInfo.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch3, "dd/MM/yyyy"));
			jsonInfo.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch3, "dd/MM/yyyy"));
			jsonInfo.addProperty("disable", -1);
			JsonObject json = new JsonObject();
			json.add("customer_promotion_program_info", jsonInfo);
			listCustomerPromotionProgram = new ArrayList<>();
			customerPromotionProgramService.search(JsonParserUtil.getGson().toJson(json), listCustomerPromotionProgram);
		} catch (Exception e) {
			logger.error("PromotionProgramBean.searchCustomerPromotionProgram:" + e.getMessage(), e);
		}
	}

	public void showDialogExcel() {
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('dlgup1').show();");

		} catch (Exception e) {
			logger.error("PromotionProgramBean.showDialogSetting:" + e.getMessage(), e);
		}
	}

	public void showDialogAdd() {
		try {
			customerPromotionProgramCrud = new CustomerPromotionProgram();
			executeScript("PF('dlgckm').show();");
		} catch (Exception e) {
			logger.error("PromotionProgramBean.showDialogAddSettings:" + e.getMessage(), e);
		}
	}

	public void showDialogEditCustomerPromotionProgram() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (customerPromotionProgramSelect != null) {
				customerPromotionProgramCrud = customerPromotionProgramSelect.clone();
				current.executeScript("PF('dlgctkhcrud').show();");
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Chưa chọn dòng để cập nhật!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.showDialogEditCustomerPromotionProgram:" + e.getMessage(), e);
		}
	}

	public void showDialogUploadCDKH() {
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('dlgup2').show();");
		} catch (Exception e) {
			logger.error("PromotionProgramBean.showDialogUploadCDKH:" + e.getMessage(), e);
		}
	}

	public void innitSourceCustomer() {
		try {
			if (customerTypesSetting != null) {
				List<Customer> list = new ArrayList<Customer>();
				customerService.selectAllByCustomerTypes(customerTypesSetting != null ? customerTypesSetting.getId()
						: 0, list);
				list.removeAll(modelCustomerSetting.getTarget());
				modelCustomerSetting.setSource(list);
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.innitSourceCustomer:" + e.getMessage(), e);
		}
	}

	public void searchCustomerSetting() {
		try {
			if (textSearch != null && !"".equals(textSearch)) {
				List<Customer> list = new ArrayList<Customer>();
				if (customerTypesSetting != null) {
					customerService.complete(formatHandler.converViToEn(textSearch), customerTypesSetting, list);

				} else {
					customerService.complete(formatHandler.converViToEn(textSearch), list);
				}
				list.removeAll(modelCustomerSetting.getTarget());
				modelCustomerSetting.setSource(list);
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.searchCustomerSetting:" + e.getMessage(), e);
		}
	}

	public void settingCustomers() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (allowSave(new Date())) {
				List<Customer> listCustomer = modelCustomerSetting.getTarget();
				if (listCustomer != null && listCustomer.size() > 0 && promotionProgramSetting != null) {
					int[] a = { 0, 0, 0 };
					for (Customer c : listCustomer) {
						CustomerPromotionProgram cp = new CustomerPromotionProgram();
						cp.setCustomer(c);
						cp.setPromotion_program(promotionProgramSetting);
						cp.setCreated_by(account.getMember().getName());
						cp.setCreated_date(new Date());
						cp.setEffective_date(promotionProgramSetting.getEffective_date());
						cp.setExpiry_date(promotionProgramSetting.getExpiry_date());
						CustomerPromotionProgramReqInfo t = new CustomerPromotionProgramReqInfo();
						t.setCustomer_promotion_program(cp);// wrap container
						int chk = customerPromotionProgramService.insert(t);
						switch (chk) {
						case 0:
							a[0]++;
							break;
						case -2:
							a[1]++;
							break;
						default:
							a[2]++;
							break;
						}

					}
					searchCustomerPromotionProgram();
					StringBuilder noidung = new StringBuilder();
					noidung.append("Thông tin cài đặt CTKM:" + promotionProgramSetting.getProgram_code());
					noidung.append("<br/>Khách hàng cài đặt thành công: <b style=\"color:red;\">" + a[0] + "</b>");
					noidung.append("<br/>Khách hàng đã được cài đặt trước đó(trùng): <b style=\"color:red;\">" + a[1]
							+ "</b>");
					noidung.append("<br/>Khách hàng cài đặt thất bại: <b style=\"color:red;\">" + a[2] + "</b>");
					current.executeScript("swaldesignclose('Thông báo','" + noidung.toString() + "','info');");

				} else {
					current.executeScript("swaldesigntimer('Cảnh báo', 'Thông tin không đầy đủ, điền thông tin chứa(*)','warning',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không được phân quyền thực hiện hoặc tháng đã khoá!','error',2000);");
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.settingCustomers:" + e.getMessage(), e);
		}
	}

	public void copyCTKM() {
		try {
			if (promotionProgramCrud != null) {
				promotionProgramCrud.setId(0);
				promotionProgramCrud.setProgram_code(promotionProgramService.initPromotionProgramCode());
				promotionProgramCrud.setNote(null);
				promotionProgramCrud.setCreated_by(null);
				promotionProgramCrud.setCreated_date(null);
				promotionProgramCrud.setLast_modifed_by(null);
				promotionProgramCrud.setLast_modifed_date(null);
			}
			for (PromotionProgramDetail t : listPromotionProgramDetail) {
				t.setId(0);
				t.setPromotion_program(null);
				t.setCreated_by(null);
				t.setCreated_date(null);
				t.setLast_modifed_by(null);
				t.setLast_modifed_date(null);
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.copyCTKM:" + e.getMessage(), e);
		}
	}

	public void exportExcel() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (listPromotionProgramDetail != null && listPromotionProgramDetail.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "masoctkm", "ngayhieuluc", "ngayhethan", "ghichu", "masp", "tensp", "maspkm",
						"tenspkm", "sothung", "sodvsp", "soluongkm", "hinhthuckm" };
				results.add(title);
				String masoctkm = promotionProgramCrud.getProgram_code();
				String ngayhl = ToolTimeCustomer.convertDateToString(promotionProgramCrud.getEffective_date(),
						"dd/MM/yyyy");
				String ngayhh = promotionProgramCrud.getExpiry_date() == null ? "" : ToolTimeCustomer
						.convertDateToString(promotionProgramCrud.getExpiry_date(), "dd/MM/yyyy");
				String ghichu = promotionProgramCrud.getNote() == null ? "" : promotionProgramCrud.getNote();
				for (PromotionProgramDetail it : listPromotionProgramDetail) {
					Object[] row = { masoctkm, ngayhl, ngayhh, ghichu, it.getProduct().getProduct_code(),
							it.getProduct().getProduct_name(), it.getPromotion_product().getProduct_code(),
							it.getPromotion_product().getProduct_name(), it.getBox_quatity(), it.getSpecification(),
							it.getPromotion_quantity(), it.getPromotion_form() };
					results.add(row);
				}
				ToolReport.printReportExcelRaw(results, "chi_tiet_chuong_trinh_khuyen_mai" + masoctkm);
			} else {
				notify.message("Không có dữ liệu");
			}
		} catch (Exception e) {
			logger.error("PromotionProgramBean.exportExcel:" + e.getMessage(), e);
		}
	}

	public void changeProductAndSpecification() {
		try {
			if (promotionProgramDetailCrud != null) {
				Product p = promotionProgramDetailCrud.getPromotion_product();
				if (p != null) {
					promotionProgramDetailCrud.setSpecification(BigDecimal
							.valueOf(promotionProgramDetailCrud.getBox_quatity())
							.multiply(BigDecimal.valueOf(p.getSpecification())).doubleValue());
				}
			}
		} catch (Exception e) {
		}
	}

	public List<Customer> completeCustomerForCustomerProgam(String text) {
		try {
			List<Customer> list = new ArrayList<>();
			customerService.complete(formatHandler.converViToEn(text), customerTypesSearch3, list);
			return list;
		} catch (Exception e) {
			logger.error("PromotionProgramBean.completeCustomerByType:" + e.getMessage(), e);
		}
		return null;

	}

	public PromotionProgram getPromotionProgramCrud() {
		return promotionProgramCrud;
	}

	public void setPromotionProgramCrud(PromotionProgram promotionProgramCrud) {
		this.promotionProgramCrud = promotionProgramCrud;
	}

	public PromotionProgram getPromotionProgramSelect() {
		return promotionProgramSelect;
	}

	public void setPromotionProgramSelect(PromotionProgram promotionProgramSelect) {
		this.promotionProgramSelect = promotionProgramSelect;
	}

	public List<PromotionProgram> getListPromotionProgram() {
		return listPromotionProgram;
	}

	public void setListPromotionProgram(List<PromotionProgram> listPromotionProgram) {
		this.listPromotionProgram = listPromotionProgram;
	}

	public PromotionProgramDetail getPromotionProgramDetailCrud() {
		return promotionProgramDetailCrud;
	}

	public void setPromotionProgramDetailCrud(PromotionProgramDetail promotionProgramDetailCrud) {
		this.promotionProgramDetailCrud = promotionProgramDetailCrud;
	}

	public PromotionProgramDetail getPromotionProgramDetailSelect() {
		return promotionProgramDetailSelect;
	}

	public void setPromotionProgramDetailSelect(PromotionProgramDetail promotionProgramDetailSelect) {
		this.promotionProgramDetailSelect = promotionProgramDetailSelect;
	}

	public List<PromotionProgramDetail> getListPromotionProgramDetail() {
		return listPromotionProgramDetail;
	}

	public void setListPromotionProgramDetail(List<PromotionProgramDetail> listPromotionProgramDetail) {
		this.listPromotionProgramDetail = listPromotionProgramDetail;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Product getPromotionProduct() {
		return promotionProduct;
	}

	public void setPromotionProduct(Product promotionProduct) {
		this.promotionProduct = promotionProduct;
	}

	public Product getProduct2() {
		return product2;
	}

	public void setProduct2(Product product2) {
		this.product2 = product2;
	}

	public Product getPromotionProduct2() {
		return promotionProduct2;
	}

	public void setPromotionProduct2(Product promotionProduct2) {
		this.promotionProduct2 = promotionProduct2;
	}

	public FormatHandler getFormatHandler() {
		return formatHandler;
	}

	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
	}

	public List<CustomerPromotionProgram> getListCustomerPromotionProgramSet() {
		return listCustomerPromotionProgramSet;
	}

	public void setListCustomerPromotionProgramSet(List<CustomerPromotionProgram> listCustomerPromotionProgramSet) {
		this.listCustomerPromotionProgramSet = listCustomerPromotionProgramSet;
	}

	public CustomerPromotionProgram getCustomerPromotionProgramSetCrud() {
		return customerPromotionProgramSetCrud;
	}

	public void setCustomerPromotionProgramSetCrud(CustomerPromotionProgram customerPromotionProgramSetCrud) {
		this.customerPromotionProgramSetCrud = customerPromotionProgramSetCrud;
	}

	public CustomerPromotionProgram getCustomerPromotionProgramSetSelect() {
		return customerPromotionProgramSetSelect;
	}

	public void setCustomerPromotionProgramSetSelect(CustomerPromotionProgram customerPromotionProgramSetSelect) {
		this.customerPromotionProgramSetSelect = customerPromotionProgramSetSelect;
	}

	public Customer getCustomerSearchSet() {
		return customerSearchSet;
	}

	public void setCustomerSearchSet(Customer customerSearchSet) {
		this.customerSearchSet = customerSearchSet;
	}

	public CustomerTypes getCustomerTypesSearchSet() {
		return customerTypesSearchSet;
	}

	public void setCustomerTypesSearchSet(CustomerTypes customerTypesSearchSet) {
		this.customerTypesSearchSet = customerTypesSearchSet;
	}

	public List<CustomerTypes> getListCustomerTypes() {
		return listCustomerTypes;
	}

	public void setListCustomerTypes(List<CustomerTypes> listCustomerTypes) {
		this.listCustomerTypes = listCustomerTypes;
	}

	public CustomerTypes getCustomerTypesSet() {
		return customerTypesSet;
	}

	public void setCustomerTypesSet(CustomerTypes customerTypesSet) {
		this.customerTypesSet = customerTypesSet;
	}

	public CustomerTypes getCustomerTypesSearch() {
		return customerTypesSearch;
	}

	public void setCustomerTypesSearch(CustomerTypes customerTypesSearch) {
		this.customerTypesSearch = customerTypesSearch;
	}

	public Customer getCustomerSearch() {
		return customerSearch;
	}

	public void setCustomerSearch(Customer customerSearch) {
		this.customerSearch = customerSearch;
	}

	public String getPromotionProgramCodeSearch() {
		return promotionProgramCodeSearch;
	}

	public void setPromotionProgramCodeSearch(String promotionProgramCodeSearch) {
		this.promotionProgramCodeSearch = promotionProgramCodeSearch;
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

	public List<CustomerPromotionProgram> getListCustomerPromotionProgram() {
		return listCustomerPromotionProgram;
	}

	public void setListCustomerPromotionProgram(List<CustomerPromotionProgram> listCustomerPromotionProgram) {
		this.listCustomerPromotionProgram = listCustomerPromotionProgram;
	}

	public CustomerPromotionProgram getCustomerPromotionProgramSelect() {
		return customerPromotionProgramSelect;
	}

	public void setCustomerPromotionProgramSelect(CustomerPromotionProgram customerPromotionProgramSelect) {
		this.customerPromotionProgramSelect = customerPromotionProgramSelect;
	}

	public CustomerPromotionProgram getCustomerPromotionProgramCrud() {
		return customerPromotionProgramCrud;
	}

	public void setCustomerPromotionProgramCrud(CustomerPromotionProgram customerPromotionProgramCrud) {
		this.customerPromotionProgramCrud = customerPromotionProgramCrud;
	}

	public CustomerTypes getCustomerTypesSetting() {
		return customerTypesSetting;
	}

	public void setCustomerTypesSetting(CustomerTypes customerTypesSetting) {
		this.customerTypesSetting = customerTypesSetting;
	}

	public DualListModel<Customer> getModelCustomerSetting() {
		return modelCustomerSetting;
	}

	public void setModelCustomerSetting(DualListModel<Customer> modelCustomerSetting) {
		this.modelCustomerSetting = modelCustomerSetting;
	}

	public PromotionProgram getPromotionProgramSetting() {
		return promotionProgramSetting;
	}

	public void setPromotionProgramSetting(PromotionProgram promotionProgramSetting) {
		this.promotionProgramSetting = promotionProgramSetting;
	}

	public String getTextSearch() {
		return textSearch;
	}

	public void setTextSearch(String textSearch) {
		this.textSearch = textSearch;
	}

	public boolean isRewrite() {
		return rewrite;
	}

	public void setRewrite(boolean rewrite) {
		this.rewrite = rewrite;
	}

	public Date getFromDateFox() {
		return fromDateFox;
	}

	public void setFromDateFox(Date fromDateFox) {
		this.fromDateFox = fromDateFox;
	}

	public Date getToDateFox() {
		return toDateFox;
	}

	public void setToDateFox(Date toDateFox) {
		this.toDateFox = toDateFox;
	}
}