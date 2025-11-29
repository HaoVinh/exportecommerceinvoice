package trong.lixco.com.info;

import lombok.Data;
@Data
public class DuLieuHoaDonVSIP {
	String makh;
	String masp;
	double soluong;
	String malohang;
	String ghichuxuat;
	int nhomtheohd;
	public DuLieuHoaDonVSIP(String makh, String masp, double soluong, String malohang, String ghichuxuat, int nhomtheohd) {
		super();
		this.makh = makh;
		this.masp = masp;
		this.soluong = soluong;
		this.malohang = malohang;
		this.ghichuxuat = ghichuxuat;
		this.nhomtheohd = nhomtheohd;
	}
}
