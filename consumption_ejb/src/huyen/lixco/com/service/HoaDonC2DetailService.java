package huyen.lixco.com.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lixco.com.delivery.Product;
import lixco.com.entity.HoaDonC2;
import lixco.com.entity.HoaDonChiTietC2;
import trong.lixco.com.service.AbstractService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class HoaDonC2DetailService extends AbstractService<HoaDonChiTietC2> {

	@Inject
	private EntityManager em;

	@Resource
	private SessionContext ct;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	@Override
	protected Class<HoaDonChiTietC2> getEntityClass() {
		return HoaDonChiTietC2.class;
	}

	public List<HoaDonChiTietC2> findByHoaDonC2Id(Long hoaDonC2Id) {
	    CriteriaBuilder cb = em.getCriteriaBuilder();
	    CriteriaQuery<HoaDonChiTietC2> cq = cb.createQuery(HoaDonChiTietC2.class);
	    Root<HoaDonChiTietC2> root = cq.from(HoaDonChiTietC2.class);

	    cq.select(root).where(cb.equal(root.get("hoaDonC2").get("id"), hoaDonC2Id));

	    return em.createQuery(cq).getResultList();
	}

}
