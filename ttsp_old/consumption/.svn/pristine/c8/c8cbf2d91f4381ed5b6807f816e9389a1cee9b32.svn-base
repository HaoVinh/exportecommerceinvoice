package lixco.com.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
@Entity
@Table(name="invencongno")
@Data
public class InvenCongNo implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_date;
	private String created_by;
	@ManyToOne(fetch=FetchType.LAZY)
	private Customer customer;
	private double invenFirst;//no dau ky
	private double invenNo;//no trong thang
	private double InvenCo;//thanh toan trong thang
	private double InvenFinal;//no cuoi ky
	private int month;
	private int year;
	
}
