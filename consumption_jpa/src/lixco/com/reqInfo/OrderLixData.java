package lixco.com.reqInfo;

import java.util.Date;
import java.util.List;

import lixco.com.annotation.JsonRequired;
public class OrderLixData{
	@JsonRequired(defaultType=false)
	private long id;
	@JsonRequired(defaultType=false)
	private String order_code;
	private Date created_date;
	private String created_by;
	private Date last_modifed_date;
	private String last_modifed_by;
	@JsonRequired(defaultType=false)
	private Date order_date;
	@JsonRequired(defaultType=false)
	private Date delivery_date;
	@JsonRequired(defaultType=false)
	private CustomerData customer;
	@JsonRequired(defaultType=false)
	private List<OrderDetailData> list_order_detail;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
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
	public Date getOrder_date() {
		return order_date;
	}
	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}
	public Date getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(Date delivery_date) {
		this.delivery_date = delivery_date;
	}
	public CustomerData getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerData customer) {
		this.customer = customer;
	}
	public List<OrderDetailData> getList_order_detail() {
		return list_order_detail;
	}
	public void setList_order_detail(List<OrderDetailData> list_order_detail) {
		this.list_order_detail = list_order_detail;
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
	public static class CustomerData{
		@JsonRequired(defaultType=false)
		private String customer_code;

		public String getCustomer_code() {
			return customer_code;
		}

		public void setCustomer_code(String customer_code) {
			this.customer_code = customer_code;
		}
	}
	public static class ProductData{
		@JsonRequired(defaultType=false)
		private String product_code;

		public String getProduct_code() {
			return product_code;
		}

		public void setProduct_code(String product_code) {
			this.product_code = product_code;
		}
	}
	public static class OrderDetailData{
//		 {id:0,created_date:'',created_by:'',product_code:'',box_quantity:0,promotion_forms:0}
		@JsonRequired(defaultType=false)
		private long id;
		private Date created_date;
		private String created_by;
		private Date last_modifed_date;
		private String last_modifed_by;
		@JsonRequired(defaultType=false)
		private ProductData product;
		@JsonRequired(defaultType=true)
		private double box_quantity;
		@JsonRequired(defaultType=false)
		private int promotion_forms;
		@JsonRequired(defaultType=false)
		private long order_id;
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
		
		public ProductData getProduct() {
			return product;
		}
		public void setProduct(ProductData product) {
			this.product = product;
		}
		public double getBox_quantity() {
			return box_quantity;
		}
		public void setBox_quantity(double box_quantity) {
			this.box_quantity = box_quantity;
		}
		public int getPromotion_forms() {
			return promotion_forms;
		}
		public void setPromotion_forms(int promotion_forms) {
			this.promotion_forms = promotion_forms;
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
		public long getOrder_id() {
			return order_id;
		}
		public void setOrder_id(long order_id) {
			this.order_id = order_id;
		}
	}
}

