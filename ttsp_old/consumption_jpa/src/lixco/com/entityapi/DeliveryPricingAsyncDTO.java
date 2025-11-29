package lixco.com.entityapi;

import java.util.List;

import lombok.Data;
@Data
public class DeliveryPricingAsyncDTO {
	
	List<DeliveryPricingDTO> deliveryPricingDTOs;
	public DeliveryPricingAsyncDTO(List<DeliveryPricingDTO> deliveryPricingDTOs) {
		super();
		this.deliveryPricingDTOs = deliveryPricingDTOs;
		
	}

}
