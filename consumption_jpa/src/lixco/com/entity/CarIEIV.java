package lixco.com.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Table(name="carieiv")
@Data
public class CarIEIV implements Serializable,Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	private IEInvoice ieInvoice;//chủ xe
	@ManyToOne
	private Car car;//loại xe
	private String note;
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarIEIV other = (CarIEIV) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public CarIEIV clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (CarIEIV) super.clone();
	}
}
