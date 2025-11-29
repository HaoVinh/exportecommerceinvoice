package lixco.com.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import lixco.com.entity.Carrier;
import lixco.com.entity.Customer;
import lixco.com.entity.IECategories;
import lixco.com.entity.KeHoachGiaoHang;
import lixco.com.entity.OrderDetail;
import lixco.com.entity.OrderLix;
import lixco.com.entity.Product;
import lixco.com.interfaces.ICarrierService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IOrderDetailService;
import lixco.com.reqInfo.OrderDetailReqInfo;

import org.hibernate.Hibernate;
import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class OrderDetailService implements IOrderDetailService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@Override
	public int selectByOrder(long orderId, List<OrderDetail> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderDetail> cq = cb.createQuery(OrderDetail.class);
			Root<OrderDetail> root = cq.from(OrderDetail.class);
			root.fetch("product", JoinType.INNER);
			root.fetch("order_lix", JoinType.INNER);
			cq.select(root).where(cb.equal(root.get("order_lix").get("id"), orderId))
					.orderBy(cb.asc(root.get("indexsort")));
			TypedQuery<OrderDetail> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("OrderDetailService.selectByOrder:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Object[]> selectByOrder(OrderLix orderLix) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
			Root<OrderDetail> root = cq.from(OrderDetail.class);
			cq.multiselect(root.get("product").get("product_name"), cb.sum(root.get("box_quantity")),
					root.get("product").get("factor"), root.get("product").get("specification"))
					.where(cb.equal(root.get("order_lix"), orderLix)).groupBy(root.get("product"));
			TypedQuery<Object[]> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("OrderDetailService.selectByOrder:" + e.getMessage(), e);
		}
		return new ArrayList<Object[]>();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Object[]> selectByOrders(List<OrderLix> orderLixs) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
			Root<OrderDetail> root = cq.from(OrderDetail.class);
			cq.multiselect(root.get("product").get("product_name"), cb.sum(root.get("box_quantity")),
					root.get("product").get("factor"), root.get("product").get("specification"),
					root.get("product").get("product_code")).where(cb.in(root.get("order_lix")).value(orderLixs))
					.groupBy(root.get("product"));
			TypedQuery<Object[]> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("OrderDetailService.selectByOrder:" + e.getMessage(), e);
		}
		return new ArrayList<Object[]>();
	}

	@Override
	public List<OrderDetail> selectByOrdersAll(List<OrderLix> orderLixs) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderDetail> cq = cb.createQuery(OrderDetail.class);
			Root<OrderDetail> root = cq.from(OrderDetail.class);
			root.fetch("order_lix", JoinType.LEFT);

			Join<OrderDetail, OrderLix> orderLix_ = (Join) root.fetch("order_lix", JoinType.LEFT);
			orderLix_.fetch("promotion_program", JoinType.LEFT);
			orderLix_.fetch("pricing_program", JoinType.LEFT);
			orderLix_.fetch("car", JoinType.LEFT);
			orderLix_.fetch("customer", JoinType.LEFT);
			orderLix_.fetch("ie_categories", JoinType.LEFT);
			root.fetch("product", JoinType.LEFT);

			cq.select(root).where(cb.in(root.get("order_lix")).value(orderLixs));
			TypedQuery<OrderDetail> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("OrderDetailService.selectByOrder:" + e.getMessage(), e);
		}
		return new ArrayList<OrderDetail>();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<OrderDetail> selectByOrdersHuy() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderDetail> cq = cb.createQuery(OrderDetail.class);
			Root<OrderDetail> root = cq.from(OrderDetail.class);
			cq.select(root).where(cb.equal(root.get("huyct"), true));
			TypedQuery<OrderDetail> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
		}
		return new ArrayList<OrderDetail>();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int insert(OrderDetailReqInfo t) {
		int res = -1;
		try {
			OrderDetail p = t.getOrder_detail();
			if (p != null) {
				Product product = p.getProduct();
				OrderLix orderLix = p.getOrder_lix();
				int check = checkExsits(product != null ? product.getId() : 0, p.getId(),
						orderLix != null ? orderLix.getId() : 0);
				res = check;
				if (check == 0) {
					em.persist(p);
					if (p.getId() > 0) {
						res = 0;
						em.flush();
					}
				} else {
					res = -2;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("OrderDetailService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int update(OrderDetailReqInfo t) {
		int res = -1;
		try {
			OrderDetail p = t.getOrder_detail();
			if (p != null) {
				Product product = p.getProduct();
				OrderLix orderLix = p.getOrder_lix();
				int check = checkExsits(product != null ? product.getId() : 0, p.getId(),
						orderLix != null ? orderLix.getId() : 0);
				res = check;
				if (check == 0) {
					p = em.merge(p);
					if (p != null) {
						selectById(p.getId(), t);
						res = 0;
					}
				} else {
					res = -2;
				}
			}
		} catch (Exception e) {
			logger.error("OrderDetailService.update:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int updateSort(OrderDetail orderDetail) {
		int res = -1;
		try {
			Query query = em
					.createQuery("update OrderDetail set indexsort=:indexsort,columnsort=:columnsort,ascd=:ascd where id=:id");
			query.setParameter("indexsort", orderDetail.getIndexsort());
			query.setParameter("columnsort", orderDetail.getColumnsort());
			query.setParameter("ascd", orderDetail.isAscd());
			query.setParameter("id", orderDetail.getId());
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error("InvoiceService.updateSort:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int updateHuy(OrderDetail orderDetail) {
		int res = -1;
		try {
			Query query = em.createQuery("update OrderDetail set huyct=:huyct where id=:id");
			query.setParameter("huyct", orderDetail.isHuyct());
			query.setParameter("id", orderDetail.getId());
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error("InvoiceService.updateHuy:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int updateBoxQuantityActual(OrderDetail orderDetail) {
		int res = -1;
		try {
			// Query query = em
			// .createQuery("update OrderDetail set box_quantity_actual=:sl,quantity=:slct,total=:tt where id=:id");
			// query.setParameter("sl", orderDetail.getBox_quantity_actual());
			// query.setParameter("slct", orderDetail.getQuantity());
			// query.setParameter("tt", orderDetail.getTotal());
			// query.setParameter("id", orderDetail.getId());
			// return query.executeUpdate();

			// sua lai 22/06/2023
			Query query = em.createQuery("update OrderDetail set box_quantity_actual=:sl,quantity=:slct where id=:id");
			query.setParameter("sl", orderDetail.getBox_quantity_actual());
			query.setParameter("slct", orderDetail.getQuantity());
			query.setParameter("id", orderDetail.getId());
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error("InvoiceService.updateBoxQuantityActual:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int updateMaLH(OrderDetail orderDetail) {
		int res = -1;
		try {
			Query query = em.createQuery("update OrderDetail set batch_code=:batch_code where id=:id");
			query.setParameter("batch_code", orderDetail.getBatch_code());
			query.setParameter("id", orderDetail.getId());
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error("InvoiceService.updateBoxQuantityActual:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int updateTotal(OrderDetail orderDetail) {
		int res = -1;
		try {
			Query query = em.createQuery("update OrderDetail set total=:total where id=:id");
			query.setParameter("total", orderDetail.getTotal());
			query.setParameter("id", orderDetail.getId());
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error("InvoiceService.updateBoxQuantityActual:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int updatePriceTotal(OrderDetail orderDetail) {
		int res = -1;
		try {
			Query query = em.createQuery("update OrderDetail set unit_price=:unit_price,total=:total where id=:id");
			query.setParameter("unit_price", orderDetail.getUnit_price());
			query.setParameter("total", orderDetail.getTotal());
			query.setParameter("id", orderDetail.getId());
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error("InvoiceService.updateBoxQuantityActual:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectById(long id, OrderDetailReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderDetail> cq = cb.createQuery(OrderDetail.class);
			Root<OrderDetail> root = cq.from(OrderDetail.class);
			root.fetch("product", JoinType.INNER);
			root.fetch("order_lix", JoinType.INNER);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<OrderDetail> query = em.createQuery(cq);
			t.setOrder_detail(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			logger.error("OrderDetailService.selectById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public OrderDetail selectById(long id) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderDetail> cq = cb.createQuery(OrderDetail.class);
			Root<OrderDetail> root = cq.from(OrderDetail.class);
			root.fetch("product", JoinType.INNER);
			root.fetch("order_lix", JoinType.INNER);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<OrderDetail> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			logger.error("OrderDetailService.selectById:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int deleteById(long id) {
		int res = -1;
		try {
			// JQPL
			// delete danh sách sản phẩm khuyến mãi
			Query query = em.createQuery("delete from PromotionOrderDetail where order_detail.id=:id ");
			query.setParameter("id", id);
			if (query.executeUpdate() >= 0) {
				Query query2 = em.createQuery("delete from OrderDetail where id=:id ");
				query2.setParameter("id", id);
				res = query2.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("OrderDetailService.deleteById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int checkExsits(long productId, long id, long orderId) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<OrderDetail> root = cq.from(OrderDetail.class);
			ParameterExpression<Long> pId = cb.parameter(Long.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			ParameterExpression<Long> pOrderId = cb.parameter(Long.class);
			Predicate dis = cb.disjunction();
			Predicate con1 = cb.conjunction();
			Predicate con2 = cb.conjunction();
			con1.getExpressions().add(cb.equal(pId, 0));
			con1.getExpressions().add(cb.equal(root.get("order_lix").get("id"), pOrderId));
			con1.getExpressions().add(cb.equal(root.get("product").get("id"), pProductId));
			dis.getExpressions().add(con1);
			con2.getExpressions().add(cb.notEqual(pId, 0));
			con2.getExpressions().add(cb.notEqual(root.get("id"), pId));
			con2.getExpressions().add(cb.equal(root.get("order_lix").get("id"), pOrderId));
			con2.getExpressions().add(cb.equal(root.get("product").get("id"), pProductId));
			dis.getExpressions().add(con2);
			cq.select(cb.count(root.get("id"))).where(dis);
			TypedQuery<Long> query = em.createQuery(cq);
			query.setParameter(pId, id);
			query.setParameter(pProductId, productId);
			query.setParameter(pOrderId, orderId);
			res = query.getSingleResult().intValue();
		} catch (Exception e) {
			logger.error("OrderDetailService.checkExist:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int deleteAll(long orderId) {
		int res = -1;
		try {
			// JPQL
			Query query = em.createQuery("delete from OrderDetail where order_lix.id =:id ");
			query.setParameter("id", orderId);
			res = query.executeUpdate();
		} catch (Exception e) {
			logger.error("OrderDetailService.deleteAll:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectBy(String orderCode, String productCode, List<OrderDetail> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderDetail> cq = cb.createQuery(OrderDetail.class);
			Root<OrderDetail> root = cq.from(OrderDetail.class);
			Join<OrderDetail, Product> product_ = root.join("product", JoinType.INNER);
			Join<OrderDetail, OrderLix> orderLix_ = root.join("order_lix", JoinType.INNER);
			Predicate con = cb.conjunction();
			con.getExpressions().add(cb.equal(orderLix_.get("order_code"), orderCode));
			con.getExpressions().add(cb.equal(product_.get("product_code"), productCode));
			cq.select(root).where(con);
			TypedQuery<OrderDetail> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("OrderDetailService.selectBy:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int insertLoad(OrderDetailReqInfo t) {
		int res = -1;
		try {
			OrderDetail p = t.getOrder_detail();
			if (p != null) {
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("OrderDetailService.insertLoad:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public List<KeHoachGiaoHang> findBy(boolean hcm, boolean bd, Date ngaygh) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();

			// TRUY VẤN CHÍNH: chỉ gom THEO KHÁCH + CARRIER
			CriteriaQuery<KeHoachGiaoHang> cq = cb.createQuery(KeHoachGiaoHang.class);
			Root<OrderDetail> root = cq.from(OrderDetail.class);
			Join<OrderDetail, Product> product_ = root.join("product", JoinType.INNER);
			Join<OrderDetail, OrderLix> orderLix_ = root.join("order_lix", JoinType.INNER);
			Join<OrderLix, Carrier> carrierJoin = orderLix_.join("carrier", JoinType.LEFT);

			// Điều kiện ngày: so sánh theo DATE, bỏ giờ
			Expression<Date> ngayXevaoDateOnly = cb.function("date", Date.class, orderLix_.get("ngaygioxevao"));

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.equal(ngayXevaoDateOnly, ngaygh));

			// Điều kiện brand
			List<Predicate> brandPredicates = new ArrayList<>();
			if (hcm) {
				brandPredicates.add(cb.equal(orderLix_.get("brand"), "HO CHI MINH"));
			}
			if (bd) {
				brandPredicates.add(cb.equal(orderLix_.get("brand"), "BINH DUONG"));
			}
			if (!brandPredicates.isEmpty()) {
				predicates.add(cb.or(brandPredicates.toArray(new Predicate[0])));
			}

			// ===== SELECT CHÍNH =====
			// !!! QUAY LẠI ĐÚNG 9 THAM SỐ NHƯ BẢN GỐC !!!
			cq.select(cb.construct(KeHoachGiaoHang.class,
			// 1. customer (GROUP BY)
					orderLix_.get("customer"),
					// 2. delivery_date đại diện
					cb.max(orderLix_.get("delivery_date")),
					// 3. ngaygioxevao đại diện
					cb.max(orderLix_.get("ngaygioxevao")),
					// 4. khoiluongvc gốc (placeholder, sẽ override sau)
					cb.max(orderLix_.get("khoiluongvc")),
					// 5. carrier (GROUP BY)
					carrierJoin,
					// 6. luuythanhtoan đại diện
					cb.max(orderLix_.get("luuythanhtoan")),
					// 7. tổng khối lượng (placeholder, giá trị thật lấy từ map)
					cb.literal(0d),
					// 8. ie_categories đại diện
					cb.max(orderLix_.get("ie_categories")),
					// 9. id đại diện
					cb.max(orderLix_.get("id")))).where(predicates.toArray(new Predicate[0]))
					.groupBy(orderLix_.get("customer"), carrierJoin);

			TypedQuery<KeHoachGiaoHang> query = em.createQuery(cq);
			List<KeHoachGiaoHang> keHoachGiaoHangs = query.getResultList();

			// ==== TÍNH TỔNG SỐ XE & KHỐI LƯỢNG VC THEO (KHÁCH, CARRIER) ====
			Map<String, Long> tongSlXeMap = calculateTotalSlXe(hcm, bd, ngaygh);
			Map<String, Double> tongKhoiLuongVCMap = calculateTotalKhoiLuongVC(hcm, bd, ngaygh);

			for (KeHoachGiaoHang kh : keHoachGiaoHangs) {
				Long customerId = (kh.getCustomer() != null) ? kh.getCustomer().getId() : null;
				Long carrierId = (kh.getCarrier() != null) ? kh.getCarrier().getId() : null;
				String key = customerId + "#" + (carrierId != null ? carrierId : "NULL");

				// Cập nhật tổng SLXE đúng (theo từng order rồi gộp)
				Long totalSlXe = tongSlXeMap.get(key);
				if (totalSlXe != null) {
					kh.setSlxe(totalSlXe.intValue());
				}

				// Cập nhật tổng khối lượng VC đúng (logic: header !=0 thì dùng
				// header, =0 thì dùng tổng chi tiết)
				Double totalKhoiLuongVC = tongKhoiLuongVCMap.get(key);
				if (totalKhoiLuongVC != null) {
					kh.setKhoiluongvc(totalKhoiLuongVC);
				}

				// Khởi tạo lazy
				if (kh.getCustomer() != null)
					Hibernate.initialize(kh.getCustomer());
				if (kh.getCarrier() != null)
					Hibernate.initialize(kh.getCarrier());
				if (kh.getIeCategories() != null)
					Hibernate.initialize(kh.getIeCategories());
			}

			return keHoachGiaoHangs;
		} catch (Exception e) {
			logger.error("OrderDetailService.findBy: " + e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	private Map<String, Double> calculateTotalKhoiLuongVC(boolean hcm, boolean bd, Date ngaygh) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<OrderDetail> root = cq.from(OrderDetail.class);

		Join<OrderDetail, Product> product_ = root.join("product", JoinType.INNER);
		Join<OrderDetail, OrderLix> ol = root.join("order_lix", JoinType.INNER);
		Join<OrderLix, Carrier> carrierJoin = ol.join("carrier", JoinType.LEFT);

		// ====== Điều kiện ngày (so sánh theo DATE, bỏ giờ) ======
		Expression<Date> ngayXevaoDateOnly = cb.function("date", Date.class, ol.get("ngaygioxevao"));

		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(ngayXevaoDateOnly, ngaygh));

		// ====== Điều kiện brand ======
		List<Predicate> brandPredicates = new ArrayList<>();
		if (hcm) {
			brandPredicates.add(cb.equal(ol.get("brand"), "HO CHI MINH"));
		}
		if (bd) {
			brandPredicates.add(cb.equal(ol.get("brand"), "BINH DUONG"));
		}
		if (!brandPredicates.isEmpty()) {
			predicates.add(cb.or(brandPredicates.toArray(new Predicate[0])));
		}
		cq.where(predicates.toArray(new Predicate[0]));

		// ====== Tính khối lượng theo CHI TIẾT ======
		// KHÔNG dùng .as(Double.class) để tránh CAST ... AS double precision
		Expression<Number> calculatedWeight = cb.prod(
				cb.prod(cb.diff(root.<Number> get("box_quantity"), root.<Number> get("box_quantity_actual")),
						product_.<Number> get("specification")), product_.<Number> get("factor"));

		Expression<Number> sumDetailWeight = cb.sum(calculatedWeight);

		cq.multiselect(ol.get("customer").get("id").alias("customerId"), carrierJoin.get("id").alias("carrierId"),
				ol.get("id").alias("orderId"), ol.<Number> get("khoiluongvc").alias("khoiluongvc"),
				sumDetailWeight.alias("detailWeight")).groupBy(ol.get("customer").get("id"), carrierJoin.get("id"),
				ol.get("id"), ol.get("khoiluongvc"));

		List<Tuple> results = em.createQuery(cq).getResultList();

		Map<String, Double> khoiLuongVCMap = new HashMap<>();

		for (Tuple tuple : results) {
			Long customerId = tuple.get("customerId", Long.class);
			Long carrierId = tuple.get("carrierId", Long.class);

			Number khoiluongvcNum = tuple.get("khoiluongvc", Number.class);
			Number detailWeightNum = tuple.get("detailWeight", Number.class);

			Double khoiluongvc = khoiluongvcNum != null ? khoiluongvcNum.doubleValue() : null;
			Double detailWeight = detailWeightNum != null ? detailWeightNum.doubleValue() : null;

			double orderWeight;
			if (khoiluongvc != null && khoiluongvc.doubleValue() != 0d) {
				orderWeight = khoiluongvc;
			} else {
				orderWeight = (detailWeight != null ? detailWeight : 0d);
			}

			String key = customerId + "#" + (carrierId != null ? carrierId : "NULL");
			khoiLuongVCMap.merge(key, orderWeight, Double::sum);
		}

		return khoiLuongVCMap;
	}

	private Map<String, Long> calculateTotalSlXe(boolean hcm, boolean bd, Date ngaygh) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		// Tuple để chứa (Customer ID, Carrier ID, SUM(slxe))
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<OrderLix> ol = cq.from(OrderLix.class);
		Join<OrderLix, Carrier> carrierJoin = ol.join("carrier", JoinType.LEFT);

		// Conditions (cùng ngày và brand logic)
		Expression<Date> ngayXevaoDateOnly = cb.function("date", Date.class, ol.get("ngaygioxevao"));
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(ngayXevaoDateOnly, ngaygh));

		List<Predicate> brandPredicates = new ArrayList<>();
		if (hcm) {
			brandPredicates.add(cb.equal(ol.get("brand"), "HO CHI MINH"));
		}
		if (bd) {
			brandPredicates.add(cb.equal(ol.get("brand"), "BINH DUONG"));
		}
		if (!brandPredicates.isEmpty()) {
			predicates.add(cb.or(brandPredicates.toArray(new Predicate[0])));
		}
		cq.where(predicates.toArray(new Predicate[0]));

		// SELECT và GROUP BY (Customer ID và Carrier ID)
		cq.multiselect(ol.get("customer").get("id").alias("customerId"), carrierJoin.get("id").alias("carrierId"),
				cb.sum(ol.<Long> get("slxe")).alias("totalSlXe")).groupBy(ol.get("customer").get("id"),
				carrierJoin.get("id"));

		List<Tuple> results = em.createQuery(cq).getResultList();

		Map<String, Long> slXeMap = new HashMap<>();
		for (Tuple tuple : results) {
			Long customerId = tuple.get("customerId", Long.class);
			Long carrierId = tuple.get("carrierId", Long.class);
			Long totalSlXe = tuple.get("totalSlXe", Long.class);

			// Key: Customer ID + "#" + Carrier ID (hoặc "NULL")
			String key = customerId + "#" + (carrierId != null ? carrierId : "NULL");
			slXeMap.put(key, totalSlXe);
		}
		return slXeMap;
	}

	@Inject
	ICustomerService customerService;
	@Inject
	ICarrierService carrierService;

	@Override
	public List<KeHoachGiaoHang> findBy2(boolean hcm, boolean bd, Date ngaygh) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();

			// TRUY VẤN CHÍNH: chỉ gom THEO KHÁCH + CARRIER
			CriteriaQuery<KeHoachGiaoHang> cq = cb.createQuery(KeHoachGiaoHang.class);
			Root<OrderDetail> root = cq.from(OrderDetail.class);
			Join<OrderDetail, Product> product_ = root.join("product", JoinType.INNER);
			Join<OrderDetail, OrderLix> orderLix_ = root.join("order_lix", JoinType.INNER);
			Join<OrderLix, Carrier> carrierJoin = orderLix_.join("carrier", JoinType.LEFT);

			// Điều kiện ngày: so sánh theo DATE, bỏ giờ
			Expression<Date> ngayXevaoDateOnly = cb.function("date", Date.class, orderLix_.get("ngaygioxevao"));

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.equal(ngayXevaoDateOnly, ngaygh));

			// Điều kiện brand
			List<Predicate> brandPredicates = new ArrayList<>();
			if (hcm) {
				brandPredicates.add(cb.equal(orderLix_.get("brand"), "HO CHI MINH"));
			}
			if (bd) {
				brandPredicates.add(cb.equal(orderLix_.get("brand"), "BINH DUONG"));
			}
			if (!brandPredicates.isEmpty()) {
				predicates.add(cb.or(brandPredicates.toArray(new Predicate[0])));
			}

			// ===== SELECT CHÍNH =====
			// !!! QUAY LẠI ĐÚNG 9 THAM SỐ NHƯ BẢN GỐC !!!
			cq.select(cb.construct(KeHoachGiaoHang.class,
			// 1. customer (GROUP BY)
					orderLix_.get("customer"),
					// 2. delivery_date đại diện
					cb.max(orderLix_.get("delivery_date")),
					// 3. ngaygioxevao đại diện
					cb.max(orderLix_.get("ngaygioxevao")),
					// 4. khoiluongvc gốc (placeholder, sẽ override sau)
					cb.max(orderLix_.get("khoiluongvc")),
					// 5. carrier (GROUP BY)
					carrierJoin,
					// 6. luuythanhtoan đại diện
					cb.max(orderLix_.get("luuythanhtoan")),
					// 7. tổng khối lượng (placeholder, giá trị thật lấy từ map)
					cb.literal(0d),
					// 8. ie_categories đại diện
					cb.max(orderLix_.get("ie_categories")),
					// 9. id đại diện
					cb.max(orderLix_.get("id")))).where(predicates.toArray(new Predicate[0]))
					.groupBy(orderLix_.get("customer"), carrierJoin);

			TypedQuery<KeHoachGiaoHang> query = em.createQuery(cq);
			List<KeHoachGiaoHang> keHoachGiaoHangs = query.getResultList();

			// ==== TÍNH TỔNG SỐ XE & KHỐI LƯỢNG VC THEO (KHÁCH, CARRIER) ====
			Map<String, Long> tongSlXeMap = calculateTotalSlXe(hcm, bd, ngaygh);
			Map<String, Double> tongKhoiLuongVCMap = calculateTotalKhoiLuongVC(hcm, bd, ngaygh);

			for (KeHoachGiaoHang kh : keHoachGiaoHangs) {
				Long customerId = (kh.getCustomer() != null) ? kh.getCustomer().getId() : null;
				Long carrierId = (kh.getCarrier() != null) ? kh.getCarrier().getId() : null;
				String key = customerId + "#" + (carrierId != null ? carrierId : "NULL");

				// Cập nhật tổng SLXE đúng (theo từng order rồi gộp)
				Long totalSlXe = tongSlXeMap.get(key);
				if (totalSlXe != null) {
					kh.setSlxe(totalSlXe.intValue());
				}

				// Cập nhật tổng khối lượng VC đúng (logic: header !=0 thì dùng
				// header, =0 thì dùng tổng chi tiết)
				Double totalKhoiLuongVC = tongKhoiLuongVCMap.get(key);
				if (totalKhoiLuongVC != null) {
					kh.setKhoiluongvc(totalKhoiLuongVC);
				}

				// ★ THAY THẾ Hibernate.initialize bằng cách set lại DTO/Entity
				// nếu cần
				if (kh.getCustomer() != null)
					kh.setCustomer(new Customer(kh.getCustomer().getId(), kh.getCustomer().getCustomer_code(), kh
							.getCustomer().getCustomer_name()));
				if (kh.getCarrier() != null)
					kh.setCarrier(new Carrier(kh.getCarrier().getId(), kh.getCarrier().getCarrier_code(), kh
							.getCarrier().getCarrier_name()));
				if (kh.getIeCategories() != null)
					kh.setIeCategories(new IECategories(kh.getIeCategories().getId(), kh.getIeCategories().getCode(),
							kh.getIeCategories().getContent()));
			}

			return keHoachGiaoHangs;
		} catch (Exception e) {
			logger.error("OrderDetailService.findBy: " + e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	@Override
	public double khoiluongtongdonhang(long idorder) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ROUND( ");
		sql.append("    ( ");
		sql.append("        SELECT COALESCE(SUM(od.box_quantity * p.specification * p.factor), 0) ");
		sql.append("        FROM orderdetail od ");
		sql.append("        JOIN product p ON od.product_id = p.id ");
		sql.append("        WHERE od.order_lix_id = :idorder ");
		sql.append("    ) + ");
		sql.append("    ( ");
		sql.append("        SELECT COALESCE(SUM(pod.quantity * p.factor), 0) ");
		sql.append("        FROM promotionorderdetail pod ");
		sql.append("        JOIN orderdetail od ON pod.order_detail_id = od.id ");
		sql.append("        JOIN product p ON pod.product_id = p.id ");
		sql.append("        WHERE od.order_lix_id = :idorder ");
		sql.append("    ) ");
		sql.append(") AS khoiluong_vanchuyen;");
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter("idorder", idorder);
		return ((BigDecimal) query.getSingleResult()).doubleValue();
	}
}
