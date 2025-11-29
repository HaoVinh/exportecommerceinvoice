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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.entity.Car;
import lixco.com.entity.Customer;
import lixco.com.entity.Driver;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class DriverService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Driver> selectAll() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Driver> cq = cb.createQuery(Driver.class);
			Root<Driver> root = cq.from(Driver.class);
			cq.select(root);
			TypedQuery<Driver> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Driver>();
	}
	public int insert(Driver p) {
		int res = -1;
		try {
			if (p != null) {
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("DriverService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	public int update(Driver p) {
		int res = -1;
		try {
			if (p != null) {
				p = em.merge(p);
				if (p != null) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("DriverService.update:" + e.getMessage(), e);
		}
		return res;
	}

	public Driver findById(long id) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Driver> cq = cb.createQuery(Driver.class);
			Root<Driver> root = cq.from(Driver.class);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<Driver> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			logger.error("DriverService.selectById:" + e.getMessage(), e);
		}
		return null;
	}

	public int deleteById(long id) {
		int res = -1;
		try {
			// JQPL
			Query query = em.createQuery("delete from Driver where id=:id ");
			query.setParameter("id", id);
			res = query.executeUpdate();
		} catch (Exception e) {
			logger.error("DriverService.deleteById:" + e.getMessage(), e);
		}
		return res;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int complete(String text, List<Driver> list) {
		int res = -1;
		try {
			if (text != null && !"".equals(text)) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Driver> cq = cb.createQuery(Driver.class);
				Root<Driver> root = cq.from(Driver.class);
				Predicate dis = cb.disjunction();
				dis.getExpressions().add(cb.like(root.get("name"), "%" + text + "%"));
				dis.getExpressions().add(cb.like(root.get("phone"), "%" + text + "%"));
				dis.getExpressions().add(cb.like(root.get("cccd"), "%" + text + "%"));
				cq.select(root).where(dis);
				TypedQuery<Driver> query = em.createQuery(cq);
				list.addAll(query.getResultList());
			}
			res = 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
