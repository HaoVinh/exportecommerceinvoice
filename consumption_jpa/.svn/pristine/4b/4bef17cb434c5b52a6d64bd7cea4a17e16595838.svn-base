package lixco.com.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "xacnhankiemtradetail")
@Data
public class XacNhanKiemTraDetail implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_date;
	private String created_by;
	@Temporal(TemporalType.TIMESTAMP)
	private Date last_modifed_date;
	private String last_modifed_by;
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;
	@ManyToOne(fetch = FetchType.LAZY)
	private XacNhanKiemTra xacNhanKiemTra;
	
	private double quantityCa1;
	private double quantityCa2;
	private double quantityCa3;
	
	private double quantityTTSP;
	
	private double chenhlech;
	
	private int trangthaicl;
	@Column(columnDefinition = "LONGTEXT")
	private String dataPallet;
	private String note;
	
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XacNhanKiemTraDetail other = (XacNhanKiemTraDetail) obj;
		if (other.id == 0) {
			return true;
		}
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public XacNhanKiemTraDetail clone() throws CloneNotSupportedException {
		return (XacNhanKiemTraDetail) super.clone();
	}


}
