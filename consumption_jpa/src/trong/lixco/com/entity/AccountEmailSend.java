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

@Table(name = "accountemailsend")
@Entity
@Data
public class AccountEmailSend {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private long id;
	@Column(name = "createdDate")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createdDate = new Date();
	private String codeEmp;
	private String nameEmp;
	/*
	 * 1: tiep nhan phieu yeu cau kiem tra hang hoa
	 */
	private String typeAcc;
	private String email;
	private String note;

}
