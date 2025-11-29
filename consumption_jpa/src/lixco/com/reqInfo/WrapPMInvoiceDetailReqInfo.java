package lixco.com.reqInfo;

import lixco.com.entity.InvoiceDetail;

public class WrapPMInvoiceDetailReqInfo implements Cloneable{
	private InvoiceDetail invoice_detail=null;
	private String member_name;
	
	public WrapPMInvoiceDetailReqInfo() {
	}
	
	public WrapPMInvoiceDetailReqInfo(InvoiceDetail invoice_detail, String member_name) {
		this.invoice_detail = invoice_detail;
		this.member_name = member_name;
	}

	public InvoiceDetail getInvoice_detail() {
		return invoice_detail;
	}
	public void setInvoice_detail(InvoiceDetail invoice_detail) {
		this.invoice_detail = invoice_detail;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	@Override
	protected WrapPMInvoiceDetailReqInfo clone() throws CloneNotSupportedException {
		WrapPMInvoiceDetailReqInfo cloned=new WrapPMInvoiceDetailReqInfo();
		cloned.setInvoice_detail(invoice_detail.clone());
		cloned.setMember_name(member_name);
		return cloned;
	}
}
