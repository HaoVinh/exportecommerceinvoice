package lixco.com.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "goodsimportbreak")
@Data
// phiếu nhập hàng đổ vỡ
public class GoodsImportBreak implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	private String createdBy;
	private long createdById;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifedDate;
	private String lastModifedBy;
	private String importCode;
	@Temporal(TemporalType.DATE)
	private Date importDate;
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;
	private String loaiPhieu;
	private String note;
	private String soxe;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goodsImportBreak")
	private List<GoodsImportBreakDetail> goodsImportBreakDetails;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoodsImportBreak other = (GoodsImportBreak) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public GoodsImportBreak clone() throws CloneNotSupportedException {
		return (GoodsImportBreak) super.clone();
	}

}
