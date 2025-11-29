package lixco.com.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
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
import javax.persistence.criteria.Subquery;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.commom_ejb.PagingInfo;
import lixco.com.commom_ejb.ToolTimeCustomer;
import lixco.com.entity.Customer;
import lixco.com.entity.CustomerPricingProgram;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.PricingProgramDetail;
import lixco.com.entity.PromotionProgram;
import lixco.com.interfaces.ICustomerPricingProgramService;
import lixco.com.reqInfo.CustomerPricingProgramReqInfo;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class CustomerPricingProgramService implements ICustomerPricingProgramService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;
	
	@Override
	public int caphnhatlaingay(List<Long> ids,Date sDate,Date eDate) {
		int res = -1;
		try {
			if (ids != null && ids.size() != 0) {
				Query query = em.createQuery("update CustomerPricingProgram set effective_date=:sDate, expiry_date=:eDate where id in :ids");
				query.setParameter("ids", ids);
				query.setParameter("sDate", sDate);
				query.setParameter("eDate", eDate);
				res=  query.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CustomerPricingProgramService.caphnhatlaingay:" + e.getMessage(), e);
		}
		return res;
	}
	
	@Override
	public int search(String json, PagingInfo page, List<CustomerPricingProgram> list) {
		int res = -1;
		long totalRow = 0;
		long totalPage = 0;
		try {/*
			 * {
			 * customer_pricing_program_info:{customer_types_id:0,customer_id:0
			 * ,program_code:'',from_date:'',to_date:'',disable:-1},
			 * page:{page_index:0, page_size:0}}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j.get("customer_pricing_program_info"),
					"customer_id", null);
			HolderParser hCustomerTypeId = JsonParserUtil.getValueNumber(j.get("customer_pricing_program_info"),
					"customer_types_id", null);
			HolderParser hProgramCode = JsonParserUtil.getValueString(j.get("customer_pricing_program_info"),
					"program_code", null);
			HolderParser hFromDate = JsonParserUtil.getValueString(j.get("customer_pricing_program_info"), "from_date",
					null);
			HolderParser hToDate = JsonParserUtil.getValueString(j.get("customer_pricing_program_info"), "to_date",
					null);
			HolderParser hDisable = JsonParserUtil.getValueNumber(j.get("customer_pricing_program_info"), "disable",
					null);
			HolderParser hPageIndex = JsonParserUtil.getValueNumber(j.get("page"), "page_index", null);
			HolderParser hPageSize = JsonParserUtil.getValueNumber(j.get("page"), "page_size", null);
			int pageIndex = Integer.parseInt(Objects.toString(hPageIndex.getValue(), "0"));
			int pageSize = Integer.parseInt(Objects.toString(hPageSize.getValue(), "0"));
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CustomerPricingProgram> cq = cb.createQuery(CustomerPricingProgram.class);
			Root<CustomerPricingProgram> root_ = cq.from(CustomerPricingProgram.class);
			Join<CustomerPricingProgram, PricingProgram> pricingProgram_ = (Join) root_.fetch("pricing_program",
					JoinType.INNER);
			Join<CustomerPricingProgram, Customer> customer_ = (Join) root_.fetch("customer", JoinType.INNER);
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
			dis3.getExpressions().add(cb.equal(pricingProgram_.get("program_code"), pProgramCode));
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
					.orderBy(cb.desc(root_.get("effective_date")));
			TypedQuery<CustomerPricingProgram> query = em.createQuery(cq);
			query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue(), null)));
			query.setParameter(pCustomerTypesId, Long.parseLong(Objects.toString(hCustomerTypeId.getValue(), null)));
			query.setParameter(pProgramCode, Objects.toString(hProgramCode.getValue(), null));
			query.setParameter(pFromDate, ToolTimeCustomer.convertStringToDate(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy HH:mm:ss"));
			query.setParameter(pToDate, ToolTimeCustomer.convertStringToDate(
					Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy HH:mm:ss"));
			query.setParameter(pDisable, Integer.parseInt(Objects.toString(hDisable.getValue(), null)));
			query.setFirstResult((pageIndex - 1) * pageSize);
			query.setMaxResults(pageSize);
			list.addAll(query.getResultList());
			// paging
			CriteriaQuery<Long> cq1 = cb.createQuery(Long.class);
			root_ = cq1.from(CustomerPricingProgram.class);
			pricingProgram_ = root_.join("pricing_program", JoinType.INNER);
			customer_ = root_.join("customer", JoinType.INNER);
			cq1.select(cb.count(root_.get("id"))).where(cb.and(predicates.toArray(new Predicate[0])));
			TypedQuery<Long> query2 = em.createQuery(cq1);
			query2.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue(), null)));
			query2.setParameter(pCustomerTypesId, Long.parseLong(Objects.toString(hCustomerTypeId.getValue(), null)));
			query2.setParameter(pProgramCode, Objects.toString(hProgramCode.getValue(), null));
			query2.setParameter(pFromDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy"));
			query2.setParameter(pToDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy"));
			query2.setParameter(pDisable, Integer.parseInt(Objects.toString(hDisable.getValue(), null)));
			totalRow = query2.getSingleResult();
			if (pageSize != 0) {
				totalPage = (long) Math.ceil((double) totalRow / pageSize);
			}
			page.setTotalRow(totalRow);
			page.setTotalPage(totalPage);
			res = 0;
		} catch (Exception e) {
			logger.error("CustomerPricingProgramService.search:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public List<CustomerPricingProgram> findAllByPricingProgram(long idprograms) {
		try {
			if (idprograms != 0) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<CustomerPricingProgram> cq = cb.createQuery(CustomerPricingProgram.class);
				Root<CustomerPricingProgram> root = cq.from(CustomerPricingProgram.class);
				root.fetch("customer", JoinType.LEFT);
				cq.select(root).where(cb.equal(root.get("pricing_program").get("id"),idprograms));
				TypedQuery<CustomerPricingProgram> query = em.createQuery(cq);
				return query.getResultList();
			}
		} catch (Exception e) {
			logger.error("CustomerPricingProgramService.findAllByPricingProgram:" + e.getMessage(), e);
		}
		return new ArrayList<CustomerPricingProgram>();
	}
	@Override
	public int insert(CustomerPricingProgramReqInfo t) {
		int res = -1;
		try {
			CustomerPricingProgram p = t.getCustomer_pricing_program();
			if (p != null) {
				// kiểm tra chương trình đơn giá có áp dụng cho khách hàng này
				// chưa
				// JPQL
				Query query = em
						.createQuery("select count(p) from CustomerPricingProgram as p where p.pricing_program=:pc and p.customer=:c ");
				query.setParameter("pc", p.getPricing_program());
				query.setParameter("c", p.getCustomer());
				int chk = Integer.parseInt(Objects.toString(query.getSingleResult()));
				if (chk == 0) {
					em.persist(p);
					if (p.getId() > 0) {
						// reload data.
						CriteriaBuilder cb = em.getCriteriaBuilder();
						CriteriaQuery<CustomerPricingProgram> cq = cb.createQuery(CustomerPricingProgram.class);
						Root<CustomerPricingProgram> root = cq.from(CustomerPricingProgram.class);
						Join<CustomerPricingProgram, Customer> customer_ = root.join("customer", JoinType.INNER);
						// (long id, long customerId,String customerCode,String
						// customerName,long pricingProgramId,Date
						// effective_date,Date expiry_date, boolean disable,
						// String note)
						cq.select(
								cb.construct(CustomerPricingProgram.class, root.get("id"), customer_.get("id"),
										customer_.get("customer_code"), customer_.get("customer_name"),
										root.get("pricing_program").get("id"), root.get("effective_date"),
										root.get("expiry_date"), root.get("disable"), root.get("note"))).where(
								cb.equal(root.get("id"), p.getId()));
						TypedQuery<CustomerPricingProgram> query2 = em.createQuery(cq);
						t.setCustomer_pricing_program(query2.getSingleResult());
						res = 0;
					}
				} else {
					res = -2;// duplicatie
				}

			}
		} catch (Exception e) {
			logger.error("CustomerPricingProgramService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int update(CustomerPricingProgramReqInfo t) {
		int res = -1;
		try {
			CustomerPricingProgram p = t.getCustomer_pricing_program();
			if (p != null) {
				// kiểm tra chương trình đơn giá có áp dụng cho khách hàng này
				// chưa
				// JPQL
				Query query = em
						.createQuery("select count(p) from CustomerPricingProgram as p where p.pricing_program=:pc and p.customer=:c and p.id <> :idc ");
				query.setParameter("pc", p.getPricing_program());
				query.setParameter("c", p.getCustomer());
				query.setParameter("idc", p.getId());
				int chk = Integer.parseInt(Objects.toString(query.getSingleResult()));
				if (chk == 0) {
					p = em.merge(p);
					if (p != null) {
						// reload data.
						CriteriaBuilder cb = em.getCriteriaBuilder();
						CriteriaQuery<CustomerPricingProgram> cq = cb.createQuery(CustomerPricingProgram.class);
						Root<CustomerPricingProgram> root = cq.from(CustomerPricingProgram.class);
						Join<CustomerPricingProgram, Customer> customer_ = root.join("customer", JoinType.INNER);
						// (long id, long customerId,String customerCode,String
						// customerName,long pricingProgramId,Date
						// effective_date,Date expiry_date, boolean disable,
						// String note)
						cq.select(
								cb.construct(CustomerPricingProgram.class, root.get("id"), customer_.get("id"),
										customer_.get("customer_code"), customer_.get("customer_name"),
										root.get("pricing_program").get("id"), root.get("effective_date"),
										root.get("expiry_date"), root.get("disable"), root.get("note"))).where(
								cb.equal(root.get("id"), p.getId()));
						TypedQuery<CustomerPricingProgram> query2 = em.createQuery(cq);
						t.setCustomer_pricing_program(query2.getSingleResult());
						res = 0;
					}
				} else {
					res = -2;// duplicate
				}
			}
		} catch (Exception e) {
			logger.error("CustomerPricingProgramService.update:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectById(long id, CustomerPricingProgramReqInfo t) {
		int res = -1;
		try {
			CustomerPricingProgram p = em.find(CustomerPricingProgram.class, id);
			if (p != null) {
				t.setCustomer_pricing_program(p);
				res = 0;
			}
		} catch (Exception e) {
			logger.error("CustomerPricingProgramService.selectById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int deleteById(long id) {
		int res = -1;
		try {
			// JQPL
			Query query = em.createQuery("delete from CustomerPricingProgram where id = :id");
			query.setParameter("id", id);
			res = query.executeUpdate();
		} catch (Exception e) {
			logger.error("CustomerPricingProgramService.deleteById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectAll(long programId, List<CustomerPricingProgram> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CustomerPricingProgram> cq = cb.createQuery(CustomerPricingProgram.class);
			Root<CustomerPricingProgram> root = cq.from(CustomerPricingProgram.class);
			cq.select(root).where(cb.equal(root.get("pricing_program").get("id"), programId));
			root.fetch("customer", JoinType.INNER);
			TypedQuery<CustomerPricingProgram> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("CustomerPricingProgramService.selectAll:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectBy(long programId, long customerId, CustomerPricingProgramReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CustomerPricingProgram> cq = cb.createQuery(CustomerPricingProgram.class);
			Root<CustomerPricingProgram> root = cq.from(CustomerPricingProgram.class);
			root.fetch("pricing_program", JoinType.INNER);
			root.fetch("customer", JoinType.INNER);
			cq.select(root).where(
					cb.and(cb.equal(root.get("pricing_program").get("id"), programId),
							cb.equal(root.get("customer").get("id"), customerId)));
			res = 0;
		} catch (Exception e) {
			logger.error("CustomerPricingProgramService.selectBy:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public long selectForCustomerSub(long id, Date date, long idproduct) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<PricingProgram> root = cq.from(PricingProgram.class);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.lessThanOrEqualTo(root.get("effective_date"), date));

			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.isNull(root.get("expiry_date")));
			dis.getExpressions().add(cb.greaterThanOrEqualTo(root.get("expiry_date"), date));
			predicates.add(dis);
			predicates.add(cb.equal(root.get("parent_pricing_program").get("id"), id));
			predicates.add(cb.isFalse(root.get("disable")));
			List<Predicate> subpredicates = new LinkedList<Predicate>();
			Subquery<PricingProgramDetail> sqOne = cq.subquery(PricingProgramDetail.class);
			Root subroot = sqOne.from(PricingProgramDetail.class);
			Predicate predicatePdCode = cb.equal(subroot.get("product").get("id"), idproduct);
			subpredicates.add(predicatePdCode);
			sqOne.select(subroot.get("pricing_program")).where(cb.or(subpredicates.toArray(new Predicate[0])));
			Predicate predicateSub = cb.equal(root, cb.any(sqOne));
			predicates.add(predicateSub);// truy cap chi tiet
			cq.select(root.get("id")).where(cb.and(predicates.toArray(new Predicate[0])));
			TypedQuery<Long> query = em.createQuery(cq);

			List<Long> list = query.getResultList();
			if (list.size() > 0) {
				return list.get(0);
			}

		} catch (Exception e) {
			logger.error("CustomerPricingProgramService.selectForCustomer:" + e.getMessage(), e);
		}
		return 0l;
	}

	@Override
	public int selectForCustomer(String json, CustomerPricingProgramReqInfo t) {
		int res = -1;
		try {
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hDeliveryDate = JsonParserUtil.getValueString(j, "date", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			if (hDeliveryDate.getErr() == 0 && hCustomerId.getErr() == 0) {

				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<CustomerPricingProgram> cq = cb.createQuery(CustomerPricingProgram.class);
				Root<CustomerPricingProgram> root = cq.from(CustomerPricingProgram.class);
				Join<CustomerPricingProgram, PricingProgram> pricingPr_ = root.join("pricing_program", JoinType.INNER);
				List<Predicate> predicates = new ArrayList<>();
				ParameterExpression<Date> pDeliveryDate = cb.parameter(Date.class);
				ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
				predicates.add(cb.equal(root.get("customer").get("id"), pCustomerId));
				
				predicates.add(cb.lessThanOrEqualTo(root.get("effective_date"), pDeliveryDate));
				Predicate dis = cb.disjunction();
				dis.getExpressions().add(cb.isNull(root.get("expiry_date")));
				dis.getExpressions().add(cb.greaterThanOrEqualTo(root.get("expiry_date"), pDeliveryDate));
				predicates.add(dis);


				predicates.add(cb.isNull(root.get("pricing_program").get("parent_pricing_program")));
				Predicate dis2 = cb.conjunction(); //conjunction: và
				dis2.getExpressions().add(cb.isFalse(root.get("disable")));
				dis2.getExpressions().add(cb.isFalse(root.get("pricing_program").get("disable")));
				predicates.add(dis2);
				
				predicates.add(cb.lessThanOrEqualTo(root.get("pricing_program").get("effective_date"), pDeliveryDate));
				Predicate dis3 = cb.disjunction(); //disjunction: hoặc
				dis3.getExpressions().add(cb.isNull(root.get("pricing_program").get("expiry_date")));
				dis3.getExpressions().add(cb.greaterThanOrEqualTo(root.get("pricing_program").get("expiry_date"), pDeliveryDate));
				predicates.add(dis3);

				// (long id, long customerId, long pricingprogramId,long
				// parentPricingProgramId, Date effective_date,Date expiry_date)
				cq.select(
						cb.construct(CustomerPricingProgram.class, root.get("id"), root.get("customer").get("id"), root
								.get("pricing_program").get("id"), pricingPr_.get("program_code"), pricingPr_
								.get("effective_date"), pricingPr_.get("expiry_date"),
								pricingPr_.get("parent_pricing_program").get("id"), root.get("effective_date"), root
										.get("expiry_date"))).where(cb.and(predicates.toArray(new Predicate[0])))
						.orderBy(cb.desc(root.get("effective_date")));
				TypedQuery<CustomerPricingProgram> query = em.createQuery(cq);

				query.setParameter(pDeliveryDate, ToolTimeCustomer.convertStringToDate(
						Objects.toString(hDeliveryDate.getValue(), null), "dd/MM/yyyy"));
				query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue(), "0")));
				List<CustomerPricingProgram> list = query.getResultList();
				if (list.size() > 0) {
					t.setCustomer_pricing_program(list.get(0));
				}
				res = 0;
			} else {
				res = -1;
			}
		} catch (Exception e) {
			logger.error("CustomerPricingProgramService.selectForCustomer:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectForCustomerDate(String json, List<PricingProgram> pricingPrograms) {
		int res = -1;
		try {
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hDeliveryDate = JsonParserUtil.getValueString(j, "date", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			if (hDeliveryDate.getErr() == 0 && hCustomerId.getErr() == 0) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<CustomerPricingProgram> cq = cb.createQuery(CustomerPricingProgram.class);
				Root<CustomerPricingProgram> root = cq.from(CustomerPricingProgram.class);
				Join<CustomerPricingProgram, PricingProgram> pricingPr_ = root.join("pricing_program", JoinType.INNER);
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
				Root<CustomerPricingProgram> rootSub = subquery.from(CustomerPricingProgram.class);
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
				// (long id, long customerId, long pricingprogramId,long
				// parentPricingProgramId, Date effective_date,Date expiry_date)
				cq.select(
						cb.construct(CustomerPricingProgram.class, root.get("id"), root.get("customer").get("id"), root
								.get("pricing_program").get("id"), pricingPr_.get("program_code"), pricingPr_
								.get("effective_date"), pricingPr_.get("expiry_date"),
								pricingPr_.get("parent_pricing_program").get("id"), root.get("effective_date"), root
										.get("expiry_date"))).where(cb.and(predicates.toArray(new Predicate[0])))
						.orderBy(cb.desc(root.get("created_date")));
				TypedQuery<CustomerPricingProgram> query = em.createQuery(cq);
				query.setParameter(pDeliveryDate, ToolTimeCustomer.convertStringToDate(
						Objects.toString(hDeliveryDate.getValue(), null), "dd/MM/yyyy"));
				query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue(), "0")));
				List<CustomerPricingProgram> list = query.getResultList();
				for (int i = 0; i < list.size(); i++) {
					pricingPrograms.add(list.get(i).getPricing_program());
				}
				res = 0;
			} else {
				res = -1;
			}
		} catch (Exception e) {
			logger.error("CustomerPricingProgramService.selectForCustomer:" + e.getMessage(), e);
		}
		return res;
	}

}
