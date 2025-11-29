package lixco.com.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lixco.com.entity.TheoDoiSLBH_SX;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class TheoDoiSLBH_SXService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TheoDoiSLBH_SX search(Date ngay) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TheoDoiSLBH_SX> cq = cb.createQuery(TheoDoiSLBH_SX.class);
			Root<TheoDoiSLBH_SX> root_ = cq.from(TheoDoiSLBH_SX.class);
			cq.select(root_).where(cb.equal(root_.get("sLdate"), ngay));
			TypedQuery<TheoDoiSLBH_SX> query = em.createQuery(cq).setMaxResults(1);
			List<TheoDoiSLBH_SX> results = query.getResultList();
			if (results.size() != 0)
				return results.get(0);
		} catch (Exception e) {
			logger.error("InvoiceService.reportPhieuXuatKho:" + e.getMessage(), e);
		}
		return null;
	}

	public TheoDoiSLBH_SX findByIdSafe(long id) {
		TheoDoiSLBH_SX iv = em.find(TheoDoiSLBH_SX.class, id);
		return iv;
	}

	public TheoDoiSLBH_SX saveOrUpdate(TheoDoiSLBH_SX theoDoiSLBH_SX) {
		try {
			TheoDoiSLBH_SX old=search(theoDoiSLBH_SX.getSLdate());
			Date now=new Date();
			if ( old!= null) {
				old.setLast_modifed_date(now);
				old.setTheoDoiSLBH_SX_Data(theoDoiSLBH_SX.getTheoDoiSLBH_SX_Data());
				em.merge(old);
				return old;
			} else {
				theoDoiSLBH_SX.setCreated_date(now);
				em.persist(theoDoiSLBH_SX);
				return theoDoiSLBH_SX;
			}
		} catch (Exception e) {
			logger.error("KeHoachSL_SanXuatService.saveOrUpdateKeHoachSL_SanXuat:" + e.getMessage(), e);
		}
		return null;
	}
}
