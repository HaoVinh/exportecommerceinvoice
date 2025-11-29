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
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
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
import lixco.com.entity.GoodsReceiptNote;
import lixco.com.entity.GoodsReceiptNoteDetail;
import lixco.com.entity.IECategories;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.Product;
import lixco.com.entity.Warehouse;
import lixco.com.entity.XacNhanKiemTra;
import lixco.com.entity.XacNhanKiemTraDetail;
import lixco.com.entity.YeuCauKiemTraHang;
import lixco.com.entity.YeuCauKiemTraHangDetail;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IGoodsReceiptNoteDetailService;
import lixco.com.interfaces.IProductService;
import lixco.com.reqInfo.ChiTietDuBaoTon;
import lixco.com.reqInfo.CustomerPricingProgramReqInfo;
import lixco.com.reqInfo.GoodsReceiptNoteReqInfo;
import lixco.com.reqInfo.IECategoriesReqInfo;
import lixco.com.reqInfo.Message;
import lixco.com.reqInfo.PalletLixDTO;
import lixco.com.reqInfo.ProductLixDTO;
import lixco.com.reqInfo.ShiftDTO;
import lixco.com.reqInfo.TeamLixDTO;
import lixco.com.reqInfo.WarehouseReqInfo;
import lixco.com.reqInfo.WrapInvoiceDetailReqInfo;
import lixco.com.service.GoodsImportBreakDetailService;
import lixco.com.service.XacNhanKiemTraDetailService;
import lixco.com.service.XacNhanKiemTraProccessService;
import lixco.com.service.XacNhanKiemTraService;
import lixco.com.service.YeuCauKiemTraHangDetailService;
import lixco.com.service.YeuCauKiemTraHangService;
import lixco.com.service.YeuCauKiemTraHangTHService;
import lombok.Getter;
import lombok.Setter;
import lombok.var;
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
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.AccountDatabase;
import trong.lixco.com.entity.ParamReportDetail;
import trong.lixco.com.service.AccountDatabaseService;
import trong.lixco.com.service.ParamReportDetailService;
import trong.lixco.com.util.ConvertNumberToText;
import trong.lixco.com.util.MyStringUtil;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.MyUtilExcel;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Named
@ViewScoped
public class XacNhanKiemTraBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	AccountDatabaseService accountDatabaseService;

	@Inject
	IProductService productService;
	@Getter
	@Setter
	Date dateSearchGN;
	@Getter
	List<XacNhanKiemTraDetail> xacNhanKiemTraDetailGNs;

	@Getter
	List<String> dulieukhongnapduoc;
	@Inject
	IGoodsReceiptNoteDetailService goodsReceiptNoteDetailService;
	@Inject
	XacNhanKiemTraService xacNhanKiemTraService;
	@Inject
	XacNhanKiemTraProccessService xacNhanKiemTraProccessService;
	@Inject
	XacNhanKiemTraDetailService xacNhanKiemTraDetailService;
	@Getter
	List<XacNhanKiemTra> xacNhanKiemTras;
	@Getter
	@Setter
	XacNhanKiemTra xacNhanKiemTra;
	@Getter
	@Setter
	XacNhanKiemTra xacNhanKiemTraSelect;

	@Getter
	@Setter
	XacNhanKiemTraDetail xacNhanKiemTraDetailCrud;
	@Getter
	@Setter
	List<XacNhanKiemTraDetail> xacNhanKiemTraDetails;

	/* search phiếu */
	@Getter
	@Setter
	private String stextStr;
	@Getter
	@Setter
	private Date fromDateSearch;
	@Getter
	@Setter
	private Date toDateSearch;

	@Override
	protected void initItem() {
		dateSearchGN = ToolTimeCustomer.plusDayNow(-8);
		fromDateSearch = ToolTimeCustomer.plusDayNow(-15);
		search();
	}

	public void search() {
		PrimeFaces current = PrimeFaces.current();
		try {
			xacNhanKiemTras = new ArrayList<>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("stextStr", MyStringUtil.replaceD(stextStr));
			jsonInfo.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			JsonObject json = new JsonObject();
			json.add("xacnhankiemtra", jsonInfo);
			xacNhanKiemTraService.search(JsonParserUtil.getGson().toJson(json), xacNhanKiemTras);
		} catch (Exception e) {
			e.printStackTrace();
		}
		current.executeScript("PF('tablect').clearFilters();");
	}

	public void searchGN() {
		PrimeFaces current = PrimeFaces.current();
		try {
			xacNhanKiemTraDetailGNs = new ArrayList<XacNhanKiemTraDetail>();
			dulieukhongnapduoc = new ArrayList<String>();
			AccountDatabase accountDatabase = accountDatabaseService.findByName("giaonhan");
			if (accountDatabase != null) {
				Gson gson = JsonParserUtil.getGson();
				// Goi ham dong bo BD
				List<String> params = new ArrayList<String>();
				params.add("date");
				List<String> values = new ArrayList<String>();
				values.add(MyUtil.chuyensangStr(dateSearchGN));
				Call call = trong.lixco.com.api.CallAPI.getInstance("Bearer " + accountDatabase.getToken())
						.getMethodGet(accountDatabase.getAddress() + "data/palletlix/palletByDate", params, values);
				Response response = call.execute();
				if (response.isSuccessful()) {
					String data = response.body().string();
					JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
					if (jsonObject != null) {
						PalletLixDTO[] arrDetail = gson.fromJson(jsonObject.get("data"), PalletLixDTO[].class);
						List<PalletLixDTO> palletLixDTOs = new ArrayList<>(Arrays.asList(arrDetail));
						// Nhom san pham
						Map<ProductLixDTO, Map<Integer, Map<ShiftDTO, List<PalletLixDTO>>>> palletLixDTONhomSPs = palletLixDTOs
								.stream().collect(
										Collectors.groupingBy(
												PalletLixDTO::getProductLixDTO,
												Collectors.groupingBy(PalletLixDTO::getStatusPallet,
														Collectors.groupingBy(PalletLixDTO::getShiftDTO))));

						for (Entry<ProductLixDTO, Map<Integer, Map<ShiftDTO, List<PalletLixDTO>>>> entry1 : palletLixDTONhomSPs.entrySet()) {
							// System.out.print("ProductLixDTO: " +
							// entry1.getKey().getCode());
							for (Entry<Integer, Map<ShiftDTO, List<PalletLixDTO>>> entry2 : entry1.getValue().entrySet()) {
								// System.out.print("\tStatusPallet: " +
								// entry2.getKey());
								double soluongca1 = 0;
								double soluongca2 = 0.0;
								double soluongca3 = 0.0;
								List<PalletLixDTO> palletLixDTOSubEntry2s = new ArrayList<PalletLixDTO>();
								for (Entry<ShiftDTO, List<PalletLixDTO>> entry3 : entry2.getValue().entrySet()) {
									List<PalletLixDTO> palletLixDTOSubs = entry3.getValue();
									palletLixDTOSubEntry2s.addAll(palletLixDTOSubs);
									switch (entry3.getKey().getShift_num()) {
									case 1:
										soluongca1 = palletLixDTOSubs.stream().mapToDouble(PalletLixDTO::getNumPallet)
												.sum();
										break;
									case 2:
										soluongca2 = palletLixDTOSubs.stream().mapToDouble(PalletLixDTO::getNumPallet)
												.sum();
										break;
									case 3:
										soluongca3 = palletLixDTOSubs.stream().mapToDouble(PalletLixDTO::getNumPallet)
												.sum();
										break;
									}
								}

								XacNhanKiemTraDetail xn = new XacNhanKiemTraDetail();
								Product product = productService.selectByCode(entry1.getKey().getCode());
								if (product != null) {
									xn.setProduct(product);
									xn.setQuantityCa1(soluongca1);
									xn.setQuantityCa2(soluongca2);
									xn.setQuantityCa3(soluongca3);
									double quantityTTSP = goodsReceiptNoteDetailService.quantityImpFromPX(dateSearchGN,
											product.getId());
									xn.setQuantityTTSP(product.getSpecification() != 0 ? quantityTTSP
											/ product.getSpecification() : quantityTTSP);
									xn.setChenhlech(Math.abs((xn.getQuantityCa1() + xn.getQuantityCa2() + xn
											.getQuantityCa3()) - xn.getQuantityTTSP()));
									String dataPallet = gson.toJson(palletLixDTOSubEntry2s);
									xn.setDataPallet(dataPallet);
									xn.setTrangthaicl(entry2.getKey());
									xacNhanKiemTraDetailGNs.add(xn);
								} else {
									dulieukhongnapduoc.add("Không có mã sản phẩm trong TTSP: "
											+ entry1.getKey().getCode());
								}

								// System.out.print("\t" + soluongca1 + "\t" +
								// soluongca2 + "\t" + soluongca3 + "\t"
								// + palletLixDTOSubEntry2s);
							}
							// System.out.println("");
						}

					}
				} else {
					noticeError("Xảy ra lỗi " + response.toString());
				}
				showDialog("dlggiaonhan");
				if (dulieukhongnapduoc.size() != 0) {
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Thông báo lỗi", dulieukhongnapduoc
									.toString()));
					updateform("formpalletgiaonhan:messNotCode");
				}
				response.close();
			} else {
				noticeError("Chưa cài đặt tài khoản liên kết đến hệ thống giao nhận. ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			noticeError("Liên kết, xử lý số liệu giao nhận lỗi " + e.getMessage());
		}
	}

	public void taoPhieuYeuCauXacNhan(boolean kiemtradaco) {
		try {
			boolean process = true;
			if (kiemtradaco) {
				boolean dataInvalable = xacNhanKiemTraService.checkDataIsAvailable(dateSearchGN);
				if (dataInvalable) {
					showDialog("dlthongbaodl");
					process = false;
				}
			}
			if (process) {
				if (allowSave(dateSearchGN) && allowUpdate(dateSearchGN, getCreateByUser())) {
					if (xacNhanKiemTraDetailGNs.size() != 0) {
						dulieukhongnapduoc = new ArrayList<String>();
						Map<Integer, List<XacNhanKiemTraDetail>> palletLixDTONhomSPs = xacNhanKiemTraDetailGNs.stream()
								.collect(Collectors.groupingBy(XacNhanKiemTraDetail::getTrangthaicl));
						for (Entry<Integer, List<XacNhanKiemTraDetail>> entry1 : palletLixDTONhomSPs.entrySet()) {
							List<XacNhanKiemTraDetail> xacNhanKiemTraDetailNhoms = entry1.getValue();
							XacNhanKiemTra xn = new XacNhanKiemTra();
							xn.setCreatedBy(getCreateByUser());
							xn.setCreatedDate(new Date());
							xn.setTrangthaicl(entry1.getKey());
							xn.setRequestDate(dateSearchGN);
							xn.setXacNhanKiemTraDetails(xacNhanKiemTraDetailNhoms);
							Message message = new Message();
							int code = xacNhanKiemTraProccessService.saveOrUpdate(xn, message);
							switch (code) {
							case -1:
								String trangthai = "";
								switch (entry1.getKey()) {
								case 0:
									trangthai = "Chưa kiểm";
									break;
								case 1:
									trangthai = "Đạt";
									break;
								case 2:
									trangthai = "Chờ kiểm";
									break;
								case 3:
									trangthai = "Không đạt";
									break;
								}

								dulieukhongnapduoc.add("Không lưu được phiếu: '" + trangthai + "' Lỗi: "
										+ message.getUser_message());
								break;
							}
						}
					} else {
						notice("Không có dữ liệu.");
					}
					if (dulieukhongnapduoc.size() == 0) {
						success();
						search();
					} else {
						noticeError(dulieukhongnapduoc.toString());
					}
				} else {
					noticeError("Tài khoản này không có quyền thực hiện hoặc tháng đã khoá.");
				}
			}
		} catch (Exception e) {
			noticeError("Xảy ra lỗi: " + e.getMessage());
		}

	}

	public void showEdit() {
		try {
			if (xacNhanKiemTraSelect != null) {
				xacNhanKiemTra = xacNhanKiemTraSelect.clone();
				xacNhanKiemTraDetails = xacNhanKiemTraDetailService.findByXacNhanKiemTra(xacNhanKiemTraSelect.getId());
				executeScript("PF('wgtablect').clearFilters();");
			}
		} catch (Exception e) {
			logger.error("GoogsReceiptNoteBean.loadGoodsReceiptNote:" + e.getMessage(), e);
		}
	}

	public void delete() {
		try {
			if (allowDelete(xacNhanKiemTraSelect.getRequestDate(), xacNhanKiemTraSelect.getCreatedBy())) {
				if (xacNhanKiemTraSelect != null && xacNhanKiemTraSelect.getId() != 0) {
					XacNhanKiemTra xnOld = xacNhanKiemTraService.findById(xacNhanKiemTraSelect.getId());
					if (xnOld != null) {
						if (!xnOld.isDakiemtra()) {
							int code = xacNhanKiemTraService.deleteById(xacNhanKiemTraSelect.getId());
							if (code >= 0) {
								success();
								xacNhanKiemTras.remove(xacNhanKiemTraSelect);
								xacNhanKiemTra = new XacNhanKiemTra();
							} else {
								noticeError("Xảy ra lỗi, kiểm tra log server.");
							}
						} else {
							noticeError("Phiếu đã xác nhận, không xóa được.");
						}
					} else {
						noticeError("Phiếu đã bị xóa.");
					}
				} else {
					noticeError("Tải lại form, chọn lại phiếu để xóa.");
				}
			} else {
				noticeError("Tài khoản này không có quyền thực hiện hoặc tháng đã khoá.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveOrUpdateDetail(XacNhanKiemTraDetail item) {
		try {
			if (xacNhanKiemTraDetailCrud != null && xacNhanKiemTra != null) {
				xacNhanKiemTraDetailCrud.setXacNhanKiemTra(xacNhanKiemTra);

				if (xacNhanKiemTraDetailCrud.getId() == 0) {
					if (allowSave(xacNhanKiemTra.getRequestDate())) {
						xacNhanKiemTraDetailCrud.setCreated_by(getAccount().getMember().getName());
						xacNhanKiemTraDetailCrud.setCreated_date(new Date());
						int ck = xacNhanKiemTraDetailService.insert(xacNhanKiemTraDetailCrud);
						if (ck == 0) {
							xacNhanKiemTraDetails.add(0, xacNhanKiemTraDetailCrud);
							executeScript("PF('wgtablect').clearFilters();");
							success("Thành công!");
						} else {
							error("Không lưu được. kiểm tra log server");
						}
					} else {
						warning("Tài khoản này không có quyền thực hiện hoặc tháng đã khoá");
					}
				} else {
					if (allowUpdate(new Date())) {
						xacNhanKiemTraDetailCrud.setLast_modifed_by(getAccount().getMember().getName());
						xacNhanKiemTraDetailCrud.setLast_modifed_date(new Date());
						int ck = xacNhanKiemTraDetailService.update(xacNhanKiemTraDetailCrud);
						if (ck == 0) {
							xacNhanKiemTraDetails.set(xacNhanKiemTraDetails.indexOf(xacNhanKiemTraDetailCrud),
									xacNhanKiemTraDetailCrud);
							executeScript("PF('wgtablect').clearFilters();");
							success("Cập nhật thành công!");
						} else {
							error("Không lưu được. kiểm tra log server");
						}
					} else {
						warning("Tài khoản này không có quyền thực hiện hoặc tháng đã khoá");
					}
				}

			}
		} catch (Exception e) {
			logger.error("PricingProgramBean.saveOrUpdateCustomerPricingProgramSet:" + e.getMessage(), e);
		}
	}

	public void editDetail(XacNhanKiemTraDetail item) {
		this.xacNhanKiemTraDetailCrud = item;
		showDialog("dlchitiet");
	}
	public void resetDetail() {
		this.xacNhanKiemTraDetailCrud = new XacNhanKiemTraDetail();
	}

	public void deleteDetail(XacNhanKiemTraDetail item) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			
				if (allowDelete(item.getXacNhanKiemTra().getRequestDate())) {
					XacNhanKiemTraDetail xacNhanKiemTraDetail = xacNhanKiemTraDetailService.findById(item
							.getId());
					if ( xacNhanKiemTraDetail.getXacNhanKiemTra().isDakiemtra()) {
						noticeError("Phiếu đã xác nhận không xóa được.");
					} else {
						executeScript("PF('wgtablect').clearFilters();");
						int code = xacNhanKiemTraDetailService.deleteById(xacNhanKiemTraDetail.getId());
						if (code > 0) {
							notify.success();
							xacNhanKiemTraDetails.remove(xacNhanKiemTraDetail);
						} else {
							error("Lỗi khi xóa (kiểm tra log server).");
						}
					}
				} else {
					warning("Tài khoản này không có quyền thực hiện hoặc tháng đã khoá.");
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
