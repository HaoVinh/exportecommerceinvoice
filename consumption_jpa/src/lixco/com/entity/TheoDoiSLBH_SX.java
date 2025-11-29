package lixco.com.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "theodoislbh_sx")
@Data
public class TheoDoiSLBH_SX implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_date;
	private String created_by;
	private long created_by_id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date last_modifed_date;
	private String last_modifed_by;
	
	@Temporal(TemporalType.DATE)
	private Date sLdate;
	@Column(columnDefinition = "TEXT")
	String theoDoiSLBH_SX_Data;
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TheoDoiSLBH_SX other = (TheoDoiSLBH_SX) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	

}
