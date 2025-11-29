package lixco.com.reqfox;

import java.util.Date;
import java.util.List;

public class Invoice {
	private long id;
	private String maxn;
	private String soct;
	private String makh;
	private Date ngay;
	private String makho;
	private String soxe;
	private double hesothue;
	private String htthanhtoa;
	private double tienthue;
	private String sohdong;
	private int cid;
	private boolean dathtoan;
	private boolean locked;
	private String sohd;
	private String soserie;
	private String sodhang;
	private String manvbh;
	private String manoivc;
	private String port_id;
	private String matk;
	private String mabx;
	private String htbocxep;
	private boolean ngoaigio;
	private String masoctkm;
	private String manvvc;
	private String noidungvc;
	private int so_cont40;
	private int so_cont20;
	private String masoctdg;
	private String iddonhang;
	private String soldd;
	private Date ngaygh;
	private String quydoi;
	private String ghichu;
	private Date ngayttoan;
	private String po;
	private int tglenhang;
	private boolean no;
	private String sohdvcnb;
	private Date ngaypn;
	private String idbd;
	private String guid;
	private int capnhat;
	private String matracuu;
	private String idfox;
	private List<InvoiceDetail> listInvoiceDetail;
	
	
	public Invoice() {
	}
	public Invoice(long id, String maxn, String soct, String makh, Date ngay, String makho, String soxe,
			double hesothue, String htthanhtoa, boolean dathtoan, String sohd, String soserie, String sodhang,
			String manoivc, String port_id,String matk, String mabx, String htbocxep, boolean ngoaigio, String masoctkm, String manvvc,
			String noidungvc, String masoctdg, String iddonhang, String soldd,
			Date ngaygh, String quydoi, String ghichu, String po, int tglenhang,String guid, int capnhat, String matracuu, String idfox) {
		this.id = id;
		this.maxn = maxn;
		this.soct = soct;
		this.makh = makh;
		this.ngay = ngay;
		this.makho = makho;
		this.soxe = soxe;
		this.hesothue = hesothue;
		this.htthanhtoa = htthanhtoa;
		this.dathtoan = dathtoan;
		this.sohd = sohd;
		this.soserie = soserie;
		this.sodhang = sodhang;
		this.manoivc = manoivc;
		this.port_id = port_id;
		this.matk = matk;
		this.mabx = mabx;
		this.htbocxep = htbocxep;
		this.ngoaigio = ngoaigio;
		this.masoctkm = masoctkm;
		this.manvvc = manvvc;
		this.noidungvc = noidungvc;
		this.masoctdg = masoctdg;
		this.iddonhang = iddonhang;
		this.soldd = soldd;
		this.ngaygh = ngaygh;
		this.quydoi = quydoi;
		this.ghichu = ghichu;
		this.po = po;
		this.tglenhang = tglenhang;
		this.guid = guid;
		this.capnhat = capnhat;
		this.matracuu = matracuu;
		this.idfox = idfox;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMaxn() {
		return maxn;
	}
	public void setMaxn(String maxn) {
		this.maxn = maxn;
	}
	public String getSoct() {
		return soct;
	}
	public void setSoct(String soct) {
		this.soct = soct;
	}
	public String getMakh() {
		return makh;
	}
	public void setMakh(String makh) {
		this.makh = makh;
	}
	public Date getNgay() {
		return ngay;
	}
	public void setNgay(Date ngay) {
		this.ngay = ngay;
	}
	public String getMakho() {
		return makho;
	}
	public void setMakho(String makho) {
		this.makho = makho;
	}
	public String getSoxe() {
		return soxe;
	}
	public void setSoxe(String soxe) {
		this.soxe = soxe;
	}
	public double getHesothue() {
		return hesothue;
	}
	public void setHesothue(double hesothue) {
		this.hesothue = hesothue;
	}
	public String getHtthanhtoa() {
		return htthanhtoa;
	}
	public void setHtthanhtoa(String htthanhtoa) {
		this.htthanhtoa = htthanhtoa;
	}
	public double getTienthue() {
		return tienthue;
	}
	public void setTienthue(double tienthue) {
		this.tienthue = tienthue;
	}
	public String getSohdong() {
		return sohdong;
	}
	public void setSohdong(String sohdong) {
		this.sohdong = sohdong;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public boolean isDathtoan() {
		return dathtoan;
	}
	public void setDathtoan(boolean dathtoan) {
		this.dathtoan = dathtoan;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public String getSohd() {
		return sohd;
	}
	public void setSohd(String sohd) {
		this.sohd = sohd;
	}
	public String getSoserie() {
		return soserie;
	}
	public void setSoserie(String soserie) {
		this.soserie = soserie;
	}
	public String getSodhang() {
		return sodhang;
	}
	public void setSodhang(String sodhang) {
		this.sodhang = sodhang;
	}
	public String getManvbh() {
		return manvbh;
	}
	public void setManvbh(String manvbh) {
		this.manvbh = manvbh;
	}
	public String getManoivc() {
		return manoivc;
	}
	public void setManoivc(String manoivc) {
		this.manoivc = manoivc;
	}
	public String getPort_id() {
		return port_id;
	}
	public void setPort_id(String port_id) {
		this.port_id = port_id;
	}
	public String getMatk() {
		return matk;
	}
	public void setMatk(String matk) {
		this.matk = matk;
	}
	public String getMabx() {
		return mabx;
	}
	public void setMabx(String mabx) {
		this.mabx = mabx;
	}
	public String getHtbocxep() {
		return htbocxep;
	}
	public void setHtbocxep(String htbocxep) {
		this.htbocxep = htbocxep;
	}
	public boolean isNgoaigio() {
		return ngoaigio;
	}
	public void setNgoaigio(boolean ngoaigio) {
		this.ngoaigio = ngoaigio;
	}
	public String getMasoctkm() {
		return masoctkm;
	}
	public void setMasoctkm(String masoctkm) {
		this.masoctkm = masoctkm;
	}
	public String getManvvc() {
		return manvvc;
	}
	public void setManvvc(String manvvc) {
		this.manvvc = manvvc;
	}
	public String getNoidungvc() {
		return noidungvc;
	}
	public void setNoidungvc(String noidungvc) {
		this.noidungvc = noidungvc;
	}
	public int getSo_cont40() {
		return so_cont40;
	}
	public void setSo_cont40(int so_cont40) {
		this.so_cont40 = so_cont40;
	}
	public int getSo_cont20() {
		return so_cont20;
	}
	public void setSo_cont20(int so_cont20) {
		this.so_cont20 = so_cont20;
	}
	public String getMasoctdg() {
		return masoctdg;
	}
	public void setMasoctdg(String masoctdg) {
		this.masoctdg = masoctdg;
	}
	public String getIddonhang() {
		return iddonhang;
	}
	public void setIddonhang(String iddonhang) {
		this.iddonhang = iddonhang;
	}
	public String getSoldd() {
		return soldd;
	}
	public void setSoldd(String soldd) {
		this.soldd = soldd;
	}
	public Date getNgaygh() {
		return ngaygh;
	}
	public void setNgaygh(Date ngaygh) {
		this.ngaygh = ngaygh;
	}
	public String getQuydoi() {
		return quydoi;
	}
	public void setQuydoi(String quydoi) {
		this.quydoi = quydoi;
	}
	public String getGhichu() {
		return ghichu;
	}
	public void setGhichu(String ghichu) {
		this.ghichu = ghichu;
	}
	public Date getNgayttoan() {
		return ngayttoan;
	}
	public void setNgayttoan(Date ngayttoan) {
		this.ngayttoan = ngayttoan;
	}
	public String getPo() {
		return po;
	}
	public void setPo(String po) {
		this.po = po;
	}
	public int getTglenhang() {
		return tglenhang;
	}
	public void setTglenhang(int tglenhang) {
		this.tglenhang = tglenhang;
	}
	public boolean isNo() {
		return no;
	}
	public void setNo(boolean no) {
		this.no = no;
	}
	public String getSohdvcnb() {
		return sohdvcnb;
	}
	public void setSohdvcnb(String sohdvcnb) {
		this.sohdvcnb = sohdvcnb;
	}
	public Date getNgaypn() {
		return ngaypn;
	}
	public void setNgaypn(Date ngaypn) {
		this.ngaypn = ngaypn;
	}
	public String getIdbd() {
		return idbd;
	}
	public void setIdbd(String idbd) {
		this.idbd = idbd;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public int getCapnhat() {
		return capnhat;
	}
	public void setCapnhat(int capnhat) {
		this.capnhat = capnhat;
	}
	public String getMatracuu() {
		return matracuu;
	}
	public void setMatracuu(String matracuu) {
		this.matracuu = matracuu;
	}
	public String getIdfox() {
		return idfox;
	}
	public void setIdfox(String idfox) {
		this.idfox = idfox;
	}
	public List<InvoiceDetail> getListInvoiceDetail() {
		return listInvoiceDetail;
	}
	public void setListInvoiceDetail(List<InvoiceDetail> listInvoiceDetail) {
		this.listInvoiceDetail = listInvoiceDetail;
	}
}
