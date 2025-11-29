package lixco.com.bean;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import lixco.com.commom_ejb.MyMath;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.SessionHelper;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.Product;
import lixco.com.entity.PromotionalPricing;
import lixco.com.hddt.InvoiceToJson;
import lixco.com.hddt.LoaiMaXuatNhap;
import lixco.com.interfaces.IInvoiceDetailService;
import lixco.com.interfaces.IInvoiceService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IPromotionalPricingService;
import lixco.com.reqInfo.ProductReqInfo;
import lixco.com.reqInfo.PromotionalPricingReqInfo;
import lombok.Getter;
import lombok.Setter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jboss.logging.Logger;
import org.joda.time.LocalDate;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.convertfont.FontType;
import trong.lixco.com.convertfont.MyConvertFontFactory;
import trong.lixco.com.util.MyStringUtil;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.MyUtilExcel;
import trong.lixco.com.util.Notify;

import com.google.gson.JsonObject;

@Named
@ViewScoped
public class PromotionalPricingBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IPromotionalPricingService promotionalPricingService;
	@Inject
	private IProductService productService;
	private PromotionalPricing promotionalPricingCrud;
	private PromotionalPricing promotionalPricingSelect;
	private List<PromotionalPricing> listPromotionalPricing;
	private Product productSearch;
	private Date fromDateSearch;
	private Date toDateSearch;
	private Account account;
	private FormatHandler formatHandler;

	@Override
	protected void initItem() {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
			account = SessionHelper.getInstance().getSession("account", request, Account.class);
			formatHandler = FormatHandler.getInstance();

			LocalDate today = new LocalDate();
			fromDateSearch = MyUtil.thembotthang(today.toDate(), -1);
			psDate = today.dayOfMonth().withMinimumValue().toDate();
			peDate = today.dayOfMonth().withMaximumValue().toDate();
			pmonth = today.getMonthOfYear();
			pyear = today.getYear();

			search();
		} catch (Exception e) {
			logger.error("PromotionalPricingBean.initItem:" + e.getMessage(), e);
		}
	}

	@Getter
	@Setter
	private String stextStr;

	public void search() {
		try {
			listPromotionalPricing = new ArrayList<>();
			JsonObject jsonInfo = new JsonObject();
			jsonInfo.addProperty("stextStr", MyStringUtil.replaceD(stextStr));
			jsonInfo.addProperty("product_id", productSearch == null ? 0 : productSearch.getId());
			jsonInfo.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "dd/MM/yyyy"));
			jsonInfo.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "dd/MM/yyyy"));
			JsonObject json = new JsonObject();
			json.add("promotional_pricing_info", jsonInfo);
			promotionalPricingService.search(JsonParserUtil.getGson().toJson(json), listPromotionalPricing);
		} catch (Exception e) {
			logger.error("PromotionalPricingBean.search:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdate() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (promotionalPricingCrud != null) {
				Product product = promotionalPricingCrud.getProduct();
				Date effectiveDate = promotionalPricingCrud.getEffective_date();
				Date expriryDate = promotionalPricingCrud.getExpiry_date();
				if (product != null && effectiveDate != null && expriryDate != null) {
					PromotionalPricingReqInfo t = new PromotionalPricingReqInfo(promotionalPricingCrud);
					if (promotionalPricingCrud.getId() == 0) {
						if (allowSave(new Date())) {
							promotionalPricingCrud.setCreated_by(account.getMember().getName());
							promotionalPricingCrud.setCreated_date(new Date());
							int chk = promotionalPricingService.insert(t);
							switch (chk) {
							case 0:
								success();
								listPromotionalPricing.add(0, promotionalPricingCrud.clone());
								break;
							case -2:
								current.executeScript("swaldesigntimer('Cảnh báo', 'Sản phẩm đã tồn tại!','warning',2000);");
								break;
							default:
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Lưu đơn giá sản phẩm thất bại!','error',2000);");
								break;
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
						}
					} else {
						if (allowUpdate(new Date())) {
							promotionalPricingCrud.setLast_modifed_by(account.getMember().getName());
							promotionalPricingCrud.setLast_modifed_date(new Date());
							int chk = promotionalPricingService.update(t);
							switch (chk) {
							case 0:
								success();
								listPromotionalPricing.set(listPromotionalPricing.indexOf(promotionalPricingCrud),
										promotionalPricingCrud);
								break;
							case -2:
								current.executeScript("swaldesigntimer('Cảnh báo', 'Sản phẩm đã tồn tại!','warning',2000);");
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
					current.executeScript("swaldesigntimer('Cảnh báo', 'Thông tin không đầy đủ, điền đủ thông tin chứa(*)!','warning',2000);");
				}
			}
		} catch (Exception e) {
			logger.error("PromotionalPricingBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}

	public void showDialogEdit() {
		PrimeFaces current = PrimeFaces.current();
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (promotionalPricingSelect != null) {
				promotionalPricingCrud = promotionalPricingSelect.clone();
				current.executeScript("PF('dlg1').show();");
			} else {
				notify.message("Chọn dòng để chỉnh sửa!");
			}
		} catch (Exception e) {
			logger.error("PromotionalPricingBean.showDialogEdit:" + e.getMessage(), e);
		}
	}

	public void showDialog() {
		PrimeFaces current = PrimeFaces.current();
		try {
			promotionalPricingCrud = new PromotionalPricing();
			current.executeScript("PF('dlg1').show();");
		} catch (Exception e) {
			logger.error("promotionalPricingSelect.showDialog:" + e.getMessage(), e);
		}
	}

	public void delete() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (promotionalPricingSelect != null) {
				if (allowDelete(new Date())) {
					if (promotionalPricingService.deleteById(promotionalPricingSelect.getId()) != -1) {
						success();
						listPromotionalPricing.remove(promotionalPricingSelect);
					} else {
						current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Lỗi hệ thống!','error',2000);");
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Chưa chọn dòng để xóa!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("promotionalPricingSelect.delete:" + e.getMessage(), e);
		}
	}

	public List<Product> completeProduct(String text) {
		try {
			List<Product> list = new ArrayList<Product>();
			productService.findLike(FormatHandler.getInstance().converViToEn(text), 120, list);
			return list;
		} catch (Exception e) {
			logger.error("promotionalPricingSelect.completeProduct:" + e.getMessage(), e);
		}
		return null;
	}

	public void showDialogUpload() {
		try {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('uploadpdffile').show();");
		} catch (Exception e) {
			logger.error("ProductBean.showDialogUpload:" + e.getMessage(), e);
		}
	}

	@Getter
	@Setter
	Date tungay;
	@Getter
	@Setter
	Date denngay;

	public void napDonGiaKM(FileUploadEvent event) {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			if (event.getFile() != null) {
				UploadedFile part = event.getFile();
				byte[] byteFile = event.getFile().getContent();
				List<PromotionalPricing> listPromotionalPricingTemp = new ArrayList<>();
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
						case 2:
							try {
								// đơn giá
								cellvalue = cellvalue.replace(",", ".");
								lix.setUnit_price(Double.parseDouble(cellvalue));
							} catch (Exception e) {
							}
							break;
						}
					}
					lix.setEffective_date(tungay);
					lix.setExpiry_date(denngay);
					listPromotionalPricingTemp.add(lix);
				}
				workBook = null;// free
				// delete all
				// promotionalPricingService.deleteAll();
				for (PromotionalPricing it : listPromotionalPricingTemp) {
					it.setCreated_by(account.getMember().getName());
					it.setCreated_date(new Date());
					promotionalPricingService.insert(new PromotionalPricingReqInfo(it));
				}
				search();
				notify.success();

			}
		} catch (Exception e) {
			logger.error("ProductBean.loadExcel:" + e.getMessage(), e);
		}
	}

	@Getter
	@Setter
	Date psDate;
	@Getter
	@Setter
	Date peDate;
	@Getter
	@Setter
	int pmonth;
	@Getter
	@Setter
	int pyear;
	private Set<Invoice> invoices;
	@Getter
	@Setter
	Invoice invoiceSelect;
	@Getter
	@Setter
	List<InvoiceDetail> invoiceDetails;

	@Inject
	IInvoiceDetailService invoiceDetailService;
	@Inject
	IInvoiceService invoiceService;

	public void ajax_setDate() {
		LocalDate lc = new LocalDate();
		psDate = lc.withMonthOfYear(pmonth).withYear(pyear).dayOfMonth().withMinimumValue().toDate();
		peDate = lc.withMonthOfYear(pmonth).withYear(pyear).dayOfMonth().withMaximumValue().toDate();
	}

	public void kiemTraDonGiaKM() {
		invoices = new HashSet<Invoice>();
		invoiceDetails = new ArrayList<InvoiceDetail>();
		// Danh sach chi tiet hoa don
		List<InvoiceDetail> details = new ArrayList<InvoiceDetail>();
		invoiceDetailService.selectByIECategories(Arrays.asList(LoaiMaXuatNhap.makhuyenmailaygiavon), psDate, peDate,
				details);
		for (int i = 0; i < details.size(); i++) {
			
			PromotionalPricing pr = promotionalPricingService.search(details.get(i).getInvoice().getInvoice_date(),
					details.get(i).getProduct().getId());
			if (pr != null) {
				if (details.get(i).getInvoice().getTax_value() == 0) {
					if (details.get(i).getUnit_price() != pr.getUnit_price()) {
						details.get(i).setChenhlechgiakm(true);
						invoices.add(details.get(i).getInvoice());
					}
				}
			}
		}
		if (invoices.size() == 0)
			success("Không có chênh lệch khuyến mãi.");
	}

	@Getter
	@Setter
	boolean chonhet = false;

	public void chonHetPhieu() {
		if (invoices != null)
			for (Invoice inv : invoices) {
				inv.setChonphieu(chonhet);
			}
	}

	public void apDungDonGiaDung() {
		int sochitietapdung = 0;
		for (Invoice inv : invoices) {
			if (inv.isChonphieu()) {
				List<InvoiceDetail> invoiceDetails = new ArrayList<InvoiceDetail>();
				invoiceDetailService.selectByInvoice(inv.getId(), invoiceDetails);
				for (int i = 0; i < invoiceDetails.size(); i++) {
					PromotionalPricing pr = promotionalPricingService.search(invoiceDetails.get(i).getInvoice()
							.getInvoice_date(), invoiceDetails.get(i).getProduct().getId());
					if (pr != null) {
						if (invoiceDetails.get(i).getInvoice().getTax_value() == 0) {
							if (invoiceDetails.get(i).getUnit_price() != pr.getUnit_price()) {
								sochitietapdung++;
								invoiceDetails.get(i).setUnit_price(pr.getUnit_price());
								invoiceDetails.get(i).setTotal(MyMath.round(
										invoiceDetails.get(i).getUnit_price() * invoiceDetails.get(i).getQuantity()));
								invoiceDetailService.capnhatdongiakm(invoiceDetails.get(i).getId(), pr.getUnit_price(),getAccount().getUserName());
							}
						}
					}
				}
				double total = invoiceDetails.stream().mapToDouble(x -> x.getTotal()).sum();
				invoiceService.updateTongTienKM(inv.getId(), MyMath.round(total),getAccount().getUserName());
			}
		}
		success("So SP ap dung: " + sochitietapdung);
		kiemTraDonGiaKM();
	}

	public void selectInvoice() {
		invoiceDetails = new ArrayList<InvoiceDetail>();
		if (invoiceSelect != null) {
			invoiceDetailService.selectByInvoice(invoiceSelect.getId(), invoiceDetails);
			for (int i = 0; i < invoiceDetails.size(); i++) {
				PromotionalPricing pr = promotionalPricingService.search(invoiceSelect.getInvoice_date(),
						invoiceDetails.get(i).getProduct().getId());
				if (pr != null) {
					if (invoiceDetails.get(i).getUnit_price() != pr.getUnit_price()) {
						invoiceDetails.get(i).setUnit_price_ss(pr.getUnit_price());
						invoiceDetails.get(i).setChenhlechgiakm(true);
						invoices.add(invoiceDetails.get(i).getInvoice());
					}
				}
			}
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public PromotionalPricing getPromotionalPricingCrud() {
		return promotionalPricingCrud;
	}

	public void setPromotionalPricingCrud(PromotionalPricing promotionalPricingCrud) {
		this.promotionalPricingCrud = promotionalPricingCrud;
	}

	public PromotionalPricing getPromotionalPricingSelect() {
		return promotionalPricingSelect;
	}

	public void setPromotionalPricingSelect(PromotionalPricing promotionalPricingSelect) {
		this.promotionalPricingSelect = promotionalPricingSelect;
	}

	public List<PromotionalPricing> getListPromotionalPricing() {
		return listPromotionalPricing;
	}

	public void setListPromotionalPricing(List<PromotionalPricing> listPromotionalPricing) {
		this.listPromotionalPricing = listPromotionalPricing;
	}

	public Product getProductSearch() {
		return productSearch;
	}

	public void setProductSearch(Product productSearch) {
		this.productSearch = productSearch;
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

	public FormatHandler getFormatHandler() {
		return formatHandler;
	}

	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
	}

	public List<Invoice> getInvoices() {
		if (invoices != null)
			return new ArrayList<>(invoices);
		return new ArrayList<>();
	}
}
