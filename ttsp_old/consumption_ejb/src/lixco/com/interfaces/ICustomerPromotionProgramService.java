package lixco.com.interfaces;

import java.util.Date;
import java.util.List;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.CustomerPromotionProgram;
import lixco.com.entity.PromotionProgram;
import lixco.com.reqInfo.CustomerPromotionProgramReqInfo;

public interface ICustomerPromotionProgramService {
	public List<CustomerPromotionProgram> findAllByPromotionProgram(long idprograms);
	public int caphnhatlaingay(List<Long> ids,Date sDate,Date eDate);
	public int search(String json,List<CustomerPromotionProgram> list);
	public int insert(CustomerPromotionProgramReqInfo t);
	public int update(CustomerPromotionProgramReqInfo t);
	public int selectById(long id,CustomerPromotionProgramReqInfo t);
	public int deleteById(long id);
	public int selectAll(long programId,List<CustomerPromotionProgram> list);
	public int selectBy(long programId,long customerId,CustomerPromotionProgramReqInfo t);
	public int selectForCustomer(String json,CustomerPromotionProgramReqInfo t);
	public int selectForCustomerDate(String json, List<PromotionProgram> promotionPrograms);
}
