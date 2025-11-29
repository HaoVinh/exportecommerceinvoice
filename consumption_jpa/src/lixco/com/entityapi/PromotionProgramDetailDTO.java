package lixco.com.entityapi;

import lixco.com.entity.PromotionProgramDetail;
import lombok.Data;
@Data
public class PromotionProgramDetailDTO{
	private long id;
	private String masp;//sản phẩm áp dụng chương trình khuyến mãi
	private String maspkm;//sản phẩm được khuyến mãi 
	private double box_quatity;//số lượng thùng mua
	private double specification;//số đơn vị sản phẩm cái này tự nhập vô ví dụ mua 20 chai được tặng 1 chai
	private double promotion_quantity;//số sản phẩm khuyến mãi
	private int promotion_form;//hinhthuckhuyenmai
	
	public PromotionProgramDetailDTO(PromotionProgramDetail ppd) {
		this.masp = ppd.getProduct()!=null?ppd.getProduct().getProduct_code():"";
		this.maspkm = ppd.getPromotion_product()!=null?ppd.getPromotion_product().getProduct_code():"";
		this.box_quatity = ppd.getBox_quatity();
		this.specification = ppd.getSpecification();
		this.promotion_quantity = ppd.getPromotion_quantity();
		this.promotion_form = ppd.getPromotion_form();
	}
	
}
