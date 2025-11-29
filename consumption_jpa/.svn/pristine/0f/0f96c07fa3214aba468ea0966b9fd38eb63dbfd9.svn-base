package lixco.com.entityapi;

import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.InvoiceDetailTemp;
import lixco.com.entity.Product;
import lombok.Data;
import lombok.Data;

@Data
public class InvoiceDetailDTO3 {
	// Khóa liên kết
	private Long hoaDonC2Id; // id của HoaDonC2 (nếu có)
	private Long hoaDonChiTietC2Id; // id của HoaDonChiTietC2 (nếu có)

	// Thông tin từ InvoiceDetail gốc
	private Long invoiceDetailId; // id dòng chi tiết gốc (InvoiceDetail)
	private Long productId;
	private String productCode; // mã sản phẩm đơn hàng
	private String productName; // tên sản phẩm
	private double quantity; // số lượng gốc

	// Thông tin tách hóa đơn C2
	private double quantityEdit; // số lượng tách (nhập liệu)
	private String voucherCode2; // số CT cấp 2
	private double splittedQuantity;// số đã tách trc

	private String vehicleNumber; // số xe
	private String batchCode;     // mã lô hàng
	// Constructor: map từ InvoiceDetail gốc
	public InvoiceDetailDTO3(InvoiceDetail invoiceDetail) {
		this.invoiceDetailId = invoiceDetail.getId();
		this.productId = invoiceDetail.getProduct().getId();
		this.productCode = invoiceDetail.getProduct().getProduct_code();
		this.productName = invoiceDetail.getProduct().getProduct_name();
		this.quantity = invoiceDetail.getQuantity();
		this.quantityEdit = 0d;
        this.splittedQuantity = 0d;
	}
	public InvoiceDetailDTO3(InvoiceDetailTemp invoiceDetailTemp) {
        if (invoiceDetailTemp != null) {
            this.invoiceDetailId = invoiceDetailTemp.getId();
            if (invoiceDetailTemp.getProduct() != null) {
                this.productId = invoiceDetailTemp.getProduct().getId();
                this.productCode = invoiceDetailTemp.getProduct().getProduct_code();
                this.productName = invoiceDetailTemp.getProduct().getProduct_name();
            }
            this.quantity = invoiceDetailTemp.getQuantity();
        }
        this.quantityEdit = 0d;
        this.splittedQuantity = 0d;
    }
}
