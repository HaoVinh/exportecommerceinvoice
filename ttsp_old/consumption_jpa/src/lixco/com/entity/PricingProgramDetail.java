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

import lixco.com.entityapi.PricingProgramDetailDTO;

@Entity
@Table(name="pricingprogramdetail")
public class PricingProgramDetail implements Serializable,Cloneable{
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
	private double unit_price;//đơn giá,
	private double quantity;//số lượng
	@ManyToOne(fetch=FetchType.LAZY)
	private Product product;//sản phẩm
	@ManyToOne(fetch=FetchType.LAZY)
	private PricingProgram pricing_program;//chương trình định giá
	private double revenue_per_ton; //lợi nhuận mỗi tấn.
	
	public PricingProgramDetail(PricingProgramDetailDTO ppd) {
		this.unit_price = ppd.getUnit_price();
		this.quantity = ppd.getQuantity();
		this.revenue_per_ton = ppd.getRevenue_per_ton();
	}
	
	public PricingProgramDetail() {
	}
	public PricingProgramDetail(long id) {
		this.id=id;
	}
	
	public PricingProgramDetail(long id, double unit_price) {
		this.id = id;
		this.unit_price = unit_price;
	}
	public PricingProgramDetail(long id, double unit_price,double revenue_per_ton) {
		this.id = id;
		this.unit_price = unit_price;
		this.revenue_per_ton=revenue_per_ton;
	}
	
	
	public PricingProgramDetail(long id, double unit_price, double quantity, long productId,String productCode,String productName,long pricingProgramId, double revenue_per_ton) {
		this.id = id;
		this.unit_price = unit_price;
		this.quantity = quantity;
		this.product = new Product(productId,productCode, productName);
		this.pricing_program = new PricingProgram(pricingProgramId);
		this.revenue_per_ton = revenue_per_ton;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public Date getLast_modifed_date() {
		return last_modifed_date;
	}
	public void setLast_modifed_date(Date last_modifed_date) {
		this.last_modifed_date = last_modifed_date;
	}
	public String getLast_modifed_by() {
		return last_modifed_by;
	}
	public void setLast_modifed_by(String last_modifed_by) {
		this.last_modifed_by = last_modifed_by;
	}
	public double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public PricingProgram getPricing_program() {
		return pricing_program;
	}
	public void setPricing_program(PricingProgram pricing_program) {
		this.pricing_program = pricing_program;
	}
	public double getRevenue_per_ton() {
		return revenue_per_ton;
	}
	public void setRevenue_per_ton(double revenue_per_ton) {
		this.revenue_per_ton = revenue_per_ton;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PricingProgramDetail other = (PricingProgramDetail) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public PricingProgramDetail clone() throws CloneNotSupportedException {
		return (PricingProgramDetail)super.clone();
	}
	

}
