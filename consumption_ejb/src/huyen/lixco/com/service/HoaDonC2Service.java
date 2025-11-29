package huyen.lixco.com.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import lixco.com.delivery.Product;
import lixco.com.entity.HoaDonC2;
import lixco.com.entity.HoaDonChiTietC2;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import trong.lixco.com.service.AbstractService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class HoaDonC2Service extends AbstractService<HoaDonC2> {

	@Inject
	private EntityManager em;

	@Resource
	private SessionContext ct;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	@Override
	protected Class<HoaDonC2> getEntityClass() {
		return HoaDonC2.class;
	}

	// Đếm số C2 theo hóa đơn cha (để sinh STT)
	public long countByInvoiceId(Long invoiceId) {
		String jpql = "SELECT COUNT(c) FROM HoaDonC2 c WHERE c.invoiceTemp.id = :invoiceTempId";
		return em.createQuery(jpql, Long.class).setParameter("invoiceTempId", invoiceId).getSingleResult();
	}

	public List<HoaDonC2> findByInvoiceId(Long invoiceId) {
		// Lấy danh sách hóa đơn cấp 2 kèm chi tiết cấp 2
		List<HoaDonC2> hoaDonC2List = em
				.createQuery(
						"SELECT DISTINCT c FROM HoaDonC2 c " + "LEFT JOIN FETCH c.chiTietC2List ct "
								+ "WHERE c.invoice.id = :invoiceId", HoaDonC2.class)
				.setParameter("invoiceId", invoiceId).getResultList();

		// Lấy invoice và nạp list chi tiết gốc
		Invoice invoice = em
				.createQuery(
						"SELECT i FROM Invoice i " + "LEFT JOIN FETCH i.list_invoice_detail lid "
								+ "WHERE i.id = :invoiceId", Invoice.class).setParameter("invoiceId", invoiceId)
				.getSingleResult();

		// Gán lại invoice đầy đủ vào các HoaDonC2
		for (HoaDonC2 c2 : hoaDonC2List) {
			c2.setInvoice(invoice);
		}

		return hoaDonC2List;
	}

	public Invoice findByIdWithDetails(Long id) {
		String jpql = "SELECT i FROM Invoice i " + "LEFT JOIN FETCH i.list_invoice_detail d " + "WHERE i.id = :id";
		return em.createQuery(jpql, Invoice.class).setParameter("id", id).getSingleResult();
	}

	public List<InvoiceDetail> findByInvoiceIdWithProduct(Long invoiceId) {
		String jpql = "SELECT d FROM InvoiceDetail d " + "LEFT JOIN FETCH d.product p "
				+ "WHERE d.invoice.id = :invoiceId";
		return em.createQuery(jpql, InvoiceDetail.class).setParameter("invoiceId", invoiceId).getResultList();
	}

	public List<HoaDonChiTietC2> findChiTietByInvoiceId(Long invoiceId) {
	    CriteriaBuilder cb = em.getCriteriaBuilder();
	    CriteriaQuery<HoaDonChiTietC2> cq = cb.createQuery(HoaDonChiTietC2.class);
	    Root<HoaDonChiTietC2> root = cq.from(HoaDonChiTietC2.class);

	    // join fetch để lấy luôn HoaDonC2 và InvoiceDetail + Product
	    root.fetch("hoaDonC2", JoinType.LEFT);
	    Fetch<HoaDonChiTietC2, InvoiceDetail> invoiceDetailFetch = root.fetch("invoiceDetail", JoinType.LEFT);
	    invoiceDetailFetch.fetch("product", JoinType.LEFT);

	    cq.where(cb.equal(root.get("hoaDonC2").get("invoice").get("id"), invoiceId));

	    return em.createQuery(cq).getResultList();
	}


}
