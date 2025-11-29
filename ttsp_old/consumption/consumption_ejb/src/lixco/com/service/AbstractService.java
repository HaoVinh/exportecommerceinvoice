package lixco.com.service;

import java.util.List;

import javax.ejb.SessionContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class AbstractService<T> {

	protected abstract Class<T> getEntityClass();

	protected abstract EntityManager getEntityManager();

	protected abstract SessionContext getUt();

	public T create(T entity) {
		getEntityManager().persist(entity);
		return getEntityManager().merge(entity);
	}

	public T update(T account) {
		return getEntityManager().merge(account);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean delete(T account) {
		boolean result = true;
		try {
			getEntityManager().remove(getEntityManager().merge(account));
			getEntityManager().flush();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			getUt().setRollbackOnly();
		}
		return result;
	}

	public T findById(long id) {
		return getEntityManager().find(getEntityClass(), id);
	}

	public T findByIdLockWrite(long id) {
		return getEntityManager().find(getEntityClass(), id, LockModeType.PESSIMISTIC_WRITE);
	}

	public List<T> findAll() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
		Root<T> root = cq.from(getEntityClass());
		cq.select(root);
		TypedQuery<T> query = getEntityManager().createQuery(cq);

		return query.getResultList();
	}

}
