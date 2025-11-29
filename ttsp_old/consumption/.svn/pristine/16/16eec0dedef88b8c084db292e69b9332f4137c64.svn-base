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
import lixco.com.entity.GoodsImportBreak;
import lixco.com.entity.GoodsImportBreakDetail;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class GoodsImportBreakService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	public int search(String json, List<GoodsImportBreak> list) {
		int res = -1;
		try {
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);

			HolderParser hstextStr = JsonParserUtil.getValueString(j.get("goodsImportBreak"), "stextStr", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j.get("goodsImportBreak"), "customer_id", null);
			HolderParser hFromDate = JsonParserUtil.getValueString(j.get("goodsImportBreak"), "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j.get("goodsImportBreak"), "to_date", null);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<GoodsImportBreak> cq = cb.createQuery(GoodsImportBreak.class);
			Root<GoodsImportBreak> root_ = cq.from(GoodsImportBreak.class);
			root_.fetch("customer", JoinType.LEFT);

			ParameterExpression<Date> pFromDate = cb.parameter(Date.class);
			ParameterExpression<Date> pToDate = cb.parameter(Date.class);
			ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
			List<Predicate> predicates = new ArrayList<Predicate>();
			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.isNull(pFromDate));
			dis.getExpressions().add(cb.greaterThanOrEqualTo(root_.get("importDate"), pFromDate));
			predicates.add(dis);
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.isNull(pToDate));
			dis1.getExpressions().add(cb.lessThanOrEqualTo(root_.get("importDate"), pToDate));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.equal(pCustomerId, 0));
			dis2.getExpressions().add(cb.equal(root_.get("customer").get("id"), pCustomerId));
			predicates.add(dis2);

			List<Predicate> predicatesStr = new ArrayList<Predicate>();

			String stextStr = Objects.toString(hstextStr.getValue());
			if (hstextStr.getValue() != null && !"".equals(stextStr.trim())) {
				Predicate predicatevoucher_code = cb.like(root_.get("importCode"), "%" + stextStr + "%");// so
				predicatesStr.add(predicatevoucher_code);

				Join<GoodsImportBreak, Customer> cus_ = root_.join("customer", JoinType.LEFT);
				Predicate predicateCuscode = cb.like(cus_.get("customer_code"), "%" + stextStr + "%");// ma
				predicatesStr.add(predicateCuscode);
				Predicate predicateCus = cb.like(cus_.get("customer_name"), "%" + stextStr + "%");// ten
				predicatesStr.add(predicateCus);

				Predicate predicatecreated_by = cb.like(root_.get("createdBy"), "%" + stextStr + "%");// nguoi
				predicatesStr.add(predicatecreated_by);

				List<Predicate> subpredicates = new LinkedList<Predicate>();
				Subquery<GoodsImportBreakDetail> sqOne = cq.subquery(GoodsImportBreakDetail.class);
				Root subroot = sqOne.from(GoodsImportBreakDetail.class);
				Predicate predicatePdCode = cb.like(subroot.get("product").get("product_code"), "%" + stextStr + "%");
				subpredicates.add(predicatePdCode);
				Predicate predicatePdName = cb.like(subroot.get("product").get("product_name"), "%" + stextStr + "%");
				subpredicates.add(predicatePdName);
				sqOne.select(subroot.get("goodsImportBreak")).where(cb.or(subpredicates.toArray(new Predicate[0])));
				Predicate predicateSub = cb.equal(root_, cb.any(sqOne));
				predicatesStr.add(predicateSub);// truy cap chi tiet
			}
			if (predicatesStr.size() != 0) {
				cq.select(root_)
						.where(cb.and(predicates.toArray(new Predicate[0])),
								cb.or(predicatesStr.toArray(new Predicate[0])))
						.orderBy(cb.desc(root_.get("importDate")));
			} else {
				cq.select(root_).where(cb.and(predicates.toArray(new Predicate[0])))
						.orderBy(cb.desc(root_.get("importDate")));
			}
			TypedQuery<GoodsImportBreak> query = em.createQuery(cq);
			query.setParameter(pFromDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pToDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue(), "0")));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public int insert(GoodsImportBreak p) {
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

	public int update(GoodsImportBreak p) {
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

	public GoodsImportBreak findById(long id) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<GoodsImportBreak> cq = cb.createQuery(GoodsImportBreak.class);
			Root<GoodsImportBreak> root = cq.from(GoodsImportBreak.class);
			root.fetch("customer", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<GoodsImportBreak> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int deleteById(long id) {
		int res = -1;
		try {
			GoodsImportBreak t = em.find(GoodsImportBreak.class, id);
			if (t != null) {
				em.remove(t);
				res = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	public int selectByIdToExcel(List<Long> receiptIds, List<Object[]> list) {
		int res = -1;
		try {
//			Object[] title = { "ID", "Số CT", "Ngày", "Mã KH","Tên KH", "X/N","Số xe","Ghi chú", "IDCT", "Mã SP", "Tên SP",
//					"Số lượng","Số lượng (kg)", "Đơn giá", "Thành tiền","Mã lô hàng", "Hệ số quy đổi" };
			if (receiptIds.size() != 0) {
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT ").append("i.id, ").append("i.importCode, ").append("i.importDate, ")
						.append("c.customer_code, ").append("c.customer_name, ").append("i.loaiPhieu, ").append("i.soxe, ").append("i.note, ").append("ind.id as idct, ")
						.append("pd.product_code, ").append("pd.product_name, ").append("ind.quantity, ").append("ind.quantity*pd.factor, ")
						.append("ind.price, ").append("ind.total, ").append(" '', ").append("pd.factor ");
						

				sql.append("FROM ").append("GoodsImportBreak AS i ")
						.append(" LEFT JOIN ")
						.append("GoodsImportBreakDetail AS ind ON i.id = ind.goodsImportBreak_id ")
						.append("LEFT JOIN ").append("customer AS c ON c.id = i.customer_id ")
						.append("LEFT JOIN ")
						.append("product AS pd ON pd.id = ind.product_id ");
				sql.append("WHERE ").append("i.id IN :idInvs");

				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("idInvs", receiptIds);
				list.addAll(query.getResultList());
				res = 0;
			}
		} catch (Exception e) {
			logger.error("GoodsReceiptNote.selectByIdToExcel:" + e.getMessage(), e);
		}
		return res;
	}
	
}
