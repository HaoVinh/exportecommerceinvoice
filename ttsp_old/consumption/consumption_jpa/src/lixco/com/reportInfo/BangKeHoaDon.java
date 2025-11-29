package lixco.com.reportInfo;

import java.util.Date;


public class BangKeHoaDon {
	private long invoice_id;
	private String voucher_code;
	private String invoice_code;
	private Date invoice_date;
	private long customer_id;
	private String customer_code;
	private String customer_name;
	private String address;
	private String company_name;
	private long ie_categories_id;
	private String ie_categories_code;
	private String ie_categories_name;
	private Double total_amount;
	private Double total_tax_amount;
	private Double total_amount_with_vat;
	private long created_by_id;
	private String created_by;
	private String po;
	
	public BangKeHoaDon() {
	}

	public long getInvoice_id() {
		return invoice_id;
	}

	public void setInvoice_id(long invoice_id) {
		this.invoice_id = invoice_id;
	}
	public String getVoucher_code() {
		return voucher_code;
	}

	public void setVoucher_code(String voucher_code) {
		this.voucher_code = voucher_code;
	}

	public String getInvoice_code() {
		return invoice_code;
	}

	public void setInvoice_code(String invoice_code) {
		this.invoice_code = invoice_code;
	}

	public Date getInvoice_date() {
		return invoice_date;
	}

	public void setInvoice_date(Date invoice_date) {
		this.invoice_date = invoice_date;
	}

	public long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_code() {
		return customer_code;
	}

	public void setCustomer_code(String customer_code) {
		this.customer_code = customer_code;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
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

	public Double getTotal_amount_with_vat() {
		return total_amount_with_vat;
	}

	public void setTotal_amount_with_vat(Double total_amount_with_vat) {
		this.total_amount_with_vat = total_amount_with_vat;
	}

	public long getCreated_by_id() {
		return created_by_id;
	}

	public void setCreated_by_id(long created_by_id) {
		this.created_by_id = created_by_id;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getPo() {
		return po;
	}

	public void setPo(String po) {
		this.po = po;
	}

	
	
}
