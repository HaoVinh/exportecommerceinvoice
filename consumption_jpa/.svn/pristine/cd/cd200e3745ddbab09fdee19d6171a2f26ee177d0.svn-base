package lixco.com.reqInfo;

import java.util.ArrayList;
import java.util.List;


import lixco.com.entity.ExportBatch;
import lixco.com.entity.InvoiceDetail;

public class WrapInvoiceDetailReqInfo implements Cloneable{
	private long id;
	private InvoiceDetail invoice_detail=null;
	private List<ExportBatch> list_export_batch=null;
	private double inv_quantity;//tồn kho
	private double inv_quantity_cal;//Dự báo tồn.
	public WrapInvoiceDetailReqInfo() {
	}
	public WrapInvoiceDetailReqInfo(InvoiceDetail invoice_detail, List<ExportBatch> list_export_batch) {
		this.id=invoice_detail.getId();
		this.invoice_detail = invoice_detail;
		this.list_export_batch = list_export_batch;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public WrapInvoiceDetailReqInfo(InvoiceDetail invoice_detail) {
		this.id=invoice_detail.getId();
		this.invoice_detail = invoice_detail;
	}
	public InvoiceDetail getInvoice_detail() {
		return invoice_detail;
	}
	public void setInvoice_detail(InvoiceDetail invoice_detail) {
		this.invoice_detail = invoice_detail;
	}
	public List<ExportBatch> getList_export_batch() {
		return list_export_batch;
	}
	public void setList_export_batch(List<ExportBatch> list_export_batch) {
		this.list_export_batch = list_export_batch;
	}
	public double getInv_quantity() {
		return inv_quantity;
	}
	public void setInv_quantity(double inv_quantity) {
		this.inv_quantity = inv_quantity;
	}
	public double getInv_quantity_cal() {
		return inv_quantity_cal;
	}
	public void setInv_quantity_cal(double inv_quantity_cal) {
		this.inv_quantity_cal = inv_quantity_cal;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((invoice_detail == null) ? 0 : invoice_detail.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WrapInvoiceDetailReqInfo other = (WrapInvoiceDetailReqInfo) obj;
		if (invoice_detail == null) {
			if (other.invoice_detail != null)
				return false;
		} else if (!invoice_detail.equals(other.invoice_detail))
			return false;
		return true;
	}
	@Override
	public WrapInvoiceDetailReqInfo clone() throws CloneNotSupportedException {
		WrapInvoiceDetailReqInfo w=new WrapInvoiceDetailReqInfo();
		w.setInvoice_detail(this.invoice_detail==null ? null :invoice_detail.clone());
		if(list_export_batch !=null){
			List<ExportBatch> list=new ArrayList<ExportBatch>();
			w.setList_export_batch(list);
			for(ExportBatch ex:list_export_batch){
				list.add(ex.clone());
			}
		}
		return w;
	}
}
