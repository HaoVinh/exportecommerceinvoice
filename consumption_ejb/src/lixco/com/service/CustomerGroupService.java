package lixco.com.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lixco.com.entity.CustomerGroup;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class CustomerGroupService {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<CustomerGroup> selectAll() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CustomerGroup> cq = cb.createQuery(CustomerGroup.class);
			Root<CustomerGroup> root = cq.from(CustomerGroup.class);
			cq.select(root);
			TypedQuery<CustomerGroup> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CustomerGroup>();
	}
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public CustomerGroup findByCode(String code) {
		try {
			if (code != null && !"".equals(code)) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<CustomerGroup> cq = cb.createQuery(CustomerGroup.class);
				Root<CustomerGroup> root = cq.from(CustomerGroup.class);
				cq.select(root).where(cb.equal(root.get("code"), code));
				TypedQuery<CustomerGroup> query = em.createQuery(cq);
				return query.getSingleResult();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public CustomerGroup findByName(String name) {
		try {
			if (name != null && !"".equals(name)) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<CustomerGroup> cq = cb.createQuery(CustomerGroup.class);
				Root<CustomerGroup> root = cq.from(CustomerGroup.class);
				cq.select(root).where(cb.equal(root.get("name"), name));
				TypedQuery<CustomerGroup> query = em.createQuery(cq);
				return query.getSingleResult();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int insert(CustomerGroup p) {
		int res = -1;
		try {
			if (p != null) {
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public int update(CustomerGroup p) {
		int res = -1;
		try {
			if (p != null) {
				p = em.merge(p);
				if (p != null) {
					res = 0;
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return res;
	}

}
