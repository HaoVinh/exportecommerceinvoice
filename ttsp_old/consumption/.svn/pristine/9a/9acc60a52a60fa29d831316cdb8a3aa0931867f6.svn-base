package lixco.com.service;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lixco.com.entity.KhoaDuLieu;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class KhoaDuLieuService extends AbstractService<KhoaDuLieu> {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	public KhoaDuLieu findByMonthYear(int month, int year) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<KhoaDuLieu> cq = cb.createQuery(KhoaDuLieu.class);
			Root<KhoaDuLieu> root = cq.from(KhoaDuLieu.class);
			cq.select(root).where(cb.equal(root.get("kmonth"), month), cb.equal(root.get("kyear"), year));
			return em.createQuery(cq).getSingleResult();
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	protected Class<KhoaDuLieu> getEntityClass() {
		return KhoaDuLieu.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

}
