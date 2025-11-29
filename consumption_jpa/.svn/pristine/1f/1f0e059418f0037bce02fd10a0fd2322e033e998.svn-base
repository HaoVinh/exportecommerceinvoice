package lixco.com.entity;

import java.io.Serializable;
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
@Table(name = "invoicedelete")
@Data
public class InvoiceDelete implements Serializable, Cloneable {// hóa đơn chính
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_date;
	private String voucher_code;
	@Temporal(TemporalType.DATE)
	private Date invoice_date;
	private String idref;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvoiceDelete other = (InvoiceDelete) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
