package trong.lixco.com.info;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DonHangNPPAPI {
	long iddonhang;
	String madonhang;
	Date ngaydathang;
	Date ngaygiaohang;
	String makhachhang;
	String tenkhachhang;
	boolean daxuat;
	boolean chonphieu;
	String ghichu;
	String manoivc;
	String manppttsp;
	String chinhanh;

	public DonHangNPPAPI(String madonhang, Date ngaydathang, Date ngaygiaohang, String makhachhang,
			String tenkhachhang, boolean daxuat,long iddonhang, String ghichu,String manoivc,String manppttsp) {
		super();
		this.iddonhang=iddonhang;
		this.madonhang = madonhang;
		this.ngaydathang = ngaydathang;
		this.ngaygiaohang = ngaygiaohang;
		this.makhachhang = makhachhang;
		this.tenkhachhang = tenkhachhang;
		this.daxuat = daxuat;
		this.ghichu=ghichu;
		this.manoivc=manoivc;
		this.manppttsp=manppttsp;
	}
	public DonHangNPPAPI(String madonhang, Date ngaydathang, Date ngaygiaohang, String makhachhang,long iddonhang,String ghichu,String manoivc,String manppttsp
			) {
		super();
		this.madonhang = madonhang;
		this.ngaydathang = ngaydathang;
		this.ngaygiaohang = ngaygiaohang;
		this.makhachhang = makhachhang;
		this.iddonhang=iddonhang;
		this.ghichu=ghichu;
		this.manoivc=manoivc;
		this.manppttsp=manppttsp;
	}

	List<DonHangNPPAPIChiTiet> donHangNPPAPIChiTiets;
}
