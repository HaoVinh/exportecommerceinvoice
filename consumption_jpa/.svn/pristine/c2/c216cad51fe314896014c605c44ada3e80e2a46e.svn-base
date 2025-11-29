package lixco.com.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "mylog")
@Data
public class MyLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long idphieu;
	@Temporal(TemporalType.TIMESTAMP)
	private Date datelog;
	private String action;
	private String user;
	private String data;
}
