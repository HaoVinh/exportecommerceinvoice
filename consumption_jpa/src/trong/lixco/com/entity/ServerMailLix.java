package trong.lixco.com.entity;

/**
 * @author vantrong
 *
 * 01-08-2017
 */
import javax.persistence.Entity;

@Entity
public class ServerMailLix extends AbstractEntity {
	private String pkey;
	private String pvalue;
	public String getKey() {
		return pkey;
	}
	public void setKey(String key) {
		this.pkey = key;
	}
	public String getValue() {
		return pvalue;
	}
	public void setValue(String value) {
		this.pvalue = value;
	}

	

}
