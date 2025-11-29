package lixco.com.service;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import lixco.com.entity.BienNhanVanChuyen;
import lixco.com.entity.FreightContract;
import lixco.com.reqInfo.FreightContractReqInfo;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class BienNhanVanChuyenService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	
	public int insert(BienNhanVanChuyen t) {
		int res = -1;
		try {
			if (t != null) {
				em.persist(t);
				if (t.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("FreightContractService.insert:" + e.getMessage(), e);
		}
		return res;
	}

}
