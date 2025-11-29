package lixco.com.reqfox;

import java.util.Objects;

public class Customer {
	private String masothue;
	private String makh;
	private String tenkh;
	private String diachi;
	private String dienthoai;
	private String fax;
	private ThanhPho thanhpho;
	private String taikhoan;
	private String donvi;
	private LoaiKH loaikh;
	private String masokh;
	private double songayno;
	private String sohieuct;
	private String luu_y;
	private String diadiemgh;
	private double tlkkhich;
	private double tlhoahong;
	private double tiennpp;
	private String masoncc;
	private boolean not_conti;
	private double tlhhong_ntr;
	private boolean khongin;
	private String email;
	private String tenemail;
	
	public Customer() {
	}
	
	public Customer(String masothue, String makh, String tenkh, String diachi, String dienthoai, String fax,
			String matp, String thanhpho,String makhuvuc,String tenkhuvuc,String maquocgia, String tenqgia, String tenqgia_e,
			String taikhoan, String donvi, String kh_categ_id, String kh_category, Long makenh, String masokh, double songayno,
			String luu_y, String diadiemgh, double tlkkhich, double tlhoahong, double tiennpp,
			String masoncc, boolean not_conti, double tlhhong_ntr, boolean khongin, String email) {
		this.masothue = masothue;
		this.makh = makh;
		this.tenkh = tenkh;
		this.diachi = diachi;
		this.dienthoai = dienthoai;
		this.fax = fax;
		if(kh_categ_id!=null && !"".equals(kh_categ_id)){
			String mk=null;
			if(makenh !=null){
				mk=String.format("%02d", makenh);
			}
			this.loaikh=new LoaiKH(kh_categ_id, kh_category, mk);
		}
		if(matp !=null && !"".equals(matp)){
		   this.thanhpho = new ThanhPho();
//		   matp, thanhpho, new KhuVuc(makhuvuc, tenkhuvuc), new QuocGia(maquocgia, tenqgia, tenqgia_e)
		   this.thanhpho.setMatp(matp);
		   this.thanhpho.setThanhpho(thanhpho);
		   if(makhuvuc !=null && !"".equals(makhuvuc)){
			   this.thanhpho.setKhuvuc(new KhuVuc(makhuvuc, tenkhuvuc));
		   }
		   if(maquocgia!=null && !"".equals(maquocgia)){
			   this.thanhpho.setQuocgia(new QuocGia(maquocgia, tenqgia, tenqgia_e));
		   }
		}
		this.taikhoan = taikhoan;
		this.donvi = donvi;
		this.masokh = masokh;
		this.songayno = songayno;
		this.luu_y = luu_y;
		this.diadiemgh = diadiemgh;
		this.tlkkhich = tlkkhich;
		this.tlhoahong = tlhoahong;
		this.tiennpp = tiennpp;
		this.masoncc = masoncc;
		this.not_conti = not_conti;
		this.tlhhong_ntr = tlhhong_ntr;
		this.khongin = khongin;
		this.email = email;
	}

	public static class ThanhPho{
		private String matp;
		private String thanhpho;
		private KhuVuc khuvuc;
		private double dongiavcbg;
		private double dongiavckg;
		private double dongiavcnrc;
		private QuocGia quocgia;
		public ThanhPho() {
		}
		public ThanhPho(String matp, String thanhpho, KhuVuc khuvuc,QuocGia quocgia) {
			this.matp = matp;
			this.thanhpho = thanhpho;
			this.khuvuc = khuvuc;
			this.quocgia = quocgia;
		}
		public String getMatp() {
			return matp;
		}
		public void setMatp(String matp) {
			this.matp = matp;
		}
		public String getThanhpho() {
			return thanhpho;
		}
		public void setThanhpho(String thanhpho) {
			this.thanhpho = thanhpho;
		}
		public KhuVuc getKhuvuc() {
			return khuvuc;
		}
		public void setKhuvuc(KhuVuc khuvuc) {
			this.khuvuc = khuvuc;
		}
		public double getDongiavcbg() {
			return dongiavcbg;
		}
		public void setDongiavcbg(double dongiavcbg) {
			this.dongiavcbg = dongiavcbg;
		}
		public double getDongiavckg() {
			return dongiavckg;
		}
		public void setDongiavckg(double dongiavckg) {
			this.dongiavckg = dongiavckg;
		}
		public double getDongiavcnrc() {
			return dongiavcnrc;
		}
		public void setDongiavcnrc(double dongiavcnrc) {
			this.dongiavcnrc = dongiavcnrc;
		}
		public QuocGia getQuocgia() {
			return quocgia;
		}
		public void setQuocgia(QuocGia quocgia) {
			this.quocgia = quocgia;
		}
		
	}
	public static class KhuVuc{
		private String makhuvuc;
		private String tenkhuvuc;
		public KhuVuc() {
		}
		public KhuVuc(String makhuvuc, String tenkhuvuc) {
			this.makhuvuc = makhuvuc;
			this.tenkhuvuc = tenkhuvuc;
		}
		public String getMakhuvuc() {
			return makhuvuc;
		}
		public void setMakhuvuc(String makhuvuc) {
			this.makhuvuc = makhuvuc;
		}
		public String getTenkhuvuc() {
			return tenkhuvuc;
		}
		public void setTenkhuvuc(String tenkhuvuc) {
			this.tenkhuvuc = tenkhuvuc;
		}
		
	}
	public static class QuocGia{
		private String maquocgia;
		private String tenqgia;
		private String tenqgia_e;
		public QuocGia() {
		}
		
