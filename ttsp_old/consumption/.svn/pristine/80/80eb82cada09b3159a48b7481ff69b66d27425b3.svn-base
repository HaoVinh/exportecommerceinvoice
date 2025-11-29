package lixco.com.entityapi;

import java.util.Date;

import lixco.com.entity.ProductCom;
import lombok.Data;

@Data
public class ProductComDTO{// loại sản phẩm
	private Date created_date;
	private String created_by;
	private Date last_modifed_date;
	private String last_modifed_by;
	private String pcom_code;// mã danh mục sản phẩm
	private String pcom_name;//tên danh mục sản phẩm
	private String unit;// đơn vị tính
	private String product_brand;// Danh mục brand
	private boolean disable;
	
	public ProductComDTO(ProductCom pc) {
		this.created_date = pc.getCreated_date();
		this.created_by = pc.getCreated_by();
		this.last_modifed_date = pc.getLast_modifed_date();
		this.last_modifed_by = pc.getLast_modifed_by();
		this.pcom_code = pc.getPcom_code();
		this.pcom_name = pc.getPcom_name();
		this.unit = pc.getUnit();
		this.product_brand = pc.getProduct_brand()!=null?pc.getProduct_brand().getPbrand_code():"";
		this.disable = pc.isDisable();
	}
}
