package trong.lixco.com.info;

import lombok.Data;
@Data
public class DuLieuHoaDonChietKhau {
	String masp;
	double soluong;
	double dongia;
	double thanhtien;
	String makh;
	String maxuatnhap;
	String htthanhtoan;
	String manguoivanchuyen;
	public DuLieuHoaDonChietKhau(String masp, double soluong, double dongia, double thanhtien, String makh,
			String maxuatnhap, String htthanhtoan, String manguoivanchuyen) {
		super();
		this.masp = masp;
		this.soluong = soluong;
		this.dongia = dongia;
		this.thanhtien = thanhtien;
		this.makh = makh;
		this.maxuatnhap = maxuatnhap;
		this.htthanhtoan = htthanhtoan;
		this.manguoivanchuyen = manguoivanchuyen;
	}
}
