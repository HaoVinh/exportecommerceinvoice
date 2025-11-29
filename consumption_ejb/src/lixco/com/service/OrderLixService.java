package lixco.com.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.commom_ejb.ToolTimeCustomer;
import lixco.com.entity.Car;
import lixco.com.entity.Customer;
import lixco.com.entity.ExportBatchOD;
import lixco.com.entity.ExportBatchPOD;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.OrderDetail;
import lixco.com.entity.OrderLix;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.Product;
import lixco.com.entity.PromotionProgram;
import lixco.com.interfaces.IOrderLixService;
import lixco.com.reqInfo.OrderLixReqInfo;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class OrderLixService implements IOrderLixService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int search(String json, List<OrderLix> list, List<Integer> statusSearch) {
		int res = -1;
		try {/*
			 * { order_info:{from_date:
			 * '',to_date:'',customer_id:0,order_code:'',voucher_code:'',ie_categories_id:0,po_no:'',delivered:-1,status:-1},
			 * page:{page_index:0, page_size:0}}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hstextStr = JsonParserUtil.getValueString(j.get("order_info"), "stextStr", null);
			HolderParser hFromDate = JsonParserUtil.getValueString(j.get("order_info"), "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j.get("order_info"), "to_date", null);
			
			HolderParser hDeliveryDate = JsonParserUtil.getValueString(j.get("order_info"), "delivery_date", null);
			HolderParser hNgayGioVaoDate = JsonParserUtil.getValueString(j.get("order_info"), "ngaygioxevao", null);
			
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j.get("order_info"), "customer_id", null);
			HolderParser hOrderCode = JsonParserUtil.getValueString(j.get("order_info"), "order_code", null);
			HolderParser hVoucherCode = JsonParserUtil.getValueString(j.get("order_info"), "voucher_code", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(j.get("order_info"), "ie_categories_id", null);
			HolderParser hPoNo = JsonParserUtil.getValueString(j.get("order_info"), "po_no", null);
			HolderParser hDelivered = JsonParserUtil.getValueNumber(j.get("order_info"), "delivered", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j.get("order_info"), "product_id", null);
			HolderParser hBrand = JsonParserUtil.getValueString(j.get("order_info"), "brand", null);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderLix> cq = cb.createQuery(OrderLix.class);
			Root<OrderLix> root_ = cq.from(OrderLix.class);
			root_.fetch("customer", JoinType.LEFT);
			root_.fetch("promotion_program", JoinType.LEFT);
			root_.fetch("pricing_program", JoinType.LEFT);
			root_.fetch("payment_method", JoinType.LEFT);
			root_.fetch("car", JoinType.LEFT);
			root_.fetch("warehouse", JoinType.LEFT);
			root_.fetch("ie_categories", JoinType.LEFT);
			root_.fetch("delivery_pricing", JoinType.LEFT);
			root_.fetch("freight_contract", JoinType.LEFT);
			root_.fetch("freight_contract", JoinType.LEFT);
			root_.fetch("carrier", JoinType.LEFT);

			List<Predicate> predicates = new ArrayList<Predicate>();
			ParameterExpression<Date> pFromDate = cb.parameter(Date.class);
			ParameterExpression<Date> pToDate = cb.parameter(Date.class);
			
			ParameterExpression<Date> pDeliveryDate = cb.parameter(Date.class);
			ParameterExpression<Date> pNgayGioVaoDate = cb.parameter(Date.class);
			
			ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
			ParameterExpression<String> pOrderCode = cb.parameter(String.class);
			ParameterExpression<String> pVoucherCode = cb.parameter(String.class);
			ParameterExpression<Long> pIECategoriesId = cb.parameter(Long.class);
			ParameterExpression<String> pPoNo = cb.parameter(String.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);

			Subquery<Long> subquery = cq.subquery(Long.class);
			Root<OrderDetail> rootSub = subquery.from(OrderDetail.class);
			subquery.select(rootSub.get("order_lix").get("id")).where(cb.equal(rootSub.get("product"), pProductId));

			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.isNull(pFromDate));
			dis.getExpressions().add(cb.equal(pFromDate, ""));
			dis.getExpressions().add(cb.greaterThanOrEqualTo(root_.get("order_date"), pFromDate));
			predicates.add(dis);
			
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.isNull(pToDate));
			dis1.getExpressions().add(cb.equal(pToDate, ""));
			dis1.getExpressions().add(cb.lessThanOrEqualTo(root_.get("order_date"), pToDate));
			predicates.add(dis1);
			
			Predicate dis_ = cb.disjunction();
			dis_.getExpressions().add(cb.isNull(pDeliveryDate));
			dis_.getExpressions().add(cb.equal(pDeliveryDate, ""));
			dis_.getExpressions().add(cb.equal(root_.get("delivery_date"), pDeliveryDate));
			predicates.add(dis_);
			
			 Expression<Date> ngayXevaoDateOnly =
		                cb.function("date", Date.class, root_.get("ngaygioxevao"));
			Predicate dis1_ = cb.disjunction();
			dis1_.getExpressions().add(cb.isNull(pNgayGioVaoDate));
			dis1_.getExpressions().add(cb.equal(pNgayGioVaoDate, ""));
			dis1_.getExpressions().add(cb.equal(ngayXevaoDateOnly, pNgayGioVaoDate));
			predicates.add(dis1_);
			
			
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.equal(pCustomerId, 0));
			dis2.getExpressions().add(cb.equal(root_.get("customer").get("id"), pCustomerId));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pOrderCode, ""));
			dis3.getExpressions().add(cb.like(root_.get("order_code"), pOrderCode));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pVoucherCode, ""));
			dis4.getExpressions().add(cb.like(root_.get("voucher_code"), pVoucherCode));
			predicates.add(dis4);
			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.equal(pIECategoriesId, 0));
			dis5.getExpressions().add(cb.equal(root_.get("ie_categories").get("id"), pIECategoriesId));
			predicates.add(dis5);
			Predicate dis7 = cb.disjunction();
			dis7.getExpressions().add(cb.equal(pPoNo, ""));
			dis7.getExpressions().add(cb.like(root_.get("po_no"), pPoNo));
			predicates.add(dis7);

			String valueBrand = (String) hBrand.getValue();
			if (valueBrand != null && !"TAT CA".equals(valueBrand)) {
				predicates.add(cb.equal(root_.get("brand"), valueBrand));
			}

			Predicate disStatusSearch = cb.disjunction();
			Predicate disDHHuy = cb.disjunction();

			boolean stadd = false;
			boolean locdhhuy = false;
			for (int i = 0; i < statusSearch.size(); i++) {
				int trangthai = Integer.parseInt(statusSearch.get(i) + "");
				if (trangthai != -1 && trangthai < 3) {
					stadd = true;
					disStatusSearch.getExpressions().add(cb.equal(root_.get("status"), trangthai));
				} else if (trangthai == 3) {
					locdhhuy = true;
					disDHHuy.getExpressions().add(cb.equal(root_.get("dhhuy"), true));
				} else {
					if (trangthai == 4) {
						locdhhuy = true;
						disDHHuy.getExpressions().add(cb.equal(root_.get("dhhuy"), false));
					}
				}
			}
			if (stadd)
				predicates.add(disStatusSearch);
			if (locdhhuy)
				predicates.add(disDHHuy);

			Predicate dis9 = cb.disjunction();
			dis9.getExpressions().add(cb.equal(pProductId, 0));
			dis9.getExpressions().add(cb.in(root_.get("id")).value(subquery));
			predicates.add(dis9);

			List<Predicate> predicatesStr = new ArrayList<Predicate>();
			String stextStr = Objects.toString(hstextStr.getValue());
			if (hstextStr.getValue() != null && !"".equals(stextStr.trim())) {
				Predicate predicateinvoice_code = cb.like(root_.get("order_code"), "%" + stextStr + "%");// id
																											// fox
				predicatesStr.add(predicateinvoice_code);
				Predicate predicatevoucher_code = cb.like(root_.get("voucher_code"), "%" + stextStr + "%");// so
																											// chung
																											// tu
				predicatesStr.add(predicatevoucher_code);
				Join<Invoice, Customer> cus_ = root_.join("customer", JoinType.LEFT);
				Predicate predicateCuscode = cb.like(cus_.get("customer_code"), "%" + stextStr + "%");// ma
																										// khach
																										// hang
				predicatesStr.add(predicateCuscode);
				Predicate predicateCus = cb.like(cus_.get("customer_name"), "%" + stextStr + "%");// ten
																									// khach
																									// hang
				predicatesStr.add(predicateCus);
				Predicate predicatecreated_by = cb.like(root_.get("created_by"), "%" + stextStr + "%");// nguoi
				predicatesStr.add(predicatecreated_by);
				// tao
				Predicate predicatepo_no = cb.like(root_.get("po_no"), "%" + stextStr + "%");// so
																								// po
				predicatesStr.add(predicatepo_no);

				Join<OrderLix, Car> car_ = root_.join("car", JoinType.LEFT);
				Predicate predicatecar = cb.like(car_.get("license_plate"), "%" + stextStr + "%");// so
																									// xe
				predicatesStr.add(predicatecar);

				List<Predicate> subpredicates = new LinkedList<Predicate>();
				Subquery<OrderDetail> sqOne = cq.subquery(OrderDetail.class);
				Root subroot = sqOne.from(OrderDetail.class);
				Join<OrderDetail, Product> prd_ = subroot.join("product", JoinType.LEFT);
				Predicate predicatePdCode = cb.like(prd_.get("product_code"), "%" + stextStr + "%");
				subpredicates.add(predicatePdCode);
				Predicate predicatePdName = cb.like(prd_.get("product_name"), "%" + stextStr + "%");
				subpredicates.add(predicatePdName);
				sqOne.select(subroot.get("order_lix")).where(cb.or(subpredicates.toArray(new Predicate[0])));
				Predicate predicateSub = cb.equal(root_, cb.any(sqOne));
				predicatesStr.add(predicateSub);// truy cap chi tiet
			}
			if (predicatesStr.size() != 0) {
				cq.select(root_)
						.where(cb.and(predicates.toArray(new Predicate[0])),
								cb.or(predicatesStr.toArray(new Predicate[0])))
						.orderBy(cb.desc(root_.get("order_code")));
			} else {
				cq.select(root_).where(cb.and(predicates.toArray(new Predicate[0])))
						.orderBy(cb.desc(root_.get("order_code")));
			}

			TypedQuery<OrderLix> query = em.createQuery(cq);

			query.setParameter(pFromDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pToDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy"));
			
			query.setParameter(pDeliveryDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hDeliveryDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pNgayGioVaoDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hNgayGioVaoDate.getValue(), null), "dd/MM/yyyy"));
			
			query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue(), "0")));
			query.setParameter(pOrderCode, "%" + Objects.toString(hOrderCode.getValue(), "") + "%");
			query.setParameter(pVoucherCode, Objects.toString(hVoucherCode.getValue(), ""));
			query.setParameter(pIECategoriesId, Long.parseLong(Objects.toString(hIECategoriesId.getValue(), "0")));
			query.setParameter(pPoNo, Objects.toString(hPoNo.getValue(), ""));
			query.setParameter(pProductId, Long.parseLong(Objects.toString(hProductId.getValue())));

			list.addAll(query.getResultList());
			res = 0;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("OrderLixService.search:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int insert(OrderLixReqInfo t) {
		int res = -1;
		try {
			OrderLix p = t.getOrder_lix();
			if (p != null) {
				p.setOrder_code(initOrderCode());
				if (p.getVoucher_code() == null || "".equals(p.getVoucher_code())) {
					p.setVoucher_code(initVoucherCode(p.getOrder_date(), 0));
				}
				int check = checkByCode(p.getOrder_code(), p.getId());
				if (check > 0) {
					res = -2;
				} else if (check != -1) {
					em.persist(p);
					if (p.getId() > 0) {
						selectById(p.getId(), t);
						res = 0;
					}
				}
			}
		} catch (Exception e) {
			logger.error("OrderLixService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int update(OrderLixReqInfo t) {
		int res = -1;
		try {
			OrderLix p = t.getOrder_lix();
			if (p != null) {
				int check = checkByCode(p.getOrder_code(), p.getId());
				if (check > 0) {
					res = -2;
				} else if (check != -1) {
					p = em.merge(p);
					if (p != null) {
						OrderLixReqInfo f = new OrderLixReqInfo();
						selectById(p.getId(), f);
						t.setOrder_lix(f.getOrder_lix());
						res = 0;
					}
				}
			}
		} catch (Exception e) {
			logger.error("OrderLixService.update:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectById(long id, OrderLixReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderLix> cq = cb.createQuery(OrderLix.class);
			Root<OrderLix> root_ = cq.from(OrderLix.class);
			root_.fetch("customer", JoinType.INNER);
			root_.fetch("promotion_program", JoinType.LEFT);
			root_.fetch("pricing_program", JoinType.LEFT);
			root_.fetch("payment_method", JoinType.LEFT);
			root_.fetch("car", JoinType.LEFT);
			root_.fetch("warehouse", JoinType.LEFT);
			root_.fetch("ie_categories", JoinType.LEFT);
			root_.fetch("delivery_pricing", JoinType.LEFT);
			root_.fetch("freight_contract", JoinType.LEFT);
			root_.fetch("carrier", JoinType.LEFT);
			cq.select(root_).where(cb.equal(root_.get("id"), id));
			TypedQuery<OrderLix> query = em.createQuery(cq);
			t.setOrder_lix(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			logger.error("OrderLixService.selectById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public OrderLix selectById(long id) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderLix> cq = cb.createQuery(OrderLix.class);
			Root<OrderLix> root_ = cq.from(OrderLix.class);
			root_.fetch("customer", JoinType.INNER);
			root_.fetch("promotion_program", JoinType.LEFT);
			root_.fetch("pricing_program", JoinType.LEFT);
			root_.fetch("payment_method", JoinType.LEFT);
			root_.fetch("car", JoinType.LEFT);
			root_.fetch("warehouse", JoinType.LEFT);
			root_.fetch("ie_categories", JoinType.LEFT);
			root_.fetch("delivery_pricing", JoinType.LEFT);
			root_.fetch("freight_contract", JoinType.LEFT);
			root_.fetch("carrier", JoinType.LEFT);
			cq.select(root_).where(cb.equal(root_.get("id"), id));
			TypedQuery<OrderLix> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			logger.error("OrderLixService.selectById:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public OrderLix finByIdAll(long id) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderLix> cq = cb.createQuery(OrderLix.class);
			Root<OrderLix> root_ = cq.from(OrderLix.class);
			root_.fetch("customer", JoinType.INNER);
			root_.fetch("promotion_program", JoinType.LEFT);
			root_.fetch("pricing_program", JoinType.LEFT);
			root_.fetch("payment_method", JoinType.LEFT);
			root_.fetch("car", JoinType.LEFT);
			root_.fetch("warehouse", JoinType.LEFT);
			root_.fetch("ie_categories", JoinType.LEFT);
			root_.fetch("delivery_pricing", JoinType.LEFT);
			root_.fetch("freight_contract", JoinType.LEFT);
			root_.fetch("carrier", JoinType.LEFT);
			cq.select(root_).where(cb.equal(root_.get("id"), id));
			TypedQuery<OrderLix> query = em.createQuery(cq);
			return query.getSingleResult();

		} catch (Exception e) {
			logger.error("OrderLixService.selectById:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int deleteById(long id) {
		int res = -1;
		try {
			// delete tất cả chi tiết sản phẩm khuyến mãi
			Query queryDelPromotionOrderDetail = em
					.createQuery("delete PromotionOrderDetail as p where p.order_detail.id in (select dt.id from OrderDetail as dt where dt.order_lix.id=:id ) ");
			queryDelPromotionOrderDetail.setParameter("id", id);
			if (queryDelPromotionOrderDetail.executeUpdate() >= 0) {
				// thực hiện xóa chi tiết đơn hàng
				Query queryDelOrderDetail = em.createQuery("delete OrderDetail as dt where dt.order_lix.id=:id");
				queryDelOrderDetail.setParameter("id", id);
				if (queryDelOrderDetail.executeUpdate() >= 0) {
					// thực hiện delete đơn hàng
					Query queryDelOrderLix = em.createQuery("delete OrderLix as d where d.id=:id");
					queryDelOrderLix.setParameter("id", id);
					if (queryDelOrderLix.executeUpdate() > 0) {
						res = 0;
					} else {
						ct.getUserTransaction().rollback();
					}
				} else {
					ct.getUserTransaction().rollback();
				}

			} else {
				ct.getUserTransaction().rollback();
			}
		} catch (Exception e) {
			logger.error("OrderLixService.deleteById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public String initOrderCode() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
			Root<OrderLix> root = cq.from(OrderLix.class);
			// cq.multiselect(cb.coalesce(cb.max(root.get("id")),0));
			cq.multiselect(cb.coalesce(cb.max(cb.quot((Expression) root.get("order_code"), 1)), 0));
			TypedQuery<Integer> query = em.createQuery(cq);
			int max = query.getSingleResult();
			double p = (double) max / 100000000;
			if (p < 1) {
				return String.format("%08d", max + 1);
			}
			return (max + 1) + "";
		} catch (Exception e) {
			logger.error("OrderLixService.initOrderCode:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int selectByOrderCode(String orderCode, OrderLixReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderLix> cq = cb.createQuery(OrderLix.class);
			Root<OrderLix> root_ = cq.from(OrderLix.class);
			root_.fetch("customer", JoinType.INNER);
			root_.fetch("promotion_program", JoinType.LEFT);
			root_.fetch("pricing_program", JoinType.LEFT);
			root_.fetch("payment_method", JoinType.LEFT);
			root_.fetch("car", JoinType.LEFT);
			root_.fetch("warehouse", JoinType.LEFT);
			root_.fetch("ie_categories", JoinType.LEFT);
			root_.fetch("delivery_pricing", JoinType.LEFT);
			root_.fetch("carrier", JoinType.LEFT);
			cq.select(root_).where(cb.equal(root_.get("order_code"), orderCode));
			TypedQuery<OrderLix> query = em.createQuery(cq);
			t.setOrder_lix(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			logger.error("OrderLixService.selectByOrderCode:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public List<OrderLix> selectByOrderVoucher(String voucher_code, long idkh) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderLix> cq = cb.createQuery(OrderLix.class);
			Root<OrderLix> root_ = cq.from(OrderLix.class);
			root_.fetch("customer", JoinType.INNER);
			cq.select(root_).where(cb.equal(root_.get("voucher_code"), voucher_code),
					cb.equal(root_.get("customer").get("id"), idkh));
			TypedQuery<OrderLix> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("OrderLixService.selectByOrderVoucher:" + e.getMessage(), e);
		}
		return new ArrayList<OrderLix>();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectByOrderCodeId(String orderCode, OrderLixReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderLix> cq = cb.createQuery(OrderLix.class);
			Root<OrderLix> root_ = cq.from(OrderLix.class);
			cq.select(cb.construct(OrderLix.class, root_.get("id")))
					.where(cb.equal(root_.get("order_code"), orderCode));
			TypedQuery<OrderLix> query = em.createQuery(cq);
			t.setOrder_lix(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			logger.error("OrderLixService.selectByOrderCode:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int checkByCode(String code, long id) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<OrderLix> root = cq.from(OrderLix.class);
			ParameterExpression<String> pCode = cb.parameter(String.class);
			ParameterExpression<Long> pId = cb.parameter(Long.class);
			Predicate dis = cb.disjunction();
			Predicate con1 = cb.conjunction();
			Predicate con2 = cb.conjunction();
			con1.getExpressions().add(cb.equal(pId, 0));
			con1.getExpressions().add(cb.equal(root.get("order_code"), pCode));
			dis.getExpressions().add(con1);
			con2.getExpressions().add(cb.notEqual(pId, 0));
			con2.getExpressions().add(cb.notEqual(root.get("id"), pId));
			con2.getExpressions().add(cb.equal(root.get("order_code"), pCode));
			dis.getExpressions().add(con2);
			cq.select(cb.count(root.get("id"))).where(dis);
			TypedQuery<Long> query = em.createQuery(cq);
			query.setParameter(pId, id);
			query.setParameter(pCode, code);
			res = query.getSingleResult().intValue();
		} catch (Exception e) {
			logger.error("OrderLixService.checkByCode:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public String initVoucherCode(Date orderDate, long id) {
		String result = "";
		try {
			if (orderDate != null) {
				int year = ToolTimeCustomer.getYearM(orderDate);
				int month = ToolTimeCustomer.getMonthM(orderDate);
				result = year + "-" + String.format("%02d", month) + "/00001";
				String pattent = year + "-" + String.format("%02d", month) + "/";
				Query query = em.createNativeQuery("select max(replace(d.voucher_code,'" + pattent
						+ "','')) from orderlix as d where  d.voucher_code like '" + pattent + "%';");
				// select max(replace(d.voucher_code,'2024-01/','')) from
				// orderlix as d where d.voucher_code like '2024-01/%';
				List<Object> listDt = query.getResultList();
				if (listDt.size() > 0) {
					String voucher = Objects.toString(listDt.get(0), null);
					if (voucher != null) {
						result = year + "-" + String.format("%02d", month) + "/"
								+ String.format("%05d", (Integer.parseInt(voucher) + 1));
						return result;
					}
				}
				return result;
			}
		} catch (Exception e) {
			logger.error("OrderLixService.initVoucherCode:" + e.getMessage(), e);
		}
		return result;
	}

	@Override
	public double getRealExportBox(long orderId, long productId) {
		double boxQuantity = 0;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Double> cq = cb.createQuery(Double.class);
			Root<InvoiceDetail> root = cq.from(InvoiceDetail.class);
			Join<InvoiceDetail, Invoice> invoice_ = root.join("invoice", JoinType.INNER);
			Join<InvoiceDetail, Product> product_ = root.join("product", JoinType.INNER);
			Predicate con = cb.conjunction();
			con.getExpressions().add(cb.equal(invoice_.get("order_lix").get("id"), orderId));
			con.getExpressions().add(cb.equal(root.get("product").get("id"), productId));
			con.getExpressions().add(cb.isTrue(root.get("ex_order")));
			con.getExpressions().add(cb.isFalse(invoice_.get("ipromotion")));
			cq.multiselect(cb.quot(cb.sum(root.get("quantity")), product_.get("specification"))).where(con);
			TypedQuery<Double> query = em.createQuery(cq);
			// List<Double> list =query.getResultList();
			// if(list.size()>0){
			// return list.get(0);
			// }
			return Double.parseDouble(Objects.toString(query.getSingleResult(), "0"));
		} catch (Exception e) {
			// logger.error("OrderLixService.getRealExportBox:"+e.getMessage(),e);
		}
		return boxQuantity;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public OrderLix selectOnlyId(long nppOrderId) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderLix> cq = cb.createQuery(OrderLix.class);
			Root<OrderLix> root = cq.from(OrderLix.class);
			cq.select(
					cb.construct(OrderLix.class, root.get("id"), root.get("pricing_program").get("id"),
							root.get("promotion_program").get("id"), root.get("order_date"), root.get("delivery_date")))
					.where(cb.equal(root.get("npp_order_id"), nppOrderId));
			TypedQuery<OrderLix> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public OrderLix findById(long id) {
		try {
			return em.find(OrderLix.class, id);
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public OrderLix selectByNppIdOrder(long nppOrderId) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderLix> cq = cb.createQuery(OrderLix.class);
			Root<OrderLix> root = cq.from(OrderLix.class);
			root.fetch("car", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("npp_order_id"), nppOrderId));
			TypedQuery<OrderLix> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}
	

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int insertByLoad(OrderLixReqInfo t) {
		int res = -1;
		try {
			OrderLix p = t.getOrder_lix();
			if (p != null) {
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("OrderLixService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int updatePricingOrPromotionProgram(long id, String memberName, PricingProgram pricingProgram,
			PromotionProgram promotionProgram) {
		int res = -1;
		try {
			Query query = em
					.createQuery("update OrderLix set pricing_program=:pri,promotion_program=:pro,last_modifed_by=:m where id =:id");
			query.setParameter("pri", pricingProgram);
			query.setParameter("pro", promotionProgram);
			query.setParameter("m", memberName);
			query.setParameter("id", id);
			res = query.executeUpdate();
		} catch (Exception e) {
			logger.error("OrderLixService.updatePricingOrPromotionProgram:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public void updateTinhTrangDH(Long id) throws Exception {
		String sql = "call capnhattinhtrangdonhang(?);";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, id);
		query.executeUpdate();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int updateChangePro(OrderLix p) {
		int res = -1;
		try {
			if (p != null && p.getId() != 0) {
				p = em.merge(p);
			}
		} catch (Exception e) {
			logger.error("OrderLixService.updateAll:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int getListExportBatchOD(long orderDetailId, List<ExportBatchOD> list) {
		int res = 0;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ExportBatchOD> cq = cb.createQuery(ExportBatchOD.class);
			Root<ExportBatchOD> root_ = cq.from(ExportBatchOD.class);
			root_.fetch("batch", JoinType.INNER);
			cq.select(root_).where(cb.equal(root_.get("order_detail").get("id"), orderDetailId));
			TypedQuery<ExportBatchOD> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("OrderLixService.getListExportBatchOD:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int getListExportBatchPOD(long promotionOrderDetail, List<ExportBatchPOD> list) {
		int res = 0;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ExportBatchPOD> cq = cb.createQuery(ExportBatchPOD.class);
			Root<ExportBatchPOD> root_ = cq.from(ExportBatchPOD.class);
			root_.fetch("batch", JoinType.INNER);
			cq.select(root_).where(cb.equal(root_.get("promotion_order_detail").get("id"), promotionOrderDetail));
			TypedQuery<ExportBatchPOD> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("OrderLixService.getListExportBatchPOD:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int getListInventory(List<Long> listProductId, Date deliveryDate, List<Object[]> listInventory) {
		int res = -1;
		try {
			if (deliveryDate != null && listProductId != null && listProductId.size() > 0) {
				Date currentDate = new Date();
				int month = ToolTimeCustomer.getMonthM(currentDate);
				int year = ToolTimeCustomer.getYearM(currentDate);
				if (month == 1) {
					month = 12;
					year = year - 1;
				} else {
					month = month - 1;
				}
				Date fromDate = ToolTimeCustomer.getDateMinCustomer(ToolTimeCustomer.getMonthM(deliveryDate),
						ToolTimeCustomer.getYearM(deliveryDate));
				StringBuilder sql = new StringBuilder();
				sql.append("select t1.product_id,sum(unit_opening_balance+unit_import_quantity-unit_export_quantity) as inve,sum(unit_opening_balance+unit_import_quantity-unit_export_quantity-cal_quantity) as inv_cal from( ");
				sql.append("select iv.product_id,iv.closing_balance as unit_opening_balance, 0 as unit_import_quantity, 0 as unit_export_quantity,0 cal_quantity  from inventory as iv where iv.inventory_month=:m and iv.inventory_year=:y and iv.product_id in (:lst) ");
				sql.append("union all ");
				sql.append("select dtn.product_id,0,dtn.quantity, 0,0 from goodsreceiptnotedetail as dtn ");
				sql.append("inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id ");
				sql.append("where  dn.import_date >= :fd and dtn.product_id in (:lst) ");
				sql.append("union all ");
				sql.append("select dtx.product_id,0,0,dtx.quantity,0 from invoicedetail as dtx ");
				sql.append("inner join invoice as dx on dtx.invoice_id=dx.id ");
				sql.append("where dx.delivery_date >= :fd and dtx.product_id in (:lst)");
				sql.append("union all ");
				sql.append("select dtx_.product_id,0,0,0,dtx_.quantity from orderdetail as dtx_ ");
				sql.append("inner join orderlix as dx_ on dtx_.order_lix_id=dx_.id ");
				sql.append("where dx_.delivery_date>=:fd and dx_.delivery_date >=:d and dtx_.product_id in (:lst) and dx_.status=0 ");
				sql.append("union all ");
				sql.append("select pdt.product_id,0,0,0,pdt.quantity from promotionorderdetail as pdt ");
				sql.append("inner join orderdetail as dt on pdt.order_detail_id=dt.id ");
				sql.append("inner join orderlix as d on dt.order_lix_id=d.id ");
				sql.append("where d.delivery_date >=:fd and d.delivery_date >=:d and pdt.product_id in (:lst) and d.status=0 ");
				sql.append(") as t1 ");
				sql.append("group by t1.product_id ");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("m", month);
				query.setParameter("y", year);
				query.setParameter("lst", listProductId);
				query.setParameter("fd", ToolTimeCustomer.convertDateToString(fromDate, "yyyy-MM-dd"));
				query.setParameter("d", ToolTimeCustomer.convertDateToString(deliveryDate, "yyyy-MM-dd"));
				listInventory.addAll(query.getResultList());
				res = 0;
			}
		} catch (Exception e) {
			logger.error("OrderLixService.getListInventory:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int getListRealExportQuantity(long orderId, List<Object[]> listRealExport) {
		int res = -1;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select dt.product_id,d.ipromotion,dt.quantity,dt.quantity/p.specification from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p on dt.product_id=p.id ");
			sql.append("where d.order_lix_id=:id and d.exported=1 ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("id", orderId);
			listRealExport.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("OrderLixService.getListRealExportQuantity:" + e.getMessage(), e);
		}
		return res;
	}

	// lấy số lượng đã xuất và số lượng còn lại của đơn hàng
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int getListExportExtensionOrder(long orderId, List<Object[]> list) {
		int res = 0;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.product_id,sum(t1.export_quantity/p1.specification) as real_quantity,sum(t1.order_quantity-t1.export_quantity/p1.specification) as remain_quantity ,sum(t1.export_quantity) as real_quantity_dvt from ( ");
			sql.append("select dtx.product_id, dtx.quantity as export_quantity,0 as order_quantity from invoicedetail as dtx ");
			sql.append("inner join invoice as dx on dtx.invoice_id=dx.id ");
			sql.append("where dx.order_lix_id=:id and dx.ipromotion=false ");
			sql.append("union all ");
			sql.append("select dt.product_id,0,dt.box_quantity from orderdetail as dt ");
			sql.append("where dt.order_lix_id=:id ) as t1 ");
			sql.append("inner join product as p1 on t1.product_id=p1.id ");
			sql.append("group by t1.product_id ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("id", orderId);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("OrderLixService.getListExportExtensionOrder:" + e.getMessage(), e);
		}
		return 0;
	}

	// lấy số lượng đã xuất và số lượng còn lại của khuyen mai đơn hàng
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int getListExportExtensionOrderKM(long orderId, List<Object[]> list) {
		int res = 0;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select dtx.product_id, dtx.quantity, dtx.productdh_code from invoicedetail as dtx ");
			sql.append("inner join invoice as dx on dtx.invoice_id=dx.id  ");
			sql.append("where dx.order_lix_id=:id and dx.ipromotion=true  ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("id", orderId);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("OrderLixService.getListExportExtensionOrder:" + e.getMessage(), e);
		}
		return 0;
	}

	@Override
	public int updateNoPromotionOrderDetail(long id, boolean no) {
		int res = -1;
		try {
			Query query = em.createQuery("update PromotionOrderDetail set no=:no where id =:id");
			query.setParameter("no", no);
			query.setParameter("id", id);
			res = query.executeUpdate();
		} catch (Exception e) {
			logger.error("OrderLixService.updateNoOrderDetail:" + e.getMessage(), e);
		}
		return res;
	}
}
