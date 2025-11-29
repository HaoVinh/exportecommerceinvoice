package lixco.com.entityapi;

import lombok.Data;

@Data
public class SanPhamData2 {
	private String productBrandCode;
	private String productBrandName;
	private String productComCode;
	private String productComName;
	private String productCode;
	private String productName;
	private String productTypeCode;
	private String productTypeName;
	private String leverCode;
	private double quycach;
	private double trongluonggoi;
	
	public SanPhamData2(String productBrandCode, String productBrandName, String productComCode, String productComName,
			String productCode, String productName, String productTypeCode, String productTypeName, String leverCode) {
		super();
		this.productBrandCode = productBrandCode;
		this.productBrandName = productBrandName;
		this.productComCode = productComCode;
		this.productComName = productComName;
		this.productCode = productCode;
		this.productName = productName;
		this.productTypeCode = productTypeCode;
		this.productTypeName = productTypeName;
		this.leverCode = leverCode;
	}
	public SanPhamData2(String productBrandCode, String productBrandName, String productComCode, String productComName,
			String productCode, String productName, String productTypeCode, String productTypeName, String leverCode, double quycach) {
		super();
		this.productBrandCode = productBrandCode;
		this.productBrandName = productBrandName;
		this.productComCode = productComCode;
		this.productComName = productComName;
		this.productCode = productCode;
		this.productName = productName;
		this.productTypeCode = productTypeCode;
		this.productTypeName = productTypeName;
		this.leverCode = leverCode;
		this.quycach = quycach;
	}
	public SanPhamData2(String productBrandCode, String productBrandName, String productComCode, String productComName,
			String productCode, String productName, String productTypeCode, String productTypeName, String leverCode, double quycach,double trongluonggoi) {
		super();
		this.productBrandCode = productBrandCode;
		this.productBrandName = productBrandName;
		this.productComCode = productComCode;
		this.productComName = productComName;
		this.productCode = productCode;
		this.productName = productName;
		this.productTypeCode = productTypeCode;
		this.productTypeName = productTypeName;
		this.leverCode = leverCode;
		this.quycach = quycach;
		this.trongluonggoi=trongluonggoi;
	}
}
