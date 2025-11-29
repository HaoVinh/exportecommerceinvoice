package lixco.com.reqfox;

public class Car {
	private long id;
	private String soxe;
	private String tenxe;
	private String nhomxe;
	private String masochuxe;
	private String dienthoai;
	private String idfox;
	public Car() {
	}
	
	public Car(long id,String soxe, String tenxe, String nhomxe, String masochuxe, String dienthoai,String idfox) {
		this.id=id;
		this.soxe = soxe;
		this.tenxe = tenxe;
		this.nhomxe = nhomxe;
		this.masochuxe = masochuxe;
		this.dienthoai = dienthoai;
		this.idfox=idfox;
	}

	public String getSoxe() {
		return soxe;
	}
	public void setSoxe(String soxe) {
		this.soxe = soxe;
	}
	public String getTenxe() {
		return tenxe;
	}
	public void setTenxe(String tenxe) {
		this.tenxe = tenxe;
	}
	public String getNhomxe() {
		return nhomxe;
	}
	public void setNhomxe(String nhomxe) {
		this.nhomxe = nhomxe;
	}
	public String getMasochuxe() {
		return masochuxe;
	}
	public void setMasochuxe(String masochuxe) {
		this.masochuxe = masochuxe;
	}
	public String getDienthoai() {
		return dienthoai;
	}
	public void setDienthoai(String dienthoai) {
		this.dienthoai = dienthoai;
	}

	public String getIdfox() {
		return idfox;
	}

	public void setIdfox(String idfox) {
		this.idfox = idfox;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
