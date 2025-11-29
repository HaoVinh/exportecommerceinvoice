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
import lixco.com.commom_ejb.PagingInfo;
import lixco.com.commom_ejb.ToolTimeCustomer;
import lixco.com.entity.Car;
import lixco.com.entity.City;
import lixco.com.entity.Contract;
import lixco.com.entity.Country;
import lixco.com.entity.Currency;
import lixco.com.entity.Customer;
import lixco.com.entity.HarborCategory;
import lixco.com.entity.IEInvoice;
import lixco.com.entity.IEInvoiceDetail;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.PaymentMethod;
import lixco.com.entity.Product;
import lixco.com.entity.ProductType;
import lixco.com.entity.Stevedore;
import lixco.com.entity.Warehouse;
import lixco.com.interfaces.IIEInvoiceService;
import lixco.com.reportInfo.IECommercialInvoiceReportInfo;
import lixco.com.reportInfo.IEExportReport;
import lixco.com.reportInfo.IEInvoiceCustomerBillNoDetailReport;
import lixco.com.reportInfo.IEInvoiceHarborBillNoDetailReport;
import lixco.com.reportInfo.IEInvoiceListByContNoReport;
import lixco.com.reportInfo.IEInvoiceListByTaxValueZeroReport;
import lixco.com.reportInfo.IEInvoiceOrderListByProductCodeReport;
import lixco.com.reportInfo.IEInvoiceOrderListByVoucherReport;
import lixco.com.reportInfo.IEInvoiceReport;
import lixco.com.reportInfo.IEOrderFormReport;
import lixco.com.reportInfo.IEPackingListReport;
import lixco.com.reportInfo.IEProformaInvocieReportInfo;
import lixco.com.reportInfo.IEVanningReport;
import lixco.com.reportInfo.ReportFormD;
import lixco.com.reqInfo.IEInvoiceDetailReqInfo;
import lixco.com.reqInfo.IEInvoiceReqInfo;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class IEInvoiceService implements IIEInvoiceService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;
	@Override
	public int capNhatDG_TTVND(IEInvoiceDetail ieInvoiceDetail) {
		int res = -1;
		try {
			Query query = em
					.createQuery("update IEInvoiceDetail set unit_price=:unitPrice, total_amount=:total_amount where id =:id");
			query.setParameter("unitPrice", ieInvoiceDetail.getUnit_price());
			query.setParameter("total_amount", ieInvoiceDetail.getTotal_amount());
			query.setParameter("id", ieInvoiceDetail.getId());
			res = query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	@Override
	public int insert(IEInvoiceReqInfo t) {
		int res = -1;
		try {
			IEInvoice p = t.getIe_invoice();
			if (p != null) {
				if (p.getInvoice_code() == null || "".equals(p.getInvoice_code())) {
					p.setInvoice_code(initInvoiceCode());
				}
				if (p.getVoucher_code() == null || "".equals(p.getVoucher_code())) {
					p.setVoucher_code(initVoucherCode());
				}
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("IEInvoiceService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int update(IEInvoiceReqInfo t) {
		int res = -1;
		try {
			IEInvoice p = t.getIe_invoice();
			if (p != null) {
				if (em.merge(p) != null) {
					selectById(p.getId(), t);
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("IEInvoiceService.update:" + e.getMessage(), e);
		}
		return res;
	}

	@Override@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int search(String json, PagingInfo page, List<IEInvoice> list) {
		int res = -1;
		try {
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hstextStr = JsonParserUtil.getValueString(j.get("ie_invoice"), "stextStr", null);
			HolderParser hFromDate = JsonParserUtil.getValueString(j.get("ie_invoice"), "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j.get("ie_invoice"), "to_date", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j.get("ie_invoice"), "customer_id", null);
			HolderParser hInvoiceCode = JsonParserUtil.getValueString(j.get("ie_invoice"), "invoice_code", null);
			HolderParser hVoucherCode = JsonParserUtil.getValueString(j.get("ie_invoice"), "voucher_code", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(j.get("ie_invoice"), "ie_categories_id", null);
			HolderParser hContractVoucherCode = JsonParserUtil.getValueString(j.get("ie_invoice"),
					"contract_voucher_code", null);

			HolderParser hProductId = JsonParserUtil.getValueNumber(j.get("ie_invoice"), "product_id", null);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			ParameterExpression<Date> pFromDate = cb.parameter(Date.class);
			ParameterExpression<Date> pToDate = cb.parameter(Date.class);
			ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
			ParameterExpression<String> pInvoiceCode = cb.parameter(String.class);
			ParameterExpression<String> pVoucherCode = cb.parameter(String.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			ParameterExpression<Long> pIECategoriesId = cb.parameter(Long.class);
			ParameterExpression<String> pContractVoucherCode = cb.parameter(String.class);
			CriteriaQuery<IEInvoice> cq = cb.createQuery(IEInvoice.class);
			Root<IEInvoice> root_ = cq.from(IEInvoice.class);
			root_.fetch("customer", JoinType.INNER);
			root_.fetch("car", JoinType.LEFT);
			root_.fetch("stocker", JoinType.LEFT);
			root_.fetch("payment_method", JoinType.LEFT);
			root_.fetch("ie_categories", JoinType.LEFT);
			Join<IEInvoice, Contract> contract_ = (Join) root_.fetch("contract", JoinType.LEFT);
			root_.fetch("stevedore", JoinType.LEFT);
			root_.fetch("form_up_goods", JoinType.LEFT);
			root_.fetch("warehouse", JoinType.LEFT);
			root_.fetch("harbor_category", JoinType.LEFT);
			root_.fetch("currency", JoinType.LEFT);
			root_.fetch("post_of_tran", JoinType.LEFT);
			root_.fetch("invoice", JoinType.LEFT);

			Subquery<Long> subquery = cq.subquery(Long.class);
			Root<IEInvoiceDetail> rootSub = subquery.from(IEInvoiceDetail.class);
			subquery.select(rootSub.get("ie_invoice").get("id")).where(cb.equal(rootSub.get("product"), pProductId));

			List<Predicate> predicates = new ArrayList<Predicate>();
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.isNull(pFromDate));
			dis1.getExpressions().add(cb.greaterThanOrEqualTo(root_.get("invoice_date"), pFromDate));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.isNull(pToDate));
			dis2.getExpressions().add(cb.lessThanOrEqualTo(root_.get("invoice_date"), pToDate));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pCustomerId, 0));
			dis3.getExpressions().add(cb.equal(root_.get("customer").get("id"), pCustomerId));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pInvoiceCode, ""));
			dis4.getExpressions().add(cb.equal(root_.get("invoice_code"), pInvoiceCode));
			predicates.add(dis4);
			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.equal(pVoucherCode, ""));
			dis5.getExpressions().add(cb.equal(root_.get("voucher_code"), pVoucherCode));
			predicates.add(dis5);
			Predicate dis6 = cb.disjunction();
			dis6.getExpressions().add(cb.equal(pProductId, 0));
			dis6.getExpressions().add(cb.in(root_.get("id")).value(subquery));
			predicates.add(dis6);
			Predicate dis7 = cb.disjunction();
			dis7.getExpressions().add(cb.equal(pIECategoriesId, 0));
			dis7.getExpressions().add(cb.equal(root_.get("ie_categories").get("id"), pIECategoriesId));
			predicates.add(dis7);
			Predicate dis8 = cb.disjunction();
			dis8.getExpressions().add(cb.equal(pContractVoucherCode, ""));
			dis8.getExpressions().add(cb.equal(contract_.get("voucher_code"), pContractVoucherCode));
			predicates.add(dis8);

			List<Predicate> predicatesStr = new ArrayList<Predicate>();
			String stextStr = Objects.toString(hstextStr.getValue());
			if (hstextStr.getValue() != null && !"".equals(stextStr.trim())) {
				Predicate predicateinvoice_code = cb.like(root_.get("idfox"), "%" + stextStr + "%");// id
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
																										// tao
				predicatesStr.add(predicatecreated_by);
				Predicate predicatecar = cb.like(contract_.get("voucher_code"), "%" + stextStr + "%");// hop
																										// dong
				predicatesStr.add(predicatecar);

				List<Predicate> subpredicates = new LinkedList<Predicate>();
				Subquery<IEInvoiceDetail> sqOne = cq.subquery(IEInvoiceDetail.class);
				Root subroot = sqOne.from(IEInvoiceDetail.class);
				Join<IEInvoiceDetail, Product> prd_ = subroot.join("product", JoinType.LEFT);
				Predicate predicatePdCode = cb.like(prd_.get("product_code"), "%" + stextStr + "%");
				subpredicates.add(predicatePdCode);
				Predicate predicatePdName = cb.like(prd_.get("product_name"), "%" + stextStr + "%");
				subpredicates.add(predicatePdName);
				sqOne.select(subroot.get("ie_invoice")).where(cb.or(subpredicates.toArray(new Predicate[0])));
				Predicate predicateSub = cb.equal(root_, cb.any(sqOne));
				predicatesStr.add(predicateSub);// truy cap chi tiet
			}
			if (predicatesStr.size() != 0) {
				cq.select(root_).where(cb.and(predicates.toArray(new Predicate[0])),
						cb.or(predicatesStr.toArray(new Predicate[0])));
			} else {
				cq.select(root_).where(cb.and(predicates.toArray(new Predicate[0])));
			}

			TypedQuery<IEInvoice> query = em.createQuery(cq);

			query.setParameter(pFromDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hFromDate.getValue()), "dd/MM/yyyy"));
			query.setParameter(pToDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hToDate.getValue()), "dd/MM/yyyy"));
			query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter(pInvoiceCode, Objects.toString(hInvoiceCode.getValue(), ""));
			query.setParameter(pVoucherCode, Objects.toString(hVoucherCode.getValue(), ""));
			query.setParameter(pProductId, Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter(pIECategoriesId, Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter(pContractVoucherCode, Objects.toString(hContractVoucherCode.getValue(), ""));

			list.addAll(query.getResultList());
			res = 0;

		} catch (Exception e) {
			logger.error("IEInvoiceService.search:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int deleteById(long id) {
		int res = -1;
		try {
			// thực hiện xóa chi tiết phiếu xuất xuất khẩu
			Query queryDelIEInvoiceDetail = em
					.createQuery("delete from IEInvoiceDetail as dt where dt.ie_invoice.id = :id");
			queryDelIEInvoiceDetail.setParameter("id", id);
			if (queryDelIEInvoiceDetail.executeUpdate() >= 0) {
				// thực hiện delete phiếu xuất xuất khẩu
				Query queryDelIEInvoice = em.createQuery("delete from IEInvoice as d where d.id=:id");
				queryDelIEInvoice.setParameter("id", id);
				if (queryDelIEInvoice.executeUpdate() >= 0) {
					res = 0;
				} else {
					ct.getUserTransaction().rollback();
				}
			} else {
				ct.getUserTransaction().rollback();
			}
		} catch (Exception e) {
			logger.error("IEInvoiceService.deleteById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectById(long id, IEInvoiceReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEInvoice> cq = cb.createQuery(IEInvoice.class);
			Root<IEInvoice> root = cq.from(IEInvoice.class);
			root.fetch("customer", JoinType.INNER);
			root.fetch("car", JoinType.LEFT);
			root.fetch("stocker", JoinType.LEFT);
			root.fetch("payment_method", JoinType.LEFT);
			root.fetch("ie_categories", JoinType.LEFT);
			root.fetch("contract", JoinType.LEFT);
			root.fetch("stevedore", JoinType.LEFT);
			root.fetch("form_up_goods", JoinType.LEFT);
			root.fetch("warehouse", JoinType.LEFT);
			root.fetch("harbor_category", JoinType.LEFT);
			root.fetch("currency", JoinType.LEFT);
			root.fetch("post_of_tran", JoinType.LEFT);
			root.fetch("invoice", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<IEInvoice> query = em.createQuery(cq);
			t.setIe_invoice(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectById:" + e.getMessage(), e);
		}
		return res;
	}
	@Override@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public IEInvoice selectById(long id) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEInvoice> cq = cb.createQuery(IEInvoice.class);
			Root<IEInvoice> root = cq.from(IEInvoice.class);
			root.fetch("customer", JoinType.INNER);
			root.fetch("car", JoinType.LEFT);
			root.fetch("stocker", JoinType.LEFT);
			root.fetch("payment_method", JoinType.LEFT);
			root.fetch("ie_categories", JoinType.LEFT);
			root.fetch("contract", JoinType.LEFT);
			root.fetch("stevedore", JoinType.LEFT);
			root.fetch("form_up_goods", JoinType.LEFT);
			root.fetch("warehouse", JoinType.LEFT);
			root.fetch("harbor_category", JoinType.LEFT);
			root.fetch("currency", JoinType.LEFT);
			root.fetch("post_of_tran", JoinType.LEFT);
			root.fetch("invoice", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<IEInvoice> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectById:" + e.getMessage(), e);
		}
		return null;
	}
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private String initInvoiceCode() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
			Root<IEInvoice> root = cq.from(IEInvoice.class);
			// cq.multiselect(cb.coalesce(cb.max(root.get("id")),0));
			cq.multiselect(cb.coalesce(cb.max(cb.quot((Expression) root.get("invoice_code"), 1)), 0));
			TypedQuery<Integer> query = em.createQuery(cq);
			int max = query.getSingleResult();
			double p = (double) max / 100000000;
			if (p < 1) {
				return String.format("%08d", max + 1);
			}
			return (max + 1) + "";
		} catch (Exception e) {
			logger.error("IEInvoiceService.initInvoiceCode:" + e.getMessage(), e);
		}
		return null;
	}
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private String initVoucherCode() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
			Root<IEInvoice> root = cq.from(IEInvoice.class);
			// cq.multiselect(cb.coalesce(cb.max(root.get("id")),0));
			cq.multiselect(cb.coalesce(cb.max(cb.quot((Expression) root.get("voucher_code"), 1)), 0));
			TypedQuery<Integer> query = em.createQuery(cq);
			int max = query.getSingleResult();
			double p = (double) max / 10000000;
			if (p < 1) {
				return String.format("%07d", max + 1);
			}
			return (max + 1) + "";
		} catch (Exception e) {
			logger.error("IEInvoiceService.initVoucherCode:" + e.getMessage(), e);
		}
		return null;

	}

	@Override
	public int insertDetail(IEInvoiceDetailReqInfo t) {
		int res = -1;
		try {
			IEInvoiceDetail p = t.getIe_invoice_detail();
			if (p != null) {
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("IEInvoiceService.insertDetail:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int updateDetail(IEInvoiceDetailReqInfo t) {
		int res = -1;
		try {
			IEInvoiceDetail p = t.getIe_invoice_detail();
			if (p != null) {
				if (em.merge(p) != null) {
					selectDetailById(p.getId(), t);
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("IEInvoiceService.updateDetail:" + e.getMessage(), e);
		}
		return res;
	}

	@Override@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public IEInvoice selectByCodeOnlyId(String invoiceCode) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEInvoice> cq = cb.createQuery(IEInvoice.class);
			Root<IEInvoice> root = cq.from(IEInvoice.class);
			cq.select(cb.construct(IEInvoice.class, root.get("id"))).where(
					cb.equal(root.get("invoice_code"), invoiceCode));
			TypedQuery<IEInvoice> query = em.createQuery(cq);
			IEInvoice ieInvoice = query.getSingleResult();
			return ieInvoice;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectByCodeOnlyId:" + e.getMessage(), e);
		}
		return null;
	}

	@Override@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEInvoideDetailByInvoice(long iEInvoiceId, List<IEInvoiceDetail> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEInvoiceDetail> cq = cb.createQuery(IEInvoiceDetail.class);
			Root<IEInvoiceDetail> root = cq.from(IEInvoiceDetail.class);
			root.fetch("product", JoinType.INNER);
			cq.select(root).where(cb.equal(root.get("ie_invoice").get("id"), iEInvoiceId));
			TypedQuery<IEInvoiceDetail> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEInvoideDetailByInvoice:" + e.getMessage(), e);
		}
		return res;
	}

	@Override@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEInvoideDetailByInvoices(List<Long> iEInvoiceIds, List<IEInvoiceDetail> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEInvoiceDetail> cq = cb.createQuery(IEInvoiceDetail.class);
			Root<IEInvoiceDetail> root = cq.from(IEInvoiceDetail.class);
			root.fetch("product", JoinType.INNER);
			cq.select(root).where(cb.in(root.get("ie_invoice").get("id")).value(iEInvoiceIds));
			TypedQuery<IEInvoiceDetail> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEInvoideDetailByInvoices:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int deleteDetailById(long id) {
		int res = -1;
		try {
			// JQPL
			Query query = em.createQuery("delete from IEInvoiceDetail where id=:id ");
			query.setParameter("id", id);
			res = query.executeUpdate();
		} catch (Exception e) {
			logger.error("IEInvoiceService.deleteDetailById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectDetailById(long id, IEInvoiceDetailReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEInvoiceDetail> cq = cb.createQuery(IEInvoiceDetail.class);
			Root<IEInvoiceDetail> root = cq.from(IEInvoiceDetail.class);
			root.fetch("product", JoinType.INNER);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<IEInvoiceDetail> query = em.createQuery(cq);
			t.setIe_invoice_detail(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectDetailById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectCommercialInvoiceReport(long iEInvoiceId, List<IECommercialInvoiceReportInfo> list) {
		int res = -1;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select p.id as product_id,p.product_code as product_code,p.barcode as barcode, p.en_name as product_en_name,p.specification,p.unit,pt.id as product_type_id,pt.en_name as product_type_en_name,t.quantity_export, ");
			sql.append("t.foreign_unit_price,t.total_export_foreign_amount,t.order_no from product as p ");
			sql.append("inner join producttype as pt on p.product_type_id=pt.id ");
			sql.append("inner join ( select d.product_id,sum(d.quantity_export) as quantity_export,sum(d.foreign_unit_price)/count(d.id) as foreign_unit_price, ");
			sql.append("sum(d.total_export_foreign_amount) as total_export_foreign_amount,d.order_no as order_no ");
			sql.append("from ieinvoicedetail as d  where d.ie_invoice_id=:pid group by d.product_id) as t on t.product_id=p.id  order by pt.en_name, p.en_name asc");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("pid", iEInvoiceId);
			List<Object[]> listObj = query.getResultList();
			for (Object[] p : listObj) {
				IECommercialInvoiceReportInfo item = new IECommercialInvoiceReportInfo(Long.parseLong(Objects.toString(
						p[0], "0")), Objects.toString(p[1], null),Objects.toString(p[2], null),Objects.toString(p[3], null), Double.parseDouble(Objects.toString(p[4])),
						Objects.toString(p[5], null), Long.parseLong(Objects.toString(p[6], "0")), Objects.toString(
								p[7], null), Double.parseDouble(Objects.toString(p[8], "0")),
						Double.parseDouble(Objects.toString(p[9], "0")),
						Double.parseDouble(Objects.toString(p[10], "0")), Objects.toString(p[11], ""));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectCommercialInvoiceReport:" + e.getMessage(), e);
		}
		return res;
	}
	

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectCommercialInvoiceReport(String json, List<IECommercialInvoiceReportInfo> list) {
		int res = -1;
		try {/* list_ie_invoice_detail_id:[]} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hListIdDetail = JsonParserUtil.getValueList(j, "list_ie_invoice_detail_id", null,
					new TypeToken<List<Long>>() {
					}.getType());
			List<Long> listId = (List<Long>) hListIdDetail.getValue();
			StringBuilder sql = new StringBuilder();
			sql.append("select p.id as product_id,p.product_code as product_code,p.barcode as barcode,p.en_name as product_en_name,p.specification,p.unit,pt.id as product_type_id,pt.en_name as product_type_en_name,t.quantity_export, ");
			sql.append("t.foreign_unit_price,t.total_export_foreign_amount,t.order_no from product as p ");
			sql.append("inner join producttype as pt on p.product_type_id=pt.id ");
			sql.append("inner join ( select d.product_id,sum(d.quantity_export) as quantity_export,sum(d.foreign_unit_price)/count(d.id) as foreign_unit_price, ");
			sql.append("sum(d.total_export_foreign_amount) as total_export_foreign_amount,d.order_no as order_no ");
			sql.append("from ieinvoicedetail as d  where d.id in :pid group by d.product_id) as t on t.product_id=p.id  order by pt.en_name, p.en_name asc ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("pid", listId);
			List<Object[]> listObj = query.getResultList();
			for (Object[] p : listObj) {
				IECommercialInvoiceReportInfo item = new IECommercialInvoiceReportInfo(Long.parseLong(Objects
						.toString(p[0])), Objects.toString(p[1], null),
						Objects.toString(p[2], null),
						Objects.toString(p[3], null), 
						Double.parseDouble(Objects.toString(p[4], "0")), Objects.toString(p[5], null),
						Long.parseLong(Objects.toString(p[6], "0")), Objects.toString(p[7], "0"),
						Double.parseDouble(Objects.toString(p[8], "0")),
						Double.parseDouble(Objects.toString(p[9], "0")),
						Double.parseDouble(Objects.toString(p[10], "0")), Objects.toString(p[11], ""));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectCommercialInvoiceReport_:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectOrderFormReport(long iEInvoiceId, List<IEOrderFormReport> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEOrderFormReport> cq = cb.createQuery(IEOrderFormReport.class);
			Root<IEInvoiceDetail> root = cq.from(IEInvoiceDetail.class);
			Join<IEInvoiceDetail, Product> product = root.join("product", JoinType.INNER);
			cq.select(
					cb.construct(IEOrderFormReport.class, product.get("product_code"), product.get("product_name"),
							product.get("lever_code"), product.get("specification"), root.get("quantity_export"),
							root.get("quantity"), root.get("unit_price"), root.get("total_amount"),
							root.get("batch_code"))).where(cb.equal(root.get("ie_invoice").get("id"), iEInvoiceId));
			TypedQuery<IEOrderFormReport> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectOrderFormReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEProformaInvoiceReport(long iEInvoiceId, List<IEProformaInvocieReportInfo> list) {
		int res = -1;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select p.id as product_id,p.en_name as product_en_name,p.specification,p.unit,pt.id as product_type_id,pt.en_name as product_type_en_name,t.quantity_export, ");
			sql.append("t.foreign_unit_price,t.total_export_foreign_amount from product as p ");
			sql.append("inner join producttype as pt on p.product_type_id=pt.id ");
			sql.append("inner join ( select d.product_id,sum(d.quantity_export) as quantity_export,sum(d.foreign_unit_price)/count(d.id) as foreign_unit_price, ");
			sql.append("sum(d.total_export_foreign_amount) as total_export_foreign_amount ");
			sql.append("from ieinvoicedetail as d  where d.ie_invoice_id=:pid group by d.product_id) as t on t.product_id=p.id  ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("pid", iEInvoiceId);
			List<Object[]> listObj = query.getResultList();
			for (Object[] p : listObj) {
				IEProformaInvocieReportInfo item = new IEProformaInvocieReportInfo(Long.parseLong(Objects
						.toString(p[0])), Objects.toString(p[1], null),
						Double.parseDouble(Objects.toString(p[2], "0")), Objects.toString(p[3], null),
						Long.parseLong(Objects.toString(p[4])), Objects.toString(p[5], null),
						Double.parseDouble(Objects.toString(p[6], "0")),
						Double.parseDouble(Objects.toString(p[7], "0")),
						Double.parseDouble(Objects.toString(p[8], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEProformaInvoiceReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEProformaInvoiceReport(String json, List<IEProformaInvocieReportInfo> list) {
		int res = -1;
		try {
			/* list_ie_invoice_detail_id:[]} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hListIdDetail = JsonParserUtil.getValueList(j, "list_ie_invoice_detail_id", null,
					new TypeToken<List<Long>>() {
					}.getType());
			List<Long> listId = (List<Long>) hListIdDetail.getValue();
			StringBuilder sql = new StringBuilder();
			sql.append("select p.id as product_id,p.en_name as product_en_name,p.specification,p.unit,pt.id as product_type_id,pt.en_name as product_type_en_name,t.quantity_export, ");
			sql.append("t.foreign_unit_price,t.total_export_foreign_amount from product as p ");
			sql.append("inner join producttype as pt on p.product_type_id=pt.id ");
			sql.append("inner join ( select d.product_id,sum(d.quantity_export) as quantity_export,sum(d.foreign_unit_price)/count(d.id) as foreign_unit_price, ");
			sql.append("sum(d.total_export_foreign_amount) as total_export_foreign_amount ");
			sql.append("from ieinvoicedetail as d  where d.id in :pid group by d.product_id) as t on t.product_id=p.id  ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("pid", listId);
			List<Object[]> listObj = query.getResultList();
			for (Object[] p : listObj) {
				IEProformaInvocieReportInfo item = new IEProformaInvocieReportInfo(Long.parseLong(Objects
						.toString(p[0])), Objects.toString(p[1], null),
						Double.parseDouble(Objects.toString(p[2], null)), Objects.toString(p[3], null),
						Long.parseLong(Objects.toString(p[4])), Objects.toString(p[5], null),
						Double.parseDouble(Objects.toString(p[6], "0")),
						Double.parseDouble(Objects.toString(p[7], "0")),
						Double.parseDouble(Objects.toString(p[8], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEExportReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEPackingListReport(long iEInvoiceId, List<IEPackingListReport> list) {
		int res = -1;
		try {
			// (long product_id, String product_en_name, long
			// product_type_id,String product_type_en_name, String unit, double
			// specification, double tare, double factor,double quantity_export)
			StringBuilder sql = new StringBuilder();
			sql.append("select p.id as product_id,p.en_name as product_en_name,pt.id as product_type_id,pt.en_name as product_type_en_name,p.unit,p.specification,p.tare,p.factor,t.quantity_export, ");
			sql.append("t.foreign_unit_price,t.total_export_foreign_amount, t.order_no, p.product_code,t.container_no, t.ft_container, p.barcode,p.cbm from product as p ");
			sql.append("inner join producttype as pt on p.product_type_id=pt.id ");
			sql.append("inner join ( select d.product_id,sum(d.quantity_export) as quantity_export,sum(d.foreign_unit_price)/count(d.id) as foreign_unit_price, ");
			sql.append("sum(d.total_export_foreign_amount) as total_export_foreign_amount, d.order_no as order_no, d.container_no as container_no, d.ft_container as ft_container  ");
			sql.append("from ieinvoicedetail as d  where d.ie_invoice_id=:pid group by d.product_id) as t on t.product_id=p.id order by pt.en_name, p.en_name asc");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("pid", iEInvoiceId);
			List<Object[]> listObj = query.getResultList();
			for (Object[] p : listObj) {
				IEPackingListReport item = new IEPackingListReport(Long.parseLong(Objects.toString(p[0])),
						Objects.toString(p[1], ""), Long.parseLong(Objects.toString(p[2])), Objects.toString(p[3],
								""), Objects.toString(p[4], ""), Double.parseDouble(Objects.toString(p[5])),
						Double.parseDouble(Objects.toString(p[6])), Double.parseDouble(Objects.toString(p[7])),
						Double.parseDouble(Objects.toString(p[8])), Double.parseDouble(Objects.toString(p[9])),
						Double.parseDouble(Objects.toString(p[10])), Objects.toString(p[11], ""), Objects.toString(p[12]),Objects.toString(p[13]),Objects.toString(p[14]),Objects.toString(p[15],""),Double.parseDouble(Objects.toString(p[16])));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEPackingListReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEPackingListReport(String json, List<IEPackingListReport> list) {
		int res = -1;
		try {
			/* list_ie_invoice_detail_id:[]} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hListIdDetail = JsonParserUtil.getValueList(j, "list_ie_invoice_detail_id", null,
					new TypeToken<List<Long>>() {
					}.getType());
			List<Long> listId = (List<Long>) hListIdDetail.getValue();
			// (long product_id, String product_en_name, long
			// product_type_id,String product_type_en_name, String unit, double
			// specification, double tare, double factor,double quantity_export)
			StringBuilder sql = new StringBuilder();
			sql.append("select p.id as product_id,p.en_name as product_en_name,pt.id as product_type_id,pt.en_name as product_type_en_name,p.unit,p.specification,p.tare,p.factor,t.quantity_export, ");
			sql.append("t.foreign_unit_price,t.total_export_foreign_amount, t.order_no,p.product_code from product as p ");
			sql.append("inner join producttype as pt on p.product_type_id=pt.id ");
			sql.append("inner join ( select d.product_id,sum(d.quantity_export) as quantity_export,sum(d.foreign_unit_price)/count(d.id) as foreign_unit_price, ");
			sql.append("sum(d.total_export_foreign_amount) as total_export_foreign_amount, d.order_no as order_no ");
			sql.append("from ieinvoicedetail as d  where d.id in :pid group by d.product_id) as t on t.product_id=p.id   order by pt.en_name, p.en_name asc ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("pid", listId);
			List<Object[]> listObj = query.getResultList();
			for (Object[] p : listObj) {
				IEPackingListReport item = new IEPackingListReport(Long.parseLong(Objects.toString(p[0])),
						Objects.toString(p[1], null), Long.parseLong(Objects.toString(p[2])), Objects.toString(p[3],
								null), Objects.toString(p[4], null), Double.parseDouble(Objects.toString(p[5])),
						Double.parseDouble(Objects.toString(p[6])), Double.parseDouble(Objects.toString(p[7])),
						Double.parseDouble(Objects.toString(p[8])), Double.parseDouble(Objects.toString(p[9])),
						Double.parseDouble(Objects.toString(p[10])), Objects.toString(p[11], ""),Objects.toString(p[12]));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEExportReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEVanningReport(long iEInvoiceId, List<IEVanningReport> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEVanningReport> cq = cb.createQuery(IEVanningReport.class);
			Root<IEInvoiceDetail> root = cq.from(IEInvoiceDetail.class);
			Join<IEInvoiceDetail, Product> product = root.join("product", JoinType.INNER);
			Join<Product, ProductType> productType = product.join("product_type", JoinType.LEFT);
			// (long product_type_id, String product_type_en_name, String
			// product_en_name)
			cq.select(
					cb.construct(IEVanningReport.class, productType.get("id"), productType.get("en_name"),
							product.get("en_name"), root.get("container_no"), root.get("order_no"))).distinct(true)
					.where(cb.equal(root.get("ie_invoice").get("id"), iEInvoiceId))
					.orderBy(cb.asc(productType.get("en_name")), cb.asc(product.get("en_name")));
			TypedQuery<IEVanningReport> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEVanningReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEVanningReport(String json, List<IEVanningReport> list) {
		int res = -1;
		try {
			/* list_ie_invoice_detail_id:[]} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hListIdDetail = JsonParserUtil.getValueList(j, "list_ie_invoice_detail_id", null,
					new TypeToken<List<Long>>() {
					}.getType());
			List<Long> listId = (List<Long>) hListIdDetail.getValue();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEVanningReport> cq = cb.createQuery(IEVanningReport.class);
			Root<IEInvoiceDetail> root = cq.from(IEInvoiceDetail.class);
			Join<IEInvoiceDetail, Product> product = root.join("product", JoinType.INNER);
			Join<Product, ProductType> productType = product.join("product_type", JoinType.LEFT);
			// (long product_type_id, String product_type_en_name, String
			// product_en_name)
			cq.select(
					cb.construct(IEVanningReport.class, productType.get("id"), productType.get("en_name"),
							product.get("en_name"), root.get("container_no"), root.get("order_no"))).distinct(true)
					.where(root.get("id").in(listId))
					.orderBy(cb.asc(productType.get("en_name")), cb.asc(product.get("en_name")));
			TypedQuery<IEVanningReport> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEVanningReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEInvoiceReport(long iEInvoiceId, List<IEInvoiceReport> list) {
		int res = -1;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select t.product_id,p.product_name,p.unit,t.quantity_export,t.foreign_unit_price,t.total_export_foreign_amount from product as p ");
			sql.append("inner join ( select d.product_id,sum(d.quantity_export) as quantity_export,sum(d.foreign_unit_price)/count(d.id) as foreign_unit_price, ");
			sql.append("sum(d.total_export_foreign_amount) as total_export_foreign_amount ");
			sql.append(" from ieinvoicedetail as d  where d.ie_invoice_id=:pid group by d.product_id) as t on t.product_id=p.id ");
			sql.append("order by p.product_name ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("pid", iEInvoiceId);
			List<Object[]> listObj = query.getResultList();
			for (Object[] p : listObj) {
				IEInvoiceReport item = new IEInvoiceReport(Long.parseLong(Objects.toString(p[0])), Objects.toString(
						p[1], null), Objects.toString(p[2], null), Double.parseDouble(Objects.toString(p[3])),
						Double.parseDouble(Objects.toString(p[4])), Double.parseDouble(Objects.toString(p[5])));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEInvoiceReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEExportReport(long iEInvoiceId, List<IEExportReport> list) {
		int res = -1;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select t.product_id,p.product_name,p.unit,p.specification,t.quantity_export,t.unit_price,t.total_amount from product as p ");
			sql.append("inner join ( select d.product_id,sum(d.quantity_export) as quantity_export,sum(d.unit_price)/count(d.id) as unit_price, ");
			sql.append("sum(d.total_amount) as total_amount ");
			sql.append(" from ieinvoicedetail as d  where d.ie_invoice_id=:pid group by d.product_id) as t on t.product_id=p.id ");
			sql.append("order by p.product_name ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("pid", iEInvoiceId);
			List<Object[]> listObj = query.getResultList();
			for (Object[] p : listObj) {
				// (long produtc_id,String product_name, String unit, double
				// specification, double quantity_export,double unit_price,
				// double total_amount)
				IEExportReport item = new IEExportReport(Long.parseLong(Objects.toString(p[0])), Objects.toString(p[1],
						null), Objects.toString(p[2], null), Double.parseDouble(Objects.toString(p[3])),
						Double.parseDouble(Objects.toString(p[4])), Double.parseDouble(Objects.toString(p[5])),
						Double.parseDouble(Objects.toString(p[6])));
				list.add(item);
			}
			res = 0;

		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEExportReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEInvoiceOrderListByVoucherReport(String json, List<IEInvoiceOrderListByVoucherReport> list) {
		int res = -1;
		try {
			// {from_date:'',to_date:'',customer_id:0,invoice_code:'',voucher_code:'',product_id:0,ie_categories_id:0,contract_voucher_code:''}
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			HolderParser hInvoiceCode = JsonParserUtil.getValueString(j, "invoice_code", null);
			HolderParser hVoucherCode = JsonParserUtil.getValueString(j, "voucher_code", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(j, "ie_categories_id", null);
			HolderParser hContractVoucherCode = JsonParserUtil.getValueString(j, "contract_voucher_code", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			ParameterExpression<Date> pFromDate = cb.parameter(Date.class);
			ParameterExpression<Date> pToDate = cb.parameter(Date.class);
			ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
			ParameterExpression<String> pInvoiceCode = cb.parameter(String.class);
			ParameterExpression<String> pVoucherCode = cb.parameter(String.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			ParameterExpression<Long> pIECategoriesId = cb.parameter(Long.class);
			ParameterExpression<String> pContractVoucherCode = cb.parameter(String.class);
			CriteriaQuery<IEInvoiceOrderListByVoucherReport> cq = cb
					.createQuery(IEInvoiceOrderListByVoucherReport.class);
			Root<IEInvoiceDetail> root = cq.from(IEInvoiceDetail.class);
			Join<IEInvoiceDetail, Product> product_ = root.join("product", JoinType.INNER);
			Join<InvoiceDetail, IEInvoice> iEInvoice_ = root.join("ie_invoice", JoinType.INNER);
			Join<IEInvoice, Customer> customer_ = iEInvoice_.join("customer", JoinType.INNER);
			Join<IEInvoice, Car> car_ = iEInvoice_.join("car", JoinType.LEFT);
			Join<IEInvoice, PaymentMethod> paymentMethod_ = iEInvoice_.join("payment_method", JoinType.LEFT);
			Join<IEInvoice, Stevedore> stevedore_ = iEInvoice_.join("stevedore", JoinType.LEFT);
			Join<IEInvoice, Contract> contract_ = iEInvoice_.join("contract", JoinType.LEFT);
			Join<IEInvoice, Warehouse> warehouse_ = iEInvoice_.join("warehouse", JoinType.LEFT);
			Join<IEInvoice, Currency> currency_ = iEInvoice_.join("currency", JoinType.LEFT);
			Join<IEInvoice, HarborCategory> harborCategory_ = iEInvoice_.join("post_of_tran", JoinType.LEFT);
			List<Predicate> predicates = new ArrayList<Predicate>();
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.isNull(pFromDate));
			dis1.getExpressions().add(cb.greaterThanOrEqualTo(iEInvoice_.get("invoice_date"), pFromDate));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.isNull(pToDate));
			dis2.getExpressions().add(cb.lessThanOrEqualTo(iEInvoice_.get("invoice_date"), pToDate));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pCustomerId, 0));
			dis3.getExpressions().add(cb.equal(iEInvoice_.get("customer").get("id"), pCustomerId));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pInvoiceCode, ""));
			dis4.getExpressions().add(cb.equal(iEInvoice_.get("invoice_code"), pInvoiceCode));
			predicates.add(dis4);
			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.equal(pVoucherCode, ""));
			dis5.getExpressions().add(cb.equal(iEInvoice_.get("voucher_code"), pVoucherCode));
			predicates.add(dis5);
			Predicate dis6 = cb.disjunction();
			dis6.getExpressions().add(cb.equal(pProductId, 0));
			dis6.getExpressions().add(cb.equal(product_.get("id"), pProductId));
			predicates.add(dis6);
			Predicate dis7 = cb.disjunction();
			dis7.getExpressions().add(cb.equal(pIECategoriesId, 0));
			dis7.getExpressions().add(cb.equal(iEInvoice_.get("ie_categories").get("id"), pIECategoriesId));
			predicates.add(dis7);
			Predicate dis8 = cb.disjunction();
			dis8.getExpressions().add(cb.equal(pContractVoucherCode, ""));
			dis8.getExpressions().add(cb.equal(contract_.get("voucher_code"), pContractVoucherCode));
			predicates.add(dis8);
			// (String voucher_code, Date invoice_date, String
			// contract_voucher_code,
			// String customer_code, String customer_name, String license_plate,
			// String payment_name,
			// String warehouse_name, double tax_value, Date etd_date, double
			// exchange_rate, String currency_type,
			// String note, String bill_no, String declaration_code, String
			// post_of_tran_code, String product_code,
			// String product_name, double quantity_export, double
			// foreign_unit_price, double total_foreign_amount,
			// String driver_name)
			cq.select(
					cb.construct(IEInvoiceOrderListByVoucherReport.class, iEInvoice_.get("voucher_code"),
							iEInvoice_.get("invoice_date"), contract_.get("voucher_code"),
							customer_.get("customer_code"), customer_.get("customer_name"), car_.get("license_plate"),
							paymentMethod_.get("method_name"), stevedore_.get("content"), warehouse_.get("name"),
							iEInvoice_.get("tax_value"), iEInvoice_.get("etd_date"), iEInvoice_.get("exchange_rate"),
							currency_.get("currency_type"), iEInvoice_.get("note"), iEInvoice_.get("bill_no"),
							iEInvoice_.get("declaration_code"), harborCategory_.get("port_no"),
							product_.get("product_code"), product_.get("product_name"), root.get("quantity_export"),
							root.get("foreign_unit_price"), root.get("total_foreign_amount"),
							iEInvoice_.get("driver_name"))).where(cb.and(predicates.toArray(new Predicate[0])))
					.orderBy(cb.asc(iEInvoice_.get("voucher_code")));
			TypedQuery<IEInvoiceOrderListByVoucherReport> query = em.createQuery(cq);
			query.setParameter(pFromDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pToDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter(pInvoiceCode, Objects.toString(hInvoiceCode.getValue(), ""));
			query.setParameter(pVoucherCode, Objects.toString(hVoucherCode.getValue(), ""));
			query.setParameter(pProductId, Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter(pIECategoriesId, Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter(pContractVoucherCode, Objects.toString(hContractVoucherCode.getValue(), ""));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEInvoiceOrderListByVoucherReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEInvoiceOrderListByProductCodeReport(String json, List<IEInvoiceOrderListByProductCodeReport> list) {
		int res = -1;
		try {
			// {from_date:'',to_date:'',customer_id:0,invoice_code:'',voucher_code:'',product_id:0,ie_categories_id:0,contract_voucher_code:''}
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			HolderParser hInvoiceCode = JsonParserUtil.getValueString(j, "invoice_code", null);
			HolderParser hVoucherCode = JsonParserUtil.getValueString(j, "voucher_code", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(j, "ie_categories_id", null);
			HolderParser hContractVoucherCode = JsonParserUtil.getValueString(j, "contract_voucher_code", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			ParameterExpression<Date> pFromDate = cb.parameter(Date.class);
			ParameterExpression<Date> pToDate = cb.parameter(Date.class);
			ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
			ParameterExpression<String> pInvoiceCode = cb.parameter(String.class);
			ParameterExpression<String> pVoucherCode = cb.parameter(String.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			ParameterExpression<Long> pIECategoriesId = cb.parameter(Long.class);
			ParameterExpression<String> pContractVoucherCode = cb.parameter(String.class);
			CriteriaQuery<IEInvoiceOrderListByProductCodeReport> cq = cb
					.createQuery(IEInvoiceOrderListByProductCodeReport.class);
			Root<IEInvoiceDetail> root = cq.from(IEInvoiceDetail.class);
			Join<IEInvoiceDetail, Product> product_ = root.join("product", JoinType.INNER);
			Join<InvoiceDetail, IEInvoice> iEInvoice_ = root.join("ie_invoice", JoinType.INNER);
			Join<IEInvoice, Customer> customer_ = iEInvoice_.join("customer", JoinType.INNER);
			Join<IEInvoice, Car> car_ = iEInvoice_.join("car", JoinType.LEFT);
			Join<IEInvoice, PaymentMethod> paymentMethod_ = iEInvoice_.join("payment_method", JoinType.LEFT);
			Join<IEInvoice, Stevedore> stevedore_ = iEInvoice_.join("stevedore", JoinType.LEFT);
			Join<IEInvoice, Contract> contract_ = iEInvoice_.join("contract", JoinType.LEFT);
			Join<IEInvoice, Warehouse> warehouse_ = iEInvoice_.join("warehouse", JoinType.LEFT);
			Join<IEInvoice, Currency> currency_ = iEInvoice_.join("currency", JoinType.LEFT);
			Join<IEInvoice, HarborCategory> harborCategory_ = iEInvoice_.join("post_of_tran", JoinType.LEFT);
			List<Predicate> predicates = new ArrayList<Predicate>();
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.isNull(pFromDate));
			dis1.getExpressions().add(cb.greaterThanOrEqualTo(iEInvoice_.get("invoice_date"), pFromDate));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.isNull(pToDate));
			dis2.getExpressions().add(cb.lessThanOrEqualTo(iEInvoice_.get("invoice_date"), pToDate));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pCustomerId, 0));
			dis3.getExpressions().add(cb.equal(iEInvoice_.get("customer").get("id"), pCustomerId));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pInvoiceCode, ""));
			dis4.getExpressions().add(cb.equal(iEInvoice_.get("invoice_code"), pInvoiceCode));
			predicates.add(dis4);
			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.equal(pVoucherCode, ""));
			dis5.getExpressions().add(cb.equal(iEInvoice_.get("voucher_code"), pVoucherCode));
			predicates.add(dis5);
			Predicate dis6 = cb.disjunction();
			dis6.getExpressions().add(cb.equal(pProductId, 0));
			dis6.getExpressions().add(cb.equal(product_.get("id"), pProductId));
			predicates.add(dis6);
			Predicate dis7 = cb.disjunction();
			dis7.getExpressions().add(cb.equal(pIECategoriesId, 0));
			dis7.getExpressions().add(cb.equal(iEInvoice_.get("ie_categories").get("id"), pIECategoriesId));
			predicates.add(dis7);
			Predicate dis8 = cb.disjunction();
			dis8.getExpressions().add(cb.equal(pContractVoucherCode, ""));
			dis8.getExpressions().add(cb.equal(contract_.get("voucher_code"), pContractVoucherCode));
			predicates.add(dis8);
			// (String voucher_code, Date invoice_date, String
			// contract_voucher_code,
			// String customer_code, String customer_name, String license_plate,
			// String payment_name,
			// String warehouse_name, double tax_value, Date etd_date, double
			// exchange_rate, String currency_type,
			// String note, String bill_no, String declaration_code, String
			// post_of_tran_code, String product_code,
			// String product_name, double quantity_export, double
			// foreign_unit_price, double total_foreign_amount,
			// String driver_name)
			cq.select(
					cb.construct(IEInvoiceOrderListByProductCodeReport.class, iEInvoice_.get("voucher_code"),
							iEInvoice_.get("invoice_date"), contract_.get("voucher_code"),
							customer_.get("customer_code"), customer_.get("customer_name"), car_.get("license_plate"),
							paymentMethod_.get("method_name"), stevedore_.get("content"), warehouse_.get("name"),
							iEInvoice_.get("tax_value"), iEInvoice_.get("etd_date"), iEInvoice_.get("exchange_rate"),
							currency_.get("currency_type"), iEInvoice_.get("note"), iEInvoice_.get("bill_no"),
							iEInvoice_.get("declaration_code"), harborCategory_.get("port_no"),
							product_.get("product_code"), product_.get("product_name"), root.get("quantity_export"),
							root.get("foreign_unit_price"), root.get("total_foreign_amount"),
							iEInvoice_.get("driver_name"))).where(cb.and(predicates.toArray(new Predicate[0])))
					.orderBy(cb.asc(product_.get("product_code")));
			TypedQuery<IEInvoiceOrderListByProductCodeReport> query = em.createQuery(cq);
			query.setParameter(pFromDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pToDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter(pInvoiceCode, Objects.toString(hInvoiceCode.getValue(), ""));
			query.setParameter(pVoucherCode, Objects.toString(hVoucherCode.getValue(), ""));
			query.setParameter(pProductId, Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter(pIECategoriesId, Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter(pContractVoucherCode, Objects.toString(hContractVoucherCode.getValue(), ""));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEInvoiceOrderListByProductCodeReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEInvoicListByContNoReport(String json, List<IEInvoiceListByContNoReport> list) {
		int res = -1;
		try {
			// {from_date:'',to_date:'',customer_id:0,invoice_code:'',voucher_code:'',product_id:0,ie_categories_id:0,contract_voucher_code:''}
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			HolderParser hInvoiceCode = JsonParserUtil.getValueString(j, "invoice_code", null);
			HolderParser hVoucherCode = JsonParserUtil.getValueString(j, "voucher_code", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(j, "ie_categories_id", null);
			HolderParser hContractVoucherCode = JsonParserUtil.getValueString(j, "contract_voucher_code", null);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			ParameterExpression<Date> pFromDate = cb.parameter(Date.class);
			ParameterExpression<Date> pToDate = cb.parameter(Date.class);
			ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
			ParameterExpression<String> pInvoiceCode = cb.parameter(String.class);
			ParameterExpression<String> pVoucherCode = cb.parameter(String.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			ParameterExpression<Long> pIECategoriesId = cb.parameter(Long.class);
			ParameterExpression<String> pContractVoucherCode = cb.parameter(String.class);
			CriteriaQuery<IEInvoiceListByContNoReport> cq = cb.createQuery(IEInvoiceListByContNoReport.class);
			Root<IEInvoiceDetail> root = cq.from(IEInvoiceDetail.class);

			Join<IEInvoiceDetail, Product> product_ = root.join("product", JoinType.INNER);
			Join<InvoiceDetail, IEInvoice> iEInvoice_ = root.join("ie_invoice", JoinType.INNER);
			Join<IEInvoice, Customer> customer_ = iEInvoice_.join("customer", JoinType.INNER);
			Join<IEInvoice, Contract> contract_ = iEInvoice_.join("contract", JoinType.LEFT);
			Join<Customer, City> city_ = customer_.join("city", JoinType.LEFT);
			iEInvoice_.join("post_of_tran", JoinType.LEFT);

			List<Predicate> predicates = new ArrayList<Predicate>();
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.isNull(pFromDate));
			dis1.getExpressions().add(cb.greaterThanOrEqualTo(iEInvoice_.get("invoice_date"), pFromDate));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.isNull(pToDate));
			dis2.getExpressions().add(cb.lessThanOrEqualTo(iEInvoice_.get("invoice_date"), pToDate));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pCustomerId, 0));
			dis3.getExpressions().add(cb.equal(iEInvoice_.get("customer").get("id"), pCustomerId));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pInvoiceCode, ""));
			dis4.getExpressions().add(cb.equal(iEInvoice_.get("invoice_code"), pInvoiceCode));
			predicates.add(dis4);
			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.equal(pVoucherCode, ""));
			dis5.getExpressions().add(cb.equal(iEInvoice_.get("voucher_code"), pVoucherCode));
			predicates.add(dis5);
			Predicate dis6 = cb.disjunction();
			dis6.getExpressions().add(cb.equal(pProductId, 0));
			dis6.getExpressions().add(cb.equal(product_.get("id"), pProductId));
			predicates.add(dis6);
			Predicate dis7 = cb.disjunction();
			dis7.getExpressions().add(cb.equal(pIECategoriesId, 0));
			dis7.getExpressions().add(cb.equal(iEInvoice_.get("ie_categories").get("id"), pIECategoriesId));
			predicates.add(dis7);
			Predicate dis8 = cb.disjunction();
			dis8.getExpressions().add(cb.equal(pContractVoucherCode, ""));
			dis8.getExpressions().add(cb.equal(contract_.get("voucher_code"), pContractVoucherCode));
			predicates.add(dis8);
			// (String city_name, String customer_name, String ft_container,
			// String container_no,
			// String arrival_place, String container_number, double
			// quantity_export)
			cq.select(
					cb.construct(IEInvoiceListByContNoReport.class, city_.get("city_name"),
							customer_.get("customer_name"), root.get("ft_container"), root.get("container_no"),
							root.get("arrival_place"), cb.sum(root.get("container_number")),
							cb.sum(cb.prod(root.get("quantity_export"),product_.get("factor")))))
					.where(cb.and(predicates.toArray(new Predicate[0])))
					.groupBy(city_.get("city_name"), customer_.get("customer_name"), root.get("ft_container"),
							cb.substring(root.get("container_no").as(String.class), 1, 11));

			TypedQuery<IEInvoiceListByContNoReport> query = em.createQuery(cq);
			query.setParameter(pFromDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pToDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter(pInvoiceCode, Objects.toString(hInvoiceCode.getValue(), ""));
			query.setParameter(pVoucherCode, Objects.toString(hVoucherCode.getValue(), ""));
			query.setParameter(pProductId, Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter(pIECategoriesId, Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter(pContractVoucherCode, Objects.toString(hContractVoucherCode.getValue(), ""));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEInvoicListByContNoReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEInvoiceListByTaxValueZeroReport(String json, List<IEInvoiceListByTaxValueZeroReport> list) {
		int res = -1;
		try {
			// {from_date:'',to_date:'',customer_id:0,invoice_code:'',voucher_code:'',product_id:0,ie_categories_id:0,contract_voucher_code:''}
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			HolderParser hInvoiceCode = JsonParserUtil.getValueString(j, "invoice_code", null);
			HolderParser hVoucherCode = JsonParserUtil.getValueString(j, "voucher_code", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(j, "ie_categories_id", null);
			HolderParser hContractVoucherCode = JsonParserUtil.getValueString(j, "contract_voucher_code", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			ParameterExpression<Date> pFromDate = cb.parameter(Date.class);
			ParameterExpression<Date> pToDate = cb.parameter(Date.class);
			ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
			ParameterExpression<String> pInvoiceCode = cb.parameter(String.class);
			ParameterExpression<String> pVoucherCode = cb.parameter(String.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			ParameterExpression<Long> pIECategoriesId = cb.parameter(Long.class);
			ParameterExpression<String> pContractVoucherCode = cb.parameter(String.class);
			CriteriaQuery<IEInvoiceListByTaxValueZeroReport> cq = cb
					.createQuery(IEInvoiceListByTaxValueZeroReport.class);
			Root<IEInvoiceDetail> root = cq.from(IEInvoiceDetail.class);
			Join<IEInvoiceDetail, Product> product_ = root.join("product", JoinType.INNER);
			Join<InvoiceDetail, IEInvoice> iEInvoice_ = root.join("ie_invoice", JoinType.INNER);
			Join<IEInvoice, Customer> customer_ = iEInvoice_.join("customer", JoinType.INNER);
			Join<IEInvoice, Contract> contract_ = iEInvoice_.join("contract", JoinType.LEFT);
			Join<Customer, City> city_ = customer_.join("city", JoinType.LEFT);
			Join<City, Country> country_ = city_.join("country", JoinType.INNER);
			iEInvoice_.join("post_of_tran", JoinType.LEFT);
			List<Predicate> predicates = new ArrayList<Predicate>();
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.isNull(pFromDate));
			dis1.getExpressions().add(cb.greaterThanOrEqualTo(iEInvoice_.get("invoice_date"), pFromDate));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.isNull(pToDate));
			dis2.getExpressions().add(cb.lessThanOrEqualTo(iEInvoice_.get("invoice_date"), pToDate));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pCustomerId, 0));
			dis3.getExpressions().add(cb.equal(iEInvoice_.get("customer").get("id"), pCustomerId));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pInvoiceCode, ""));
			dis4.getExpressions().add(cb.equal(iEInvoice_.get("invoice_code"), pInvoiceCode));
			predicates.add(dis4);
			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.equal(pVoucherCode, ""));
			dis5.getExpressions().add(cb.equal(iEInvoice_.get("voucher_code"), pVoucherCode));
			predicates.add(dis5);
			Predicate dis6 = cb.disjunction();
			dis6.getExpressions().add(cb.equal(pProductId, 0));
			dis6.getExpressions().add(cb.equal(product_.get("id"), pProductId));
			predicates.add(dis6);
			Predicate dis7 = cb.disjunction();
			dis7.getExpressions().add(cb.equal(pIECategoriesId, 0));
			dis7.getExpressions().add(cb.equal(iEInvoice_.get("ie_categories").get("id"), pIECategoriesId));
			predicates.add(dis7);
			Predicate dis8 = cb.disjunction();
			dis8.getExpressions().add(cb.equal(pContractVoucherCode, ""));
			dis8.getExpressions().add(cb.equal(contract_.get("voucher_code"), pContractVoucherCode));
			predicates.add(dis8);
			// (String contract_voucher_code, Date contract_date, String
			// customer_name,
			// String properties, String country_name, double
			// contract_foreign_total_amount, double contract_total_amount,
			// Date payment_period, String declaration_code, Date
			// declaration_date, String goods, double declaration_quantity,
			// double declaration_foreign_total_amount, double
			// declaration_total_amount, String method_of_transportation,
			// String voucher_code, Date invoice_date, double quantity_export,
			// String material_name,
			// double foreign_total_amount, double total_amount, String payment,
			// String payment_voucher,
			// Date payment_voucher_date, double
			// payment_voucher_foreign_total_amount, double
			// payment_voucher_total_amount,
			// String payment_report)
			cq.select(
					cb.construct(IEInvoiceListByTaxValueZeroReport.class, contract_.get("voucher_code"),
							contract_.get("contract_date"), customer_.get("customer_name"),
							country_.get("country_name"), root.get("total_foreign_amount"), root.get("total_amount"),
							iEInvoice_.get("declaration_code"), root.get("quantity_export"),
							root.get("total_foreign_amount"), root.get("total_amount"), iEInvoice_.get("voucher_code"),
							iEInvoice_.get("invoice_date"), root.get("quantity_export"),
							root.get("total_foreign_amount"), root.get("total_amount"), iEInvoice_.get("payment"),
							root.get("total_foreign_amount"), root.get("total_amount"))).where(
					cb.and(predicates.toArray(new Predicate[0])));
			TypedQuery<IEInvoiceListByTaxValueZeroReport> query = em.createQuery(cq);
			query.setParameter(pFromDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pToDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter(pInvoiceCode, Objects.toString(hInvoiceCode.getValue(), ""));
			query.setParameter(pVoucherCode, Objects.toString(hVoucherCode.getValue(), ""));
			query.setParameter(pProductId, Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter(pIECategoriesId, Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter(pContractVoucherCode, Objects.toString(hContractVoucherCode.getValue(), ""));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEInvoicListByContNoReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEInvoiceHarborBillNoDetailReport(String json, List<IEInvoiceHarborBillNoDetailReport> list) {
		int res = -1;
		try {
			/* list_ie_invoice_detail_id:[]} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hListIdDetail = JsonParserUtil.getValueList(j, "list_ie_invoice_detail_id", null,
					new TypeToken<List<Long>>() {
					}.getType());
			List<Long> listId = (List<Long>) hListIdDetail.getValue();
			// (String container_no, String ft_container, long product_id,String
			// product_en_name, long product_type_id, String
			// product_type_en_name, String unit,
			// double specification, double tare, double factor, double
			// quantity_export)
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEInvoiceHarborBillNoDetailReport> cq = cb
					.createQuery(IEInvoiceHarborBillNoDetailReport.class);
			Root<IEInvoiceDetail> root = cq.from(IEInvoiceDetail.class);
			ParameterExpression<List> param = cb.parameter(List.class);
			Join<IEInvoiceDetail, Product> product = root.join("product", JoinType.INNER);
			Join<Product, ProductType> productType = product.join("product_type", JoinType.LEFT);
			cq.select(
					cb.construct(IEInvoiceHarborBillNoDetailReport.class, root.get("container_no"),
							root.get("ft_container"), product.get("id"), product.get("en_name"), productType.get("id"),
							productType.get("en_name"), product.get("unit"), product.get("specification"),
							product.get("tare"), product.get("factor"), root.get("quantity_export")))
					.where(root.get("id").in(param))
					.orderBy(cb.asc(root.get("container_no")), cb.asc(root.get("ft_container")),
							cb.asc(productType.get("en_name")), cb.asc(product.get("en_name")));
			TypedQuery<IEInvoiceHarborBillNoDetailReport> query = em.createQuery(cq);
			query.setParameter(param, listId);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEInvoiceHarborBillNoDetailReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectIEInvoiceCustomerBillNoDetailReport(String json, List<IEInvoiceCustomerBillNoDetailReport> list) {
		int res = -1;
		try {
			/* list_ie_invoice_detail_id:[]} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hListIdDetail = JsonParserUtil.getValueList(j, "list_ie_invoice_detail_id", null,
					new TypeToken<List<Long>>() {
					}.getType());
			List<Long> listId = (List<Long>) hListIdDetail.getValue();
			// (String container_no,long product_type_id, String
			// product_en_name, String product_type_en_name, String unit, double
			// specification, double tare,
			// double factor, double quantity_export)
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEInvoiceCustomerBillNoDetailReport> cq = cb
					.createQuery(IEInvoiceCustomerBillNoDetailReport.class);
			Root<IEInvoiceDetail> root = cq.from(IEInvoiceDetail.class);
			ParameterExpression<List> param = cb.parameter(List.class);
			Join<IEInvoiceDetail, Product> product = root.join("product", JoinType.INNER);
			Join<Product, ProductType> productType = product.join("product_type", JoinType.LEFT);
			cq.select(
					cb.construct(IEInvoiceCustomerBillNoDetailReport.class, root.get("container_no"),
							productType.get("id"), product.get("en_name"), productType.get("en_name"),
							product.get("unit"), product.get("specification"), product.get("tare"),
							product.get("factor"), root.get("quantity_export"),root.get("ie_invoice").get("vgm")))
					.where(root.get("id").in(param))
					.orderBy(cb.asc(productType.get("en_name")), cb.asc(product.get("en_name")),
							cb.asc(root.get("container_no")));
			TypedQuery<IEInvoiceCustomerBillNoDetailReport> query = em.createQuery(cq);
			query.setParameter(param, listId);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEInvoiceCustomerBillNoDetailReport:" + e.getMessage(), e);
		}
		return res;
	}

	@Override@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int reportFormD(String json, List<ReportFormD> listFormD) {
		int res = -1;
		try {
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hListIdDetail = JsonParserUtil.getValueList(j, "list_ie_invoice_detail_id", null,
					new TypeToken<List<Long>>() {
					}.getType());
			List<Long> listId = (List<Long>) hListIdDetail.getValue();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ReportFormD> cq = cb.createQuery(ReportFormD.class);
			Root<IEInvoiceDetail> root = cq.from(IEInvoiceDetail.class);
			Join<IEInvoiceDetail, Product> product_ = root.join("product", JoinType.INNER);
			Join<Product, ProductType> productType_ = product_.join("product_type", JoinType.LEFT);
			// (long product_type_id, String product_type_en_name, long
			// product_id, String product_en_name,Double total_cartons, Double
			// total_net_weight, Double total_gross_weight, Double
			// foreign_total_amount)
			// private Double total_cartons;//số lượng xuất khẩu/ specification
			// private Double total_net_weight;// quantity_export* factor/1000
			// (MTS)
			// private Double total_gross_weight;//
			// quantity_export/specification * tare (KGS)
			// private Double foreign_total_amount;//tổng số tiền ngoại tệ
			cq.select(
					cb.construct(
							ReportFormD.class,
							productType_.get("id"),
							productType_.get("en_name"),
							product_.get("id"),
							product_.get("en_name"),
							cb.quot(root.get("quantity_export"), product_.get("specification")),
							cb.quot(cb.prod(root.get("quantity_export"), product_.get("factor")), 1000),
							cb.quot(cb.prod(root.get("quantity_export"), product_.get("tare")),
									product_.get("specification")), root.get("total_foreign_amount")))
					.where(root.get("id").in(listId)).orderBy(cb.desc(productType_.get("en_name")));
			TypedQuery<ReportFormD> query = em.createQuery(cq);
			listFormD.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("IEInvocieService.reportFormD:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int searchProvidedFox(String json, List<Object[]> list) {
		int res = -1;
		try {
			/* {created_date:''} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hCreatedDate = JsonParserUtil.getValueString(j, "created_date", null);
			String createdDateStr = Objects.toString(hCreatedDate.getValue(), null);
			Date createdDate = ToolTimeCustomer.convertStringToDate(createdDateStr, "dd/MM/yyyy");
			if (createdDate != null) {
				Date fromDate = ToolTimeCustomer.getFirstDateOfDay(createdDate);
				Date toDate = ToolTimeCustomer.getLastDateOfDay(createdDate);
				StringBuilder sql = new StringBuilder();
				sql.append("select d.invoice_code as id,ie.old_code as maxn,d.voucher_code as soct,c.customer_code as makh, d.invoice_date as ngay,w.old_code as makho,cr.license_plate as soxe, ");
				sql.append("d.tax_value as hesothue,pm.old_code as htthanhtoa,ct.voucher_code as sohd,d.paid as dathtoan,d.exchange_rate as tigia,d.bill_no,d.declaration_code as tokhai_no, ");
				sql.append("hb.harbor_code_old as port_no, d.etd_date as etd,cy.currency_type as donvitien,i.invoice_code as idhoadon, s.stocker_code as matk,st.old_code as mabx,d.time_out as ngoaigio, ");
				sql.append("f.old_code as htbocxep,d.delivered as dagiaohang,d.shipped_per as shippedper,d.term_of_delivery as term_deliv, d.up_goods_date as load_date,hbx.harbor_code_old as portoftran, ");
				sql.append("d.freight,d.reference_no as ref_no,d.driver_name as taixe,d.note as ghichu, d.shipping_mark as shipmark ");
				sql.append("from ieinvoice as d ");
				sql.append("inner join iecategories as ie on d.ie_categories_id=d.id ");
				sql.append("inner join customer as c on d.customer_id=c.id ");
				sql.append("left join warehouse as w on d.warehouse_id=w.id ");
				sql.append("left join car as cr on d.car_id=cr.id ");
				sql.append("left join paymentmethod as pm on d.payment_method_id=pm.id ");
				sql.append("left join contract as ct on d.contract_id=ct.id ");
				sql.append("left join harborcategory as hb on d.harbor_category_id=hb.id ");
				sql.append("left join currency as cy on d.currency_id=cy.id ");
				sql.append("left join invoice  as i on d.invoice_id=i.id ");
				sql.append("left join stocker as s on d.stocker_id=s.id ");
				sql.append("left join stevedore as st on d.stevedore_id= st.id ");
				sql.append("left join formupgoods as f on d.form_up_goods_id=f.id ");
				sql.append("left join harborcategory as hbx on d.post_of_tran_id=hbx.id ");
				sql.append("where d.created_date>=:fd and d.created_date <=:td ");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("fd", ToolTimeCustomer.convertDateToString(fromDate, "yyyy-MM-dd HH:mm:ss"));
				query.setParameter("td", ToolTimeCustomer.convertDateToString(toDate, "yyyy-MM-dd HH:mm:ss"));
				list.addAll(query.getResultList());
				res = 0;
			}
		} catch (Exception e) {
			logger.error("IEInvoiceService.searchProvidedFox:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int getListIEInvoiceDetailProvidedFox(String json, List<Object[]> list) {
		int res = -1;
		try {
			/* {list_invoice_id:['00021092','00021098']} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hListInvoiceId = JsonParserUtil.getValueList(j, "list_invoice_id", null,
					new TypeToken<ArrayList<String>>() {
					}.getType());
			List<String> listData = (List<String>) hListInvoiceId.getValue();
			if (listData != null && listData.size() > 0) {
				StringBuilder sql = new StringBuilder();
				sql.append("select d.invoice_code as id,ie.old_code as maxn,d.voucher_code as soct,c.customer_code as makh, d.invoice_date as ngay,w.old_code as makho,cr.license_plate as soxe, ");
				sql.append("d.tax_value as hesothue,pm.old_code as htthanhtoa,ct.voucher_code as sohd,d.paid as dathtoan,d.exchange_rate as tigia,d.bill_no,d.declaration_code as tokhai_no, ");
				sql.append("hb.harbor_code_old as port_no, d.etd_date as etd,cy.currency_type as donvitien,i.invoice_code as idhoadon, s.stocker_code as matk,st.old_code as mabx,d.time_out as ngoaigio, ");
				sql.append("f.old_code as htbocxep,d.delivered as dagiaohang,d.shipped_per as shippedper,d.term_of_delivery as term_deliv, d.up_goods_date as load_date,hbx.harbor_code_old as portoftran, ");
				sql.append("d.freight,d.reference_no as ref_no,d.driver_name as taixe,d.note as ghichu, d.shipping_mark as shipmark ");
				sql.append("from ieinvoice as d ");
				sql.append("inner join iecategories as ie on d.ie_categories_id=d.id ");
				sql.append("inner join customer as c on d.customer_id=c.id ");
				sql.append("left join warehouse as w on d.warehouse_id=w.id ");
				sql.append("left join car as cr on d.car_id=cr.id ");
				sql.append("left join paymentmethod as pm on d.payment_method_id=pm.id ");
				sql.append("left join contract as ct on d.contract_id=ct.id ");
				sql.append("left join harborcategory as hb on d.harbor_category_id=hb.id ");
				sql.append("left join currency as cy on d.currency_id=cy.id ");
				sql.append("left join invoice  as i on d.invoice_id=i.id ");
				sql.append("left join stocker as s on d.stocker_id=s.id ");
				sql.append("left join stevedore as st on d.stevedore_id= st.id ");
				sql.append("left join formupgoods as f on d.form_up_goods_id=f.id ");
				sql.append("left join harborcategory as hbx on d.post_of_tran_id=hbx.id ");
				sql.append("where d.created_date>=:fd and d.created_date <=:td ");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("lst", listData);
				list.addAll(query.getResultList());
				res = 0;
			}
		} catch (Exception e) {
			logger.error("IEInvoiceService.getListIEInvoiceDetailProvidedFox:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public IEInvoice findByIdSafe(long id) {
		return em.find(IEInvoice.class, id);
	}

	@Override@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public IEInvoice findById(long id) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEInvoice> cq = cb.createQuery(IEInvoice.class);
			Root<IEInvoice> root = cq.from(IEInvoice.class);
			root.fetch("customer", JoinType.INNER);
			root.fetch("car", JoinType.LEFT);
			root.fetch("stocker", JoinType.LEFT);
			root.fetch("payment_method", JoinType.LEFT);
			root.fetch("ie_categories", JoinType.LEFT);
			root.fetch("contract", JoinType.LEFT);
			root.fetch("stevedore", JoinType.LEFT);
			root.fetch("form_up_goods", JoinType.LEFT);
			root.fetch("warehouse", JoinType.LEFT);
			root.fetch("harbor_category", JoinType.LEFT);
			root.fetch("currency", JoinType.LEFT);
			root.fetch("post_of_tran", JoinType.LEFT);
			root.fetch("invoice", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<IEInvoice> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			logger.error("IEInvoiceService.findById:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Object[]> selectAllExcel(List<Long> idinvoices) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select iev.id as idinv,iev.voucher_code as vch,iev.invoice_date,cus.customer_code,cus.customer_name, ");
			sql.append("car.license_plate,paym.method_name,iec.code,iev.note,iev.ie_reason,cte.content,cont.voucher_code,DATE_FORMAT(iev.up_goods_date, '%d/%m/%Y'),DATE_FORMAT(iev.ETD, '%d/%m/%Y'),DATE_FORMAT(iev.ETA, '%d/%m/%Y'),DATE_FORMAT(iev.duedate, '%d/%m/%Y'), ");
			sql.append("iev.paid,iev.delivered,iev.exchange_rate,iev.bill_no,iev.declaration_code,hb.harbor_name, ");
			sql.append("cr.currency_type,iev.reference_no,iev.shipped_per,iev.term_of_delivery,iev.shipping_mark, ");
			sql.append("iev.freight, iev.driver_name,iev.place_delivery,iev.place_discharge, ");
			sql.append("ied.id, pd.product_code,pd.product_name,ied.quantity_export,ied.total_foreign_amount,ied.foreign_unit_price,ied.total_export_foreign_amount, ");
			sql.append("ied.unit_price,ied.total_amount,ied.order_no,ied.container_no,ied.ft_container,ied.container_number,ied.seal_number,ied.batch_code,ied.arrival_place,ied.quantity_export/pd.specification,ied.quantity_export*pd.factor ");
			sql.append("from  ieinvoicedetail as ied ");
			sql.append("left join  product as pd on pd.id=ied.product_id ");
			sql.append("left join  ieinvoice as iev on iev.id=ied.ie_invoice_id ");
			sql.append("left join  currency as cr on cr.id=iev.currency_id ");
			sql.append("left join  customer as cus on cus.id=iev.customer_id ");
			sql.append("left join  car as car on car.id=iev.car_id ");
			sql.append("left join  PaymentMethod as paym on paym.id=iev.payment_method_id ");
			sql.append("left join  harborcategory as hb on hb.id=iev.harbor_category_id ");
			sql.append("left join  iecategories as iec on iec.id=iev.ie_categories_id ");
			sql.append("left join  contract as cont on cont.id=iev.contract_id ");
			sql.append("left join  stevedore as cte on cte.id=iev.stevedore_id ");

			sql.append("where  iev.id in :idinvoices ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("idinvoices", idinvoices);
			List<Object[]> listObj = query.getResultList();
			return listObj;

		} catch (Exception e) {
			logger.error("IEInvoiceService.selectIEExportReport:" + e.getMessage(), e);
		}
		return new ArrayList<Object[]>();
	}

}
