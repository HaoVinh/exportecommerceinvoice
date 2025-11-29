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

import lixco.com.entityapi.CityDTO;
import lombok.Data;

@Entity
@Table(name = "khoadulieu")
@Data
public class KhoaDuLieu implements Serializable {
	private static final long serialVersionUID = 2323632799050358633L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_date;
	private int kmonth;
	private int kyear;
	private boolean kkhoa;
	private String note;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhoaDuLieu other = (KhoaDuLieu) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
