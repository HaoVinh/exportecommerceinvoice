package lixco.com.interfaces;

import java.util.Date;
import java.util.List;


public interface IDmsService {
	public int getListPricingProgram(Date fromDate,List<Object[]> list);
	public int getListPricingProgramDetail(long id,List<Object[]> listDetail);
	public int getListPromotionProgram(Date fromDate,List<Object[]> list);
	public int getListPromotionProgramDetail(long id,List<Object[]> list);
}
