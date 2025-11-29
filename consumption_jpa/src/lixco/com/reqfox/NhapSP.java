package lixco.com.reqfox;

import java.util.Date;

public class NhapSP {
	private long id;
	private String soct;
	private String mact;
	private Date ngay;
	private String makh;
	private String makho;
	private String maxn;
	private String masp;
	private double soluong;
	private double dongia;
	private double sotien;
	private String sohd;
	private boolean locked;
	private String idnhanh;
	private String ckeynhanh;
	private String soxe;
	private String malohang;
	private String lydonkho;
	private String soldd;
	private String tkno;
	private String tkco;
	private String sogiaonhan;
	private String idfox;
	
	public NhapSP() {
	}
	public NhapSP(long id,String soct, Date ngay, String makh, String makho, String maxn, String masp,
			double soluong,double dongia,double sotien,String sohd,String soxe, String malohang, String lydonkho, String soldd,String idfox,String sogiaonhan) {
		this.id=id;
		this.soct = soct;
		this.ngay = ngay;
		this.makh = makh;
		this.makho = makho;
		this.maxn = maxn;
		this.masp = masp;
		this.soluong = soluong;
		this.dongia=dongia;
		this.sotien=sotien;
		this.soxe = soxe;
		this.malohang = malohang;
		this.lydonkho = lydonkho;
		this.soldd = soldd;
		this.idfox = idfox;
		this.sohd=sohd;
		this.sogiaonhan=sogiaonhan;
	}

	public String getSoct() {
		return soct;
	}
	public void setSoct(String soct) {
		this.soct = soct;
	}
	public String getMact() {
		return mact;
	}
	public void setMact(String mact) {
		this.mact = mact;
	}
	public Date getNgay() {
		return ngay;
	}
	public void setNgay(Date ngay) {
		this.ngay = ngay;
	}
	public String getMakh() {
		return makh;
	}
	public void setMakh(String makh) {
		this.makh = makh;
	}
	public String getMakho() {
		return makho;
	}
	public void setMakho(String makho) {
		this.makho = makho;
	}
	public String getMaxn() {
		return maxn;
	}
	public void setMaxn(String maxn) {
		this.maxn = maxn;
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
	public String getSohd() {
		return sohd;
	}
	public void setSohd(String sohd) {
		this.sohd = sohd;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIdnhanh() {
		return idnhanh;
	}
	public void setIdnhanh(String idnhanh) {
		this.idnhanh = idnhanh;
	}
	public String getCkeynhanh() {
		return ckeynhanh;
	}
	public void setCkeynhanh(String ckeynhanh) {
		this.ckeynhanh = ckeynhanh;
	}
	public String getSoxe() {
		return soxe;
	}
	public void setSoxe(String soxe) {
		this.soxe = soxe;
	}
	public String getMalohang() {
		return malohang;
	}
	public void setMalohang(String malohang) {
		this.malohang = malohang;
	}
	public String getLydonkho() {
		return lydonkho;
	}
	public void setLydonkho(String lydonkho) {
		this.lydonkho = lydonkho;
	}
	public String getSoldd() {
		return soldd;
	}
	public void setSoldd(String soldd) {
		this.soldd = soldd;
	}
	public String getTkno() {
		return tkno;
	}
	public void setTkno(String tkno) {
		this.tkno = tkno;
	}
	public String getTkco() {
		return tkco;
	}
	public void setTkco(String tkco) {
		this.tkco = tkco;
	}
	public String getSogiaonhan() {
		return sogiaonhan;
	}
	public void setSogiaonhan(String sogiaonhan) {
		this.sogiaonhan = sogiaonhan;
	}
	public String getIdfox() {
		return idfox;
	}
	public void setIdfox(String idfox) {
		this.idfox = idfox;
	}
	
}
