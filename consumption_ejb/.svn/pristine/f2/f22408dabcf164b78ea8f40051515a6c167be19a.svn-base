package lixco.com.einvoice_service;

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

import lixco.com.entity.LixPathParam;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConfigSystemService {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;
	
	public LixPathParam findById(long id){
		try{
			return em.find(LixPathParam.class, id);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public LixPathParam findByKey(String key) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<LixPathParam> cq = cb.createQuery(LixPathParam.class);
			Root<LixPathParam> root = cq.from(LixPathParam.class);
			cq.select(root).where(cb.equal(root.get("lixkey"), key));
			TypedQuery<LixPathParam> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
		}
		return null;
	}
	
}
