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
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.entity.Customer;
import lixco.com.entity.OrderDetail;
import lixco.com.entity.OrderLix;
import lixco.com.entity.Product;
import lixco.com.entity.ProductCom;
import lixco.com.entity.ProductKM;
import lixco.com.entity.PromotionOrderDetail;
import lixco.com.interfaces.IPromotionOrderDetailService;
import lixco.com.reqInfo.PromotionOrderDetailReqInfo;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class PromotionOrderDetailService implements IPromotionOrderDetailService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@Override
	public int selectByOrder(long orderId, List<PromotionOrderDetail> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PromotionOrderDetail> cq = cb.createQuery(PromotionOrderDetail.class);
			Root<PromotionOrderDetail> root = cq.from(PromotionOrderDetail.class);
			root.fetch("product", JoinType.INNER).fetch("promotion_product_group", JoinType.LEFT);
			Join<PromotionOrderDetail, OrderDetail> orderDetail_ = (Join) root.fetch("order_detail", JoinType.INNER);
			orderDetail_.fetch("product", JoinType.INNER);
			cq.select(root).where(cb.equal(orderDetail_.get("order_lix").get("id"), orderId));
			TypedQuery<PromotionOrderDetail> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("PromotionOrderDetailService.selectByOrder:" + e.getMessage(), e);
		}
		return res;
	}

