package lixco.com.interfaces;

import java.util.List;

import lixco.com.entity.HarborCategory;
import lixco.com.entity.Stocker;
import lixco.com.reqInfo.HarborCategoryReqInfo;

public interface IHarborCategoryService {
	public int selectAll(List<HarborCategory> list);
	public int insert(HarborCategoryReqInfo t);
	public int update(HarborCategoryReqInfo t);
	public int selectById(long id,HarborCategoryReqInfo t);
	public int deleteById(long id);
	public List<HarborCategory> search(String macang, String tencang);
	public int search(int harbor,List<HarborCategory> list);
	public int initCode(HarborCategory t);
	public int selectByCode(String code,HarborCategoryReqInfo t);
	public int complete(String text,List<HarborCategory> list);
}
