package lixco.com.service;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import lixco.com.entity.MyLogDMS;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class MyLogDMSService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;
	
	public int create(MyLogDMS p) {
		int res = -1;
		try {
			if (p != null) {
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("MyLogDMSService.insert:" + e.getMessage(), e);
		}
		return res;
	}

}
