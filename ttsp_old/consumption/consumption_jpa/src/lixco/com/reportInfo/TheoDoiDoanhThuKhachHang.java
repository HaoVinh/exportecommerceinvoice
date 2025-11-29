package lixco.com.reportInfo;

public class TheoDoiDoanhThuKhachHang {
	private long customer_id;
	private String customer_code;
	private String customer_name;
	private Double quantity;
	private Double box_quantity;
	private Double total_amount;
	
	public TheoDoiDoanhThuKhachHang() {
	}
	
	public TheoDoiDoanhThuKhachHang(long customer_id,String customer_code, String customer_name, Double quantity, Double box_quantity,
			Double total_amount) {
		this.customer_id = customer_id;
		this.customer_code=customer_code;
		this.customer_name = customer_name;
		this.quantity = quantity;
		this.box_quantity = box_quantity;
		this.total_amount = total_amount;
	}

	public long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_code() {
		return customer_code;
	}

	public void setCustomer_code(String customer_code) {
		this.customer_code = customer_code;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getBox_quantity() {
		if(box_quantity==null)
			return 0.0;
		return box_quantity;
	}

	public void setBox_quantity(Double box_quantity) {
		this.box_quantity = box_quantity;
	}

	public Double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(Double total_amount) {
		this.total_amount = total_amount;
	}
	
}
