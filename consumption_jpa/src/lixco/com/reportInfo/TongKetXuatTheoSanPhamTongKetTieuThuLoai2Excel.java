package lixco.com.reportInfo;

public class TongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel {
	private long product_com_id;
	private String pcom_code;
	private String pcom_name;
	private long ie_categories_id;
	private String ie_categories_code;
	private String ie_categories_name;
	private Double quantity_kg;//số lượng kg
	private Double quantity;//số luong
	private Double total_amount;
	private Double total_tax_amount;
	public TongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel() {
	}
	public long getProduct_com_id() {
		return product_com_id;
	}
	public void setProduct_com_id(long product_com_id) {
		this.product_com_id = product_com_id;
	}
	public String getPcom_code() {
		return pcom_code;
	}
	public void setPcom_code(String pcom_code) {
		this.pcom_code = pcom_code;
	}
	public String getPcom_name() {
		return pcom_name;
	}
	public void setPcom_name(String pcom_name) {
		this.pcom_name = pcom_name;
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
	public Double getQuantity_kg() {
		return quantity_kg;
	}
	public void setQuantity_kg(Double quantity_kg) {
		this.quantity_kg = quantity_kg;
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
	public Double getTotal_tax_amount() {
		return total_tax_amount;
	}
	public void setTotal_tax_amount(Double total_tax_amount) {
		this.total_tax_amount = total_tax_amount;
	}
}
