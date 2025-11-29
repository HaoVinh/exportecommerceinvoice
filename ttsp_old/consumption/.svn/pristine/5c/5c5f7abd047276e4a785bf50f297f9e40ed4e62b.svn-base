package lixco.com.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.jboss.resteasy.annotations.StringParameterUnmarshallerBinder;

import lixco.com.loaddata.BienNhanVanChuyenChiTiet;
import lombok.Data;
@Entity
@Table(name="biennhanvanchuyen")
@Data
public class BienNhanVanChuyen implements Serializable,Cloneable{
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
	@ManyToOne(fetch=FetchType.LAZY)
	private Car car;//số xe 
	@Transient
	private String cargroup;//số xe để nhoms
	private double total;//tổng cộng
	private String chitiet;
	
	String sophieuvc;
	String nguoivanchuyen;
	double tongkhoiluong;
	
	@Transient
	List<BienNhanVanChuyenChiTiet> bienNhanVanChuyenChiTiets;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BienNhanVanChuyen other = (BienNhanVanChuyen) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	@Override
	public BienNhanVanChuyen clone() throws CloneNotSupportedException {
		return (BienNhanVanChuyen) super.clone();
	}
}
