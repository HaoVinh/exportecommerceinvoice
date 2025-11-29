package trong.lixco.com.info;

import java.util.Date;

import lombok.Data;
@Data
public class DuLieuHoaDonOnline {
	Date ngay;;
	String madonhang;
	String masp;
	double dongia;
	double soluong;
	double thanhtien;
	double chietkhau;
	double thanhtientong;
	String makh;
	
	//bo sung thuoc tinh cho nap hoa don mau 1
	String maxuatnhap;
	String htthanhtoan;
	
	public DuLieuHoaDonOnline(Date ngay, String madonhang, String masp, double dongia, double soluong,
			double thanhtien, double chietkhau, double thanhtientong, String makh) {
		super();
		this.ngay = ngay;
		this.madonhang = madonhang;
		this.masp = masp;
		this.dongia = dongia;
		this.soluong = soluong;
		this.thanhtien = thanhtien;
		this.chietkhau = chietkhau;
		this.thanhtientong = thanhtientong;
		this.makh = makh;
	}

	public DuLieuHoaDonOnline(Date ngay, String madonhang, String masp, double dongia, double soluong,
			double thanhtien, double chietkhau, double thanhtientong, String makh, String maxuatnhap, String htthanhtoan) {
		super();
		this.ngay = ngay;
		this.madonhang = madonhang;
		this.masp = masp;
		this.dongia = dongia;
		this.soluong = soluong;
		this.thanhtien = thanhtien;
		this.chietkhau = chietkhau;
		this.thanhtientong = thanhtientong;
		this.makh = makh;
		this.maxuatnhap = maxuatnhap;
		this.htthanhtoan = htthanhtoan;
	}
	
}
