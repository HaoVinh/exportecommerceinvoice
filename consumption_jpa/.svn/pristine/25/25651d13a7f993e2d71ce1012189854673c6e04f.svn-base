package lixco.com.entityapi;

import java.util.Date;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lixco.com.entity.Product;
import lixco.com.entity.ProductKM;
import lombok.Data;

@Data
public class ProductKMDTO {// sản phẩm

	private long id;

	private Date created_date;
	private String created_by;
	private Date last_modifed_date;
	private String last_modifed_by;
	private String promotion_product;// Sản phẩm khuyến mãi
	private double quantity; // qui cách đóng gói.
	private boolean spchinh;
	private boolean disable;// không còn sử dụng

	public ProductKMDTO(ProductKM pKM) {
		this.created_date = pKM.getCreated_date();
		this.created_by = pKM.getCreated_by();
		this.last_modifed_date = pKM.getLast_modifed_date();
		this.last_modifed_by = pKM.getLast_modifed_by();
		this.promotion_product = pKM.getPromotion_product() != null ? pKM.getPromotion_product().getProduct_code()
				: null;
		this.quantity = pKM.getQuantity();
		this.spchinh = pKM.isSpchinh();
		this.disable = pKM.isDisable();

	}
}
