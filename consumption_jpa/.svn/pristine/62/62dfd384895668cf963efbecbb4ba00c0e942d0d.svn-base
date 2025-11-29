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

import lombok.Data;


@Entity
@Table(name = "invoicetemp")
@Data
public class InvoiceTemp implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_date;
	private String created_by;
	private long created_by_id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date last_modifed_date;
	private String last_modifed_by;
	@Column(unique = true)
	private String invoice_temp_code;// mã hóa đơn (id foxpro)
	private String voucher_code;// mã chứng từ
	@Temporal(TemporalType.DATE)
	private Date invoice_temp_date;// ngày hóa đơn
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;// khách hàng
	@ManyToOne(fetch = FetchType.LAZY)
	private Car car;// xe
	@Transient
	private String soxe;
	@ManyToOne(fetch = FetchType.LAZY)
	private PaymentMethod payment_method;// hình thức thanh toán
	@ManyToOne(fetch = FetchType.LAZY)
	private DeliveryPricing delivery_pricing;// đơn giá vận chuyển
	@ManyToOne(fetch = FetchType.LAZY)
	private FreightContract freight_contract;// hợp đồng vận chuyển
	private String department_name;// bộ phận
	private String note;// ghi chú hóa đơn
	@ManyToOne(fetch = FetchType.LAZY)
	private IECategories ie_categories;// Danh mục xuất nhập
	private String ie_reason;// lý do xuất nhập
	@ManyToOne(fetch = FetchType.LAZY)
	private Warehouse warehouse;// mã kho
	private double tax_value;// hệ số thuế
	private double tongtien;// tong tien
	private double thue;// thue
	private double tax_edit;// điều chỉnh thuế
	@ManyToOne(fetch = FetchType.LAZY)
	private Contract contract;// hợp đồng mua bán
	private String contract_no;// số hợp đồng mua bán
	private String invoice_temp_serie;// serie hóa đơn
	private String order_voucher;// số đơn hàng (số chứng từ đơn hàng)
	private String order_code;// mã đơn hàng
	private String movement_commands_no;// số lệnh điều động
	private boolean payment;// đã thanh toán
	private Date payment_date;// ngày thanh toan
	private boolean timeout;// ngoài giờ
	@ManyToOne(fetch = FetchType.LAZY)
	private PromotionProgram promotion_program;// chương trình khuyến mãi
	@ManyToOne(fetch = FetchType.LAZY)
	private PricingProgram pricing_program;// chương trình đơn giá
	@ManyToOne(fetch = FetchType.LAZY)
	private HarborCategory harbor_category;// cảng đến
	@ManyToOne(fetch = FetchType.LAZY)
	private Stocker stocker;// thủ kho
	@ManyToOne(fetch = FetchType.LAZY)
	private Stevedore stevedore;// bốc xếp
	@ManyToOne(fetch = FetchType.LAZY)
	private FormUpGoods form_up_goods;// hình thức lên hàng
	@ManyToOne(fetch = FetchType.LAZY)
	private OrderLix order_lix;// đơn hàng
	private long idorderlix;// đơn hàng
	private String po_no;// số po
	private String note2;
	@ManyToOne(fetch = FetchType.LAZY)
	private Carrier carrier;// người vận chuyển
	private String transport_content;// nội dung vận chuyển
	@Temporal(TemporalType.TIMESTAMP)
	private Date delivery_date;
	@Temporal(TemporalType.TIMESTAMP)
	private Date s_up_goods_date;// ngày giờ bắt đầulên hàng
	@Temporal(TemporalType.TIMESTAMP)
	private Date c_up_goods_date;// ngày giờ lên hàng xong
	private int time_up_goods;// thời gian lên hàng số phút
	private String e_invoice_id;// id hóa đơn điện tử
	private String content_qd;// nội dung chuyển đổi
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "invoicetemp")
	private List<InvoiceDetailTemp> list_invoice_detail_temp;
	private double discount;// chiếc khấu
	private boolean ipromotion;
	@ManyToOne(fetch = FetchType.LAZY)
	private InvoiceTemp invoice_temp_own;// hóa đơn chính
	private String exchange_val;
	private int editVersion;
	private String refId;
	private String refIdVCNB;
	private int editVersionVCNB;
	private String lookup_code;
	private String lookup_code_vcnb;
	private int status;// 0 chưa hoàn thành, 1 hoàn thành (tính doanh thu), 2
						// hủy hóa đơn
	private boolean exported;
	private String idfox;
	private String donvitien;

	private String idhoadongoc;
	private String sohoadongoc;
	private double tygia;
	private long idhoadonxk;
	private String ldd;
	private String sophieuvc;
	private String quydoi;

	private String jsonhoadon;
	@Temporal(TemporalType.TIMESTAMP)
	private Date datehoadon;

	@Transient
	private boolean chonphieu;
	private boolean naponline;

	private String act;
	private String useraction;

	// bo sung 08/08/2023
	private double chietkhau;
	private String lydochietkhau;
	
	//Bo sung ngày 12/10/2023
	private boolean phieudacopy;
	private boolean isSaved = false;
	public InvoiceTemp() {
		
	}
	public InvoiceTemp(long id) {
		this.id = id;
	}
	@Override
	public InvoiceTemp clone() throws CloneNotSupportedException {
		return (InvoiceTemp) super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvoiceTemp other = (InvoiceTemp) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
