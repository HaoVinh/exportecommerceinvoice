package lixco.com.entityapi;

import java.util.Date;

import lombok.Data;

@Data
public class YeuCauKiemTraHangDetailDTO {
	private long id;
	/*
	 * Sản phẩm
	 */
	private String masp;
	private String tensp;
	private String dvt;
	private String dvt_baobi;
	private double hsquidoithung;

	private double quantity;// số lượng dvt
	private String lohang;
	private int nguongoc;
	private String tinhtrang;

	private boolean kiemtradat;
	private String tieuchuan;
	private String nguyennhan;
	private String huonggiaiquyet;
	private String huonggiaiquyet2;
	private Date giahanluukho;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YeuCauKiemTraHangDetailDTO other = (YeuCauKiemTraHangDetailDTO) obj;
		if (other.id == 0) {
			return true;
		}
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public YeuCauKiemTraHangDetailDTO clone() throws CloneNotSupportedException {
		return (YeuCauKiemTraHangDetailDTO) super.clone();
	}

	public YeuCauKiemTraHangDetailDTO(long id, String masp, String tensp, String dvt, String dvt_baobi,
			double hsquidoithung, double quantity, String lohang, int nguongoc, String tinhtrang, boolean kiemtradat,
			String tieuchuan, String nguyennhan, String huonggiaiquyet, Date giahanluukho,String huonggiaiquyet2) {
		super();
		this.id = id;
		this.masp = masp;
		this.tensp = tensp;
		this.dvt = dvt;
		this.dvt_baobi = dvt_baobi;
		this.hsquidoithung = hsquidoithung;
		this.quantity = quantity;
		this.lohang = lohang;
		this.nguongoc = nguongoc;
		this.tinhtrang = tinhtrang;

		this.kiemtradat = kiemtradat;
		this.tieuchuan = tieuchuan;
		this.nguyennhan = nguyennhan;
		this.huonggiaiquyet = huonggiaiquyet;
		this.giahanluukho = giahanluukho;
		this.huonggiaiquyet2=huonggiaiquyet2;
	}

}
