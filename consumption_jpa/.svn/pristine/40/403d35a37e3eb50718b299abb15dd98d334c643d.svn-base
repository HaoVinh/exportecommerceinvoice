package lixco.com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import trong.lixco.com.entity.AbstractEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hoadonchitietc2")
@Getter
@Setter
public class HoaDonChiTietC2 extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;
	@Column(name = "productdh_code")
	private String productdh_code; // mã sản phẩm

	@Column(name = "product_name")
	private String product_name; // tên sản phẩm

	private double quantity;// số lượng
	private double real_quantity;// số lượng tách

	@ManyToOne
	@JoinColumn(name = "hoa_don_c2_id")
	private HoaDonC2 hoaDonC2;

	@ManyToOne(fetch = FetchType.LAZY)
	private InvoiceDetail invoiceDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private InvoiceDetailTemp invoiceDetailTemp;
	


	private String batchCode;     // mã lô hàng

}
