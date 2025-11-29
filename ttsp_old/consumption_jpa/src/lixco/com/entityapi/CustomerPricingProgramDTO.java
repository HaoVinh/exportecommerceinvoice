package lixco.com.entityapi;

import java.util.Date;

import lixco.com.entity.CustomerPricingProgram;
import lombok.Data;
@Data
public class CustomerPricingProgramDTO{
	private long id;
	private String customerCode;//khách hàng
	private Date effective_date;//ngày hiệu lực
	private Date expiry_date;//ngày kết thúc
	private boolean disable;
	private String note;
	
	public CustomerPricingProgramDTO(CustomerPricingProgram cpp) {
		this.id = cpp.getId();
		this.customerCode = cpp.getCustomer()!=null?cpp.getCustomer().getCustomer_code():"";
		this.effective_date = cpp.getEffective_date();
		this.expiry_date = cpp.getExpiry_date();
		this.disable = cpp.isDisable();
		this.note = cpp.getNote();
	}
	
	
}
