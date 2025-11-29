package lixco.com.interfaces;

import java.util.List;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.Customer;
import lixco.com.entity.CustomerTypes;
import lixco.com.entityapi.CustomerAsyncDTO;
import lixco.com.entityapi.KhachHangData;
import lixco.com.reqInfo.CustomerReqInfo;

public interface ICustomerService {
	public int selectAll(List<Customer> list);

	public List<Customer> findNotSync();

	public int search(String json, List<Customer> list);

	public int insert(CustomerReqInfo info);

	public int insertAvaiCode(CustomerReqInfo t);

	public int update(CustomerReqInfo info);
	public int updateCapNhat(List<Long> ids);

	public int selectById(long id, CustomerReqInfo info);

	public int deleteById(long id);

	public int checkCustomerCode(String customerCode, long customeId);

	public int autoComplete(String text, int size, List<Customer> list);

	public int selectAllByCustomerTypes(long customerTypesId, List<Customer> list);

	public int complete(String text, List<Customer> list);

	public int complete(String text, CustomerTypes customerTypes, List<Customer> list);

	public int selectByCode(String code, CustomerReqInfo t);

	public Customer selectOnlyId(String code);

	public Customer findById(long id);

	public int selectBy(String json, List<Customer> list);

	public List<Customer> selectAllByCity(long cityid, long customerTypes_id);

	/* phần foxpro */
	public lixco.com.reqfox.Customer getCustomerFoxPro(long customerId);

	int selectByCodeId(String code, CustomerReqInfo t);

	public Customer selectByCode(String code);
	public Customer selectByCodeNotDisable(String code);

	public int dongbokhachhang(CustomerAsyncDTO customerAsyncDTO, StringBuilder errors);
	public List<KhachHangData> complete2(String makh, String tenkh);
}
