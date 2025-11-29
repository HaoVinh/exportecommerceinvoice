package lixco.com.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data
@Entity
@Table(name="dvtxuatkhau")
public class DVTXuatKhau implements Serializable,Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String dvtsp;
	private String dvtPac;
	private String dvtTons;
	private String note;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		if (id != other.getId())
			return false;
		return true;
	}
	@Override
	public Car clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (Car) super.clone();
	}
}
