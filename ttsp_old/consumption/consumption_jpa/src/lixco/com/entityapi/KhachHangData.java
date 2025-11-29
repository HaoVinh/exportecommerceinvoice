package lixco.com.entityapi;

import lombok.Data;

@Data
public class KhachHangData {
	private String makh;
	private String tenkh;
	private String dienthoaiEmail;
	private String diachi;
	public KhachHangData(String makh, String tenkh, String dienthoaiEmail, String diachi) {
		super();
		this.makh = makh;
		this.tenkh = tenkh;
		this.dienthoaiEmail = dienthoaiEmail;
		this.diachi = diachi;
	}
	

}
