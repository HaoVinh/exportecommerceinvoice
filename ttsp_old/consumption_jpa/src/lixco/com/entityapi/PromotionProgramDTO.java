package lixco.com.entityapi;

import java.util.Date;
import java.util.List;

import lixco.com.entity.PromotionProgram;
import lombok.Data;
@Data
public class PromotionProgramDTO{
	private long id;
	private String program_code;// mã chương trình
	private String voucher_code;// mã chứng từ
	private Date effective_date;// ngày hiệu lực
	private Date expiry_date;// ngày kết thúc
	private boolean disable;// không sử dụng
	private String note;
	private List<PromotionProgramDetailDTO> programDetailDTOs;
	private List<CustomerPromotionProgramDTO> customerPromotionProgramDTOs;
	
	public PromotionProgramDTO(PromotionProgram pp) {
		this.id=pp.getId();
        this.program_code = pp.getProgram_code();
        this.voucher_code = pp.getVoucher_code();
        this.effective_date = pp.getEffective_date();
        this.expiry_date = pp.getExpiry_date();
        this.disable = pp.isDisable();
        this.note = pp.getNote();
       
    }

}
