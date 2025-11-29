package lixco.com.reqInfo;

import lixco.com.entity.FreightContractDetail;

public class FreightContractDetailReqInfo {
	private FreightContractDetail freight_contract_detail=null;
	

	public FreightContractDetailReqInfo() {
	}

	
	public FreightContractDetailReqInfo(FreightContractDetail freight_contract_detail) {
		this.freight_contract_detail = freight_contract_detail;
	}


	public FreightContractDetail getFreight_contract_detail() {
		return freight_contract_detail;
	}

	public void setFreight_contract_detail(FreightContractDetail freight_contract_detail) {
		this.freight_contract_detail = freight_contract_detail;
	}
	
}
