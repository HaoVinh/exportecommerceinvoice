package trong.lixco.com.info;

import java.util.Date;

import trong.lixco.com.util.MyUtil;
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
	String loainhapxuat;
	
	String maspchinh;
	
	public DuLieuHoaDonOnline(Date ngay, String madonhang, String masp, double dongia, double soluong,
			double thanhtien, double chietkhau, double thanhtientong, String makh,String loainhapxuat,String maspchinh) {
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
		this.loainhapxuat=loainhapxuat;
		this.maspchinh=maspchinh;
	}

	public DuLieuHoaDonOnline(Date ngay, String madonhang, String masp, double dongia, double soluong,
			double thanhtien, double chietkhau, double thanhtientong, String makh, String maxuatnhap, String htthanhtoan,String maspchinh) {
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
		this.maspchinh=maspchinh;
	}

	public String getDonGia_Maspchinh() {
		return maspchinh+"_" + MyUtil.dinhdangso(dongia);
	}

}
