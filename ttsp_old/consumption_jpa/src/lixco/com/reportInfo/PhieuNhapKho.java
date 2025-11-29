package lixco.com.reportInfo;

import lombok.Data;

@Data
public class PhieuNhapKho {
	private long product_id;
	private String product_code;
	private String product_name;
	private String unit;
	private Double kg_quantity;
	private Double unit_quantity;
	private Double real_import_quantity;
	private Double unit_price;
	private Double total_amount;
	
	public PhieuNhapKho() {
	}
	
	public PhieuNhapKho(long product_id, String product_code, String product_name, String unit, Double kg_quantity,
			Double unit_quantity, Double unit_price, Double total_amount) {
		this.product_id = product_id;
		this.product_code = product_code;
		this.product_name = product_name;
		this.unit = unit;
		this.kg_quantity = kg_quantity;
		this.unit_quantity = unit_quantity;
		this.unit_price = unit_price;
		this.total_amount = total_amount;
	}

	
	
}
