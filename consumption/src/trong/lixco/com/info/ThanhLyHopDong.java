package trong.lixco.com.info;

import lombok.Data;
@Data
public class ThanhLyHopDong {
	String masp;
	String tensp;
	double soluong;
	double dongia;
	double thanhtien;
	String dvt;
	public ThanhLyHopDong(String masp, String tensp, double soluong, double dongia, double thanhtien,String dvt) {
		super();
		this.masp = masp;
		this.tensp = tensp;
		this.soluong = soluong;
		this.dongia = dongia;
		this.thanhtien = thanhtien;
		this.dvt = dvt;
	}
	
}
