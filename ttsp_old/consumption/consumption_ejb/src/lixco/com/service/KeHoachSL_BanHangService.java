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

import lixco.com.entity.KeHoachSL_BanHang;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class KeHoachSL_BanHangService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	public List<KeHoachSL_BanHang> search(int year) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<KeHoachSL_BanHang> cq = cb.createQuery(KeHoachSL_BanHang.class);
			Root<KeHoachSL_BanHang> root = cq.from(KeHoachSL_BanHang.class);
			cq.select(root).where(cb.equal(root.get("year"), year));
			TypedQuery<KeHoachSL_BanHang> query = em.createQuery(cq);
			List<KeHoachSL_BanHang> list = query.getResultList();
			return list;
		} catch (Exception e) {
			logger.error("KeHoachSL_BanHangService.search:" + e.getMessage(), e);
		}
		return new ArrayList<KeHoachSL_BanHang>();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int saveOrUpdateKeHoachSL_BanHang(List<KeHoachSL_BanHang> list) {
		int res = -1;
		try {
			for (KeHoachSL_BanHang t : list) {
				if (t.getId() != 0) {
					em.merge(t);
				} else {
					em.persist(t);
				}
			}
			em.flush();
			res = 0;
		} catch (Exception e) {
			logger.error("KeHoachSL_BanHangService.saveOrUpdateKeHoachSL_BanHang:" + e.getMessage(), e);
			ct.setRollbackOnly();
		}
		return res;
	}
}
