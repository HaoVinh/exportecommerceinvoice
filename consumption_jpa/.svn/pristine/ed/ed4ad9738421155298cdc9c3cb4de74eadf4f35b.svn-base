package trong.lixco.com.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Table(name="accountemailconfirm")
@Entity
@Data
public class AccountEmailConfirm {
	@Id
	@GeneratedValue(strategy =IDENTITY)
	private long id;
	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createdDate = new Date();
	private String uid;
	private String user;
	private String pass;
	private String codeEmp;
	private String nameEmp;

}
