package trong.lixco.com.service;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import trong.lixco.com.entity.AccountEmailConfirm;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AccountEmailConfirmService {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	public AccountEmailConfirm findUId(String uid) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<AccountEmailConfirm> cq = cb.createQuery(AccountEmailConfirm.class);
			Root<AccountEmailConfirm> root = cq.from(AccountEmailConfirm.class);
			cq.select(root).where(cb.equal(root.get("uid"), uid));
			TypedQuery<AccountEmailConfirm> query = em.createQuery(cq);
			List<AccountEmailConfirm> results = query.getResultList();
			if (results.size()!=0)
				return results.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void create(AccountEmailConfirm entity){
		em.persist(entity);
	}
	public void delete(long idAccountEmailConfirm){
		Query query = em
				.createQuery("DELETE FROM AccountEmailConfirm WHERE id = :idAccountEmailConfirm");
		query.setParameter("idAccountEmailConfirm", idAccountEmailConfirm);
		query.executeUpdate();
	}

}
