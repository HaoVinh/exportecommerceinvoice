package lixco.com.hddt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lixco.com.commom_ejb.MyMath;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.common.einvoice.JsonParserUtil2;
import lixco.com.einvoice_data.CustomData;
import lixco.com.einvoice_data.EInvoiceDetailV3;
import lixco.com.einvoice_data.EInvoiceV3;
import lixco.com.einvoice_entity.ConfigEInvoice;
import lixco.com.einvoice_entity.EInvoiceData;
import lixco.com.einvoice_service.EInvoiceService;
import lixco.com.entity.Customer;
import lixco.com.entity.IECategories;
import lixco.com.entity.IEInvoice;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.PaymentMethod;
import lixco.com.entity.Product;
import lixco.com.interfaces.IIEInvoiceService;
import lixco.com.interfaces.IInvoiceDetailService;
import lixco.com.interfaces.IInvoiceService;
import lixco.com.interfaces.IProductService;
import lixco.com.reqInfo.InvoiceDetailReqInfo;

public class InvoiceToJson {
	private final static long EINVOICE_TYPE = 1;
	private final static long EINVOICE_TYPE_XK = 2;

	public static String hinhthucttkhongcaidat[] = { "A", "P", "5", "&", "M", "+" };
	public static String maxuatnhapxuatkhau[] = { "T", "8" };
	public static String makhuyenmai[] = { "A", "P", "5", "&", "M", "+", "(" };
	public static String magomsanpham[] = { "A", "P", "5", "&", "+", "(" };
	public static String macaidatsotienduong[] = { "X", "-", "$" };
	public static String mabatbuoccodongiathanhtien[] = { "-", "$", "%", "&", "(", ")", "^", "+", "1", "5", "8", "A",
			"E", "P", "Q", "T", "V", "X", "Y" };

