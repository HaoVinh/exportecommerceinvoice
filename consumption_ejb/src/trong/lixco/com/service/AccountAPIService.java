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

import trong.lixco.com.entity.AccountAPI;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AccountAPIService {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	public boolean findUSerPass(String user, String pass) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<AccountAPI> cq = cb.createQuery(AccountAPI.class);
			Root<AccountAPI> root = cq.from(AccountAPI.class);
			cq.select(root).where(cb.equal(root.get("username"), user), cb.equal(root.get("password"), pass));
			TypedQuery<AccountAPI> query = em.createQuery(cq);
			List<AccountAPI> acs = query.getResultList();
			if (acs.size()!=0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public AccountAPI findById(long id) {
		try {
			return em.find(AccountAPI.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	public void updateToken(long id, String token,long timetoken) {
		try {
			Query query = em.createQuery("update AccountAPI set token=:token, timetoken=:timetoken where id=:id");
			query.setParameter("token", token);
			query.setParameter("timetoken", timetoken);
			query.setParameter("id", id);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
