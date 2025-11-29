package lixco.com.reqfox;

import java.util.List;

public class WrapDataInvoice {
	private String id_fox_order=null;
	private String id_fox_ieinvoice=null;
	private List<Invoice> list_invoice;
	
	public WrapDataInvoice() {
	}
	
	public WrapDataInvoice(String id_fox_order, String id_fox_ieinvoice, List<Invoice> list_invoice) {
		this.id_fox_order = id_fox_order;
		this.id_fox_ieinvoice = id_fox_ieinvoice;
		this.list_invoice = list_invoice;
	}

	public String getId_fox_order() {
		return id_fox_order;
	}
	public void setId_fox_order(String id_fox_order) {
		this.id_fox_order = id_fox_order;
	}
	public String getId_fox_ieinvoice() {
		return id_fox_ieinvoice;
	}
	public void setId_fox_ieinvoice(String id_fox_ieinvoice) {
		this.id_fox_ieinvoice = id_fox_ieinvoice;
	}
	public List<Invoice> getList_invoice() {
		return list_invoice;
	}
	public void setList_invoice(List<Invoice> list_invoice) {
		this.list_invoice = list_invoice;
	}
	
}
