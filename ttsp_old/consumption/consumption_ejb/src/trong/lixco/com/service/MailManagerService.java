package trong.lixco.com.service;

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
import javax.persistence.criteria.Root;

import trong.lixco.com.entity.MailManager;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MailManagerService extends AbstractService<MailManager> {
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
	protected Class<MailManager> getEntityClass() {
		return MailManager.class;
	}

	public MailManager getMailinfoLix() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<MailManager> cq = cb.createQuery(MailManager.class);
		Root<MailManager> root = cq.from(MailManager.class);
		cq.select(root);
		TypedQuery<MailManager> query = em.createQuery(cq);
		List<MailManager> results = query.getResultList();
		if (results.size() != 0)
			return results.get(0);
		return null;
	}
}
