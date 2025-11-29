package lixco.com.service;

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

import org.jboss.logging.Logger;

import lixco.com.entity.BindUser;
import lixco.com.interfaces.IBindUserService;
@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class BindUserService implements IBindUserService{
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;
	@Override
	public BindUser getBindUser(long memberId) {
		try{
			CriteriaBuilder cb=em.getCriteriaBuilder();
			CriteriaQuery<BindUser> cq=cb.createQuery(BindUser.class);
			Root<BindUser> root=cq.from(BindUser.class);
			cq.select(root).where(cb.equal(root.get("member_id"), memberId));
			TypedQuery<BindUser> query=em.createQuery(cq);
			List<BindUser> list=query.getResultList();
			if(list.size()>0){
				return list.get(0);
			}
		}catch (Exception e) {
			logger.error("BindUserService.getBindUser:"+e.getMessage(),e);
		}
		return null;
	}
	@Override
	public BindUser getBindUserByIdFox(String idfox) {
		try{
			CriteriaBuilder cb=em.getCriteriaBuilder();
			CriteriaQuery<BindUser> cq=cb.createQuery(BindUser.class);
			Root<BindUser> root=cq.from(BindUser.class);
			cq.select(root).where(cb.equal(root.get("idfox"), idfox));
			TypedQuery<BindUser> query=em.createQuery(cq);
			List<BindUser> list=query.getResultList();
			if(list.size()>0){
				return list.get(0);
			}
		}catch (Exception e) {
			logger.error("BindUserService.getBindUser:"+e.getMessage(),e);
		}
		return null;
	}

}
