package lixco.com.interfaces;

import java.util.List;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.PricingProgram;
import lixco.com.entityapi.PricingProgramAsyncDTO;
import lixco.com.reqInfo.PricingProgramReqInfo;
import lixco.com.reqfox.WrapDataPricingProgram;

public interface IPricingProgramService {
	public int updateCapNhat(List<Long> ids);

	public List<PricingProgram> findNotSync();

	public int selectAll(List<PricingProgram> list);

	public int search(String json, List<PricingProgram> list);

	public int insert(PricingProgramReqInfo info);

	public int update(PricingProgramReqInfo info);

	public int selectById(long id, PricingProgramReqInfo info);

	public int deleteById(long id);

	public int getDateMaxSubPricingProgram(long parent_program_id, StringBuilder list);

	public String initPricingProgramCode();

	public int findLike(String text, int size, List<PricingProgram> list);

	public int complete(String text, List<PricingProgram> list);

	public int selectByCode(String code, PricingProgramReqInfo t);

	/* foxpro */
	public int saveOrUpdate(List<WrapDataPricingProgram> data, String userName);

	public PricingProgram findByCode(String code);

	public int dongbodongia(PricingProgramAsyncDTO pricingProgramAsyncDTO, StringBuilder errors);
	public long findByCodegetId(String code);
}
