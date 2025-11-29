package lixco.com.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
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
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import lixco.com.commom_ejb.ToolTimeCustomer;
import lixco.com.entity.OrderDetail;
import lixco.com.entity.OrderLix;
import lixco.com.entity.PromotionOrderDetail;
import lixco.com.entity.PromotionProgram;
import lixco.com.entity.PromotionProgramDetail;
import lixco.com.entity.PromotionalPricing;
import lixco.com.interfaces.IProcessLogicOrderService;
import lixco.com.reqInfo.Message;
import lixco.com.reqInfo.WrapOrderDetailNppReqInfo;
import lixco.com.reqInfo.WrapOrderNppReqInfo;
import lixco.com.reqInfo.WrapPMOrderDetailReqInfo;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.BEAN)
public class ProcessLogicOrderService implements IProcessLogicOrderService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource(lookup = "java:/consumption")
	DataSource datasource;
	@Resource
	private UserTransaction ut;

	@Override
	public int saveOrUpdate(WrapOrderNppReqInfo data, StringBuilder messages) throws IllegalStateException,
			SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			OrderLix orderLix = data.getOrder_lix();
			List<OrderDetail> listDetail = data.getList_order_detail();
			List<PromotionOrderDetail> listPromotion = data.getList_promotion_order_detail();
			// check orderlix npp đã tồn tại chưa
			Query query = em.createQuery("select d.id from OrderLix as d where d.npp_order_id=:id ");
			query.setParameter("id", orderLix.getNpp_order_id());
			List<Long> listId = query.getResultList();
			if (listId.size() > 0) {
				// đã tồn tại thì update
				// xóa đơn hàng củ nạp lại đơn hàng mới
				long id = listId.get(0);
				// xóa chi tiết khuyến mãi của đơn hàng cũ
				Query queryDelPromotionOrderDetail = em
						.createQuery("delete PromotionOrderDetail as p where p.order_detail.id in (select dt.id from OrderDetail as dt where dt.order_lix.id=:id ) ");
				queryDelPromotionOrderDetail.setParameter("id", id);
				if (queryDelPromotionOrderDetail.executeUpdate() < 0) {
					res = -1;
					messages.append("promotion order detail not update ");
					ut.rollback();
					return res;
				}
				// xóa chi tiết đơn hàng củ
				Query querydelCT = em.createQuery("delete OrderDetail as d where d.order_lix.id= :id ");
				querydelCT.setParameter("id", id);
				if (querydelCT.executeUpdate() < 0) {
					res = -1;
					messages.append("order detail not update ");
					ut.rollback();
					return res;
				}
				Query queryDelOrder = em.createQuery("delete OrderLix as d where d.id=:id");
				queryDelOrder.setParameter("id", id);
				if (queryDelOrder.executeUpdate() < 0) {
					res = -1;
					ut.rollback();
					messages.append("orders not update");
					return res;
				}
				// tiến hành tạo lại dữ liệu
			}
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cqOrderCoder = cb.createQuery(Integer.class);
			Root<OrderLix> root = cqOrderCoder.from(OrderLix.class);
			// cq.multiselect(cb.coalesce(cb.max(root.get("id")),0));
			cqOrderCoder.multiselect(cb.coalesce(cb.max(cb.quot((Expression) root.get("order_code"), 1)), 0));
			TypedQuery<Integer> queryOrderCode = em.createQuery(cqOrderCoder);
			int max = queryOrderCode.getSingleResult();
			double p = (double) max / 100000000;
			if (p < 1) {
				orderLix.setOrder_code(String.format("%08d", max + 1));
			} else {
				orderLix.setOrder_code(String.format("%08d", max + 1));
			}
			if (orderLix.getVoucher_code() == null && "".equals(orderLix.getVoucher_code())) {
				Date orderDate = orderLix.getOrder_date();
				int year = ToolTimeCustomer.getYearM(orderDate);
				int month = ToolTimeCustomer.getMonthM(orderDate);
				Query queryVoucherCode = em
						.createNativeQuery("select max(replace(replace(d.voucher_code,'-',''),'/','')) from orderlix as d where d.order_date >= :fd");
				queryVoucherCode.setParameter("fd", ToolTimeCustomer.convertDateToString(
						ToolTimeCustomer.getDateMinCustomer(1, year), "yyyy-MM-dd"));
				List<Object> listDt = queryVoucherCode.getResultList();
				if (listDt.size() > 0) {
					String voucher = Objects.toString(listDt.get(0), null);
					if (voucher != null) {
						voucher = voucher.substring(6);
						voucher = year + "-" + String.format("%02d", month) + "/" + voucher;
						orderLix.setVoucher_code(voucher);
					} else {
						orderLix.setVoucher_code(year + "-" + String.format("%02d", month) + "/00001");
					}
				} else {
					orderLix.setVoucher_code(year + "-" + String.format("%02d", month) + "/00001");
				}
			}
			em.persist(orderLix);
			if (orderLix.getId() == 0) {
				res = -1;
				messages.append("orders are not saved");
				ut.rollback();
				return res;
			}
			for (OrderDetail d : listDetail) {
				Query query2 = em.createQuery("select count(id) from OrderDetail where npp_order_detail_id=:id ");
				query2.setParameter("id", d.getNpp_order_detail_id());
				int count2 = Integer.parseInt(Objects.toString(query2.getSingleResult()));
				if (count2 > 0) {
					res = -1;
					messages.append("npp_order_detail_id duplicate");
					ut.rollback();
					return res;
				}
				em.persist(d);
				if (d.getId() == 0) {
					res = -1;
					messages.append("Order details are not saved");
					ut.rollback();
					return res;
				}
			}
			if (listPromotion != null) {
				for (PromotionOrderDetail p1 : listPromotion) {
					em.persist(p1);
					if (p1.getId() == 0) {
						res = -1;
						messages.append("Promotional order details are not saved");
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
			messages.append("ProcessLogicOrderService.insert(WrapOrderNppReqInfo data):" + e.getMessage());
			logger.error("ProcessLogicOrderService.insert(WrapOrderNppReqInfo data):" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int deleteNppOrder(long nppOrderId, StringBuilder messages) throws IllegalStateException, SystemException,
			SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			Query query = em.createQuery("select d.id from OrderLix as d where d.npp_order_id=:id ");
			query.setParameter("id", nppOrderId);
			List<Long> listId = query.getResultList();
			if (listId.size() > 0) {
				long id = listId.get(0);
				// xóa chi tiết khuyến mãi của đơn hàng cũ
				Query queryDelPromotionOrderDetail = em
						.createQuery("delete PromotionOrderDetail as p where p.order_detail.id in (select dt.id from OrderDetail as dt where dt.order_lix.id=:id ) ");
				queryDelPromotionOrderDetail.setParameter("id", id);
				if (queryDelPromotionOrderDetail.executeUpdate() < 0) {
					res = -1;
					messages.append("promotion order detail not delete ");
					ut.rollback();
					return res;
				}
				// xóa chi tiết đơn hàng củ
				Query querydelCT = em.createQuery("delete OrderDetail as d where d.order_lix.id= :id ");
				querydelCT.setParameter("id", id);
				if (querydelCT.executeUpdate() < 0) {
					res = -1;
					messages.append("order detail not delete ");
					ut.rollback();
					return res;
				}
				Query queryDelOrder = em.createQuery("delete OrderLix as d where d.id=:id");
				queryDelOrder.setParameter("id", id);
				if (queryDelOrder.executeUpdate() < 0) {
					res = -1;
					ut.rollback();
					messages.append("orders not delete");
					return res;
				}
			} else {
				res = -1;
				ut.rollback();
				messages.append("npp_order_id not found");
				return res;
			}
			res = 0;
			ut.commit();
		} catch (Exception e) {
			res = -1;
			ut.rollback();
			messages.append("ProcessLogicOrderService.deleteNppOrder(nppOrderId,messages):" + e.getMessage());
			logger.error("ProcessLogicOrderService.deleteNppOrder(nppOrderId,messages):" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int saveOrUpdateDetailNpp(WrapOrderDetailNppReqInfo data, StringBuilder messages)
			throws IllegalStateException, SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			OrderDetail orderDetail = data.getOrder_detail();
			List<PromotionOrderDetail> listPromotion = data.getList_promotion_order_detail();
			// xóa chi tiết khuyến mãi của đơn hàng cũ
			Query queryOrderDetail = em.createQuery("select id from OrderDetail where npp_order_detail_id= :nid");
			queryOrderDetail.setParameter("id", orderDetail.getNpp_order_detail_id());
			List<Long> listId = queryOrderDetail.getResultList();
			if (listId.size() > 0) {
				long idDetail = listId.get(0);
				Query queryDelPromotionOrderDetail = em
						.createQuery("delete PromotionOrderDetail as p where p.order_detail.id =:idd ");
				queryDelPromotionOrderDetail.setParameter("idd", idDetail);
				if (queryDelPromotionOrderDetail.executeUpdate() < 0) {
					res = -1;
					messages.append("promotion order detail not update ");
					ut.rollback();
					return res;
				}
				// xóa chi tiết đơn hàng củ
				Query querydelCT = em.createQuery("delete OrderDetail as d where d.id= :idd ");
				querydelCT.setParameter("idd", idDetail);
				if (querydelCT.executeUpdate() < 0) {
					res = -1;
					messages.append("order detail not update ");
					ut.rollback();
					return res;
				}
				em.persist(orderDetail);
				if (orderDetail.getId() == 0) {
					res = -1;
					messages.append("Order details are not saved");
					ut.rollback();
					return res;
				}
				if (listPromotion != null) {
					for (PromotionOrderDetail p1 : listPromotion) {
						em.persist(p1);
						if (p1.getId() == 0) {
							res = -1;
							messages.append("Promotional order details are not saved");
							ut.rollback();
							return res;
						}
					}
				}
			} else {
				res = -1;
				messages.append("npp_order_detail_id not found!");
				ut.rollback();
				return res;
			}
			res = 0;
			ut.commit();
		} catch (Exception e) {
			res = -1;
			ut.rollback();
			messages.append("ProcessLogicOrderService.saveOrUpdateDetailNpp(data,messages):" + e.getMessage());
			logger.error("ProcessLogicOrderService.saveOrUpdateDetailNpp(data,messages):" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int deleteNppOrderDetail(long nppOrderDetailId, StringBuilder messages) throws IllegalStateException,
			SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			Query queryOrderDetail = em.createQuery("select id from OrderDetail where npp_order_detail_id=:id");
			queryOrderDetail.setParameter("id", nppOrderDetailId);
			List<Long> listId = queryOrderDetail.getResultList();
			if (listId.size() > 0) {
				long id = listId.get(0);
				Query queryDelPromotionOrderDetail = em
						.createQuery("delete PromotionOrderDetail as p where p.order_detail.id =:idd ");
				queryDelPromotionOrderDetail.setParameter("idd", id);
				if (queryDelPromotionOrderDetail.executeUpdate() < 0) {
					res = -1;
					messages.append("promotion order detail not delete ");
					ut.rollback();
					return res;
				}
				// xóa chi tiết đơn hàng củ
				Query querydelCT = em.createQuery("delete OrderDetail as d where d.id= :idd ");
				querydelCT.setParameter("idd", id);
				if (querydelCT.executeUpdate() < 0) {
					res = -1;
					messages.append("order detail not delete ");
					ut.rollback();
					return res;
				}
				res = 0;
				ut.commit();
			} else {
				res = -1;
				ut.rollback();
				messages.append("npp_order_detail_id not found");
				return res;
			}
		} catch (Exception e) {
			res = -1;
			ut.rollback();
			messages.append("ProcessLogicOrderService.deleteNppOrderDetail(nppOrderDetailId,messages):"
					+ e.getMessage());
			logger.error("ProcessLogicOrderService.saveOrUpdateDetailNpp(nppOrderDetailId,messages):" + e.getMessage(),
					e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int deleteOrderDetail(long id, Message message) throws IllegalStateException, SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<OrderDetail> cqOrderdetail = cb.createQuery(OrderDetail.class);
			Root<OrderDetail> rootOrderDetail = cqOrderdetail.from(OrderDetail.class);
			// rootOrderDetail.fetch("order_lix",JoinType.INNER);
			cqOrderdetail.select(rootOrderDetail).where(cb.equal(rootOrderDetail.get("id"), id));
			TypedQuery<OrderDetail> queryOrderDetail = em.createQuery(cqOrderdetail);
			List<OrderDetail> listOrderDetailTrans = queryOrderDetail.getResultList();
			if (listOrderDetailTrans.size() == 0) {
				res = -1;
				ut.rollback();
				message.setUser_message("Không tìm thấy chi tiết đơn hàng");
				message.setInternal_message("error OrderDetail not found id:" + id);
				return res;
			}
			OrderDetail orderDetailTrans = listOrderDetailTrans.get(0);
			// kiểm tra đơn hàng đã chuyển qua phiếu xuất chưa nếu chuyển qua
			// rồi không được xóa.
			// Query
			// queryCountInvoice=em.createQuery("select count(p.id) from Invoice p where p.order_lix.id=(select d.order_lix.id from OrderDetail d where d.id=:id) ");
			// queryCountInvoice.setParameter("id", id);
			// int
			// countInvoice=Integer.parseInt(Objects.toString(queryCountInvoice.getSingleResult(),"0"));
			// if(countInvoice >0){
			// res=-1;
			// message.setUser_message("Đơn hàng đã chuyển qua phiếu xuất không được xóa");
			// message.setInternal_message("");
			// ut.rollback();
			// return res;
			// }
			// xóa chi tiết đơn hàng khuyến mãi của chi tiết đơn hàng
			Query queryDelPromotionOrderDetail = em
					.createQuery("delete from PromotionOrderDetail p where p.order_detail.id=:id ");
			queryDelPromotionOrderDetail.setParameter("id", orderDetailTrans.getId());
			if (queryDelPromotionOrderDetail.executeUpdate() < 0) {
				res = -1;
				message.setUser_message("Xóa chi tiết đơn hàng khuyến mãi thất bại");
				message.setInternal_message("");
				ut.rollback();
				return res;
			}
			// xóa chi tiết đơn hàng
			Query queryDelOrderDetail = em.createQuery("delete from OrderDetail p where p.id=:id");
			queryDelOrderDetail.setParameter("id", orderDetailTrans.getId());
			if (queryDelOrderDetail.executeUpdate() <= 0) {
				res = -1;
				message.setUser_message("Xóa chi tiết đơn hàng thất bại");
				message.setInternal_message("");
				ut.rollback();
				return res;
			}
			ut.commit();
			res = 0;
		} catch (Exception e) {
			res = -1;
			ut.rollback();
			message.setUser_message("Xóa chi tiết đơn hàng thất bại!");
			message.setInternal_message("ProcessLogicOrderService.deleteOrderDetail:");
			logger.error("ProcessLogicOrderService.deleteOrderDetail:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int deleteOrderLix(long id, Message message) throws IllegalStateException, SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			OrderLix orderLixTrans = em.find(OrderLix.class, id);
			if (orderLixTrans == null) {
				res = 0;
				message.setUser_message("Không tìm thấy đơn hàng");
				message.setInternal_message("error OrderLix not found");
				ut.rollback();
				return res;
			}
			// Nếu đơn hàng đã chuyển qua hóa đơn thì không được xóa.
			Query queryCountInvoice = em.createQuery("select count(d.id) from Invoice as d where d.order_lix.id=:id");
			queryCountInvoice.setParameter("id", id);
			int countInvoice = Integer.parseInt(Objects.toString(queryCountInvoice.getSingleResult(), "0"));
			if (countInvoice > 0) {
				res = -1;
				message.setUser_message("Đơn hàng đã chuyển qua phiếu xuất không được xóa");
				message.setInternal_message("");
				ut.rollback();
				return res;
			}
			// xóa chi tiết đơn hàng khuyến mãi
			Query queryDelPromotionOrderDetail = em
					.createQuery("delete from PromotionOrderDetail p where p.order_detail.id in (select d.id from OrderDetail as d where d.order_lix.id=:id)");
			queryDelPromotionOrderDetail.setParameter("id", id);
			if (queryDelPromotionOrderDetail.executeUpdate() < 0) {
				res = -1;
				message.setUser_message("Xóa chi tiết đơn hàng khuyến mãi thất bại");
				message.setInternal_message("");
				ut.rollback();
				return res;
			}
			// xóa chi tiết của đơn hàng
			Query queryDelOrderDetail = em.createQuery("delete from OrderDetail d where d.order_lix.id=:id");
			queryDelOrderDetail.setParameter("id", id);
			if (queryDelOrderDetail.executeUpdate() < 0) {
				res = -1;
				message.setUser_message("Xóa chi tiết đơn hàng thất bại");
				message.setInternal_message("");
				ut.rollback();
				return res;
			}
			// xóa đơn hàng
			Query queryDelOrder = em.createQuery("delete from OrderLix where id=:id");
			queryDelOrder.setParameter("id", id);
			if (queryDelOrder.executeUpdate() < 0) {
				res = -1;
				message.setUser_message("Xóa đơn hàng thất bại");
				message.setInternal_message("");
				return res;
			}
			ut.commit();
			res = 0;
		} catch (Exception e) {
			res = -1;
			ut.rollback();
			message.setUser_message("Xóa đơn hàng thất bại!");
			message.setInternal_message("ProcessLogicOrderService.deleteOrderLix:" + e.getMessage());
			logger.error("ProcessLogicOrderService.deleteOrderLix:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int insertOrUpdateOrderDetail(WrapPMOrderDetailReqInfo t, Message message) throws IllegalStateException,
			SystemException, SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			OrderDetail orderDetail = t.getOrder_detail();
			OrderLix orderLix = orderDetail.getOrder_lix();
			String memberName = t.getMemberName();
			CriteriaQuery<OrderLix> cqOrder = cb.createQuery(OrderLix.class);
			Root<OrderLix> rootOrder = cqOrder.from(OrderLix.class);
			rootOrder.fetch("promotion_program", JoinType.LEFT);
			rootOrder.fetch("pricing_program", JoinType.LEFT);
			cqOrder.select(rootOrder).where(cb.equal(rootOrder.get("id"), orderLix.getId()));
			TypedQuery<OrderLix> queryOrder = em.createQuery(cqOrder);
			List<OrderLix> listOrderTrans = queryOrder.getResultList();
			if (listOrderTrans.size() == 0) {
				res = -1;
				message.setUser_message("Chưa lưu đơn hàng.");
				message.setInternal_message("error OrderLix not found id:" + orderLix.getId());
				ut.rollback();
				return res;
			}
			OrderLix orderLixTrans = listOrderTrans.get(0);

			if (orderDetail.getId() == 0) {
				orderDetail.setCreated_date(new Date());
				orderDetail.setCreated_by(memberName);
				em.persist(orderDetail);
				if (orderDetail.getId() == 0) {
					res = -1;
					message.setUser_message("Xảy ra lỗi khi lưu chi tiết.");
					message.setInternal_message("error persist OrderDetail");
					ut.rollback();
					return res;
				}
				PromotionProgram promotionProgramTrans = orderLixTrans.getPromotion_program();
				Date deliveryDate = orderLixTrans.getDelivery_date();
				if (orderDetail.getPromotion_forms() != 0 && promotionProgramTrans != null) {

					// Tải cài đặt chương trình khuyến mãi
					CriteriaQuery<PromotionProgramDetail> cqPPD = cb.createQuery(PromotionProgramDetail.class);
					Root<PromotionProgramDetail> rootPPD_ = cqPPD.from(PromotionProgramDetail.class);
					rootPPD_.fetch("promotion_product", JoinType.INNER);
					Predicate con1 = cb.conjunction();
					con1.getExpressions().add(
							cb.equal(rootPPD_.get("product").get("id"), orderDetail.getProduct().getId()));
					con1.getExpressions().add(
							cb.equal(rootPPD_.get("promotion_program").get("id"), promotionProgramTrans.getId()));
					con1.getExpressions().add(
							cb.equal(rootPPD_.get("promotion_form"), orderDetail.getPromotion_forms()));
					cqPPD.select(rootPPD_).where(con1);
					TypedQuery<PromotionProgramDetail> queryPPD = em.createQuery(cqPPD);
					List<PromotionProgramDetail> listPPDTrans = queryPPD.getResultList();
					for (PromotionProgramDetail ppd : listPPDTrans) {
						PromotionOrderDetail pod = new PromotionOrderDetail();
						pod.setOrder_detail(orderDetail);
						pod.setProduct(ppd.getPromotion_product());

						// tìm và cài đặt đơn giá sản phẩm khuyến mãi (cai don
						// gia cho
						// danh sach khuyen mai)
						CriteriaQuery<PromotionalPricing> cqPromotionPricing = cb.createQuery(PromotionalPricing.class);
						Root<PromotionalPricing> rootPromotionPricing_ = cqPromotionPricing
								.from(PromotionalPricing.class);
						Predicate cons = cb.conjunction();
						cons.getExpressions().add(
								cb.lessThanOrEqualTo(rootPromotionPricing_.get("effective_date"), deliveryDate));
						cons.getExpressions().add(
								cb.greaterThanOrEqualTo(rootPromotionPricing_.get("expiry_date"), deliveryDate));
						cons.getExpressions().add(
								cb.equal(rootPromotionPricing_.get("product").get("id"), ppd.getPromotion_product()
										.getId()));
						cqPromotionPricing.select(rootPromotionPricing_).where(cons)
								.orderBy(cb.desc(rootPromotionPricing_.get("effective_date")));
						TypedQuery<PromotionalPricing> queryPromotionPricing = em.createQuery(cqPromotionPricing);
						List<PromotionalPricing> listPromotionPricingTrans = queryPromotionPricing.getResultList();
						if (listPromotionPricingTrans.size() > 0) {
							pod.setUnit_price(listPromotionPricingTrans.get(0).getUnit_price());
						}
//						int quantity = (int) (orderDetail.getBox_quantity() / ppd.getBox_quatity())
//								* (int) ppd.getSpecification();
						double quantity = (double)((int) (orderDetail.getBox_quantity() / ppd.getBox_quatity()))
								* ppd.getSpecification();
						pod.setQuantity(quantity);
//						int quantityAct = (int) (orderDetail.getBox_quantity_actual() / ppd.getBox_quatity())
//								* (int) ppd.getSpecification();
//						pod.setQuantityAct(quantityAct);
						pod.setCreated_date(new Date());
						pod.setCreated_by(memberName);
						if (quantity == 0) {

						} else {
							em.persist(pod);
							if (pod.getId() == 0) {
								res = -1;
								message.setUser_message("Không tạo khuyến mãi.");
								message.setInternal_message("");
								ut.rollback();
								return res;
							}
						}
					}
				}

			} else {
				// update
				// load lại chi tiết đơn hàng
				OrderDetail orderDetailTrans = em.find(OrderDetail.class, orderDetail.getId());
				if (orderDetailTrans == null) {
					res = -1;
					message.setUser_message("Chi tiết đơn hàng đã xóa, vui lòng tải lại đơn hàng");
					message.setInternal_message("");
					ut.rollback();
					return res;
				}
				// kiểm tra khi update sản phẩm có bị trùng với sản phẩm nào
				// trong đơn hàng không
				// Query
				// queryCountProduct=em.createQuery("select count(d.id) from OrderDetail d where d.order_lix.id=:id and d.product.id=:p and d.id <> :di ");
				// queryCountProduct.setParameter("id",orderLixTrans.getId());
				// queryCountProduct.setParameter("p",orderDetail.getProduct().getId());
				// queryCountProduct.setParameter("di",orderDetail.getId());
				// int
				// countProduct=Integer.parseInt(Objects.toString(queryCountProduct.getSingleResult(),"0"));
				// if(countProduct >0){
				// res=-1;
				// message.setUser_message("Sản phẩm đã có trong đơn hàng");
				// message.setInternal_message("");
				// ut.rollback();
				// return res;
				// }
				// update chi tiết đơn hàng
				Query queryUpdate = em
						.createQuery("update OrderDetail set last_modifed_date=:lmd,last_modifed_by=:lmb,product.id=:p,box_quantity=:bq,box_quantity_actual=:bqac,quantity=:q,total=:tt,unit_price=:up,promotion_forms=:f,batch_code=:bc,container_number=:cn,seal_number=:sn where id=:id ");
				queryUpdate.setParameter("lmd", new Date());
				queryUpdate.setParameter("lmb", memberName);
				queryUpdate.setParameter("p", orderDetail.getProduct().getId());
				queryUpdate.setParameter("bq", orderDetail.getBox_quantity());
				queryUpdate.setParameter("bqac", orderDetail.getBox_quantity_actual());
				queryUpdate.setParameter("q", orderDetail.getQuantity());
				queryUpdate.setParameter("up", orderDetail.getUnit_price());
				queryUpdate.setParameter("tt", orderDetail.getTotal());
				queryUpdate.setParameter("f", orderDetail.getPromotion_forms());
				queryUpdate.setParameter("bc", orderDetail.getBatch_code());
				queryUpdate.setParameter("cn", orderDetail.getContainer_number());
				queryUpdate.setParameter("sn", orderDetail.getSeal_number());
				queryUpdate.setParameter("id", orderDetail.getId());
				if (queryUpdate.executeUpdate() <= 0) {
					res = -1;
					message.setUser_message("Xảy ra lỗi khi lưu chi tiết.");
					message.setInternal_message("");
					ut.rollback();
					return res;
				}
				// xóa chi tiết đơn hàng khuyến mãi
				Query queryDelPromotionOrderDetail = em
						.createQuery("delete from PromotionOrderDetail where order_detail.id=:id ");
				queryDelPromotionOrderDetail.setParameter("id", orderDetail.getId());
				if (queryDelPromotionOrderDetail.executeUpdate() < 0) {
					res = -1;
					message.setUser_message("Không xóa được chi tiết khuyến mãi.");
					message.setInternal_message("");
					ut.rollback();
					return res;
				}
				// tạo lại chi tiết sản phẩm khuyến mãi
				PromotionProgram promotionProgramTrans = orderLixTrans.getPromotion_program();
				Date deliveryDate = orderLixTrans.getDelivery_date();
				if (orderDetail.getPromotion_forms() != 0 && promotionProgramTrans != null) {
					CriteriaQuery<PromotionProgramDetail> cqPPD = cb.createQuery(PromotionProgramDetail.class);
					Root<PromotionProgramDetail> rootPPD_ = cqPPD.from(PromotionProgramDetail.class);
					rootPPD_.fetch("promotion_product", JoinType.INNER);
					Predicate con1 = cb.conjunction();
					con1.getExpressions().add(
							cb.equal(rootPPD_.get("product").get("id"), orderDetail.getProduct().getId()));
					con1.getExpressions().add(
							cb.equal(rootPPD_.get("promotion_program").get("id"), promotionProgramTrans.getId()));
					con1.getExpressions().add(
							cb.equal(rootPPD_.get("promotion_form"), orderDetail.getPromotion_forms()));
					cqPPD.select(rootPPD_).where(con1);
					TypedQuery<PromotionProgramDetail> queryPPD = em.createQuery(cqPPD);
					List<PromotionProgramDetail> listPPDTrans = queryPPD.getResultList();
					for (PromotionProgramDetail ppd : listPPDTrans) {
						PromotionOrderDetail pod = new PromotionOrderDetail();
						pod.setOrder_detail(orderDetail);
						pod.setProduct(ppd.getPromotion_product());
						// tìm nạp đơn giá sản phẩm khuyến mãi
						CriteriaQuery<PromotionalPricing> cqPromotionPricing = cb.createQuery(PromotionalPricing.class);
						Root<PromotionalPricing> rootPromotionPricing_ = cqPromotionPricing
								.from(PromotionalPricing.class);
						Predicate cons = cb.conjunction();
						cons.getExpressions().add(
								cb.lessThanOrEqualTo(rootPromotionPricing_.get("effective_date"), deliveryDate));
						cons.getExpressions().add(
								cb.greaterThanOrEqualTo(rootPromotionPricing_.get("expiry_date"), deliveryDate));
						cons.getExpressions().add(
								cb.equal(rootPromotionPricing_.get("product").get("id"), ppd.getPromotion_product()
										.getId()));
						cqPromotionPricing.select(rootPromotionPricing_).where(cons)
								.orderBy(cb.desc(rootPromotionPricing_.get("effective_date")));
						TypedQuery<PromotionalPricing> queryPromotionPricing = em.createQuery(cqPromotionPricing);
						List<PromotionalPricing> listPromotionPricingTrans = queryPromotionPricing.getResultList();
						if (listPromotionPricingTrans.size() > 0) {
							pod.setUnit_price(listPromotionPricingTrans.get(0).getUnit_price());
						}

//						int quantity = (int) (orderDetail.getBox_quantity() / ppd.getBox_quatity())
//								* (int) ppd.getSpecification();
						double quantity = (double)((int) (orderDetail.getBox_quantity() / ppd.getBox_quatity()))
								* ppd.getSpecification();
						pod.setQuantity(quantity);
						// int
						// quantityAct=(int)(orderDetail.getBox_quantity_actual()/ppd.getBox_quatity())*(int)ppd.getSpecification();
						// pod.setQuantityAct(quantityAct);

						pod.setCreated_date(new Date());
						pod.setCreated_by(memberName);
						if (quantity == 0) {

						} else {
							em.persist(pod);
							if (pod.getId() == 0) {
								res = -1;
								message.setUser_message("Không tạo được chi tiết khuyến mãi.");
								message.setInternal_message("");
								ut.rollback();
								return res;
							}
						}
					}
				}

			}
			ut.commit();
			res = 0;
		} catch (Exception e) {
			res = -1;
			ut.rollback();
			message.setUser_message("Lưu chi tiết đơn hàng thất bại!");
			message.setInternal_message("ProcessLogicOrderService.insertOrUpdateOrderDetail:");
			logger.error("ProcessLogicOrderService.insertOrUpdateOrderDetail:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int updatePrice(WrapPMOrderDetailReqInfo t, Message message) throws IllegalStateException, SystemException,
			SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			OrderDetail orderDetail = t.getOrder_detail();
			// update
			// load lại chi tiết đơn hàng
			OrderDetail orderDetailTrans = em.find(OrderDetail.class, orderDetail.getId());
			if (orderDetailTrans == null) {
				res = -1;
				message.setUser_message("Chi tiết đơn hàng đã xóa, vui lòng tải lại đơn hàng");
				message.setInternal_message("");
				ut.rollback();
				return res;
			}
			// update chi tiết đơn hàng
			Query queryUpdate = em
					.createQuery("update OrderDetail set last_modifed_date=:lmd,last_modifed_by=:lmb,product.id=:p,box_quantity=:bq,box_quantity_actual=:bqac,quantity=:q,total=:tt,unit_price=:up,promotion_forms=:f,batch_code=:bc,container_number=:cn,seal_number=:sn where id=:id ");
			queryUpdate.setParameter("lmd", new Date());
			queryUpdate.setParameter("lmb", t.getMemberName());
			queryUpdate.setParameter("p", orderDetail.getProduct().getId());
			queryUpdate.setParameter("bq", orderDetail.getBox_quantity());
			queryUpdate.setParameter("bqac", orderDetail.getBox_quantity_actual());
			queryUpdate.setParameter("q", orderDetail.getQuantity());
			queryUpdate.setParameter("up", orderDetail.getUnit_price());
			queryUpdate.setParameter("tt", orderDetail.getTotal());
			queryUpdate.setParameter("f", orderDetail.getPromotion_forms());
			queryUpdate.setParameter("bc", orderDetail.getBatch_code());
			queryUpdate.setParameter("cn", orderDetail.getContainer_number());
			queryUpdate.setParameter("sn", orderDetail.getSeal_number());
			queryUpdate.setParameter("id", orderDetail.getId());
			if (queryUpdate.executeUpdate() <= 0) {
				res = -1;
				message.setUser_message("Xảy ra lỗi khi lưu chi tiết.");
				message.setInternal_message("");
				ut.rollback();
				return res;
			}
			ut.commit();
			res = 0;
		} catch (Exception e) {
			res = -1;
			ut.rollback();
			message.setUser_message("Lưu chi tiết đơn hàng thất bại!");
			message.setInternal_message("ProcessLogicOrderService.insertOrUpdateOrderDetail:");
			logger.error("ProcessLogicOrderService.insertOrUpdateOrderDetail:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int updatePromo(WrapPMOrderDetailReqInfo t, Message message) throws IllegalStateException, SystemException,
			SQLException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			OrderDetail orderDetail = t.getOrder_detail();
			OrderLix orderLix = orderDetail.getOrder_lix();
			String memberName = t.getMemberName();
			CriteriaQuery<OrderLix> cqOrder = cb.createQuery(OrderLix.class);
			Root<OrderLix> rootOrder = cqOrder.from(OrderLix.class);
			rootOrder.fetch("promotion_program", JoinType.LEFT);
			rootOrder.fetch("pricing_program", JoinType.LEFT);
			cqOrder.select(rootOrder).where(cb.equal(rootOrder.get("id"), orderLix.getId()));
			TypedQuery<OrderLix> queryOrder = em.createQuery(cqOrder);
			List<OrderLix> listOrderTrans = queryOrder.getResultList();
			if (listOrderTrans.size() == 0) {
				res = -1;
				message.setUser_message("Chưa lưu đơn hàng.");
				message.setInternal_message("error OrderLix not found id:" + orderLix.getId());
				ut.rollback();
				return res;
			}
			OrderLix orderLixTrans = listOrderTrans.get(0);

			// xóa chi tiết đơn hàng khuyến mãi
			Query queryDelPromotionOrderDetail = em
					.createQuery("delete from PromotionOrderDetail where order_detail.id=:id ");
			queryDelPromotionOrderDetail.setParameter("id", orderDetail.getId());
			if (queryDelPromotionOrderDetail.executeUpdate() < 0) {
				res = -1;
				message.setUser_message("Không xóa được chi tiết khuyến mãi.");
				message.setInternal_message("");
				ut.rollback();
				return res;
			}
			// tạo lại chi tiết sản phẩm khuyến mãi
			PromotionProgram promotionProgramTrans = orderLixTrans.getPromotion_program();
			Date deliveryDate = orderLixTrans.getDelivery_date();
			if (orderDetail.getPromotion_forms() != 0 && promotionProgramTrans != null) {
				CriteriaQuery<PromotionProgramDetail> cqPPD = cb.createQuery(PromotionProgramDetail.class);
				Root<PromotionProgramDetail> rootPPD_ = cqPPD.from(PromotionProgramDetail.class);
				rootPPD_.fetch("promotion_product", JoinType.INNER);
				Predicate con1 = cb.conjunction();
				con1.getExpressions()
						.add(cb.equal(rootPPD_.get("product").get("id"), orderDetail.getProduct().getId()));
				con1.getExpressions().add(
						cb.equal(rootPPD_.get("promotion_program").get("id"), promotionProgramTrans.getId()));
				con1.getExpressions().add(cb.equal(rootPPD_.get("promotion_form"), orderDetail.getPromotion_forms()));
				cqPPD.select(rootPPD_).where(con1);
				TypedQuery<PromotionProgramDetail> queryPPD = em.createQuery(cqPPD);
				List<PromotionProgramDetail> listPPDTrans = queryPPD.getResultList();
				for (PromotionProgramDetail ppd : listPPDTrans) {
					PromotionOrderDetail pod = new PromotionOrderDetail();
					pod.setOrder_detail(orderDetail);
					pod.setProduct(ppd.getPromotion_product());
					// tìm nạp đơn giá sản phẩm khuyến mãi
					CriteriaQuery<PromotionalPricing> cqPromotionPricing = cb.createQuery(PromotionalPricing.class);
					Root<PromotionalPricing> rootPromotionPricing_ = cqPromotionPricing.from(PromotionalPricing.class);
					Predicate cons = cb.conjunction();
					cons.getExpressions().add(
							cb.lessThanOrEqualTo(rootPromotionPricing_.get("effective_date"), deliveryDate));
					cons.getExpressions().add(
							cb.greaterThanOrEqualTo(rootPromotionPricing_.get("expiry_date"), deliveryDate));
					cons.getExpressions()
							.add(cb.equal(rootPromotionPricing_.get("product").get("id"), ppd.getPromotion_product()
									.getId()));
					cqPromotionPricing.select(rootPromotionPricing_).where(cons)
							.orderBy(cb.desc(rootPromotionPricing_.get("effective_date")));
					TypedQuery<PromotionalPricing> queryPromotionPricing = em.createQuery(cqPromotionPricing);
					List<PromotionalPricing> listPromotionPricingTrans = queryPromotionPricing.getResultList();
					if (listPromotionPricingTrans.size() > 0) {
						pod.setUnit_price(listPromotionPricingTrans.get(0).getUnit_price());
					}

//					int quantity = (int) (orderDetail.getBox_quantity() / ppd.getBox_quatity())
//							* (int) ppd.getSpecification();
					
					double quantity = (double)((int) (orderDetail.getBox_quantity() / ppd.getBox_quatity()))
							* ppd.getSpecification();
					pod.setQuantity(quantity);
					pod.setCreated_date(new Date());
					pod.setCreated_by(memberName);
					if (quantity == 0) {

					} else {
						em.persist(pod);
						if (pod.getId() == 0) {
							res = -1;
							message.setUser_message("Không tạo được chi tiết khuyến mãi.");
							message.setInternal_message("");
							ut.rollback();
							return res;
						}
					}
				}
			}
			ut.commit();
			res = 0;
		} catch (Exception e) {
			res = -1;
			ut.rollback();
			message.setUser_message("Lưu chi tiết đơn hàng thất bại!");
			message.setInternal_message("ProcessLogicOrderService.insertOrUpdateOrderDetail:");
			logger.error("ProcessLogicOrderService.insertOrUpdateOrderDetail:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}
}
