package lixco.com.service;

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

import lixco.com.ejb.util.StaticValue;
import lixco.com.entity.CotHienThi;
import lixco.com.reportInfo.TonKhoThang;

import org.jboss.logging.Logger;

import trong.lixco.com.entity.TonKhoThucTe;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class TonKhoThucTeService extends AbstractService<TonKhoThucTe> {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	public TonKhoThucTe findIdSP(long idsp) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TonKhoThucTe> cq = cb.createQuery(TonKhoThucTe.class);
			Root<TonKhoThucTe> root = cq.from(TonKhoThucTe.class);
			cq.select(root).where(cb.equal(root.get("idsp"), idsp));
			return em.createQuery(cq).getSingleResult();
		} catch (Exception e) {
		}
		return null;
	}

	public void saveOrUpdate(List<TonKhoThang> tonKhoThangs) {
		for (int i = 0; i < tonKhoThangs.size(); i++) {
			TonKhoThucTe ttt = findIdSP(tonKhoThangs.get(i).getProduct_id());
			if (ttt != null) {
				ttt.setTon(Math.round(tonKhoThangs.get(i).getKg_closing_balance() / tonKhoThangs.get(i).getFactor()));
				em.merge(ttt);
			} else {
				ttt = new TonKhoThucTe(tonKhoThangs.get(i).getProduct_id(), tonKhoThangs.get(i)
						.getProduct_code(), tonKhoThangs.get(i).getProduct_name(), Math.round(tonKhoThangs.get(i)
						.getKg_closing_balance() / tonKhoThangs.get(i).getFactor()));
				em.persist(ttt);
			}
		}
	}
	public double kiemton(long idsp) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Double> cq = cb.createQuery(Double.class);
			Root<TonKhoThucTe> root = cq.from(TonKhoThucTe.class);
			cq.select(root.get("ton")).where(cb.equal(root.get("idsp"), idsp));
			double tonkho= em.createQuery(cq).getSingleResult();
			return tonkho+StaticValue.CHENHLECHTON;
		} catch (Exception e) {
		}
		return 0;
	}


	@Override
	protected Class<TonKhoThucTe> getEntityClass() {
		return TonKhoThucTe.class;
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
