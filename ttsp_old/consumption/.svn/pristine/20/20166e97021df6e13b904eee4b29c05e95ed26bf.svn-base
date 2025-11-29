package trong.lixco.com.service;

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

import lixco.com.entity.DongBo;
import trong.lixco.com.entity.AccountAPI;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DongBoService {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	public DongBo findName(String name) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<DongBo> cq = cb.createQuery(DongBo.class);
			Root<DongBo> root = cq.from(DongBo.class);
			cq.select(root).where(cb.equal(root.get("name"), name));
			TypedQuery<DongBo> query = em.createQuery(cq);
			DongBo ac = query.getSingleResult();
			if (ac != null)
				return ac;
		} catch (Exception e) {
		}
		return null;
	}

	public void saveOrUpdate(DongBo dongBo) {
		try {
			if(dongBo.getId()==0){
				em.persist(dongBo);
			}else{
				em.merge(dongBo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
