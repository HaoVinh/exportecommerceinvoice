package lixco.com.report_service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.commom_ejb.MyMath;
import lixco.com.commom_ejb.MyUtilEJB;
import lixco.com.commom_ejb.ToolTimeCustomer;
import lixco.com.entity.Customer;
import lixco.com.entity.IECategories;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.Product;
import lixco.com.entity.ProductKM;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.BangKeChiTietHoaDon;
import lixco.com.reportInfo.BangKeHoaDon;
import lixco.com.reportInfo.BangKeKhoiLuongVanChuyen;
import lixco.com.reportInfo.BangKeKhoiLuongVanChuyenTheoKhachHang;
import lixco.com.reportInfo.BangKeKhoiLuongVanChuyenTheoNVVC;
import lixco.com.reportInfo.BangKeSanPhamChuaGiao;
import lixco.com.reportInfo.BaoCaoHopGiaoBan;
import lixco.com.reportInfo.BaoCaoLever;
import lixco.com.reportInfo.ChiTietCongNo;
import lixco.com.reportInfo.CongNo;
import lixco.com.reportInfo.DoanhThuKhachHangTheoThang;
import lixco.com.reportInfo.LuyKeNhapXuat;
import lixco.com.reportInfo.NhapXuatNgay;
import lixco.com.reportInfo.SoLieuBaoCaoTongHop;
import lixco.com.reportInfo.SoLieuBaoSLBH;
import lixco.com.reportInfo.TheKhoSanPham;
import lixco.com.reportInfo.TheoDoiDoanhThuKhachHang;
import lixco.com.reportInfo.ThongKeSanLuongDoanhThu;
import lixco.com.reportInfo.ThongKeTheoThang;
import lixco.com.reportInfo.TonKhoDauThang;
import lixco.com.reportInfo.TonKhoSanPham;
import lixco.com.reportInfo.TonKhoThang;
import lixco.com.reportInfo.TongKetTieuThuKhachHang;
import lixco.com.reportInfo.TongKetXuatSanPhamTheoQuocGia;
import lixco.com.reportInfo.TongKetXuatTheoBrandName;
import lixco.com.reportInfo.TongKetXuatTheoKhachHang;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai1;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai2;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai3;
import lixco.com.reportInfo.TongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel;
import lixco.com.reportInfo.TongKetXuatTheoThanhPhoTongKetTieuThu;
import lixco.com.service.IECategoriesService;
import lixco.com.service.ProductKMService;
import lixco.com.service.ProductService;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class ReportService implements IReportService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int reportTongKetCongNoKhachHang(String json, List<TongKetXuatTheoKhachHang> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hContractId = JsonParserUtil.getValueNumber(data, "contract_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			HolderParser hTypeP = JsonParserUtil.getValueNumber(data, "typep", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TongKetXuatTheoKhachHang> cq = cb.createQuery(TongKetXuatTheoKhachHang.class);
			Root<InvoiceDetail> root = cq.from(InvoiceDetail.class);
			Join<InvoiceDetail, Invoice> invoice_ = root.join("invoice", JoinType.INNER);
			Join<Invoice, Customer> customer_ = invoice_.join("customer", JoinType.INNER);
			Join<InvoiceDetail, Product> product_ = root.join("product", JoinType.INNER);
			Join<Product, ProductType> productType_ = product_.join("product_type", JoinType.INNER);
			ParameterExpression<Date> pFromDate = cb.parameter(Date.class);
			ParameterExpression<Date> pToDate = cb.parameter(Date.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			ParameterExpression<Long> pProductTypeId = cb.parameter(Long.class);
			ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
			ParameterExpression<Long> pContractId = cb.parameter(Long.class);
			ParameterExpression<Long> pIECategoriesId = cb.parameter(Long.class);
			ParameterExpression<Integer> pTypep = cb.parameter(Integer.class);
			List<Predicate> predicates = new ArrayList<Predicate>();
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.isNull(pFromDate));
			dis1.getExpressions().add(cb.greaterThanOrEqualTo(invoice_.get("invoice_date"), pFromDate));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.isNull(pToDate));
			dis2.getExpressions().add(cb.lessThanOrEqualTo(invoice_.get("invoice_date"), pToDate));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pProductId, 0));
			dis3.getExpressions().add(cb.equal(product_.get("id"), pProductId));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pProductTypeId, 0));
			dis4.getExpressions().add(cb.equal(product_.get("product_type").get("id"), pProductTypeId));
			predicates.add(dis4);
			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.equal(pCustomerId, 0));
			dis5.getExpressions().add(cb.equal(customer_.get("id"), pCustomerId));
			predicates.add(dis5);
			Predicate dis6 = cb.disjunction();
			dis6.getExpressions().add(cb.equal(pContractId, 0));
			dis6.getExpressions().add(cb.equal(invoice_.get("contract").get("id"), pContractId));
			predicates.add(dis6);
			Predicate dis7 = cb.disjunction();
			dis7.getExpressions().add(cb.equal(pIECategoriesId, 0));
			dis7.getExpressions().add(cb.equal(invoice_.get("ie_categories").get("id"), pIECategoriesId));
			predicates.add(dis7);
			Predicate dis8 = cb.disjunction();
			dis8.getExpressions().add(cb.equal(pTypep, -1));
			dis8.getExpressions().add(cb.equal(product_.get("typep"), pTypep));
			predicates.add(dis8);
			// (long customer_id, String customer_name, long product_id, String
			// product_name,double quantity, double box_quantity)
			cq.select(
					cb.construct(
							TongKetXuatTheoKhachHang.class,
							customer_.get("id"),
							customer_.get("customer_name"),
							product_.get("id"),
							product_.get("product_name"),
							productType_.get("id"),
							productType_.get("name"),
							cb.sum(cb.prod(root.get("quantity"), product_.get("factor"))),
							cb.sum(cb.quot(root.get("quantity"), product_.get("specification"))),
							cb.sum(cb.prod(root.get("quantity"), root.get("unit_price"))),
							cb.sum(cb.prod(cb.prod(root.get("quantity"), root.get("unit_price")),
									invoice_.get("tax_value"))),
							cb.quot(cb.sum(invoice_.get("tax_value")), cb.count(root.get("id")))))
					.where(cb.and(predicates.toArray(new Predicate[0])))
					.groupBy(customer_.get("id"), customer_.get("customer_name"), product_.get("id"),
							product_.get("product_name"), productType_.get("id"), productType_.get("name"))
					.orderBy(cb.asc(customer_.get("customer_name")), cb.asc(product_.get("product_name")),
							cb.asc(productType_.get("name")));
			TypedQuery<TongKetXuatTheoKhachHang> query = em.createQuery(cq);
			query.setParameter(pFromDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pToDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pProductId, Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter(pProductTypeId, Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter(pContractId, Long.parseLong(Objects.toString(hContractId.getValue())));
			query.setParameter(pIECategoriesId, Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter(pTypep, Integer.parseInt(Objects.toString(hTypeP.getValue())));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetCongNoKhachHang:" + e.getMessage(), e);
		}
		return res;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int reportTongKetTieuThuKhachHang(String json, List<TongKetTieuThuKhachHang> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hContractId = JsonParserUtil.getValueNumber(data, "contract_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			HolderParser hTypeP = JsonParserUtil.getValueNumber(data, "typep", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.customer_id,c1.customer_name,pt1.id,pt1.name,t1.product_id,p1.product_name,t1.quantity*p1.factor as kg_quantity,t1.quantity/p1.specification as box_quantity,t1.unit_price, ");
			sql.append("t1.total_amount,t1.tax_value from customer as c1 ");
			sql.append("inner join( ");
			sql.append("select d.customer_id,dt.product_id,dt.unit_price,sum(dt.quantity) as quantity, sum(dt.total) as total_amount, ");
			sql.append("sum(d.tax_value)/count(dt.id) as tax_value from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p on dt.product_id=p.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id=:p) and (:pt=0 or p.product_type_id =:pt) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ct=0 or d.contract_id=:ct) and (:ie=0 or d.ie_categories_id=:ie) and (:tp=-1 or p.typep=:tp) ");
			sql.append("group by d.customer_id,dt.product_id,dt.unit_price) as t1 on t1.customer_id=c1.id ");
			sql.append("inner join product as p1 on t1.product_id=p1.id ");
			sql.append("inner join producttype as pt1 on p1.product_type_id=pt1.id ");
			sql.append("order by c1.id,pt1.id ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hContractId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter("tp", Integer.parseInt(Objects.toString(hTypeP.getValue(), "-1")));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				TongKetTieuThuKhachHang item = new TongKetTieuThuKhachHang();
				item.setCustomer_id(Long.parseLong(Objects.toString(p[0])));
				item.setCustomer_name(Objects.toString(p[1]));
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[2])));
				item.setProduct_type_name(Objects.toString(p[3]));
				item.setProduct_id(Long.parseLong(Objects.toString(p[4])));
				item.setProduct_name(Objects.toString(p[5]));
				item.setQuantity(Double.parseDouble(Objects.toString(p[6], "0")));
				item.setBox_quantity(Double.parseDouble(Objects.toString(p[7], "0")));
				item.setUnit_price(Double.parseDouble(Objects.toString(p[8], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[9], "0")));
				item.setTax_value(Double.parseDouble(Objects.toString(p[10], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetTieuThuKhachHangTheoThang:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportTongKetTieuThuKhachHangDVT(String json, List<TongKetTieuThuKhachHang> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hContractId = JsonParserUtil.getValueNumber(data, "contract_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			HolderParser hTypeP = JsonParserUtil.getValueNumber(data, "typep", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.customer_id,c1.customer_name,pt1.id,pt1.name,t1.product_id,p1.product_name,t1.quantity,t1.quantity/p1.specification as box_quantity,t1.unit_price, ");
			sql.append("t1.total_amount,t1.tax_value from customer as c1 ");
			sql.append("inner join( ");
			sql.append("select d.customer_id,dt.product_id,dt.unit_price,sum(dt.quantity) as quantity, sum(dt.total) as total_amount, ");
			sql.append("sum(d.tax_value)/count(dt.id) as tax_value from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p on dt.product_id=p.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id=:p) and (:pt=0 or p.product_type_id =:pt) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ct=0 or d.contract_id=:ct) and (:ie=0 or d.ie_categories_id=:ie) and (:tp=-1 or p.typep=:tp) ");
			sql.append("group by d.customer_id,dt.product_id,dt.unit_price) as t1 on t1.customer_id=c1.id ");
			sql.append("inner join product as p1 on t1.product_id=p1.id ");
			sql.append("inner join producttype as pt1 on p1.product_type_id=pt1.id ");
			sql.append("order by c1.id,pt1.id ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hContractId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter("tp", Integer.parseInt(Objects.toString(hTypeP.getValue(), "-1")));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				TongKetTieuThuKhachHang item = new TongKetTieuThuKhachHang();
				item.setCustomer_id(Long.parseLong(Objects.toString(p[0])));
				item.setCustomer_name(Objects.toString(p[1]));
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[2])));
				item.setProduct_type_name(Objects.toString(p[3]));
				item.setProduct_id(Long.parseLong(Objects.toString(p[4])));
				item.setProduct_name(Objects.toString(p[5]));
				item.setQuantity(Double.parseDouble(Objects.toString(p[6], "0")));
				item.setBox_quantity(Double.parseDouble(Objects.toString(p[7], "0")));
				item.setUnit_price(Double.parseDouble(Objects.toString(p[8], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[9], "0")));
				item.setTax_value(Double.parseDouble(Objects.toString(p[10], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetTieuThuKhachHangDVT:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportTheoDoiDoanhThuKhachHang(String json, List<TheoDoiDoanhThuKhachHang> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hContractId = JsonParserUtil.getValueNumber(data, "contract_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			HolderParser hTypeP = JsonParserUtil.getValueNumber(data, "typep", null);
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TheoDoiDoanhThuKhachHang> cq = cb.createQuery(TheoDoiDoanhThuKhachHang.class);
			Root<InvoiceDetail> root = cq.from(InvoiceDetail.class);
			Join<InvoiceDetail, Invoice> invoice_ = root.join("invoice", JoinType.INNER);
			Join<Invoice, Customer> customer_ = invoice_.join("customer", JoinType.INNER);
			Join<InvoiceDetail, Product> product_ = root.join("product", JoinType.INNER);
			ParameterExpression<Date> pFromDate = cb.parameter(Date.class);
			ParameterExpression<Date> pToDate = cb.parameter(Date.class);
			ParameterExpression<Long> pProductId = cb.parameter(Long.class);
			ParameterExpression<Long> pProductTypeId = cb.parameter(Long.class);
			ParameterExpression<Long> pCustomerId = cb.parameter(Long.class);
			ParameterExpression<Long> pContractId = cb.parameter(Long.class);
			ParameterExpression<Long> pIECategoriesId = cb.parameter(Long.class);
			ParameterExpression<Integer> pTypep = cb.parameter(Integer.class);
			List<Predicate> predicates = new ArrayList<Predicate>();
			Predicate dis1 = cb.disjunction();
			dis1.getExpressions().add(cb.isNull(pFromDate));
			dis1.getExpressions().add(cb.greaterThanOrEqualTo(invoice_.get("invoice_date"), pFromDate));
			predicates.add(dis1);
			Predicate dis2 = cb.disjunction();
			dis2.getExpressions().add(cb.isNull(pToDate));
			dis2.getExpressions().add(cb.lessThanOrEqualTo(invoice_.get("invoice_date"), pToDate));
			predicates.add(dis2);
			Predicate dis3 = cb.disjunction();
			dis3.getExpressions().add(cb.equal(pProductId, 0));
			dis3.getExpressions().add(cb.equal(product_.get("id"), pProductId));
			predicates.add(dis3);
			Predicate dis4 = cb.disjunction();
			dis4.getExpressions().add(cb.equal(pProductTypeId, 0));
			dis4.getExpressions().add(cb.equal(product_.get("product_type").get("id"), pProductTypeId));
			predicates.add(dis4);
			Predicate dis5 = cb.disjunction();
			dis5.getExpressions().add(cb.equal(pCustomerId, 0));
			dis5.getExpressions().add(cb.equal(customer_.get("id"), pCustomerId));
			predicates.add(dis5);
			Predicate dis6 = cb.disjunction();
			dis6.getExpressions().add(cb.equal(pContractId, 0));
			dis6.getExpressions().add(cb.equal(invoice_.get("contract").get("id"), pContractId));
			predicates.add(dis6);
			Predicate dis7 = cb.disjunction();
			dis7.getExpressions().add(cb.equal(pIECategoriesId, 0));
			dis7.getExpressions().add(cb.equal(invoice_.get("ie_categories").get("id"), pIECategoriesId));
			predicates.add(dis7);
			Predicate dis8 = cb.disjunction();
			dis8.getExpressions().add(cb.equal(pTypep, -1));
			dis8.getExpressions().add(cb.equal(product_.get("typep"), pTypep));
			predicates.add(dis8);
			// (long customer_id, String customer_name, double quantity, double
			// box_quantity,double total_amount)
			cq.select(
					cb.construct(TheoDoiDoanhThuKhachHang.class, customer_.get("id"), customer_.get("customer_code"),
							customer_.get("customer_name"), cb.sum(root.get("quantity")),
							cb.sum(cb.quot(root.get("quantity"), product_.get("specification"))),
							cb.sum(cb.prod(root.get("quantity"), root.get("unit_price")))))
					.where(cb.and(predicates.toArray(new Predicate[0])))
					.groupBy(customer_.get("id"), customer_.get("customer_code"), customer_.get("customer_name"));
			TypedQuery<TheoDoiDoanhThuKhachHang> query = em.createQuery(cq);
			query.setParameter(pFromDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pToDate,
					ToolTimeCustomer.convertStringToDate(Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy"));
			query.setParameter(pProductId, Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter(pProductTypeId, Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter(pCustomerId, Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter(pContractId, Long.parseLong(Objects.toString(hContractId.getValue())));
			query.setParameter(pIECategoriesId, Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter(pTypep, Integer.parseInt(Objects.toString(hTypeP.getValue())));
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTheoDoiDoanhThuKhachHang:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1(String json,
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai1> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hContractId = JsonParserUtil.getValueNumber(data, "contract_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			HolderParser hTypeP = JsonParserUtil.getValueNumber(data, "typep", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select p1.product_type_id,pt1.name,t1.product_id,p1.product_name,t1.quantity,t1.total_amount,t1.total_tax_amount,t1.total_amount+t1.total_tax_amount as total_amount_with_vat ");
			sql.append("from( select dt.product_id,sum(dt.quantity*p.factor) as quantity,sum(dt.total) as total_amount, ");
			sql.append("sum(dt.total*d.tax_value) as total_tax_amount ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p  on dt.product_id=p.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id=:p) and (:pt=0 or p.product_type_id =:pt) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ct=0 or d.contract_id=:ct) and (:ie=0 or d.ie_categories_id=:ie) and (:tp=-1 or p.typep=:tp) ");
			sql.append("group by dt.product_id) as t1  ");
			sql.append("inner join product as p1 on t1.product_id=p1.id ");
			sql.append("inner join producttype as pt1 on p1.product_type_id=pt1.id ");
			sql.append("order by pt1.name,p1.product_name ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hContractId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter("tp", Integer.parseInt(Objects.toString(hTypeP.getValue(), "-1")));
			List<Object[]> listResult = query.getResultList();
			// (long product_type_id, String product_type_name, long
			// product_id,String product_name, Double quantity, Double
			// box_quantity, Double total_amount, Double total_tax_amount,Double
			// total_amount_with_vat)
			for (Object[] p : listResult) {
				TongKetXuatTheoSanPhamTongKetTieuThuLoai1 item = new TongKetXuatTheoSanPhamTongKetTieuThuLoai1();
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[0])));
				item.setProduct_type_name(Objects.toString(p[1], null));
				item.setProduct_id(Long.parseLong(Objects.toString(p[2])));
				item.setProduct_name(Objects.toString(p[3]));
				item.setQuantity(MyMath.roundCustom(Double.parseDouble(Objects.toString(p[4], "0")), 2));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[5], "0")));
				item.setTotal_tax_amount((double) MyMath.round(Double.parseDouble(Objects.toString(p[6], "0"))));
				item.setTotal_amount_with_vat(Double.parseDouble(Objects.toString(p[7], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2(String json,
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai2> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hContractId = JsonParserUtil.getValueNumber(data, "contract_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			HolderParser hTypeP = JsonParserUtil.getValueNumber(data, "typep", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.product_type_id,pt1.name,t1.product_com_id,pc1.pcom_name,t1.ie_categories_id,ie1.content, ");
			sql.append("t1.quantity,t1.total_amount,t1.total_tax_amount ");
			sql.append("from(select p.product_type_id,p.product_com_id,d.ie_categories_id, sum(dt.quantity*p.factor) as quantity,sum(dt.total) as total_amount, ");
			sql.append("sum(dt.total*d.tax_value) as total_tax_amount ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p  on dt.product_id=p.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id=:p) and (:pt=0 or p.product_type_id =:pt) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ct=0 or d.contract_id=:ct) and (:ie=0 or d.ie_categories_id=:ie) and (:tp=-1 or p.typep=:tp) ");
			sql.append("group by p.product_type_id,p.product_com_id,d.ie_categories_id) as t1 ");
			sql.append("inner join productcom as pc1 on t1.product_com_id=pc1.id ");
			sql.append("inner join producttype as pt1 on t1.product_type_id=pt1.id ");
			sql.append("inner join iecategories as ie1 on t1.ie_categories_id=ie1.id ");
			sql.append("order by pt1.name,pc1.pcom_name ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hContractId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter("tp", Integer.parseInt(Objects.toString(hTypeP.getValue(), "-1")));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				TongKetXuatTheoSanPhamTongKetTieuThuLoai2 item = new TongKetXuatTheoSanPhamTongKetTieuThuLoai2();
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[0])));
				item.setProduct_type_name(Objects.toString(p[1]));
				item.setProduct_com_id(Long.parseLong(Objects.toString(p[2])));
				item.setProduct_com_name(Objects.toString(p[3]));
				item.setIe_categories_id(Long.parseLong(Objects.toString(p[4])));
				item.setIe_categories_name(Objects.toString(p[5]));
				item.setQuantity(MyMath.roundCustom(Double.parseDouble(Objects.toString(p[6], "0")), 2));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[7], "0")));
				item.setTotal_tax_amount((double) MyMath.round(Double.parseDouble(Objects.toString(p[8], "0"))));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3(String json,
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai3> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hContractId = JsonParserUtil.getValueNumber(data, "contract_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			HolderParser hTypeP = JsonParserUtil.getValueNumber(data, "typep", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select p1.product_type_id,pt1.name,t1.product_id,p1.product_name,t1.quantity,t1.unit_price,t1.total_amount,t1.total_tax_amount ");
			sql.append("from( select dt.product_id,dt.unit_price,sum(dt.quantity*p.factor) as quantity,sum(dt.total) as total_amount, ");
			sql.append("sum(dt.total*d.tax_value) as total_tax_amount ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p  on dt.product_id=p.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id=:p) and (:pt=0 or p.product_type_id =:pt) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ct=0 or d.contract_id=:ct) and (:ie=0 or d.ie_categories_id=:ie) and (:tp=-1 or p.typep=:tp) ");
			sql.append("group by dt.product_id,dt.unit_price) as t1  ");
			sql.append("inner join product as p1 on t1.product_id=p1.id ");
			sql.append("inner join producttype as pt1 on p1.product_type_id=pt1.id ");
			sql.append("order by pt1.name,p1.product_name ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hContractId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter("tp", Integer.parseInt(Objects.toString(hTypeP.getValue(), "-1")));
			List<Object[]> listResult = query.getResultList();
			// (long product_type_id, String product_type_name, long
			// product_id,String product_name, Double quantity, Double
			// box_quantity, Double total_amount, Double total_tax_amount,Double
			// total_amount_with_vat)
			for (Object[] p : listResult) {
				TongKetXuatTheoSanPhamTongKetTieuThuLoai3 item = new TongKetXuatTheoSanPhamTongKetTieuThuLoai3();
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[0])));
				item.setProduct_type_name(Objects.toString(p[1], null));
				item.setProduct_id(Long.parseLong(Objects.toString(p[2])));
				item.setProduct_name(Objects.toString(p[3]));
				item.setQuantity(MyMath.roundCustom(Double.parseDouble(Objects.toString(p[4], "0")), 2));
				item.setUnit_price(Double.parseDouble(Objects.toString(p[5], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[6], "0")));
				item.setTotal_tax_amount((double) MyMath.round(Double.parseDouble(Objects.toString(p[7], "0"))));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportTongKetXuatTheoThanhPho(String json, List<TongKetXuatTheoThanhPhoTongKetTieuThu> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,ie_categories_id:0,area_id:0,city_id:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hAreaId = JsonParserUtil.getValueNumber(data, "area_id", null);
			HolderParser hCityId = JsonParserUtil.getValueNumber(data, "city_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select c1.area_id,a1.area_code,a1.area_name,t1.city_id,c1.city_code,c1.city_name,p1.product_type_id,pt1.code,pt1.name,pc1.product_brand_id,pb1.pbrand_code,pb1.pbrand_name,t1.product_id,p1.product_code,p1.product_name, t1.box_quantity,t1.quantity,t1.total_amount,t1.total_foreign_amount ");
			sql.append("from (select dt.product_id,c.city_id,sum(dt.quantity/p.specification) as box_quantity,sum(dt.quantity*p.factor) as quantity,sum(dt.total) as total_amount,sum(dt.quantity*dt.foreign_unit_price) as total_foreign_amount ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join customer as c on  d.customer_id=c.id ");
			sql.append("inner join city as ct on c.city_id=ct.id ");
			sql.append("inner join product as p  on dt.product_id=p.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id=:p) and (:pt=0 or p.product_type_id =:pt) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ie=0 or d.ie_categories_id=:ie) and (:a=0 or ct.area_id=:a) and (:ct =0 or c.city_id=:ct) ");
			sql.append("group by dt.product_id,c.city_id) as t1 ");
			sql.append("inner join product as p1 on t1.product_id=p1.id ");
			sql.append("inner join productcom as pc1 on p1.product_com_id=pc1.id ");
			sql.append("inner join productbrand as pb1 on pc1.product_brand_id=pb1.id ");
			sql.append("inner join producttype as pt1 on p1.product_type_id=pt1.id ");
			sql.append("inner join city as c1 on t1.city_id=c1.id ");
			sql.append("inner join area as a1 on c1.area_id=a1.id ");
			sql.append("order by a1.area_name,c1.city_name,pb1.pbrand_name,p1.product_name ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hCityId.getValue())));
			query.setParameter("a", Long.parseLong(Objects.toString(hAreaId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				// c1.area_id,a1.area_code,a1.area_name,t1.city_id,c1.city_name,p1.product_type_id,pt1.code,pt1.name,pc1.product_brand_id,pb1.pbrand_name,t1.product_id,p1.product_name,
				// t1.box_quantity,t1.quantity,t1.total_amount,t1.total_foreign_amount
				TongKetXuatTheoThanhPhoTongKetTieuThu item = new TongKetXuatTheoThanhPhoTongKetTieuThu();
				item.setArea_id(Long.parseLong(Objects.toString(p[0])));
				item.setArea_code(Objects.toString(p[1]));
				item.setArea_name(Objects.toString(p[2]));
				item.setCity_id(Long.parseLong(Objects.toString(p[3])));
				item.setCity_code(Objects.toString(p[4]));
				item.setCity_name(Objects.toString(p[5]));
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[6])));
				item.setProduct_type_code(Objects.toString(p[7]));
				item.setProduct_type_name(Objects.toString(p[8]));
				item.setProduct_brand_id(Long.parseLong(Objects.toString(p[9])));
				item.setProduct_brand_code(Objects.toString(p[10]));
				item.setProduct_brand_name(Objects.toString(Objects.toString(p[11])));
				item.setProduct_id(Long.parseLong(Objects.toString(p[12])));
				item.setProduct_code(Objects.toString(p[13]));
				item.setProduct_name(Objects.toString(p[14]));
				item.setBox_quantity(Double.parseDouble(Objects.toString(p[15], "0")));
				item.setQuantity(Double.parseDouble(Objects.toString(p[16], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[17], "0")));
				item.setTotal_foreign_amount(Double.parseDouble(Objects.toString(p[18], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetXuatTheoThanhPho:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportTongKetXuatSanPhamTheoQuocGia(String json, List<TongKetXuatSanPhamTheoQuocGia> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,ie_categories_id:0,area_id:0,city_id:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hAreaId = JsonParserUtil.getValueNumber(data, "area_id", null);
			HolderParser hCityId = JsonParserUtil.getValueNumber(data, "city_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.country_id,ctr1.country_code,ctr1.country_name,t1.quantity_bg,t1.total_amount_bg,t1.total_foreign_amount_bg,t1.quantity_ntr,t1.total_amount_ntr,t1.total_foreign_amount_ntr ");
			sql.append("from(select ct.country_id,SUM(CASE When pt.typept =1 Then dt.quantity Else 0 End ) as quantity_bg,SUM(CASE When pt.typept =1 Then (dt.total) Else 0 End )  as total_amount_bg, ");
			sql.append("SUM(CASE When pt.typept =1 Then (dt.quantity*dt.foreign_unit_price) Else 0 End )  as total_foreign_amount_bg,SUM(CASE When pt.typept <>1 Then dt.quantity Else 0 End ) as quantity_ntr, ");
			sql.append("SUM(CASE When pt.typept <>1 Then (dt.total) Else 0 End )  as total_amount_ntr,SUM(CASE When pt.typept <>1 Then (dt.quantity*dt.foreign_unit_price) Else 0 End )  as total_foreign_amount_ntr ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p on dt.product_id=p.id ");
			sql.append("inner join producttype as pt on p.product_type_id=pt.id ");
			sql.append("inner join customer as c on d.customer_id=c.id ");
			sql.append("inner join city as ct on c.city_id=ct.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id=:p) and (:pt=0 or p.product_type_id =:pt) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ie=0 or d.ie_categories_id=:ie) and (:a=0 or ct.area_id=:a) and (:ct =0 or c.city_id=:ct) ");
			sql.append("group by ct.country_id) as t1 ");
			sql.append("inner join country as ctr1 on t1.country_id=ctr1.id ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hCityId.getValue())));
			query.setParameter("a", Long.parseLong(Objects.toString(hAreaId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				TongKetXuatSanPhamTheoQuocGia item = new TongKetXuatSanPhamTheoQuocGia();
				// t1.country_id,ctr1.country_code,ctr1.country_name,t1.quantity_bg,t1.total_amount_bg,t1.total_foreign_amount_bg,t1.quantity_ntr,t1.total_amount_ntr,t1.total_foreign_amount_ntr
				item.setCountry_id(Long.parseLong(Objects.toString(p[0])));
				item.setCountry_code(Objects.toString(p[1]));
				item.setCountry_name(Objects.toString(p[2]));
				item.setQuantity_bg(Double.parseDouble(Objects.toString(p[3], "0")));
				item.setTotal_amount_bg(Double.parseDouble(Objects.toString(p[4], "0")));
				item.setTotal_foreign_amount_bg(Double.parseDouble(Objects.toString(p[5], "0")));
				item.setQuantity_ntr(Double.parseDouble(Objects.toString(p[6], "0")));
				item.setTotal_amount_ntr(Double.parseDouble(Objects.toString(p[7], "0")));
				item.setTotal_foreign_amount_ntr(Double.parseDouble(Objects.toString(p[8], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetXuatSanPhamTheoQuocGia:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportTongKetXuatTheoBrandName(String json, List<TongKetXuatTheoBrandName> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hContractId = JsonParserUtil.getValueNumber(data, "contract_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.product_type_id,pt1.name,t1.product_brand_id,pb1.pbrand_name,t1.quantity,t1.total_amount,t1.total_tax_amount,t1.total_amount+t1.total_tax_amount as total_amount_with_vat ");
			sql.append("from(select p.product_type_id,pc.product_brand_id,sum(dt.quantity*p.factor) as quantity, ");
			sql.append("sum(dt.total) as total_amount,sum(dt.total*d.tax_value) as total_tax_amount ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p on dt. product_id=p.id ");
			sql.append("inner join productcom as pc on p.product_com_id=pc.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id=:p) and (:pt=0 or p.product_type_id =:pt) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ct=0 or d.contract_id=:ct) and (:ie=0 or d.ie_categories_id=:ie) ");
			sql.append("group by p.product_type_id,pc.product_brand_id) as t1 ");
			sql.append("inner join producttype as pt1 on t1.product_type_id=pt1.id ");
			sql.append("inner join productbrand as pb1 on t1.product_brand_id=pb1.id ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hContractId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				TongKetXuatTheoBrandName item = new TongKetXuatTheoBrandName();
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[0])));
				item.setProduct_type_name(Objects.toString(p[1]));
				item.setProduct_brand_id(Long.parseLong(Objects.toString(p[2])));
				item.setProduct_brand_name(Objects.toString(p[3]));
				item.setQuantity(Double.parseDouble(Objects.toString(p[4], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[5], "0")));
				item.setTotal_tax_amount(Double.parseDouble(Objects.toString(p[6], "0")));
				item.setTotal_amount_with_vat(Double.parseDouble(Objects.toString(p[7], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetXuatTheoThanhPho:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportBangKeHoaDon(String json, List<BangKeHoaDon> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,customer_id:0,contract_id:0,ie_categories_id:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hContractId = JsonParserUtil.getValueNumber(data, "contract_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select d.id,d.voucher_code,d.invoice_code,d.invoice_date,d.customer_id,c1.customer_code,c1.customer_name,c1.address,c1.company_name,d.ie_categories_id,ie1.code,ie1.content, d.tongtien, d.thue,d.tongtien+d.thue,d.created_by_id,d.created_by  ");

			sql.append(" FROM invoice as d   ");

			sql.append(" LEFT JOIN iecategories as ie1 on d.ie_categories_id=ie1.id   ");
			sql.append(" LEFT JOIN customer as c1 on d.customer_id=c1.id   ");
			sql.append(" WHERE (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) AND (:c=0 or d.customer_id=:c) and (:ct=0 or d.contract_id=:ct) and (:ie=0 or d.ie_categories_id=:ie) ");
			sql.append(" ORDER BY d.invoice_date,d.voucher_code ");

			// sql.append("from( select dt.invoice_id,sum(dt.total) as total_amount from  ");
			// sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			// sql.append("inner join product as p on dt. product_id=p.id ");
			// sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id=:p) ");
			// sql.append("and (:c=0 or d.customer_id=:c) and (:ct=0 or d.contract_id=:ct) and (:ie=0 or d.ie_categories_id=:ie) ");
			// sql.append("group by dt.invoice_id) as t1 ");
			// sql.append("inner join invoice as d1 on t1.invoice_id=d1.id ");
			// sql.append("inner join iecategories as ie1 on d1.ie_categories_id=ie1.id ");
			// sql.append("inner join customer as c1 on d1.customer_id=c1.id order by d1.invoice_date,d1.voucher_code ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hContractId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				BangKeHoaDon item = new BangKeHoaDon();
				item.setInvoice_id(Long.parseLong(Objects.toString(p[0])));
				item.setVoucher_code(Objects.toString(p[1]));
				item.setInvoice_code(Objects.toString(p[2]));
				item.setInvoice_date((Date) p[3]);
				item.setCustomer_id(Long.parseLong(Objects.toString(p[4])));
				item.setCustomer_code(Objects.toString(p[5]));
				item.setCustomer_name(Objects.toString(p[6]));
				item.setAddress(Objects.toString(p[7]));
				item.setCompany_name(Objects.toString(p[8]));
				item.setIe_categories_id(Long.parseLong(Objects.toString(p[9])));
				item.setIe_categories_code(Objects.toString(p[10]));
				item.setIe_categories_name(Objects.toString(p[11]));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[12], "0")));
				item.setTotal_tax_amount(Double.parseDouble(Objects.toString(p[13], "0")));
				item.setTotal_amount_with_vat(Double.parseDouble(Objects.toString(p[14], "0")));
				item.setCreated_by_id(Long.parseLong(Objects.toString(p[15])));
				item.setCreated_by(Objects.toString(p[16]));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportBangKeHoaDon:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportCongNo(String json, List<CongNo> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_type_id:0,customer_id:0,ie_categories_id:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select d.customer_id,c1.customer_code,c1.customer_name,c1.city_id,ct1.city_code,ct1.city_name,sum(d.tongtien) ");
			sql.append("from  invoice as d ");
			sql.append("left join customer as c1 on d.customer_id=c1.id ");
			sql.append("left join city as ct1 on c1.city_id=ct1.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ie=0 or d.ie_categories_id=:ie) ");
			sql.append("group by d.customer_id");

			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				CongNo item = new CongNo();
				item.setCustomer_id(Long.parseLong(Objects.toString(p[0])));
				item.setCustomer_code(Objects.toString(p[1]));
				item.setCustomer_name(Objects.toString(p[2]));
				try {
					item.setCity_id(Long.parseLong(Objects.toString(p[3])));
					item.setCity_code(Objects.toString(p[4]));
					item.setCity_name(Objects.toString(p[5]));
				} catch (Exception e) {
				}

				item.setTotal_amount(Double.parseDouble(Objects.toString(p[6])));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportCongNo:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportChiTietCongNo(String json, List<ChiTietCongNo> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_type_id:0,customer_id:0,ie_categories_id:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select d.customer_id,c.customer_code,c.customer_name,dt.invoice_id,d.invoice_date,d.invoice_code,d.voucher_code,d.ie_categories_id,ie.code,ie.content,dt.product_id,p.product_code,p.product_name, ");
			sql.append("dt.quantity,dt.unit_price,dt.total as total_amount,d.car_id,ca.license_plate ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p on dt.product_id=p.id ");
			sql.append("inner join customer as c on d.customer_id=c.id ");
			sql.append("left join iecategories as ie on d.ie_categories_id=ie.id ");
			sql.append("left join car as ca on d.car_id=ca.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:pt=0 or p.product_type_id =:pt) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ie=0 or d.ie_categories_id=:ie) ");
			sql.append("order by c.customer_name,d.invoice_date,d.invoice_code,p.product_name ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				int a = 1;
				double g = 5.5;
				long l1 = 1l;
				Long g2 = 1L;
				Integer b = 1;
				// d.customer_id,c.customer_code,c.customer_name,d.invoice_date,d.invoice_code,d.ie_categories_id,ie.code,ie.content,dt.product_id,p.product_code,p.product_name,
				ChiTietCongNo item = new ChiTietCongNo();
				item.setCustomer_id(Long.parseLong(Objects.toString(p[0])));
				item.setCustomer_code(Objects.toString(p[1]));
				item.setCustomer_name(Objects.toString(Objects.toString(p[2])));
				item.setInvoice_id(Long.parseLong(Objects.toString(p[3])));
				item.setInvoice_date((Date) p[4]);
				item.setInvoice_code(Objects.toString(p[5]));
				item.setVoucher_code(Objects.toString(p[6]));
				item.setIe_categories_id(Long.parseLong(Objects.toString(p[7], "0")));
				item.setIe_categories_code(Objects.toString(p[8], null));
				item.setIe_categories_name(Objects.toString(p[9], null));
				item.setProduct_id(Long.parseLong(Objects.toString(p[10])));
				item.setProduct_code(Objects.toString(p[11]));
				item.setProduct_name(Objects.toString(p[12]));
				item.setQuantity(Double.parseDouble(Objects.toString(p[13], "0")));
				item.setUnit_price(Double.parseDouble(Objects.toString(p[14], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[15], "0")));
				item.setCar_id(Long.parseLong(Objects.toString(p[16], "0")));
				item.setLicense_plate(Objects.toString(p[17], null));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportChiTietCongNo:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportThongKeSanLuongDoanhThu(String json, List<ThongKeSanLuongDoanhThu> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,customer_id:0,ie_categories_id:0,typep:-1}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			HolderParser hTypeP = JsonParserUtil.getValueNumber(data, "typep", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select p.product_type_id,pt.name,sum(dt.quantity*p.factor)/1000 as quantity,sum(dt.total)/1000000 as total_amount from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p on dt.product_id=p.id ");
			sql.append("inner join producttype as pt on p.product_type_id=pt.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id =:p) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ie=0 or d.ie_categories_id=:ie) and (:tp=-1 or p.typep=:tp) ");
			sql.append("group by p.product_type_id,pt.name ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter("tp", Integer.parseInt(Objects.toString(hTypeP.getValue(), "-1")));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				ThongKeSanLuongDoanhThu item = new ThongKeSanLuongDoanhThu();
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[0])));
				item.setProduct_type_name(Objects.toString(Objects.toString(p[1], "0")));
				item.setQuantity(Double.parseDouble(Objects.toString(p[2], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[3], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportThongKeSanLuongDoanhThu:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel(String json,
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hContractId = JsonParserUtil.getValueNumber(data, "contract_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			HolderParser hTypeP = JsonParserUtil.getValueNumber(data, "typep", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.product_id,p1.product_code,p1.product_name,p1.factor,p1.specification,t1.quantity/p1.specification as quantity,t1.total_amount,t1.total_tax_amount ");
			sql.append("from( select dt.product_id,sum(dt.quantity) as quantity,sum(dt.total) as total_amount, sum(dt.total*d.tax_value) as total_tax_amount ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p  on dt.product_id=p.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id=:p) and (:pt=0 or p.product_type_id =:pt) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ct=0 or d.contract_id=:ct) and (:ie=0 or d.ie_categories_id=:ie) and (:tp=-1 or p.typep=:tp) ");
			sql.append("group by dt.product_id) as t1 ");
			sql.append("inner join product as p1 on t1.product_id=p1.id ");
			sql.append("order by p1.product_name ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hContractId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter("tp", Integer.parseInt(Objects.toString(hTypeP.getValue(), "-1")));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				TongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel item = new TongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel();
				item.setProduct_id(Long.parseLong(Objects.toString(p[0])));
				;
				item.setProduct_code(Objects.toString(p[1]));
				item.setProduct_name(Objects.toString(p[2]));
				item.setFactor(Double.parseDouble(Objects.toString(p[3], "0")));
				item.setSpecification(Double.parseDouble(Objects.toString(p[4], "0")));
				item.setBox_quantity(Double.parseDouble(Objects.toString(p[5], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[6], "0")));
				item.setTotal_tax_amount((double) MyMath.round(Double.parseDouble(Objects.toString(p[7], "0"))));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1Excel:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel(String json,
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hContractId = JsonParserUtil.getValueNumber(data, "contract_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			HolderParser hTypeP = JsonParserUtil.getValueNumber(data, "typep", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.product_com_id,pc1.pcom_code,pc1.pcom_name,ie1.code,ie1.content,t1.quantity_kg,t1.quantity,t1.total_amount,t1.total_tax_amount ");
			sql.append("from(select p.product_type_id,p.product_com_id,d.ie_categories_id,sum(dt.quantity) as quantity,sum(dt.quantity*p.factor) as quantity_kg,sum(dt.total) as total_amount, ");
			sql.append("sum(dt.total*d.tax_value) as total_tax_amount ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p  on dt.product_id=p.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id=:p) and (:pt=0 or p.product_type_id =:pt) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ct=0 or d.contract_id=:ct) and (:ie=0 or d.ie_categories_id=:ie) and (:tp=-1 or p.typep=:tp) ");
			sql.append("group by p.product_type_id,p.product_com_id,d.ie_categories_id) as t1 ");
			sql.append("inner join productcom as pc1 on t1.product_com_id=pc1.id ");
			sql.append("inner join producttype as pt1 on t1.product_type_id=pt1.id ");
			sql.append("inner join iecategories as ie1 on t1.ie_categories_id=ie1.id ");
			sql.append("order by pt1.name,pc1.pcom_name ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hContractId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter("tp", Integer.parseInt(Objects.toString(hTypeP.getValue(), "-1")));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				// t1.product_com_id,pc1.pcom_code,pc1.pcom_name,t1.ie_categories_id,ie1.content,t.quantity_kg,t1.quantity,t1.total_amount,t1.total_tax_amount
				TongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel item = new TongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel();
				item.setProduct_com_id(Long.parseLong(Objects.toString(p[0])));
				item.setPcom_code(Objects.toString(p[1]));
				item.setPcom_name(Objects.toString(p[2]));
				item.setIe_categories_code(Objects.toString(p[3]));
				item.setIe_categories_name(Objects.toString(p[4]));
				item.setQuantity_kg(MyMath.roundCustom(Double.parseDouble(Objects.toString(p[5], "0")), 2));
				item.setQuantity(Double.parseDouble(Objects.toString(p[6], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[7], "0")));
				item.setTotal_tax_amount((double) MyMath.round(Double.parseDouble(Objects.toString(p[8], "0"))));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai2Excel:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel(String json,
			List<TongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hContractId = JsonParserUtil.getValueNumber(data, "contract_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			HolderParser hTypeP = JsonParserUtil.getValueNumber(data, "typep", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select  t1.ie_categories_id,ie1.code,ie1.content,t1.product_id,p1.product_code,p1.product_name,p1.product_type_id,pt1.name,t1.quantity,t1.quantity_kg,t1.total_amount,t1.total_tax_amount ");
			sql.append("from( select dt.product_id,d.ie_categories_id,sum(dt.quantity) as quantity,sum(dt.quantity*p.factor) as quantity_kg,sum(dt.total) as total_amount, ");
			sql.append("sum(dt.total*d.tax_value) as total_tax_amount ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p  on dt.product_id=p.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id=:p) and (:pt=0 or p.product_type_id =:pt) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ct=0 or d.contract_id=:ct) and (:ie=0 or d.ie_categories_id=:ie) and (:tp=-1 or p.typep=:tp) ");
			sql.append("group by dt.product_id,d.ie_categories_id) as t1 ");
			sql.append("inner join product as p1 on t1.product_id=p1.id ");
			sql.append("inner join iecategories as ie1 on t1.ie_categories_id=ie1.id ");
			sql.append("inner join producttype as pt1 on p1.product_type_id=pt1.id ");
			sql.append("order by ie1.content,pt1.name,p1.product_name ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hContractId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			query.setParameter("tp", Integer.parseInt(Objects.toString(hTypeP.getValue(), "-1")));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				TongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel item = new TongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel();
				item.setIe_categories_id(Long.parseLong(Objects.toString(p[0])));
				item.setIe_categories_code(Objects.toString(p[1]));
				item.setIe_categories_name(Objects.toString(p[2]));
				item.setProduct_id(Long.parseLong(Objects.toString(p[3])));
				item.setProduct_code(Objects.toString(p[4]));
				item.setProduct_name(Objects.toString(p[5]));
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[6])));
				item.setProduct_type_name(Objects.toString(p[7]));
				item.setQuantity(Double.parseDouble(Objects.toString(p[8], "0")));
				item.setQuantity_kg(MyMath.roundCustom(Double.parseDouble(Objects.toString(p[9], "0")), 2));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[10], "0")));
				item.setTotal_tax_amount((double) MyMath.round(Double.parseDouble(Objects.toString(p[11], "0"))));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai3Excel:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportThongKeTheoThang(String json, List<ThongKeTheoThang> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,customer_id:0,ie_categories_id:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(data, "product_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(data, "customer_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.product_type_id,pt1.code,pt1.name,t1.quantity,t1.total_amount from( ");
			sql.append("select p.product_type_id,sum(dt.quantity)/1000 as quantity,sum(dt.total)/1000000 as total_amount from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p on dt.product_id=p.id ");
			sql.append("inner join customer as c on d.customer_id=c.id ");
			sql.append("where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:p=0 or dt.product_id=:p) ");
			sql.append("and (:c=0 or d.customer_id=:c) and (:ie=0 or d.ie_categories_id=:ie) ");
			sql.append("group by p.product_type_id) as t1 ");
			sql.append("inner join producttype as pt1 on t1.product_type_id=pt1.id ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", Objects.toString(ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"), ""));
			query.setParameter("td", Objects.toString(ToolTimeCustomer.convertStringPattern(
					Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"), ""));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				ThongKeTheoThang t = new ThongKeTheoThang();
				t.setProduct_type_id(Long.parseLong(Objects.toString(p[0])));
				t.setProduct_type_code(Objects.toString(p[1]));
				t.setProduct_type_name(Objects.toString(p[2]));
				t.setQuantity(Double.parseDouble(Objects.toString(p[3], "0")));
				t.setTotal_amount(Double.parseDouble(Objects.toString(p[4], "0")));
				list.add(t);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportThongKeTheoThang:" + e.getMessage(), e);
		}
		return res;
	}

	/*
	 * Tính tồn = tổng số lượng nhân với hệ số quy đổi Mở ngày 11/05/2024 (chị
	 * Thu Hà) Có thể số bị lệch với thẻ kho (thẻ kho tính từng phiếu sau đó
	 * cộng lại với nhau)
	 */
	@Override
	public int reportTonKhoThang(String json, List<TonKhoThang> list) {
		int res = -1;
		try {
			/* {month:0, year:0, product_type_id:0,product_id:0,typep:-1} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hMonth = JsonParserUtil.getValueNumber(j, "month", null);
			HolderParser hYear = JsonParserUtil.getValueNumber(j, "year", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(j, "product_type_id", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hTypep = JsonParserUtil.getValueNumber(j, "typep", null);
			int month = Integer.parseInt(Objects.toString(hMonth.getValue()));
			int year = Integer.parseInt(Objects.toString(hYear.getValue()));
			if (month != 0 && year != 0) {
				int monthOB = (month == 1 ? 12 : month - 1);
				int yearOB = (month == 1 ? year - 1 : year);
				Date fromDate = ToolTimeCustomer.getDateMinCustomer(month, year);
				Date toDate = ToolTimeCustomer.getDateMaxCustomer(month, year);
				String fd = ToolTimeCustomer.convertDateToString(fromDate, "yyyy-MM-dd");
				String td = ToolTimeCustomer.convertDateToString(toDate, "yyyy-MM-dd");

				StringBuilder sql = new StringBuilder();
				sql.append("select t1.product_id,p1.product_code,p1.product_name,p1.product_type_id,pt1.code,pt1.name,sum(t1.kg_opening_balance) as kg_opening_balance, ");
				sql.append("sum(ROUND(t1.unit_import_quantity,2)) as unit_import_quantity,ROUND(sum(t1.unit_import_quantity)*p1.factor,2) as kg_import_quantity,ROUND(sum(t1.unit_export_quantity),2) as unit_export_quantity,ROUND(sum(t1.unit_export_quantity) *p1.factor,2) as kg_export_quantity, ");
				sql.append("sum(t1.export_total_amount) as export_total_amount, p1.factor,p1.unit as product_unit, nh.name as nhanhang ");
				sql.append("from( ");
				sql.append("select iv.product_id,iv.closing_balance as kg_opening_balance, 0 as unit_import_quantity, 0 as unit_export_quantity,0 as export_total_amount  from inventory as iv where iv.inventory_month=:mth and iv.inventory_year=:yr ");
				sql.append("union all ");
				sql.append("select dtn.product_id,0,ROUND(dtn.quantity,2) as unit_import_quantity, 0,0 from goodsreceiptnotedetail as dtn ");
				sql.append("inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id ");
				sql.append("where  dn.import_date >= :fd and dn.import_date <= :td ");
				sql.append("union all ");

				sql.append("select dtx.product_id,0,0, ROUND(dtx.quantity,2) as unit_export_quantity, dtx.total as export_total_amount from invoicedetail as dtx ");
				sql.append("inner join invoice as dx on dtx.invoice_id=dx.id ");
				sql.append("where dx.invoice_date >= :fd and dx.invoice_date <= :td AND dx.ie_categories_id != :idmaxuatnhap ) as t1 ");

				sql.append("left join product as p1 on t1.product_id=p1.id ");
				sql.append("left join producttype as pt1 on p1.product_type_id=pt1.id ");
				sql.append("left join nhanhang as nh on p1.nhanHang_id=nh.id ");
				sql.append("where (:p=0 or t1.product_id=:p) and (:pt=0 or p1.product_type_id=:pt) and (:tp=-1 or p1.typep=:tp) ");
				sql.append("group by t1.product_id ");
				sql.append("order by pt1.name,p1.product_code ");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("mth", monthOB);
				query.setParameter("yr", yearOB);
				query.setParameter("fd", fd);
				query.setParameter("td", td);
				query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
				query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
				query.setParameter("tp", Integer.parseInt(Objects.toString(hTypep.getValue())));
				query.setParameter("idmaxuatnhap", 35);// ID: 35 là mã '0' (mã
														// nợ khuyến mãi)
				List<Object[]> listResult = query.getResultList();
				for (Object[] p : listResult) {

					TonKhoThang item = new TonKhoThang();
					item.setProduct_id(Long.parseLong(Objects.toString(p[0])));
					item.setProduct_code(Objects.toString(p[1]));
					item.setProduct_name(Objects.toString(p[2]));
					item.setProduct_type_id(Long.parseLong(Objects.toString(p[3])));
					item.setProduct_type_code(Objects.toString(p[4]));
					item.setProduct_type_name(Objects.toString(p[5]));

					item.setKg_opening_balance(Double.parseDouble(Objects.toString(p[6], "0")));
					item.setUnit_import_quantity(Double.parseDouble(Objects.toString(p[7], "0")));
					item.setKg_import_quantity(Double.parseDouble(Objects.toString(p[8], "0")));
					item.setUnit_export_quantity(Double.parseDouble(Objects.toString(p[9], "0")));
					item.setKg_export_quantity(Double.parseDouble(Objects.toString(p[10], "0")));

					item.setExport_total_amount(Double.parseDouble(Objects.toString(p[11], "0")));
					item.setFactor(Double.parseDouble(Objects.toString(p[12], "0")));
					item.setProduct_unit(Objects.toString(p[13]));
					item.setNhanhang(Objects.toString(p[14]));
					item.setKg_closing_balance(MyMath.roundCustom(
							item.getKg_opening_balance() + item.getKg_import_quantity() - item.getKg_export_quantity(),
							2));

					/*
					 * Kiểm tra nếu số lượng tồn kho < 1 đơn vị tính => cộng vào
					 * số xuất hoặc nhập 05/09/2023 (anh Khánh yêu cầu điều
					 * chỉnh đối với đơn vị tính khác KG thì làm tròn -> Chi
					 * tiết group zalo TTSP)
					 */
					if (!"KG".equalsIgnoreCase(item.getProduct_unit())) {
						double tonconlai = item.getKg_closing_balance();
						if (Math.abs(tonconlai) < item.getFactor()) {
							if (item.getKg_export_quantity() != 0) {
								item.setKg_export_quantity(item.getKg_export_quantity() + tonconlai);
							} else {
								item.setKg_import_quantity(item.getKg_import_quantity() - tonconlai);
							}
							item.setKg_closing_balance(0.0);
						}
					}
					list.add(item);
				}
				res = 0;
			} else {
				res = -1;
			}
		} catch (Exception e) {
			logger.error("ReportService.reportTonKhoThang:" + e.getMessage(), e);
		}
		return res;
	}

	/*
	 * Tính tồn = số lượng nhân với hệ số quy đổi theo từng phiếu Đóng lại ngày
	 * 11/05/2024 (chị Thu Hà)
	 */
	// @Override
	// public int reportTonKhoThang(String json, List<TonKhoThang> list) {
	// int res = -1;
	// try {
	// /* {month:0, year:0, product_type_id:0,product_id:0,typep:-1} */
	// JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
	// HolderParser hMonth = JsonParserUtil.getValueNumber(j, "month", null);
	// HolderParser hYear = JsonParserUtil.getValueNumber(j, "year", null);
	// HolderParser hProductTypeId = JsonParserUtil.getValueNumber(j,
	// "product_type_id", null);
	// HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id",
	// null);
	// HolderParser hTypep = JsonParserUtil.getValueNumber(j, "typep", null);
	// int month = Integer.parseInt(Objects.toString(hMonth.getValue()));
	// int year = Integer.parseInt(Objects.toString(hYear.getValue()));
	// if (month != 0 && year != 0) {
	// int monthOB = (month == 1 ? 12 : month - 1);
	// int yearOB = (month == 1 ? year - 1 : year);
	// Date fromDate = ToolTimeCustomer.getDateMinCustomer(month, year);
	// Date toDate = ToolTimeCustomer.getDateMaxCustomer(month, year);
	// String fd = ToolTimeCustomer.convertDateToString(fromDate, "yyyy-MM-dd");
	// String td = ToolTimeCustomer.convertDateToString(toDate, "yyyy-MM-dd");
	//
	// StringBuilder sql = new StringBuilder();
	// sql.append("select t1.product_id,p1.product_code,p1.product_name,p1.product_type_id,pt1.code,pt1.name,sum(t1.kg_opening_balance) as kg_opening_balance, ");
	// sql.append("sum(ROUND(t1.unit_import_quantity,2)) as unit_import_quantity,sum(ROUND(t1.unit_import_quantity*p1.factor,2)) as kg_import_quantity,sum(ROUND(t1.unit_export_quantity,2)) as unit_export_quantity,sum(ROUND(t1.unit_export_quantity *p1.factor,2)) as kg_export_quantity, ");
	// sql.append("sum(t1.export_total_amount) as export_total_amount, p1.factor,p1.unit as product_unit ");
	// sql.append("from( ");
	// sql.append("select iv.product_id,iv.closing_balance as kg_opening_balance, 0 as unit_import_quantity, 0 as unit_export_quantity,0 as export_total_amount  from inventory as iv where iv.inventory_month=:mth and iv.inventory_year=:yr ");
	// sql.append("union all ");
	// sql.append("select dtn.product_id,0,ROUND(dtn.quantity,2) as unit_import_quantity, 0,0 from goodsreceiptnotedetail as dtn ");
	// sql.append("inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id ");
	// sql.append("where  dn.import_date >= :fd and dn.import_date <= :td ");
	// sql.append("union all ");
	//
	// sql.append("select dtx.product_id,0,0, ROUND(dtx.quantity,2) as unit_export_quantity, dtx.total as export_total_amount from invoicedetail as dtx ");
	// sql.append("inner join invoice as dx on dtx.invoice_id=dx.id ");
	// sql.append("where dx.invoice_date >= :fd and dx.invoice_date <= :td AND dx.ie_categories_id != :idmaxuatnhap ) as t1 ");
	//
	// sql.append("inner join product as p1 on t1.product_id=p1.id ");
	// sql.append("inner join producttype as pt1 on p1.product_type_id=pt1.id ");
	// sql.append("where (:p=0 or t1.product_id=:p) and (:pt=0 or p1.product_type_id=:pt) and (:tp=-1 or p1.typep=:tp) ");
	// sql.append("group by t1.product_id ");
	// sql.append("order by pt1.name,p1.product_code ");
	// Query query = em.createNativeQuery(sql.toString());
	// query.setParameter("mth", monthOB);
	// query.setParameter("yr", yearOB);
	// query.setParameter("fd", fd);
	// query.setParameter("td", td);
	// query.setParameter("p",
	// Long.parseLong(Objects.toString(hProductId.getValue())));
	// query.setParameter("pt",
	// Long.parseLong(Objects.toString(hProductTypeId.getValue())));
	// query.setParameter("tp",
	// Integer.parseInt(Objects.toString(hTypep.getValue())));
	// query.setParameter("idmaxuatnhap", 35);// ID: 35 là mã '0' (mã
	// // nợ khuyến mãi)
	// List<Object[]> listResult = query.getResultList();
	// for (Object[] p : listResult) {
	//
	// TonKhoThang item = new TonKhoThang();
	// item.setProduct_id(Long.parseLong(Objects.toString(p[0])));
	// item.setProduct_code(Objects.toString(p[1]));
	// item.setProduct_name(Objects.toString(p[2]));
	// item.setProduct_type_id(Long.parseLong(Objects.toString(p[3])));
	// item.setProduct_type_code(Objects.toString(p[4]));
	// item.setProduct_type_name(Objects.toString(p[5]));
	//
	// item.setKg_opening_balance(Double.parseDouble(Objects.toString(p[6],
	// "0")));
	// item.setUnit_import_quantity(Double.parseDouble(Objects.toString(p[7],
	// "0")));
	// item.setKg_import_quantity(Double.parseDouble(Objects.toString(p[8],
	// "0")));
	// item.setUnit_export_quantity(Double.parseDouble(Objects.toString(p[9],
	// "0")));
	// item.setKg_export_quantity(Double.parseDouble(Objects.toString(p[10],
	// "0")));
	//
	// item.setExport_total_amount(Double.parseDouble(Objects.toString(p[11],
	// "0")));
	// item.setFactor(Double.parseDouble(Objects.toString(p[12], "0")));
	// item.setProduct_unit(Objects.toString(p[13]));
	// item.setKg_closing_balance(MyMath.roundCustom(
	// item.getKg_opening_balance() + item.getKg_import_quantity() -
	// item.getKg_export_quantity(),
	// 2));
	//
	// /*
	// * Kiểm tra nếu số lượng tồn kho < 1 đơn vị tính => cộng vào
	// * số xuất hoặc nhập 05/09/2023 (anh Khánh yêu cầu điều
	// * chỉnh đối với đơn vị tính khác KG thì làm tròn -> Chi
	// * tiết group zalo TTSP)
	// */
	// if (!"KG".equalsIgnoreCase(item.getProduct_unit())) {
	// double tonconlai = item.getKg_closing_balance();
	// if (Math.abs(tonconlai) < item.getFactor()) {
	// if (item.getKg_export_quantity() != 0) {
	// item.setKg_export_quantity(item.getKg_export_quantity() + tonconlai);
	// } else {
	// item.setKg_import_quantity(item.getKg_import_quantity() - tonconlai);
	// }
	// item.setKg_closing_balance(0.0);
	// }
	// }
	// list.add(item);
	// }
	// res = 0;
	// } else {
	// res = -1;
	// }
	// } catch (Exception e) {
	// logger.error("ReportService.reportTonKhoThang:" + e.getMessage(), e);
	// }
	// return res;
	// }
	/*
	 * Tính tồn = tổng số lượng nhân với hệ số quy đổi Mở ngày 11/05/2024 (chị
	 * Thu Hà)
	 */
	@Override
	public int reportTonKhoSanPham(String json, List<TonKhoSanPham> list) {
		int res = -1;
		try {
			/* {from_date:'',to_date:'product_id:0, product_type_id:0,typep:-1} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(j, "product_type_id", null);
			HolderParser hTypep = JsonParserUtil.getValueNumber(j, "typep", null);
			String fromDate = Objects.toString(hFromDate.getValue(), null);
			String toDate = Objects.toString(hToDate.getValue(), null);
			Date fd = ToolTimeCustomer.convertStringToDate(fromDate, "dd/MM/yyyy");
			Date td = ToolTimeCustomer.convertStringToDate(toDate, "dd/MM/yyyy");
			if (fd != null && td != null) {
				fromDate = ToolTimeCustomer.convertDateToString(fd, "yyyy-MM-dd");
				toDate = ToolTimeCustomer.convertDateToString(td, "yyyy-MM-dd");
				int month = ToolTimeCustomer.getMonthM(fd);
				int year = ToolTimeCustomer.getYearM(fd);
				int monthOB = (month == 1 ? 12 : month - 1);// thÃ¡ng tÃ­nh
															// láº¥y
															// tá»“n Ä‘áº§u
				int yearOB = (month == 1 ? year - 1 : year);// nÄƒm tÃ­nh láº¥y
															// tá»“n
															// Ä‘áº§u
				Date firstDateOfMonth = ToolTimeCustomer.getDateMinCustomer(month, year);
				String fromDateOB = "";// ngÃ y tÃ­nh tá»“n Ä‘áº§u ká»³ theo ngÃ
										// y
				String toDateOB = "";// ngÃ y tÃ­nh tá»“n Ä‘áº§u ká»³ theo ngÃ y
				if (firstDateOfMonth.getTime() < fd.getTime()) {
					fromDateOB = ToolTimeCustomer.convertDateToString(firstDateOfMonth, "yyyy-MM-dd");
					toDateOB = ToolTimeCustomer.convertDateToString(fd, "yyyy-MM-dd");
				}

				StringBuilder sql = new StringBuilder();
				sql.append("select p2.product_type_id,pt2.code,pt2.name,pc2.product_brand_id,pb2.pbrand_code,pb2.pbrand_name,t2.product_id,p2.lever_code,p2.product_code,p2.product_name,p2.factor, ");
				sql.append("p2.specification,p2.promotion_product_id,pkm1.product_code as promotion_product_code,pkm1.product_name as promotion_product_name, ");
				sql.append("(t2.tondau)+ROUND((t2.nhap_bd*p2.factor),2)-ROUND((t2.xuat_bd*p2.factor),2) as tondau_kg,  0,");
				sql.append("ROUND((t2.nhap_chinh*p2.factor),2) as nhap_chinh_kg, ");
				sql.append("t2.nhap_chinh,  ");
				sql.append("ROUND((t2.xuat_chinh*p2.factor),2) as xuat_chinh_kg, ");
				sql.append("t2.xuat_chinh, ");
				sql.append("t2.export_total_amount,  ");

				sql.append("((t2.tondau)+ROUND((t2.nhap_bd*p2.factor),2)-ROUND((t2.xuat_bd*p2.factor),2) ");
				sql.append("+ ");
				sql.append("ROUND((t2.nhap_chinh*p2.factor),2) ");
				sql.append("- ");
				sql.append("ROUND((t2.xuat_chinh*p2.factor),2)) as kg_closing_balance, p2.unit as product_unit,nh.name as nhanhang  ");
				sql.append("from(  ");

				sql.append("select t1.product_id,sum(t1.tondau) as tondau, sum(ROUND(t1.nhap_bd,2)) as nhap_bd,  ");
				sql.append("sum(ROUND(t1.xuat_bd,2)) as xuat_bd,sum(t1.nhap_chinh) as nhap_chinh,  ");
				sql.append("sum(ROUND(t1.xuat_chinh,2)) as xuat_chinh,sum(ROUND(t1.export_total_amount,2)) as export_total_amount  ");
				sql.append("	from(  ");
				sql.append("	select iv.product_id,iv.closing_balance as tondau, 0 as nhap_bd, 0 as xuat_bd,0 as nhap_chinh, 0 as xuat_chinh,0 as export_total_amount  ");
				sql.append("	from inventory as iv where iv.inventory_month=:mth and iv.inventory_year=:yr  ");
				sql.append("	union all  ");
				if (!"".equals(fromDateOB)) {
					sql.append("	select dtns.product_id,0,ROUND(dtns.quantity,2),0,0,0,0 from goodsreceiptnotedetail as dtns  ");
					sql.append("	inner join goodsreceiptnote as dns on dtns.goods_receipt_note_id=dns.id  ");
					sql.append("	where  dns.import_date >= :fdob and dns.import_date < :tdob ");
					sql.append("	union all  ");
					sql.append("	select dtxs.product_id,0,0,ROUND(dtxs.quantity,2),0,0,0 from invoicedetail as dtxs  ");
					sql.append("	inner join invoice as dxs on dtxs.invoice_id=dxs.id   ");
					sql.append("	where dxs.invoice_date >= :fdob and dxs.invoice_date < :tdob AND dxs.ie_categories_id != :idmaxuatnhap ");
					sql.append("	union all  ");
				}
				sql.append("select dtn.product_id,0,0,0,ROUND(dtn.quantity,2),0,0 from goodsreceiptnotedetail as dtn  ");
				sql.append("	inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id  ");
				if (!"".equals(fromDateOB)) {
					sql.append("	where  dn.import_date >= :fd and dn.import_date <= :td  ");
				} else {
					sql.append("	where  dn.import_date >= :fd and dn.import_date <= :td  ");
				}
				sql.append("	union all  ");
				sql.append("	select dtx.product_id,0,0,0,0, ROUND(dtx.quantity,2),dtx.total as export_total_amount from invoicedetail as dtx  ");
				sql.append("	inner join invoice as dx on dtx.invoice_id=dx.id  ");
				sql.append("	where dx.invoice_date >= :fd and dx.invoice_date <= :td AND dx.ie_categories_id != :idmaxuatnhap) ");

				sql.append("as t1  ");
				sql.append("group by t1.product_id  ) as t2  ");
				sql.append("inner join product as p2 on t2.product_id=p2.id  ");
				sql.append("inner join producttype as pt2 on p2.product_type_id=pt2.id  ");
				sql.append("left join productcom as pc2 on p2.product_com_id=pc2.id  ");
				sql.append("left join productbrand as pb2 on pc2.product_brand_id=pb2.id  ");
				sql.append("left join product as pkm1 on p2.promotion_product_id=pkm1.id  ");
				sql.append("left join nhanhang as nh on p2.nhanHang_id=nh.id ");
				sql.append("where (:p=0 or t2.product_id=:p) and (:pt=0 or p2.product_type_id=:pt) and (:tp=-1 or p2.typep=:tp) ");
				sql.append("order by pt2.name,pb2.pbrand_name,p2.product_code ");

				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("mth", monthOB);
				query.setParameter("yr", yearOB);
				if (!"".equals(fromDateOB)) {
					query.setParameter("fdob", fromDateOB);
					query.setParameter("tdob", toDateOB);
				}
				query.setParameter("fd", fromDate);
				query.setParameter("td", toDate);
				query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
				query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
				query.setParameter("tp", Integer.parseInt(Objects.toString(hTypep.getValue(), "-1")));
				query.setParameter("idmaxuatnhap", 35);// ID: 35 lÃ mÃ£ '0' (mÃ£
														// ná»£ khuyáº¿n mÃ£i)
				List<Object[]> listResult = query.getResultList();

				for (Object[] p : listResult) {
					TonKhoSanPham item = new TonKhoSanPham();
					item.setProduct_type_id(Long.parseLong(Objects.toString(p[0])));
					item.setProduct_type_code(Objects.toString(p[1]));
					item.setProduct_type_name(Objects.toString(p[2]));
					item.setPbrand_id(Long.parseLong(Objects.toString(p[3], "0")));
					item.setPbrand_code(Objects.toString(p[4], null));
					item.setPbrand_name(Objects.toString(p[5], null));
					item.setProduct_id(Long.parseLong(Objects.toString(p[6])));
					item.setLever_code(Objects.toString(p[7], null));// lever_code
					item.setProduct_code(Objects.toString(p[8]));// mÃ£ sáº£n
																	// pháº©m
					item.setProduct_name(Objects.toString(p[9]));// tÃªn sáº£n
																	// pháº©m
					item.setFactor(Double.parseDouble(Objects.toString(p[10], "0")));
					item.setSpecification(Double.parseDouble(Objects.toString(p[11], "0")));
					item.setPromotion_product_id(Long.parseLong(Objects.toString(p[12], "0")));
					item.setPromotion_product_code(Objects.toString(p[13], null));
					item.setPromotion_product_name(Objects.toString(p[14], null));
					item.setKg_opening_balance(Double.parseDouble(Objects.toString(p[15], "0")));
					item.setUnit_opening_balance(Double.parseDouble(Objects.toString(p[16], "0")));
					item.setKg_import_quantity(Double.parseDouble(Objects.toString(p[17], "0")));
					item.setUnit_import_quantity(Double.parseDouble(Objects.toString(p[18], "0")));
					item.setKg_export_quantity(Double.parseDouble(Objects.toString(p[19], "0")));
					item.setUnit_export_quantity(Double.parseDouble(Objects.toString(p[20], "0")));
					item.setExport_total_amount(Double.parseDouble(Objects.toString(p[21], "0")));
					item.setKg_closing_balance(Double.parseDouble(Objects.toString(p[22], "0")));
					item.setProduct_unit(Objects.toString(p[23]));
					item.setNhanhang(Objects.toString(p[24]));

					if (!"KG".equalsIgnoreCase(item.getProduct_unit())) {
						double tonconlai = item.getKg_closing_balance();
						if (Math.abs(tonconlai) < item.getFactor()) {
							if (item.getKg_export_quantity() != 0) {
								item.setKg_export_quantity(item.getKg_export_quantity() + tonconlai);
							} else {
								item.setKg_import_quantity(item.getKg_import_quantity() - tonconlai);
							}
							item.setKg_closing_balance(0.0);
						}
					}

					if (item.getKg_opening_balance() != 0 || item.getKg_import_quantity() != 0
							|| item.getKg_export_quantity() != 0) // kg
						list.add(item);
				}
				res = 0;
			} else {
				res = -1;
			}
		} catch (Exception e) {
			logger.error("ReportService.reportTonKhoSanPham:" + e.getMessage(), e);
		}
		return res;
	}
	@Override
	public int reportTonKhoSanPhamChiTietTheoNgay(String json, List<TonKhoSanPhamTheoNgay> list) {
	    int res = -1;
	    try {
	        JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
	        HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
	        HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
	        HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
	        HolderParser hProductTypeId = JsonParserUtil.getValueNumber(j, "product_type_id", null);
	        HolderParser hTypep = JsonParserUtil.getValueNumber(j, "typep", null);
	        
	        String fromDate = Objects.toString(hFromDate.getValue(), null);
	        String toDate = Objects.toString(hToDate.getValue(), null);
	        Date fd = ToolTimeCustomer.convertStringToDate(fromDate, "dd/MM/yyyy");
	        Date td = ToolTimeCustomer.convertStringToDate(toDate, "dd/MM/yyyy");
	        
	        if (fd != null && td != null) {
	            fromDate = ToolTimeCustomer.convertDateToString(fd, "yyyy-MM-dd");
	            toDate = ToolTimeCustomer.convertDateToString(td, "yyyy-MM-dd");
	            int month = ToolTimeCustomer.getMonthM(fd);
	            int year = ToolTimeCustomer.getYearM(fd);
	            int monthOB = (month == 1 ? 12 : month - 1);
	            int yearOB = (month == 1 ? year - 1 : year);
	            Date firstDateOfMonth = ToolTimeCustomer.getDateMinCustomer(month, year);
	            String fromDateOB = "";
	            String toDateOB = "";
	            if (firstDateOfMonth.getTime() < fd.getTime()) {
	                fromDateOB = ToolTimeCustomer.convertDateToString(firstDateOfMonth, "yyyy-MM-dd");
	                toDateOB = ToolTimeCustomer.convertDateToString(fd, "yyyy-MM-dd");
	            }
	            
	            // Hàm làm tròn tiện ích (đến 6 chữ số thập phân)
	            // Cần được định nghĩa ở một nơi có thể truy cập (ví dụ: lớp Utility hoặc định nghĩa trong hàm nếu cần thiết)
	            // Ta tạm định nghĩa như sau:
	            final double SCALE = 1000000.0;
	            java.util.function.Function<Double, Double> round = (value) -> 
	                Math.round(value * SCALE) / SCALE;

	            // Query lấy dữ liệu gộp theo sản phẩm (sửa các phép tính Tồn đầu/Tồn cuối để làm tròn)
	            StringBuilder sql = new StringBuilder();
	            sql.append("select p2.product_type_id,pt2.code,pt2.name,pc2.product_brand_id,pb2.pbrand_code,pb2.pbrand_name,t2.product_id,p2.lever_code,p2.product_code,p2.product_name,p2.factor, ");
	            sql.append("p2.specification,p2.promotion_product_id,pkm1.product_code as promotion_product_code,pkm1.product_name as promotion_product_name, ");
	            
	            // Áp dụng ROUND cho TỒN ĐẦU (Tondau_kg)
	            sql.append("ROUND(((t2.tondau)+ROUND((t2.nhap_bd*p2.factor),2)-ROUND((t2.xuat_bd*p2.factor),2)), 6) as tondau_kg, 0,");
	            
	            sql.append("ROUND((t2.nhap_chinh*p2.factor),2) as nhap_chinh_kg, ");
	            sql.append("t2.nhap_chinh, ");
	            sql.append("ROUND((t2.xuat_chinh*p2.factor),2) as xuat_chinh_kg, ");
	            sql.append("t2.xuat_chinh, ");
	            sql.append("t2.export_total_amount, ");
	            
	            // Áp dụng ROUND cho TỒN CUỐI (kg_closing_balance)
	            sql.append("ROUND(((t2.tondau)+ROUND((t2.nhap_bd*p2.factor),2)-ROUND((t2.xuat_bd*p2.factor),2) ");
	            sql.append("+ ");
	            sql.append("ROUND((t2.nhap_chinh*p2.factor),2) ");
	            sql.append("- ");
	            sql.append("ROUND((t2.xuat_chinh*p2.factor),2)), 6) as kg_closing_balance, p2.unit as product_unit,nh.name as nhanhang ");
	            
	            sql.append("from( ");
	            sql.append("select t1.product_id,sum(t1.tondau) as tondau, sum(ROUND(t1.nhap_bd,2)) as nhap_bd, ");
	            sql.append("sum(ROUND(t1.xuat_bd,2)) as xuat_bd,sum(t1.nhap_chinh) as nhap_chinh, ");
	            sql.append("sum(ROUND(t1.xuat_chinh,2)) as xuat_chinh,sum(ROUND(t1.export_total_amount,2)) as export_total_amount ");
	            sql.append("	from( ");
	            sql.append("	select iv.product_id,iv.closing_balance as tondau, 0 as nhap_bd, 0 as xuat_bd,0 as nhap_chinh, 0 as xuat_chinh,0 as export_total_amount ");
	            sql.append("	from inventory as iv where iv.inventory_month=:mth and iv.inventory_year=:yr ");
	            sql.append("	union all ");
	            if (!"".equals(fromDateOB)) {
	                sql.append("	select dtns.product_id,0,ROUND(dtns.quantity,2),0,0,0,0 from goodsreceiptnotedetail as dtns ");
	                sql.append("	inner join goodsreceiptnote as dns on dtns.goods_receipt_note_id=dns.id ");
	                sql.append("	where dns.import_date >= :fdob and dns.import_date < :tdob ");
	                sql.append("	union all ");
	                sql.append("	select dtxs.product_id,0,0,ROUND(dtxs.quantity,2),0,0,0 from invoicedetail as dtxs ");
	                sql.append("	inner join invoice as dxs on dtxs.invoice_id=dxs.id ");
	                sql.append("	where dxs.invoice_date >= :fdob and dxs.invoice_date < :tdob AND dxs.ie_categories_id != :idmaxuatnhap ");
	                sql.append("	union all ");
	            }
	            sql.append("select dtn.product_id,0,0,0,ROUND(dtn.quantity,2),0,0 from goodsreceiptnotedetail as dtn ");
	            sql.append("	inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id ");
	            sql.append("	where dn.import_date >= :fd and dn.import_date <= :td ");
	            sql.append("	union all ");
	            
	            // Đã loại bỏ dấu trừ cho EXPORT (dtx.quantity)
	            sql.append("	select dtx.product_id,0,0,0,0, ROUND(dtx.quantity,2),dtx.total as export_total_amount from invoicedetail as dtx ");
	            
	            sql.append("	inner join invoice as dx on dtx.invoice_id=dx.id ");
	            sql.append("	where dx.invoice_date >= :fd and dx.invoice_date <= :td AND dx.ie_categories_id != :idmaxuatnhap) ");
	            sql.append("as t1 ");
	            sql.append("group by t1.product_id ) as t2 ");
	            sql.append("inner join product as p2 on t2.product_id=p2.id ");
	            sql.append("inner join producttype as pt2 on p2.product_type_id=pt2.id ");
	            sql.append("left join productcom as pc2 on p2.product_com_id=pc2.id ");
	            sql.append("left join productbrand as pb2 on pc2.product_brand_id=pb2.id ");
	            sql.append("left join product as pkm1 on p2.promotion_product_id=pkm1.id ");
	            sql.append("left join nhanhang as nh on p2.nhanHang_id=nh.id ");
	            sql.append("where (:p=0 or t2.product_id=:p) and (:pt=0 or p2.product_type_id=:pt) and (:tp=-1 or p2.typep=:tp) ");
	            sql.append("order by pt2.name,pb2.pbrand_name,p2.product_code ");

	            Query query = em.createNativeQuery(sql.toString());
	            // ... (Phần setParameter giữ nguyên) ...
	            query.setParameter("mth", monthOB);
	            query.setParameter("yr", yearOB);
	            if (!"".equals(fromDateOB)) {
	                query.setParameter("fdob", fromDateOB);
	                query.setParameter("tdob", toDateOB);
	            }
	            query.setParameter("fd", fromDate);
	            query.setParameter("td", toDate);
	            query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
	            query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
	            query.setParameter("tp", Integer.parseInt(Objects.toString(hTypep.getValue(), "-1")));
	            query.setParameter("idmaxuatnhap", 35);
	            
	            List<Object[]> listResult = query.getResultList();

	            // Query lấy dữ liệu chi tiết theo ngày
	            StringBuilder sqlByDay = new StringBuilder();
	            sqlByDay.append("select dtns.product_id, DATE(dns.import_date) as transaction_date, ");
	            sqlByDay.append("sum(ROUND(dtns.quantity, 2)) as quantity, 'IMPORT' as transaction_type ");
	            sqlByDay.append("from goodsreceiptnotedetail dtns ");
	            sqlByDay.append("inner join goodsreceiptnote dns on dtns.goods_receipt_note_id = dns.id ");
	            sqlByDay.append("where DATE(dns.import_date) >= :fd and DATE(dns.import_date) <= :td ");
	            sqlByDay.append("group by dtns.product_id, DATE(dns.import_date) ");
	            sqlByDay.append("union all ");
	            sqlByDay.append("select dtx.product_id, DATE(dx.invoice_date) as transaction_date, ");
	            
	            // ĐÃ BỎ DẤU TRỪ (-) VÌ TA CẦN SỐ LƯỢNG DƯƠNG CHO MAP EXPORT
	            sqlByDay.append("sum(ROUND(dtx.quantity, 2)) as quantity, 'EXPORT' as transaction_type ");
	            
	            sqlByDay.append("from invoicedetail dtx ");
	            sqlByDay.append("inner join invoice dx on dtx.invoice_id = dx.id ");
	            sqlByDay.append("where DATE(dx.invoice_date) >= :fd and DATE(dx.invoice_date) <= :td ");
	            sqlByDay.append("and dx.ie_categories_id != :idmaxuatnhap ");
	            sqlByDay.append("group by dtx.product_id, DATE(dx.invoice_date) ");

	            Query queryByDay = em.createNativeQuery(sqlByDay.toString());
	            queryByDay.setParameter("fd", fromDate);
	            queryByDay.setParameter("td", toDate);
	            queryByDay.setParameter("idmaxuatnhap", 35);

	            List<Object[]> listResultByDay = queryByDay.getResultList();
	            
	            // Map để lưu dữ liệu giao dịch theo product_id và ngày
	            Map<String, Double> dailyTransactionMap = new HashMap<>();
	            for (Object[] dayData : listResultByDay) {
	                Long productId = ((Number) dayData[0]).longValue();
	                java.sql.Date sqlDate = (java.sql.Date) dayData[1];
	                Double quantity = ((Number) dayData[2]).doubleValue();
	                String transactionType = Objects.toString(dayData[3]);
	                
	                // Lưu giá trị vào map (quantity giờ đã là số dương từ SQL, không cần Math.abs)
	                String key = productId + "_" + sqlDate.toString() + "_" + transactionType;
	                dailyTransactionMap.put(key, quantity);
	            }

	            // Xử lý dữ liệu
	            for (Object[] p : listResult) {
	                TonKhoSanPhamTheoNgay item = new TonKhoSanPhamTheoNgay();
	                // ... (Phần gán các thuộc tính giữ nguyên) ...
	                item.setProduct_type_id(Long.parseLong(Objects.toString(p[0])));
	                item.setProduct_type_code(Objects.toString(p[1]));
	                item.setProduct_type_name(Objects.toString(p[2]));
	                item.setPbrand_id(Long.parseLong(Objects.toString(p[3], "0")));
	                item.setPbrand_code(Objects.toString(p[4], null));
	                item.setPbrand_name(Objects.toString(p[5], null));
	                item.setProduct_id(Long.parseLong(Objects.toString(p[6])));
	                item.setLever_code(Objects.toString(p[7], null));
	                item.setProduct_code(Objects.toString(p[8]));
	                item.setProduct_name(Objects.toString(p[9]));
	                item.setFactor(Double.parseDouble(Objects.toString(p[10], "0")));
	                item.setSpecification(Double.parseDouble(Objects.toString(p[11], "0")));
	                item.setPromotion_product_id(Long.parseLong(Objects.toString(p[12], "0")));
	                item.setPromotion_product_code(Objects.toString(p[13], null));
	                item.setPromotion_product_name(Objects.toString(p[14], null));
	                item.setKg_opening_balance(Double.parseDouble(Objects.toString(p[15], "0")));
	                item.setProduct_unit(Objects.toString(p[22]));
	                item.setNhanhang(Objects.toString(p[24]));

	                // Kiểm tra xem sản phẩm có dữ liệu trong khoảng thời gian không
	                boolean hasData = false;
	                
	                // Lặp qua từng ngày
	                Date currentDate = new Date(fd.getTime());
	                double runningBalance = item.getKg_opening_balance();
	                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	                List<TonKhoSanPhamTheoNgay> productDayItems = new ArrayList<>();
	                
	                while (currentDate.getTime() <= td.getTime()) {
	                    TonKhoSanPhamTheoNgay dayItem = new TonKhoSanPhamTheoNgay();
	                    dayItem.copyFrom(item);
	                    dayItem.setTransaction_date(currentDate);

	                    double dailyImport = 0;
	                    double dailyExport = 0;

	                    // Tìm giao dịch trong ngày từ map
	                    String dateStr = dateFormat.format(currentDate);
	                    Double importQty = dailyTransactionMap.get(item.getProduct_id() + "_" + dateStr + "_IMPORT");
	                    Double exportQty = dailyTransactionMap.get(item.getProduct_id() + "_" + dateStr + "_EXPORT");
	                    
	                    if (importQty != null) {
	                        dailyImport = importQty;
	                        hasData = true;
	                    }
	                    if (exportQty != null) {
	                        dailyExport = exportQty;
	                        hasData = true;
	                    }

	                    dayItem.setUnit_import_quantity(round.apply(dailyImport));
	                    dayItem.setUnit_export_quantity(round.apply(dailyExport));
	                    
	                    // Tính Unit import/export quantity
	                    double factor = item.getFactor() != null && item.getFactor() != 0 ? item.getFactor() : 1;
	                    
	                    // Áp dụng ROUND khi tính toán KG
	                    dayItem.setKg_import_quantity(round.apply(dailyImport * factor));
	                    dayItem.setKg_export_quantity(round.apply(dailyExport * factor));
	                    
	                    dayItem.setKg_opening_balance(round.apply(runningBalance));
	                    
	                    // Áp dụng ROUND cho phép cộng trừ tồn kho để loại bỏ sai số
	                    runningBalance = runningBalance + dayItem.getKg_import_quantity() - dayItem.getKg_export_quantity();
	                    runningBalance = round.apply(runningBalance); // Làm tròn sau khi tính toán
	                    
	                    dayItem.setKg_closing_balance(runningBalance);

	                    productDayItems.add(dayItem);

	                    // Sang ngày tiếp theo
	                    currentDate = new Date(currentDate.getTime() + 24 * 60 * 60 * 1000);
	                }
	                
	                // Chỉ thêm vào list nếu sản phẩm có dữ liệu hoặc tồn đầu khác 0
	                // (Sau khi đã làm tròn, các giá trị siêu nhỏ sẽ về 0.0)
	                if (hasData || item.getKg_opening_balance() != 0) {
	                    list.addAll(productDayItems);
	                }
	            }
	            res = 0;
	        } else {
	            res = -1;
	        }
	    } catch (Exception e) {
	        logger.error("ReportService.reportTonKhoSanPhamChiTietTheoNgay:" + e.getMessage(), e);
	    }
	    return res;
	}

	// Helper method để so sánh hai ngày
	private boolean isSameDay(Date date1, Date date2) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		return fmt.format(date1).equals(fmt.format(date2));
	}
	/*
	 * Tính tồn = số lượng nhân với hệ số quy đổi theo từng phiếu Đóng lại ngày
	 * 11/05/2024 (chị Thu Hà)
	 */
	// @Override
	// public int reportTonKhoSanPham(String json, List<TonKhoSanPham> list) {
	// int res = -1;
	// try {
	// /* {from_date:'',to_date:'product_id:0, product_type_id:0,typep:-1} */
	// JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
	// HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date",
	// null);
	// HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
	// HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id",
	// null);
	// HolderParser hProductTypeId = JsonParserUtil.getValueNumber(j,
	// "product_type_id", null);
	// HolderParser hTypep = JsonParserUtil.getValueNumber(j, "typep", null);
	// String fromDate = Objects.toString(hFromDate.getValue(), null);
	// String toDate = Objects.toString(hToDate.getValue(), null);
	// Date fd = ToolTimeCustomer.convertStringToDate(fromDate, "dd/MM/yyyy");
	// Date td = ToolTimeCustomer.convertStringToDate(toDate, "dd/MM/yyyy");
	// if (fd != null && td != null) {
	// fromDate = ToolTimeCustomer.convertDateToString(fd, "yyyy-MM-dd");
	// toDate = ToolTimeCustomer.convertDateToString(td, "yyyy-MM-dd");
	// int month = ToolTimeCustomer.getMonthM(fd);
	// int year = ToolTimeCustomer.getYearM(fd);
	// int monthOB = (month == 1 ? 12 : month - 1);// tháng tính lấy
	// // tồn đầu
	// int yearOB = (month == 1 ? year - 1 : year);// năm tính lấy tồn
	// // đầu
	// Date firstDateOfMonth = ToolTimeCustomer.getDateMinCustomer(month, year);
	// String fromDateOB = "";// ngày tính tồn đầu kỳ theo ngày
	// String toDateOB = "";// ngày tính tồn đầu kỳ theo ngày
	// if (firstDateOfMonth.getTime() < fd.getTime()) {
	// fromDateOB = ToolTimeCustomer.convertDateToString(firstDateOfMonth,
	// "yyyy-MM-dd");
	// toDateOB = ToolTimeCustomer.convertDateToString(fd, "yyyy-MM-dd");
	// }
	//
	// StringBuilder sql = new StringBuilder();
	// sql.append("select p2.product_type_id,pt2.code,pt2.name,pc2.product_brand_id,pb2.pbrand_code,pb2.pbrand_name,t2.product_id,p2.lever_code,p2.product_code,p2.product_name,p2.factor, ");
	// sql.append("p2.specification,p2.promotion_product_id,pkm1.product_code as promotion_product_code,pkm1.product_name as promotion_product_name, ");
	// sql.append("sum((t2.tondau)+ROUND((t2.nhap_bd*p2.factor),2)-ROUND((t2.xuat_bd*p2.factor),2)) as tondau_kg,  0,");
	// sql.append("sum(ROUND((t2.nhap_chinh*p2.factor),2)) as nhap_chinh_kg, ");
	// sql.append("t2.nhap_chinh,  ");
	// sql.append("sum(ROUND((t2.xuat_chinh*p2.factor),2)) as xuat_chinh_kg, ");
	// sql.append("t2.xuat_chinh, ");
	// sql.append("t2.export_total_amount,  ");
	//
	// sql.append("sum(((t2.tondau)+ROUND((t2.nhap_bd*p2.factor),2)-ROUND((t2.xuat_bd*p2.factor),2) ");
	// sql.append("+ ");
	// sql.append("ROUND((t2.nhap_chinh*p2.factor),2) ");
	// sql.append("- ");
	// sql.append("ROUND((t2.xuat_chinh*p2.factor),2))) as kg_closing_balance, p2.unit as product_unit  ");
	// sql.append("from(  ");
	//
	// sql.append("select t1.product_id,t1.tondau as tondau, ROUND(t1.nhap_bd,2) as nhap_bd,  ");
	// sql.append("ROUND(t1.xuat_bd,2) as xuat_bd,t1.nhap_chinh as nhap_chinh,  ");
	// sql.append("ROUND(t1.xuat_chinh,2) as xuat_chinh,ROUND(t1.export_total_amount,2) as export_total_amount  ");
	// sql.append("	from(  ");
	// sql.append("	select iv.product_id,iv.closing_balance as tondau, 0 as nhap_bd, 0 as xuat_bd,0 as nhap_chinh, 0 as xuat_chinh,0 as export_total_amount  ");
	// sql.append("	from inventory as iv where iv.inventory_month=:mth and iv.inventory_year=:yr  ");
	// sql.append("	union all  ");
	// if (!"".equals(fromDateOB)) {
	// sql.append("	select dtns.product_id,0,ROUND(dtns.quantity,2),0,0,0,0 from goodsreceiptnotedetail as dtns  ");
	// sql.append("	inner join goodsreceiptnote as dns on dtns.goods_receipt_note_id=dns.id  ");
	// sql.append("	where  dns.import_date >= :fdob and dns.import_date < :tdob ");
	// sql.append("	union all  ");
	// sql.append("	select dtxs.product_id,0,0,ROUND(dtxs.quantity,2),0,0,0 from invoicedetail as dtxs  ");
	// sql.append("	inner join invoice as dxs on dtxs.invoice_id=dxs.id   ");
	// sql.append("	where dxs.invoice_date >= :fdob and dxs.invoice_date < :tdob AND dxs.ie_categories_id != :idmaxuatnhap ");
	// sql.append("	union all  ");
	// }
	// sql.append("	select dtn.product_id,0,0,0,ROUND(dtn.quantity,2),0,0 from goodsreceiptnotedetail as dtn  ");
	// sql.append("	inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id  ");
	// if (!"".equals(fromDateOB)) {
	// sql.append("	where  dn.import_date >= :fd and dn.import_date <= :td  ");
	// } else {
	// sql.append("	where  dn.import_date >= :fd and dn.import_date <= :td  ");
	// }
	// sql.append("	union all  ");
	// sql.append("	select dtx.product_id,0,0,0,0, ROUND(dtx.quantity,2),dtx.total as export_total_amount from invoicedetail as dtx  ");
	// sql.append("	inner join invoice as dx on dtx.invoice_id=dx.id  ");
	// // sql.append("	inner join product as pdd on dtx.product_id=pdd.id  ");
	// sql.append("	where dx.invoice_date >= :fd and dx.invoice_date <= :td AND dx.ie_categories_id != :idmaxuatnhap) ");
	//
	// sql.append("as t1  ");
	// // sql.append("group by t1.product_id  ) as t2  ");
	// sql.append("  ) as t2  ");
	// sql.append("inner join product as p2 on t2.product_id=p2.id  ");
	// sql.append("inner join producttype as pt2 on p2.product_type_id=pt2.id  ");
	// sql.append("left join productcom as pc2 on p2.product_com_id=pc2.id  ");
	// sql.append("left join productbrand as pb2 on pc2.product_brand_id=pb2.id  ");
	// sql.append("left join product as pkm1 on p2.promotion_product_id=pkm1.id  ");
	// sql.append("where (:p=0 or t2.product_id=:p) and (:pt=0 or p2.product_type_id=:pt) and (:tp=-1 or p2.typep=:tp) ");
	// sql.append("group by p2.id ");
	// sql.append("order by pt2.name,pb2.pbrand_name,p2.product_code ");
	//
	//
	// Query query = em.createNativeQuery(sql.toString());
	// query.setParameter("mth", monthOB);
	// query.setParameter("yr", yearOB);
	// if (!"".equals(fromDateOB)) {
	// query.setParameter("fdob", fromDateOB);
	// query.setParameter("tdob", toDateOB);
	// }
	// query.setParameter("fd", fromDate);
	// query.setParameter("td", toDate);
	// query.setParameter("p",
	// Long.parseLong(Objects.toString(hProductId.getValue())));
	// query.setParameter("pt",
	// Long.parseLong(Objects.toString(hProductTypeId.getValue())));
	// query.setParameter("tp",
	// Integer.parseInt(Objects.toString(hTypep.getValue(), "-1")));
	// query.setParameter("idmaxuatnhap", 35);// ID: 35 là mã '0' (mã
	// // nợ khuyến mãi)
	// List<Object[]> listResult = query.getResultList();
	//
	// for (Object[] p : listResult) {
	// TonKhoSanPham item = new TonKhoSanPham();
	// item.setProduct_type_id(Long.parseLong(Objects.toString(p[0])));
	// item.setProduct_type_code(Objects.toString(p[1]));
	// // mã sản phẩm
	// item.setProduct_type_name(Objects.toString(p[2]));
	// item.setPbrand_id(Long.parseLong(Objects.toString(p[3], "0")));
	// item.setPbrand_code(Objects.toString(p[4], null));
	// item.setPbrand_name(Objects.toString(p[5], null));
	// item.setProduct_id(Long.parseLong(Objects.toString(p[6])));
	// // id mã sản phẩm
	// item.setLever_code(Objects.toString(p[7], null));// lever_code
	// item.setProduct_code(Objects.toString(p[8]));// mã sản phẩm
	// item.setProduct_name(Objects.toString(p[9]));// tên sản phẩm
	// item.setFactor(Double.parseDouble(Objects.toString(p[10], "0")));
	// item.setSpecification(Double.parseDouble(Objects.toString(p[11], "0")));
	// item.setPromotion_product_id(Long.parseLong(Objects.toString(p[12],
	// "0")));
	// item.setPromotion_product_code(Objects.toString(p[13], null));
	// item.setPromotion_product_name(Objects.toString(p[14], null));
	//
	// item.setKg_opening_balance(Double.parseDouble(Objects.toString(p[15],
	// "0")));
	// item.setUnit_opening_balance(Double.parseDouble(Objects.toString(p[16],
	// "0")));
	// item.setKg_import_quantity(Double.parseDouble(Objects.toString(p[17],
	// "0")));
	// item.setUnit_import_quantity(Double.parseDouble(Objects.toString(p[18],
	// "0")));
	// item.setKg_export_quantity(Double.parseDouble(Objects.toString(p[19],
	// "0")));
	// item.setUnit_export_quantity(Double.parseDouble(Objects.toString(p[20],
	// "0")));
	// item.setExport_total_amount(Double.parseDouble(Objects.toString(p[21],
	// "0")));
	// item.setKg_closing_balance(Double.parseDouble(Objects.toString(p[22],
	// "0")));
	// item.setProduct_unit(Objects.toString(p[23]));
	//
	// if (!"KG".equalsIgnoreCase(item.getProduct_unit())) {
	// double tonconlai = item.getKg_closing_balance();
	// if (Math.abs(tonconlai) < item.getFactor()) {
	// if (item.getKg_export_quantity() != 0) {
	// item.setKg_export_quantity(item.getKg_export_quantity() + tonconlai);
	// } else {
	// item.setKg_import_quantity(item.getKg_import_quantity() - tonconlai);
	// }
	// item.setKg_closing_balance(0.0);
	// }
	// }
	//
	// if (item.getKg_opening_balance() != 0 || item.getKg_import_quantity() !=
	// 0
	// || item.getKg_export_quantity() != 0) // kg
	// list.add(item);
	// }
	// res = 0;
	// } else {
	// res = -1;
	// }
	// } catch (Exception e) {
	// logger.error("ReportService.reportTonKhoSanPham:" + e.getMessage(), e);
	// }
	// return res;
	// }

	@Override
	public int reportTheKhoSanPham(String json, List<TheKhoSanPham> list) {
		int res = -1;
		try {/* {from_month:0,to_month:0,year:0,,product_id:0,product_type_id:0} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromMonth = JsonParserUtil.getValueNumber(j, "from_month", null);
			HolderParser hToMonth = JsonParserUtil.getValueNumber(j, "to_month", null);
			HolderParser hYear = JsonParserUtil.getValueNumber(j, "year", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(j, "product_type_id", null);
			StringBuilder sql = new StringBuilder();
			int fromMonth = Integer.parseInt(Objects.toString(hFromMonth.getValue()));
			int toMonth = Integer.parseInt(Objects.toString(hToMonth.getValue()));
			sql.append("select t2.* from( ");

			sql.append("select month(dn1.import_date) as data_month,year(dn1.import_date) as data_year,tn1.product_id as product_id ,pn1.product_code as product_code,pn1.product_name as product_name,dn1.import_date as data_date,dn1.voucher_code as data_code,dn1.customer_id as customer_id,cn1.customer_code as customer_code,cn1.customer_name, tn1.import_quantity as import_quantity, 0 as export_quantity,0 as ivn_quantity, 1 as typeie, tn1.batch_code as batch_code,pn1.factor as factor,'' as carrier_name,tn1.dtnpo as po ");
			sql.append("from (select dtn.goods_receipt_note_id,dtn.product_id,sum(ROUND(dtn.quantity*pn.factor,3)) as import_quantity, dtn.batch_code as batch_code, dtn.po as dtnpo from goodsreceiptnotedetail as dtn ");
			sql.append("inner join product as pn on dtn.product_id=pn.id ");
			sql.append("inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id ");
			sql.append("where (:pt=0 or pn.product_type_id=:pt) and (:p=0 or dtn.product_id=:p) and (:fm=0 or month(dn.import_date) >=:fm) and (:tm =0 or month(dn.import_date) <=:tm) and (:yr=0 or year(dn.import_date)=:yr) ");
			sql.append("group by dn.id, dtn.batch_code,dtn.product_id) as tn1 ");
			sql.append("inner join goodsreceiptnote as dn1 on tn1.goods_receipt_note_id=dn1.id ");
			sql.append("inner join product as pn1 on tn1.product_id=pn1.id ");
			sql.append("inner join customer as cn1 on dn1.customer_id=cn1.id ");
			sql.append("union all ");

			sql.append("select month(dx1.invoice_date),year(dx1.invoice_date),tx1.product_id,px1.product_code,px1.product_name,dx1.invoice_date,dx1.voucher_code,dx1.customer_id,cx1.customer_code,cx1.customer_name, 0,tx1.quantity,0,2,tx1.batch_code,px1.factor, carr.carrier_name,dx1.po_no from( ");
			sql.append("select dtx.invoice_id,dtx.product_id,0,sum(ROUND(dtx.quantity*px.factor,3)) as quantity, dtx.note_batch_code as batch_code from invoicedetail as dtx ");
			sql.append("inner join product as px on dtx.product_id=px.id ");
			sql.append("inner join invoice as dx on dtx.invoice_id=dx.id ");
			sql.append("where (:pt=0 or px.product_type_id=:pt) and (:p=0 or dtx.product_id=:p) and (:fm=0 or month(dx.invoice_date) >=:fm) and (:tm =0 or month(dx.invoice_date) <=:tm) and (:yr=0 or year(dx.invoice_date)=:yr)  AND dx.ie_categories_id != :idmaxuatnhap ");
			sql.append("group by  dtx.invoice_id,dtx.product_id,dtx.note_batch_code) as tx1 ");
			sql.append("inner join invoice as dx1 on tx1.invoice_id=dx1.id ");
			sql.append("LEFT join carrier as carr on dx1.carrier_id=carr.id ");
			sql.append("inner join customer as cx1 on dx1.customer_id=cx1.id ");
			sql.append("inner join product as px1  on tx1.product_id=px1.id ");
			sql.append("union all ");
			sql.append("select ivn.inventory_month,ivn.inventory_year,ivn.product_id,p.product_code,p.product_name,null, null,null, null,null,0,0, ivn.closing_balance*p.factor, 3,null,0,'','' ");
			sql.append("from inventory as ivn ");
			sql.append("inner join product as p on ivn.product_id=p.id ");
			sql.append("where (:pt=0 or p.product_type_id=:pt) and (:p=0 or ivn.product_id=:p) and (:fm=0 or ivn.inventory_month >=:fm) and (:tm =0 or ivn.inventory_month <=:tm) and (:yr=0 or ivn.inventory_year=:yr)) as t2 ");
			sql.append("order by t2.product_name,t2.data_date,t2.typeie,t2.customer_name,t2.data_code ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fm", fromMonth);
			query.setParameter("tm", toMonth);
			query.setParameter("yr", Integer.parseInt(Objects.toString(hYear.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("idmaxuatnhap", 35);// ID: 35 là mã '0' (mã nợ
													// khuyến mãi)
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				TheKhoSanPham item = new TheKhoSanPham();
				item.setData_month(Integer.parseInt(Objects.toString(p[0])));
				item.setData_year(Integer.parseInt(Objects.toString(p[1])));
				item.setProduct_id(Long.parseLong(Objects.toString(p[2])));
				item.setProduct_code(Objects.toString(p[3]));
				item.setProduct_name(Objects.toString(p[4]));
				item.setData_date((Date) p[5]);
				item.setData_code(Objects.toString(p[6], ""));
				item.setCustomer_id(Long.parseLong(Objects.toString(p[7], "0")));
				item.setCustomer_code(Objects.toString(p[8], ""));
				item.setCustomer_name(Objects.toString(p[9], ""));
				item.setKg_import_quantity(MyMath.roundCustom(Double.parseDouble(Objects.toString(p[10], "0")), 3));
				item.setKg_export_quantity(MyMath.roundCustom(Double.parseDouble(Objects.toString(p[11], "0")), 3));
				item.setKg_ivn_quantity(MyMath.roundCustom(Double.parseDouble(Objects.toString(p[12], "0")), 3));
				item.setTypeie(Integer.parseInt(Objects.toString(p[13])));
				item.setBatch_code(Objects.toString(p[14], ""));
				item.setFactor(Double.parseDouble(Objects.toString(p[15], "0")));
				item.setCarrierName(Objects.toString(p[16], ""));
				item.setPoNo(Objects.toString(p[17], ""));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTheKhoSanPham:" + e.getMessage(), e);
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public int reportTheKhoSanPhamDVT(String json, List<TheKhoSanPham> list) {
		int res = -1;
		try {/* {from_month:0,to_month:0,year:0,,product_id:0,product_type_id:0} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromMonth = JsonParserUtil.getValueNumber(j, "from_month", null);
			HolderParser hToMonth = JsonParserUtil.getValueNumber(j, "to_month", null);
			HolderParser hYear = JsonParserUtil.getValueNumber(j, "year", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(j, "product_type_id", null);
			StringBuilder sql = new StringBuilder();
			int fromMonth = Integer.parseInt(Objects.toString(hFromMonth.getValue()));
			int toMonth = Integer.parseInt(Objects.toString(hToMonth.getValue()));
			sql.append("select t2.* from( ");

			sql.append("select month(dn1.import_date) as data_month,year(dn1.import_date) as data_year,tn1.product_id as product_id ,pn1.product_code as product_code,pn1.product_name as product_name,dn1.import_date as data_date,dn1.voucher_code as data_code,dn1.customer_id as customer_id,cn1.customer_code as customer_code,cn1.customer_name, tn1.import_quantity as import_quantity, 0 as export_quantity,0 as ivn_quantity, 1 as typeie, tn1.batch_code as batch_code,pn1.factor as factor,'' as carrier_name,tn1.dtnpo as po,pn1.unit as product_unit ");
			sql.append("from (select dtn.goods_receipt_note_id,dtn.product_id,sum(ROUND(dtn.quantity,2)) as import_quantity, dtn.batch_code as batch_code, dtn.po as dtnpo from goodsreceiptnotedetail as dtn ");
			sql.append("inner join product as pn on dtn.product_id=pn.id ");
			sql.append("inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id ");
			sql.append("where (:pt=0 or pn.product_type_id=:pt) and (:p=0 or dtn.product_id=:p) and (:fm=0 or month(dn.import_date) >=:fm) and (:tm =0 or month(dn.import_date) <=:tm) and (:yr=0 or year(dn.import_date)=:yr) ");
			sql.append("group by dn.id, dtn.batch_code,dtn.product_id) as tn1 ");
			sql.append("inner join goodsreceiptnote as dn1 on tn1.goods_receipt_note_id=dn1.id ");
			sql.append("inner join product as pn1 on tn1.product_id=pn1.id ");
			sql.append("inner join customer as cn1 on dn1.customer_id=cn1.id ");
			sql.append("union all ");

			sql.append("select month(dx1.invoice_date),year(dx1.invoice_date),tx1.product_id,px1.product_code,px1.product_name,dx1.invoice_date,dx1.voucher_code,dx1.customer_id,cx1.customer_code,cx1.customer_name, 0,tx1.quantity,0,2,tx1.batch_code,px1.factor, carr.carrier_name,dx1.po_no,px1.unit as product_unit from( ");
			sql.append("select dtx.invoice_id,dtx.product_id,0,sum(ROUND(dtx.quantity,2)) as quantity, dtx.note_batch_code as batch_code from invoicedetail as dtx ");
			sql.append("inner join product as px on dtx.product_id=px.id ");
			sql.append("inner join invoice as dx on dtx.invoice_id=dx.id ");
			sql.append("where (:pt=0 or px.product_type_id=:pt) and (:p=0 or dtx.product_id=:p) and (:fm=0 or month(dx.invoice_date) >=:fm) and (:tm =0 or month(dx.invoice_date) <=:tm) and (:yr=0 or year(dx.invoice_date)=:yr)  AND dx.ie_categories_id != :idmaxuatnhap ");
			sql.append("group by  dtx.invoice_id,dtx.product_id,dtx.note_batch_code) as tx1 ");
			sql.append("inner join invoice as dx1 on tx1.invoice_id=dx1.id ");
			sql.append("LEFT join carrier as carr on dx1.carrier_id=carr.id ");
			sql.append("inner join customer as cx1 on dx1.customer_id=cx1.id ");
			sql.append("inner join product as px1  on tx1.product_id=px1.id ");
			sql.append("union all ");

			sql.append("select ivn.inventory_month,ivn.inventory_year,ivn.product_id,p.product_code,p.product_name,null, null,null, null,null,0,0, ivn.closing_balance, 3,null,0,'','',p.unit as product_unit ");
			sql.append("from inventory as ivn ");
			sql.append("inner join product as p on ivn.product_id=p.id ");
			sql.append("where (:pt=0 or p.product_type_id=:pt) and (:p=0 or ivn.product_id=:p) and (:fm=0 or ivn.inventory_month >=:fm) and (:tm =0 or ivn.inventory_month <=:tm) and (:yr=0 or ivn.inventory_year=:yr)) as t2 ");
			sql.append("order by t2.product_name,t2.data_date,t2.typeie,t2.customer_name,t2.data_code ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fm", fromMonth);
			query.setParameter("tm", toMonth);
			query.setParameter("yr", Integer.parseInt(Objects.toString(hYear.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("idmaxuatnhap", 35);// ID: 35 là mã '0' (mã nợ
													// khuyến mãi)
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				TheKhoSanPham item = new TheKhoSanPham();
				item.setData_month(Integer.parseInt(Objects.toString(p[0])));
				item.setData_year(Integer.parseInt(Objects.toString(p[1])));
				item.setProduct_id(Long.parseLong(Objects.toString(p[2])));
				item.setProduct_code(Objects.toString(p[3]));
				item.setProduct_name(Objects.toString(p[4]));
				item.setData_date((Date) p[5]);
				item.setData_code(Objects.toString(p[6], ""));
				item.setCustomer_id(Long.parseLong(Objects.toString(p[7], "0")));
				item.setCustomer_code(Objects.toString(p[8], ""));
				item.setCustomer_name(Objects.toString(p[9], ""));
				item.setKg_import_quantity(MyMath.roundCustom(Double.parseDouble(Objects.toString(p[10], "0")), 2));
				item.setKg_export_quantity(MyMath.roundCustom(Double.parseDouble(Objects.toString(p[11], "0")), 2));
				item.setKg_ivn_quantity(MyMath.roundCustom(Double.parseDouble(Objects.toString(p[12], "0")), 2));
				item.setTypeie(Integer.parseInt(Objects.toString(p[13])));
				item.setBatch_code(Objects.toString(p[14], ""));
				item.setFactor(Double.parseDouble(Objects.toString(p[15], "0")));
				item.setCarrierName(Objects.toString(p[16], ""));
				item.setPoNo(Objects.toString(p[17], ""));
				item.setProduct_dvt(Objects.toString(p[18], ""));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTheKhoSanPham:" + e.getMessage(), e);
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public int reportDoanhThuTheoThang(String json, List<DoanhThuKhachHangTheoThang> list) {
		int res = -1;
		try {
			/*
			 * {from_month:0,to_month:0,year:0,product_id:0,product_type_id:0,
			 * customer_id:0,ie_categories_id:0,typep:-1}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromMonth = JsonParserUtil.getValueNumber(j, "from_month", null);
			HolderParser hToMonth = JsonParserUtil.getValueNumber(j, "to_month", null);
			HolderParser hYear = JsonParserUtil.getValueNumber(j, "year", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueNumber(j, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(j, "ie_categories_id", null);
			HolderParser hTypep = JsonParserUtil.getValueNumber(j, "typep", null);
			int fromMonth = Integer.parseInt(Objects.toString(hFromMonth.getValue()));
			int toMonth = Integer.parseInt(Objects.toString(hToMonth.getValue()));
			int year = Integer.parseInt(Objects.toString(hYear.getValue()));
			if (fromMonth != 0 && toMonth != 0 && year != 0) {
				Date fromDate = ToolTimeCustomer.getDateMinCustomer(fromMonth, year);
				Date toDate = ToolTimeCustomer.getDateMaxCustomer(toMonth, year);
				StringBuilder sql = new StringBuilder();
				sql.append("select t1.customer_id,c1.customer_code,c1.customer_name,t1.data_month,t1.data_year,t1.total_amount ");
				sql.append("from(select d.customer_id,month(d.invoice_date) as data_month, year(d.invoice_date) as data_year, sum(dt.total) as total_amount from invoicedetail as dt ");
				sql.append("inner join invoice as d on dt.invoice_id=d.id ");
				sql.append("inner join product as p on dt.product_id=p.id ");
				sql.append("where d.invoice_date>=:fd and d.invoice_date <=:td and (:pt=0 or p.product_type_id=:pt) and (:p=0 or dt.product_id=:p) and (:c =0 or  d.customer_id=:c) and (:ie=0 or d.ie_categories_id=:ie) and (:tp=-1 or p.typep=:tp) ");
				sql.append("group by  d.customer_id,data_month, data_year) as t1 ");
				sql.append("inner join customer as c1 on t1.customer_id=c1.id ");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("fd", ToolTimeCustomer.convertDateToString(fromDate, "yyyy-MM-dd"));
				query.setParameter("td", ToolTimeCustomer.convertDateToString(toDate, "yyyy-MM-dd"));
				query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
				query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
				query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
				query.setParameter("tp", Integer.parseInt(Objects.toString(hTypep.getValue())));
				query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
				List<Object[]> listResult = query.getResultList();
				Map<Long, DoanhThuKhachHangTheoThang> map = new LinkedHashMap<Long, DoanhThuKhachHangTheoThang>();
				for (Object[] p : listResult) {
					long customerId = Long.parseLong(Objects.toString(p[0]));
					String customerCode = Objects.toString(p[1]);
					String customerName = Objects.toString(p[2]);
					int dataMonth = Integer.parseInt(Objects.toString(p[3]));
					int dataYear = Integer.parseInt(Objects.toString(p[4]));
					double totalAmount = Double.parseDouble(Objects.toString(p[5]));
					DoanhThuKhachHangTheoThang item = null;
					if (map.containsKey(customerId)) {
						item = map.get(customerId);
					} else {
						item = new DoanhThuKhachHangTheoThang(customerId, customerCode, customerName, dataYear);
						map.put(customerId, item);
					}
					switch (dataMonth) {
					case 1:
						item.setJanuary_total_amount(totalAmount);
						break;
					case 2:
						item.setFebruary_total_amount(totalAmount);
						break;
					case 3:
						item.setMarch_total_amount(totalAmount);
						break;
					case 4:
						item.setApril_total_amount(totalAmount);
						break;
					case 5:
						item.setMay_total_amount(totalAmount);
						break;
					case 6:
						item.setJune_total_amount(totalAmount);
						break;
					case 7:
						item.setJuly_total_amount(totalAmount);
						break;
					case 8:
						item.setAugust_total_amount(totalAmount);
						break;
					case 9:
						item.setSeptember_total_amount(totalAmount);
						break;
					case 10:
						item.setOctober_total_amount(totalAmount);
						break;
					case 11:
						item.setNovember_total_amount(totalAmount);
						break;
					case 12:
						item.setDecember_total_amount(totalAmount);
						break;
					default:
						break;
					}
				}
				if (map.size() > 0) {
					list.addAll(map.values());
				}
				res = 0;
			} else {
				res = -1;
			}
		} catch (Exception e) {
			logger.error("ReportService.reportDoanhThuTheoThang:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportBangKeChiTietHoaDon(String json, List<BangKeChiTietHoaDon> list) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,customer_id:0,contract_id:0,ie_categories_id:0}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			HolderParser hContractId = JsonParserUtil.getValueNumber(j, "contract_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(j, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select d.invoice_date,d.invoice_code,od.order_code,od.voucher_code as order_voucher,cr.license_plate,dt.product_id,p.lever_code,p.product_code,p.product_name,p.unit, ");
			sql.append("dt.quantity/p.specification as box_quantity, dt.quantity*p.factor as kg_quantity, dt.quantity as unit_quantity,dt.unit_price, dt.total as total_amount,dt.total*d.tax_value as total_tax_amount,d.note, ");
			sql.append("d.customer_id,c.customer_code,c.customer_name,cts.name as customer_type_name,d.ie_categories_id,ie.code as ie_categories_code,ie.content as ie_categories_name ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join iecategories as ie on d.ie_categories_id=ie.id ");
			sql.append("inner join customer as c on d.customer_id=c.id ");
			sql.append("left join customertypes as cts on c.customer_types_id=cts.id ");
			sql.append("inner join product as p on dt.product_id=p.id ");
			sql.append("left join car as cr on d.car_id=cr.id ");
			sql.append("left join orderlix as od on d.order_lix_id=od.id ");
			sql.append("where (:fd = '' or d.invoice_date>=:fd) and (:td = '' or d.invoice_date<=:td) and (:p = 0 or dt.product_id=:p) and (:c=0 or d.customer_id=:c) and (:ie=0 or d.ie_categories_id=:ie) and (:ct=0 or d.contract_id=:ct)  ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", Objects.toString(ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue()), "dd/MM/yyyy", "yyyy-MM-dd"), ""));
			query.setParameter("td", Objects.toString(ToolTimeCustomer.convertStringPattern(
					Objects.toString(hToDate.getValue()), "dd/MM/yyyy", "yyyy-MM-dd"), ""));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hContractId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				BangKeChiTietHoaDon item = new BangKeChiTietHoaDon();
				item.setInvoice_date((Date) p[0]);
				item.setInvoice_code(Objects.toString(p[1], null));
				item.setOrder_code(Objects.toString(p[2], null));
				item.setOrder_voucher(Objects.toString(p[3], null));
				item.setLicense_plate(Objects.toString(p[4], null));
				item.setProduct_id(Long.parseLong(Objects.toString(p[5], null)));
				item.setLever_code(Objects.toString(p[6], null));
				item.setProduct_code(Objects.toString(p[7], null));
				item.setProduct_name(Objects.toString(p[8], null));
				item.setUnit(Objects.toString(p[9], null));
				item.setBox_quantity(Double.parseDouble(Objects.toString(p[10], "0")));
				item.setKg_quantity(Double.parseDouble(Objects.toString(p[11], "0")));
				item.setUnit_quantity(Double.parseDouble(Objects.toString(p[12], "0")));
				item.setUnit_price(Double.parseDouble(Objects.toString(p[13], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[14], "0")));
				item.setTotal_tax_amount(Double.parseDouble(Objects.toString(p[15], "0")));
				item.setNote(Objects.toString(p[16], null));
				item.setCustomer_id(Long.parseLong(Objects.toString(p[17], "0")));
				item.setCustomer_code(Objects.toString(p[18], null));
				item.setCustomer_name(Objects.toString(p[19], null));
				;
				item.setCustomer_type_name(Objects.toString(p[20], null));
				item.setIe_categories_id(Long.parseLong(Objects.toString(p[21], "0")));
				item.setIe_categories_name(Objects.toString(p[22], null));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportBangKeChiTietHoaDon:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportNhapXuatTheoNgay(String json, List<NhapXuatNgay> list) {
		int res = -1;
		try {
			/*
			 * {from_date:'',to_date:'',product_id:0,
			 * product_type_id:0,customer_id:0,ie_categories_id:0}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueString(j, "product_id", null);
			HolderParser hProductTypeId = JsonParserUtil.getValueString(j, "product_type_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			HolderParser hIECategoriesId = JsonParserUtil.getValueNumber(j, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.data_date,t1.product_type_id,pt1.code as product_type_code,pt1.name as product_type_name,t1.ie_categories_id, ");
			sql.append("ie1.code as ie_categories_code,ie1.content as ie_categories_name,t1.import_quantity,t1.export_quantity ");
			sql.append("from(select dn.import_date as data_date,dn.ie_categories_id,pn.product_type_id,sum(dtn.quantity) as import_quantity,0 as export_quantity from goodsreceiptnotedetail as dtn ");
			sql.append("inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id ");
			sql.append("inner join product as pn on dtn.product_id=pn.id ");
			sql.append("where (:fd='' or dn.import_date >= :fd) and (:td = '' or dn.import_date <= :td) and (:p=0 or dtn.product_id=:p) and (:pt=0 or pn.product_type_id=:pt) and (:c=0 or dn.customer_id=:c) and (:ie=0 or dn.ie_categories_id=:ie) ");
			sql.append("group by dn.import_date,dn.ie_categories_id,pn.product_type_id ");
			sql.append("union all ");
			sql.append("select dx.invoice_date,dx.ie_categories_id,px.product_type_id,0 as import_quantity,sum(dtx.quantity) as export_quantity from invoicedetail as dtx ");
			sql.append("inner join invoice as dx on dtx.invoice_id=dx.id ");
			sql.append("inner join product as px on dtx.product_id=px.id ");
			sql.append("where (:fd='' or dx.invoice_date >= :fd) and (:td='' or dx.invoice_date <=:td) and (:p=0 or dtx.product_id=:p) and (:pt=0 or px.product_type_id=:pt) and (:c=0 or dx.customer_id=:c) and (:ie=0 or dx.ie_categories_id=:ie) ");
			sql.append("group by dx.invoice_date,dx.ie_categories_id,px.product_type_id ) as t1 ");
			sql.append("inner join iecategories as ie1 on t1.ie_categories_id=ie1.id ");
			sql.append("inner join producttype as pt1 on t1.product_type_id=pt1.id ");
			sql.append("order by t1.data_date,pt1.name,ie1.content ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", Objects.toString(ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue()), "dd/MM/yyyy", "yyyy-MM-dd"), ""));
			query.setParameter("td", Objects.toString(ToolTimeCustomer.convertStringPattern(
					Objects.toString(hToDate.getValue()), "dd/MM/yyyy", "yyyy-MM-dd"), ""));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("pt", Long.parseLong(Objects.toString(hProductTypeId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ie", Long.parseLong(Objects.toString(hIECategoriesId.getValue())));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				NhapXuatNgay item = new NhapXuatNgay();
				item.setData_date((Date) p[0]);
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[1])));
				item.setProduct_type_code(Objects.toString(p[2]));
				item.setProduct_type_name(Objects.toString(p[3]));
				item.setIe_categories_id(Long.parseLong(Objects.toString(p[4])));
				item.setIe_categories_code(Objects.toString(p[5]));
				item.setIe_categories_name(Objects.toString(p[6]));
				item.setImport_quantity(Double.parseDouble(Objects.toString(p[7], "0")));
				item.setExport_quantity(Double.parseDouble(Objects.toString(p[8], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportNhapXuatTheoNgay:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportBangKeSanPhamChuaGiao(String json, List<BangKeSanPhamChuaGiao> list) {
		int res = -1;
		try {
			/*
			 * {month:0,year:0,product_id:0,customer_id:0,car_type_id:0,car_id:0}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT t1.pdid, t1.masp,t1.tensp,sum(t1.sldonhang),sum(t1.slthucxuat)/t1.qd,t1.makh,t1.tenkh,t1.idkh ");
			sql.append("FROM ( ");
			sql.append("	SELECT p.id as pdid, p.product_code as masp, p.product_name as tensp, odt.box_quantity as sldonhang ,0 as slthucxuat, cus.customer_code as makh, cus.customer_name as tenkh, cus.id as idkh,p.specification as qd ");
			sql.append("	FROM orderdetail as odt ");
			sql.append("	inner join orderlix as od on odt.order_lix_id=od.id ");
			sql.append("	inner join customer as cus on od.customer_id=cus.id ");
			sql.append("	inner join product as p on odt.product_id=p.id ");
			sql.append("	where od.order_date >= :fd and od.order_date <=:td ");
			sql.append("	union all ");

			sql.append("	SELECT p.id as pdid, p.product_code as masp, p.product_name as tensp, (odtp.quantity/p.specification) as sldonhang ,0 as slthucxuat, cus.customer_code as makh, cus.customer_name as tenkh, cus.id as idkh,p.specification as qd ");
			sql.append("	FROM promotionorderdetail as odtp ");
			sql.append("	inner join orderdetail as odt on odtp.order_detail_id=odt.id ");
			sql.append("	inner join orderlix as od on odt.order_lix_id=od.id ");
			sql.append("	inner join customer as cus on od.customer_id=cus.id ");
			sql.append("	inner join product as p on odt.product_id=p.id ");
			sql.append("	where od.order_date >= :fd and od.order_date <=:td ");
			sql.append("	union all ");

			sql.append("	SELECT p.id as pdid, p.product_code as masp, p.product_name as tensp,0 as sldonhang,idt.real_quantity as slthucxuat, cus.customer_code as makh, cus.customer_name as tenkh, cus.id as idkh,p.specification as qd");
			sql.append("	FROM invoicedetail as idt ");
			sql.append("	inner join invoice as iv on idt.invoice_id=iv.id ");
			sql.append("	inner join customer as cus on iv.customer_id=cus.id ");
			sql.append("	inner join product as p on idt.product_id=p.id ");
			sql.append("	where iv.order_lix_id in (SELECT od2.id FROM orderlix as od2 where od2.order_date >= :fd and od2.order_date <=:td) ");
			sql.append(") as t1 ");
			// sql.append(" where  t1.pdid=4374 ");
			sql.append("group by t1.idkh, t1.pdid ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", hFromDate.getValue());
			query.setParameter("td", hToDate.getValue());
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				BangKeSanPhamChuaGiao item = new BangKeSanPhamChuaGiao();
				item.setMasp(Objects.toString(p[1], null));
				item.setTensp(Objects.toString(p[2], null));
				double soluongdonhang = Double.parseDouble(Objects.toString(p[3], "0"));
				if (soluongdonhang != 0) {
					double sothungchuagiao = Double.parseDouble(Objects.toString(p[3], "0"))
							- Double.parseDouble(Objects.toString(p[4], "0"));
					item.setSothungchuagiao(sothungchuagiao);
					item.setMakh(Objects.toString(p[5], null));
					item.setTenkh(Objects.toString(p[6], null));
					list.add(item);
				}
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportBangKeKhoiLuongTheoXe:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportBangKeKhoiLuongVanChuyen(String json, List<BangKeKhoiLuongVanChuyen> list) {
		int res = -1;
		try {
			/*
			 * {month:0,year:0,product_id:0,customer_id:0,car_type_id:0,car_id:0}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hMonth = JsonParserUtil.getValueNumber(j, "month", null);
			HolderParser hYear = JsonParserUtil.getValueNumber(j, "year", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			HolderParser hCarTypeId = JsonParserUtil.getValueNumber(j, "car_type_id", null);
			HolderParser hCarId = JsonParserUtil.getValueNumber(j, "car_id", null);
			int month = Integer.parseInt(Objects.toString(hMonth.getValue()));
			int year = Integer.parseInt(Objects.toString(hYear.getValue()));
			Date fromDate = null;
			Date toDate = null;
			if (month != 0 && year != 0) {
				fromDate = ToolTimeCustomer.getDateMinCustomer(month, year);
				toDate = ToolTimeCustomer.getDateMaxCustomer(month, year);
			}
			StringBuilder sql = new StringBuilder();
			sql.append("select d.car_id,cr.license_plate,cr.driver,dt.invoice_id,d.invoice_date,d.invoice_code,p.product_type_id,pt.code,pt.name,pt.typept, ");
			sql.append("d.customer_id,c.customer_code,c.customer_name,c.city_id,ct.city_code,ct.city_name,dt.quantity*p.factor as kg_quantity, crr.carrier_name ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join customer as c on d.customer_id=c.id ");
			sql.append("left join city as ct on c.city_id=ct.id ");
			sql.append("left join carrier as crr on d.carrier_id=crr.id ");
			sql.append("left join car as cr on d.car_id=cr.id ");
			sql.append("inner join product as p on dt.product_id=p.id ");
			sql.append("inner join producttype as pt on p.product_type_id=pt.id ");
			sql.append("where (:fd='' or d.invoice_date >= :fd) and (:td='' or d.invoice_date <=:td) and (:p=0 or dt.product_id=:p) and (:c=0 or d.customer_id=:c) and (:ct=0 or cr.car_type_id=:ct) and (:cr=0 or d.car_id=:cr) ");
			sql.append("order by cr.license_plate,d.id,c.customer_name ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", Objects.toString(ToolTimeCustomer.convertDateToString(fromDate, "yyyy-MM-dd"), ""));
			query.setParameter("td", Objects.toString(ToolTimeCustomer.convertDateToString(toDate, "yyyy-MM-dd"), ""));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hCarTypeId.getValue())));
			query.setParameter("cr", Long.parseLong(Objects.toString(hCarId.getValue())));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				BangKeKhoiLuongVanChuyen item = new BangKeKhoiLuongVanChuyen();
				item.setCar_id(Long.parseLong(Objects.toString(p[0], "0")));
				item.setLicense_plate(Objects.toString(p[1], null));
				item.setDriver(Objects.toString(p[2], null));
				item.setInvoice_id(Long.parseLong(Objects.toString(p[3])));
				item.setInvoice_date((Date) p[4]);
				item.setInvoice_code(Objects.toString(p[5]));
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[6], "0")));
				item.setProduct_type_code(Objects.toString(p[7]));
				item.setProduct_type_name(Objects.toString(p[8]));
				item.setTypept(Integer.parseInt(Objects.toString(p[9])));
				item.setCustomer_id(Long.parseLong(Objects.toString(p[10])));
				item.setCustomer_code(Objects.toString(p[11]));
				item.setCustomer_name(Objects.toString(p[12]));
				item.setCity_id(Long.parseLong(Objects.toString(p[13], "0")));
				item.setCity_code(Objects.toString(p[14], null));
				item.setCity_name(Objects.toString(p[15], null));
				item.setKg_quantity(Double.parseDouble(Objects.toString(p[16], "0")));
				item.setCarrier_name(Objects.toString(p[17], null));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportBangKeKhoiLuongTheoXe:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportBangKeKhoiLuongVanChuyenTheoKhachHang(String json, List<BangKeKhoiLuongVanChuyenTheoKhachHang> list) {
		int res = -1;
		try {
			/*
			 * {month:0,year:0,product_id:0,customer_id:0,car_type_id:0,car_id:0}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hMonth = JsonParserUtil.getValueNumber(j, "month", null);
			HolderParser hYear = JsonParserUtil.getValueNumber(j, "year", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			HolderParser hCarTypeId = JsonParserUtil.getValueNumber(j, "car_type_id", null);
			HolderParser hCarId = JsonParserUtil.getValueNumber(j, "car_id", null);
			int month = Integer.parseInt(Objects.toString(hMonth.getValue()));
			int year = Integer.parseInt(Objects.toString(hYear.getValue()));
			Date fromDate = null;
			Date toDate = null;
			if (month != 0 && year != 0) {
				fromDate = ToolTimeCustomer.getDateMinCustomer(month, year);
				toDate = ToolTimeCustomer.getDateMaxCustomer(month, year);
			}
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.customer_id,c1.customer_code,c1.customer_name,t1.typept,t1.kg_quantity from( ");
			sql.append("select d.customer_id,pt.typept,sum(dt.quantity*p.factor) as kg_quantity ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("left join car as cr on d.car_id=cr.id ");
			sql.append("inner join product as p on dt.product_id=p.id ");
			sql.append("inner join producttype as pt on p.product_type_id=pt.id ");
			sql.append("where (:fd='' or d.invoice_date >= :fd) and (:td='' or d.invoice_date <=:td) and (:p=0 or dt.product_id=:p) and (:c=0 or d.customer_id=:c) and (:ct=0 or cr.car_type_id=:ct) and (:cr=0 or d.car_id=:cr) ");
			sql.append("group by d.customer_id,pt.typept) as t1 ");
			sql.append("inner join customer as c1 on t1.customer_id=c1.id ");
			sql.append("order by c1.customer_name,t1.typept desc ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", Objects.toString(ToolTimeCustomer.convertDateToString(fromDate, "yyyy-MM-dd"), ""));
			query.setParameter("td", Objects.toString(ToolTimeCustomer.convertDateToString(toDate, "yyyy-MM-dd"), ""));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hCarTypeId.getValue())));
			query.setParameter("cr", Long.parseLong(Objects.toString(hCarId.getValue())));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				BangKeKhoiLuongVanChuyenTheoKhachHang item = new BangKeKhoiLuongVanChuyenTheoKhachHang();
				item.setCustomer_id(Long.parseLong(Objects.toString(p[0])));
				item.setCustomer_code(Objects.toString(p[1]));
				item.setCustomer_name(Objects.toString(p[2]));
				item.setTypept(Integer.parseInt(Objects.toString(p[3])));
				item.setKg_quantity(Double.parseDouble(Objects.toString(p[4], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportBangKeKhoiLuongTheoKhachHang:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int reportBangKeKhoiLuongVanChuyenTheoNVVC(String json, List<BangKeKhoiLuongVanChuyenTheoNVVC> list) {
		int res = 0;
		try {
			/*
			 * {month:0,year:0,product_id:0,customer_id:0,car_type_id:0,car_id:0}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hMonth = JsonParserUtil.getValueNumber(j, "month", null);
			HolderParser hYear = JsonParserUtil.getValueNumber(j, "year", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			HolderParser hCarTypeId = JsonParserUtil.getValueNumber(j, "car_type_id", null);
			HolderParser hCarId = JsonParserUtil.getValueNumber(j, "car_id", null);
			int month = Integer.parseInt(Objects.toString(hMonth.getValue()));
			int year = Integer.parseInt(Objects.toString(hYear.getValue()));
			Date fromDate = null;
			Date toDate = null;
			if (month != 0 && year != 0) {
				fromDate = ToolTimeCustomer.getDateMinCustomer(month, year);
				toDate = ToolTimeCustomer.getDateMaxCustomer(month, year);
			}
			StringBuilder sql = new StringBuilder();
			sql.append("select d1.car_id,cr1.license_plate,d1.invoice_date,d1.invoice_code,d1.customer_id,c1.customer_code,c1.customer_name, ");
			sql.append("d1.carrier_id,crr1.carrier_code,crr1.carrier_name,t1.kg_quantity ");
			sql.append("from(select dt.invoice_id,sum(dt.quantity*p.factor) as kg_quantity  ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("inner join product as p on dt.product_id=p.id ");
			sql.append("where (:fd='' or d.invoice_date >= :fd) and (:td='' or d.invoice_date <=:td) and (:p=0 or dt.product_id=:p) and (:c=0 or d.customer_id=:c) and (:cr=0 or d.car_id=:cr) ");
			sql.append("group by dt.invoice_id) as t1 ");
			sql.append("inner join invoice as d1 on t1.invoice_id=d1.id ");
			sql.append("inner join customer as c1 on d1.customer_id=c1.id ");
			sql.append("left join car as cr1 on d1.car_id=cr1.id ");
			sql.append("left join carrier as crr1 on d1.carrier_id=crr1.id ");
			sql.append("where (:ct=0 or cr1.car_type_id=:ct) ");
			sql.append("order by cr1.license_plate,c1.customer_name,d1.invoice_date ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", Objects.toString(ToolTimeCustomer.convertDateToString(fromDate, "yyyy-MM-dd"), ""));
			query.setParameter("td", Objects.toString(ToolTimeCustomer.convertDateToString(toDate, "yyyy-MM-dd"), ""));
			query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
			query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
			query.setParameter("ct", Long.parseLong(Objects.toString(hCarTypeId.getValue())));
			query.setParameter("cr", Long.parseLong(Objects.toString(hCarId.getValue())));
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				BangKeKhoiLuongVanChuyenTheoNVVC item = new BangKeKhoiLuongVanChuyenTheoNVVC();
				item.setCar_id(Long.parseLong(Objects.toString(p[0], "0")));
				item.setLicense_plate(Objects.toString(p[1], ""));
				item.setInvoice_date((Date) p[2]);
				item.setInvoice_code(Objects.toString(p[3]));
				item.setCustomer_id(Long.parseLong(Objects.toString(p[4])));
				item.setCustomer_code(Objects.toString(p[5]));
				item.setCustomer_name(Objects.toString(p[6]));
				item.setCarrier_id(Long.parseLong(Objects.toString(p[7], "0")));
				item.setCarrier_code(Objects.toString(p[8], ""));
				item.setCarrier_name(Objects.toString(p[9], ""));
				item.setKg_quantity(Double.parseDouble(Objects.toString(p[10], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportBangKeKhoiLuongTheoNVVC:" + e.getMessage(), e);
		}
		return res;
	}

	/**
	 * Phan loai san pham lever Ma lever khac null, ma lever la so, ma lever >=7
	 * ky tu NULLIF(p2.lever_code, '') IS NOT NULL AND p2.typep = false AND
	 * p2.lever_code REGEXP '^[0-9]+$' AND LENGTH(p2.lever_code)>=7"
	 */
	@Override
	public int reportBaoCaoLever(String json, List<BaoCaoLever> list) {
		int res = -1;
		try {
			/* {from_date:'',to_date:'product_id:0} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			String fromDate = Objects.toString(hFromDate.getValue(), null);
			String toDate = Objects.toString(hToDate.getValue(), null);
			Date fd = ToolTimeCustomer.convertStringToDate(fromDate, "dd/MM/yyyy");
			Date td = ToolTimeCustomer.convertStringToDate(toDate, "dd/MM/yyyy");
			if (fd != null && td != null) {
				fromDate = ToolTimeCustomer.convertDateToString(fd, "yyyy-MM-dd");
				toDate = ToolTimeCustomer.convertDateToString(td, "yyyy-MM-dd");
				int month = ToolTimeCustomer.getMonthM(fd);
				int year = ToolTimeCustomer.getYearM(fd);
				int monthOB = (month == 1 ? 12 : month - 1);// tháng tính lấy
															// tồn đầu
				int yearOB = (month == 1 ? year - 1 : year);// năm tính lấy tồn
															// đầu
				Date firstDateOfMonth = ToolTimeCustomer.getDateMinCustomer(month, year);
				String fromDateOB = "";// ngày tính tồn đầu kỳ theo ngày
				String toDateOB = "";// ngày tính tồn đầu kỳ theo ngày
				if (firstDateOfMonth.getTime() < fd.getTime()) {
					fromDateOB = ToolTimeCustomer.convertDateToString(firstDateOfMonth, "yyyy-MM-dd");
					toDateOB = fromDate;
				}
				StringBuilder sql = new StringBuilder();
				sql.append("select t2.product_id,p2.product_code,p2.product_name,p2.lever_code,p2.specification,p2.factor,pc2.product_brand_id,pb2.pbrand_code,pb2.pbrand_name,p2.product_com_id,pc2.pcom_code,pc2.pcom_name, ");
				sql.append("t2.unit_opening_balance,t2.unit_opening_balance/p2.specification as box_opening_balance,t2.unit_opening_balance*p2.factor as kg_opening_balance, ");
				sql.append("t2.unit_import_quantity, t2.unit_import_quantity/p2.specification as box_import_quantity,t2.unit_import_quantity*p2.factor as kg_import_quantity, ");
				sql.append("t2.unit_export_quantity,t2.unit_export_quantity/p2.specification as box_export_quantity,t2.unit_export_quantity*p2.factor as kg_export_quantity, ");
				sql.append("t2.unit_opening_balance+unit_import_quantity-unit_export_quantity as unit_closing_balance,(t2.unit_opening_balance+t2.unit_import_quantity-t2.unit_export_quantity)/p2.specification as box_closing_balance, ");
				sql.append("(t2.t2.unit_opening_balance+t2.unit_import_quantity-t2.unit_export_quantity)*p2.factor as kg_closing_balance ");
				sql.append("from(select t1.product_id,sum(t1.unit_opening_balance) as unit_opening_balance, sum(t1.unit_import_quantity) as unit_import_quantity,sum(t1.unit_export_quantity) as unit_export_quantity from ( ");
				sql.append("select iv.product_id,iv.closing_balance as unit_opening_balance, 0 as unit_import_quantity, 0 as unit_export_quantity ");
				sql.append("from inventory as iv where iv.inventory_month=:mth and iv.inventory_year=:yr ");
				sql.append("union all ");
				sql.append("select dtns.product_id,dtns.quantity as unit_opening_balance,0 as unit_import_quantity, 0 as unit_export_quantity ");
				sql.append("from goodsreceiptnotedetail as dtns ");
				sql.append("inner join goodsreceiptnote as dns on dtns.goods_receipt_note_id=dns.id ");
				sql.append("where dns.import_date >= :fdob and dns.import_date < :tdob ");
				sql.append("union all ");
				sql.append("select dtxs.product_id,-dtxs.quantity as unit_opening_balance,0 as unit_import_quantity, 0 as unit_export_quantity ");
				sql.append("from invoicedetail as dtxs ");
				sql.append("inner join invoice as dxs on dtxs.invoice_id=dxs.id ");
				sql.append("where dxs.invoice_date >= :fdob and dxs.invoice_date < :tdob AND dxs.ie_categories_id != :idmaxuatnhap ");
				sql.append("union all ");
				sql.append("select dtn.product_id,0 as unit_opening_balance,dtn.quantity as unit_import_quantity, 0 as unit_export_quantity ");
				sql.append("from goodsreceiptnotedetail as dtn ");
				sql.append("inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id ");
				sql.append("where  dn.import_date >= :fd and dn.import_date <= :td ");
				sql.append("union all ");
				sql.append("select dtx.product_id,0 as unit_opening_balance,0 as unit_import_quantity, dtx.quantity as unit_export_quantity ");
				sql.append("from invoicedetail as dtx ");
				sql.append("inner join invoice as dx on dtx.invoice_id=dx.id ");
				sql.append("where dx.invoice_date >= :fd and dx.invoice_date <= :td AND dx.ie_categories_id != :idmaxuatnhap ) as t1 ");
				sql.append("group by t1.product_id) as t2 ");
				sql.append("inner join product as p2 on t2.product_id=p2.id ");
				sql.append("left join productcom as pc2 on p2.product_com_id=pc2.id ");
				sql.append("left join productbrand as pb2 on pc2.product_brand_id=pb2.id ");
				sql.append("where  (:p=0 or t2.product_id=:p) AND NULLIF(p2.lever_code, '') IS NOT NULL AND p2.typep = false AND p2.lever_code REGEXP '^[0-9]+$' AND LENGTH(p2.lever_code)>=7");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("mth", monthOB);
				query.setParameter("yr", yearOB);
				query.setParameter("fdob", fromDateOB);
				query.setParameter("tdob", toDateOB);
				query.setParameter("fd", fromDate);
				query.setParameter("td", toDate);
				query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
				query.setParameter("idmaxuatnhap", 35);// ID: 35 là mã '0' (mã
														// nợ khuyến mãi)
				List<Object[]> listResult = query.getResultList();
				for (Object[] p : listResult) {
					BaoCaoLever item = new BaoCaoLever();
					item.setProduct_id(Long.parseLong(Objects.toString(p[0])));
					item.setProduct_code(Objects.toString(p[1]));
					item.setProduct_name(Objects.toString(p[2]));
					item.setLever_code(Objects.toString(p[3]));
					item.setSpecification(Double.parseDouble(Objects.toString(p[4])));
					item.setFactor(Double.parseDouble(Objects.toString(p[5])));
					item.setPbrand_id(Long.parseLong(Objects.toString(p[6], "0")));
					item.setPbrand_code(Objects.toString(p[7], null));
					item.setPbrand_name(Objects.toString(p[8], null));
					item.setPcom_id(Long.parseLong(Objects.toString(p[9], "0")));
					item.setPcom_code(Objects.toString(p[10], null));
					item.setPcom_name(Objects.toString(p[11], null));
					item.setUnit_opening_balance(Double.parseDouble(Objects.toString(p[12], "0")));
					item.setBox_opening_balance(Double.parseDouble(Objects.toString(p[13], "0")));
					item.setKg_opening_balance(Double.parseDouble(Objects.toString(p[14], "0")));
					item.setUnit_import_quantity(Double.parseDouble(Objects.toString(p[15], "0")));
					item.setBox_import_quantity(Double.parseDouble(Objects.toString(p[16], "0")));
					item.setKg_import_quantity(Double.parseDouble(Objects.toString(p[17], "0")));
					item.setUnit_export_quantity(Double.parseDouble(Objects.toString(p[18], "0")));
					item.setBox_export_quantity(Double.parseDouble(Objects.toString(p[19], "0")));
					item.setKg_export_quantity(Double.parseDouble(Objects.toString(p[20], "0")));
					item.setUnit_closing_balance(Double.parseDouble(Objects.toString(p[21], "0")));
					item.setBox_closing_balance(Double.parseDouble(Objects.toString(p[22], "0")));
					item.setKg_closing_balance(Double.parseDouble(Objects.toString(p[23], "0")));
					if (item.getKg_opening_balance() != 0 || item.getKg_import_quantity() != 0
							|| item.getKg_export_quantity() != 0 || item.getKg_closing_balance() != 0)
						list.add(item);
				}
				res = 0;
			}

		} catch (Exception e) {
			logger.error("ReportService.reportBaoCaoLever:" + e.getMessage(), e);
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lixco.com.interfaces.IReportService#reportLaySoLieuBaoCaoTongHop(java
	 * .lang.String, java.util.List)
	 */
	@Inject
	IProductService productService;

	@Inject
	IIECategoriesService ieCategoriesService;

	@Override
	public int reportInLuyKeXuatNhap(String json, List<LuyKeNhapXuat> list) {
		int res = -1;
		try {
			/*
			 * {month:0,year:0,product_id:0,customer_id:0,ie_categories_id:0,typep
			 * :-1}
			 */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hMonth = JsonParserUtil.getValueNumber(j, "month", null);
			HolderParser hYear = JsonParserUtil.getValueNumber(j, "year", null);
			HolderParser hProductId = JsonParserUtil.getValueNumber(j, "product_id", null);
			HolderParser hCustomerId = JsonParserUtil.getValueNumber(j, "customer_id", null);
			HolderParser hIeCategoriesId = JsonParserUtil.getValueNumber(j, "ie_categories_id", null);
			HolderParser hTypep = JsonParserUtil.getValueNumber(j, "typep", null);
			int month = Integer.parseInt(Objects.toString(hMonth.getValue()));
			int year = Integer.parseInt(Objects.toString(hYear.getValue()));
			if (month != 0 && year != 0) {
				Date fromDate = ToolTimeCustomer.getDateMinCustomer(month, year);
				Date toDate = ToolTimeCustomer.getDateMaxCustomer(month, year);
				StringBuilder sql = new StringBuilder();
				sql.append("select t2.product_type_id,pt2.code,pt2.name,t2.ie_categories_id,ie2.code as ie_categories_code,ie2.content as ie_categories_name,t2.import_quantity,t2.export_quantity,t2.lk_import,t2.lk_export from ( ");
				sql.append("select p1.product_type_id,t1.ie_categories_id,sum(t1.import_quantity) as import_quantity,sum(t1.export_quantity) as export_quantity,sum(t1.lk_import) as lk_import,sum(t1.lk_export) as lk_export ");
				sql.append("from(select dtn.product_id,dn.ie_categories_id,dtn.quantity as import_quantity, 0 as export_quantity,0 as lk_import,0 as lk_export ");
				sql.append("from goodsreceiptnotedetail as dtn ");
				sql.append("inner join goodsreceiptnote as dn on dtn.goods_receipt_note_id=dn.id ");
				sql.append("where dn.import_date >=:fd and dn.import_date <=:td and (:p=0 or dtn.product_id=:p) and (:c=0 or dn.customer_id=:c) and (:ie=0 or dn.ie_categories_id=:ie) ");
				sql.append("union all ");
				sql.append("select dtx.product_id,dx.ie_categories_id,0,dtx.quantity,0,0 from invoicedetail as dtx ");
				sql.append("inner join invoice as dx on dtx.invoice_id=dx.id ");
				sql.append("where dx.invoice_date >=:fd and dx.invoice_date <=:td and (:p=0 or dtx.product_id=:p) and (:c=0 or dx.customer_id=:c) and (:ie=0  or dx.ie_categories_id=:ie) ");
				sql.append("union all ");
				sql.append("select dtnlk.product_id,dnlk.ie_categories_id,0,0,dtnlk.quantity,0 from goodsreceiptnotedetail as dtnlk ");
				sql.append("inner join goodsreceiptnote as dnlk on dtnlk.goods_receipt_note_id=dnlk.id ");
				sql.append("where dnlk.import_date >=:fdk and dnlk.import_date <=:tdk and (:p=0 or dtnlk.product_id=:p) and (:c=0 or dnlk.customer_id=:c) and (:ie=0 or dnlk.ie_categories_id=:ie) ");
				sql.append("union all ");
				sql.append("select dtxlk.product_id,dxlk.ie_categories_id,0,0,0,dtxlk.quantity from invoicedetail as dtxlk ");
				sql.append("inner join invoice as dxlk on dtxlk.invoice_id=dxlk.id ");
				sql.append("where dxlk.invoice_date >=:fdk and dxlk.invoice_date <=:tdk and (:p=0 or dtxlk.product_id=:p) and (:c=0 or dxlk.customer_id=:c) and (:ie=0 or dxlk.ie_categories_id=:ie) ");
				sql.append(") as t1 ");
				sql.append("inner join product as p1 on t1.product_id= p1.id ");
				sql.append("where (:tp=-1 or p1.typep=:tp) ");
				sql.append("group by p1.product_type_id,t1.ie_categories_id ) as t2 ");
				sql.append("inner join producttype as pt2 on t2.product_type_id=pt2.id ");
				sql.append("inner join iecategories as ie2 on t2.ie_categories_id=ie2.id ");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("fd", ToolTimeCustomer.convertDateToString(fromDate, "yyyy-MM-dd"));
				query.setParameter("td", ToolTimeCustomer.convertDateToString(toDate, "yyyy-MM-dd"));
				query.setParameter("fdk", year + "-01-01");
				query.setParameter("tdk", ToolTimeCustomer.convertDateToString(toDate, "yyyy-MM-dd"));
				query.setParameter("p", Long.parseLong(Objects.toString(hProductId.getValue())));
				query.setParameter("c", Long.parseLong(Objects.toString(hCustomerId.getValue())));
				query.setParameter("ie", Long.parseLong(Objects.toString(hIeCategoriesId.getValue())));
				query.setParameter("tp", Integer.parseInt(Objects.toString(hTypep.getValue())));
				List<Object[]> listResult = new ArrayList<>();
				listResult.addAll(query.getResultList());
				for (Object[] p : listResult) {
					LuyKeNhapXuat item = new LuyKeNhapXuat();
					item.setProduct_type_id(Long.parseLong(Objects.toString(p[0])));
					item.setProduct_type_code(Objects.toString(p[1]));
					item.setProduct_type_name(Objects.toString(p[2]));
					item.setIe_categories_id(Long.parseLong(Objects.toString(p[3])));
					item.setIe_categories_code(Objects.toString(p[4]));
					item.setIe_categories_name(Objects.toString(p[5]));
					item.setImport_quantity(Double.parseDouble(Objects.toString(p[6], "0")));
					item.setExport_quantity(Double.parseDouble(Objects.toString(p[7], "0")));
					item.setLk_import(Double.parseDouble(Objects.toString(p[8], "0")));
					item.setLk_export(Double.parseDouble(Objects.toString(p[9], "0")));
					list.add(item);
				}
				res = 0;
			}

		} catch (Exception e) {
			logger.error("ReportService.reportInLuyKeXuatNhap:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public TonKhoDauThang reportTonKhoDauThang(int month, int year, long product_id) {
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("select iv.product_id,iv.opening_balance as kg_opening_balance,(iv.opening_balance/p.factor) as Unit_opening_balance,iv.closing_balance as closing_balance   from inventory as iv inner join product as p on iv.product_id=p.id where iv.inventory_month=:mth and iv.inventory_year=:yr and iv.inventory_month=:mth and iv.product_id=:p ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("mth", month);
			query.setParameter("yr", year);
			query.setParameter("p", Long.parseLong(Objects.toString(product_id)));
			List<Object[]> ps = query.setMaxResults(1).getResultList();
			if (ps.size() != 0) {
				TonKhoDauThang item = new TonKhoDauThang();
				item.setProduct_id(Long.parseLong(Objects.toString(ps.get(0)[0])));
				item.setKg_opening_balance(Double.parseDouble(Objects.toString(ps.get(0)[1], "0")));
				item.setUnit_opening_balance(Double.parseDouble(Objects.toString(ps.get(0)[2], "0")));
				item.setKg_inv_balance(Double.parseDouble(Objects.toString(ps.get(0)[3], "0")));
				return item;
			} else {
				return new TonKhoDauThang();
			}
		} catch (Exception e) {
			logger.error("ReportService.reportTonKhoThang:" + e.getMessage(), e);
		}
		return new TonKhoDauThang();
	}

	@Override
	public TonKhoDauThang reportTonKhoDauThangDVT(int month, int year, long product_id) {
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("select iv.product_id,iv.opening_balance as kg_opening_balance,(iv.opening_balance/p.factor) as Unit_opening_balance,(iv.closing_balance/p.factor) as closing_balance   from inventory as iv inner join product as p on iv.product_id=p.id where iv.inventory_month=:mth and iv.inventory_year=:yr and iv.inventory_month=:mth and iv.product_id=:p ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("mth", month);
			query.setParameter("yr", year);
			query.setParameter("p", Long.parseLong(Objects.toString(product_id)));
			List<Object[]> ps = query.setMaxResults(1).getResultList();
			if (ps.size() != 0) {
				TonKhoDauThang item = new TonKhoDauThang();
				item.setProduct_id(Long.parseLong(Objects.toString(ps.get(0)[0])));
				item.setKg_opening_balance(Double.parseDouble(Objects.toString(ps.get(0)[1], "0")));
				item.setUnit_opening_balance(Double.parseDouble(Objects.toString(ps.get(0)[2], "0")));
				item.setKg_inv_balance(Double.parseDouble(Objects.toString(ps.get(0)[3], "0")));
				return item;
			} else {
				return new TonKhoDauThang();
			}
		} catch (Exception e) {
			logger.error("ReportService.reportTonKhoThang:" + e.getMessage(), e);
		}
		return new TonKhoDauThang();
	}

	@Override
	public int reportBaoCaoHopGiaoBan(String json, List<BaoCaoHopGiaoBan> list) {
		int res = -1;
		try {
			/* {from_date:'',to_date:'product_id:0} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
			String fromDate = Objects.toString(hFromDate.getValue(), null);
			String toDate = Objects.toString(hToDate.getValue(), null);
			Date fd = ToolTimeCustomer.convertStringToDate(fromDate, "dd/MM/yyyy");
			Date td = ToolTimeCustomer.convertStringToDate(toDate, "dd/MM/yyyy");
			if (fd != null && td != null) {
				fromDate = ToolTimeCustomer.convertDateToString(fd, "yyyy-MM-dd");
				toDate = ToolTimeCustomer.convertDateToString(td, "yyyy-MM-dd");

				StringBuilder sql = new StringBuilder();
				sql.append("SELECT 'N' As xn, pt.code As manhomsp, pt.name As tennhomsp, iecat.code as maxn, iecat.content as tenxn, SUM(dtns.quantity * p.factor) As soluong ");
				sql.append("FROM goodsreceiptnotedetail as dtns ");
				sql.append("INNER JOIN goodsreceiptnote as dns on dtns.goods_receipt_note_id=dns.id ");
				sql.append("INNER JOIN IECategories as iecat on dns.ie_categories_id=iecat.id  ");
				sql.append("INNER JOIN product as p on dtns.product_id=p.id  ");
				sql.append("INNER JOIN productType as pt on p.product_type_id=pt.id  ");
				sql.append("WHERE dns.import_date >= :fd and dns.import_date <= :td and iecat.code in ('B','S') AND p.product_type_id !='O' ");
				sql.append("GROUP BY 1,2,3,4  ");
				sql.append("UNION ALL ");
				sql.append("SELECT 'X' As xn, pt.code As manhomsp, pt.name As tennhomsp, iecat.code as maxn, iecat.content as tenxn, SUM(dtxs.quantity * p.factor) As soluong  ");
				sql.append("FROM invoicedetail as dtxs ");
				sql.append("INNER JOIN invoice as dxs on dtxs.invoice_id=dxs.id ");
				sql.append("INNER JOIN customer as cus on dxs.customer_id= cus.id ");
				sql.append("INNER JOIN IECategories as iecat on dxs.ie_categories_id=iecat.id  ");
				sql.append("INNER JOIN product as p on dtxs.product_id=p.id  ");
				sql.append("INNER JOIN productType as pt on p.product_type_id=pt.id  ");
				sql.append("WHERE dxs.invoice_date >= :fd and dxs.invoice_date <= :td AND iecat.code NOT IN('Y','C','V','Z','O','2','3','U') AND pt.code NOT IN ('H','O') AND cus.customer_code NOT IN ('CO125','BL146','CO400') ");
				sql.append("GROUP BY 1,2,3,4  ");

				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("fd", fromDate);
				query.setParameter("td", toDate);
				List<Object[]> listResult = query.getResultList();

				StringBuilder sqldt = new StringBuilder();
				sqldt.append("SELECT SUM(dtxs.total) As doanhthu  ");
				sqldt.append("FROM invoicedetail as dtxs ");
				sqldt.append("INNER JOIN invoice as dxs on dtxs.invoice_id=dxs.id ");
				sqldt.append("WHERE dxs.invoice_date >= :fd and dxs.invoice_date <= :td ");
				Query querydt = em.createNativeQuery(sqldt.toString());
				querydt.setParameter("fd", fromDate);
				querydt.setParameter("td", toDate);
				double doanhthu = 0;
				try {
					Object dtobj = querydt.getSingleResult();
					doanhthu = Double.parseDouble(Objects.toString(dtobj));
				} catch (Exception e) {
				}
				for (Object[] p : listResult) {

					BaoCaoHopGiaoBan item = new BaoCaoHopGiaoBan();
					item.setNhapxuat(Objects.toString(p[0]));
					item.setManhom(Objects.toString(p[1]));
					item.setTennhom(Objects.toString(p[2]));
					item.setMaxuatnhap(Objects.toString(p[3]));
					item.setLoaixuatnhap(Objects.toString(p[4]));
					item.setSoluong(Double.parseDouble(Objects.toString(p[5])));
					item.setDoanhthu(doanhthu);
					list.add(item);
				}
				res = 0;
			}

		} catch (Exception e) {
			logger.error("ReportService.reportBaoCaoLever:" + e.getMessage(), e);
		}
		return res;
	}

	@Inject
	ProductKMService productKMService;

	@Override
	public int reportLaySoLieuBaoCaoTongHop(String json, List<SoLieuBaoCaoTongHop> list, boolean tachcombo) {
		int res = 0;
		/*
		 * Bổ sung thêm mã ")" ngày 12/01/2024 (chị Gấm)
		 */
		String[] laycacmaxn = { "1", "E", "X", "T", "8", "V", "Q", "A", "5", "P", "&", "$", "M", "%", "^", "R", "+",
				"-", ":", "(", ")" };
		List<String> xns = Arrays.asList(laycacmaxn);
		try {

			/* {from_date:'',to_date:'',company_code:'TD'} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
			HolderParser hCompanyCode = JsonParserUtil.getValueString(j, "company_code", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select a.area_code,a.area_name,cy.city_code,cy.city_name,c.customer_code,c.customer_name,ct.code as customer_type_code,ct.name as customer_type_name, ");
			sql.append("pt.code as product_type_code,pt.name as product_type_name,pt.typept,p.product_code,p.product_name,dt.quantity,dt.total as total_amount, ");
			sql.append("d.invoice_date,ie.code as ie_categories_code,ie.content as ie_categories_name,p.typep,p.factor,p.specification, ");
			sql.append("cc.name as customer_channel_name,pc.pcom_code,pc.pcom_name,pb.pbrand_code,pbrand_name, p.maspchinh,dt.total_foreign_amount as total_amount_nt, pri.id as iddongia, cont.id as idhopdong, d.invoice_date as ngayhd, ord.order_date as ngaydh, p.id as idsp,cont.voucher_code as sohd, ");
			sql.append("country.country_name, ");
			sql.append("cgr.name ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("LEFT join PricingProgram as pri on d.pricing_program_id=pri.id ");
			sql.append("LEFT join Contract as cont on d.contract_id=cont.id ");
			sql.append("LEFT join orderlix as ord on d.idorderlix=ord.id ");
			sql.append("inner join product as p on dt.product_id=p.id ");
			sql.append("left join productcom as pc on p.product_com_id=pc.id ");
			sql.append("left join productbrand as pb on pc.product_brand_id=pb.id ");
			sql.append("left join producttype as pt on p.product_type_id=pt.id ");
			sql.append("left join iecategories as ie on d.ie_categories_id=ie.id ");
			sql.append("left join customer as c on d.customer_id=c.id ");
			sql.append("left join customertypes as ct on c.customer_types_id=ct.id ");
			sql.append("left join customergroup as cgr on ct.customerGroup_id=cgr.id ");
			sql.append("left join city as cy on c.city_id=cy.id ");
			sql.append("left join country as country on cy.country_id=country.id ");
			sql.append("left join area as a on cy.area_id=a.id ");
			sql.append("left join customerchannel as cc on ct.channal_id=cc.id ");
			sql.append("where (:fd='' or d.invoice_date>=:fd) and (:td='' or d.invoice_date<=:td) AND ie.code in :iecodes");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", Objects.toString(ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"), ""));
			query.setParameter("td", Objects.toString(ToolTimeCustomer.convertStringPattern(
					Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"), ""));
			query.setParameter("iecodes", xns);
			List<Object[]> listResult = query.getResultList();
			List<IECategories> ieCategories = ieCategoriesService.findAll();

			for (Object[] p : listResult) {

				SoLieuBaoCaoTongHop item = new SoLieuBaoCaoTongHop();
				item.setArea_code(Objects.toString(p[0], null));
				item.setArea_name(Objects.toString(p[1], null));
				item.setCity_code(Objects.toString(p[2], null));
				item.setCity_name(Objects.toString(p[3], null));
				item.setCustomer_code(Objects.toString(p[4], null));
				item.setCustomer_name(Objects.toString(p[5], null));
				item.setCustomer_type_code(Objects.toString(p[6], null));
				item.setCustomer_type_name(Objects.toString(p[7], null));
				item.setProduct_type_code(Objects.toString(p[8], null));
				item.setProduct_type_name(Objects.toString(p[9], null));
				item.setTypept(Integer.parseInt(Objects.toString(p[10], "0")));
				item.setProduct_code(Objects.toString(p[11], null));
				item.setProduct_name(Objects.toString(p[12], null));
				item.setQuantity(Double.parseDouble(Objects.toString(p[13], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[14], "0")));
				Date invoiceDate = (Date) p[15];
				int month = ToolTimeCustomer.getMonthM(invoiceDate);
				int year = ToolTimeCustomer.getYearM(invoiceDate);
				String monthYear = ToolTimeCustomer.convertDateToString(invoiceDate, "MM/yyyy");
				item.setMonth_and_year(monthYear);
				item.setIe_categories_code(Objects.toString(p[16], null));
				item.setIe_categories_name(Objects.toString(p[17], null));
				item.setTypep(Boolean.parseBoolean(Objects.toString(p[18], "0")));
				item.setFactor(Double.parseDouble(Objects.toString(p[19], "0")));
				item.setSpecification(Double.parseDouble(Objects.toString(p[20], "0")));
				item.setCustomer_channel_name(Objects.toString(p[21], null));
				item.setPcom_code(Objects.toString(p[22], null));
				item.setPcom_name(Objects.toString(p[23], null));
				item.setPbrand_code(Objects.toString(p[24], null));
				item.setPbrand_name(Objects.toString(p[25], null));
				item.setYear(year);
				item.setMaspchinh(Objects.toString(p[26], null));
				item.setTotal_amount_nt(Double.parseDouble(Objects.toString(p[27], "0")));
				item.setIdctdongia(Long.parseLong(Objects.toString(p[28], "0")));
				item.setIdhopdong(Long.parseLong(Objects.toString(p[29], "0")));
				item.setNgayhoadon((Date) p[30]);
				item.setNgaydonhang((Date) p[31]);
				item.setIdsp(Long.parseLong(Objects.toString(p[32], "0")));
				item.setSohd(Objects.toString(p[33], ""));
				item.setQuocgia(Objects.toString(p[34], ""));
				item.setNhomkhachhang(Objects.toString(p[35], ""));

				if (month != -1) {
					if (month <= 3) {
						item.setQuarter("Quí 01/" + year);
					} else if (month <= 6 && month >= 4) {
						item.setQuarter("Quí 02/" + year);
					} else if (month >= 7 && month <= 9) {
						item.setQuarter("Quí 03/" + year);
					} else {
						item.setQuarter("Qui 04/" + year);
					}
				}
				String companyCode = Objects.toString(hCompanyCode.getValue(), "HO CHI MINH");
				switch (companyCode) {
				case "HO CHI MINH":
					item.setTd_quantity(item.getQuantity());
					item.setTd_total_amount(item.getTotal_amount());
					item.setBd_quantity(0.0);
					item.setBd_total_amount(0.0);
					item.setBn_quantity(0.0);
					item.setBn_total_amount(0.0);
					break;
				case "BINH DUONG":
					item.setTd_quantity(0.0);
					item.setTd_total_amount(0.0);
					item.setBd_quantity(item.getQuantity());
					item.setBd_total_amount(item.getTotal_amount());
					item.setBn_quantity(0.0);
					item.setBn_total_amount(0.0);
					break;
				case "BAC NINH":
					item.setTd_quantity(0.0);
					item.setTd_quantity(0.0);
					item.setBd_quantity(0.0);
					item.setBd_total_amount(0.0);
					item.setBn_quantity(item.getQuantity());
					item.setBn_total_amount(item.getTotal_amount());
					break;
				}
				if (tachcombo) {
					List<ProductKM> productKMs = productKMService.findByCodeProductMain(item.getProduct_code());
					/*
					 * Kiểm tra nếu trong danh sách sản phẩm khuyến mãi có sản
					 * phẩm chính mới đem tách combo
					 */
					// if (productKMs.size() == 0) {
					// System.out.println();
					// }
					if (productKMs.size() != 0) {
						boolean cosanphamchinh = false;
						for (int i = 0; i < productKMs.size(); i++) {
							if (productKMs.get(i).isSpchinh()) {
								cosanphamchinh = true;
								break;
							}
						}
						if (cosanphamchinh) {
							String maspcombo = new String(item.getProduct_code());
							double slcombo = new Double(item.getQuantity());
							for (int i = 0; i < productKMs.size(); i++) {
								if (productKMs.get(i).isSpchinh()) {
									Product pd = productKMs.get(i).getPromotion_product();
									if (pd != null) {
										if (pd.getProduct_type() != null) {
											item.setProduct_type_code(pd.getProduct_type().getCode());
											item.setProduct_type_name(pd.getProduct_type().getName());
											item.setTypept(pd.getProduct_type().getTypept());
										} else {
											item.setProduct_type_code("");
											item.setProduct_type_name("");
											item.setTypept(0);
										}
										item.setProduct_code(pd.getProduct_code());
										item.setProduct_name(pd.getProduct_name());
										item.setQuantity(slcombo * productKMs.get(i).getQuantity());
										item.setFactor(pd.getFactor());
										item.setSpecification(pd.getSpecification());
										if (pd.getProduct_com() != null) {
											item.setPcom_code(pd.getProduct_com().getPcom_code());
											item.setPcom_name(pd.getProduct_com().getPcom_name());
											if (pd.getProduct_com().getProduct_brand() != null) {
												item.setPbrand_code(pd.getProduct_com().getProduct_brand()
														.getPbrand_code());
												item.setPbrand_name(pd.getProduct_com().getProduct_brand()
														.getPbrand_name());
											} else {
												item.setPbrand_code("");
												item.setPbrand_name("");
											}
										} else {
											item.setPcom_code("");
											item.setPcom_name("");
											item.setPbrand_code("");
											item.setPbrand_name("");
										}
										item.setMaspcombo(maspcombo);
										switch (companyCode) {
										case "HO CHI MINH":
											item.setTd_quantity(item.getQuantity());
											item.setTd_total_amount(item.getTotal_amount());
											item.setBd_quantity(0.0);
											item.setBd_total_amount(0.0);
											item.setBn_quantity(0.0);
											item.setBn_total_amount(0.0);
											break;
										case "BINH DUONG":
											item.setTd_quantity(0.0);
											item.setTd_total_amount(0.0);
											item.setBd_quantity(item.getQuantity());
											item.setBd_total_amount(item.getTotal_amount());
											item.setBn_quantity(0.0);
											item.setBn_total_amount(0.0);
											break;
										case "BAC NINH":
											item.setTd_quantity(0.0);
											item.setTd_quantity(0.0);
											item.setBd_quantity(0.0);
											item.setBd_total_amount(0.0);
											item.setBn_quantity(item.getQuantity());
											item.setBn_total_amount(item.getTotal_amount());
											break;
										}
										// SP chinhs
										list.add(item);

									}
								} else {
									// SP khuyen mai
									Product pdp = productKMs.get(i).getPromotion_product();
									if (pdp != null) {
										SoLieuBaoCaoTongHop item2 = (SoLieuBaoCaoTongHop) item.clone();
										if (pdp.getProduct_type() != null) {
											item2.setProduct_type_code(pdp.getProduct_type().getCode());
											item2.setProduct_type_name(pdp.getProduct_type().getName());
											item2.setTypept(pdp.getProduct_type().getTypept());
										} else {
											item2.setProduct_type_code("");
											item2.setProduct_type_name("");
											item2.setTypept(0);
										}
										item2.setProduct_code(pdp.getProduct_code());
										item2.setProduct_name(pdp.getProduct_name());
										item2.setQuantity(slcombo * productKMs.get(i).getQuantity());
										item2.setFactor(pdp.getFactor());
										item2.setSpecification(pdp.getSpecification());
										item2.setTotal_amount(0.0);
										String ieCateChuyendoi = item.getIe_categories_code();
										/*
										 * Cập nhật lại ngày 28/10/2023 (Thúy
										 * KD, chị Gấm) $ -> &; P -> P; M -> M
										 */
										if ("V".equals(ieCateChuyendoi)) {
											ieCateChuyendoi = "Q";
										} else if ("E".equals(ieCateChuyendoi)) {
											ieCateChuyendoi = "5";
										} else if ("-".equals(ieCateChuyendoi)) {
											ieCateChuyendoi = "+";
										} else if ("$".equals(ieCateChuyendoi)) {
											ieCateChuyendoi = "&";
										} else if ("R".equals(ieCateChuyendoi)) {
											ieCateChuyendoi = "$";
										} else if ("1".equals(ieCateChuyendoi)) {
											ieCateChuyendoi = "(";
										} else if ("P".equals(ieCateChuyendoi)) {
											ieCateChuyendoi = "P";
										} else if ("M".equals(ieCateChuyendoi)) {
											ieCateChuyendoi = "M";
										} else {
											ieCateChuyendoi = "A";
										}
										IECategories ieCategory = null;
										for (int k = 0; k < ieCategories.size(); k++) {
											if (ieCateChuyendoi.equals(ieCategories.get(k).getCode())) {
												ieCategory = ieCategories.get(k);
												break;

											}
										}

										if (ieCategories != null) {
											item2.setIe_categories_code(ieCategory.getCode());
											item2.setIe_categories_name(ieCategory.getContent());
										} else {
											item2.setIe_categories_code("");
											item2.setIe_categories_name("");
										}

										if (pdp.getProduct_com() != null) {
											item2.setPcom_code(pdp.getProduct_com().getPcom_code());
											item2.setPcom_name(pdp.getProduct_com().getPcom_name());
											if (pdp.getProduct_com().getProduct_brand() != null) {
												item2.setPbrand_code(pdp.getProduct_com().getProduct_brand()
														.getPbrand_code());
												item2.setPbrand_name(pdp.getProduct_com().getProduct_brand()
														.getPbrand_name());
											} else {
												item2.setPbrand_code("");
												item2.setPbrand_name("");
											}
										} else {
											item2.setPcom_code("");
											item2.setPcom_name("");
											item2.setPbrand_code("");
											item2.setPbrand_name("");
										}
										item2.setMaspcombo(maspcombo);
										switch (companyCode) {
										case "HO CHI MINH":
											item2.setTd_quantity(item2.getQuantity());
											item2.setTd_total_amount(item2.getTotal_amount());
											item2.setBd_quantity(0.0);
											item2.setBd_total_amount(0.0);
											item2.setBn_quantity(0.0);
											item2.setBn_total_amount(0.0);
											break;
										case "BINH DUONG":
											item2.setTd_quantity(0.0);
											item2.setTd_total_amount(0.0);
											item2.setBd_quantity(item2.getQuantity());
											item2.setBd_total_amount(item2.getTotal_amount());
											item2.setBn_quantity(0.0);
											item2.setBn_total_amount(0.0);
											break;
										case "BAC NINH":
											item2.setTd_quantity(0.0);
											item2.setTd_quantity(0.0);
											item2.setBd_quantity(0.0);
											item2.setBd_total_amount(0.0);
											item2.setBn_quantity(item2.getQuantity());
											item2.setBn_total_amount(item2.getTotal_amount());
											break;
										}
										list.add(item2);
									}
								}
							}
						} else {
							// khong co san pham chinh
							list.add(item);
						}
					} else {
						// khong co danh sach khuyen mai
						list.add(item);
					}
				} else {
					// Khong tach combo
					list.add(item);
				}
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportLaySoLieuBaoCaoTongHop:" + e.getMessage(), e);
		}
		return res;
	}

	// San luong ban hang
	@Override
	public List<Object[]> reportLaySoLieuBaoCaoTongHop2(String json) {
		String[] laycacmaxn = { "1", "E", "X", "T", "8", "V", "Q", "A", "5", "P", "&", "$", "M", "%", "^", "R", "+",
				"-", ":", "(", ")" };
		List<String> xns = Arrays.asList(laycacmaxn);
		try {
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT main.phanloai, CASE ");
			sql.append("WHEN main.kenhkh_moi ='KÊNH MT' THEN  ");
			sql.append("CASE ");
			sql.append("WHEN main.nhomkh  = 'ONLINE' OR main.nhomkh  = 'HORECA' OR main.nhomkh  ='SIÊU THỊ LIX' THEN 'MT' ");
			sql.append("ELSE  ");
			sql.append("CASE ");
			sql.append("WHEN main.nhomkh ='SIÊU THỊ OEM' THEN 'OB-MT' ");
			sql.append("ELSE main.nhomkh ");
			sql.append("END ");
			sql.append("END ");
			sql.append("ELSE  ");
			sql.append("CASE ");
			sql.append("WHEN main.kenhkh_moi ='KÊNH GT' THEN 'GT' ");
			sql.append("ELSE  ");
			sql.append("CASE ");
			sql.append("WHEN main.kenhkh_moi ='KÊNH XUẤT KHẨU' THEN 'XK' ");
			sql.append("ELSE main.kenhkh_moi ");
			sql.append("END ");
			sql.append("END ");
			sql.append("END AS kenhmoi, ");
			sql.append("sum(main.quantity) ");
			sql.append("FROM ( ");
			sql.append("SELECT  ");
			sql.append("ie.code AS ie_categories_code, ");
			sql.append("CASE ");
			sql.append("WHEN EXISTS (SELECT 1 FROM productkm AS subpkm WHERE subpkm.product_id = p.id AND subpkm.spchinh = TRUE) THEN ");
			sql.append("CASE ptkm.typept WHEN 1 THEN 'BỘT GIẶT' ELSE 'NTRL' END ");
			sql.append("ELSE ");
			sql.append("CASE pt.typept WHEN 1 THEN 'BỘT GIẶT' ELSE 'NTRL' END ");
			sql.append("END AS phanloai, ");
			sql.append("CASE ");
			sql.append("WHEN EXISTS (SELECT 1 FROM productkm AS subpkm WHERE subpkm.product_id = p.id AND subpkm.spchinh = TRUE) THEN pmkm.product_code ");
			sql.append("ELSE p.product_code ");
			sql.append("END AS masp, ");
			sql.append("CASE ");
			sql.append("WHEN EXISTS (SELECT 1 FROM productkm AS subpkm WHERE subpkm.product_id = p.id AND subpkm.spchinh = TRUE) THEN pmkm.product_name ");
			sql.append("ELSE p.product_name ");
			sql.append("END AS tensp, ");
			sql.append("ROUND(( ");
			sql.append("CASE ");
			sql.append("WHEN EXISTS (SELECT 1 FROM productkm AS subpkm WHERE subpkm.product_id = p.id AND subpkm.spchinh = TRUE) THEN dt.quantity * pm.quantity ");
			sql.append("ELSE dt.quantity ");
			sql.append("END ");
			sql.append(") * CASE ");
			sql.append("WHEN EXISTS (SELECT 1 FROM productkm AS subpkm WHERE subpkm.product_id = p.id AND subpkm.spchinh = TRUE) THEN pmkm.factor ");
			sql.append("ELSE p.factor ");
			sql.append(" END, 2) AS quantity, ");
			sql.append("CASE ");
			sql.append("WHEN EXISTS (SELECT 1 FROM productkm AS subpkm WHERE subpkm.product_id = p.id AND subpkm.spchinh = TRUE) THEN ");
			sql.append("CASE pmkm.typep WHEN 1 THEN 'XUAT KHAU' ELSE 'NOI DIA' END ");
			sql.append("ELSE ");
			sql.append("CASE p.typep WHEN 1 THEN 'XUAT KHAU' ELSE 'NOI DIA' END ");
			sql.append("END AS xuatkhaunoidia, ");
			sql.append("CASE ");
			sql.append("WHEN EXISTS (SELECT 1 FROM productkm AS subpkm WHERE subpkm.product_id = p.id AND subpkm.spchinh = TRUE) THEN pmkm.specification ");
			sql.append("ELSE p.specification ");
			sql.append("END AS specification, ");
			sql.append("cc.name AS kenhkhachhang, ");
			sql.append("cgr.name AS nhomkhachhang, ");
			sql.append("CASE ");
			sql.append("WHEN cc.name IS NOT NULL THEN ");
			sql.append("CASE ");
			sql.append("WHEN cc.name IN ('KÊNH HORECA', 'KÊNH ONLINE', 'KÊNH SIÊU THỊ') AND ie.code != 'P' THEN 'KÊNH MT' ");
			sql.append("ELSE cc.name ");
			sql.append("END ");
			sql.append("ELSE NULL ");
			sql.append("END AS kenhkh_moi, ");
			sql.append("CASE ");
			sql.append("WHEN cc.name = 'KÊNH SIÊU THỊ' THEN ");
			sql.append("CASE ");
			sql.append("WHEN ie.code IN ('E', '5', '%', ')','$','&') THEN 'SIÊU THỊ LIX' ");
			sql.append("WHEN ie.code IN ('1', '(', '^') THEN 'SIÊU THỊ OEM' ");
			sql.append("ELSE ct.name ");
			sql.append("END ");
			sql.append("WHEN cc.name = 'KÊNH XUẤT KHẨU' THEN ct.name ");
			sql.append("ELSE cgr.name ");
			sql.append("END AS nhomkh, ");
			sql.append("CASE ");
			sql.append("WHEN EXISTS (SELECT 1 FROM productkm AS subpkm WHERE subpkm.product_id = p.id AND subpkm.spchinh = TRUE) THEN pmkm.id ");
			sql.append("ELSE p.id ");
			sql.append("END AS idsp, ");
			sql.append("CASE pm.spchinh WHEN 0 THEN 1 ELSE 0 END AS spkm ");
			sql.append("FROM invoicedetail AS dt ");
			sql.append("INNER JOIN invoice AS d ON dt.invoice_id = d.id ");
			sql.append("INNER JOIN product AS p ON dt.product_id = p.id ");
			sql.append("LEFT JOIN productcom AS pc ON p.product_com_id = pc.id ");
			sql.append("LEFT JOIN productbrand AS pb ON pc.product_brand_id = pb.id ");
			sql.append("LEFT JOIN producttype AS pt ON p.product_type_id = pt.id ");
			sql.append("LEFT JOIN customer AS c ON d.customer_id = c.id ");
			sql.append("LEFT JOIN customertypes AS ct ON c.customer_types_id = ct.id ");
			sql.append("LEFT JOIN customergroup AS cgr ON ct.customerGroup_id = cgr.id ");
			sql.append("LEFT JOIN city AS cy ON c.city_id = cy.id ");
			sql.append("LEFT JOIN country AS country ON cy.country_id = country.id ");
			sql.append("LEFT JOIN customerchannel AS cc ON ct.channal_id = cc.id ");
			sql.append("LEFT JOIN productkm AS pm ON pm.product_id = p.id ");
			sql.append("LEFT JOIN product AS pmkm ON pm.promotion_product_id = pmkm.id ");
			sql.append("LEFT JOIN productcom AS pckm ON pmkm.product_com_id = pckm.id ");
			sql.append("LEFT JOIN productbrand AS pbkm ON pckm.product_brand_id = pbkm.id ");
			sql.append("LEFT JOIN producttype AS ptkm ON pmkm.product_type_id = ptkm.id ");
			sql.append("LEFT JOIN iecategories AS ie ON d.ie_categories_id = ie.id ");
			sql.append("WHERE ('' = :fd OR d.invoice_date >= :fd) ");
			sql.append("AND ('' = :td OR d.invoice_date <= :td) ");
			sql.append("AND ie.code IN :iecodes ");
			sql.append("GROUP BY dt.id, masp, quantity) as main ");

			sql.append("group by main.phanloai, kenhmoi order by main.phanloai, kenhmoi ");

			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", Objects.toString(ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"), ""));
			query.setParameter("td", Objects.toString(ToolTimeCustomer.convertStringPattern(
					Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"), ""));
			query.setParameter("iecodes", xns);
			List<Object[]> listResult = query.getResultList();
			return listResult;


		} catch (Exception e) {
			logger.error("ReportService.reportLaySoLieuBaoCaoTongHop:" + e.getMessage(), e);
		}
		return new ArrayList<Object[]>();
	}

	@Override
	public int reportLaySoLieuBaoCaoTongHopcu(String json, List<SoLieuBaoCaoTongHop> list, boolean tachcombo) {
		int res = 0;
		String[] laycacmaxn = { "1", "E", "X", "T", "8", "V", "Q", "A", "5", "P", "&", "$", "M", "%", "^", "R", "+",
				"-", ":", "(" };
		List<String> xns = Arrays.asList(laycacmaxn);
		try {
			/* {from_date:'',to_date:'',company_code:'TD'} */
			JsonObject j = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(j, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(j, "to_date", null);
			HolderParser hCompanyCode = JsonParserUtil.getValueString(j, "company_code", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select a.area_code,a.area_name,cy.city_code,cy.city_name,c.customer_code,c.customer_name,ct.code as customer_type_code,ct.name as customer_type_name, ");
			sql.append("pt.code as product_type_code,pt.name as product_type_name,pt.typept,p.product_code,p.product_name,dt.quantity,dt.total as total_amount, ");
			sql.append("d.invoice_date,ie.code as ie_categories_code,ie.content as ie_categories_name,p.typep,p.factor,p.specification, ");
			sql.append("cc.name as customer_channel_name,pc.pcom_code,pc.pcom_name,pb.pbrand_code,pbrand_name, p.maspchinh,dt.total_foreign_amount as total_amount_nt, pri.id as iddongia, cont.id as idhopdong, d.invoice_date as ngayhd, ord.order_date as ngaydh, p.id as idsp ");
			sql.append("from invoicedetail as dt ");
			sql.append("inner join invoice as d on dt.invoice_id=d.id ");
			sql.append("LEFT join PricingProgram as pri on d.pricing_program_id=pri.id ");
			sql.append("LEFT join Contract as cont on d.contract_id=cont.id ");
			sql.append("LEFT join orderlix as ord on d.idorderlix=ord.id ");
			sql.append("inner join product as p on dt.product_id=p.id ");
			sql.append("left join productcom as pc on p.product_com_id=pc.id ");
			sql.append("left join productbrand as pb on pc.product_brand_id=pb.id ");
			sql.append("left join producttype as pt on p.product_type_id=pt.id ");
			sql.append("left join iecategories as ie on d.ie_categories_id=ie.id ");
			sql.append("left join customer as c on d.customer_id=c.id ");
			sql.append("left join customertypes as ct on c.customer_types_id=ct.id ");
			sql.append("left join city as cy on c.city_id=cy.id ");
			sql.append("left join area as a on cy.area_id=a.id ");
			sql.append("left join customerchannel as cc on ct.channal_id=cc.id ");
			sql.append("where (:fd='' or d.invoice_date>=:fd) and (:td='' or d.invoice_date<=:td) AND ie.code in :iecodes");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", Objects.toString(ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"), ""));
			query.setParameter("td", Objects.toString(ToolTimeCustomer.convertStringPattern(
					Objects.toString(hToDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"), ""));
			query.setParameter("iecodes", xns);
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				SoLieuBaoCaoTongHop item = new SoLieuBaoCaoTongHop();
				item.setArea_code(Objects.toString(p[0], null));
				item.setArea_name(Objects.toString(p[1], null));
				item.setCity_code(Objects.toString(p[2], null));
				item.setCity_name(Objects.toString(p[3], null));
				item.setCustomer_code(Objects.toString(p[4], null));
				item.setCustomer_name(Objects.toString(p[5], null));
				item.setCustomer_type_code(Objects.toString(p[6], null));
				item.setCustomer_type_name(Objects.toString(p[7], null));
				item.setProduct_type_code(Objects.toString(p[8], null));
				item.setProduct_type_name(Objects.toString(p[9], null));
				item.setTypept(Integer.parseInt(Objects.toString(p[10], "0")));
				item.setProduct_code(Objects.toString(p[11], null));
				item.setProduct_name(Objects.toString(p[12], null));
				item.setQuantity(Double.parseDouble(Objects.toString(p[13], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[14], "0")));
				Date invoiceDate = (Date) p[15];
				int month = ToolTimeCustomer.getMonthM(invoiceDate);
				int year = ToolTimeCustomer.getYearM(invoiceDate);
				String monthYear = ToolTimeCustomer.convertDateToString(invoiceDate, "MM/yyyy");
				item.setMonth_and_year(monthYear);
				item.setIe_categories_code(Objects.toString(p[16], null));
				item.setIe_categories_name(Objects.toString(p[17], null));
				item.setTypep(Boolean.parseBoolean(Objects.toString(p[18], "0")));
				item.setFactor(Double.parseDouble(Objects.toString(p[19], "0")));
				item.setSpecification(Double.parseDouble(Objects.toString(p[20], "0")));
				item.setCustomer_channel_name(Objects.toString(p[21], null));
				item.setPcom_code(Objects.toString(p[22], null));
				item.setPcom_name(Objects.toString(p[23], null));
				item.setPbrand_code(Objects.toString(p[24], null));
				item.setPbrand_name(Objects.toString(p[25], null));
				item.setYear(year);
				item.setMaspchinh(Objects.toString(p[26], null));
				item.setTotal_amount_nt(Double.parseDouble(Objects.toString(p[27], "0")));
				item.setIdctdongia(Long.parseLong(Objects.toString(p[28], "0")));
				item.setIdhopdong(Long.parseLong(Objects.toString(p[29], "0")));
				item.setNgayhoadon((Date) p[30]);
				item.setNgaydonhang((Date) p[31]);
				item.setIdsp(Long.parseLong(Objects.toString(p[32], "0")));

				if (month != -1) {
					if (month <= 3) {
						item.setQuarter("Quí 01/" + year);
					} else if (month <= 6 && month >= 4) {
						item.setQuarter("Quí 02/" + year);
					} else if (month >= 7 && month <= 9) {
						item.setQuarter("Quí 03/" + year);
					} else {
						item.setQuarter("Qui 04/" + year);
					}
				}
				String companyCode = Objects.toString(hCompanyCode.getValue(), "HO CHI MINH");
				switch (companyCode) {
				case "HO CHI MINH":
					item.setTd_quantity(item.getQuantity());
					item.setTd_total_amount(item.getTotal_amount());
					item.setBd_quantity(0.0);
					item.setBd_total_amount(0.0);
					item.setBn_quantity(0.0);
					item.setBn_total_amount(0.0);
					break;
				case "BINH DUONG":
					item.setTd_quantity(0.0);
					item.setTd_total_amount(0.0);
					item.setBd_quantity(item.getQuantity());
					item.setBd_total_amount(item.getTotal_amount());
					item.setBn_quantity(0.0);
					item.setBn_total_amount(0.0);
					break;
				case "BAC NINH":
					item.setTd_quantity(0.0);
					item.setTd_quantity(0.0);
					item.setBd_quantity(0.0);
					item.setBd_total_amount(0.0);
					item.setBn_quantity(item.getQuantity());
					item.setBn_total_amount(item.getTotal_amount());
					break;
				}
				if (tachcombo && item.getMaspchinh() != null && !"".equals(item.getMaspchinh())) {
					double hesoqdcombo = new Double(item.getFactor());
					String maspcombo = new String(item.getProduct_code());
					double slcombo = new Double(item.getQuantity());

					Product pd = productService.selectByCodeAll(item.getMaspchinh());
					if (pd != null) {
						if (pd.getProduct_type() != null) {
							item.setProduct_type_code(pd.getProduct_type().getCode());
							item.setProduct_type_name(pd.getProduct_type().getName());
							item.setTypept(pd.getProduct_type().getTypept());
						} else {
							item.setProduct_type_code("");
							item.setProduct_type_name("");
							item.setTypept(0);
						}
						item.setProduct_code(pd.getProduct_code());
						item.setProduct_name(pd.getProduct_name());
						item.setQuantity(slcombo);
						item.setFactor(pd.getFactor());
						item.setSpecification(pd.getSpecification());
						if (pd.getProduct_com() != null) {
							item.setPcom_code(pd.getProduct_com().getPcom_code());
							item.setPcom_name(pd.getProduct_com().getPcom_name());
							if (pd.getProduct_com().getProduct_brand() != null) {
								item.setPbrand_code(pd.getProduct_com().getProduct_brand().getPbrand_code());
								item.setPbrand_name(pd.getProduct_com().getProduct_brand().getPbrand_name());
							} else {
								item.setPbrand_code("");
								item.setPbrand_name("");
							}
						} else {
							item.setPcom_code("");
							item.setPcom_name("");
							item.setPbrand_code("");
							item.setPbrand_name("");
						}
						item.setMaspcombo(maspcombo);
						switch (companyCode) {
						case "HO CHI MINH":
							item.setTd_quantity(item.getQuantity());
							item.setTd_total_amount(item.getTotal_amount());
							item.setBd_quantity(0.0);
							item.setBd_total_amount(0.0);
							item.setBn_quantity(0.0);
							item.setBn_total_amount(0.0);
							break;
						case "BINH DUONG":
							item.setTd_quantity(0.0);
							item.setTd_total_amount(0.0);
							item.setBd_quantity(item.getQuantity());
							item.setBd_total_amount(item.getTotal_amount());
							item.setBn_quantity(0.0);
							item.setBn_total_amount(0.0);
							break;
						case "BAC NINH":
							item.setTd_quantity(0.0);
							item.setTd_quantity(0.0);
							item.setBd_quantity(0.0);
							item.setBd_total_amount(0.0);
							item.setBn_quantity(item.getQuantity());
							item.setBn_total_amount(item.getTotal_amount());
							break;
						}
						// SP chinhs
						list.add(item);
						// SP khuyen mai
						Product pdcb = productService.selectByCodeAll(maspcombo);
						if (pdcb.getPromotion_product() != null) {
							Product pdp = pd.getPromotion_product();

							if (pdp != null) {
								pdp = productService.selectByCodeAll(pdp.getProduct_code());
								SoLieuBaoCaoTongHop item2 = (SoLieuBaoCaoTongHop) item.clone();
								if (pdp.getProduct_type() != null) {
									item2.setProduct_type_code(pdp.getProduct_type().getCode());
									item2.setProduct_type_name(pdp.getProduct_type().getName());
									item2.setTypept(pdp.getProduct_type().getTypept());
								} else {
									item2.setProduct_type_code("");
									item2.setProduct_type_name("");
									item2.setTypept(0);
								}
								item2.setProduct_code(pdp.getProduct_code());
								item2.setProduct_name(pdp.getProduct_name());
								item2.setQuantity(slcombo);
								item2.setFactor(pdp.getFactor());
								item2.setSpecification(pdp.getSpecification());
								item2.setTotal_amount(0.0);
								String ieCateChuyendoi = item.getIe_categories_code();
								if ("V".equals(ieCateChuyendoi)) {
									ieCateChuyendoi = "Q";
								} else if ("E".equals(ieCateChuyendoi)) {
									ieCateChuyendoi = "5";
								} else if ("-".equals(ieCateChuyendoi)) {
									ieCateChuyendoi = "+";
								} else if ("$".equals(ieCateChuyendoi) || "R".equals(ieCateChuyendoi)) {
									ieCateChuyendoi = "$";
								} else if ("1".equals(ieCateChuyendoi)) {
									ieCateChuyendoi = "(";
								} else {
									ieCateChuyendoi = "A";
								}

								IECategories ieCategories = ieCategoriesService.selectByCodeAll(ieCateChuyendoi);
								if (ieCategories != null) {
									item2.setIe_categories_code(ieCategories.getCode());
									item2.setIe_categories_name(ieCategories.getContent());
								} else {
									item2.setIe_categories_code("");
									item2.setIe_categories_name("");
								}

								if (pdp.getProduct_com() != null) {
									item2.setPcom_code(pdp.getProduct_com().getPcom_code());
									item2.setPcom_name(pdp.getProduct_com().getPcom_name());
									if (pdp.getProduct_com().getProduct_brand() != null) {
										item2.setPbrand_code(pdp.getProduct_com().getProduct_brand().getPbrand_code());
										item2.setPbrand_name(pdp.getProduct_com().getProduct_brand().getPbrand_name());
									} else {
										item2.setPbrand_code("");
										item2.setPbrand_name("");
									}
								} else {
									item2.setPcom_code("");
									item2.setPcom_name("");
									item2.setPbrand_code("");
									item2.setPbrand_name("");
								}
								item2.setMaspcombo(maspcombo);
								switch (companyCode) {
								case "HO CHI MINH":
									item2.setTd_quantity(item2.getQuantity());
									item2.setTd_total_amount(item2.getTotal_amount());
									item2.setBd_quantity(0.0);
									item2.setBd_total_amount(0.0);
									item2.setBn_quantity(0.0);
									item2.setBn_total_amount(0.0);
									break;
								case "BINH DUONG":
									item2.setTd_quantity(0.0);
									item2.setTd_total_amount(0.0);
									item2.setBd_quantity(item2.getQuantity());
									item2.setBd_total_amount(item2.getTotal_amount());
									item2.setBn_quantity(0.0);
									item2.setBn_total_amount(0.0);
									break;
								case "BAC NINH":
									item2.setTd_quantity(0.0);
									item2.setTd_quantity(0.0);
									item2.setBd_quantity(0.0);
									item2.setBd_total_amount(0.0);
									item2.setBn_quantity(item2.getQuantity());
									item2.setBn_total_amount(item2.getTotal_amount());
									break;
								}
								list.add(item2);
							}
						}
					} else {
						list.add(item);
					}
				} else {
					list.add(item);
				}
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportLaySoLieuBaoCaoTongHop:" + e.getMessage(), e);
		}
		return res;
	}
}
