package lixco.com.reportInfo;

public class ThongKeSanLuongDoanhThu {
	private long product_type_id;
	private String product_type_code;
	private String product_type_name;
	private Double quantity;//tổng số lượng đơn vị tấn.
	private Double total_amount;//tổng tiền đơn vị triệu đồng
	
	public ThongKeSanLuongDoanhThu() {
	}
	
	public ThongKeSanLuongDoanhThu(long product_type_id, String product_type_code, String product_type_name,
			Double quantity, Double total_amount) {
		this.product_type_id = product_type_id;
		this.product_type_code = product_type_code;
		this.product_type_name = product_type_name;
		this.quantity = quantity;
		this.total_amount = total_amount;
	}

	public long getProduct_type_id() {
		return product_type_id;
	}
	public void setProduct_type_id(long product_type_id) {
		this.product_type_id = product_type_id;
	}
	public String getProduct_type_code() {
		return product_type_code;
	}
	public void setProduct_type_code(String product_type_code) {
		this.product_type_code = product_type_code;
	}
	public String getProduct_type_name() {
		return product_type_name;
	}
	public void setProduct_type_name(String product_type_name) {
		this.product_type_name = product_type_name;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(Double total_amount) {
		this.total_amount = total_amount;
	}
}
