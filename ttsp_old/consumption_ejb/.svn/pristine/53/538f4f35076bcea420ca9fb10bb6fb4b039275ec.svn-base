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
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.commom_ejb.MyUtilEJB;
import lixco.com.entity.Car;
import lixco.com.entity.CarOwner;
import lixco.com.entity.Customer;
import lixco.com.entity.Invoice;
import lixco.com.entity.Product;
import lixco.com.entity.ProductBrand;
import lixco.com.entity.ProductCom;
import lixco.com.entity.ProductGroup;
import lixco.com.entity.ProductKM;
import lixco.com.entity.ProductType;
import lixco.com.entity.PromotionProductGroup;
import lixco.com.entityapi.KhachHangData;
import lixco.com.entityapi.ProductAsyncDTO;
import lixco.com.entityapi.ProductBrandDTO;
import lixco.com.entityapi.ProductComDTO;
import lixco.com.entityapi.ProductDTO;
import lixco.com.entityapi.ProductGroupDTO;
import lixco.com.entityapi.ProductKMDTO;
import lixco.com.entityapi.ProductTypeDTO;
import lixco.com.entityapi.PromotionProductGroupDTO;
import lixco.com.entityapi.SanPhamData;
import lixco.com.interfaces.IProductBrandService;
import lixco.com.interfaces.IProductComService;
import lixco.com.interfaces.IProductGroupService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IPromotionProductGroupService;
import lixco.com.reqInfo.ProductBrandReqInfo;
import lixco.com.reqInfo.ProductComReqInfo;
import lixco.com.reqInfo.ProductGroupReqInfo;
import lixco.com.reqInfo.ProductReqInfo;
import lixco.com.reqInfo.ProductTypeReqInfo;
import lixco.com.reqInfo.PromotionProductGroupReqInfo;
import lixco.com.reqfox.Customer.KhuVuc;

