package lixco.com.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import lixco.com.entity.Customer;
import lixco.com.entity.GoodsReceiptNote;
import lixco.com.entity.GoodsReceiptNoteDetail;
import lixco.com.entity.IECategories;
import lixco.com.entity.Product;
import lixco.com.entityapi.GoodsReceiptNoteDTO;
import lixco.com.entityapi.GoodsReceiptNoteDetailDTO;

@Stateless
@TransactionManagement(value = TransactionManagementType.BEAN)
public class GoodsReceiptNoteAPIService {
	@Inject
	private EntityManager em;
	@Resource(lookup = "java:/consumption")
	DataSource datasource;
	@Resource
	private UserTransaction ut;
	public List<GoodsReceiptNoteDTO> search(Date startDate, Date endDate, String maxuatnhap,String idloaibo) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<GoodsReceiptNoteDTO> cq = cb.createQuery(GoodsReceiptNoteDTO.class);
			Root<GoodsReceiptNote> root_ = cq.from(GoodsReceiptNote.class);
			Join<GoodsReceiptNote, Customer> customer_ = root_.join("customer", JoinType.LEFT);
			Join<GoodsReceiptNote, IECategories> iECategories_ = root_.join("ie_categories", JoinType.LEFT);
			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(cb.greaterThanOrEqualTo(root_.get("import_date"), startDate));
			predicates.add(cb.lessThanOrEqualTo(root_.get("import_date"), endDate));

			if (maxuatnhap != null && !"".equals(maxuatnhap.trim())) {
				String maxnArray[] = maxuatnhap.split(";");
				if (maxnArray.length != 0) {
					List<String> maxns = Arrays.asList(maxnArray);
					predicates.add(cb.in(iECategories_.get("code")).value(maxns));
				}
			}
			if (idloaibo != null && !"".equals(idloaibo.trim())) {
				String idloaiboArray[] = idloaibo.split(";");
				if (idloaiboArray.length != 0) {
					List<Long> idlbs=new ArrayList<Long>();
					for (int i = 0; i < idloaiboArray.length; i++) {
						idlbs.add(Long.parseLong(idloaiboArray[i]));
					}
					predicates.add(cb.in(root_.get("id")).value(idlbs).not());
				}
			}
			cq.select(
					cb.construct(GoodsReceiptNoteDTO.class, root_.get("id"), root_.get("voucher_code"),root_.get("import_date"),
							customer_.get("customer_code"), customer_.get("customer_name"),  iECategories_.get("code"),
							iECategories_.get("content")))
					.where(cb.and(predicates.toArray(new Predicate[0])))
					.orderBy(cb.asc(root_.get("import_date")), cb.asc(root_.get("id")));

			TypedQuery<GoodsReceiptNoteDTO> query = em.createQuery(cq);

			List<GoodsReceiptNoteDTO> goodsReceiptNoteDTOs = query.getResultList();
			for (int i = 0; i < goodsReceiptNoteDTOs.size(); i++) {
				goodsReceiptNoteDTOs.get(i).setGoodsReceiptNoteDetailDTOs(findGoodsReceiptNoteDetailDTO(goodsReceiptNoteDTOs.get(i).getId()));
			}
			return goodsReceiptNoteDTOs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<GoodsReceiptNoteDTO>();
	}

	public List<GoodsReceiptNoteDetailDTO> findGoodsReceiptNoteDetailDTO(long idGr) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<GoodsReceiptNoteDetailDTO> cq = cb.createQuery(GoodsReceiptNoteDetailDTO.class);
			Root<GoodsReceiptNoteDetail> root_ = cq.from(GoodsReceiptNoteDetail.class);
			Join<GoodsReceiptNoteDetail, Product> product_ = root_.join("product", JoinType.LEFT);
			cq.select(
					cb.construct(GoodsReceiptNoteDetailDTO.class, root_.get("id"), product_.get("product_code"),
							product_.get("product_name"), root_.get("quantity"), root_.get("total"),
							root_.get("vcnb_invoice_code")))
					.where(cb.equal(root_.get("goods_receipt_note").get("id"), idGr));
			TypedQuery<GoodsReceiptNoteDetailDTO> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<GoodsReceiptNoteDetailDTO>();
	}

}
