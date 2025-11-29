package vinh.lixco.com.apiecommerce;

public class OrderDetailDTO {
	private int stt;

	public int getStt() {
		return stt;
	}

	public void setStt(int stt) {
		this.stt = stt;
	}

	private String orderId;
	private String name;
	private String sku;
	private Double itemPrice;
	private String orderItemId;
	private boolean isVariant;
	private String loaitmdt;
	private Double voucherSeller;
	private int quantity;
	private int skuQuantity;
	private Double splitPrice;
	private Double unitPrice;
	private String promotionType;// phân biệt hàng bán và hàng KM của shopee
	private boolean is_gift; // phân biệt hàng bán và hàng KM của tiktok
	private String orderDetailType;
	private Double shopeeDiscount;
	private Double sellerDiscount;
	private Double comboDiscount;
	private Double discountedPrice;
	private Double shippingFeeDiscount;
	private boolean combo;
	private String imageURL;
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Double getShippingFeeDiscount() {
		return shippingFeeDiscount;
	}

	public void setShippingFeeDiscount(Double shippingFeeDiscount) {
		this.shippingFeeDiscount = shippingFeeDiscount;
	}

	public boolean isCombo() {
		return combo;
	}

	public void setCombo(boolean combo) {
		this.combo = combo;
	}

	public Double getShippingDiscount() {
		return shippingFeeDiscount;
	}

	public void setShippingDiscount(Double shippingDiscount) {
		this.shippingFeeDiscount = shippingDiscount;
	}

	private String dataDetailJSON;
	public String getDataDetailJSON() {
		return dataDetailJSON;
	}

	public void setDataDetailJSON(String dataDetailJSON) {
		this.dataDetailJSON = dataDetailJSON;
	}

	public Double getShopeeDiscount() {
		return shopeeDiscount;
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

	public Double getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(Double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public String getOrderDetailType() {
		return orderDetailType;
	}

	public void setOrderDetailType(String orderDetailType) {
		this.orderDetailType = orderDetailType;
	}

	public boolean isIs_gift() {
		return is_gift;
	}

	public void setIs_gift(boolean is_gift) {
		this.is_gift = is_gift;
	}

	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getSplitPrice() {
		return splitPrice;
	}

	public void setSplitPrice(Double splitPrice) {
		this.splitPrice = splitPrice;
	}

	public int getSkuQuantity() {
		return skuQuantity;
	}

	public void setSkuQuantity(int skuQuantity) {
		this.skuQuantity = skuQuantity;
	}

	private Double lastItemPrice;

	public Double getLastItemPrice() {
		return lastItemPrice;
	}

	public void setLastItemPrice(Double lastItemPrice) {
		this.lastItemPrice = lastItemPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getVoucherSeller() {
		return voucherSeller;
	}

	public void setVoucherSeller(Double voucherSeller) {
		this.voucherSeller = voucherSeller;
	}

	public String getLoaitmdt() {
		return loaitmdt;
	}

	public void setLoaitmdt(String loaitmdt) {
		this.loaitmdt = loaitmdt;
	}

	public boolean isVariant() {
		return isVariant;
	}

	public void setVariant(boolean isVariant) {
		this.isVariant = isVariant;
	}

	// Getters and setters
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
}
