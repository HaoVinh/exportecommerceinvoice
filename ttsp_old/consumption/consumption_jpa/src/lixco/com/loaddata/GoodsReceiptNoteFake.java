package lixco.com.loaddata;

import java.util.Date;

import lixco.com.entity.Customer;
import lixco.com.entity.IECategories;
import lixco.com.entity.Product;
import lixco.com.entity.Warehouse;

public class GoodsReceiptNoteFake{
	private String voucher_code;
	private Date import_date;
	private Customer customer;
	private Warehouse warehouse;
	private IECategories ie_categories;
	private Product product;
	private double quantity;
	private double unit_price;
	private double total;
	private String vcnb_invoice_code;
	private String license_plate;
	private String batch_code;
	private String sogiaonhan;
	private String movement_commands;// sooes lệnh điều động
	private String note;
	private String idfox;
	
	
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getSogiaonhan() {
		return sogiaonhan;
	}
	public void setSogiaonhan(String sogiaonhan) {
		this.sogiaonhan = sogiaonhan;
	}
	public String getVoucher_code() {
		return voucher_code;
	}
	public void setVoucher_code(String voucher_code) {
		this.voucher_code = voucher_code;
	}
	public Date getImport_date() {
		return import_date;
	}
	public void setImport_date(Date import_date) {
		this.import_date = import_date;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Warehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}
	public IECategories getIe_categories() {
		return ie_categories;
	}
	public void setIe_categories(IECategories ie_categories) {
		this.ie_categories = ie_categories;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}
	public String getVcnb_invoice_code() {
		return vcnb_invoice_code;
	}
	public void setVcnb_invoice_code(String vcnb_invoice_code) {
		this.vcnb_invoice_code = vcnb_invoice_code;
	}
	public String getLicense_plate() {
		return license_plate;
	}
	public void setLicense_plate(String license_plate) {
		this.license_plate = license_plate;
	}
	public String getBatch_code() {
		return batch_code;
	}
	public void setBatch_code(String batch_code) {
		this.batch_code = batch_code;
	}
	public String getMovement_commands() {
		return movement_commands;
	}
	public void setMovement_commands(String movement_commands) {
		this.movement_commands = movement_commands;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getIdfox() {
		return idfox;
	}
	public void setIdfox(String idfox) {
		this.idfox = idfox;
	}

	public static class KeyPhieuNhap{
		private String voucherCode;
		private Date importDate;
		private Customer customer;
		private Warehouse warehouse;
		private IECategories ie_categories;
		private String batch_code;
		
		public KeyPhieuNhap() {
		}
		
		public KeyPhieuNhap(String voucherCode, Date importDate, Customer customer, Warehouse warehouse,
				IECategories ie_categories,String batch_code) {
			this.voucherCode = voucherCode;
			this.importDate = importDate;
			this.customer = customer;
			this.warehouse = warehouse;
			this.ie_categories = ie_categories;
			this.batch_code=batch_code;
		}

		public String getVoucherCode() {
			return voucherCode;
		}
		public void setVoucherCode(String voucherCode) {
			this.voucherCode = voucherCode;
		}
		public Date getImportDate() {
			return importDate;
		}
		public void setImportDate(Date importDate) {
			this.importDate = importDate;
		}
		public Customer getCustomer() {
			return customer;
		}
		public void setCustomer(Customer customer) {
			this.customer = customer;
		}
		public Warehouse getWarehouse() {
			return warehouse;
		}
		public void setWarehouse(Warehouse warehouse) {
			this.warehouse = warehouse;
		}
		public IECategories getIe_categories() {
			return ie_categories;
		}
		public void setIe_categories(IECategories ie_categories) {
			this.ie_categories = ie_categories;
		}
		public String getBatch_code() {
			return batch_code;
		}

		public void setBatch_code(String batch_code) {
			this.batch_code = batch_code;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((customer == null) ? 0 : customer.hashCode());
			result = prime * result + ((ie_categories == null) ? 0 : ie_categories.hashCode());
			result = prime * result + ((importDate == null) ? 0 : importDate.hashCode());
			result = prime * result + ((voucherCode == null) ? 0 : voucherCode.hashCode());
			result = prime * result + ((warehouse == null) ? 0 : warehouse.hashCode());
			if(customer==null){
			}else if("N".equals(customer.getCustomer_code())){
				result=prime*result+((batch_code == null) ? 0 : batch_code.hashCode());
			}
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			KeyPhieuNhap other = (KeyPhieuNhap) obj;
			if (customer == null) {
				if (other.customer != null)
					return false;
			} else if (!customer.equals(other.customer))
				return false;
			if (ie_categories == null) {
				if (other.ie_categories != null)
					return false;
			} else if (!ie_categories.equals(other.ie_categories))
				return false;
			if (importDate == null) {
				if (other.importDate != null)
					return false;
			} else if (!importDate.equals(other.importDate))
				return false;
			if (voucherCode == null) {
				if (other.voucherCode != null)
					return false;
			} else if (!voucherCode.equals(other.voucherCode))
				return false;
			if (warehouse == null) {
				if (other.warehouse != null)
					return false;
			} else if (!warehouse.equals(other.warehouse))
				return false;
			return true;
		}

		
	}
}