	public static ThongBao toJsonHoaDon(Invoice invoiceLoad, EInvoiceService eInvoiceService,
			IInvoiceService invoiceService, IInvoiceDetailService invoiceDetailService,
			IIEInvoiceService ieInvoiceService, boolean lamtron, String chinhanhapdung, String userAction,
			IProductService productService) {
		ThongBao thongbao = new ThongBao();
		try {
			// Lấy config và data Invoice.
			ConfigEInvoice configEInvoice = eInvoiceService.getConfigEInvoice();
			EInvoiceData eInvoiceData = eInvoiceService.selectEInvoiceDataById(EINVOICE_TYPE);
			if (configEInvoice == null || eInvoiceData == null) {
				thongbao.setLoi(true);
				thongbao.setThongtinloi("Không lấy được thông tin cấu hình.");
				return thongbao;
			}
			if (invoiceLoad != null) {
				boolean macdsotienduong = false;
				List<InvoiceDetail> listDetail = new ArrayList<>();
				invoiceDetailService.selectByInvoice(invoiceLoad.getId(), listDetail);
				if (listDetail == null || listDetail.size() == 0) {
					thongbao.setLoi(true);
					thongbao.setThongtinloi("Không có chi tiết hóa đơn.");
					return thongbao;
				}
				Customer customer = invoiceLoad.getCustomer();
				List<EInvoiceV3> v3s = new ArrayList<EInvoiceV3>();
				EInvoiceV3 v3 = new EInvoiceV3();

				String refID = invoiceLoad.getRefId();
				if (refID != null && !"".equals(refID)) {
					int editVersion = invoiceLoad.getEditVersion() + 1;
					// Mã đánh dấu phiên bản của hóa đơn (Dùng khi update, cộng
					// thêm 1 mỗi lần update) 0
					v3.setEditVersion(editVersion);
					invoiceLoad.setEditVersion(editVersion);
					invoiceService.updateEditVersion(invoiceLoad.getId(), editVersion, userAction);
				} else {
					refID = UUID.randomUUID().toString();
					invoiceLoad.setRefId(refID);
					invoiceService.updateRefId(invoiceLoad.getId(), refID, userAction);
				}

				// PK ID hóa đơn (not null)
				v3.setRefID(refID);
				v3.setCompanyID(eInvoiceData.getCompany_id());
				// Mã số thuế công ty (Not null) 0101243150-675
				v3.setCompanyTaxCode(configEInvoice.getTax_code());
				v3.setInvoiceTemplateID(eInvoiceData.getInvoiceTemplateID());
				v3.setOrganizationUnitID(eInvoiceData.getOrganizationUnitID());
				v3.setSourceType(0);
				v3.setInvTemplateNoSeries(eInvoiceData.getInv_template_no() + "-" + eInvoiceData.getInv_series());
				v3.setIsInheritFromOldTemplate(true);
				v3.setInvoiceType(eInvoiceData.getInvoiceType());
				v3.setInvTemplateNo(eInvoiceData.getInv_template_no());
				v3.setInvSeries(eInvoiceData.getInv_series());
				v3.setPaymentStatus(0);

				v3.setAccountObjectID("");
				v3.setAccountObjectCode(customer.getCustomer_code());
				v3.setAccountObjectName(customer.getCompany_name());
				if (customer.getCompany_name() == null)
					v3.setAccountObjectName("");
				if (customer.isNot_print_customer_name()) {
					v3.setContactName("");
				} else {
					v3.setContactName(customer.getCustomer_name());
				}
				/*
				 * @thêm ngày 21-3-2023, set trường người mua hàng
				 */
				String nguoiMuaHang = customer.getNguoi_mua_hang();
				if (nguoiMuaHang != null && !"".equals(nguoiMuaHang)) {
					v3.setContactName(nguoiMuaHang);
				}
				v3.setAccountObjectAddress(customer.getAddress());
				v3.setAccountObjectTaxCode(customer.getTax_code());
				v3.setAccountObjectBankAccount(customer.getBank_account_no());
				v3.setAccountObjectBankName(customer.getBank_info());
				if (customer.getCitizenIDNumber() != null) {
					v3.setCitizenIDNumber(customer.getCitizenIDNumber());
				}

				PaymentMethod method = invoiceLoad.getPayment_method();
				IECategories iec = invoiceLoad.getIe_categories();
				if (iec == null)
					iec = new IECategories();
				v3.setPaymentMethod(method == null ? "" : method.getMethod_name());
				for (int i = 0; i < hinhthucttkhongcaidat.length; i++) {
					if (hinhthucttkhongcaidat[i].equals(iec.getCode())) {
						v3.setPaymentMethod("");
						break;
					}
				}
				v3.setPaymentRule(30);

				v3.setInvNo("<Chưa cấp số>");
				v3.setInvDate(ToolTimeCustomer.getFirstDateOfDay(invoiceLoad.getInvoice_date()));

				v3.setExchangeRateOperation(0);
				v3.setCurrencyCode("VND");
				v3.setCurrencyID("VND");

				boolean hdxuatkhau = false;
				for (int i = 0; i < maxuatnhapxuatkhau.length; i++) {
					if (maxuatnhapxuatkhau[i].equals(iec.getCode())) {
						hdxuatkhau = true;
						break;
					}
				}
				if (hdxuatkhau) {
					v3.setCurrencyCode(invoiceLoad.getDonvitien());
					v3.setCurrencyID(invoiceLoad.getDonvitien());
				}

				boolean hdkhuyenmai = false;
				for (int i = 0; i < makhuyenmai.length; i++) {
					if (makhuyenmai[i].equals(iec.getCode())) {
						hdkhuyenmai = true;
						break;
					}
				}

				if (hdkhuyenmai && invoiceLoad.getTax_value() == 0) {
					v3.setTotalSaleAmountOC(0);
					v3.setTotalSaleAmount(0);
					v3.setTotalDiscountAmountOC(0);
					v3.setTotalDiscountAmount(0);
					v3.setTotalAmountOC(0);
					v3.setTotalAmount(0);
					v3.setTotalSaleAmountOther(0);
					v3.setTotalVATAmountOther(0);
					v3.setTotalAmountWithVATOC(0);
					v3.setTotalAmountWithoutVAT(0);
					v3.setTotalAmountWithVAT(0);
					v3.setVATRateOther("0");
				} else {
					if (hdxuatkhau) {
						double tongtiennt = listDetail.stream().mapToDouble(f -> f.getTotal_foreign_amount()).sum();
						if (lamtron) {
							tongtiennt = MyMath.round(tongtiennt);
						} else {
							tongtiennt = MyMath.roundCustom(tongtiennt, 2);
						}
						double tienthuent = 0;
						if (lamtron) {
							tienthuent = MyMath.round(tongtiennt * invoiceLoad.getTax_value());
						} else {
							tienthuent = MyMath.roundCustom(tongtiennt * invoiceLoad.getTax_value(), 2);
						}
						v3.setTotalSaleAmountOC(tongtiennt);
						v3.setTotalVATAmountOC(tienthuent);
						v3.setTotalAmountOC(tongtiennt + tienthuent);
						v3.setTotalAmountWithVATOC(tongtiennt + tienthuent);

					} else {
						for (int i = 0; i < macaidatsotienduong.length; i++) {
							if (macaidatsotienduong[i].equals(iec.getCode())) {
								macdsotienduong = true;
								break;
							}
						}

						double tongtiennt = invoiceLoad.getTongtien();
						if (macdsotienduong)
							tongtiennt = Math.abs(invoiceLoad.getTongtien());
						if (lamtron) {
							tongtiennt = MyMath.round(tongtiennt);
						}
						double tienthuent = invoiceLoad.getThue();
						if (macdsotienduong)
							tienthuent = Math.abs(tienthuent);
						if (lamtron) {
							tienthuent = MyMath.round(tienthuent);
						}
						v3.setTotalSaleAmountOC(tongtiennt);
						v3.setTotalVATAmountOC(tienthuent);
						v3.setTotalAmountOC(tongtiennt + tienthuent);
						v3.setTotalAmountWithVATOC(tongtiennt + tienthuent);

					}
					double tongtien = invoiceLoad.getTongtien();
					if (macdsotienduong)
						tongtien = Math.abs(tongtien);
					if (lamtron) {
						tongtien = MyMath.round(tongtien);
					}
					double thue = invoiceLoad.getThue();
					if (lamtron) {
						thue = MyMath.round(thue);
					}
					if (macdsotienduong)
						thue = Math.abs(thue);
					v3.setTotalDiscountAmountOC(0);
					v3.setTotalSaleAmount(tongtien);
					v3.setTotalDiscountAmount(0);
					v3.setTotalVATAmount(thue);

					v3.setVATRate(invoiceLoad.getTax_value() * 100);
					v3.setTotalAmount(tongtien + thue);
					v3.setTotalSaleAmountOther(0);
					v3.setTotalVATAmountOther(0);
					v3.setTotalAmountWithoutVAT(tongtien);
					v3.setTotalAmountWithVAT(tongtien + thue);
					v3.setVATRateOther(null);
				}
				v3.setPublishStatus(0);
				v3.setIsInvoiceDeleted(false);
				v3.setSendToTaxStatus(0);
				v3.setInvoiceCode(null);
				v3.setMessageCode(null);
				v3.setMessageRefNo(null);
				v3.setMessageRefDate(null);
				v3.setErrorInvoiceStatus(0);
				v3.setIsTaxReduction43(false);
				if (invoiceLoad.getTax_value() == 0.08) {
					v3.setIsTaxReduction43(true);
				}
				v3.setSendInvoiceStatus(0);
				v3.setCreatedBy("");
				v3.setModifiedBy("");
				if (customer != null && customer.getCompany_name() != null
						&& !"".equals(customer.getCompany_name().trim())) {
					v3.setReceiverName(customer.getCompany_name());
				} else {
					v3.setReceiverName(customer.getCustomer_name());
				}
				try {
					// Email người nhận (maxlength = 255)
					String[] email = customer.getEmail().split(";");
					if (email.length != 0) {
						v3.setReceiverEmail(email[0]);
						if (email.length > 1)
							v3.setReceiverEmailCC(invoiceLoad.getCustomer().getEmail().replace(email[0] + ";", ""));
					}
				} catch (Exception e) {
				}
				// "EInvoiceStatus"
				// 1 - là hóa đơn gốc
				// 2 - hóa đơn hủy
				// 3 - hóa đơn thay thế
				// 4 - hóa đơn điều chỉnh
				boolean hoadondieuchinh = false;
				if (invoiceLoad.getIdhoadongoc() != null && !"".equals(invoiceLoad.getIdhoadongoc().trim())) {
					Invoice invoiceLoadTT = invoiceService.findByIdSafe(Long.parseLong(invoiceLoad.getIdhoadongoc()
							.trim()));
					Customer cus = invoiceLoadTT.getCustomer();
					if (cus != null && "CH103".equals(cus.getCustomer_code())) {
						// MAKH huy
						v3.setEInvoiceStatus(3);
					} else {
						v3.setEInvoiceStatus(4);
						hoadondieuchinh = true;
					}
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					v3.setOrgOrganizationUnitID(eInvoiceData.getOrganizationUnitID());
					v3.setOrgRefID(invoiceLoadTT.getRefId());
					v3.setOrgInvNo(invoiceLoadTT.getVoucher_code());
					v3.setOrgInvDate(sf.format(invoiceLoadTT.getInvoice_date()) + "T07:00:00+07:00");
					v3.setOrgTransactionID(invoiceLoadTT.getLookup_code());
					v3.setOrgInvTemplateNo(eInvoiceData.getInv_template_no());
					String series = eInvoiceData.getInv_series().substring(1, eInvoiceData.getInv_series().length());
					String newseriesyear_New = ((invoiceLoad.getInvoice_date().getYear() + 1900) + "").substring(2, 4);
					String newseriesyear_Old = ((invoiceLoadTT.getInvoice_date().getYear() + 1900) + "")
							.substring(2, 4);

					v3.setOrgInvSeries(series.replace(newseriesyear_New, newseriesyear_Old));

				} else {
					v3.setEInvoiceStatus(1);
				}
				v3.setPaymentExpirationDate(invoiceLoad.getInvoice_date());
				v3.setReceiverMobile("");
				v3.setIsInvoiceReceipted(false);
				v3.setTypeChangeInvoice(0);
				v3.setIsInvoiceDiscount(false);
				v3.setTypeDiscount(0);
				v3.setBusinessArea(0);
				v3.setContractCode(null);
				v3.setContractDate(null);
				v3.setContractOwner(null);
				v3.setJournalMemo(null);
				v3.setTransporterName(null);
				v3.setTransport(null);
				v3.setListFromStockName(null);
				v3.setListToStockName(null);
				v3.setTransportContractCode(null);
				v3.setDeliveryType(null);
				v3.setStockOutFullName(null);
				v3.setStockInFullName(null);
				v3.setReceiptCode(null);
				v3.setReceiptName(null);
				v3.setEntityState(1);

				StringBuffer ghichu = new StringBuffer();
				double tygia = 1;
				if (hdxuatkhau) {
					IEInvoice ieInvoice = ieInvoiceService.findByIdSafe(invoiceLoad.getIdhoadonxk());
					if (ieInvoice != null) {
						tygia = ieInvoice.getExchange_rate();
						ghichu.append("Số hóa đơn thương mại: ").append(ieInvoice.getVoucher_code());
						if (ieInvoice.getDeclaration_code() != null && !"".equals(ieInvoice.getDeclaration_code())) {
							ghichu.append(". Số tờ khai: ").append(ieInvoice.getDeclaration_code()).append(".");
						} else {
							ghichu.append(".");
						}
						invoiceLoad.setNote(invoiceLoad.getNote() + " " + ghichu.toString());
					} else {
						thongbao.setLoi(true);
						thongbao.setThongtinloi("Không tìm thấy số hóa đơn thương mại hoặc đơn vị tiền.");
						return thongbao;
					}
				}
				StringBuffer ghichubigC = new StringBuffer();
				if (chinhanhapdung.equals("BAC NINH") && "BL151".equals(customer.getCustomer_code())) {
					ghichubigC.append("Giao hàng tại ").append(customer.getCustomer_name());
				}
				try {
					if (ghichu.toString().isEmpty() && ("CL027".equals(customer.getCustomer_code()))
							|| "VM".equals(customer.getCustomer_types().getCode())) {
						ghichu.append(". ").append(ghichubigC);
					} else {
						ghichu.append(ghichubigC);
					}
					invoiceLoad.setNote(invoiceLoad.getNote() + " " + ghichu.toString());
				} catch (Exception e) {
				}

				if (invoiceLoad.getOrder_voucher() != null || !"".equals(invoiceLoad.getOrder_voucher())
						|| (invoiceLoad.getPo_no() != null || !"".equals(invoiceLoad.getPo_no()))
						|| invoiceLoad.getNote() != null || !"".equals(invoiceLoad.getNote())) {
					CustomData customData = new CustomData();
					if (invoiceLoad.getOrder_voucher() != null && !"".equals(invoiceLoad.getOrder_voucher())) {
						customData.setCustomField1(invoiceLoad.getOrder_voucher());
					}
					if (invoiceLoad.getPo_no() != null && !"".equals(invoiceLoad.getPo_no())) {
						customData.setCustomField2(invoiceLoad.getPo_no());
					}
					if (invoiceLoad.getNote() != null && !"".equals(invoiceLoad.getNote().toString())) {
						customData.setCustomField6(invoiceLoad.getNote());
					}
					// Cập nhật ngày 24/07/2023 chị Hà (đối với các phiếu khuyến
					// mãi và hệ số thuế =0 => không truyen ghi chu qua misa)
					if (hdkhuyenmai && invoiceLoad.getTax_value() == 0) {
						customData.setCustomField6("");
					}
					v3.setCustomData(JsonParserUtil2.getGson().toJson(customData));
				}
				v3.setApproveStep(-3);
				v3.setIsImport(false);
				v3.setCreatedDate(invoiceLoad.getInvoice_date());
				v3.setCreatedBy(null);
				v3.setEditVersion(0);
				v3.setExchangeRate(tygia);
				/*
				 * Hóa đơn khi có số lượng buộc phải có đơn giá, thành tiền Cập
				 * nhật ngày 17/10/2023
				 */
				boolean buoccodongiathanhtien = false;
				for (int i = 0; i < mabatbuoccodongiathanhtien.length; i++) {
					if (mabatbuoccodongiathanhtien[i].equals(iec.getCode())) {
						buoccodongiathanhtien = true;
						break;
					}
				}

				// Neu hoa don khuyen mai gom san pham lai
				boolean hdgomsanpham = false;
				for (int i = 0; i < magomsanpham.length; i++) {
					if (magomsanpham[i].equals(iec.getCode())) {
						hdgomsanpham = true;
						break;
					}
				}
				List<InvoiceDetail> invoiceDetails = new ArrayList<InvoiceDetail>(listDetail);
				if (hdgomsanpham) {
					invoiceDetails.clear();
					Map<Product, List<InvoiceDetail>> datagroups1 = listDetail.stream().collect(
							Collectors.groupingBy(p -> p.getProduct(), Collectors.toList()));
					for (Product key : datagroups1.keySet()) {
						double soluong = 0;
						double tongtien = 0;
						List<InvoiceDetail> invs = datagroups1.get(key);
						for (int i = 0; i < invs.size(); i++) {
							soluong += invs.get(i).getQuantity();
							tongtien += invs.get(i).getTotal();
						}
						InvoiceDetail ivd = new InvoiceDetail();
						ivd.setProduct(invs.get(0).getProduct());
						ivd.setQuantity(soluong);
						ivd.setUnit_price(invs.get(0).getUnit_price());
						ivd.setTotal(MyMath.round(tongtien));
						invoiceDetails.add(ivd);
					}
				}

				// danh sách chi tiết hóa đơn điện tửe
				List<EInvoiceDetailV3> listEInvoiceDetailV3 = new ArrayList<>();
				int stt = 1;
				for (InvoiceDetail p : invoiceDetails) {
					/*
					 * Cập nhật bổ sung 17/10/2023 Nếu hóa đơn điều chỉnh, hóa
					 * đơn chiết khấu (dòng chiết khấu), hóa đơn có mã xuất nhập
					 * Y và mã số thuế KH khác nội bộ (0301444263*) thì không
					 * kiểm tra Hóa đơn chiết khấu số lượng đang là 0 nên không
					 * kiểm tra.
					 */
					if (buoccodongiathanhtien) {
						if (hoadondieuchinh
								|| ("Y".equals(iec.getCode()) && v3.getAccountObjectTaxCode() != null && !v3
										.getAccountObjectTaxCode().contains("0301444263"))) {
							// khong kiem tra don gia thanh tien
						} else {
							if (p.getQuantity() != 0) {
								if (p.getUnit_price() == 0 || p.getTotal() == 0) {
									// Hóa đơn không được chuyển
									thongbao.setLoi(true);
									thongbao.setThongtinloi("Vui lòng kiểm tra lại dữ liệu phiếu "
											+ invoiceLoad.getVoucher_code() + ": Mã sản phẩm ("
											+ p.getProduct().getProduct_code() + ") không có đơn giá hoặc thành tiền.");
									return thongbao;
								}
								if (hdxuatkhau == false) {
									if (Math.round(p.getUnit_price() * p.getQuantity()) != p.getTotal()) {
										// Hóa đơn không được chuyển
										thongbao.setLoi(true);
										thongbao.setThongtinloi("Vui lòng kiểm tra lại dữ liệu phiếu "
												+ invoiceLoad.getVoucher_code() + ": Mã sản phẩm ("
												+ p.getProduct().getProduct_code()
												+ ") số lượng X đơn giá != thành tiền.");
										return thongbao;
									}
								}
							}
						}
					}

					EInvoiceDetailV3 itemv3 = new EInvoiceDetailV3();
					InvoiceDetail invoiceDetailLoad = p;
					if (p.getId() != 0) {
						// load lại chi tiết phiếu nhập
						InvoiceDetailReqInfo invoiceDetailReqInfo = new InvoiceDetailReqInfo();
						invoiceDetailService.selectById(p.getId(), invoiceDetailReqInfo);
						invoiceDetailLoad = invoiceDetailReqInfo.getInvoice_detail();
						if (invoiceDetailLoad.getRefDetailID() != null) {
							itemv3.setRefDetailID(invoiceDetailLoad.getRefDetailID());
						} else {
							String refDetailID = UUID.randomUUID().toString();
							itemv3.setRefDetailID(refDetailID);
							invoiceDetailLoad.setRefDetailID(refDetailID);
							invoiceDetailLoad.setRefID(UUID.randomUUID().toString());
							invoiceDetailService.update(invoiceDetailLoad, userAction);
						}
					}
					itemv3.setRefID(refID);
					itemv3.setInventoryItemID(invoiceDetailLoad.getProduct().getProduct_code());
					itemv3.setInventoryItemCode(invoiceDetailLoad.getProduct().getProduct_code());
					itemv3.setInventoryItemName(invoiceDetailLoad.getProduct().getProduct_name());

					itemv3.setDescription(invoiceDetailLoad.getProduct().getProduct_name());
					itemv3.setUnitName(invoiceDetailLoad.getProduct().getUnit());
					itemv3.setQuantity(invoiceDetailLoad.getQuantity());
					itemv3.setInWards(0);
					itemv3.setIsPromotion(false);
					itemv3.setInventoryItemType(0);
					itemv3.setAmountOCBefore(p.getTotal());

					/**
					 * Ngay 31/03/2023 NHK dieu chinh lai cho phep the hien so
					 * (-) tren hoa don
					 */
					if (hdkhuyenmai && invoiceLoad.getTax_value() == 0) {
						// itemv3.setUnitPrice(invoiceDetailLoad.getUnit_price());
						itemv3.setUnitPrice(0);
						itemv3.setAmountOC(0);
						itemv3.setAmountOCBefore(0);
						itemv3.setAmount(0);
						itemv3.setVATAmountOC(0);
						itemv3.setVATAmount(0);
						itemv3.setVATRate(0);
						/*
						 * Cập nhat them trang thai khuyen mai (ngay 20/07/2023)
						 */
						itemv3.setIsPromotion(true);
						itemv3.setInventoryItemType(2);
					} else {
						itemv3.setAmount(invoiceDetailLoad.getTotal());
						if (hdxuatkhau) {
							double tongtiennt = invoiceDetailLoad.getTotal_foreign_amount();
							if (lamtron) {
								tongtiennt = MyMath.round(tongtiennt);
							}
							double VATAmountOC = MyMath.roundCustom(tongtiennt * invoiceLoad.getTax_value(), 2);
							if (lamtron) {
								VATAmountOC = MyMath.round(VATAmountOC);
							}
							itemv3.setAmountOC(MyMath.roundCustom(tongtiennt, 2));
							itemv3.setAmountOCBefore(itemv3.getAmountOC());
							itemv3.setUnitPrice(MyMath.roundCustom(invoiceDetailLoad.getForeign_unit_price(), 5));
							itemv3.setVATAmountOC(VATAmountOC);
						} else if (macdsotienduong) {
							double tongtiennt = Math.abs(invoiceDetailLoad.getTotal_foreign_amount());
							if (lamtron) {
								tongtiennt = MyMath.round(tongtiennt);
							}
							itemv3.setAmount(Math.abs(invoiceDetailLoad.getTotal()));
							itemv3.setAmountOC(itemv3.getAmount());
							itemv3.setAmountOCBefore(invoiceDetailLoad.getTotal());
							itemv3.setUnitPrice(Math.abs(MyMath.roundCustom(invoiceDetailLoad.getUnit_price(), 2)));
							itemv3.setVATAmountOC(MyMath.round(invoiceDetailLoad.getTotal()
									* invoiceLoad.getTax_value()));
						} else {
							itemv3.setAmountOC(itemv3.getAmount());
							itemv3.setAmountOCBefore(itemv3.getAmount());
							itemv3.setUnitPrice(MyMath.roundCustom(invoiceDetailLoad.getUnit_price(), 2));
							itemv3.setVATAmountOC(MyMath.round(invoiceDetailLoad.getTotal()
									* invoiceLoad.getTax_value()));
						}

						itemv3.setVATAmount(MyMath.round(invoiceDetailLoad.getTotal() * invoiceLoad.getTax_value()));
						itemv3.setVATRate(MyMath.round(invoiceLoad.getTax_value() * 100));
					}
					itemv3.setDiscountRate(0);
					itemv3.setDiscountAmountOC(0);
					itemv3.setDiscountAmount(0);
					itemv3.setSortOrder(stt);
					itemv3.setIsTemp(false);
					itemv3.setCompanyID(0);
					itemv3.setUnitAfterTax(0);
					itemv3.setAmountAfterTax(0);
					itemv3.setIsDescription(null);
					itemv3.setOutWards(0);
					itemv3.setInventoryItemNote(null);
					itemv3.setSortOrderView(stt);
					itemv3.setEntityState(1);
					itemv3.setInventoryItemNote(null);
					/*
					 * Cập nhat them trang thai chiet khau (ngay 13/07/2023)
					 * CKN: CP CHIẾT KHẤU
					 */
					Product product = productService.findIdGetPCom(invoiceDetailLoad.getProduct().getId());
					if (product != null && product.getProduct_com() != null
							&& "CKN".equals(product.getProduct_com().getPcom_code())) {
						itemv3.setInventoryItemType(4);
						itemv3.setAmount(Math.abs(itemv3.getAmount()));
						itemv3.setAmountOC(Math.abs(itemv3.getAmountOC()));
						itemv3.setAmountOCBefore(invoiceDetailLoad.getTotal());
						itemv3.setVATAmount(Math.abs(itemv3.getVATAmount()));
						itemv3.setVATAmountOC(Math.abs(itemv3.getVATAmountOC()));
					}
					listEInvoiceDetailV3.add(itemv3);
					stt++;
				}

				// kiem tra de thong bao tien khong khop nhau
				double tienchitiet = listEInvoiceDetailV3.stream().mapToDouble(f -> f.getAmountOCBefore()).sum();
				double tienhang = v3.getTotalSaleAmountOC();
				if (Math.abs(Math.abs(tienchitiet) - Math.abs(tienhang)) >= 1) {
					thongbao.setLoi(true);
					thongbao.setThongtinloi("Vui lòng kiểm tra lại dữ liệu phiếu " + invoiceLoad.getVoucher_code()
							+ ": tiền tổng chi tiết(" + MyMath.roundCustom(tienchitiet, 2) + "); tiền trên phiếu ("
							+ MyMath.roundCustom(tienhang, 2) + ")");
					return thongbao;
				} else {
					v3.setInvoiceDetails(listEInvoiceDetailV3);
					v3s.add(v3);
					String jsonInputString = JsonParserUtil2.getGson().toJson(v3s);
					if (invoiceLoad.getTax_value() == 0 && hdkhuyenmai) {
						jsonInputString = jsonInputString.replace("\"VATRate\":0.0,", "");
						jsonInputString = jsonInputString.replace("\"VATRate\":0,", "");
					}
					thongbao.setLoi(false);
					thongbao.setDulieu(jsonInputString);
					return thongbao;
				}
			}
		} catch (Exception e) {
			thongbao.setLoi(true);
			thongbao.setThongtinloi(e.getMessage());
			return thongbao;
		}
		return thongbao;
	}

