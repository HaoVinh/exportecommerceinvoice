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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lixco.com.entity.Car;
import lixco.com.entity.CarIEIV;
import lixco.com.entity.IEInvoice;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class CarIEIVService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	public List<Car> findByIEInv(long idIEInv) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Car> cq = cb.createQuery(Car.class);
			Root<CarIEIV> root = cq.from(CarIEIV.class);
			cq.select(root.get("car")).where(cb.equal(root.get("ieInvoice").get("id"), idIEInv));
			TypedQuery<Car> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("CarIEIVService.findByIEInv:" + e.getMessage(), e);
		}
		return new ArrayList<Car>();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean saveOrUpdate(List<Car> cars, IEInvoice ieInvoice) {
		try {
			Query query = em.createQuery("DELETE FROM CarIEIV WHERE ieInvoice_id = :idIEInv");
			query.setParameter("idIEInv", ieInvoice.getId());
			query.executeUpdate();
			if (cars != null)
				for (Car car : cars) {
					CarIEIV carIEIV = new CarIEIV();
					carIEIV.setCar(car);
					carIEIV.setIeInvoice(ieInvoice);
					em.persist(carIEIV);
				}
		} catch (Exception e) {
			ct.setRollbackOnly();
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
