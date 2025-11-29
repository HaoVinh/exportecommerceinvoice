package lixco.com.entityapi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lixco.com.entity.PromotionProductGroup;
import lombok.Data;

@Data
public class PromotionProductGroupDTO {
	private long id;
	private String code;
	private String name;
	private String unit;
	private double carton_quantity;
	private String carton_unit;
	
	public PromotionProductGroupDTO(PromotionProductGroup ppg) {
		this.code = ppg.getCode();
		this.name = ppg.getName();
		this.unit = ppg.getUnit();
		this.carton_quantity = ppg.getCarton_quantity();
		this.carton_unit = ppg.getCarton_unit();
	}

	
}
