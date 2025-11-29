package lixco.com.service;

import java.util.ArrayList;
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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.Carrier;
import lixco.com.interfaces.ICarrierService;
import lixco.com.reqInfo.CarrierReqInfo;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class CarrierService implements ICarrierService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@Override
	public int selectAll(List<Carrier> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Carrier> cq = cb.createQuery(Carrier.class);
			Root<Carrier> root = cq.from(Carrier.class);
			cq.select(root);
			TypedQuery<Carrier> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("CarrierService.selectAll:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int insert(CarrierReqInfo t) {
		int res = -1;
		try {
			Carrier p = t.getCarrier();
			if (p != null) {
				if (p.getCarrier_code() == null)
					p.setCarrier_code(initCode());
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("CarrierService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int update(CarrierReqInfo t) {
		int res = -1;
		try {
			Carrier p = t.getCarrier();
			if (p != null) {
				p = em.merge(p);
				if (p != null) {
					selectById(p.getId(), t);
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("CarrierService.update:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectById(long id, CarrierReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Carrier> cq = cb.createQuery(Carrier.class);
			Root<Carrier> root = cq.from(Carrier.class);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<Carrier> query = em.createQuery(cq);
			t.setCarrier(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			logger.error("CarrierService.selectById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int deleteById(long id) {
		int res = -1;
		try {
			// JQPL
			Query query = em.createQuery("delete from Carrier where id=:id ");
			query.setParameter("id", id);
			res = query.executeUpdate();
		} catch (Exception e) {
			logger.error("CarrierService.deleteById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int search(String json, List<Carrier> list) {
		int res = -1;
		try {
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hCarrierCode = JsonParserUtil.getValueString(j.get("carrier_info"), "carrier_code", null);
			HolderParser hCarrierName = JsonParserUtil.getValueString(j.get("carrier_info"), "carrier_name", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Carrier> cq = cb.createQuery(Carrier.class);
			Root<Carrier> root_ = cq.from(Carrier.class);
			List<Predicate> predicates = new ArrayList<Predicate>();
			ParameterExpression<String> pCarrierCode = cb.parameter(String.class);
			ParameterExpression<String> pCarrierName = cb.parameter(String.class);
			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.equal(pCarrierCode, ""));
			dis.getExpressions().add(cb.equal(root_.get("carrier_code"), pCarrierCode));
			predicates.add(dis);
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.equal(pCarrierName, ""));
			dis1.getExpressions().add(cb.equal(root_.get("carrier_name"), pCarrierName));
			predicates.add(dis1);
			cq.select(root_).where(cb.and(predicates.toArray(new Predicate[0]))).orderBy(cb.desc(root_.get("id")));
			TypedQuery<Carrier> query = em.createQuery(cq);
			query.setParameter(pCarrierCode, Objects.toString(hCarrierCode.getValue(), ""));
			query.setParameter(pCarrierName, Objects.toString(hCarrierName.getValue(), ""));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("CarrierService.search:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int complete(String text, List<Carrier> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Carrier> cq = cb.createQuery(Carrier.class);
			Root<Carrier> root = cq.from(Carrier.class);
			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.equal(root.get("carrier_code"), text));
			dis.getExpressions().add(cb.like(root.get("carrier_name"), "%" + text + "%"));
			cq.select(cb.construct(Carrier.class, root.get("id"), root.get("carrier_code"), root.get("carrier_name")))
					.where(dis);
			TypedQuery<Carrier> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("CarrierService.complete:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectByCode(String code, CarrierReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Carrier> cq = cb.createQuery(Carrier.class);
			Root<Carrier> root = cq.from(Carrier.class);
			cq.select(root).where(cb.equal(root.get("carrier_code"), code));
			TypedQuery<Carrier> query = em.createQuery(cq);
			t.setCarrier(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			// logger.error("CarrierService.selectByCode:"+e.getMessage(),e);
		}
		return res;
	}

	private String initCode() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
			Root<Carrier> root = cq.from(Carrier.class);
			// cq.multiselect(cb.coalesce(cb.max(root.get("id")),0));
			cq.multiselect(cb.coalesce(cb.max(cb.quot((Expression) root.get("carrier_code"), 1)), 0));
			TypedQuery<Integer> query = em.createQuery(cq);
			int max = query.getSingleResult();
			double p = (double) max / 1000;
			if (p < 1) {
				return String.format("%03d", max + 1);
			}
			return "" + max + 1;
		} catch (Exception e) {
			// logger.error("CarrierService.initCode:"+e.getMessage(),e);
		}
		return null;
	}

}
