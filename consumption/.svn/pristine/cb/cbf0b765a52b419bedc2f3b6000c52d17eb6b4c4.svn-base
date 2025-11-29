package lixco.com.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import lixco.com.commom_ejb.MyMath;
import lixco.com.common.SessionHelper;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.BindUser;
import lixco.com.entity.Car;
import lixco.com.entity.Carrier;
import lixco.com.entity.Contract;
import lixco.com.entity.ContractDetail;
import lixco.com.entity.ContractReqInfo;
import lixco.com.entity.Currency;
import lixco.com.entity.Customer;
import lixco.com.entity.CustomerPricingProgram;
import lixco.com.entity.CustomerPromotionProgram;
import lixco.com.entity.FreightContract;
import lixco.com.entity.FreightContractDetail;
import lixco.com.entity.GoodsReceiptNote;
import lixco.com.entity.GoodsReceiptNoteDetail;
import lixco.com.entity.HarborCategory;
import lixco.com.entity.IEInvoice;
import lixco.com.entity.IEInvoiceDetail;
import lixco.com.entity.InvenCongNo;
import lixco.com.entity.Inventory;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.OrderDetail;
import lixco.com.entity.OrderLix;
import lixco.com.entity.PaymentMethod;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.PricingProgramDetail;
import lixco.com.entity.Product;
import lixco.com.entity.ProductType;
import lixco.com.entity.PromotionOrderDetail;
import lixco.com.entity.PromotionProductGroup;
import lixco.com.entity.PromotionProgram;
import lixco.com.entity.PromotionProgramDetail;
import lixco.com.entity.PromotionalPricing;
import lixco.com.entity.Stocker;
import lixco.com.entity.VoucherPayment;
import lixco.com.interfaces.IBindUserService;
import lixco.com.interfaces.ICarService;
import lixco.com.interfaces.ICarrierService;
import lixco.com.interfaces.IContractService;
import lixco.com.interfaces.ICountryService;
import lixco.com.interfaces.ICurrencyService;
import lixco.com.interfaces.ICustomerPricingProgramService;
import lixco.com.interfaces.ICustomerPromotionProgramService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IDeliveryPricingService;
import lixco.com.interfaces.IFormUpGoodsService;
import lixco.com.interfaces.IFreightContractService;
import lixco.com.interfaces.IGoodsReceiptNoteDetailService;
import lixco.com.interfaces.IGoodsReceiptNoteService;
import lixco.com.interfaces.IHarborCategoryService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IIEInvoiceService;
import lixco.com.interfaces.IInvenCongNoService;
import lixco.com.interfaces.IInventoryService;
import lixco.com.interfaces.IInvoiceDetailService;
import lixco.com.interfaces.IInvoiceService;
import lixco.com.interfaces.IOrderDetailService;
import lixco.com.interfaces.IOrderLixService;
import lixco.com.interfaces.IPaymentMethodService;
import lixco.com.interfaces.IPricingProgramDetailService;
import lixco.com.interfaces.IPricingProgramService;
import lixco.com.interfaces.IProcessLogicInvoiceService;
import lixco.com.interfaces.IProductComService;
import lixco.com.interfaces.IProductGroupService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IPromotionOrderDetailService;
import lixco.com.interfaces.IPromotionProductGroupService;
import lixco.com.interfaces.IPromotionProgramDetailService;
import lixco.com.interfaces.IPromotionProgramService;
import lixco.com.interfaces.IPromotionalPricingService;
import lixco.com.interfaces.IStevedoreService;
import lixco.com.interfaces.IStockerService;
import lixco.com.interfaces.IVoucherPaymentService;
import lixco.com.interfaces.IWarehouseService;
import lixco.com.loaddata.GoodsReceiptNoteFake;
import lixco.com.loaddata.GoodsReceiptNoteFake.KeyPhieuNhap;
import lixco.com.loaddata.PromotionOrderDetailFake;
import lixco.com.reqInfo.CarReqInfo;
import lixco.com.reqInfo.CarrierReqInfo;
import lixco.com.reqInfo.ContractDetailReqInfo;
import lixco.com.reqInfo.CountryReqInfo;
import lixco.com.reqInfo.CustomerPricingProgramReqInfo;
import lixco.com.reqInfo.CustomerPromotionProgramReqInfo;
import lixco.com.reqInfo.CustomerReqInfo;
import lixco.com.reqInfo.DeliveryPricingReqInfo;
import lixco.com.reqInfo.FormUpGoodsReqInfo;
import lixco.com.reqInfo.FreightContractDetailReqInfo;
import lixco.com.reqInfo.FreightContractReqInfo;
import lixco.com.reqInfo.GoodsReceiptNoteDetailReqInfo;
import lixco.com.reqInfo.GoodsReceiptNoteReqInfo;
import lixco.com.reqInfo.HarborCategoryReqInfo;
import lixco.com.reqInfo.IECategoriesReqInfo;
import lixco.com.reqInfo.IEInvoiceDetailReqInfo;
import lixco.com.reqInfo.IEInvoiceReqInfo;
import lixco.com.reqInfo.InvoiceDetailReqInfo;
import lixco.com.reqInfo.InvoiceReqInfo;
import lixco.com.reqInfo.OrderDetailReqInfo;
import lixco.com.reqInfo.OrderLixReqInfo;
import lixco.com.reqInfo.PaymentMethodReqInfo;
import lixco.com.reqInfo.PricingProgramDetailReqInfo;
import lixco.com.reqInfo.PricingProgramReqInfo;
import lixco.com.reqInfo.ProductComReqInfo;
import lixco.com.reqInfo.ProductGroupReqInfo;
import lixco.com.reqInfo.ProductReqInfo;
import lixco.com.reqInfo.ProductTypeReqInfo;
import lixco.com.reqInfo.PromotionOrderDetailReqInfo;
import lixco.com.reqInfo.PromotionProductGroupReqInfo;
import lixco.com.reqInfo.PromotionProgramDetailReqInfo;
import lixco.com.reqInfo.PromotionProgramReqInfo;
import lixco.com.reqInfo.PromotionalPricingReqInfo;
import lixco.com.reqInfo.StevedoreReqInfo;
import lixco.com.reqInfo.StockerReqInfo;
import lixco.com.reqInfo.VoucherPaymentReqInfo;
import lixco.com.reqInfo.WarehouseReqInfo;

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
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.convertfont.FontType;
import trong.lixco.com.convertfont.MyConvertFontFactory;
import trong.lixco.com.info.InvoiceExcelMisa;
import trong.lixco.com.util.MyUtilExcel;
import trong.lixco.com.util.Notify;

