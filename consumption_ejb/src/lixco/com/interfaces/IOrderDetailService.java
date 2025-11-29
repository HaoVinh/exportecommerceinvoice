package lixco.com.interfaces;

import java.util.Date;
import java.util.List;

import lixco.com.entity.KeHoachGiaoHang;
import lixco.com.entity.OrderDetail;
import lixco.com.entity.OrderLix;
import lixco.com.reqInfo.OrderDetailReqInfo;

public interface IOrderDetailService {
	public int selectByOrder(long orderId,List<OrderDetail> list);
	public List<Object[]> selectByOrders(List<OrderLix> orderLixs);
	public List<OrderDetail> selectByOrdersAll(List<OrderLix> orderLixs);
	public List<Object[]>  selectByOrder(OrderLix orderLix);
	public int insert(OrderDetailReqInfo info);
	public int update(OrderDetailReqInfo info);
	public int selectById(long id,OrderDetailReqInfo info);
	public int deleteById(long id);
	public int checkExsits(long productId,long id,long orderId);
	public int deleteAll(long orderId);
	public int selectBy(String orderCode,String productCode,List<OrderDetail> list);
	/*phần nap dữ liệu*/
	public int insertLoad(OrderDetailReqInfo info);
	public int updateBoxQuantityActual(OrderDetail orderDetail);
	public int updateMaLH(OrderDetail orderDetail);
	public int updateSort(OrderDetail orderDetail);
	public OrderDetail selectById(long id);
	public int updateHuy(OrderDetail orderDetail);
	public int updateTotal(OrderDetail orderDetail);
	public int updatePriceTotal(OrderDetail orderDetail);
	public List<OrderDetail> selectByOrdersHuy();
	public List<KeHoachGiaoHang> findBy(boolean hcm, boolean bd, Date ngaygh);
	public List<KeHoachGiaoHang> findBy2(boolean hcm, boolean bd, Date ngaygh);
	public double khoiluongtongdonhang(long idorder);
	
}
