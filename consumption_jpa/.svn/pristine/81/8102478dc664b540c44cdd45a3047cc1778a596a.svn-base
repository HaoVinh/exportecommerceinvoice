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
@Table(name = "XacNhanKiemTra")
@Data
public class XacNhanKiemTra implements Serializable, Cloneable {
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
	private int trangthaicl; //0: chua kiem, 1: đạt, 2:chờ kiểm, 3:không đạt
	@Column(columnDefinition = "TEXT")
	private String note;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "xacNhanKiemTra")
	private List<XacNhanKiemTraDetail> xacNhanKiemTraDetails;
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
		XacNhanKiemTra other = (XacNhanKiemTra) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public XacNhanKiemTra clone() throws CloneNotSupportedException {
		return (XacNhanKiemTra) super.clone();
	}

}
