package lixco.com.entityapi;

import java.util.Date;
import java.util.List;

import lixco.com.entity.Invoice;
import lombok.Data;

@Data
public class InvoiceDTO3 {
    private long id;
    private String invoice_code;
    private String voucher_code;
    private Date invoice_date;
    private double totalAmount;
    private String customerName;
    private String customerCode;
    private String licensePlate;
    private String poNo;
    private String ieCode;
    private String createdBy;
    private String pricingProgramCode;
    private String promotionProgramCode;
    private long idOrderLix;
    private Date delivery_date;
    private boolean payment;
    private Date paymentDate;
    private String contractNo;
    private String lookup_code;
    private String refId;
    private String note;
    private String carrierName;
    private String sophieuVC;
    private String deliveryAddress;
    private String transportContent;
    private String contractVoucherCode;
    private double discount;
    private String ldd;
    private String idhoadongoc;
    private String sohoadongoc;
    
    private double tongtien;
	private boolean phieudacopy;
    
    //truong them
    private boolean chonphieu;
    
    
    
	
	public InvoiceDTO3(Invoice invoice) {
	    if (invoice != null) {
	        this.id = invoice.getId();
	        this.invoice_code = invoice.getInvoice_code();
	        this.voucher_code = invoice.getVoucher_code();
	        this.invoice_date = invoice.getInvoice_date();
	        this.totalAmount = invoice.getTongtien();
	        this.customerName = invoice.getCustomer() != null ? invoice.getCustomer().getCustomer_name() : null;
	        this.customerCode = invoice.getCustomer() != null ? invoice.getCustomer().getCustomer_code() : null;
	        this.licensePlate = invoice.getCar() != null ? invoice.getCar().getLicense_plate() : null;
	        this.poNo = invoice.getPo_no();
	        this.ieCode = invoice.getIe_categories() != null ? invoice.getIe_categories().getCode() : null;
	        this.createdBy = invoice.getCreated_by();
	        this.pricingProgramCode = invoice.getPricing_program() != null ? invoice.getPricing_program().getProgram_code() : null;
	        this.promotionProgramCode = invoice.getPromotion_program() != null ? invoice.getPromotion_program().getProgram_code() : null;
	        this.idOrderLix = invoice.getIdorderlix();
	        this.delivery_date = invoice.getDelivery_date();
	        this.payment = invoice.isPayment();
	        this.paymentDate = invoice.getPayment_date();
	        this.contractNo = invoice.getContract_no();
	        this.lookup_code = invoice.getLookup_code();
	        this.refId = invoice.getRefId();
	        this.note = invoice.getNote();
	        this.carrierName = invoice.getCarrier() != null ? invoice.getCarrier().getCarrier_name() : null;
	        this.sophieuVC = invoice.getSophieuvc();
	        this.deliveryAddress = invoice.getDelivery_pricing() != null ? invoice.getDelivery_pricing().getAddress() : null;
	        this.transportContent = invoice.getTransport_content();
	        this.contractVoucherCode = invoice.getContract() != null ? invoice.getContract().getVoucher_code() : null;
	        this.discount = invoice.getDiscount();
	        this.ldd = invoice.getLdd();
	        this.idhoadongoc = invoice.getIdhoadongoc();
			this.sohoadongoc = invoice.getSohoadongoc();
	        
	        this.tongtien=invoice.getTongtien();
			this.phieudacopy = invoice.isPhieudacopy();
	    }
	}




	public InvoiceDTO3(long id, String invoice_code, String voucher_code, Date invoice_date, double totalAmount,
			String customerName, String customerCode, String licensePlate, String poNo, String ieCode,
			String createdBy, String pricingProgramCode, String promotionProgramCode, long idOrderLix,
			Date delivery_date, boolean payment, Date paymentDate, String contractNo, String lookup_code, String refId,
			String note, String carrierName, String sophieuVC, String deliveryAddress, String transportContent,
			String contractVoucherCode, double discount, String ldd, String idhoadongoc, String sohoadongoc,
			double tongtien, boolean phieudacopy) {
		super();
		this.id = id;
		this.invoice_code = invoice_code;
		this.voucher_code = voucher_code;
		this.invoice_date = invoice_date;
		this.totalAmount = totalAmount;
		this.customerName = customerName;
		this.customerCode = customerCode;
		this.licensePlate = licensePlate;
		this.poNo = poNo;
		this.ieCode = ieCode;
		this.createdBy = createdBy;
		this.pricingProgramCode = pricingProgramCode;
		this.promotionProgramCode = promotionProgramCode;
		this.idOrderLix = idOrderLix;
		this.delivery_date = delivery_date;
		this.payment = payment;
		this.paymentDate = paymentDate;
		this.contractNo = contractNo;
		this.lookup_code = lookup_code;
		this.refId = refId;
		this.note = note;
		this.carrierName = carrierName;
		this.sophieuVC = sophieuVC;
		this.deliveryAddress = deliveryAddress;
		this.transportContent = transportContent;
		this.contractVoucherCode = contractVoucherCode;
		this.discount = discount;
		this.ldd = ldd;
		this.idhoadongoc = idhoadongoc;
		this.sohoadongoc = sohoadongoc;
		this.tongtien = tongtien;
		this.phieudacopy = phieudacopy;
	}




	
}
