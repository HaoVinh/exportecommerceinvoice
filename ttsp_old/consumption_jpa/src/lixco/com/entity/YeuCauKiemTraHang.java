package lixco.com.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "yeucaukiemtrahang")
@Data
// phiếu nhập hàng đổ vỡ
public class YeuCauKiemTraHang implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	private String createdBy;
	
	private long createdById;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifedDate;
	private String lastModifedBy;
	private String requestCode;
	@Temporal(TemporalType.DATE)
	private Date requestDate;
	@Column(columnDefinition = "TEXT")
	private String note;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "yeuCauKiemTraHang")
	private List<YeuCauKiemTraHangDetail> YeuCauKiemTraHangDetails;

	/*
	 * KCS kiem tra
	 */
	private boolean dakiemtra;
	@Temporal(TemporalType.DATE)
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
		YeuCauKiemTraHang other = (YeuCauKiemTraHang) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public YeuCauKiemTraHang clone() throws CloneNotSupportedException {
		return (YeuCauKiemTraHang) super.clone();
	}

}
