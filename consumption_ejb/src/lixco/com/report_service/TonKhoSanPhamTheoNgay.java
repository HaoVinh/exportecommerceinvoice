package lixco.com.report_service;

import java.util.Date;

import lombok.Data;
@Data
public class TonKhoSanPhamTheoNgay {
	private Long product_type_id;
	private String product_type_code;
	private String product_type_name;
	private Long pbrand_id;
	private String pbrand_code;
	private String pbrand_name;
	private Long product_id;
	private String lever_code;
	private String product_code;
	private String product_name;
	private Double factor;
	private Double specification;
	private Long promotion_product_id;
	private String promotion_product_code;
	private String promotion_product_name;
	private Double kg_opening_balance;
	private Double unit_opening_balance;
	private Double kg_import_quantity;
	private Double unit_import_quantity;
	private Double kg_export_quantity;
	private Double unit_export_quantity;
	private Double export_total_amount;
	private Double kg_closing_balance;
	private String product_unit;
	private String nhanhang;
	private Date transaction_date;

	// Constructors
	public TonKhoSanPhamTheoNgay() {
		this.kg_opening_balance = 0.0;
		this.unit_opening_balance = 0.0;
		this.kg_import_quantity = 0.0;
		this.unit_import_quantity = 0.0;
		this.kg_export_quantity = 0.0;
		this.unit_export_quantity = 0.0;
		this.export_total_amount = 0.0;
		this.kg_closing_balance = 0.0;
	}

	// Copy từ object khác
	public void copyFrom(TonKhoSanPhamTheoNgay other) {
		this.product_type_id = other.product_type_id;
		this.product_type_code = other.product_type_code;
		this.product_type_name = other.product_type_name;
		this.pbrand_id = other.pbrand_id;
		this.pbrand_code = other.pbrand_code;
		this.pbrand_name = other.pbrand_name;
		this.product_id = other.product_id;
		this.lever_code = other.lever_code;
		this.product_code = other.product_code;
		this.product_name = other.product_name;
		this.factor = other.factor;
		this.specification = other.specification;
		this.promotion_product_id = other.promotion_product_id;
		this.promotion_product_code = other.promotion_product_code;
		this.promotion_product_name = other.promotion_product_name;
		this.product_unit = other.product_unit;
		this.nhanhang = other.nhanhang;
	}
}
