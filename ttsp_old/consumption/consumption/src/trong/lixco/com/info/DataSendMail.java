package trong.lixco.com.info;

import lombok.Data;
@Data
public class DataSendMail {
	String title;
	String param1;
	String param2;
	String param3;
	public DataSendMail(String title, String param1, String param2, String param3) {
		super();
		this.title = title;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
	}
	
}
