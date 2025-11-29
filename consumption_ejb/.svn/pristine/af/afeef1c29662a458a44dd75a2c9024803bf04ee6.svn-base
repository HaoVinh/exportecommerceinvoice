package lixco.com.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import lixco.com.entity.Batch;
import lixco.com.entity.ExportBatch;
import lixco.com.entity.ExportBatchOD;
import lixco.com.entity.ExportBatchPOD;
import lixco.com.entity.Product;
import lixco.com.interfaces.IBatchService;
import lixco.com.reqInfo.BatchReqInfo;
import lixco.com.reqInfo.ExportBatchODFake;
import lixco.com.reqInfo.ExportBatchPODFake;
import lixco.com.reqInfo.ExportBatchODFake.BatchFake;
import lixco.com.reqInfo.ExportBatchPODFake.BatchPODFake;
@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class BatchService implements IBatchService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;
	@Override
	public int insert(BatchReqInfo t) {
		int res=-1;
		try{
			Batch p=t.getBatch();
			if(p !=null){
				em.persist(p);
				if(p.getId() >0){
					selectById(p.getId(), t);
					res=0;
				}
			}
		}catch(Exception e){
			logger.error("BatchService.insert:"+e.getMessage(),e);
		}
		return res;
	}

	@Override
	public int update(BatchReqInfo t) {
		int res=-1;
		try{
			Batch p=t.getBatch();
			if(p !=null){
				p=em.merge(p);
				if(p !=null){
					selectById(p.getId(), t);
					res=0;
				}
			}
		}catch(Exception e){
			logger.error("BatchService.update:"+e.getMessage(),e);
		}
		return res;
	}

	@Override
	public int deleteById(long id) {
		int res=-1;
		try{
			//JPQl
			Query query=em.createQuery("delete from Batch where id= :id");
			res=query.executeUpdate();
		}catch(Exception e){
			logger.error("BatchService.deleteById:"+e.getMessage(),e);
		}
		return res;
	}

	@Override
	public int exportAutoBatch(long productId,double quantity) {
		int res=-1;
		try{
			//tìm lô hàng nào có ngày sản xuất nhỏ nhất
//			CriteriaBuilder cb=em.getCriteriaBuilder();
//			CriteriaQuery<Batch> cq=cb.createQuery(Batch.class);
//			Root<Batch> root= cq.from(Batch.class);
//			cq.select(root).where(arg0)
			
		}catch(Exception e){
			logger.error("BatchService.exportAutoBatch:"+e.getMessage(),e);
		}
		return res;
	}

	@Override
	public int exportManualBatch(List<Batch> list) {
		int res=-1;
		try{
			
		}catch (Exception e) {
			logger.error("BatchService.exportManualBatch:"+e.getMessage(),e);
		}
		return res;
	}

	@Override
	public int selectById(long id,BatchReqInfo t) {
		int res=-1;
		try{
			CriteriaBuilder cb=em.getCriteriaBuilder();
			CriteriaQuery<Batch> cq=cb.createQuery(Batch.class);
			Root<Batch> root= cq.from(Batch.class);
			root.fetch("product",JoinType.INNER);
			cq.select(root).where(cb.equal(root.get("id"),id));
			TypedQuery<Batch> query=em.createQuery(cq);
			t.setBatch(query.getSingleResult());
			res=0;
		}catch (Exception e) {
			logger.error("BatchService.insert:"+e.getMessage(),e);
		}
		return res;
	}
	@Override
	public BatchFake selectByIdFake(long id) {
		try{
			CriteriaBuilder cb=em.getCriteriaBuilder();
			CriteriaQuery<BatchFake> cq=cb.createQuery(BatchFake.class);
			Root<Batch> root= cq.from(Batch.class);
			Join<Batch,Product> product_=root.join("product",JoinType.INNER);
			cq.select(cb.construct(BatchFake.class,root.get("id"),root.get("batch_code"),root.get("quantity_import"),root.get("quantity_export"),
					root.get("manufacture_date"),root.get("expiration_date"),cb.quot(cb.diff(root.get("quantity_import"),root.get("quantity_export")),product_.get("specification")),
					cb.diff(root.get("quantity_import"),root.get("quantity_export")),product_.get("id"),product_.get("product_code"),product_.get("product_name"),
					product_.get("specification"),product_.get("factor")
					)).where(cb.equal(root.get("id"),id));
			TypedQuery<BatchFake> query=em.createQuery(cq);
			return  query.getSingleResult();
		}catch (Exception e) {
//			logger.error("BatchService.selectByIdFake:"+e.getMessage(),e);
		}
		return null;
	}
	@Override
	public BatchPODFake selectByIdPODFake(long id) {
		try{
			CriteriaBuilder cb=em.getCriteriaBuilder();
			CriteriaQuery<BatchPODFake> cq=cb.createQuery(BatchPODFake.class);
			Root<Batch> root= cq.from(Batch.class);
			Join<Batch,Product> product_=root.join("product",JoinType.INNER);
			cq.select(cb.construct(BatchPODFake.class,root.get("id"),root.get("batch_code"),root.get("quantity_import"),root.get("quantity_export"),
					root.get("manufacture_date"),root.get("expiration_date"),
					cb.diff(root.get("quantity_import"),root.get("quantity_export")),product_.get("id"),product_.get("product_code"),product_.get("product_name"),
					product_.get("specification"),product_.get("factor")
					)).where(cb.equal(root.get("id"),id));
			TypedQuery<BatchPODFake> query=em.createQuery(cq);
			return  query.getSingleResult();
		}catch (Exception e) {
			logger.error("BatchService.selectByIdPODFake:"+e.getMessage(),e);
		}
		return null;
	}

