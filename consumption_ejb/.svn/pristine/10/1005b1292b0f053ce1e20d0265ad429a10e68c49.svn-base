package lixco.com.report_service;

import java.util.ArrayList;
import java.util.Date;
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
import lixco.com.commom_ejb.ToolTimeCustomer;
import lixco.com.entity.Customer;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.Product;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.IReportKHService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.BangKeChiTietHoaDon;
import lixco.com.reportInfo.BangKeHoaDon;
import lixco.com.reportInfo.BangKeKhoiLuongVanChuyen;
import lixco.com.reportInfo.BangKeKhoiLuongVanChuyenTheoKhachHang;
import lixco.com.reportInfo.BangKeKhoiLuongVanChuyenTheoNVVC;
import lixco.com.reportInfo.BaoCaoHopGiaoBan;
import lixco.com.reportInfo.BaoCaoLever;
import lixco.com.reportInfo.ChiTietCongNo;
import lixco.com.reportInfo.CongNo;
import lixco.com.reportInfo.CongNoToiHanThanhToan;
import lixco.com.reportInfo.DoanhThuKhachHangTheoThang;
import lixco.com.reportInfo.SoDuCongNo;
import lixco.com.reportInfo.TongKetTheoSanPham;
import lixco.com.reportInfo.LuyKeNhapXuat;
import lixco.com.reportInfo.NhapXuatNgay;
import lixco.com.reportInfo.SoLieuBaoCaoTongHop;
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

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class ReportKHService implements IReportKHService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;

	// select
	// t1.product_type_id,pt1.name,t1.product_brand_id,pb1.pbrand_name,pc1.pcom_code,pc1.pcom_name,t1.quantity,t1.total_amount,t1.total_tax_amount,t1.total_amount+t1.total_tax_amount
	// as total_amount_with_vat
	// from
	// (select
	// p.product_type_id,pc.product_brand_id,pc.id,sum(ROUND((dt.quantity*p.factor),0))
	// as quantity, sum(ROUND((dt.quantity*dt.unit_price),0)) as
	// total_amount,sum(dt.quantity*dt.unit_price*d.tax_value) as
	// total_tax_amount
	// from invoicedetail as dt
	// inner join invoice as d on dt.invoice_id=d.id
	// inner join product as p on dt. product_id=p.id
	// inner join productcom as pc on p.product_com_id=pc.id
	// where ('2022-06-01' = '' or d.invoice_date >= '2022-06-01') and
	// ('2022-06-30' = '' or d.invoice_date <= '2022-06-30') and
	// d.customer_id=581
	// group by p.product_type_id,pc.product_brand_id,pc.id)
	// as t1
	//
	// inner join producttype as pt1 on t1.product_type_id=pt1.id
	// inner join productbrand as pb1 on t1.product_brand_id=pb1.id
	// inner join productcom as pc1 on t1.id=pc1.id
	@Override
	public int inthongketheosanpham(String json, List<TongKetTheoSanPham> list, List<Customer> customers) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,product_type_id:0,customer_id:0,contract_id:0,ie_categories_id:0,typep:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductType = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hProductBrand = JsonParserUtil.getValueNumber(data, "product_brand_id", null);
			HolderParser hIEcategories = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);

			StringBuilder sql = new StringBuilder();

			sql.append("select t1.product_type_id,pt1.name,t1.product_brand_id,pb1.pbrand_name,pc1.pcom_code,pc1.pcom_name,t1.quantity,t1.total_amount,t1.total_tax_amount,t1.total_amount+t1.total_tax_amount as total_amount_with_vat ");
			sql.append("from ");
			sql.append("	(select p.product_type_id,pc.product_brand_id,pc.id,sum(dt.quantity*p.factor) as quantity, sum(ROUND((dt.quantity*dt.unit_price),0)) as total_amount,sum(ROUND((dt.quantity*dt.unit_price*d.tax_value),0)) as total_tax_amount ");
			sql.append("	from invoicedetail as dt  ");
			sql.append("	inner join invoice as d on dt.invoice_id=d.id  ");
			sql.append("	inner join product as p on dt. product_id=p.id  ");
			sql.append("	inner join productcom as pc on p.product_com_id=pc.id   ");
			sql.append("	inner join productbrand as pb on pc.product_brand_id=pb.id  ");
			sql.append("	inner join producttype as pt on p.product_type_id=pt.id  ");
			sql.append("	where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:pdt=0 or pt.id=:pdt) and (:pdb=0 or pb.id=:pdb) and (:ieid=0 or d.ie_categories_id=:ieid) ");
			if (customers.size() != 0)
				sql.append(" and d.customer_id in :cusid ");
			sql.append("	group by p.product_type_id,pc.product_brand_id,pc.id)  ");
			sql.append("as t1  ");
			sql.append("inner join producttype as pt1 on t1.product_type_id=pt1.id  ");
			sql.append("inner join productbrand as pb1 on t1.product_brand_id=pb1.id  ");
			sql.append("inner join productcom as pc1 on t1.id=pc1.id  order by pt1.name,pc1.pcom_name");

			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("pdt", Long.parseLong(Objects.toString(hProductType.getValue())));
			query.setParameter("pdb", Long.parseLong(Objects.toString(hProductBrand.getValue())));
			query.setParameter("ieid", Long.parseLong(Objects.toString(hIEcategories.getValue())));
			List<Long> idCus = new ArrayList<>();
			for (int i = 0; i < customers.size(); i++) {
				idCus.add(customers.get(i).getId());
			}
			if (customers.size() != 0)
				query.setParameter("cusid", idCus);
			List<Object[]> listResult = query.getResultList();
			// (long product_type_id, String product_type_name, long
			// product_id,String product_name, Double quantity, Double
			// box_quantity, Double total_amount, Double total_tax_amount,Double
			// total_amount_with_vat)
			for (Object[] p : listResult) {
				TongKetTheoSanPham item = new TongKetTheoSanPham();
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[0])));
				item.setProduct_type_name(Objects.toString(p[1], null));
				item.setProduct_brand_id(Long.parseLong(Objects.toString(p[2])));
				item.setProduct_brand_name(Objects.toString(p[3]));
				item.setProduct_com_code(Objects.toString(p[4]));
				item.setProduct_com_name(Objects.toString(p[5]));
				item.setQuantity(Double.parseDouble(Objects.toString(p[6], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[7], "0")));
				item.setTotal_tax_amount(Double.parseDouble(Objects.toString(p[8], "0")));
				item.setTotal_amount_with_vat(Double.parseDouble(Objects.toString(p[9], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int inthongketheosanpham2(String json, List<TongKetTheoSanPham> list, List<Customer> customers) {
		int res = -1;
		try {
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductType = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hProductBrand = JsonParserUtil.getValueNumber(data, "product_brand_id", null);
			HolderParser hIEcategories = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);

			StringBuilder sql = new StringBuilder();

			sql.append("select t1.product_type_id,pt1.name,t1.product_brand_id,pb1.pbrand_name,pc1.pcom_code,pc1.pcom_name,t1.quantity,t1.total_amount,t1.total_tax_amount,t1.total_amount+t1.total_tax_amount as total_amount_with_vat, t1.content ");
			sql.append("from ");
			sql.append("	(select p.product_type_id,pc.product_brand_id,pc.id,sum(dt.quantity*p.factor) as quantity, sum(ROUND((dt.quantity*dt.unit_price),0)) as total_amount,sum(ROUND((dt.quantity*dt.unit_price*d.tax_value),0)) as total_tax_amount, iec.content ");
			sql.append("	from invoicedetail as dt  ");
			sql.append("	left join invoice as d on dt.invoice_id=d.id  ");
			sql.append("	left join IECategories as iec on d.ie_categories_id=iec.id  ");
			sql.append("	left join product as p on dt. product_id=p.id  ");
			sql.append("	left join productcom as pc on p.product_com_id=pc.id   ");
			sql.append("	left join productbrand as pb on pc.product_brand_id=pb.id  ");
			sql.append("	left join producttype as pt on p.product_type_id=pt.id  ");
			sql.append("	where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:pdt=0 or pt.id=:pdt) and (:pdb=0 or pb.id=:pdb) and (:ieid=0 or d.ie_categories_id=:ieid) ");
			if (customers.size() != 0)
				sql.append(" and d.customer_id in :cusid ");
			sql.append("	group by p.product_type_id,pc.product_brand_id,pc.id,iec.id)  ");
			sql.append("as t1  ");
			sql.append("left join producttype as pt1 on t1.product_type_id=pt1.id  ");
			sql.append("left join productbrand as pb1 on t1.product_brand_id=pb1.id  ");
			sql.append("left join productcom as pc1 on t1.id=pc1.id  order by pt1.name,pc1.pcom_name,t1.content");

			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("pdt", Long.parseLong(Objects.toString(hProductType.getValue())));
			query.setParameter("pdb", Long.parseLong(Objects.toString(hProductBrand.getValue())));
			query.setParameter("ieid", Long.parseLong(Objects.toString(hIEcategories.getValue())));
			List<Long> idCus = new ArrayList<>();
			for (int i = 0; i < customers.size(); i++) {
				idCus.add(customers.get(i).getId());
			}
			if (customers.size() != 0)
				query.setParameter("cusid", idCus);
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				TongKetTheoSanPham item = new TongKetTheoSanPham();
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[0])));
				item.setProduct_type_name(Objects.toString(p[1], null));
				item.setProduct_brand_id(Long.parseLong(Objects.toString(p[2])));
				item.setProduct_brand_name(Objects.toString(p[3]));
				item.setProduct_com_code(Objects.toString(p[4]));
				item.setProduct_com_name(Objects.toString(p[5]));
				item.setQuantity(Double.parseDouble(Objects.toString(p[6], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[7], "0")));
				item.setTotal_tax_amount(Double.parseDouble(Objects.toString(p[8], "0")));
				item.setTotal_amount_with_vat(Double.parseDouble(Objects.toString(p[9], "0")));
				item.setIe_categories_name(Objects.toString(p[10]));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetXuatTheoSanPhamTongKetTieuThuLoai1:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int inthongketheokhachhangdvt(String json, List<TongKetTieuThuKhachHang> list, List<Customer> customers) {
		int res = -1;
		try {
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductType = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hProductBrand = JsonParserUtil.getValueNumber(data, "product_brand_id", null);
			HolderParser hIEcategories = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.customer_id,c1.customer_code,c1.customer_name,pt1.id,pt1.name,t1.product_id,p1.product_code,p1.product_name,t1.quantity,t1.quantity/p1.specification as box_quantity,t1.unit_price, ");
			sql.append("t1.total_amount,t1.tax_value ");
			sql.append("from customer as c1 ");
			sql.append("inner join( ");
			sql.append("	select d.customer_id,dt.product_id,dt.unit_price,sum(dt.quantity) as quantity, sum(dt.quantity*dt.unit_price) as total_amount, ");
			sql.append("	sum(d.tax_value)/count(dt.id) as tax_value ");
			sql.append("	from invoicedetail as dt ");
			sql.append("	left join invoice as d on dt.invoice_id=d.id  ");
			sql.append("	left join IECategories as iec on d.ie_categories_id=iec.id  ");
			sql.append("	left join product as p on dt. product_id=p.id  ");
			sql.append("	left join productcom as pc on p.product_com_id=pc.id   ");
			sql.append("	left join productbrand as pb on pc.product_brand_id=pb.id  ");
			sql.append("	left join producttype as pt on p.product_type_id=pt.id  ");
			sql.append("	where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:pdt=0 or pt.id=:pdt) and (:pdb=0 or pb.id=:pdb) and (:ieid=0 or d.ie_categories_id=:ieid) ");
			sql.append(" 	and iec.code not in :excodes ");

			if (customers.size() != 0)
				sql.append(" and d.customer_id in :cusid ");
			sql.append("	group by d.customer_id,dt.product_id,dt.unit_price) ");

			sql.append("as t1 on t1.customer_id=c1.id ");
			sql.append("inner join product as p1 on t1.product_id=p1.id ");
			sql.append("inner join producttype as pt1 on p1.product_type_id=pt1.id ");
			sql.append("order by c1.customer_name,pt1.name, p1.product_name, t1.quantity desc ");

			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("pdt", Long.parseLong(Objects.toString(hProductType.getValue())));
			query.setParameter("pdb", Long.parseLong(Objects.toString(hProductBrand.getValue())));
			query.setParameter("ieid", Long.parseLong(Objects.toString(hIEcategories.getValue())));
			List<Long> idCus = new ArrayList<>();
			for (int i = 0; i < customers.size(); i++) {
				idCus.add(customers.get(i).getId());
			}
			if (customers.size() != 0)
				query.setParameter("cusid", idCus);
			List<String> excodes = new ArrayList<>();
			excodes.add("Y");
			excodes.add("C");
			excodes.add("M");// xuat hang trung bày
			query.setParameter("excodes", excodes);
			List<Object[]> listResult = query.getResultList();
			for (int i = 0; i < listResult.size(); i++) {
				Object[] p = listResult.get(i);
				TongKetTieuThuKhachHang item = new TongKetTieuThuKhachHang();
				item.setCustomer_id(Long.parseLong(Objects.toString(p[0])));
				item.setCustomer_code(Objects.toString(p[1]));
				item.setCustomer_name(Objects.toString(p[2]));
				item.setProduct_type_id(Long.parseLong(Objects.toString(p[3])));
				item.setProduct_type_name(Objects.toString(p[4]));
				item.setProduct_id(Long.parseLong(Objects.toString(p[5])));
				String code = Objects.toString(p[6]);
				if (i > 0 && Objects.toString(listResult.get(i - 1)[6]).equals(code)) {
					item.setProduct_code("");
					item.setProduct_name("");
				} else {
					item.setProduct_code(Objects.toString(p[6]));
					item.setProduct_name(Objects.toString(p[7]));
				}
				item.setProduct_code_temp(Objects.toString(p[6]));
				item.setProduct_name_temp(Objects.toString(p[7]));
				item.setQuantity(Double.parseDouble(Objects.toString(p[8], "0")));
				item.setBox_quantity(Double.parseDouble(Objects.toString(p[9], "0")));
				item.setUnit_price(Double.parseDouble(Objects.toString(p[10], "0")));
				item.setTotal_amount(Double.parseDouble(Objects.toString(p[11], "0")));
				item.setTax_value(Double.parseDouble(Objects.toString(p[12], "0")));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportTongKetTieuThuKhachHangDVT:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int bangkehoadon(String json, List<BangKeHoaDon> list, List<Customer> customers) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_id:0,customer_id:0,contract_id:0,ie_categories_id:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductType = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hProductBrand = JsonParserUtil.getValueNumber(data, "product_brand_id", null);
			HolderParser hIEcategories = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.invoice_id,d1.voucher_code,d1.invoice_code,d1.invoice_date,d1.customer_id,c1.customer_code,c1.customer_name,c1.address,c1.company_name,d1.ie_categories_id,ie1.code,ie1.content, ");
			sql.append("d1.tongtien,d1.thue as total_tax_amount,(d1.tongtien + d1.thue) as  total_amount_with_vat,d1.created_by_id,d1.created_by,d1.po_no ");
			sql.append("from( select dt.invoice_id,sum(dt.quantity*dt.unit_price) as total_amount from invoicedetail as dt ");
			sql.append("	left join invoice as d on dt.invoice_id=d.id ");
			sql.append("	left join product as p on dt. product_id=p.id ");
			sql.append("	left join productcom as pc on p.product_com_id=pc.id   ");
			sql.append("	left join productbrand as pb on pc.product_brand_id=pb.id  ");
			sql.append("	left join producttype as pt on p.product_type_id=pt.id  ");
			sql.append("	where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:pdt=0 or pt.id=:pdt) and (:pdb=0 or pb.id=:pdb) and (:ieid=0 or d.ie_categories_id=:ieid) ");
			if (customers.size() != 0)
				sql.append(" and d.customer_id in :cusid ");
			sql.append("group by dt.invoice_id) as t1 ");
			sql.append("left join invoice as d1 on t1.invoice_id=d1.id ");
			sql.append("left join iecategories as ie1 on d1.ie_categories_id=ie1.id ");
			sql.append("left join customer as c1 on d1.customer_id=c1.id order by d1.invoice_date,d1.voucher_code ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("pdt", Long.parseLong(Objects.toString(hProductType.getValue())));
			query.setParameter("pdb", Long.parseLong(Objects.toString(hProductBrand.getValue())));
			query.setParameter("ieid", Long.parseLong(Objects.toString(hIEcategories.getValue())));
			List<Long> idCus = new ArrayList<>();
			for (int i = 0; i < customers.size(); i++) {
				idCus.add(customers.get(i).getId());
			}
			if (customers.size() != 0)
				query.setParameter("cusid", idCus);
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
				item.setTotal_amount((double)p[12]);
				item.setTotal_tax_amount((double)p[13]);
				item.setTotal_amount_with_vat((double)p[14]);
				item.setCreated_by_id(Long.parseLong(Objects.toString(p[15])));
				item.setCreated_by(Objects.toString(p[16]));
				item.setPo(Objects.toString(p[17]));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportBangKeHoaDon:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int congno(String json, List<CongNo> list, List<Customer> customers) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_type_id:0,customer_id:0,ie_categories_id:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductType = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hProductBrand = JsonParserUtil.getValueNumber(data, "product_brand_id", null);
			HolderParser hIEcategories = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select t1.customer_id,c1.customer_code,c1.customer_name,c1.city_id,ct1.city_code,ct1.city_name,t1.total_amount ");
			sql.append("from( select d.customer_id,sum(dt.total) as total_amount ");
			sql.append("	from invoicedetail as dt ");
			sql.append("	left join invoice as d on dt.invoice_id=d.id ");
			sql.append("	left join IECategories as iec on d.ie_categories_id=iec.id  ");
			sql.append("	left join product as p on dt.product_id=p.id ");
			sql.append("	left join productcom as pc on p.product_com_id=pc.id   ");
			sql.append("	left join productbrand as pb on pc.product_brand_id=pb.id  ");
			sql.append("	left join producttype as pt on p.product_type_id=pt.id  ");
			sql.append("	where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:pdt=0 or pt.id=:pdt) and (:pdb=0 or pb.id=:pdb) and (:ieid=0 or d.ie_categories_id=:ieid) ");
			sql.append(" 	and iec.code not in :excodes ");
			if (customers.size() != 0)
				sql.append(" and d.customer_id in :cusid ");
			sql.append("	group by d.customer_id) as t1 ");
			sql.append("left join customer as c1 on t1.customer_id=c1.id ");
			sql.append("left join city as ct1 on c1.city_id=ct1.id ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("pdt", Long.parseLong(Objects.toString(hProductType.getValue())));
			query.setParameter("pdb", Long.parseLong(Objects.toString(hProductBrand.getValue())));
			query.setParameter("ieid", Long.parseLong(Objects.toString(hIEcategories.getValue())));
			List<Long> idCus = new ArrayList<>();
			for (int i = 0; i < customers.size(); i++) {
				idCus.add(customers.get(i).getId());
			}
			if (customers.size() != 0)
				query.setParameter("cusid", idCus);

			List<String> excodes = new ArrayList<>();
			excodes.add("Y");// noi bo
			excodes.add("C");// xuat khac
			excodes.add("M");// xuat hang trung bày
			query.setParameter("excodes", excodes);

			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				CongNo item = new CongNo();
				item.setCustomer_id(Long.parseLong(Objects.toString(p[0])));
				item.setCustomer_code(Objects.toString(p[1]));
				item.setCustomer_name(Objects.toString(p[2]));
				item.setCity_id(Long.parseLong(Objects.toString(p[3])));
				item.setCity_code(Objects.toString(p[4]));
				item.setCity_name(Objects.toString(p[5]));
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
	public int chitietcongno(String json, List<ChiTietCongNo> list, List<Customer> customers) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_type_id:0,customer_id:0,ie_categories_id:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductType = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hProductBrand = JsonParserUtil.getValueNumber(data, "product_brand_id", null);
			HolderParser hIEcategories = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select d.customer_id,c.customer_code,c.customer_name,dt.invoice_id,d.invoice_date,d.invoice_code,d.voucher_code,d.ie_categories_id,iec.code,iec.content,dt.product_id,p.product_code,p.product_name, ");
			sql.append("dt.quantity*p.factor,dt.unit_price,dt.quantity*dt.unit_price as total_amount,d.car_id,ca.license_plate ");
			sql.append("	from invoicedetail as dt ");
			sql.append("	left join invoice as d on dt.invoice_id=d.id ");
			sql.append("	left join IECategories as iec on d.ie_categories_id=iec.id  ");
			sql.append("	left join product as p on dt.product_id=p.id ");
			sql.append("	left join productcom as pc on p.product_com_id=pc.id   ");
			sql.append("	left join productbrand as pb on pc.product_brand_id=pb.id  ");
			sql.append("	left join producttype as pt on p.product_type_id=pt.id  ");
			sql.append("	left join customer as c on d.customer_id=c.id ");
			sql.append("	left join car as ca on d.car_id=ca.id ");

			sql.append("	where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:pdt=0 or pt.id=:pdt) and (:pdb=0 or pb.id=:pdb) and (:ieid=0 or d.ie_categories_id=:ieid) ");
			sql.append(" 	and iec.code not in :excodes ");
			if (customers.size() != 0)
				sql.append(" and d.customer_id in :cusid ");

			sql.append("order by c.customer_name,d.invoice_date,d.voucher_code,p.product_name ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("pdt", Long.parseLong(Objects.toString(hProductType.getValue())));
			query.setParameter("pdb", Long.parseLong(Objects.toString(hProductBrand.getValue())));
			query.setParameter("ieid", Long.parseLong(Objects.toString(hIEcategories.getValue())));
			List<Long> idCus = new ArrayList<>();
			for (int i = 0; i < customers.size(); i++) {
				idCus.add(customers.get(i).getId());
			}
			if (customers.size() != 0)
				query.setParameter("cusid", idCus);

			List<String> excodes = new ArrayList<>();
			excodes.add("Y");
			excodes.add("C");
			excodes.add("M");// xuat hang trung bày
			query.setParameter("excodes", excodes);
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
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
	public int congnotoihanthanhtoan(String json, List<CongNoToiHanThanhToan> list, List<Customer> customers) {
		int res = -1;
		try {
			/*
			 * {from_date:
			 * '',to_date:'',product_type_id:0,customer_id:0,ie_categories_id:0}
			 */
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hnow = JsonParserUtil.getValueString(data, "now", null);
			HolderParser hProductType = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hProductBrand = JsonParserUtil.getValueNumber(data, "product_brand_id", null);
			HolderParser hIEcategories = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();

			sql.append("SELECT c.customer_code,c.customer_name,iec.content,od.voucher_code as voucher_code_dh,d.voucher_code as voucher_code_iv,d.invoice_date, ");
			sql.append("CASE WHEN c.days_debt_quantity=0 or DATE_ADD(d.invoice_date, INTERVAL c.days_debt_quantity DAY)>:now THEN sum((dt.total)+(dt.total*d.tax_value)) END as chuatoihan, ");
			sql.append("CASE WHEN DATE_ADD(d.invoice_date, INTERVAL c.days_debt_quantity DAY)=:now THEN sum((dt.total)+(dt.total*d.tax_value)) END as toihan, ");
			sql.append("CASE WHEN DATE_ADD(d.invoice_date, INTERVAL c.days_debt_quantity DAY)<:now THEN sum((dt.total)+(dt.total*d.tax_value)) END as quahan,d.payment,iec.code,d.po_no ");
			sql.append("FROM invoicedetail as dt ");

			sql.append("left join invoice as d on dt.invoice_id=d.id ");
			sql.append("left join IECategories as iec on d.ie_categories_id=iec.id  ");
			sql.append("left join OrderLix as od on d.order_lix_id=od.id  ");
			sql.append("left join product as p on dt.product_id=p.id ");
			sql.append("left join productcom as pc on p.product_com_id=pc.id   ");
			sql.append("left join productbrand as pb on pc.product_brand_id=pb.id  ");
			sql.append("left join producttype as pt on p.product_type_id=pt.id  ");
			sql.append("left join customer as c on d.customer_id=c.id ");
			sql.append("where d.payment = false and (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:pdt=0 or pt.id=:pdt) and (:pdb=0 or pb.id=:pdb) and (:ieid=0 or d.ie_categories_id=:ieid) ");
			sql.append("and iec.code not in :excodes ");
			if (customers.size() != 0)
				sql.append("and d.customer_id in :cusid ");

			sql.append(" group by  c.customer_name, iec.content,od.voucher_code, d.voucher_code, d.invoice_date ");
			sql.append(" order by c.customer_name,iec.content,d.invoice_date ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", Objects.toString(hFromDate.getValue(), null));
			query.setParameter("td", Objects.toString(hToDate.getValue(), null));
			query.setParameter("now", Objects.toString(hnow.getValue(), null));

			// String fd="\t fd: "+Objects.toString(hFromDate.getValue(), null);
			// String td="\t td: "+Objects.toString(hToDate.getValue(), null);
			// String b="\t Now: "+Objects.toString(hnow.getValue(), null);
			// System.out.println(fd+td+b);

			query.setParameter("pdt", Long.parseLong(Objects.toString(hProductType.getValue())));
			query.setParameter("pdb", Long.parseLong(Objects.toString(hProductBrand.getValue())));
			query.setParameter("ieid", Long.parseLong(Objects.toString(hIEcategories.getValue())));
			List<Long> idCus = new ArrayList<>();
			for (int i = 0; i < customers.size(); i++) {
				idCus.add(customers.get(i).getId());
			}
			if (customers.size() != 0)
				query.setParameter("cusid", idCus);

			List<String> excodes = new ArrayList<>();
			excodes.add("P");
			excodes.add("A");
			excodes.add("5");
			excodes.add("Q");
			excodes.add("&");
			excodes.add("+");
			excodes.add("(");
			query.setParameter("excodes", excodes);
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				CongNoToiHanThanhToan item = new CongNoToiHanThanhToan();
				item.setCustomer_code(Objects.toString(p[0]));
				item.setCustomer_name(Objects.toString(p[1]));
				item.setLoainhapxuat(Objects.toString(p[2]));
				item.setVoucher_code_dh(Objects.toString(p[3]));
				item.setVoucher_code_iv(Objects.toString(p[4]));
				item.setInvoice_date((Date) p[5]);
				// double a=Double.parseDouble(Objects.toString(p[6], "0"));
				// System.out.println("Chua toi hang: "+a);
				item.setChuatoihan(Double.parseDouble(Objects.toString(p[6], "0")));
				item.setToihan(Double.parseDouble(Objects.toString(p[7], "0")));
				item.setQuahan(Double.parseDouble(Objects.toString(p[8], "0")));
				item.setMaloainhapxuat(Objects.toString(p[10]));
				item.setPo(Objects.toString(p[11]));
				list.add(item);
			}
			res = 0;
		} catch (Exception e) {
			logger.error("ReportService.reportChiTietCongNo:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public List<SoDuCongNo> soducongno(String json, long idCus) {
		try {
			List<SoDuCongNo> soDuCongNos = congnotrongthang(json, idCus);
			List<SoDuCongNo> uynhiemchis = uynhiemchi(json, idCus);
			for (int i = 0; i < uynhiemchis.size(); i++) {
				boolean status = true;
				for (int j = 0; j < soDuCongNos.size(); j++) {
					if (soDuCongNos.get(j).getDate().equals(uynhiemchis.get(i).getDate())) {
						soDuCongNos.get(j).setUnc_code(uynhiemchis.get(i).getUnc_code());
						soDuCongNos.get(j).setTotalCo(uynhiemchis.get(i).getTotalCo());
						status = false;
						break;
					}
				}
				if (status) {
					soDuCongNos.add(uynhiemchis.get(i));
				}
			}
			return soDuCongNos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<SoDuCongNo>();
	}

	private List<SoDuCongNo> congnotrongthang(String json, long idCus) {
		List<SoDuCongNo> list = new ArrayList<SoDuCongNo>();
		try {
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductType = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hProductBrand = JsonParserUtil.getValueNumber(data, "product_brand_id", null);
			HolderParser hIEcategories = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select c.customer_code,c.customer_name,d.invoice_date,d.voucher_code,sum(((dt.total)+(dt.total*d.tax_value))) as total_amount, d.quydoi ");
			sql.append("	from invoicedetail as dt ");
			sql.append("	left join invoice as d on dt.invoice_id=d.id ");
			sql.append("	left join IECategories as iec on d.ie_categories_id=iec.id  ");
			sql.append("	left join product as p on dt.product_id=p.id ");
			sql.append("	left join productcom as pc on p.product_com_id=pc.id   ");
			sql.append("	left join productbrand as pb on pc.product_brand_id=pb.id  ");
			sql.append("	left join producttype as pt on p.product_type_id=pt.id  ");
			sql.append("	left join customer as c on d.customer_id=c.id ");
			sql.append("	left join car as ca on d.car_id=ca.id ");

			sql.append("	where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:pdt=0 or pt.id=:pdt) and (:pdb=0 or pb.id=:pdb) and (:ieid=0 or d.ie_categories_id=:ieid) ");
			sql.append(" 	and iec.code not in :excodes ");
			if (idCus != 0)
				sql.append(" and d.customer_id = :cusid ");

			sql.append("group by c.customer_name,d.invoice_date,d.voucher_code ");
			sql.append("order by c.customer_name,d.invoice_date,d.voucher_code ");

			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("pdt", Long.parseLong(Objects.toString(hProductType.getValue())));
			query.setParameter("pdb", Long.parseLong(Objects.toString(hProductBrand.getValue())));
			query.setParameter("ieid", Long.parseLong(Objects.toString(hIEcategories.getValue())));

			if (idCus != 0)
				query.setParameter("cusid", idCus);

			List<String> excodes = new ArrayList<>();
			excodes.add("P");
			excodes.add("A");
			excodes.add("5");
			excodes.add("Q");
			excodes.add("&");
			excodes.add("+");
			excodes.add("(");
			excodes.add("(");
			excodes.add("M");
			query.setParameter("excodes", excodes);

			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				SoDuCongNo item = new SoDuCongNo();
				item.setCustomer_code(Objects.toString(p[0]));
				item.setCustomer_name(Objects.toString(p[1]));
				item.setDate((Date) p[2]);
				item.setVoucher_code(Objects.toString(p[3]));
				item.setTotalNo(Double.parseDouble(Objects.toString(p[4], "0")));
				item.setNote(Objects.toString(p[5], ""));
				list.add(item);
			}
		} catch (Exception e) {
			logger.error("ReportService.reportChiTietCongNo:" + e.getMessage(), e);
		}
		return list;
	}

	private List<SoDuCongNo> uynhiemchi(String json, long idCus) {
		List<SoDuCongNo> list = new ArrayList<SoDuCongNo>();
		try {
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT cv.customer_code,cv.customer_name,dv.payment_date as invoice_date, dv.voucher_code, sum(total_amount) as total_amounttt  FROM voucherpayment as dv ");
			sql.append("left join customer as cv on dv.payment_customer_id=cv.id  ");
			sql.append("where (:fd = '' or dv.payment_date >= :fd) and (:td = '' or dv.payment_date <= :td) ");
			if (idCus != 0)
				sql.append(" and dv.payment_customer_id = :cusid ");
			sql.append("group by cv.customer_name,dv.payment_date,dv.voucher_code ");
			sql.append("order by cv.customer_name,dv.payment_date,dv.voucher_code ");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			if (idCus != 0)
				query.setParameter("cusid", idCus);
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				SoDuCongNo item = new SoDuCongNo();
				item.setCustomer_code(Objects.toString(p[0]));
				item.setCustomer_name(Objects.toString(Objects.toString(p[1])));
				item.setDate((Date) p[2]);
				item.setUnc_code(Objects.toString(p[3]));
				item.setTotalCo(Double.parseDouble(Objects.toString(p[4], "0")));
				list.add(item);
			}
		} catch (Exception e) {
			logger.error("ReportService.reportChiTietCongNo:" + e.getMessage(), e);
		}
		return list;
	}

	@Override
	public int soducongno2(String json, List<SoDuCongNo> list, List<Customer> customers) {
		int res = -1;
		try {
			List<SoDuCongNo> soDuCongNos = congnotrongthanggroup(json, customers);
			List<SoDuCongNo> uynhiemchis = uynhiemchigroup(json, customers);
			for (int i = 0; i < uynhiemchis.size(); i++) {
				boolean status = true;
				for (int j = 0; j < soDuCongNos.size(); j++) {
					if (soDuCongNos.get(j).getCustomer_code().equals(uynhiemchis.get(i).getCustomer_code())) {
						soDuCongNos.get(j).setUnc_code(uynhiemchis.get(i).getUnc_code());
						soDuCongNos.get(j).setTotalCo(uynhiemchis.get(i).getTotalCo());
						status = false;
						break;
					}
				}
				if (status) {
					soDuCongNos.add(uynhiemchis.get(i));
				}
			}
			list.addAll(soDuCongNos);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	private List<SoDuCongNo> congnotrongthanggroup(String json, List<Customer> customers) {
		List<SoDuCongNo> list = new ArrayList<SoDuCongNo>();
		try {
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			HolderParser hProductType = JsonParserUtil.getValueNumber(data, "product_type_id", null);
			HolderParser hProductBrand = JsonParserUtil.getValueNumber(data, "product_brand_id", null);
			HolderParser hIEcategories = JsonParserUtil.getValueNumber(data, "ie_categories_id", null);
			StringBuilder sql = new StringBuilder();
			sql.append("select c.customer_code,c.customer_name,sum(((dt.total)+(dt.total*d.tax_value))) as total_amount ");
			sql.append("	from invoicedetail as dt ");
			sql.append("	left join invoice as d on dt.invoice_id=d.id ");
			sql.append("	left join IECategories as iec on d.ie_categories_id=iec.id  ");
			sql.append("	left join product as p on dt.product_id=p.id ");
			sql.append("	left join productcom as pc on p.product_com_id=pc.id   ");
			sql.append("	left join productbrand as pb on pc.product_brand_id=pb.id  ");
			sql.append("	left join producttype as pt on p.product_type_id=pt.id  ");
			sql.append("	left join customer as c on d.customer_id=c.id ");
			sql.append("	left join car as ca on d.car_id=ca.id ");

			sql.append("	where (:fd = '' or d.invoice_date >= :fd) and (:td = '' or d.invoice_date <= :td) and (:pdt=0 or pt.id=:pdt) and (:pdb=0 or pb.id=:pdb) and (:ieid=0 or d.ie_categories_id=:ieid) ");
			sql.append(" 	and iec.code not in :excodes ");
			if (customers.size() != 0)
				sql.append(" and d.customer_id in :cusid ");

			sql.append("group by c.customer_code");

			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("pdt", Long.parseLong(Objects.toString(hProductType.getValue())));
			query.setParameter("pdb", Long.parseLong(Objects.toString(hProductBrand.getValue())));
			query.setParameter("ieid", Long.parseLong(Objects.toString(hIEcategories.getValue())));
			List<Long> idCus = new ArrayList<>();
			for (int i = 0; i < customers.size(); i++) {
				idCus.add(customers.get(i).getId());
			}
			if (customers.size() != 0)
				query.setParameter("cusid", idCus);

			List<String> excodes = new ArrayList<>();
			excodes.add("P");
			excodes.add("A");
			excodes.add("5");
			excodes.add("Q");
			excodes.add("&");
			excodes.add("+");
			excodes.add("(");
			excodes.add("M");
			query.setParameter("excodes", excodes);

			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				SoDuCongNo item = new SoDuCongNo();
				item.setCustomer_code(Objects.toString(p[0]));
				item.setCustomer_name(Objects.toString(Objects.toString(p[1])));
				item.setTotalNo(MyMath.round(Double.parseDouble(Objects.toString(p[2], "0"))));
				list.add(item);
			}
		} catch (Exception e) {
			logger.error("ReportService.reportChiTietCongNo:" + e.getMessage(), e);
		}
		return list;
	}

	private List<SoDuCongNo> uynhiemchigroup(String json, List<Customer> customers) {
		List<SoDuCongNo> list = new ArrayList<SoDuCongNo>();
		try {
			JsonObject data = JsonParserUtil.getGson().fromJson(json, JsonObject.class);
			HolderParser hFromDate = JsonParserUtil.getValueString(data, "from_date", null);
			HolderParser hToDate = JsonParserUtil.getValueString(data, "to_date", null);
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT cv.customer_code,cv.customer_name,sum(total_amount) as total_amounttt  FROM voucherpayment as dv ");
			sql.append("left join customer as cv on dv.payment_customer_id=cv.id  ");
			sql.append("where (:fd = '' or dv.payment_date >= :fd) and (:td = '' or dv.payment_date <= :td) ");
			if (customers.size() != 0)
				sql.append(" and dv.payment_customer_id in :cusid ");
			sql.append("group by cv.customer_code");
			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("fd", ToolTimeCustomer.convertStringPattern(
					Objects.toString(hFromDate.getValue(), null), "dd/MM/yyyy", "yyyy-MM-dd"));
			query.setParameter("td", ToolTimeCustomer.convertStringPattern(Objects.toString(hToDate.getValue(), null),
					"dd/MM/yyyy", "yyyy-MM-dd"));
			List<Long> idCus = new ArrayList<>();
			for (int i = 0; i < customers.size(); i++) {
				idCus.add(customers.get(i).getId());
			}
			if (customers.size() != 0)
				query.setParameter("cusid", idCus);
			List<Object[]> listResult = query.getResultList();
			for (Object[] p : listResult) {
				SoDuCongNo item = new SoDuCongNo();
				item.setCustomer_code(Objects.toString(p[0]));
				item.setCustomer_name(Objects.toString(Objects.toString(p[1])));
				item.setTotalCo(Double.parseDouble(Objects.toString(p[2], "0")));
				list.add(item);
			}
		} catch (Exception e) {
			logger.error("ReportService.reportChiTietCongNo:" + e.getMessage(), e);
		}
		return list;
	}
}
