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

import lixco.com.entity.Driver;
import lixco.com.entity.DriverIEIV;
import lixco.com.entity.IEInvoice;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class DriverIEIVService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	public List<Driver> findByIEInv(long idIEInv) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Driver> cq = cb.createQuery(Driver.class);
			Root<DriverIEIV> root = cq.from(DriverIEIV.class);
			cq.select(root.get("driver")).where(cb.equal(root.get("ieInvoice").get("id"), idIEInv));
			TypedQuery<Driver> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("DriverIEIVService.findByIEInv:" + e.getMessage(), e);
		}
		return new ArrayList<Driver>();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean saveOrUpdate(List<Driver> drivers, IEInvoice ieInvoice) {
		try {
			Query query = em.createQuery("DELETE FROM DriverIEIV WHERE ieInvoice_id = :idIEInv");
			query.setParameter("idIEInv", ieInvoice);
			query.executeUpdate();
			if(drivers!=null)
			for (Driver driver : drivers) {
				DriverIEIV driverIEIV=new DriverIEIV();
				driverIEIV.setDriver(driver);
				driverIEIV.setIeInvoice(ieInvoice);
				em.persist(driverIEIV);

			}
		} catch (Exception e) {
			ct.setRollbackOnly();
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
