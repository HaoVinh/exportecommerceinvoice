package lixco.com.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Table(name="thongsocaidat")
@Data
public class ThongSoCaiDat implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	@Column(columnDefinition = "TEXT")
	private String value;
	@Column(columnDefinition = "TEXT")
	private String note;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThongSoCaiDat other = (ThongSoCaiDat) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
