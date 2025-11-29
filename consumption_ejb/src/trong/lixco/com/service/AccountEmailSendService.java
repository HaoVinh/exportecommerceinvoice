package trong.lixco.com.service;

import java.util.ArrayList;
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

import trong.lixco.com.entity.AccountEmailSend;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AccountEmailSendService {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	public List<AccountEmailSend>findTypeAcc(int typeAcc) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<AccountEmailSend> cq = cb.createQuery(AccountEmailSend.class);
			Root<AccountEmailSend> root = cq.from(AccountEmailSend.class);
			cq.select(root).where(cb.equal(root.get("typeAcc"), typeAcc));
			TypedQuery<AccountEmailSend> query = em.createQuery(cq);
			List<AccountEmailSend> results = query.getResultList();
				return results;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<AccountEmailSend>();
	}
	public void create(AccountEmailSend entity){
		em.persist(entity);
	}
}
