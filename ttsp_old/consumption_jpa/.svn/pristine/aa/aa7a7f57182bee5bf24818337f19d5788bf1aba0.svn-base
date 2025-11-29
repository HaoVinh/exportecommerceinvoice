package lixco.com.reqInfo;

import java.util.Date;


public class ExportBatchPODFake {
	private long id;
	private BatchPODFake batch_fake;
	private double quantity;//số lượng đơn vị tính
	private boolean select;
	public ExportBatchPODFake() {
	}
	
	public ExportBatchPODFake(long id,long batchId,String batchCode,double quantityImport,double quantityExport,Date manufactureDate,Date expirationDate,double remainQuantity,double quantity,
			long productId,String productCode,String productName,double specification,double factor) {
		this.id = id;
		this.batch_fake = new BatchPODFake(batchId, batchCode, quantityImport, quantityExport, manufactureDate, expirationDate,remainQuantity,
				productId,productCode,productName,specification,factor);
		this.quantity = quantity;
	}
	public static class ProductPODFake{
		private long id;
		private String product_code;
		private String product_name;
		private double specification;
		private double factor;
		
		public ProductPODFake() {
		}
		
		public ProductPODFake(long id, String product_code, String product_name, double specification, double factor) {
			this.id = id;
			this.product_code = product_code;
			this.product_name = product_name;
			this.specification = specification;
			this.factor = factor;
		}

		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getProduct_code() {
			return product_code;
		}
		public void setProduct_code(String product_code) {
			this.product_code = product_code;
		}
		public String getProduct_name() {
			return product_name;
		}
		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}
		public double getSpecification() {
			return specification;
		}
		public void setSpecification(double specification) {
			this.specification = specification;
		}
		public double getFactor() {
			return factor;
		}
		public void setFactor(double factor) {
			this.factor = factor;
		}
	}
	public static class BatchPODFake{
		private long id;
		private String batch_code;
		private double quantity_import;//số lượng
		private double quantity_export;//số lượng xuất
		private Date manufacture_date;//ngày sản xuất 
		private Date expiration_date;//ngày hết hạn
		private double remain_quantity;
		private ProductPODFake product;
		
		
		public BatchPODFake() {
		}
		
		public BatchPODFake(long id, String batch_code, double quantity_import, double quantity_export,
				Date manufacture_date, Date expiration_date,double remain_quatity,
				long productId,String productCode,String productName,double specification,double factor) {
			this.id = id;
			this.batch_code = batch_code;
			this.quantity_import = quantity_import;
			this.quantity_export = quantity_export;
			this.manufacture_date = manufacture_date;
			this.expiration_date = expiration_date;
			this.remain_quantity=remain_quatity;
			this.product=new ProductPODFake(productId, productCode, productName, specification, factor);
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getBatch_code() {
			return batch_code;
		}

		public void setBatch_code(String batch_code) {
			this.batch_code = batch_code;
		}

		public double getQuantity_import() {
			return quantity_import;
		}

		public void setQuantity_import(double quantity_import) {
			this.quantity_import = quantity_import;
		}

		public double getQuantity_export() {
			return quantity_export;
		}

		public void setQuantity_export(double quantity_export) {
			this.quantity_export = quantity_export;
		}

		public Date getManufacture_date() {
			return manufacture_date;
		}

		public void setManufacture_date(Date manufacture_date) {
			this.manufacture_date = manufacture_date;
		}

		public Date getExpiration_date() {
			return expiration_date;
		}

		public void setExpiration_date(Date expiration_date) {
			this.expiration_date = expiration_date;
		}

		public double getRemain_quantity() {
			return remain_quantity;
		}

		public void setRemain_quantity(double remain_quantity) {
			this.remain_quantity = remain_quantity;
		}

		public ProductPODFake getProduct() {
			return product;
		}

		public void setProduct(ProductPODFake product) {
			this.product = product;
		}
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BatchPODFake getBatch_fake() {
		return batch_fake;
	}

	public void setBatch_fake(BatchPODFake batch_fake) {
		this.batch_fake = batch_fake;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}
}