//	private String initCode(){
//		try{
//			CriteriaBuilder cb=em.getCriteriaBuilder();
//			CriteriaQuery<Long> cq=cb.createQuery(Long.class);
//			Root<Batch> root= cq.from(Batch.class);
//			cq.select(cb.max(root.get("id")));
//			TypedQuery<Long> query=em.createQuery(cq);
//			List<Long> list=query.getResultList();
//			if(list.size() >0){
//				return String.format("%06d", list.get(0)+1);
//			}
//			return String.format("%06d", 1);
//		}catch (Exception e) {
//			logger.error("BatchService.initCode:"+e.getMessage(),e);
//		}
//		return null;
//	}

	@Override
	public double getQuantityRemaining(long productId) {
		try{
			CriteriaBuilder cb=em.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq=cb.createQuery(Object[].class);
			Root<Batch> root= cq.from(Batch.class);
			cq.multiselect(cb.sum(root.get("quantity_import")),cb.sum(root.get("quantity_export"))).where(cb.equal(root.get("product").get("id"),productId));
			TypedQuery<Object[]> query=em.createQuery(cq);
			List<Object[]> list=query.getResultList();
			double remain=0;
			for(Object[] p: list){
				Object p1=p[0];
				Object p2=p[1];
				double quantityImport=Double.parseDouble(Objects.toString(p1,"0"));
				double quantityExport=Double.parseDouble(Objects.toString(p2, "0"));
				remain=BigDecimal.valueOf(quantityImport).subtract(BigDecimal.valueOf(quantityExport)).doubleValue();
			}
			return remain;
		}catch(Exception e){
			logger.error("BatchService.getQuantityRemaining:"+e.getMessage(),e);
		}
		return 0;
	}

	@Override
	public int selectByCode(String batch_code, BatchReqInfo t) {
		int res=-1;
		try{
			CriteriaBuilder cb=em.getCriteriaBuilder();
			CriteriaQuery<Batch> cq=cb.createQuery(Batch.class);
			Root<Batch> root= cq.from(Batch.class);
			cq.select(root).where(cb.equal(root.get("batch_code"),batch_code));
			TypedQuery<Batch> query=em.createQuery(cq);
			t.setBatch(query.getSingleResult());
			res=0;
		}catch (Exception e) {
//			logger.error("BatchService.selectByCode:"+e.getMessage(),e);
		}
		return res;
	}

	@Override
	public int exportBatchByInvoiceDetail(long productId, long invoiceDetailId,List<Batch> listBatch) {
		int res=-1;
		try{
			//lấy lô hàng còn tồn
			CriteriaBuilder cb=em.getCriteriaBuilder();
			CriteriaQuery<Batch> cqBatch=cb.createQuery(Batch.class);
			Root<Batch> rootBatch=cqBatch.from(Batch.class);
			rootBatch.fetch("product",JoinType.INNER);
			Predicate con= cb.conjunction();
			con.getExpressions().add(cb.equal(rootBatch.get("product").get("id"), productId));
			con.getExpressions().add(cb.greaterThan(rootBatch.get("quantity_import"), rootBatch.get("quantity_export")));
			con.getExpressions().add(cb.equal(rootBatch.get("status"),1));
			cqBatch.select(rootBatch).where(con).orderBy(cb.asc(rootBatch.get("manufacture_date")));
			TypedQuery<Batch> queryBatch=em.createQuery(cqBatch);
			listBatch.addAll(queryBatch.getResultList());
			res=0;
			
		}catch (Exception e) {
			logger.error("BatchService.exportBatchByInvoiceDetail:"+e.getMessage(),e);
		}
		return res;
	}
	@Override
	public int exportBatchByOrderDetail(long productId, long orderDetailId,List<BatchFake> listBatch) {
		int res=-1;
		try{
			//lấy lô hàng còn tồn
			CriteriaBuilder cb=em.getCriteriaBuilder();
			CriteriaQuery<BatchFake> cqBatch=cb.createQuery(BatchFake.class);
			Root<Batch> rootBatch=cqBatch.from(Batch.class);
			Join<Batch, Product> product2_= rootBatch.join("product",JoinType.INNER);
			Predicate con= cb.conjunction();
			con.getExpressions().add(cb.equal(rootBatch.get("product").get("id"), productId));
			con.getExpressions().add(cb.greaterThan(rootBatch.get("quantity_import"), rootBatch.get("quantity_export")));
			con.getExpressions().add(cb.equal(rootBatch.get("status"),1));
			cqBatch.select(cb.construct(BatchFake.class,rootBatch.get("id"),rootBatch.get("batch_code"),rootBatch.get("quantity_import"),rootBatch.get("quantity_export"),
					rootBatch.get("manufacture_date"),rootBatch.get("expiration_date"),
					cb.quot(cb.diff(rootBatch.get("quantity_import"),rootBatch.get("quantity_export")),product2_.get("specification")),
					cb.diff(rootBatch.get("quantity_import"),rootBatch.get("quantity_export")),product2_.get("id"),product2_.get("product_code"),product2_.get("product_name"),
					product2_.get("specification"),product2_.get("factor")
					)).where(con).orderBy(cb.asc(rootBatch.get("manufacture_date")));
			TypedQuery<BatchFake> queryBatch=em.createQuery(cqBatch);
			listBatch.addAll(queryBatch.getResultList());
			res=0;
		}catch (Exception e) {
		    logger.error("BatchService.exportBatchByOrderDetail:"+e.getMessage(),e);
		}
		return res;
	}
	@Override
	public int exportBatchByPromotionOrderDetail(long productId, long promotionOrderDetailId, List<BatchPODFake> listBatch) {
		int res=-1;
		try{
			//lấy lô hàng xuất của chi tiết hóa đơn
			CriteriaBuilder cb=em.getCriteriaBuilder();
			//lấy danh sách lô hàng còn tồn .
			CriteriaQuery<BatchPODFake> cqBatch=cb.createQuery(BatchPODFake.class);
			Root<Batch> rootBatch=cqBatch.from(Batch.class);
			Join<Batch, Product> product2_= rootBatch.join("product",JoinType.INNER);
			Predicate con= cb.conjunction();
			con.getExpressions().add(cb.equal(rootBatch.get("product").get("id"), productId));
			con.getExpressions().add(cb.greaterThan(rootBatch.get("quantity_import"), rootBatch.get("quantity_export")));
			con.getExpressions().add(cb.equal(rootBatch.get("status"),1));
			cqBatch.select(cb.construct(BatchPODFake.class,rootBatch.get("id"),rootBatch.get("batch_code"),rootBatch.get("quantity_import"),rootBatch.get("quantity_export"),
					rootBatch.get("manufacture_date"),rootBatch.get("expiration_date"),
					cb.diff(rootBatch.get("quantity_import"),rootBatch.get("quantity_export")),product2_.get("id"),product2_.get("product_code"),product2_.get("product_name"),
					product2_.get("specification"),product2_.get("factor")
					)).where(con).orderBy(cb.asc(rootBatch.get("manufacture_date")));
			TypedQuery<BatchPODFake> queryBatch=em.createQuery(cqBatch);
			listBatch.addAll(queryBatch.getResultList());
			res=0;
		}catch (Exception e) {
			logger.error("BatchService.exportBatchByPromotionOrderDetail:"+e.getMessage(),e);
		}
		return res;
	}

}
