package lixco.com.service;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lixco.com.entity.CotHienThi;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class CotHienThiService extends AbstractService<CotHienThi> {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public CotHienThi find(String tenbang, String taikhoan) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CotHienThi> cq = cb.createQuery(CotHienThi.class);
			Root<CotHienThi> root = cq.from(CotHienThi.class);
			cq.select(root).where(cb.equal(root.get("tenbang"), tenbang), cb.equal(root.get("taikhoan"), taikhoan));
			return em.createQuery(cq).getSingleResult();
		} catch (Exception e) {
		}
		return null;
	}
	public void saveOrUpdate(String cothienthi,String tenbang,String taikhoan){
		CotHienThi cht=find(tenbang, taikhoan);
		if(cht!=null){
			cht.setCothienthi(cothienthi);;
			em.merge(cht);
		}else{
			cht=new CotHienThi();
			cht.setCreated_date(new Date());
			cht.setTenbang(tenbang);
			cht.setTaikhoan(taikhoan);
			cht.setCothienthi(cothienthi);
			em.persist(cht);
		}
	}

	@Override
	protected Class<CotHienThi> getEntityClass() {
		return CotHienThi.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

}
