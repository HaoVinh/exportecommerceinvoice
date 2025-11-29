package lixco.com.service;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import lixco.com.entity.Product;
import lixco.com.entity.YeuCauKiemTraHang;
import lixco.com.entity.YeuCauKiemTraHangDetail;
import lixco.com.entityapi.YeuCauKiemTraHangDTO;
import lixco.com.entityapi.YeuCauKiemTraHangDetailDTO;
import lixco.com.reqInfo.Message;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.BEAN)
public class YeuCauKiemTraHangTHService {
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

	@Inject
	YeuCauKiemTraHangDetailService yeuCauKiemTraHangDetailService;

	public List<YeuCauKiemTraHangDTO> searchAPI(Date startDate, Date endDate) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<YeuCauKiemTraHangDTO> cq = cb.createQuery(YeuCauKiemTraHangDTO.class);
			Root<YeuCauKiemTraHang> root_ = cq.from(YeuCauKiemTraHang.class);
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.greaterThanOrEqualTo(root_.get("requestDate"), startDate));
			predicates.add(cb.lessThanOrEqualTo(root_.get("requestDate"), endDate));
			cq.select(
					cb.construct(YeuCauKiemTraHangDTO.class, root_.get("id"), root_.get("createdBy"),
							root_.get("requestCode"), root_.get("requestDate"), root_.get("note"),
							root_.get("dakiemtra"), root_.get("ngayKiemTra"), root_.get("createdCheck")
							))
					.where(cb.and(predicates.toArray(new Predicate[0]))).orderBy(cb.desc(root_.get("requestDate")));
			TypedQuery<YeuCauKiemTraHangDTO> query = em.createQuery(cq);
			List<YeuCauKiemTraHangDTO> yeuCauKiemTraHangDTOs = query.getResultList();
			int sizeYCs = yeuCauKiemTraHangDTOs.size();
			for (int i = 0; i < sizeYCs; i++) {
				List<YeuCauKiemTraHangDetailDTO> yeuCauKiemTraHangDetailDTOs = yeuCauKiemTraHangDetailService
						.findByYeuCauKiemTraHangDTO(yeuCauKiemTraHangDTOs.get(i).getId());
				yeuCauKiemTraHangDTOs.get(i).setYeuCauKiemTraHangDetailDTOs(yeuCauKiemTraHangDetailDTOs);

			}
			return yeuCauKiemTraHangDTOs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<YeuCauKiemTraHangDTO>();
	}

	public long checkCode(int thang, int nam, String code, long id) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) AS SoLuong FROM YeuCauKiemTraHang WHERE MONTH(requestDate) = :thang AND YEAR(requestDate) = :nam AND requestCode =:code AND id != :id");
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter("thang", thang);
		query.setParameter("nam", nam);
		query.setParameter("code", code);
		query.setParameter("id", id);
		BigInteger listObj = (BigInteger) query.getSingleResult();
		return listObj.longValue();
	}

	public int updateKetQuaKiemTra(YeuCauKiemTraHangDTO pDTO, StringBuilder messages) throws IllegalStateException,
			SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			List<YeuCauKiemTraHangDetailDTO> yeuCauKiemTraHangDetailDTOs = pDTO.getYeuCauKiemTraHangDetailDTOs();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			// cập nhật cả phiếu nhập
			if (pDTO.getId() != 0) {
				YeuCauKiemTraHang p = em.find(YeuCauKiemTraHang.class, pDTO.getId());
				if (p == null) {
					res = -1;
					messages.append("Phiếu không tồn tại.");
					ut.rollback();
					return res;
				}
				/*
				 * KCS kiem tra
				 */
				p.setDakiemtra(pDTO.isDakiemtra());
				p.setNgayKiemTra(pDTO.getNgayKiemTra());
				p.setCreatedCheck(pDTO.getCreatedCheck());
				p = em.merge(p);
				if (p == null) {
					res = -1;
					messages.append("Không cập nhật được phiếu, kiểm tra log.");
					ut.rollback();
					return res;
				}
				if (yeuCauKiemTraHangDetailDTOs != null) {
					for (YeuCauKiemTraHangDetailDTO dtDTO : yeuCauKiemTraHangDetailDTOs) {
						YeuCauKiemTraHangDetail ycOld = yeuCauKiemTraHangDetailService.findById(dtDTO.getId());
						if (ycOld != null) {
							ycOld.setTinhtrang(dtDTO.getTinhtrang());
							ycOld.setKiemtradat(dtDTO.isKiemtradat());
							ycOld.setTieuchuan(dtDTO.getTieuchuan());
							ycOld.setNguyennhan(dtDTO.getNguyennhan());
							ycOld.setHuonggiaiquyet(dtDTO.getHuonggiaiquyet());
							ycOld.setGiahanluukho(dtDTO.getGiahanluukho());
							ycOld = em.merge(ycOld);
							if (ycOld == null) {
								res = -1;
								messages.append("error update row detail id: " + ycOld.getId() + " - "
										+ ycOld.getProduct().getProduct_code());
								ut.rollback();
								return res;
							}
						}

					}
				}

			}
			res = 0;
			ut.commit();
		} catch (Exception e) {
			e.printStackTrace();
			res = -1;
			messages.append("Không cập nhật được phiếu, kiểm tra log.");
			ut.rollback();
			logger.error("YeuCauKiemTraHangTHService.updateKetQuaKiemTra:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	public int saveOrUpdateYeuCauKiemTraHangService(YeuCauKiemTraHang p,
			List<YeuCauKiemTraHangDetail> yeuCauKiemTraHangDetails, Message messages) throws IllegalStateException,
			SystemException, SQLException {
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
					YeuCauKiemTraHang YeuCauKiemTraHang = em.find(YeuCauKiemTraHang.class, p.getId());
					if (YeuCauKiemTraHang == null) {
						res = -1;
						messages.setUser_message("Phiếu không tồn tại.");
						messages.setInternal_message("error request data!");
						ut.rollback();
						return res;
					}

					// cập nhật phiếu nhập
					p.setLastModifedDate(new Date());
					p = em.merge(p);
					if (p == null) {
						res = -1;
						messages.setUser_message("Cập nhật phiếu không thành công.");
						messages.setInternal_message("");
						ut.rollback();
						return res;
					}
					if (yeuCauKiemTraHangDetails != null && yeuCauKiemTraHangDetails.size() > 0) {
						int stt = 0;
						for (YeuCauKiemTraHangDetail dt : yeuCauKiemTraHangDetails) {
							stt++;
							if (dt != null) {
								// String batchCode=dt.getBatch_code();
								Product product = dt.getProduct();
								if (product != null) {
									// khi người dùng thêm mới phiếu nhập
									if (dt.getId() == 0) {
										// lưu chi tiết phiếu nhập
										dt.setYeuCauKiemTraHang(YeuCauKiemTraHang);
										em.persist(dt);
										if (dt.getId() == 0) {
											res = -1;
											messages.setUser_message("Lưu chi tiết phiếu không thành công.");
											messages.setInternal_message("error detail row " + stt + "-"
													+ product.getProduct_code());
											ut.rollback();
											return res;
										}

										dt = em.merge(dt);
										if (dt == null) {
											res = -1;
											messages.setUser_message("Cập nhật chi tiết phiếu không thành công.");
											messages.setInternal_message("error row detail " + stt + " "
													+ product.getProduct_code());
											ut.rollback();
											return res;
										}
									} else {
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

				} else {// trường hợp thêm mới 1 phiếu
					p.setCreatedDate(new Date());
					em.persist(p);
					if (p.getId() == 0) {
						res = -1;
						messages.setUser_message("Lưu phiếu không thành công.");
						messages.setInternal_message("error persist YeuCauKiemTraHangPos");
						ut.rollback();
						return res;
					}
					int stt = 0;
					for (YeuCauKiemTraHangDetail dt : yeuCauKiemTraHangDetails) {
						stt++;
						Product product = dt.getProduct();
						if (dt.getId() == 0 && product != null && product.getId() != 0) {
							// lưu chi tiết phiếu
							dt.setCreated_date(new Date());
							dt.setYeuCauKiemTraHang(p);
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
			messages.setUser_message("Lưu phiếu không thành công.");
			messages.setInternal_message("YeuCauKiemTraHangTHService.saveOrUpdateYeuCauKiemTraHangService:"
					+ e.getMessage());
			ut.rollback();
			logger.error("YeuCauKiemTraHangTHService.saveOrUpdateYeuCauKiemTraHangService:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

}
