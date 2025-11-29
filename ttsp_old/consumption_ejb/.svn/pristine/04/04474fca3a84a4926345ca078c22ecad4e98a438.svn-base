package lixco.com.interfaces;

import java.util.Date;
import java.util.List;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.Car;
import lixco.com.entity.FreightContract;
import lixco.com.entity.FreightContractDetail;
import lixco.com.reqInfo.FreightContractDetailReqInfo;
import lixco.com.reqInfo.FreightContractReqInfo;

public interface IFreightContractService {
	public int search(String json,List<FreightContract> list);
	public int searchType(String json,List<FreightContract> list);
	public int insert(FreightContractReqInfo t);
	public int update(FreightContractReqInfo t);
	public int selectById(long id,FreightContractReqInfo t);
	public int deleteById(long id);
	public int deleteByFreightContractDetailId(long id);
	public int deleteDetailByFreightContract(long freightContractId);
	public String initOrderCode();
	public int selectByOrderCode(String contractCode,FreightContractReqInfo t);
	public int selectByIdDetail(long id,FreightContractDetailReqInfo t);
	public int insertDetail(FreightContractDetailReqInfo t);
	public int updateDetail(FreightContractDetailReqInfo t);
	public int selectFreightContractDetailByFreightContractId(long freightContractId,List<FreightContractDetail> list);
	public int getListInvoiceCode(long freightContractId,List<String> list);
	public int getListInvoiceCode(Date date, long idCus,List<String> list);
	public int getListDataFreightContractByInvoice(List<String> listInvoiceCode,List<FreightContractDetail> list);
	public Car getCarByInvoice(List<String> invoiceCode);
	
	public FreightContract selectByOrderCode(String contractCode);
}
