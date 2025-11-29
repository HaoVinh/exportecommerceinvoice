package lixco.com.reqInfo;

import lombok.Data;
@Data
public class ExpectedInventoryReqInfo {
	private long product_id;
	private String product_code;
	private String product_name;
	private double inv_quantity;// tồn kho
	private double exp_export_quantity;// dư xuat
	
	private double exp_export_quantityGT;// dư xuat GT
	private double exp_export_quantityKHAC;// dư xuat KHAC
	
	private double exp_closing_quantity;// du ton cuoi
	private double av30_quantity;// binh quan 30 ngay
	private double hdhopdongxuatkhau;// binh quan 30 ngay
	private double tonghopdong;// binh quan 30 ngay

	private long product_id_cn;
	private double inv_quantityBD;// tồn kho
	private double exp_export_quantityBD;// dư xuat BD
	
	private double exp_export_quantityBDGT;// dư xuat BD
	private double exp_export_quantityBDKHAC;// dư xuat BD
	
	private double exp_closing_quantityBD;// du ton cuoi
	private double av30_quantityBD;// binh quan 30 ngay
	private double hdhopdongxuatkhauBD;// binh quan 30 ngay
	private double tonghopdongBD;// binh quan 30 ngay

	private boolean dagoptemp;// bien tam danh dau da gop
	
	
	public ExpectedInventoryReqInfo() {
	}
	public ExpectedInventoryReqInfo(long product_id, String product_code, String product_name) {
		this.product_id = product_id;
		this.product_code = product_code;
		this.product_name = product_name;
	}
	public ExpectedInventoryReqInfo(long product_id, String product_code, String product_name, double inv_quantity,
			double exp_export_quantity, double exp_closing_quantity, double av30_quantity, double hdhopdongxuatkhau,
			double tonghopdong) {
		this.product_id = product_id;
		this.product_code = product_code;
		this.product_name = product_name;
		this.inv_quantity = inv_quantity;
		
		this.exp_export_quantity = exp_export_quantity + hdhopdongxuatkhau - tonghopdong;
		if (this.exp_export_quantity < 0)
			this.exp_export_quantity = 0;
		double tonxuatkhau = tonghopdong - hdhopdongxuatkhau;
		if (tonxuatkhau < 0)
			tonxuatkhau = 0;
		this.exp_closing_quantity = this.inv_quantity - (this.exp_export_quantity + tonxuatkhau);
		this.av30_quantity = av30_quantity;
		this.hdhopdongxuatkhau = hdhopdongxuatkhau;
		this.tonghopdong = tonghopdong;
	}

	public ExpectedInventoryReqInfo(long product_id, String product_code, String product_name, double inv_quantityBD,
			double exp_export_quantityBD, double exp_closing_quantityBD, double av30_quantityBD,
			double hdhopdongxuatkhauBD, double tonghopdongBD, boolean dagoptemp, long product_id_cn) {
		super();
		this.product_id = product_id;
		this.product_code = product_code;
		this.product_name = product_name;
		this.inv_quantityBD = inv_quantityBD;
		this.exp_export_quantityBD = exp_export_quantityBD;
		this.exp_closing_quantityBD = this.inv_quantityBD - this.exp_export_quantityBD;
		this.av30_quantityBD = av30_quantityBD;
		this.hdhopdongxuatkhauBD = hdhopdongxuatkhauBD;
		this.tonghopdongBD = tonghopdongBD;
		this.dagoptemp = dagoptemp;
		this.product_id_cn = product_id_cn;
	}
	
	
	public double getInv_quantityTH() {
		return inv_quantity+inv_quantityBD;
	}
	public double getExp_export_quantityTH() {
		double tonxuatkhau = tonghopdong - hdhopdongxuatkhau;
		return exp_export_quantity+exp_export_quantityBD+ tonxuatkhau;
	}
	public double getExp_closing_quantityTH() {
		return getInv_quantityTH()-getExp_export_quantityTH();
	}
	public double getAv30_quantityTH() {
		return (av30_quantity+av30_quantityBD);
	}
	public double getHdhopdongxuatkhauTH() {
		return hdhopdongxuatkhau+hdhopdongxuatkhauBD;
	}
	public double getTonghopdongTH() {
		return tonghopdong+tonghopdongBD;
	}
}
