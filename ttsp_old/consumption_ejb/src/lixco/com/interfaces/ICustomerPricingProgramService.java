package lixco.com.interfaces;

import java.util.Date;
import java.util.List;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.CustomerPricingProgram;
import lixco.com.entity.PricingProgram;
import lixco.com.reqInfo.CustomerPricingProgramReqInfo;

public interface ICustomerPricingProgramService {
	public int caphnhatlaingay(List<Long> ids,Date sDate,Date eDate);
	public int search(String json,PagingInfo page,List<CustomerPricingProgram> list);
	public List<CustomerPricingProgram> findAllByPricingProgram(long idprograms);
	public int insert(CustomerPricingProgramReqInfo t);
	public int update(CustomerPricingProgramReqInfo t);
	public int selectById(long id,CustomerPricingProgramReqInfo t);
	public int deleteById(long id);
	public int selectAll(long programId,List<CustomerPricingProgram> list);
	public int selectBy(long programId,long customerId,CustomerPricingProgramReqInfo t);
	public int selectForCustomer(String json,CustomerPricingProgramReqInfo t);
	public int selectForCustomerDate(String json, List<PricingProgram> pricingPrograms);
	public long selectForCustomerSub(long id,Date date,long idproduct);
}
