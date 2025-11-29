package lixco.com.reportInfo;

import java.util.Date;

import lombok.Data;
@Data
public class TheKhoSanPham implements Cloneable {
	private int data_month;
	private int data_year;
	private long product_id;
	private String product_code;
	private String product_name;
	private Date data_date;
	private String data_code;
	private long customer_id;
	private String customer_code;
	private String customer_name;
	private Double kg_import_quantity;
	private Double kg_export_quantity;
	private Double kg_ivn_quantity;
	private int typeie;
	private String batch_code;
	private Double kg_ivn_quantityFirst;//ton dau thang
	private Double kg_ivn_quantityLast;//ton cuoi thang
	private Double kg_ivn_quantityLastNow;//cong don tu dau den hien thai
	private double factor;
	private String carrierName;
	private String poNo;
	
	public TheKhoSanPham() {
	}

	public TheKhoSanPham(int data_month, int data_year, long product_id,String product_code,String product_name, int typeie,String customer_name) {
		this.data_month = data_month;
		this.data_year = data_year;
		this.product_id = product_id;
		this.product_code=product_code;
		this.product_name=product_name;
		this.typeie = typeie;
		this.customer_name=customer_name;
	}

	public TheKhoSanPham(int data_month, int data_year, long product_id,String product_code,String product_name, Double kg_import_quantity, Double kg_export_quantity,
			Double kg_ivn_quantity, int typeie,String customer_name,String carrierName, String poNo) {
		this.data_month = data_month;
		this.data_year = data_year;
		this.product_id = product_id;
		this.product_code=product_code;
		this.product_name=product_name;
		this.kg_import_quantity = kg_import_quantity;
		this.kg_export_quantity = kg_export_quantity;
		this.kg_ivn_quantity = kg_ivn_quantity;
		this.typeie = typeie;
		this.customer_name=customer_name;
		this.carrierName=carrierName;
		this.poNo=poNo;
	}

	

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TheKhoSanPham other = (TheKhoSanPham) obj;
		if (batch_code == null) {
			if (other.batch_code != null)
				return false;
		} else if (!batch_code.equals(other.batch_code))
			return false;
		if (carrierName == null) {
			if (other.carrierName != null)
				return false;
		} else if (!carrierName.equals(other.carrierName))
			return false;
		if (customer_code == null) {
			if (other.customer_code != null)
				return false;
		} else if (!customer_code.equals(other.customer_code))
			return false;
		if (customer_id != other.customer_id)
			return false;
		if (customer_name == null) {
			if (other.customer_name != null)
				return false;
		} else if (!customer_name.equals(other.customer_name))
			return false;
		if (data_code == null) {
			if (other.data_code != null)
				return false;
		} else if (!data_code.equals(other.data_code))
			return false;
		if (data_date == null) {
			if (other.data_date != null)
				return false;
		} else if (!data_date.equals(other.data_date))
			return false;
		if (data_month != other.data_month)
			return false;
		if (data_year != other.data_year)
			return false;
		if (Double.doubleToLongBits(factor) != Double.doubleToLongBits(other.factor))
			return false;
		if (kg_export_quantity == null) {
			if (other.kg_export_quantity != null)
				return false;
		} else if (!kg_export_quantity.equals(other.kg_export_quantity))
			return false;
		if (kg_import_quantity == null) {
			if (other.kg_import_quantity != null)
				return false;
		} else if (!kg_import_quantity.equals(other.kg_import_quantity))
			return false;
		if (kg_ivn_quantity == null) {
			if (other.kg_ivn_quantity != null)
				return false;
		} else if (!kg_ivn_quantity.equals(other.kg_ivn_quantity))
			return false;
		if (kg_ivn_quantityFirst == null) {
			if (other.kg_ivn_quantityFirst != null)
				return false;
		} else if (!kg_ivn_quantityFirst.equals(other.kg_ivn_quantityFirst))
			return false;
		if (kg_ivn_quantityLast == null) {
			if (other.kg_ivn_quantityLast != null)
				return false;
		} else if (!kg_ivn_quantityLast.equals(other.kg_ivn_quantityLast))
			return false;
		if (kg_ivn_quantityLastNow == null) {
			if (other.kg_ivn_quantityLastNow != null)
				return false;
		} else if (!kg_ivn_quantityLastNow.equals(other.kg_ivn_quantityLastNow))
			return false;
		if (poNo == null) {
			if (other.poNo != null)
				return false;
		} else if (!poNo.equals(other.poNo))
			return false;
		if (product_code == null) {
			if (other.product_code != null)
				return false;
		} else if (!product_code.equals(other.product_code))
			return false;
		if (product_id != other.product_id)
			return false;
		if (product_name == null) {
			if (other.product_name != null)
				return false;
		} else if (!product_name.equals(other.product_name))
			return false;
		if (typeie != other.typeie)
			return false;
		return true;
	}

	

	
}
