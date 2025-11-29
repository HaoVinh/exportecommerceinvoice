package lixco.com.reqInfo;

import lixco.com.entity.ContractDetail;
import lombok.Data;
@Data
public class ProcessContract {
	private ContractDetail contract_detail=null;
	private double dathuchien;
	private double tiendathuchien;
	private double conlai;
	private double tienconlai;
	
	public ProcessContract() {
	}
	public ProcessContract(ContractDetail contract_detail, double dathuchien,double tiendathuchien) {
		this.contract_detail = contract_detail;
		this.dathuchien = dathuchien;
		this.tiendathuchien = tiendathuchien;
		
		this.conlai=(contract_detail.getQuantity()*contract_detail.getProduct().getFactor())-this.dathuchien;
		this.tienconlai=this.conlai*contract_detail.getUnit_price();
		
	}
	public ProcessContract(ContractDetail contract_detail) {
		this.contract_detail = contract_detail;
		this.conlai=(contract_detail.getQuantity()*contract_detail.getProduct().getFactor())-this.dathuchien;
		this.tienconlai=this.conlai*contract_detail.getUnit_price();
	}
	
	
	
	
}
