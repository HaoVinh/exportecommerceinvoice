package lixco.com.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import trong.lixco.com.entity.AbstractEntity;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ecom_order")
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class EcomOrder extends AbstractEntity {
	private String orderId;
	private String customerLastName;
	private String customerFirstName;
	private Date createdAt;
	private Date updatedAt; // thời gian webhook sàn trả về
	private Date thoigiancapnhat; // thời gian hệ thống cập nhật trạng thái
	private Double shippingFee;
	private String status;

	private String orderNumber;

	private Double price;
	private String loaitmdt;
	private Double lastPrice;
	private Double shopeeDiscount;
	private Double sellerDiscount;
	private Double comboDiscount;
	private Double discountedPrice;
	private Double shippingDiscount;
	private Double totalSellerDiscount;
	
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	private List<EcomOrderDetail> orderDetails;

	@Transient
	private boolean select = false;

	private String myStatus;
	private String refID;
	private String jsonhoadon;
	private String lookup_code;
	private Date datehoadon;
	private int editVersion;

	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY)
	private PaymentMethod payment_method;
	@OneToOne(fetch = FetchType.LAZY)
	private IECategories ieCategories;
	private String ghichu;
	private String orderType;
	private String dataJson;
	private boolean daxuathddt;
	private boolean autoExported;
	private String paymentDataJson;
	
	@Override
	public final int hashCode() {
		return getClass().hashCode();
	}
}
