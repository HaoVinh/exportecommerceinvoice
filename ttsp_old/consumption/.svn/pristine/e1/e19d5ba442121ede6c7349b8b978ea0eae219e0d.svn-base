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

import trong.lixco.com.entity.ServerMailLix;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ServerMailLixService extends AbstractService<ServerMailLix> {
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
	protected Class<ServerMailLix> getEntityClass() {
		return ServerMailLix.class;
	}
	public List<ServerMailLix> configServerMail() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ServerMailLix> cq = cb.createQuery(ServerMailLix.class);
		Root<ServerMailLix> root = cq.from(ServerMailLix.class);
		cq.select(root);
		TypedQuery<ServerMailLix> query = em.createQuery(cq);
		return query.getResultList();
	}
	
	
}
