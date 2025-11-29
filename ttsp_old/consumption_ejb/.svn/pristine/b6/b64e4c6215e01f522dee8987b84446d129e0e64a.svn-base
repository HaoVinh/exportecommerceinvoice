package lixco.com.interfaces;

import java.sql.SQLException;

import javax.transaction.SystemException;

import lixco.com.reqInfo.Message;
import lixco.com.reqInfo.WrapOrderDetailNppReqInfo;
import lixco.com.reqInfo.WrapOrderNppReqInfo;
import lixco.com.reqInfo.WrapPMOrderDetailReqInfo;

public interface IProcessLogicOrderService {
	public int saveOrUpdate(WrapOrderNppReqInfo data,StringBuilder messages) throws IllegalStateException, SystemException, SQLException;
	public int deleteNppOrder(long nppOrderId,StringBuilder messages) throws IllegalStateException, SystemException, SQLException;
	public int saveOrUpdateDetailNpp(WrapOrderDetailNppReqInfo data,StringBuilder messages) throws IllegalStateException, SystemException, SQLException;
	public int deleteNppOrderDetail(long nppOrderDetailId,StringBuilder messages)throws IllegalStateException, SystemException, SQLException;
	public int deleteOrderDetail(long id,Message message) throws IllegalStateException, SystemException, SQLException;
	public int deleteOrderLix(long id,Message message) throws IllegalStateException, SystemException, SQLException;
	public int insertOrUpdateOrderDetail(WrapPMOrderDetailReqInfo t,Message message) throws IllegalStateException, SystemException, SQLException;
	public int updatePrice(WrapPMOrderDetailReqInfo t,Message message) throws IllegalStateException, SystemException, SQLException;
	public int updatePromo(WrapPMOrderDetailReqInfo t, Message message) throws IllegalStateException, SystemException,
	SQLException;
}
