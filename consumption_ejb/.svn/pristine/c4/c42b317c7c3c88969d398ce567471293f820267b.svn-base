package lixco.com.service;

import java.util.ArrayList;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import lixco.com.entity.Product;
import lixco.com.entity.XacNhanKiemTraDetail;
import lixco.com.entityapi.XacNhanKiemTraDetailDTO;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class XacNhanKiemTraDetailService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int insert(XacNhanKiemTraDetail p) {
		int res = -1;
		try {
			if (p != null) {
				em.persist(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int update(XacNhanKiemTraDetail p) {
		int res = -1;
		try {
			if (p != null) {
				p = em.merge(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public XacNhanKiemTraDetail findById(long id) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<XacNhanKiemTraDetail> cq = cb.createQuery(XacNhanKiemTraDetail.class);
			Root<XacNhanKiemTraDetail> root_ = cq.from(XacNhanKiemTraDetail.class);
			root_.fetch("product", JoinType.INNER);
			root_.fetch("xacNhanKiemTra", JoinType.INNER);
			cq.select(root_).where(cb.equal(root_.get("id"), id));
			TypedQuery<XacNhanKiemTraDetail> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
		}
		return null;
	}

	public int deleteById(long id) {
		int res = -1;
		try {
			Query query = em.createQuery("delete from XacNhanKiemTraDetail where id=:id");
			query.setParameter("id", id);
			res = query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<XacNhanKiemTraDetail> findByXacNhanKiemTra(long importId) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<XacNhanKiemTraDetail> cq = cb.createQuery(XacNhanKiemTraDetail.class);
			Root<XacNhanKiemTraDetail> root_ = cq.from(XacNhanKiemTraDetail.class);
			root_.fetch("product", JoinType.INNER);
			root_.fetch("xacNhanKiemTra", JoinType.INNER);
			cq.select(root_).where(cb.equal(root_.get("xacNhanKiemTra").get("id"), importId));
			TypedQuery<XacNhanKiemTraDetail> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<XacNhanKiemTraDetail>();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<XacNhanKiemTraDetailDTO> findByXacNhanKiemTraDTO(long importId) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<XacNhanKiemTraDetailDTO> cq = cb.createQuery(XacNhanKiemTraDetailDTO.class);
			Root<XacNhanKiemTraDetail> root_ = cq.from(XacNhanKiemTraDetail.class);
			Join<XacNhanKiemTraDetail, Product> product_ = root_.join("product", JoinType.LEFT);
			cq.select(cb.construct(XacNhanKiemTraDetailDTO.class, root_.get("id"), product_.get("product_code"),
					product_.get("product_name"), product_.get("unit"), product_.get("packing_unit"),
					product_.get("specification"), root_.get("quantityCa1"), root_.get("quantityCa2"),
					root_.get("quantityCa3"), root_.get("quantityTTSP"), root_.get("chenhlech"),
					root_.get("trangthaicl"), root_.get("dataPallet"), root_.get("note")))
					.where(cb.equal(root_.get("xacNhanKiemTra").get("id"), importId));
			TypedQuery<XacNhanKiemTraDetailDTO> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<XacNhanKiemTraDetailDTO>();
	}

	public List<XacNhanKiemTraDetailDTO> findChiTietKhongDatByPhieuId(Long phieuId) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<XacNhanKiemTraDetailDTO> cq = cb.createQuery(XacNhanKiemTraDetailDTO.class);
			Root<XacNhanKiemTraDetail> root = cq.from(XacNhanKiemTraDetail.class);
			Join<XacNhanKiemTraDetail, Product> product = root.join("product", JoinType.LEFT);

			cq.select(cb.construct(XacNhanKiemTraDetailDTO.class, root.get("id"), product.get("product_code"),
					product.get("product_name"), product.get("unit"), product.get("packing_unit"),
					product.get("specification"), root.get("quantityCa1"), root.get("quantityCa2"),
					root.get("quantityCa3"), root.get("quantityTTSP"), root.get("chenhlech"), root.get("trangthaicl"),
					root.get("dataPallet"), root.get("note")));

			cq.where(cb.and(cb.equal(root.get("xacNhanKiemTra").get("id"), phieuId),
					cb.equal(root.get("trangthaicl"), 3)));

			cq.orderBy(cb.asc(root.get("id")));

			return em.createQuery(cq).getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Lỗi findChiTietKhongDatByPhieuId: " + phieuId, e);
		}
		return new ArrayList<>();
	}
}
