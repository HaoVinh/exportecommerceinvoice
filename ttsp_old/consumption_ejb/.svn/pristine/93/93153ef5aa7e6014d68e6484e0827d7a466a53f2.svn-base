package lixco.com.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.commom_ejb.MyUtilEJB;
import lixco.com.entity.Area;
import lixco.com.entity.City;
import lixco.com.entity.Country;
import lixco.com.entity.Customer;
import lixco.com.entity.CustomerChannel;
import lixco.com.entity.CustomerTypes;
import lixco.com.entityapi.CityDTO;
import lixco.com.entityapi.CustomerAsyncDTO;
import lixco.com.entityapi.CustomerChannelDTO;
import lixco.com.entityapi.CustomerDTO;
import lixco.com.entityapi.CustomerTypesDTO;
import lixco.com.entityapi.KhachHangData;
import lixco.com.interfaces.IAreaService;
import lixco.com.interfaces.ICityService;
import lixco.com.interfaces.ICountryService;
import lixco.com.interfaces.ICustomerChannelService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.ICustomerTypesService;
import lixco.com.reqInfo.CityReqInfo;
import lixco.com.reqInfo.CustomerChannelReqInfo;
import lixco.com.reqInfo.CustomerReqInfo;
import lixco.com.reqInfo.CustomerTypesReqInfo;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class CustomerService implements ICustomerService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@Override
	public Customer findById(long id) {
		try {
			Customer cus = em.find(Customer.class, id);
			if (cus.getCustomer_types() != null)
				cus.getCustomer_types().getCode();
			return cus;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int selectAll(List<Customer> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> root = cq.from(Customer.class);
			cq.select(root);
			TypedQuery<Customer> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("CustomerService.selectAll:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public List<Customer> findNotSync() {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> root = cq.from(Customer.class);
			root.fetch("customer_types", JoinType.LEFT);
			root.fetch("city", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("capnhat"), true));
			TypedQuery<Customer> query = em.createQuery(cq);
			return query.getResultList();

		} catch (Exception e) {
			logger.error("CustomerService.selectById:" + e.getMessage(), e);
		}
		return new ArrayList<Customer>();
	}

	@Override
	public int search(String json, List<Customer> list) {
		int res = -1;
		try {
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hstextStr = JsonParserUtil.getValueString(j.get("customer_info"), "stextStr", null);
			HolderParser hCustomerTypesId = JsonParserUtil.getValueNumber(j.get("customer_info"), "customer_types_id",
					null);
			HolderParser hCityId = JsonParserUtil.getValueNumber(j.get("customer_info"), "city_id", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> root_ = cq.from(Customer.class);
			root_.fetch("customer_types", JoinType.LEFT);
			root_.fetch("city", JoinType.LEFT);
			List<Predicate> predicates = new ArrayList<Predicate>();
			ParameterExpression<Long> pCustomerTypesId = cb.parameter(Long.class);
			ParameterExpression<Long> pCityId = cb.parameter(Long.class);

			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.equal(pCustomerTypesId, 0));
			dis1.getExpressions().add(cb.equal(root_.get("customer_types").get("id"), pCustomerTypesId));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.equal(pCityId, 0));
			dis2.getExpressions().add(cb.equal(root_.get("city").get("id"), pCityId));
			predicates.add(dis2);

			List<Predicate> predicatesStr = new ArrayList<Predicate>();

			String stextStr = Objects.toString(hstextStr.getValue());
			if (hstextStr.getValue() != null && !"".equals(stextStr.trim())) {
				Predicate predicate_code = cb.like(root_.get("customer_code"), "%" + stextStr + "%");
				predicatesStr.add(predicate_code);
				Predicate predicate_name = cb.like(root_.get("customer_name"), "%" + stextStr + "%");
				predicatesStr.add(predicate_name);
				Predicate predicate_address = cb.like(root_.get("address"), "%" + stextStr + "%");
				predicatesStr.add(predicate_address);

				Predicate predicate_tax = cb.like(root_.get("tax_code"), "%" + stextStr + "%");
				predicatesStr.add(predicate_tax);
				Predicate predicate_phone = cb.like(root_.get("cell_phone"), "%" + stextStr + "%");
				predicatesStr.add(predicate_phone);
				Predicate predicate_hphone = cb.like(root_.get("home_phone"), "%" + stextStr + "%");
				predicatesStr.add(predicate_hphone);
			}

			if (predicatesStr.size() != 0) {
				cq.select(root_)
						.where(cb.and(predicates.toArray(new Predicate[0])),
								cb.or(predicatesStr.toArray(new Predicate[0])))
						.orderBy(cb.desc(root_.get("created_date")));
			} else {
				cq.select(root_).where(cb.and(predicates.toArray(new Predicate[0])))
						.orderBy(cb.desc(root_.get("created_date")));
			}
			TypedQuery<Customer> query = em.createQuery(cq);

			query.setParameter(pCustomerTypesId, Long.parseLong(Objects.toString(hCustomerTypesId.getValue(), "0")));
			query.setParameter(pCityId, Long.parseLong(Objects.toString(hCityId.getValue(), "0")));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("CustomerService.search:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectBy(String json, List<Customer> list) {
		int res = -1;
		try {/*
			 * { customer_info:{customer_code:
			 * '',customer_name:'',tax_code:'',phone_number:'',customer_types_id:0}}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hCustomerCode = JsonParserUtil.getValueString(j.get("customer_info"), "customer_code", null);
			HolderParser hCustomerName = JsonParserUtil.getValueString(j.get("customer_info"), "customer_name", null);
			HolderParser hTaxCode = JsonParserUtil.getValueString(j.get("customer_info"), "tax_code", null);
			HolderParser hPhoneNumber = JsonParserUtil.getValueString(j.get("customer_info"), "phone_number", null);
			HolderParser hCustomerTypesId = JsonParserUtil.getValueNumber(j.get("customer_info"), "customer_types_id",
					null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> root_ = cq.from(Customer.class);
			root_.fetch("customer_types", JoinType.LEFT);
			root_.fetch("city", JoinType.LEFT);
			List<Predicate> predicates = new ArrayList<Predicate>();
			ParameterExpression<String> pCustomerCode = cb.parameter(String.class);
			ParameterExpression<String> pCustomerName = cb.parameter(String.class);
			ParameterExpression<String> pCustomerNameLike = cb.parameter(String.class);
			ParameterExpression<String> pTaxCode = cb.parameter(String.class);
			ParameterExpression<String> pPhoneNumber = cb.parameter(String.class);
			ParameterExpression<Long> pCustomerTypesId = cb.parameter(Long.class);
			Predicate dis = cb.disjunction();
			Predicate con = cb.conjunction();
			dis.getExpressions().add(cb.isNull(pCustomerCode));
			dis.getExpressions().add(cb.equal(pCustomerCode, ""));
			con.getExpressions().add(cb.notEqual(pCustomerCode, ""));
			con.getExpressions().add(cb.equal(root_.get("customer_code"), pCustomerCode));
			dis.getExpressions().add(con);
			predicates.add(dis);
			Predicate dis1 = cb.disjunction();
			Predicate con1 = cb.conjunction();
			dis1.getExpressions().add(cb.isNull(pCustomerName));
			dis1.getExpressions().add(cb.equal(pCustomerName, ""));
			con1.getExpressions().add(cb.notEqual(pCustomerName, ""));
			con1.getExpressions().add(cb.like(root_.get("customer_name"), pCustomerNameLike));
			dis1.getExpressions().add(con1);
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			Predicate con2 = cb.conjunction();
			dis2.getExpressions().add(cb.isNull(pTaxCode));
			dis2.getExpressions().add(cb.equal(pTaxCode, ""));
			con2.getExpressions().add(cb.notEqual(pTaxCode, ""));
			con2.getExpressions().add(cb.equal(root_.get("tax_code"), pTaxCode));
			dis2.getExpressions().add(con2);
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.isNull(pPhoneNumber));
			dis3.getExpressions().add(cb.equal(pPhoneNumber, ""));
			dis3.getExpressions().add(cb.equal(root_.get("cell_phone"), pPhoneNumber));
			dis3.getExpressions().add(cb.equal(root_.get("home_phone"), pPhoneNumber));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			Predicate con4 = cb.conjunction();
			dis4.getExpressions().add(cb.equal(pCustomerTypesId, 0));
			con4.getExpressions().add(cb.notEqual(pCustomerTypesId, 0));
			con4.getExpressions().add(cb.equal(root_.get("customer_types").get("id"), pCustomerTypesId));
			dis4.getExpressions().add(con4);
			predicates.add(dis4);
			cq.select(root_).where(cb.and(predicates.toArray(new Predicate[0])));
			TypedQuery<Customer> query = em.createQuery(cq);
			query.setParameter(pCustomerCode, Objects.toString(hCustomerCode.getValue(), null));
			query.setParameter(pCustomerName, Objects.toString(hCustomerName.getValue(), null));
			query.setParameter(pCustomerNameLike, "%" + Objects.toString(hCustomerName.getValue(), null) + "%");
			query.setParameter(pTaxCode, Objects.toString(hTaxCode.getValue(), null));
			query.setParameter(pPhoneNumber, Objects.toString(hPhoneNumber.getValue(), null));
			query.setParameter(pCustomerTypesId, Long.parseLong(Objects.toString(hCustomerTypesId.getValue(), "0")));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("CustomerService.search:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int insertAvaiCode(CustomerReqInfo t) {
		int res = -1;
		try {
			Customer p = t.getCustomer();
			if (p != null) {
				p.setCapnhat(true);
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("CustomerService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int insert(CustomerReqInfo t) {
		int res = -1;
		try {
			Customer p = t.getCustomer();
			if (p != null) {
				p.setCustomer_code(initOrderCode());
				p.setCapnhat(true);
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("CustomerService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	private String initOrderCode() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
			Root<Customer> root = cq.from(Customer.class);
			// cq.multiselect(cb.coalesce(cb.max(root.get("id")),0));
			cq.multiselect(cb.coalesce(cb.max(cb.quot((Expression) root.get("customer_code"), 1)), 0));
			TypedQuery<Integer> query = em.createQuery(cq);
			int max = query.getSingleResult();
			double p = (double) max / 1000000;
			if (p < 1) {
				return String.format("%05d", max + 1);
			}
			return (max + 1) + "";
		} catch (Exception e) {
			logger.error("CustomerService.initcustomer_code:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int update(CustomerReqInfo t) {
		int res = -1;
		try {
			Customer p = t.getCustomer();
			if (p != null) {
				p.setCapnhat(true);
				p = em.merge(p);
				if (p != null) {
					res = selectById(p.getId(), t);
				}
			}
		} catch (Exception e) {
			logger.error("CustomerService.update:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int updateCapNhat(List<Long> ids) {
		int res = -1;
		try {
			if (ids != null && ids.size() != 0) {
				Query query = em.createQuery("update Customer set capnhat=false where id in :ids");
				query.setParameter("ids", ids);
				return query.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("CustomerService.updateCapNhat:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectById(long id, CustomerReqInfo info) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> root = cq.from(Customer.class);
			root.fetch("customer_types", JoinType.LEFT);
			root.fetch("city", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<Customer> query = em.createQuery(cq);
			info.setCustomer(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			logger.error("CustomerService.selectById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int deleteById(long id) {
		int res = -1;
		try {
			// JQPL
			Query query = em.createQuery("delete from Customer where id=:id ");
			query.setParameter("id", id);
			res = query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CustomerService.delete:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int checkCustomerCode(String customerCode, long customerId) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Customer> root = cq.from(Customer.class);
			ParameterExpression<String> pCustomerCode = cb.parameter(String.class);
			ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
			Predicate dis = cb.disjunction();
			Predicate con1 = cb.conjunction();
			Predicate con2 = cb.conjunction();
			con1.getExpressions().add(cb.equal(pCustomerId, 0));
			con1.getExpressions().add(cb.equal(root.get("customer_code"), pCustomerCode));
			dis.getExpressions().add(con1);
			con2.getExpressions().add(cb.notEqual(pCustomerId, 0));
			con2.getExpressions().add(cb.notEqual(root.get("id"), pCustomerId));
			con2.getExpressions().add(cb.equal(root.get("customer_code"), pCustomerCode));
			dis.getExpressions().add(con2);
			cq.select(cb.count(root.get("id"))).where(dis);
			TypedQuery<Long> query = em.createQuery(cq);
			query.setParameter(pCustomerId, customerId);
			query.setParameter(pCustomerCode, customerCode);
			res = query.getSingleResult().intValue();
		} catch (Exception e) {
			logger.error("CustomerService.checkCustomerCode:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int autoComplete(String text, int size, List<Customer> list) {
		return 0;
	}

	@Override
	public int selectAllByCustomerTypes(long customerTypesId, List<Customer> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> root = cq.from(Customer.class);
			cq.select(root).where(cb.equal(root.get("customer_types").get("id"), customerTypesId));
			TypedQuery<Customer> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("CustomerService.selectAllByCustomerTypes:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int complete(String text, List<Customer> list) {
		int res = -1;
		try {
			if (text != null && !"".equals(text)) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
				Root<Customer> root = cq.from(Customer.class);
				Predicate dis = cb.disjunction();
				dis.getExpressions().add(cb.equal(root.get("customer_code"), text));
				dis.getExpressions().add(cb.like(root.get("customer_name"), "%" + text + "%"));
				cq.select(
						cb.construct(Customer.class, root.get("id"), root.get("customer_code"),
								root.get("customer_name"))).where(dis);
				TypedQuery<Customer> query = em.createQuery(cq);
				list.addAll(query.getResultList());
			}
			res = 0;
		} catch (Exception e) {
			logger.error("CustomerService.complete<Text,list>:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public List<KhachHangData> complete2(String makh, String tenkh) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<KhachHangData> cq = cb.createQuery(KhachHangData.class);
			Root<Customer> root = cq.from(Customer.class);
			Predicate dis = cb.disjunction();
			if (makh != null && !"".equals(makh))
				dis.getExpressions().add(cb.equal(root.get("customer_code"), makh));
			if (tenkh != null && !"".equals(tenkh))
				dis.getExpressions().add(cb.like(root.get("customer_name"), "%" + tenkh + "%"));
			cq.select(
					cb.construct(KhachHangData.class,root.get("customer_code"), root.get("customer_name"),concat(";",root.get("cell_phone"),root.get("email")),root.get("address")))
					.where(dis);
			TypedQuery<KhachHangData> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("CustomerService.complete<Text,list>:" + e.getMessage(), e);
		}
		return new ArrayList<KhachHangData>();
	}
	private Expression<String> concat(String delimiter, Expression<String> ... expressions) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
	    Expression<String> result = null;
	    for (int i = 0; i < expressions.length; i++) {
	        final boolean first = i == 0, last = i == (expressions.length - 1);
	        final Expression<String> expression = expressions[i];
	        if (first && last) {
	            result = expression;
	        } else if (first) {
	            result = cb.concat(expression, delimiter);
	        } else {
	            result = cb.concat(result, expression);
	            if (!last) {
	                result = cb.concat(result, delimiter);
	            }
	        }
	    }
	    return result;
	}

	@Override
	public int complete(String text, CustomerTypes customerTypes, List<Customer> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> root = cq.from(Customer.class);
			Predicate con = cb.conjunction();
			if (customerTypes != null) {
				con.getExpressions().add(cb.equal(root.get("customer_types").get("id"), customerTypes.getId()));
			}
			if (text != null && !"".equals(text)) {
				Predicate dis = cb.disjunction();
				dis.getExpressions().add(cb.equal(root.get("customer_code"), text));
				dis.getExpressions().add(cb.like(root.get("customer_name"), "%" + text + "%"));
				con.getExpressions().add(dis);
			}
			cq.select(
					cb.construct(Customer.class, root.get("id"), root.get("customer_code"), root.get("customer_name")))
					.where(con);
			TypedQuery<Customer> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;

		} catch (Exception e) {
			logger.error("CustomerService.complete<text,customerTypes,list>:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectByCode(String code, CustomerReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> root = cq.from(Customer.class);
			cq.select(root).where(cb.equal(root.get("customer_code"), code));
			TypedQuery<Customer> query = em.createQuery(cq);
			t.setCustomer(query.getSingleResult());
		} catch (Exception e) {
			// logger.error("CustomerService.selectByCode:"+e.getMessage(),e);
		}
		return res;
	}

	@Override
	public Customer selectByCode(String code) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> root = cq.from(Customer.class);
			cq.select(root).where(cb.equal(root.get("customer_code"), code));
			TypedQuery<Customer> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			// logger.error("CustomerService.selectByCode:"+e.getMessage(),e);
		}
		return null;
	}

	@Override
	public int selectByCodeId(String code, CustomerReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> root = cq.from(Customer.class);
			cq.select(cb.construct(Customer.class, root.get("id"))).where(cb.equal(root.get("customer_code"), code));
			TypedQuery<Customer> query = em.createQuery(cq);
			t.setCustomer(query.getSingleResult());
		} catch (Exception e) {
			// logger.error("CustomerService.selectByCode:"+e.getMessage(),e);
		}
		return res;
	}

	@Override
	public Customer selectOnlyId(String code) {
		try {
			Query query = em.createQuery("select new Customer(id)  from Customer where customer_code=:cd");
			query.setParameter("cd", code);
			return (Customer) query.getSingleResult();
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public lixco.com.reqfox.Customer getCustomerFoxPro(long customerId) {
		try {
			// Customer(String masothue, String makh, String tenkh, String
			// diachi, String dienthoai, String fax,
			// String matp, String thanhpho,String makhuvuc,String
			// tenkhuvuc,String maquocgia, String tenqgia, String tenqgia_e,
			// String taikhoan, String donvi, String kh_categ_id, String
			// kh_category, String makenh, String masokh, double songayno,
			// String luu_y, String diadiemgh, double tlkkhich, double
			// tlhoahong, double tiennpp,
			// String masoncc, boolean not_conti, double tlhhong_ntr, boolean
			// khongin, String email)
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<lixco.com.reqfox.Customer> cq = cb.createQuery(lixco.com.reqfox.Customer.class);
			Root<Customer> root = cq.from(Customer.class);
			Join<Customer, City> city_ = root.join("city", JoinType.LEFT);
			Join<City, Country> country_ = city_.join("country", JoinType.LEFT);
			Join<City, Area> area_ = city_.join("area", JoinType.LEFT);
			Join<Customer, CustomerTypes> customerTypes_ = root.join("customer_types", JoinType.LEFT);
			Join<CustomerTypes, CustomerChannel> customerChannel_ = customerTypes_.join("customer_channel",
					JoinType.LEFT);
			cq.select(
					cb.construct(lixco.com.reqfox.Customer.class, root.get("tax_code"), root.get("customer_code"),
							root.get("customer_name"), root.get("address"), root.get("home_phone"), root.get("fax"),
							city_.get("city_code"), city_.get("city_name"), area_.get("area_code"),
							area_.get("area_name"), country_.get("country_code"), country_.get("country_name"),
							country_.get("en_name"), root.get("bank_account_no"), root.get("company_name"),
							customerTypes_.get("code"), customerTypes_.get("name"), customerChannel_.get("id"),
							root.get("customer_dfcode"), root.get("days_debt_quantity"), root.get("note"),
							root.get("location_delivery"), root.get("encourage_rate"), root.get("commission_value"),
							root.get("amount_npp"), root.get("supplier_code"), root.get("disable"),
							root.get("commission_ntrl_value"), root.get("not_print_customer_name"), root.get("email")))
					.where(cb.equal(root.get("id"), customerId));
			TypedQuery<lixco.com.reqfox.Customer> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			logger.error("CustomerService.getCustomerFoxPro:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<Customer> selectAllByCity(long cityid, long customerTypes_id) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
			Root<Customer> root = cq.from(Customer.class);
			List<Predicate> predicates = new LinkedList<Predicate>();
			if (cityid != 0) {
				predicates.add(cb.equal(root.get("city").get("id"), cityid));
			}
			if (customerTypes_id != 0) {
				predicates.add(cb.equal(root.get("customer_types").get("id"), customerTypes_id));
			}

			cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
			TypedQuery<Customer> query = em.createQuery(cq);
			List<Customer> customers = query.setMaxResults(1000).getResultList();

			// for (int i = 0; i < customers.size(); i++) {
			// if (customers.get(i).getCustomer_types() != null)
			// customers.get(i).getCustomer_types().getCode();
			// }
			return customers;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Customer>();
		}
	}

	@Inject
	ICityService cityService;
	@Inject
	ICountryService countryService;
	@Inject
	IAreaService areaService;
	@Inject
	ICustomerChannelService customerChannelService;
	@Inject
	ICustomerTypesService customerTypesService;

	@Override
	public int dongbokhachhang(CustomerAsyncDTO customerAsyncDTO, StringBuilder errors) {
		int dem = 0;

		try {
			// Dong bo city
			List<CityDTO> cityDTOs = customerAsyncDTO.getCityDTOs();
			for (CityDTO it : cityDTOs) {
				City cityNew = new City(it);
				cityNew.setCountry(countryService.findByCode(it.getCountryCode()));
				cityNew.setArea(areaService.findByCode(it.getAreaCode()));
				City pOld = cityService.findByCode(it.getCity_code());
				if (pOld != null) {
					cityNew.setId(pOld.getId());
					CityReqInfo cityReqInfo = new CityReqInfo(cityNew);
					int result = cityService.update(cityReqInfo);
					if (result == -1) {
						errors.append("Lỗi city:" + it.getCity_code() + ";");
					}
				} else {
					CityReqInfo cityReqInfo = new CityReqInfo(cityNew);
					int result = cityService.insert(cityReqInfo);
					if (result == -1) {
						errors.append("Lỗi city:" + it.getCity_code() + ";");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Loi dong bo city");
		}

		// kenh khach hang
		List<CustomerChannelDTO> customerChannelDTOs = customerAsyncDTO.getCustomerChannelDTOs();
		for (CustomerChannelDTO it : customerChannelDTOs) {
			CustomerChannel cChannelNew = new CustomerChannel(it);
			CustomerChannel pOld = customerChannelService.findByName(it.getName());
			if (pOld != null) {
				cChannelNew.setId(pOld.getId());
				CustomerChannelReqInfo customerChannelReqInfo = new CustomerChannelReqInfo(cChannelNew);
				int result = customerChannelService.update(customerChannelReqInfo);
				if (result == -1) {
					errors.append("Lỗi CustomerChannel:" + it.getName() + ";");
				}
			} else {
				CustomerChannelReqInfo productComReqInfo = new CustomerChannelReqInfo(cChannelNew);
				int result = customerChannelService.insert(productComReqInfo);
				if (result == -1) {
					errors.append("Lỗi CustomerChannel:" + it.getName() + ";");
				}
			}
		}
		// loai khach hang
		List<CustomerTypesDTO> customerTypesDTOs = customerAsyncDTO.getCustomerTypesDTOs();
		for (CustomerTypesDTO it : customerTypesDTOs) {
			CustomerTypes customerTypesNew = new CustomerTypes(it);
			customerTypesNew.setCustomer_channel(customerChannelService.findByName(it.getNameCustomer_channel()));
			CustomerTypes pOld = customerTypesService.findByCode(it.getCode());
			if (pOld != null) {
				customerTypesNew.setId(pOld.getId());
				CustomerTypesReqInfo customerTypesReqInfo = new CustomerTypesReqInfo(customerTypesNew);
				int result = customerTypesService.update(customerTypesReqInfo);
				if (result != 0) {
					errors.append("Lỗi CustomerTypes:" + it.getName() + ";");
				}
			} else {
				CustomerTypesReqInfo customerTypesReqInfo = new CustomerTypesReqInfo(customerTypesNew);
				int result = customerTypesService.insert(customerTypesReqInfo);
				if (result != 0) {
					errors.append("Lỗi CustomerTypes:" + it.getName() + ";");
				}
			}
		}
		// Dong bo khach hang
		List<CustomerDTO> customerDTOs = customerAsyncDTO.getCustomerDTOs();
		for (CustomerDTO it : customerDTOs) {
			Customer customer = new Customer(it);
			customer.setCustomer_types(customerTypesService.findByCode(it.getCustomer_typesCode()));
			customer.setCity(cityService.findByCode(it.getCityCode()));
			Customer pOld = selectByCode(it.getCustomer_code());
			if (pOld != null) {
				customer.setId(pOld.getId());
				customer.setCreated_date(pOld.getCreated_date());
				customer.setLast_modifed_date(new Date());
				customer.setCreated_by(pOld.getCreated_by());
				customer.setGhichudongbo(pOld.getGhichudongbo() + ";Sync: " + MyUtilEJB.chuyensangStrHH(new Date()));
				CustomerReqInfo t = new CustomerReqInfo(customer);
				int result = update(t);
				if (result == -1) {
					errors.append("Lỗi Customer:" + it.getCustomer_code() + ";");
				} else {
					dem++;
				}
			} else {
				customer.setId(0);
				customer.setCreated_date(new Date());
				customer.setCreated_by("Ðồng bộ");
				customer.setGhichudongbo("Sync: " + MyUtilEJB.chuyensangStrHH(new Date()));
				CustomerReqInfo t = new CustomerReqInfo(customer);
				int result = insertAvaiCode(t);
				if (result == -1) {
					errors.append("Lỗi Customer:" + it.getCustomer_code() + ";");
				} else {
					dem++;
				}
			}
		}
		return dem;
	}
}
