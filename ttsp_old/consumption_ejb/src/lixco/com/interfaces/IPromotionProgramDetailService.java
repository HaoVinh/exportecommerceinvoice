package lixco.com.interfaces;

import java.util.List;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.PromotionProgramDetail;
import lixco.com.reqInfo.PromotionProgramDetailReqInfo;

public interface IPromotionProgramDetailService {
	public List<PromotionProgramDetail> findAllByPromotionProgram(long idprograms);
	public int selectAllByPromotionProgram(long program_id,List<PromotionProgramDetail> list);
	public int search(String json,PagingInfo page,List<PromotionProgramDetail> list);
	public int insert(PromotionProgramDetailReqInfo t);
	public int update(PromotionProgramDetailReqInfo t);
	public int selectById(long id,PromotionProgramDetailReqInfo t);
	public int deleteById(long id);
	public int checkExist(long productId,long promotionProductId,long id,long programId,int form);
	public int deleteAll(long programId);
	public int selectBy(String json,List<PromotionProgramDetail> list);
	public int updateByPredicate(long productId,long productProId, long promotion_form, long programId, double box_quatity, double promotion_quantity);
}
