package lixco.com.reqInfo;

import lombok.Data;

@Data
public class ExpectedInventoryReqInfoSub {
	private long product_id;
	private String product_code;
	private String product_name;
	private double inv_quantity;// tồn kho
	private double exp_export_quantity;// dư xuat
	private double exp_export_quantityXK;// dư xuat xuat khau
	private double exp_closing_quantity;// du ton cuoi
	private double av30_quantity;// binh quan 30 ngay

	private double hdhopdongxuatkhau;// hoa don xuat khau
	private double tonghopdong;// tong hop dong xuat khau
	
	private double factor;

	public ExpectedInventoryReqInfoSub() {
	}

	public ExpectedInventoryReqInfoSub(long product_id, String product_code, String product_name,double factor) {
		this.product_id = product_id;
		this.product_code = product_code;
		this.product_name = product_name;
		this.factor=factor;
	}

	public ExpectedInventoryReqInfoSub(long product_id, String product_code, String product_name, double inv_quantity,
			double exp_export_quantity, double exp_closing_quantity, double av30_quantity, double hdhopdongxuatkhau,
			double tonghopdong, double factor) {
		this.product_id = product_id;
		this.product_code = product_code;
		this.product_name = product_name;
		this.inv_quantity = inv_quantity;

		this.exp_export_quantity = exp_export_quantity;
		if (this.exp_export_quantity < 0)
			this.exp_export_quantity = 0;
		this.exp_export_quantityXK = tonghopdong - hdhopdongxuatkhau;
		if (this.exp_export_quantityXK < 0)
			this.exp_export_quantityXK = 0;

		this.exp_closing_quantity = this.inv_quantity - this.exp_export_quantity - this.exp_export_quantityXK;
		this.av30_quantity = av30_quantity;
		
		
		this.hdhopdongxuatkhau = hdhopdongxuatkhau;
		this.tonghopdong = tonghopdong;
		
		this.factor=factor;
	}

}
