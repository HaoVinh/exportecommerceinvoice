package lixco.com.interfaces;

import java.util.List;

import lixco.com.entity.DeliveryPricing;
import lixco.com.entityapi.DeliveryPricingAsyncDTO;
import lixco.com.reqInfo.DeliveryPricingReqInfo;

public interface IDeliveryPricingService {
	public List<DeliveryPricing> findNotSync();
	public int updateCapNhat(List<Long> ids) ;
	public int selectAll(List<DeliveryPricing> list);

	public int insert(DeliveryPricingReqInfo t);

	public int update(DeliveryPricingReqInfo t);

	public int selectById(long id, DeliveryPricingReqInfo t);

	public int deleteById(long id);

	public int search(String json, List<DeliveryPricing> list);

	public String initPlaceCode();

	public int selectByPlaceCode(String placeCode, DeliveryPricingReqInfo t);
	public DeliveryPricing selectByPlaceCode(String placeCode);

	public int checkCode(String code, long id);

	public int seletcBy(String json, List<DeliveryPricing> list);
	public int dongbodongiavc(DeliveryPricingAsyncDTO deliveryPricingAsyncDTO, StringBuilder errors);
}
