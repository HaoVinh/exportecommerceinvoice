package lixco.com.bean;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.lang.reflect.Type;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import lixco.com.commom_ejb.MyMath;
import lixco.com.commom_ejb.PagingInfo;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.NavigationInfo;
import lixco.com.common.SessionHelper;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Car;
import lixco.com.entity.CarOwner;
import lixco.com.entity.CarType;
import lixco.com.entity.CotHienThi;
import lixco.com.entity.Customer;
import lixco.com.entity.CustomerTypes;
import lixco.com.entity.DeliveryPricing;
import lixco.com.entity.FreightContract;
import lixco.com.entity.HeThong;
import lixco.com.entity.IECategories;
import lixco.com.entity.Invoice;
import lixco.com.entity.OrderDetail;
import lixco.com.entity.OrderLix;
import lixco.com.entity.PaymentMethod;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.PricingProgramDetail;
import lixco.com.entity.Product;
import lixco.com.entity.PromotionOrderDetail;
import lixco.com.entity.PromotionProgram;
import lixco.com.entity.PromotionProgramDetail;
import lixco.com.entity.TransportPricingNew;
import lixco.com.entity.Warehouse;
import lixco.com.hddt.LoaiMaXuatNhap;
import lixco.com.interfaces.IBatchService;
import lixco.com.interfaces.IBindUserService;
import lixco.com.interfaces.ICarOwnerService;
import lixco.com.interfaces.ICarService;
import lixco.com.interfaces.ICarTypeService;
import lixco.com.interfaces.ICustomerPricingProgramService;
import lixco.com.interfaces.ICustomerPromotionProgramService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.ICustomerTypesService;
import lixco.com.interfaces.IDeliveryPricingService;
import lixco.com.interfaces.IFreightContractService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IInvoiceService;
import lixco.com.interfaces.IOrderDetailService;
import lixco.com.interfaces.IOrderLixService;
import lixco.com.interfaces.IPaymentMethodService;
import lixco.com.interfaces.IPricingProgramDetailService;
import lixco.com.interfaces.IPricingProgramService;
import lixco.com.interfaces.IProcessLogicInvoiceService;
import lixco.com.interfaces.IProcessLogicOrderService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IPromotionOrderDetailService;
import lixco.com.interfaces.IPromotionProgramDetailService;
import lixco.com.interfaces.IPromotionProgramService;
import lixco.com.interfaces.IPromotionalPricingService;
import lixco.com.interfaces.IWarehouseService;
import lixco.com.reqInfo.CarReqInfo;
import lixco.com.reqInfo.CustomerPricingProgramReqInfo;
import lixco.com.reqInfo.CustomerPromotionProgramReqInfo;
import lixco.com.reqInfo.DeliveryPricingReqInfo;
import lixco.com.reqInfo.Message;
import lixco.com.reqInfo.OrderDetailReqInfo;
import lixco.com.reqInfo.OrderLixReqInfo;
import lixco.com.reqInfo.PricingProgramDetailReqInfo;
import lixco.com.reqInfo.ProductReqInfo;
import lixco.com.reqInfo.PromotionOrderDetailReqInfo;
import lixco.com.reqInfo.PromotionalPricingReqInfo;
import lixco.com.reqInfo.WrapExtensionOrderReqInfo;
import lixco.com.reqInfo.WrapOrderDetailLixReqInfo;
import lixco.com.reqInfo.WrapOrderLixReqInfo;
import lixco.com.reqInfo.WrapPMOrderDetailReqInfo;
import lixco.com.service.CotHienThiService;
import lixco.com.service.HeThongService;
import lixco.com.service.TonKhoThucTeService;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.columntoggler.ColumnToggler;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.data.SortEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.Visibility;
import org.primefaces.model.file.UploadedFile;

