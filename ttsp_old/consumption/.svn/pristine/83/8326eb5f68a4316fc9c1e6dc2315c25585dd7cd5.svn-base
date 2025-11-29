package lixco.com.interfaces;

import java.util.List;

import lixco.com.entity.PromotionProgram;
import lixco.com.entityapi.PromotionProgramAsyncDTO;
import lixco.com.reqInfo.PromotionProgramReqInfo;
import lixco.com.reqfox.WrapDataPromotionProgram;

public interface IPromotionProgramService {
	public int dongbokhuyenmai(PromotionProgramAsyncDTO programAsyncDTO, StringBuilder errors);
	public PromotionProgram findByCode(String programCode);
	public List<PromotionProgram> findNotSync();
	public int selectAll(List<PromotionProgram> list);
	public int search(String json,List<PromotionProgram> list);
	public int insert(PromotionProgramReqInfo t);
	public int update(PromotionProgramReqInfo t);
	public int updateCapNhat(List<Long> ids);
	public int selectById(long id,PromotionProgramReqInfo t);
	public int deleteById(long id);
	public String initPromotionProgramCode();
	public int findLike(String text,int size,List<PromotionProgram> list);
	public int complete(String text,List<PromotionProgram> list);
	public int selectByCode(String programCode,PromotionProgramReqInfo t);
	int saveOrUpdate(List<WrapDataPromotionProgram> data, String userName);
}
