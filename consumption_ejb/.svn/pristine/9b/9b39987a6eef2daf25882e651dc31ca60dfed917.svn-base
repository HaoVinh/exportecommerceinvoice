package lixco.com.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.commom_ejb.MyMath;
import lixco.com.commom_ejb.MyUtilEJB;
import lixco.com.entity.Contract;
import lixco.com.entity.ContractDetail;
import lixco.com.entity.ContractReqInfo;
import lixco.com.entity.IEInvoice;
import lixco.com.entity.IEInvoiceDetail;
import lixco.com.entity.Invoice;
import lixco.com.entity.Product;
import lixco.com.interfaces.IContractService;
import lixco.com.interfaces.IIEInvoiceService;
import lixco.com.interfaces.IProcessLogicIEInvoiceService;
import lixco.com.reqInfo.Message;
import lixco.com.reqInfo.ProcessContractIEInvoice;
import lixco.com.reqInfo.WrapDataIEInvoiceReqInfo;
import lixco.com.reqInfo.WrapIEInvoiceDetailReqInfo;
import lixco.com.reqInfo.WrapProcessContractIEInvoiceReqInfo;

@Stateless
@TransactionManagement(value = TransactionManagementType.BEAN)
public class ProcessLogicIEInvoiceService implements IProcessLogicIEInvoiceService {
	@Inject
	private EntityManager em;
	@Inject
	private IContractService contractService;
	@Inject
	private Logger logger;
	@Resource(lookup = "java:/consumption")
	DataSource datasource;
	@Resource
	private UserTransaction ut;

	@Inject
	IIEInvoiceService iEInvoiceService;

