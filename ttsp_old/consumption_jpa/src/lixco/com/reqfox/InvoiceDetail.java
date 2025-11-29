package lixco.com.reqfox;

public class InvoiceDetail {
	private long id;
	private String masp;
	private double soluong;
	private double sothung;
	private double dongia;
	private double sotien;
	private String maso;
	private double thucxuatn;
	private String ckey;
	private String cid;
	private String malohang;
	private String maspdh;
	private double dongiant;
	private double sotiennt;
	private String ckey_hdbh;
	private String ckeybd;
	private String guid;
	private String idfox;
	private String id_parent_fox;
	
	public InvoiceDetail() {
	}
	
	public InvoiceDetail(long id, String masp, double soluong,double sothung, double dongia, double sotien, String maso,
			double thucxuatn, String malohang, String maspdh, double dongiant, double sotiennt, String guid, String idfox, String id_parent_fox) {
		this.id = id;
		this.masp = masp;
		this.soluong = soluong;
		this.sothung=sothung;
		this.dongia = dongia;
		this.sotien = sotien;
		this.maso = maso;
		this.thucxuatn = thucxuatn;
		this.malohang = malohang;
		this.maspdh = maspdh;
		this.dongiant = dongiant;
		this.sotiennt = sotiennt;
		this.guid = guid;
		this.idfox = idfox;
		this.id_parent_fox = id_parent_fox;
	}
	public InvoiceDetail(long id, String masp, double soluong, double dongia, double sotien, String maso,
			double thucxuatn, String malohang, String maspdh, double dongiant, double sotiennt, String guid, String idfox, String id_parent_fox) {
		this.id = id;
		this.masp = masp;
		this.soluong = soluong;
		this.dongia = dongia;
		this.sotien = sotien;
		this.maso = maso;
		this.thucxuatn = thucxuatn;
		this.malohang = malohang;
		this.maspdh = maspdh;
		this.dongiant = dongiant;
		this.sotiennt = sotiennt;
		this.guid = guid;
		this.idfox = idfox;
		this.id_parent_fox = id_parent_fox;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
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
	public double getSothung() {
		return sothung;
	}
	public void setSothung(double sothung) {
		this.sothung = sothung;
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
	public String getMaso() {
		return maso;
	}
	public void setMaso(String maso) {
		this.maso = maso;
	}
	public double getThucxuatn() {
		return thucxuatn;
	}
	public void setThucxuatn(double thucxuatn) {
		this.thucxuatn = thucxuatn;
	}
	public String getCkey() {
		return ckey;
	}
	public void setCkey(String ckey) {
		this.ckey = ckey;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getMalohang() {
		return malohang;
	}
	public void setMalohang(String malohang) {
		this.malohang = malohang;
	}
	public String getMaspdh() {
		return maspdh;
	}
	public void setMaspdh(String maspdh) {
		this.maspdh = maspdh;
	}
	public double getDongiant() {
		return dongiant;
	}
	public void setDongiant(double dongiant) {
		this.dongiant = dongiant;
	}
	public double getSotiennt() {
		return sotiennt;
	}
	public void setSotiennt(double sotiennt) {
		this.sotiennt = sotiennt;
	}
	public String getCkey_hdbh() {
		return ckey_hdbh;
	}
	public void setCkey_hdbh(String ckey_hdbh) {
		this.ckey_hdbh = ckey_hdbh;
	}
	public String getCkeybd() {
		return ckeybd;
	}
	public void setCkeybd(String ckeybd) {
		this.ckeybd = ckeybd;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getIdfox() {
		return idfox;
	}
	public void setIdfox(String idfox) {
		this.idfox = idfox;
	}
	public String getId_parent_fox() {
		return id_parent_fox;
	}
	public void setId_parent_fox(String id_parent_fox) {
		this.id_parent_fox = id_parent_fox;
	}
}
