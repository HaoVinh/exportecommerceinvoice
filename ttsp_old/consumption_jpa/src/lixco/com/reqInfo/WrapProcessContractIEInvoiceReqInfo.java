package lixco.com.reqInfo;

import java.util.ArrayList;
import java.util.List;

import lixco.com.entity.IEInvoice;

public class WrapProcessContractIEInvoiceReqInfo implements Cloneable{
	private String member_name;
	private long member_id;
	private IEInvoice ie_invoice;
	private List<ProcessContractIEInvoice> list_process_contract;
	
	public WrapProcessContractIEInvoiceReqInfo() {
	}
	
	public WrapProcessContractIEInvoiceReqInfo(String member_name, long member_id, IEInvoice ie_invoice,List<ProcessContractIEInvoice> list_process_contract) {
		this.member_name = member_name;
		this.member_id = member_id;
		this.ie_invoice = ie_invoice;
		this.list_process_contract = list_process_contract;
	}

	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public long getMember_id() {
		return member_id;
	}
	public void setMember_id(long member_id) {
		this.member_id = member_id;
	}
	public IEInvoice getIe_invoice() {
		return ie_invoice;
	}
	public void setIe_invoice(IEInvoice ie_invoice) {
		this.ie_invoice = ie_invoice;
	}
	public List<ProcessContractIEInvoice> getList_process_contract() {
		return list_process_contract;
	}
	public void setList_process_contract(List<ProcessContractIEInvoice> list_process_contract) {
		this.list_process_contract = list_process_contract;
	}
	@Override
	public WrapProcessContractIEInvoiceReqInfo clone() throws CloneNotSupportedException {
		try{
			WrapProcessContractIEInvoiceReqInfo cloned=new WrapProcessContractIEInvoiceReqInfo();
			 cloned.setIe_invoice(ie_invoice==null?null :ie_invoice.clone());
			 cloned.setMember_id(member_id);
			 cloned.setMember_name(member_name);
			 cloned.setList_process_contract(new ArrayList<ProcessContractIEInvoice>());
			 if(list_process_contract !=null && list_process_contract.size()>0){
				 for(ProcessContractIEInvoice p:list_process_contract){
					 cloned.getList_process_contract().add(p);
				 }
			 }
			 return cloned;
		}catch (Exception e) {
		}
		return null;
	}
}
