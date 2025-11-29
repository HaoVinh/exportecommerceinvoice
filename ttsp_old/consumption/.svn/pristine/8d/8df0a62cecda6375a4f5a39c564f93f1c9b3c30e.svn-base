package lixco.com.entityapi;

import java.util.Date;

import lixco.com.entity.CustomerPromotionProgram;
import lombok.Data;

@Data
public class CustomerPromotionProgramDTO {
	private long id;
	private String customer;
	private Date effective_date;// ngày hiệu lực
	private Date expiry_date;// ngày kết thúc
	private boolean disable;

	public CustomerPromotionProgramDTO(CustomerPromotionProgram cpp) {
		this.id=cpp.getId();
		this.customer = cpp.getCustomer() != null ? cpp.getCustomer().getCustomer_code() : "";
		this.effective_date = cpp.getEffective_date();
		this.expiry_date = cpp.getExpiry_date();
		this.disable = cpp.isDisable();
	}
}
