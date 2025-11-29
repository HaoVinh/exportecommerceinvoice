package lixco.com.reqInfo;

import lixco.com.entity.IEInvoiceDetail;

public class WrapIEInvoiceDetailReqInfo implements Cloneable{
	private IEInvoiceDetail ie_invoice_detail=null;
	private String member_name;
	
	public WrapIEInvoiceDetailReqInfo() {
	}
	
	
	public WrapIEInvoiceDetailReqInfo(IEInvoiceDetail ie_invoice_detail, String member_name) {
		this.ie_invoice_detail = ie_invoice_detail;
		this.member_name = member_name;
	}


	public IEInvoiceDetail getIe_invoice_detail() {
		return ie_invoice_detail;
	}


	public void setIe_invoice_detail(IEInvoiceDetail ie_invoice_detail) {
		this.ie_invoice_detail = ie_invoice_detail;
	}


	public String getMember_name() {
		return member_name;
	}


	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}


	@Override
	public WrapIEInvoiceDetailReqInfo clone() throws CloneNotSupportedException {
		return (WrapIEInvoiceDetailReqInfo) super.clone();
	}
}
