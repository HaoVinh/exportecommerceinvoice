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
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name = "yeucaukiemtrahangdetail")
@Data
//Chi tiết phiếu nhập hàng đổ vỡ
public class YeuCauKiemTraHangDetail implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_date;
	private String created_by;
	@Temporal(TemporalType.TIMESTAMP)
	private Date last_modifed_date;
	private String last_modifed_by;
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;
	@ManyToOne(fetch = FetchType.LAZY)
	private YeuCauKiemTraHang yeuCauKiemTraHang;
	private double quantity;// số lượng dvt
	private String lohang;
	private int nguongoc;
	private String note;
	
	/*
	 * KCS kiem tra
	 */
	private String tinhtrang;
	private boolean kiemtradat;
	private String tieuchuan;
	private String nguyennhan;
	private String huonggiaiquyet;//ketqua
	private String huonggiaiquyet2;
	@Temporal(TemporalType.DATE)
	private Date giahanluukho;
	
	@Transient
	private int minv;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YeuCauKiemTraHangDetail other = (YeuCauKiemTraHangDetail) obj;
		if (other.id == 0) {
			return true;
		}
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public YeuCauKiemTraHangDetail clone() throws CloneNotSupportedException {
		return (YeuCauKiemTraHangDetail) super.clone();
	}

}
