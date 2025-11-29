package lixco.com.entityapi;

import lixco.com.entity.PricingProgramDetail;
import lombok.Data;

@Data
public class PricingProgramDetailDTO {
	private double unit_price;// đơn giá,
	private double quantity;// số lượng
	private String productCode;// sản phẩm
	private double revenue_per_ton; // lợi nhuận mỗi tấn.
	private String pricing_program_code;// chương trình định giá

	public PricingProgramDetailDTO(PricingProgramDetail ppd) {
		this.unit_price = ppd.getUnit_price();
		this.quantity = ppd.getQuantity();
		this.productCode = ppd.getProduct() != null ? ppd.getProduct().getProduct_code() : "";
		this.revenue_per_ton = ppd.getRevenue_per_ton();
	}
}