import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.ParamReportDetail;
import trong.lixco.com.info.CallAPIData;
import trong.lixco.com.info.DateJsonDeserializer;
import trong.lixco.com.info.DateJsonSerializer;
import trong.lixco.com.info.DonHangNPPAPI;
import trong.lixco.com.info.DonHangNPPAPIChiTiet;
import trong.lixco.com.info.DonHangNPPAPIExcel;
import trong.lixco.com.info.SanPhamDonHang;
import trong.lixco.com.service.AccountDatabaseService;
import trong.lixco.com.service.ParamReportDetailService;
import trong.lixco.com.util.CallAPI;
import trong.lixco.com.util.MyStringUtil;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.MyUtilExcel;
import trong.lixco.com.util.MyUtilPDF;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Named
@ViewScoped
public class OrderLixBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IOrderLixService orderLixService;
	@Inject
	private IProductService productService;
	@Inject
	private ICustomerService customerService;
	@Inject
	private ICustomerTypesService customerTypesService;
	@Inject
	private IPaymentMethodService paymentMethodService;
	@Inject
	private IWarehouseService warehouseService;
	@Inject
	private IPromotionProgramService promotionProgramService;
	@Inject
	private IPricingProgramService pricingProgramService;
	@Inject
	private ICustomerPromotionProgramService customerPromotionProgramService;
	@Inject
	private ICustomerPricingProgramService customerPricingProgramService;
	@Inject
	private IPromotionProgramDetailService promotionProgramDetailService;
	@Inject
	private IPricingProgramDetailService pricingProgramDetailService;
	@Inject
	private ICarService carService;
	@Inject
	private IFreightContractService freightContractService;
	@Inject
	private IDeliveryPricingService deliveryPricingService;
	@Inject
	private IOrderDetailService orderDetailService;
	@Inject
	private IPromotionOrderDetailService promotionOrderDetailService;
	@Inject
	private IPromotionalPricingService promotionalPricingService;
	@Inject
	private IBatchService batchService;
	@Inject
	private IProcessLogicInvoiceService processLogicInvoiceService;// tonghopkhoxuly.jas
	@Inject
	private IProcessLogicOrderService processLogicOrderService;
	@Inject
	private ICarTypeService carTypeService;
	@Inject
	private ICarOwnerService carOwnerService;
	@Inject
	private IBindUserService bindUserService;
	@Inject
	private AccountDatabaseService accountDatabaseService;
	@Inject
	private IInvoiceService invoiceService;
	private List<PaymentMethod> listPaymentMethod;
	private List<Warehouse> listWarehouse;
	private List<CustomerTypes> listCustomerTypes;
	private OrderLix orderLixCrud;
	private boolean changePricingProgram = false;
	private boolean changePromotionProgram = false;
	private TransportPricingNew transportPricingNewCrud;
	private OrderLix orderLixSelect;
	private List<OrderLix> listOrderLix;
	private OrderDetail orderDetailCrud;
	private OrderDetail orderDetailPick;
	private List<PromotionProgramDetail> listPromotionProgramDetailCrud;
	private OrderDetail orderDetailSelect;
	private Date fromDateFCFilter;
	private Date toDateFCFilter;
	private CustomerTypes customerTypesFCFilter;
	private Customer customerFcFilter;
	private String contractNoFCFilter;
	private List<FreightContract> listFreightContractSelect;
	private int pageSizeHDVC;
	private NavigationInfo navigationInfoHDVC;
	private List<Integer> listRowPerPageHDVC;
	/* search orderlix */
	private Date fromDateSearch;
	private Date toDateSearch;
	@Getter
	@Setter
	private Product productSearch;
	private Customer customerSearch;
	private String orderCodeSearch;
	private String voucherCodeSearch;
	private IECategories iECategoriesSearch;
	@Getter
	@Setter
	private String brandSearch = "TAT CA";
	private String poNoSearch;
	private int deliveredSearch;
	private List<Integer> statusSearch;

	/* search chi tiết */
	private Account account;
	private FormatHandler formatHandler;
	private boolean tabOrder;
	/* hóa đơn */
	private List<WrapOrderDetailLixReqInfo> listWrapOrderDetailLixReqInfo;
	private List<PromotionOrderDetail> listPromotionOrderDetailInvoice;
	private boolean allex;
	/* xe */
	private Car carCrud;
	private Car carSelect;
	private List<Car> listCar;
	private List<CarType> listCarType;
	private List<CarOwner> listCarOwner;
	private CarType carTypeSearch;
	private CarOwner carOwnerSearch;
	private String licensePlateSearch;
	private String phoneNumberSearch;
	/* nơi giao hàng */
	private List<DeliveryPricing> listDeliveryPricing;
	/* order new */
	private List<OrderDetail> listOrderDetail;
	private List<OrderDetail> listOrderDetailFilter;
	private List<PromotionOrderDetail> listPromotionOrderDetail;
	private List<PromotionOrderDetail> listPromotionOrderDetailFilter;

	@Getter
	@Setter
	Date psDate;
	@Getter
	@Setter
	Date peDate;
	@Getter
	List<DonHangNPPAPI> donHangNPPAPIs;
	@Setter
	@Getter
	List<DonHangNPPAPI> donHangNPPAPIFilters;
	@Getter
	List<DonHangNPPAPIChiTiet> donHangNPPAPIChiTiets;
	@Getter
	@Setter
	DonHangNPPAPI donHangNPPAPISelect;
	@Getter
	@Setter
	boolean chonhetDMS = false;

	@Inject
	CotHienThiService cotHienThiService;
	Gson gson;
	@Getter
	List<Boolean> cotdsdonhang = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true,
			true, false, false, false, false, true, false);
	final String TENBANG = "DSDONHANG";

	public void onToggle(ToggleEvent e) {
		cotdsdonhang.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
		String jsondatacot = gson.toJson(cotdsdonhang);
		cotHienThiService.saveOrUpdate(jsondatacot, TENBANG, getAccount().getUserName());
	}

	public void chonHetPhieuDMS() {
		if (donHangNPPAPIs != null)
			for (int i = 0; i < donHangNPPAPIs.size(); i++) {
				donHangNPPAPIs.get(i).setChonphieu(chonhetDMS);
			}
	}

	public void taidonhangDMS() {
		try {
			if (psDate == null) {
				noticeError("Chưa nhập ngày để lọc ");
			} else {
				donHangNPPAPIs = new ArrayList<DonHangNPPAPI>();
				donHangNPPAPIFilters = new ArrayList<DonHangNPPAPI>();
				String data = CallAPI.dsdonHangDMS("tkphianam", psDate, peDate);
				GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
				gsonBuilder.registerTypeAdapter(Date.class, new DateJsonSerializer());
				gsonBuilder.registerTypeAdapter(Date.class, new DateJsonDeserializer());
				Gson gson = gsonBuilder.create();
				CallAPIData callAPIData = gson.fromJson(data, CallAPIData.class);
				if ("0".equals(callAPIData.getErr())) {
					if (callAPIData.getDt() != null) {
						final DonHangNPPAPI[] dsarray = gson.fromJson(callAPIData.getDt(), DonHangNPPAPI[].class);
						donHangNPPAPIs = Arrays.asList(dsarray);
					}
				} else {
					noticeError(callAPIData.getMsg());
				}
				PrimeFaces current = PrimeFaces.current();
				current.executeScript("PF('tbdonhangdms').clearFilters();");
			}
		} catch (Exception e) {
			noticeError("Xảy ra lỗi kết nối đến DMS " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public void selectDonHang() {
		donHangNPPAPIChiTiets = donHangNPPAPISelect.getDonHangNPPAPIChiTiets();
	}

	@Getter
	List<String> dulieukhongnapduoc;

	public void napvaodonhang() {
		dulieukhongnapduoc = new ArrayList<String>();
		boolean chonphieu = false;
		List<DonHangNPPAPI> donHangNPPAPIs = new ArrayList<DonHangNPPAPI>();
		if (donHangNPPAPIFilters != null && donHangNPPAPIFilters.size() != 0) {
			donHangNPPAPIs.addAll(donHangNPPAPIFilters);
		} else {
			donHangNPPAPIs.addAll(this.donHangNPPAPIs);
		}
		if (donHangNPPAPIs != null && donHangNPPAPIs.size() != 0) {
			for (int i = 0; i < donHangNPPAPIs.size(); i++) {
				if (donHangNPPAPIs.get(i).isChonphieu()) {
					chonphieu = true;
					OrderLix orOld = orderLixService.selectByNppIdOrder(donHangNPPAPIs.get(i).getIddonhang());
					boolean status = true;
					if (orOld != null) {
						if (orOld.getCar() != null || orOld.isDelivered())
							status = false;
					}
					if (status) {
						if (orOld != null) {
							if (allowDelete(null)) {
								Message message = new Message();
								int code = 0;
								try {
									code = processLogicOrderService.deleteOrderLix(orOld.getId(), message);
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (code != 0) {
									dulieukhongnapduoc.add("Không nạp được ĐH -> SoDH: "
											+ donHangNPPAPIs.get(i).getMadonhang() + "(không xóa được đơn cũ)"
											+ message.getInternal_message());
									return;
								}
							} else {
								dulieukhongnapduoc.add("Không nạp được ĐH -> SoDH: "
										+ donHangNPPAPIs.get(i).getMadonhang() + ": Tài khoản không có quyền nạp lại");
							}
						}
						String makh = donHangNPPAPIs.get(i).getMakhachhang();
						if (donHangNPPAPIs.get(i).getManppttsp() != null
								&& !"".equals(donHangNPPAPIs.get(i).getManppttsp()))
							makh = donHangNPPAPIs.get(i).getManppttsp();
						Customer customer = customerService.selectByCode(makh);
						if (customer != null) {
							OrderLix orderLix = new OrderLix();
							orderLix.setNpp_order_id(donHangNPPAPIs.get(i).getIddonhang());
							orderLix.setCustomer(customer);
							orderLix.setBrand(getDatabaseDH());
							orderLix.setCreated_date(new Date());
							orderLix.setCreated_by_id(account.getMember().getId());

							orderLix.setVoucher_code("W" + donHangNPPAPIs.get(i).getMadonhang());
							orderLix.setOrderDateCreate(MyUtil.loaibogio(donHangNPPAPIs.get(i).getNgaydathang()));
							orderLix.setOrder_date(MyUtil.loaibogio(donHangNPPAPIs.get(i).getNgaygiaohang()));
							orderLix.setDelivery_date(donHangNPPAPIs.get(i).getNgaygiaohang());
							orderLix.setCreated_by(account.getMember().getName());
							orderLix.setWarehouse(warehouseService.selectByCode("F"));// F:kho
																						// thanh
																						// pham
							orderLix.setNote(donHangNPPAPIs.get(i).getGhichu());
							orderLix.setPayment_method(paymentMethodService.selectByCode("CK"));
							orderLix.setTax_value(0.1);
							String maxn = "";
							if ("BL972".equals(makh)) {
								// HORECA (KÊNH NPP)
								maxn = "R";
							} else if ("CO453".equals(makh)) {
								// ONLINE (KÊNH NPP)(CTY TNHH THƯƠNG MẠI
								// PHÁT TRIỂN
								// NHÃ
								// UYÊN)
								maxn = "$";
							} else {
								maxn = "X";
							}
							orderLix.setIe_categories(ieCategoriesService.selectByCode(maxn));

							caidatdongiakhuyenmai(orderLix);

							// tìm địa điểm đơn giá giao hàng
							JsonObject json1 = new JsonObject();
							json1.addProperty("customer_id", orderLix.getCustomer().getId());
							json1.addProperty("disable", 0);
							List<DeliveryPricing> list = new ArrayList<>();
							deliveryPricingService.seletcBy(JsonParserUtil.getGson().toJson(json1), list);
							// nạp địa điểm giao hàng và đơn giá giao hàng
							if (list.size() > 0) {
								orderLix.setDelivery_pricing(list.get(0));
							} else {
								orderLix.setDelivery_pricing(null);
							}
							OrderLixReqInfo ot = new OrderLixReqInfo();
							ot.setOrder_lix(orderLix);
							int chk = orderLixService.insert(ot);
							if (chk == 0) {
								// Xac nhan don hang
								xacnhandonhangDMS(orderLix.getNpp_order_id(), orderLix.getVoucher_code());
								List<DonHangNPPAPIChiTiet> chiTiets = donHangNPPAPIs.get(i).getDonHangNPPAPIChiTiets();
								for (int j = 0; j < chiTiets.size(); j++) {
									OrderDetail orderDetail = new OrderDetail();
									orderDetail.setOrder_lix(ot.getOrder_lix());
									// masp
									Product product = productService.selectByCode(chiTiets.get(j).getMaspchinh());
									if (product != null) {
										orderDetail.setProduct(product);
										double thung = chiTiets.get(j).getSoluongthung();
										// double soluong =
										// MyMath.roundCustom(thung
										// *
										// orderDetail.getProduct().getSpecification(),
										// 2);
										// orderDetail.setQuantity(soluong);
										orderDetail.setBox_quantity(thung);
										// orderDetail.setBox_quantity_actual(thung);

										// cai dat don gia
										orderDetail.setUnit_price(0);
										orderDetail.setUnit_price_goc(0);
										if (orderLix.getPricing_program() != null) {
											boolean statusPrice = true;
											PricingProgramDetailReqInfo t = new PricingProgramDetailReqInfo();
											long idPriceSub = customerPricingProgramService.selectForCustomerSub(
													orderLix.getPricing_program().getId(), orderLix.getOrder_date(),
													product.getId());
											if (idPriceSub != 0) {

												pricingProgramDetailService.findSettingPricing(idPriceSub,
														product.getId(), t);

												if (t.getPricing_program_detail() != null) {
													if (t.getPricing_program_detail().getUnit_price() != 0) {
														orderDetail.setUnit_price(t.getPricing_program_detail()
																.getUnit_price());
														orderDetail.setUnit_price_goc(t.getPricing_program_detail()
																.getUnit_price());
														statusPrice = false;
													}
												}
											}
											if (statusPrice) {
												pricingProgramDetailService.findSettingPricing(orderLix
														.getPricing_program().getId(), product.getId(), t);
												if (t.getPricing_program_detail() != null) {
													if (t.getPricing_program_detail().getUnit_price() != 0) {
														orderDetail.setUnit_price(t.getPricing_program_detail()
																.getUnit_price());
														orderDetail.setUnit_price_goc(t.getPricing_program_detail()
																.getUnit_price());
													}
												}
											}
										}
										orderDetail.setPromotion_forms(chiTiets.get(j).getLoaikm());
										// sua lai 22/06/2023
										double soluong = MyMath.roundCustom(orderDetail.getBox_quantity()
												* orderDetail.getProduct().getSpecification(), 2);

										orderDetail.setTotal(MyMath.round(soluong * orderDetail.getUnit_price()));
										OrderDetailReqInfo p = new OrderDetailReqInfo(orderDetail);
										if (orderDetailService.insert(p) == 0) {
											if (orderLix.getPromotion_program() != null) {
												if (orderDetail.getPromotion_forms() != 0) {
													// Cai dat khuyen mai
													JsonObject js = new JsonObject();
													js.addProperty("product_id", product.getId());
													js.addProperty("promotion_program_id", orderLix
															.getPromotion_program().getId());
													js.addProperty("promotion_form", orderDetail.getPromotion_forms());
													List<PromotionProgramDetail> programDetails = new ArrayList<>();
													promotionProgramDetailService.selectBy(JsonParserUtil.getGson()
															.toJson(js), programDetails);
													for (PromotionProgramDetail ppd : programDetails) {
														PromotionOrderDetail pod = new PromotionOrderDetail();
														pod.setOrder_detail(orderDetail);
														pod.setProduct(ppd.getPromotion_product());
														// tim nạp đơn giá sản
														// phẩm
														// khuyến mãi
														PromotionalPricingReqInfo ppr = new PromotionalPricingReqInfo();
														JsonObject json = new JsonObject();
														json.addProperty(
																"date",
																ToolTimeCustomer.convertDateToString(pod
																		.getOrder_detail().getOrder_lix()
																		.getOrder_date(), "dd/MM/yyyy"));
														json.addProperty("product_id", ppd.getPromotion_product()
																.getId());
														promotionalPricingService.findSettingPromotionalPricing(
																JsonParserUtil.getGson().toJson(json), ppr);
														if (ppr.getPromotional_pricing() != null) {
															pod.setUnit_price(ppr.getPromotional_pricing()
																	.getUnit_price());
														}
														int quantity = (int) BigDecimal
																.valueOf(orderDetail.getBox_quantity())
																.divide(BigDecimal.valueOf(ppd.getBox_quatity()),
																		MathContext.DECIMAL32).doubleValue();
														pod.setQuantity(quantity * ppd.getSpecification());
														pod.setCreated_date(new Date());
														pod.setCreated_by(account.getMember().getName());
														if (pod.getQuantity() != 0) {
															promotionOrderDetailService
																	.insert(new PromotionOrderDetailReqInfo(pod));
														}
													}
												}
											} else {
												if (orderDetail.getPromotion_forms() != 0)
													dulieukhongnapduoc
															.add("\tKhông nạp được khuyến mãi (không có ct KM) -> SP: "
																	+ orderDetail.getProduct().getProduct_code());
											}
										} else {
											dulieukhongnapduoc.add("\tKhông nạp được ĐHCT -> SP: "
													+ orderDetail.getProduct().getProduct_code());
										}
									} else {
										dulieukhongnapduoc.add("\tKhông tìm thấy mã SP -> SP: "
												+ chiTiets.get(j).getMaspchinh());
									}
								}
							} else {
								dulieukhongnapduoc.add("Không nạp được ĐH  -> SoDH: " + orderLix.getVoucher_code());

							}
						} else {
							dulieukhongnapduoc.add("Không nạp được ĐH -> SoDH: " + donHangNPPAPIs.get(i).getMadonhang()
									+ ": không tìm thấy mã KH " + makh);
						}
					} else {
						dulieukhongnapduoc.add("Không nạp được ĐH -> SoDH: " + donHangNPPAPIs.get(i).getMadonhang()
								+ ": Đã giao hàng hoặc đã có số xe");
					}
				}
			}
			if (chonphieu == false) {
				noticeError("Chưa chọn phiếu");
			} else {
				search();
				taidonhangDMS();
				if (dulieukhongnapduoc.size() == 0) {
					success();
				} else {
					showDialogErrorinfor();
				}
				PrimeFaces current = PrimeFaces.current();
				current.executeScript("PF('tbdonhangdms').clearFilters();");
			}

		} else {
			noticeError("Chưa tải phiếu");
		}
	}

	public void boxacnhanDMS(DonHangNPPAPI donHangNPPAPI) {
		dulieukhongnapduoc = new ArrayList<String>();
		try {
			// Xac nhan don hang
			String data = CallAPI.boxacnhandonHangDMS(donHangNPPAPI.getIddonhang());
			GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
			gsonBuilder.registerTypeAdapter(Date.class, new DateJsonSerializer());
			gsonBuilder.registerTypeAdapter(Date.class, new DateJsonDeserializer());
			Gson gson = gsonBuilder.create();
			CallAPIData callAPIData = gson.fromJson(data, CallAPIData.class);
			if ("0".equals(callAPIData.getErr())) {
				search();
				taidonhangDMS();
				PrimeFaces current = PrimeFaces.current();
				current.executeScript("PF('tbdonhangdms').clearFilters();");
			} else {
				dulieukhongnapduoc.add("Khong bo xac nhan duoc DH -> SoDH: " + donHangNPPAPI.getMadonhang() + " loi: "
						+ callAPIData.getMsg());
			}

		} catch (Exception e) {
			e.getStackTrace();
			dulieukhongnapduoc.add("Xảy ra lỗi: " + e.getMessage());
		}
		if (dulieukhongnapduoc.size() == 0) {
			success();
		} else {
			showDialogErrorinfor();
		}

	}

	public void napvaodonhangExcel(List<DonHangNPPAPI> donHangNPPAPIs) {
		dulieukhongnapduoc = new ArrayList<String>();
		if (donHangNPPAPIs != null && donHangNPPAPIs.size() != 0) {
			for (int i = 0; i < donHangNPPAPIs.size(); i++) {
				OrderLix orOld = null;
				boolean status = true;
				if (donHangNPPAPIs.get(i).getIddonhang() != 0) {
					orOld = orderLixService.selectByNppIdOrder(donHangNPPAPIs.get(i).getIddonhang());
					if (orOld != null) {
						if (orOld.getCar() != null || orOld.isDelivered())
							status = false;
					}
				}
				if (status) {
					if (orOld != null) {
						if (allowDelete(null)) {
							Message message = new Message();
							int code = 0;
							try {
								code = processLogicOrderService.deleteOrderLix(orOld.getId(), message);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (code != 0) {
								dulieukhongnapduoc.add("Không nạp được ĐH -> SoDH: "
										+ donHangNPPAPIs.get(i).getMadonhang() + " (không xóa được đơn cũ)"
										+ message.getInternal_message());
								return;
							}
						} else {
							dulieukhongnapduoc.add("Không nạp được ĐH -> SoDH: " + donHangNPPAPIs.get(i).getMadonhang()
									+ ": Tài khoản không có quyền nạp lại");
						}
					}
					String makh = donHangNPPAPIs.get(i).getMakhachhang();
					if (donHangNPPAPIs.get(i).getManppttsp() != null
							&& !"".equals(donHangNPPAPIs.get(i).getManppttsp()))
						makh = donHangNPPAPIs.get(i).getManppttsp();

					Customer customer = customerService.selectByCode(makh);
					if (customer != null) {
						OrderLix orderLix = new OrderLix();
						orderLix.setNpp_order_id(donHangNPPAPIs.get(i).getIddonhang());
						orderLix.setCustomer(customer);

						orderLix.setCreated_date(new Date());
						orderLix.setCreated_by_id(account.getMember().getId());

						orderLix.setVoucher_code("W" + donHangNPPAPIs.get(i).getMadonhang());
						orderLix.setOrder_date(MyUtil.loaibogio(donHangNPPAPIs.get(i).getNgaydathang()));
						orderLix.setDelivery_date(donHangNPPAPIs.get(i).getNgaygiaohang());
						orderLix.setCreated_by(account.getMember().getName());
						orderLix.setWarehouse(warehouseService.selectByCode("F"));// F:kho
																					// thanh
																					// pham
						orderLix.setPayment_method(paymentMethodService.selectByCode("CK"));
						orderLix.setTax_value(0.1);
						String maxn = "";
						if ("BL972".equals(makh)) {
							// HORECA (KÊNH NPP)
							maxn = "R";
						} else if ("CO453".equals(makh)) {
							// ONLINE (KÊNH NPP)(CTY TNHH THƯƠNG MẠI
							// PHÁT TRIỂN
							// NHÃ
							// UYÊN)
							maxn = "$";
						} else {
							maxn = "X";
						}
						orderLix.setIe_categories(ieCategoriesService.selectByCode(maxn));

						caidatdongiakhuyenmai(orderLix);

						// tìm địa điểm đơn giá giao hàng
						JsonObject json1 = new JsonObject();
						json1.addProperty("customer_id", orderLix.getCustomer().getId());
						json1.addProperty("disable", 0);
						List<DeliveryPricing> list = new ArrayList<>();
						deliveryPricingService.seletcBy(JsonParserUtil.getGson().toJson(json1), list);
						// nạp địa điểm giao hàng và đơn giá giao hàng
						if (list.size() > 0) {
							orderLix.setDelivery_pricing(list.get(0));
						} else {
							orderLix.setDelivery_pricing(null);
						}
						OrderLixReqInfo ot = new OrderLixReqInfo();
						ot.setOrder_lix(orderLix);
						int chk = orderLixService.insert(ot);
						if (chk == 0) {
							// Xac nhan don hang
							List<DonHangNPPAPIChiTiet> chiTiets = donHangNPPAPIs.get(i).getDonHangNPPAPIChiTiets();
							for (int j = 0; j < chiTiets.size(); j++) {
								OrderDetail orderDetail = new OrderDetail();
								orderDetail.setOrder_lix(ot.getOrder_lix());
								// masp
								Product product = productService.selectByCode(chiTiets.get(j).getMaspchinh());
								if (product != null) {
									orderDetail.setProduct(product);
									double thung = chiTiets.get(j).getSoluongthung();
									// double soluong = MyMath.roundCustom(thung
									// *
									// orderDetail.getProduct().getSpecification(),
									// 2);
									// orderDetail.setQuantity(soluong);
									orderDetail.setBox_quantity(thung);
									// orderDetail.setBox_quantity_actual(thung);

									// cai dat don gia
									orderDetail.setUnit_price(0);
									orderDetail.setUnit_price_goc(0);
									if (orderLix.getPricing_program() != null) {
										boolean statusPrice = true;
										PricingProgramDetailReqInfo t = new PricingProgramDetailReqInfo();
										long idPriceSub = customerPricingProgramService.selectForCustomerSub(orderLix
												.getPricing_program().getId(), orderLix.getOrder_date(), product
												.getId());
										if (idPriceSub != 0) {

											pricingProgramDetailService.findSettingPricing(idPriceSub, product.getId(),
													t);

											if (t.getPricing_program_detail() != null) {
												if (t.getPricing_program_detail().getUnit_price() != 0) {
													orderDetail.setUnit_price(t.getPricing_program_detail()
															.getUnit_price());
													orderDetail.setUnit_price_goc(t.getPricing_program_detail()
															.getUnit_price());
													statusPrice = false;
												}
											}
										}
										if (statusPrice) {
											pricingProgramDetailService.findSettingPricing(orderLix
													.getPricing_program().getId(), product.getId(), t);
											if (t.getPricing_program_detail() != null) {
												if (t.getPricing_program_detail().getUnit_price() != 0) {
													orderDetail.setUnit_price(t.getPricing_program_detail()
															.getUnit_price());
												}
											}
										}
									}
									orderDetail.setPromotion_forms(chiTiets.get(j).getLoaikm());
									// sua lai 22/06/2023
									double soluong = MyMath.roundCustom(orderDetail.getBox_quantity()
											* orderDetail.getProduct().getSpecification(), 2);
									orderDetail.setTotal(MyMath.round(soluong * orderDetail.getUnit_price()));
									OrderDetailReqInfo p = new OrderDetailReqInfo(orderDetail);
									if (orderDetailService.insert(p) == 0) {
										if (orderLix.getPromotion_program() != null) {
											if (orderDetail.getPromotion_forms() != 0) {
												// Cai dat khuyen mai
												JsonObject js = new JsonObject();
												js.addProperty("product_id", product.getId());
												js.addProperty("promotion_program_id", orderLix.getPromotion_program()
														.getId());
												js.addProperty("promotion_form", orderDetail.getPromotion_forms());
												List<PromotionProgramDetail> programDetails = new ArrayList<>();
												promotionProgramDetailService.selectBy(
														JsonParserUtil.getGson().toJson(js), programDetails);
												if (programDetails.size() != 0) {
													for (PromotionProgramDetail ppd : programDetails) {
														PromotionOrderDetail pod = new PromotionOrderDetail();
														pod.setOrder_detail(orderDetail);
														pod.setProduct(ppd.getPromotion_product());
														// tim nạp đơn giá sản
														// phẩm
														// khuyến
														// mãi
														// double quantity =
														// BigDecimal
														// .valueOf(orderDetail.getBox_quantity()
														// .multiply(
														// BigDecimal.valueOf(ppd.getPromotion_quantity()))
														// .divide(BigDecimal.valueOf(ppd.getBox_quatity()),
														// MathContext.DECIMAL32).doubleValue();
														// pod.setQuantity((int)
														// quantity);
														int quantity = (int) BigDecimal
																.valueOf(orderDetail.getBox_quantity())
																.divide(BigDecimal.valueOf(ppd.getBox_quatity()),
																		MathContext.DECIMAL32).doubleValue();
														pod.setQuantity(quantity * ppd.getSpecification());
														pod.setCreated_date(new Date());
														pod.setCreated_by(account.getMember().getName());
														if (pod.getQuantity() != 0) {
															promotionOrderDetailService
																	.insert(new PromotionOrderDetailReqInfo(pod));
														}
													}
												} else {
													dulieukhongnapduoc.add("\tKhông có hình thức KM "
															+ orderDetail.getPromotion_forms() + " -> SP: "
															+ orderDetail.getProduct().getProduct_code());
												}
											}
										} else {
											if (orderDetail.getPromotion_forms() != 0)
												dulieukhongnapduoc.add("\tKhông nạp được ĐHCTKM -> SP: "
														+ orderDetail.getProduct().getProduct_code());
										}
									} else {
										dulieukhongnapduoc.add("\tKhông nạp được ĐHCT -> SP: "
												+ orderDetail.getProduct().getProduct_code());
									}
								} else {
									dulieukhongnapduoc.add("\tKhông tìm thấy mã SP -> SP: "
											+ chiTiets.get(j).getMaspchinh());
								}
							}
						} else {
							dulieukhongnapduoc.add("Không nạp được ĐH  -> SoDH: " + orderLix.getVoucher_code());

						}
					} else {
						dulieukhongnapduoc.add("Không nạp được ĐH -> SoDH: " + donHangNPPAPIs.get(i).getMadonhang()
								+ ": không tìm thấy mã KH " + makh);
					}
				} else {
					dulieukhongnapduoc.add("Không nạp được ĐH -> SoDH: " + donHangNPPAPIs.get(i).getMadonhang()
							+ ": Đã giao hàng hoặc đã có số xe");
				}
			}
			search();
			if (dulieukhongnapduoc.size() == 0) {
				success();
			} else {
				showDialogErrorinfor();
			}
		} else {
			notice("Chưa chọn phiếu");
		}
	}

	public void loadFileExcelDMS(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			if (allowUpdate(null)) {
				if (event.getFile() != null) {
					UploadedFile part = event.getFile();
					byte[] byteFile = event.getFile().getContent();
					List<DonHangNPPAPIExcel> donHangNPPAPIExcels = new ArrayList<>();
					Workbook workBook = MyUtilExcel.getWorkbook(new ByteArrayInputStream(byteFile), part.getFileName());
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
						DonHangNPPAPIExcel lix = new DonHangNPPAPIExcel();
						while (cells.hasNext()) {
							Cell cell = cells.next();
							int columnIndex = cell.getColumnIndex();
							switch (columnIndex) {
							case 0:
								try {
									// masp
									String masp = Objects.toString(MyUtilExcel.getCellValue(cell), null);
									try {
										double maspIsNumber = Double.parseDouble(masp);
										masp=((int) maspIsNumber)+"";
									} catch (Exception e) {
									}
									lix.setMaspchinh(masp);
									break;
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;
							case 1:
								try {
									// tensp
									String tensp = Objects.toString(MyUtilExcel.getCellValue(cell), null);
									lix.setTenspchinh(tensp);
									break;
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;
							case 2:
								try {
									// số lượng thùng
									String slThung = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
									double thung = Double.parseDouble(slThung);
									lix.setSoluongthung(thung);
									break;
								} catch (Exception e) {
								}
								break;
							case 3:
								String htkm = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								int km = Integer.parseInt(htkm);
								if (km > 0) {
									lix.setLoaikm(km);
								}
								break;
							case 4:
								String madonhang = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								lix.setMadonhang(madonhang);
								break;
							case 5:
								String ngaydathang = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								if (ngaydathang != null && !"".equals(ngaydathang)) {
									Date orderDate = ToolTimeCustomer.convertStringToDate(ngaydathang, "dd/MM/yyyy");
									lix.setNgaydathang(orderDate);
								}
								break;
							case 6:
								String ngaygiaohang = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								if (ngaygiaohang != null && !"".equals(ngaygiaohang)) {
									Date orderDate = ToolTimeCustomer.convertStringToDate(ngaygiaohang, "dd/MM/yyyy");
									lix.setNgaygiaohang(orderDate);
								}
								break;
							case 7:
								String makhachhang = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								lix.setMakhachhang(makhachhang);
								break;
							case 9:
								String ghichu = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								lix.setGhichu(ghichu);
								break;
							case 12:
								String iddonhangStr = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								long iddonhang = Long.parseLong(iddonhangStr);
								lix.setIddonhang(iddonhang);
								break;
							case 13:
								String manoivc = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								lix.setManoivc(manoivc);
								break;
							case 14:
								String manppttsp = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								lix.setManppttsp(manppttsp);
								break;
							}

						}
						donHangNPPAPIExcels.add(lix);
					}
					List<DonHangNPPAPI> donHangNPPAPIs = new ArrayList<DonHangNPPAPI>();
					Map<Long, List<DonHangNPPAPIExcel>> datagroups1 = donHangNPPAPIExcels.stream().collect(
							Collectors.groupingBy(p -> p.getIddonhang(), Collectors.toList()));
					for (long key : datagroups1.keySet()) {
						List<DonHangNPPAPIExcel> invs = datagroups1.get(key);
						DonHangNPPAPI dh = new DonHangNPPAPI(invs.get(0).getMadonhang(), invs.get(0).getNgaydathang(),
								invs.get(0).getNgaygiaohang(), invs.get(0).getMakhachhang(),
								invs.get(0).getIddonhang(), invs.get(0).getGhichu(), invs.get(0).getManoivc(), invs
										.get(0).getManppttsp());
						dh.setChinhanh(getDatabaseDH());
						List<DonHangNPPAPIChiTiet> chitiets = new ArrayList<DonHangNPPAPIChiTiet>();
						for (DonHangNPPAPIExcel dhct : invs) {
							DonHangNPPAPIChiTiet ct = new DonHangNPPAPIChiTiet(dhct.getMaspchinh(),
									dhct.getTenspchinh(), dhct.getSoluongthung(), dhct.getLoaikm());
							chitiets.add(ct);
						}
						dh.setDonHangNPPAPIChiTiets(chitiets);
						donHangNPPAPIs.add(dh);
					}
					napvaodonhangExcel(donHangNPPAPIs);
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.loadFileExcel:" + e.getMessage(), e);
		}
	}

	public void xacnhandonhangDMS(long iddonhang, String madonhang) {
		try {
			String data = CallAPI.xacnhandonHangDMS(iddonhang);
			GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
			gsonBuilder.registerTypeAdapter(Date.class, new DateJsonSerializer());
			gsonBuilder.registerTypeAdapter(Date.class, new DateJsonDeserializer());
			Gson gson = gsonBuilder.create();
			CallAPIData callAPIData = gson.fromJson(data, CallAPIData.class);
			if ("0".equals(callAPIData.getErr())) {

			} else {
				dulieukhongnapduoc
						.add("Khong xac nhan duoc DH -> SoDH: " + madonhang + " loi: " + callAPIData.getMsg());

			}
		} catch (Exception e) {
			noticeError("Xảy ra lỗi kết nối đến DMS " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	private void caidatdongiakhuyenmai(OrderLix orderLixCrud) {
		PrimeFaces current = PrimeFaces.current();
		try {
			// reset flag thay đổi đơn giá và khuyến mãi
			changePricingProgram = false;
			changePromotionProgram = false;
			if (orderLixCrud != null && orderLixCrud.getCustomer() != null && orderLixCrud.getOrder_date() != null) {
				// Tai chuong trinh don gia
				JsonObject json = new JsonObject();
				json.addProperty("date",
						ToolTimeCustomer.convertDateToString(orderLixCrud.getOrder_date(), "dd/MM/yyyy"));
				json.addProperty("customer_id", orderLixCrud.getCustomer().getId());
				CustomerPricingProgramReqInfo t = new CustomerPricingProgramReqInfo();
				customerPricingProgramService.selectForCustomer(JsonParserUtil.getGson().toJson(json), t);
				PricingProgram pricingProgramTrans = t.getCustomer_pricing_program() == null ? null : t
						.getCustomer_pricing_program().getPricing_program();

				// Tai chuong trinh khuyen mai
				CustomerPromotionProgramReqInfo t1 = new CustomerPromotionProgramReqInfo();
				customerPromotionProgramService.selectForCustomer(JsonParserUtil.getGson().toJson(json), t1);
				PromotionProgram promotionProgramTrans = t1.getCustomer_promotion_program() == null ? null : t1
						.getCustomer_promotion_program().getPromotion_program();

				orderLixCrud.setPricing_program(pricingProgramTrans);
				orderLixCrud.setPromotion_program(promotionProgramTrans);
			} else {
				orderLixCrud.setPricing_program(null);
				orderLixCrud.setPromotion_program(null);
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.changeDeliveryDate:" + e.getMessage(), e);
		}
	}

	@Override
	protected void initItem() {
		try {
			gson = new Gson();
			CotHienThi cothienthi = cotHienThiService.find(TENBANG, getAccount().getUserName());
			if (cothienthi != null) {
				Type type = new TypeToken<List<Boolean>>() {
				}.getType();
				cotdsdonhang = gson.fromJson(cothienthi.getCothienthi(), type);
			}
			formatHandler = FormatHandler.getInstance();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);

			listPaymentMethod = new ArrayList<>();
			paymentMethodService.selectAll(listPaymentMethod);
			listWarehouse = new ArrayList<>();
			warehouseService.selectAll(listWarehouse);
			listCustomerTypes = new ArrayList<>();
			customerTypesService.selectAll(listCustomerTypes);

			fromDateSearch = ToolTimeCustomer.plusDayNow(-15);
			psDate = ToolTimeCustomer.plusDayNow(-3);
			statusSearch = new ArrayList<Integer>();
			deliveredSearch = -1;
			statusSearch.add(-1);
			search();
			createdNew();
			tabOrder = true;
			listWrapOrderDetailLixReqInfo = new ArrayList<>();
			listPromotionOrderDetailInvoice = new ArrayList<>();

			// orderLixSelect = orderLixService.selectById(118182);
			// showEditOrderLix();

		} catch (Exception e) {
			logger.error("OrderLixBean.initItem:" + e.getMessage(), e);
		}
	}

	public void tinhlaikhuyenmaisoluong(boolean capnhatsokhuyenmai) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			// kiểm tra hóa đơn có tồn tại không
			if (orderLixCrud != null && orderLixCrud.getId() != 0) {
				listOrderDetail = new ArrayList<>();
				orderDetailService.selectByOrder(orderLixCrud.getId(), listOrderDetail);
				listPromotionOrderDetail = new ArrayList<>();
				promotionOrderDetailService.selectByOrder(orderLixCrud.getId(), listPromotionOrderDetail);
				sumOrderLix();

				// // show thông tin sản phẩm khuyến mãi của đơn hàng

				//
				// List<Object[]> slhoadons = new ArrayList<>();
				// orderLixService.getListExportExtensionOrder(orderLixCrud.getId(),
				// slhoadons);
				// boolean status = true;
				// for (int i = 0; i < listOrderDetail.size(); i++) {
				// double slhdthung = 0;
				// double slhddvt = 0;
				//
				// for (Object[] v : slhoadons) {
				// long productId = Long.parseLong(Objects.toString(v[0]));
				// if (listOrderDetail.get(i).getProduct().getId() == productId)
				// {
				// slhdthung = Double.parseDouble(Objects.toString(v[1], "0"));
				// slhddvt = Double.parseDouble(Objects.toString(v[3], "0"));
				// }
				// }
				// listOrderDetail.get(i).setBox_quantity_actual(slhdthung);
				// listOrderDetail.get(i).setQuantity(slhddvt);
				// // sua lai 22/06/2023
				// // listOrderDetail.get(i)
				// // .setTotal(
				// // MyMath.round(listOrderDetail.get(i).getQuantity()
				// // * listOrderDetail.get(i).getUnit_price()));
				//
				// orderDetailService.updateBoxQuantityActual(listOrderDetail.get(i));
				//
				// if (listOrderDetail.get(i).getBox_quantity() !=
				// listOrderDetail.get(i).getBox_quantity_actual()) {
				// status = false;
				// }
				// }
				// // kiem tra, cap nhat trang thai da giao hang
				// if (status) {
				// orderLixCrud.setDelivered(true);
				// OrderLixReqInfo orderLixReqInfo = new
				// OrderLixReqInfo(orderLixCrud);
				// orderLixService.update(orderLixReqInfo);
				// orderLixCrud = orderLixReqInfo.getOrder_lix();
				// }
				if (capnhatsokhuyenmai) {
					List<Object[]> slhoadonKMs = new ArrayList<>();
					orderLixService.getListExportExtensionOrderKM(orderLixCrud.getId(), slhoadonKMs);
					for (int i = 0; i < listPromotionOrderDetail.size(); i++) {
						double soluongAct = 0;
						for (Object[] v : slhoadonKMs) {
							long productId = Long.parseLong(Objects.toString(v[0]));
							if (listPromotionOrderDetail.get(i).getOrder_detail().getProduct().getProduct_code()
									.equals(Objects.toString(v[2]).toString())
									&& listPromotionOrderDetail.get(i).getProduct().getId() == productId) {
								soluongAct += Double.parseDouble(Objects.toString(v[1], "0"));

							}
						}
						listPromotionOrderDetail.get(i).setQuantityAct(soluongAct);
						promotionOrderDetailService.updateQuantityAc(listPromotionOrderDetail.get(i));

					}
				}
				PrimeFaces current = PrimeFaces.current();
				current.executeScript("PF('tablect').clearFilters();");
				current.executeScript("PF('tablekm').clearFilters();");
			} else {
				notify.warning("Đơn hàng chưa lưu không tạo hóa đơn được");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.tabInvoiceOnClick:" + e.getMessage(), e);
		}
	}

	public void capnhatSLThucXuat() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			// kiểm tra hóa đơn có tồn tại không
			if (orderLixCrud != null && orderLixCrud.getId() != 0) {
				listOrderDetail = new ArrayList<>();
				orderDetailService.selectByOrder(orderLixCrud.getId(), listOrderDetail);
				listPromotionOrderDetail = new ArrayList<>();
				promotionOrderDetailService.selectByOrder(orderLixCrud.getId(), listPromotionOrderDetail);

				// show thông tin sản phẩm khuyến mãi của đơn hàng

				List<Object[]> slhoadons = new ArrayList<>();
				orderLixService.getListExportExtensionOrder(orderLixCrud.getId(), slhoadons);
				boolean status = true;
				for (int i = 0; i < listOrderDetail.size(); i++) {
					double slhdthung = 0;
					double slhddvt = 0;

					for (Object[] v : slhoadons) {
						long productId = Long.parseLong(Objects.toString(v[0]));
						if (listOrderDetail.get(i).getProduct().getId() == productId) {
							slhdthung = Double.parseDouble(Objects.toString(v[1], "0"));
							slhddvt = Double.parseDouble(Objects.toString(v[3], "0"));
						}
					}
					listOrderDetail.get(i).setBox_quantity_actual(slhdthung);
					listOrderDetail.get(i).setQuantity(slhddvt);
					orderDetailService.updateBoxQuantityActual(listOrderDetail.get(i));

					if (listOrderDetail.get(i).getBox_quantity() != listOrderDetail.get(i).getBox_quantity_actual()) {
						status = false;
					}
				}
				// kiem tra, cap nhat trang thai da giao hang
				if (status) {
					orderLixCrud.setDelivered(true);
					OrderLixReqInfo orderLixReqInfo = new OrderLixReqInfo(orderLixCrud);
					orderLixService.update(orderLixReqInfo);
					orderLixCrud = orderLixReqInfo.getOrder_lix();
				}
				List<Object[]> slhoadonKMs = new ArrayList<>();
				orderLixService.getListExportExtensionOrderKM(orderLixCrud.getId(), slhoadonKMs);
				for (int i = 0; i < listPromotionOrderDetail.size(); i++) {
					double soluongAct = 0;
					for (Object[] v : slhoadonKMs) {
						long productId = Long.parseLong(Objects.toString(v[0]));
						if (listPromotionOrderDetail.get(i).getOrder_detail().getProduct().getProduct_code()
								.equals(Objects.toString(v[2]).toString())
								&& listPromotionOrderDetail.get(i).getProduct().getId() == productId) {
							soluongAct += Double.parseDouble(Objects.toString(v[1], "0"));

						}
					}
					listPromotionOrderDetail.get(i).setQuantityAct(soluongAct);
					promotionOrderDetailService.updateQuantityAc(listPromotionOrderDetail.get(i));

				}
				PrimeFaces current = PrimeFaces.current();
				current.executeScript("PF('tablect').clearFilters();");
				current.executeScript("PF('tablekm').clearFilters();");
			} else {
				notify.warning("Đơn hàng chưa lưu không tạo hóa đơn được");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.tabInvoiceOnClick:" + e.getMessage(), e);
		}
	}

	public void viewInventory() {
		try {
			if (orderLixCrud != null) {
				List<Long> listProductId = new ArrayList<>();
				for (OrderDetail w : listOrderDetail) {
					listProductId.add(w.getProduct().getId());
				}
				for (PromotionOrderDetail p : listPromotionOrderDetail) {
					listProductId.add(p.getProduct().getId());
				}
				List<Object[]> list = new ArrayList<>();
				orderLixService.getListInventory(listProductId, orderLixCrud.getOrder_date(), list);
				if (list.size() > 0) {
					Map<Long, Double[]> map = new LinkedHashMap<Long, Double[]>();
					for (Object[] p : list) {
						Double[] arr = { Double.parseDouble(Objects.toString(p[1], "0")),
								Double.parseDouble(Objects.toString(p[2], "0")) };
						map.put(Long.parseLong(Objects.toString(p[0])), arr);
					}
					for (OrderDetail w : listOrderDetail) {
						long id = w.getProduct().getId();
						if (map.containsKey(id)) {
							w.setInv_quantity(map.get(id)[0]);
							w.setInv_quantity_cal(map.get(id)[1]);
						}
					}
					for (PromotionOrderDetail p : listPromotionOrderDetail) {
						long id = p.getProduct().getId();
						if (map.containsKey(id)) {
							p.setInv_quantity(map.get(id)[0]);
							p.setInv_quantity_cal(map.get(id)[1]);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.viewInventory:" + e.getMessage(), e);
		}
	}

	public void viewRealExportQuatity() {
		try {
			if (orderLixCrud != null && orderLixCrud.getId() != 0) {
				List<Object[]> list = new ArrayList<>();
				orderLixService.getListRealExportQuantity(orderLixCrud.getId(), list);
				if (list.size() > 0) {
					Map<String, Double[]> map = new LinkedHashMap<>();
					for (Object[] p : list) {
						String key = Objects.toString(p[0]);
						key += "-" + Objects.toString(p[1]);
						if (!map.containsKey(key)) {
							Double[] arr = { Double.parseDouble(Objects.toString(p[2], "0")),
									Double.parseDouble(Objects.toString(p[3], "0")) };
							map.put(key, arr);
						}

					}
					for (OrderDetail d : listOrderDetail) {
						String key = d.getProduct().getId() + "-false";
						if (map.containsKey(key)) {
							d.setBox_quantity_actual(map.get(key)[1]);
						}
					}
					for (PromotionOrderDetail p : listPromotionOrderDetail) {
						String key = p.getProduct().getId() + "-true";
						if (map.containsKey(key)) {
							p.setReal_quantity(map.get(key)[0]);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.viewRealExportQuatity:" + e.getMessage(), e);
		}
	}

	public void deleteOrderLix() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (orderLixSelect != null) {
				if (allowDelete(null)) {
					Message message = new Message();
					int code = processLogicOrderService.deleteOrderLix(orderLixSelect.getId(), message);
					if (code == 0) {
						success();
						listOrderLix.remove(orderLixSelect);
						createdNew();
					} else {
						// đưa ra mã lỗi
						String m = message.getUser_message() + " \\n" + message.getInternal_message();
						current.executeScript("swaldesignclose('Xảy ra lỗi', '" + m + "','warning');");
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Chưa chọn đơn hàng để xóa','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.deleteOrderLix:" + e.getMessage(), e);
		}

	}

	public void createNewCar() {
		carCrud = new Car();
	}

	public void saveOrUpdateCar() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (carCrud != null) {
				String licensePlate = carCrud.getLicense_plate();
				String driver = carCrud.getDriver();
				if (licensePlate != null && !"".equals(licensePlate) && driver != null && !"".equals(driver)
						&& carCrud.getCar_type() != null && carCrud.getCar_owner() != null) {
					CarReqInfo t = new CarReqInfo(carCrud);
					if (carCrud.getId() == 0) {
						// check code đã tồn tại chưa
						if (allowSave(null)) {
							carCrud.setCreated_date(new Date());
							carCrud.setCreated_by(account.getMember().getName());
							if (carService.insert(t) != -1) {
								notify.success("Thêm thành công");
								Car clone = carCrud.clone();
								orderLixCrud.setCar(clone);
								listCar.add(0, carCrud.clone());
								createNewCar();
							} else {
								notify.warning("Thêm thất bại");
							}

						} else {
							notify.warning("Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!");
						}
					} else {
						// check code update đã tồn tại chưa
						if (allowUpdate(null)) {
							carCrud.setLast_modifed_by(account.getMember().getName());
							carCrud.setLast_modifed_date(new Date());
							if (carService.update(t) != -1) {
								notify.success("Cập nhật thành công");
								listCar.set(listCar.indexOf(carCrud), t.getCar());
							} else {
								notify.warning("Cập nhật thất bại!");
							}
						} else {
							notify.warning("Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!");
						}
					}
				} else {
					notify.warning("Thông tin không đầy đủ, điền đủ thông tin chứa(*)");
				}
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.saveOrUpdateCar:" + e.getMessage(), e);
		}
	}

	public void showEditCar() {
		try {
			if (carSelect != null) {
				Car clone = carSelect.clone();
				orderLixCrud.setCar(clone);
				carCrud = clone;
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.showEditCar:" + e.getMessage(), e);
		}
	}

	public void searchCar() {
		try {
			listCar = new ArrayList<Car>();
			/*
			 * { car_info:{car_owner_id:0,car_type_id:0,license_plate:
			 * '',phone_number:''}, page:{page_index:0, page_size:0}}
			 */
			PagingInfo page = new PagingInfo();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("car_owner_id", carOwnerSearch == null ? 0 : carOwnerSearch.getId());
			jsonInfo.addProperty("car_type_id", carTypeSearch == null ? 0 : carTypeSearch.getId());
			jsonInfo.addProperty("license_plate", licensePlateSearch);
			jsonInfo.addProperty("phone_number", phoneNumberSearch);
			JsonObject json = new JsonObject();
			json.add("car_info", jsonInfo);
		} catch (Exception e) {
			logger.error("OrderLixBean.searchCar:" + e.getMessage(), e);
		}
	}

	public void tabOrderOnClick() {
		this.tabOrder = true;
		// listOrderDetailFilter=null;
		// listPromotionOrderDetailFilter=null;
	}

	public void tabInvoiceOnClick() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			khoiluongxuat = 0;
			listWrapOrderDetailLixReqInfo = new ArrayList<>();
			listPromotionOrderDetailInvoice = new ArrayList<>();
			// kiểm tra hóa đơn có tồn tại không
			if (orderLixCrud != null && orderLixCrud.getId() != 0) {

				allex = false;
				this.tabOrder = false;
				List<Long> listProductId = new ArrayList<>();
				Map<Long, WrapOrderDetailLixReqInfo> mapEx = new LinkedHashMap<>();
				for (OrderDetail dt : listOrderDetail) {
					WrapOrderDetailLixReqInfo t = new WrapOrderDetailLixReqInfo(dt);
					listWrapOrderDetailLixReqInfo.add(t);
					listProductId.add(dt.getProduct().getId());
					mapEx.put(dt.getProduct().getId(), t);
				}
				List<Object[]> listDataExtensionOrder = new ArrayList<>();
				orderLixService.getListExportExtensionOrder(orderLixCrud.getId(), listDataExtensionOrder);
				if (listDataExtensionOrder.size() > 0) {
					for (Object[] v : listDataExtensionOrder) {
						Long productId = Long.parseLong(Objects.toString(v[0]));
						if (mapEx.containsKey(productId)) {
							mapEx.get(productId).setReal_quantity(Double.parseDouble(Objects.toString(v[1], "0")));
							mapEx.get(productId).setRemain_quantity(Double.parseDouble(Objects.toString(v[2], "0")));
						}
					}
				}
				tinhlaikhuyenmaisoluong(true);
				updateform("tablect,tablekm,menuformid:tabview1:tongtien,menuformid:tabview1:tongtien,menuformid:tabview1:napExcel");
			} else {
				notify.warning("Đơn hàng chưa lưu không tạo hóa đơn được");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.tabInvoiceOnClick:" + e.getMessage(), e);
		}
	}

	public void ajaxNo(PromotionOrderDetail promotionOrderDetail) {
		orderLixService.updateNoPromotionOrderDetail(promotionOrderDetail.getId(), promotionOrderDetail.isNo());
	}

	@Getter
	double khoiluongxuat;

	public void exportAll() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (allex) {
				listPromotionOrderDetailInvoice = new ArrayList<>();
				if (listWrapOrderDetailLixReqInfo != null && listWrapOrderDetailLixReqInfo.size() > 0) {
					Map<Long, WrapOrderDetailLixReqInfo> mapEx = new LinkedHashMap<>();
					for (WrapOrderDetailLixReqInfo p : listWrapOrderDetailLixReqInfo) {
						if (p.getOrder_detail().isHuyct() == false)
							mapEx.put(p.getOrder_detail().getProduct().getId(), p);
					}
					List<Object[]> listDataExtensionOrder = new ArrayList<>();
					orderLixService.getListExportExtensionOrder(orderLixCrud.getId(), listDataExtensionOrder);
					if (listDataExtensionOrder.size() > 0) {
						for (Object[] v : listDataExtensionOrder) {
							Long productId = Long.parseLong(Objects.toString(v[0]));
							if (mapEx.containsKey(productId)) {
								mapEx.get(productId).setReal_quantity(Double.parseDouble(Objects.toString(v[1], "0")));
								double remainQuantity = Double.parseDouble(Objects.toString(v[2], "0"));
								mapEx.get(productId).setRemain_quantity(remainQuantity);
								mapEx.get(productId).setExport_quantity(remainQuantity);
								double unitExportQuantity = BigDecimal
										.valueOf(remainQuantity)
										.multiply(
												BigDecimal.valueOf(mapEx.get(productId).getOrder_detail().getProduct()
														.getSpecification())).doubleValue();
								if (unitExportQuantity < 0)
									unitExportQuantity = 0;
								mapEx.get(productId).setExport_unit_quantity(unitExportQuantity);
							}
						}
					}
				}
			} else {
				listPromotionOrderDetailInvoice = new ArrayList<>();
				if (listWrapOrderDetailLixReqInfo != null && listWrapOrderDetailLixReqInfo.size() > 0) {
					Map<Long, WrapOrderDetailLixReqInfo> mapEx = new LinkedHashMap<>();
					for (WrapOrderDetailLixReqInfo p : listWrapOrderDetailLixReqInfo) {
						p.setExport_quantity(0);
						p.setExport_unit_quantity(0);
						mapEx.put(p.getOrder_detail().getProduct().getId(), p);
					}
				}
			}
			tinhkhoiluong();
		} catch (Exception e) {
			logger.error("OrderLixBean.exportAll:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdateOrderLix() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (orderLixCrud != null) {
				// kiểm tra thông tin có đầy đủ không
				IECategories ie = orderLixCrud.getIe_categories();
				Date ngaydathang = orderLixCrud.getOrderDateCreate();
				Date ngayDH = orderLixCrud.getOrder_date();
				Warehouse wh = orderLixCrud.getWarehouse();
				Customer customer = orderLixCrud.getCustomer();
				if (ie != null && ngayDH != null && wh != null && customer != null && ngaydathang != null) {
					OrderLixReqInfo t = new OrderLixReqInfo();
					OrderLix orderLixClone = orderLixCrud.clone();
					t.setOrder_lix(orderLixClone);
					if (orderLixClone.getId() == 0) {
						if (allowSave(null)) {
							orderLixClone.setCreated_by(account.getMember().getName());
							orderLixClone.setCreated_date(new Date());
							orderLixClone.setCreated_by_id(account.getMember().getId());
							int chk = orderLixService.insert(t);
							switch (chk) {
							case 0:
								// cập nhật lại flag false
								OrderLix orderLixTrans = t.getOrder_lix();
								// check trường hợp copy;
								if (listOrderDetail != null && listOrderDetail.size() > 0) {
									for (OrderDetail w : listOrderDetail) {
										OrderDetailReqInfo olf = new OrderDetailReqInfo();
										OrderDetail cloned = w.clone();
										olf.setOrder_detail(cloned);
										cloned.setCreated_date(new Date());
										cloned.setCreated_by(account.getMember().getName());
										cloned.setOrder_lix(t.getOrder_lix());
										WrapPMOrderDetailReqInfo wrap = new WrapPMOrderDetailReqInfo(cloned, account
												.getMember().getName());
										Message message = new Message();
										processLogicOrderService.insertOrUpdateOrderDetail(wrap, message);
									}
									success("Sao chép thành công.");
								} else {
									success("Lưu đơn hàng thành công.");
								}
								orderLixCrud = orderLixTrans;
								listOrderLix.add(0, orderLixCrud.clone());
								// show thông tin chi tiết đơn hàng
								listOrderDetail = new ArrayList<>();
								orderDetailService.selectByOrder(orderLixCrud.getId(), listOrderDetail);
								// show thông tin sản phẩm khuyến khuyến mãi của
								// đơn hàng
								listPromotionOrderDetail = new ArrayList<>();
								promotionOrderDetailService.selectByOrder(orderLixCrud.getId(),
										listPromotionOrderDetail);
								// Cai lai don gia
								for (int i = 0; i < listOrderDetail.size(); i++) {
									caidatlaidongia(listOrderDetail.get(i));
									orderDetailService.updatePriceTotal(listOrderDetail.get(i));
								}
								sumOrderLix();
								break;
							case -2:
								current.executeScript("swaldesigntimer('Cảnh báo', 'Mã đơn hàng đã tồn tại!','warning',2000);");
								break;
							default:
								current.executeScript("swaldesigntimer('Xảy ra lỗi', 'Lưu đơn hàng thất bại!','warning',2000);");
								break;
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					} else {
						if (allowUpdate(null)) {
							orderLixClone.setLast_modifed_by(account.getMember().getName());
							orderLixClone.setLast_modifed_date(new Date());
							// bình thường
							int chk = orderLixService.update(t);
							switch (chk) {
							case 0:
								success("Lưu thành công");
								listOrderLix.set(listOrderLix.indexOf(t.getOrder_lix()), t.getOrder_lix());
								listOrderDetail = new ArrayList<>();
								orderDetailService.selectByOrder(t.getOrder_lix().getId(), listOrderDetail);
								// Cai lai don gia
								for (int i = 0; i < listOrderDetail.size(); i++) {
									caidatlaidongia(listOrderDetail.get(i));
									orderDetailService.updatePriceTotal(listOrderDetail.get(i));
								}
								break;
							case -2:
								current.executeScript("swaldesigntimer('Cảnh báo', 'Mã đơn hàng đã tồn tại!','warning',2000);");
								break;

							default:
								current.executeScript("swaldesigntimer('Xảy ra lỗi', 'Kiểm tra file log server','warning',2000);");
								break;
							}

						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}

					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo', 'Thông tin không đầy đủ, điền đầy đủ thông tin chứa (*)','warning',2500);");
				}
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.saveOrUpdateOrderLix:" + e.getMessage(), e);
		}
		// clear filter
		current.executeScript("PF('tablect').clearFilters();");
		current.executeScript("PF('tablekm').clearFilters();");
	}

	public void confirmChangePricingAndPromotion() {
		try {
			// cập nhật chương trình đơn giá cho đơn hàng
			changePricingProgram = true;
			changePromotionProgram = true;
			JsonObject json = new JsonObject();
			json.addProperty("date", ToolTimeCustomer.convertDateToString(orderLixCrud.getOrder_date(), "dd/MM/yyyy"));
			json.addProperty("customer_id", orderLixCrud.getCustomer().getId());
			CustomerPricingProgramReqInfo t = new CustomerPricingProgramReqInfo();
			customerPricingProgramService.selectForCustomer(JsonParserUtil.getGson().toJson(json), t);
			/* cập nhật chương trình khuyến mãi cho khách hàng */
			CustomerPromotionProgramReqInfo t1 = new CustomerPromotionProgramReqInfo();
			customerPromotionProgramService.selectForCustomer(JsonParserUtil.getGson().toJson(json), t1);
			// nạp chương trình đơn giá
			if (t.getCustomer_pricing_program() != null) {
				PricingProgram pricingProgram = t.getCustomer_pricing_program().getPricing_program();
				orderLixCrud.setPricing_program(pricingProgram);
			} else {
				orderLixCrud.setPricing_program(null);
			}
			/* nạp chương trình khuyến mãi */
			if (t1.getCustomer_promotion_program() != null) {
				PromotionProgram promotionProgram = t1.getCustomer_promotion_program().getPromotion_program();
				orderLixCrud.setPromotion_program(promotionProgram);
			} else {
				orderLixCrud.setPromotion_program(null);
			}
			orderLixService.updateChangePro(orderLixCrud);
			for (int i = 0; i < listOrderDetail.size(); i++) {
				caidatlaidongia(listOrderDetail.get(i));
				listOrderDetail.get(i).setLast_modifed_by(account.getMember().getName());
				listOrderDetail.get(i).setLast_modifed_date(new Date());
				Message message = new Message();
				WrapPMOrderDetailReqInfo wrap = new WrapPMOrderDetailReqInfo(listOrderDetail.get(i), account
						.getMember().getName());
				processLogicOrderService.insertOrUpdateOrderDetail(wrap, message);
			}

			orderLixSelect = orderLixService.finByIdAll(orderLixCrud.getId());
			showEditOrderLix();
			listOrderLix.set(listOrderLix.indexOf(orderLixCrud), orderLixCrud);
			updateform(":menuformid");
		} catch (Exception e) {
			logger.error("OrderLixBean.confirmChangePricingAndPromotion:" + e.getMessage(), e);
		}
	}

	public void confirmChangePricing() {
		try {
			changePricingProgram = true;
			// cập nhật chương trình đơn giá cho đơn hàng
			JsonObject json = new JsonObject();
			json.addProperty("date", ToolTimeCustomer.convertDateToString(orderLixCrud.getOrder_date(), "dd/MM/yyyy"));
			json.addProperty("customer_id", orderLixCrud.getCustomer().getId());
			CustomerPricingProgramReqInfo t = new CustomerPricingProgramReqInfo();
			customerPricingProgramService.selectForCustomer(JsonParserUtil.getGson().toJson(json), t);
			// nạp chương trình đơn giá
			if (t.getCustomer_pricing_program() != null) {
				PricingProgram pricingProgram = t.getCustomer_pricing_program().getPricing_program();
				orderLixCrud.setPricing_program(pricingProgram);
			} else {
				orderLixCrud.setPricing_program(null);
			}
			orderLixService.updateChangePro(orderLixCrud);
			for (int i = 0; i < listOrderDetail.size(); i++) {
				caidatlaidongia(listOrderDetail.get(i));
				listOrderDetail.get(i).setLast_modifed_by(account.getMember().getName());
				listOrderDetail.get(i).setLast_modifed_date(new Date());
				Message message = new Message();
				WrapPMOrderDetailReqInfo wrap = new WrapPMOrderDetailReqInfo(listOrderDetail.get(i), account
						.getMember().getName());
				processLogicOrderService.updatePrice(wrap, message);
			}
			orderLixSelect = orderLixService.finByIdAll(orderLixCrud.getId());
			showEditOrderLix();
			listOrderLix.set(listOrderLix.indexOf(orderLixCrud), orderLixCrud);
			updateform(":menuformid");
		} catch (Exception e) {
			logger.error("OrderLixBean.confirmChangePricing:" + e.getMessage(), e);
		}
	}

	public void confirmChangePromotion() {
		try {
			changePromotionProgram = true;
			JsonObject json = new JsonObject();
			json.addProperty("date", ToolTimeCustomer.convertDateToString(orderLixCrud.getOrder_date(), "dd/MM/yyyy"));
			json.addProperty("customer_id", orderLixCrud.getCustomer().getId());
			/* cập nhật chương trình khuyến mãi cho khách hàng */
			CustomerPromotionProgramReqInfo t1 = new CustomerPromotionProgramReqInfo();
			customerPromotionProgramService.selectForCustomer(JsonParserUtil.getGson().toJson(json), t1);
			// PromotionProgram
			// promotionProgram=t1.getCustomer_promotion_program().getPromotion_program();
			/* nạp chương trình khuyến mãi */
			if (t1.getCustomer_promotion_program() != null) {
				orderLixCrud.setPromotion_program(t1.getCustomer_promotion_program().getPromotion_program());
			} else {
				orderLixCrud.setPromotion_program(null);
			}
			orderLixService.updateChangePro(orderLixCrud);
			for (int i = 0; i < listOrderDetail.size(); i++) {
				caidatlaidongia(listOrderDetail.get(i));
				listOrderDetail.get(i).setLast_modifed_by(account.getMember().getName());
				listOrderDetail.get(i).setLast_modifed_date(new Date());
				Message message = new Message();
				WrapPMOrderDetailReqInfo wrap = new WrapPMOrderDetailReqInfo(listOrderDetail.get(i), account
						.getMember().getName());
				processLogicOrderService.insertOrUpdateOrderDetail(wrap, message);
			}
			orderLixSelect = orderLixService.finByIdAll(orderLixCrud.getId());
			showEditOrderLix();
			listOrderLix.set(listOrderLix.indexOf(orderLixCrud), orderLixCrud);
			updateform(":menuformid");
		} catch (Exception e) {
			logger.error("OrderLixBean.confirmChangePromotion:" + e.getMessage(), e);
		}
	}

	public void changeOrderDate() {
		PrimeFaces current = PrimeFaces.current();
		try {
			// reset flag thay đổi đơn giá và khuyến mãi
			changePricingProgram = false;
			changePromotionProgram = false;
			if (orderLixCrud != null && orderLixCrud.getCustomer() != null && orderLixCrud.getOrder_date() != null) {
				/* {order_date:'',customer_id:0} */
				// cập nhật chương trình đơn giá cho đơn hàng
				JsonObject json = new JsonObject();
				json.addProperty("date",
						ToolTimeCustomer.convertDateToString(orderLixCrud.getOrder_date(), "dd/MM/yyyy"));
				json.addProperty("customer_id", orderLixCrud.getCustomer().getId());
				CustomerPricingProgramReqInfo t = new CustomerPricingProgramReqInfo();

				customerPricingProgramService.selectForCustomer(JsonParserUtil.getGson().toJson(json), t);

				PricingProgram pricingProgramTrans = t.getCustomer_pricing_program() == null ? null : t
						.getCustomer_pricing_program().getPricing_program();
				/* cập nhật chương trình khuyến mãi cho khách hàng */
				CustomerPromotionProgramReqInfo t1 = new CustomerPromotionProgramReqInfo();
				customerPromotionProgramService.selectForCustomer(JsonParserUtil.getGson().toJson(json), t1);
				PromotionProgram promotionProgramTrans = t1.getCustomer_promotion_program() == null ? null : t1
						.getCustomer_promotion_program().getPromotion_program();
				if (orderLixCrud.getId() != 0) {
					PricingProgram pricingProgramCurrent = orderLixCrud.getPricing_program();
					PromotionProgram promotionProgramCurrent = orderLixCrud.getPromotion_program();
					boolean kt1 = (pricingProgramCurrent == null && pricingProgramTrans != null)
							|| (pricingProgramCurrent != null && pricingProgramTrans == null)
							|| (pricingProgramCurrent != null && pricingProgramTrans != null && !pricingProgramCurrent
									.equals(pricingProgramTrans));
					boolean kt2 = (promotionProgramCurrent == null && promotionProgramTrans != null)
							|| (promotionProgramCurrent != null && promotionProgramTrans == null)
							|| (promotionProgramCurrent != null && promotionProgramTrans != null && !promotionProgramCurrent
									.equals(promotionProgramTrans));
					if (kt1 && kt2) {
						current.executeScript("swalfunction2('Cảnh báo','Có sự thay đổi chương trình đơn giá và khuyến mãi, bạn có muốn thay đổi không?','warning','confirmChangePricingAndPromotion()')");
					} else if (kt1 && !kt2) {
						current.executeScript("swalfunction2('Cảnh báo','Có sự thay đổi chương trình đơn giá, bạn có muốn thay đổi không?','warning','confirmChangePricing()')");
					} else if (!kt1 && kt2) {
						current.executeScript("swalfunction2('Cảnh báo','Có sự thay đổi chương trình khuyến mãi, bạn có muốn thay đổi không?','warning','confirmChangePromotion()')");
					}
				} else {
					orderLixCrud.setPricing_program(pricingProgramTrans);
					orderLixCrud.setPromotion_program(promotionProgramTrans);
				}
			} else {
				orderLixCrud.setPricing_program(null);
				orderLixCrud.setPromotion_program(null);
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.changeDeliveryDate:" + e.getMessage(), e);
		}
	}

	@Inject
	private IIECategoriesService ieCategoriesService;

	public void customerChange() {
		try {
			if (orderLixCrud != null) {
				// orderLixCrud.setFlag_up(true);
				Customer obj = orderLixCrud.getCustomer();
				if (obj != null) {
					// tìm địa điểm đơn giá giao hàng
					JsonObject json1 = new JsonObject();
					json1.addProperty("customer_id", obj.getId());
					json1.addProperty("disable", 0);
					List<DeliveryPricing> list = new ArrayList<>();
					deliveryPricingService.seletcBy(JsonParserUtil.getGson().toJson(json1), list);
					// nạp địa điểm giao hàng
					if (list.size() > 0) {
						DeliveryPricing deliveryPricingCurrent = orderLixCrud.getDelivery_pricing();
						if (deliveryPricingCurrent != null) {
							DeliveryPricingReqInfo deliveryPricingD = new DeliveryPricingReqInfo();
							deliveryPricingService.selectById(deliveryPricingCurrent.getId(), deliveryPricingD);
							deliveryPricingCurrent = deliveryPricingD.getDelivery_pricing();
						}
						// nếu cùng khách hàng thì check còn địa điểm giao hàng
						// còn sử dụng nữa không
						if (deliveryPricingCurrent != null
								&& deliveryPricingCurrent.getCustomer().equals(orderLixCrud.getCustomer())) {
							DeliveryPricingReqInfo deliveryPricingReqInfo = new DeliveryPricingReqInfo();
							deliveryPricingService.selectById(deliveryPricingCurrent.getId(), deliveryPricingReqInfo);
							if (deliveryPricingReqInfo.getDelivery_pricing() != null
									&& !deliveryPricingReqInfo.getDelivery_pricing().isDisable()) {
								// do not thing!
							} else {
								orderLixCrud.setDelivery_pricing(list.get(0));
							}
						} else {
							orderLixCrud.setDelivery_pricing(list.get(0));
						}
					} else {
						orderLixCrud.setDelivery_pricing(null);
					}

					if (orderLixCrud.getIe_categories() == null) {
						// khach hang noi bo -> ma nhap xuat Y
						Customer cus = customerService.findById(orderLixCrud.getCustomer().getId());
						if (cus.getCustomer_types() != null)
							if ("NB".equals(cus.getCustomer_types().getCode())) {
								orderLixCrud.setIe_categories(ieCategoriesService.selectByCodeOld("Y"));
							}
					}
				} else {
					orderLixCrud.setDelivery_pricing(null);
				}
				changeOrderDate();
			}

		} catch (Exception e) {
			logger.error("OrderLixBean.customerChange:" + e.getMessage(), e);
		}
	}

	public void showDialogPickDeliveryPricing() {
		PrimeFaces current = PrimeFaces.current();
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			listDeliveryPricing = new ArrayList<>();
			if (orderLixCrud != null && orderLixCrud.getCustomer() != null) {
				/* {place_code:'',customer_id:0} */
				JsonObject json = new JsonObject();
				json.addProperty("customer_id", orderLixCrud.getCustomer().getId());
				json.addProperty("disable", 0);
				deliveryPricingService.seletcBy(JsonParserUtil.getGson().toJson(json), listDeliveryPricing);
				current.executeScript("PF('dlgpickpd').show();");
			} else {
				notify.warning("Chưa chọn khách hàng");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.showDialogPickDeliveryPricing:" + e.getMessage(), e);
		}
	}

	public void pickDeliveryPricing(SelectEvent event) {
		try {
			if (orderLixCrud != null && orderLixCrud.getCustomer() != null) {
				orderLixCrud.setDelivery_pricing((DeliveryPricing) event.getObject());
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.pickDeliveryPricing:" + e.getMessage(), e);
		}
	}

	public void rowFreightContractClick(SelectEvent event) {
		try {
			FreightContract obj = (FreightContract) event.getObject();
			if (orderLixCrud != null) {
				orderLixCrud.setFreight_contract(obj);
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.rowFreightContractClick:" + e.getMessage(), e);
		}
	}

	public void nextOrPrev(int next) {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (listOrderLix != null && listOrderLix.size() > 0) {
				int index = listOrderLix.indexOf(orderLixCrud);
				int size = listOrderLix.size() - 1;
				if (index == -1) {
					orderLixSelect = listOrderLix.get(0);
				} else {
					switch (next) {
					case 1:
						if (index == size) {
							warning("Hết danh sách");
						} else {
							orderLixSelect = listOrderLix.get(index + 1);
						}
						break;
					default:
						if (index == 0) {
							warning("Hết danh sách");
						} else {
							orderLixSelect = listOrderLix.get(index - 1);
						}
						break;
					}
				}
			}
			showEditOrderLix();
		} catch (Exception e) {
			logger.error("OrderLixBean.nextOrPrev:" + e.getMessage(), e);
		}
	}

	public List<CarOwner> completeCarOwner(String text) {
		return listCarOwner;
	}

	public List<Customer> completeCustomer(String text) {
		try {
			List<Customer> list = new ArrayList<>();
			customerService.complete(formatHandler.converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("OrderLixBean.completeCustomer:" + e.getMessage(), e);
		}
		return null;
	}

	public List<Customer> completeCustomerFC(String text) {
		try {
			List<Customer> list = new ArrayList<>();
			if (customerTypesFCFilter != null) {
				customerService.complete(formatHandler.converViToEn(text), customerTypesFCFilter, list);
			} else {
				customerService.complete(formatHandler.converViToEn(text), list);
			}
			return list;
		} catch (Exception e) {
			logger.error("OrderLixBean.completeCustomerFC:" + e.getMessage(), e);
		}
		return null;
	}

	public List<PricingProgram> completePricingProgram(String text) {
		try {
			List<PricingProgram> list = new ArrayList<>();
			pricingProgramService.complete(formatHandler.converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("OrderLixBean.completePricingProgram:" + e.getMessage(), e);
		}
		return null;
	}

	public List<PromotionProgram> completePromotionProgram(String text) {
		try {
			List<PromotionProgram> list = new ArrayList<>();
			promotionProgramService.complete(formatHandler.converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("OrderLixBean.completePromotionProgram:" + e.getMessage(), e);
		}
		return null;
	}

	public List<Product> completeProduct(String text) {
		try {
			List<Product> list = new ArrayList<>();
			productService.complete3(formatHandler.converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("OrderLixBean.completeProduct:" + e.getMessage(), e);
		}
		return null;
	}

	@Getter
	@Setter
	private String stextStr;

	public void search() {
		try {
			listOrderLix = new ArrayList<OrderLix>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("stextStr", MyStringUtil.replaceD(stextStr));
			jsonInfo.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("product_id", productSearch == null ? 0 : productSearch.getId());
			jsonInfo.addProperty("customer_id", customerSearch == null ? 0 : customerSearch.getId());
			jsonInfo.addProperty("order_code", orderCodeSearch);
			jsonInfo.addProperty("voucher_code", voucherCodeSearch);
			jsonInfo.addProperty("ie_categories_id", iECategoriesSearch == null ? 0 : iECategoriesSearch.getId());
			jsonInfo.addProperty("po_no", poNoSearch);
			jsonInfo.addProperty("delivered", deliveredSearch);
			jsonInfo.addProperty("brand", brandSearch);
			JsonObject json = new JsonObject();
			json.add("order_info", jsonInfo);
			orderLixService.search(JsonParserUtil.getGson().toJson(json), listOrderLix, statusSearch);
		} catch (Exception e) {
			logger.error("OrderLixBean.search:" + e.getMessage(), e);
		}
	}

	public void onTabChange(TabChangeEvent event) {
		if (event.getTab() != null)
			if ("Danh sách đơn hàng".equals(event.getTab().getTitle())) {
				if (listOrderLix == null || listOrderLix.size() == 0) {
					search();
				}
				PrimeFaces.current().ajax().update("menuformid:tabview1:tablesp");
			}

	}

	@Getter
	@Setter
	boolean chonhet = false;

	public void chonHetPhieu() {
		if (listOrderLix != null)
			if (allowUpdate(orderLixCrud.getOrder_date())) {
				for (int i = 0; i < listOrderLix.size(); i++) {
					listOrderLix.get(i).setChonphieu(chonhet);
				}
			}
	}

	@Getter
	@Setter
	boolean huyCT = false;

	public void huyHetCT() {
		if (listOrderDetail != null)
			if (allowUpdate(null)) {
				for (int i = 0; i < listOrderDetail.size(); i++) {
					listOrderDetail.get(i).setHuyct(huyCT);
					orderDetailService.updateHuy(listOrderDetail.get(i));
				}
				for (int i = 0; i < listPromotionOrderDetail.size(); i++) {
					listPromotionOrderDetail.get(i).setHuyct(huyCT);
					promotionOrderDetailService.updateHuy(listPromotionOrderDetail.get(i));
				}
				updateform("menuformid:tabview1:tablekm");
			} else {
				listOrderDetail = new ArrayList<>();
				orderDetailService.selectByOrder(orderLixCrud.getId(), listOrderDetail);
				listPromotionOrderDetail = new ArrayList<>();
				promotionOrderDetailService.selectByOrder(orderLixCrud.getId(), listPromotionOrderDetail);
				updateform("menuformid:tabview1:tablect,menuformid:tabview1:tablekm");
				executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
			}
	}

	public void capnhattrangthaihuy(OrderDetail odDetail) {
		if (allowUpdate(null)) {
			orderDetailService.updateHuy(odDetail);
			for (int i = 0; i < listPromotionOrderDetail.size(); i++) {
				if (listPromotionOrderDetail.get(i).getOrder_detail().getId() == odDetail.getId()) {
					listPromotionOrderDetail.get(i).setHuyct(odDetail.isHuyct());
					promotionOrderDetailService.updateHuy(listPromotionOrderDetail.get(i));
				}
			}
			updateform("menuformid:tabview1:tablekm");
		} else {
			int index = listOrderDetail.indexOf(odDetail);
			if (index != -1)
				listOrderDetail.get(index).setHuyct(!odDetail.isHuyct());
			listPromotionOrderDetail = new ArrayList<>();
			promotionOrderDetailService.selectByOrder(orderLixCrud.getId(), listPromotionOrderDetail);
			updateform("menuformid:tabview1:tablect,menuformid:tabview1:tablekm");
			executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
		}
	}

	public void showEditOrderLix() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (orderLixSelect != null) {
				orderLixCrud = orderLixSelect.clone();
				// show thông tin chi tiết đơn hàng
				loadOrderLixDetail();
				clearSort();
				// show thông tin sản phẩm khuyến khuyến mãi của đơn hàng
				listPromotionOrderDetail = new ArrayList<>();
				promotionOrderDetailService.selectByOrder(orderLixCrud.getId(), listPromotionOrderDetail);
				sumOrderLix();
				if (!tabOrder) {
					tabInvoiceOnClick();
				}
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.showEditOrderLix:" + e.getMessage(), e);
		}
		current.executeScript("PF('tablect').clearFilters();");
		current.executeScript("PF('tablekm').clearFilters();");
	}

	@Getter
	@Setter
	DataTable dataTable;
	@Getter
	String columnSort;

	public void loadOrderLixDetail() {
		try {
			if (orderLixCrud != null) {
				listOrderDetail = new ArrayList<>();
				orderDetailService.selectByOrder(orderLixCrud.getId(), listOrderDetail);
			}
			if (listOrderDetail.size() != 0) {
				columnSort = listOrderDetail.get(0).getColumnsort();
				if (dataTable != null && listOrderDetail.size() != 0) {
					if (columnSort != null) {
						dataTable.setValueExpression("sortBy",
								createValueExpression("#{item." + columnSort + "}", Object.class));
						dataTable.setSortOrder(listOrderDetail.get(0).isAscd() ? "ascending" : "descending");
					} else {
						dataTable.setValueExpression("sortBy", null);
					}
				}
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.showEditOrderLix:" + e.getMessage(), e);
		}
	}

	public void capnhatthanhtien() {
		for (int i = 0; i < listOrderLix.size(); i++) {
			List<OrderDetail> listOrderDetail = new ArrayList<OrderDetail>();
			orderDetailService.selectByOrder(listOrderLix.get(i).getId(), listOrderDetail);
			for (int j = 0; j < listOrderDetail.size(); j++) {
				double total = MyMath.round(listOrderDetail.get(j).getBox_quantity()
						* listOrderDetail.get(j).getProduct().getSpecification()
						* listOrderDetail.get(j).getUnit_price());
				listOrderDetail.get(j).setTotal(total);
				orderDetailService.updateTotal(listOrderDetail.get(j));
			}
			notice("Cập nhật hoàn thành");
		}
	}

	public ValueExpression createValueExpression(String expression, Class<?> expectedType) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
		ELContext elContext = facesContext.getELContext();
		return expressionFactory.createValueExpression(elContext, expression, expectedType);
	}

	public void clearSort() {
		try {
			// DataTable dataTable = (DataTable)
			// FacesContext.getCurrentInstance().getViewRoot()
			// .findComponent("menuformid:tabview1:tablect");
			// dataTable.reset();
		} catch (Exception e) {
		}

	}

	public double getRealExportBox(OrderDetail item) {
		try {
			return orderLixService.getRealExportBox(item.getOrder_lix().getId(), item.getProduct().getId());
		} catch (Exception e) {
			logger.error("OrderLixBean.getRealExportBox:" + e.getMessage(), e);
		}
		return 0;
	}

	public List<Car> completeCar(String text) {
		try {
			if (text != null && !"".equals(text)) {
				List<Car> list = new ArrayList<>();
				carService.complete(formatHandler.converViToEn(text), list);
				return list;
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.completeCar:" + e.getMessage(), e);
		}
		return null;
	}

	public void createdNew() {
		PrimeFaces current = PrimeFaces.current();
		try {
			orderLixCrud = new OrderLix();
			orderLixCrud.setOrderDateCreate(ToolTimeCustomer.getFirstDateOfDay(new Date()));
			orderLixCrud.setBrand(getDatabaseDH());
			orderLixCrud.setOrder_date(ToolTimeCustomer.getFirstDateOfDay(new Date()));
			orderLixCrud.setDelivery_date(orderLixCrud.getOrder_date());
			orderLixCrud.setCreated_by(account.getMember().getName());
			orderLixCrud.setWarehouse(warehouseService.selectByCode("F"));// F:kho
																			// thanh
																			// pham
			listOrderDetail = new ArrayList<>();
			listPromotionOrderDetail = new ArrayList<>();
			listWrapOrderDetailLixReqInfo = new ArrayList<>();
			listPromotionOrderDetailInvoice = new ArrayList<>();
			changeOrderDate();
		} catch (Exception e) {
			logger.error("OrderLixBean.createNew:" + e.getMessage(), e);
		}
		// clear filter
		current.executeScript("PF('tablect').clearFilters();");
		current.executeScript("PF('tablekm').clearFilters();");
	}

	public void kiemTraSoLuong(WrapOrderDetailLixReqInfo t) {
		try {
			double unitExportQuantity = MyMath.roundCustom(t.getExport_quantity()
					* t.getOrder_detail().getProduct().getSpecification(), 2);
			t.setExport_unit_quantity(unitExportQuantity);
			t.setTotal_unit_quantity(MyMath.roundCustom(t.getExport_unit_quantity()
					* t.getOrder_detail().getUnit_price(), 2));
			tinhkhoiluong();
		} catch (Exception e) {
			logger.error("OrderLixBean.kiemTraSoLuong:" + e.getMessage(), e);
		}
	}

	public void tinhkhoiluong() {
		khoiluongxuat = 0;
		if (listWrapOrderDetailLixReqInfo != null)
			for (int i = 0; i < listWrapOrderDetailLixReqInfo.size(); i++) {
				khoiluongxuat += listWrapOrderDetailLixReqInfo.get(i).getExport_unit_quantity()
						* listWrapOrderDetailLixReqInfo.get(i).getOrder_detail().getProduct().getFactor();
			}
		if (listPromotionOrderDetailInvoice != null)
			for (int i = 0; i < listPromotionOrderDetailInvoice.size(); i++) {
				if (listPromotionOrderDetailInvoice.get(i).isNo() == false)
					khoiluongxuat += listPromotionOrderDetailInvoice.get(i).getQuantity()
							* listPromotionOrderDetailInvoice.get(i).getProduct().getFactor();
			}
		khoiluongxuat=Math.round(khoiluongxuat);
		updateform("menuformid:tabview1:khoiluongxuat");
	}

	@Inject
	HeThongService heThongService;

	public void createInvoiceByExtensionOrder() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (orderLixCrud != null && orderLixCrud.getId() != 0) {
				if (allowUpdate(null)) {
					tinhkhuyenmaixuattiep();
					List<WrapOrderDetailLixReqInfo> listData = new ArrayList<>();
					if (listWrapOrderDetailLixReqInfo != null && listWrapOrderDetailLixReqInfo.size() > 0) {
						for (WrapOrderDetailLixReqInfo w : listWrapOrderDetailLixReqInfo) {
							if (w.getExport_unit_quantity() > 0) {
								listData.add(w);
							}
						}
					}
					if (listData.size() > 0) {
						// prepare data
						WrapExtensionOrderReqInfo data = new WrapExtensionOrderReqInfo(orderLixCrud.getId(), account
								.getMember().getId(), account.getMember().getName(), listData,
								listPromotionOrderDetailInvoice);
						StringBuilder messages = new StringBuilder();
						// list id invoice chỉ dùng cho việc binding result vs
						// foxpro
						List<Long> listIdResult = new ArrayList<>();
						HeThong ht = heThongService.findById(1);
						int code = processLogicInvoiceService.createInvoiceByExtensionOrder(data, listIdResult,
								messages, null, ht.isKiemton());
						if (code == 0) {
							success("Đã chuyển qua phiếu xuất.");
						} else {
							current.executeScript("swaldesignclose('Xảy ra lỗi!', '" + messages.toString()
									+ "!','warning',2000);");
						}
						// Cap nhat trang thai don hang
						try {
							orderLixService.updateTinhTrangDH(orderLixCrud.getId());
						} catch (Exception e) {
							error(e.getLocalizedMessage());
						}
						// load lại dữ liệu đơn hàng.
						OrderLixReqInfo orderLixReqInfo = new OrderLixReqInfo();
						orderLixService.selectById(orderLixCrud.getId(), orderLixReqInfo);

						listOrderLix.set(listOrderLix.indexOf(orderLixReqInfo.getOrder_lix()),
								orderLixReqInfo.getOrder_lix());
						orderLixCrud = orderLixReqInfo.getOrder_lix();
						// show thông tin chi tiết đơn hàng
						listOrderDetail = new ArrayList<>();
						orderDetailService.selectByOrder(orderLixCrud.getId(), listOrderDetail);

						// tinhlaikhuyenmaisoluong();
						sumOrderLix();
						if (!tabOrder) {
							tabInvoiceOnClick();
						}
					} else {
						current.executeScript("swaldesigntimer('Cảnh báo', 'Chưa nhập số lượng xuất','warning',2000);");
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo', 'Đơn hàng chưa được lưu!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.createInvoiceByExtensionOrder:" + e.getMessage(), e);
		}
	}

	
	public void createInvoiceTempByExtensionOrder() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (orderLixCrud != null && orderLixCrud.getId() != 0) {
				if (allowUpdate(null)) {
					tinhkhuyenmaixuattiep();
					List<WrapOrderDetailLixReqInfo> listData = new ArrayList<>();
					if (listWrapOrderDetailLixReqInfo != null && listWrapOrderDetailLixReqInfo.size() > 0) {
						for (WrapOrderDetailLixReqInfo w : listWrapOrderDetailLixReqInfo) {
							if (w.getExport_unit_quantity() > 0) {
								listData.add(w);
							}
						}
					}
					if (listData.size() > 0) {
						// prepare data
						WrapExtensionOrderReqInfo data = new WrapExtensionOrderReqInfo(orderLixCrud.getId(), account
								.getMember().getId(), account.getMember().getName(), listData,
								listPromotionOrderDetailInvoice);
						StringBuilder messages = new StringBuilder();
						// list id invoice chỉ dùng cho việc binding result vs
						// foxpro
						List<Long> listIdResult = new ArrayList<>();
						HeThong ht = heThongService.findById(1);
						int code = processLogicInvoiceService.createInvoiceTempByExtensionOrder(data, listIdResult,
								messages, null, ht.isKiemton());
						if (code == 0) {
							success("Đã chuyển qua phiếu xuất.");
						} else {
							current.executeScript("swaldesignclose('Xảy ra lỗi!', '" + messages.toString()
									+ "!','warning',2000);");
						}
						// Cap nhat trang thai don hang
						try {
							orderLixService.updateTinhTrangDH(orderLixCrud.getId());
						} catch (Exception e) {
							error(e.getLocalizedMessage());
						}
						// load lại dữ liệu đơn hàng.
						OrderLixReqInfo orderLixReqInfo = new OrderLixReqInfo();
						orderLixService.selectById(orderLixCrud.getId(), orderLixReqInfo);

						listOrderLix.set(listOrderLix.indexOf(orderLixReqInfo.getOrder_lix()),
								orderLixReqInfo.getOrder_lix());
						orderLixCrud = orderLixReqInfo.getOrder_lix();
						// show thông tin chi tiết đơn hàng
						listOrderDetail = new ArrayList<>();
						orderDetailService.selectByOrder(orderLixCrud.getId(), listOrderDetail);

						// tinhlaikhuyenmaisoluong();
						sumOrderLix();
						if (!tabOrder) {
							tabInvoiceOnClick();
						}
					} else {
						current.executeScript("swaldesigntimer('Cảnh báo', 'Chưa nhập số lượng xuất','warning',2000);");
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo', 'Đơn hàng chưa được lưu!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.createInvoiceByExtensionOrder:" + e.getMessage(), e);
		}
	}
	public void capnhattinhtrangdonhang() {
		if (listOrderLix != null && listOrderLix.size() != 0) {
			try {
				for (int i = 0; i < listOrderLix.size(); i++) {
					orderLixService.updateTinhTrangDH(listOrderLix.get(i).getId());
				}
				search();
				success();
			} catch (Exception e) {
				error(e.getLocalizedMessage());
			}
		} else {
			warning("Không có danh sách cập nhật.");
		}

	}

	@Getter
	@Setter
	boolean taodonhangcn = false;

	public void createInvoiceMain() {
		PrimeFaces current = PrimeFaces.current();
		StringBuilder messages = new StringBuilder();
		try {
			if (orderLixCrud != null && orderLixCrud.getId() != 0) {
				if (allowUpdate(null)) {
					tinhlaikhuyenmaisoluong(true);
					// prepare data
					WrapOrderLixReqInfo data = new WrapOrderLixReqInfo(orderLixCrud.getId(), account.getMember()
							.getName(), account.getMember().getId());

					HeThong ht = heThongService.findById(1);
					int code = processLogicInvoiceService.createInvoiceByOrderLix(data, messages, ht.isKiemton());
					if (code == 0) {
						success();
					} else {
						current.executeScript("swaldesignclose('Xảy ra lỗi!', '" + messages.toString()
								+ "!','warning',2000);");
					}
					// // Cap nhat trang thai don hang
					try {
						orderLixService.updateTinhTrangDH(orderLixCrud.getId());
					} catch (Exception e) {
						error(e.getLocalizedMessage());
					}
					// load lại dữ liệu đơn hàng.
					OrderLixReqInfo orderLixReqInfo = new OrderLixReqInfo();
					orderLixService.selectById(orderLixCrud.getId(), orderLixReqInfo);

					// Cap nhat trang thai da giao hang
					// orderLixReqInfo.getOrder_lix().setDelivered(true);
					// orderLixService.update(orderLixReqInfo);
					// orderLixCrud = orderLixReqInfo.getOrder_lix();
					// saveOrUpdateApiOrderFox();

					listOrderLix.set(listOrderLix.indexOf(orderLixReqInfo.getOrder_lix()),
							orderLixReqInfo.getOrder_lix());
					orderLixCrud = orderLixReqInfo.getOrder_lix();
					tinhlaikhuyenmaisoluong(false);
					if (!tabOrder) {
						tabInvoiceOnClick();
					}
				} else {
					noticeError("Tài khoản này không có quyền thực hiện hoặc tháng đã khoá.");
				}
			} else {
				noticeError("Lưu đơn hàng trước khi chuyển.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			noticeError(messages.toString());
		}
	}

	
	
	public void createInvoiceTempMain() {
		PrimeFaces current = PrimeFaces.current();
		StringBuilder messages = new StringBuilder();
		try {
			if (orderLixCrud != null && orderLixCrud.getId() != 0) {
				if (allowUpdate(null)) {
					tinhlaikhuyenmaisoluong(true);
					// prepare data
					WrapOrderLixReqInfo data = new WrapOrderLixReqInfo(orderLixCrud.getId(), account.getMember()
							.getName(), account.getMember().getId());

					HeThong ht = heThongService.findById(1);
					int code = processLogicInvoiceService.createInvoiceTempByOrderLix(data, messages, ht.isKiemton());
					if (code == 0) {
						success();
					} else {
						current.executeScript("swaldesignclose('Xảy ra lỗi!', '" + messages.toString()
								+ "!','warning',2000);");
					}
					// // Cap nhat trang thai don hang
					try {
						orderLixService.updateTinhTrangDH(orderLixCrud.getId());
					} catch (Exception e) {
						error(e.getLocalizedMessage());
					}
					// load lại dữ liệu đơn hàng.
					OrderLixReqInfo orderLixReqInfo = new OrderLixReqInfo();
					orderLixService.selectById(orderLixCrud.getId(), orderLixReqInfo);

					// Cap nhat trang thai da giao hang
					// orderLixReqInfo.getOrder_lix().setDelivered(true);
					// orderLixService.update(orderLixReqInfo);
					// orderLixCrud = orderLixReqInfo.getOrder_lix();
					// saveOrUpdateApiOrderFox();

					listOrderLix.set(listOrderLix.indexOf(orderLixReqInfo.getOrder_lix()),
							orderLixReqInfo.getOrder_lix());
					orderLixCrud = orderLixReqInfo.getOrder_lix();
					tinhlaikhuyenmaisoluong(false);
					if (!tabOrder) {
						tabInvoiceOnClick();
					}
				} else {
					noticeError("Tài khoản này không có quyền thực hiện hoặc tháng đã khoá.");
				}
			} else {
				noticeError("Lưu đơn hàng trước khi chuyển.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			noticeError(messages.toString());
		}
	}
	public void tinhkhuyenmai() {
		try {
			listPromotionOrderDetailInvoice = new ArrayList<>();
			if (listWrapOrderDetailLixReqInfo != null) {
				for (WrapOrderDetailLixReqInfo t : listWrapOrderDetailLixReqInfo) {
					if (t.getExport_quantity() > 0) {
						// tính khuyến mãi
						JsonObject js = new JsonObject();
						js.addProperty("product_id", t.getOrder_detail().getProduct().getId());
						js.addProperty("promotion_program_id", orderLixCrud.getPromotion_program().getId());
						js.addProperty("promotion_form", t.getOrder_detail().getPromotion_forms());
						List<PromotionProgramDetail> list = new ArrayList<>();
						promotionProgramDetailService.selectBy(JsonParserUtil.getGson().toJson(js), list);
						lv1: for (PromotionProgramDetail ppd : list) {
							PromotionOrderDetail pod = new PromotionOrderDetail();
							pod.setOrder_detail(t.getOrder_detail());
							Product pr = ppd.getPromotion_product();
							pod.setProduct(pr);
							// tim nạp đơn giá sản phẩm khuyến mãi
							PromotionalPricingReqInfo ppr = new PromotionalPricingReqInfo();
							JsonObject json = new JsonObject();
							json.addProperty(
									"date",
									ToolTimeCustomer.convertDateToString(t.getOrder_detail().getOrder_lix()
											.getOrder_date(), "dd/MM/yyyy"));
							json.addProperty("product_id", ppd.getPromotion_product().getId());
							promotionalPricingService.findSettingPromotionalPricing(
									JsonParserUtil.getGson().toJson(json), ppr);
							if (ppr.getPromotional_pricing() != null) {
								pod.setUnit_price(ppr.getPromotional_pricing().getUnit_price());
							}

							double quantity = BigDecimal.valueOf(t.getExport_quantity())
									.multiply(BigDecimal.valueOf(ppd.getPromotion_quantity()))
									.divide(BigDecimal.valueOf(ppd.getBox_quatity()), MathContext.DECIMAL32)
									.doubleValue();
							pod.setQuantity((int) quantity);

							for (PromotionOrderDetail p : listPromotionOrderDetailInvoice) {
								if (p.getProduct().equals(pod.getProduct())) {
									p.setQuantity(Double.sum(p.getQuantity(), pod.getQuantity()));
									continue lv1;
								}
							}
							listPromotionOrderDetailInvoice.add(pod);
						}
					}
				}
			}
		} catch (Exception e) {

		}
	}

	public void tinhkhuyenmaixuattiep() {
		try {
			listPromotionOrderDetailInvoice = new ArrayList<>();
			if (listWrapOrderDetailLixReqInfo != null) {
				for (WrapOrderDetailLixReqInfo t : listWrapOrderDetailLixReqInfo) {
					if (t.getExport_quantity() > 0) {
						if (orderLixCrud.getPromotion_program() != null) {
							// tính khuyến mãi
							JsonObject js = new JsonObject();
							js.addProperty("product_id", t.getOrder_detail().getProduct().getId());
							js.addProperty("promotion_program_id",
									orderLixCrud.getPromotion_program() != null ? orderLixCrud.getPromotion_program()
											.getId() : 0);
							js.addProperty("promotion_form", t.getOrder_detail().getPromotion_forms());
							List<PromotionProgramDetail> list = new ArrayList<>();
							promotionProgramDetailService.selectBy(JsonParserUtil.getGson().toJson(js), list);
							lv1: for (PromotionProgramDetail ppd : list) {
								PromotionOrderDetail pod = new PromotionOrderDetail();
								pod.setOrder_detail(t.getOrder_detail());
								Product pr = ppd.getPromotion_product();
								pod.setProduct(pr);
								// tim nạp đơn giá sản phẩm khuyến mãi
								PromotionalPricingReqInfo ppr = new PromotionalPricingReqInfo();
								JsonObject json = new JsonObject();
								json.addProperty(
										"date",
										ToolTimeCustomer.convertDateToString(t.getOrder_detail().getOrder_lix()
												.getOrder_date(), "dd/MM/yyyy"));
								json.addProperty("product_id", ppd.getPromotion_product().getId());
								promotionalPricingService.findSettingPromotionalPricing(JsonParserUtil.getGson()
										.toJson(json), ppr);
								if (ppr.getPromotional_pricing() != null) {
									pod.setUnit_price(ppr.getPromotional_pricing().getUnit_price());
								}
								// double quantity =
								// t.getExport_quantity()*ppd.getPromotion_quantity()/ppd.getBox_quatity();
								// pod.setQuantity((int) quantity);

								double quantity = (int) t.getExport_quantity() / ppd.getBox_quatity()
										* ppd.getSpecification();
								pod.setQuantity((int) quantity);

								for (PromotionOrderDetail p : listPromotionOrderDetailInvoice) {
									if (p.getProduct().equals(pod.getProduct())) {
										p.setQuantity(Double.sum(p.getQuantity(), pod.getQuantity()));
										continue lv1;
									}
								}
								listPromotionOrderDetailInvoice.add(pod);
							}
						}
					}
				}
			}
			tinhkhoiluong();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showEditOrderDetail() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (orderLixCrud != null && orderLixCrud.getId() != 0) {
				if (orderDetailSelect != null) {
					orderDetailCrud = orderDetailSelect.clone();
					orderDetailCrud.setOrder_lix(orderLixCrud);
					// load danh sách sản phẩm chương trình khuyến mãi
					listPromotionProgramDetailCrud = new ArrayList<>();
					if (orderLixCrud.getPromotion_program() != null) {
						// load danh sách sản phẩm chương trình khuyến mãi
						/*
						 * {product_id:0,promotion_product_id:0,promotion_program_id
						 * :0,promotion_form:-1}
						 */
						JsonObject json = new JsonObject();
						json.addProperty("product_id", orderDetailCrud.getProduct().getId());
						json.addProperty("promotion_program_id", orderLixCrud.getPromotion_program().getId());
						json.addProperty("promotion_form", -1);
						promotionProgramDetailService.selectBy(JsonParserUtil.getGson().toJson(json),
								listPromotionProgramDetailCrud);
					}
					current.executeScript("PF('dlgc1').show();");
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo', 'Chưa chọn dòng để chỉnh sửa!','warning',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo', 'Đơn hàng chưa được lưu!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.showEditOrderDetail:" + e.getMessage(), e);
		}
	}

	public void showDialogCar() {
		PrimeFaces current = PrimeFaces.current();
		try {
			listCarOwner = new ArrayList<>();
			carOwnerService.selectAll(listCarOwner);
			listCarType = new ArrayList<>();
			carTypeService.selectAll(listCarType);
			createNewCar();
			searchCar();
			current.executeScript("PF('dlgcar').show();");
		} catch (Exception e) {
			logger.error("OrderLixBean.showDialogCar:" + e.getMessage(), e);
		}
	}

	public void showDialogPickHDVC() {
		PrimeFaces current = PrimeFaces.current();
		try {
			fromDateFCFilter = ToolTimeCustomer.getDateMinCustomer(ToolTimeCustomer.getMonthCurrent(),
					ToolTimeCustomer.getYearCurrent());
			toDateFCFilter = ToolTimeCustomer.getDateMaxCustomer(ToolTimeCustomer.getMonthCurrent(),
					ToolTimeCustomer.getYearCurrent());
			contractNoFCFilter = null;
			customerFcFilter = null;
			customerTypesFCFilter = null;
			navigationInfoHDVC = new NavigationInfo();
			navigationInfoHDVC.setCurrentPage(1);
			navigationInfoHDVC.setLimit(pageSizeHDVC);
			navigationInfoHDVC.setMaxIndices(5);
			current.executeScript("PF('dlg2').show();");
		} catch (Exception e) {
			logger.error("OrderLixBean.showDialogPickHDVC:" + e.getMessage(), e);
		}
	}

	public void searchHDVC() {
		try {
			/*
			 * {freight_contract_info:{contract_no:
			 * '',from_date:'',to_date:'',customer_id:0,car_id:0,payment_method_id:0,payment:-1},
			 * page:{page_index:0, page_size:0}}
			 */
			listFreightContractSelect = new ArrayList<>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("contract_no", contractNoFCFilter);
			jsonInfo.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateFCFilter, "dd/MM/yyyy"));
			jsonInfo.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateFCFilter, "dd/MM/yyyy"));
			jsonInfo.addProperty("customer_id", customerFcFilter == null ? 0 : customerFcFilter.getId());
			jsonInfo.addProperty("payment", -1);
			JsonObject json = new JsonObject();
			json.add("freight_contract_info", jsonInfo);
			freightContractService.search(JsonParserUtil.getGson().toJson(json), listFreightContractSelect);
		} catch (Exception e) {
			logger.error("OrderLixBean.searchHDVC:" + e.getMessage(), e);
		}
	}

	public void closeDialogPickHDVC() {

		try {
			listFreightContractSelect = null;
			contractNoFCFilter = null;
			customerFcFilter = null;
			customerTypesFCFilter = null;
			listRowPerPageHDVC = null;
			navigationInfoHDVC = null;
		} catch (Exception e) {
			logger.error("OrderLixBean.closeDialogPickHDVC:" + e.getMessage(), e);
		}
	}

	public void showDialogOrderDetail() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (orderLixCrud != null && orderLixCrud.getId() != 0) {
				orderDetailCrud = new OrderDetail();
				orderDetailCrud.setOrder_lix(orderLixCrud);
				listPromotionProgramDetailCrud = null;
				current.executeScript("PF('dlgc1').show();");
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo', 'Lưu đơn hàng trước khi thêm sản phẩm đơn hàng!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.showDialogOrderDetail:" + e.getMessage(), e);
		}
	}

	public void changeProduct() {
		try {
			listPromotionProgramDetailCrud = new ArrayList<>();
			if (orderDetailCrud != null && orderDetailCrud.getProduct() != null) {
				if (orderLixCrud.getPromotion_program() != null) {
					// Tải và cài đặt một hình thức khuyến mãi mặc định
					JsonObject json = new JsonObject();
					json.addProperty("product_id", orderDetailCrud.getProduct().getId());
					json.addProperty("promotion_program_id", orderLixCrud.getPromotion_program().getId());
					json.addProperty("promotion_form", -1);
					promotionProgramDetailService.selectBy(JsonParserUtil.getGson().toJson(json),
							listPromotionProgramDetailCrud);
					orderDetailCrud.setPromotion_forms(0);
					if (listPromotionProgramDetailCrud.size() > 0) {
						orderDetailCrud.setPromotion_forms(listPromotionProgramDetailCrud.get(0).getPromotion_form());
					}
				}
				orderDetailCrud.setUnit_price(0);
				orderDetailCrud.setUnit_price_goc(0);
				caidatlaidongia(orderDetailCrud);

				// Quy đổi số lượng thùng -> số lượng đơn vị tính
				Product pd = productService.findById(orderDetailCrud.getProduct().getId());
				double quantity = MyMath.roundCustom(orderDetailCrud.getBox_quantity() * pd.getSpecification(), 2);
				orderDetailCrud.setTotal(MyMath.roundCustom(quantity * orderDetailCrud.getUnit_price(), 0));
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.loadInfo:" + e.getMessage(), e);
		}
	}

	private void caidatlaidongia(OrderDetail orderDetailCrud) {
		if (orderLixCrud.getPricing_program() != null) {
			// Tải và cài đặt chương trình đơn giá
			boolean statusPrice = true;
			PricingProgramDetailReqInfo t1 = new PricingProgramDetailReqInfo();
			// Tìm cài đặt chương trình đơn giá con
			long idPriceSub = customerPricingProgramService.selectForCustomerSub(orderLixCrud.getPricing_program()
					.getId(), orderLixCrud.getOrder_date(), orderDetailCrud.getProduct().getId());
			if (idPriceSub != 0) {
				pricingProgramDetailService.findSettingPricing(idPriceSub, orderDetailCrud.getProduct().getId(), t1);

				if (t1.getPricing_program_detail() != null) {
					if (t1.getPricing_program_detail().getUnit_price() != 0) {
						orderDetailCrud.setUnit_price_goc(t1.getPricing_program_detail().getUnit_price());
						double dgsauchietkhau = orderDetailCrud.getUnit_price_goc()
								- (orderDetailCrud.getUnit_price_goc() * orderLixCrud.getChietkhau() / 100);
						orderDetailCrud.setUnit_price(Math.round(dgsauchietkhau));
						double quantity = MyMath.roundCustom(orderDetailCrud.getBox_quantity()
								* orderDetailCrud.getProduct().getSpecification(), 2);
						orderDetailCrud.setTotal(MyMath.roundCustom(quantity * orderDetailCrud.getUnit_price(), 0));
						statusPrice = false;
					}
				}
			}
			// Nếu không có ct đơn giá con -> lấy chương trình đơn giá
			// cha
			if (statusPrice) {
				pricingProgramDetailService.findSettingPricing(orderLixCrud.getPricing_program().getId(),
						orderDetailCrud.getProduct().getId(), t1);
				if (t1.getPricing_program_detail() != null) {
					if (t1.getPricing_program_detail().getUnit_price() != 0) {
						orderDetailCrud.setUnit_price_goc(t1.getPricing_program_detail().getUnit_price());
						double dgsauchietkhau = orderDetailCrud.getUnit_price_goc()
								- (orderDetailCrud.getUnit_price_goc() * orderLixCrud.getChietkhau() / 100);
						orderDetailCrud.setUnit_price(Math.round(dgsauchietkhau));
						double quantity = MyMath.roundCustom(orderDetailCrud.getBox_quantity()
								* orderDetailCrud.getProduct().getSpecification(), 2);
						orderDetailCrud.setTotal(MyMath.roundCustom(quantity * orderDetailCrud.getUnit_price(), 0));
					}
				}
			}
		}
		// tim nạp đơn giá sản phẩm khuyến mãi
		boolean caidgkm = false;
		String[] maspcaidgkm = LoaiMaXuatNhap.makhuyenmailaygiavon;
		if (orderLixCrud.getIe_categories() != null)
			for (int i = 0; i < maspcaidgkm.length; i++) {
				if (maspcaidgkm[i].equals(orderLixCrud.getIe_categories().getCode())
						&& orderLixCrud.getTax_value() == 0) {
					caidgkm = true;
					break;
				}
			}
		if (caidgkm) {
			orderDetailCrud.setUnit_price(0);
			orderDetailCrud.setUnit_price_goc(0);
			PromotionalPricingReqInfo ppr = new PromotionalPricingReqInfo();
			JsonObject json = new JsonObject();
			json.addProperty("date", ToolTimeCustomer.convertDateToString(orderLixCrud.getOrder_date(), "dd/MM/yyyy"));
			json.addProperty("product_id", orderDetailCrud.getProduct().getId());
			promotionalPricingService.findSettingPromotionalPricing(JsonParserUtil.getGson().toJson(json), ppr);
			if (ppr.getPromotional_pricing() != null) {
				orderDetailCrud.setUnit_price(ppr.getPromotional_pricing().getUnit_price());
				orderDetailCrud.setUnit_price_goc(ppr.getPromotional_pricing().getUnit_price());
			}
		}
	}

	public void selectPromotionForm(PromotionProgramDetail item) {
		try {
			if (orderDetailCrud != null) {
				orderDetailCrud.setPromotion_forms(item.getPromotion_form());
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.selectPromotionForm:" + e.getMessage(), e);
		}
	}

	public void changeBoxOrderDetail() {
		try {
			if (orderDetailCrud != null) {
				if (orderDetailCrud.getProduct() != null) {
					ProductReqInfo pr = new ProductReqInfo();
					productService.selectById(orderDetailCrud.getProduct().getId(), pr);
					double quantity = MyMath.roundCustom(orderDetailCrud.getBox_quantity()
							* pr.getProduct().getSpecification(), 2);
					orderDetailCrud.setTotal(MyMath.roundCustom(quantity * orderDetailCrud.getUnit_price(), 0));
				} else {
					orderDetailCrud.setQuantity(0);
					orderDetailCrud.setTotal(0);
				}
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.changeBoxOrderDetail:" + e.getMessage(), e);
		}
	}

	public void changePricingProgramAction() {
		try {
			this.changePricingProgram = true;
		} catch (Exception e) {
			logger.error("OrderLixBean.changePricingProgramAction:" + e.getMessage(), e);
		}
	}

	public void changePromotionProgramAction() {
		try {
			this.changePromotionProgram = true;
		} catch (Exception e) {
			logger.error("OrderLixBean.changePromotionProgramAction:" + e.getMessage(), e);
		}
	}

	public void onSort(SortEvent event) {
		DataTable dataTable = (DataTable) event.getSource();

		String columnsort = dataTable.getSortField();
		boolean isAscending = event.isAscending();
		sortField(columnsort, isAscending);
		int size = listOrderDetail.size();
		for (int i = 0; i < size; i++) {
			listOrderDetail.get(i).setIndexsort(i);
			listOrderDetail.get(i).setColumnsort(columnsort);
			listOrderDetail.get(i).setAscd(isAscending);
			orderDetailService.updateSort(listOrderDetail.get(i));

		}
		loadOrderLixDetail();
	}

	public void sortField(String field, boolean isAscending) {
		if ("id".equals(field)) {
			if (isAscending) {
				listOrderDetail.sort(Comparator.comparingLong(OrderDetail::getId));
			} else {
				listOrderDetail.sort(Comparator.comparingLong(OrderDetail::getId).reversed());
			}
		} else if ("product.product_code".equals(field)) {
			if (isAscending) {
				Collections.sort(listOrderDetail, Comparator.comparing(o -> o.getProduct().getProduct_code()));
			} else {
				Collections.sort(listOrderDetail,
						Comparator.comparing(o -> o.getProduct().getProduct_code(), Comparator.reverseOrder()));
			}
		} else if ("product.product_name".equals(field)) {
			if (isAscending) {
				Collections.sort(listOrderDetail, Comparator.comparing(o -> o.getProduct().getProduct_name()));
			} else {
				Collections.sort(listOrderDetail,
						Comparator.comparing(o -> o.getProduct().getProduct_name(), Comparator.reverseOrder()));
			}
		}
	}

	public void saveOrUpdateOrderDetail() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (orderDetailCrud != null && orderLixCrud != null && orderLixCrud.getId() != 0) {
				// kiểm tra thông tin có đầy đủ không
				Product product = orderDetailCrud.getProduct();
				double box = orderDetailCrud.getBox_quantity();
				if (product != null && box != 0) {
					OrderDetail cloned = orderDetailCrud.clone();
					WrapPMOrderDetailReqInfo wrap = new WrapPMOrderDetailReqInfo(cloned, account.getMember().getName());
					if (orderDetailCrud.getId() == 0) {
						if (allowSave(null)) {
							if (listOrderDetail.size() != 0 && listOrderDetail.get(0).getColumnsort() != null) {
								cloned.setAscd(listOrderDetail.get(0).isAscd());
								cloned.setColumnsort(listOrderDetail.get(0).getColumnsort());
								cloned.setIndexsort(listOrderDetail.size());
							}

							Message message = new Message();
							int code = processLogicOrderService.insertOrUpdateOrderDetail(wrap, message);
							if (code == 0) {
								// load lại chi tiết đơn hàng
								OrderDetailReqInfo orderDetailReqInfo = new OrderDetailReqInfo();
								orderDetailService.selectById(cloned.getId(), orderDetailReqInfo);
								listOrderDetail.add(orderDetailReqInfo.getOrder_detail());

								// load lai sản phẩm khuyến mãi đơn hàng
								listPromotionOrderDetail = new ArrayList<>();
								promotionOrderDetailService.selectByOrder(orderLixCrud.getId(),
										listPromotionOrderDetail);
								createNewOrderDetail();
								success();
							} else {
								// đưa ra mã lỗi
								String m = message.getUser_message() + " \\n" + message.getInternal_message();
								current.executeScript("swaldesignclose('Xảy ra lỗi', '" + m + "','warning');");
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					} else {
						if (allowUpdate(null)) {
							cloned.setLast_modifed_by(account.getMember().getName());
							cloned.setLast_modifed_date(new Date());
							Message message = new Message();
							int code = processLogicOrderService.insertOrUpdateOrderDetail(wrap, message);
							if (code == 0) {
								// load lại chi tiết đơn hàng
								OrderDetailReqInfo orderDetailReqInfo = new OrderDetailReqInfo();
								orderDetailService.selectById(cloned.getId(), orderDetailReqInfo);
								orderDetailCrud = orderDetailReqInfo.getOrder_detail().clone();
								listOrderDetail.set(listOrderDetail.indexOf(orderDetailCrud),
										orderDetailReqInfo.getOrder_detail());

								// load lai sản phẩm khuyến mãi đơn hàng
								listPromotionOrderDetail = new ArrayList<>();
								promotionOrderDetailService.selectByOrder(orderLixCrud.getId(),
										listPromotionOrderDetail);
								createNewOrderDetail();
								success();
							} else {
								// đưa ra mã lỗi
								String m = message.getUser_message() + " \\n" + message.getInternal_message();
								current.executeScript("swaldesignclose('Xảy ra lỗi', '" + m + "','warning');");
							}

						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo', 'Thông tin chi tiết đơn hàng không đầy đủ, điền đủ thông tin chứa(*)','warning',2000);");
				}
				sumOrderLix();
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.saveOrUpdate:" + e.getMessage(), e);
		}
		// clear filter
		current.executeScript("PF('tablect').clearFilters();");
		current.executeScript("PF('tablekm').clearFilters();");
	}

	public void createNewOrderDetail() {
		try {
			orderDetailCrud = new OrderDetail();
			orderDetailCrud.setOrder_lix(orderLixCrud);
			listPromotionProgramDetailCrud = null;
		} catch (Exception e) {
			logger.error("OrderLixBean.createNewOrderDetail:" + e.getMessage(), e);
		}
	}

	public void deleteOrderLixDetail() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (orderLixCrud != null && orderLixCrud.getId() != 0) {
				if (orderDetailSelect != null) {
					if (allowDelete(null)) {
						Message message = new Message();
						int code = processLogicOrderService.deleteOrderDetail(orderDetailSelect.getId(), message);
						if (code == 0) {
							success();
							listPromotionOrderDetail = new ArrayList<>();
							promotionOrderDetailService.selectByOrder(orderDetailSelect.getOrder_lix().getId(),
									listPromotionOrderDetail);
							listOrderDetail.remove(orderDetailSelect);
							sumOrderLix();
						} else {
							// đưa ra mã lỗi
							String m = message.getUser_message() + " \\n" + message.getInternal_message();
							current.executeScript("swaldesignclose('Xảy ra lỗi', '" + m + "','warning');");
						}
					} else {
						current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo', 'Không xóa được, chưa chọn phiếu','warning',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo', 'Đơn hàng chưa được lưu','error',2000);");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.deleteOrderLixDetail:" + e.getMessage(), e);
		}
		// clear filter
		current.executeScript("PF('tablect').clearFilters();");
		current.executeScript("PF('tablekm').clearFilters();");
	}

	public void calPromotionProduct() {
		try {
		} catch (Exception e) {
			logger.error("OrderLixBean.calSanPhamKhuyenMai:" + e.getMessage(), e);
		}
	}

	public double sumOrderLix() {
		double total = 0;
		try {
			if (listOrderDetail != null && listOrderDetail.size() > 0) {
				double totalevent = listOrderDetail.stream().mapToDouble(f -> f.getTotal()).sum();
				total = MyMath.round(totalevent);
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.sumOrderLix:" + e.getMessage(), e);
		}
		return total;
	}

	public double sumOrderLixVAT() {
		double vat = 0;
		try {
			if (listOrderDetail != null && listOrderDetail.size() > 0) {
				double totalevent = listOrderDetail.stream().mapToDouble(f -> f.getTotal()).sum();
				vat = MyMath.round(totalevent * orderLixCrud.getTax_value());
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.sumOrderLix:" + e.getMessage(), e);
		}
		return vat;
	}

	public void copyOrderLix() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (orderLixCrud != null && orderLixCrud.getId() != 0) {
				orderLixCrud.setId(0);
				orderLixCrud.setOrder_code(null);
				orderLixCrud.setVoucher_code(null);
				orderLixCrud.setNpp_order_id(null);
				orderLixCrud.setIdfox(null);
				orderLixCrud.setStatus(0);
				orderLixCrud.setDelivered(false);
				for (OrderDetail d : listOrderDetail) {
					d.setId(0);
					d.setOrder_lix(orderLixCrud);
					// d.setPromotion_forms(0);
					d.setNote(null);
					d.setLast_modifed_by(null);
					d.setLast_modifed_date(null);
					d.setNpp_order_detail_id(null);
					d.setBox_quantity_actual(0);
					d.setQuantity(0);
					double quantity = MyMath.roundCustom(d.getBox_quantity() * d.getProduct().getSpecification(), 2);
					d.setTotal(MyMath.roundCustom(quantity * d.getUnit_price(), 0));
					d.setIdfox(null);
				}
				listPromotionOrderDetail = new ArrayList<>();
				success("Nhấn nút 'Lưu/Cập nhật' để lưu phiếu copy");
			}
		} catch (Exception e) {
			noticeError(e.getMessage());
			logger.error("OrderLixBean.copyOrderLix:" + e.getMessage(), e);
		}
		// clear filter
		current.executeScript("PF('tablect').clearFilters();");
		current.executeScript("PF('tablekm').clearFilters();");
	}

	@Inject
	ParamReportDetailService paramReportDetailService;

	public void baoCaoTongHopPDFExcel(String loaibaocao) {
		PrimeFaces current = PrimeFaces.current();
		try {
			if ("indonhangtonghop".equals(loaibaocao)) {
				List<OrderLix> orderChooses = new ArrayList<OrderLix>();
				Car carChoose = null;
				Customer customer = null;
				for (int i = 0; i < listOrderLix.size(); i++) {
					if (listOrderLix.get(i).isChonphieu()) {
						orderChooses.add(listOrderLix.get(i));
						if ((carChoose != null && !carChoose.equals(listOrderLix.get(i).getCar()))
								|| (customer != null && !customer.equals(listOrderLix.get(i).getCustomer()))) {
							current.executeScript("swaldesigntimer('Không in được.', 'Các phiếu không cùng khách hàng hoặc số xe.','warning',2000);");
							return;
						} else {
							customer = listOrderLix.get(i).getCustomer();
							carChoose = listOrderLix.get(i).getCar();
						}
					}
				}
				if (orderChooses.size() != 0) {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/donhang/donhangtonghop.jasper");
					StringBuilder sophieu = new StringBuilder();
					String ngay = "";
					String tenkh = customer != null ? customer.getCustomer_name() : "";
					String ghichu = "";
					String maso = "";
					String soxe = carChoose != null ? carChoose.getLicense_plate() : "";
					String makh = customer != null ? customer.getCustomer_code() : "";
					String hoadon = "";
					List<SanPhamDonHang> spchinhs = new ArrayList<SanPhamDonHang>();
					List<Object[]> objspchinhs = orderDetailService.selectByOrders(orderChooses);
					double sumthung = 0;
					double trongluongtong = 0;
					for (int i = 0; i < objspchinhs.size(); i++) {
						SanPhamDonHang sp = new SanPhamDonHang();
						sp.setStt(i + 1);
						sp.setTensanpham((String) objspchinhs.get(i)[0]);
						sp.setSothung((double) objspchinhs.get(i)[1]);
						sumthung += sp.getSothung();
						spchinhs.add(sp);
						double trongluong = (double) objspchinhs.get(i)[2];
						double quydoi = (double) objspchinhs.get(i)[3];
						trongluongtong += trongluong * sp.getSothung() * quydoi;
					}

					List<SanPhamDonHang> spkms = new ArrayList<SanPhamDonHang>();
					List<Object[]> objspkhuyenmais = promotionOrderDetailService.selectByOrders(orderChooses);
					for (int i = 0; i < objspkhuyenmais.size(); i++) {
						SanPhamDonHang sp = new SanPhamDonHang();
						sp.setStt(i + 1);
						sp.setTensanpham((String) objspkhuyenmais.get(i)[0]);
						sp.setSothung((double) objspkhuyenmais.get(i)[1]);
						spkms.add(sp);
						double trongluong = (double) objspkhuyenmais.get(i)[2];
						trongluongtong += trongluong * sp.getSothung();
					}
					for (int i = 0; i < orderChooses.size(); i++) {
						sophieu.append(orderChooses.get(i).getVoucher_code());
						if (i + 1 < orderChooses.size())
							sophieu.append(";");
						if (i == 0) {
							ngay = MyUtil.chuyensangStr(orderChooses.get(i).getOrder_date());
							ghichu = orderChooses.get(i).getNote();
						}
					}

					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);

					List<ParamReportDetail> listConfig = paramReportDetailService
							.findByParamReportName("thongtinchung");
					for (ParamReportDetail p : listConfig) {
						importParam.put(p.getKey(), p.getValue());
					}

					importParam.put("spchinhs", spchinhs);
					importParam.put("spkhuyenmais", spkms);
					importParam.put("sophieu", sophieu.toString());
					importParam.put("ngay", ngay);
					importParam.put("tenkh", tenkh);
					importParam.put("ghichu", ghichu);
					importParam.put("maso", maso);
					importParam.put("soxe", soxe);
					importParam.put("makh", makh);
					importParam.put("hoadon", hoadon);
					importParam.put("sumthung", sumthung);
					importParam.put("trongluongtong", trongluongtong);

					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JREmptyDataSource());
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("utility.printPDF('" + ba + "')");

				} else {
					current.executeScript("swaldesigntimer('Không in được', 'Không có đơn hàng được chọn','warning',2000);");
				}
			} else if ("thukhotonghop".equals(loaibaocao)) {
				List<OrderLix> orderChooses = new ArrayList<OrderLix>();
				for (int i = 0; i < listOrderLix.size(); i++) {
					if (listOrderLix.get(i).isChonphieu()) {
						orderChooses.add(listOrderLix.get(i));
					}
				}
				if (orderChooses.size() != 0 && orderChooses.size() <= 3) {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/donhang/thukhotonghop.jasper");

					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);
					List<ParamReportDetail> listConfig = paramReportDetailService
							.findByParamReportName("thongtinchung");
					for (ParamReportDetail p : listConfig) {
						importParam.put(p.getKey(), p.getValue());
					}

					// Khach hang 1
					String sophieu1 = orderChooses.get(0).getVoucher_code();
					String tenkh1 = orderChooses.get(0).getCustomer() != null ? orderChooses.get(0).getCustomer()
							.getCustomer_name() : "";
					String ghichu1 = orderChooses.get(0).getNote();
					String soxe1 = orderChooses.get(0).getCar() != null ? orderChooses.get(0).getCar()
							.getLicense_plate() : "";
					String makh1 = orderChooses.get(0).getCustomer() != null ? orderChooses.get(0).getCustomer()
							.getCustomer_code() : "";

					importParam.put("sophieu1", sophieu1);
					importParam.put("tenkh1", tenkh1);
					importParam.put("ghichu1", ghichu1);
					importParam.put("soxe1", soxe1);
					importParam.put("makh1", makh1);

					List<SanPhamDonHang> spchinhs = new ArrayList<SanPhamDonHang>();
					List<Object[]> objspchinhs = orderDetailService.selectByOrder(orderChooses.get(0));
					List<SanPhamDonHang> spkms = new ArrayList<SanPhamDonHang>();
					List<Object[]> objspkhuyenmais = promotionOrderDetailService.selectByOrder(orderChooses.get(0));

					for (int i = 0; i < objspchinhs.size(); i++) {
						SanPhamDonHang sp = new SanPhamDonHang();
						sp.setTensanpham((String) objspchinhs.get(i)[0]);
						sp.setSothung((double) objspchinhs.get(i)[1]);
						sp.setTrongluong((double) objspchinhs.get(i)[2]);
						sp.setQuydoi((double) objspchinhs.get(i)[3]);
						spchinhs.add(sp);

					}
					for (int i = 0; i < objspkhuyenmais.size(); i++) {
						SanPhamDonHang sp = new SanPhamDonHang();
						sp.setTensanpham((String) objspkhuyenmais.get(i)[0]);
						sp.setSothung((double) objspkhuyenmais.get(i)[1]);
						sp.setTrongluong((double) objspchinhs.get(i)[2]);
						sp.setQuydoi((double) objspchinhs.get(i)[3]);
						spkms.add(sp);
					}

					if (orderChooses.size() > 1) {
						// Khach hang 2
						String sophieu2 = orderChooses.get(1).getVoucher_code();
						String tenkh2 = orderChooses.get(1).getCustomer() != null ? orderChooses.get(1).getCustomer()
								.getCustomer_name() : "";
						String ghichu2 = orderChooses.get(1).getNote();
						String soxe2 = orderChooses.get(1).getCar() != null ? orderChooses.get(1).getCar()
								.getLicense_plate() : "";
						String makh2 = orderChooses.get(1).getCustomer() != null ? orderChooses.get(1).getCustomer()
								.getCustomer_code() : "";

						importParam.put("sophieu2", sophieu2);
						importParam.put("tenkh2", tenkh2);
						importParam.put("ghichu2", ghichu2);
						importParam.put("soxe2", soxe2);
						importParam.put("makh2", makh2);

						List<Object[]> objspchinh1s = orderDetailService.selectByOrder(orderChooses.get(1));
						List<Object[]> objspkhuyenmai1s = promotionOrderDetailService
								.selectByOrder(orderChooses.get(1));
						for (int i = 0; i < objspchinh1s.size(); i++) {
							boolean status = true;
							for (int j = 0; j < spchinhs.size(); j++) {
								if (spchinhs.get(j).equals((String) objspchinh1s.get(i)[0])) {
									spchinhs.get(j).setSothung2((double) objspchinh1s.get(i)[1]);
									status = false;
									break;
								}
							}
							if (status) {
								SanPhamDonHang sp = new SanPhamDonHang();
								sp.setTensanpham((String) objspchinh1s.get(i)[0]);
								sp.setSothung2((double) objspchinh1s.get(i)[1]);
								sp.setTrongluong((double) objspchinh1s.get(i)[2]);
								sp.setQuydoi((double) objspchinh1s.get(i)[3]);
								spchinhs.add(sp);
							}

						}
						for (int i = 0; i < objspkhuyenmai1s.size(); i++) {
							boolean status = true;
							for (int j = 0; j < spkms.size(); j++) {
								if (spkms.get(j).equals((String) objspkhuyenmai1s.get(i)[0])) {
									spkms.get(j).setSothung2((double) objspkhuyenmai1s.get(i)[1]);
									status = false;
									break;
								}
							}
							if (status) {
								SanPhamDonHang sp = new SanPhamDonHang();
								sp.setTensanpham((String) objspkhuyenmai1s.get(i)[0]);
								sp.setSothung2((double) objspkhuyenmai1s.get(i)[1]);
								sp.setTrongluong((double) objspkhuyenmai1s.get(i)[2]);
								sp.setQuydoi((double) objspkhuyenmai1s.get(i)[3]);
								spchinhs.add(sp);
							}

						}

						if (orderChooses.size() > 2) {
							String sophieu3 = orderChooses.get(2).getVoucher_code();
							String tenkh3 = orderChooses.get(2).getCustomer() != null ? orderChooses.get(2)
									.getCustomer().getCustomer_name() : "";
							String ghichu3 = orderChooses.get(2).getNote();
							String soxe3 = orderChooses.get(2).getCar() != null ? orderChooses.get(2).getCar()
									.getLicense_plate() : "";
							String makh3 = orderChooses.get(2).getCustomer() != null ? orderChooses.get(2)
									.getCustomer().getCustomer_code() : "";

							importParam.put("sophieu3", sophieu3);
							importParam.put("tenkh3", tenkh3);
							importParam.put("ghichu3", ghichu3);
							importParam.put("soxe3", soxe3);
							importParam.put("makh3", makh3);

							List<Object[]> objspchinh2s = orderDetailService.selectByOrder(orderChooses.get(2));
							List<Object[]> objspkhuyenmai2s = promotionOrderDetailService.selectByOrder(orderChooses
									.get(2));
							for (int i = 0; i < objspchinh2s.size(); i++) {
								boolean status = true;
								for (int j = 0; j < spchinhs.size(); j++) {
									if (spchinhs.get(j).equals((String) objspchinh2s.get(i)[0])) {
										spchinhs.get(j).setSothung3((double) objspchinh2s.get(i)[1]);
										status = false;
										break;
									}
								}
								if (status) {
									SanPhamDonHang sp = new SanPhamDonHang();
									sp.setTensanpham((String) objspchinh2s.get(i)[0]);
									sp.setSothung3((double) objspchinh2s.get(i)[1]);
									sp.setTrongluong((double) objspchinh2s.get(i)[2]);
									sp.setQuydoi((double) objspchinh2s.get(i)[3]);
									spchinhs.add(sp);
								}

							}
							for (int i = 0; i < objspkhuyenmai2s.size(); i++) {
								boolean status = true;
								for (int j = 0; j < spkms.size(); j++) {
									if (spkms.get(j).equals((String) objspkhuyenmai2s.get(i)[0])) {
										spkms.get(j).setSothung3((double) objspkhuyenmai2s.get(i)[1]);
										status = false;
										break;
									}
								}
								if (status) {
									SanPhamDonHang sp = new SanPhamDonHang();
									sp.setTensanpham((String) objspkhuyenmai2s.get(i)[0]);
									sp.setSothung3((double) objspkhuyenmai2s.get(i)[1]);
									sp.setTrongluong((double) objspkhuyenmai2s.get(i)[2]);
									sp.setQuydoi((double) objspkhuyenmai2s.get(i)[3]);
									spchinhs.add(sp);
								}

							}

						}

					}
					double sumthung = 0;
					double trongluongtong = 0;
					for (int i = 0; i < spchinhs.size(); i++) {
						spchinhs.get(i).setStt(i + 1);
						double sthung = spchinhs.get(i).getSothung() + spchinhs.get(i).getSothung2()
								+ spchinhs.get(i).getSothung3();
						sumthung += sthung;
						trongluongtong += spchinhs.get(i).getTrongluong() * sthung * spchinhs.get(i).getQuydoi();
					}
					for (int i = 0; i < spkms.size(); i++) {
						spkms.get(i).setStt(i + 1);
						double sthung = spkms.get(i).getSothung() + spkms.get(i).getSothung2()
								+ spkms.get(i).getSothung3();
						trongluongtong += spkms.get(i).getTrongluong() * sthung;
					}

					importParam.put("spchinhs", spchinhs);
					importParam.put("spkhuyenmais", spkms);
					importParam.put("sumthung", sumthung);
					importParam.put("trongluongtong", trongluongtong);

					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JREmptyDataSource());
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("utility.printPDF('" + ba + "')");

				} else {
					current.executeScript("swaldesigntimer('Không in được', 'Không có đơn hàng được chọn hoặc quá 3 đơn hàng.','warning',2000);");
				}
			} else if ("tonghopdonhang".equals(loaibaocao)) {
				List<OrderLix> orderChooses = new ArrayList<OrderLix>();
				for (int i = 0; i < listOrderLix.size(); i++) {
					if (listOrderLix.get(i).isChonphieu()) {
						orderChooses.add(listOrderLix.get(i));
					}
				}
				if (orderChooses.size() != 0) {
					String reportPath = FacesContext.getCurrentInstance().getExternalContext()
							.getRealPath("/resources/reports/donhang/tonghopdonhang.jasper");
					List<SanPhamDonHang> spchinhs = new ArrayList<SanPhamDonHang>();
					List<Object[]> objspchinhs = orderDetailService.selectByOrders(orderChooses);
					double sumthung = 0;
					double trongluongtong = 0;
					for (int i = 0; i < objspchinhs.size(); i++) {
						SanPhamDonHang sp = new SanPhamDonHang();
						sp.setStt(i + 1);
						sp.setTensanpham((String) objspchinhs.get(i)[0]);
						sp.setSothung((double) objspchinhs.get(i)[1]);
						sumthung += sp.getSothung();
						spchinhs.add(sp);
						double trongluong = (double) objspchinhs.get(i)[2];
						double quydoi = (double) objspchinhs.get(i)[3];
						trongluongtong += trongluong * sp.getSothung() * quydoi;
					}

					List<SanPhamDonHang> spkms = new ArrayList<SanPhamDonHang>();
					List<Object[]> objspkhuyenmais = promotionOrderDetailService.selectByOrders(orderChooses);
					for (int i = 0; i < objspkhuyenmais.size(); i++) {
						SanPhamDonHang sp = new SanPhamDonHang();
						sp.setStt(i + 1);
						sp.setTensanpham((String) objspkhuyenmais.get(i)[0]);
						sp.setSothung((double) objspkhuyenmais.get(i)[1]);
						spkms.add(sp);
						double trongluong = (double) objspkhuyenmais.get(i)[2];
						trongluongtong += trongluong * sp.getSothung();
					}

					Map<String, Object> importParam = new HashMap<String, Object>();
					Locale locale = new Locale("vi", "VI");
					importParam.put(JRParameter.REPORT_LOCALE, locale);
					List<ParamReportDetail> listConfig = paramReportDetailService
							.findByParamReportName("thongtinchung");
					for (ParamReportDetail p : listConfig) {
						importParam.put(p.getKey(), p.getValue());
					}
					importParam.put("spchinhs", spchinhs);
					importParam.put("spkhuyenmais", spkms);
					importParam.put("sumthung", sumthung);
					importParam.put("trongluongtong", trongluongtong);
					JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, importParam,
							new JREmptyDataSource());
					byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
					String ba = Base64.getEncoder().encodeToString(data);
					current.executeScript("utility.printPDF('" + ba + "')");

				} else {
					current.executeScript("swaldesigntimer('Không in được', 'Không có đơn hàng được chọn','warning',2000);");
				}
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.exportPDF:" + e.getMessage(), e);
		}
	}

	final int NUMBERTEXTNAME_CUS = 80;

	public void exportPDF() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (orderLixCrud != null && orderLixCrud.getId() > 0) {
				String reportPath = "";
				String reportPath2 = "";
				reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/donhang/donhang.jasper");
				reportPath2 = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/donhang/donhang2.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
				List<ParamReportDetail> listConfig = paramReportDetailService.findByParamReportName("thongtinchung");
				for (ParamReportDetail p : listConfig) {
					importParam.put(p.getKey(), p.getValue());
				}
				importParam.put("voucher_code", orderLixCrud.getVoucher_code());
				importParam.put("order_date",
						ToolTimeCustomer.convertDateToString(orderLixCrud.getOrder_date(), "dd/MM/yyyy"));
				importParam.put("license_plate", orderLixCrud.getCar() == null ? "" : orderLixCrud.getCar()
						.getLicense_plate());
				// Trọng tải
				List<OrderDetail> delOrds = new ArrayList<OrderDetail>();
				double trongtai = 0;
				for (int i = 0; i < listOrderDetail.size(); i++) {
					if (listOrderDetail.get(i).isHuyct()) {
						delOrds.add(listOrderDetail.get(i));
					} else {
						trongtai += listOrderDetail.get(i).getBox_quantity()
								* listOrderDetail.get(i).getProduct().getSpecification()
								* listOrderDetail.get(i).getProduct().getFactor();
					}

				}
				listOrderDetail.removeAll(delOrds);

				String customerName = orderLixCrud.getCustomer().getCustomer_name();
				// slipt NUMBERTEXTNAME_CUS ký tự
				String line1 = "";
				String line2 = "";
				if (customerName.length() > NUMBERTEXTNAME_CUS) {
					String customersub = customerName.substring(0, NUMBERTEXTNAME_CUS);
					char last = customerName.charAt(NUMBERTEXTNAME_CUS);
					if (last != ' ') {
						// tìm last space trong đoạn NUMBERTEXTNAME_CUS
						int space = customersub.lastIndexOf(' ');
						line1 = customersub.substring(0, space);
						line2 = customerName.substring(space);
					} else {
						line1 = customersub;
						line2 = customerName.substring(NUMBERTEXTNAME_CUS);
					}
				} else {
					line1 = customerName;
				}
				importParam.put("customer_name1", line1);
				importParam.put("customer_name2", line2);
				importParam.put("customer_code", orderLixCrud.getCustomer().getCustomer_code());
				importParam.put("note", orderLixCrud.getNote());
				importParam.put("listOrderDetailN", listOrderDetail);

				List<PromotionOrderDetail> delOrdPromos = new ArrayList<PromotionOrderDetail>();
				for (int i = 0; i < listPromotionOrderDetail.size(); i++) {
					if (listPromotionOrderDetail.get(i).isHuyct()) {
						delOrdPromos.add(listPromotionOrderDetail.get(i));
					}
				}
				listPromotionOrderDetail.removeAll(delOrdPromos);

				List<PromotionOrderDetail> listPromoParams = new ArrayList<PromotionOrderDetail>();
				Map<Product, List<PromotionOrderDetail>> datagroups1 = listPromotionOrderDetail.stream().collect(
						Collectors.groupingBy(p -> p.getProduct(), Collectors.toList()));
				for (Product key : datagroups1.keySet()) {
					List<PromotionOrderDetail> invs = datagroups1.get(key);
					double quantityTotal = invs.stream().mapToDouble(f -> (int) f.getQuantity()).sum();
					PromotionOrderDetail prd = new PromotionOrderDetail();
					prd.setProduct(key);
					prd.setQuantity(quantityTotal);
					listPromoParams.add(prd);
					trongtai += prd.getQuantity() * key.getFactor();
				}
				importParam.put("trongtai", (double) MyMath.round(trongtai));
				importParam.put("listPromotionOrderDetailN", listPromoParams);
				JasperPrint jasperPrint = JasperFillManager
						.fillReport(reportPath, importParam, new JREmptyDataSource());
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				List<byte[]> listByte = new ArrayList<>();
				listByte.add(data);
				// JasperPrint jasperPrint2 =
				// JasperFillManager.fillReport(reportPath2, importParam,
				// new JREmptyDataSource());
				// byte[] data2 =
				// JasperExportManager.exportReportToPdf(jasperPrint2);
				// listByte.add(data2);
				if (!"".equals(reportPath2)) {
					JasperPrint jasperPrint3 = JasperFillManager.fillReport(reportPath2, importParam,
							new JREmptyDataSource());
					byte[] data3 = JasperExportManager.exportReportToPdf(jasperPrint3);
					listByte.add(data3);
				}
				byte[] lastData = MyUtilPDF.mergePDF(listByte);
				String ba = Base64.getEncoder().encodeToString(lastData);
				current.executeScript("utility.printPDF('" + ba + "')");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.exportPDF:" + e.getMessage(), e);
		}
	}
	public void exportPDFSub() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (orderLixCrud != null && orderLixCrud.getId() > 0) {
				String reportPath = "";
				String reportPath2 = "";
				reportPath = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/donhang/donhang.jasper");
				reportPath2 = FacesContext.getCurrentInstance().getExternalContext()
						.getRealPath("/resources/reports/donhang/donhang2.jasper");
				Map<String, Object> importParam = new HashMap<String, Object>();
				Locale locale = new Locale("vi", "VI");
				importParam.put(JRParameter.REPORT_LOCALE, locale);
				List<ParamReportDetail> listConfig = paramReportDetailService.findByParamReportName("thongtinchung");
				for (ParamReportDetail p : listConfig) {
					importParam.put(p.getKey(), p.getValue());
				}
				importParam.put("voucher_code", orderLixCrud.getVoucher_code());
				importParam.put("order_date",
						ToolTimeCustomer.convertDateToString(orderLixCrud.getOrder_date(), "dd/MM/yyyy"));
				importParam.put("license_plate", orderLixCrud.getCar() == null ? "" : orderLixCrud.getCar()
						.getLicense_plate());
				// Trọng tải
				List<OrderDetail> listOrderDetail = new ArrayList<OrderDetail>();
				double trongtai = khoiluongxuat;
				for (int i = 0; i < listWrapOrderDetailLixReqInfo.size(); i++) {
					if(listWrapOrderDetailLixReqInfo.get(i).getExport_unit_quantity()!=0){
					OrderDetail item=listWrapOrderDetailLixReqInfo.get(i).getOrder_detail().clone();
					item.setBox_quantity(listWrapOrderDetailLixReqInfo.get(i).getExport_quantity());
					item.setQuantity(listWrapOrderDetailLixReqInfo.get(i).getExport_unit_quantity());
					item.setTotal(listWrapOrderDetailLixReqInfo.get(i).getTotal_unit_quantity());
					listOrderDetail.add(item);}

				}
				if(listOrderDetail.size()==0){
					warning("Không có dữ liệu xuất.");
					return;
				}

				String customerName = orderLixCrud.getCustomer().getCustomer_name();
				// slipt NUMBERTEXTNAME_CUS ký tự
				String line1 = "";
				String line2 = "";
				if (customerName.length() > NUMBERTEXTNAME_CUS) {
					String customersub = customerName.substring(0, NUMBERTEXTNAME_CUS);
					char last = customerName.charAt(NUMBERTEXTNAME_CUS);
					if (last != ' ') {
						// tìm last space trong đoạn NUMBERTEXTNAME_CUS
						int space = customersub.lastIndexOf(' ');
						line1 = customersub.substring(0, space);
						line2 = customerName.substring(space);
					} else {
						line1 = customersub;
						line2 = customerName.substring(NUMBERTEXTNAME_CUS);
					}
				} else {
					line1 = customerName;
				}
				importParam.put("customer_name1", line1);
				importParam.put("customer_name2", line2);
				importParam.put("customer_code", orderLixCrud.getCustomer().getCustomer_code());
				importParam.put("note", orderLixCrud.getNote());
				importParam.put("listOrderDetailN", listOrderDetail);

				List<PromotionOrderDetail> listPromotionOrderDetail = new ArrayList<PromotionOrderDetail>();
				for (int i = 0; i < listPromotionOrderDetailInvoice.size(); i++) {
					if (listPromotionOrderDetailInvoice.get(i).isNo()==false) {
						listPromotionOrderDetail.add(listPromotionOrderDetailInvoice.get(i));
					}
				}

				List<PromotionOrderDetail> listPromoParams = new ArrayList<PromotionOrderDetail>();
				Map<Product, List<PromotionOrderDetail>> datagroups1 = listPromotionOrderDetail.stream().collect(
						Collectors.groupingBy(p -> p.getProduct(), Collectors.toList()));
				for (Product key : datagroups1.keySet()) {
					List<PromotionOrderDetail> invs = datagroups1.get(key);
					double quantityTotal = invs.stream().mapToDouble(f -> (int) f.getQuantity()).sum();
					PromotionOrderDetail prd = new PromotionOrderDetail();
					prd.setProduct(key);
					prd.setQuantity(quantityTotal);
					listPromoParams.add(prd);
				}
				importParam.put("trongtai", (double) MyMath.round(trongtai));
				importParam.put("listPromotionOrderDetailN", listPromoParams);
				JasperPrint jasperPrint = JasperFillManager
						.fillReport(reportPath, importParam, new JREmptyDataSource());
				byte[] data = JasperExportManager.exportReportToPdf(jasperPrint);
				List<byte[]> listByte = new ArrayList<>();
				listByte.add(data);
				if (!"".equals(reportPath2)) {
					JasperPrint jasperPrint3 = JasperFillManager.fillReport(reportPath2, importParam,
							new JREmptyDataSource());
					byte[] data3 = JasperExportManager.exportReportToPdf(jasperPrint3);
					listByte.add(data3);
				}
				byte[] lastData = MyUtilPDF.mergePDF(listByte);
				String ba = Base64.getEncoder().encodeToString(lastData);
				current.executeScript("utility.printPDF('" + ba + "')");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.exportPDF:" + e.getMessage(), e);
		}
	}
	public void showDialogUpload() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (orderLixCrud != null && orderLixCrud.getId() != 0) {
				current.executeScript("PF('dlgup1').show();");
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Đơn hàng chưa được lưu hoặc không tồn tại!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.showDialogUpload:" + e.getMessage(), e);
		}
	}

	public void loadFileExcelOrderLix(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		PrimeFaces current = PrimeFaces.current();
		try {
			if (allowUpdate(null)) {
				if (event.getFile() != null) {
					UploadedFile part = event.getFile();
					byte[] byteFile = event.getFile().getContent();
					List<OrderDetail> listDetail = new ArrayList<>();
					Workbook workBook = MyUtilExcel.getWorkbook(new ByteArrayInputStream(byteFile), part.getFileName());
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
						OrderDetail lix = new OrderDetail();
						while (cells.hasNext()) {
							Cell cell = cells.next();
							int columnIndex = cell.getColumnIndex();

							switch (columnIndex) {
							case 1:
								try {
									// masp
									String masp = Objects.toString(MyUtilExcel.getCellValue(cell), null);
									try {
										double maspIsNumber = Double.parseDouble(masp);
										masp=((int) maspIsNumber)+"";
									} catch (Exception e) {
									}
									if (masp != null && !"".equals(masp)) {
										ProductReqInfo p = new ProductReqInfo();
										productService.selectByCode(masp, p);
										Product product = p.getProduct();
										lix.setUnit_price(0);
										lix.setUnit_price_goc(0);
										if (product != null) {
											lix.setProduct(product);
											if (orderLixCrud.getPricing_program() != null) {
												boolean statusPrice = true;
												PricingProgramDetailReqInfo t1 = new PricingProgramDetailReqInfo();
												long idPriceSub = customerPricingProgramService.selectForCustomerSub(
														orderLixCrud.getPricing_program().getId(),
														orderLixCrud.getOrder_date(), lix.getProduct().getId());
												if (idPriceSub != 0) {
													pricingProgramDetailService.findSettingPricing(idPriceSub, lix
															.getProduct().getId(), t1);

													if (t1.getPricing_program_detail() != null) {
														if (t1.getPricing_program_detail().getUnit_price() != 0) {
															lix.setUnit_price(t1.getPricing_program_detail()
																	.getUnit_price());
															lix.setUnit_price_goc(t1.getPricing_program_detail()
																	.getUnit_price());
															statusPrice = false;
														}
													}
												}
												if (statusPrice) {
													pricingProgramDetailService
															.findSettingPricing(orderLixCrud.getPricing_program()
																	.getId(), lix.getProduct().getId(), t1);
													if (t1.getPricing_program_detail() != null) {
														if (t1.getPricing_program_detail().getUnit_price() != 0) {
															lix.setUnit_price(t1.getPricing_program_detail()
																	.getUnit_price());
															lix.setUnit_price_goc(t1.getPricing_program_detail()
																	.getUnit_price());
														}
													}
												}
											}
											// tim nạp đơn giá sản phẩm khuyến
											// mãi
											boolean caidgkm = false;
											String[] maspcaidgkm = LoaiMaXuatNhap.makhuyenmailaygiavon;
											if (orderLixCrud.getIe_categories() != null)
												for (int i = 0; i < maspcaidgkm.length; i++) {
													if (maspcaidgkm[i]
															.equals(orderLixCrud.getIe_categories().getCode())
															&& orderLixCrud.getTax_value() == 0) {
														caidgkm = true;
														break;
													}
												}
											if (caidgkm) {
												lix.setUnit_price(0);
												lix.setUnit_price_goc(0);
												PromotionalPricingReqInfo ppr = new PromotionalPricingReqInfo();
												JsonObject json = new JsonObject();
												json.addProperty(
														"date",
														ToolTimeCustomer.convertDateToString(
																orderLixCrud.getOrder_date(), "dd/MM/yyyy"));
												json.addProperty("product_id", lix.getProduct().getId());
												promotionalPricingService.findSettingPromotionalPricing(JsonParserUtil
														.getGson().toJson(json), ppr);
												if (ppr.getPromotional_pricing() != null) {
													lix.setUnit_price(ppr.getPromotional_pricing().getUnit_price());
													lix.setUnit_price_goc(ppr.getPromotional_pricing().getUnit_price());
												}
											}
										}
									} else {
										continue lv1;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;
							case 3:
								try {
									// số lượng thùng
									String slThung = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
									double thung = Double.parseDouble(slThung);
									// double soluong = MyMath.roundCustom(thung
									// * lix.getProduct().getSpecification(),
									// 0);
									// lix.setQuantity(soluong);
									lix.setBox_quantity(thung);
									// lix.setBox_quantity_actual(thung);

									// sua lai 22/06/2023
									double soluong = MyMath.roundCustom(lix.getBox_quantity()
											* lix.getProduct().getSpecification(), 2);
									lix.setTotal(MyMath.round(soluong * lix.getUnit_price()));
								} catch (Exception e) {
								}
								break;
							case 4:
								String htkm = Objects.toString(MyUtilExcel.getCellValue(cell), "0");
								int km = (int) Double.parseDouble(htkm);
								if (km > 0) {
									lix.setPromotion_forms(km);
									break;
								}
							case 5:
								String malohang = Objects.toString(MyUtilExcel.getCellValue(cell), "");
								lix.setBatch_code(malohang);
								break;
							}
						}
						listDetail.add(lix);
					}
					workBook = null;// free
					for (OrderDetail it : listDetail) {
						it.setOrder_lix(orderLixCrud);
						OrderDetailReqInfo p = new OrderDetailReqInfo(it);
						if (orderDetailService.insert(p) == 0) {
							// saveOrUpdateOrderDetailFox(p.getOrder_detail().getId());
							if (it.getPromotion_forms() != 0 && orderLixCrud.getPromotion_program() != null) {
								// tìm kiếm và lưu danh sách sản phẩm khuyến
								// mãi.
								/*
								 * {product_id:0,promotion_product_id:0,
								 * promotion_program_id:0,promotion_form:0}
								 */

								JsonObject js = new JsonObject();
								js.addProperty("product_id", it.getProduct().getId());
								js.addProperty("promotion_program_id", orderLixCrud.getPromotion_program().getId());
								js.addProperty("promotion_form", it.getPromotion_forms());
								List<PromotionProgramDetail> list = new ArrayList<>();
								promotionProgramDetailService.selectBy(JsonParserUtil.getGson().toJson(js), list);
								for (PromotionProgramDetail ppd : list) {
									PromotionOrderDetail pod = new PromotionOrderDetail();
									pod.setOrder_detail(p.getOrder_detail());
									pod.setProduct(ppd.getPromotion_product());
									// tim nạp đơn giá sản phẩm khuyến mãi
									PromotionalPricingReqInfo ppr = new PromotionalPricingReqInfo();
									JsonObject json = new JsonObject();
									json.addProperty(
											"date",
											ToolTimeCustomer.convertDateToString(p.getOrder_detail().getOrder_lix()
													.getOrder_date(), "dd/MM/yyyy"));
									json.addProperty("product_id", ppd.getPromotion_product().getId());
									promotionalPricingService.findSettingPromotionalPricing(JsonParserUtil.getGson()
											.toJson(json), ppr);
									if (ppr.getPromotional_pricing() != null) {
										pod.setUnit_price(ppr.getPromotional_pricing().getUnit_price());
									}
									double quantity = BigDecimal.valueOf(it.getBox_quantity())
											.multiply(BigDecimal.valueOf(ppd.getSpecification()))
											.divide(BigDecimal.valueOf(ppd.getBox_quatity()), MathContext.DECIMAL32)
											.doubleValue();
									pod.setQuantity((int) quantity);
									pod.setCreated_date(new Date());
									pod.setCreated_by(account.getMember().getName());
									promotionOrderDetailService.insert(new PromotionOrderDetailReqInfo(pod));

								}
							}
						}

					}
					// show thông tin chi tiết đơn hàng
					listOrderDetail = new ArrayList<>();
					orderDetailService.selectByOrder(orderLixCrud.getId(), listOrderDetail);
					// show thông tin sản phẩm khuyến khuyến mãi của đơn hàng
					listPromotionOrderDetail = new ArrayList<>();
					promotionOrderDetailService.selectByOrder(orderLixCrud.getId(), listPromotionOrderDetail);
					sumOrderLix();
					if (!tabOrder) {
						tabInvoiceOnClick();
					}
					// clear filter
					current.executeScript("PF('tablect').clearFilters();");
					current.executeScript("PF('tablekm').clearFilters();");
					notify.success();
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
			}
		} catch (Exception e) {
			logger.error("OrderLixBean.loadFileExcel:" + e.getMessage(), e);
		}
	}

	public void capnhattrangthaiDH() {
		List<OrderLix> orderChooses = new ArrayList<OrderLix>();
		for (int i = 0; i < listOrderLix.size(); i++) {
			if (listOrderLix.get(i).isChonphieu()) {
				orderChooses.add(listOrderLix.get(i));
			}
		}
		if (listOrderLix != null && listOrderLix.size() != 0) {
			try {
				processLogicInvoiceService.capnhattrangthaidonhang(listOrderLix);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		success();

	}

	public void capnhattrangthaiDHHuy() {
		List<OrderDetail> orderDetails = orderDetailService.selectByOrdersHuy();
		for (int i = 0; i < orderDetails.size(); i++) {
			orderDetailService.updateHuy(orderDetails.get(i));
		}
		success();

	}

	public void exportIEInvoiceListExcel() {
		try {
			List<OrderLix> orderChooses = new ArrayList<OrderLix>();
			for (int i = 0; i < listOrderLix.size(); i++) {
				if (listOrderLix.get(i).isChonphieu()) {
					orderChooses.add(listOrderLix.get(i));
				}
			}
			if (orderChooses.size() != 0) {
				List<OrderDetail> orderDetails = orderDetailService.selectByOrdersAll(orderChooses);
				// xuat file excel.
				if (orderDetails.size() > 0) {
					List<Object[]> results = new ArrayList<Object[]>();
					Object[] title = { "ID_ĐH", "SoCT", "NgayĐH", "NgayGH", "SoPO", "MaKH", "TenKH", "CTKM", "CTĐG",
							"SoXe", "SoHDon", "SoHĐong", "SoSerie", "HeSoThue", "LoaiXN", "DaGH", "LyDoKhongGH", "LĐĐ",
							"SoCTKH",

							"ID_CT", "MaSP", "TenSP", "SlThung_ban", "SlThung_ban(daxuat)", "SLDVT_ban",
							"SLDVT_ban(daxuat)", "SLDVT_KM", "SLDVT_KM(daxuat)", "DonGia", "ThanhTien", "HTKM",
							"QuyCach", "MaLH", "SoCont", "SoSeal", "SPChinh", "Huy"

					};
					results.add(title);
					for (OrderDetail it : orderDetails) {
						if (it.getProduct() != null) {
							Customer cus = it.getOrder_lix().getCustomer() != null ? it.getOrder_lix().getCustomer()
									: new Customer();

							IECategories ie = it.getOrder_lix().getIe_categories() != null ? it.getOrder_lix()
									.getIe_categories() : new IECategories();
							String ctdg = it.getOrder_lix().getPricing_program() != null ? it.getOrder_lix()
									.getPricing_program().getProgram_code() : "";
							String ctkm = it.getOrder_lix().getPromotion_program() != null ? it.getOrder_lix()
									.getPromotion_program().getProgram_code() : "";
							String car = it.getOrder_lix().getCar() != null ? it.getOrder_lix().getCar()
									.getLicense_plate() : "";

							Object[] row = { it.getOrder_lix().getId(), it.getOrder_lix().getVoucher_code(),
									MyUtil.chuyensangStr(it.getOrder_lix().getOrder_date()),
									MyUtil.chuyensangStr(it.getOrder_lix().getDelivery_date()),
									it.getOrder_lix().getPo_no(), cus.getCustomer_code(), cus.getCustomer_name(), ctkm,
									ctdg, car, it.getOrder_lix().getInvoice_no(), it.getOrder_lix().getContract_no(),
									it.getOrder_lix().getSerie_no(), it.getOrder_lix().getTax_value(), ie.getCode(),
									it.getOrder_lix().isDelivered(), it.getOrder_lix().getReason_not_delivered(),
									it.getOrder_lix().getLdd(), it.getOrder_lix().getCus_voucher(), it.getId(),
									it.getProduct().getProduct_code(), it.getProduct().getProduct_name(),
									it.getBox_quantity(), it.getBox_quantity_actual(),
									it.getBox_quantity() * it.getProduct().getSpecification(),
									it.getBox_quantity_actual() * it.getProduct().getSpecification(), 0, 0,
									it.getUnit_price(), it.getTotal(), it.getPromotion_forms(), it.getNote(),
									it.getBatch_code(), it.getContainer_number(), it.getSeal_number(), "-",
									it.isHuyct() };
							results.add(row);
						}
					}
					List<PromotionOrderDetail> promotionOrderDetails = promotionOrderDetailService
							.selectByOrderDetails(orderDetails);

					for (PromotionOrderDetail it : promotionOrderDetails) {
						if (it.getProduct() != null) {
							Customer cus = it.getOrder_detail().getOrder_lix().getCustomer() != null ? it
									.getOrder_detail().getOrder_lix().getCustomer() : new Customer();

							IECategories ie = it.getOrder_detail().getOrder_lix().getIe_categories() != null ? it
									.getOrder_detail().getOrder_lix().getIe_categories() : new IECategories();
							String ctdg = it.getOrder_detail().getOrder_lix().getPricing_program() != null ? it
									.getOrder_detail().getOrder_lix().getPricing_program().getProgram_code() : "";
							String ctkm = it.getOrder_detail().getOrder_lix().getPromotion_program() != null ? it
									.getOrder_detail().getOrder_lix().getPromotion_program().getProgram_code() : "";
							String car = it.getOrder_detail().getOrder_lix().getCar() != null ? it.getOrder_detail()
									.getOrder_lix().getCar().getLicense_plate() : "";
							Object[] row = { it.getOrder_detail().getOrder_lix().getId(),
									it.getOrder_detail().getOrder_lix().getVoucher_code(),
									MyUtil.chuyensangStr(it.getOrder_detail().getOrder_lix().getOrder_date()),
									MyUtil.chuyensangStr(it.getOrder_detail().getOrder_lix().getDelivery_date()),
									it.getOrder_detail().getOrder_lix().getPo_no(), cus.getCustomer_code(),
									cus.getCustomer_name(), ctkm, ctdg, car,
									it.getOrder_detail().getOrder_lix().getInvoice_no(),
									it.getOrder_detail().getOrder_lix().getContract_no(),
									it.getOrder_detail().getOrder_lix().getSerie_no(),
									it.getOrder_detail().getOrder_lix().getTax_value(), ie.getCode(),
									it.getOrder_detail().getOrder_lix().isDelivered(),
									it.getOrder_detail().getOrder_lix().getReason_not_delivered(),
									it.getOrder_detail().getOrder_lix().getLdd(),
									it.getOrder_detail().getOrder_lix().getCus_voucher(), it.getId(),
									it.getProduct().getProduct_code(), it.getProduct().getProduct_name(), 0, 0, 0, 0,
									it.getQuantity(), it.getQuantityAct(), 0, 0, "-", it.getNote(), "", "", "",
									it.getOrder_detail().getProduct().getProduct_code(), it.isHuyct() };
							results.add(row);
						}
					}

					ToolReport.printReportExcelRaw(results, "DSChiTietDonHang");
				} else {
					noticeError("Không có chi tiết đơn hàng.");
				}

			} else {
				noticeError("Chưa chọn đơn hàng xuất.");
			}
		} catch (Exception e) {
			logger.error("IEInvoiceBean.exportIEInvoiceListByContNoReport:" + e.getMessage(), e);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public OrderLix getOrderLixCrud() {
		return orderLixCrud;
	}

	public void setOrderLixCrud(OrderLix orderLixCrud) {
		this.orderLixCrud = orderLixCrud;
	}

	public OrderLix getOrderLixSelect() {
		return orderLixSelect;
	}

	public void setOrderLixSelect(OrderLix orderLixSelect) {
		this.orderLixSelect = orderLixSelect;
	}

	public List<OrderLix> getListOrderLix() {
		return listOrderLix;
	}

	public void setListOrderLix(List<OrderLix> listOrderLix) {
		this.listOrderLix = listOrderLix;
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

	public String getOrderCodeSearch() {
		return orderCodeSearch;
	}

	public void setOrderCodeSearch(String orderCodeSearch) {
		this.orderCodeSearch = orderCodeSearch;
	}

	public String getVoucherCodeSearch() {
		return voucherCodeSearch;
	}

	public void setVoucherCodeSearch(String voucherCodeSearch) {
		this.voucherCodeSearch = voucherCodeSearch;
	}

	public IECategories getiECategoriesSearch() {
		return iECategoriesSearch;
	}

	public void setiECategoriesSearch(IECategories iECategoriesSearch) {
		this.iECategoriesSearch = iECategoriesSearch;
	}

	public String getPoNoSearch() {
		return poNoSearch;
	}

	public void setPoNoSearch(String poNoSearch) {
		this.poNoSearch = poNoSearch;
	}

	public int getDeliveredSearch() {
		return deliveredSearch;
	}

	public void setDeliveredSearch(int deliveredSearch) {
		this.deliveredSearch = deliveredSearch;
	}

	public List<PaymentMethod> getListPaymentMethod() {
		return listPaymentMethod;
	}

	public void setListPaymentMethod(List<PaymentMethod> listPaymentMethod) {
		this.listPaymentMethod = listPaymentMethod;
	}

	public List<Warehouse> getListWarehouse() {
		return listWarehouse;
	}

	public void setListWarehouse(List<Warehouse> listWarehouse) {
		this.listWarehouse = listWarehouse;
	}

	public List<CustomerTypes> getListCustomerTypes() {
		return listCustomerTypes;
	}

	public void setListCustomerTypes(List<CustomerTypes> listCustomerTypes) {
		this.listCustomerTypes = listCustomerTypes;
	}

	public FormatHandler getFormatHandler() {
		return formatHandler;
	}

	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
	}

	public TransportPricingNew getTransportPricingNewCrud() {
		return transportPricingNewCrud;
	}

	public void setTransportPricingNewCrud(TransportPricingNew transportPricingNewCrud) {
		this.transportPricingNewCrud = transportPricingNewCrud;
	}

	public List<FreightContract> getListFreightContractSelect() {
		return listFreightContractSelect;
	}

	public void setListFreightContractSelect(List<FreightContract> listFreightContractSelect) {
		this.listFreightContractSelect = listFreightContractSelect;
	}

	public Date getFromDateFCFilter() {
		return fromDateFCFilter;
	}

	public void setFromDateFCFilter(Date fromDateFCFilter) {
		this.fromDateFCFilter = fromDateFCFilter;
	}

	public Date getToDateFCFilter() {
		return toDateFCFilter;
	}

	public void setToDateFCFilter(Date toDateFCFilter) {
		this.toDateFCFilter = toDateFCFilter;
	}

	public Customer getCustomerFcFilter() {
		return customerFcFilter;
	}

	public void setCustomerFcFilter(Customer customerFcFilter) {
		this.customerFcFilter = customerFcFilter;
	}

	public String getContractNoFCFilter() {
		return contractNoFCFilter;
	}

	public void setContractNoFCFilter(String contractNoFCFilter) {
		this.contractNoFCFilter = contractNoFCFilter;
	}

	public CustomerTypes getCustomerTypesFCFilter() {
		return customerTypesFCFilter;
	}

	public void setCustomerTypesFCFilter(CustomerTypes customerTypesFCFilter) {
		this.customerTypesFCFilter = customerTypesFCFilter;
	}

	public int getPageSizeHDVC() {
		return pageSizeHDVC;
	}

	public void setPageSizeHDVC(int pageSizeHDVC) {
		this.pageSizeHDVC = pageSizeHDVC;
	}

	public NavigationInfo getNavigationInfoHDVC() {
		return navigationInfoHDVC;
	}

	public void setNavigationInfoHDVC(NavigationInfo navigationInfoHDVC) {
		this.navigationInfoHDVC = navigationInfoHDVC;
	}

	public List<Integer> getListRowPerPageHDVC() {
		return listRowPerPageHDVC;
	}

	public void setListRowPerPageHDVC(List<Integer> listRowPerPageHDVC) {
		this.listRowPerPageHDVC = listRowPerPageHDVC;
	}

	public List<PromotionProgramDetail> getListPromotionProgramDetailCrud() {
		return listPromotionProgramDetailCrud;
	}

	public void setListPromotionProgramDetailCrud(List<PromotionProgramDetail> listPromotionProgramDetailCrud) {
		this.listPromotionProgramDetailCrud = listPromotionProgramDetailCrud;
	}

	public boolean isTabOrder() {
		return tabOrder;
	}

	public void setTabOrder(boolean tabOrder) {
		this.tabOrder = tabOrder;
	}

	public List<WrapOrderDetailLixReqInfo> getListWrapOrderDetailLixReqInfo() {
		return listWrapOrderDetailLixReqInfo;
	}

	public void setListWrapOrderDetailLixReqInfo(List<WrapOrderDetailLixReqInfo> listWrapOrderDetailLixReqInfo) {
		this.listWrapOrderDetailLixReqInfo = listWrapOrderDetailLixReqInfo;
	}

	public List<PromotionOrderDetail> getListPromotionOrderDetailInvoice() {
		return listPromotionOrderDetailInvoice;
	}

	public void setListPromotionOrderDetailInvoice(List<PromotionOrderDetail> listPromotionOrderDetailInvoice) {
		this.listPromotionOrderDetailInvoice = listPromotionOrderDetailInvoice;
	}

	public Car getCarCrud() {
		return carCrud;
	}

	public void setCarCrud(Car carCrud) {
		this.carCrud = carCrud;
	}

	public Car getCarSelect() {
		return carSelect;
	}

	public void setCarSelect(Car carSelect) {
		this.carSelect = carSelect;
	}

	public List<Car> getListCar() {
		return listCar;
	}

	public void setListCar(List<Car> listCar) {
		this.listCar = listCar;
	}

	public List<CarType> getListCarType() {
		return listCarType;
	}

	public void setListCarType(List<CarType> listCarType) {
		this.listCarType = listCarType;
	}

	public List<CarOwner> getListCarOwner() {
		return listCarOwner;
	}

	public void setListCarOwner(List<CarOwner> listCarOwner) {
		this.listCarOwner = listCarOwner;
	}

	public CarType getCarTypeSearch() {
		return carTypeSearch;
	}

	public void setCarTypeSearch(CarType carTypeSearch) {
		this.carTypeSearch = carTypeSearch;
	}

	public CarOwner getCarOwnerSearch() {
		return carOwnerSearch;
	}

	public void setCarOwnerSearch(CarOwner carOwnerSearch) {
		this.carOwnerSearch = carOwnerSearch;
	}

	public String getLicensePlateSearch() {
		return licensePlateSearch;
	}

	public void setLicensePlateSearch(String licensePlateSearch) {
		this.licensePlateSearch = licensePlateSearch;
	}

	public String getPhoneNumberSearch() {
		return phoneNumberSearch;
	}

	public void setPhoneNumberSearch(String phoneNumberSearch) {
		this.phoneNumberSearch = phoneNumberSearch;
	}

	public boolean isAllex() {
		return allex;
	}

	public void setAllex(boolean allex) {
		this.allex = allex;
	}

	public List<DeliveryPricing> getListDeliveryPricing() {
		return listDeliveryPricing;
	}

	public void setListDeliveryPricing(List<DeliveryPricing> listDeliveryPricing) {
		this.listDeliveryPricing = listDeliveryPricing;
	}

	public boolean isChangePricingProgram() {
		return changePricingProgram;
	}

	public void setChangePricingProgram(boolean changePricingProgram) {
		this.changePricingProgram = changePricingProgram;
	}

	public boolean isChangePromotionProgram() {
		return changePromotionProgram;
	}

	public void setChangePromotionProgram(boolean changePromotionProgram) {
		this.changePromotionProgram = changePromotionProgram;
	}

	public List<OrderDetail> getListOrderDetail() {
		return listOrderDetail;
	}

	public void setListOrderDetail(List<OrderDetail> listOrderDetail) {
		this.listOrderDetail = listOrderDetail;
	}

	public List<OrderDetail> getListOrderDetailFilter() {
		return listOrderDetailFilter;
	}

	public void setListOrderDetailFilter(List<OrderDetail> listOrderDetailFilter) {
		this.listOrderDetailFilter = listOrderDetailFilter;
	}

	public List<PromotionOrderDetail> getListPromotionOrderDetail() {
		return listPromotionOrderDetail;
	}

	public void setListPromotionOrderDetail(List<PromotionOrderDetail> listPromotionOrderDetail) {
		this.listPromotionOrderDetail = listPromotionOrderDetail;
	}

	public List<PromotionOrderDetail> getListPromotionOrderDetailFilter() {
		return listPromotionOrderDetailFilter;
	}

	public void setListPromotionOrderDetailFilter(List<PromotionOrderDetail> listPromotionOrderDetailFilter) {
		this.listPromotionOrderDetailFilter = listPromotionOrderDetailFilter;
	}

	public OrderDetail getOrderDetailCrud() {
		return orderDetailCrud;
	}

	public void setOrderDetailCrud(OrderDetail orderDetailCrud) {
		this.orderDetailCrud = orderDetailCrud;
	}

	public OrderDetail getOrderDetailPick() {
		return orderDetailPick;
	}

	public void setOrderDetailPick(OrderDetail orderDetailPick) {
		this.orderDetailPick = orderDetailPick;
	}

	public OrderDetail getOrderDetailSelect() {
		return orderDetailSelect;
	}

	public void setOrderDetailSelect(OrderDetail orderDetailSelect) {
		this.orderDetailSelect = orderDetailSelect;
	}

	public List<Integer> getStatusSearch() {
		return statusSearch;
	}

	public void setStatusSearch(List<Integer> statusSearch) {
		this.statusSearch = statusSearch;
	}

}
