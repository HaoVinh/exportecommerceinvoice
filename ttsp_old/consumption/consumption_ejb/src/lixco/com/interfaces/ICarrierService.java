package lixco.com.interfaces;

import java.util.List;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.Carrier;
import lixco.com.reqInfo.CarrierReqInfo;

public interface ICarrierService {
	public int selectAll(List<Carrier> list);
	public int insert(CarrierReqInfo t);
	public int update(CarrierReqInfo t);
	public int selectById(long id,CarrierReqInfo t);
	public Carrier findById(long id);
	public int selectByCode(String code,CarrierReqInfo t);
	public Carrier selectByCode(String code);
	public int deleteById(long id);
	public int search(String json,List<Carrier> list);
	public int complete(String text,List<Carrier> list);
}
