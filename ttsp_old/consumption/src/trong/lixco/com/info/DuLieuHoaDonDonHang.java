package trong.lixco.com.info;

import java.util.Date;

import lombok.Data;

@Data
public class DuLieuHoaDonDonHang {
	Date ngayhoadon = null;
	double soluongthung = 0;
	String masp;
	long iddonhangchitiet;
	long iddonhang;
	String soxe;
	String sohoadon;
	String lohang;

	public DuLieuHoaDonDonHang(Date ngayhoadon, double soluongthung, String masp, long iddonhangchitiet,
			long iddonhang, String soxe, String sohoadon, String lohang) {
		super();
		this.ngayhoadon = ngayhoadon;
		this.soluongthung = soluongthung;
		this.masp = masp;
		this.iddonhangchitiet = iddonhangchitiet;
		this.iddonhang = iddonhang;
		this.soxe = soxe;
		this.sohoadon = sohoadon;
		this.lohang = lohang;
	}

}
