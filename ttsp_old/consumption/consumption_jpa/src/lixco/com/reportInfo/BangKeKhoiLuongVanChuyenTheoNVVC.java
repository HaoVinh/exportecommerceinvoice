package lixco.com.reportInfo;

import java.util.Date;

public class BangKeKhoiLuongVanChuyenTheoNVVC {
	private long car_id;
	private String license_plate;
	private Date invoice_date;
	private String invoice_code;
	private long customer_id;
	private String customer_code;
	private String customer_name;
	private long carrier_id;
	private String carrier_code;
	private String carrier_name;
	private Double kg_quantity;
	
	public BangKeKhoiLuongVanChuyenTheoNVVC() {
	}
	public long getCar_id() {
		return car_id;
	}
	public void setCar_id(long car_id) {
		this.car_id = car_id;
	}
	public String getLicense_plate() {
		return license_plate;
	}
	public void setLicense_plate(String license_plate) {
		this.license_plate = license_plate;
	}
	public Date getInvoice_date() {
		return invoice_date;
	}
	public void setInvoice_date(Date invoice_date) {
		this.invoice_date = invoice_date;
	}
	public String getInvoice_code() {
		return invoice_code;
	}
	public void setInvoice_code(String invoice_code) {
		this.invoice_code = invoice_code;
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
	public long getCarrier_id() {
		return carrier_id;
	}
	public void setCarrier_id(long carrier_id) {
		this.carrier_id = carrier_id;
	}
	public String getCarrier_code() {
		return carrier_code;
	}
	public void setCarrier_code(String carrier_code) {
		this.carrier_code = carrier_code;
	}
	public String getCarrier_name() {
		return carrier_name;
	}
	public void setCarrier_name(String carrier_name) {
		this.carrier_name = carrier_name;
	}
	public Double getKg_quantity() {
		return kg_quantity;
	}
	public void setKg_quantity(Double kg_quantity) {
		this.kg_quantity = kg_quantity;
	}
}
