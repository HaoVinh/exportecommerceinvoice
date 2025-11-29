package lixco.com.reqInfo;

import java.util.ArrayList;
import java.util.List;

import lixco.com.entity.IEInvoice;
import lixco.com.entity.IEInvoiceDetail;

public class WrapDataIEInvoiceReqInfo implements Cloneable {
	private IEInvoice ie_invoice=null;
	private List<IEInvoiceDetail> list_ie_invoice_detail;
	private long member_id;
	private String member_name;
	
	public WrapDataIEInvoiceReqInfo() {
	}
	
	public WrapDataIEInvoiceReqInfo(IEInvoice ie_invoice, long member_id, String member_name) {
		this.ie_invoice = ie_invoice;
		this.member_id = member_id;
		this.member_name = member_name;
	}

	public WrapDataIEInvoiceReqInfo(IEInvoice ie_invoice, List<IEInvoiceDetail> list_ie_invoice_detail, long member_id,
			String member_name) {
		this.ie_invoice = ie_invoice;
		this.list_ie_invoice_detail = list_ie_invoice_detail;
		this.member_id = member_id;
		this.member_name = member_name;
	}

	public List<IEInvoiceDetail> getList_ie_invoice_detail() {
		return list_ie_invoice_detail;
	}

	public void setList_ie_invoice_detail(List<IEInvoiceDetail> list_ie_invoice_detail) {
		this.list_ie_invoice_detail = list_ie_invoice_detail;
	}

	public IEInvoice getIe_invoice() {
		return ie_invoice;
	}
	public void setIe_invoice(IEInvoice ie_invoice) {
		this.ie_invoice = ie_invoice;
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
	@Override
	public WrapDataIEInvoiceReqInfo clone() throws CloneNotSupportedException {
		WrapDataIEInvoiceReqInfo cloned=new WrapDataIEInvoiceReqInfo();
		if(ie_invoice!=null){
			cloned.setIe_invoice(ie_invoice);
		}
		if(list_ie_invoice_detail!=null && list_ie_invoice_detail.size()>0){
			List<IEInvoiceDetail> listCloned=new ArrayList<IEInvoiceDetail>();
			for(IEInvoiceDetail dt:list_ie_invoice_detail){
				listCloned.add(dt.clone());
			}
			cloned.setList_ie_invoice_detail(listCloned);
		}
		cloned.setMember_id(member_id);
		cloned.setMember_name(member_name);
		return cloned;
	}
}
