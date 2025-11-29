package lixco.com.entityapi;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceTempDTO {
	public InvoiceTempDTO() {
		super();
	}

	private Long idInvoice;
	private String customerCode;
	private String customerName;
	private String orderCode;
	private String orderVoucher;
	private Date invoiceDate;
	private double taxValue; // Giá trị thuế
	private String warehouseCode; // Mã kho
	private String ieCategories; // Mã loại xuất/nhập
	private String content = ""; // Nội dung
	private String note = ""; // Ghi chú
	private double tongTien; // Tổng tiền
	private double thue; // Thuế
	private String poNo; // Mã đơn hàng PO
	private String lookupCode = ""; // Mã tra cứu
	private Date delivery_date;
	private String voucher_code;
	private List<InvoiceDetailTempDTO> invoiceDetailTemps;
	private long orderId;
	private boolean exported;
	private boolean isSaved;

	public InvoiceTempDTO(Object[] result) {
		this.idInvoice = (result[0] != null) ? ((BigInteger) result[0]).longValue() : 0;
		this.customerCode = (String) result[1];
		this.customerName = (String) result[2];
		this.orderCode = (String) result[3];
		this.orderVoucher = (String) result[4];
		this.invoiceDate = (Date) result[5];
		this.taxValue = (result[6] != null) ? ((Double) result[6]) : 0;
		this.warehouseCode = (String) result[7];
		this.ieCategories = (String) result[8];
		this.content = (String) result[9];
		this.note = (String) result[10];
		this.tongTien = (result[11] != null) ? ((Double) result[11]) : 0;
		this.thue = (result[12] != null) ? ((Double) result[12]) : 0;
		this.poNo = (String) result[13];
		this.lookupCode = (String) result[14];
		this.delivery_date = (Date) result[15];
		this.voucher_code = (String) result[16];
		this.orderId = (result[17] != null) ? ((BigInteger) result[17]).longValue() : 0;
		this.exported = (result[18] != null) && ((Boolean) result[18]);
		this.isSaved = (result[19] != null) && ((Boolean) result[19]);
	}
}
//private Long invoiceDetailId;
//private double quantity;// Số lượng yêu cầu
//private String productCode;
//private String productName;
//private double boxQuantity;// Số lượng thùng/pallet của sản phẩm
//private double specification;
//private String productDHCode;
//
//private boolean spchinh;// Trường đánh dấu sp chính hay sp khuyến mãi

//private double real_quantity; // Số lượng thực xuất
//this.invoiceDetailId = (result[5] != null) ? ((BigInteger) result[5]).longValue() : 0;
//this.quantity = (result[6] != null) ? ((BigDecimal) result[6]).doubleValue() : 0;
//this.productCode = (String) result[7];
//this.productName = (String) result[8];
//this.boxQuantity = (result[9] != null) ? ((BigDecimal) result[9]).doubleValue() : 0;
//this.specification = (result[10] != null) ? ((BigDecimal) result[10]).doubleValue() : 0;
//this.productDHCode = (String) result[11];
//
//this.spchinh = this.productDHCode == null;
