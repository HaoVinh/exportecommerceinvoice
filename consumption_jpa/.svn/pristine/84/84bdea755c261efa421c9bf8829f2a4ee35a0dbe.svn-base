package lixco.com.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lixco.com.entityapi.CustomerGroupDTO;
import lixco.com.entityapi.CustomerTypesDTO;
import lombok.Data;
@Entity
@Table(name="customergroup")
@Data
public class CustomerGroup implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String code;
	private String name;
	private String note;
	
	public CustomerGroup() {
		
	}
	public CustomerGroup(CustomerGroupDTO customerGroupDTO) {
		this.code = customerGroupDTO.getCode();
		this.name = customerGroupDTO.getName();
		this.note = customerGroupDTO.getNote();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerGroup other = (CustomerGroup) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
