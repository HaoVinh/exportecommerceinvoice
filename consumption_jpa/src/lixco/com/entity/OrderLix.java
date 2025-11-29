package lixco.com.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import trong.lixco.com.util.GiaTriMacDinh;
import lombok.Data;

@Entity
@Table(name = "orderlix")
@Data
public class OrderLix implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_date;
	private long created_by_id;
	private String created_by;
	private String ldd;
	@Temporal(TemporalType.TIMESTAMP)
	private Date last_modifed_date;
	private String last_modifed_by;
	@Column(unique = true)
	private String order_code;// mã đơn hàng
	private String voucher_code;// số chứng từ.
	@Temporal(TemporalType.DATE)
	private Date orderDateCreate;// ngày đơn hàng
	private String brand;
	@Temporal(TemporalType.TIMESTAMP)
	private Date order_date;// ngày đơn hàng
	@Temporal(TemporalType.TIMESTAMP)
	private Date delivery_date;// ngày giao hàng

	@ManyToOne(fetch = FetchType.LAZY)
	private Carrier carrier;// người vận chuyển

	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;// khách hàng

	@ManyToOne(fetch = FetchType.LAZY)
	private PromotionProgram promotion_program;// chương trình khuyến mãi
	@ManyToOne(fetch = FetchType.LAZY)
	private PricingProgram pricing_program; // chương trình định giá
	@ManyToOne(fetch = FetchType.LAZY)
	private PaymentMethod payment_method;// phương thức thanh toán
	@ManyToOne(fetch = FetchType.LAZY)
	private Car car;// xe
	private String invoice_no;// số hóa đơn
	private String contract_no;// số hợp đồng.
	@ManyToOne(fetch = FetchType.LAZY)
	private FreightContract freight_contract;// hợp đồng vận chuyển
	private String serie_no;// số serie
	private String cus_voucher;// chứng từ khách hàng
	@ManyToOne(fetch = FetchType.LAZY)
	private Warehouse warehouse;// nhà kho
	private double tax_value = GiaTriMacDinh.HESOTHUE;// hệ số thuế
	@ManyToOne(fetch = FetchType.LAZY)
	private IECategories ie_categories;// danh mục xuất nhập
	@ManyToOne(fetch = FetchType.LAZY)
	private DeliveryPricing delivery_pricing;// đơn giá vận chuyển
	private boolean delivered;// đã giao
	private String reason_not_delivered;// lý do không giao hàng
	private String note;
	private String note2;
	private String po_no;// số po
	@Temporal(TemporalType.TIMESTAMP)
	private Date s_up_goods_date;// ngày giờ bắt đầulên hàng
	@Temporal(TemporalType.TIMESTAMP)
	private Date c_up_goods_date;// ngày giờ lên hàng xong
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order_lix")
	private List<OrderDetail> list_order_detail;
	private int status;// 0 phiếu chua chuyen,1 đang chuyển , 2 đã chuyển
	@Column(unique = true)
	private Long npp_order_id;
	private String idfox;
	private String sophieuvc;

	private boolean dhhuy;

	// bo sung 08/08/2023
	private double chietkhau;
	private String lydochietkhau;

	@Transient
	private boolean chonphieu;

	private boolean dathanhtoan;
	private String luuythanhtoan;
	@Temporal(TemporalType.TIMESTAMP)
	private Date ngaygioxevao;
	private int slxe;
	private double khoiluongvc;

	public OrderLix() {

	}

	public OrderLix(long id) {
		this.id = id;
	}

	public OrderLix(long id, boolean delivered, Car car) {
		this.id = id;
		this.delivered = delivered;
		this.car = car;
	}

	public OrderLix(long id, Long pricingProgramId, Long promotionProgramId, Date order_date, Date delivery_date) {
		this.id = id;
		if (pricingProgramId != null) {
			this.pricing_program = new PricingProgram(pricingProgramId);
		}
		if (promotionProgramId != null) {
			this.promotion_program = new PromotionProgram(pricingProgramId);
		}
		this.order_date = order_date;
		this.delivery_date = delivery_date;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderLix other = (OrderLix) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public OrderLix clone() throws CloneNotSupportedException {
		return (OrderLix) super.clone();
	}

}
