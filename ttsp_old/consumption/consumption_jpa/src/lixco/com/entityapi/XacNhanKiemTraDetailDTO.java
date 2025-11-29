package lixco.com.entityapi;

import java.util.Date;

import lombok.Data;

@Data
public class XacNhanKiemTraDetailDTO {
	private static final long serialVersionUID = 1L;
	private long id;
	private Date created_date;
	private String created_by;
	private Date last_modifed_date;
	private String last_modifed_by;
	/*
	 * Sản phẩm
	 */
	private String masp;
	private String tensp;
	private String dvt;
	private String dvt_baobi;
	private double hsquidoithung;

	private double quantityCa1;
	private double quantityCa2;
	private double quantityCa3;
	private double quantityTTSP;
	private double chenhlech;

	private int trangthaicl;
	private String dataPallet;
	private String note;

	public XacNhanKiemTraDetailDTO(long id, String masp, String tensp, String dvt, String dvt_baobi,
			double hsquidoithung, double quantityCa1, double quantityCa2, double quantityCa3, double quantityTTSP,
			double chenhlech, int trangthaicl, String dataPallet, String note) {
		super();
		this.id = id;
		this.masp = masp;
		this.tensp = tensp;
		this.dvt = dvt;
		this.dvt_baobi = dvt_baobi;
		this.hsquidoithung = hsquidoithung;

		this.quantityCa1 = quantityCa1;
		this.quantityCa2 = quantityCa2;
		this.quantityCa3 = quantityCa3;
		this.quantityTTSP = quantityTTSP;
		this.chenhlech = chenhlech;
		this.trangthaicl = trangthaicl;
		this.dataPallet = dataPallet;
		this.note = note;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XacNhanKiemTraDetailDTO other = (XacNhanKiemTraDetailDTO) obj;
		if (other.id == 0) {
			return true;
		}
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public XacNhanKiemTraDetailDTO clone() throws CloneNotSupportedException {
		return (XacNhanKiemTraDetailDTO) super.clone();
	}

}
