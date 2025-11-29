package lixco.com.einvoice_entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="configeinvoice")
@Entity
public class ConfigEInvoice {
	@Id
	@GeneratedValue(strategy =IDENTITY)
	private long id;
	private String grant_type;
	private String tax_code;
	private String token;
	private String url;
	
	private String tencongty;
	
	public ConfigEInvoice(String grant_type, String tax_code, String token, String url) {
		this.grant_type = grant_type;
		this.tax_code = tax_code;
		this.token = token;
		this.url = url;
	}

	public ConfigEInvoice() {
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	public String getTax_code() {
		return tax_code;
	}

	public void setTax_code(String tax_code) {
		this.tax_code = tax_code;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTencongty() {
		return tencongty;
	}

	public void setTencongty(String tencongty) {
		this.tencongty = tencongty;
	}
	
}
