package lixco.com.entityapi;

import lixco.com.entity.DeliveryPricing;
import lombok.Data;

@Data
public class DeliveryPricingDTO {
	private long id;
	private String makh;// khánh hàng
	private String place_code;// mã nơi
	private String address;// địa chỉ
	private double km;// số km
	private double unit_price;// đơn giá
	private double unit_priceun;// đơn giá không sử dụng
	private String place_arrived;// nơi đến
	private boolean disable;// không sử dụng

	public DeliveryPricingDTO(DeliveryPricing deliveryPricing) {
		this.id = deliveryPricing.getId();
		this.makh = deliveryPricing.getCustomer() != null ? deliveryPricing.getCustomer().getCustomer_code() : "";
		this.place_code = deliveryPricing.getPlace_code();
		this.address = deliveryPricing.getAddress();
		this.km = deliveryPricing.getKm();
		this.unit_price = deliveryPricing.getUnit_price();
		this.unit_priceun = deliveryPricing.getUnit_priceun();
		this.place_arrived = deliveryPricing.getPlace_arrived();
		this.disable = deliveryPricing.isDisable();
	}
}
