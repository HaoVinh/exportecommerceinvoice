package lixco.com.reportInfo;

public class ReportFormD {
	private long product_type_id;
	private String product_type_en_name;
	private long product_id;
	private String product_en_name;
	private Double total_cartons;//số lượng xuất khẩu/ specification
	private Double total_net_weight;// quantity_export* factor/1000 (MTS)
	private Double total_gross_weight;// quantity_export/specification * tare (KGS)
	private Double foreign_total_amount;//tổng số tiền ngoại tệ
	
	public ReportFormD() {
	}
	
	
	public ReportFormD(long product_type_id, String product_type_en_name, long product_id, String product_en_name,
			Double total_cartons, Double total_net_weight, Double total_gross_weight, Double foreign_total_amount) {
		this.product_type_id = product_type_id;
		this.product_type_en_name = product_type_en_name;
		this.product_id = product_id;
		this.product_en_name = product_en_name;
		this.total_cartons = total_cartons;
		this.total_net_weight = total_net_weight;
		this.total_gross_weight = total_gross_weight;
		this.foreign_total_amount = foreign_total_amount;
	}


	public long getProduct_type_id() {
		return product_type_id;
	}
	public void setProduct_type_id(long product_type_id) {
		this.product_type_id = product_type_id;
	}
	public String getProduct_type_en_name() {
		return product_type_en_name;
	}
	public void setProduct_type_en_name(String product_type_en_name) {
		this.product_type_en_name = product_type_en_name;
	}
	public long getProduct_id() {
		return product_id;
	}
	public void setProduct_id(long product_id) {
		this.product_id = product_id;
	}
	public String getProduct_en_name() {
		return product_en_name;
	}
	public void setProduct_en_name(String product_en_name) {
		this.product_en_name = product_en_name;
	}


	public Double getTotal_cartons() {
		if(total_cartons==null){
			return 0.0;
		}
		return total_cartons;
	}


	public void setTotal_cartons(Double total_cartons) {
		this.total_cartons = total_cartons;
	}


	public Double getTotal_net_weight() {
		if(total_net_weight==null){
			return 0.0;
		}
		return total_net_weight;
	}


	public void setTotal_net_weight(Double total_net_weight) {
		this.total_net_weight = total_net_weight;
	}


	public Double getTotal_gross_weight() {
		if(total_gross_weight==null){
			return 0.0;
		}
		return total_gross_weight;
	}


	public void setTotal_gross_weight(Double total_gross_weight) {
		this.total_gross_weight = total_gross_weight;
	}


	public Double getForeign_total_amount() {
		if(foreign_total_amount==null){
			return 0.0;
		}
		return foreign_total_amount;
	}


	public void setForeign_total_amount(Double foreign_total_amount) {
		this.foreign_total_amount = foreign_total_amount;
	}
}
