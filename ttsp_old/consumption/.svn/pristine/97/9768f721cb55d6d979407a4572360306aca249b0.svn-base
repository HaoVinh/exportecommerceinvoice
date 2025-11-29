package lixco.com.service;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import lixco.com.entity.GoodsImportBreak;
import lixco.com.entity.GoodsImportBreakDetail;
import lixco.com.entity.Product;
import lixco.com.reqInfo.Message;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.BEAN)
public class GoodsImportBreakTHService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;
	@Resource(lookup = "java:/consumption")
	DataSource datasource;
	@Resource
	UserTransaction ut;
	public long checkCode(int thang, int nam, String code, long id) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) AS SoLuong FROM GoodsImportBreak WHERE MONTH(importDate) = :thang AND YEAR(importDate) = :nam AND importCode =:code AND id != :id");
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter("thang", thang);
		query.setParameter("nam", nam);
		query.setParameter("code", code);
		query.setParameter("id", id);
		BigInteger  listObj = (BigInteger) query.getSingleResult();
		return listObj.longValue();
	}

	public int saveOrUpdateGoodsImportBreakService(GoodsImportBreak p,
			List<GoodsImportBreakDetail> goodsImportBreakDetails, Message messages, String user)
			throws IllegalStateException, SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			if (p != null) {
				ut.begin();
				con = datasource.getConnection();
				con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				// cập nhật cả phiếu nhập
				if (p.getId() != 0) {
					GoodsImportBreak goodsImportBreak = em.find(GoodsImportBreak.class, p.getId());
					if (goodsImportBreak == null) {
						res = -1;
						messages.setUser_message("Phiếu nhập không tồn tại.");
						messages.setInternal_message("error request data!");
						ut.rollback();
						return res;
					}

					// cập nhật phiếu nhập
					p.setLastModifedBy(user);
					p.setLastModifedDate(new Date());
					p = em.merge(p);
					if (p == null) {
						res = -1;
						messages.setUser_message("Cập nhật phiếu nhập không thành công.");
						messages.setInternal_message("");
						ut.rollback();
						return res;
					}
					if (goodsImportBreakDetails != null && goodsImportBreakDetails.size() > 0) {
						int stt = 0;
						for (GoodsImportBreakDetail dt : goodsImportBreakDetails) {
							stt++;
							if (dt != null) {
								// String batchCode=dt.getBatch_code();
								Product product = dt.getProduct();
								if (product != null) {
									// khi người dùng thêm mới phiếu nhập
									if (dt.getId() == 0) {
										// lưu chi tiết phiếu nhập
										dt.setGoodsImportBreak(goodsImportBreak);
										em.persist(dt);
										if (dt.getId() == 0) {
											res = -1;
											messages.setUser_message("Lưu chi tiết phiếu nhập không thành công.");
											messages.setInternal_message("error detail row " + stt + "-"
													+ product.getProduct_code());
											ut.rollback();
											return res;
										}

										dt = em.merge(dt);
										if (dt == null) {
											res = -1;
											messages.setUser_message("Cập nhật chi tiết phiếu nhập không thành công.");
											messages.setInternal_message("error row detail " + stt + " "
													+ product.getProduct_code());
											ut.rollback();
											return res;
										}
									} else {
										// trường hợp khi cập nhật lại chi tiết
										// phiếu nhập
										// load trans chi tiết phiếu nhập trong
										// database
										CriteriaQuery<GoodsImportBreakDetail> cqReceiptDetail = cb
												.createQuery(GoodsImportBreakDetail.class);
										Root<GoodsImportBreakDetail> rootReceiptDetail = cqReceiptDetail
												.from(GoodsImportBreakDetail.class);
										rootReceiptDetail.fetch("product", JoinType.INNER);
										cqReceiptDetail.select(rootReceiptDetail).where(
												cb.equal(rootReceiptDetail.get("id"), dt.getId()));
										TypedQuery<GoodsImportBreakDetail> query = em.createQuery(cqReceiptDetail);
										GoodsImportBreakDetail transDetailOld = query.getSingleResult();
										// số lượng chi tiết nhập hiện tại trong
										// database
										double quantityReceipt = transDetailOld.getQuantity();
										// gở số lượng lô hàng chứa số lượng chi
										// tiết phiếu nhập ra
										// nếu lô hàng đã xuất
										// quantityExportBatchTrans<quantityImportBatchRemain
										// tức là xuất số lượng lô hàng không
										// đụng gì đến số lượng của chi tiết
										// phiếu nhập
										// thì cập nhật bình thường

										// cập nhật lại chi tiết phiếu nhập
										dt = em.merge(dt);
										if (dt == null) {
											res = -1;
											messages.setUser_message("Cập nhật chi tiết phiếu nhập không thành công.");
											messages.setInternal_message("error detail row " + stt);
											ut.rollback();
											return res;
										}

									}
								} else {
									res = -1;
									messages.setUser_message("Thông tin chi tiết phiếu nhập tại dòng " + stt
											+ " không đầy đủ!");
									messages.setInternal_message("error data request!");
									ut.rollback();
									return res;
								}
							}
						}
					}

				} else {// trường hợp thêm mới 1 phiếu nhập
					// lưu phiếu nhập
					p.setCreatedBy(user);
					p.setCreatedDate(new Date());

					em.persist(p);
					if (p.getId() == 0) {
						res = -1;
						messages.setUser_message("Lưu phiếu nhập không thành công.");
						messages.setInternal_message("error persist GoodsImportBreakPos");
						ut.rollback();
						return res;
					}
					int stt = 0;
					for (GoodsImportBreakDetail dt : goodsImportBreakDetails) {
						stt++;
						Product product = dt.getProduct();
						if (dt.getId() == 0 && product != null && product.getId() != 0) {
							// lưu chi tiết phiếu nhập
							dt.setCreated_by(user);
							dt.setCreated_date(new Date());
							dt.setGoodsImportBreak(p);
							em.persist(dt);
							if (dt.getId() == 0) {
								res = -1;
								messages.setUser_message("Không lưu được: " + product.getProduct_code());
								messages.setInternal_message("error detail row " + stt + "-"
										+ dt.getProduct().getProduct_code());
								ut.rollback();
								return res;
							}
						} else {
							res = -1;
							messages.setUser_message("Thông tin không đầy đủ!");
							messages.setInternal_message("error data request!");
							ut.rollback();
							return res;
						}

					}
				}
			} else {
				res = -1;
				messages.setUser_message("Thông tin không đầy đủ!");
				messages.setInternal_message("error data request!");
				ut.rollback();
				return res;
			}
			res = 0;
			ut.commit();
		} catch (Exception e) {
			e.printStackTrace();
			res = -1;
			messages.setUser_message("Lưu phiếu nhập không thành công.");
			messages.setInternal_message("ProcessLogicGoodsImportBreakService.saveOrUpdateGoodsImportBreakService:"
					+ e.getMessage());
			ut.rollback();
			logger.error("ProcessLogicGoodsImportBreakService.saveOrUpdateGoodsImportBreakService:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

}
