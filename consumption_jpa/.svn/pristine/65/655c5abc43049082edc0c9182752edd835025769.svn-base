package lixco.com.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;













import trong.lixco.com.entity.AbstractEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hoadonc2")
@Getter
@Setter
public class HoaDonC2 extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	private String voucher_code2;// mã chứng từ cấp 2

	@OneToMany(mappedBy = "hoaDonC2", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HoaDonChiTietC2> chiTietC2List;

	@ManyToOne(fetch = FetchType.LAZY)
	private Invoice invoice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private InvoiceDetail invoiceDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private InvoiceTemp invoiceTemp;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private InvoiceDetailTemp invoiceDetailTemp;

	private String vehicleNumber; // số xe
}
