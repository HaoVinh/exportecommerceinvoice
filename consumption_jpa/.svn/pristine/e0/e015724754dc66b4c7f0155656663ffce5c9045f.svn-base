package lixco.com.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
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
@Table(name="orderdetail")
@Data
public class OrderDetail implements Serializable,Cloneable{
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
	@ManyToOne(fetch=FetchType.LAZY)
	private Product product;// sản phẩm
	private double box_quantity;//số lượng thùng
	private double box_quantity_actual;//thuc xuat số lượng thùng
	private double quantity;//số lượng
	private double unit_price;//đơn giá
	private double unit_price_goc;//đơn giá goc
	private double unit_pricePO;//đơn giá PO
	private double total;//thanh tien
	private String note;//qui cách text thông số 
	private int promotion_forms;// hình thức khuyến mãi
	@ManyToOne(fetch=FetchType.LAZY)
	private OrderLix order_lix;
	private String batch_code;
	@Column(unique=true)
	private Long npp_order_detail_id;
	private int container_number;// số container
	private int seal_number;//số seal
	@Transient
	private double inv_quantity;//số lượng tồn thực tế
	@Transient
	private double inv_quantity_cal;//số lượng tồn tính toán (dự báo tồn)
	private String idfox;
	
	private int indexsort;
	private String columnsort;
	private boolean ascd;
	
	private boolean huyct;
	
	@Transient
	private String thukho;
	
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
		OrderDetail other = (OrderDetail) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public OrderDetail clone() throws CloneNotSupportedException {
		return (OrderDetail)super.clone();
	}
	
}
