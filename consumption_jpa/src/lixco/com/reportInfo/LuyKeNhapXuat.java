package lixco.com.reportInfo;

public class LuyKeNhapXuat {
	private long product_type_id;
	private String product_type_code;
	private String product_type_name;
	private long ie_categories_id;
	private String ie_categories_code;
	private String ie_categories_name;
	private Double import_quantity;//số lượng nhập
	private Double export_quantity;//số lượng xuất
	private Double lk_import;// lũy kế nhập
	private Double lk_export;//lũy kế xuất
	
	public LuyKeNhapXuat() {
	}
	
	public LuyKeNhapXuat(long product_type_id, String product_type_code, String product_type_name,
			long ie_categories_id, String ie_categories_code, String ie_categories_name, Double import_quantity,
			Double export_quantity, Double lk_import, Double lk_export) {
		this.product_type_id = product_type_id;
		this.product_type_code = product_type_code;
		this.product_type_name = product_type_name;
		this.ie_categories_id = ie_categories_id;
		this.ie_categories_code = ie_categories_code;
		this.ie_categories_name = ie_categories_name;
		this.import_quantity = import_quantity;
		this.export_quantity = export_quantity;
		this.lk_import = lk_import;
		this.lk_export = lk_export;
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
	public long getIe_categories_id() {
		return ie_categories_id;
	}
	public void setIe_categories_id(long ie_categories_id) {
		this.ie_categories_id = ie_categories_id;
	}
	public String getIe_categories_code() {
		return ie_categories_code;
	}
	public void setIe_categories_code(String ie_categories_code) {
		this.ie_categories_code = ie_categories_code;
	}
	public String getIe_categories_name() {
		return ie_categories_name;
	}
	public void setIe_categories_name(String ie_categories_name) {
		this.ie_categories_name = ie_categories_name;
	}
	public Double getImport_quantity() {
		return import_quantity;
	}
	public void setImport_quantity(Double import_quantity) {
		this.import_quantity = import_quantity;
	}
	public Double getExport_quantity() {
		return export_quantity;
	}
	public void setExport_quantity(Double export_quantity) {
		this.export_quantity = export_quantity;
	}
	public Double getLk_import() {
		return lk_import;
	}
	public void setLk_import(Double lk_import) {
		this.lk_import = lk_import;
	}
	public Double getLk_export() {
		return lk_export;
	}
	public void setLk_export(Double lk_export) {
		this.lk_export = lk_export;
	}
}
