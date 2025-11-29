package lixco.com.interfaces;

import java.util.List;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.Car;
import lixco.com.reqInfo.CarReqInfo;

public interface ICarService {
	public int selectAll(List<Car> list);

	public int insert(CarReqInfo t);

	public int update(CarReqInfo t);

	public int selectById(long id, CarReqInfo t);

	public int deleteById(long id);

	public int search(String json, List<Car> list);

	public int selectByLicensePlate(String licensePlate, CarReqInfo t);

	public int complete(String text, List<Car> list);

	public int complete2(String text, List<Car> list);

	/* Phần foxpro */
	public lixco.com.reqfox.Car getCarFoxPro(long carId);

	public int updateIdFox(lixco.com.reqfox.Car car);

	public Car selectByCode(String licensePlate);
	public Car selectById(long id);
}
