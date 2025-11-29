package lixco.com.reqInfo;

import java.util.ArrayList;
import java.util.List;


import lixco.com.entity.ExportBatch;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.InvoiceDetailTemp;
import lixco.com.entity.PromotionOrderDetail;

public class WrapInvoiceDetailTempReqInfo implements Cloneable{
	private InvoiceDetailTemp invoice_detail=null;
	private List<ExportBatch> list_export_batch=null;
	private double inv_quantity;//tồn kho
	private double inv_quantity_cal;//Dự báo tồn.
	private long invoice_id;
	private long member_id;
	public long getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(long invoice_id) {
		this.invoice_id = invoice_id;
	}
	public long getMember_id() {
		return member_id;
	}
	public void setMember_id(long member_id) {
		this.member_id = member_id;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public List<PromotionOrderDetail> getList_promotion_order_detail() {
		return list_promotion_order_detail;
	}
	public void setList_promotion_order_detail(List<PromotionOrderDetail> list_promotion_order_detail) {
		this.list_promotion_order_detail = list_promotion_order_detail;
	}
	public List<WrapInvoiceDetailTempReqInfo> getList_wrap_invoice_detail() {
		return list_wrap_invoice_detail;
	}
	public void setList_wrap_invoice_detail(List<WrapInvoiceDetailTempReqInfo> list_wrap_invoice_detail) {
		this.list_wrap_invoice_detail = list_wrap_invoice_detail;
	}
	private String member_name;
	private List<PromotionOrderDetail> list_promotion_order_detail;
	private List<WrapInvoiceDetailTempReqInfo> list_wrap_invoice_detail;
	public WrapInvoiceDetailTempReqInfo() {
	}
	public WrapInvoiceDetailTempReqInfo(InvoiceDetailTemp invoice_detail, List<ExportBatch> list_export_batch) {
		this.invoice_detail = invoice_detail;
		this.list_export_batch = list_export_batch;
	}
	public WrapInvoiceDetailTempReqInfo(InvoiceDetailTemp invoice_detail) {
		this.invoice_detail = invoice_detail;
	}
	public InvoiceDetailTemp getInvoice_detail() {
		return invoice_detail;
	}
	public void setInvoice_detail(InvoiceDetailTemp invoice_detail) {
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
	public WrapInvoiceDetailTempReqInfo(long invoice_id, long member_id, String member_name,
			List<WrapInvoiceDetailTempReqInfo> list_wrap_invoice_detail,
			List<PromotionOrderDetail> list_promotion_order_detail) {
		this.invoice_id = invoice_id;
		this.member_id = member_id;
		this.member_name = member_name;
		this.list_wrap_invoice_detail = list_wrap_invoice_detail;
		this.list_promotion_order_detail = list_promotion_order_detail;
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
		WrapInvoiceDetailTempReqInfo other = (WrapInvoiceDetailTempReqInfo) obj;
		if (invoice_detail == null) {
			if (other.invoice_detail != null)
				return false;
		} else if (!invoice_detail.equals(other.invoice_detail))
			return false;
		return true;
	}
	@Override
	public WrapInvoiceDetailTempReqInfo clone() throws CloneNotSupportedException {
		WrapInvoiceDetailTempReqInfo w=new WrapInvoiceDetailTempReqInfo();
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
