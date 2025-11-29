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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name = "invoicedetail")
@Data
public class InvoiceDetail implements Serializable, Cloneable {
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
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;// sản phẩm
	private double quantity;// số lượng
	private double unit_price;// đơn giá
	@Transient
	private double unit_price_ss;// đơn giá khuyen mai (de so sanh chenh lech)
	@Transient
	private boolean chenhlechgiakm;// đơn giá khuyen mai (de so sanh chenh lech)
	private double total;// sotien
	private double real_quantity;// số lượng nhập xuất thực tế
	private String note;// ghi chú
	private String productdh_code;// mã sản phẩm đơn hàng
	@Column(insertable = false, updatable = false)
	private Long detail_own_id;// ckey hdbh
	private double foreign_unit_price;// đơn giá ngoại tệ
	private double total_foreign_amount;// số tiền ngoại tệ
	@ManyToOne(fetch = FetchType.LAZY)
	private Invoice invoice;
	private boolean ex_order;// chuyển từ đơn hàng qua
	private String note_batch_code;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "detail_own_id")
	private InvoiceDetail invoice_detail_own;// id chi tiet hoa don chinh, áp
												// dụng cho trường hợp sản phẩm
												// khuyển mãi thuộc chi tiết hóa
												// đơn chính nào.
	private String refDetailID;// Khóa chính Chi tiết HĐĐT
								// 49e59629-144b-c705-73e6-38617f7626a2
	private String RefID;// Khóa ngoại kết nối với Hóa đơn ĐT
							// 5f2b48ff-b866-1a88-c158-25d265f01734
	private String refDetailVCNBID;
	private String refIDVCNB;
	private String idfox;

	private long idcthdxuatkhau;

	private boolean naponline = false;

	private long idchitietdonhang;
	private long idchitietdonhangkm;

	private String act;
	private String useraction;

	private boolean hdsuagia;
	private String napdulieu;

	private double slpallet;

	// huyen
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "invoiceDetail")
	private List<HoaDonC2> hoaDonC2;

	@Override
	public InvoiceDetail clone() throws CloneNotSupportedException {
		return (InvoiceDetail) super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvoiceDetail other = (InvoiceDetail) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
