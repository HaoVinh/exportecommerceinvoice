package lixco.com.reportInfo;

import java.util.Date;

import lombok.Data;
@Data
public class BangKeKhoiLuongVanChuyen {
	private long car_id;
	private String license_plate;
	private String driver;
	private long invoice_id;
	private Date invoice_date;
	private String invoice_code;
	private long product_type_id;
	private String product_type_code;
	private String product_type_name;
	private int typept;
	private long customer_id;
	private String customer_code;
	private String customer_name;
	private long city_id;
	private String city_code;
	private String city_name;
	private Double kg_quantity;
	private String carrier_name;//ngươi van chuyen
	
	public BangKeKhoiLuongVanChuyen() {
	}

}
