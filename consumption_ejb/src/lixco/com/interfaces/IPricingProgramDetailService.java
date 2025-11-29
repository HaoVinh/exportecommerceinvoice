package lixco.com.interfaces;

import java.util.Date;
import java.util.List;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.PricingProgramDetail;
import lixco.com.reqInfo.PricingProgramDetailReqInfo;

public interface IPricingProgramDetailService {
	public int selectAllByPricingProgram(long programId,List<PricingProgramDetail> list);
	public List<PricingProgramDetail> findAllByPricingProgram(long idpricingPrograms);
	public int search(String json,PagingInfo page,List<PricingProgramDetail> list);
	public int insert(PricingProgramDetailReqInfo info);
	public int update(PricingProgramDetailReqInfo info);
	public int selectById(long id,PricingProgramDetailReqInfo info);
	public int deleteById(long id);
	public int checkExsits(long productId,long id,long programId);
	public int deleteAll(long programId);
	public int updateByPredicate(long productId, long programId,double unitPrice, double revenue,double quantity);
	public int findSettingPricing(long programId,long productId,PricingProgramDetailReqInfo t);
	public int findSettingPricingChild(long parentProgramId,long productId,PricingProgramDetailReqInfo t, Date date);
	public int findSettingPricingChild(long parentProgramId, long productId, PricingProgramDetailReqInfo t);
	public int updateLoiNhuan(long programId, long product_id,double loinhuan);
	public PricingProgramDetail findByProgramIdAndProductId(long programId, long product_id);
}
