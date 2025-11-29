package lixco.com.entityapi;

import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.Product;
import lombok.Data;

@Data
public class InvoiceDetailDTO {

	private long idtc;
	private String productCode;// sản phẩm
	private double quantity;// số lượng
	private String note_batch_code;
	private double dongia;
	private double thanhtien;
	private Product product;

	public InvoiceDetailDTO(InvoiceDetail invoiceDetail) {
		this.idtc = invoiceDetail.getId();
		this.productCode = invoiceDetail.getProduct().getProduct_code();
		this.quantity = invoiceDetail.getQuantity();
		this.note_batch_code = invoiceDetail.getNote_batch_code();
	}

}
