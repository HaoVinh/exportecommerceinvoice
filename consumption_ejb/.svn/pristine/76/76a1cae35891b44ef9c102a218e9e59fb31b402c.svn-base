package lixco.com.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

import lixco.com.entity.EcomOrder;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EcomOrderService extends AbstractService<EcomOrder> {
	protected final Logger logger = Logger.getLogger(getClass());
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<EcomOrder> getEntityClass() {
		// TODO Auto-generated method stub
		return EcomOrder.class;
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

	public List<EcomOrder> findByOrderSn(String orderNumber) {
		if (orderNumber == null || orderNumber.trim().isEmpty()) {
			logger.warn("Invalid orderNumber: null or empty");
			return new ArrayList<>();
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EcomOrder> cq = cb.createQuery(EcomOrder.class);
		Root<EcomOrder> root = cq.from(EcomOrder.class);
		root.fetch("customer", JoinType.LEFT);
		root.fetch("payment_method", JoinType.LEFT);
//		root.fetch("ie_categories", JoinType.LEFT);

		Predicate orderNumberPredicate = cb.equal(root.get("orderNumber"), orderNumber);

		cq.select(root).where(orderNumberPredicate).orderBy(cb.desc(root.get("id")));

		return em.createQuery(cq).getResultList();
	}

	public EcomOrder findByCode(String code) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EcomOrder> cq = cb.createQuery(EcomOrder.class);
		Root<EcomOrder> root = cq.from(EcomOrder.class);
		root.fetch("customer", JoinType.LEFT);
		root.fetch("payment_method", JoinType.LEFT);
//		root.fetch("ie_categories", JoinType.LEFT);

		cq.select(root).where(cb.equal(root.get("orderNumber"), code));
		TypedQuery<EcomOrder> query = em.createQuery(cq);
		List<EcomOrder> results = query.getResultList();
		if (results.size() != 0) {
			return results.get(0);
		} else {
			return null;
		}
	}

	public int updateStatusAndMyStatus(String orderNumber, String status, String myStatus) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<EcomOrder> update = cb.createCriteriaUpdate(EcomOrder.class);
		Root<EcomOrder> root = update.from(EcomOrder.class);
		root.fetch("customer", JoinType.LEFT);
		root.fetch("payment_method", JoinType.LEFT);
//		root.fetch("ie_categories", JoinType.LEFT);

		update.set(root.get("status"), status);
		update.set(root.get("myStatus"), myStatus);
		update.where(cb.equal(root.get("orderNumber"), orderNumber));
		Query query = em.createQuery(update);
		return query.executeUpdate(); // trả về số row update thành công
	}

	public EcomOrder findByCodeAndPlatform(String code, String platform) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EcomOrder> cq = cb.createQuery(EcomOrder.class);
		Root<EcomOrder> root = cq.from(EcomOrder.class);
		root.fetch("customer", JoinType.LEFT);
		root.fetch("payment_method", JoinType.LEFT);
//		root.fetch("ie_categories", JoinType.LEFT);

		Predicate orderNumberPredicate = cb.equal(cb.trim(cb.lower(root.get("orderNumber"))),
				code.trim().toLowerCase());
		Predicate platformPredicate = cb.equal(cb.trim(cb.lower(root.get("loaitmdt"))), platform.trim().toLowerCase());

		cq.select(root).where(cb.and(orderNumberPredicate, platformPredicate));

		List<EcomOrder> results = em.createQuery(cq).getResultList();

		if (results.isEmpty()) {
			return null;
		} else if (results.size() > 1) {
			System.out.println("Có bản ghi trùng orderNumber=" + code + ", platform=" + platform);
		}

		return results.get(0);
	}

	public EcomOrder findByCodeAndOrderType(String code, String orderType) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EcomOrder> cq = cb.createQuery(EcomOrder.class);
		Root<EcomOrder> root = cq.from(EcomOrder.class);
		root.fetch("customer", JoinType.LEFT);
		root.fetch("payment_method", JoinType.LEFT);
