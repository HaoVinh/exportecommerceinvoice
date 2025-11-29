package lixco.com.entityapi;

import java.util.Date;
import java.util.List;

import lixco.com.entity.Product;
import lixco.com.entity.ProductKM;
import lombok.Data;

@Data
public class ProductDTO {// sản phẩm
	private long id;
	private String product_code;
	private Date created_date;
	private String created_by;
	private Date last_modifed_date;
	private String last_modifed_by;
	private String product_name;// tên sản phẩm
	private String en_name;// tên tiếng anh
	private String customs_name;// tên hải quan
	private String product_info; // thông tin sản phẩm
	private String unit;// đơn vị tính
	private String lever_code;// mã lever
	private double specification; // qui cách đóng gói.
	private double box_quantity;// số lượng thùng/pallet
	private double factor;// hệ số chuyển đổi
	private double tare;// trong luong bb thuc te
	private String packing_unit;// đơn vị đóng gói
	private boolean typep;// xuất khẩu hay nội đia
	private String other_code;// mã khác
	private double reserve_quantity;// số lượng dự trữ
	private String product_com;// loại sản phẩm
	private String promotion_product;// Sản phẩm khuyến mãi
	private boolean disable;// không còn sử dụng
	private String product_type;
	private String product_group;
	private String promotion_product_group;// nhóm sản phẩm khuyến mãi.

	private String maspchinh;
	private String maspcu;
	private String tensp_com;
	private String ghichudongbo;
	
	private List<ProductKMDTO> productKMDTOs;

	public ProductDTO(Product p) {
		this.id=p.getId();
		this.product_code = p.getProduct_code();
		this.created_date = p.getCreated_date();
		this.created_by = p.getCreated_by();
		this.last_modifed_date = p.getLast_modifed_date();
		this.last_modifed_by = p.getLast_modifed_by();
		this.product_name = p.getProduct_name();
		this.en_name = p.getEn_name();
		this.customs_name = p.getCustoms_name();
		this.product_info = p.getProduct_info();
		this.unit = p.getUnit();
		this.lever_code = p.getLever_code();
		this.specification = p.getSpecification();
		this.box_quantity = p.getBox_quantity();
		this.factor = p.getFactor();
		this.tare = p.getTare();
		this.packing_unit = p.getPacking_unit();
		this.typep = p.isTypep();
		this.other_code = p.getOther_code();
		this.reserve_quantity = p.getReserve_quantity();
		this.product_com = p.getProduct_com() != null ? p.getProduct_com().getPcom_code() : null;
		this.promotion_product = p.getPromotion_product() != null ? p.getPromotion_product().getProduct_code() : null;
		this.disable = p.isDisable();// không còn sử dụng
		this.product_type = p.getProduct_type() != null ? p.getProduct_type().getCode() : null;
		this.product_group = p.getProduct_group() != null ? p.getProduct_group().getCode() : null;
		this.promotion_product_group = p.getPromotion_product_group() != null ? p.getPromotion_product_group()
				.getCode() : null;

		this.maspchinh = p.getMaspchinh();
		this.maspcu = p.getMaspcu();
		this.tensp_com = p.getTensp_com();
		this.ghichudongbo = p.getGhichudongbo();

	}
}
