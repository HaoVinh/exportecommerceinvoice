package lixco.com.reportInfo;

import lombok.Data;

@Data
public class TongKetTieuThuKhachHang {
	private long customer_id;
	private String customer_code;
	private String customer_name;
	private long product_type_id;
	private String product_type_name;
	private long product_id;
	private String product_code;
	private String product_name;
	private String product_code_temp;
	private String product_name_temp;
	private Double quantity;
	private Double box_quantity;
	private Double unit_price;
	private Double total_amount;
	private Double tax_value;
	public TongKetTieuThuKhachHang() {
	}
	public TongKetTieuThuKhachHang(long customer_id, String customer_name, long product_type_id,
			String product_type_name, long product_id, String product_name, Double quantity, Double box_quantity,
			Double unit_price, Double total_amount, Double tax_value) {
		this.customer_id = customer_id;
		this.customer_name = customer_name;
		this.product_type_id = product_type_id;
		this.product_type_name = product_type_name;
		this.product_id = product_id;
		this.product_name = product_name;
		this.quantity = quantity;
		this.box_quantity = box_quantity;
		this.unit_price = unit_price;
		this.total_amount = total_amount;
		this.tax_value = tax_value;
	}
	

	
	
}
