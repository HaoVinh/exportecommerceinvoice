package lixco.com.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Data;
@Data
@Entity
@Table(name="goodsReceiptnotedetail")
public class GoodsReceiptNoteDetail implements Serializable,Cloneable{// chi tiết phiếu nhập kho
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
	@ManyToOne(fetch=FetchType.LAZY)
	private Product product;
	@ManyToOne(fetch=FetchType.LAZY)
	private GoodsReceiptNote goods_receipt_note;
	private double quantity;//số lượng
	private double price;
	private double total;
	private String tk_1;////tài khoản nợ
	private String tk_2;//tài khoản có 
	@OneToOne(fetch=FetchType.LAZY)
	private Batch batch;//lô hàng
	private String batch_code;
	private long delivery_detail_id;
	private String license_plate;//số xe
	private String vcnb_invoice_code;
	private String delivery_code;
	@Transient
	private int minv;
	private String idfox;
	
	//Trong bo sung 10/08/2023
	private String po;
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoodsReceiptNoteDetail other = (GoodsReceiptNoteDetail) obj;
		if(other.id==0 && minv==other.minv){
			return true;
		}
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public GoodsReceiptNoteDetail clone() throws CloneNotSupportedException {
		return (GoodsReceiptNoteDetail)super.clone();
	}
	
}
