package lixco.com.entityapi;

import java.util.Date;

import lixco.com.entity.ProductBrand;
import lombok.Data;

@Data
public class ProductBrandDTO {// sản phẩm brand
	private long id;

	private Date created_date;
	private String created_by;
	private Date last_modifed_date;
	private String last_modifed_by;
	private String pbrand_code;// mã thương hiệu sản phẩm
	private String pbrand_name;// tên thương hiệu sản phẩm
	private String unit;// đơn vị tính
	private boolean disable;

	public ProductBrandDTO(ProductBrand pb) {
		this.created_date = pb.getCreated_date();
		this.created_by = pb.getCreated_by();
		this.last_modifed_date = pb.getLast_modifed_date();
		this.last_modifed_by = pb.getLast_modifed_by();
		this.pbrand_code = pb.getPbrand_code();
		this.pbrand_name = pb.getPbrand_name();
		this.unit = pb.getUnit();
		this.disable = pb.isDisable();
	}

}
