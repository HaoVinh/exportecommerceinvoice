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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.commom_ejb.ToolTimeCustomer;
import lixco.com.entity.Inventory;
import lixco.com.entity.Product;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.IInventoryService;
import lixco.com.reportInfo.HangChamLuanChuyen;
import lixco.com.reportInfo.TonKhoThang;
import lixco.com.report_service.ReportService;
import lixco.com.reqInfo.InfoInventory;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class InventoryService implements IInventoryService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@Override
	public int search(String json, List<Inventory> list) {
		int res = -1;
		try {
			/* {month:0, year:0,product_id:0,product_type_id:0} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hMonth = JsonParserUtil.getValueNumber(j, "month", null);
			HolderParser hYear = JsonParserUtil.getValueNumber(j, "year", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(j, "product_type_id", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			ParameterExpression<Integer> pMonth = cb.parameter(Integer.class);
			ParameterExpression<Integer> pYear = cb.parameter(Integer.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			ParameterExpression<Long> pProductTypeId = cb.parameter(Long.class);
			CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
			Root<Inventory> root = cq.from(Inventory.class);
			Join<Inventory, Product> product_ = root.join("product", JoinType.INNER);
			List<Predicate> predicates = new ArrayList<Predicate>();
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.equal(pMonth, 0));
			dis1.getExpressions().add(cb.equal(root.get("inventory_month"), pMonth));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.equal(pYear, 0));
			dis2.getExpressions().add(cb.equal(root.get("inventory_year"), pYear));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pProductId, 0));
			dis3.getExpressions().add(cb.equal(root.get("product").get("id"), pProductId));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pProductTypeId, 0));
			dis4.getExpressions().add(cb.equal(product_.get("product_type").get("id"), pProductTypeId));
			predicates.add(dis4);
			cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
			TypedQuery<Inventory> query = em.createQuery(cq);
			query.setParameter(pMonth, Integer.parseInt(Objects.toString(hMonth.getValue())));
			query.setParameter(pYear, Integer.parseInt(Objects.toString(hYear.getValue())));
			query.setParameter(pProductId, Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter(pProductTypeId, Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("InventoryService.search:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int saveOrUpdateInventory(int month, int year, String memberName, List<TonKhoThang> list) {
		int res = -1;
		try {
			// kiểm tra dữ liệu đã tồn tại chưa
			Query query = em
					.createQuery("select count(id) from Inventory where inventory_month=:m and inventory_year=:y");
			query.setParameter("m", month);
			query.setParameter("y", year);
			int count = Integer.parseInt(Objects.toString(query.getSingleResult()));
			if (count > 0) {
				// xóa dữ liệu cũ.
				Query queryDel = em
						.createQuery("delete from Inventory where inventory_month=:m and inventory_year=:y ");
				queryDel.setParameter("m", month);
				queryDel.setParameter("y", year);
				if (queryDel.executeUpdate() <= 0) {
					res = -1;
					ct.getUserTransaction().rollback();
					return res;
				}
			}
			// lưu dữ liệu
			for (TonKhoThang t : list) {
				Inventory item = new Inventory();
				item.setCreated_by(memberName);
				item.setCreated_date(new Date());
				item.setInventory_month(month);
				item.setInventory_year(year);
				item.setProduct(new Product(t.getProduct_id()));
				item.setOpening_balance(t.getKg_opening_balance());
				item.setImport_quantity(t.getKg_import_quantity());
				item.setExport_quantity(t.getKg_export_quantity());
				item.setExport_amount(t.getExport_total_amount());
				item.setClosing_balance(t.getKg_closing_balance());
				em.persist(item);
			}
			em.flush();
			res = 0;
		} catch (Exception e) {
			logger.error("InventoryService.saveOrUpdateInventory:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int infoInventory(int month, int year, InfoInventory infoInventory) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InfoInventory> cq = cb.createQuery(InfoInventory.class);
			Root<Inventory> root = cq.from(Inventory.class);
			cq.select(
					cb.construct(InfoInventory.class, root.get("inventory_month"), root.get("inventory_year"),
							cb.min(root.get("created_date"))))
					.where(cb.and(cb.equal(root.get("inventory_month"), month),
							cb.equal(root.get("inventory_year"), year)))
					.groupBy(root.get("inventory_month"), root.get("inventory_year"));
			TypedQuery<InfoInventory> query = em.createQuery(cq);
			List<InfoInventory> list = query.getResultList();
			if (list.size() > 0) {
				infoInventory.setInventory_year(year);
				infoInventory.setInventory_month(month);
				infoInventory.setCreated_date(list.get(0).getCreated_date());
				res = 0;
			}
		} catch (Exception e) {
			logger.error("InventoryService.infoInventory:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int getListInventory(String json, List<TonKhoThang> list) {
		int res = -1;
		try {
			/* {month:0, year:0, product_type_id:0,product_id:0,typep:-1} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hMonth = JsonParserUtil.getValueNumber(j, "month", null);
			HolderParser hYear = JsonParserUtil.getValueNumber(j, "year", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(j, "product_type_id", null);
			HolderParser hTypeId = JsonParserUtil.getValueNumber(j, "typep", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			ParameterExpression<Integer> pMonth = cb.parameter(Integer.class);
			ParameterExpression<Integer> pYear = cb.parameter(Integer.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			ParameterExpression<Long> pProductTypeId = cb.parameter(Long.class);
			ParameterExpression<Integer> pTypep = cb.parameter(Integer.class);
			CriteriaQuery<TonKhoThang> cq = cb.createQuery(TonKhoThang.class);
			Root<Inventory> root = cq.from(Inventory.class);
			Join<Inventory, Product> product_ = root.join("product", JoinType.INNER);
			Join<Product, ProductType> productType_ = product_.join("product_type", JoinType.INNER);
			List<Predicate> predicates = new ArrayList<Predicate>();
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.equal(pMonth, 0));
			dis1.getExpressions().add(cb.equal(root.get("inventory_month"), pMonth));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.equal(pYear, 0));
			dis2.getExpressions().add(cb.equal(root.get("inventory_year"), pYear));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pProductId, 0));
			dis3.getExpressions().add(cb.equal(root.get("product").get("id"), pProductId));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pProductTypeId, 0));
			dis4.getExpressions().add(cb.equal(productType_.get("id"), pProductTypeId));
			predicates.add(dis4);
			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.equal(pTypep, -1));
			dis5.getExpressions().add(cb.equal(product_.get("typep"), pTypep));
			predicates.add(dis5);
			cq.select(
					cb.construct(TonKhoThang.class, product_.get("id"), product_.get("product_code"),
							product_.get("product_name"), productType_.get("id"), productType_.get("code"),
							productType_.get("name"), root.get("opening_balance"),
							cb.diff(root.get("opening_balance"), product_.get("factor")), root.get("import_quantity"),
							cb.diff(root.get("import_quantity"), product_.get("factor")), root.get("export_quantity"),
							cb.diff(root.get("export_quantity"), product_.get("factor")), root.get("closing_balance"),
							root.get("export_amount"), product_.get("factor"))).where(
					cb.and(predicates.toArray(new Predicate[0])));
			TypedQuery<TonKhoThang> query = em.createQuery(cq);
			query.setParameter(pMonth, Integer.parseInt(Objects.toString(hMonth.getValue())));
			query.setParameter(pYear, Integer.parseInt(Objects.toString(hYear.getValue())));
			query.setParameter(pProductId, Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter(pProductTypeId, Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter(pTypep, Integer.parseInt(Objects.toString(hTypeId.getValue())));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("InventoryService.getListInventory:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TonKhoThang> tonkhothang(String json) {
		try {
			/* {month:0, year:0, product_type_id:0,product_id:0,typep:-1} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hMonth = JsonParserUtil.getValueNumber(j, "month", null);
			HolderParser hYear = JsonParserUtil.getValueNumber(j, "year", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(j, "product_type_id", null);
			HolderParser hTypeId = JsonParserUtil.getValueNumber(j, "typep", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			ParameterExpression<Integer> pMonth = cb.parameter(Integer.class);
			ParameterExpression<Integer> pYear = cb.parameter(Integer.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			ParameterExpression<Long> pProductTypeId = cb.parameter(Long.class);
			ParameterExpression<Integer> pTypep = cb.parameter(Integer.class);
			CriteriaQuery<TonKhoThang> cq = cb.createQuery(TonKhoThang.class);
			Root<Inventory> root = cq.from(Inventory.class);
			Join<Inventory, Product> product_ = root.join("product", JoinType.INNER);
			Join<Product, ProductType> productType_ = product_.join("product_type", JoinType.INNER);
			List<Predicate> predicates = new ArrayList<Predicate>();
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.equal(pMonth, 0));
			dis1.getExpressions().add(cb.equal(root.get("inventory_month"), pMonth));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.equal(pYear, 0));
			dis2.getExpressions().add(cb.equal(root.get("inventory_year"), pYear));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pProductId, 0));
			dis3.getExpressions().add(cb.equal(root.get("product").get("id"), pProductId));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pProductTypeId, 0));
			dis4.getExpressions().add(cb.equal(productType_.get("id"), pProductTypeId));
			predicates.add(dis4);
			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.equal(pTypep, -1));
			dis5.getExpressions().add(cb.equal(product_.get("typep"), pTypep));
			predicates.add(dis5);
			cq.select(
					cb.construct(TonKhoThang.class, product_.get("id"), product_.get("product_code"),
							product_.get("product_name"), productType_.get("id"), productType_.get("code"),
							productType_.get("name"), root.get("closing_balance"))).where(
					cb.and(predicates.toArray(new Predicate[0])));
			TypedQuery<TonKhoThang> query = em.createQuery(cq);
			query.setParameter(pMonth, Integer.parseInt(Objects.toString(hMonth.getValue())));
			query.setParameter(pYear, Integer.parseInt(Objects.toString(hYear.getValue())));
			query.setParameter(pProductId, Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter(pProductTypeId, Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter(pTypep, Integer.parseInt(Objects.toString(hTypeId.getValue())));
			return query.getResultList();
		} catch (Exception e) {
			logger.error("InventoryService.getListInventory:" + e.getMessage(), e);
		}
		return new ArrayList<TonKhoThang>();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public double getInventoryCurrentOfProduct(long productId) {
		try {
			int month = ToolTimeCustomer.getMonthCurrent();
			int year = ToolTimeCustomer.getYearCurrent();
			int monthPre = month - 1;
			int yearPre = year;
			if (month == 1) {
				monthPre = 12;
				yearPre = year - 1;
			}
			StringBuilder sql = new StringBuilder();
			sql.append("select sum(unit_opening_balance)+sum(unit_import_quantity)-sum(unit_export_quantity) as inve from ( ");
			sql.append("select iv.closing_balance as unit_opening_balance, 0 as unit_import_quantity, 0 as unit_export_quantity  from inventory as iv where iv.inventory_month=:m and iv.inventory_year=:y and iv.product_id=:p ");
			sql.append("union all ");
			sql.append("select 0,dtn.quantity, 0 from goodsreceiptnotedetail as dtn ");
			sql.append("inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id ");
			sql.append("where  dn.import_date >= :fd and dtn.product_id=:p ");
			sql.append("union all ");
			sql.append("select 0,0, dtx.quantity from invoicedetail as dtx ");
			sql.append("inner join invoice as dx on dtx.invoice_id=dx.id ");
			sql.append("where dx.invoice_date >= :fd and dtx.product_id=:p ) as t1 ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("m", monthPre);
			query.setParameter("y", yearPre);
			query.setParameter("p", productId);
			query.setParameter("fd", ToolTimeCustomer.convertDateToString(
					ToolTimeCustomer.getDateMinCustomer(month, year), "yyyy-MM-dd"));
			List<Object> list = query.getResultList();
			if (list.size() > 0) {
				return Double.parseDouble(Objects.toString(list.get(0), "0"));
			}
		} catch (Exception e) {
			logger.error("InventoryService.getInventoryCurrentOfProduct:" + e.getMessage(), e);
		}
		return 0;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Object[]> getListExpectedInventory2(String json, boolean bonoibo) {
		int res = -1;
		try {
			/* {cal_inv_date:'',exp_from_date:'',exp_to_date:''} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hCalInvDate = JsonParserUtil.getValueString(j, "cal_inv_date", null);
			HolderParser hExpFromDate = JsonParserUtil.getValueString(j, "exp_from_date", null);
			HolderParser hExpToDate = JsonParserUtil.getValueString(j, "exp_to_date", null);
			Date invDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hCalInvDate.getValue(), null),
					"dd/MM/yyyy");
			Date expFromDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hExpFromDate.getValue(), null),
					"dd/MM/yyyy");
			Date expToDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hExpToDate.getValue(), null),
					"dd/MM/yyyy");
			if (invDate != null && expFromDate != null && expToDate != null) {
				int month = ToolTimeCustomer.getMonthM(invDate);
				int year = ToolTimeCustomer.getYearM(invDate);
				if (month == 1) {
					month = 12;
					year = year - 1;
				} else {
					month = month - 1;
				}
				Date firstDateOfMonth = ToolTimeCustomer.getDateMinCustomer(ToolTimeCustomer.getMonthM(invDate),
						ToolTimeCustomer.getYearM(invDate));
				Date toDate30 = ToolTimeCustomer.minusOrAddDate(invDate, -1);
				Date fromDate30 = ToolTimeCustomer.minusOrAddDate(toDate30, -30);

				StringBuilder sql = new StringBuilder();
				sql.append("select t1.product_id,p.product_code,p.product_name, ");
				sql.append("sum(t1.opening_balance+(t1.import_quantity*p.factor)-(t1.invoice_quantity*p.factor)) as inv_quantity, ");
				sql.append("(sum(dhthung+dhthungxuat)*p.specification *p.factor)+(sum(dhkmdvt+dhkmdvtxuat) *p.factor), ");
				// sql.append("((sum(t1.dhthung)*p.specification)-(sum(dhthungxuat)*p.specification)+(dhkmdvt-dhkmdvtxuat))*p.factor as expected_export_quantity, ");
				sql.append("0 as expected_close_balance, ");
				sql.append("sum(t1.cal30_quantity *p.factor) as bq30_quantity, ");
				sql.append("sum(t1.ctinvoiced *p.factor) as ctinvoiced, ");
				sql.append("sum(t1.ctdetail *p.factor) as ctdetail, ");
				sql.append("(sum(dhthungxuat)*p.specification *p.factor) ");
				sql.append("from( ");
				sql.append("select ivn.product_id,");
				sql.append("ivn.closing_balance as opening_balance,");
				sql.append("0 as import_quantity,");
				sql.append("0 as invoice_quantity,");
				sql.append("0 as dhthung,");
				sql.append("0 as dhthungxuat,");
				sql.append("0 as dhkmdvt,");
				sql.append("0 as dhkmdvtxuat,");
				sql.append("0 as cal30_quantity,0 as ctinvoiced,0 as ctdetail  ");
				sql.append("from inventory as ivn where ivn.inventory_month=:m and ivn.inventory_year=:y ");

				sql.append("union all ");
				sql.append("select dtn.product_id,0,dtn.quantity,0,0,0,0,0,0,0,0 from goodsreceiptnotedetail as dtn ");
				sql.append("inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id ");
				sql.append("where  dn.import_date >=:invfd and dn.import_date <=:invtd ");

				sql.append("union all ");
				sql.append("select dtx.product_id,0,0,dtx.quantity,0,0,0,0,0,0,0 from invoicedetail as dtx  ");
				sql.append("inner join invoice as dx on dtx.invoice_id=dx.id  AND dx.ie_categories_id != :idmaxuatnhap  ");
				sql.append("where  dx.invoice_date >=:invfd and dx.invoice_date <=:invtd    ");

				// Xuat trogn 30 ngay
				sql.append("union all ");
				sql.append("select dt30.product_id,0,0,0,0,0,0,0,dt30.quantity,0,0 from invoicedetail as dt30 ");
				sql.append("inner join invoice as d30 on dt30.invoice_id=d30.id ");
				sql.append("where d30.delivery_date >=:d30f and d30.delivery_date <=:d30t ");

				// hop dong va phieu xuat xuat khau
				sql.append("union all ");
				sql.append("select x.product_id,0,0,0,0,0,0,0,0,x.quantity,0 from invoicedetail as x  ");
				sql.append("inner join invoice as ix on x.invoice_id =ix.id ");
				sql.append("where ix.contract_id in (select ct.id from contract as ct ");
				sql.append("where ct.effective_date >= :fd) ");
				sql.append("union all ");
				sql.append("select ctd.product_id,0,0,0,0,0,0,0,0,0,ctd.quantity from contractdetail as ctd  ");
				sql.append("inner join contract as ct on ctd.contract_id=ct.id ");
				sql.append("where ct.expiry_date >= :fd ");

				sql.append(") as t1 ");
				sql.append("inner join product as p on t1.product_id=p.id ");
				sql.append("group by t1.product_id ");
				sql.append("order by p.product_name ");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("m", month);
				query.setParameter("y", year);
				query.setParameter("invfd", ToolTimeCustomer.convertDateToString(firstDateOfMonth, "yyyy-MM-dd"));
				query.setParameter("invtd", ToolTimeCustomer.convertDateToString(invDate, "yyyy-MM-dd"));
				query.setParameter("fd", ToolTimeCustomer.convertDateToString(expFromDate, "yyyy-MM-dd"));
				query.setParameter("d30f", ToolTimeCustomer.convertDateToString(fromDate30, "yyyy-MM-dd"));
				query.setParameter("d30t", ToolTimeCustomer.convertDateToString(toDate30, "yyyy-MM-dd"));
				query.setParameter("idmaxuatnhap", 35);// ID: 35 là mã '0' (mã
														// nợ khuyến mãi)
				return query.getResultList();
			}
		} catch (Exception e) {
			logger.error("InventoryService.getListExpectedInventory:" + e.getMessage(), e);
		}
		return new ArrayList<Object[]>();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Object[]> getListExpectedOrder(String json, boolean bonoibo) {
		try {
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hCalInvDate = JsonParserUtil.getValueString(j, "cal_inv_date", null);
			HolderParser hExpFromDate = JsonParserUtil.getValueString(j, "exp_from_date", null);
			HolderParser hExpToDate = JsonParserUtil.getValueString(j, "exp_to_date", null);
			Date invDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hCalInvDate.getValue(), null),
					"dd/MM/yyyy");
			Date expFromDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hExpFromDate.getValue(), null),
					"dd/MM/yyyy");
			Date expToDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hExpToDate.getValue(), null),
					"dd/MM/yyyy");
			if (invDate != null && expFromDate != null && expToDate != null) {

				StringBuilder sql = new StringBuilder();
				sql.append("select t1.brand,t1.product_id,p.product_code,p.product_name, ");
				sql.append("(sum(dhthung+dhthungxuat)*p.specification *p.factor)+(sum(dhkmdvt+dhkmdvtxuat) *p.factor),0 as chuakt ");
				sql.append("from( ");

				/*
				 * idproduct, sldh chua giao, sldh chua giao xong, sldh chua
				 * giao (km), sldh chua giao xng (km)
				 */
				// don hang chua giao
				sql.append("select od.brand as brand,odd.product_id, odd.box_quantity as dhthung,0 as dhthungxuat,0 as dhkmdvt,0 as dhkmdvtxuat from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("LEFT join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join product as pdo on odd.product_id=pdo.id  ");
				sql.append("where odd.huyct =false AND od.delivery_date >=:fd and od.delivery_date <=:td  and od.delivered = false  ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");
				sql.append("union all  ");
				// don hang chua giao xong
				sql.append("select od.brand as brand,odd.product_id,0,odd.box_quantity-odd.box_quantity_actual,0,0 from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("LEFT join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join product as pdo on odd.product_id=pdo.id  ");
				sql.append("where odd.huyct =false AND od.delivery_date >=:fd and od.delivery_date <=:td  AND od.delivered = true AND odd.box_quantity_actual < odd.box_quantity  ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");
				sql.append("union all  ");

				// don hang chua giao KM
				sql.append("select od.brand as brand,oddp.product_id,0,0,oddp.quantity,0 from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("LEFT join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join promotionorderdetail as oddp on oddp.order_detail_id=odd.id ");
				sql.append("where  od.delivery_date >=:fd and od.delivery_date <=:td  and od.delivered = false  ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				sql.append("union all  ");

				// don hang chua giao xong KM
				sql.append("select od.brand as brand,oddp.product_id,0,0,0,oddp.quantityAct-oddp.quantityAct from orderdetail as odd  ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("LEFT join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join promotionorderdetail as oddp on oddp.order_detail_id=odd.id ");
				sql.append("where  od.delivery_date >=:fd and od.delivery_date <=:td  AND od.delivered = true AND oddp.quantityAct < oddp.quantity ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125

				sql.append(") as t1 ");
				sql.append("inner join product as p on t1.product_id=p.id ");
				sql.append("group by t1.brand, t1.product_id ");
				sql.append("order by p.product_name ");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("fd", ToolTimeCustomer.convertDateToString(expFromDate, "yyyy-MM-dd"));
				query.setParameter("td", ToolTimeCustomer.convertDateToString(expToDate, "yyyy-MM-dd"));

				return query.getResultList();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Object[]>();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Object[]> getListExpectedOrderKenhKH(String json, boolean bonoibo) {
		try {
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hCalInvDate = JsonParserUtil.getValueString(j, "cal_inv_date", null);
			HolderParser hExpFromDate = JsonParserUtil.getValueString(j, "exp_from_date", null);
			HolderParser hExpToDate = JsonParserUtil.getValueString(j, "exp_to_date", null);
			Date invDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hCalInvDate.getValue(), null),
					"dd/MM/yyyy");
			Date expFromDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hExpFromDate.getValue(), null),
					"dd/MM/yyyy");
			Date expToDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hExpToDate.getValue(), null),
					"dd/MM/yyyy");
			if (invDate != null && expFromDate != null && expToDate != null) {

				StringBuilder sql = new StringBuilder();
				sql.append("select t1.brand,t1.product_id,p.product_code,p.product_name, ");
				sql.append("(sum(dhthung+dhthungxuat)*p.specification *p.factor)+(sum(dhkmdvt+dhkmdvtxuat) *p.factor),0 as chuakt, kenhkh as kenhkh ");
				sql.append("from( ");

				/*
				 * idproduct, sldh chua giao, sldh chua giao xong, sldh chua
				 * giao (km), sldh chua giao xng (km)
				 */
				// don hang chua giao
				sql.append("select od.brand as brand,odd.product_id, odd.box_quantity as dhthung,0 as dhthungxuat,0 as dhkmdvt,0 as dhkmdvtxuat, ");
				sql.append("CASE WHEN cusch.id = 1 THEN 'GT' ELSE 'KHAC' END AS kenhkh  ");
				sql.append("from orderdetail as odd ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("LEFT join customer as cus on od.customer_id=cus.id  ");
				sql.append("LEFT join customertypes AS cust ON cus.customer_types_id = cust.id  ");
				sql.append("LEFT join customerchannel AS cusch ON cust.channal_id = cusch.id  ");
				sql.append("inner join product as pdo on odd.product_id=pdo.id  ");
				sql.append("where odd.huyct =false AND od.delivery_date >=:fd and od.delivery_date <=:td  and od.delivered = false  ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");
				sql.append("union all  ");
				// don hang chua giao xong
				sql.append("select od.brand as brand,odd.product_id,0,odd.box_quantity-odd.box_quantity_actual,0,0, ");
				sql.append("CASE WHEN cusch.id = 1 THEN 'GT' ELSE 'KHAC' END AS kenhkh  ");
				sql.append("from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("LEFT join customer as cus on od.customer_id=cus.id  ");
				sql.append("LEFT join customertypes AS cust ON cus.customer_types_id = cust.id  ");
				sql.append("LEFT join customerchannel AS cusch ON cust.channal_id = cusch.id  ");
				sql.append("inner join product as pdo on odd.product_id=pdo.id  ");
				sql.append("where odd.huyct =false AND od.delivery_date >=:fd and od.delivery_date <=:td  AND od.delivered = true AND odd.box_quantity_actual < odd.box_quantity  ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");
				sql.append("union all  ");

				// don hang chua giao KM
				sql.append("select od.brand as brand,oddp.product_id,0,0,oddp.quantity,0, ");
				sql.append("CASE WHEN cusch.id = 1 THEN 'GT' ELSE 'KHAC' END AS kenhkh  ");
				sql.append("from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("LEFT join customer as cus on od.customer_id=cus.id  ");
				sql.append("LEFT join customertypes AS cust ON cus.customer_types_id = cust.id  ");
				sql.append("LEFT join customerchannel AS cusch ON cust.channal_id = cusch.id  ");
				sql.append("inner join promotionorderdetail as oddp on oddp.order_detail_id=odd.id ");
				sql.append("where  od.delivery_date >=:fd and od.delivery_date <=:td  and od.delivered = false  ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				sql.append("union all  ");

				// don hang chua giao xong KM
				sql.append("select od.brand as brand,oddp.product_id,0,0,0,oddp.quantityAct-oddp.quantityAct, ");
				sql.append("CASE WHEN cusch.id = 1 THEN 'GT' ELSE 'KHAC' END AS kenhkh  ");
				sql.append("from orderdetail as odd  ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("LEFT join customer as cus on od.customer_id=cus.id  ");
				sql.append("LEFT join customertypes AS cust ON cus.customer_types_id = cust.id  ");
				sql.append("LEFT join customerchannel AS cusch ON cust.channal_id = cusch.id  ");
				sql.append("inner join promotionorderdetail as oddp on oddp.order_detail_id=odd.id ");
				sql.append("where  od.delivery_date >=:fd and od.delivery_date <=:td  AND od.delivered = true AND oddp.quantityAct < oddp.quantity ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125

				sql.append(") as t1 ");
				sql.append("inner join product as p on t1.product_id=p.id ");
				sql.append("group by t1.brand,t1.kenhkh, t1.product_id ");
				sql.append("order by p.product_name ");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("fd", ToolTimeCustomer.convertDateToString(expFromDate, "yyyy-MM-dd"));
				query.setParameter("td", ToolTimeCustomer.convertDateToString(expToDate, "yyyy-MM-dd"));

				return query.getResultList();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Object[]>();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int getListExpectedInventory(String json, List<Object[]> list, boolean bonoibo) {
		int res = -1;
		try {
			/* {cal_inv_date:'',exp_from_date:'',exp_to_date:''} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hCalInvDate = JsonParserUtil.getValueString(j, "cal_inv_date", null);
			HolderParser hExpFromDate = JsonParserUtil.getValueString(j, "exp_from_date", null);
			HolderParser hExpToDate = JsonParserUtil.getValueString(j, "exp_to_date", null);
			Date invDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hCalInvDate.getValue(), null),
					"dd/MM/yyyy");
			Date expFromDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hExpFromDate.getValue(), null),
					"dd/MM/yyyy");
			Date expToDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hExpToDate.getValue(), null),
					"dd/MM/yyyy");
			if (invDate != null && expFromDate != null && expToDate != null) {
				int month = ToolTimeCustomer.getMonthM(invDate);
				int year = ToolTimeCustomer.getYearM(invDate);
				if (month == 1) {
					month = 12;
					year = year - 1;
				} else {
					month = month - 1;
				}
				Date firstDateOfMonth = ToolTimeCustomer.getDateMinCustomer(ToolTimeCustomer.getMonthM(invDate),
						ToolTimeCustomer.getYearM(invDate));
				Date toDate30 = ToolTimeCustomer.minusOrAddDate(invDate, -1);
				Date fromDate30 = ToolTimeCustomer.minusOrAddDate(toDate30, -30);

				StringBuilder sql = new StringBuilder();
				sql.append("select t1.product_id,p.product_code,p.product_name, ");
				sql.append("sum(t1.opening_balance+(t1.import_quantity*p.factor)-(t1.invoice_quantity*p.factor)) as inv_quantity, ");
				sql.append("(sum(dhthung+dhthungxuat)*p.specification *p.factor)+(sum(dhkmdvt+dhkmdvtxuat) *p.factor), ");
				// sql.append("((sum(t1.dhthung)*p.specification)-(sum(dhthungxuat)*p.specification)+(dhkmdvt-dhkmdvtxuat))*p.factor as expected_export_quantity, ");
				sql.append("0 as expected_close_balance, ");
				sql.append("sum(t1.cal30_quantity *p.factor) as bq30_quantity, ");
				sql.append("sum(t1.ctinvoiced *p.factor) as ctinvoiced, ");
				sql.append("sum(t1.ctdetail *p.factor) as ctdetail, ");
				sql.append("(sum(dhthungxuat)*p.specification *p.factor) ");
				sql.append("from( ");
				sql.append("select ivn.product_id,");
				sql.append("ivn.closing_balance as opening_balance,");
				sql.append("0 as import_quantity,");
				sql.append("0 as invoice_quantity,");
				sql.append("0 as dhthung,");
				sql.append("0 as dhthungxuat,");
				sql.append("0 as dhkmdvt,");
				sql.append("0 as dhkmdvtxuat,");
				sql.append("0 as cal30_quantity,0 as ctinvoiced,0 as ctdetail  ");
				sql.append("from inventory as ivn where ivn.inventory_month=:m and ivn.inventory_year=:y ");

				sql.append("union all ");
				sql.append("select dtn.product_id,0,dtn.quantity,0,0,0,0,0,0,0,0 from goodsreceiptnotedetail as dtn ");
				sql.append("inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id ");
				sql.append("where  dn.import_date >=:invfd and dn.import_date <=:invtd ");

				sql.append("union all ");
				sql.append("select dtx.product_id,0,0,dtx.quantity,0,0,0,0,0,0,0 from invoicedetail as dtx  ");
				sql.append("inner join invoice as dx on dtx.invoice_id=dx.id  AND dx.ie_categories_id != :idmaxuatnhap  ");
				sql.append("where  dx.invoice_date >=:invfd and dx.invoice_date <=:invtd    ");

				// don hang chua giao
				sql.append("union all ");
				sql.append("select odd.product_id,0,0,0,odd.box_quantity,0,0,0,0,0,0 from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("LEFT join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join product as pdo on odd.product_id=pdo.id  ");
				sql.append("where odd.huyct =false AND od.delivery_date >=:fd and od.delivery_date <=:td  and od.delivered = false  ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");
				sql.append("union all  ");
				// don hang chua giao xong
				sql.append("select odd.product_id,0,0,0,0,odd.box_quantity-odd.box_quantity_actual,0,0,0,0,0 from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("LEFT join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join product as pdo on odd.product_id=pdo.id  ");
				sql.append("where odd.huyct =false AND od.delivery_date >=:fd and od.delivery_date <=:td  AND od.delivered = true AND odd.box_quantity_actual < odd.box_quantity  ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");
				sql.append("union all  ");

				// don hang chua giao KM
				sql.append("select oddp.product_id,0,0,0,0,0,oddp.quantity,0,0,0,0 from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("LEFT join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join promotionorderdetail as oddp on oddp.order_detail_id=odd.id ");
				sql.append("where  od.delivery_date >=:fd and od.delivery_date <=:td  and od.delivered = false  ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");
				// ");
				sql.append("union all  ");

				// don hang chua giao xong KM
				sql.append("select oddp.product_id,0,0,0,0,0,0,oddp.quantityAct-oddp.quantityAct,0,0,0 from orderdetail as odd  ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("LEFT join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join promotionorderdetail as oddp on oddp.order_detail_id=odd.id ");
				sql.append("where  od.delivery_date >=:fd and od.delivery_date <=:td  AND od.delivered = true AND oddp.quantityAct < oddp.quantity ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");

				// Xuat trogn 30 ngay
				sql.append("union all ");
				sql.append("select dt30.product_id,0,0,0,0,0,0,0,dt30.quantity,0,0 from invoicedetail as dt30 ");
				sql.append("inner join invoice as d30 on dt30.invoice_id=d30.id ");
				sql.append("where d30.delivery_date >=:d30f and d30.delivery_date <=:d30t ");

				// hop dong va phieu xuat xuat khau
				sql.append("union all ");
				sql.append("select x.product_id,0,0,0,0,0,0,0,0,x.quantity,0 from invoicedetail as x  ");
				sql.append("inner join invoice as ix on x.invoice_id =ix.id ");
				sql.append("where ix.contract_id in (select ct.id from contract as ct ");
				sql.append("where ct.effective_date >= :fd) ");
				sql.append("union all ");
				sql.append("select ctd.product_id,0,0,0,0,0,0,0,0,0,ctd.quantity from contractdetail as ctd  ");
				sql.append("inner join contract as ct on ctd.contract_id=ct.id ");
				sql.append("where ct.expiry_date >= :fd ");

				sql.append(") as t1 ");
				sql.append("inner join product as p on t1.product_id=p.id ");
				sql.append("group by t1.product_id ");
				sql.append("order by p.product_name ");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("m", month);
				query.setParameter("y", year);
				query.setParameter("invfd", ToolTimeCustomer.convertDateToString(firstDateOfMonth, "yyyy-MM-dd"));
				query.setParameter("invtd", ToolTimeCustomer.convertDateToString(invDate, "yyyy-MM-dd"));
				query.setParameter("fd", ToolTimeCustomer.convertDateToString(expFromDate, "yyyy-MM-dd"));
				query.setParameter("td", ToolTimeCustomer.convertDateToString(expToDate, "yyyy-MM-dd"));
				query.setParameter("d30f", ToolTimeCustomer.convertDateToString(fromDate30, "yyyy-MM-dd"));
				query.setParameter("d30t", ToolTimeCustomer.convertDateToString(toDate30, "yyyy-MM-dd"));
				query.setParameter("idmaxuatnhap", 35);// ID: 35 là mã '0' (mã
														// nợ khuyến mãi)
				list.addAll(query.getResultList());
				res = 0;
			}
		} catch (Exception e) {
			logger.error("InventoryService.getListExpectedInventory:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int getSPChuaGiao(String json, List<Object[]> list) {
		int res = -1;
		try {
			/* {cal_inv_date:'',exp_from_date:'',exp_to_date:''} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hExpFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hExpToDate = JsonParserUtil.getValueString(j, "to_date", null);

			StringBuilder sql = new StringBuilder();
			sql.append("select t1.product_id,p.product_code,p.product_name, ");
			sql.append("sum(t1.dhthung), ");
			sql.append("sum(t1.dhthungdaxuat), ");
			sql.append("sum(t1.dhkmdvt)*p.specification, ");
			sql.append("sum(t1.dhkmdvtdaxuat)*p.specification, ");
			sql.append("t1.idkh, ");
			sql.append("t1.makh, ");
			sql.append("t1.tenkh, ");
			sql.append("GROUP_CONCAT(t1.po SEPARATOR ';') ");
			sql.append("from( ");

			// don hang chua giao
			sql.append("select odd.product_id,odd.box_quantity as dhthung,0 as dhthungdaxuat,0 as dhkmdvt,0 as dhkmdvtdaxuat ,cus.id as idkh,cus.customer_code as makh, cus.customer_name as tenkh, od.po_no as po from orderdetail as odd   ");
			sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
			sql.append("LEFT JOIN Customer as cus on cus.id=od.customer_id  ");
			sql.append("inner join product as pdo on odd.product_id=pdo.id  ");
			sql.append("where odd.huyct =false AND od.order_date >=:fd and od.order_date <=:td  and od.delivered = false  ");

			sql.append("union all  ");
			// don hang chua giao xong
			sql.append("select odd.product_id,odd.box_quantity,odd.box_quantity_actual,0,0,cus.id ,cus.customer_code , cus.customer_name, od.po_no from orderdetail as odd   ");
			sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
			sql.append("LEFT JOIN Customer as cus on cus.id=od.customer_id  ");
			sql.append("inner join product as pdo on odd.product_id=pdo.id  ");
			sql.append("where odd.huyct =false AND od.order_date >=:fd and od.order_date <=:td  AND od.delivered = true   ");

			sql.append(") as t1 ");
			sql.append("inner join product as p on t1.product_id=p.id ");
			sql.append("group by t1.idkh, t1.product_id ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", Objects.toString(hExpFromDate.getValue()));
			query.setParameter("td", Objects.toString(hExpToDate.getValue()));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("InventoryService.getListExpectedInventory:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int getListExpectedInventoryDetail(String json, List<Object[]> list, boolean bonoibo) {
		int res = -1;
		try {
			/* {cal_inv_date:'',exp_from_date:'',exp_to_date:''} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hExpFromDate = JsonParserUtil.getValueString(j, "exp_from_date", null);
			HolderParser hExpToDate = JsonParserUtil.getValueString(j, "exp_to_date", null);
			HolderParser hProduct = JsonParserUtil.getValueString(j, "product_id", null);

			Date expFromDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hExpFromDate.getValue(), null),
					"dd/MM/yyyy");
			Date expToDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hExpToDate.getValue(), null),
					"dd/MM/yyyy");
			Long productid = Long.parseLong(Objects.toString(hProduct.getValue(), "0"));
			if (productid != 0 && expFromDate != null && expToDate != null) {

				StringBuilder sql = new StringBuilder();
				sql.append("select p.id,p.product_code,p.product_name, ");
				sql.append("t1.voucher_code, ");
				sql.append("t1.order_date, ");
				sql.append("sum(t1.box_quantity) as soluongdh_thung, ");
				sql.append("sum(t1.box_quantity_actual)  as soluongdhxuat_thung, ");
				sql.append("sum(t1.quantitykm) as soluongkm_dvt, ");
				sql.append("sum(t1.quantitykm_Act) as soluongkmxuat_dvt, ");
				sql.append("p.specification, ");
				sql.append("p.factor, ");
				sql.append("t1.tenkhachhang, ");
				sql.append("t1.ngaydh ");

				sql.append("from( ");

				// don hang chua giao (ma don hang, ngay, sldon hang(thung), da
				// xuat(thung), slkhuyen mai, da xuat km, con lai)
				sql.append("select odd.product_id as product_id, od.voucher_code as voucher_code,od.delivery_date as order_date,odd.box_quantity as box_quantity,0 as box_quantity_actual,0 as quantitykm,0 as quantitykm_Act, cus.customer_name as tenkhachhang,od.orderDateCreate as ngaydh from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("left join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join product as pdo on odd.product_id=pdo.id  ");
				sql.append("where odd.huyct =false AND od.delivery_date >=:fd and od.delivery_date <=:td  and od.delivered = false ");
				sql.append("AND odd.product_id = :pd ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");
				sql.append("union all  ");
				// don hang chua giao xong
				sql.append("select odd.product_id as product_id,od.voucher_code,od.delivery_date,odd.box_quantity,odd.box_quantity_actual,0,0, cus.customer_name,od.orderDateCreate from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("left join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join product as pdo on odd.product_id=pdo.id  ");
				sql.append("where odd.huyct =false AND  od.delivery_date >=:fd and od.delivery_date <=:td   AND od.delivered = true AND odd.box_quantity_actual < odd.box_quantity  ");
				sql.append("AND odd.product_id = :pd ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");
				sql.append("union all  ");

				// don hang chua giao KM
				sql.append("select odd.product_id as product_id,od.voucher_code,od.delivery_date,0,0,oddp.quantity,0, cus.customer_name,od.orderDateCreate from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("left join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join promotionorderdetail as oddp on oddp.order_detail_id=odd.id ");
				sql.append("where  od.delivery_date >=:fd and od.delivery_date <=:td  and od.delivered = false  ");
				sql.append("AND oddp.product_id = :pd ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");
				sql.append("union all  ");

				// don hang chua giao xong KM
				sql.append("select odd.product_id as product_id,od.voucher_code,od.delivery_date,0,0,0,oddp.quantityAct, cus.customer_name,od.orderDateCreate from orderdetail as odd  ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("left join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join promotionorderdetail as oddp on oddp.order_detail_id=odd.id ");
				sql.append("where  od.delivery_date >=:fd and od.delivery_date <=:td  AND od.delivered = true AND oddp.quantityAct < oddp.quantity ");
				sql.append("AND oddp.product_id = :pd ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");

				sql.append(") as t1 ");
				sql.append("inner join product as p on t1.product_id=p.id ");
				sql.append("group by t1.voucher_code, t1.order_date, t1.product_id ");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("fd", ToolTimeCustomer.convertDateToString(expFromDate, "yyyy-MM-dd"));
				query.setParameter("td", ToolTimeCustomer.convertDateToString(expToDate, "yyyy-MM-dd"));
				query.setParameter("pd", productid);
				list.addAll(query.getResultList());
				res = 0;
			}
		} catch (Exception e) {
			logger.error("InventoryService.getListExpectedInventory:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int getListExpectedInventoryDetail2(String json, List<Object[]> list, boolean bonoibo) {
		int res = -1;
		try {
			/* {cal_inv_date:'',exp_from_date:'',exp_to_date:''} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hExpFromDate = JsonParserUtil.getValueString(j, "exp_from_date", null);
			HolderParser hExpToDate = JsonParserUtil.getValueString(j, "exp_to_date", null);
			HolderParser hProduct = JsonParserUtil.getValueString(j, "product_code", null);
			HolderParser hChinhanh = JsonParserUtil.getValueString(j, "chinhanh", null);

			Date expFromDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hExpFromDate.getValue(), null),
					"dd/MM/yyyy");
			Date expToDate = ToolTimeCustomer.convertStringToDate(Objects.toString(hExpToDate.getValue(), null),
					"dd/MM/yyyy");
			String productcode = Objects.toString(hProduct.getValue(), "");
			// chinhanh: HO CHI MINH, BINH DUONG, BAC NINH
			String chinhanh = Objects.toString(hChinhanh.getValue(), "");
			if (!"".equals(productcode) && expFromDate != null && expToDate != null && !"".equals(chinhanh)) {
				StringBuilder sql = new StringBuilder();
				sql.append("select p.id,p.product_code,p.product_name, ");
				sql.append("t1.voucher_code, ");
				sql.append("t1.order_date, ");
				sql.append("sum(t1.box_quantity) as soluongdh_thung, ");
				sql.append("sum(t1.box_quantity_actual)  as soluongdhxuat_thung, ");
				sql.append("sum(t1.quantitykm) as soluongkm_dvt, ");
				sql.append("sum(t1.quantitykm_Act) as soluongkmxuat_dvt, ");
				sql.append("p.specification, ");
				sql.append("p.factor, ");
				sql.append("t1.tenkhachhang, ");
				sql.append("t1.ngaydh, ");
				sql.append("t1.brand ");
				sql.append("from( ");

				// don hang chua giao (ma don hang, ngay, sldon hang(thung), da
				// xuat(thung), slkhuyen mai, da xuat km, con lai)
				sql.append("select odd.product_id as product_id, od.voucher_code as voucher_code,od.delivery_date as order_date,odd.box_quantity as box_quantity,0 as box_quantity_actual,0 as quantitykm,0 as quantitykm_Act, cus.customer_name as tenkhachhang,od.orderDateCreate as ngaydh, od.brand as brand from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("left join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join product as pdo on odd.product_id=pdo.id  ");
				sql.append("where odd.huyct =false AND od.delivery_date >=:fd and od.delivery_date <=:td  and od.delivered = false ");
				sql.append("AND od.brand = :chinhanh ");
				sql.append("AND pdo.product_code = :pd ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");
				sql.append("union all  ");
				// don hang chua giao xong
				sql.append("select odd.product_id as product_id,od.voucher_code,od.delivery_date,odd.box_quantity,odd.box_quantity_actual,0,0, cus.customer_name,od.orderDateCreate, od.brand from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("left join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join product as pdo on odd.product_id=pdo.id  ");
				sql.append("where odd.huyct =false AND  od.delivery_date >=:fd and od.delivery_date <=:td   AND od.delivered = true AND odd.box_quantity_actual < odd.box_quantity  ");
				sql.append("AND od.brand = :chinhanh ");
				sql.append("AND pdo.product_code = :pd ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");
				sql.append("union all  ");

				// don hang chua giao KM
				sql.append("select odd.product_id as product_id,od.voucher_code,od.delivery_date,0,0,oddp.quantity,0, cus.customer_name,od.orderDateCreate, od.brand from orderdetail as odd   ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("left join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join promotionorderdetail as oddp on oddp.order_detail_id=odd.id ");
				sql.append("inner join product as pdo on oddp.product_id=pdo.id  ");
				sql.append("where  od.delivery_date >=:fd and od.delivery_date <=:td  and od.delivered = false  ");
				sql.append("AND od.brand = :chinhanh ");
				sql.append("AND pdo.product_code = :pd ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");
				sql.append("union all  ");

				// don hang chua giao xong KM
				sql.append("select odd.product_id as product_id,od.voucher_code,od.delivery_date,0,0,0,oddp.quantityAct, cus.customer_name,od.orderDateCreate, od.brand from orderdetail as odd  ");
				sql.append("inner join orderlix as od on odd.order_lix_id=od.id  ");
				sql.append("left join customer as cus on od.customer_id=cus.id  ");
				sql.append("inner join promotionorderdetail as oddp on oddp.order_detail_id=odd.id ");
				sql.append("inner join product as pdo on oddp.product_id=pdo.id  ");
				sql.append("where  od.delivery_date >=:fd and od.delivery_date <=:td  AND od.delivered = true AND oddp.quantityAct < oddp.quantity ");
				sql.append("AND od.brand = :chinhanh ");
				sql.append("AND pdo.product_code = :pd ");
				if (bonoibo)
					sql.append("AND cus.customer_code not in ('CO400','BL146','CO125') ");// CO400,BL146,CO125
				// ");

				sql.append(") as t1 ");
				sql.append("inner join product as p on t1.product_id=p.id ");
				sql.append("group by t1.voucher_code, t1.order_date, t1.product_id ");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("fd", ToolTimeCustomer.convertDateToString(expFromDate, "yyyy-MM-dd"));
				query.setParameter("td", ToolTimeCustomer.convertDateToString(expToDate, "yyyy-MM-dd"));
				query.setParameter("pd", productcode);
				query.setParameter("chinhanh", chinhanh);
				list.addAll(query.getResultList());
				res = 0;
			}
		} catch (Exception e) {
			logger.error("InventoryService.getListExpectedInventory:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public Inventory selectByIdProduct(long id, int month, int year) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Inventory> cq = cb.createQuery(Inventory.class);
			Root<Inventory> root = cq.from(Inventory.class);
			cq.select(root).where(cb.equal(root.get("product").get("id"), id),
					cb.equal(root.get("inventory_month"), month), cb.equal(root.get("inventory_year"), year));
			TypedQuery<Inventory> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
			// logger.error("ProductService.selectByCode:"+e.getMessage(),e);
		}
	}

	@Override
	public Inventory create(Inventory entity) {
		em.persist(entity);
		return em.merge(entity);
	}

	@Override
	public Inventory update(Inventory account) {
		return em.merge(account);

	}
}
