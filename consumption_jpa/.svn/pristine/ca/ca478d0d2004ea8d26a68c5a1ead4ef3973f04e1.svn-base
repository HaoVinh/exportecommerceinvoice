package lixco.com.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lixco.com.entityapi.ProductTypeDTO;

@Entity
@Table(name = "producttype")
public class ProductType implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(unique = true)
	private String code;
	private String name;
	private String en_name;
	private int typept;
	
	

	public ProductType() {
	}

	public ProductType(long id, String code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public ProductType(long id, String code, String name, String en_name) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.en_name = en_name;
	}

	public ProductType(ProductTypeDTO ptDTO) {
		this.code = ptDTO.getCode();
		this.name = ptDTO.getName();
		this.en_name = ptDTO.getEn_name();
		this.typept = ptDTO.getTypept();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEn_name() {
		return en_name;
	}

	public void setEn_name(String en_name) {
		this.en_name = en_name;
	}

	public int getTypept() {
		return typept;
	}

	public void setTypept(int typept) {
		this.typept = typept;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductType other = (ProductType) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public ProductType clone() throws CloneNotSupportedException {
		return (ProductType) super.clone();
	}

}
