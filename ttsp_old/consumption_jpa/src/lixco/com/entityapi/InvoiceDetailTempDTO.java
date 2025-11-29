package lixco.com.entityapi;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class InvoiceDetailTempDTO {

	private Long invoiceDetailId;
	private double quantity;// Số lượng yêu cầu
	private String productCode;
	private String productName;
	private double boxQuantity;// Số lượng thùng/pallet của sản phẩm
	private double specification;
	private String productDHCode;

	private boolean spchinh;// Trường đánh dấu sp chính hay sp khuyến mãi
	private double realQuantity; // Số lượng thực xuất
	private double realQuantityDVT;
	private double unit_price;
	private Long invoiceTempId;
	public InvoiceDetailTempDTO(Object[] result) {
		this.invoiceDetailId = (result[0] != null) ? ((BigInteger) result[0]).longValue() : 0;
		this.quantity = (result[1] != null) ? ((BigDecimal) result[1]).doubleValue() : 0;
		this.productCode = (String) result[2];
		this.productName = (String) result[3];
		this.boxQuantity = (result[4] != null) ? ((BigDecimal) result[4]).doubleValue() : 0;
		this.specification = (result[5] != null) ? ((BigDecimal) result[5]).doubleValue() : 0;
		this.productDHCode = (String) result[6];
		this.unit_price = (result[7] != null) ? ((BigDecimal) result[7]).doubleValue() : 0;
		this.spchinh = this.productDHCode == null;
		this.realQuantityDVT = (result[8] != null) ? ((BigDecimal) result[8]).doubleValue() : 0;
		this.invoiceTempId = (result[9] != null) ? ((BigInteger) result[9]).longValue() : 0;
		this.realQuantity = (result[10] != null) ? ((BigDecimal) result[10]).doubleValue() : 0;

	}
}
