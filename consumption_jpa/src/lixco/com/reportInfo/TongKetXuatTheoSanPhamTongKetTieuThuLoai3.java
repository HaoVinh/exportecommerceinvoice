package lixco.com.reportInfo;

public class TongKetXuatTheoSanPhamTongKetTieuThuLoai3 {
	private long product_type_id;
	private String product_type_name;
	private long product_id;
	private String product_name;
	private Double quantity;//số kg
	private Double unit_price;
	private Double total_amount;
	private Double total_tax_amount;
	
	public TongKetXuatTheoSanPhamTongKetTieuThuLoai3() {
	}
	
	public TongKetXuatTheoSanPhamTongKetTieuThuLoai3(long product_type_id, String product_type_name, long product_id,
			String product_name, Double quantity, Double unit_price, Double total_amount, Double total_tax_amount) {
		this.product_type_id = product_type_id;
		this.product_type_name = product_type_name;
		this.product_id = product_id;
		this.product_name = product_name;
		this.quantity = quantity;
		this.unit_price = unit_price;
		this.total_amount = total_amount;
		this.total_tax_amount = total_tax_amount;
	}

	public long getProduct_type_id() {
		return product_type_id;
	}
	public void setProduct_type_id(long product_type_id) {
		this.product_type_id = product_type_id;
	}
	public String getProduct_type_name() {
		return product_type_name;
	}
	public void setProduct_type_name(String product_type_name) {
		this.product_type_name = product_type_name;
	}
	public long getProduct_id() {
		return product_id;
	}
	public void setProduct_id(long product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
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
	public Double getTotal_tax_amount() {
		return total_tax_amount;
	}
	public void setTotal_tax_amount(Double total_tax_amount) {
		this.total_tax_amount = total_tax_amount;
	}
}
