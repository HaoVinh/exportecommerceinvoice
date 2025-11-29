package huyen.lixco.com.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lixco.com.entity.Customer;
import lixco.com.entity.IECategories;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.InvoiceDetailTemp;
import lixco.com.entity.InvoiceTemp;
import lixco.com.entity.Product;
import lixco.com.interfaces.IInvoiceDetailService;
import lixco.com.interfaces.IVoucherPaymentService;
import lixco.com.reqInfo.InvoiceDetailReqInfo;

import org.jboss.logging.Logger;

import trong.lixco.com.service.AbstractService;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class InvoiceDetailTempService extends AbstractService<InvoiceDetailTemp> {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;
	@Override
	protected SessionContext getUt() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	@Override
	protected Class<InvoiceDetailTemp> getEntityClass() {
		return InvoiceDetailTemp.class;
	}
	

	public InvoiceDetailTemp selectById(long id) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InvoiceDetailTemp> cq = cb.createQuery(InvoiceDetailTemp.class);
			Root<InvoiceDetailTemp> root = cq.from(InvoiceDetailTemp.class);
			root.fetch("product", JoinType.INNER);
			root.fetch("invoicetemp", JoinType.INNER);
			root.fetch("invoice_detail_temp_own", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<InvoiceDetailTemp> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			logger.error("InvoiceDetailTempService.selectById:" + e.getMessage(), e);
		}
		return null;
	}
	public int selectByInvoice(long invoiceId, List<InvoiceDetailTemp> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InvoiceDetailTemp> cq = cb.createQuery(InvoiceDetailTemp.class);
			Root<InvoiceDetailTemp> root = cq.from(InvoiceDetailTemp.class);
			root.fetch("product", JoinType.INNER);
			root.fetch("invoicetemp", JoinType.INNER);
			cq.select(root).where(cb.equal(root.get("invoicetemp").get("id"), invoiceId));
			TypedQuery<InvoiceDetailTemp> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("InvoiceDetailTempService.selectByOrder:" + e.getMessage(), e);
		}
		return res;
	}
	
	
}
