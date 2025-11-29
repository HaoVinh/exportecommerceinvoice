package lixco.com.interfaces;

import java.util.Date;
import java.util.List;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.Contract;
import lixco.com.entity.ContractDetail;
import lixco.com.entity.ContractReqInfo;
import lixco.com.reqInfo.ContractDetailReqInfo;

public interface IContractService {
	public int insert(ContractReqInfo contract);
	public int update(ContractReqInfo contract);
	public int selectById(long id,ContractReqInfo t);
	public int deleteById(long id);
	public int search(String json,List<Contract> list);
	public int insertDetail(ContractDetailReqInfo t);
	public int updateDetail(ContractDetailReqInfo t);
	public int selectContractDetailByContractId(long contractId,List<ContractDetail> list);
	public int selectContractDetailByContractIds(List<Long> contractId,List<ContractDetail> list);
	public int findAll(List<ContractDetail> list);
	public int selectByIdDetail(long id,ContractDetailReqInfo t);
	public int deleteDetailById(long id);
	public String initVoucherCode();
	public int complete(String text,List<Contract> list);
	public int completeDate(String text,List<Contract> list,Date date);
	public int selectContractDetail(String json,List<ContractDetail> list);
	public int processContractIEInvoice(String json,List<Object[]> list);
	public int processContractIEInvoice2(String json,List<Object[]> list);
	public int reportLiquidation(long id,List<Object[]> list);
	/*phần nạp*/
	public Contract selectByCodeOnlyId(String contractCode); 
	public int insertLoad(ContractReqInfo contract);
	public Contract selectByVoucherOnlyId(String voucherCode);
	/*phần foxpro*/
	public int saveOrUpdateFoxpro(List<lixco.com.reqfox.Contract> list,String userName);
	public Contract findById(long id);
	public List<ContractDetail> selectDetailByContProd(long contractId,String masp);
}
