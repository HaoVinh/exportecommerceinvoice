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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import lixco.com.entity.Product;
import lixco.com.entity.YeuCauKiemTraHangDetail;
import lixco.com.entityapi.YeuCauKiemTraHangDetailDTO;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class YeuCauKiemTraHangDetailService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int insert(YeuCauKiemTraHangDetail p) {
		int res = -1;
		try {
			if (p != null) {
				em.persist(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int update(YeuCauKiemTraHangDetail p) {
		int res = -1;
		try {
			if (p != null) {
				p = em.merge(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public YeuCauKiemTraHangDetail findById(long id) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<YeuCauKiemTraHangDetail> cq = cb.createQuery(YeuCauKiemTraHangDetail.class);
			Root<YeuCauKiemTraHangDetail> root_ = cq.from(YeuCauKiemTraHangDetail.class);
			root_.fetch("product", JoinType.INNER);
			root_.fetch("yeuCauKiemTraHang", JoinType.INNER);
			cq.select(root_).where(cb.equal(root_.get("id"), id));
			TypedQuery<YeuCauKiemTraHangDetail> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
		}
		return null;
	}

	public int deleteById(long id) {
		int res = -1;
		try {
			Query query = em.createQuery("delete from YeuCauKiemTraHangDetail where id=:id");
			query.setParameter("id", id);
			res = query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<YeuCauKiemTraHangDetail> findByYeuCauKiemTraHang(long importId) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<YeuCauKiemTraHangDetail> cq = cb.createQuery(YeuCauKiemTraHangDetail.class);
			Root<YeuCauKiemTraHangDetail> root_ = cq.from(YeuCauKiemTraHangDetail.class);
			root_.fetch("product", JoinType.INNER);
			root_.fetch("yeuCauKiemTraHang", JoinType.INNER);
			cq.select(root_).where(cb.equal(root_.get("yeuCauKiemTraHang").get("id"), importId));
			TypedQuery<YeuCauKiemTraHangDetail> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<YeuCauKiemTraHangDetail>();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<YeuCauKiemTraHangDetailDTO> findByYeuCauKiemTraHangDTO(long importId) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<YeuCauKiemTraHangDetailDTO> cq = cb.createQuery(YeuCauKiemTraHangDetailDTO.class);
			Root<YeuCauKiemTraHangDetail> root_ = cq.from(YeuCauKiemTraHangDetail.class);
			Join<YeuCauKiemTraHangDetail, Product> product_ = root_.join("product", JoinType.LEFT);
			cq.select(
					cb.construct(YeuCauKiemTraHangDetailDTO.class, root_.get("id"),product_.get("product_code"),
							product_.get("product_name"), product_.get("unit"),
							product_.get("packing_unit"), product_.get("specification"),
							root_.get("quantity"), root_.get("lohang"), root_.get("nguongoc"), root_.get("tinhtrang"),
							root_.get("kiemtradat"),root_.get("tieuchuan"),root_.get("nguyennhan"),root_.get("huonggiaiquyet"),root_.get("giahanluukho"),root_.get("huonggiaiquyet2"))).where(cb.equal(root_.get("yeuCauKiemTraHang").get("id"), importId));
			TypedQuery<YeuCauKiemTraHangDetailDTO> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<YeuCauKiemTraHangDetailDTO>();
	}

}
