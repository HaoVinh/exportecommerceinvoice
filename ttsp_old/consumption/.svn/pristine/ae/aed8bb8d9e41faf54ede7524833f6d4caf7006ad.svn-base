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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.commom_ejb.ToolTimeCustomer;
import lixco.com.entity.Car;
import lixco.com.entity.CarOwner;
import lixco.com.entity.FreightContract;
import lixco.com.entity.FreightContractDetail;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.Product;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.IFreightContractService;
import lixco.com.reqInfo.FreightContractDetailReqInfo;
import lixco.com.reqInfo.FreightContractReqInfo;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class FreightContractService implements IFreightContractService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@Override
	public int search(String json, List<FreightContract> list) {
		int res = -1;
		try {/*
			 * {freight_contract_info:{contract_no:
			 * '',from_date:'',to_date:'',customer_id:0,car_id:0,payment_method_id:0,payment:-1},
			 * page:{page_index:0, page_size:0}}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hContractCode = JsonParserUtil.getValueString(j.get("contract"), "contract_code", null);
			HolderParser hContractNo = JsonParserUtil.getValueString(j.get("contract"), "contract_no", null);
			HolderParser hFromDate = JsonParserUtil.getValueString(j.get("contract"), "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j.get("contract"), "to_date", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j.get("contract"), "customer_id", null);
			HolderParser hCarId = JsonParserUtil.getValueNumber(j.get("contract"), "car_id", null);
			HolderParser hPaymentMethodId = JsonParserUtil.getValueNumber(j.get("contract"), "payment_method_id", null);
			HolderParser hPayment = JsonParserUtil.getValueNumber(j.get("contract"), "payment", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<FreightContract> cq = cb.createQuery(FreightContract.class);
			Root<FreightContract> root_ = cq.from(FreightContract.class);
			root_.fetch("customer", JoinType.INNER);
			Fetch<Car, CarOwner> car_ = root_.fetch("car", JoinType.INNER);
			root_.fetch("payment_method", JoinType.LEFT);
			car_.fetch("car_owner", JoinType.INNER);
			List<Predicate> predicates = new ArrayList<Predicate>();
			ParameterExpression<String> pContractCode = cb.parameter(String.class);
			ParameterExpression<String> pContractNo = cb.parameter(String.class);
			ParameterExpression<Date> pFromDate = cb.parameter(Date.class);
			ParameterExpression<Date> pToDate = cb.parameter(Date.class);
			ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
			ParameterExpression<Long> pCarId = cb.parameter(Long.class);
			ParameterExpression<Long> pPaymentMethodId = cb.parameter(Long.class);
			ParameterExpression<Integer> pPayment = cb.parameter(Integer.class);

			Predicate dis0 = cb.disjunction();
			dis0.getExpressions().add(cb.equal(pContractCode, ""));
			dis0.getExpressions().add(cb.equal(root_.get("contract_code"), pContractCode));
			predicates.add(dis0);
			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.equal(pContractNo, ""));
			dis.getExpressions().add(cb.equal(root_.get("contract_no"), pContractNo));
			predicates.add(dis);
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.isNull(pFromDate));
			dis1.getExpressions().add(cb.greaterThanOrEqualTo(root_.get("contract_date"), pFromDate));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.isNull(pToDate));
			dis2.getExpressions().add(cb.lessThanOrEqualTo(root_.get("contract_date"), pToDate));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pCustomerId, 0));
			dis3.getExpressions().add(cb.equal(root_.get("customer").get("id"), pCustomerId));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pCarId, 0));
			dis4.getExpressions().add(cb.equal(root_.get("car").get("id"), pCarId));
			predicates.add(dis4);
			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.equal(pPaymentMethodId, 0));
			dis5.getExpressions().add(cb.equal(root_.get("payment_method").get("id"), pPaymentMethodId));
			predicates.add(dis5);
			Predicate dis6 = cb.disjunction();
			dis6.getExpressions().add(cb.equal(pPayment, -1));
			dis6.getExpressions().add(cb.equal(root_.get("payment"), pPayment));
			predicates.add(dis6);

			cq.select(root_).where(cb.and(predicates.toArray(new Predicate[0])))
					.orderBy(cb.desc(root_.get("contract_code")));
			TypedQuery<FreightContract> query = em.createQuery(cq);

			query.setParameter(pContractCode, Objects.toString(hContractCode.getValue(), ""));
			query.setParameter(pContractNo, Objects.toString(hContractNo.getValue(), ""));
			query.setParameter(pFromDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pToDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue(), "0")));
			query.setParameter(pCarId, Long.parseLong(Objects.toString(hCarId.getValue(), "0")));
			query.setParameter(pPaymentMethodId, Long.parseLong(Objects.toString(hPaymentMethodId.getValue(), "0")));
			query.setParameter(pPayment, Integer.parseInt(Objects.toString(hPayment.getValue(), "-1")));

			list.addAll(query.getResultList());
			res = 0;

		} catch (Exception e) {
			logger.error("FreightContractService.search:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int searchType(String json, List<FreightContract> list) {
		int res = -1;
		try {/*
			 * {freight_contract_info:{contract_no:
			 * '',from_date:'',to_date:'',customer_id:0,car_id:0,payment_method_id:0,payment:-1},
			 * page:{page_index:0, page_size:0}}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hContractCode = JsonParserUtil.getValueString(j.get("contract"), "contract_code", null);
			HolderParser hContractNo = JsonParserUtil.getValueString(j.get("contract"), "contract_no", null);
			HolderParser hFromDate = JsonParserUtil.getValueString(j.get("contract"), "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j.get("contract"), "to_date", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j.get("contract"), "customer_id", null);
			HolderParser hCustomerTypeId = JsonParserUtil.getValueNumber(j.get("contract"), "customer_type_id", null);
			HolderParser hCarId = JsonParserUtil.getValueNumber(j.get("contract"), "car_id", null);
			HolderParser hPaymentMethodId = JsonParserUtil.getValueNumber(j.get("contract"), "payment_method_id", null);
			HolderParser hPayment = JsonParserUtil.getValueNumber(j.get("contract"), "payment", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<FreightContract> cq = cb.createQuery(FreightContract.class);
			Root<FreightContract> root_ = cq.from(FreightContract.class);
			root_.fetch("customer", JoinType.INNER);
			Fetch<Car, CarOwner> car_ = root_.fetch("car", JoinType.INNER);
			root_.fetch("payment_method", JoinType.LEFT);
			car_.fetch("car_owner", JoinType.INNER);
			List<Predicate> predicates = new ArrayList<Predicate>();
			ParameterExpression<String> pContractCode = cb.parameter(String.class);
			ParameterExpression<String> pContractNo = cb.parameter(String.class);
			ParameterExpression<Date> pFromDate = cb.parameter(Date.class);
			ParameterExpression<Date> pToDate = cb.parameter(Date.class);
			ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
			ParameterExpression<Long> pCustomerTypeId = cb.parameter(Long.class);
			ParameterExpression<Long> pCarId = cb.parameter(Long.class);
			ParameterExpression<Long> pPaymentMethodId = cb.parameter(Long.class);
			ParameterExpression<Integer> pPayment = cb.parameter(Integer.class);

			Predicate dis0 = cb.disjunction();
			dis0.getExpressions().add(cb.equal(pContractCode, ""));
			dis0.getExpressions().add(cb.equal(root_.get("contract_code"), pContractCode));
			predicates.add(dis0);
			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.equal(pContractNo, ""));
			dis.getExpressions().add(cb.equal(root_.get("contract_no"), pContractNo));
			predicates.add(dis);
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.isNull(pFromDate));
			dis1.getExpressions().add(cb.greaterThanOrEqualTo(root_.get("contract_date"), pFromDate));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.isNull(pToDate));
			dis2.getExpressions().add(cb.lessThanOrEqualTo(root_.get("contract_date"), pToDate));
			predicates.add(dis2);

			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pCustomerId, 0));
			dis3.getExpressions().add(cb.equal(root_.get("customer").get("id"), pCustomerId));
			predicates.add(dis3);

			Predicate dis3_1 = cb.disjunction();
			dis3_1.getExpressions().add(cb.equal(pCustomerTypeId, 0));
			dis3_1.getExpressions().add(
					cb.equal(root_.get("customer").get("customer_types").get("id"), pCustomerTypeId));
			predicates.add(dis3_1);

			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pCarId, 0));
			dis4.getExpressions().add(cb.equal(root_.get("car").get("id"), pCarId));
			predicates.add(dis4);
			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.equal(pPaymentMethodId, 0));
			dis5.getExpressions().add(cb.equal(root_.get("payment_method").get("id"), pPaymentMethodId));
			predicates.add(dis5);
			Predicate dis6 = cb.disjunction();
			dis6.getExpressions().add(cb.equal(pPayment, -1));
			dis6.getExpressions().add(cb.equal(root_.get("payment"), pPayment));
			predicates.add(dis6);

			cq.select(root_).where(cb.and(predicates.toArray(new Predicate[0])))
					.orderBy(cb.desc(root_.get("contract_code")));
			TypedQuery<FreightContract> query = em.createQuery(cq);

			query.setParameter(pContractCode, Objects.toString(hContractCode.getValue(), ""));
			query.setParameter(pContractNo, Objects.toString(hContractNo.getValue(), ""));
			query.setParameter(pFromDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pToDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue(), "0")));
			query.setParameter(pCustomerTypeId, Long.parseLong(Objects.toString(hCustomerTypeId.getValue(), "0")));
			query.setParameter(pCarId, Long.parseLong(Objects.toString(hCarId.getValue(), "0")));
			query.setParameter(pPaymentMethodId, Long.parseLong(Objects.toString(hPaymentMethodId.getValue(), "0")));
			query.setParameter(pPayment, Integer.parseInt(Objects.toString(hPayment.getValue(), "-1")));

			list.addAll(query.getResultList());
			res = 0;

		} catch (Exception e) {
			logger.error("FreightContractService.search:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int insert(FreightContractReqInfo t) {
		int res = -1;
		try {
			FreightContract p = t.getFreight_contract();
			if (p != null) {
				// p.setContract_code(initContractCode());
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("FreightContractService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	private String initContractCode() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
			Root<FreightContract> root = cq.from(FreightContract.class);
			// cq.multiselect(cb.coalesce(cb.max(root.get("id")),0));
			cq.multiselect(cb.coalesce(cb.max(cb.quot((Expression) root.get("contract_code"), 1)), 0));
			TypedQuery<Integer> query = em.createQuery(cq);
			int max = query.getSingleResult();
			double p = (double) max / 100000000;
			if (p < 1) {
				return String.format("%08d", max + 1);
			}
			return (max + 1) + "";
		} catch (Exception e) {
			logger.error("FreightContractService.initContractCode:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int update(FreightContractReqInfo t) {
		int res = -1;
		try {
			FreightContract p = t.getFreight_contract();
			if (p != null) {
				p = em.merge(p);
				if (p != null) {
					FreightContractReqInfo f = new FreightContractReqInfo();
					selectById(p.getId(), f);
					t.setFreight_contract(f.getFreight_contract());
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("FreightContractService.update:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectById(long id, FreightContractReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<FreightContract> cq = cb.createQuery(FreightContract.class);
			Root<FreightContract> root_ = cq.from(FreightContract.class);
			root_.fetch("customer", JoinType.INNER);
			root_.fetch("car", JoinType.LEFT);
			root_.fetch("payment_method", JoinType.LEFT);
			cq.select(root_).where(cb.equal(root_.get("id"), id));
			TypedQuery<FreightContract> query = em.createQuery(cq);
			t.setFreight_contract(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			logger.error("FreightContractService.selectById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int deleteById(long id) {
		int res = -1;
		try {
			// JPQL
			Query query = em.createQuery("delete from FreightContract as fc where fc.id=:id ");
			Query query2 = em.createQuery("delete from FreightContractDetail as dt  where dt.freight_contract =:id");
			query.setParameter("id", id);
			query2.setParameter("id", id);
			if (query2.executeUpdate() >= 0) {
				if (query.executeUpdate() > 0) {
					res = 1;
				} else {
					ct.getUserTransaction().rollback();
				}
			} else {
				ct.getUserTransaction().rollback();
			}
		} catch (Exception e) {
			logger.error("FreightContractService.deleteById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public String initOrderCode() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
			Root<FreightContract> root = cq.from(FreightContract.class);
			// cq.multiselect(cb.coalesce(cb.max(root.get("id")),0));
			cq.multiselect(cb.coalesce(cb.max(cb.quot((Expression) root.get("contract_code"), 1)), 0));
			TypedQuery<Integer> query = em.createQuery(cq);
			int max = query.getSingleResult();
			double p = (double) max / 100000000;
			if (p < 1) {
				return String.format("%08d", max + 1);
			}
			return (max + 1) + "";
		} catch (Exception e) {
			logger.error("FreightContractService.initOrderCode:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int selectByOrderCode(String contractCode, FreightContractReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<FreightContract> cq = cb.createQuery(FreightContract.class);
			Root<FreightContract> root_ = cq.from(FreightContract.class);
			root_.fetch("customer", JoinType.INNER);
			root_.fetch("car", JoinType.LEFT);
			root_.fetch("payment_method", JoinType.LEFT);
			cq.select(root_).where(cb.equal(root_.get("contract_code"), contractCode));
			TypedQuery<FreightContract> query = em.createQuery(cq);
			t.setFreight_contract(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			logger.error("FreightContractService.selectByOrderCode:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public FreightContract selectByOrderCode(String contractCode) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<FreightContract> cq = cb.createQuery(FreightContract.class);
			Root<FreightContract> root_ = cq.from(FreightContract.class);
			root_.fetch("customer", JoinType.INNER);
			root_.fetch("car", JoinType.LEFT);
			root_.fetch("payment_method", JoinType.LEFT);
			cq.select(root_).where(cb.equal(root_.get("contract_code"), contractCode));
			TypedQuery<FreightContract> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			// logger.error("FreightContractService.selectByOrderCode:"+e.getMessage(),e);
		}
		return null;
	}

	@Override
	public int insertDetail(FreightContractDetailReqInfo t) {
		int res = -1;
		try {
			FreightContractDetail p = t.getFreight_contract_detail();
			if (p != null) {
				em.persist(p);
				if (p.getId() > 0) {
					t.setFreight_contract_detail(p);
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("FreightContractService.insertDetail:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int updateDetail(FreightContractDetailReqInfo t) {
		int res = -1;
		try {
			FreightContractDetail p = t.getFreight_contract_detail();
			if (p != null) {
				p = em.merge(p);
				if (p != null) {
					FreightContractDetailReqInfo f = new FreightContractDetailReqInfo();
					selectByIdDetail(p.getId(), f);
					t.setFreight_contract_detail(f.getFreight_contract_detail());
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("FreightContractService.updateDetail:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectFreightContractDetailByFreightContractId(long freightContractId, List<FreightContractDetail> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<FreightContractDetail> cq = cb.createQuery(FreightContractDetail.class);
			Root<FreightContractDetail> root = cq.from(FreightContractDetail.class);
			root.fetch("product_type", JoinType.INNER);
			cq.select(root).where(cb.equal(root.get("freight_contract").get("id"), freightContractId));
			TypedQuery<FreightContractDetail> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("FreightContractService.selectFreightContractDetailByFreightContractId:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectByIdDetail(long id, FreightContractDetailReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<FreightContractDetail> cq = cb.createQuery(FreightContractDetail.class);
			Root<FreightContractDetail> root_ = cq.from(FreightContractDetail.class);
			root_.fetch("product_type", JoinType.INNER);
			cq.select(root_).where(cb.equal(root_.get("id"), id));
			TypedQuery<FreightContractDetail> query = em.createQuery(cq);
			t.setFreight_contract_detail(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			logger.error("FreightContractService.selectByIdDetail:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int getListInvoiceCode(long freightContractId, List<String> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<String> cq = cb.createQuery(String.class);
			Root<Invoice> root_ = cq.from(Invoice.class);
			cq.multiselect(root_.get("invoice_code")).where(
					cb.equal(root_.get("freight_contract").get("id"), freightContractId));
			TypedQuery<String> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("FreightContractService.getListInvoiceCode:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int getListInvoiceCode(Date date, long idCus, List<String> list) {
		int res = -1;
		try {
			if (date != null && idCus != 0) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<String> cq = cb.createQuery(String.class);
				Root<Invoice> root_ = cq.from(Invoice.class);
				cq.multiselect(root_.get("voucher_code")).where(cb.equal(root_.get("customer").get("id"), idCus),
						cb.equal(root_.get("invoice_date"), date));
				TypedQuery<String> query = em.createQuery(cq);
				list.addAll(query.getResultList());
				res = 0;
			}
		} catch (Exception e) {
			logger.error("FreightContractService.getListInvoiceCode:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int getListDataFreightContractByInvoice(List<String> listInvoiceCode, List<FreightContractDetail> list) {
		int res = -1;
		try {
			if (listInvoiceCode.size() > 0) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<FreightContractDetail> cq = cb.createQuery(FreightContractDetail.class);
				Root<InvoiceDetail> root_ = cq.from(InvoiceDetail.class);
				Join<InvoiceDetail, Invoice> invoice_ = root_.join("invoice", JoinType.INNER);
				Join<InvoiceDetail, Product> product_ = root_.join("product", JoinType.INNER);
				Join<Product, ProductType> productType_ = product_.join("product_type", JoinType.INNER);
				cq.select(
						cb.construct(FreightContractDetail.class, productType_.get("id"), productType_.get("code"),
								productType_.get("name"), cb.sum(root_.get("quantity"))))
						.where(cb.in(invoice_.get("voucher_code")).value(listInvoiceCode))
						.groupBy(productType_.get("id"), productType_.get("code"), productType_.get("name"));
				TypedQuery<FreightContractDetail> query = em.createQuery(cq);
				list.addAll(query.getResultList());
				res = 0;
			}
		} catch (Exception e) {
			logger.error("FreightContractService.getListDataFreightContractByInvoice:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int deleteByFreightContractDetailId(long id) {
		int res = -1;
		try {
			Query query = em.createQuery("delete from FreightContractDetail where id =:id");
			query.setParameter("id", id);
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error("FreightContractService.deleteByFreightContractDetailId:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public Car getCarByInvoice(List<String> invoiceCode) {
		try {
			if (invoiceCode != null && invoiceCode.size() > 0) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Car> cq = cb.createQuery(Car.class);
				Root<Invoice> root = cq.from(Invoice.class);
				Join<Invoice, Car> car_ = root.join("car", JoinType.INNER);
				Join<Car, CarOwner> carOwner_ = car_.join("car_owner", JoinType.LEFT);
				// (long id, String license_plate, long carOwnerId,String
				// carOwnerName)
				cq.select(
						cb.construct(Car.class, car_.get("id"), car_.get("license_plate"), carOwner_.get("id"),
								carOwner_.get("name"))).where(root.get("invoice_code").in(invoiceCode));
				TypedQuery<Car> query = em.createQuery(cq);
				List<Car> list = query.getResultList();
				if (list.size() > 0) {
					return list.get(0);
				}
			}
		} catch (Exception e) {
			logger.error("FreightContractService.getCarByInvoice:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int deleteDetailByFreightContract(long freightContractId) {
		int res = -1;
		try {
			Query query = em.createQuery("delete from FreightContractDetail where freight_contract.id=:id ");
			query.setParameter("id", freightContractId);
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error("FreightContractService.deleteDetailByFreightContract:" + e.getMessage(), e);
		}
		return res;
	}

}
