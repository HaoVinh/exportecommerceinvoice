package lixco.com.reportInfo;

import java.util.Date;

import lombok.Data;

@Data
public class SoLieuBaoCaoTongHop implements Cloneable {
	private String area_code;
	private String area_name;
	private String city_code;
	private String city_name;
	private String customer_code;
	private String customer_name;
	private String customer_type_code;
	private String customer_type_name;
	private String product_type_code;
	private String product_type_name;
	private String product_code;
	private String product_name;
	private boolean typep;
	private double quantity;
	private double total_amount;
	private double total_amount_nt;
	private String month_and_year;
	private String ie_categories_code;
	private String ie_categories_name;
	private int typept;
	private int year;
	private String pcom_code;
	private String pcom_name;
	private double total_profit;//tổng lợi nhuận
	private double factor;//hệ số quy đổi
	private double specification;
	private String customer_channel_name;
	private String pbrand_code;
	private String pbrand_name;
	private String quarter;
	private double bd_quantity;
	private double td_quantity;
	private double bn_quantity;
	private double bd_total_amount;
	private double td_total_amount;
	private double bn_total_amount;
	
	private String maspchinh;
	private String maspcombo;
	
	private long idhopdong;
	private long idctdongia;
	private double loinhuantong;
	private Date ngayhoadon;
	private Date ngaydonhang;
	private long idsp;
	
	private String sohd;
	private String quocgia;
	private String nhomkhachhang;
	
	
	
	public SoLieuBaoCaoTongHop() {
	}
	
	public String dataGroupBy() {
		return customer_code+ie_categories_code+product_code+month_and_year;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
