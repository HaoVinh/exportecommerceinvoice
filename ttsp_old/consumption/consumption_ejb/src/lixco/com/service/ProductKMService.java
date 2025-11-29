package lixco.com.service;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import lixco.com.entity.ProductKM;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class ProductKMService {
	@Inject
	private EntityManager em;
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ProductKM> findByIdProductMain(Long idProduct) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ProductKM> cq = cb.createQuery(ProductKM.class);
			Root<ProductKM> root = cq.from(ProductKM.class);
			root.fetch("promotion_product", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("product").get("id"), idProduct));
			TypedQuery<ProductKM> query = em.createQuery(cq);
			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ProductKM>();
	}
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ProductKM> findByCodeProductMain(String productCode) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ProductKM> cq = cb.createQuery(ProductKM.class);
			Root<ProductKM> root = cq.from(ProductKM.class);
			root.fetch("promotion_product", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("product").get("product_code"), productCode));
			TypedQuery<ProductKM> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ProductKM>();
	}

	public int deleteById(long id) {
		int res = -1;
		try {
			Query query = em.createQuery("delete from ProductKM where id=:id ");
			query.setParameter("id", id);
			res = query.executeUpdate();
		} catch (Exception e) {
		}
		return res;
	}
}
