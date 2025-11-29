package lixco.com.reqInfo;

import java.util.Date;

import lixco.com.annotation.JsonDate;
import lixco.com.annotation.JsonRequired;
import lixco.com.annotation.TestM;

public class OrderSearchInfo implements TestM{
	private OrderInfo order_info;
	private Page page;
	public static class OrderInfo implements TestM{
		@JsonDate(pattern="dd/MM/yyyy")
		@JsonRequired(defaultType=true)
		private Date from_date;
		@JsonDate(pattern="dd/MM/yyyy")
		private Date to_date;
		private long customer_id;
		private String order_code;
		private String voucher_code;
		private long ie_categories;
		private String po_no;
		private int delivered;
		private int status;
		public Date getFrom_date() {
			return from_date;
		}
		public void setFrom_date(Date from_date) {
			this.from_date = from_date;
		}
		public Date getTo_date() {
			return to_date;
		}
		public void setTo_date(Date to_date) {
			this.to_date = to_date;
		}
		public long getCustomer_id() {
			return customer_id;
		}
		public void setCustomer_id(long customer_id) {
			this.customer_id = customer_id;
		}
		public String getOrder_code() {
			return order_code;
		}
		public void setOrder_code(String order_code) {
			this.order_code = order_code;
		}
		public String getVoucher_code() {
			return voucher_code;
		}
		public void setVoucher_code(String voucher_code) {
			this.voucher_code = voucher_code;
		}
		public long getIe_categories() {
			return ie_categories;
		}
		public void setIe_categories(long ie_categories) {
			this.ie_categories = ie_categories;
		}
		public String getPo_no() {
			return po_no;
		}
		public void setPo_no(String po_no) {
			this.po_no = po_no;
		}
		public int getDelivered() {
			return delivered;
		}
		public void setDelivered(int delivered) {
			this.delivered = delivered;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		
	}
	public static class Page implements TestM{
		@JsonRequired(defaultType=true)
		private int page_index;
		@JsonRequired(defaultType=true)
		private int page_size;
		public int getPage_index() {
			return page_index;
		}
		public void setPage_index(int page_index) {
			this.page_index = page_index;
		}
		public int getPage_size() {
			return page_size;
		}
		public void setPage_size(int page_size) {
			this.page_size = page_size;
		}
	}
	public OrderInfo getOrder_info() {
		return order_info;
	}
	public void setOrder_info(OrderInfo order_info) {
		this.order_info = order_info;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
}
