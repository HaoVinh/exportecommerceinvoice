package lixco.com.reqInfo;


public class WrapExportODDataReqInfo {
	private ExportBatchODFake export_batch_od_fake;
	private double quantity_export;// số lượng xuất đvt
	private double box_quantity_export;//số lượng xuất thùng
	private boolean select;
	
	public WrapExportODDataReqInfo() {
	}
	
	public WrapExportODDataReqInfo(ExportBatchODFake export_batch_od_fake, double quantity_export,double box_quantity_export,boolean select) {
		this.export_batch_od_fake = export_batch_od_fake;
		this.quantity_export = quantity_export;
		this.box_quantity_export=box_quantity_export;
		this.select = select;
	}
	public ExportBatchODFake getExport_batch_od_fake() {
		return export_batch_od_fake;
	}

	public void setExport_batch_od_fake(ExportBatchODFake export_batch_od_fake) {
		this.export_batch_od_fake = export_batch_od_fake;
	}

	public double getQuantity_export() {
		return quantity_export;
	}

	public void setQuantity_export(double quantity_export) {
		this.quantity_export = quantity_export;
	}

	public double getBox_quantity_export() {
		return box_quantity_export;
	}

	public void setBox_quantity_export(double box_quantity_export) {
		this.box_quantity_export = box_quantity_export;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}
}