@Named
@ViewScoped
public class DataSettingBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IPricingProgramService pricingProgramService;
	@Inject
	private IPromotionProgramService promotionProgramService;
	@Inject
	private IPromotionProgramDetailService promotionProgramDetailService;
	@Inject
	private ICustomerPromotionProgramService customerPromotionProgramService;
	@Inject
	private IPricingProgramDetailService pricingProgramDetailService;
	@Inject
	private ICustomerPricingProgramService customerPricingProgramService;
	@Inject
	private IProductService productService;
	@Inject
	private IInventoryService inventoryService;
	@Inject
	private IPromotionalPricingService promotionalPricingService;
	@Inject
	private IPromotionProductGroupService promotionProductGroupService;
	@Inject
	private IHarborCategoryService harborCategoryService;
	@Inject
	private ICarrierService carrierService;
	@Inject
	private IStockerService stockerService;
	@Inject
	private ICountryService countryService;
	@Inject
	private ICustomerService customerService;
	@Inject
	private IOrderLixService orderLixService;
	@Inject
	private IOrderDetailService orderDetailService;
	@Inject
	private IIECategoriesService iECategoriesService;
	@Inject
	private IWarehouseService warehouseService;
	@Inject
	private IPaymentMethodService paymentMethodService;
	@Inject
	private IDeliveryPricingService deliveryPricingService;
	@Inject
	private ICarService carService;
	@Inject
	private IFreightContractService freightContractService;
	@Inject
	private IPromotionOrderDetailService promotionOrderDetailService;
	@Inject
	private IGoodsReceiptNoteService goodsReceiptNoteService;
	@Inject
	private IGoodsReceiptNoteDetailService goodsReceiptNoteDetailService;
	@Inject
	private IProductGroupService productGroupService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private IProductComService productComService;
	@Inject
	private IContractService contractService;
	@Inject
	private ICurrencyService currencyService;
	@Inject
	private IStevedoreService stevedoreService;
	@Inject
	private IFormUpGoodsService formUpGoodsService;
	@Inject
	private IInvoiceService invoiceService;
	@Inject
	private IInvoiceDetailService invoiceDetailService;
	@Inject
	private IProcessLogicInvoiceService processLogicInvoiceService;
	@Inject
	private IIEInvoiceService iEInvoiceService;
	private Account account;

	@Override
	protected void initItem() {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);

		} catch (Exception e) {

		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public void showDialogNapCTDG() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('ctdg').show();");
	}

	public void showDialogNapCTCTDG() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('ctctdg').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogNapCDCTDG() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('cdctdg').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogNapCTKM() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('ctkm').show();");
	}

	public void showDialogNapCTCTKM() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('ctctkm').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogNapDonHang() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('donhang').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogNapCTDonHang() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('ctdonhang').show();");
	}

	public void showDialogNapHopDong() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('hopdong').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogNapCTHopDong() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('cthopdong').show();");
	}

	public void showDialogNapKMDonHang() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('kmdonhang').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogNapXuatHang() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('xuathang').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogNapXuatHangMisa() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('xuathangmisa').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogNapCTXuatHang() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('ctxuathang').show();");
	}

	public void capnhattongtien() {
		List<Invoice> invoices = invoiceService.findAll();
		for (int i = 0; i < invoices.size(); i++) {
			try {
				List<InvoiceDetail> listWrapInvoiceDetail = new ArrayList<InvoiceDetail>();
				invoiceDetailService.selectByInvoice(invoices.get(i).getId(), listWrapInvoiceDetail);
				if (listWrapInvoiceDetail != null && listWrapInvoiceDetail.size() > 0) {
					double totalevent = listWrapInvoiceDetail.stream().mapToDouble(f -> f.getTotal()).sum();
					invoices.get(i).setTongtien(MyMath.roundCustom(totalevent, 2));
					invoices.get(i).setThue(MyMath.roundCustom(totalevent * invoices.get(i).getTax_value(), 2));
					invoiceService.updateTotalTax(invoices.get(i), getAccount().getUserName());
				}
			} catch (Exception e) {
				invoices.get(i).setTongtien(0);
				invoices.get(i).setThue(0);
				invoiceService.updateTotalTax(invoices.get(i), getAccount().getUserName());
				logger.error("InvoiceBean.sumInvoice:" + e.getMessage(), e);
			}
		}
		thongbao("Cập nhật thành công.");
	}

	public void showDialogNapXuatXuatKhau() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('xuatxuatkhau').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogNapCTXuatXuatKhau() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('ctxuatxuatkhau').show();");
	}

	public void showDialogNapCDCTKM() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('cdctkm').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogNapDonGiaKM() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('dongiakm').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogNhapHang() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('nhaphang').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogNhomSPKM() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('nhomspkm').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogHarbor() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('harbor').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogCarrier() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('carrier').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogStocker() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('stocker').show();");
		} catch (Exception e) {

		}
	}

	public void showDialogNaptatca() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('naptatca').show();");
		} catch (Exception e) {

		}
	}

	public void naptatca(FileUploadEvent event) {
		try {
			if (event.getFile() != null) {
				fileUploadEvents.add(event);

			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napXuatHang:" + e.getMessage(), e);
		}
	}

	List<FileUploadEvent> fileUploadEvents = new ArrayList<FileUploadEvent>();

	public void batdaunaptatca() {
		Collections.sort(fileUploadEvents, new Comparator<FileUploadEvent>() {
			public int compare(FileUploadEvent s1, FileUploadEvent s2) {
				String s1name = s1.getFile().getFileName();
				String s2name = s2.getFile().getFileName();
				System.out.println(s1name + " -- " + s2name);
				Double numero1 = Double.parseDouble(s1name.substring(0, s1name.indexOf(".")));
				Double numero2 = Double.parseDouble(s2name.substring(0, s2name.indexOf(".")));
				return numero1.compareTo(numero2);
			}
		});
		if (fileUploadEvents.size() == 0) {
			canhbao("Không có dữ liệu nạp");
		} else {
			for (int i = 0; i < fileUploadEvents.size(); i++) {
				String filename = fileUploadEvents.get(i).getFile().getFileName();
				if (filename.contains("dmsp.xls")) {
					napSanPham(fileUploadEvents.get(i));
				} else if (filename.contains("tonsp.xls")) {
					naptonkho(fileUploadEvents.get(i));
				} else if (filename.contains("wchitiet_dhang.xls")) {
					napCTDonHang(fileUploadEvents.get(i));
				} else if (filename.contains("wchitiet_dhangkm.xls")) {
					napKMDonhang(fileUploadEvents.get(i));
				} else if (filename.contains("wchitiet_hopdong.xls")) {
					napCTHopDong(fileUploadEvents.get(i));
				} else if (filename.contains("wchitiet_hopdongvc.xls")) {
					napCTHopDongVC(fileUploadEvents.get(i));
				} else if (filename.contains("wchitiet_xsp.xls")) {
					napCTXuatHang(fileUploadEvents.get(i));
				} else if (filename.contains("wchitiet_xsp_xk.xls")) {
					napCTXuatXuatKhau(fileUploadEvents.get(i));
				} else if (filename.contains("wdonhang.xls")) {
					napDonHang(fileUploadEvents.get(i));
				} else if (filename.contains("whopdongsp.xls")) {
					napHopDong(fileUploadEvents.get(i));
				} else if (filename.contains("whopdongvc.xls")) {
					napHopDongVC(fileUploadEvents.get(i));
				} else if (filename.contains("wnhapsp.xls")) {
					napPhieuNhap(fileUploadEvents.get(i));
				} else if (filename.contains("wuynhiemchi.xls")) {
					napuynhiemchi(fileUploadEvents.get(i));
				} else if (filename.contains("wxsp.xls")) {
					napXuatHang(fileUploadEvents.get(i));
				} else if (filename.contains("wxsp_xk.xls")) {
					napXuatXuatKhau(fileUploadEvents.get(i));
				}

			}
			fileUploadEvents.clear();
			thongbao("Nạp dữ liệu thành công.");
		}

	}

	public void napXuatHang(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				List<Invoice> listInvoiceTemp = new ArrayList<Invoice>();
				Workbook workBook = getWorkbook(event.getFile().getInputStream(), part.getFileName());
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
					Invoice lix = new Invoice();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);

						int columnIndex = cell.getColumnIndex();

						switch (columnIndex) {
						case 0:
							try {
								// mã xuất hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setInvoice_code(cellvalue);
									lix.setIdfox(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// mã xuất nhập
								if (cellvalue != null && !"".equals(cellvalue)) {
									IECategoriesReqInfo iereq = new IECategoriesReqInfo();
									iECategoriesService.selectByCodeOld(cellvalue, iereq);
									if (iereq.getIe_categories() != null) {
										lix.setIe_categories(iereq.getIe_categories());
									} else {
										// continue lv1;
									}
								} else {
									// continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// số chứng từ
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setVoucher_code(cellvalue);
								} else {
									// continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// mã khách hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									CustomerReqInfo cReqInfo = new CustomerReqInfo();
									customerService.selectByCode(cellvalue, cReqInfo);
									if (cReqInfo.getCustomer() != null) {
										lix.setCustomer(cReqInfo.getCustomer());
									} else {
										// continue lv1;
									}
								} else {
									// continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 4:
							if (cellvalue != null && !"".equals(cellvalue)) {
								Date invoiceDate = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
								if (invoiceDate != null) {
									lix.setInvoice_date(invoiceDate);
								} else {
									// continue lv1;
								}
							} else {
								// continue lv1;
							}
							break;
						case 5:
							try {
								// ma kho
								if (cellvalue != null && !"".equals(cellvalue)) {
									WarehouseReqInfo wreq = new WarehouseReqInfo();
									warehouseService.selectByCodeOld(cellvalue, wreq);
									if (wreq.getWarehouse() != null) {
										lix.setWarehouse(wreq.getWarehouse());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// số xe
								if (cellvalue != null && !"".equals(cellvalue)) {
									CarReqInfo carReq = new CarReqInfo();
									carService.selectByLicensePlate(cellvalue, carReq);
									if (carReq.getCar() != null) {
										lix.setCar(carReq.getCar());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								// hệ số thuế
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setTax_value(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 8:
							try {
								// hình thức thanh tán
								if (cellvalue != null && !"".equals(cellvalue)) {
									PaymentMethodReqInfo payReq = new PaymentMethodReqInfo();
									paymentMethodService.selectByCodeOld(cellvalue, payReq);
									if (payReq.getPayment_method() != null) {
										lix.setPayment_method(payReq.getPayment_method());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 12:
							try {
								// đã thanh toán
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setPayment(Boolean.parseBoolean(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 14:
							try {
								// số hợp đồng mua bán
								if (cellvalue != null && !"".equals(cellvalue)) {
									Contract contract = contractService.selectByVoucherOnlyId(cellvalue);
									if (contract != null) {
										lix.setContract(contract);
									}

								}
							} catch (Exception e) {
							}
							break;
						case 15:
							// số serie
							lix.setInvoice_serie(cellvalue);
							break;
						case 16:
							// số đơn hàng
							if (cellvalue != null && !"".equals(cellvalue)) {
								// tìm đơn hàng
							}
							break;
						case 17:
							// Mã nhân viên bán hàng
							try {
								BindUser bindUser = bindUserService.getBindUserByIdFox(cellvalue);
								if (bindUser != null) {
									lix.setCreated_by(MyConvertFontFactory.convert(FontType.VNI_WINDOWS,
											FontType.UNICODE, bindUser.getMember_name()));
								} else {
									lix.setCreated_by(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 18:
							try {
								// mã nơi vc
								if (cellvalue != null && !"".equals(cellvalue)) {
									DeliveryPricingReqInfo dfReq = new DeliveryPricingReqInfo();
									deliveryPricingService.selectByPlaceCode(cellvalue, dfReq);
									if (dfReq.getDelivery_pricing() != null) {
										lix.setDelivery_pricing(dfReq.getDelivery_pricing());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 19:
							try {
								// port ID
								if (cellvalue != null && !"".equals(cellvalue)) {
									HarborCategoryReqInfo hReq = new HarborCategoryReqInfo();
									harborCategoryService.selectByCode(cellvalue, hReq);
									if (hReq.getHarbor_category() != null) {
										lix.setHarbor_category(hReq.getHarbor_category());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 20:
							try {
								// mã thủ kho
								StockerReqInfo stockerReqInfo = new StockerReqInfo();
								stockerService.selectByCode(cellvalue, stockerReqInfo);
								if (stockerReqInfo.getStocker() != null) {
									lix.setStocker(stockerReqInfo.getStocker());
								}
							} catch (Exception e) {
							}
							break;
						case 21:
							try {
								// mã bốc xếp
								if (cellvalue != null && !"".equals(cellvalue)) {
									StevedoreReqInfo sdReq = new StevedoreReqInfo();
									stevedoreService.selectByCode(cellvalue, sdReq);
									lix.setStevedore(sdReq.getStevedore());
								}
							} catch (Exception e) {
							}
							break;
						case 22:
							try {
								// hình thức bốc xếp
								if (cellvalue != null && !"".equals(cellvalue)) {
									FormUpGoodsReqInfo formUpGoodsReqInfo = new FormUpGoodsReqInfo();
									formUpGoodsService.selectByCode(cellvalue, formUpGoodsReqInfo);
									lix.setForm_up_goods(formUpGoodsReqInfo.getForm_up_goods());
								}
							} catch (Exception e) {
							}
							break;
						case 23:
							try {
								// ngoài giờ
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setTimeout(Boolean.parseBoolean(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 24:
							try {
								// mã số chương trình km
								if (cellvalue != null && !"".equals(cellvalue)) {
									PromotionProgramReqInfo promotionProgramReqInfo = new PromotionProgramReqInfo();
									promotionProgramService.selectByCode(cellvalue, promotionProgramReqInfo);
									if (promotionProgramReqInfo.getPromotion_program() != null) {
										lix.setPromotion_program(promotionProgramReqInfo.getPromotion_program());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 25:
							try {
								// mã nvvc
								if (cellvalue != null && !"".equals(cellvalue)) {
									CarrierReqInfo carrierReqInfo = new CarrierReqInfo();
									carrierService.selectByCode(cellvalue, carrierReqInfo);
									if (carrierReqInfo.getCarrier() != null) {
										lix.setCarrier(carrierReqInfo.getCarrier());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 26:
							try {
								// nội dung vc
								lix.setTransport_content(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 29:
							try {
								// mã số ct đơn giá
								if (cellvalue != null && !"".equals(cellvalue)) {
									PricingProgramReqInfo pricingProgramReqInfo = new PricingProgramReqInfo();
									pricingProgramService.selectByCode(cellvalue, pricingProgramReqInfo);
									if (pricingProgramReqInfo.getPricing_program() != null) {
										lix.setPricing_program(pricingProgramReqInfo.getPricing_program());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 30:
							try {
								// mã đơn hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									OrderLixReqInfo orderLixReqInfo = new OrderLixReqInfo();
									orderLixService.selectByOrderCode(cellvalue, orderLixReqInfo);
									if (orderLixReqInfo.getOrder_lix() != null) {
										lix.setOrder_lix(orderLixReqInfo.getOrder_lix());
										lix.setIdorderlix(orderLixReqInfo.getOrder_lix().getId());
										lix.setOrder_code(cellvalue);
									}
								}
							} catch (Exception e) {
							}
							break;
						case 31:
							try {
								// lệnh điều động
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setMovement_commands_no(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 32:
							try {
								// ngày giao hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date deliveryDate = ToolTimeCustomer
											.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									if (deliveryDate != null) {
										lix.setDelivery_date(deliveryDate);
									}
								}
							} catch (Exception e) {
							}
							break;
						case 34:
							try {
								// số PO
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setPo_no(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 35:
							try {
								// thời gian lên hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									int tglh = (int) Double.parseDouble(cellvalue);
									lix.setTime_up_goods(tglh);
								}
							} catch (Exception e) {
							}
							break;
						case 40:
							try {
								// guid hóa đơn điện tử
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setRefId(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 41:
							try {
								// editVersion hóa đơn điện tử
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setEditVersion((int) Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 42:
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setLookup_code(cellvalue);
								}
							} catch (Exception e) {
							}
							break;

						// DACK
						// case 43:
						// try {
						// // quy đổi
						// if (cellvalue != null && !"".equals(cellvalue)) {
						// lix.setExchange_val(cellvalue);
						// }
						// } catch (Exception e) {
						// }
						// break;

						case 44:
							try {
								// id hoa don goc
								lix.setIdhoadongoc(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 46:
							try {
								// so quy doi
								lix.setQuydoi(cellvalue);
							} catch (Exception e) {
							}
							break;
						}
					}
					listInvoiceTemp.add(lix);
				}
				workBook = null;// free
				for (Invoice it : listInvoiceTemp) {
					InvoiceReqInfo t = new InvoiceReqInfo();
					invoiceService.selectByInvoiceCodeOnlyId(it.getInvoice_code(), t);
					Invoice p = t.getInvoice();
					t.setInvoice(it);
					if (p != null) {
						it.setId(p.getId());
						it.setLast_modifed_by(account.getMember().getName());
						it.setLast_modifed_date(new Date());
						it.setStatus(1);
						it.setExported(true);
						invoiceService.updateAll(it, getAccount().getUserName());
					} else {
						it.setCreated_date(new Date());
						it.setStatus(1);
						it.setExported(true);
						invoiceService.insert(t);
					}
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napXuatHang:" + e.getMessage(), e);
		}
	}

	public void napXuatHangMisa(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				List<InvoiceExcelMisa> listInvoiceTemp = new ArrayList<InvoiceExcelMisa>();
				Workbook workBook = getWorkbook(event.getFile().getInputStream(), part.getFileName());
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
					InvoiceExcelMisa lix = new InvoiceExcelMisa();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 2:
							try {
								// mã xuất hàng(hoa don)
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setSohoadon(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 3:
							if (cellvalue != null && !"".equals(cellvalue)) {
								Date invoiceDate = ToolTimeCustomer.convertStringToDate(cellvalue, "dd/MM/yyyy");
								if (invoiceDate != null) {
									lix.setNgayhoadon(invoiceDate);
								} else {
									// continue lv1;
								}
							} else {
								// continue lv1;
							}
							break;

						case 5:
							try {
								// mã khách hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setMakhachhang(cellvalue);
								} else {
									// continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 14:
							try {
								// hinh thuc khuyen mai
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setHinhthuct(cellvalue);
								} else {
									// continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 16:
							try {
								// mã hang
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setMahang(cellvalue);
								} else {
									// continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 19:
							try {
								// soluong
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setSoluong(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 20:
							try {
								// dongia
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setDongia(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 21:
							try {
								// thanh tien
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setThanhtien(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 24:
							try {
								// hệ số thuế
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setThue(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 25:
							try {
								// tien thue
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setTienthue(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 26:
							try {
								// tongtien
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setTongtien(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;

						case 31:
							try {
								// Số đơn hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setDonhang(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						}
					}
					listInvoiceTemp.add(lix);
				}
				workBook = null;// free

				Map<String, List<InvoiceExcelMisa>> hoadons = listInvoiceTemp.stream().collect(
						Collectors.groupingBy(InvoiceExcelMisa::getSohoadon, Collectors.toList()));
				for (String key : hoadons.keySet()) {
					List<InvoiceExcelMisa> hds = hoadons.get(key);
					InvoiceReqInfo t = new InvoiceReqInfo();
					invoiceService.selectByInvoiceCodeOnlyIdYear(key, 2023, t);
					Invoice p = t.getInvoice();
					if (p == null) {
						p = new Invoice();
						p.setCreated_date(new Date());
						p.setStatus(1);
						p.setExported(true);
						p.setVoucher_code(hds.get(0).getSohoadon());
						p.setInvoice_date(hds.get(0).getNgayhoadon());
						Customer cus = customerService.selectByCode(hds.get(0).getMakhachhang());
						if (cus != null) {
							p.setCustomer(cus);
							PaymentMethod pm = paymentMethodService.selectByName(hds.get(0).getHinhthuct());
							p.setPayment_method(pm);
							p.setTax_value(hds.get(0).getThue() / 100);
							p.setThue(hds.get(0).getTienthue());
							p.setTongtien(hds.get(0).getTongtien());
							p.setOrder_voucher(hds.get(0).getDonhang());
							t.setInvoice(p);
							if (p.getTongtien() > 0) {
								p.setIe_categories(iECategoriesService.selectByCode("X"));
							}
							if (p.getTongtien() == 0) {
								p.setIe_categories(iECategoriesService.selectByCode("A"));
							}
							invoiceService.insert(t);

							for (int i = 0; i < hds.size(); i++) {
								InvoiceDetail invoiceDetail = new InvoiceDetail();
								invoiceDetail.setInvoice(t.getInvoice());
								ProductReqInfo pReq = new ProductReqInfo();
								productService.selectByCode(hds.get(i).getMahang(), pReq);
								if (pReq.getProduct() != null) {
									invoiceDetail.setProduct(pReq.getProduct());
								} else {
									break;
								}
								invoiceDetail.setQuantity(hds.get(i).getSoluong());
								invoiceDetail.setReal_quantity(hds.get(i).getSoluong());
								invoiceDetail.setUnit_price(hds.get(i).getDongia());
								invoiceDetail.setTotal(hds.get(i).getThanhtien());
								InvoiceDetailReqInfo td = new InvoiceDetailReqInfo(invoiceDetail);
								invoiceDetailService.insert(td);
							}
						}
					}

				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napXuatHang:" + e.getMessage(), e);
		}
	}

	public void napXuatXuatKhau(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				// byte[] byteFile = event.getFile().getContent();
				List<IEInvoice> listIEInvoiceTemp = new ArrayList<IEInvoice>();
				Workbook workBook = getWorkbook(event.getFile().getInputStream(), part.getFileName());
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
					IEInvoice lix = new IEInvoice();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// mã xuất xuất khẩu
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setInvoice_code(cellvalue);
									lix.setIdfox(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// mã xuất nhập
								if (cellvalue != null && !"".equals(cellvalue)) {
									IECategoriesReqInfo iereq = new IECategoriesReqInfo();
									iECategoriesService.selectByCodeOld(cellvalue, iereq);
									if (iereq.getIe_categories() != null) {
										lix.setIe_categories(iereq.getIe_categories());
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
								// số chứng từ
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setVoucher_code(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// mã khách hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									CustomerReqInfo cReqInfo = new CustomerReqInfo();
									customerService.selectByCode(cellvalue, cReqInfo);
									if (cReqInfo.getCustomer() != null) {
										lix.setCustomer(cReqInfo.getCustomer());
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
								// ngày hóa đơn
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date invoiceDate = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									if (invoiceDate != null) {
										lix.setInvoice_date(invoiceDate);
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
								// ma kho
								if (cellvalue != null && !"".equals(cellvalue)) {
									WarehouseReqInfo wreq = new WarehouseReqInfo();
									warehouseService.selectByCodeOld(cellvalue, wreq);
									if (wreq.getWarehouse() != null) {
										lix.setWarehouse(wreq.getWarehouse());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// số xe
								if (cellvalue != null && !"".equals(cellvalue)) {
									CarReqInfo carReq = new CarReqInfo();
									carService.selectByLicensePlate(cellvalue, carReq);
									if (carReq.getCar() != null) {
										lix.setCar(carReq.getCar());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								// hệ số thuế
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setTax_value(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 8:
							try {
								// hình thức thanh tán
								if (cellvalue != null && !"".equals(cellvalue)) {
									PaymentMethodReqInfo payReq = new PaymentMethodReqInfo();
									paymentMethodService.selectByCodeOld(cellvalue, payReq);
									if (payReq.getPayment_method() != null) {
										lix.setPayment_method(payReq.getPayment_method());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 10:
							try {
								// số hợp dồng mua bán
								if (cellvalue != null && !"".equals(cellvalue)) {
									Contract contract = contractService.selectByVoucherOnlyId(cellvalue);
									if (contract != null) {
										lix.setContract(contract);
									}
								}
							} catch (Exception e) {
							}
							break;
						case 11:
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setPaid(Boolean.parseBoolean(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 13:
							try {
								// tỉ giá
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setExchange_rate(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 15:
							// bill no
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setBill_no(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 16:
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setDeclaration_code(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 17:
							try {
								// port no
								if (cellvalue != null && !"".equals(cellvalue)) {
									HarborCategoryReqInfo hbReqInfo = new HarborCategoryReqInfo();
									harborCategoryService.selectByCode(cellvalue, hbReqInfo);
									if (hbReqInfo.getHarbor_category() != null) {
										lix.setHarbor_category(hbReqInfo.getHarbor_category());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 18:
							try {
								// edt date
								if (cellvalue != null) {
									Date edtd = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									lix.setEtd_date(edtd);
									lix.setETD(edtd);
								}
							} catch (Exception e) {
							}
							break;
						case 19:
							try {
								// đơn vị tiền
								if (cellvalue != null && !"".equals(cellvalue)) {
									Currency currency = currencyService.selectByType(cellvalue);
									lix.setCurrency(currency);
								}
							} catch (Exception e) {
							}
							break;
						case 21:
							try {
								// id hóa dơn
								if (cellvalue != null && !"".equals(cellvalue)) {
									InvoiceReqInfo invoiceReqInfo = new InvoiceReqInfo();
									invoiceService.selectByInvoiceCodeOnlyId(cellvalue, invoiceReqInfo);
									if (invoiceReqInfo.getInvoice() != null) {
										lix.setInvoice(invoiceReqInfo.getInvoice());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 22:
							try {
								// mã thủ kho
								if (cellvalue != null && !"".equals(cellvalue)) {
									StockerReqInfo stockerReqInfo = new StockerReqInfo();
									stockerService.selectByCode(cellvalue, stockerReqInfo);
									if (stockerReqInfo.getStocker() != null) {
										lix.setStocker(stockerReqInfo.getStocker());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 23:
							try {
								// mã bốc xếp
								if (cellvalue != null && !"".equals(cellvalue)) {
									StevedoreReqInfo stevedoreReqInfo = new StevedoreReqInfo();
									stevedoreService.selectByCode(cellvalue, stevedoreReqInfo);
									if (stevedoreReqInfo.getStevedore() != null) {
										lix.setStevedore(stevedoreReqInfo.getStevedore());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 24:
							try {
								// ngoài giờ
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setTime_out(Boolean.parseBoolean(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 25:
							try {
								// hình thức bốc xếp
								if (cellvalue != null && !"".equals(cellvalue)) {
									FormUpGoodsReqInfo formUpGoodsReqInfo = new FormUpGoodsReqInfo();
									formUpGoodsService.selectByCode(cellvalue, formUpGoodsReqInfo);
									if (formUpGoodsReqInfo.getForm_up_goods() != null) {
										lix.setForm_up_goods(formUpGoodsReqInfo.getForm_up_goods());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 26:
							try {
								// đã giao hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setDelivered(Boolean.parseBoolean(Objects.toString(cellvalue)));
								}
							} catch (Exception e) {
							}
							break;
						case 27:
							try {
								// mã nhân viên bán hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									BindUser bindUser = bindUserService.getBindUserByIdFox(cellvalue);
									if (bindUser != null) {
										lix.setCreated_by(MyConvertFontFactory.convert(FontType.VNI_WINDOWS,
												FontType.UNICODE, bindUser.getMember_name()));
									} else {
										lix.setCreated_by(cellvalue);
									}
								}
							} catch (Exception e) {
							}
							break;
						case 28:
							try {
								// shippedper
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setShipped_per(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 30:
							try {
								// term_delive
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setTerm_of_delivery(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 33:
							try {
								// load date
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date ld = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									if (ld != null) {
										lix.setUp_goods_date(ld);
									}
								}
							} catch (Exception e) {
							}
							break;
						case 34:
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									HarborCategoryReqInfo pot = new HarborCategoryReqInfo();
									harborCategoryService.selectByCode(cellvalue, pot);
									lix.setPost_of_tran(pot.getHarbor_category());
								}
							} catch (Exception e) {
							}
							break;
						case 35:
							try {
								// freight
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setFreight(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 36:
							try {
								// ref no
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setReference_no(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 41:
							try {
								// ghi chú tạm
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setNote(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 42:
							try {
								// shipping mark
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setShipping_mark(cellvalue);
								}
							} catch (Exception e) {
							}
							break;

						}
					}
					listIEInvoiceTemp.add(lix);
				}
				workBook = null;// free
				for (IEInvoice it : listIEInvoiceTemp) {
					IEInvoiceReqInfo t = new IEInvoiceReqInfo();
					IEInvoice ieInvoice = iEInvoiceService.selectByCodeOnlyId(it.getInvoice_code());
					t.setIe_invoice(it);
					if (ieInvoice != null) {
						it.setId(ieInvoice.getId());
						it.setLast_modifed_by(account.getMember().getName());
						it.setLast_modifed_date(new Date());
						iEInvoiceService.update(t);
					} else {
						// it.setCreated_by(account.getMember().getName());
						it.setCreated_date(new Date());
						iEInvoiceService.insert(t);
					}
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napXuatXuatKhau:" + e.getMessage(), e);
		}
	}

	@Inject
	IBindUserService bindUserService;

	public void napDonHang(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<OrderLix> listOrderLixTemp = new ArrayList<OrderLix>();
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
					OrderLix lix = new OrderLix();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// mã đơn hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setOrder_code(cellvalue);
									lix.setIdfox(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// mã khách hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									CustomerReqInfo cReqInfo = new CustomerReqInfo();
									customerService.selectByCode(cellvalue, cReqInfo);
									if (cReqInfo.getCustomer() != null) {
										lix.setCustomer(cReqInfo.getCustomer());
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
								// mã xuất nhập
								if (cellvalue != null && !"".equals(cellvalue)) {
									IECategoriesReqInfo iereq = new IECategoriesReqInfo();
									iECategoriesService.selectByCodeOld(cellvalue, iereq);
									if (iereq.getIe_categories() != null) {
										lix.setIe_categories(iereq.getIe_categories());
									} else {
										continue lv1;
									}
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// ma kho
								if (cellvalue != null && !"".equals(cellvalue)) {
									WarehouseReqInfo wreq = new WarehouseReqInfo();
									warehouseService.selectByCodeOld(cellvalue, wreq);
									if (wreq.getWarehouse() != null) {
										lix.setWarehouse(wreq.getWarehouse());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								// số chứng từ
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setVoucher_code(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 5:
							try {
								// ngày đơn hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date orderDate = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									if (orderDate != null) {
										lix.setOrder_date(orderDate);
									} else {
										continue lv1;
									}

								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// số hợp đồng
								lix.setContract_no(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								// số xe
								if (cellvalue != null && !"".equals(cellvalue)) {
									CarReqInfo carReq = new CarReqInfo();
									carService.selectByLicensePlate(cellvalue, carReq);
									if (carReq.getCar() != null) {
										lix.setCar(carReq.getCar());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 8:
							try {
								// số hóa đơn
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setInvoice_no(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 9:
							try {
								// hệ số thuế
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setTax_value(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 10:
							try {
								// hình thức thanh toan
								if (cellvalue != null && !"".equals(cellvalue)) {
									PaymentMethodReqInfo payReq = new PaymentMethodReqInfo();
									paymentMethodService.selectByCodeOld(cellvalue, payReq);
									if (payReq.getPayment_method() != null) {
										lix.setPayment_method(payReq.getPayment_method());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 15:
							try {
								// số serie
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setSerie_no(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 17:
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setDelivered(Boolean.parseBoolean(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 18:
							try {
								// ngay giao hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date deliveryDate = ToolTimeCustomer
											.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									if (deliveryDate != null) {
										lix.setDelivery_date(deliveryDate);
									}
								}
							} catch (Exception e) {
							}
							break;
						case 19:
							try {
								// mã noi vc
								if (cellvalue != null && !"".equals(cellvalue)) {
									DeliveryPricingReqInfo dpreq = new DeliveryPricingReqInfo();
									deliveryPricingService.selectByPlaceCode(cellvalue, dpreq);
									if (dpreq.getDelivery_pricing() != null) {
										lix.setDelivery_pricing(dpreq.getDelivery_pricing());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 20:
							// mã nhân viên bán hàng
							if (cellvalue != null && !"".equals(cellvalue)) {
								BindUser bindUser = bindUserService.getBindUserByIdFox(cellvalue);
								if (bindUser != null) {
									lix.setCreated_by(MyConvertFontFactory.convert(FontType.VNI_WINDOWS,
											FontType.UNICODE, bindUser.getMember_name()));
								} else {
									lix.setCreated_by(cellvalue);
								}
							}
							break;
						case 21:
							// mã số chương trình km
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									PromotionProgramReqInfo propReq = new PromotionProgramReqInfo();
									promotionProgramService.selectByCode(cellvalue, propReq);
									if (propReq.getPromotion_program() != null) {
										lix.setPromotion_program(propReq.getPromotion_program());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 22:
							// mã số chương trình đơn giá.
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									PricingProgramReqInfo pripReq = new PricingProgramReqInfo();
									pricingProgramService.selectByCode(cellvalue, pripReq);
									if (pripReq.getPricing_program() != null) {
										lix.setPricing_program(pripReq.getPricing_program());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 24:
							// so po
							if (cellvalue != null && !"".equals(cellvalue)) {
								lix.setPo_no(cellvalue);
							}
							break;
						case 27:
							try {
								lix.setNote(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 28:
							try {
								lix.setReason_not_delivered(cellvalue);
							} catch (Exception e) {
							}
							break;
						}
					}
					listOrderLixTemp.add(lix);
				}
				workBook = null;// free
				for (OrderLix it : listOrderLixTemp) {
					OrderLixReqInfo t = new OrderLixReqInfo();
					orderLixService.selectByOrderCode(it.getOrder_code(), t);
					OrderLix p = t.getOrder_lix();
					t.setOrder_lix(it);
					if (p != null) {
						it.setId(p.getId());
						it.setLast_modifed_by(account.getMember().getName());
						it.setLast_modifed_date(new Date());
						orderLixService.update(t);
					} else {
						it.setCreated_date(new Date());
						orderLixService.insertByLoad(t);
					}
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napCTDonGia:" + e.getMessage(), e);
		}
	}

	public void napHopDong(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				List<Contract> listContractTemp = new ArrayList<Contract>();
				Workbook workBook = getWorkbook(event.getFile().getInputStream(), part.getFileName());
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
					Contract lix = new Contract();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// mã hợp đồng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setContract_code(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// Số hợp đồng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setVoucher_code(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// mã khách hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									CustomerReqInfo cusrq = new CustomerReqInfo();
									customerService.selectByCode(cellvalue, cusrq);
									if (cusrq.getCustomer() != null) {
										lix.setCustomer(cusrq.getCustomer());
									} else {
										continue lv1;
									}
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// ngày min
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date effectiveDate = ToolTimeCustomer.convertStringToDateFox(cellvalue,
											"dd/MM/yyyy");
									if (effectiveDate == null) {
										continue lv1;
									}
									lix.setEffective_date(effectiveDate);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								// ngày max
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date expriedDate = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									lix.setExpiry_date(expriedDate);
								}
							} catch (Exception e) {
							}
							break;
						case 5:
							try {
								// thanh lý
								if (cellvalue != null && !"".equals(cellvalue)) {
									boolean pd = Boolean.parseBoolean(cellvalue);
									lix.setLiquidated(pd);
								}
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// đơn vị tiền
								if (cellvalue != null && !"".equals(cellvalue)) {
									Currency currency = currencyService.selectByType(cellvalue);
									lix.setCurrency(currency);
								}
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								// ngày nhập hợp đồng
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date dateng = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									lix.setContract_date(dateng);
								} else {
									lix.setContract_date(lix.getEffective_date());
								}
							} catch (Exception e) {
							}
							break;
						}
					}
					listContractTemp.add(lix);
				}
				workBook = null;// free
				for (Contract it : listContractTemp) {
					ContractReqInfo t = new ContractReqInfo();
					// select code
					Contract contractTrans = contractService.selectByCodeOnlyId(it.getContract_code());
					if (contractTrans != null) {
						it.setId(contractTrans.getId());
						it.setLast_modifed_by(account.getMember().getName());
						it.setLast_modifed_date(new Date());
						contractService.update(new ContractReqInfo(it));
					} else {
						it.setCreated_date(new Date());
						it.setCreated_by(account.getMember().getName());
						contractService.insertLoad(new ContractReqInfo(it));
					}
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napHopDong:" + e.getMessage(), e);
		}
	}

	public void napHopDongVC(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				List<FreightContract> listContractTemp = new ArrayList<FreightContract>();
				Workbook workBook = getWorkbook(event.getFile().getInputStream(), part.getFileName());
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
					FreightContract lix = new FreightContract();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// mã hợp đồng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setContract_code(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// Số hợp đồng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setContract_no(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// ngày
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date effectiveDate = ToolTimeCustomer.convertStringToDateFox(cellvalue,
											"dd/MM/yyyy");
									if (effectiveDate == null) {
										continue lv1;
									}
									lix.setContract_date(effectiveDate);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// mã khách hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									CustomerReqInfo cusrq = new CustomerReqInfo();
									customerService.selectByCode(cellvalue, cusrq);
									if (cusrq.getCustomer() != null) {
										lix.setCustomer(cusrq.getCustomer());
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
								// so xe
								if (cellvalue != null && !"".equals(cellvalue)) {
									Car car = carService.selectByCode(cellvalue);
									if (car != null) {
										lix.setCar(car);
									}
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 5:
							try {
								// thanh lý
								String thanhly = Objects.toString(MyUtilExcel.getCellValue(cell), "FALSE");
								if (thanhly != null && !"".equals(thanhly)) {
									boolean pd = Boolean.parseBoolean(thanhly);
									lix.setDisable(pd);
								}
							} catch (Exception e) {
							}
							break;

						case 7:
							try {
								// ngày thanh ly
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date dateng = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									lix.setPayment_date(dateng);
								}
							} catch (Exception e) {
							}
							break;

						}
					}
					listContractTemp.add(lix);
				}
				workBook = null;// free
				for (FreightContract it : listContractTemp) {
					// select code
					FreightContract idOld = freightContractService.selectByOrderCode(it.getContract_code());
					if (idOld != null) {
						it.setId(idOld.getId());
						it.setLast_modifed_by(account.getMember().getName());
						it.setLast_modifed_date(new Date());
						freightContractService.update(new FreightContractReqInfo(it));
					} else {
						it.setCreated_date(new Date());
						it.setCreated_by(account.getMember().getName());
						freightContractService.insert(new FreightContractReqInfo(it));
					}
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napHopDong:" + e.getMessage(), e);
		}
	}

	public void napCTDonHang(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<OrderDetail> listOrderDetail = new ArrayList<>();
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
					OrderDetail lix = new OrderDetail();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// mã đơn hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									OrderLixReqInfo orderReqInfo = new OrderLixReqInfo();
									orderLixService.selectByOrderCodeId(cellvalue, orderReqInfo);
									if (orderReqInfo.getOrder_lix() != null) {
										lix.setOrder_lix(orderReqInfo.getOrder_lix());
									}
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// mã sản phẩm
								if (cellvalue != null && !"".equals(cellvalue)) {
									ProductReqInfo pReq = new ProductReqInfo();
									productService.selectByCode(cellvalue, pReq);
									if (pReq.getProduct() != null) {
										lix.setProduct(pReq.getProduct());
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
							// max số
							lix.setNote(cellvalue);
							break;

						case 3:
							try {
								// Số thùng
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setBox_quantity(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								// Số lượng
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setQuantity(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 5:
							try {
								// đơn giá
								cellvalue = cellvalue.replace(",", ".");
								lix.setUnit_price(Double.parseDouble(cellvalue));
								lix.setUnit_price_goc(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// so tien
								cellvalue = cellvalue.replace(",", ".");
								lix.setTotal(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								lix.setIdfox(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 9:
							try {
								// mã lô hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setBatch_code(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 11:
							try {
								// Số thùng thuc te
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setBox_quantity_actual(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 15:
							try {
								// hình thức km
								lix.setPromotion_forms((int) Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						}
					}
					listOrderDetail.add(lix);
				}
				workBook = null;// free
				for (OrderDetail it : listOrderDetail) {
					it.setCreated_date(new Date());
					it.setCreated_by(account.getMember().getName());
					OrderDetailReqInfo t = new OrderDetailReqInfo(it);
					orderDetailService.insertLoad(t);
				}
				notify.success();
			}

		} catch (Exception e) {
			logger.error("DataSettingBean.napCTDonHang:" + e.getMessage(), e);
		}
	}

	public void napCTXuatHang(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<InvoiceDetail> listInvoiceDetail = new ArrayList<>();
				Workbook workBook = getWorkbook(new ByteArrayInputStream(byteFile), part.getFileName());
				Sheet firstSheet = workBook.getSheetAt(0);
				Iterator<Row> rows = firstSheet.iterator();
				while (rows.hasNext()) {
					rows.next();
					rows.remove();
					break;
				}
				Set<Invoice> invoices = new HashSet<Invoice>();
				lv1: while (rows.hasNext()) {
					Row row = rows.next();
					Iterator<Cell> cells = row.cellIterator();
					InvoiceDetail lix = new InvoiceDetail();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// mã phiếu xuất
								if (cellvalue != null && !"".equals(cellvalue)) {
									InvoiceReqInfo invoiceReqInfo = new InvoiceReqInfo();
									invoiceService.selectByInvoiceCodeOnlyId(cellvalue, invoiceReqInfo);

									if (invoiceReqInfo.getInvoice() != null) {
										lix.setInvoice(invoiceReqInfo.getInvoice());
										invoices.add(invoiceReqInfo.getInvoice());
									} else {
										System.out.println(cellvalue);
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
								// mã sản phẩm
								if (cellvalue != null && !"".equals(cellvalue)) {
									ProductReqInfo pReq = new ProductReqInfo();
									productService.selectByCode(cellvalue, pReq);
									if (pReq.getProduct() != null) {
										lix.setProduct(pReq.getProduct());
									} else {
										System.out.println(cellvalue);
										// continue lv1;
									}
								} else {
									// continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// Số lượng
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setQuantity(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;

						case 3:
							try {
								// đongia
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setUnit_price(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								// so tien
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setTotal(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 5:
							try {
								// ma số
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setNote(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								lix.setIdfox(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 9:
							try {
								// mã lô hàng
								lix.setNote_batch_code(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 10:
							try {
								// mã sản phẩm đơn hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setProductdh_code(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 11:
							try {
								// đơn giá ngoại tệ
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setForeign_unit_price(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 15:
							try {
								// guid
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setRefDetailID(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						}
					}
					listInvoiceDetail.add(lix);
				}
				workBook = null;// free
				for (InvoiceDetail it : listInvoiceDetail) {
					it.setReal_quantity(it.getQuantity());
					it.setCreated_date(new Date());
					it.setCreated_by(account.getMember().getName());
					InvoiceDetailReqInfo t = new InvoiceDetailReqInfo(it);
					invoiceDetailService.insert(t);
				}
				List<InvoiceDetail> invoiceDetails = null;
				for (Invoice it : invoices) {
					invoiceDetails = new ArrayList<InvoiceDetail>();
					invoiceDetailService.selectByInvoice(it.getId(), invoiceDetails);
					if (invoiceDetails != null && invoiceDetails.size() > 0) {
						double totalevent = invoiceDetails.stream().mapToDouble(f -> f.getTotal()).sum();
						it.setTongtien(MyMath.roundCustom(totalevent, 2));
						it.setThue(MyMath.roundCustom(totalevent * it.getTax_value(), 2));
						invoiceService.updateTotalTax(it, getAccount().getUserName());
					}
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napCTXuatHang:" + e.getMessage(), e);
		}
	}

	public void napCTXuatXuatKhau(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				List<IEInvoiceDetail> listIEInvoiceDetail = new ArrayList<>();
				Workbook workBook = getWorkbook(event.getFile().getInputStream(), part.getFileName());
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
					IEInvoiceDetail lix = new IEInvoiceDetail();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();

						switch (columnIndex) {
						case 0:
							try {
								// mã xuất xuất khẩu
								if (cellvalue != null && !"".equals(cellvalue)) {
									IEInvoice ieInvoice = iEInvoiceService.selectByCodeOnlyId(cellvalue);
									if (ieInvoice != null) {
										lix.setIe_invoice(ieInvoice);
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
								// mã sản phẩm
								if (cellvalue != null && !"".equals(cellvalue)) {
									ProductReqInfo pReq = new ProductReqInfo();
									productService.selectByCode(cellvalue, pReq);
									if (pReq.getProduct() != null) {
										lix.setProduct(pReq.getProduct());
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
								// Số lượng
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setQuantity(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;

						case 3:
							try {
								// đongia
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setUnit_price(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setTotal_amount(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								lix.setIdfox(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 9:
							try {
								// đơn giá nt
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setForeign_unit_price(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 10:
							try {
								// số tiền nt
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setTotal_foreign_amount(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 11:
							try {
								// cont no
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setContainer_no(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 12:
							try {
								// cont_20_40;
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setFt_container(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 13:
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setOrder_no(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 15:
							try {
								// số lượng xk
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setQuantity_export(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 16:
							try {
								// số tiền nt xuất khẩu
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setTotal_export_foreign_amount(Double.parseDouble(cellvalue));
								}

							} catch (Exception e) {
							}
							break;
						case 17:
							try {
								// số cont
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setContainer_number((int) Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 18:
							try {
								// nơi đến
								if (cellvalue != null && !"".equals(cellvalue)) {
									// <f:selectItem itemLabel="--select--"
									// itemValue="" />
									// <f:selectItem itemLabel="Bình Dương"
									// itemValue="Bình Dương" />
									// <f:selectItem
									// itemLabel="Bình Dương Cát Lái"
									// itemValue="Bình Dương Cát Lái" />
									// <f:selectItem itemLabel="Khác"
									// itemValue="Khác" />
									if ("B".equals(cellvalue)) {
										lix.setArrival_place("Bình Dương");
									} else if ("C".equals(cellvalue)) {
										lix.setArrival_place("Cát Lái");
									} else if ("T".equals(cellvalue)) {
										lix.setArrival_place("Bình Dương Cát Lái");
									}
								}
							} catch (Exception e) {
							}
							break;
						case 19:
							try {
								// mã lô hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setBatch_code(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						}
					}
					listIEInvoiceDetail.add(lix);
				}
				workBook = null;// free
				for (IEInvoiceDetail it : listIEInvoiceDetail) {
					IEInvoiceDetailReqInfo t = new IEInvoiceDetailReqInfo(it);
					it.setCreated_date(new Date());
					it.setCreated_by(account.getMember().getName());
					iEInvoiceService.insertDetail(t);
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napCTXuatXuatKhau:" + e.getMessage(), e);
		}
	}

	public void napCTHopDong(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				List<ContractDetail> listContractDetailTemp = new ArrayList<>();
				Workbook workBook = getWorkbook(event.getFile().getInputStream(), part.getFileName());
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
					ContractDetail lix = new ContractDetail();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// mã hop đồng
								if (cellvalue != null && !"".equals(cellvalue)) {
									Contract contract = contractService.selectByCodeOnlyId(cellvalue);
									if (contract == null) {
										continue lv1;
									}
									lix.setContract(contract);
								} else {
									continue lv1;
								}
							} catch (Exception e) {

							}
							break;
						case 1:
							try {
								// mã sản phẩm
								if (cellvalue != null && !"".equals(cellvalue)) {
									ProductReqInfo pReq = new ProductReqInfo();
									productService.selectByCode(cellvalue, pReq);
									if (pReq.getProduct() != null) {
										lix.setProduct(pReq.getProduct());
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
							// Số lượng
							if (cellvalue != null && !"".equals(cellvalue)) {
								cellvalue = cellvalue.replace(",", ".");
								lix.setQuantity(Double.parseDouble(Objects.toString(cellvalue)));
							}
							break;

						case 3:
							try {
								// đơn giá
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setUnit_price(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								// đơn giá
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setTotal(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// khuyến mãi
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setPromotion(Boolean.parseBoolean(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								// Lợi nhuận
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setProfit(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						}
					}
					listContractDetailTemp.add(lix);
				}
				workBook = null;// free
				for (ContractDetail it : listContractDetailTemp) {
					ContractDetailReqInfo t = new ContractDetailReqInfo(it);
					contractService.insertDetail(t);
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napCTDonHang:" + e.getMessage(), e);
		}
	}

	public void napCTHopDongVC(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				List<FreightContractDetail> listContractDetailTemp = new ArrayList<>();
				Workbook workBook = getWorkbook(event.getFile().getInputStream(), part.getFileName());
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
					FreightContractDetail lix = new FreightContractDetail();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// mã hop đồng
								if (cellvalue != null && !"".equals(cellvalue)) {
									FreightContract contract = freightContractService.selectByOrderCode(cellvalue);
									if (contract == null) {
										continue lv1;
									}
									lix.setFreight_contract(contract);
								} else {
									continue lv1;
								}
							} catch (Exception e) {

							}
							break;
						case 1:
							try {
								// loai sản phẩm
								if (cellvalue != null && !"".equals(cellvalue)) {
									ProductType productType = productTypeService.selectByCode(cellvalue);
									if (productType != null) {
										lix.setProduct_type(productType);
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
								// đơn giá
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setUnit_price(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 3:
							// Số lượng
							if (cellvalue != null && !"".equals(cellvalue)) {
								cellvalue = cellvalue.replace(",", ".");
								lix.setQuantity(Double.parseDouble(Objects.toString(cellvalue)));
							}
							break;

						case 4:
							try {
								// so tien
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setTotal_amount(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 5:
							try {
								// don gia ho tro
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setSup_unit_price(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 6:
							try {

								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setGc(Boolean.parseBoolean(cellvalue));
								}
							} catch (Exception e) {
							}
							break;

						}
					}
					listContractDetailTemp.add(lix);
				}
				workBook = null;// free
				for (FreightContractDetail it : listContractDetailTemp) {
					FreightContractDetailReqInfo t = new FreightContractDetailReqInfo(it);
					freightContractService.insertDetail(t);
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napCTDonHang:" + e.getMessage(), e);
		}
	}

	public void napKMDonhang(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<PromotionOrderDetailFake> listPromotionOrderDetailFake = new ArrayList<>();
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
					PromotionOrderDetailFake lix = new PromotionOrderDetailFake();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// mã đơn hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setOrder_code(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// mã sản phẩm đơn hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setProductdh_code(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 2:
							// mã sản phẩm dc km
							if (cellvalue != null && !"".equals(cellvalue)) {
								ProductReqInfo pReq = new ProductReqInfo();
								productService.selectByCode(cellvalue, pReq);
								if (pReq.getProduct() != null) {
									lix.setProduct(pReq.getProduct());
								} else {
									continue lv1;
								}
							} else {
								continue lv1;
							}
							break;

						case 3:
							try {
								// Số lượng
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setQuantity(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								// đơn giá
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setUnit_price(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// mã số
								lix.setSpecification(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								// mã sản phẩm km
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setProductkm_code(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 8:
							try {
								// ghi chú
								lix.setNote(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 10:
							try {
								// mã lô hàng
								lix.setBatch_code(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 13:
							try {
								lix.setOwe(Boolean.parseBoolean(cellvalue));
							} catch (Exception e) {
							}
							break;
						}
					}
					if (lix.getOrder_code() != null && !"".equals(lix.getOrder_code()) && lix.getProduct() != null
							&& lix.getProductdh_code() != null) {
						listPromotionOrderDetailFake.add(lix);
					}
				}
				workBook = null;// free
				List<String> listSpecTrack = new ArrayList<>();
				p: for (PromotionOrderDetailFake it : listPromotionOrderDetailFake) {
					PromotionOrderDetail item = new PromotionOrderDetail();
					// truy tìm orderdetail
					List<OrderDetail> listDetail = new ArrayList<>();
					orderDetailService.selectBy(it.getOrder_code(), it.getProductdh_code(), listDetail);
					if (listDetail.size() > 0) {
						boolean kt = false;
						for (OrderDetail dt : listDetail) {
							if (dt.getPromotion_forms() != 0) {
								item.setOrder_detail(dt);
								kt = true;
								break;
							}
						}
						if (!kt) {
							continue p;
						}
					} else {
						listSpecTrack.add(it.getOrder_code() + it.getProductdh_code());
						continue p;
					}
					item.setCreated_date(new Date());
					item.setCreated_by(account.getMember().getName());
					item.setBatch_code(it.getBatch_code());
					item.setProductdh_code(it.getProductdh_code());
					item.setProduct(it.getProduct());
					item.setSpecification(it.getSpecification());
					item.setProductkm_code(it.getProductkm_code());
					item.setQuantity(it.getQuantity());
					item.setUnit_price(it.getUnit_price());
					item.setNote(it.getNote());
					item.setOwe(it.isOwe());
					promotionOrderDetailService.insert(new PromotionOrderDetailReqInfo(item));

				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napKMDonhang:" + e.getMessage(), e);
		}
	}

	public void napCTDonGia(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<PricingProgram> listPricingProgramTemp = new ArrayList<PricingProgram>();
				Workbook workBook = getWorkbook(new ByteArrayInputStream(byteFile), part.getFileName());
				Sheet firstSheet = workBook.getSheetAt(0);
				Iterator<Row> rows = firstSheet.iterator();
				List<String> listParentNotRef = new ArrayList<>();
				while (rows.hasNext()) {
					rows.next();
					rows.remove();
					break;
				}
				int i = 0;
				lv1: while (rows.hasNext()) {
					Row row = rows.next();
					Iterator<Cell> cells = row.cellIterator();
					PricingProgram lix = new PricingProgram();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							i++;
							try {
								// mã chương trình
								if (cellvalue != null && !"".equals(cellvalue) && cellvalue.contains("DG")) {
									lix.setProgram_code(cellvalue);
								} else {
									System.out.println("sssssss");
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// từ ngày
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date tn = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									if (tn == null) {
										System.out.println(cellvalue);
										continue lv1;
									} else {
										lix.setEffective_date(tn);
									}
								}
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// denngay
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date dn = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									if (dn == null) {
										System.out.println(cellvalue);
									} else {
										lix.setExpiry_date(dn);
									}

								}
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// mã chương trình chính
								if (cellvalue != null && !"".equals(cellvalue)) {
									PricingProgramReqInfo parent = new PricingProgramReqInfo();
									pricingProgramService.selectByCode(cellvalue, parent);
									if (parent.getPricing_program() == null) {
										System.out.println("không tìm thấy chương trình đơn giá cha");
										listParentNotRef.add(cellvalue);
									}
									lix.setParent_pricing_program(parent.getPricing_program());

								}
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								// update time
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date updatetime = ToolTimeCustomer.convertStringToDateFox(cellvalue,
											"dd/MM/yyyy HH:mm:ss");
									if (updatetime == null) {
										System.out.println(cellvalue);
									}
									lix.setUpdate_time(updatetime);
								}
							} catch (Exception e) {
							}
							break;
						case 5:
							try {
								String note = Objects.toString(MyUtilExcel.getCellValue(cell), null);
								lix.setNote(note);
							} catch (Exception e) {
							}
							break;

						}
					}
					listPricingProgramTemp.add(lix);
				}
				workBook = null;// free
				for (PricingProgram it : listPricingProgramTemp) {
					PricingProgramReqInfo t = new PricingProgramReqInfo();
					pricingProgramService.selectByCode(it.getProgram_code(), t);
					PricingProgram p = t.getPricing_program();
					t.setPricing_program(it);
					if (p != null) {
						it.setId(p.getId());
						// it.setLast_modifed_by(account.getMember().getName());
						// it.setLast_modifed_date(new Date());
						it.setCreated_by(account.getMember().getName());
						it.setCreated_date(new Date());
						pricingProgramService.update(t);
					} else {
						it.setCreated_by(account.getMember().getName());
						it.setCreated_date(new Date());
						pricingProgramService.insert(t);
					}
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napCTDonGia:" + e.getMessage(), e);
		}
	}

	public void napPhieuNhap(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				// UploadedFile part = event.getFile();
				// byte[] byteFile = event.getFile().getContent();
				List<GoodsReceiptNoteFake> listGoodsReceiptFake = new ArrayList<GoodsReceiptNoteFake>();
				Workbook workBook = getWorkbook(event.getFile().getInputStream(), event.getFile().getFileName());
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
					GoodsReceiptNoteFake lix = new GoodsReceiptNoteFake();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// Số chứng từ.
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setVoucher_code(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// ngaynhap
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date importDate = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									if (importDate == null) {
										continue lv1;
									} else {
										lix.setImport_date(importDate);
									}
								}
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// mã khách hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									CustomerReqInfo cReq = new CustomerReqInfo();
									customerService.selectByCodeId(cellvalue, cReq);
									if (cReq.getCustomer() != null) {
										lix.setCustomer(cReq.getCustomer());
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
								// mã kho
								if (cellvalue != null && !"".equals(cellvalue)) {
									WarehouseReqInfo wReq = new WarehouseReqInfo();
									warehouseService.selectByCodeOld(cellvalue, wReq);
									if (wReq.getWarehouse() != null) {
										lix.setWarehouse(wReq.getWarehouse());
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
								// mã xuất nhập
								if (cellvalue != null && !"".equals(cellvalue)) {
									IECategoriesReqInfo ieReq = new IECategoriesReqInfo();
									iECategoriesService.selectByCodeOld(cellvalue, ieReq);
									if (ieReq.getIe_categories() != null) {
										lix.setIe_categories(ieReq.getIe_categories());
									} else {
										continue lv1;
									}
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// mã sản phẩm
								if (cellvalue != null && !"".equals(cellvalue)) {
									ProductReqInfo pReq = new ProductReqInfo();
									productService.selectByCode(cellvalue, pReq);
									if (pReq.getProduct() != null) {
										lix.setProduct(pReq.getProduct());
									} else {
										continue lv1;
									}
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								// số lượng
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setQuantity(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 8:
							// đơn giá
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setUnit_price(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 9:
							// Số tiền
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									cellvalue = cellvalue.replace(",", ".");
									lix.setTotal(Double.parseDouble(cellvalue));
								}
							} catch (Exception e) {
							}
							break;
						case 10:
							try {
								// só hóa đơn vcnb
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setVcnb_invoice_code(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 12:
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setIdfox(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 15:
							try {
								// biển số xe
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setLicense_plate(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 16:
							try {
								// loo hangf
								lix.setBatch_code(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 17:
							try {
								// số lệnh điều động
								lix.setMovement_commands(cellvalue);
							} catch (Exception e) {
							}
							break;

						case 20:
							try {
								lix.setSogiaonhan(cellvalue);
							} catch (Exception e) {
							}
							break;
						}
					}
					listGoodsReceiptFake.add(lix);
				}
				workBook = null;// free
				Map<KeyPhieuNhap, List<GoodsReceiptNoteFake>> map = new LinkedHashMap<KeyPhieuNhap, List<GoodsReceiptNoteFake>>();
				for (GoodsReceiptNoteFake it : listGoodsReceiptFake) {
					// convert to entity
					KeyPhieuNhap keyPhieuNhap = new KeyPhieuNhap(it.getVoucher_code(), it.getImport_date(),
							it.getCustomer(), it.getWarehouse(), it.getIe_categories(), it.getBatch_code());
					if (!map.containsKey(keyPhieuNhap)) {
						map.put(keyPhieuNhap, new ArrayList<GoodsReceiptNoteFake>());
					}
					map.get(keyPhieuNhap).add(it);

				}
				long index = 0;
				// loop map
				for (Map.Entry<KeyPhieuNhap, List<GoodsReceiptNoteFake>> entry : map.entrySet()) {
					KeyPhieuNhap key = entry.getKey();
					List<GoodsReceiptNoteFake> listFake = entry.getValue();
					// chekc phiếu nhập đã tồn tại chưa
					GoodsReceiptNote goodsTrans = goodsReceiptNoteService.checkReceiptNoteExists(key.getVoucherCode(),
							key.getBatch_code(), key.getImportDate(), key.getCustomer().getId(), key.getWarehouse()
									.getId(), key.getIe_categories().getId());
					if (goodsTrans == null) {
						// tạo phiếu nhập dự trên key.
						GoodsReceiptNote phieu = new GoodsReceiptNote();
						phieu.setVoucher_code(key.getVoucherCode());
						phieu.setImport_date(key.getImportDate());
						phieu.setCustomer(key.getCustomer());
						// check nếu code là phân xưởng công nghệ
						if (key.getCustomer() != null && "N".equals(key.getCustomer().getCustomer_code())) {
							if (listFake.size() > 0) {
								phieu.setBatch_code(listFake.get(0).getBatch_code());
							}
						}
						phieu.setWarehouse(key.getWarehouse());
						phieu.setIe_categories(key.getIe_categories());
						// phieu.setVcnb_invoice_code(key.getVcnb_invoice_code());
						// phieu.setLicense_plate(license_plate);
						phieu.setCreated_by(account.getMember().getName());
						phieu.setCreated_date(new Date());
						GoodsReceiptNoteReqInfo goodReq = new GoodsReceiptNoteReqInfo(phieu);
						goodsReceiptNoteService.insert(goodReq);
						goodsTrans = goodReq.getGoods_receipt_note();
					}
					if (goodsTrans != null) {
						// insert detail
						for (GoodsReceiptNoteFake dt : listFake) {
							index++;
							GoodsReceiptNoteDetail detail = new GoodsReceiptNoteDetail();
							detail.setGoods_receipt_note(goodsTrans);
							detail.setCreated_by(account.getMember().getName());
							detail.setCreated_date(new Date());
							detail.setProduct(dt.getProduct());
							detail.setQuantity(dt.getQuantity());
							detail.setPrice(dt.getUnit_price());
							detail.setTotal(dt.getTotal());
							detail.setLicense_plate(dt.getLicense_plate());
							detail.setVcnb_invoice_code(dt.getVcnb_invoice_code());
							detail.setBatch_code(dt.getBatch_code());
							detail.setDelivery_code(dt.getSogiaonhan());
							detail.setIdfox(dt.getIdfox());
							GoodsReceiptNoteDetailReqInfo td = new GoodsReceiptNoteDetailReqInfo(detail);
							goodsReceiptNoteDetailService.insert(td);
						}
					}

				}
				System.out.println("index: " + index);
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napCTDonGia:" + e.getMessage(), e);
		}
	}

	public void napCTKM(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<PromotionProgram> listPromotionProgramTemp = new ArrayList<PromotionProgram>();
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
					PromotionProgram lix = new PromotionProgram();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {

								// mã chương trình
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setProgram_code(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// từ ngày
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date tn = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									if (tn == null) {
										System.out.println(cellvalue);
										continue lv1;
									} else {
										lix.setEffective_date(tn);
									}
								}
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// denngay
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date dn = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									if (dn == null) {
										System.out.println(cellvalue);
									} else {
										lix.setExpiry_date(dn);
									}

								}
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								lix.setNote(cellvalue);
							} catch (Exception e) {
							}
							break;

						}
					}
					listPromotionProgramTemp.add(lix);
				}
				workBook = null;// free
				for (PromotionProgram it : listPromotionProgramTemp) {
					PromotionProgramReqInfo t = new PromotionProgramReqInfo();
					promotionProgramService.selectByCode(it.getProgram_code(), t);
					PromotionProgram p = t.getPromotion_program();
					t.setPromotion_program(it);
					if (p != null) {
						it.setId(p.getId());
						// it.setLast_modifed_by(account.getMember().getName());
						// it.setLast_modifed_date(new Date());
						it.setCreated_by(account.getMember().getName());
						it.setCreated_date(new Date());
						promotionProgramService.update(t);
					} else {
						it.setCreated_by(account.getMember().getName());
						it.setCreated_date(new Date());
						promotionProgramService.insert(t);
					}
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napCTKM:" + e.getMessage(), e);
		}
	}

	public void napCTCTDonGia(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<PricingProgramDetail> listPricingProgramDetailTemp = new ArrayList<PricingProgramDetail>();
				Workbook workBook = getWorkbook(new ByteArrayInputStream(byteFile), part.getFileName());
				Sheet firstSheet = workBook.getSheetAt(0);
				Iterator<Row> rows = firstSheet.iterator();
				List<String> listCodeNotRef = new ArrayList<>();
				List<String> listProductNofRel = new ArrayList<>();
				while (rows.hasNext()) {
					rows.next();
					rows.remove();
					break;
				}
				lv1: while (rows.hasNext()) {
					Row row = rows.next();
					Iterator<Cell> cells = row.cellIterator();
					PricingProgramDetail lix = new PricingProgramDetail();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// mã chương trình
								if (cellvalue != null && !"".equals(cellvalue) && cellvalue.contains("DG")) {
									// find chương trình
									PricingProgramReqInfo ppr = new PricingProgramReqInfo();
									pricingProgramService.selectByCode(cellvalue, ppr);
									if (ppr.getPricing_program() != null) {
										lix.setPricing_program(ppr.getPricing_program());
									} else {
										listCodeNotRef.add(cellvalue);
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
								// mã sản phẩm
								if (cellvalue != null && !"".equals(cellvalue)) {
									// tim sản phẩm
									ProductReqInfo preq = new ProductReqInfo();
									productService.selectByCode(cellvalue, preq);
									if (preq.getProduct() != null) {
										lix.setProduct(preq.getProduct());
									} else {
										listProductNofRel.add(preq.getProduct().getProduct_code());
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
								// số lượng
								cellvalue = cellvalue.replace(",", ".");
								lix.setQuantity(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// đơn giá
								cellvalue = cellvalue.replace(",", ".");
								lix.setUnit_price(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								// update time
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date updatetime = ToolTimeCustomer.convertStringToDateFox(cellvalue,
											"dd/MM/yyyy HH:mm:ss");
									if (updatetime == null) {
										System.out.println(cellvalue);
									}
									lix.setLast_modifed_date(updatetime);

								}
							} catch (Exception e) {
							}
							break;
						case 5:
							try {
								// lợi nhuận
								cellvalue = cellvalue.replace(",", ".");
								lix.setRevenue_per_ton(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;

						}
					}
					listPricingProgramDetailTemp.add(lix);
				}
				workBook = null;// free

				for (PricingProgramDetail it : listPricingProgramDetailTemp) {
					pricingProgramDetailService.insert(new PricingProgramDetailReqInfo(it));
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napCTCTDonGia:" + e.getMessage(), e);
		}
	}

	public void napCTCTKM(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<PromotionProgramDetail> listPromotionProgramDetailTemp = new ArrayList<PromotionProgramDetail>();
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
					PromotionProgramDetail lix = new PromotionProgramDetail();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// mã sp
								if (cellvalue != null && !"".equals(cellvalue)) {
									// find masp
									ProductReqInfo productReqInfo = new ProductReqInfo();
									productService.selectByCode(cellvalue, productReqInfo);
									if (productReqInfo.getProduct() != null) {
										lix.setProduct(productReqInfo.getProduct());
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
								// mã sản phẩm km
								if (cellvalue != null && !"".equals(cellvalue)) {
									// tim sản phẩm km
									ProductReqInfo preq = new ProductReqInfo();
									productService.selectByCode(cellvalue, preq);
									if (preq.getProduct() != null) {
										lix.setPromotion_product(preq.getProduct());
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
								// số lượng khuyến mãi
								cellvalue = cellvalue.replace(",", ".");
								lix.setPromotion_quantity(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// mã ct km
								if (cellvalue != null && !"".equals(cellvalue)) {
									// tim sản phẩm km
									PromotionProgramReqInfo ctkmreq = new PromotionProgramReqInfo();
									promotionProgramService.selectByCode(cellvalue, ctkmreq);
									if (ctkmreq.getPromotion_program() != null) {
										lix.setPromotion_program(ctkmreq.getPromotion_program());
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
								// số thùng
								cellvalue = cellvalue.replace(",", ".");
								lix.setBox_quatity(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 5:
							try {
								// số donvi sp
								cellvalue = cellvalue.replace(",", ".");
								lix.setSpecification(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// hình thức km
								lix.setPromotion_form((int) Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;

						}
					}
					listPromotionProgramDetailTemp.add(lix);
				}
				workBook = null;// free

				for (PromotionProgramDetail it : listPromotionProgramDetailTemp) {
					it.setCreated_by(account.getMember().getName());
					it.setCreated_date(new Date());
					promotionProgramDetailService.insert(new PromotionProgramDetailReqInfo(it));
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napCTCTKM:" + e.getMessage(), e);
		}
	}

	public void napCDCTDonGia(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<CustomerPricingProgram> listCusomerPricingProgram = new ArrayList<CustomerPricingProgram>();
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
					CustomerPricingProgram lix = new CustomerPricingProgram();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// mã chương trình
								if (cellvalue != null && !"".equals(cellvalue) && cellvalue.contains("DG")) {
									// find chương trình
									PricingProgramReqInfo ppr = new PricingProgramReqInfo();
									pricingProgramService.selectByCode(cellvalue, ppr);
									if (ppr.getPricing_program() != null) {
										lix.setPricing_program(ppr.getPricing_program());
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
								// mã khách hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									// tim sản phẩm
									CustomerReqInfo preq = new CustomerReqInfo();
									customerService.selectByCode(cellvalue, preq);
									if (preq.getCustomer() != null) {
										lix.setCustomer(preq.getCustomer());
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
								// từ ngày
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date effectiveDate = ToolTimeCustomer.convertStringToDateFox(cellvalue,
											"dd/MM/yyyy");
									if (effectiveDate != null) {
										lix.setEffective_date(effectiveDate);
									} else {
										continue lv1;
									}
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// từ ngày
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date expiryDate = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									if (expiryDate != null) {
										lix.setExpiry_date(expiryDate);
									} else {
									}
								} else {
								}
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								// dis
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setDisable(Boolean.parseBoolean(cellvalue));

								}
							} catch (Exception e) {
							}
							break;
						case 5:
							try {
								// note
								lix.setNote(cellvalue);
							} catch (Exception e) {
							}
							break;

						}
					}
					listCusomerPricingProgram.add(lix);
				}
				workBook = null;// free

				for (CustomerPricingProgram it : listCusomerPricingProgram) {
					customerPricingProgramService.insert(new CustomerPricingProgramReqInfo(it));
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napCDCTDonGia:" + e.getMessage(), e);
		}
	}

	public void napCDCTKM(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<CustomerPromotionProgram> listCusomerPromotionProgram = new ArrayList<CustomerPromotionProgram>();
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
					CustomerPromotionProgram lix = new CustomerPromotionProgram();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// mã khách hàng
								if (cellvalue != null && !"".equals(cellvalue)) {
									// find khách hàng
									CustomerReqInfo creq = new CustomerReqInfo();
									customerService.selectByCode(cellvalue, creq);
									if (creq.getCustomer() != null) {
										lix.setCustomer(creq.getCustomer());
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
								// mã số ct km
								if (cellvalue != null && !"".equals(cellvalue)) {
									// tim ct km
									PromotionProgramReqInfo preq = new PromotionProgramReqInfo();
									promotionProgramService.selectByCode(cellvalue, preq);
									if (preq.getPromotion_program() != null) {
										lix.setPromotion_program(preq.getPromotion_program());
									} else {
										continue lv1;
									}
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// từ ngày
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date effectiveDate = ToolTimeCustomer.convertStringToDateFox(cellvalue,
											"dd/MM/yyyy");
									if (effectiveDate != null) {
										lix.setEffective_date(effectiveDate);
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
								// từ ngày
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date expiryDate = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									if (expiryDate != null) {
										lix.setExpiry_date(expiryDate);
									} else {
									}
								} else {
								}
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// dis
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setDisable(Boolean.parseBoolean(cellvalue));

								}
							} catch (Exception e) {
							}
							break;

						}
					}
					listCusomerPromotionProgram.add(lix);
				}
				workBook = null;// free

				for (CustomerPromotionProgram it : listCusomerPromotionProgram) {
					customerPromotionProgramService.insert(new CustomerPromotionProgramReqInfo(it));
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napCDCTDG:" + e.getMessage(), e);
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

	public void napDonGiaKM(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<PromotionalPricing> listPromotionalPricingTemp = new ArrayList<>();
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
					PromotionalPricing lix = new PromotionalPricing();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									ProductReqInfo p = new ProductReqInfo();
									productService.selectByCode(cellvalue, p);
									if (p.getProduct() == null) {
										continue lv1;
									}
									lix.setProduct(p.getProduct());
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// đơn giá
								cellvalue = cellvalue.replace(",", ".");
								lix.setUnit_price(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// ngày hiệu lực
								lix.setEffective_date(ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy"));
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// ngày hết hạn
								lix.setExpiry_date(ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy"));
							} catch (Exception e) {
							}
							break;
						}
					}
					listPromotionalPricingTemp.add(lix);
				}
				workBook = null;// free
				// delete all
				promotionalPricingService.deleteAll();
				for (PromotionalPricing it : listPromotionalPricingTemp) {
					it.setCreated_by(account.getMember().getName());
					it.setCreated_date(new Date());
					promotionalPricingService.insert(new PromotionalPricingReqInfo(it));
				}
				notify.success();

			}
		} catch (Exception e) {
			logger.error("ProductBean.loadExcel:" + e.getMessage(), e);
		}
	}

	public void napNhomSPKM(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<PromotionProductGroup> listPromotionProductGroupTemp = new ArrayList<>();
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
					PromotionProductGroup lix = new PromotionProductGroup();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();

						switch (columnIndex) {
						case 0:
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setCode(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
								continue lv1;
							}
							break;
						case 1:
							try {
								lix.setName(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								lix.setUnit(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// số lượng carton
								cellvalue = cellvalue.replace(",", ".");
								lix.setCarton_quantity(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								// đơn vị carton
								lix.setCarton_unit(cellvalue);
							} catch (Exception e) {
							}
							break;
						}
					}
					listPromotionProductGroupTemp.add(lix);
				}
				workBook = null;// free
				// delete all
				promotionProductGroupService.deleteAll();
				for (PromotionProductGroup it : listPromotionProductGroupTemp) {
					promotionProductGroupService.insert(new PromotionProductGroupReqInfo(it));
				}
				notify.success();

			}
		} catch (Exception e) {
			logger.error("ProductBean.loadExcel:" + e.getMessage(), e);
		}
	}

	public void loadHarborExcel(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<HarborCategory> listHarbor = new ArrayList<HarborCategory>();
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
					HarborCategory lix = new HarborCategory();
					while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();

						switch (columnIndex) {
						case 0:
							try {
								// code
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setHarbor_code_old(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// tên cảng
								lix.setHarbor_name(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// địa chỉ
								lix.setAddress(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// type
								int type = (int) Double.parseDouble(cellvalue);
								lix.setHarbor_type(type);
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									CountryReqInfo creqinfo = new CountryReqInfo();
									countryService.selectByCode(cellvalue.trim(), creqinfo);
									lix.setCountry(creqinfo.getCountry());
								}
							} catch (Exception e) {
							}
						}
					}
					listHarbor.add(lix);
				}
				workBook = null;// free
				for (HarborCategory it : listHarbor) {
					HarborCategoryReqInfo t = new HarborCategoryReqInfo();

					harborCategoryService.selectByCode(it.getHarbor_code(), t);
					HarborCategory p = t.getHarbor_category();
					t.setHarbor_category(it);
					if (p != null) {
						it.setId(p.getId());
						it.setLast_modifed_by(account.getMember().getName());
						it.setLast_modifed_date(new Date());
						harborCategoryService.update(t);
					} else {
						// init code new
						harborCategoryService.initCode(it);
						it.setCreated_by(account.getMember().getName());
						it.setCreated_date(new Date());
						harborCategoryService.insert(t);
					}
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.loadExcel:" + e.getMessage(), e);
		}
	}

	public void napExcelCarrier(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<Carrier> listCarrier = new ArrayList<Carrier>();
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
					Carrier lix = new Carrier();
					while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();

						switch (columnIndex) {
						case 0:
							try {
								// code
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setCarrier_code(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// tên người vận chuyển
								lix.setCarrier_name(cellvalue);
							} catch (Exception e) {
							}
							break;
						}
					}
					listCarrier.add(lix);
				}
				workBook = null;// free
				for (Carrier it : listCarrier) {
					CarrierReqInfo t = new CarrierReqInfo();
					carrierService.selectByCode(it.getCarrier_code(), t);
					Carrier p = t.getCarrier();
					t.setCarrier(it);
					if (p != null) {
						it.setId(p.getId());
						carrierService.update(t);
					} else {
						it.setCreated_by(account.getMember().getName());
						it.setCreated_date(new Date());
						carrierService.insert(t);
					}
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napExcelCarrier:" + e.getMessage(), e);
		}
	}

	public void napExcelStocker(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<Stocker> listStocker = new ArrayList<Stocker>();
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
					Stocker lix = new Stocker();
					while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();

						switch (columnIndex) {
						case 0:
							try {
								// code
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setStocker_code(cellvalue);
								} else {
									continue lv1;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// tên thủ kho
								lix.setStocker_name(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// nghĩ việc
								lix.setDisable(Boolean.parseBoolean(cellvalue));
							} catch (Exception e) {
							}
							break;
						}
					}
					listStocker.add(lix);
				}
				workBook = null;// free
				for (Stocker it : listStocker) {
					StockerReqInfo t = new StockerReqInfo();
					stockerService.selectByCode(it.getStocker_code(), t);
					Stocker p = t.getStocker();
					t.setStocker(it);
					if (p != null) {
						it.setId(p.getId());
						stockerService.update(t);
					} else {
						it.setCreated_by(account.getMember().getName());
						it.setCreated_date(new Date());
						stockerService.insert(t);
					}
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napExcelCarrier:" + e.getMessage(), e);
		}
	}

	public void showDialogNapSanPham() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('napsanpham').show();");
	}

	public void showDialogUNC() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('napuynhiemchi').show();");
	}

	public void showDialogTonkho() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('naptonkho').show();");
	}

	public void napSanPham(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<Product> listProductTemp = new ArrayList<Product>();
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
					Product lix = new Product();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();

						switch (columnIndex) {
						case 0:
							try {
								// tên sản phẩm
								if (cellvalue != null && !"".equals(cellvalue)) {
									lix.setProduct_code(cellvalue);
								} else {
									break lv2;
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// tên sản phẩm tiếng việt
								lix.setProduct_name(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// tên sản phẩm tiếng anh
								lix.setEn_name(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// tên khai báo hải quan
								lix.setCustoms_name(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								if (cellvalue != null) {
									ProductGroupReqInfo pg = new ProductGroupReqInfo();
									productGroupService.selectByCode(cellvalue, pg);
									lix.setProduct_group(pg.getProduct_Group());
								}
							} catch (Exception e) {
							}
							break;
						case 5:
							try {
								if (cellvalue != null && !"".equals(cellvalue)) {
									ProductTypeReqInfo pt = new ProductTypeReqInfo();
									productTypeService.selectByCode(cellvalue, pt);
									lix.setProduct_type(pt.getProduct_type());
								}
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// đơn vị tính
								lix.setUnit(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								// hệ số quy đổi
								cellvalue = cellvalue.replace(",", ".");
								lix.setFactor(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 9:
							try {
								// sản phẩm com
								if (cellvalue != null && !"".equals(cellvalue)) {
									ProductComReqInfo com = new ProductComReqInfo();
									productComService.selectByCode(cellvalue, com);
									lix.setProduct_com(com.getProduct_com());
								}
							} catch (Exception e) {
							}
							break;
						case 10:
							try {
								// qui cách đóng gói
								cellvalue = cellvalue.replace(",", ".");
								lix.setSpecification(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 11:
							try {
								// đơn vị bao bì
								lix.setPacking_unit(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 12:
							try {
								// trọng lượng thùng
								cellvalue = cellvalue.replace(",", ".");
								lix.setTare(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 13:
							try {
								// nhóm sản phẩm khuyến mãi
								if (cellvalue != null && !"".equals(cellvalue)) {
									PromotionProductGroupReqInfo km = new PromotionProductGroupReqInfo();
									promotionProductGroupService.selectByCode(cellvalue, km);
									lix.setPromotion_product_group(km.getPromotion_product_group());
								}
							} catch (Exception e) {
							}
							break;
						case 14:
							try {
								// số lượng dự trữ
								cellvalue = cellvalue.replace(",", ".");
								lix.setReserve_quantity(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 15:
							try {
								// mã sản phẩm lever
								lix.setLever_code(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 16:
							try {
								// không còn sử dụng
								lix.setDisable(Boolean.parseBoolean(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 17:
							try {
								// xuất khẩu
								lix.setTypep(Boolean.parseBoolean(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 18:
							try {
								// thông tin ghi trên packaing list
								lix.setProduct_info(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 19:
							try {
								// sản phẩm khuyến mãi đính kèm trực tiếp trên
								// sản phẩm.
								ProductReqInfo prop = new ProductReqInfo();
								productService.selectByCode(cellvalue, prop);
								lix.setPromotion_product(prop.getProduct());
							} catch (Exception e) {
							}
							break;
						case 20:
							try {
								// số lượng thùng trên pallet
								cellvalue = cellvalue.replace(",", ".");
								lix.setBox_quantity(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 21:
							try {
								// ma san pham chinh
								lix.setMaspchinh(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 22:
							try {
								// ma san pham cu
								lix.setMaspcu(cellvalue);
							} catch (Exception e) {
							}
							break;
						case 24:
							try {
								// ten san pham _com
								lix.setTensp_com(cellvalue);
							} catch (Exception e) {
							}
							break;
						}
					}
					listProductTemp.add(lix);
				}
				workBook = null;// free
				for (Product it : listProductTemp) {
					ProductReqInfo t = new ProductReqInfo();
					productService.selectByCode(it.getProduct_code(), t);
					Product p = t.getProduct();
					t.setProduct(it);
					if (p != null) {
						it.setId(p.getId());
						it.setLast_modifed_date(new Date());
						it.setCreated_by(account.getMember().getName());
						productService.update(t);
					} else {
						it.setCreated_date(new Date());
						it.setCreated_by(account.getMember().getName());
						productService.insert(t);
					}
				}
				notify.success();

			}
		} catch (Exception e) {
			logger.error("DataSettingBean.loadExcel:" + e.getMessage(), e);
		}
	}

	@Inject
	IVoucherPaymentService voucherPaymentService;

	public void napuynhiemchi(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<VoucherPayment> listProductTemp = new ArrayList<VoucherPayment>();
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
					VoucherPayment voucherPayment = new VoucherPayment();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// so chung tu
								if (cellvalue != null && !"".equals(cellvalue)) {
									voucherPayment.setVoucher_code(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								// ngay
								if (cellvalue != null && !"".equals(cellvalue)) {
									Date paymentDate = ToolTimeCustomer.convertStringToDateFox(cellvalue, "dd/MM/yyyy");
									if (paymentDate != null) {
										voucherPayment.setPayment_date(paymentDate);
									}
								}
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// ma kh tra tien
								if (cellvalue != null && !"".equals(cellvalue)) {
									CustomerReqInfo cus = new CustomerReqInfo();
									customerService.selectByCode(cellvalue, cus);
									if (cus.getCustomer() != null) {
										voucherPayment.setPayment_customer(cus.getCustomer());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// ma kh nhan tien
								if (cellvalue != null && !"".equals(cellvalue)) {
									CustomerReqInfo cus = new CustomerReqInfo();
									customerService.selectByCode(cellvalue, cus);
									if (cus.getCustomer() != null) {
										voucherPayment.setReceiver_customer(cus.getCustomer());
									}
								}
							} catch (Exception e) {
							}
							break;
						case 4:
							// so tien
							cellvalue = cellvalue.replace(",", ".");
							voucherPayment.setTotal_amount(Double.parseDouble(cellvalue));
							break;
						case 5:
							try {
								// so hop dong
								if (cellvalue != null && !"".equals(cellvalue)) {
									Contract contract = contractService.selectByVoucherOnlyId(cellvalue);
									if (contract != null) {
										voucherPayment.setContract(contract);
									}
								}
							} catch (Exception e) {
							}
							break;
						case 6:
							try {
								// đơn vị tien
								if (cellvalue != null && !"".equals(cellvalue)) {
									Currency cr = currencyService.selectByType(cellvalue);
									voucherPayment.setCurrency(cr);
								}
							} catch (Exception e) {
							}
							break;
						case 7:
							try {
								// ghi chu
								if (cellvalue != null && !"".equals(cellvalue)) {
									voucherPayment.setNote(cellvalue);
								}
							} catch (Exception e) {
							}
							break;
						}
					}
					listProductTemp.add(voucherPayment);
				}
				workBook = null;// free
				for (VoucherPayment it : listProductTemp) {
					VoucherPaymentReqInfo t = new VoucherPaymentReqInfo();
					it.setCreated_date(new Date());
					it.setCreated_by(account.getMember().getName());
					t.setVoucher_payment(it);
					voucherPaymentService.insert(t);
				}
				notify.success();

			}
		} catch (Exception e) {
			logger.error("DataSettingBean.loadExcel:" + e.getMessage(), e);
		}
	}

	public void naptonkho(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<Inventory> inventories = new ArrayList<Inventory>();
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
					Inventory lix = new Inventory();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// ma san pham
								if (cellvalue != null && !"".equals(cellvalue)) {
									Product product = productService.selectByCode(cellvalue);
									if (product != null) {
										lix.setProduct(product);
									} else {
										break lv2;
									}
								} else {
									break lv2;
								}
							} catch (Exception e) {
							}
							break;

						case 1:
							try {
								// sl ton dau
								cellvalue = cellvalue.replace(",", ".");
								lix.setOpening_balance(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								// sl nhap
								cellvalue = cellvalue.replace(",", ".");
								lix.setImport_quantity(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								// sl xuat
								cellvalue = cellvalue.replace(",", ".");
								lix.setExport_quantity(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						case 4:
							try {
								// sl ton cuoi
								cellvalue = cellvalue.replace(",", ".");
								lix.setClosing_balance(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;

						case 5:
							try {
								// thang
								cellvalue = cellvalue.replace(",", ".");
								lix.setInventory_month((int) Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;

						case 6:
							try {
								// nam
								cellvalue = cellvalue.replace(",", ".");
								lix.setInventory_year((int) Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						}
					}
					inventories.add(lix);
				}
				workBook = null;// free
				for (Inventory it : inventories) {
					Inventory ivOld = inventoryService.selectByIdProduct(it.getProduct().getId(),
							it.getInventory_month(), it.getInventory_year());
					if (ivOld == null) {
						inventoryService.create(it);
					} else {
						it.setId(ivOld.getId());
						inventoryService.update(it);
					}
				}
				notify.success();

			}
		} catch (Exception e) {
			logger.error("DataSettingBean.loadExcel:" + e.getMessage(), e);
		}
	}

	public void napcongno(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<InvenCongNo> invenCongNos = new ArrayList<InvenCongNo>();
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
					InvenCongNo invCongNo = new InvenCongNo();
					lv2: while (cells.hasNext()) {
						Cell cell = cells.next();
						String cellvalue = Objects.toString(MyUtilExcel.getCellValue(cell), null);
						cellvalue = MyConvertFontFactory.convert(FontType.VNI_WINDOWS, FontType.UNICODE, cellvalue);
						int columnIndex = cell.getColumnIndex();
						switch (columnIndex) {
						case 0:
							try {
								// makh
								if (cellvalue != null && !"".equals(cellvalue)) {
									Customer cus = customerService.selectByCode(cellvalue);
									invCongNo.setCustomer(cus);
								}
							} catch (Exception e) {
							}
							break;

						case 1:
							// so tien
							cellvalue = cellvalue.replace(",", ".");
							invCongNo.setInvenFinal(Double.parseDouble(cellvalue));
							break;
						case 2:// thang
							invCongNo.setMonth((int) Double.parseDouble(cellvalue));
							break;
						case 3:
							// nam
							invCongNo.setYear((int) Double.parseDouble(cellvalue));
							break;

						}
						invenCongNos.add(invCongNo);
					}
				}
				workBook = null;// free
				for (InvenCongNo it : invenCongNos) {
					it.setCreated_date(new Date());
					it.setCreated_by(account.getMember().getName());
					invenCongNoService.save(it);
				}
				notify.success();
			}
		} catch (Exception e) {
			logger.error("DataSettingBean.napCongnp:" + e.getMessage(), e);
		}
	}

	@Inject
	IInvenCongNoService invenCongNoService;
}
