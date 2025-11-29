package lixco.com.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name = "invoicedetaillog")
@Data
public class InvoiceDetailLog {
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
