package lixco.com.entityapi;

import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
public class YeuCauKiemTraHangDTO {
	private long id;
	private String createdBy;
	private String requestCode;
	private Date requestDate;
	private String note;
	private List<YeuCauKiemTraHangDetailDTO> yeuCauKiemTraHangDetailDTOs;

	/*
	 * TP.Kho van xac nhan
	 */
	private boolean tpxacnhan;
	private Date tpxacnhanDate;
	private String tpxacnhanName;
	/*
	 * KCS kiem tra
	 */
	private boolean kcsdatiepnhan;
	private Date ngaytiepnhan;
	private String nguoitiepnhan;

	private boolean dakiemtra;
	private Date ngayKiemTra;
	private String createdCheck;

	private boolean tpkcsxacnhankq;
	private Date tpkcsxacnhankqDate;
	private String tpkcsxacnhankqName;

	/*
	 * BLĐ xác nhận
	 */
	private boolean bldxacnhan;
	private Date bldxacnhanDate;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YeuCauKiemTraHangDTO other = (YeuCauKiemTraHangDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public YeuCauKiemTraHangDTO clone() throws CloneNotSupportedException {
		return (YeuCauKiemTraHangDTO) super.clone();
	}

	public YeuCauKiemTraHangDTO(long id, String createdBy, String requestCode, Date requestDate, String note,
			boolean tpxacnhan, Date tpxacnhanDate, String tpxacnhanName, boolean kcsdatiepnhan, Date ngaytiepnhan,
			String nguoitiepnhan, boolean dakiemtra, Date ngayKiemTra, String createdCheck, boolean bldxacnhan,
			Date bldxacnhanDate, boolean tpkcsxacnhankq, Date tpkcsxacnhankqDate, String tpkcsxacnhankqName) {
		super();
		this.id = id;
		this.createdBy = createdBy;
		this.requestCode = requestCode;
		this.requestDate = requestDate;
		this.note = note;
		this.tpxacnhan = tpxacnhan;
		this.tpxacnhanDate = tpxacnhanDate;
		this.tpxacnhanName = tpxacnhanName;
		this.kcsdatiepnhan = kcsdatiepnhan;
		this.ngaytiepnhan = ngaytiepnhan;
		this.nguoitiepnhan = nguoitiepnhan;
		this.dakiemtra = dakiemtra;
		this.ngayKiemTra = ngayKiemTra;
		this.createdCheck = createdCheck;
		this.bldxacnhan = bldxacnhan;
		this.bldxacnhanDate = bldxacnhanDate;
		this.tpkcsxacnhankq=tpkcsxacnhankq;
		this.tpkcsxacnhankqDate=tpkcsxacnhankqDate;
		this.tpkcsxacnhankqName=tpkcsxacnhankqName;
	}

}
