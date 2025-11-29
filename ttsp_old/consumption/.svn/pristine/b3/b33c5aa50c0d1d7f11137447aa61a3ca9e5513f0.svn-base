package lixco.com.reqInfo;

import lixco.com.entity.Batch;
import lixco.com.entity.ExportBatchOD;
import lixco.com.entity.Product;
import lixco.com.reqInfo.ExportBatchODFake.BatchFake;
import lixco.com.reqInfo.ExportBatchODFake.ProductFake;

public class WrapExportODDataRealReqInfo {
	private ExportBatchOD export_batch_od;
	private double quantity_export;
	private double box_quantity_export;
	private boolean select;
	
	public WrapExportODDataRealReqInfo() {
	}
	
	public WrapExportODDataRealReqInfo(ExportBatchOD export_batch_od, double quantity_export, boolean select) {
		this.export_batch_od = export_batch_od;
		this.quantity_export = quantity_export;
		this.select = select;
	}

	public WrapExportODDataRealReqInfo(WrapExportODDataReqInfo t) {
		ExportBatchODFake exportBatchODFake=t.getExport_batch_od_fake();
		if(exportBatchODFake !=null){
			this.export_batch_od=new ExportBatchOD();
			BatchFake batchFake=t.getExport_batch_od_fake().getBatch_fake();
			if(batchFake !=null){
				Batch batch =new Batch();
				batch.setId(batchFake.getId());
				batch.setBatch_code(batchFake.getBatch_code());
				batch.setManufacture_date(batchFake.getManufacture_date());
				batch.setExpiration_date(batchFake.getExpiration_date());
				batch.setQuantity_import(batchFake.getQuantity_import());
				batch.setQuantity_export(batchFake.getQuantity_export());
				ProductFake productFake=batchFake.getProduct();
				if(productFake !=null){
					Product product=new Product();
					product.setId(productFake.getId());
					product.setProduct_code(productFake.getProduct_code());
					product.setProduct_name(productFake.getProduct_name());
					product.setSpecification(productFake.getSpecification());
					product.setFactor(productFake.getFactor());
					batch.setProduct(product);
				}
				export_batch_od.setBatch(batch);
			}
			export_batch_od.setBox_quantity(exportBatchODFake.getBox_quantity());
			export_batch_od.setQuantity(exportBatchODFake.getQuantity());
			export_batch_od.setSelect(exportBatchODFake.isSelect());
		}
		this.quantity_export=t.getQuantity_export();
		this.box_quantity_export=t.getBox_quantity_export();
		this.select=t.isSelect();
	}

	public ExportBatchOD getExport_batch_od() {
		return export_batch_od;
	}
	public void setExport_batch_od(ExportBatchOD export_batch_od) {
		this.export_batch_od = export_batch_od;
	}
	public double getQuantity_export() {
		return quantity_export;
	}
	public void setQuantity_export(double quantity_export) {
		this.quantity_export = quantity_export;
	}
	public boolean isSelect() {
		return select;
	}
	public void setSelect(boolean select) {
		this.select = select;
	}

	public double getBox_quantity_export() {
		return box_quantity_export;
	}

	public void setBox_quantity_export(double box_quantity_export) {
		this.box_quantity_export = box_quantity_export;
	}
	
}
