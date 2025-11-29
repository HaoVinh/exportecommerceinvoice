package lixco.com.service;

import java.util.ArrayList;
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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import lixco.com.entity.KeHoachGiaoHang;

import org.hibernate.Hibernate;
import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class KeHoachGiaoHangService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean saveOrUpdate(List<KeHoachGiaoHang> keHoachGiaoHangs, String byAccount) {
		Date now = new Date();
		try {
			Query query = em
					.createQuery("DELETE FROM KeHoachGiaoHang WHERE ngay = :ngay");
			query.setParameter("ngay", keHoachGiaoHangs.get(0).getNgay());
			query.executeUpdate();
			for (KeHoachGiaoHang keHoachGiaoHang : keHoachGiaoHangs) {
				if (keHoachGiaoHang.getId() != null) {
					keHoachGiaoHang.setLast_modifed_date(now);
					keHoachGiaoHang.setLast_modifed_by(byAccount);
					em.merge(keHoachGiaoHang);
				} else {
					keHoachGiaoHang.setCreated_date(now);
					keHoachGiaoHang.setCreated_by(byAccount);
					em.persist(keHoachGiaoHang);
				}
			}
		} catch (Exception e) {
			ct.setRollbackOnly();
			e.printStackTrace();
			return false;
		}
		return true;
	}
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<KeHoachGiaoHang> search(Date ngay) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<KeHoachGiaoHang> cq = cb.createQuery(KeHoachGiaoHang.class);
			Root<KeHoachGiaoHang> root_ = cq.from(KeHoachGiaoHang.class);
			root_.fetch("customer", JoinType.INNER);
			root_.fetch("carrier", JoinType.LEFT);
			cq.select(root_).where(cb.equal(root_.get("ngay"), ngay));
			TypedQuery<KeHoachGiaoHang> query = em.createQuery(cq);
			List<KeHoachGiaoHang> results = query.getResultList();
			return results;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<KeHoachGiaoHang>();
	}

	public KeHoachGiaoHang findByIdSafe(long id) {
		KeHoachGiaoHang iv = em.find(KeHoachGiaoHang.class, id);
		return iv;
	}
	public KeHoachGiaoHang findById(long id) {
		try {
			KeHoachGiaoHang p = em.find(KeHoachGiaoHang.class, id);
			if (p != null) {
				Hibernate.initialize(p.getCustomer());
				Hibernate.initialize(p.getCarrier());
				return p;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