//		root.fetch("ie_categories", JoinType.LEFT);

		Predicate orderNumberPredicate = cb.equal(cb.trim(cb.lower(root.get("orderNumber"))),
				code.trim().toLowerCase());
		Predicate platformPredicate = cb.equal(cb.trim(cb.lower(root.get("orderType"))),
				orderType.trim().toLowerCase());

		cq.select(root).where(cb.and(orderNumberPredicate, platformPredicate));

		List<EcomOrder> results = em.createQuery(cq).getResultList();

		if (results.isEmpty()) {
			return null;
		} else if (results.size() > 1) {
			System.out.println("Có bản ghi trùng orderNumber=" + code + ", platform=" + orderType);
		}

		return results.get(0);
	}

	public List<EcomOrder> findByPlatformDatetoDate(String platform, Date fromDate, Date toDate, String orderType) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EcomOrder> cq = cb.createQuery(EcomOrder.class);
		Root<EcomOrder> root = cq.from(EcomOrder.class);

		// Fetch luôn customer để tránh lazy exception
		root.fetch("customer", JoinType.LEFT);
		root.fetch("payment_method", JoinType.LEFT);

		// Xử lý điều kiện ngày
		Predicate datePredicate = cb.conjunction();
		if (fromDate != null && toDate != null) {
			datePredicate = cb.between(root.get("createdAt"), fromDate, toDate);
		} else if (fromDate != null) {
			datePredicate = cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
		} else if (toDate != null) {
			datePredicate = cb.lessThanOrEqualTo(root.get("createdAt"), toDate);
		}

		// Xử lý điều kiện platform
		Predicate finalPredicate = datePredicate;
		if (!"Tất cả".equals(platform)) {
			Predicate platformPredicate = cb.equal(root.get("loaitmdt"), platform);
			finalPredicate = cb.and(platformPredicate, datePredicate);
		}

		// Xử lý điều kiện orderType
		if (!"Tất cả".equals(orderType)) {
			Predicate orderTypePredicate = cb.equal(root.get("orderType"), orderType);
			finalPredicate = cb.and(finalPredicate, orderTypePredicate);
		}

		cq.select(root).where(finalPredicate).orderBy(cb.desc(root.get("id")));

		TypedQuery<EcomOrder> query = em.createQuery(cq);
		return query.getResultList();
	}

	public long countShopeeOrders() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<EcomOrder> root = cq.from(EcomOrder.class);
//		root.fetch("customer", JoinType.LEFT);
//		root.fetch("payment_method", JoinType.LEFT);
//		root.fetch("ie_categories", JoinType.LEFT);

		cq.select(cb.count(root));
		cq.where(cb.equal(root.get("loaitmdt"), "Shopee"));

		return em.createQuery(cq).getSingleResult();
	}

	public long countLazadaOrders() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<EcomOrder> root = cq.from(EcomOrder.class);
//		root.fetch("customer", JoinType.LEFT);
//		root.fetch("payment_method", JoinType.LEFT);
//		root.fetch("ie_categories", JoinType.LEFT);

		cq.select(cb.count(root));
		cq.where(cb.equal(root.get("loaitmdt"), "Lazada"));

		return em.createQuery(cq).getSingleResult();
	}

	public long countTikTokOrders() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<EcomOrder> root = cq.from(EcomOrder.class);
//		root.fetch("customer", JoinType.LEFT);
//		root.fetch("payment_method", JoinType.LEFT);
//		root.fetch("ie_categories", JoinType.LEFT);

		cq.select(cb.count(root));
		cq.where(cb.equal(root.get("loaitmdt"), "TikTok"));

		return em.createQuery(cq).getSingleResult();
	}

	public EcomOrder findByCodeAndPlatformAndOrderType(String code, String platform, String orderType) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<EcomOrder> cq = cb.createQuery(EcomOrder.class);
		Root<EcomOrder> root = cq.from(EcomOrder.class);
		root.fetch("customer", JoinType.LEFT);
		root.fetch("payment_method", JoinType.LEFT);
