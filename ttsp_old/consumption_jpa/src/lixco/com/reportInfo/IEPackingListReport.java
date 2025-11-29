package lixco.com.reportInfo;

public class IEPackingListReport {
	private long product_id;
	private String product_en_name;
	private long product_type_id;
	private String product_type_en_name;
	private String unit;
	private String unit_p;
	private double specification;
	private double tare;
	private double factor;
	private double quantity_export;

	private double total_export_foreign_amount;
	private double foreign_unit_price;
	String orderno;
	String product_code;

	public IEPackingListReport() {
	}

	public IEPackingListReport(long product_id, String product_en_name, long product_type_id,
			String product_type_en_name, String unit, double specification, double tare, double factor,
			double quantity_export, double total_export_foreign_amount, double foreign_unit_price, String orderno,
			String product_code) {
		this.product_id = product_id;
		this.product_en_name = product_en_name;
		this.product_type_id = product_type_id;
		this.product_type_en_name = product_type_en_name;
		this.unit = unit;
		this.specification = specification;
		this.tare = tare;
		this.factor = factor;
		this.quantity_export = quantity_export;
		this.unit_p = unit;
		this.total_export_foreign_amount = total_export_foreign_amount;
		this.foreign_unit_price = foreign_unit_price;
		this.orderno = orderno;
		this.product_code = product_code;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public long getProduct_id() {
		return product_id;
	}

	public void setProduct_id(long product_id) {
		this.product_id = product_id;
	}

	public String getProduct_en_name() {
		return product_en_name;
	}

	public void setProduct_en_name(String product_en_name) {
		this.product_en_name = product_en_name;
	}

	public long getProduct_type_id() {
		return product_type_id;
	}

	public void setProduct_type_id(long product_type_id) {
		this.product_type_id = product_type_id;
	}

	public String getProduct_type_en_name() {
		return product_type_en_name;
	}

	public void setProduct_type_en_name(String product_type_en_name) {
		this.product_type_en_name = product_type_en_name;
	}

	public double getQuantity_export() {
		return quantity_export;
	}

	public void setQuantity_export(double quantity_export) {
		this.quantity_export = quantity_export;
	}

	public double getSpecification() {
		return specification;
	}

	public void setSpecification(double specification) {
		this.specification = specification;
	}

	public double getTare() {
		return tare;
	}

	public void setTare(double tare) {
		this.tare = tare;
	}

	public double getFactor() {
		return factor;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit_p() {
		return unit_p;
	}

	public void setUnit_p(String unit_p) {
		this.unit_p = unit_p;
	}

	public double getTotal_export_foreign_amount() {
		return total_export_foreign_amount;
	}

	public void setTotal_export_foreign_amount(double total_export_foreign_amount) {
		this.total_export_foreign_amount = total_export_foreign_amount;
	}

	public double getForeign_unit_price() {
		return foreign_unit_price;
	}

	public void setForeign_unit_price(double foreign_unit_price) {
		this.foreign_unit_price = foreign_unit_price;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

}
