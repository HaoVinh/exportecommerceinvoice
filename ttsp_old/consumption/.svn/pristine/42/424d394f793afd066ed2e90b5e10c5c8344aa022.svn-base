package lixco.com.entityapi;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class XacNhanKiemTraDTO {
	private long id;
	private Date createdDate;
	private String createdBy;
	
	private long createdById;
	private Date lastModifedDate;
	private String lastModifedBy;
	private String requestCode;
	private Date requestDate;
	private int trangthaicl;
	private String note;
	private List<XacNhanKiemTraDetailDTO> xacNhanKiemTraDetailDTOs;
	/*
	 * KCS kiem tra
	 */
	private boolean dakiemtra;
	private Date ngayKiemTra;
	private String createdCheck;
	
	public XacNhanKiemTraDTO(long id, String createdBy, String requestCode, Date requestDate,int trangthaicl, String note,
			boolean dakiemtra, Date ngayKiemTra, String createdCheck) {
		super();
		this.id = id;
		this.createdBy = createdBy;
		this.requestCode = requestCode;
		this.requestDate = requestDate;
		this.trangthaicl=trangthaicl;
		this.note = note;
		this.dakiemtra = dakiemtra;
		this.ngayKiemTra = ngayKiemTra;
		this.createdCheck = createdCheck;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XacNhanKiemTraDTO other = (XacNhanKiemTraDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public XacNhanKiemTraDTO clone() throws CloneNotSupportedException {
		return (XacNhanKiemTraDTO) super.clone();
	}

}
