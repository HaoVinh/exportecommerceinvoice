package lixco.com.interfaces;

import java.util.Date;
import java.util.List;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.PromotionalPricing;
import lixco.com.reqInfo.PromotionalPricingReqInfo;
import lixco.com.reqfox.WrapDataPromotionProgram;

public interface IPromotionalPricingService {
	public PromotionalPricing search(Date invoiceDate, long productid);
	public int search(String json,List<PromotionalPricing> list);
	public int insert(PromotionalPricingReqInfo t);
	public int update(PromotionalPricingReqInfo t);
	public int selectById(long id,PromotionalPricingReqInfo t);
	public int deleteById(long id);
	public int checkExsits(PromotionalPricing p);
	public int deleteAll();
	public int findSettingPromotionalPricing(String json,PromotionalPricingReqInfo t);

	
}
