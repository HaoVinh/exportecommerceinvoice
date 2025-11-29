package lixco.com.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;

import lixco.com.commom_ejb.MyMath;
import lixco.com.commom_ejb.MyUtilEJB;
import lixco.com.common.ApiCallClient;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.SessionHelper;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.delivery.ShiftLix;
import lixco.com.entity.Customer;
import lixco.com.entity.GoodsReceiptNoteDetail;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.Product;
import lixco.com.entity.YeuCauKiemTraHang;
import lixco.com.entity.YeuCauKiemTraHangDetail;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IProductService;
import lixco.com.reqInfo.ChiTietDuBaoTon;
import lixco.com.reqInfo.IECategoriesReqInfo;
import lixco.com.reqInfo.Message;
import lixco.com.reqInfo.PalletLixDTO;
import lixco.com.reqInfo.WarehouseReqInfo;
import lixco.com.reqInfo.WrapInvoiceDetailReqInfo;
import lixco.com.service.YeuCauKiemTraHangDetailService;
import lixco.com.service.YeuCauKiemTraHangService;
import lixco.com.service.YeuCauKiemTraHangTHService;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.file.UploadedFile;

import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.api.DefinedName;
import trong.lixco.com.apitaikhoan.AccountData;
import trong.lixco.com.apitaikhoan.AccountDataService;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.AccountDatabase;
import trong.lixco.com.entity.AccountEmailConfirm;
import trong.lixco.com.entity.AccountEmailSend;
import trong.lixco.com.entity.ParamReportDetail;
import trong.lixco.com.info.DataSendMail;
import trong.lixco.com.service.AccountDatabaseService;
import trong.lixco.com.service.AccountEmailConfirmService;
import trong.lixco.com.service.AccountEmailSendService;
import trong.lixco.com.service.MailManagerService;
import trong.lixco.com.service.ParamReportDetailService;
import trong.lixco.com.service.ServerMailLixService;
import trong.lixco.com.util.ConvertNumberToText;
import trong.lixco.com.util.MyMailUtil;
import trong.lixco.com.util.MyStringUtil;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.MyUtilExcel;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.StaticPath;
import trong.lixco.com.util.ToolReport;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Named
@ViewScoped
public class YeuCauKiemTraHangBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private YeuCauKiemTraHangService yeuCauKiemTraHangService;
	@Inject
	private YeuCauKiemTraHangDetailService yeuCauKiemTraHangDetailService;
	@Inject
	private IProductService productService;
	@Inject
	ParamReportDetailService paramReportDetailService;
	@Inject
	private AccountDatabaseService accountDatabaseService;
	@Getter
	@Setter
	private YeuCauKiemTraHang yeuCauKiemTraHangCrud;
	@Getter
	@Setter
	private YeuCauKiemTraHang yeuCauKiemTraHangSelect;
	@Getter
	@Setter
	private List<YeuCauKiemTraHang> yeuCauKiemTraHangs;
	@Getter
	@Setter
	private YeuCauKiemTraHangDetail yeuCauKiemTraHangDetailCrud;
	@Getter
	@Setter
	private List<YeuCauKiemTraHangDetail> yeuCauKiemTraHangDetails;
	@Getter
	@Setter
	private List<YeuCauKiemTraHangDetail> yeuCauKiemTraHangDetailFilters;

	/* search phiếu nhập */
	@Getter
	@Setter
	private Date fromDateSearch;
	@Getter
	@Setter
	private Date toDateSearch;
	@Getter
	@Setter
	private Account account;
	/* Thông tin search */
	@Getter
	@Setter
	private Product productSearch;
	@Getter
	@Setter
	private String batchCodeSearch;
	@Getter
	@Setter
	private FormatHandler formatHandler;

	@Override
	protected void initItem() {
		try {
			formatHandler = FormatHandler.getInstance();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);
			createNew();
			createNewDetail();
			fromDateSearch = ToolTimeCustomer.plusMonthNow(-2);
			search();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addYeuCauKiemTraHangDetail() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (yeuCauKiemTraHangCrud != null && yeuCauKiemTraHangDetailCrud != null) {
				if (allowSave(yeuCauKiemTraHangCrud.getRequestDate())) {
					if (yeuCauKiemTraHangDetailCrud.getProduct() != null) {
						yeuCauKiemTraHangDetailCrud.setMinv(yeuCauKiemTraHangDetails.size() + 1);
						yeuCauKiemTraHangDetailCrud.setQuantity(1);
						yeuCauKiemTraHangDetailCrud.setCreated_by(account.getMember().getName());
						yeuCauKiemTraHangDetailCrud.setCreated_date(new Date());
						yeuCauKiemTraHangDetails.add(yeuCauKiemTraHangDetailCrud.clone());
					} else {
						if (yeuCauKiemTraHangDetailCrud.getProduct() == null) {
							warning("Chưa nhập sản phẩm để thêm vào.");
						}
					}
				} else {
					warning("Tài khoản này không có quyền thực hiện hoặc tháng đã khoá.");
				}
				yeuCauKiemTraHangDetailCrud = new YeuCauKiemTraHangDetail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			yeuCauKiemTraHangDetailCrud = new YeuCauKiemTraHangDetail();
			logger.error("YeuCauKiemTraHangBean.addYeuCauKiemTraHangDetail:" + e.getMessage(), e);
		}
		current.executeScript("PF('wgtablect').clearFilters();");
	}

	public void showDialogTongHop() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('dlgth').show();");
		} catch (Exception e) {
			logger.error("YeuCauKiemTraHangBean.showDialogTongHop:" + e.getMessage(), e);
		}
	}

	public void deleteYeuCauKiemTraHangDetail(YeuCauKiemTraHangDetail f) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			if (f.getYeuCauKiemTraHang() == null) {
				if (f.getId() == 0) {
					int index = -1;
					for (int i = 0; i < yeuCauKiemTraHangDetails.size(); i++) {
						if (yeuCauKiemTraHangDetails.get(i).getMinv() == f.getMinv()) {
							index = i;
							break;
						}
					}
					if (index != -1) {
						yeuCauKiemTraHangDetails.remove(index);
						notify.success();
						current.executeScript("PF('wgtablect').clearFilters();");
					}
				}
			} else {
				if (allowDelete(f.getYeuCauKiemTraHang().getRequestDate())) {
					YeuCauKiemTraHang yeuCauKiemTraHangOld = yeuCauKiemTraHangService.findById(f.getYeuCauKiemTraHang()
							.getId());
					if (yeuCauKiemTraHangOld != null && yeuCauKiemTraHangOld.isDakiemtra()) {
						noticeError("Phiếu đã kiểm tra không xóa được.");
					} else {
						current.executeScript("PF('wgtablect').clearFilters();");
						int code = yeuCauKiemTraHangDetailService.deleteById(f.getId());
						if (code > 0) {
							notify.success();
							yeuCauKiemTraHangDetails.remove(f);
						} else {
							error("Lỗi khi xóa (kiểm tra log server).");
						}
					}
				} else {
					warning("Tài khoản này không có quyền thực hiện hoặc tháng đã khoá.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Getter
	@Setter
	private String stextStr;

	public void search() {
		PrimeFaces current = PrimeFaces.current();
		try {
			yeuCauKiemTraHangs = new ArrayList<>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("stextStr", MyStringUtil.replaceD(stextStr));
			jsonInfo.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			JsonObject json = new JsonObject();
			json.add("ycKiemTraHang", jsonInfo);
			yeuCauKiemTraHangService.search(JsonParserUtil.getGson().toJson(json), yeuCauKiemTraHangs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		current.executeScript("PF('wgtablect').clearFilters();");
	}

	public void onTabChange(TabChangeEvent event) {
		if ("Danh sách phiếu".equals(event.getTab().getTitle())) {
			if (yeuCauKiemTraHangs == null || yeuCauKiemTraHangs.size() == 0) {
				search();
				PrimeFaces.current().ajax().update("menuformid:tabview1:tablesp");
			}
		}

	}

	public void createNew() {
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('wgtablect').clearFilters();");
			yeuCauKiemTraHangCrud = new YeuCauKiemTraHang();
			yeuCauKiemTraHangCrud.setCreatedBy(account.getMember().getName());
			yeuCauKiemTraHangCrud.setRequestDate(new Date());
			installCode();
			yeuCauKiemTraHangDetails = new ArrayList<>();
			yeuCauKiemTraHangDetailCrud = new YeuCauKiemTraHangDetail();
		} catch (Exception e) {
			logger.error("YeuCauKiemTraHangBean.createNew:" + e.getMessage(), e);
		}

	}

	public void installCode() {
		yeuCauKiemTraHangService.initCode(yeuCauKiemTraHangCrud);
	}

	public void createNewDetail() {
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('wgtablect').clearFilters();");
			yeuCauKiemTraHangDetailCrud = new YeuCauKiemTraHangDetail();
		} catch (Exception e) {
			logger.error("YeuCauKiemTraHangBean.createNewDetail:" + e.getMessage(), e);
		}
	}

	public List<Product> completeProduct(String text) {
		try {
			List<Product> list = new ArrayList<Product>();
			productService.complete3(formatHandler.converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("GoogsReceiptNoteBean.completeProduct:" + e.getMessage(), e);
		}
		return null;
	}

	public void deleteYeuCauKiemTraHang() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (allowDelete(yeuCauKiemTraHangSelect.getRequestDate())) {
				current.executeScript("PF('wgtablect').clearFilters();");
				if (yeuCauKiemTraHangSelect != null && yeuCauKiemTraHangSelect.getId() != 0) {
					YeuCauKiemTraHang yc = yeuCauKiemTraHangService.findById(yeuCauKiemTraHangSelect.getId());
					if (yc.isTpxacnhan()) {
						error("Trưởng phòng đã xác nhận, không xóa phiếu được.");
					} else {
						// delete detail
						int code = yeuCauKiemTraHangService.deleteById(yeuCauKiemTraHangSelect.getId());
						if (code >= 0) {
							success();
							yeuCauKiemTraHangs.remove(yeuCauKiemTraHangSelect);
							yeuCauKiemTraHangCrud = new YeuCauKiemTraHang();
							yeuCauKiemTraHangDetails = new ArrayList<YeuCauKiemTraHangDetail>();
						} else {
							error("Lỗi khi xóa (kiểm tra log server).");
						}
					}
				}
			} else {
				warning("Tài khoản này không có quyền thực hiện hoặc tháng đã khoá.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
		}
	}

	public long kiemtrasochungtu(Date ngay, String sochungtu, long id) {
		long socttrung = yeuCauKiemTraHangTHService
				.checkCode(ngay.getMonth() + 1, ngay.getYear() + 1900, sochungtu, id);
		return socttrung;
	}

	@Inject
	YeuCauKiemTraHangTHService yeuCauKiemTraHangTHService;

	public void saveOrUpdate() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (yeuCauKiemTraHangCrud != null) {
				if (kiemtrasochungtu(yeuCauKiemTraHangCrud.getRequestDate(), yeuCauKiemTraHangCrud.getRequestCode(),
						yeuCauKiemTraHangCrud.getId()) != 0) {
					noticeError("Số chứng từ này đã tồn tại trong tháng.");
				} else {
					Message message = new Message();
					// check info
					Date requestDate = yeuCauKiemTraHangCrud.getRequestDate();
					if (yeuCauKiemTraHangCrud.getRequestCode() != null
							&& !"".equals(yeuCauKiemTraHangCrud.getRequestCode()) && requestDate != null) {
						// Kiểm tra điều kiện lưu
						for (int i = 0; i < yeuCauKiemTraHangDetails.size(); i++) {
							if ("".equals(yeuCauKiemTraHangDetails.get(i).getLohang().trim())
									|| "".equals(yeuCauKiemTraHangDetails.get(i).getTinhtrang().trim())
									|| yeuCauKiemTraHangDetails.get(i).getNguongoc() == 0
									|| yeuCauKiemTraHangDetails.get(i).getQuantity() == 0) {
								noticeError("Nhập đủ dữ liệu (số lượng, lô hàng, tình trạng, nguồn gốc) dòng " + i + 1
										+ "(" + yeuCauKiemTraHangDetails.get(i).getProduct().getProduct_code() + ")");
								return;
							}
						}

						if (yeuCauKiemTraHangCrud.getId() == 0) {
							if (allowSave(yeuCauKiemTraHangCrud.getRequestDate())) {
								if (yeuCauKiemTraHangDetails.size() != 0) {
									int code = yeuCauKiemTraHangTHService.saveOrUpdateYeuCauKiemTraHangService(
											yeuCauKiemTraHangCrud, yeuCauKiemTraHangDetails, message);
									switch (code) {
									case -1:
										String m = message.getUser_message() + " \\n" + message.getInternal_message();
										current.executeScript("swaldesignclose('Xảy ra lỗi', '" + m + "','warning');");
										break;
									default:
										success();
										// tải lại
										yeuCauKiemTraHangs.add(0, yeuCauKiemTraHangCrud.clone());
										// load danh sách chi tiết
										yeuCauKiemTraHangDetails = yeuCauKiemTraHangDetailService
												.findByYeuCauKiemTraHang(yeuCauKiemTraHangCrud.getId());
										// Gửi mail TP Kho van xac nhan yeu cau
										 guiMailXacNhan();
										break;
									}
								} else {
									noticeError("Chưa nhập chi tiết yêu cầu.");
								}
							} else {
								current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
							}
						} else {
							YeuCauKiemTraHang YeuCauKiemTraHangOld = yeuCauKiemTraHangService
									.findById(yeuCauKiemTraHangCrud.getId());
							if (YeuCauKiemTraHangOld.isDakiemtra()) {
								noticeError("Phiếu đã kiểm tra không cập nhật được.");
							} else {
								if (YeuCauKiemTraHangOld != null && allowUpdate(YeuCauKiemTraHangOld.getRequestDate())
										&& allowUpdate(yeuCauKiemTraHangCrud.getRequestDate())) {
									int code = yeuCauKiemTraHangTHService.saveOrUpdateYeuCauKiemTraHangService(
											yeuCauKiemTraHangCrud, yeuCauKiemTraHangDetails, message);
									switch (code) {
									case -1:
										String m = message.getUser_message() + " \\n" + message.getInternal_message();
										current.executeScript("swaldesignclose('Xảy ra lỗi', '" + m + "','warning');");
										break;
									default:
										success();
										// guiMailXacNhan();
										// tải lại
										yeuCauKiemTraHangs.set(yeuCauKiemTraHangs.indexOf(yeuCauKiemTraHangCrud),
												yeuCauKiemTraHangCrud.clone());
										// load danh sách chi tiết
										yeuCauKiemTraHangDetails = yeuCauKiemTraHangDetailService
												.findByYeuCauKiemTraHang(yeuCauKiemTraHangCrud.getId());
										break;
									}
								} else {
									current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','warning',2000);");
								}
							}
						}
					} else {
						warning("Nhập đầy đủ thông tin (*)");
					}
				}
			}
		} catch (Exception e) {
			logger.error("GoogsReceiptNoteBean.saveOrUpdateReceiptNote:" + e.getMessage(), e);
		}
		current.executeScript("PF('wgtablect').clearFilters();");
	}

	@Inject
	MailManagerService mailManagerService;
	@Inject
	ServerMailLixService serverMailLixService;
	@Inject
	AccountEmailSendService accountEmailSendService;
	@Inject
	AccountEmailConfirmService accountEmailConfirmService;
	static final String CONFIRM_XACNHANYEUCAU = "/XNYeuCauServlet";

	public void guiMailXacNhan() {
		// 2: TP. xác nhận yêu cầu
		List<AccountEmailSend> accountEmailSends = accountEmailSendService.findTypeAcc(2);
		for (int i = 0; i < accountEmailSends.size(); i++) {
			AccountData accountData = AccountDataService.laytaikhoan(accountEmailSends.get(i).getCodeEmp());
			if (accountData != null) {
				AccountEmailConfirm acc = new AccountEmailConfirm();
				acc.setUid(UUID.randomUUID().toString());
				acc.setUser(accountData.getUserName());
				acc.setPass(accountData.getPassword());
				acc.setCodeEmp(accountData.getMember().getCode());
				acc.setNameEmp(accountData.getMember().getName());
				accountEmailConfirmService.create(acc);
				String linkConfirm = StaticPath.getLinkConfirmEmail() + CONFIRM_XACNHANYEUCAU + "?sessionIdFromEmail="
						+ acc.getUid() + "&idphieu=" + yeuCauKiemTraHangCrud.getId();
				boolean result = MyMailUtil.sendMailConfirmYCKT(yeuCauKiemTraHangCrud, yeuCauKiemTraHangDetails,
						accountEmailSends.get(i).getEmail(), linkConfirm, mailManagerService, serverMailLixService,
						new DataSendMail("", " để xác nhận yêu cầu ", "", ""), null,
						"Xác nhận yêu cầu kiểm tra hàng hóa");
				if (result) {
					info("Đã gửi mail Trưởng phòng xác nhận yêu cầu.");
				} else {
					warning("Gửi mail Trưởng phòng xác nhận yêu cầu không thành công.");
				}
			}
		}
	}

	public void loadYeuCauKiemTraHang() {
		PrimeFaces current = PrimeFaces.current();
		try {
			current.executeScript("PF('wgtablect').clearFilters();");
			if (yeuCauKiemTraHangSelect != null) {
				yeuCauKiemTraHangCrud = yeuCauKiemTraHangSelect.clone();
				yeuCauKiemTraHangDetails = yeuCauKiemTraHangDetailService.findByYeuCauKiemTraHang(yeuCauKiemTraHangCrud
						.getId());
			}
		} catch (Exception e) {
			logger.error("GoogsReceiptNoteBean.loadYeuCauKiemTraHang:" + e.getMessage(), e);
		}
	}

	public void nextOrPrev(int next) {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (yeuCauKiemTraHangs != null && yeuCauKiemTraHangs.size() > 0) {
				int index = yeuCauKiemTraHangs.indexOf(yeuCauKiemTraHangCrud);
				int size = yeuCauKiemTraHangs.size() - 1;
				if (index == -1) {
					yeuCauKiemTraHangSelect = yeuCauKiemTraHangs.get(0);
					yeuCauKiemTraHangCrud = yeuCauKiemTraHangSelect.clone();
				} else {
					switch (next) {
					case 1:
						if (index == size) {
							yeuCauKiemTraHangSelect = yeuCauKiemTraHangs.get(0);
							yeuCauKiemTraHangCrud = yeuCauKiemTraHangSelect.clone();
						} else {
							yeuCauKiemTraHangSelect = yeuCauKiemTraHangs.get(index + 1);
							yeuCauKiemTraHangCrud = yeuCauKiemTraHangSelect.clone();
						}
						break;

					default:
						if (index == 0) {
							yeuCauKiemTraHangSelect = yeuCauKiemTraHangs.get(size);
							yeuCauKiemTraHangCrud = yeuCauKiemTraHangSelect.clone();
						} else {
							yeuCauKiemTraHangSelect = yeuCauKiemTraHangs.get(index - 1);
							yeuCauKiemTraHangCrud = yeuCauKiemTraHangSelect.clone();
						}
						break;
					}
				}
				// load chi tiết phiếu nhập
				yeuCauKiemTraHangDetails = yeuCauKiemTraHangDetailService.findByYeuCauKiemTraHang(yeuCauKiemTraHangCrud
						.getId());
			}
		} catch (Exception e) {
			logger.error("YeuCauKiemTraHangBean.nextOrPrev:" + e.getMessage(), e);
		}
		// clear filter
		current.executeScript("PF('wgtablect').clearFilters();");
	}

	public void inphieu(String loaiin) {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (yeuCauKiemTraHangCrud != null) {
				if ("yeucau".equals(loaiin)) {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/yeucaukiemtra/yeucaukiemtrahang.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);

					List<ParamReportDetail> listConfig = paramReportDetailService
							.findByParamReportName("thongtinchung");
					for (ParamReportDetail p : listConfig) {
						importParam.put(p.getKey(), p.getValue());
					}
					importParam.put("requestDateStr",
							ToolTimeCustomer.convertDateToStringNgayThangNam(yeuCauKiemTraHangCrud.getRequestDate()));
					importParam.put("requestCode", yeuCauKiemTraHangCrud.getRequestCode());
					importParam.put("nhanvientao", yeuCauKiemTraHangCrud.getCreatedBy());
					importParam.put(
							"logo",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/lixco_logo.png"));
					importParam.put(
							"assignKV",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/assign.png"));
					importParam.put("assignKVDate", MyUtil.chuyensangStr(yeuCauKiemTraHangCrud.getTpxacnhanDate()));
					importParam.put(
							"assignQLCL",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/assign.png"));
					importParam.put("assignQLCLDate", MyUtil.chuyensangStr(yeuCauKiemTraHangCrud.getNgaytiepnhan()));

					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JRBeanCollectionDataSource(yeuCauKiemTraHangDetails));
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("utility.printPDF('" + ba + "')");
				} else if ("ketqua".equals(loaiin)) {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/yeucaukiemtra/ketquakiemtrahang.jasper");
					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);

					List<ParamReportDetail> listConfig = paramReportDetailService
							.findByParamReportName("thongtinchung");
					for (ParamReportDetail p : listConfig) {
						importParam.put(p.getKey(), p.getValue());
					}
					importParam.put("requestDateStr", MyUtil.chuyensangStr(yeuCauKiemTraHangCrud.getRequestDate()));
					importParam.put("requestCode", yeuCauKiemTraHangCrud.getRequestCode());
					importParam.put("nhanvienkiemtra", yeuCauKiemTraHangCrud.getCreatedCheck());
					importParam.put("ngaykiemtra",
							ToolTimeCustomer.convertDateToStringNgayThangNam(yeuCauKiemTraHangCrud.getNgayKiemTra()));

					importParam.put(
							"assignQLCLKQ",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/assign.png"));
					importParam.put("assignQLCLKQDate",
							MyUtil.chuyensangStr(yeuCauKiemTraHangCrud.getTpkcsxacnhankqDate()));
					importParam.put(
							"assignBLD",
							FacesContext.getCurrentInstance().getExternalContext()
									.getRealPath("/resources/gfx/assign.png"));
					importParam.put("assignBLDDate", MyUtil.chuyensangStr(yeuCauKiemTraHangCrud.getBldxacnhanDate()));

					// importParam.put("ketquadat",
					// yeuCauKiemTraHangCrud.isKetquadat());
					// importParam.put("giahanluukhodate",MyUtil.chuyensangStr(yeuCauKiemTraHangCrud.getGiaHanLuuKhoDate()));
					// importParam.put("huonggiaiquyet",yeuCauKiemTraHangCrud.getHuongGiaiQuyet());
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JRBeanCollectionDataSource(yeuCauKiemTraHangDetails));
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("utility.printPDF('" + ba + "')");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// clear filter
		current.executeScript("PF('wgtablect').clearFilters();");
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

	public void showDialogUpload() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dlgup1').show();");
	}

	public void exportExcelTongHop() {
		try {
			List<Long> idReceipt = new ArrayList<Long>();
			int size = yeuCauKiemTraHangs.size();
			for (int i = 0; i < size; i++) {
				idReceipt.add(yeuCauKiemTraHangs.get(i).getId());
			}
			List<Object[]> datas = new ArrayList<>();
			yeuCauKiemTraHangService.selectByIdToExcel(idReceipt, datas);

			// xuat file excel.
			if (datas.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "ID", "Số CT", "Ngày", "Nhân viên tạo", "Ghi chú", "Đã kiểm tra", "Ngày kiểm tra",
						"Nhân viên kiểm tra", "IDCT", "Mã SP", "Tên SP", "Số lượng(ĐVT)", "Số lượng (thùng)",
						"Lô hàng", "Nguồn gốc", "Tình trạng", "Kết quả đạt", "Ngày gia hạn", "TC hóa lý vi sinh",
						"Nguyên nhân", "Kết luận" };
				results.add(title);
				for (Object[] it : datas) {
					try {
						results.add(it);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}

				ToolReport.printReportExcelRawXLSX(results, "phieunhapxuatdoibe");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("YeuCauKiemTraHangBean.exportExcelTongHop:" + e.getMessage(), e);
		}
	}

	// Nap chi tiết
	@Getter
	List<String> dulieukhongnapduoc;
	@Getter
	@Setter
	boolean ghilaidulieu = false;

	public void loadExcelNapCT(FileUploadEvent event) {
		try {
			dulieukhongnapduoc = new ArrayList<String>();
			UploadedFile part = event.getFile();
			if (part != null) {
				List<YeuCauKiemTraHangDetail> listDetail = new ArrayList<YeuCauKiemTraHangDetail>();
				Workbook workBook = MyUtilExcel.getWorkbook(new ByteArrayInputStream(part.getContent()),
						part.getFileName());
				Sheet firstSheet = workBook.getSheetAt(0);
				Iterator<Row> rows = firstSheet.iterator();
				while (rows.hasNext()) {
					rows.next();
					rows.remove();
					break;
				}
				Date now = new Date();
				while (rows.hasNext()) {
					Row row = rows.next();
					Iterator<Cell> cells = row.cellIterator();
					String masp = null;
					double soluong = 0;
					String malohang = null;
					String tinhtrang = null;
					int nguongoc = 0;
					while (cells.hasNext()) {
						Cell cell = cells.next();
						int columnIndex = cell.getColumnIndex();

						switch (columnIndex) {
						case 0:// masanpham
							try {
								masp = Objects.toString(MyUtilExcel.getCellValue(cell), "");
							} catch (Exception e) {
							}
							break;
						case 2:// so luong
							try {
								soluong = Double.parseDouble(Objects.toString(MyUtilExcel.getCellValue(cell), "0"));
							} catch (Exception e) {
							}
							break;

						case 3:// malohang
							try {
								malohang = ((long) cell.getNumericCellValue()) + "";
							} catch (Exception e) {
								malohang = Objects.toString(MyUtilExcel.getCellValue(cell), "");
							}
							break;

						case 4:// tinh trang
							try {
								tinhtrang = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
							} catch (Exception e) {
							}
							break;

						case 5:// nguon goc
							try {
								nguongoc = (int) Double.parseDouble(Objects.toString(MyUtilExcel.getCellValue(cell),
										"0"));
							} catch (Exception e) {
							}
							break;
						}
					}
					Product product = productService.selectByCode(masp);
					if (product != null) {
						YeuCauKiemTraHangDetail detail = new YeuCauKiemTraHangDetail();
						detail.setCreated_date(now);
						detail.setCreated_by(getCreateByUser());
						detail.setProduct(product);
						detail.setQuantity(soluong);
						detail.setLohang(malohang);
						detail.setNguongoc(nguongoc);
						detail.setTinhtrang(tinhtrang);
						detail.setNote("Nạp excel.");
						listDetail.add(detail);
					} else {
						noticeError("Không có SP này: " + masp);
					}
				}
				workBook = null;// free
				if (yeuCauKiemTraHangDetails == null)
					yeuCauKiemTraHangDetails = new ArrayList<>();
				if (ghilaidulieu) {
					for (int i = 0; i < yeuCauKiemTraHangDetails.size(); i++) {
						try {
							if (yeuCauKiemTraHangDetails.get(i).getId() != 0) {
								yeuCauKiemTraHangDetailService.deleteById(yeuCauKiemTraHangDetails.get(i).getId());
							}
						} catch (Exception e) {
						}
					}
					yeuCauKiemTraHangDetails.clear();
				}

				for (YeuCauKiemTraHangDetail it : listDetail) {
					yeuCauKiemTraHangDetails.add(it);
				}
				executeScript("PF('wgtablect').clearFilters();");
				updateform("menuformid:tabview1:tablect");
				success();

			} else {
				noticeError("Không đọc được file upload");
			}
		} catch (Exception e) {
			logger.error("invoiceBean.loadExcelThanhToan:" + e.getMessage(), e);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
