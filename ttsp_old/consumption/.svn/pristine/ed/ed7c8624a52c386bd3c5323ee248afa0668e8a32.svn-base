package lixco.com.entityapi;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class InvoiceApiDTO {
	// Biến thông tin hóa đơn
	private Long idInvoice; // ID của hóa đơn
	private String voucherCode; // Mã chứng từ
	private Date invoiceDate; // Ngày hóa đơn
	private String customerCode; // Mã khách hàng
	private String customerName; // Tên khách hàng
	private String orderVoucher; // Mã chứng từ đơn hàng
	private double taxValue; // Giá trị thuế
	private String codeWarhouse; // Mã kho
	private String ieCategories; // Mã loại xuất/nhập
	private String content; // Nội dung
	private String note; // Ghi chú
	private String invoiceSerie; // Số hóa đơn
	private double tongTien; // Tổng tiền
	private double thue; // Thuế
	private String licensePlate; // Biển số xe
	private String voucherCodeCont; // Mã chứng từ container
	private String poNo; // Mã đơn hàng PO
	private String lookupCode; // Mã tra cứu

	// Biến thông tin chi tiết hóa đơn
	private Long invoiceDetailId; // ID chi tiết hóa đơn
	private String productCode; // Mã sản phẩm
	private String productName; // Tên sản phẩm
	private double quantity; // Số lượng
	private double unitPrice; // Đơn giá
	private double total; // Tổng tiền
	private double foreignUnitPrice; // Đơn giá ngoại tệ
	private double totalForeignAmount; // Tổng số tiền ngoại tệ
	private String productDHCode; // Mã sản phẩm đơn hàng
	private Long invoiceDetailOwnId; // ID chi tiết hóa đơn sở hữu

	public InvoiceApiDTO(Object[] result) {
		this.idInvoice = (result[0] != null) ? ((BigInteger) result[0]).longValue() : 0;
		this.voucherCode = (String) result[1];
		this.invoiceDate = (Date) result[2];
		this.customerCode = (String) result[3];
		this.customerName = (String) result[4];
		this.orderVoucher = (String) result[5];
		this.taxValue = (result[6] != null) ? ((BigDecimal) result[6]).doubleValue() : 0;
		this.codeWarhouse = (String) result[7];
		this.ieCategories = (String) result[8];
		this.content = (String) result[9];
		this.note = (String) result[10];
		this.invoiceSerie = (String) result[11];
		this.tongTien = (result[12] != null) ? ((Double) result[12]) : 0;
		this.thue = (result[13] != null) ? ((Double) result[13]) : 0;
		this.licensePlate = (String) result[14];
		this.voucherCodeCont = (String) result[15];
		this.poNo = (String) result[16];
		this.lookupCode = (String) result[17];
		this.invoiceDetailId = (result[18] != null) ? ((BigInteger) result[18]).longValue() : 0;
		this.productCode = (String) result[19];
		this.productName = (String) result[20];
		this.quantity = (result[21] != null) ? ((BigDecimal) result[21]).doubleValue() : 0;
		this.unitPrice = (result[22] != null) ? ((BigDecimal) result[22]).doubleValue() : 0;
		this.total = (result[23] != null) ? ((BigDecimal) result[23]).doubleValue() : 0;
		this.foreignUnitPrice = (result[24] != null) ? ((BigDecimal) result[24]).doubleValue() : 0;
		this.totalForeignAmount = (result[25] != null) ? ((BigDecimal) result[25]).doubleValue() : 0;
		this.productDHCode = (String) result[26];
		this.invoiceDetailOwnId = (result[27] != null) ? ((BigInteger) result[27]).longValue() : 0;
	}

}
