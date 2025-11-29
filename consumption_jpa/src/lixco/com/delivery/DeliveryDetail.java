package lixco.com.delivery;

public class DeliveryDetail {
	private long id;
	private ProductDelivery productLix;
	private double number;
	private String str_pallet;
	private String lot;
	
	public DeliveryDetail() {
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public ProductDelivery getProductLix() {
		return productLix;
	}
	public void setProductLix(ProductDelivery productLix) {
		this.productLix = productLix;
	}
	public double getNumber() {
		return number;
	}
	public void setNumber(double number) {
		this.number = number;
	}
	public String getStr_pallet() {
		return str_pallet;
	}
	public void setStr_pallet(String str_pallet) {
		this.str_pallet = str_pallet;
	}
	
	public String getLot() {
		return lot;
	}
	public void setLot(String lot) {
		this.lot = lot;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeliveryDetail other = (DeliveryDetail) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