	public static ThongBao toJsonXuatKho(Invoice invoiceLoad, EInvoiceService eInvoiceService,
			IInvoiceService invoiceService, IInvoiceDetailService invoiceDetailService,
			IIEInvoiceService ieInvoiceService, boolean lamtron, String userAction) {
		ThongBao thongbao = new ThongBao();
		try {
			// Lấy config và data Invocie.
			ConfigEInvoice configEInvoice = eInvoiceService.getConfigEInvoice();
			EInvoiceData eInvoiceData = eInvoiceService.selectEInvoiceDataById(EINVOICE_TYPE_XK);
			if (configEInvoice == null || eInvoiceData == null) {
				thongbao.setLoi(true);
				thongbao.setThongtinloi("Không lấy được thông tin cấu hình.");
				return thongbao;
			}
			if (invoiceLoad != null) {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				List<InvoiceDetail> listDetail = new ArrayList<>();
				invoiceDetailService.selectByInvoice(invoiceLoad.getId(), listDetail);
				if (listDetail == null || listDetail.size() == 0) {
					thongbao.setLoi(true);
					thongbao.setThongtinloi("Không có chi tiết hóa đơn.");
					return thongbao;
				}
				Customer customer = invoiceLoad.getCustomer();
				List<EInvoiceV3> v3s = new ArrayList<EInvoiceV3>();
				EInvoiceV3 v3 = new EInvoiceV3();

				String refID = invoiceLoad.getRefId();
				if (refID != null && !"".equals(refID)) {
					int editVersion = invoiceLoad.getEditVersion() + 1;
					// Mã đánh dấu phiên bản của hóa đơn (Dùng khi update, cộng
					// thêm 1 mỗi lần update) 0
					v3.setEditVersion(editVersion);
					invoiceLoad.setEditVersion(editVersion);
					invoiceService.updateEditVersion(invoiceLoad.getId(), editVersion, userAction);
				} else {
					refID = UUID.randomUUID().toString();
					invoiceLoad.setRefId(refID);
					invoiceService.updateRefId(invoiceLoad.getId(), refID, userAction);
				}
				// PK ID hóa đơn (not null)
				v3.setRefID(refID);
				v3.setCompanyID(eInvoiceData.getCompany_id());
				// Mã số thuế công ty (Not null) 0101243150-675
				v3.setCompanyTaxCode(configEInvoice.getTax_code());
				v3.setInvoiceTemplateID(eInvoiceData.getInvoiceTemplateID());
				v3.setOrganizationUnitID(eInvoiceData.getOrganizationUnitID());
				v3.setSourceType(0);
				v3.setInvTemplateNoSeries(eInvoiceData.getInv_template_no() + "-" + eInvoiceData.getInv_series());
				v3.setIsInheritFromOldTemplate(true);
				v3.setInvoiceType(eInvoiceData.getInvoiceType());
				v3.setInvTemplateNo(eInvoiceData.getInv_template_no());
				v3.setInvSeries(eInvoiceData.getInv_series());
				v3.setPaymentStatus(0);

				v3.setAccountObjectID("");
				v3.setAccountObjectCode(customer.getCustomer_code());
				v3.setAccountObjectName(customer.getCompany_name());
				if (customer.getCompany_name() == null)
					v3.setAccountObjectName("");
				if (customer.isNot_print_customer_name()) {
					v3.setContactName("");
				} else {
					v3.setContactName(customer.getCustomer_name());
				}
				v3.setAccountObjectAddress(customer.getAddress());
				v3.setAccountObjectTaxCode(customer.getTax_code());
				v3.setAccountObjectBankAccount(customer.getBank_account_no());
				v3.setAccountObjectBankName(customer.getBank_info());

				PaymentMethod method = invoiceLoad.getPayment_method();
				IECategories iec = invoiceLoad.getIe_categories();
				if (iec == null)
					iec = new IECategories();
				v3.setPaymentMethod(method == null ? "" : method.getMethod_name());
				v3.setPaymentRule(30);
				v3.setInvNo("<Chưa cấp số>");
				v3.setInvDate(ToolTimeCustomer.getFirstDateOfDay(invoiceLoad.getInvoice_date()));
				v3.setExchangeRate(1);
				v3.setExchangeRateOperation(0);
				v3.setCurrencyCode("VND");
				v3.setCurrencyID("VND");

				// Phieu xuat kho
				v3.setContractCode(invoiceLoad.getLdd());

				v3.setContractDate(sf.format(invoiceLoad.getInvoice_date()) + "T07:00:00+07:00");
				v3.setContractOwner(configEInvoice.getTencongty());// ten cong
																	// tu
				v3.setTransporterName(invoiceLoad.getCarrier() == null ? "" : invoiceLoad.getCarrier()
						.getCarrier_name());
				v3.setTransport(invoiceLoad.getCar() == null ? "" : invoiceLoad.getCar().getLicense_plate());
				v3.setListFromStockName(configEInvoice.getTencongty());
				v3.setListToStockName(customer == null ? "" : customer.getCompany_name());
				v3.setDeliveryType("1");// Ki?u phi?u Giá tr?: 1: phi?u n?i b?,
										// 2: phi?u d?i lý 1
				v3.setTransportContractCode(invoiceLoad.getFreight_contract() == null ? "" : invoiceLoad
						.getFreight_contract().getContract_no());
				v3.setStockOutFullName("TKX");
				v3.setStockInFullName("Người nhận hàng");

				double tongtiennt = 0;
				double tienthuent = 0;
				v3.setTotalSaleAmountOC(tongtiennt);
				v3.setTotalVATAmountOC(tienthuent);
				v3.setTotalAmountOC(tongtiennt + tienthuent);
				v3.setTotalAmountWithVATOC(tongtiennt + tienthuent);
				v3.setExchangeRate(1);

				double tongtien = 0;
				double thue = 0;

				v3.setTotalDiscountAmountOC(0);
				v3.setTotalSaleAmount(tongtien);
				v3.setTotalDiscountAmount(0);
				v3.setTotalVATAmount(thue);
				v3.setVATRate(invoiceLoad.getTax_value() * 100);
				v3.setTotalAmount(tongtien + thue);
				v3.setTotalSaleAmountOther(0);
				v3.setTotalVATAmountOther(0);
				v3.setTotalAmountWithoutVAT(tongtien);
				v3.setTotalAmountWithVAT(tongtien + thue);
				v3.setVATRateOther(null);

				v3.setPublishStatus(0);
				v3.setIsInvoiceDeleted(false);
				v3.setSendToTaxStatus(0);
				v3.setInvoiceCode(null);
				v3.setMessageCode(null);
				v3.setMessageRefNo(null);
				v3.setMessageRefDate(null);
				v3.setErrorInvoiceStatus(0);
				v3.setIsTaxReduction43(false);
				if (invoiceLoad.getTax_value() == 0.08) {
					v3.setIsTaxReduction43(true);
				}
				v3.setSendInvoiceStatus(0);
				v3.setCreatedBy("");
				v3.setModifiedBy("");
				if (customer != null && customer.getCompany_name() != null
						&& !"".equals(customer.getCompany_name().trim())) {
					v3.setReceiverName(customer.getCompany_name());
				} else {
					v3.setReceiverName(customer.getCustomer_name());
				}

				try {
					// Email người nhận (maxlength = 255)
					String[] email = customer.getEmail().split(";");
					if (email.length != 0) {
						v3.setReceiverEmail(email[0]);
						if (email.length > 1)
							v3.setReceiverEmailCC(invoiceLoad.getCustomer().getEmail().replace(email[0] + ";", ""));
					}
				} catch (Exception e) {
				}
				// "EInvoiceStatus"
				// 1 - là hóa đơn gốc
				// 2 - hóa đơn hủy
				// 3 - hóa đơn thay thế
				// 4 - hóa đơn điều chỉnh
				if (invoiceLoad.getIdhoadongoc() != null && !"".equals(invoiceLoad.getIdhoadongoc().trim())) {
					Invoice invoiceLoadTT = invoiceService.findByIdSafe(Long.parseLong(invoiceLoad.getIdhoadongoc()
							.trim()));
					Customer cus = invoiceLoadTT.getCustomer();
					if (cus != null && "CH103".equals(cus.getCustomer_code())) {// CH103:
						// MAKH
						// huy
						v3.setEInvoiceStatus(3);
					} else {
						v3.setEInvoiceStatus(4);
					}
					v3.setOrgOrganizationUnitID(eInvoiceData.getOrganizationUnitID());
					v3.setOrgRefID(invoiceLoadTT.getRefId());
					v3.setOrgInvNo(invoiceLoadTT.getVoucher_code());
					v3.setOrgInvDate(sf.format(invoiceLoadTT.getInvoice_date()) + "T07:00:00+07:00");
					v3.setOrgTransactionID(invoiceLoadTT.getLookup_code());
					v3.setOrgInvTemplateNo(eInvoiceData.getInv_template_no());
					String series = eInvoiceData.getInv_series().substring(1, eInvoiceData.getInv_series().length());
					String newseriesyear_New = ((invoiceLoad.getInvoice_date().getYear() + 1900) + "").substring(2, 4);
					String newseriesyear_Old = ((invoiceLoadTT.getInvoice_date().getYear() + 1900) + "")
							.substring(2, 4);

					v3.setOrgInvSeries(series.replace(newseriesyear_New, newseriesyear_Old));
				} else {
					v3.setEInvoiceStatus(1);
				}
				v3.setPaymentExpirationDate(invoiceLoad.getInvoice_date());
				v3.setReceiverMobile("");
				v3.setIsInvoiceReceipted(false);
				v3.setTypeChangeInvoice(0);
				v3.setIsInvoiceDiscount(false);
				v3.setTypeDiscount(0);
				v3.setBusinessArea(0);

				v3.setReceiptCode(null);
				v3.setReceiptName(null);
				v3.setEntityState(1);

				if (invoiceLoad.getOrder_voucher() != null && !"".equals(invoiceLoad.getOrder_voucher())
						|| (invoiceLoad.getPo_no() != null && !"".equals(invoiceLoad.getPo_no()))) {
					CustomData customData = new CustomData();
					if (invoiceLoad.getOrder_voucher() != null && !"".equals(invoiceLoad.getOrder_voucher())) {
						customData.setCustomField1(invoiceLoad.getOrder_voucher());
					}
					if (invoiceLoad.getPo_no() != null && !"".equals(invoiceLoad.getPo_no())) {
						customData.setCustomField2(invoiceLoad.getPo_no());
					}
					v3.setCustomData(JsonParserUtil2.getGson().toJson(customData));
				}
				v3.setApproveStep(-3);
				v3.setIsImport(false);
				v3.setCreatedDate(invoiceLoad.getInvoice_date());
				v3.setCreatedBy(null);
				v3.setEditVersion(0);

				// danh sách chi tiết hóa đơn điện tửe
				List<EInvoiceDetailV3> listEInvoiceDetailV3 = new ArrayList<>();
				int stt = 1;
				for (InvoiceDetail p : listDetail) {
					EInvoiceDetailV3 itemv3 = new EInvoiceDetailV3();
					// load lại chi tiết phiếu nhập
					InvoiceDetailReqInfo invoiceDetailReqInfo = new InvoiceDetailReqInfo();
					invoiceDetailService.selectById(p.getId(), invoiceDetailReqInfo);
					InvoiceDetail invoiceDetailLoad = invoiceDetailReqInfo.getInvoice_detail();
					if (invoiceDetailLoad.getRefDetailID() != null) {
						itemv3.setRefDetailID(invoiceDetailLoad.getRefDetailID());
					} else {
						String refDetailID = UUID.randomUUID().toString();
						itemv3.setRefDetailID(refDetailID);
						invoiceDetailLoad.setRefDetailID(refDetailID);
						invoiceDetailLoad.setRefID(UUID.randomUUID().toString());
						invoiceDetailService.update(invoiceDetailLoad, userAction);
					}
					itemv3.setRefID(refID);
					itemv3.setInventoryItemID(invoiceDetailLoad.getProduct().getProduct_code());
					itemv3.setInventoryItemCode(invoiceDetailLoad.getProduct().getProduct_code());
					itemv3.setInventoryItemName(invoiceDetailLoad.getProduct().getProduct_name());
					itemv3.setDescription(invoiceDetailLoad.getProduct().getProduct_name());
					itemv3.setUnitName(invoiceDetailLoad.getProduct().getUnit());
					itemv3.setQuantity(invoiceDetailLoad.getQuantity());
					itemv3.setInWards(0);

					double tongtienntd = invoiceDetailLoad.getTotal();
					itemv3.setAmountOC(MyMath.round(tongtienntd));
					itemv3.setUnitPrice(MyMath.roundCustom(invoiceDetailLoad.getUnit_price(), 2));
					itemv3.setVATAmountOC(MyMath.round(tongtienntd * invoiceLoad.getTax_value()));
					itemv3.setAmount(invoiceDetailLoad.getTotal());
					itemv3.setVATAmount(MyMath.round(invoiceDetailLoad.getTotal() * invoiceLoad.getTax_value()));
					itemv3.setVATRate(invoiceLoad.getTax_value() * 100);

					itemv3.setDiscountRate(0);
					itemv3.setDiscountAmountOC(0);
					itemv3.setDiscountAmount(0);
					itemv3.setSortOrder(stt);
					itemv3.setIsPromotion(false);
					itemv3.setIsTemp(false);
					itemv3.setCompanyID(0);
					itemv3.setInventoryItemType(0);
					itemv3.setUnitAfterTax(0);
					itemv3.setAmountAfterTax(0);
					itemv3.setIsDescription(null);
					itemv3.setOutWards(0);
					itemv3.setInventoryItemNote(null);
					itemv3.setSortOrderView(stt);
					itemv3.setEntityState(1);
					itemv3.setInventoryItemNote(null);
					listEInvoiceDetailV3.add(itemv3);
					stt++;
				}
				v3.setInvoiceDetails(listEInvoiceDetailV3);
				v3s.add(v3);
				String jsonInputString = JsonParserUtil2.getGson().toJson(v3s);

				thongbao.setLoi(false);
				thongbao.setDulieu(jsonInputString);
				return thongbao;

			}
		} catch (Exception e) {
			thongbao.setLoi(true);
			thongbao.setThongtinloi(e.getMessage());
			return thongbao;
		}
		return thongbao;
	}
}
