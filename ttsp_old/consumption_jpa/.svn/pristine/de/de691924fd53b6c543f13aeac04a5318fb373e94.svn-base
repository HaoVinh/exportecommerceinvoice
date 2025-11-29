package lixco.com.entityapi;

import java.util.Date;
import java.util.List;

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
	 * KCS kiem tra
	 */
	private boolean dakiemtra;
	private Date ngayKiemTra;
	private String createdCheck;

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
			boolean dakiemtra, Date ngayKiemTra, String createdCheck) {
		super();
		this.id = id;
		this.createdBy = createdBy;
		this.requestCode = requestCode;
		this.requestDate = requestDate;
		this.note = note;
		this.dakiemtra = dakiemtra;
		this.ngayKiemTra = ngayKiemTra;
		this.createdCheck = createdCheck;
	}

}
