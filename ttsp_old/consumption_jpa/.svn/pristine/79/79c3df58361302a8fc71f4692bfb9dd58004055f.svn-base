package lixco.com.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Data;
@Entity
@Table(name="promotionorderdetail")
@Data
public class PromotionOrderDetail implements Serializable,Cloneable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_date;
	private String created_by;
	@Temporal(TemporalType.TIMESTAMP)
	private Date last_modifed_date;
	private String last_modifed_by;
	private String productdh_code;//mã sản phẩm đơn hàng
	@ManyToOne(fetch=FetchType.LAZY)
	private Product product;//sản phẩm khuyến mãi
	private double quantity;//số lượng
	private double quantityAct;//số lượng da xuat
	private double unit_price;//đơn giá
	private String specification;//qui cách thông số <=> mã số ct củ
	private String productkm_code;//mã sản phẩm khuyến mãi .
	private String note;//ghi chú
	private int order_number;//số thứ tự hd
	private boolean owe;// nợ hay không.
	@ManyToOne(fetch=FetchType.LAZY)
	private OrderDetail order_detail;
	private String batch_code;
	@Transient
	private double real_quantity;//số lượng thực xuất sau khi chuyển qua hóa đơn
	@Transient
	private double inv_quantity;//số lượng tồn thực tế
	@Transient
	private double inv_quantity_cal;//số lượng tồn tính toán (dự báo tồn)
	private boolean no;
	
	private boolean huyct;
	@ManyToOne(fetch=FetchType.LAZY)
	private InvoiceDetailTemp invoice_detail_temp;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PromotionOrderDetail other = (PromotionOrderDetail) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public PromotionOrderDetail clone() throws CloneNotSupportedException {
		return (PromotionOrderDetail)super.clone();
	}
}
