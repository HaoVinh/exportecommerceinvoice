package lixco.com.interfaces;

import java.sql.SQLException;
import java.util.List;

import javax.transaction.SystemException;

import lixco.com.entity.IEInvoiceDetail;
import lixco.com.reqInfo.Message;
import lixco.com.reqInfo.WrapDataIEInvoiceReqInfo;
import lixco.com.reqInfo.WrapIEInvocieReqInfo;
import lixco.com.reqInfo.WrapIEInvoiceDetailReqInfo;
import lixco.com.reqInfo.WrapProcessContractIEInvoiceReqInfo;

public interface IProcessLogicIEInvoiceService {
	public int saveListProcessContract(WrapProcessContractIEInvoiceReqInfo t,List<Long> listProductId, Message message) throws SQLException, IllegalStateException, SecurityException, SystemException;
	public int insertOrUpdateIEInvoiceDetail(WrapIEInvoiceDetailReqInfo t,Message message)throws SQLException, IllegalStateException, SecurityException, SystemException;
	public int deleteIEInvoiceDetail(long id,Message message)throws SQLException, IllegalStateException, SecurityException, SystemException;
	public int insertOrUpdateIEInvoice(WrapDataIEInvoiceReqInfo t,Message message)throws SQLException, IllegalStateException, SecurityException, SystemException;
	public long checkforward(long ieinvoice_id)throws SQLException, IllegalStateException, SecurityException, SystemException;
	public void update(IEInvoiceDetail ieInvoiceDetail)throws SQLException, IllegalStateException, SecurityException, SystemException;

}
