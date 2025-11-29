package lixco.com.service;

import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lixco.com.entity.EcomOrder;
import lixco.com.entity.EcomOrderDetail;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EcomOrderDetailService extends AbstractService<EcomOrderDetail> {
	protected final Logger logger = Logger.getLogger(getClass());
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<EcomOrderDetail> getEntityClass() {
		// TODO Auto-generated method stub
		return EcomOrderDetail.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected SessionContext getUt() {
		// TODO Auto-generated method stub
		return ct;
	}

	public List<EcomOrderDetail> findByCodeAndPlatform(String orderNumber, String platform) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EcomOrderDetail> cq = cb.createQuery(EcomOrderDetail.class);
		Root<EcomOrderDetail> root = cq.from(EcomOrderDetail.class);

		// WHERE order.orderNumber = :orderNumber AND order.loaitmdt = :platform
		cq.where(cb.equal(root.get("orderId"), orderNumber), cb.equal(root.get("loaitmdt"), platform));
		TypedQuery<EcomOrderDetail> query = em.createQuery(cq);
		return query.getResultList();
	}

	public List<EcomOrderDetail> findByOrderItemNumberAndOrderId(String orderItemId, String orderId, String loaitmdt) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<EcomOrderDetail> cq = cb.createQuery(EcomOrderDetail.class);
			Root<EcomOrderDetail> root = cq.from(EcomOrderDetail.class);

			cq.where(cb.equal(root.get("orderItemNumber"), orderItemId), cb.equal(root.get("orderId"), orderId),
					cb.equal(root.get("loaitmdt"), loaitmdt));

			return em.createQuery(cq).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<EcomOrderDetail> findByCodeAndPlatformAndOrderType(String orderNumber, String platform,
			String orderType) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EcomOrderDetail> cq = cb.createQuery(EcomOrderDetail.class);
		Root<EcomOrderDetail> root = cq.from(EcomOrderDetail.class);

		cq.where(cb.equal(root.get("orderId"), orderNumber), cb.equal(root.get("loaitmdt"), platform),
				cb.equal(root.get("order").get("orderType"), orderType));

		return em.createQuery(cq).getResultList();
	}
	public List<EcomOrderDetail> findByCodeAndPlatformAndOrderTypeAndOrderItemId(String orderNumber, String platform, String orderType, String orderItemId) {
	    try {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<EcomOrderDetail> cq = cb.createQuery(EcomOrderDetail.class);
	        Root<EcomOrderDetail> root = cq.from(EcomOrderDetail.class);

	        cq.where(cb.equal(root.get("orderId"), orderNumber),
	                 cb.equal(root.get("loaitmdt"), platform),
	                 cb.equal(root.get("order").get("orderType"), orderType),
	                 cb.equal(root.get("orderItemNumber"), orderItemId));

	        return em.createQuery(cq).getResultList();
	    } catch (Exception e) {
	        return Collections.emptyList();
	    }
	}
	public List<EcomOrderDetail> findByCodeAndPlatformAndOrderTypeAndOrderId(long orderId, String orderNumber, String platform, String orderType) {
	    CriteriaBuilder cb = em.getCriteriaBuilder();
	    CriteriaQuery<EcomOrderDetail> cq = cb.createQuery(EcomOrderDetail.class);
	    Root<EcomOrderDetail> root = cq.from(EcomOrderDetail.class);
	    
	    // Join với EcomOrder thông qua trường order
	    Join<EcomOrderDetail, EcomOrder> orderJoin = root.join("order");
	    
	    // Xây dựng điều kiện where
	    cq.select(root)
	      .where(
	          cb.equal(orderJoin.get("id"), orderId),
	          cb.equal(root.get("orderId"), orderNumber),
	          cb.equal(root.get("loaitmdt"), platform),
	          cb.equal(root.get("orderType"), orderType)
	      );

	    return em.createQuery(cq).getResultList();
	}

	public void removeDuplicateDetailsByOrderNumberAndPlatform(String orderNumber, String platform) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();

			// B1: Tìm id lớn nhất (EcomOrder mới nhất) theo orderNumber + loaitmdt
			CriteriaQuery<Long> cqMax = cb.createQuery(Long.class);
			Root<EcomOrder> orderRoot = cqMax.from(EcomOrder.class);
			cqMax.select(cb.greatest(orderRoot.get("id")))
					.where(cb.and(cb.equal(orderRoot.get("orderNumber"), orderNumber),
							cb.equal(orderRoot.get("loaitmdt"), platform)));
			Long newestOrderId = em.createQuery(cqMax).getSingleResult();

			if (newestOrderId == null) {
				logger.warn(
						"Không tìm thấy EcomOrder mới nhất với orderNumber=" + orderNumber + ", platform=" + platform);
				return;
			}

			// B2: Tìm tất cả detail thuộc các order cũ (id < newestOrderId)
			CriteriaQuery<EcomOrderDetail> cq = cb.createQuery(EcomOrderDetail.class);
			Root<EcomOrderDetail> detailRoot = cq.from(EcomOrderDetail.class);
			Join<EcomOrderDetail, EcomOrder> orderJoin = detailRoot.join("order");

			cq.select(detailRoot).where(cb.and(cb.equal(orderJoin.get("orderNumber"), orderNumber),
					cb.equal(orderJoin.get("loaitmdt"), platform), cb.notEqual(orderJoin.get("id"), newestOrderId) // loại
																													// order
																													// mới
																													// nhất
			));

			List<EcomOrderDetail> oldDetails = em.createQuery(cq).getResultList();

			// B3: Xóa toàn bộ detail cũ
			for (EcomOrderDetail oldDetail : oldDetails) {
				em.remove(em.contains(oldDetail) ? oldDetail : em.merge(oldDetail));
			}

			logger.info("Đã xoá " + oldDetails.size() + " chi tiết đơn hàng trùng cho orderNumber=" + orderNumber
					+ ", platform=" + platform);

		} catch (Exception e) {
			logger.error("Error removeDuplicateDetailsByOrderNumberAndPlatform", e);
		}
	}


}
