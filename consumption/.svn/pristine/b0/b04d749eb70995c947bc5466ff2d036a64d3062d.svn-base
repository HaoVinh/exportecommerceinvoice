package vinh.lixco.com.apiecommerce;

import java.util.Date;
import java.util.List;

import java.util.Date;
import java.util.List;

public class OrderDTO {
	private String orderId;
	private String customerLastName;
	private String customerFirstName;
	private Date createdAt;
	private Date updatedAt;
	private Double shippingFee;
	private Double price;
	private Double lastPrice;
	private String orderType;
	private String paymentDataJson;
	private Double shippingDiscount;
	private Double totalSellerDiscount;
	public Double getTotalSellerDiscount() {
		return totalSellerDiscount;
	}

	public void setTotalSellerDiscount(Double totalSellerDiscount) {
		this.totalSellerDiscount = totalSellerDiscount;
	}

	public String getPaymentDataJson() {
		return paymentDataJson;
	}

	public void setPaymentDataJson(String paymentDataJson) {
		this.paymentDataJson = paymentDataJson;
	}

	public Double getShippingDiscount() {
		return shippingDiscount;
	}

	public void setShippingDiscount(Double shippingDiscount) {
		this.shippingDiscount = shippingDiscount;
	}

	public Double getShopeeDiscount() {
		return shopeeDiscount;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public void setShopeeDiscount(Double shopeeDiscount) {
		this.shopeeDiscount = shopeeDiscount;
	}

	public Double getSellerDiscount() {
		return sellerDiscount;
	}

	public void setSellerDiscount(Double sellerDiscount) {
		this.sellerDiscount = sellerDiscount;
	}

	public Double getComboDiscount() {
		return comboDiscount;
	}

	public void setComboDiscount(Double comboDiscount) {
		this.comboDiscount = comboDiscount;
	}

	private Double shopeeDiscount;
	private Double sellerDiscount;
	private Double comboDiscount;
	private Double discountedPrice;
	private String dataJson;
	public String getDataJson() {
		return dataJson;
	}

	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}

	public Double getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(Double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public Double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(Double lastPrice) {
		this.lastPrice = lastPrice;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	private String status; // Ánh xạ từ order_status hoặc statuses (tùy logic)
	private String order_status; // Từ webhook
	private List<String> statuses; // Từ API /order/get
	private List<OrderDetailDTO> orderDetails;
	private String eCommerceType;

	// Getters and setters
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Double getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(Double shippingFee) {
		this.shippingFee = shippingFee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public List<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<String> statuses) {
		this.statuses = statuses;
	}

	public List<OrderDetailDTO> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public String geteCommerceType() {
		return eCommerceType;
	}

	public void seteCommerceType(String eCommerceType) {
		this.eCommerceType = eCommerceType;
	}

}