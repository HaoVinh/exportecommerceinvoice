package lixco.com.interfaces;

import java.util.Date;
import java.util.List;







import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.ExportBatchOD;
import lixco.com.entity.ExportBatchPOD;
import lixco.com.entity.OrderLix;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.PromotionProgram;
import lixco.com.reqInfo.OrderLixReqInfo;

public interface IOrderLixService {
	public OrderLix finByIdAll(long id);
	public int search(String json,List<OrderLix> list,List<Integer> statusSearch);
	public int insert(OrderLixReqInfo t);
	public int update(OrderLixReqInfo t);
	public int updateChangePro(OrderLix p);
	public int selectById(long id,OrderLixReqInfo t);
	
	public OrderLix findById(long id);
	public int deleteById(long id);
	public String initOrderCode();
	public String initVoucherCode(Date orderDate,long id);
	public int selectByOrderCode(String orderCode,OrderLixReqInfo t);
	public int checkByCode(String code,long id);
	public double getRealExportBox(long orderId,long productId);
	public OrderLix selectOnlyId(long nppOrderId);
	public OrderLix selectById(long id);
	public int updatePricingOrPromotionProgram(long id,String memberName,PricingProgram pricingProgram,PromotionProgram promotionProgram);
	public int getListExportBatchOD(long orderDetailId, List<ExportBatchOD> list);
	public int getListExportBatchPOD(long promotionOrderDetail,List<ExportBatchPOD> list);
	public int getListInventory(List<Long> listProductId, Date deliveryDate, List<Object[]> listInventory);
	public int getListRealExportQuantity(long orderId,List<Object[]> listRealExport);
	public int getListExportExtensionOrder(long orderId,List<Object[]> list);
	public int getListExportExtensionOrderKM(long orderId,List<Object[]> list);
	/*Phần nạp dữ liệu*/
	public int insertByLoad(OrderLixReqInfo t);
	/*phần fox*/
	int selectByOrderCodeId(String orderCode, OrderLixReqInfo t);
	public OrderLix selectByNppIdOrder(long nppOrderId);
	public int updateNoPromotionOrderDetail(long id, boolean no);
	public List<OrderLix> selectByOrderVoucher(String orderCode, long idkh);
	public void updateTinhTrangDH(Long id) throws Exception;
}
