package lixco.com.interfaces;

import java.util.Date;
import java.util.List;

import lixco.com.entity.Customer;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.InvoiceDetailTemp;
import lixco.com.reqInfo.InvoiceDetailReqInfo;

public interface IInvoiceDetailService {
	public int selectByIECategories(List<String > ie_categorieCodes,Date sDate,Date eDate, List<InvoiceDetail> list);
	public int selectByInvoice(long invoiceId,List<InvoiceDetail> list);
	public int selectByInvoices(List<Long> invoiceId,List<InvoiceDetail> list);
	public int selectByInvoicesv2(List<Invoice> invoices,List<InvoiceDetail> list);
	public int selectById(long id,InvoiceDetailReqInfo t);
	public int selectByInvoiceDetailMain(long idMain,List<InvoiceDetail> list);
	public int update(InvoiceDetail detail,String userAction);
	public int insert(InvoiceDetailReqInfo t);
	
	public int selectByInvoicesToExcel(List<Long> invoiceId,List<Object[]> list);
	public int capnhatdongiakm(long id, double dongia,String userAction);
	public List<Object[]> selectByInvoicesToDMS(int monthS, int yearS);
	public int insert(InvoiceDetail p);
	public InvoiceDetail selectById(long id);
	public List<Object[]> findByCustomer(Customer customer, Date sDate, Date eDate);
	public Object[] findByCustomerTTThue(Customer customer, Date sDate, Date eDate);
	public List<InvoiceDetail> selectByInvoices(List<Long> invoiceIds);
	public int selectByInvoiceTemp(long invoiceId,List<InvoiceDetailTemp> list);
}
