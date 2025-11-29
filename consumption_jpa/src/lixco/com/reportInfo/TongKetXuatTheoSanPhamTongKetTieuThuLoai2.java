package lixco.com.reportInfo;

public class TongKetXuatTheoSanPhamTongKetTieuThuLoai2 {
	private long product_type_id;
	private String product_type_name;
	private long product_com_id;
	private String product_com_name;
	private long ie_categories_id;
	private String ie_categories_name;
	private double quantity;//số lượng kg
//	private double unit_price;
	private double total_amount;
	private double total_tax_amount;
	
	public TongKetXuatTheoSanPhamTongKetTieuThuLoai2() {
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

	public long getProduct_com_id() {
		return product_com_id;
	}

	public void setProduct_com_id(long product_com_id) {
		this.product_com_id = product_com_id;
	}

	public String getProduct_com_name() {
		return product_com_name;
	}

	public void setProduct_com_name(String product_com_name) {
		this.product_com_name = product_com_name;
	}

	public long getIe_categories_id() {
		return ie_categories_id;
	}

	public void setIe_categories_id(long ie_categories_id) {
		this.ie_categories_id = ie_categories_id;
	}

	public String getIe_categories_name() {
		return ie_categories_name;
	}

	public void setIe_categories_name(String ie_categories_name) {
		this.ie_categories_name = ie_categories_name;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}

	public double getTotal_tax_amount() {
		return total_tax_amount;
	}

	public void setTotal_tax_amount(double total_tax_amount) {
		this.total_tax_amount = total_tax_amount;
	}
	
}
