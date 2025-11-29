package lixco.com.reqInfo;

import lixco.com.entity.InvoiceTemp;

public class InvoiceTempReqInfo {
	private InvoiceTemp invoice=null;
	
	
	
	public InvoiceTempReqInfo() {
	}

	public InvoiceTempReqInfo(InvoiceTemp invoice) {
		this.invoice = invoice;
	}

	public InvoiceTemp getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceTemp invoice) {
		this.invoice = invoice;
	}
}