		public QuocGia(String maquocgia, String tenqgia, String tenqgia_e) {
			this.maquocgia = maquocgia;
			this.tenqgia = tenqgia;
			this.tenqgia_e = tenqgia_e;
		}

		public String getMaquocgia() {
			return maquocgia;
		}
		public void setMaquocgia(String maquocgia) {
			this.maquocgia = maquocgia;
		}
		public String getTenqgia() {
			return tenqgia;
		}
		public void setTenqgia(String tenqgia) {
			this.tenqgia = tenqgia;
		}
		public String getTenqgia_e() {
			return tenqgia_e;
		}
		public void setTenqgia_e(String tenqgia_e) {
			this.tenqgia_e = tenqgia_e;
		}
		
	}
	public static class LoaiKH{
		private String kh_categ_id;
		private String kh_category;
		private String makenh;
		
		public LoaiKH() {
		}
		
		public LoaiKH(String kh_categ_id, String kh_category, String makenh) {
			this.kh_categ_id = kh_categ_id;
			this.kh_category = kh_category;
			this.makenh = makenh;
		}
		public String getKh_categ_id() {
			return kh_categ_id;
		}
		public void setKh_categ_id(String kh_categ_id) {
			this.kh_categ_id = kh_categ_id;
		}
		public String getKh_category() {
			return kh_category;
		}
		public void setKh_category(String kh_category) {
			this.kh_category = kh_category;
		}
		public String getMakenh() {
			return makenh;
		}
		public void setMakenh(String makenh) {
			this.makenh = makenh;
		}
		
	}
	public String getMasothue() {
		return masothue;
	}
	public void setMasothue(String masothue) {
		this.masothue = masothue;
	}
	public String getMakh() {
		return makh;
	}
	public void setMakh(String makh) {
		this.makh = makh;
	}
	public String getTenkh() {
		return tenkh;
	}
	public void setTenkh(String tenkh) {
		this.tenkh = tenkh;
	}
	public String getDiachi() {
		return diachi;
	}
	public void setDiachi(String diachi) {
		this.diachi = diachi;
	}
	public String getDienthoai() {
		return dienthoai;
	}
	public void setDienthoai(String dienthoai) {
		this.dienthoai = dienthoai;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public ThanhPho getThanhpho() {
		return thanhpho;
	}
	public void setThanhpho(ThanhPho thanhpho) {
		this.thanhpho = thanhpho;
	}
	public String getTaikhoan() {
		return taikhoan;
	}
	public void setTaikhoan(String taikhoan) {
		this.taikhoan = taikhoan;
	}
	public String getDonvi() {
		return donvi;
	}
	public void setDonvi(String donvi) {
		this.donvi = donvi;
	}
	public LoaiKH getLoaikh() {
		return loaikh;
	}
	public void setLoaikh(LoaiKH loaikh) {
		this.loaikh = loaikh;
	}
	public String getMasokh() {
		return masokh;
	}
	public void setMasokh(String masokh) {
		this.masokh = masokh;
	}
	public double getSongayno() {
		return songayno;
	}
	public void setSongayno(double songayno) {
		this.songayno = songayno;
	}
	public String getSohieuct() {
		return sohieuct;
	}
	public void setSohieuct(String sohieuct) {
		this.sohieuct = sohieuct;
	}
	public String getLuu_y() {
		return luu_y;
	}
	public void setLuu_y(String luu_y) {
		this.luu_y = luu_y;
	}
	public String getDiadiemgh() {
		return diadiemgh;
	}
	public void setDiadiemgh(String diadiemgh) {
		this.diadiemgh = diadiemgh;
	}
	public double getTlkkhich() {
		return tlkkhich;
	}
	public void setTlkkhich(double tlkkhich) {
		this.tlkkhich = tlkkhich;
	}
	public double getTlhoahong() {
		return tlhoahong;
	}
	public void setTlhoahong(double tlhoahong) {
		this.tlhoahong = tlhoahong;
	}
	public double getTiennpp() {
		return tiennpp;
	}
	public void setTiennpp(double tiennpp) {
		this.tiennpp = tiennpp;
	}
	public String getMasoncc() {
		return masoncc;
	}
	public void setMasoncc(String masoncc) {
		this.masoncc = masoncc;
	}
	public boolean isNot_conti() {
		return not_conti;
	}
	public void setNot_conti(boolean not_conti) {
		this.not_conti = not_conti;
	}
	public double getTlhhong_ntr() {
		return tlhhong_ntr;
	}
	public void setTlhhong_ntr(double tlhhong_ntr) {
		this.tlhhong_ntr = tlhhong_ntr;
	}
	public boolean isKhongin() {
		return khongin;
	}
	public void setKhongin(boolean khongin) {
		this.khongin = khongin;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTenemail() {
		return tenemail;
	}
	public void setTenemail(String tenemail) {
		this.tenemail = tenemail;
	}
}
