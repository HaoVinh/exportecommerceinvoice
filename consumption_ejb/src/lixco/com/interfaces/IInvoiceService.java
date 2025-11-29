package lixco.com.interfaces;

import java.util.Date;
import java.util.List;

import lixco.com.entity.Customer;
import lixco.com.entity.CustomerChannel;
import lixco.com.entity.CustomerTypes;
import lixco.com.entity.ExportBatch;
import lixco.com.entity.IECategories;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceTemp;
import lixco.com.entityapi.InvoiceDTO3;
import lixco.com.reportInfo.LenhDieuDong;
import lixco.com.reportInfo.PhieuNhapKho;
import lixco.com.reportInfo.PhieuXuatKho;
import lixco.com.reportInfo.PhieuXuatKhoKiemVCNB;
import lixco.com.reqInfo.InvoiceReqInfo;
import lixco.com.reqInfo.InvoiceTempReqInfo;
import lixco.com.reqInfo.PhieuXuatKhoLever;

public interface IInvoiceService {
	public List<Invoice> findAll();

	public int search(String json, List<Invoice> list);

	public int selectById(long id, InvoiceReqInfo t);

	public Invoice findById(long id);

	public Invoice findByIdSafe(long id);

	public int selectByInvoiceOwn(long idOwn, InvoiceReqInfo t);

	public int processingContract(long contractId, List<Long> listProductId, List<Object[]> list);

	public int processingContract(String json, List<Object[]> list);

	public int getListExportBatch(long invoiceDetailId, List<ExportBatch> list);

	public int updateGetInvoice(long id, String lookup_code, String voucher_code, String userAction);

	public int updateSelect(Invoice invoice, String userAction);

	public int updateAll(Invoice invoice, String userAction);

	public int reportPhieuXuatKho(long invoiceId, List<PhieuXuatKho> list);

	public int reportLenhDieuDong(long invoiceId, List<LenhDieuDong> list);

	public int reportPhieuNhapKho(long invoiceId, List<PhieuNhapKho> list);

	public int reportPhieuXuatKhoLever(long invoiceId, List<PhieuXuatKhoLever> list);

	public int reportPhieuXuatKhoKiemVCNB(String json, List<PhieuXuatKhoKiemVCNB> list);

	public int checkInvoiceCode(String invoiceCode, long invoiceId);

	public int insert(InvoiceReqInfo t);

	public int selectByInvoiceCodeOnlyId(String invoiceCode, InvoiceReqInfo t);

	public int selectByInvoiceCodeOnlyIdYear(String invoiceCode, int year, InvoiceReqInfo t);

	public int getListInventory(List<Long> listProductId, Date deliveryDate, List<Object[]> listInventory);

	/* foxpro */
	public int getListDataInvoiceFox(long orderId, List<lixco.com.reqfox.Invoice> list);

	public int getListDataInvoiceFoxByIEInvoice(long iEInvoice, List<lixco.com.reqfox.Invoice> list);

	public int getListDataInvoiceFox(List<Long> listIdInvoice, List<lixco.com.reqfox.Invoice> list);

//	public int updateInvoiceFox(long invoiceId, String idfox);
//
//	public int updateInvoiceDetailFox(long invoiceDetailId, String idfox);

	public int prepareDataDelInvoiceDetail(long invoiceDetailId, List<String> list);

	public int prepareDataDelInvoice(long invoiceId, List<String> list);

	public int updateTotalTax(Invoice invoice, String userAction);

	public int updatePayment(Long cusid, Date date, String voucher_code, Date ngaythanhtoan, String userAction);
	public int updateSendDMS(long idinvoice);

	public int updatePayment(long idinvoice, String userAction, boolean payment);

	public int updateJsonHoaDon(Invoice invoice, String userAction);

	public Invoice findBy(Long cusid, Date date, String voucher_code);

	public int updateDonHang(Invoice invoice);

	public int updateRefId(long id, String refId, String userAction);

	public int updateEditVersion(long id, int editVersion, String userAction);

	public int updateTongTienKM(long id, double total, String userAction);

	public int updateHDCopy(List<Long> ids);

	public Object[] processingContractDetail(long idContract, long idProduct, double dongiant);

	public InvoiceTemp findByIdSafe2(long id);

	public int search2(String json, List<InvoiceTemp> list);

	public InvoiceTemp findById2(long id);

	public int selectById2(long id, InvoiceTempReqInfo t);

	public List<InvoiceTemp> findAllTemp(Date startMonth, Date endMonth);

	public boolean checkIsCopy(List<Long> invoiceIds);

	public int search(String json, List<InvoiceDTO3> list, List<CustomerChannel> customerChannels,
			List<CustomerTypes> customerTypes, boolean chuachuyenmisa, int giohanHDtruyvan,
			List<IECategories> ieCategoriesSearchs, List<Customer> customerSearchs);

	public int updateTempSelect(InvoiceTemp invoice, String userAction);

	public List<Invoice> selectByOrderCode(String orderCode, long idorderlix);
	public List<Invoice> selectByOrderCode2(String orderCode);
}
