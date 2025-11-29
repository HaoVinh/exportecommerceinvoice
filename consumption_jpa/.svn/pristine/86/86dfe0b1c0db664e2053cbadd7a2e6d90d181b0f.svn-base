package lixco.com.entityapi;

import lixco.com.entity.CustomerGroup;
import lombok.Data;

@Data
public class CustomerGroupDTO {
	private long id;
	private String code;
	private String name;
	private String note;

	public CustomerGroupDTO(CustomerGroup customerGroup) {
		this.code = customerGroup.getCode();
		this.name = customerGroup.getName();
		this.note = customerGroup.getNote();
	}

}
