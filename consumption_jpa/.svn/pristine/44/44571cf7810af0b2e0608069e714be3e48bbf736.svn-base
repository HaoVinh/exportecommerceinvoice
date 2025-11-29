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
@Table(name = "mylogdms")
@Data
public class MyLogDMS {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date datelog =new Date();
	private String action;
	private boolean success;
	private String user;
	private String data;
	
	private long idphieu;
	private String maphieu;
	private String mgs;
	public MyLogDMS(String action, boolean success, String user, String data, long idphieu, String maphieu,String mgs) {
		super();
		this.action = action;
		this.success = success;
		this.user = user;
		this.data = data;
		this.idphieu = idphieu;
		this.maphieu = maphieu;
		this.mgs=mgs;
	}
	public MyLogDMS() {
		super();
	}
}
