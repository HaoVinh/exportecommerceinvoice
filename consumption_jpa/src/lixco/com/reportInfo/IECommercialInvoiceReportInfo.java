package lixco.com.reportInfo;

import lombok.Data;

@Data
public class IECommercialInvoiceReportInfo {
	private long product_id;
	private String product_code;
	private String barcode;
	private String product_en_name;
	private double specification;
	private String unit;
	private boolean unit_PSC;
	private String unit_p;
	private long product_type_id;
	private String product_type_en_name;
	private double quantity_export;
	private double total_export_foreign_amount;
	private double foreign_unit_price;
	private String orderno;

	private String dvtPac;
	private String dvtTons;
	private boolean hdkhuyenmai;

	public IECommercialInvoiceReportInfo() {
	}

	public IECommercialInvoiceReportInfo(long product_id, String product_code, String barcode, String product_en_name,
			double specification, String unit, long product_type_id, String product_type_en_name,
			double quantity_export, double foreign_unit_price, double total_export_foreign_amount, String orderno) {
		this.product_id = product_id;
		this.product_code = product_code;
		this.barcode = barcode;
		this.product_en_name = product_en_name;
		this.specification = specification;
		this.unit = unit;
		this.product_type_id = product_type_id;
		this.product_type_en_name = product_type_en_name;
		this.quantity_export = quantity_export;
		this.total_export_foreign_amount = total_export_foreign_amount;
		this.foreign_unit_price = foreign_unit_price;
		this.unit_p = unit;
		this.orderno = orderno;
	}

}
