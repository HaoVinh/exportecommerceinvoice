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

import lixco.com.entityapi.ProductKMDTO;
import lombok.Data;

@Entity
@Table(name="productKM")
@Data
public class ProductKM implements Serializable,Cloneable{//sản phẩm
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
	private Product product;
	@ManyToOne(fetch=FetchType.LAZY)
	private Product promotion_product;//Sản phẩm khuyến mãi
	private double quantity=1; //qui cách đóng gói.
	private boolean spchinh;
	private boolean disable;//không còn sử dụng
	
	@Transient
	private String promotion_product_code;
	
	public ProductKM(ProductKMDTO pKM) {
		this.created_date = pKM.getCreated_date();
		this.created_by = pKM.getCreated_by();
		this.last_modifed_date = pKM.getLast_modifed_date();
		this.last_modifed_by = pKM.getLast_modifed_by();
		this.promotion_product_code=pKM.getPromotion_product();
		this.quantity = pKM.getQuantity();
		this.spchinh = pKM.isSpchinh();
		this.disable = pKM.isDisable();

	}
	public ProductKM() {

	}
}