//		root.fetch("ie_categories", JoinType.LEFT);

		Predicate orderNumberPredicate = cb.equal(cb.trim(cb.lower(root.get("orderNumber"))),
				code.trim().toLowerCase());
		Predicate platformPredicate = cb.equal(cb.trim(cb.lower(root.get("loaitmdt"))), platform.trim().toLowerCase());
		Predicate orderTypePredicate = cb.equal(cb.trim(cb.lower(root.get("orderType"))),
				orderType.trim().toLowerCase());

		cq.select(root).where(cb.and(orderNumberPredicate, platformPredicate, orderTypePredicate));

		List<EcomOrder> results = em.createQuery(cq).getResultList();

		if (results.isEmpty()) {
			return null;
		} else if (results.size() > 1) {
			System.out.println(
					"Có bản ghi trùng orderNumber=" + code + ", platform=" + platform + ", orderType=" + orderType);
		}

		return results.get(0);
	}

	public void removeDuplicateOrders() {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		// B1: Lấy ra các orderNumber + loaitmdt bị trùng
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<EcomOrder> root = cq.from(EcomOrder.class);
		root.fetch("customer", JoinType.LEFT);
		root.fetch("payment_method", JoinType.LEFT);
//		root.fetch("ie_categories", JoinType.LEFT);

		Predicate platformPredicate = root.get("loaitmdt").in("Lazada", "TikTok", "Shopee");
		Predicate pricePredicate = cb.greaterThan(root.get("price"), 0);

		cq.multiselect(root.get("orderNumber"), root.get("loaitmdt"), cb.count(root).alias("cnt"))
				.where(cb.and(platformPredicate, pricePredicate)).groupBy(root.get("orderNumber"), root.get("loaitmdt"))
				.having(cb.gt(cb.count(root), 1L));

		List<Tuple> duplicateGroups = em.createQuery(cq).getResultList();

		// B2: Với mỗi nhóm trùng, giữ lại bản ghi updatedAt mới nhất
		for (Tuple group : duplicateGroups) {
			String orderNumber = group.get(0, String.class);
			String platform = group.get(1, String.class);

			CriteriaQuery<EcomOrder> cq2 = cb.createQuery(EcomOrder.class);
			Root<EcomOrder> root2 = cq2.from(EcomOrder.class);
			Predicate orderNumberPredicate = cb.equal(root2.get("orderNumber"), orderNumber);
			Predicate platformPredicate2 = cb.equal(root2.get("loaitmdt"), platform);
			Predicate pricePredicate2 = cb.greaterThan(root2.get("price"), 0);

			cq2.select(root2).where(cb.and(orderNumberPredicate, platformPredicate2, pricePredicate2))
					.orderBy(cb.desc(root2.get("updatedAt")));

			List<EcomOrder> orders = em.createQuery(cq2).getResultList();

			if (orders.size() > 1) {
				// Giữ lại đơn mới nhất
				for (int i = 1; i < orders.size(); i++) {
					EcomOrder oldOrder = orders.get(i);

					// Xoá chi tiết trước
					Query q = em.createQuery("DELETE FROM EcomOrderDetail d WHERE d.order = :order");
					q.setParameter("order", oldOrder).executeUpdate();

					// Xoá order
					em.remove(em.contains(oldOrder) ? oldOrder : em.merge(oldOrder));
				}
			}
		}
	}

	public List<EcomOrder> findAndCleanOrders(String platform, Date from, Date to, String type) {
		removeDuplicateOrders();
		return findByPlatformDatetoDate(platform, from, to, type);
	}

	@Transactional
	public int autoXuatHDDT(boolean autoExported) {
	    String jpql = "UPDATE EcomOrder e " +
	                  "SET e.autoExported = :autoExported " +
	                  "WHERE e.daxuathddt = false";
	    return em.createQuery(jpql)
	             .setParameter("autoExported", autoExported)
	             .executeUpdate();
	}



}
