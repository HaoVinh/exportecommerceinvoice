package trong.lixco.com.info;

import java.util.Date;

import lombok.Data;
@Data
public class DuLieuHoaDonVSIP {
	Date ngay;
	String sochungtu;
	String makh;
	String masp;
	double soluong;
	String malohang;
	String ghichuxuat;
	int nhomtheohd;
	double slpallet;
	String PO;
	public DuLieuHoaDonVSIP(Date ngay,
	String sochungtu,String makh, String masp, double soluong, String malohang, String ghichuxuat, int nhomtheohd, double slpallet) {
		super();
		this.ngay=ngay;
		this.sochungtu=sochungtu;
		this.makh = makh;
		this.masp = masp;
		this.soluong = soluong;
		this.malohang = malohang;
		this.ghichuxuat = ghichuxuat;
		this.nhomtheohd = nhomtheohd;
		this.slpallet=slpallet;
	}
}
