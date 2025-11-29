package lixco.com.service;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.commom_ejb.ToolTimeCustomer;
import lixco.com.entity.Customer;
import lixco.com.entity.CustomerPromotionProgram;
import lixco.com.entity.PromotionProgram;
import lixco.com.interfaces.ICustomerPromotionProgramService;
import lixco.com.reqInfo.CustomerPromotionProgramReqInfo;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class CustomerPromotionProgramService implements ICustomerPromotionProgramService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@Override
	public List<CustomerPromotionProgram> findAllByPromotionProgram(long idprograms) {
		try {
			if (idprograms != 0) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<CustomerPromotionProgram> cq = cb.createQuery(CustomerPromotionProgram.class);
				Root<CustomerPromotionProgram> root = cq.from(CustomerPromotionProgram.class);
				root.fetch("customer", JoinType.LEFT);
				cq.select(root).where(cb.equal(root.get("promotion_program").get("id"), idprograms));
				TypedQuery<CustomerPromotionProgram> query = em.createQuery(cq);
				return query.getResultList();
			}
		} catch (Exception e) {
			logger.error("CustomerPromotionProgramService.findAllByPromotionProgram:" + e.getMessage(), e);
		}
		return new ArrayList<CustomerPromotionProgram>();
	}

	@Override
	public int caphnhatlaingay(List<Long> ids, Date sDate, Date eDate) {
		int res = -1;
		try {
			if (ids != null && ids.size() != 0) {
				Query query = em
						.createQuery("update CustomerPromotionProgram set effective_date=:sDate, expiry_date=:eDate where id in :ids");
				query.setParameter("ids", ids);
				query.setParameter("sDate", sDate);
				query.setParameter("eDate", eDate);
				res = query.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CustomerPromotionProgramService.caphnhatlaingay:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int search(String json, List<CustomerPromotionProgram> list) {
		int res = -1;
		try {/*
			 * {
			 * customer_promotion_program_info:{customer_types_id:0,customer_id
			 * :0,program_code:'',from_date:'',to_date:'',disable:-1},
			 * page:{page_index:0, page_size:0}}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j.get("customer_promotion_program_info"),
					"customer_id", null);
			HolderParser hCustomerTypeId = JsonParserUtil.getValueNumber(j.get("customer_promotion_program_info"),
					"customer_types_id", null);
			HolderParser hProgramCode = JsonParserUtil.getValueString(j.get("customer_promotion_program_info"),
					"program_code", null);
			HolderParser hFromDate = JsonParserUtil.getValueString(j.get("customer_promotion_program_info"),
					"from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j.get("customer_promotion_program_info"), "to_date",
					null);
			HolderParser hDisable = JsonParserUtil.getValueNumber(j.get("customer_promotion_program_info"), "disable",
					null);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CustomerPromotionProgram> cq = cb.createQuery(CustomerPromotionProgram.class);
			Root<CustomerPromotionProgram> root_ = cq.from(CustomerPromotionProgram.class);
			Join<CustomerPromotionProgram, PromotionProgram> promotionProgram_ = (Join) root_.fetch(
					"promotion_program", JoinType.INNER);
			Join<CustomerPromotionProgram, Customer> customer_ = (Join) root_.fetch("customer", JoinType.INNER);
			List<Predicate> predicates = new ArrayList<Predicate>();
			ParameterExpression<Long> pCustomerTypesId = cb.parameter(Long.class);
			ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
			ParameterExpression<String> pProgramCode = cb.parameter(String.class);
			ParameterExpression<Date> pFromDate = cb.parameter(Date.class);
			ParameterExpression<Date> pToDate = cb.parameter(Date.class);
			ParameterExpression<Integer> pDisable = cb.parameter(Integer.class);
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.equal(pCustomerTypesId, 0));
			dis1.getExpressions().add(cb.equal(customer_.get("customer_types").get("id"), pCustomerTypesId));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.equal(pCustomerId, 0));
			dis2.getExpressions().add(cb.equal(customer_.get("id"), pCustomerId));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.isNull(pProgramCode));
			dis3.getExpressions().add(cb.equal(pProgramCode, ""));
			dis3.getExpressions().add(cb.equal(promotionProgram_.get("program_code"), pProgramCode));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.isNull(pFromDate));
			dis4.getExpressions().add(cb.equal(pFromDate, ""));
			dis4.getExpressions().add(cb.greaterThanOrEqualTo(root_.get("effective_date"), pFromDate));
			predicates.add(dis4);
			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.isNull(pToDate));
			dis5.getExpressions().add(cb.equal(pToDate, ""));
			dis5.getExpressions().add(cb.isNull(root_.get("expiry_date")));
			dis5.getExpressions().add(cb.lessThanOrEqualTo(root_.get("expiry_date"), pToDate));
			predicates.add(dis5);
			Predicate dis6 = cb.disjunction();
			dis6.getExpressions().add(cb.equal(pDisable, -1));
			dis6.getExpressions().add(cb.equal(root_.get("disable"), pDisable));
			predicates.add(dis6);
			cq.select(root_).where(cb.and(predicates.toArray(new Predicate[0])))
					.orderBy(cb.desc(root_.get("created_date")));
			TypedQuery<CustomerPromotionProgram> query = em.createQuery(cq);
			query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue(), null)));
			query.setParameter(pCustomerTypesId, Long.parseLong(Objects.toString(hCustomerTypeId.getValue(), null)));
			query.setParameter(pProgramCode, Objects.toString(hProgramCode.getValue(), null));
			query.setParameter(pFromDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pToDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pDisable, Integer.parseInt(Objects.toString(hDisable.getValue(), null)));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("CustomerPromotionProgramService.search:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int insert(CustomerPromotionProgramReqInfo t) {
		int res = -1;
		try {
			CustomerPromotionProgram p = t.getCustomer_promotion_program();
			if (p != null) {
				// kiểm tra chương trình đơn giá có áp dụng cho khách hàng này
				// chưa
				// JPQL
				Query query = em
						.createQuery("select count(p) from CustomerPromotionProgram as p where p.promotion_program=:pc and p.customer=:c ");
				query.setParameter("pc", p.getPromotion_program());
				query.setParameter("c", p.getCustomer());
				int chk = Integer.parseInt(Objects.toString(query.getSingleResult()));
				if (chk == 0) {
					em.persist(p);
					if (p.getId() > 0) {
						res = 0;
					}
				} else {
					res = -2;// duplicatie
				}

			}
		} catch (Exception e) {
			logger.error("CustomerPromotionProgramService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int update(CustomerPromotionProgramReqInfo t) {
		int res = -1;
		try {
			CustomerPromotionProgram p = t.getCustomer_promotion_program();
			if (p != null) {
				// kiểm tra chương trình đơn giá có áp dụng cho khách hàng này
				// chưa
				// JPQL
				Query query = em
						.createQuery("select count(p) from CustomerPromotionProgram as p where p.promotion_program=:pc and p.customer=:c and p.id <> :idc ");
				query.setParameter("pc", p.getPromotion_program());
				query.setParameter("c", p.getCustomer());
				query.setParameter("idc", p.getId());
				int chk = Integer.parseInt(Objects.toString(query.getSingleResult()));
				if (chk == 0) {
					p = em.merge(p);
					if (p != null) {
						res = 0;
					}
				} else {
					res = -2;// duplicate
				}
			}
		} catch (Exception e) {
			logger.error("CustomerPromotionProgramService.update:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectById(long id, CustomerPromotionProgramReqInfo t) {
		int res = -1;
		try {
			CustomerPromotionProgram p = em.find(CustomerPromotionProgram.class, id);
			if (p != null) {
				t.setCustomer_promotion_program(p);
				res = 0;
			}
		} catch (Exception e) {
			logger.error("CustomerPromotionProgramService.selectById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int deleteById(long id) {
		int res = -1;
		try {
			// JQPL
			Query query = em.createQuery("delete from CustomerPromotionProgram where id = :id");
			query.setParameter("id", id);
			res = query.executeUpdate();
		} catch (Exception e) {
			logger.error("CustomerPromotionProgramService.deleteById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectAll(long programId, List<CustomerPromotionProgram> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CustomerPromotionProgram> cq = cb.createQuery(CustomerPromotionProgram.class);
			Root<CustomerPromotionProgram> root = cq.from(CustomerPromotionProgram.class);
			cq.select(root).where(cb.equal(root.get("promotion_program").get("id"), programId));
			root.fetch("customer", JoinType.INNER);
			root.fetch("promotion_program", JoinType.INNER);
			TypedQuery<CustomerPromotionProgram> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("CustomerPromotionProgramService.selectAll:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectBy(long programId, long customerId, CustomerPromotionProgramReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CustomerPromotionProgram> cq = cb.createQuery(CustomerPromotionProgram.class);
			Root<CustomerPromotionProgram> root = cq.from(CustomerPromotionProgram.class);
			root.fetch("promotion_program", JoinType.INNER);
			root.fetch("customer", JoinType.INNER);
			cq.select(root).where(
					cb.and(cb.equal(root.get("promotion_program").get("id"), programId),
							cb.equal(root.get("customer").get("id"), customerId)));
			res = 0;
		} catch (Exception e) {
			logger.error("CustomerPromotionProgramService.selectBy:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectForCustomer(String json, CustomerPromotionProgramReqInfo t) {
		int res = -1;
		try { /* {delivery_date:'',customer_id:0} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hDeliveryDate = JsonParserUtil.getValueString(j, "date", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			if (hDeliveryDate.getErr() == 0 && hCustomerId.getErr() == 0) {

				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<CustomerPromotionProgram> cq = cb.createQuery(CustomerPromotionProgram.class);
				Root<CustomerPromotionProgram> root = cq.from(CustomerPromotionProgram.class);
				Join<CustomerPromotionProgram, PromotionProgram> promotionPr_ = root.join("promotion_program",
						JoinType.INNER);
				// root.fetch("customer",JoinType.INNER);
				List<Predicate> predicates = new ArrayList<>();
				ParameterExpression<Date> pDeliveryDate = cb.parameter(Date.class);
				ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
				predicates.add(cb.equal(root.get("customer").get("id"), pCustomerId));
				predicates.add(cb.lessThanOrEqualTo(root.get("effective_date"), pDeliveryDate));
				Predicate dis = cb.disjunction();
				dis.getExpressions().add(cb.isNull(root.get("expiry_date")));
				dis.getExpressions().add(cb.greaterThanOrEqualTo(root.get("expiry_date"), pDeliveryDate));
				predicates.add(dis);
				predicates.add(cb.isFalse(root.get("disable")));

				predicates
						.add(cb.lessThanOrEqualTo(root.get("promotion_program").get("effective_date"), pDeliveryDate));
				Predicate disparent = cb.disjunction();
				disparent.getExpressions().add(cb.isNull(root.get("promotion_program").get("expiry_date")));
				disparent.getExpressions().add(
						cb.greaterThanOrEqualTo(root.get("promotion_program").get("expiry_date"), pDeliveryDate));
				predicates.add(disparent);
				predicates.add(cb.isFalse(root.get("promotion_program").get("disable")));

				cq.select(
						cb.construct(CustomerPromotionProgram.class, root.get("id"), root.get("customer").get("id"),
								root.get("promotion_program").get("id"), promotionPr_.get("program_code"),
								promotionPr_.get("effective_date"), promotionPr_.get("expiry_date"),
								root.get("effective_date"), root.get("expiry_date")))
						.where(cb.and(predicates.toArray(new Predicate[0])))
						.orderBy(cb.desc(root.get("effective_date")));
				TypedQuery<CustomerPromotionProgram> query = em.createQuery(cq);
				query.setParameter(pDeliveryDate, ToolTimeCustomer.convertStringToDate(
						Objects.toString(hDeliveryDate.getValue(), null), "dd/MM/yyyy"));
				query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue(), "0")));
				List<CustomerPromotionProgram> list = query.getResultList();
				if (list.size() > 0) {
					t.setCustomer_promotion_program(list.get(0));
				}
				res = 0;
			} else {
				res = -1;
			}
		} catch (Exception e) {
			logger.error("CustomerPromotionProgramService.selectForCustomer:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectForCustomerDate(String json, List<PromotionProgram> promotionPrograms) {
		int res = -1;
		try { /* {delivery_date:'',customer_id:0} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hDeliveryDate = JsonParserUtil.getValueString(j, "date", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			if (hDeliveryDate.getErr() == 0 && hCustomerId.getErr() == 0) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<CustomerPromotionProgram> cq = cb.createQuery(CustomerPromotionProgram.class);
				Root<CustomerPromotionProgram> root = cq.from(CustomerPromotionProgram.class);
				Join<CustomerPromotionProgram, PromotionProgram> promotionPr_ = root.join("promotion_program",
						JoinType.INNER);
				// root.fetch("customer",JoinType.INNER);
				List<Predicate> predicates = new ArrayList<>();
				ParameterExpression<Date> pDeliveryDate = cb.parameter(Date.class);
				ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
				predicates.add(cb.equal(root.get("customer").get("id"), pCustomerId));
				predicates.add(cb.lessThanOrEqualTo(root.get("effective_date"), pDeliveryDate));
				Predicate dis = cb.disjunction();
				dis.getExpressions().add(cb.isNull(root.get("expiry_date")));
				dis.getExpressions().add(cb.greaterThanOrEqualTo(root.get("expiry_date"), pDeliveryDate));

				predicates.add(dis);
				Subquery<Date> subquery = cq.subquery(Date.class);
				Root<CustomerPromotionProgram> rootSub = subquery.from(CustomerPromotionProgram.class);
				List<Predicate> predicates2 = new ArrayList<>();
				predicates2.add(cb.equal(rootSub.get("customer").get("id"), pCustomerId));
				predicates2.add(cb.lessThanOrEqualTo(rootSub.get("effective_date"), pDeliveryDate));
				Predicate dis1 = cb.disjunction();
				dis1.getExpressions().add(cb.isNull(rootSub.get("expiry_date")));
				dis1.getExpressions().add(cb.greaterThanOrEqualTo(rootSub.get("expiry_date"), pDeliveryDate));
				predicates2.add(dis1);
				subquery.select(cb.greatest(rootSub.get("effective_date"))).where(
						cb.and(predicates2.toArray(new Predicate[0])));
				predicates.add(cb.equal(root.get("effective_date"), subquery));
				predicates.add(cb.isFalse(root.get("disable")));
				// (long id, long customerId, long promotionProgramId, Date
				// effective_date,Date expiry_date)
				cq.select(
						cb.construct(CustomerPromotionProgram.class, root.get("id"), root.get("customer").get("id"),
								root.get("promotion_program").get("id"), promotionPr_.get("program_code"),
								promotionPr_.get("effective_date"), promotionPr_.get("expiry_date"),
								root.get("effective_date"), root.get("expiry_date")))
						.where(cb.and(predicates.toArray(new Predicate[0])))
						.orderBy(cb.desc(root.get("effective_date")));
				TypedQuery<CustomerPromotionProgram> query = em.createQuery(cq);
				query.setParameter(pDeliveryDate, ToolTimeCustomer.convertStringToDate(
						Objects.toString(hDeliveryDate.getValue(), null), "dd/MM/yyyy"));
				query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue(), "0")));
				List<CustomerPromotionProgram> list = query.getResultList();
				for (int i = 0; i < list.size(); i++) {
					promotionPrograms.add(list.get(i).getPromotion_program());
				}
				res = 0;
			} else {
				res = -1;
			}
		} catch (Exception e) {
			logger.error("CustomerPromotionProgramService.selectForCustomer:" + e.getMessage(), e);
		}
		return res;
	}

}