//	@Override
//	@TransactionAttribute(TransactionAttributeType.REQUIRED)
//	public int selectByOrderTemp(long orderId, List<PromotionOrderDetail> list) {
//		int res = -1;
//		try {
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<PromotionOrderDetail> cq = cb.createQuery(PromotionOrderDetail.class);
//			Root<PromotionOrderDetail> root = cq.from(PromotionOrderDetail.class);
//
//			Fetch<PromotionOrderDetail, Product> productFetch = root.fetch("product", JoinType.LEFT);
//			productFetch.fetch("promotion_product_group", JoinType.LEFT);
//			productFetch.fetch("product_type", JoinType.LEFT);
//			productFetch.fetch("product_group", JoinType.LEFT);
//			Fetch<Product, ProductCom> productComFetch = productFetch.fetch("product_com", JoinType.LEFT);
//			productComFetch.fetch("product_brand", JoinType.LEFT);
//
//			Join<PromotionOrderDetail, OrderDetail> orderDetail_ = (Join) root.fetch("order_detail", JoinType.LEFT);
//			Join<OrderDetail, OrderLix> orderLix_ = (Join) orderDetail_.fetch("order_lix", JoinType.LEFT);
//			Fetch<OrderDetail, Product> dtproductFetch = orderDetail_.fetch("product", JoinType.LEFT);
//			dtproductFetch.fetch("product_type", JoinType.LEFT);
//			dtproductFetch.fetch("product_group", JoinType.LEFT);
//			dtproductFetch.fetch("product_com", JoinType.LEFT).fetch("product_brand", JoinType.LEFT);
//
//			Fetch<OrderLix, Customer> customerFetch = orderLix_.fetch("customer", JoinType.LEFT);
//			customerFetch.fetch("customer_types",JoinType.LEFT);// khách hàng
//			customerFetch.fetch("city",JoinType.LEFT);
//			orderLix_.fetch("promotion_program", JoinType.LEFT);// chương trình KM
//			orderLix_.fetch("pricing_program", JoinType.LEFT);// chương trình đơn
//																// giá
//			orderLix_.fetch("payment_method", JoinType.LEFT);// phương thức thanh
//																// toán
//			orderLix_.fetch("car", JoinType.LEFT);// xe
//			orderLix_.fetch("freight_contract", JoinType.LEFT);// hợp đồng vận
//																// chuyển.
//			orderLix_.fetch("warehouse", JoinType.LEFT);// kho
//			orderLix_.fetch("ie_categories", JoinType.LEFT);// danh mục nhập xuất
//			orderLix_.fetch("delivery_pricing", JoinType.LEFT);// đơn giá vận chuyển
//			cq.select(root);
//			cq.where(cb.and(cb.equal(orderDetail_.get("order_lix").get("id"), orderId),
//					cb.isNotNull(orderDetail_.get("order_lix"))));
//			TypedQuery<PromotionOrderDetail> query = em.createQuery(cq);
//			list.addAll(query.getResultList());
//			res = 0;
//		} catch (Exception e) {
//			logger.error("PromotionOrderDetailService.selectByOrder:" + e.getMessage(), e);
//		}
//		return res;
//	}

	@Override
	public List<Object[]> selectByOrder(OrderLix orderLix) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
			Root<PromotionOrderDetail> root = cq.from(PromotionOrderDetail.class);
			cq.multiselect(root.get("product").get("product_name"), cb.sum(root.get("quantity")),
					root.get("product").get("factor"), root.get("product").get("specification"))
					.where(cb.equal(root.get("order_detail").get("order_lix"), orderLix)).groupBy(root.get("product"));
			TypedQuery<Object[]> query = em.createQuery(cq);
			return query.getResultList();

		} catch (Exception e) {
			logger.error("OrderDetailService.selectByOrder:" + e.getMessage(), e);
		}
		return new ArrayList<Object[]>();
	}

	@Override
	public List<Object[]> selectByOrders(List<OrderLix> orderLixs) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
			Root<PromotionOrderDetail> root = cq.from(PromotionOrderDetail.class);
			cq.multiselect(root.get("product").get("product_name"), cb.sum(root.get("quantity")),
					root.get("product").get("factor"), root.get("product").get("specification"))
					.where(cb.in(root.get("order_detail").get("order_lix")).value(orderLixs))
					.groupBy(root.get("product"));
			TypedQuery<Object[]> query = em.createQuery(cq);
			return query.getResultList();

		} catch (Exception e) {
			logger.error("OrderDetailService.selectByOrder:" + e.getMessage(), e);
		}
		return new ArrayList<Object[]>();
	}

	@Override
	public int insert(PromotionOrderDetailReqInfo t) {
		int res = -1;
		try {
			PromotionOrderDetail p = t.getPromotion_order_detail();
			if (p != null) {
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("PromotionOrderDetailService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int update(PromotionOrderDetailReqInfo t) {
		int res = -1;
		try {
			PromotionOrderDetail p = t.getPromotion_order_detail();
			if (p != null) {
				p = em.merge(p);
				if (p != null) {
					selectById(p.getId(), t);
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("PromotionOrderDetailService.update:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectById(long id, PromotionOrderDetailReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PromotionOrderDetail> cq = cb.createQuery(PromotionOrderDetail.class);
			Root<PromotionOrderDetail> root = cq.from(PromotionOrderDetail.class);
			Join<PromotionOrderDetail, OrderDetail> orderDetail_ = (Join) root.fetch("order_detail", JoinType.INNER);
			orderDetail_.fetch("product", JoinType.INNER);
			root.fetch("product", JoinType.INNER).fetch("promotion_product_group", JoinType.LEFT);
			;
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<PromotionOrderDetail> query = em.createQuery(cq);
			t.setPromotion_order_detail(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			logger.error("PromotionOrderDetailService.selectById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int deleteById(long id) {
		int res = -1;
		try {
			// JQPL
			Query query = em.createQuery("delete from PromotionOrderDetail where id=:id ");
			query.setParameter("id", id);
			res = query.executeUpdate();
		} catch (Exception e) {
			logger.error("PromotionOrderDetailService.deleteById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int updateQuantityAc(PromotionOrderDetail pod) {
		int res = -1;
		try {
			Query query = em.createQuery("update PromotionOrderDetail set quantityAct=:qua where id =:id");
			query.setParameter("qua", pod.getQuantityAct());
			query.setParameter("id", pod.getId());
			res = query.executeUpdate();
		} catch (Exception e) {
			logger.error("PromotionOrderDetail.updateQuantityAc:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int updateHuy(PromotionOrderDetail pod) {
		int res = -1;
		try {
			Query query = em.createQuery("update PromotionOrderDetail set huyct=:huyct where id=:id");
			query.setParameter("huyct", pod.isHuyct());
			query.setParameter("id", pod.getId());
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error("InvoiceService.updateHuy:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int deleteAll(long orderId) {
		int res = -1;
		try {
			// JPQL
			Query query = em.createQuery(
					"delete from PromotionOrderDetail as p where p.order_detail.id in (select dt.id from OrderDetail as dt where dt.order_lix.id=:id) ");
			query.setParameter("id", orderId);
			res = query.executeUpdate();
		} catch (Exception e) {
			logger.error("PromotionOrderDetailService.deleteAll:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectBy(String json, List<PromotionOrderDetail> list) {
		int res = -1;
		try {
			/* {product_id:0,order_lix_id:0,order_product_id:0} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hOrderLixId = JsonParserUtil.getValueNumber(j, "order_lix_id", null);
			HolderParser hOrderProductId = JsonParserUtil.getValueNumber(j, "order_product_id", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PromotionOrderDetail> cq = cb.createQuery(PromotionOrderDetail.class);
			Root<PromotionOrderDetail> root = cq.from(PromotionOrderDetail.class);
			Join<PromotionOrderDetail, OrderDetail> orderDetail_ = (Join) root.fetch("order_detail", JoinType.INNER);
			orderDetail_.fetch("product", JoinType.INNER);
			root.fetch("product", JoinType.INNER);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			ParameterExpression<Long> pOrderLixId = cb.parameter(Long.class);
			ParameterExpression<Long> pOrderProductId = cb.parameter(Long.class);
			List<Predicate> predicates = new ArrayList<Predicate>();
			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.equal(pProductId, 0));
			dis.getExpressions().add(cb.equal(root.get("product").get("id"), pProductId));
			predicates.add(dis);
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.equal(pOrderLixId, 0));
			dis1.getExpressions().add(cb.equal(orderDetail_.get("order_lix").get("id"), pOrderLixId));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.equal(pOrderProductId, 0));
			dis2.getExpressions().add(cb.equal(orderDetail_.get("product").get("id"), pOrderProductId));
			predicates.add(dis2);
			cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
			TypedQuery<PromotionOrderDetail> query = em.createQuery(cq);
			query.setParameter(pProductId, Long.parseLong(Objects.toString(hProductId.getValue(), "0")));
			query.setParameter(pOrderLixId, Long.parseLong(Objects.toString(hOrderLixId.getValue(), "0")));
			query.setParameter(pOrderProductId, Long.parseLong(Objects.toString(hOrderProductId.getValue(), "0")));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("PromotionOrderDetailService.selectBy:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int deleteBy(long orderDetailPosId) {
		int res = -1;
		try {
			// JQPL
			Query query = em.createQuery("delete from PromotionOrderDetail as p where p.order_detail.id=:id ");
			query.setParameter("id", orderDetailPosId);
			res = query.executeUpdate();
		} catch (Exception e) {
			logger.error("PromotionOrderDetailService.deleteBy:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectByOrderDetail(long orderDetailId, List<PromotionOrderDetail> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PromotionOrderDetail> cq = cb.createQuery(PromotionOrderDetail.class);
			Root<PromotionOrderDetail> root = cq.from(PromotionOrderDetail.class);
			root.fetch("product", JoinType.INNER).fetch("promotion_product_group", JoinType.LEFT);
			Join<PromotionOrderDetail, OrderDetail> orderDetail_ = (Join) root.fetch("order_detail", JoinType.INNER);
			orderDetail_.fetch("product", JoinType.INNER);
			cq.select(root).where(cb.equal(orderDetail_.get("id"), orderDetailId));
			TypedQuery<PromotionOrderDetail> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("PromotionOrderDetailService.deleteBy:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public List<PromotionOrderDetail> selectByOrderDetails(List<OrderDetail> orderDetails) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PromotionOrderDetail> cq = cb.createQuery(PromotionOrderDetail.class);
			Root<PromotionOrderDetail> root = cq.from(PromotionOrderDetail.class);
			root.fetch("product", JoinType.INNER).fetch("promotion_product_group", JoinType.LEFT);
			Join<PromotionOrderDetail, OrderDetail> orderDetail_ = (Join) root.fetch("order_detail", JoinType.INNER);

			Join<OrderDetail, OrderLix> orderLix_ = (Join) orderDetail_.fetch("order_lix", JoinType.LEFT);
			orderLix_.fetch("promotion_program", JoinType.LEFT);
			orderLix_.fetch("pricing_program", JoinType.LEFT);
			orderLix_.fetch("car", JoinType.LEFT);
			orderLix_.fetch("customer", JoinType.LEFT);
			orderLix_.fetch("ie_categories", JoinType.LEFT);

			orderDetail_.fetch("product", JoinType.INNER);
			orderDetail_.fetch("order_lix", JoinType.LEFT);
			cq.select(root).where(cb.in(root.get("order_detail")).value(orderDetails));
			TypedQuery<PromotionOrderDetail> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<PromotionOrderDetail>();
	}
}
