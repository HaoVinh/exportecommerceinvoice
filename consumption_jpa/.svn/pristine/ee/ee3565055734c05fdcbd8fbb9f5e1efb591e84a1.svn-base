package trong.lixco.com.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name="accountapi")
@Entity
@Data
public class AccountAPI {
	@Id
	@GeneratedValue(strategy =IDENTITY)
	private long id;
	private String username;
	private String password;
	private String note;
	private String token;
	private long timetoken;

}
