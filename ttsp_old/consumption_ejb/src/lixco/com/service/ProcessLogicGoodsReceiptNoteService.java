package lixco.com.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.logging.Logger;

import lixco.com.commom_ejb.ToolTimeCustomer;
import lixco.com.entity.Batch;
import lixco.com.entity.ExportBatch;
import lixco.com.entity.GoodsReceiptNote;
import lixco.com.entity.GoodsReceiptNoteDetail;
import lixco.com.entity.Product;
import lixco.com.interfaces.IProcessLogicGoodsReceiptNoteService;
import lixco.com.reqInfo.GoodsReceiptNoteDetailReqInfo;
import lixco.com.reqInfo.Message;
import lixco.com.reqInfo.WrapGoodsReceiptNoteReqInfo;
import lixco.com.reqInfo.WrapListGoodsReceiptNoteDetailReqInfo;

@Stateless
@TransactionManagement(value = TransactionManagementType.BEAN)
public class ProcessLogicGoodsReceiptNoteService implements IProcessLogicGoodsReceiptNoteService {
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

	private String initbatchCode() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
			Root<Batch> root = cq.from(Batch.class);
			// cq.multiselect(cb.coalesce(cb.max(root.get("id")),0));
			cq.multiselect(cb.coalesce(cb.max(cb.quot((Expression) root.get("batch_code"), 1)), 0));
			TypedQuery<Integer> query = em.createQuery(cq);
			int max = query.getSingleResult();
			double p = (double) max / 1000000;
			if (p < 1) {
				return String.format("%06d", max + 1);
			}
			return (max + 1) + "";
		} catch (Exception e) {
			logger.error("ProcessLogicGoodsReceiptNoteService.initbatchCode:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int deleteGoodsReceiptNoteMaster(long id, Message messages) throws IllegalStateException, SystemException,
			SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			GoodsReceiptNote f = em.find(GoodsReceiptNote.class, id);
			if (f == null) {
				res = -1;
				messages.setUser_message("Phiếu nhập không tồn tại!");
				messages.setInternal_message("");
				ut.rollback();
				return res;
			}
			// lấy danh sách chi tiết phiếu nhập
			CriteriaQuery<GoodsReceiptNoteDetail> cqDetail = cb.createQuery(GoodsReceiptNoteDetail.class);
			Root<GoodsReceiptNoteDetail> rootDetail = cqDetail.from(GoodsReceiptNoteDetail.class);
			rootDetail.fetch("product", JoinType.INNER);
			rootDetail.fetch("batch", JoinType.INNER);
			cqDetail.select(rootDetail).where(cb.equal(rootDetail.get("goods_receipt_note").get("id"), f.getId()));
			TypedQuery<GoodsReceiptNoteDetail> queryDetail = em.createQuery(cqDetail);
			List<GoodsReceiptNoteDetail> listDetail = queryDetail.getResultList();
			for (GoodsReceiptNoteDetail dt : listDetail) {
				// lô hàng của chi tiết phiếu nhập
				Batch batchTrans = dt.getBatch();
				// nếu lô hàng đã xuất thì kiểm tra số lượng xuất có sử dụng số
				// lượng của phiếu nhập này hay chưa
				double quantityExportBatchTrans = batchTrans.getQuantity_export();
				// gở số lượng lô hàng chứa số lượng chi tiết phiếu nhập ra
				double quantityImportBatchNew = BigDecimal.valueOf(batchTrans.getQuantity_import())
						.subtract(BigDecimal.valueOf(dt.getQuantity())).doubleValue();
				if (quantityExportBatchTrans > 0) {
					// nếu số lượng xuất sủ dụng số lượng của chi tiết phiếu
					// nhập thì rollback
					if (quantityExportBatchTrans > quantityImportBatchNew) {
						res = -1;
						messages.setUser_message("Xóa thất bại, Chi tiết phiếu nhập có lô hàng đã xuất sử dụng số lượng của chi tiết phiếu nhập này!");
						messages.setInternal_message("error row detail id " + dt.getId() + "-"
								+ dt.getProduct().getProduct_code() + "-" + batchTrans.getBatch_code());
						ut.rollback();
						return res;
					}
				}
				batchTrans.setQuantity_import(quantityImportBatchNew);
				batchTrans = em.merge(batchTrans);
				if (batchTrans == null) {
					res = -1;
					messages.setUser_message("Cập nhật số lượng lô hàng thất bại!");
					messages.setInternal_message("error row detail id " + dt.getId() + "-"
							+ dt.getProduct().getProduct_code());
					ut.rollback();
					return res;

				}
				// xóa chi tiết phiếu nhập sử dụng JPQL
				Query queryDelDetail = em.createQuery("delete from GoodsReceiptNoteDetail where id=:idd");
				queryDelDetail.setParameter("idd", dt.getId());
				if (queryDelDetail.executeUpdate() <= 0) {
					res = -1;
					messages.setUser_message("Xóa chi tiết phiếu nhập thất bại!");
					messages.setInternal_message("error row detail id " + dt.getId() + "-"
							+ dt.getProduct().getProduct_code() + "-" + batchTrans.getBatch_code());
					ut.rollback();
					return res;
				}
			}
			Query query2 = em.createQuery("delete from GoodsReceiptNote where id=:id ");
			query2.setParameter("id", id);
			res = query2.executeUpdate();
			ut.commit();
		} catch (Exception e) {
			res = -1;
			messages.setUser_message("Xóa phiếu nhập thất bại!");
			messages.setInternal_message("ProcessLogicGoodsReceiptNoteService.deleteGoodsReceiptNoteMaster:"
					+ e.getMessage());
			ut.rollback();
			logger.error("ProcessLogicGoodsReceiptNoteService.deleteGoodsReceiptNoteMaster:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int deleteGoodsReceiptNoteDetailMaster(long id, Message message) throws IllegalStateException,
			SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			GoodsReceiptNoteDetail transDetail = em.find(GoodsReceiptNoteDetail.class, id);
			if (transDetail == null) {
				res = -1;
				message.setUser_message("Chi tiết phiếu nhập không tồn tại!");
				message.setInternal_message("error request data!");
				ut.commit();
				return res;
			}
			// xóa chi tiết phiếu nhập sử dụng JPQL
			Query queryDelDetail = em.createQuery("delete from GoodsReceiptNoteDetail where id=:idd");
			queryDelDetail.setParameter("idd", transDetail.getId());
			if (queryDelDetail.executeUpdate() <= 0) {
				res = -1;
				message.setUser_message("Xóa chi tiết phiếu nhập thất bại!");
				message.setInternal_message("error row detail id " + transDetail.getId() + "-"
						+ transDetail.getProduct().getProduct_code());
				ut.rollback();
				return res;
			}
			res = 1;
			ut.commit();
		} catch (Exception e) {
			res = -1;
			message.setUser_message("Không xóa được chi tiết phiếu nhập!");
			message.setInternal_message("ProcessLogicGoodsReceiptNoteService.deleteGoodsReceiptNoteDetailMaster");
			ut.rollback();
			logger.error("ProcessLogicGoodsReceiptNoteService.deleteGoodsReceiptNoteDetailMaster:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int insertGoodsReceiptNoteDetail(GoodsReceiptNoteDetailReqInfo t, StringBuilder messages)
			throws IllegalStateException, SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			GoodsReceiptNoteDetail f = t.getGoods_receipt_note_detail();
			if (f != null && f.getId() == 0) {
				GoodsReceiptNote trans = em.find(GoodsReceiptNote.class, f.getGoods_receipt_note().getId());
				if (trans != null) {
					// lưu bình thường
					// kiểm tra phiếu nhập đã tồn tại chưa
					String sql1 = "select count(id) from GoodsReceiptNoteDetail where product.id=:pid and goods_receipt_note.id=:gid ";
					Query query1 = em.createQuery(sql1);
					query1.setParameter("pid", f.getProduct().getId());
					query1.setParameter("gid", trans.getId());
					int check = Integer.parseInt(Objects.toString(query1.getSingleResult(), "0"));
					if (check > 0) {
						res = -1;
						messages.append("Trùng sản phẩm!");
						ut.commit();
						return res;
					} else {
						em.persist(f);
						if (f.getId() > 0) {
							if (trans.getStatus() == 1) {
								// tạo lô hàng
								Batch batch = new Batch();
								batch.setCreated_by(f.getCreated_by());
								batch.setCreated_date(new Date());
								batch.setBatch_code(initbatchCode());
								batch.setManufacture_date(trans.getImport_date());
								batch.setProduct(f.getProduct());
								batch.setQuantity_import(f.getQuantity());
								// batch.setGoods_receipt_note_detail(f);
								em.persist(batch);
								if (batch.getId() == 0) {
									res = -1;
									ut.setRollbackOnly();
									messages.append("Tạo lô hàng thất bại!");
									return res;
								}
								f.setBatch(batch);
								if (em.merge(f) == null) {
									res = -1;
									ut.setRollbackOnly();
									messages.append("Cập nhật phiếu nhập thất bại!");
									return res;
								}

							}
							ut.commit();
							res = 0;
							// trả về kết quả
							CriteriaBuilder cb = em.getCriteriaBuilder();
							CriteriaQuery<GoodsReceiptNoteDetail> cq = cb.createQuery(GoodsReceiptNoteDetail.class);
							Root<GoodsReceiptNoteDetail> root = cq.from(GoodsReceiptNoteDetail.class);
							root.fetch("product", JoinType.INNER);
							root.fetch("goods_receipt_note", JoinType.INNER);
							root.fetch("batch", JoinType.LEFT);
							cq.select(root).where(cb.equal(root.get("id"), f.getId()));
							TypedQuery<GoodsReceiptNoteDetail> query = em.createQuery(cq);
							t.setGoods_receipt_note_detail(query.getSingleResult());
						} else {
							// không lưu được chi tiết phiếu nhập
							res = -1;
							messages.append("Lưu thất bại!");
							ut.setRollbackOnly();
							return res;
						}
					}
				} else {
					// Phiếu nhập không tồn tại
					res = -1;
					messages.append("Phiếu nhập không tồn tại!");
					ut.commit();
					return res;
				}
			} else {
				// thông tin không hợp lệ
				res = -1;
				messages.append("Thông tin không hợp lệ!");
				ut.commit();
				return res;
			}
		} catch (Exception e) {
			res = -1;
			ut.setRollbackOnly();
			logger.error("ProcessLogicGoodsReceiptNoteService.insertGoodsReceiptNoteDetail:" + e.getMessage(), e);
			messages.append("ProcessLogicGoodsReceiptNoteService.insertGoodsReceiptNoteDetail:" + e.getMessage());
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int updateGoodsReceiptNoteDetail(GoodsReceiptNoteDetailReqInfo t, StringBuilder messages)
			throws IllegalStateException, SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.getTransactionIsolation();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			GoodsReceiptNoteDetail f = t.getGoods_receipt_note_detail();
			if (f != null && f.getId() > 0) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				// nạp trans chi tiết phiếu nhập
				GoodsReceiptNoteDetail transDetail = em.find(GoodsReceiptNoteDetail.class, f.getId());
				if (transDetail != null) {
					GoodsReceiptNote transGoods = em.find(GoodsReceiptNote.class, f.getGoods_receipt_note().getId());
					// lấy lô hàng hiện tại
					Batch batchTrans = transDetail.getBatch();
					boolean check = false;
					// nếu phiếu nhập này đã hoàn hành
					if (transGoods.getStatus() == 1) {
						// kiểm tra có lô hàng này đã xuất chưa.
						CriteriaQuery<Long> cq = cb.createQuery(Long.class);
						Root<ExportBatch> root = cq.from(ExportBatch.class);
						Subquery<Long> sub = cq.subquery(Long.class);
						Root<GoodsReceiptNoteDetail> root2 = sub.from(GoodsReceiptNoteDetail.class);
						sub.select(root2.get("batch").get("id")).where(
								cb.equal(root2.get("goods_receipt_note"), transGoods.getId()));
						cq.select(cb.count(root.get("id"))).where(cb.equal(root.get("batch").get("id"), sub));
						TypedQuery<Long> query = em.createQuery(cq);
						int chk = query.getSingleResult().intValue();
						// nếu đã xuất lô hàng
						if (chk > 0) {
							// kiểm tra nếu cập nhật sản phẩm khác với sản phẩm
							// ban đầu
							if (!f.getProduct().equals(transDetail.getProduct())) {
								// Lô hàng đã xuất không được thay đổi sản phẩm
								res = -1;
								messages.append("Lô hàng đã xuất không được chỉnh sửa sản phẩm!");
							} else {
								// Lấy số lượng chi tiết phiếu nhập - sản lượng
								// lô hàng(trans)
								double sl = BigDecimal.valueOf(f.getQuantity())
										.subtract(BigDecimal.valueOf(transDetail.getQuantity())).doubleValue();
								// kiểm tra số lượng chi tiết phiếu nhập chỉnh
								// sửa nếu lớn hơn hoặc bằng sản lượng ban
								// đầu(trans) thì cập nhật bình thường
								if (sl >= 0) {
									check = true;
									// cập nhật số lượng lô hàng bằng chi tiết
									// phiếu nhập
									batchTrans.setQuantity_import(f.getQuantity());

								} else {
									// Lấy số lượng xuất thực tế của lô hàng
									CriteriaQuery<Double> cq3 = cb.createQuery(Double.class);
									Root<ExportBatch> root3 = cq3.from(ExportBatch.class);
									cq3.select(cb.sum(root3.get("quantity"))).where(
											cb.equal(root3.get("batch").get("id"), batchTrans.getId()));
									TypedQuery<Double> query3 = em.createQuery(cq3);
									double quantityExport = query3.getSingleResult();
									// kiểm tra số lượng đã xuất cho lô hàng
									// trên nếu nhỏ hơn hoặc bằng số lượng chi
									// tiết phiếu nhập thì cập nhật lại số lượng
									// lô hàng
									if (quantityExport <= f.getQuantity()) {
										check = true;
										double quantityNew = BigDecimal.valueOf(f.getQuantity())
												.subtract(BigDecimal.valueOf(quantityExport)).doubleValue();
										batchTrans.setQuantity_import(quantityNew);
									} else {
										// số lượng xuất thực tế lớn hơn số
										// lượng nhập
										res = -1;
										messages.append("Lô hàng đã xuất có số lượng lớn hơn số lượng chỉnh sửa!");
									}
								}
							}
						} else {
							// trường chưa xuất lô hàng
							// cập nhật lại số lượng và sản phẩm lô hàng giống
							// chi tiết phiếu nhập
							check = true;
							batchTrans.setProduct(f.getProduct());
							// batchTrans.setQuantity(f.getQuantity());
							// batchTrans.setUnit_price(f.getUnit_price());
						}
					} else {
						check = true;
					}
					if (check) {
						if (em.merge(f) != null) {
							batchTrans.setLast_modifed_by(f.getLast_modifed_by());
							batchTrans.setLast_modifed_date(new Date());
							if (em.merge(batchTrans) != null) {
								// trả lại kết quả
								CriteriaQuery<GoodsReceiptNoteDetail> cq4 = cb
										.createQuery(GoodsReceiptNoteDetail.class);
								Root<GoodsReceiptNoteDetail> root4 = cq4.from(GoodsReceiptNoteDetail.class);
								root4.fetch("product", JoinType.INNER);
								root4.fetch("goods_receipt_note", JoinType.INNER);
								root4.fetch("batch", JoinType.LEFT);
								cq4.select(root4).where(cb.equal(root4.get("id"), f.getId()));
								TypedQuery<GoodsReceiptNoteDetail> query4 = em.createQuery(cq4);
								t.setGoods_receipt_note_detail(query4.getSingleResult());
								res = 0;
							} else {
								ut.setRollbackOnly();
								res = -1;
								messages.append("Cập lô hàng thất bại!");
							}
						} else {
							ut.setRollbackOnly();
							res = -1;
							messages.append("Cập nhật thất bại!");
						}
					}

				} else {
					// Chi tiết phiếu nhập không tồn tại
					res = -1;
					messages.append("Chi tiết phiếu nhập không tồn tại!");
				}
			} else {
				// sai thông tin cập nhật
				res = -1;
				messages.append("Thông tin không hợp lệ!");
			}
			ut.commit();
		} catch (Exception e) {
			ut.setRollbackOnly();
			logger.error("ProcessLogicGoodsReceiptNoteService.updateGoodsReceiptNoteDetail:" + e.getMessage(), e);
			res = -1;
			messages.append("ProcessLogicGoodsReceiptNoteService.updateGoodsReceiptNoteDetail:" + e.getMessage());
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int saveOrUpdateGoodsReceiptNoteService(WrapGoodsReceiptNoteReqInfo t, Message messages)
			throws IllegalStateException, SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			GoodsReceiptNote p = t.getGoods_receipt_note();
			String memberName = t.getMember_name();
			long memberId = t.getMember_id();
			List<GoodsReceiptNoteDetail> listDetail = t.getList_goods_receipt_note_detail();
			if (p != null) {
				int status = p.getStatus();
				ut.begin();
				con = datasource.getConnection();
				con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				// cập nhật cả phiếu nhập
				if (p.getId() != 0) {
					GoodsReceiptNote goodsTrans = em.find(GoodsReceiptNote.class, p.getId());
					if (goodsTrans == null) {
						res = -1;
						messages.setUser_message("Phiếu nhập không tồn tại!");
						messages.setInternal_message("error request data!");
						ut.rollback();
						return res;
					}
					if (goodsTrans.getStatus() == 1 && status == 0) {
						res = -1;
						messages.setUser_message("Phiếu nhập đã hoàn thành không thể cập nhật trạng thái lưu tạm!");
						messages.setInternal_message("error request data!");
						ut.rollback();
						return res;
					}
					// cập nhật phiếu nhập
					p.setLast_modifed_by(memberName);
					p.setLast_modifed_date(new Date());
					p = em.merge(p);
					if (p == null) {
						res = -1;
						messages.setUser_message("Cập nhật phiếu nhập thất bại!");
						messages.setInternal_message("");
						ut.rollback();
						return res;
					}
					if (listDetail != null && listDetail.size() > 0) {
						int stt = 0;
						for (GoodsReceiptNoteDetail dt : listDetail) {
							stt++;
							if (dt != null) {
								// String batchCode=dt.getBatch_code();
								Product product = dt.getProduct();
								if (product != null) {
									// khi người dùng thêm mới phiếu nhập
									if (dt.getId() == 0) {
										// lưu chi tiết phiếu nhập
										dt.setGoods_receipt_note(goodsTrans);
										em.persist(dt);
										if (dt.getId() == 0) {
											res = -1;
											messages.setUser_message("Lưu chi tiết phiếu nhập thất bại!");
											messages.setInternal_message("error detail row " + stt + "-"
													+ product.getProduct_code());
											ut.rollback();
											return res;
										}

										dt = em.merge(dt);
										if (dt == null) {
											res = -1;
											messages.setUser_message("Cập nhật chi tiết phiếu nhập thất bại!");
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
										CriteriaQuery<GoodsReceiptNoteDetail> cqReceiptDetail = cb
												.createQuery(GoodsReceiptNoteDetail.class);
										Root<GoodsReceiptNoteDetail> rootReceiptDetail = cqReceiptDetail
												.from(GoodsReceiptNoteDetail.class);
										rootReceiptDetail.fetch("product", JoinType.INNER);
										rootReceiptDetail.fetch("batch", JoinType.LEFT);
										cqReceiptDetail.select(rootReceiptDetail).where(
												cb.equal(rootReceiptDetail.get("id"), dt.getId()));
										TypedQuery<GoodsReceiptNoteDetail> query = em.createQuery(cqReceiptDetail);
										GoodsReceiptNoteDetail transDetailOld = query.getSingleResult();
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
											messages.setUser_message("Cập nhật chi tiết phiếu nhập thất bại!");
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
					p.setCreated_by(memberName);
					p.setCreated_date(new Date());
					p.setCreated_by_id(memberId);
					p.setReceipt_code(initReceiptNoteCode());
					String voucherCode = p.getVoucher_code();
					if (voucherCode == null || "".equals(voucherCode)) {
						initCodeVoucher(p);
					}
					em.persist(p);
					if (p.getId() == 0) {
						res = -1;
						messages.setUser_message("Lưu phiếu nhập thất bại");
						messages.setInternal_message("error persist GoodsReceiptNotePos");
						ut.rollback();
						return res;
					}
					int stt = 0;
					for (GoodsReceiptNoteDetail dt : listDetail) {
						stt++;
						Product product = dt.getProduct();
						if (dt.getId() == 0 && product != null && product.getId() != 0) {

							// lưu chi tiết phiếu nhập
							dt.setCreated_by(memberName);
							dt.setCreated_date(new Date());
							dt.setGoods_receipt_note(p);
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
			res = -1;
			messages.setUser_message("Lưu phiếu nhập thất bại!");
			messages.setInternal_message("ProcessLogicGoodsReceiptNoteService.saveOrUpdateGoodsReceiptNoteService:"
					+ e.getMessage());
			ut.rollback();
			logger.error("ProcessLogicGoodsReceiptNoteService.saveOrUpdateGoodsReceiptNoteService:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	private String initReceiptNoteCode() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
			Root<GoodsReceiptNote> root = cq.from(GoodsReceiptNote.class);
			// cq.multiselect(cb.coalesce(cb.max(root.get("id")),0));
			cq.multiselect(cb.coalesce(cb.max(cb.quot((Expression) root.get("receipt_code"), 1)), 0));
			TypedQuery<Integer> query = em.createQuery(cq);
			int max = query.getSingleResult();
			double p = (double) max / 10000000;
			if (p < 1) {
				return String.format("%07d", max + 1);
			}
			return (max + 1) + "";
		} catch (Exception e) {
			logger.error("ProcessLogicGoodsReceiptNoteService.initReceiptNoteCode:" + e.getMessage(), e);
		}
		return null;
	}

	private int initCodeVoucher(GoodsReceiptNote t) {
		int res = -1;

		try {
			Date date = t.getImport_date();
			int year = ToolTimeCustomer.getYearM(date);
			int month = ToolTimeCustomer.getMonthM(date);
			int day = ToolTimeCustomer.getDayM(date);

			String voucher = day + "" + month + "" + year + "/";
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<String> cq = cb.createQuery(String.class);
			Root<GoodsReceiptNote> root = cq.from(GoodsReceiptNote.class);
			cq.select(root.get("voucher_code"))
					.where(cb.equal(root.get("import_date"), ToolTimeCustomer.getFirstDateOfDay(date)))
					.orderBy(cb.desc(root.get("import_date")));
			TypedQuery<String> query = em.createQuery(cq);
			List<String> list = query.getResultList();

			if (list.size() > 0) {
				String temp = list.get(0);
				if (temp != null) {
					int last = temp.lastIndexOf("/");
					String sub = temp.substring(last + 1);
					voucher = voucher + String.format("%02d", Integer.parseInt(sub) + 1);
					t.setVoucher_code(voucher);
				}
			} else {
				voucher = voucher + String.format("%02d", 1);
				t.setVoucher_code(voucher);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ProcessLogicGoodsReceiptNoteService.initCode:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int insertGoodsReceiptNoteDetailFromDelivery(GoodsReceiptNoteDetailReqInfo t, Message message)
			throws IllegalStateException, SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			GoodsReceiptNoteDetail f = t.getGoods_receipt_note_detail();
			String memberName = t.getMember_name();
			if (f != null && f.getId() == 0 && f.getProduct() != null) {
				GoodsReceiptNote trans = em.find(GoodsReceiptNote.class, f.getGoods_receipt_note().getId());
				if (trans != null) {
					// kiểm tra chi tiết phiếu nhập đã tồn tại chưa
					CriteriaBuilder cb = em.getCriteriaBuilder();
					CriteriaQuery<GoodsReceiptNoteDetail> cqDetail = cb.createQuery(GoodsReceiptNoteDetail.class);
					Root<GoodsReceiptNoteDetail> rootDetail = cqDetail.from(GoodsReceiptNoteDetail.class);
					Predicate conp1 = cb.conjunction();
					conp1.getExpressions().add(cb.equal(rootDetail.get("product").get("id"), f.getProduct().getId()));
					conp1.getExpressions().add(cb.equal(rootDetail.get("goods_receipt_note").get("id"), trans.getId()));
					conp1.getExpressions().add(cb.equal(rootDetail.get("delivery_code"), f.getDelivery_code()));
					/*
					 * Bổ sung thêm điều kiện mã lô hàng để thêm vào phiếu nhập (ngày 06/09/2023)
					 */
					conp1.getExpressions().add(cb.equal(rootDetail.get("batch_code"), f.getBatch_code()));
					cqDetail.select(rootDetail).where(conp1);
					TypedQuery<GoodsReceiptNoteDetail> queryDetail = em.createQuery(cqDetail);
					List<GoodsReceiptNoteDetail> listDetailTrans = queryDetail.getResultList();
					if (listDetailTrans.size() > 0) {
						GoodsReceiptNoteDetail detailTrans = listDetailTrans.get(0);
						/*
						 * Đóng lại ngày 28/08/2023
						 * tăng số lượng chi tiết phiếu nhập đó lên. double
						 * quantityImport =
						 * BigDecimal.valueOf(detailTrans.getQuantity())
						 * .add(BigDecimal
						 * .valueOf(f.getQuantity())).doubleValue();
						 * detailTrans.setQuantity(quantityImport);
						 */

						// Cài đặt lại theo số lượng mới.
						detailTrans.setQuantity(f.getQuantity());
						detailTrans.setLast_modifed_by(memberName);
						detailTrans.setLast_modifed_date(new Date());
						if (em.merge(detailTrans) == null) {
							res = -1;
							message.setUser_message("Cộng dồn số lượng chi tiết phiếu nhập thất bại!");
							message.setInternal_message("error merge GoodsReceiptNote!");
							ut.rollback();
							return res;
						}
					} else {
						f.setCreated_by(memberName);
						f.setCreated_date(new Date());
						em.persist(f);
						if (f.getId() == 0) {
							res = -1;
							message.setUser_message("Lưu chi tiết phiếu nhập sản phẩm:"
									+ f.getProduct().getProduct_name() + " thất bại!");
							message.setInternal_message("error persist GoodsReceiptNoteDetail error!");
							ut.rollback();
							return res;
						}
					}
				} else {
					// Phiếu nhập không tồn tại
					res = -1;
					message.setUser_message("Phiếu nhập không tồn tại!");
					message.setInternal_message("error load GoodsReceiptNote!");
					ut.rollback();
					return res;
				}
			} else {
				// thông tin không hợp lệ
				res = -1;
				message.setUser_message("Thông tin không đầy đủ!");
				message.setInternal_message("");
				ut.rollback();
				return res;
			}
			res = 0;
			ut.commit();
		} catch (Exception e) {
			res = -1;
			ut.rollback();
			logger.error(
					"ProcessLogicGoodsReceiptNoteService.insertGoodsReceiptNoteDetailFromDelivery:" + e.getMessage(), e);
			message.setUser_message("Lưu chi tiết phiếu nhập thất bại");
			message.setInternal_message("error request data:" + e.getMessage());
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int synthesizeDataFromDelivery(WrapListGoodsReceiptNoteDetailReqInfo t, Message message)
			throws IllegalStateException, SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			List<GoodsReceiptNoteDetail> listDetail = t.getList_goods_receipt_note_detail();
			GoodsReceiptNote receiptNote = t.getGoods_receipt_note();
			String memberName = t.getMember_name();
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			for (GoodsReceiptNoteDetail dt : listDetail) {
				// tìm kiếm có tồn tại chi tiết có sản phẩm và mã lô hàng đó
				// chưa.
				CriteriaQuery<GoodsReceiptNoteDetail> cqDetail = cb.createQuery(GoodsReceiptNoteDetail.class);
				Root<GoodsReceiptNoteDetail> rootDetail = cqDetail.from(GoodsReceiptNoteDetail.class);
				Predicate conp1 = cb.conjunction();
				conp1.getExpressions().add(cb.equal(rootDetail.get("product").get("id"), dt.getProduct().getId()));
				conp1.getExpressions().add(
						cb.equal(rootDetail.get("goods_receipt_note").get("id"), receiptNote.getId()));
				conp1.getExpressions().add(cb.equal(rootDetail.get("delivery_code"), dt.getDelivery_code()));
				cqDetail.select(rootDetail).where(conp1);
				TypedQuery<GoodsReceiptNoteDetail> query = em.createQuery(cqDetail);
				List<GoodsReceiptNoteDetail> listTransDetail = query.getResultList();
				if (listTransDetail.size() > 0) {
					// ghi đè lên số lượng phiếu nhập chính.
					GoodsReceiptNoteDetail detailTrans = listTransDetail.get(0);
					// cập nhật lại số lượng chi tiết phiếu nhập thất bại.
					detailTrans.setQuantity(dt.getQuantity());
					detailTrans.setLast_modifed_by(memberName);
					detailTrans.setLast_modifed_date(new Date());
					if (em.merge(detailTrans) == null) {
						res = -1;
						message.setUser_message("Cập nhật số lượng chi tiết phiếu nhập thất bại!");
						message.setInternal_message("error merge GoodsReceiptNoteDetail product:"
								+ dt.getProduct().getProduct_name());
						ut.rollback();
						return res;
					}
				} else {
					// tạo chi tiết phiếu nhập
					dt.setGoods_receipt_note(receiptNote);
					dt.setCreated_by(memberName);
					dt.setCreated_date(new Date());
					em.persist(dt);
					if (dt.getId() == 0) {
						res = -1;
						message.setUser_message("Tạo chi tiết phiếu nhập thất bại!");
						message.setInternal_message("error persist GoodsReceiptNoteDetail product:"
								+ dt.getProduct().getProduct_name());
						ut.rollback();
						return res;
					}
				}
			}
			res = 0;
			ut.commit();
		} catch (Exception e) {
			res = -1;
			ut.rollback();
			logger.error("ProcessLogicGoodsReceiptNoteService.synthesizeDataFromDelivery:" + e.getMessage(), e);
			message.setUser_message("Tổng hợp dữ liệu giao nhận thất bại!");
			message.setInternal_message("error request data:" + e.getMessage());
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

}
