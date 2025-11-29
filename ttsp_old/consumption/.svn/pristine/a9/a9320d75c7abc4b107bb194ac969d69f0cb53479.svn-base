package lixco.com.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import javax.persistence.criteria.Root;

import lixco.com.entity.KeHoachSL_SanXuat;
import lixco.com.entity.KeHoachSL_SanXuat;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class KeHoachSL_SanXuatService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;


	public List<KeHoachSL_SanXuat> search(int year) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<KeHoachSL_SanXuat> cq = cb.createQuery(KeHoachSL_SanXuat.class);
			Root<KeHoachSL_SanXuat> root = cq.from(KeHoachSL_SanXuat.class);
			cq.select(root).where(cb.equal(root.get("year"), year));
			TypedQuery<KeHoachSL_SanXuat> query = em.createQuery(cq);
			List<KeHoachSL_SanXuat> list = query.getResultList();
			return list;
		} catch (Exception e) {
			logger.error("KeHoachSL_SanXuatService.search:" + e.getMessage(), e);
		}
		return new ArrayList<KeHoachSL_SanXuat>();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int saveOrUpdateKeHoachSL_SanXuat(List<KeHoachSL_SanXuat> list) {
		int res = -1;
		try {
			for (KeHoachSL_SanXuat t : list) {
				if (t.getId() != 0) {
					em.merge(t);
				} else {
					em.persist(t);
				}
			}
			em.flush();
			res = 0;
		} catch (Exception e) {
			logger.error("KeHoachSL_SanXuatService.saveOrUpdateKeHoachSL_SanXuat:" + e.getMessage(), e);
			ct.setRollbackOnly();
		}
		return res;
	}
}
