package huyen.lixco.com.service;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import lixco.com.entity.HoaDonC2;
import lixco.com.entity.HoaDonChiTietC2;
import lixco.com.entity.IEInvoice;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.InvoiceDetailTemp;
import lixco.com.entity.InvoiceTemp;
import lixco.com.entity.Product;
import trong.lixco.com.service.AbstractService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class InvoiceTempService extends AbstractService<InvoiceTemp> {

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
	protected Class<InvoiceTemp> getEntityClass() {
		return InvoiceTemp.class;
	}

	// Đếm số C2 theo hóa đơn cha (để sinh STT)
	public long countByInvoiceId(Long invoiceId) {
		String jpql = "SELECT COUNT(c) FROM HoaDonC2 c WHERE c.invoice.id = :invoiceId";
		return em.createQuery(jpql, Long.class).setParameter("invoiceId", invoiceId).getSingleResult();
	}

	public InvoiceTemp findByIdWithDetails(Long id) {
		String jpql = "SELECT t FROM InvoiceTemp t " + "LEFT JOIN FETCH t.list_invoice_detail_temp d "
				+ "WHERE t.id = :id";
		List<InvoiceTemp> result = em.createQuery(jpql, InvoiceTemp.class).setParameter("id", id).getResultList();
		return result.isEmpty() ? null : result.get(0);
	}
	public List<HoaDonC2> findByInvoiceId(Long invoiceTempId) {
		// Lấy danh sách hóa đơn cấp 2 kèm chi tiết cấp 2
		List<HoaDonC2> hoaDonC2List = em
				.createQuery(
						"SELECT DISTINCT c FROM HoaDonC2 c " + "LEFT JOIN FETCH c.chiTietC2List ct "
								+ "WHERE c.invoiceTemp.id = :invoiceTempId", HoaDonC2.class)
				.setParameter("invoiceTempId", invoiceTempId).getResultList();

		// Lấy invoice và nạp list chi tiết gốc
		InvoiceTemp invoiceTemp = em
				.createQuery(
						"SELECT i FROM InvoiceTemp i " + "LEFT JOIN FETCH i.list_invoice_detail_temp lid "
								+ "WHERE i.id = :invoiceTempId", InvoiceTemp.class).setParameter("invoiceTempId", invoiceTempId)
				.getSingleResult();

		// Gán lại invoice đầy đủ vào các HoaDonC2
		for (HoaDonC2 c2 : hoaDonC2List) {
			c2.setInvoiceTemp(invoiceTemp);
		}

		return hoaDonC2List;
	}
	public List<InvoiceDetailTemp> findDetailsWithProduct(Long invoiceTempId) {
		String jpql = "SELECT d FROM InvoiceDetailTemp d " + "LEFT JOIN FETCH d.product p "
				+ "WHERE d.invoicetemp.id = :invoiceTempId";
		return em.createQuery(jpql, InvoiceDetailTemp.class).setParameter("invoiceTempId", invoiceTempId)
				.getResultList();
	}

	public List<HoaDonChiTietC2> findChiTietByInvoiceId(Long invoiceId) {
	    CriteriaBuilder cb = em.getCriteriaBuilder();
	    CriteriaQuery<HoaDonChiTietC2> cq = cb.createQuery(HoaDonChiTietC2.class);
	    Root<HoaDonChiTietC2> root = cq.from(HoaDonChiTietC2.class);

	    // join fetch để lấy luôn HoaDonC2 và InvoiceDetail + Product
	    root.fetch("hoaDonC2", JoinType.LEFT);
	    Fetch<HoaDonChiTietC2, InvoiceDetailTemp> invoiceDetailFetch = root.fetch("invoiceDetailTemp", JoinType.LEFT);
	    invoiceDetailFetch.fetch("product", JoinType.LEFT);

	    cq.where(cb.equal(root.get("hoaDonC2").get("invoiceTemp").get("id"), invoiceId));

	    return em.createQuery(cq).getResultList();
	}
	public InvoiceTemp createInvoiceTemp(InvoiceTemp invoiceTemp, List<InvoiceDetailTemp> details) {
        // persist phiếu xuất tạm
        em.persist(invoiceTemp);

        // persist danh sách chi tiết
        for (InvoiceDetailTemp d : details) {
            d.setInvoicetemp(invoiceTemp);
            em.persist(d);
        }

        return invoiceTemp;
    }
	
	public IEInvoice findByIdWithDetailsTemp(Long id) {
	    return em.createQuery(
	        "SELECT i FROM IEInvoice i " +
	        "LEFT JOIN FETCH i.customer c " +
	        "LEFT JOIN FETCH i.list_ie_invoice_detail d " +
	        "LEFT JOIN FETCH d.product p " +
	        "WHERE i.id = :id", 
	        IEInvoice.class)
	        .setParameter("id", id)
	        .getSingleResult();
	}

	public boolean existsInvoiceTempByIEInvoiceId(Long ieInvoiceId) {
	    TypedQuery<Long> query = em.createQuery(
	        "SELECT COUNT(i) FROM InvoiceTemp i WHERE i.ieInvoice.id = :ieInvoiceId", Long.class);
	    query.setParameter("ieInvoiceId", ieInvoiceId);
	    return query.getSingleResult() > 0;
	}


}
