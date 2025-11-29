package lixco.com.reqInfo;

import lixco.com.entity.Batch;
import lixco.com.entity.ExportBatchPOD;
import lixco.com.entity.Product;
import lixco.com.reqInfo.ExportBatchPODFake.BatchPODFake;
import lixco.com.reqInfo.ExportBatchPODFake.ProductPODFake;

public class WrapExportPODDataRealReqInfo {
	private ExportBatchPOD export_batch_pod;
	private double quantity_export;
	private boolean select;
	
	public WrapExportPODDataRealReqInfo() {
	}
	
	public WrapExportPODDataRealReqInfo(ExportBatchPOD export_batch_pod, double quantity_export, boolean select) {
		this.export_batch_pod = export_batch_pod;
		this.quantity_export = quantity_export;
		this.select = select;
	}

	public WrapExportPODDataRealReqInfo(WrapExportPODDataReqInfo t) {
		ExportBatchPODFake exportBatchPODFake=t.getExport_batch_pod_fake();
		if(exportBatchPODFake !=null){
			this.export_batch_pod=new ExportBatchPOD();
			BatchPODFake batchPODFake=t.getExport_batch_pod_fake().getBatch_fake();
			if(batchPODFake !=null){
				Batch batch =new Batch();
				batch.setId(batchPODFake.getId());
				batch.setBatch_code(batchPODFake.getBatch_code());
				batch.setManufacture_date(batchPODFake.getManufacture_date());
				batch.setExpiration_date(batchPODFake.getExpiration_date());
				batch.setQuantity_import(batchPODFake.getQuantity_import());
				batch.setQuantity_export(batchPODFake.getQuantity_export());
				ProductPODFake productPODFake=batchPODFake.getProduct();
				if(productPODFake !=null){
					Product product=new Product();
					product.setId(productPODFake.getId());
					product.setProduct_code(productPODFake.getProduct_code());
					product.setProduct_name(productPODFake.getProduct_name());
					product.setSpecification(productPODFake.getSpecification());
					product.setFactor(productPODFake.getFactor());
					batch.setProduct(product);
				}
				export_batch_pod.setBatch(batch);
			}
			export_batch_pod.setQuantity(exportBatchPODFake.getQuantity());
			export_batch_pod.setSelect(exportBatchPODFake.isSelect());
		}
		this.select=t.isSelect();
		this.quantity_export=t.getQuantity_export();
	}

	public ExportBatchPOD getExport_batch_pod() {
		return export_batch_pod;
	}
	public void setExport_batch_pod(ExportBatchPOD export_batch_pod) {
		this.export_batch_pod = export_batch_pod;
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
	
}