	@Override
	public int saveListProcessContract(WrapProcessContractIEInvoiceReqInfo t, List<Long> listProductIdResult,
			Message message) throws SQLException, IllegalStateException, SecurityException, SystemException {
		int res = -1;
		Connection con = null;
		try {
			IEInvoice ieInvoice = t.getIe_invoice();
			List<ProcessContractIEInvoice> listProcess = t.getList_process_contract();
			if (ieInvoice == null || listProcess == null) {
				message.setInternal_message("error request data!");
				return res;
			}
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			// load đơn hàng xuất khẩu.
			CriteriaQuery<IEInvoice> cqIEInvoice = cb.createQuery(IEInvoice.class);
			Root<IEInvoice> rootIEInvoice = cqIEInvoice.from(IEInvoice.class);
			rootIEInvoice.fetch("contract", JoinType.LEFT);
			rootIEInvoice.fetch("invoice", JoinType.LEFT);
			cqIEInvoice.select(rootIEInvoice).where(cb.equal(rootIEInvoice.get("id"), ieInvoice.getId()));
			TypedQuery<IEInvoice> query = em.createQuery(cqIEInvoice);
			List<IEInvoice> listIEInvoiceTrans = query.getResultList();
			if (listIEInvoiceTrans.size() == 0) {
				res = -1;
				ut.rollback();
				message.setUser_message("Đơn hàng xuất khẩu không tồn tại");
				message.setInternal_message("IEInvoice not found id:" + ieInvoice.getId());
				return res;
			}
			IEInvoice iEInvoiceTrans = listIEInvoiceTrans.get(0);
			Contract contractTrans = iEInvoiceTrans.getContract();
			if (contractTrans == null) {
				res = -1;
				ut.rollback();
				message.setUser_message("Đơn hàng xuất khẩu không có hợp đồng!");
				message.setInternal_message("Contract not found");
				return res;
			}
			Invoice invoiceTrans = iEInvoiceTrans.getInvoice();
			if (invoiceTrans != null) {
				res = -1;
				ut.rollback();
				message.setUser_message("Đơn hàng xuất khẩu đã chuyển qua hóa đơn không được chỉnh sửa");
				message.setInternal_message("");
				return res;
			}
			// lưu xuất từ hợp đồng.
			List<Long> listProductId = new ArrayList<Long>();
			String sqlContractDetail = "select c from ContractDetail c where c.product.id=:p and c.contract.id=:ct";
			long idContract = contractTrans.getId();
			int index = 1;
			for (ProcessContractIEInvoice p : listProcess) {
				long productId = p.getProduct_id();
				listProductId.add(productId);
				// lấy chi tiết hợp đồng
				TypedQuery<ContractDetail> queryContractDetail = em
						.createQuery(sqlContractDetail, ContractDetail.class);
				queryContractDetail.setParameter("p", productId);
				queryContractDetail.setParameter("ct", idContract);
				List<ContractDetail> listContractDetail = queryContractDetail.getResultList();
				if (listContractDetail.size() == 0) {
					res = -1;
					ut.rollback();
					message.setUser_message("Không tìm thấy sản phẩm:" + p.getProduct_code() + " trong hợp đồng!");
					message.setInternal_message("");
					return res;
				}
				ContractDetail contractDetailTrans = listContractDetail.get(0);
				IEInvoiceDetail d = new IEInvoiceDetail();
				d.setIe_invoice(iEInvoiceTrans);
				d.setProduct(new Product(productId));
				d.setQuantity_export(p.getExport_quantity());// số lượng xk
				d.setForeign_unit_price(contractDetailTrans.getUnit_price());// đơn
																				// giá
																				// ngoại
																				// tệ
				double quantity = (double) MyMath.roundCustom(p.getExport_quantity(), 2);
				d.setQuantity(quantity);
				// tính toán số tiền ngoại tệ
				double totalForeignAmount = BigDecimal.valueOf(quantity)
						.multiply(BigDecimal.valueOf(d.getForeign_unit_price())).doubleValue();
				totalForeignAmount = MyMath.roundCustom(totalForeignAmount, 2);
				d.setTotal_foreign_amount(totalForeignAmount);
				d.setTotal_export_foreign_amount(d.getTotal_foreign_amount());

				// tính toán số tiền vnd
				double totalAmount = (double) MyMath.round(d.getTotal_foreign_amount()
						* iEInvoiceTrans.getExchange_rate());
				d.setTotal_amount(totalAmount);

				// tính toán đơn giá vnđ
				double unitPrice = d.getTotal_amount() / quantity;
				d.setUnit_price(MyMath.round(unitPrice));

				d.setCreated_by(t.getMember_name());
				em.persist(d);
				if (d.getId() == 0) {
					res = -1;
					ut.rollback();
					message.setUser_message("Tạo chi tiết đơn hàng xuất khẩu thất bại!");
					message.setInternal_message("error persist IEInvoiceDetail");
					return res;
				}
				index++;
			}
			// tinh lai so tiền chênh lệch cộng vào dòng hiện tại
			List<IEInvoiceDetail> listIEInvoiceDetail = new ArrayList<IEInvoiceDetail>();
			iEInvoiceService.selectIEInvoideDetailByInvoice(iEInvoiceTrans.getId(), listIEInvoiceDetail);
			if (listIEInvoiceDetail.size() != 0) {

				iEInvoiceService.selectIEInvoideDetailByInvoice(iEInvoiceTrans.getId(), listIEInvoiceDetail);
				double sumUSD = listIEInvoiceDetail.stream().mapToDouble(f -> f.getTotal_foreign_amount()).sum();
				double sumVND = (long) MyMath.round(sumUSD * iEInvoiceTrans.getExchange_rate());
				double totalevent = listIEInvoiceDetail.stream().mapToDouble(f -> f.getTotal_amount()).sum();
				double chenhlech = sumVND - totalevent;
				if (chenhlech != 0) {
					IEInvoiceDetail d = listIEInvoiceDetail.get(listIEInvoiceDetail.size() - 1);
					d.setTotal_amount(d.getTotal_amount() + chenhlech);
					// tính toán đơn giá vnđ
					double unitPrice = d.getTotal_amount() / d.getQuantity();
					d.setUnit_price(MyMath.round(unitPrice));
					em.merge(d);
				}
			}

			// kiểm tra lại tất cả sản phẩm trên xuất có vượt hợp đồng không.
			StringBuilder sql = new StringBuilder();
			sql.append("select t2.product_id,pr2.product_code,t2.chenhsl,t2.chenhsotien from ( ");
			sql.append("select t1.product_id,sum(t1.contract_quantity-t1.ieinvoice_quantity) as chenhsl,sum(t1.contract_total_amount-t1.ieinvoice_total_amount) as chenhsotien ");
			sql.append("from ( ");
			sql.append("select dt.product_id,dt.quantity as contract_quantity, 0 as ieinvoice_quantity,dt.unit_price*dt.quantity as contract_total_amount,0 as ieinvoice_total_amount ");
			sql.append("from contractdetail as dt ");
			sql.append("where dt.contract_id=:ct and dt.product_id in (:lp) ");
			sql.append("union all ");
			sql.append("select die.product_id,0 as contract_quantity,die.quantity_export as ieinvoice_quantity,0 as contract_total_amount,die.foreign_unit_price*die.quantity_export as ieinvoice_total_amount ");
			sql.append("from ieinvoicedetail as die ");
			sql.append("inner join ieinvoice as ie on die.ie_invoice_id=ie.id ");
			sql.append("where ie.contract_id=:ct and die.product_id in (:lp) ) as t1 ");
			sql.append("group by t1.product_id ) as t2 ");
			sql.append("inner join product as pr2 on t2.product_id=pr2.id ");
			sql.append("where t2.chenhsl<0 and t2.chenhsotien<-1 ");
			Query queryKT = em.createNativeQuery(sql.toString());
			queryKT.setParameter("ct", contractTrans.getId());
			queryKT.setParameter("lp", listProductId);
			List<Object[]> listObj = queryKT.getResultList();
			if (listObj.size() > 0) {
				res = -1;
				List<String> listProductCodeNotRight = new ArrayList<>();
				for (Object[] q : listObj) {
					listProductIdResult.add(Long.parseLong(Objects.toString(q[0], "0")));
					String productCode = Objects.toString(q[1], "");
					listProductCodeNotRight.add(productCode);
				}
				message.setUser_message("Danh sách sản phẩm xuất vượt số hợp đồng:"
						+ String.join(",", listProductCodeNotRight));
				message.setInternal_message("error persist IEInvoiceDetail");
				ut.rollback();
				return res;
			}
			ut.commit();
			res = 0;
		} catch (Exception e) {
			res = -1;
			message.setUser_message("Không tạo được đơn hàng xuất xuất khẩu!");
			message.setInternal_message("ProcessLogicIEInvoiceService.saveListProcessContract");
			ut.rollback();
			logger.error("ProcessLogicIEInvoiceService.saveListProcessContract:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int insertOrUpdateIEInvoiceDetail(WrapIEInvoiceDetailReqInfo t, Message message) throws SQLException,
			IllegalStateException, SecurityException, SystemException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			IEInvoiceDetail detail = t.getIe_invoice_detail();
			IEInvoice ieInvoice = detail.getIe_invoice();
			String memberName = t.getMember_name();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEInvoice> cqIEInvoice = cb.createQuery(IEInvoice.class);
			Root<IEInvoice> rootIEInvoice = cqIEInvoice.from(IEInvoice.class);
			rootIEInvoice.fetch("contract", JoinType.LEFT);
			cqIEInvoice.select(rootIEInvoice).where(cb.equal(rootIEInvoice.get("id"), ieInvoice.getId()));
			TypedQuery<IEInvoice> queryIEInvoice = em.createQuery(cqIEInvoice);
			List<IEInvoice> listIEInvoiceTrans = queryIEInvoice.getResultList();
			if (listIEInvoiceTrans.size() == 0) {
				res = -1;
				ut.rollback();
				message.setUser_message("Đơn hàng xuất khẩu không tồn tại");
				message.setInternal_message("IEInvoice not found id:" + ieInvoice.getId());
				return res;
			}
			IEInvoice ieInvoiceTrans = listIEInvoiceTrans.get(0);
			Contract contractTrans = ieInvoiceTrans.getContract();

			// kiểm tra đơn hàng đã chuyển qua phiếu xuất chưa
			// Query queryCountInvoice = em
			// .createQuery("select count(id) from IEInvoice where invoice.id is not null and id=:id ");
			// queryCountInvoice.setParameter("id", ieInvoice.getId());
			// int count =
			// Integer.parseInt(Objects.toString(queryCountInvoice.getSingleResult()));
			// if (count > 0) {
			// res = -1;
			// message.setUser_message("Đơn hàng đã chuyển qua phiếu xuất không được cập nhật");
			// message.setInternal_message("");
			// ut.rollback();
			// return res;
			// }
			if (detail.getId() == 0) {
				// insert
				detail.setCreated_date(new Date());
				detail.setCreated_by(memberName);
				em.persist(detail);
				if (detail.getId() == 0) {
					res = -1;
					message.setUser_message("Lưu thất bại");
					message.setInternal_message("");
					ut.rollback();
					return res;
				}

			} else {
				// update
				detail.setLast_modifed_by(memberName);
				detail.setLast_modifed_date(new Date());
				if (em.merge(detail) == null) {
					res = -1;
					message.setUser_message("Cập nhật thất bại");
					message.setInternal_message("");
					ut.rollback();
					return res;
				}

			}
			// kiểm tra sau khi lưu/cập nhật có vượt số tiền ủy
			// nhiệm chi không.
			if (contractTrans != null) {
				if (contractTrans.getCustomer() != null && !contractTrans.getCustomer().isKhongxuatUNC()) {
					StringBuilder sql = new StringBuilder();
					sql.append("select sum(t.unc_amount-(t.invoice_amount+t.amount_ieinvoice)) from( ");
					sql.append("select 0 as unc_amount,dt.foreign_unit_price*dt.quantity as invoice_amount,0 as amount_ieinvoice from invoicedetail as dt ");
					sql.append("inner join invoice as d on dt.invoice_id=d.id ");
					sql.append("where d.contract_id=:cid ");
					sql.append("union all ");
					sql.append("select vp.total_amount,0,0 from voucherpayment as vp ");
					sql.append("where vp.contract_id=:cid ");
					sql.append("union all ");
					sql.append("select 0,0,iedt.foreign_unit_price*iedt.quantity_export from ieinvoicedetail as iedt ");
					sql.append("inner join ieinvoice as ie on iedt.ie_invoice_id=ie.id ");
					sql.append("where ie.contract_id=:cid and ie.invoice_id is null) as t ");
					Query query = em.createNativeQuery(sql.toString());
					query.setParameter("cid", contractTrans.getId());
					List<Object> list = query.getResultList();
					if (list.size() > 0) {
						double amount = Double.parseDouble(Objects.toString(list.get(0), "0"));
						// nếu số tiền ủy nhiệm chi vượt hơn 40 đô la thì thông
						// báo
						if (amount < -40) {
							// res = -1;
							message.setCode(1);
							message.setUser_message("Số tiền vượt ủy nhiệm chi là:" + Math.abs(amount));
							message.setInternal_message("");
							// ut.rollback();
							// return res;
						}
					} else {
						message.setCode(1);
						message.setUser_message("Chưa có ủy nhiệm chi");
						message.setInternal_message("");
					}
				}
			}
			ut.commit();
			res = 0;
		} catch (Exception e) {
			res = -1;
			message.setUser_message("Lưu thất bại");
			message.setInternal_message("ProcessLogicIEInvoiceService.insertOrUpdateIEInvoiceDetail");
			ut.rollback();
			logger.error("ProcessLogicIEInvoiceService.insertOrUpdateIEInvoiceDetail:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	@Override
	public int deleteIEInvoiceDetail(long id, Message message) throws SQLException, IllegalStateException,
			SecurityException, SystemException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<IEInvoice> cqIEInvoice = cb.createQuery(IEInvoice.class);
			Root<IEInvoiceDetail> root = cqIEInvoice.from(IEInvoiceDetail.class);
			Join<IEInvoiceDetail, IEInvoice> iEInvoice_ = root.join("ie_invoice", JoinType.INNER);
			cqIEInvoice.select(iEInvoice_).where(cb.equal(root.get("id"), id));
			TypedQuery<IEInvoice> queryIEInvoice = em.createQuery(cqIEInvoice);
			List<IEInvoice> listIEInvoiceTrans = queryIEInvoice.getResultList();
			if (listIEInvoiceTrans.size() == 0) {
				res = -1;
				message.setUser_message("Không tìm thấy chi tiết đơn hàng");
				message.setInternal_message("");
				ut.rollback();
				return res;
			}
			IEInvoice ieInvoiceTrans = listIEInvoiceTrans.get(0);
			// kiểm tra đơn hàng đã xuất chưa
			Query queryCountInvoice = em
					.createQuery("select count(id) from IEInvoice where invoice.id is not null  and id=:id ");
			queryCountInvoice.setParameter("id", ieInvoiceTrans.getId());
			int count = Integer.parseInt(Objects.toString(queryCountInvoice.getSingleResult()));
			if (count > 0) {
				res = -1;
				message.setUser_message("Đơn hàng đã chuyển qua phiếu xuất không được xóa");
				message.setInternal_message("");
				ut.rollback();
				return res;
			}
			// tiến hành delete chi tiết phiếu đơn hàng
			Query queryDel = em.createQuery("delete from IEInvoiceDetail where id=:id ");
			queryDel.setParameter("id", id);
			int countDel = queryDel.executeUpdate();
			if (countDel <= 0) {
				res = -1;
				message.setUser_message("Xóa thất bại");
				message.setInternal_message("");
				ut.rollback();
				return res;
			}
			ut.commit();
			res = 0;
		} catch (Exception e) {
			res = -1;
			message.setUser_message("Xóa thất bại");
			message.setInternal_message("ProcessLogicIEInvoiceService.deleteIEInvoiceDetail");
			ut.rollback();
			logger.error("ProcessLogicIEInvoiceService.deleteIEInvoiceDetail:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	private String initInvoiceCode() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
			Root<IEInvoice> root = cq.from(IEInvoice.class);
			// cq.multiselect(cb.coalesce(cb.max(root.get("id")),0));
			cq.multiselect(cb.coalesce(cb.max(cb.quot((Expression) root.get("invoice_code"), 1)), 0));
			TypedQuery<Integer> query = em.createQuery(cq);
			int max = query.getSingleResult();
			double p = (double) max / 100000000;
			if (p < 1) {
				return String.format("%08d", max + 1);
			}
			return (max + 1) + "";
		} catch (Exception e) {
			logger.error("ProcessLogicIEInvoiceService.initInvoiceCode:" + e.getMessage(), e);
		}
		return null;
	}

	private String initVoucherCode() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
			Root<IEInvoice> root = cq.from(IEInvoice.class);
			// cq.multiselect(cb.coalesce(cb.max(root.get("id")),0));
			cq.multiselect(cb.coalesce(cb.max(cb.quot((Expression) root.get("voucher_code"), 1)), 0));
			TypedQuery<Integer> query = em.createQuery(cq);
			int max = query.getSingleResult();
			double p = (double) max / 10000000;
			if (p < 1) {
				return String.format("%07d", max + 1);
			}
			return (max + 1) + "";
		} catch (Exception e) {
			logger.error("ProcessLogicIEInvoiceService.initVoucherCode:" + e.getMessage(), e);
		}
		return null;

	}

	@Override
	public long checkforward(long ieinvoice_id) {
		Query queryCountInvoice = em
				.createQuery("select invoice.id from IEInvoice where invoice.id is not null and id=:id ");
		queryCountInvoice.setParameter("id", ieinvoice_id);
		long invoice_id = Long.parseLong(Objects.toString(queryCountInvoice.getSingleResult()));
		return invoice_id;
	}

	@Override
	public int insertOrUpdateIEInvoice(WrapDataIEInvoiceReqInfo t, Message message) throws SQLException,
			IllegalStateException, SecurityException, SystemException {
		int res = -1;
		Connection con = null;
		try {
			ut.begin();
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			IEInvoice ieInvoice = t.getIe_invoice();
			List<IEInvoiceDetail> listDetail = t.getList_ie_invoice_detail();
			Contract contract = ieInvoice.getContract();
			if (contract != null) {
				ContractReqInfo ct = new ContractReqInfo();
				contractService.selectById(contract.getId(), ct);
				contract = ct.getContract();
			}
			long memberId = t.getMember_id();
			String memberName = t.getMember_name();
			if (ieInvoice.getId() == 0) {
				// insert
				// khởi tạo invoice code
				ieInvoice.setInvoice_code(initInvoiceCode());
				// nếu số chứng từ không có thì khởi tạo tự động
				String voucherCode = ieInvoice.getVoucher_code();
				if (voucherCode == null || "".equals(voucherCode)) {
					ieInvoice.setVoucher_code(initVoucherCode());
				}
				ieInvoice.setCreated_by_id(memberId);
				ieInvoice.setCreated_by(memberName);
				ieInvoice.setCreated_date(new Date());
				// lưu đơn hàng xuất khẩu
				em.persist(ieInvoice);
				if (ieInvoice.getId() == 0) {
					res = -1;
					message.setUser_message("Lưu đơn hàng xuất khẩu thất bại");
					message.setInternal_message("");
					ut.rollback();
					return res;
				}
				// kiểm tra có danh sách chi tiết không nếu có là trường hợp
				// copy từ phiếu cũ qua phiếu mới.
				if (listDetail != null && listDetail.size() > 0) {
					for (IEInvoiceDetail dt : listDetail) {
						// nạp lại đơn giá cho từng sản phẩm cho trường hợp
						// copy thay đổi hợp đồng
						dt.setId(0);
						dt.setIe_invoice(ieInvoice);
						dt.setCreated_by(memberName);
						dt.setCreated_date(new Date());
						em.persist(dt);
						if (dt.getId() == 0) {
							res = -1;
							message.setUser_message("Lưu chi tiết đơn hàng xuất khẩu thất bại");
							message.setInternal_message("");
							ut.rollback();
							return res;
						}
					}
					// kiểm tra có vượt ủy nhiệm chi hay không.
					if (contract != null) {
						long contractId = contract.getId();
						if (!contract.getCustomer().isKhongxuatUNC()) {
							StringBuilder sql = new StringBuilder();
							sql.append("select sum(t.unc_amount-(t.invoice_amount+t.amount_ieinvoice)) from( ");
							sql.append("select 0 as unc_amount,dt.foreign_unit_price*dt.quantity as invoice_amount,0 as amount_ieinvoice from invoicedetail as dt ");
							sql.append("inner join invoice as d on dt.invoice_id=d.id ");
							sql.append("where d.contract_id=:cid ");
							sql.append("union all ");
							sql.append("select vp.total_amount,0,0 from voucherpayment as vp ");
							sql.append("where vp.contract_id=:cid ");
							sql.append("union all ");
							sql.append("select 0,0,iedt.foreign_unit_price*iedt.quantity_export from ieinvoicedetail as iedt ");
							sql.append("inner join ieinvoice as ie on iedt.ie_invoice_id=ie.id ");
							sql.append("where ie.contract_id=:cid and ie.invoice_id is null) as t ");
							Query query = em.createNativeQuery(sql.toString());
							query.setParameter("cid", contractId);
							List<Object> list = query.getResultList();
							if (list.size() > 0) {
								double amount = Double.parseDouble(Objects.toString(list.get(0), "0"));
								// nếu vượt số tiền ủy nhiệm chi vượt hơn 40
								// đô la
								// thì không cho lưu
								if (amount < -40) {
									res = -1;
									message.setCode(1);
									message.setUser_message("Số tiền vượt ủy nhiệm chi là:" + Math.abs(amount));
									message.setInternal_message("");
									// ut.rollback();
									// return res;
								}
							}
						}
					}
				}
			} else {
				// trường hợp update
				// kiểm tra đơn hàng xuất khẩu đã chuyển qua phiếu xuất chưa
				// 13/01/2022 : đang can nhac chua dong lai dc phep cap nhat khi
				// chuyen qua hoa don
				// Query queryCountInvoice = em
				// .createQuery("select count(id) from IEInvoice where invoice.id is not null and id=:id ");
				// queryCountInvoice.setParameter("id", ieInvoice.getId());
				// int count =
				// Integer.parseInt(Objects.toString(queryCountInvoice.getSingleResult()));
				// if (count > 0) {
				// res = -1;
				// message.setUser_message("Đơn hàng đã chuyển qua phiếu xuất không được cập nhật");
				// message.setInternal_message("");
				// ut.rollback();
				// return res;
				// }

				CriteriaQuery<IEInvoice> cqIEInvoice = cb.createQuery(IEInvoice.class);
				Root<IEInvoice> rootIEInvoice = cqIEInvoice.from(IEInvoice.class);
				rootIEInvoice.fetch("contract", JoinType.LEFT);
				cqIEInvoice.select(rootIEInvoice).where(cb.equal(rootIEInvoice.get("id"), ieInvoice.getId()));
				TypedQuery<IEInvoice> queryIEInvoice = em.createQuery(cqIEInvoice);
				List<IEInvoice> listIEInvoiceTrans = queryIEInvoice.getResultList();
				if (listIEInvoiceTrans.size() == 0) {
					res = -1;
					message.setUser_message("Không tìm thấy đơn hàng xuất khẩu");
					message.setInternal_message("");
					ut.rollback();
					return res;
				}
				IEInvoice ieInvoiceTrans = listIEInvoiceTrans.get(0);
				Contract contractTrans = ieInvoiceTrans.getContract();

				// kiểm tra có sự thay đổi hợp đồng thì thay đổi đơn giá
				if ((contractTrans == null && contract != null) || (contractTrans != null && contract == null)
						|| (contractTrans != null && contract != null && !contractTrans.equals(contract))) {
					// lấy danh sách chi tiết đơn hàng xuất khẩu
					CriteriaQuery<IEInvoiceDetail> cqIEInvoiceDetail = cb.createQuery(IEInvoiceDetail.class);
					Root<IEInvoiceDetail> rootIEInvoiceDetail = cqIEInvoiceDetail.from(IEInvoiceDetail.class);
					cqIEInvoiceDetail.select(rootIEInvoiceDetail).where(
							cb.equal(rootIEInvoiceDetail.get("ie_invoice").get("id"), ieInvoice.getId()));
					TypedQuery<IEInvoiceDetail> queryIEInvoiceDetail = em.createQuery(cqIEInvoiceDetail);
					List<IEInvoiceDetail> listIEInvoiceDetailTrans = queryIEInvoiceDetail.getResultList();
					if (listIEInvoiceDetailTrans.size() > 0) {
						// kiểm tra nếu việc cập nhật hợp đồng là null thì set
						// đơn giá cho chi tiết đơn hàng xuất khẩu là 0
						if (contractTrans != null && contract == null) {
							for (IEInvoiceDetail dt : listIEInvoiceDetailTrans) {
								dt.setUnit_price(0);
								dt.setForeign_unit_price(0);
								dt.setTotal_foreign_amount(0);
								dt.setTotal_export_foreign_amount(0);
								dt.setTotal_amount(0);
								dt.setLast_modifed_by(memberName);
								dt.setLast_modifed_date(new Date());
								// cập nhật đơn hàng xuất khẩu
								if (em.merge(dt) == null) {
									res = -1;
									message.setUser_message("Cập nhật đơn giá chi tiết đơn hàng thất bại");
									message.setInternal_message("");
									ut.rollback();
									return res;
								}
							}
						} else {
							// cập nhật chi tiết đơn hàng set đơn giá theo hợp
							// đồng mới.
							for (IEInvoiceDetail dt : listIEInvoiceDetailTrans) {
								dt.setLast_modifed_by(memberName);
								dt.setLast_modifed_date(new Date());
								// lấy chi tiết hợp đồng để lấy đơn giá trong
								// hợp đồng và cài đặt cho đơn giá cho chi tiết
								// đơn hàng
								CriteriaQuery<Object> cq = cb.createQuery(Object.class);
								Root<ContractDetail> root = cq.from(ContractDetail.class);
								cq.multiselect(root.get("unit_price")).where(
										cb.and(cb.equal(root.get("contract").get("id"), contract.getId()),
												cb.equal(root.get("product").get("id"), dt.getProduct().getId())));
								TypedQuery<Object> query = em.createQuery(cq);
								List<Object> listContractDetailTrans = query.getResultList();
								if (listContractDetailTrans.size() > 0) {
									double unitPriceContract = Double.parseDouble(Objects.toString(
											listContractDetailTrans.get(0), "0"));
									dt.setForeign_unit_price(unitPriceContract);
								} else {
									dt.setForeign_unit_price(0);
								}
								// tính toán số tiền ngoại tệ làm tròn 2 số
								double totalForeignAmount = BigDecimal.valueOf(dt.getQuantity())
										.multiply(BigDecimal.valueOf(dt.getForeign_unit_price())).doubleValue();
								totalForeignAmount = (double) MyMath.roundCustom(totalForeignAmount, 2);
								dt.setTotal_foreign_amount(totalForeignAmount);
								// tính số lượng số tiền ngoại tệ xuất khẩu
								double totalExportForeignAmount = BigDecimal.valueOf(dt.getQuantity_export())
										.multiply(BigDecimal.valueOf(dt.getForeign_unit_price())).doubleValue();
								totalExportForeignAmount = (double) MyMath.roundCustom(totalExportForeignAmount, 2);
								dt.setTotal_export_foreign_amount(dt.getTotal_foreign_amount());
								// tính toán đơn giá vnđ
								double unitPrice = BigDecimal.valueOf(dt.getForeign_unit_price())
										.multiply(BigDecimal.valueOf(ieInvoice.getExchange_rate())).doubleValue();
								unitPrice = (double) MyMath.round(unitPrice);
								dt.setUnit_price(unitPrice);

								// tính toán số tiền vnd
								double totalAmount = BigDecimal.valueOf(dt.getTotal_export_foreign_amount())
										.multiply(BigDecimal.valueOf(ieInvoice.getExchange_rate())).doubleValue();
								totalAmount = (double) MyMath.round(totalAmount);
								dt.setTotal_amount(totalAmount);

								// cập nhật đơn hàng xuất khẩu
								if (em.merge(dt) == null) {
									res = -1;
									message.setUser_message("Cập nhật đơn giá chi tiết đơn hàng thất bại");
									message.setInternal_message("");
									ut.rollback();
									return res;
								}
							}
							if (contract != null && contract.getCustomer() != null) {
								boolean a = contract.getCustomer().isKhongxuatUNC();
								if (!contract.getCustomer().isKhongxuatUNC()) {
									// kiểm tra có vượt ủy nhiệm chi hay không.
									StringBuilder sql = new StringBuilder();
									sql.append("select sum(t.unc_amount-(t.invoice_amount+t.amount_ieinvoice)) from( ");
									sql.append("select 0 as unc_amount,dt.foreign_unit_price*dt.quantity as invoice_amount,0 as amount_ieinvoice from invoicedetail as dt ");
									sql.append("inner join invoice as d on dt.invoice_id=d.id ");
									sql.append("where d.contract_id=:cid ");
									sql.append("union all ");
									sql.append("select vp.total_amount,0,0 from voucherpayment as vp ");
									sql.append("where vp.contract_id=:cid ");
									sql.append("union all ");
									sql.append("select 0,0,iedt.foreign_unit_price*iedt.quantity_export from ieinvoicedetail as iedt ");
									sql.append("inner join ieinvoice as ie on iedt.ie_invoice_id=ie.id ");
									sql.append("where ie.contract_id=:cid and ie.invoice_id is null) as t ");
									Query query = em.createNativeQuery(sql.toString());
									query.setParameter("cid", contract.getId());
									List<Object> list = query.getResultList();
									if (list.size() > 0) {
										double amount = Double.parseDouble(Objects.toString(list.get(0), "0"));
										// nếu vượt số tiền ủy nhiệm chi vượt
										// hơn 40 đô
										// la thì không cho lưu
										if (amount < -40) {
											res = -1;
											message.setCode(1);
											message.setUser_message("Số tiền vượt ủy nhiệm chi là:" + Math.abs(amount));
											message.setInternal_message("");
											// ut.rollback();
											// return res;
										}
									}
								}
							}
						}
					}
				} else {
					// nếu có sự thay đổi tỉ giá thì cập nhật lại danh sách chi
					// tiết đơn hàng
					if (ieInvoice.getExchange_rate() != ieInvoiceTrans.getExchange_rate()) {
						// lấy danh sách chi tiết đơn hàng xuất khẩu
						CriteriaQuery<IEInvoiceDetail> cqIEInvoiceDetail = cb.createQuery(IEInvoiceDetail.class);
						Root<IEInvoiceDetail> rootIEInvoiceDetail = cqIEInvoiceDetail.from(IEInvoiceDetail.class);
						cqIEInvoiceDetail.select(rootIEInvoiceDetail).where(
								cb.equal(rootIEInvoiceDetail.get("ie_invoice").get("id"), ieInvoice.getId()));
						TypedQuery<IEInvoiceDetail> queryIEInvoiceDetail = em.createQuery(cqIEInvoiceDetail);
						List<IEInvoiceDetail> listIEInvoiceDetailTrans = queryIEInvoiceDetail.getResultList();
						// cập nhật chi tiết đơn hàng xuất khẩu theo tỉ giá mới.
						for (IEInvoiceDetail dt : listIEInvoiceDetailTrans) {
							dt.setLast_modifed_by(memberName);
							dt.setLast_modifed_date(new Date());
							// tính toán đơn giá vnđ
							double unitPrice = BigDecimal.valueOf(dt.getForeign_unit_price())
									.multiply(BigDecimal.valueOf(ieInvoice.getExchange_rate())).doubleValue();
							unitPrice = (double) MyMath.round(unitPrice);
							dt.setUnit_price(unitPrice);

							// tính toán số tiền vnd
							double totalAmount = BigDecimal.valueOf(dt.getTotal_export_foreign_amount())
									.multiply(BigDecimal.valueOf(ieInvoice.getExchange_rate())).doubleValue();
							totalAmount = (double) MyMath.round(totalAmount);
							dt.setTotal_amount(totalAmount);
							// cập nhật đơn hàng xuất khẩu
							if (em.merge(dt) == null) {
								res = -1;
								message.setUser_message("Cập nhật đơn giá chi tiết đơn hàng thất bại");
								message.setInternal_message("");
								ut.rollback();
								return res;
							}
						}
					}
				}
				// cập nhật đơn hàng xuất khẩu theo giá trị
				StringBuilder sql = new StringBuilder();
				sql.append("update from IEInvoice set voucher_code=:vc,invoice_date=:inv_d,customer=:c,car=:cr,payment_method=:pm,ie_categories=:ie,contract=:ct,department_name=:dn,note=:n,ie_reason=:ie_r, ");
				sql.append("stevedore=:ste,form_up_goods=:fug,up_goods_date=:ugd,warehouse=:w,tax_value=:tv,etd_date=:edtd,paid=:paid,delivered=:deli, ");
				sql.append("exchange_rate=:exr,bill_no=:bill,declaration_code=:de_code,harbor_category=:hb,currency=:cur,account_b=:acc,reference_no=:ref,");
				sql.append("shipped_per=:ship_per,term_of_delivery=:term,shipping_mark=:ship_mark,post_of_tran=:post,freight=:freight,driver_name=:driver,place_delivery=:place_de,");
				sql.append("place_discharge=:place_dis,stocker=:stocker,payment=:pay,last_modifed_date=:lmd,last_modifed_by=:lmb, ETD=:etd, ETA=:eta,duedate=:duedate, vgm=:vgm,productionDes=:productionDes,LC_NO=:LC_NO where id=:id ");
				Query queryUpdateIEInvoice = em.createQuery(sql.toString());
				queryUpdateIEInvoice.setParameter("vc", ieInvoice.getVoucher_code());
				queryUpdateIEInvoice.setParameter("inv_d", ieInvoice.getInvoice_date());
				queryUpdateIEInvoice.setParameter("c", ieInvoice.getCustomer());
				queryUpdateIEInvoice.setParameter("cr", ieInvoice.getCar());
				queryUpdateIEInvoice.setParameter("pm", ieInvoice.getPayment_method());
				queryUpdateIEInvoice.setParameter("ie", ieInvoice.getIe_categories());
				queryUpdateIEInvoice.setParameter("ct", ieInvoice.getContract());
				queryUpdateIEInvoice.setParameter("dn", ieInvoice.getDepartment_name());
				queryUpdateIEInvoice.setParameter("n", ieInvoice.getNote());
				queryUpdateIEInvoice.setParameter("ie_r", ieInvoice.getIe_reason());
				queryUpdateIEInvoice.setParameter("ste", ieInvoice.getStevedore());
				queryUpdateIEInvoice.setParameter("fug", ieInvoice.getForm_up_goods());
				queryUpdateIEInvoice.setParameter("ugd", ieInvoice.getUp_goods_date());
				queryUpdateIEInvoice.setParameter("w", ieInvoice.getWarehouse());
				queryUpdateIEInvoice.setParameter("tv", ieInvoice.getTax_value());
				queryUpdateIEInvoice.setParameter("edtd", ieInvoice.getEtd_date());
				queryUpdateIEInvoice.setParameter("paid", ieInvoice.isPaid());
				queryUpdateIEInvoice.setParameter("deli", ieInvoice.isDelivered());
				queryUpdateIEInvoice.setParameter("exr", ieInvoice.getExchange_rate());
				queryUpdateIEInvoice.setParameter("bill", ieInvoice.getBill_no());
				queryUpdateIEInvoice.setParameter("de_code", ieInvoice.getDeclaration_code());
				queryUpdateIEInvoice.setParameter("hb", ieInvoice.getHarbor_category());
				queryUpdateIEInvoice.setParameter("cur", ieInvoice.getCurrency());
				queryUpdateIEInvoice.setParameter("acc", ieInvoice.getAccount_b());
				queryUpdateIEInvoice.setParameter("ref", ieInvoice.getReference_no());
				queryUpdateIEInvoice.setParameter("ship_per", ieInvoice.getShipped_per());
				queryUpdateIEInvoice.setParameter("term", ieInvoice.getTerm_of_delivery());
				queryUpdateIEInvoice.setParameter("ship_mark", ieInvoice.getShipping_mark());
				queryUpdateIEInvoice.setParameter("post", ieInvoice.getPost_of_tran());
				queryUpdateIEInvoice.setParameter("freight", ieInvoice.getFreight());
				queryUpdateIEInvoice.setParameter("driver", ieInvoice.getDriver_name());
				queryUpdateIEInvoice.setParameter("place_de", ieInvoice.getPlace_delivery());
				queryUpdateIEInvoice.setParameter("place_dis", ieInvoice.getPlace_discharge());
				queryUpdateIEInvoice.setParameter("stocker", ieInvoice.getStocker());
				queryUpdateIEInvoice.setParameter("pay", ieInvoice.getPayment());
				queryUpdateIEInvoice.setParameter("lmd", ieInvoice.getLast_modifed_date());
				queryUpdateIEInvoice.setParameter("lmb", ieInvoice.getLast_modifed_by());
				queryUpdateIEInvoice.setParameter("etd", ieInvoice.getETD());
				queryUpdateIEInvoice.setParameter("eta", ieInvoice.getETA());
				queryUpdateIEInvoice.setParameter("duedate", ieInvoice.getDueDate());
				queryUpdateIEInvoice.setParameter("vgm", ieInvoice.getVgm());
				queryUpdateIEInvoice.setParameter("productionDes", ieInvoice.getProductionDes());
				queryUpdateIEInvoice.setParameter("LC_NO", ieInvoice.getLC_NO());
				queryUpdateIEInvoice.setParameter("id", ieInvoice.getId());
				if (queryUpdateIEInvoice.executeUpdate() <= 0) {
					res = -1;
					message.setUser_message("Cập nhật đơn hàng xuất khẩu thất bại");
					message.setInternal_message("");
					ut.rollback();
					return res;
				}
			}
			ut.commit();
			res = 0;
		} catch (Exception e) {
			res = -1;
			message.setUser_message("Lưu thất bại");
			message.setInternal_message("ProcessLogicIEInvoiceService.insertOrUpdateIEInvoice");
			ut.rollback();
			logger.error("ProcessLogicIEInvoiceService.insertOrUpdateIEInvoice:" + e.getMessage(), e);
		} finally {
			if (con != null)
				con.close();
		}
		return res;
	}

	// @Override
	// public int insertOrUpdateIEInvoice(WrapDataIEInvoiceReqInfo t, Message
	// message) throws SQLException,
	// IllegalStateException, SecurityException, SystemException {
	// int res = -1;
	// Connection con = null;
	// try {
	// ut.begin();
	// con = datasource.getConnection();
	// con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
	// CriteriaBuilder cb = em.getCriteriaBuilder();
	// IEInvoice ieInvoice = t.getIe_invoice();
	// List<IEInvoiceDetail> listDetail = t.getList_ie_invoice_detail();
	// Contract contract = ieInvoice.getContract();
	// if (contract != null) {
	// ContractReqInfo ct = new ContractReqInfo();
	// contractService.selectById(contract.getId(), ct);
	// contract = ct.getContract();
	// }
	// long memberId = t.getMember_id();
	// String memberName = t.getMember_name();
	// if (ieInvoice.getId() == 0) {
	// // insert
	// // khởi tạo invoice code
	// ieInvoice.setInvoice_code(initInvoiceCode());
	// // nếu số chứng từ không có thì khởi tạo tự động
	// String voucherCode = ieInvoice.getVoucher_code();
	// if (voucherCode == null || "".equals(voucherCode)) {
	// ieInvoice.setVoucher_code(initVoucherCode());
	// }
	// ieInvoice.setCreated_by_id(memberId);
	// ieInvoice.setCreated_by(memberName);
	// ieInvoice.setCreated_date(new Date());
	// // lưu đơn hàng xuất khẩu
	// em.persist(ieInvoice);
	// if (ieInvoice.getId() == 0) {
	// res = -1;
	// message.setUser_message("Lưu đơn hàng xuất khẩu thất bại");
	// message.setInternal_message("");
	// ut.rollback();
	// return res;
	// }
	// // kiểm tra có danh sách chi tiết không nếu có là trường hợp
	// // copy từ phiếu cũ qua phiếu mới.
	// if (listDetail != null && listDetail.size() > 0) {
	// if (contract == null) {
	// for (IEInvoiceDetail dt : listDetail) {
	// // nạp lại đơn giá cho từng sản phẩm cho trường hợp
	// // copy thay đổi hợp đồng
	// dt.setId(0);
	// dt.setIe_invoice(ieInvoice);
	// dt.setCreated_by(memberName);
	// dt.setCreated_date(new Date());
	// dt.setForeign_unit_price(0);
	// dt.setTotal_foreign_amount(0);
	// dt.setTotal_export_foreign_amount(0);
	// dt.setUnit_price(0);
	// dt.setTotal_amount(0);
	// // tính toán lại số lượng làm tròn 2 số từ số lượng
	// // xuất khẩu
	// double quantity = MyMath.roundCustom(dt.getQuantity_export(), 2);
	// dt.setQuantity(quantity);
	// em.persist(dt);
	// if (dt.getId() == 0) {
	// res = -1;
	// message.setUser_message("Lưu chi tiết đơn hàng xuất khẩu thất bại");
	// message.setInternal_message("");
	// ut.rollback();
	// return res;
	// }
	// }
	// } else {
	// long contractId = contract.getId();
	// for (IEInvoiceDetail dt : listDetail) {
	// dt.setIe_invoice(ieInvoice);
	// // tính toán lại số lượng làm tròn 2 số từ số lượng
	// // xuất khẩu
	// double quantity = MyMath.roundCustom(dt.getQuantity_export(), 2);
	// dt.setQuantity(quantity);
	// // lấy chi tiết hợp đồng để tính lấy đơn giá trong
	// // hợp đồng
	// CriteriaQuery<Object> cq = cb.createQuery(Object.class);
	// Root<ContractDetail> root = cq.from(ContractDetail.class);
	// cq.multiselect(root.get("unit_price")).where(
	// cb.and(cb.equal(root.get("contract").get("id"), contractId),
	// cb.equal(root.get("product").get("id"), dt.getProduct().getId())));
	// TypedQuery<Object> query = em.createQuery(cq);
	// List<Object> listContractDetailTrans = query.getResultList();
	// if (listContractDetailTrans.size() > 0) {
	// double unitPriceContract = Double.parseDouble(Objects.toString(
	// listContractDetailTrans.get(0), "0"));
	// dt.setForeign_unit_price(unitPriceContract);
	// } else {
	// dt.setForeign_unit_price(0);
	// }
	// // tính toán số tiền ngoại tệ làm tròn 3 chử số
	// double totalForeignAmount = BigDecimal.valueOf(quantity)
	// .multiply(BigDecimal.valueOf(dt.getForeign_unit_price())).doubleValue();
	// totalForeignAmount = (double) MyMath.roundCustom(totalForeignAmount, 3);
	// dt.setTotal_foreign_amount(totalForeignAmount);
	// // tính số lượng số tiền ngoại tệ xuất khẩu
	// double totalExportForeignAmount =
	// BigDecimal.valueOf(dt.getQuantity_export())
	// .multiply(BigDecimal.valueOf(dt.getForeign_unit_price())).doubleValue();
	// totalExportForeignAmount = (double)
	// MyMath.roundCustom(totalExportForeignAmount, 2);
	//
	// dt.setTotal_export_foreign_amount(dt.getTotal_foreign_amount());
	// // tính toán đơn giá vnđ
	// double unitPrice = BigDecimal.valueOf(dt.getForeign_unit_price())
	// .multiply(BigDecimal.valueOf(ieInvoice.getExchange_rate())).doubleValue();
	// unitPrice = (double) MyMath.round(unitPrice);
	// dt.setUnit_price(unitPrice);
	//
	// // tính toán số tiền vnd
	// double totalAmount =
	// BigDecimal.valueOf(dt.getTotal_export_foreign_amount())
	// .multiply(BigDecimal.valueOf(ieInvoice.getExchange_rate())).doubleValue();
	// totalAmount = (double) MyMath.round(totalAmount);
	// dt.setTotal_amount(totalAmount);
	//
	// dt.setCreated_by(memberName);
	// dt.setCreated_date(new Date());
	// dt.setId(0);
	// em.persist(dt);
	// if (dt.getId() == 0) {
	// res = -1;
	// message.setUser_message("Lưu chi tiết đơn hàng xuất khẩu thất bại");
	// message.setInternal_message("");
	// ut.rollback();
	// return res;
	// }
	// }
	// // kiểm tra có vượt ủy nhiệm chi hay không.
	// if (contract != null) {
	// if (!contract.getCustomer().isKhongxuatUNC()) {
	// StringBuilder sql = new StringBuilder();
	// sql.append("select sum(t.unc_amount-(t.invoice_amount+t.amount_ieinvoice)) from( ");
	// sql.append("select 0 as unc_amount,dt.foreign_unit_price*dt.quantity as invoice_amount,0 as amount_ieinvoice from invoicedetail as dt ");
	// sql.append("inner join invoice as d on dt.invoice_id=d.id ");
	// sql.append("where d.contract_id=:cid ");
	// sql.append("union all ");
	// sql.append("select vp.total_amount,0,0 from voucherpayment as vp ");
	// sql.append("where vp.contract_id=:cid ");
	// sql.append("union all ");
	// sql.append("select 0,0,iedt.foreign_unit_price*iedt.quantity_export from ieinvoicedetail as iedt ");
	// sql.append("inner join ieinvoice as ie on iedt.ie_invoice_id=ie.id ");
	// sql.append("where ie.contract_id=:cid and ie.invoice_id is null) as t ");
	// Query query = em.createNativeQuery(sql.toString());
	// query.setParameter("cid", contractId);
	// List<Object> list = query.getResultList();
	// if (list.size() > 0) {
	// double amount = Double.parseDouble(Objects.toString(list.get(0), "0"));
	// // nếu vượt số tiền ủy nhiệm chi vượt hơn 40
	// // đô la
	// // thì không cho lưu
	// if (amount < -40) {
	// res = -1;
	// message.setCode(1);
	// message.setUser_message("Số tiền vượt ủy nhiệm chi là:" +
	// Math.abs(amount));
	// message.setInternal_message("");
	// // ut.rollback();
	// // return res;
	// }
	// }
	// }
	// }
	// }
	// }
	// } else {
	// // trường hợp update
	// // kiểm tra đơn hàng xuất khẩu đã chuyển qua phiếu xuất chưa
	// // 13/01/2022 : đang can nhac chua dong lai dc phep cap nhat khi
	// // chuyen qua hoa don
	// // Query queryCountInvoice = em
	// //
	// .createQuery("select count(id) from IEInvoice where invoice.id is not null and id=:id ");
	// // queryCountInvoice.setParameter("id", ieInvoice.getId());
	// // int count =
	// //
	// Integer.parseInt(Objects.toString(queryCountInvoice.getSingleResult()));
	// // if (count > 0) {
	// // res = -1;
	// //
	// message.setUser_message("Đơn hàng đã chuyển qua phiếu xuất không được cập nhật");
	// // message.setInternal_message("");
	// // ut.rollback();
	// // return res;
	// // }
	//
	// CriteriaQuery<IEInvoice> cqIEInvoice = cb.createQuery(IEInvoice.class);
	// Root<IEInvoice> rootIEInvoice = cqIEInvoice.from(IEInvoice.class);
	// rootIEInvoice.fetch("contract", JoinType.LEFT);
	// cqIEInvoice.select(rootIEInvoice).where(cb.equal(rootIEInvoice.get("id"),
	// ieInvoice.getId()));
	// TypedQuery<IEInvoice> queryIEInvoice = em.createQuery(cqIEInvoice);
	// List<IEInvoice> listIEInvoiceTrans = queryIEInvoice.getResultList();
	// if (listIEInvoiceTrans.size() == 0) {
	// res = -1;
	// message.setUser_message("Không tìm thấy đơn hàng xuất khẩu");
	// message.setInternal_message("");
	// ut.rollback();
	// return res;
	// }
	// IEInvoice ieInvoiceTrans = listIEInvoiceTrans.get(0);
	// Contract contractTrans = ieInvoiceTrans.getContract();
	//
	// // kiểm tra có sự thay đổi hợp đồng thì thay đổi đơn giá
	// if ((contractTrans == null && contract != null) || (contractTrans != null
	// && contract == null)
	// || (contractTrans != null && contract != null &&
	// !contractTrans.equals(contract))) {
	// // lấy danh sách chi tiết đơn hàng xuất khẩu
	// CriteriaQuery<IEInvoiceDetail> cqIEInvoiceDetail =
	// cb.createQuery(IEInvoiceDetail.class);
	// Root<IEInvoiceDetail> rootIEInvoiceDetail =
	// cqIEInvoiceDetail.from(IEInvoiceDetail.class);
	// cqIEInvoiceDetail.select(rootIEInvoiceDetail).where(
	// cb.equal(rootIEInvoiceDetail.get("ie_invoice").get("id"),
	// ieInvoice.getId()));
	// TypedQuery<IEInvoiceDetail> queryIEInvoiceDetail =
	// em.createQuery(cqIEInvoiceDetail);
	// List<IEInvoiceDetail> listIEInvoiceDetailTrans =
	// queryIEInvoiceDetail.getResultList();
	// if (listIEInvoiceDetailTrans.size() > 0) {
	// // kiểm tra nếu việc cập nhật hợp đồng là null thì set
	// // đơn giá cho chi tiết đơn hàng xuất khẩu là 0
	// if (contractTrans != null && contract == null) {
	// for (IEInvoiceDetail dt : listIEInvoiceDetailTrans) {
	// dt.setUnit_price(0);
	// dt.setForeign_unit_price(0);
	// dt.setTotal_foreign_amount(0);
	// dt.setTotal_export_foreign_amount(0);
	// dt.setTotal_amount(0);
	// dt.setLast_modifed_by(memberName);
	// dt.setLast_modifed_date(new Date());
	// // cập nhật đơn hàng xuất khẩu
	// if (em.merge(dt) == null) {
	// res = -1;
	// message.setUser_message("Cập nhật đơn giá chi tiết đơn hàng thất bại");
	// message.setInternal_message("");
	// ut.rollback();
	// return res;
	// }
	// }
	// } else {
	// // cập nhật chi tiết đơn hàng set đơn giá theo hợp
	// // đồng mới.
	// for (IEInvoiceDetail dt : listIEInvoiceDetailTrans) {
	// dt.setLast_modifed_by(memberName);
	// dt.setLast_modifed_date(new Date());
	// // lấy chi tiết hợp đồng để lấy đơn giá trong
	// // hợp đồng và cài đặt cho đơn giá cho chi tiết
	// // đơn hàng
	// CriteriaQuery<Object> cq = cb.createQuery(Object.class);
	// Root<ContractDetail> root = cq.from(ContractDetail.class);
	// cq.multiselect(root.get("unit_price")).where(
	// cb.and(cb.equal(root.get("contract").get("id"), contract.getId()),
	// cb.equal(root.get("product").get("id"), dt.getProduct().getId())));
	// TypedQuery<Object> query = em.createQuery(cq);
	// List<Object> listContractDetailTrans = query.getResultList();
	// if (listContractDetailTrans.size() > 0) {
	// double unitPriceContract = Double.parseDouble(Objects.toString(
	// listContractDetailTrans.get(0), "0"));
	// dt.setForeign_unit_price(unitPriceContract);
	// } else {
	// dt.setForeign_unit_price(0);
	// }
	// // tính toán số tiền ngoại tệ làm tròn 3 chử số
	// double totalForeignAmount = BigDecimal.valueOf(dt.getQuantity())
	// .multiply(BigDecimal.valueOf(dt.getForeign_unit_price())).doubleValue();
	// totalForeignAmount = (double) MyMath.roundCustom(totalForeignAmount, 3);
	// dt.setTotal_foreign_amount(totalForeignAmount);
	// // tính số lượng số tiền ngoại tệ xuất khẩu
	// double totalExportForeignAmount =
	// BigDecimal.valueOf(dt.getQuantity_export())
	// .multiply(BigDecimal.valueOf(dt.getForeign_unit_price())).doubleValue();
	// totalExportForeignAmount = (double)
	// MyMath.roundCustom(totalExportForeignAmount, 2);
	// dt.setTotal_export_foreign_amount(dt.getTotal_foreign_amount());
	// // tính toán đơn giá vnđ
	// double unitPrice = BigDecimal.valueOf(dt.getForeign_unit_price())
	// .multiply(BigDecimal.valueOf(ieInvoice.getExchange_rate())).doubleValue();
	// unitPrice = (double) MyMath.round(unitPrice);
	// dt.setUnit_price(unitPrice);
	//
	// // tính toán số tiền vnd
	// double totalAmount =
	// BigDecimal.valueOf(dt.getTotal_export_foreign_amount())
	// .multiply(BigDecimal.valueOf(ieInvoice.getExchange_rate())).doubleValue();
	// totalAmount = (double) MyMath.round(totalAmount);
	// dt.setTotal_amount(totalAmount);
	//
	// // cập nhật đơn hàng xuất khẩu
	// if (em.merge(dt) == null) {
	// res = -1;
	// message.setUser_message("Cập nhật đơn giá chi tiết đơn hàng thất bại");
	// message.setInternal_message("");
	// ut.rollback();
	// return res;
	// }
	// }
	// if (contract != null && contract.getCustomer() != null) {
	// boolean a = contract.getCustomer().isKhongxuatUNC();
	// if (!contract.getCustomer().isKhongxuatUNC()) {
	// // kiểm tra có vượt ủy nhiệm chi hay không.
	// StringBuilder sql = new StringBuilder();
	// sql.append("select sum(t.unc_amount-(t.invoice_amount+t.amount_ieinvoice)) from( ");
	// sql.append("select 0 as unc_amount,dt.foreign_unit_price*dt.quantity as invoice_amount,0 as amount_ieinvoice from invoicedetail as dt ");
	// sql.append("inner join invoice as d on dt.invoice_id=d.id ");
	// sql.append("where d.contract_id=:cid ");
	// sql.append("union all ");
	// sql.append("select vp.total_amount,0,0 from voucherpayment as vp ");
	// sql.append("where vp.contract_id=:cid ");
	// sql.append("union all ");
	// sql.append("select 0,0,iedt.foreign_unit_price*iedt.quantity_export from ieinvoicedetail as iedt ");
	// sql.append("inner join ieinvoice as ie on iedt.ie_invoice_id=ie.id ");
	// sql.append("where ie.contract_id=:cid and ie.invoice_id is null) as t ");
	// Query query = em.createNativeQuery(sql.toString());
	// query.setParameter("cid", contract.getId());
	// List<Object> list = query.getResultList();
	// if (list.size() > 0) {
	// double amount = Double.parseDouble(Objects.toString(list.get(0), "0"));
	// // nếu vượt số tiền ủy nhiệm chi vượt
	// // hơn 40 đô
	// // la thì không cho lưu
	// if (amount < -40) {
	// res = -1;
	// message.setCode(1);
	// message.setUser_message("Số tiền vượt ủy nhiệm chi là:" +
	// Math.abs(amount));
	// message.setInternal_message("");
	// // ut.rollback();
	// // return res;
	// }
	// }
	// } else {
	// System.out.println("asdfasdf");
	// }
	// }
	// }
	// }
	// } else {
	// // nếu có sự thay đổi tỉ giá thì cập nhật lại danh sách chi
	// // tiết đơn hàng
	// if (ieInvoice.getExchange_rate() != ieInvoiceTrans.getExchange_rate()) {
	// // lấy danh sách chi tiết đơn hàng xuất khẩu
	// CriteriaQuery<IEInvoiceDetail> cqIEInvoiceDetail =
	// cb.createQuery(IEInvoiceDetail.class);
	// Root<IEInvoiceDetail> rootIEInvoiceDetail =
	// cqIEInvoiceDetail.from(IEInvoiceDetail.class);
	// cqIEInvoiceDetail.select(rootIEInvoiceDetail).where(
	// cb.equal(rootIEInvoiceDetail.get("ie_invoice").get("id"),
	// ieInvoice.getId()));
	// TypedQuery<IEInvoiceDetail> queryIEInvoiceDetail =
	// em.createQuery(cqIEInvoiceDetail);
	// List<IEInvoiceDetail> listIEInvoiceDetailTrans =
	// queryIEInvoiceDetail.getResultList();
	// // cập nhật chi tiết đơn hàng xuất khẩu theo tỉ giá mới.
	// for (IEInvoiceDetail dt : listIEInvoiceDetailTrans) {
	// dt.setLast_modifed_by(memberName);
	// dt.setLast_modifed_date(new Date());
	// // tính toán đơn giá vnđ
	// double unitPrice = BigDecimal.valueOf(dt.getForeign_unit_price())
	// .multiply(BigDecimal.valueOf(ieInvoice.getExchange_rate())).doubleValue();
	// unitPrice = (double) MyMath.round(unitPrice);
	// dt.setUnit_price(unitPrice);
	//
	// // tính toán số tiền vnd
	// double totalAmount =
	// BigDecimal.valueOf(dt.getTotal_export_foreign_amount())
	// .multiply(BigDecimal.valueOf(ieInvoice.getExchange_rate())).doubleValue();
	// totalAmount = (double) MyMath.round(totalAmount);
	// dt.setTotal_amount(totalAmount);
	// // cập nhật đơn hàng xuất khẩu
	// if (em.merge(dt) == null) {
	// res = -1;
	// message.setUser_message("Cập nhật đơn giá chi tiết đơn hàng thất bại");
	// message.setInternal_message("");
	// ut.rollback();
	// return res;
	// }
	// }
	// }
	// }
	// // cập nhật đơn hàng xuất khẩu theo giá trị
	// StringBuilder sql = new StringBuilder();
	// sql.append("update from IEInvoice set voucher_code=:vc,invoice_date=:inv_d,customer=:c,car=:cr,payment_method=:pm,ie_categories=:ie,contract=:ct,department_name=:dn,note=:n,ie_reason=:ie_r, ");
	// sql.append("stevedore=:ste,form_up_goods=:fug,up_goods_date=:ugd,warehouse=:w,tax_value=:tv,etd_date=:edtd,paid=:paid,delivered=:deli, ");
	// sql.append("exchange_rate=:exr,bill_no=:bill,declaration_code=:de_code,harbor_category=:hb,currency=:cur,account_b=:acc,reference_no=:ref,");
	// sql.append("shipped_per=:ship_per,term_of_delivery=:term,shipping_mark=:ship_mark,post_of_tran=:post,freight=:freight,driver_name=:driver,place_delivery=:place_de,");
	// sql.append("place_discharge=:place_dis,stocker=:stocker,payment=:pay,last_modifed_date=:lmd,last_modifed_by=:lmb, ETD=:etd where id=:id ");
	// Query queryUpdateIEInvoice = em.createQuery(sql.toString());
	// queryUpdateIEInvoice.setParameter("vc", ieInvoice.getVoucher_code());
	// queryUpdateIEInvoice.setParameter("inv_d", ieInvoice.getInvoice_date());
	// queryUpdateIEInvoice.setParameter("c", ieInvoice.getCustomer());
	// queryUpdateIEInvoice.setParameter("cr", ieInvoice.getCar());
	// queryUpdateIEInvoice.setParameter("pm", ieInvoice.getPayment_method());
	// queryUpdateIEInvoice.setParameter("ie", ieInvoice.getIe_categories());
	// queryUpdateIEInvoice.setParameter("ct", ieInvoice.getContract());
	// queryUpdateIEInvoice.setParameter("dn", ieInvoice.getDepartment_name());
	// queryUpdateIEInvoice.setParameter("n", ieInvoice.getNote());
	// queryUpdateIEInvoice.setParameter("ie_r", ieInvoice.getIe_reason());
	// queryUpdateIEInvoice.setParameter("ste", ieInvoice.getStevedore());
	// queryUpdateIEInvoice.setParameter("fug", ieInvoice.getForm_up_goods());
	// queryUpdateIEInvoice.setParameter("ugd", ieInvoice.getUp_goods_date());
	// queryUpdateIEInvoice.setParameter("w", ieInvoice.getWarehouse());
	// queryUpdateIEInvoice.setParameter("tv", ieInvoice.getTax_value());
	// queryUpdateIEInvoice.setParameter("edtd", ieInvoice.getEtd_date());
	// queryUpdateIEInvoice.setParameter("paid", ieInvoice.isPaid());
	// queryUpdateIEInvoice.setParameter("deli", ieInvoice.isDelivered());
	// queryUpdateIEInvoice.setParameter("exr", ieInvoice.getExchange_rate());
	// queryUpdateIEInvoice.setParameter("bill", ieInvoice.getBill_no());
	// queryUpdateIEInvoice.setParameter("de_code",
	// ieInvoice.getDeclaration_code());
	// queryUpdateIEInvoice.setParameter("hb", ieInvoice.getHarbor_category());
	// queryUpdateIEInvoice.setParameter("cur", ieInvoice.getCurrency());
	// queryUpdateIEInvoice.setParameter("acc", ieInvoice.getAccount_b());
	// queryUpdateIEInvoice.setParameter("ref", ieInvoice.getReference_no());
	// queryUpdateIEInvoice.setParameter("ship_per",
	// ieInvoice.getShipped_per());
	// queryUpdateIEInvoice.setParameter("term",
	// ieInvoice.getTerm_of_delivery());
	// queryUpdateIEInvoice.setParameter("ship_mark",
	// ieInvoice.getShipping_mark());
	// queryUpdateIEInvoice.setParameter("post", ieInvoice.getPost_of_tran());
	// queryUpdateIEInvoice.setParameter("freight", ieInvoice.getFreight());
	// queryUpdateIEInvoice.setParameter("driver", ieInvoice.getDriver_name());
	// queryUpdateIEInvoice.setParameter("place_de",
	// ieInvoice.getPlace_delivery());
	// queryUpdateIEInvoice.setParameter("place_dis",
	// ieInvoice.getPlace_discharge());
	// queryUpdateIEInvoice.setParameter("stocker", ieInvoice.getStocker());
	// queryUpdateIEInvoice.setParameter("pay", ieInvoice.getPayment());
	// queryUpdateIEInvoice.setParameter("lmd",
	// ieInvoice.getLast_modifed_date());
	// queryUpdateIEInvoice.setParameter("lmb", ieInvoice.getLast_modifed_by());
	// queryUpdateIEInvoice.setParameter("etd", ieInvoice.getETD());
	// queryUpdateIEInvoice.setParameter("id", ieInvoice.getId());
	// if (queryUpdateIEInvoice.executeUpdate() <= 0) {
	// res = -1;
	// message.setUser_message("Cập nhật đơn hàng xuất khẩu thất bại");
	// message.setInternal_message("");
	// ut.rollback();
	// return res;
	// }
	// }
	// ut.commit();
	// res = 0;
	// } catch (Exception e) {
	// res = -1;
	// message.setUser_message("Lưu thất bại");
	// message.setInternal_message("ProcessLogicIEInvoiceService.insertOrUpdateIEInvoice");
	// ut.rollback();
	// logger.error("ProcessLogicIEInvoiceService.insertOrUpdateIEInvoice:" +
	// e.getMessage(), e);
	// } finally {
	// if (con != null)
	// con.close();
	// }
	// return res;
	// }

	@Override
	public void update(IEInvoiceDetail ieInvoiceDetail) throws SQLException, IllegalStateException, SecurityException,
			SystemException {
		try {
			ut.begin();
			em.merge(ieInvoiceDetail);
			ut.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
