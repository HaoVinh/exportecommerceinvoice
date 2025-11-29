package lixco.com.interfaces;

import java.util.List;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.VoucherPayment;
import lixco.com.reqInfo.VoucherPaymentReqInfo;

public interface IVoucherPaymentService {
	public int search(String json,List<VoucherPayment> list);
	public int getVoucherPaymentBy(String json,List<VoucherPayment> list);
	public int insert(VoucherPaymentReqInfo t);
	public int update(VoucherPaymentReqInfo t);
	public int deleteById(long id);
	public int selectById(long id,VoucherPaymentReqInfo t);
	public int selectByCode(String code, VoucherPaymentReqInfo reqInfo);
}
