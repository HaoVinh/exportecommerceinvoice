package lixco.com.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class LixPathParam {
	@Id
	@GeneratedValue(strategy =IDENTITY)
	private long id;
	private String lixkey;
	private String lixvalue;
	private String note;

	
}
