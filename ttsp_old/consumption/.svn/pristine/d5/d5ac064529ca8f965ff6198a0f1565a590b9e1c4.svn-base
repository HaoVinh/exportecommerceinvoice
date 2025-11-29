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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "driverieiv")
@Data
public class DriverIEIV implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	private IEInvoice ieInvoice;// chủ xe
	@ManyToOne
	private Driver driver;// loại xe
	private String note;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DriverIEIV other = (DriverIEIV) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public DriverIEIV clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (DriverIEIV) super.clone();
	}
}
