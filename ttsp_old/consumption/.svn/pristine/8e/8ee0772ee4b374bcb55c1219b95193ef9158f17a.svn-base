package lixco.com.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.commom_ejb.MyUtilEJB;
import lixco.com.commom_ejb.PagingInfo;
import lixco.com.commom_ejb.ToolTimeCustomer;
import lixco.com.entity.City;
import lixco.com.entity.Customer;
import lixco.com.entity.CustomerChannel;
import lixco.com.entity.CustomerPricingProgram;
import lixco.com.entity.CustomerTypes;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.PricingProgramDetail;
import lixco.com.entity.Product;
import lixco.com.entityapi.CityDTO;
import lixco.com.entityapi.CustomerAsyncDTO;
import lixco.com.entityapi.CustomerChannelDTO;
import lixco.com.entityapi.CustomerDTO;
import lixco.com.entityapi.CustomerPricingProgramDTO;
import lixco.com.entityapi.CustomerTypesDTO;
import lixco.com.entityapi.PricingProgramAsyncDTO;
import lixco.com.entityapi.PricingProgramDTO;
import lixco.com.entityapi.PricingProgramDetailDTO;
import lixco.com.interfaces.ICustomerPricingProgramService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IPricingProgramDetailService;
import lixco.com.interfaces.IPricingProgramService;
import lixco.com.interfaces.IProductService;
import lixco.com.reqInfo.CityReqInfo;
import lixco.com.reqInfo.CustomerChannelReqInfo;
import lixco.com.reqInfo.CustomerPricingProgramReqInfo;
import lixco.com.reqInfo.CustomerReqInfo;
import lixco.com.reqInfo.CustomerTypesReqInfo;
import lixco.com.reqInfo.PricingProgramDetailReqInfo;
import lixco.com.reqInfo.PricingProgramReqInfo;
import lixco.com.reqfox.WrapDataPricingProgram;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class PricingProgramService implements IPricingProgramService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int selectAll(List<PricingProgram> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PricingProgram> cq = cb.createQuery(PricingProgram.class);
			Root<PricingProgram> root = cq.from(PricingProgram.class);
			cq.select(root);
			TypedQuery<PricingProgram> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("PricingProgramService.selectAll:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public List<PricingProgram> findNotSync() {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PricingProgram> cq = cb.createQuery(PricingProgram.class);
			Root<PricingProgram> root = cq.from(PricingProgram.class);
			root.fetch("parent_pricing_program", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("capnhat"), true));
			TypedQuery<PricingProgram> query = em.createQuery(cq);
			return query.getResultList();

		} catch (Exception e) {
			logger.error("CustomerService.selectById:" + e.getMessage(), e);
		}
		return new ArrayList<PricingProgram>();
	}

	@Override
	public int updateCapNhat(List<Long> ids) {
		int res = -1;
		try {
			if (ids != null && ids.size() != 0) {
				Query query = em.createQuery("update PricingProgram set capnhat=false where id in :ids");
				query.setParameter("ids", ids);
				return query.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("CustomerService.updateCapNhat:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int search(String json, List<PricingProgram> list) {
		int res = -1;
		try {
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j.get("pricing_program_info"), "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j.get("pricing_program_info"), "to_date", null);
			HolderParser hProgramCode = JsonParserUtil.getValueString(j.get("pricing_program_info"), "program_code",
					null);
			HolderParser hParentProgram = JsonParserUtil.getValueNumber(j.get("pricing_program_info"),
					"parent_pricing_program_id", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j.get("pricing_program_info"), "product_id", null);

			HolderParser hCusType = JsonParserUtil.getValueString(j.get("pricing_program_info"), "customer_types_id",
					null);
			HolderParser hCus = JsonParserUtil.getValueString(j.get("pricing_program_info"), "customer_id", null);

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PricingProgram> cq = cb.createQuery(PricingProgram.class);
			Root<PricingProgram> root_ = cq.from(PricingProgram.class);
			root_.fetch("parent_pricing_program", JoinType.LEFT);
			Join<PricingProgram, PricingProgramDetail> pricingProgramDetail_ = root_.join(
					"list_pricing_program_detail", JoinType.LEFT);
			List<Predicate> predicates = new ArrayList<Predicate>();
			ParameterExpression<Date> pFromDate = cb.parameter(Date.class);
			ParameterExpression<Date> pToDate = cb.parameter(Date.class);
			ParameterExpression<String> pProgramCode = cb.parameter(String.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			ParameterExpression<Long> pParentId = cb.parameter(Long.class);
			ParameterExpression<Integer> pDisable = cb.parameter(Integer.class);
			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.isNull(pFromDate));
			dis.getExpressions().add(cb.equal(pFromDate, ""));
			dis.getExpressions().add(cb.greaterThanOrEqualTo(root_.get("expiry_date"), pFromDate));
			predicates.add(dis);
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.isNull(pToDate));
			dis1.getExpressions().add(cb.equal(pToDate, ""));
			dis1.getExpressions().add(cb.lessThanOrEqualTo(root_.get("expiry_date"), pToDate));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.isNull(pProgramCode));
			dis2.getExpressions().add(cb.equal(pProgramCode, ""));
			dis2.getExpressions().add(cb.like(root_.get("program_code"), pProgramCode));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pProductId, 0));
			dis3.getExpressions().add(cb.equal(pricingProgramDetail_.get("product").get("id"), pProductId));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pParentId, 0));
			dis4.getExpressions().add(cb.equal(root_.get("parent_pricing_program").get("id"), pParentId));
			predicates.add(dis4);

			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.equal(pParentId, 0));
			dis5.getExpressions().add(cb.equal(root_.get("parent_pricing_program").get("id"), pParentId));
			predicates.add(dis5);

			Predicate dis6 = cb.disjunction();
			dis6.getExpressions().add(cb.equal(pParentId, 0));
			dis6.getExpressions().add(cb.equal(root_.get("parent_pricing_program").get("id"), pParentId));
			predicates.add(dis6);

			cq.select(root_).distinct(true).where(cb.and(predicates.toArray(new Predicate[0])))
					.orderBy(cb.desc(root_.get("program_code")));
			TypedQuery<PricingProgram> query = em.createQuery(cq);
			query.setParameter(pFromDate, ToolTimeCustomer.convertStringToDate(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy HH:mm:ss"));
			query.setParameter(pToDate, ToolTimeCustomer.convertStringToDate(
					Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy HH:mm:ss"));
			query.setParameter(pProgramCode, "%" + Objects.toString(hProgramCode.getValue(), "") + "%");
			query.setParameter(pProductId, Long.parseLong(Objects.toString(hProductId.getValue(), null)));
			query.setParameter(pParentId, Long.parseLong(Objects.toString(hParentProgram.getValue(), null)));
			list.addAll(query.getResultList());
			res = 0;

		} catch (Exception e) {
			logger.error("PricingProgramService.search:" + e.getMessage(), e);
		}
		return res;
	}


	@Override
	public int insert(PricingProgramReqInfo t) {
		int res = -1;
		try {
			PricingProgram p = t.getPricing_program();
			if (p != null) {
				p.setCapnhat(true);
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("PricingProgramService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int update(PricingProgramReqInfo t) {
		int res = -1;
		try {
			PricingProgram p = t.getPricing_program();
			if (p != null) {
				p.setCapnhat(true);
				p = em.merge(p);
				if (p != null) {
					t.setPricing_program(p);
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("PricingProgramService.update:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectById(long id, PricingProgramReqInfo t) {
		int res = -1;
		try {
			PricingProgram p = em.find(PricingProgram.class, id);
			if (p != null) {
				t.setPricing_program(p);
				res = 0;
			}
		} catch (Exception e) {
			logger.error("PricingProgramService.selectById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int deleteById(long id) {
		int res = -1;
		try {
			// JQPL
			Query query = em.createQuery("delete from PricingProgramDetail where pricing_program.id = :id");
			query.setParameter("id", id);
			query.executeUpdate();
			Query query1 = em.createQuery("delete from PricingProgram where id=:id ");
			query1.setParameter("id", id);
			res = query1.executeUpdate();
		} catch (Exception e) {
			logger.error("PricingProgramService.deleteById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int getDateMaxSubPricingProgram(long parent_program_id, StringBuilder result) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
			Root<PricingProgram> root_ = cq.from(PricingProgram.class);
			cq.multiselect(cb.count(root_.get("id")), cb.greatest(root_.get("effective_date")),
					cb.greatest(root_.get("expiry_date"))).where(
					cb.equal(root_.get("parent_pricing_program"), parent_program_id));
			TypedQuery<Object[]> query = em.createQuery(cq);
			List<Object[]> list = query.getResultList();
			for (Object[] obj : list) {
				int count = Integer.parseInt(Objects.toString(obj[0], "0"));
				Date maxEffectiveDate = (Date) obj[1];
				Date maxExpiryDate = (Date) obj[2];
				JsonObject json = new JsonObject();
				json.addProperty("count", count);
				json.addProperty("effective_date",
						ToolTimeCustomer.convertDateToString(maxEffectiveDate, "dd/MM/yyyy HH:mm:ss"));
				json.addProperty("expiry_date",
						ToolTimeCustomer.convertDateToString(maxExpiryDate, "dd/MM/yyyy HH:mm:ss"));
				result.append(JsonParserUtil.getGson().toJson(json));
			}
			res = 0;
		} catch (Exception e) {
			logger.error("PricingProgramService.checkDatePricingProgram:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public String initPricingProgramCode() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
			Root<PricingProgram> root = cq.from(PricingProgram.class);
			// cq.multiselect(cb.coalesce(cb.max(root.get("id")),0));
			cq.multiselect(cb.coalesce(cb.max(cb.quot((Expression) cb.substring(root.get("program_code"), 3), 1)), 0));
			TypedQuery<Integer> query = em.createQuery(cq);
			int max = query.getSingleResult();
			double p = (double) max / 100000;
			if (p < 1) {
				return "DG" + String.format("%06d", max + 1);
			}
			return "DG" + max + 1;
		} catch (Exception e) {
			logger.error("PricingProgramService.initPricingProgramCode:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int findLike(String text, int size, List<PricingProgram> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PricingProgram> cq = cb.createQuery(PricingProgram.class);
			Root<PricingProgram> root = cq.from(PricingProgram.class);
			ParameterExpression<String> paramLike = cb.parameter(String.class);
			Predicate dis = cb.disjunction();
			dis.getExpressions().add(
					cb.like(cb.function("replace", String.class, cb.lower(root.get("program_code")), cb.literal("đ"),
							cb.literal("d")), paramLike));
			dis.getExpressions().add(
					cb.like(cb.function("replace", String.class, cb.lower(root.get("voucher_code")), cb.literal("đ"),
							cb.literal("d")), paramLike));
			cq.select(root).where(dis);
			TypedQuery<PricingProgram> query = em.createQuery(cq);
			query.setParameter(paramLike, "%" + text + "%");
			if (size != -1) {
				query.setFirstResult(0);
				query.setMaxResults(size);
			}
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("PricingProgramService.findLike:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int complete(String text, List<PricingProgram> list) {
		int res = -1;
		try {
			if (text != null && !"".equals(text)) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<PricingProgram> cq = cb.createQuery(PricingProgram.class);
				Root<PricingProgram> root = cq.from(PricingProgram.class);
				Predicate con = cb.conjunction();
				con.getExpressions().add(
						cb.like(cb.function("replace", String.class, cb.lower(root.get("program_code")),
								cb.literal("đ"), cb.literal("d")), "%" + text + "%"));
				con.getExpressions().add(cb.isFalse(root.get("disable")));
				cq.select(
						cb.construct(PricingProgram.class, root.get("id"), root.get("program_code"),
								root.get("effective_date"), root.get("expiry_date"))).where(con);
				TypedQuery<PricingProgram> query = em.createQuery(cq);
				list.addAll(query.getResultList());
				res = 0;
			}
			res = 0;
		} catch (Exception e) {
			logger.error("PricingProgramService.complete:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectByCode(String code, PricingProgramReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<PricingProgram> cq = cb.createQuery(PricingProgram.class);
			Root<PricingProgram> root = cq.from(PricingProgram.class);
			cq.select(root).where(cb.equal(root.get("program_code"), code));
			TypedQuery<PricingProgram> query = em.createQuery(cq);
			t.setPricing_program(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			// logger.error("PricingProgramService.selectByCode:"+e.getMessage(),e);
		}
		return res;
	}

	@Override
	public PricingProgram findByCode(String code) {
		try {
			if (code != null && !"".equals(code)) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<PricingProgram> cq = cb.createQuery(PricingProgram.class);
				Root<PricingProgram> root = cq.from(PricingProgram.class);
				cq.select(root).where(cb.equal(root.get("program_code"), code));
				TypedQuery<PricingProgram> query = em.createQuery(cq);
				return query.getSingleResult();
			}
		} catch (Exception e) {
			// logger.error("PricingProgramService.selectByCode:"+e.getMessage(),e);
		}
		return null;
	}
	@Override
	public long findByCodegetId(String code) {
		try {
			if (code != null && !"".equals(code)) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Long> cq = cb.createQuery(Long.class);
				Root<PricingProgram> root = cq.from(PricingProgram.class);
				cq.select(root.get("id")).where(cb.equal(root.get("program_code"), code));
				TypedQuery<Long> query = em.createQuery(cq);
				return query.getSingleResult();
			}
		} catch (Exception e) {
		}
		return 0;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int saveOrUpdate(List<WrapDataPricingProgram> data, String userName) {
		int res = -1;
		try {
			for (WrapDataPricingProgram d : data) {
				lixco.com.reqfox.WrapDataPricingProgram.PricingProgram pricingProgramFox = d.getPricing_program();
				List<lixco.com.reqfox.WrapDataPricingProgram.PricingProgramDetail> listPricingProgramDetail = d
						.getList_pricing_program_detail();
				List<lixco.com.reqfox.WrapDataPricingProgram.CustomerPricingProgram> listCustomerPricingProgram = d
						.getList_customer_pricing_program();
				Query queryCK = em.createQuery("select id from PricingProgram where program_code=:cd");
				queryCK.setParameter("cd", pricingProgramFox.getMasoctdg());
				List<Long> listCTDGID = queryCK.getResultList();
				if (listCTDGID.size() > 0) {

					long idCTDG = listCTDGID.get(0);
					PricingProgram pricingProgram = new PricingProgram(idCTDG);
					Long idpr = null;
					if (pricingProgramFox.getMasoctdgc() != null && !"".equals(pricingProgramFox.getMasoctdgc())) {
						// tìm chương trình đơn giá cha
						Query queryParent = em.createQuery("select id from PricingProgram where program_code=:c");
						queryParent.setParameter("c", pricingProgramFox.getMasoctdgc());
						List<Long> listIDParent = queryParent.getResultList();
						if (listIDParent.size() > 0) {
							idpr = listIDParent.get(0);
						}
					}
					// jpql
					Query queryUp = em
							.createQuery("update PricingProgram set parent_pricing_program_id=:pr, effective_date= :fd,expiry_date=:ed,last_modifed_by=:lmb,last_modifed_date=:lmd,note=:n,update_time= :ut where program_code=:pc");
					queryUp.setParameter("pr", idpr);
					queryUp.setParameter("fd", pricingProgramFox.getTungay());
					queryUp.setParameter("ed", pricingProgramFox.getDenngay());
					queryUp.setParameter("lmb", userName);
					queryUp.setParameter("lmd", new Date());
					queryUp.setParameter("n", pricingProgramFox.getGhichu());
					queryUp.setParameter("ut", new Date());
					queryUp.setParameter("pc", pricingProgramFox.getMasoctdg());
					int code = queryUp.executeUpdate();

					// nếu cập nhật thành công
					if (code > 0) {
						// thực hiện xóa chi tiết đơn giá
						Query queryDelDetail = em
								.createQuery("delete from PricingProgramDetail where pricing_program_id =:id");
						queryDelDetail.setParameter("id", idCTDG);
						if (queryDelDetail.executeUpdate() >= 0) {
							if (listPricingProgramDetail != null && listPricingProgramDetail.size() > 0) {
								// thực hiện insert danh sách chi tiết đơn giá
								for (lixco.com.reqfox.WrapDataPricingProgram.PricingProgramDetail p : listPricingProgramDetail) {
									// thực hiện lưu hoặc cập nhật chương trình
									// đơn giá
									PricingProgramDetail item = new PricingProgramDetail();
									// id, created_by, created_date,
									// last_modifed_by, last_modifed_date,
									// quantity, revenue_per_ton, unit_price,
									// pricing_program_id, product_id
									item.setCreated_by(userName);
									item.setCreated_date(new Date());
									item.setQuantity(p.getSoluong());
									item.setUnit_price(p.getDongia());
									item.setPricing_program(pricingProgram);
									// tìm kiếm product
									Query queryProduct = em.createQuery("select id from Product where product_code=:c");
									queryProduct.setParameter("c", p.getMasp());
									List<Long> listIDProduct = queryProduct.getResultList();
									if (listIDProduct.size() > 0) {
										item.setProduct(new Product(listIDProduct.get(0)));
									}
									em.persist(item);
								}
							}
							// thực hiện xóa cài đặt chương trình đơn giá khách
							// hàng
							Query queryDelCustomerDG = em
									.createQuery("delete from CustomerPricingProgram where pricing_program.id=:idp");
							queryDelCustomerDG.setParameter("idp", idCTDG);
							int code2 = queryDelCustomerDG.executeUpdate();
							if (code2 >= 0) {
								if (listCustomerPricingProgram != null && listCustomerPricingProgram.size() > 0) {
									for (lixco.com.reqfox.WrapDataPricingProgram.CustomerPricingProgram p : listCustomerPricingProgram) {

										// thực hiện insert chương trình đơn giá
										// và khách hàng
										CustomerPricingProgram item = new CustomerPricingProgram();
										// id, created_by, created_date,
										// disable, effective_date, expiry_date,
										// last_modifed_by, last_modifed_date,
										// note, customer_id, pricing_program_id
										item.setCreated_by(userName);
										item.setCreated_date(new Date());
										item.setDisable(p.isDiscontinu());

										try {
											if (2000 > (p.getTungay().getYear() + 1900)) {
												item.setEffective_date(null);
											} else {
												item.setEffective_date(p.getTungay());
											}
										} catch (Exception e) {
										}
										try {
											if (2000 > (p.getDenngay().getYear() + 1900)) {
												item.setExpiry_date(null);
											} else {
												item.setExpiry_date(p.getDenngay());
											}
										} catch (Exception e) {
										}
										item.setNote(p.getGhichu());
										// Tìm khách hàng
										Query queryCustomer = em
												.createQuery("select id from Customer where customer_code=:c");
										queryCustomer.setParameter("c", p.getMakh());
										List<Long> listIDCus = queryCustomer.getResultList();
										if (listIDCus.size() > 0) {
											item.setCustomer(new Customer(listIDCus.get(0)));
										}
										item.setPricing_program(pricingProgram);
										em.persist(item);

									}
								}
							}

						}
					}
				} else {
					PricingProgram pricingProgram = new PricingProgram();
					// id, created_by, created_date, disable, effective_date,
					// expiry_date, last_modifed_by, last_modifed_date, note,
					// program_code, voucher_code, parent_pricing_program_id,
					// update_time
					pricingProgram.setCreated_by(userName);
					pricingProgram.setCreated_date(new Date());
					pricingProgram.setEffective_date(pricingProgramFox.getTungay());
					pricingProgram.setExpiry_date(pricingProgramFox.getDenngay());
					pricingProgram.setNote(pricingProgramFox.getGhichu());
					pricingProgram.setProgram_code(pricingProgramFox.getMasoctdg());
					if (pricingProgramFox.getMasoctdgc() != null && !"".equals(pricingProgramFox.getMasoctdgc())) {
						// tìm chương trình đơn giá cha
						Query queryParent = em.createQuery("select id from PricingProgram where program_code=:c");
						queryParent.setParameter("c", pricingProgramFox.getMasoctdgc());
						List<Long> listIDParent = queryParent.getResultList();
						if (listIDParent.size() > 0) {
							pricingProgram.setParent_pricing_program(new PricingProgram(listIDParent.get(0)));
						}
					}
					pricingProgram.setUpdate_time(pricingProgramFox.getUpdatetime());
					em.persist(pricingProgram);
					if (pricingProgram.getId() > 0) {
						if (listPricingProgramDetail != null && listPricingProgramDetail.size() > 0) {
							// thực hiện insert chi tiết đơn giá
							for (lixco.com.reqfox.WrapDataPricingProgram.PricingProgramDetail p : listPricingProgramDetail) {
								PricingProgramDetail item = new PricingProgramDetail();
								// id, created_by, created_date,
								// last_modifed_by, last_modifed_date, quantity,
								// revenue_per_ton, unit_price,
								// pricing_program_id, product_id
								item.setCreated_by(userName);
								item.setCreated_date(new Date());
								item.setQuantity(p.getSoluong());
								item.setUnit_price(p.getDongia());
								item.setPricing_program(pricingProgram);
								// tìm kiếm product
								Query queryProduct = em.createQuery("select id from Product where product_code=:c");
								queryProduct.setParameter("c", p.getMasp());
								List<Long> listIDProduct = queryProduct.getResultList();
								if (listIDProduct.size() > 0) {
									item.setProduct(new Product(listIDProduct.get(0)));
								}
								em.persist(item);
							}
						}
						if (listCustomerPricingProgram != null && listCustomerPricingProgram.size() > 0) {
							// thực hiện insert cài đặt khách hàng
							for (lixco.com.reqfox.WrapDataPricingProgram.CustomerPricingProgram p : listCustomerPricingProgram) {
								CustomerPricingProgram item = new CustomerPricingProgram();
								// id, created_by, created_date, disable,
								// effective_date, expiry_date, last_modifed_by,
								// last_modifed_date, note, customer_id,
								// pricing_program_id
								item.setCreated_by(userName);
								item.setCreated_date(new Date());
								item.setDisable(p.isDiscontinu());

								try {
									if (2000 > (p.getTungay().getYear() + 1900)) {
										item.setEffective_date(null);
									} else {
										item.setEffective_date(p.getTungay());
									}
								} catch (Exception e) {
								}
								try {
									if (2000 > (p.getDenngay().getYear() + 1900)) {
										item.setExpiry_date(null);
									} else {
										item.setExpiry_date(p.getDenngay());
									}
								} catch (Exception e) {
								}

								item.setNote(p.getGhichu());
								// Tìm khách hàng
								Query queryCustomer = em.createQuery("select id from Customer where customer_code=:c");
								queryCustomer.setParameter("c", p.getMakh());
								List<Long> listIDCus = queryCustomer.getResultList();
								if (listIDCus.size() > 0) {
									item.setCustomer(new Customer(listIDCus.get(0)));
								}
								item.setPricing_program(pricingProgram);
								em.persist(item);

							}
						}
					}

				}

			}
			res = 0;
		} catch (Exception e) {
			logger.error("PricingProgramService.saveOrUpdate:" + e.getMessage(), e);
		}
		return res;
	}

	@Inject
	IProductService productService;
	@Inject
	ICustomerService customerService;
	@Inject
	IPricingProgramDetailService pricingProgramDetailService;
	@Inject
	ICustomerPricingProgramService customerPricingProgramService;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int dongbodongia(PricingProgramAsyncDTO pricingProgramAsyncDTO, StringBuilder errors) {
		int dem = 0;

		// Dong bo PricingProgram
		List<PricingProgramDTO> pricingProgramDTOs = pricingProgramAsyncDTO.getPricingProgramDTOs();
		for (PricingProgramDTO it : pricingProgramDTOs) {
			PricingProgram prNew = new PricingProgram(it);
			prNew.setParent_pricing_program(findByCode(it.getParent_pricing_program_code()));
			PricingProgram pOld = findByCode(it.getProgram_code());
			PricingProgramReqInfo pr = new PricingProgramReqInfo(prNew);
			if (pOld != null) {
				System.out.println("Dong bo don gia cap nhat: "+it.getProgram_code());
				pr.getPricing_program().setId(pOld.getId());
				int result = update(pr);
				if (result == -1) {
					errors.append("Lỗi PricingProgram:" + it.getProgram_code() + ";");
				} else {
					Query query = em
							.createQuery("DELETE FROM PricingProgramDetail WHERE pricing_program_id = :pricing_program_id");
					query.setParameter("pricing_program_id", pr.getPricing_program().getId());
					query.executeUpdate();

					Query queryCus = em
							.createQuery("DELETE FROM CustomerPricingProgram WHERE pricing_program_id = :pricing_program_id");
					queryCus.setParameter("pricing_program_id", pr.getPricing_program().getId());
					queryCus.executeUpdate();
				}
			} else {
				System.out.println("Dong bo don gia them vao: "+it.getProgram_code());
				int result = insert(pr);
				if (result == -1) {
					errors.append("Lỗi PricingProgram:" + it.getProgram_code() + ";");
				}
			}
			// Chi tiet don gia
			List<PricingProgramDetailDTO> pricingProgramDetailDTOs = it.getPricingProgramDetailDTOs();
			for (PricingProgramDetailDTO itDt : pricingProgramDetailDTOs) {
				PricingProgramDetail prDtNew = new PricingProgramDetail(itDt);
				prDtNew.setProduct(productService.selectByCode(itDt.getProductCode()));
				if (prDtNew.getProduct() != null) {
					prDtNew.setPricing_program(pr.getPricing_program());
					PricingProgramDetailReqInfo prDt = new PricingProgramDetailReqInfo(prDtNew);
					int resultDt = pricingProgramDetailService.insert(prDt);
					if (resultDt == -1) {
						errors.append("Lỗi PricingProgramDetail:" + pr.getPricing_program().getProgram_code() + ": "
								+ itDt.getProductCode() + ";");
					}
				} else {
					errors.append("Lỗi PricingProgramDetail:" + pr.getPricing_program().getProgram_code()
							+ ": Không có mã SP " + itDt.getProductCode() + ";");
				}
			}
			// khach hang don gia
			List<CustomerPricingProgramDTO> customerPricingProgramDTOs = it.getCustomerPricingProgramDTOs();
			for (CustomerPricingProgramDTO itDt : customerPricingProgramDTOs) {
				CustomerPricingProgram prDtNew = new CustomerPricingProgram(itDt);
				prDtNew.setCustomer(customerService.selectByCode(itDt.getCustomerCode()));
				if (prDtNew.getCustomer() != null) {
					prDtNew.setPricing_program(pr.getPricing_program());
					CustomerPricingProgramReqInfo prDt = new CustomerPricingProgramReqInfo(prDtNew);
					int resultDt = customerPricingProgramService.insert(prDt);
					if (resultDt == -1) {
						errors.append("Lỗi CustomerPricingProgram:" + pr.getPricing_program().getProgram_code() + ": "
								+ itDt.getCustomerCode() + ";");
					}

				} else {
					errors.append("Lỗi CustomerPricingProgram:" + pr.getPricing_program().getProgram_code()
							+ ": Không có mã KH " + itDt.getCustomerCode() + ";");
				}
			}
		}
		return dem;
	}
}
