package lixco.com.entityapi;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceDTO2 {
	private long id;
	private String soct;
	private Date ngay;
	private String makh;
	private String tenkh;
	private String sodh;//so don hang
	private double hesothue;
	private String makho;
	private String maxn;
	private String tenxn;
	private String ghichu;
	private String serie;

	private double tienhang;
	private double tienthue;

	private String sohopdong;
	private String soxe;
	private String sopo;

	private String matracuu;

	private List<InvoiceDetailDTO2> invoiceDetailDTO2s;

	public InvoiceDTO2(long id, String soct, Date ngay, String makh, String tenkh, String sodh, double hesothue,
			String makho, String maxn, String tenxn, String ghichu, String serie, double tienhang, double tienthue,
			String soxe, String sohd, String sopo, String matracuu) {
		super();
		this.id = id;
		this.soct = soct;
		this.ngay = ngay;
		this.makh = makh;
		this.tenkh = tenkh;
		this.sodh = sodh;
		this.hesothue = hesothue;
		this.makho = makho;
		this.maxn = maxn;
		this.tenxn = tenxn;
		this.ghichu = ghichu;
		this.serie = serie;
		this.tienhang = tienhang;
		this.tienthue = tienthue;
		this.sohopdong = sohd;
		this.soxe = soxe;
		this.sopo = sopo;
		this.matracuu=matracuu;
	}

}
