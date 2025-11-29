package lixco.com.entityapi;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Data;

@Data
public class ProductKTDTO {
	private Long id;
	private String maSP;
	private String tenSP;
	private double soLuongDau;
	private double soLuongNhap;
	private double soLuongXuat;
	private double soLuongCuoi;

	public ProductKTDTO(Long id, String maSP, String tenSP, double soLuongDau, double soLuongNhap, double soLuongXuat,
			double soLuongCuoi) {
		super();
		this.id = id;
		this.maSP = maSP;
		this.tenSP = tenSP;
		this.soLuongDau = roundToTwoDecimalPlaces(soLuongDau);
		this.soLuongNhap = roundToTwoDecimalPlaces(soLuongNhap);
		this.soLuongXuat = roundToTwoDecimalPlaces(soLuongXuat);
		this.soLuongCuoi = roundToTwoDecimalPlaces(soLuongCuoi);
	}

// Getter & Setter (Nếu cần)

// Làm tròn 2 chữ số thập phân
	private double roundToTwoDecimalPlaces(double value) {
		return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	public ProductKTDTO() {
		super();
	}

}
