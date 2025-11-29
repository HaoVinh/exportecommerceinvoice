package lixco.com.reqfox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Contract {
	private String id;
	private String sohd;
	private String makh;
	private Date ngaymin;
	private Date ngaymax;
	private boolean thanhly;
	private String dvtien;
	private Date ngaynhaphd;
	private Date etd;
	private List<ContractDetail> list_contract_detail;
	
	public Contract() {
	}
	
	public Contract(String id, String sohd, String makh, Date ngaymin, Date ngaymax, boolean thanhly, String dvtien,
			Date ngaynhaphd, Date etd) {
		super();
		this.id = id;
		this.sohd = sohd;
		this.makh = makh;
		this.ngaymin = ngaymin;
		this.ngaymax = ngaymax;
		this.thanhly = thanhly;
		this.dvtien = dvtien;
		this.ngaynhaphd = ngaynhaphd;
		this.etd = etd;
		this.list_contract_detail=new ArrayList<>();
	}

	public static class ContractDetail{
		private String id;
		private String masp;
		private double soluong;
		private double dongia;
		private double sotien;
		private String ckey;
		private boolean kmai;
		private double loinhuan;
		
		public ContractDetail() {
		}
		
		public ContractDetail(String id, String masp, double soluong, double dongia, double sotien, String ckey,
				boolean kmai, double loinhuan) {
			this.id = id;
			this.masp = masp;
			this.soluong = soluong;
			this.dongia = dongia;
			this.sotien = sotien;
			this.ckey = ckey;
			this.kmai = kmai;
			this.loinhuan = loinhuan;
		}

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getMasp() {
			return masp;
		}
		public void setMasp(String masp) {
			this.masp = masp;
		}
		public double getSoluong() {
			return soluong;
		}
		public void setSoluong(double soluong) {
			this.soluong = soluong;
		}
		public double getDongia() {
			return dongia;
		}
		public void setDongia(double dongia) {
			this.dongia = dongia;
		}
		public double getSotien() {
			return sotien;
		}
		public void setSotien(double sotien) {
			this.sotien = sotien;
		}
		public String getCkey() {
			return ckey;
		}
		public void setCkey(String ckey) {
			this.ckey = ckey;
		}
		public boolean isKmai() {
			return kmai;
		}
		public void setKmai(boolean kmai) {
			this.kmai = kmai;
		}
		public double getLoinhuan() {
			return loinhuan;
		}
		public void setLoinhuan(double loinhuan) {
			this.loinhuan = loinhuan;
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSohd() {
		return sohd;
	}
	public void setSohd(String sohd) {
		this.sohd = sohd;
	}
	public String getMakh() {
		return makh;
	}
	public void setMakh(String makh) {
		this.makh = makh;
	}
	public Date getNgaymin() {
		String str="30/12/1899 00:00:00";
		try{
			DateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date=dateFormat.parse(str);
			if(ngaymin!=null && ngaymin.getTime()==date.getTime()){
				return null;
			}
		}catch(Exception e){
		}
		return ngaymin;
	}
	public void setNgaymin(Date ngaymin) {
		this.ngaymin = ngaymin;
	}
	public Date getNgaymax() {
		String str="30/12/1899 00:00:00";
		try{
			DateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date=dateFormat.parse(str);
			if(ngaymax!=null && ngaymax.getTime()==date.getTime()){
				return null;
			}
		}catch(Exception e){
		}
		return ngaymax;
	}
	public void setNgaymax(Date ngaymax) {
		this.ngaymax = ngaymax;
	}
	public boolean isThanhly() {
		return thanhly;
	}
	public void setThanhly(boolean thanhly) {
		this.thanhly = thanhly;
	}
	public String getDvtien() {
		return dvtien;
	}
	public void setDvtien(String dvtien) {
		this.dvtien = dvtien;
	}
	public Date getNgaynhaphd() {
		String str="30/12/1899 00:00:00";
		try{
			DateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date=dateFormat.parse(str);
			if(ngaynhaphd!=null && ngaynhaphd.getTime()==date.getTime()){
				return null;
			}
		}catch(Exception e){
		}
		return ngaynhaphd;
	}
	public void setNgaynhaphd(Date ngaynhaphd) {
		this.ngaynhaphd = ngaynhaphd;
	}
	public Date getEtd() {
		return etd;
	}
	public void setEtd(Date etd) {
		this.etd = etd;
	}
	public List<ContractDetail> getList_contract_detail() {
		return list_contract_detail;
	}
	public void setList_contract_detail(List<ContractDetail> list_contract_detail) {
		this.list_contract_detail = list_contract_detail;
	}
}
