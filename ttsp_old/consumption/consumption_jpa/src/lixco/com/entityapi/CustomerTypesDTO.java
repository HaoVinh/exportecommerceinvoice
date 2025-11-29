package lixco.com.entityapi;

import lixco.com.entity.CustomerTypes;
import lombok.Data;

@Data
public class CustomerTypesDTO {
	private long id;
	private String code;
	private String name;
	private String note;
	private String nameCustomer_channel;
	private String nameCustomer_group;

	public CustomerTypesDTO(CustomerTypes customerTypes) {
		this.code = customerTypes.getCode();
		this.name = customerTypes.getName();
		this.note = customerTypes.getNote();
		this.nameCustomer_channel = customerTypes.getCustomer_channel() != null ? customerTypes.getCustomer_channel()
				.getName() : "";
		this.nameCustomer_group = customerTypes.getCustomerGroup() != null ? customerTypes.getCustomerGroup().getName()
				: "";
	}

}
