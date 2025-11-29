package lixco.com.reportInfo;

public class PhieuXuatKho {
	private long product_id;
	private String product_code;
	private String product_name;
	private String batch_code;
	private String unit;
	private Double quantity;
	private Double real_quantity;
	private Double unit_price;
	private Double total_amount;
	
	public PhieuXuatKho() {
	}
	public PhieuXuatKho(long product_id, String product_code, String product_name, String batch_code, String unit,
			Double quantity, Double unit_price, Double total_amount) {
		this.product_id = product_id;
		this.product_code = product_code;
		this.product_name = product_name;
		this.batch_code = batch_code;
		this.unit = unit;
		this.quantity = quantity;
		this.real_quantity = real_quantity;
		this.unit_price = unit_price;
		this.total_amount = total_amount;
	}
	public long getProduct_id() {
		return product_id;
	}
	public void setProduct_id(long product_id) {
		this.product_id = product_id;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getBatch_code() {
		return batch_code;
	}
	public void setBatch_code(String batch_code) {
		this.batch_code = batch_code;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getReal_quantity() {
		return real_quantity;
	}
	public void setReal_quantity(Double real_quantity) {
		this.real_quantity = real_quantity;
	}
	public Double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(Double unit_price) {
		this.unit_price = unit_price;
	}
	public Double getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(Double total_amount) {
		this.total_amount = total_amount;
	}
}
