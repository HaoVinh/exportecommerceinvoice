package lixco.com.reportInfo;

import java.util.Date;

import lombok.Data;
@Data
public class SoDuCongNo {
	private String customer_code;
	private String customer_name;
	private Date date;
	private double totalFirst;
	private String voucher_code;
	private double totalNo;
	private String unc_code;
	private double totalCo;
	private double totalFinal;
	private String note;
	
	
	public SoDuCongNo() {
	}

}
