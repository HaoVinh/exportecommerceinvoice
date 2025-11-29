package lixco.com.entityapi;

import java.util.Date;

import lixco.com.entity.CustomerTypes;
import lombok.Data;

@Data
public class CustomerTypesDTO {
	private long id;
	private String code;
	private String name;
	private String note;
	private String nameCustomer_channel;

	public CustomerTypesDTO(CustomerTypes customerTypes) {
		this.code = customerTypes.getCode();
		this.name = customerTypes.getName();
		this.note = customerTypes.getNote();
		this.nameCustomer_channel = customerTypes.getCustomer_channel() != null ? customerTypes.getCustomer_channel()
				.getName() : "";
	}

}
