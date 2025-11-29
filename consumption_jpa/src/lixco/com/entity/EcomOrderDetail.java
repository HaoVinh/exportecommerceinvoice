package lixco.com.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import trong.lixco.com.entity.AbstractEntity;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "ecom_order_detail")
public class EcomOrderDetail extends AbstractEntity {

	private String orderId;
	@ManyToOne
	private EcomOrder order;
	private String name;
	private String sku;
	private Double itemPrice;
	@Transient
	private int stt;
	private String orderItemNumber;
	private String loaitmdt;
	private boolean isVariant;
	private int quantity;
	private int skuQuantity;
	private Double splitPrice;
	private Double unitPrice;
	private String promotionType;// phân biệt hàng bán và hàng KM của shopee
    private boolean is_gift; // phân biệt hàng bán và hàng KM của  tiktok
    private String orderType;
	private Double shopeeDiscount;
	private Double sellerDiscount;
	private Double comboDiscount;
	private Double discountedPrice;
	private String dataDetailJSON;
	private Double shippingFeeDiscount;
	private boolean combo;
	private String imageURL;
	@OneToOne
	private Product product;
	@Override
	public final int hashCode() {
		return getClass().hashCode();
	}

}