import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class ProductService implements IProductService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@Override
	public List<Product> findAll() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root = cq.from(Product.class);
			root.fetch("product_com", JoinType.LEFT);
			root.fetch("product_type", JoinType.LEFT);
			root.fetch("product_group", JoinType.LEFT);
			root.fetch("promotion_product", JoinType.LEFT);
			root.fetch("promotion_product_group", JoinType.LEFT);
			cq.select(root);
			TypedQuery<Product> query = em.createQuery(cq);
			return query.getResultList();

		} catch (Exception e) {
			logger.error("ProductService.selectAll:" + e.getMessage(), e);
		}
		return new ArrayList<Product>();
	}

	@Override
	public List<Product> findNotSync() {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root = cq.from(Product.class);
			root.fetch("product_com", JoinType.LEFT);
			root.fetch("product_type", JoinType.LEFT);
			root.fetch("product_group", JoinType.LEFT);
			root.fetch("promotion_product", JoinType.LEFT);
			root.fetch("promotion_product_group", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("capnhat"), true));
			TypedQuery<Product> query = em.createQuery(cq);
			return query.getResultList();

		} catch (Exception e) {
			logger.error("ProductService.findNotSync:" + e.getMessage(), e);
		}
		return new ArrayList<Product>();
	}

	@Override
	public long findByMaSPGetId(String masp) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Product> root = cq.from(Product.class);
			cq.select(root.get("id")).where(cb.equal(root.get("product_code"), masp));
			TypedQuery<Long> query = em.createQuery(cq);
			return query.getSingleResult();

		} catch (Exception e) {
			logger.error("ProductService.findNotSync:" + e.getMessage(), e);
		}
		return 0;
	}

	@Override
	public Product findIdGetPCom(long id) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root = cq.from(Product.class);
			root.fetch("product_com", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<Product> query = em.createQuery(cq);
			List<Product> results = query.setMaxResults(1).getResultList();
			if (results.size() != 0) {
				return results.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public Product findIdGetPType(long id) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root = cq.from(Product.class);
			root.fetch("product_type", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<Product> query = em.createQuery(cq);
			List<Product> results = query.setMaxResults(1).getResultList();
			if (results.size() != 0) {
				return results.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int search(String json, List<Product> list) {
		int res = -1;
		try {/*
			 * { product_info:{product_code:
			 * '',product_name:'',lever_code:'',product_com_id:0},
			 * page:{page_index:0, page_size:0}}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hProductStr = JsonParserUtil.getValueString(j.get("product_info"), "stextStr", null);
			HolderParser hProductComId = JsonParserUtil.getValueNumber(j.get("product_info"), "product_com_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(j.get("product_info"), "product_type_id", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root_ = cq.from(Product.class);

			root_.fetch("product_com", JoinType.LEFT);
			root_.fetch("product_com", JoinType.LEFT).fetch("product_brand", JoinType.LEFT);

			root_.fetch("product_type", JoinType.LEFT);
			root_.fetch("product_group", JoinType.LEFT);
			root_.fetch("promotion_product", JoinType.LEFT);
			root_.fetch("promotion_product_group", JoinType.LEFT);

			List<Predicate> predicates = new ArrayList<Predicate>();
			ParameterExpression<String> pProductStr = cb.parameter(String.class);
			ParameterExpression<Long> pProductTypeId = cb.parameter(Long.class);
			ParameterExpression<Long> pProductComId = cb.parameter(Long.class);

			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.equal(pProductTypeId, 0));
			dis2.getExpressions().add(cb.equal(root_.get("product_type").get("id"), pProductTypeId));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pProductComId, 0));
			dis3.getExpressions().add(cb.equal(root_.get("product_com").get("id"), pProductComId));
			predicates.add(dis3);

			List<Predicate> predicatesStr = new ArrayList<Predicate>();

			String stextStr = Objects.toString(hProductStr.getValue());
			if (hProductStr.getValue() != null && !"".equals(stextStr.trim())) {
				Predicate predicate_code = cb.like(root_.get("product_code"), "%" + stextStr + "%");
				predicatesStr.add(predicate_code);
				Predicate predicate_name = cb.like(root_.get("product_name"), "%" + stextStr + "%");
				predicatesStr.add(predicate_name);

				Join<Invoice, Customer> prod_type = root_.join("product_type", JoinType.LEFT);
				Predicate predicateProdTypecode = cb.like(prod_type.get("code"), "%" + stextStr + "%");// ma
																										// khach
																										// hang
				predicatesStr.add(predicateProdTypecode);
				Predicate predicateProdTypename = cb.like(prod_type.get("name"), "%" + stextStr + "%");// ten
																										// khach
																										// hang
				predicatesStr.add(predicateProdTypename);

				Join<Invoice, Customer> prod_com = root_.join("product_com", JoinType.LEFT);
				Predicate predicateProdComcode = cb.like(prod_com.get("pcom_code"), "%" + stextStr + "%");// ma
																											// khach
																											// hang
				predicatesStr.add(predicateProdComcode);
				Predicate predicateProdComname = cb.like(prod_com.get("pcom_name"), "%" + stextStr + "%");// ten
																											// khach
																											// hang
				predicatesStr.add(predicateProdComname);
			}

			if (predicatesStr.size() != 0) {
				cq.select(root_)
						.where(cb.and(predicates.toArray(new Predicate[0])),
								cb.or(predicatesStr.toArray(new Predicate[0]))).orderBy(cb.desc(root_.get("id")));
			} else {
				cq.select(root_).where(cb.and(predicates.toArray(new Predicate[0]))).orderBy(cb.desc(root_.get("id")));
			}
			TypedQuery<Product> query = em.createQuery(cq);
			query.setParameter(pProductComId, Long.parseLong(Objects.toString(hProductComId.getValue(), "0")));
			query.setParameter(pProductTypeId, Long.parseLong(Objects.toString(hProductTypeId.getValue(), "0")));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("ProductService.search:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectBy(String json, List<Product> list) {
		int res = -1;
		try {/*
			 * { product_info:{product_code:
			 * '',product_name:'',lever_code:'',product_com_id:0}}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hProductCode = JsonParserUtil.getValueString(j.get("product_info"), "product_code", null);
			HolderParser hProductName = JsonParserUtil.getValueString(j.get("product_info"), "product_name", null);
			HolderParser hLeverCode = JsonParserUtil.getValueString(j.get("product_info"), "lever_code", null);
			HolderParser hProductComId = JsonParserUtil.getValueNumber(j.get("product_info"), "product_com_id", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root_ = cq.from(Product.class);
			root_.fetch("product_com", JoinType.LEFT);
			root_.fetch("product_type", JoinType.LEFT);
			root_.fetch("product_group", JoinType.LEFT);
			root_.fetch("promotion_product_group", JoinType.LEFT);
			List<Predicate> predicates = new ArrayList<Predicate>();
			ParameterExpression<String> pProductCode = cb.parameter(String.class);
			ParameterExpression<String> pProductName = cb.parameter(String.class);
			ParameterExpression<String> pProductNameLike = cb.parameter(String.class);
			ParameterExpression<String> pLeverCode = cb.parameter(String.class);
			ParameterExpression<Long> pProductComId = cb.parameter(Long.class);
			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.isNull(pProductCode));
			dis.getExpressions().add(cb.equal(pProductCode, ""));
			dis.getExpressions().add(cb.equal(root_.get("product_code"), pProductCode));
			predicates.add(dis);
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.isNull(pProductName));
			dis1.getExpressions().add(cb.equal(pProductName, ""));
			dis1.getExpressions().add(cb.like(root_.get("product_name"), pProductNameLike));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.isNull(pLeverCode));
			dis2.getExpressions().add(cb.equal(pLeverCode, ""));
			dis2.getExpressions().add(cb.equal(root_.get("lever_code"), pLeverCode));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pProductComId, 0));
			dis3.getExpressions().add(cb.equal(root_.get("product_com").get("id"), pProductComId));
			predicates.add(dis3);
			cq.select(root_).where(cb.and(predicates.toArray(new Predicate[0])));
			TypedQuery<Product> query = em.createQuery(cq);
			query.setParameter(pProductCode, Objects.toString(hProductCode.getValue(), null));
			query.setParameter(pProductName, Objects.toString(hProductName.getValue(), null));
			query.setParameter(pProductNameLike, "%" + Objects.toString(hProductName.getValue(), null) + "%");
			query.setParameter(pLeverCode, Objects.toString(hLeverCode.getValue(), null));
			query.setParameter(pProductComId, Long.parseLong(Objects.toString(hProductComId.getValue(), "0")));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("ProductService.search:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int insert(ProductReqInfo t) {
		int res = -1;
		try {
			Product p = t.getProduct();
			if (p != null) {
				List<ProductKM> productKMs = p.getProductKMs();
				p.setProductKMs(null);
				p.setCapnhat(true);
				em.persist(p);
				if (p.getId() > 0) {
					for (int i = 0; i < productKMs.size(); i++) {
						productKMs.get(i).setProduct(p);
						productKMs.get(i).setId(0);
						if (productKMs.get(i).getPromotion_product() == null) {
							productKMs.get(i).setPromotion_product(
									selectByCode(productKMs.get(i).getPromotion_product_code()));
						}
						if (productKMs.get(i).getPromotion_product() != null)
							em.persist(productKMs.get(i));
					}
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("ProductService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int update(ProductReqInfo t) {
		int res = -1;
		try {
			Product p = t.getProduct();
			if (p != null) {
				// Xoa san pham km cu
				Query query = em.createQuery("delete from ProductKM where product_id=:id ");
				query.setParameter("id", p.getId());
				res = query.executeUpdate();

				List<ProductKM> productKMs = p.getProductKMs();
				p.setProductKMs(null);
				p.setCapnhat(true);
				p = em.merge(p);
				if (p != null) {
					selectById(p.getId(), t);
					for (int i = 0; i < productKMs.size(); i++) {
						productKMs.get(i).setProduct(p);
						productKMs.get(i).setId(0);
						if (productKMs.get(i).getPromotion_product() == null) {
							productKMs.get(i).setPromotion_product(
									selectByCode(productKMs.get(i).getPromotion_product_code()));
						}
						if (productKMs.get(i).getPromotion_product() != null)
							em.persist(productKMs.get(i));
					}
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("ProductService.update:" + e.getMessage(), e);
		}
		return res;
	}
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public int update(Product p) {
		int res = -1;
		try {
			if (p != null) {
				// Xoa san pham km cu
				Query query = em.createQuery("delete from ProductKM where product_id=:id ");
				query.setParameter("id", p.getId());
				res = query.executeUpdate();
				List<ProductKM> productKMs = p.getProductKMs();
				p.setProductKMs(null);
				p.setCapnhat(true);
				p = em.merge(p);
				if (p != null) {
					for (int i = 0; i < productKMs.size(); i++) {
						productKMs.get(i).setProduct(p);
						productKMs.get(i).setId(0);
						if (productKMs.get(i).getPromotion_product() == null) {
							productKMs.get(i).setPromotion_product(
									selectByCode(productKMs.get(i).getPromotion_product_code()));
						}
						if (productKMs.get(i).getPromotion_product() != null)
							em.persist(productKMs.get(i));
					}
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("ProductService.update:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int updateCapNhat(List<Long> ids) {
		int res = -1;
		try {
			if (ids != null && ids.size() != 0) {
				Query query = em.createQuery("update Product set capnhat=false where id in :ids");
				query.setParameter("ids", ids);
				return query.executeUpdate();
			}
		} catch (Exception e) {
			logger.error("ProductService.updateCapNhat:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectById(long id, ProductReqInfo t) {
		int res = -1;
		try {
			Product p = em.find(Product.class, id);
			if (p != null) {
				Hibernate.initialize(p.getProduct_com());
				Hibernate.initialize(p.getProduct_group());
				Hibernate.initialize(p.getProduct_type());
				Hibernate.initialize(p.getPromotion_product());
				Hibernate.initialize(p.getPromotion_product_group());
				t.setProduct(p);
				res = 0;
			}
		} catch (Exception e) {
			logger.error("ProductService.selectById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public Product findById(long id) {
		try {
			Product p = em.find(Product.class, id);
			if (p != null) {
				Hibernate.initialize(p.getProduct_com());
				Hibernate.initialize(p.getProduct_group());
				Hibernate.initialize(p.getProduct_type());
				Hibernate.initialize(p.getPromotion_product());
				Hibernate.initialize(p.getPromotion_product_group());
				return p;
			}
		} catch (Exception e) {
			logger.error("ProductService.selectById:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int deleteById(long id,StringBuilder error) {
		int res = -1;
		try {
			// JQPL
			Query query = em.createQuery("delete from Product where id=:id ");
			query.setParameter("id", id);
			res = query.executeUpdate();
		} catch (Exception e) {
			Throwable t = e.getCause();
		    if (t instanceof ConstraintViolationException) {
		        ConstraintViolationException cve = (ConstraintViolationException) t;
		        String errorMessage = cve.getSQLException().getMessage();
		        error.append(errorMessage);
		    } else {
		        error.append(e.toString());
		    }
		}
		return res;
	}

	@Override
	public int findLike(String text, int size, List<Product> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root = cq.from(Product.class);
			ParameterExpression<String> paramLike = cb.parameter(String.class);
			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.like(root.get("product_code"), paramLike));
			dis.getExpressions().add(
					cb.like(cb.function("replace", String.class, cb.lower(root.get("product_name")), cb.literal("đ"),
							cb.literal("d")), paramLike));
			cq.select(root).where(dis);
			TypedQuery<Product> query = em.createQuery(cq);
			query.setParameter(paramLike, "%" + text + "%");
			if (size != -1) {
				query.setFirstResult(0);
				query.setMaxResults(size);
			}
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("ProductService.findLike:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int checkProductCode(String code, long productId) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Product> root = cq.from(Product.class);
			ParameterExpression<String> pProductCode = cb.parameter(String.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			Predicate dis = cb.disjunction();
			Predicate con1 = cb.conjunction();
			Predicate con2 = cb.conjunction();
			con1.getExpressions().add(cb.equal(pProductId, 0));
			con1.getExpressions().add(cb.equal(root.get("product_code"), pProductCode));
			dis.getExpressions().add(con1);
			con2.getExpressions().add(cb.notEqual(pProductId, 0));
			con2.getExpressions().add(cb.notEqual(root.get("id"), pProductId));
			con2.getExpressions().add(cb.equal(root.get("product_code"), pProductCode));
			dis.getExpressions().add(con2);
			cq.select(cb.count(root.get("id"))).where(dis);
			TypedQuery<Long> query = em.createQuery(cq);
			query.setParameter(pProductId, productId);
			query.setParameter(pProductCode, code);
			res = query.getSingleResult().intValue();
		} catch (Exception e) {
			logger.error("ProductService.checkProductCode:" + e.getMessage(), e);
		}
		return res;
	}
	@Override
	public int checkProductName(String name, long productId) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Product> root = cq.from(Product.class);
			ParameterExpression<String> pProductname = cb.parameter(String.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			Predicate dis = cb.disjunction();
			Predicate con1 = cb.conjunction();
			Predicate con2 = cb.conjunction();
			con1.getExpressions().add(cb.equal(pProductId, 0));
			con1.getExpressions().add(cb.equal(root.get("product_name"), pProductname));
			dis.getExpressions().add(con1);
			con2.getExpressions().add(cb.notEqual(pProductId, 0));
			con2.getExpressions().add(cb.notEqual(root.get("id"), pProductId));
			con2.getExpressions().add(cb.equal(root.get("product_name"), pProductname));
			dis.getExpressions().add(con2);
			cq.select(cb.count(root.get("id"))).where(dis);
			TypedQuery<Long> query = em.createQuery(cq);
			query.setParameter(pProductId, productId);
			query.setParameter(pProductname, name);
			res = query.getSingleResult().intValue();
		} catch (Exception e) {
			logger.error("ProductService.checkProductCode:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int complete(String text, List<Product> list) {
		int res = -1;
		try {
			if (text != null && !"".equals(text)) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Product> cq = cb.createQuery(Product.class);
				Root<Product> root = cq.from(Product.class);
				Predicate dis = cb.disjunction();
				dis.getExpressions().add(cb.like(root.get("product_code"), "%" + text + "%"));
				dis.getExpressions().add(cb.like(root.get("product_name"), "%" + text + "%"));
				cq.select(
						cb.construct(Product.class, root.get("id"), root.get("product_code"), root.get("product_name")))
						.where(dis, cb.equal(root.get("disable"), false));
				TypedQuery<Product> query = em.createQuery(cq);
				list.addAll(query.getResultList());

			}
			res = 0;
		} catch (Exception e) {
			logger.error("ProductService.complete:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectByCode(String code, ProductReqInfo reqInfo) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root = cq.from(Product.class);
			cq.select(root).where(cb.equal(root.get("product_code"), code));
			TypedQuery<Product> query = em.createQuery(cq);
			reqInfo.setProduct(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			// logger.error("ProductService.selectByCode:"+e.getMessage(),e);
		}
		return res;
	}

	@Override
	public int complete2(String text, List<Product> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root = cq.from(Product.class);
			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.equal(root.get("product_code"), text));
			dis.getExpressions().add(
					cb.like(cb.function("replace", String.class, cb.lower(root.get("product_name")), cb.literal("đ"),
							cb.literal("d")), "%" + text + "%"));
			cq.select(
					cb.construct(Product.class, root.get("id"), root.get("product_code"), root.get("product_name"),
							root.get("box_quantity"))).where(dis);
			TypedQuery<Product> query = em.createQuery(cq);
			list.addAll(query.getResultList());
		} catch (Exception e) {
			logger.error("ProductService.complete2:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int complete3(String text, List<Product> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root = cq.from(Product.class);
			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.equal(root.get("product_code"), text));
			dis.getExpressions().add(
					cb.like(cb.function("replace", String.class, cb.lower(root.get("product_name")), cb.literal("đ"),
							cb.literal("d")), "%" + text + "%"));
			cq.select(
					cb.construct(Product.class, root.get("id"), root.get("product_code"), root.get("product_name"),
							root.get("specification"), root.get("box_quantity"), root.get("factor"), root.get("tare"),
							root.get("unit"))).where(dis);
			TypedQuery<Product> query = em.createQuery(cq);
			list.addAll(query.getResultList());
		} catch (Exception e) {
			logger.error("ProductService.complete3:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int completeByProductType(String text, long productTypeId, List<Product> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root = cq.from(Product.class);
			Predicate con = cb.conjunction();
			if (productTypeId != 0) {
				con.getExpressions().add(cb.equal(root.get("product_type").get("id"), productTypeId));
			}
			Predicate dis = cb.disjunction();
			dis.getExpressions().add(cb.equal(root.get("product_code"), text));
			dis.getExpressions().add(
					cb.like(cb.function("replace", String.class, cb.lower(root.get("product_name")), cb.literal("đ"),
							cb.literal("d")), "%" + text + "%"));
			con.getExpressions().add(dis);
			cq.select(cb.construct(Product.class, root.get("id"), root.get("product_code"), root.get("product_name")))
					.where(con);
			TypedQuery<Product> query = em.createQuery(cq);
			list.addAll(query.getResultList());
		} catch (Exception e) {
			logger.error("ProductService.completeByProductType:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public Product selectOnlyId(String productCode) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root = cq.from(Product.class);
			cq.select(cb.construct(Product.class, root.get("id"))).where(
					cb.equal(root.get("product_code"), productCode));
			TypedQuery<Product> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			// logger.error("ProductService.selectByCode:"+e.getMessage(),e);
		}
		return null;
	}

	@Override
	public Product selectForDelivery(long id) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root = cq.from(Product.class);
			Fetch<Product, ProductCom> pcom_ = root.fetch("product_com", JoinType.LEFT);
			pcom_.fetch("product_brand", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<Product> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public lixco.com.reqfox.Product getProductFoxPro(long id) {
		try {
			// (long id, String masp, String tensp, String tenkbhq,String
			// manhomsp,String tennhomsp, String categ_id,String category,
			// String e_category,String dvt, double hsqd,String masp_comp,
			// String tensp_comp, String masp_brandp, String brand_namep, String
			// dvtbp,
			// String dvtp, double sl_carton, String dv_carton,
			// double tlthung, double sldutru, String maspvs, boolean
			// discontinued, boolean lxuatkhau,
			// String thongtinpl, String maspkmai, double slpallet)
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<lixco.com.reqfox.Product> cq = cb.createQuery(lixco.com.reqfox.Product.class);
			Root<Product> root = cq.from(Product.class);
			Join<Product, Product> promotionProduct_ = root.join("promotion_product", JoinType.LEFT);
			Join<Product, ProductType> productType_ = root.join("product_type", JoinType.LEFT);
			Join<Product, ProductGroup> productGroup_ = root.join("product_group", JoinType.LEFT);
			Join<Product, ProductCom> productCom_ = root.join("product_com", JoinType.LEFT);
			Join<ProductCom, ProductBrand> productBrand_ = productCom_.join("product_brand", JoinType.LEFT);
			cq.select(
					cb.construct(lixco.com.reqfox.Product.class, root.get("id"), root.get("product_code"),
							root.get("product_name"), root.get("customs_name"), productGroup_.get("code"),
							productGroup_.get("name"), productType_.get("code"), productType_.get("name"),
							productType_.get("en_name"), root.get("unit"), root.get("factor"),
							productCom_.get("pcom_code"), productCom_.get("pcom_name"),
							productBrand_.get("pbrand_code"), productBrand_.get("pbrand_name"),
							productBrand_.get("unit"), productCom_.get("unit"), root.get("specification"),
							root.get("packing_unit"), root.get("tare"), root.get("reserve_quantity"),
							root.get("lever_code"), root.get("disable"), root.get("typep"), root.get("product_info"),
							promotionProduct_.get("product_code"), root.get("box_quantity"))).where(
					cb.equal(root.get("id"), id));
			TypedQuery<lixco.com.reqfox.Product> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Product selectByCode(String code) {
		try {
			if (code != null && !"".equals(code)) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Product> cq = cb.createQuery(Product.class);
				Root<Product> root = cq.from(Product.class);
				cq.select(root).where(cb.equal(root.get("product_code"), code));
				TypedQuery<Product> query = em.createQuery(cq);
				return query.getSingleResult();
			}
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public Product findByCodeUFull(String code) {
		try {
			if (code != null && !"".equals(code)) {
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<Product> cq = cb.createQuery(Product.class);
				Root<Product> root = cq.from(Product.class);
				root.fetch("product_com", JoinType.LEFT);
				root.fetch("product_type", JoinType.LEFT);
				root.fetch("product_group", JoinType.LEFT);
				root.fetch("promotion_product", JoinType.LEFT);
				root.fetch("promotion_product_group", JoinType.LEFT);
				cq.select(root).where(cb.equal(root.get("lever_code"), code), cb.equal(root.get("disable"), false));
				TypedQuery<Product> query = em.createQuery(cq);
				List<Product> products = query.getResultList();
				if (products.size() != 0)
					return products.get(0);
			}
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public Product selectByCodeAll(String code) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Product> cq = cb.createQuery(Product.class);
			Root<Product> root = cq.from(Product.class);
			Join<Product, Product> promotionProduct_ = root.join("promotion_product", JoinType.LEFT);
			Join<Product, ProductType> productType_ = root.join("product_type", JoinType.LEFT);
			Join<Product, ProductGroup> productGroup_ = root.join("product_group", JoinType.LEFT);
			Join<Product, ProductCom> productCom_ = root.join("product_com", JoinType.LEFT);
			Join<ProductCom, ProductBrand> productBrand_ = productCom_.join("product_brand", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("product_code"), code));
			TypedQuery<Product> query = em.createQuery(cq);
			Product pd = query.getSingleResult();
			// if(pd!=null){
			// pd.getPromotion_product();
			// }
			return pd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Inject
	IProductTypeService productTypeService;
	@Inject
	IProductGroupService productGroupService;
	@Inject
	IProductComService productComService;
	@Inject
	IProductBrandService productBrandService;
	@Inject
	IPromotionProductGroupService promotionProductGroupService;

	@Override
	public int dongbosanpham(ProductAsyncDTO productAsyncDTO, StringBuilder errors) {
		int dem = 0;

		// Dong bo san pham brand
		List<ProductBrandDTO> productBrandDTOs = productAsyncDTO.getProductBrandDTOs();
		for (ProductBrandDTO it : productBrandDTOs) {
			ProductBrand prBrand = new ProductBrand(it);
			ProductBrand pOld = productBrandService.findByCode(it.getPbrand_code());
			if (pOld != null) {
				prBrand.setId(pOld.getId());
				ProductBrandReqInfo productBrandReqInfo = new ProductBrandReqInfo(prBrand);
				int result = productBrandService.update(productBrandReqInfo);
				if (result == -1) {
					errors.append("Lỗi ProductGroup:" + it.getPbrand_code() + ";");
				}
			} else {
				ProductBrandReqInfo productBrandReqInfo = new ProductBrandReqInfo(prBrand);
				int result = productBrandService.insert(productBrandReqInfo);
				if (result == -1) {
					errors.append("Lỗi ProductGroup:" + it.getPbrand_code() + ";");
				}
			}
		}

		// Dong bo san pham com
		List<ProductComDTO> productComDTOs = productAsyncDTO.getProductComDTOs();
		for (ProductComDTO it : productComDTOs) {
			ProductCom prCom = new ProductCom(it);
			prCom.setProduct_brand(productBrandService.findByCode(it.getProduct_brand()));
			ProductCom pOld = productComService.selectByCode(it.getPcom_code());
			if (pOld != null) {
				prCom.setId(pOld.getId());
				ProductComReqInfo productComReqInfo = new ProductComReqInfo(prCom);
				int result = productComService.update(productComReqInfo);
				if (result == -1) {
					errors.append("Lỗi ProductCom:" + it.getPcom_code() + ";");
				}
			} else {
				ProductComReqInfo productComReqInfo = new ProductComReqInfo(prCom);
				int result = productComService.insert(productComReqInfo);
				if (result == -1) {
					errors.append("Lỗi ProductCom:" + it.getPcom_code() + ";");
				}
			}
		}
		// Dong bo nhom san pham
		List<ProductGroupDTO> productGroupDTOs = productAsyncDTO.getProductGroupDTOs();
		for (ProductGroupDTO it : productGroupDTOs) {
			ProductGroup prGroup = new ProductGroup(it);
			ProductGroup pOld = productGroupService.selectByCode(it.getCode());
			if (pOld != null) {
				prGroup.setId(pOld.getId());
				ProductGroupReqInfo productGroupReqInfo = new ProductGroupReqInfo(prGroup);
				int result = productGroupService.update(productGroupReqInfo);
				if (result == -1) {
					errors.append("Lỗi ProductGroup:" + it.getCode() + ";");
				}
			} else {
				ProductGroupReqInfo productTypeReqInfo = new ProductGroupReqInfo(prGroup);
				int result = productGroupService.insert(productTypeReqInfo);
				if (result == -1) {
					errors.append("Lỗi ProductGroup:" + it.getCode() + ";");
				}
			}
		}
		// Dong bo loai san pham
		List<ProductTypeDTO> productTypeDTOs = productAsyncDTO.getProductTypeDTOs();
		for (ProductTypeDTO it : productTypeDTOs) {
			ProductType prType = new ProductType(it);
			ProductType pOld = productTypeService.selectByCode(it.getCode());
			if (pOld != null) {
				prType.setId(pOld.getId());
				ProductTypeReqInfo productTypeReqInfo = new ProductTypeReqInfo(prType);
				int result = productTypeService.update(productTypeReqInfo);
				if (result == -1) {
					errors.append("Lỗi ProductType:" + it.getCode() + ";");
				}
			} else {
				ProductTypeReqInfo productTypeReqInfo = new ProductTypeReqInfo(prType);
				int result = productTypeService.insert(productTypeReqInfo);
				if (result == -1) {
					errors.append("Lỗi ProductType:" + it.getCode() + ";");
				}
			}
		}
		// nhom loai san pham km
		List<PromotionProductGroupDTO> promotionProductGroupDTOs = productAsyncDTO.getPromotionProductGroupDTOs();
		for (PromotionProductGroupDTO it : promotionProductGroupDTOs) {
			PromotionProductGroup prPd = new PromotionProductGroup(it);
			PromotionProductGroup pOld = promotionProductGroupService.selectByCode(it.getCode());
			if (pOld != null) {
				prPd.setId(pOld.getId());
				PromotionProductGroupReqInfo productTypeReqInfo = new PromotionProductGroupReqInfo(prPd);
				int result = promotionProductGroupService.update(productTypeReqInfo);
				if (result == -1) {
					errors.append("Lỗi PromotionProductGroup:" + it.getCode() + ";");
				}
			} else {
				PromotionProductGroupReqInfo productTypeReqInfo = new PromotionProductGroupReqInfo(prPd);
				int result = promotionProductGroupService.insert(productTypeReqInfo);
				if (result == -1) {
					errors.append("Lỗi PromotionProductGroup:" + it.getCode() + ";");
				}
			}
		}
		// Dong bo san pham
		List<ProductDTO> productDTOs = productAsyncDTO.getProductDTOs();
		for (ProductDTO it : productDTOs) {
			Product product = new Product(it);
			product.setProduct_com(productComService.selectByCode(it.getProduct_com()));
			product.setPromotion_product(selectByCode(it.getPromotion_product()));
			product.setProduct_type(productTypeService.selectByCode(it.getProduct_type()));
			product.setProduct_group(productGroupService.selectByCode(it.getProduct_group()));
			product.setPromotion_product_group(promotionProductGroupService.selectByCode(it
					.getPromotion_product_group()));
			List<ProductKM> productKMs = new ArrayList<ProductKM>();
			List<ProductKMDTO> productKMDTOs = it.getProductKMDTOs();
			for (int i = 0; i < productKMDTOs.size(); i++) {
				ProductKM itemKM = new ProductKM(productKMDTOs.get(i));
				itemKM.setPromotion_product(selectByCode(productKMDTOs.get(i).getPromotion_product()));
				productKMs.add(itemKM);
			}
			product.setProductKMs(productKMs);

			Product pOld = selectByCode(it.getProduct_code());
			if (pOld != null) {
				product.setId(pOld.getId());
				product.setCreated_date(pOld.getCreated_date());
				product.setLast_modifed_date(new Date());
				product.setCreated_by(pOld.getCreated_by());
				product.setGhichudongbo(pOld.getGhichudongbo() + ";Sync: " + MyUtilEJB.chuyensangStrHHmm(new Date()));
				ProductReqInfo t = new ProductReqInfo(product);
				int result = update(t);
				if (result == -1) {
					errors.append("Lỗi Product:" + it.getProduct_code() + ";");
				} else {
					dem++;
				}
			} else {
				product.setId(0);
				product.setCreated_date(new Date());
				product.setCreated_by("Đồng bộ");
				product.setGhichudongbo("Sync: " + MyUtilEJB.chuyensangStrHHmm(new Date()));
				ProductReqInfo t = new ProductReqInfo(product);
				int result = insert(t);
				if (result == -1) {
					errors.append("Lỗi Product:" + it.getProduct_code() + ";");
				} else {
					dem++;
				}
			}
		}
		return dem;
	}

	@Override
	public List<SanPhamData> complete2(String masp, String tensp) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<SanPhamData> cq = cb.createQuery(SanPhamData.class);
			Root<Product> root = cq.from(Product.class);
			Predicate dis = cb.disjunction();
			if (masp != null && !"".equals(masp))
				dis.getExpressions().add(cb.equal(root.get("product_code"), masp));
			if (tensp != null && !"".equals(tensp))
				dis.getExpressions().add(cb.like(root.get("product_name"), "%" + tensp + "%"));
			cq.select(cb.construct(SanPhamData.class, root.get("product_code"), root.get("product_name"))).where(dis);
			TypedQuery<SanPhamData> query = em.createQuery(cq);
			List<SanPhamData> results = query.getResultList();
			return results;
		} catch (Exception e) {
			logger.error("ProductService.complete2<Text,list>:" + e.getMessage(), e);
		}
		return new ArrayList<SanPhamData>();
	}

}
