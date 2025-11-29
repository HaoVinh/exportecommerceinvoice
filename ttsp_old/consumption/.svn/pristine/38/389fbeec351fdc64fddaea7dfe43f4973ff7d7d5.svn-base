package lixco.com.entityapi;

import java.util.Date;
import java.util.List;

import lixco.com.entity.PricingProgram;
import lombok.Data;

@Data
public class PricingProgramDTO {

	private long id;
	private String program_code;
	private String voucher_code;// mã chứng từ
	private Date effective_date;// ngày hiệu lực
	private Date expiry_date;// ngày kết thúc
	private String note;// ghi chú
	private boolean disable;// không còn sử dụng
	private String parent_pricing_program_code;// chương trình đơn giá cha
	private Date update_time;
	private boolean capnhat;
	
	private List<PricingProgramDetailDTO> pricingProgramDetailDTOs;
	private List<CustomerPricingProgramDTO> customerPricingProgramDTOs;
	
	public PricingProgramDTO(PricingProgram pp) {
		this.id = pp.getId();
		this.program_code = pp.getProgram_code();
		this.voucher_code = pp.getVoucher_code();
		this.effective_date = pp.getEffective_date();
		this.expiry_date = pp.getExpiry_date();
		this.note = pp.getNote();
		this.disable = pp.isDisable();
		this.parent_pricing_program_code = pp.getParent_pricing_program() != null ? pp.getParent_pricing_program()
				.getProgram_code() : "";
		this.update_time = pp.getUpdate_time();
		this.capnhat = pp.isCapnhat();
	}
	
}
