package lixco.com.service;

import org.jboss.logging.Logger;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lixco.com.entity.EcomOrder;
import lixco.com.entity.EcomOrderDetail;
import lixco.com.entity.ShopeeToken;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ShopeeTokenService extends AbstractService<ShopeeToken> {
	protected final Logger logger = Logger.getLogger(getClass());
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<ShopeeToken> getEntityClass() {
		// TODO Auto-generated method stub
		return ShopeeToken.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected SessionContext getUt() {
		// TODO Auto-generated method stub
		return ct;
	}
    public ShopeeToken getLatestToken() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ShopeeToken> cq = cb.createQuery(ShopeeToken.class);
        Root<ShopeeToken> root = cq.from(ShopeeToken.class);
        cq.select(root);
        List<ShopeeToken> result = em.createQuery(cq).setMaxResults(1).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
	
}
