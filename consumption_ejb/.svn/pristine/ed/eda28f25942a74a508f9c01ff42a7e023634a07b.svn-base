package lixco.com.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lixco.com.entity.Customer;
import lixco.com.entity.IECategories;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.InvoiceDetailTemp;
import lixco.com.entity.Product;
import lixco.com.interfaces.IInvoiceDetailService;
import lixco.com.interfaces.IVoucherPaymentService;
import lixco.com.loaddata.InvoiceDMS;
import lixco.com.loaddata.InvoiceDetailDMS;
import lixco.com.reqInfo.InvoiceDetailReqInfo;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class InvoiceDetailService implements IInvoiceDetailService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;

	@Override
	public int selectByIECategories(List<String> ie_categorieCodes, Date sDate, Date eDate, List<InvoiceDetail> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InvoiceDetail> cq = cb.createQuery(InvoiceDetail.class);
			Root<InvoiceDetail> root = cq.from(InvoiceDetail.class);
			root.fetch("product", JoinType.INNER);
			Fetch<InvoiceDetail, Invoice> inv_ = root.fetch("invoice", JoinType.INNER);
			inv_.fetch("customer", JoinType.INNER);

			Join<InvoiceDetail, Invoice> invoice_ = root.join("invoice", JoinType.LEFT);
			Join<Invoice, IECategories> ieCate_ = invoice_.join("ie_categories", JoinType.LEFT);

			List<Predicate> predicates = new ArrayList<Predicate>();
			if (sDate != null) {
				Predicate predicatesdate = cb.greaterThanOrEqualTo(root.get("invoice").get("invoice_date"), sDate);
				predicates.add(predicatesdate);
			}
			if (eDate != null) {
				Predicate predicatesdate = cb.lessThanOrEqualTo(root.get("invoice").get("invoice_date"), eDate);
				predicates.add(predicatesdate);
			}
			cq.select(root).where(cb.in(ieCate_.get("code")).value(ie_categorieCodes),
					cb.and(predicates.toArray(new Predicate[0])));
			TypedQuery<InvoiceDetail> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectByOrder:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectByInvoice(long invoiceId, List<InvoiceDetail> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InvoiceDetail> cq = cb.createQuery(InvoiceDetail.class);
			Root<InvoiceDetail> root = cq.from(InvoiceDetail.class);
			root.fetch("product", JoinType.INNER);
			root.fetch("invoice", JoinType.INNER);
			cq.select(root).where(cb.equal(root.get("invoice").get("id"), invoiceId));
			TypedQuery<InvoiceDetail> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectByOrder:" + e.getMessage(), e);
		}
		return res;
	}
	@Override
	public int selectByInvoiceTemp(long invoiceId, List<InvoiceDetailTemp> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InvoiceDetailTemp> cq = cb.createQuery(InvoiceDetailTemp.class);
			Root<InvoiceDetailTemp> root = cq.from(InvoiceDetailTemp.class);
			root.fetch("product", JoinType.INNER);
			root.fetch("invoicetemp", JoinType.INNER);
			cq.select(root).where(cb.equal(root.get("invoicetemp").get("id"), invoiceId));
			TypedQuery<InvoiceDetailTemp> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectByOrder:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectByInvoices(List<Long> invoiceIds, List<InvoiceDetail> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InvoiceDetail> cq = cb.createQuery(InvoiceDetail.class);
			Root<InvoiceDetail> root = cq.from(InvoiceDetail.class);
			root.fetch("product", JoinType.INNER);
			root.fetch("invoice", JoinType.INNER);
			cq.select(root).where(cb.in(root.get("invoice").get("id")).value(invoiceIds));
			TypedQuery<InvoiceDetail> query = em.createQuery(cq);
			List<InvoiceDetail> results = query.getResultList();
			int size = results.size();
			for (int i = 0; i < size; i++) {
				if (results.get(i).getInvoice().getCustomer() != null)
					results.get(i).getInvoice().getCustomer().getCustomer_code();
				if (results.get(i).getInvoice().getWarehouse() != null)
					results.get(i).getInvoice().getWarehouse().getCode();
				if (results.get(i).getInvoice().getIe_categories() != null)
					results.get(i).getInvoice().getIe_categories().getCode();
				if (results.get(i).getInvoice().getContract() != null)
					results.get(i).getInvoice().getContract().getContract_code();
				if (results.get(i).getInvoice().getCar() != null)
					results.get(i).getInvoice().getCar().getLicense_plate();
			}
			list.addAll(results);
			res = 0;
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectByOrder:" + e.getMessage(), e);
		}
		return res;
	}
	@Override
	public List<InvoiceDetail> selectByInvoices(List<Long> invoiceIds ) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InvoiceDetail> cq = cb.createQuery(InvoiceDetail.class);
			Root<InvoiceDetail> root = cq.from(InvoiceDetail.class);
			root.fetch("product", JoinType.INNER);
			cq.select(root).where(cb.in(root.get("invoice").get("id")).value(invoiceIds));
			TypedQuery<InvoiceDetail> query = em.createQuery(cq);
			List<InvoiceDetail> results = query.getResultList();
			return results;
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectByOrder:" + e.getMessage(), e);
		}
		return new ArrayList<InvoiceDetail>();
	}

	@Override
	public int selectByInvoicesv2(List<Invoice> invoices, List<InvoiceDetail> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InvoiceDetail> cq = cb.createQuery(InvoiceDetail.class);
			Root<InvoiceDetail> root = cq.from(InvoiceDetail.class);
			root.fetch("product", JoinType.INNER);
			root.fetch("invoice", JoinType.INNER);
			cq.select(root).where(cb.in(root.get("invoice")).value(invoices));
			TypedQuery<InvoiceDetail> query = em.createQuery(cq);
			List<InvoiceDetail> results = query.getResultList();
			list.addAll(results);
			res = 0;
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectByOrder:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int selectById(long id, InvoiceDetailReqInfo t) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InvoiceDetail> cq = cb.createQuery(InvoiceDetail.class);
			Root<InvoiceDetail> root = cq.from(InvoiceDetail.class);
			root.fetch("product", JoinType.INNER);
			root.fetch("invoice", JoinType.INNER);
			root.fetch("invoice_detail_own", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<InvoiceDetail> query = em.createQuery(cq);
			t.setInvoice_detail(query.getSingleResult());
			res = 0;
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectById:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public InvoiceDetail selectById(long id) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InvoiceDetail> cq = cb.createQuery(InvoiceDetail.class);
			Root<InvoiceDetail> root = cq.from(InvoiceDetail.class);
			root.fetch("product", JoinType.INNER);
			root.fetch("invoice", JoinType.INNER);
			root.fetch("invoice_detail_own", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("id"), id));
			TypedQuery<InvoiceDetail> query = em.createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectById:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int selectByInvoiceDetailMain(long idMain, List<InvoiceDetail> list) {
		int res = -1;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InvoiceDetail> cq = cb.createQuery(InvoiceDetail.class);
			Root<InvoiceDetail> root = cq.from(InvoiceDetail.class);
			root.fetch("product", JoinType.INNER);
			root.fetch("invoice", JoinType.INNER);
			root.fetch("invoice_detail_own", JoinType.LEFT);
			cq.select(root).where(cb.equal(root.get("invoice_detail_own").get("id"), idMain));
			TypedQuery<InvoiceDetail> query = em.createQuery(cq);
			list.addAll(query.getResultList());
			res = 0;
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectByInvoiceDetailMain:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int update(InvoiceDetail detail, String userAction) {
		int res = -1;
		try {
			//cap nhat tu hoa don
			detail.setUseraction(userAction);
			detail.setAct("All_truHD");
			if (em.merge(detail) != null) {
				res = 0;
			}
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectByInvoiceDetailMain:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int insert(InvoiceDetailReqInfo t) {
		int res = -1;
		try {
			InvoiceDetail p = t.getInvoice_detail();
			if (p != null) {
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("InvoiceDetailService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public int insert(InvoiceDetail p) {
		int res = -1;
		try {
			if (p != null) {
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("InvoiceDetailService.insert:" + e.getMessage(), e);
		}
		return res;
	}

	@Override
	public List<Object[]> selectByInvoicesToDMS(int monthS, int yearS) {
		try {
			StringBuilder sql = new StringBuilder();

			sql.append("SELECT ").append("i.id as idinvoice, ").append("'BL146' as mact, ")
					.append("i.voucher_code as vcinvoice, ").append("i.invoice_date as invDate, ")
					.append("c.customer_code as cusCode, ").append("ie.code AS codeie, ").append("'' as ai, ")
					.append("pd.product_code, ").append("ind.quantity as qud, ").append("ind.unit_price, ")
					.append("ind.total as ttd, ").append("i.tax_value, ").append("i.order_voucher, ")
					.append(" i.note AS noteinvoice, ").append("(ind.quantity /pd.specification) as qudthung, ")
					.append("0 as bi, ").append("'' as ci, ").append("i.lookup_code as lk, ")
					.append("ind.productdh_code as pdcode, i.refId as refId").append(" FROM ").append("invoice AS i ").append("JOIN ")
					.append("invoicedetail AS ind ON i.id = ind.invoice_id ").append("LEFT JOIN ")
					.append("iecategories AS ie ON ie.id = i.ie_categories_id ").append("LEFT JOIN ")
					.append("customer AS c ON c.id = i.customer_id ").append("LEFT JOIN ")
					.append("product AS pd ON pd.id = ind.product_id ").append("WHERE ")
					.append("YEAR(i.invoice_date) = :yearS AND  MONTH(i.invoice_date) = :monthS");

			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("yearS", yearS);
			query.setParameter("monthS", monthS);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectByInvoicesToExcel:" + e.getMessage(), e);
		}
		return null;
	}
	
	@Override // Giả sử bạn đang implement một Interface
	public List<InvoiceDMS> selectByInvoicesToDMS2(int monthS, int yearS) {
	    // 1. CHUYỂN ĐỔI THAM SỐ THỜI GIAN
	    Calendar cal = Calendar.getInstance();
	    
	    // Đặt ngày đầu tháng
	    cal.set(Calendar.YEAR, yearS);
	    // Lưu ý: Calendar.MONTH là 0-indexed (0 là tháng 1, 11 là tháng 12)
	    cal.set(Calendar.MONTH, monthS - 1); 
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    Date startDate = cal.getTime();

	    // Đặt ngày cuối tháng (Ngày 1 tháng sau, trừ đi 1ms)
	    cal.add(Calendar.MONTH, 1);
	    cal.add(Calendar.MILLISECOND, -1);
	    Date endDate = cal.getTime();
	    
	    // --- Bắt đầu logic truy vấn ---
	    try {
	    	/*
			 *  H3	NPP HORECA (MN)
				KK	SIÊU THỊ KING KONG
				PB	NHÀ PHÂN PHỐI NỘI ĐỊA (MB)
				PP	NHÀ PHÂN PHỐI NỘI ĐỊA (MN)
				VP	CÔNG TY VĨNH PHÁT
			 */
	        // 2. Chuẩn bị SQL - Thay thế điều kiện YEAR/MONTH
	        StringBuilder sql = new StringBuilder();
	        sql.append("SELECT ")
	            // Trường Invoice (Giữ nguyên)
	            .append("i.id as idinvoice, 'BL146' as mact, i.voucher_code as vcinvoice, i.invoice_date as invDate, ")
	            .append("c.customer_code as cusCode, ie.code AS codeie, '' as ai, i.tax_value, i.order_voucher, ")
	            .append("i.note AS noteinvoice, i.lookup_code as lk, i.refId as refId, ")
	            // Trường InvoiceDetail (Giữ nguyên)
	            .append("pd.product_code, ind.quantity as qud, ind.unit_price, ind.total as ttd, ")
	            .append("(ind.quantity /pd.specification) as qudthung, 0 as bi, '' as ci, ind.productdh_code as pdcode ")
	            .append(" FROM ").append("invoice AS i ")
	            .append("JOIN ").append("invoicedetail AS ind ON i.id = ind.invoice_id ")
	            .append("LEFT JOIN ").append("iecategories AS ie ON ie.id = i.ie_categories_id ")
	            .append("LEFT JOIN ").append("customer AS c ON c.id = i.customer_id ")
	            .append("LEFT JOIN ").append("CustomerTypes AS custy ON custy.id = c.customer_types_id ")
	            .append("LEFT JOIN ").append("product AS pd ON pd.id = ind.product_id ")
	            .append("WHERE ")
	            // *** ĐIỀU KIỆN ĐÃ SỬA: Dùng BETWEEN với tham số ngày ***
	            .append("i.invoice_date BETWEEN :startDate AND :endDate ") 
	            .append("AND i.updateIV = true AND custy.code IN ('PB','PP','KK','H3','VP') AND (i.refId is not null OR i.refId != '')")
	            .append(" ORDER BY i.id, ind.id"); 

	        Query query = em.createNativeQuery(sql.toString());
	        // *** SET THAM SỐ MỚI ***
	        query.setParameter("startDate", startDate);
	        query.setParameter("endDate", endDate);

	        // 3. Thực thi và Mapping (Giữ nguyên logic)
	        @SuppressWarnings("unchecked")
	        List<Object[]> results = query.getResultList();

	        Map<Long, InvoiceDMS> invoiceMap = new HashMap<>();

	        for (Object[] row : results) {
	            Long invoiceId = ((Number) row[0]).longValue(); // idinvoice
	            
	            InvoiceDMS invoice = invoiceMap.get(invoiceId);
	            if (invoice == null) {
	                invoice = mapToInvoice(row);
	                invoiceMap.put(invoiceId, invoice);
	            }
	            
	            InvoiceDetailDMS detail = mapToInvoiceDetail(row);
	            invoice.addDetail(detail);
	        }

	        return new ArrayList<>(invoiceMap.values());

	    } catch (Exception e) {
	        logger.error("InvoiceDetailService.selectByInvoicesToDMS2: " + e.getMessage(), e);
	    }
	    return null;
	}
	@Override // Giả sử bạn đang implement một Interface
	public List<InvoiceDMS> selectByInvoicesToDMS2MienBacNapVaoMienNam(int monthS, int yearS) {
	    // 1. CHUYỂN ĐỔI THAM SỐ THỜI GIAN
	    Calendar cal = Calendar.getInstance();
	    
	    // Đặt ngày đầu tháng
	    cal.set(Calendar.YEAR, yearS);
	    // Lưu ý: Calendar.MONTH là 0-indexed (0 là tháng 1, 11 là tháng 12)
	    cal.set(Calendar.MONTH, monthS - 1); 
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    Date startDate = cal.getTime();

	    // Đặt ngày cuối tháng (Ngày 1 tháng sau, trừ đi 1ms)
	    cal.add(Calendar.MONTH, 1);
	    cal.add(Calendar.MILLISECOND, -1);
	    Date endDate = cal.getTime();
	    
	    // --- Bắt đầu logic truy vấn ---
	    try {
	    	/*
			 *  H3	NPP HORECA (MN)
				KK	SIÊU THỊ KING KONG
				PB	NHÀ PHÂN PHỐI NỘI ĐỊA (MB)
				PP	NHÀ PHÂN PHỐI NỘI ĐỊA (MN)
				VP	CÔNG TY VĨNH PHÁT
			 */
	        // 2. Chuẩn bị SQL - Thay thế điều kiện YEAR/MONTH
	    	String[] dmshoadonphiabac = { "CL056", "CL449", "CS169", "CL317", "CL326", "CL405", "DN089", "CO299", "CL523",
			"NCT75" };
	        StringBuilder sql = new StringBuilder();
	        sql.append("SELECT ")
	            // Trường Invoice (Giữ nguyên)
	            .append("i.id as idinvoice, 'BL146' as mact, i.voucher_code as vcinvoice, i.invoice_date as invDate, ")
	            .append("c.customer_code as cusCode, ie.code AS codeie, '' as ai, i.tax_value, i.order_voucher, ")
	            .append("i.note AS noteinvoice, i.lookup_code as lk, i.refId as refId, ")
	            // Trường InvoiceDetail (Giữ nguyên)
	            .append("pd.product_code, ind.quantity as qud, ind.unit_price, ind.total as ttd, ")
	            .append("(ind.quantity /pd.specification) as qudthung, 0 as bi, '' as ci, ind.productdh_code as pdcode ")
	            .append(" FROM ").append("invoice AS i ")
	            .append("JOIN ").append("invoicedetail AS ind ON i.id = ind.invoice_id ")
	            .append("LEFT JOIN ").append("iecategories AS ie ON ie.id = i.ie_categories_id ")
	            .append("LEFT JOIN ").append("customer AS c ON c.id = i.customer_id ")
	            .append("LEFT JOIN ").append("CustomerTypes AS custy ON custy.id = c.customer_types_id ")
	            .append("LEFT JOIN ").append("product AS pd ON pd.id = ind.product_id ")
	            .append("WHERE ")
	            // *** ĐIỀU KIỆN ĐÃ SỬA: Dùng BETWEEN với tham số ngày ***
	            .append("i.invoice_date BETWEEN :startDate AND :endDate ") 
	            .append("AND i.updateIV = true AND c.customer_code IN :codeNPP AND (i.refId is not null OR i.refId != '')")
	            .append(" ORDER BY i.id, ind.id"); 

	        Query query = em.createNativeQuery(sql.toString());
	        // *** SET THAM SỐ MỚI ***
	        query.setParameter("startDate", startDate);
	        query.setParameter("endDate", endDate);
	        List<String> codeNPPList = Arrays.asList(dmshoadonphiabac);
	        query.setParameter("codeNPP", codeNPPList);

	        // 3. Thực thi và Mapping (Giữ nguyên logic)
	        List<Object[]> results = query.getResultList();

	        Map<Long, InvoiceDMS> invoiceMap = new HashMap<>();

	        for (Object[] row : results) {
	            Long invoiceId = ((Number) row[0]).longValue(); // idinvoice
	            
	            InvoiceDMS invoice = invoiceMap.get(invoiceId);
	            if (invoice == null) {
	                invoice = mapToInvoice(row);
	                invoiceMap.put(invoiceId, invoice);
	            }
	            
	            InvoiceDetailDMS detail = mapToInvoiceDetail(row);
	            invoice.addDetail(detail);
	        }

	        return new ArrayList<>(invoiceMap.values());

	    } catch (Exception e) {
	        logger.error("InvoiceDetailService.selectByInvoicesToDMS2: " + e.getMessage(), e);
	    }
	    return null;
	}

    // Hàm hỗ trợ Mapping Invoice
    private InvoiceDMS mapToInvoice(Object[] row) {
        InvoiceDMS invoice = new InvoiceDMS();
        // Cần đảm bảo thứ tự các trường khớp với SQL
        invoice.setId(((Number) row[0]).longValue()); // 0: idinvoice
        invoice.setMaCt((String) row[1]);             // 1: mact
        invoice.setVoucherCode((String) row[2]);      // 2: vcinvoice
        invoice.setInvoiceDate((java.util.Date) row[3]); // 3: invDate (tùy thuộc vào kiểu trả về của DB)
        invoice.setCustomerCode((String) row[4]);     // 4: cusCode
        invoice.setCodeIe((String) row[5]);           // 5: codeie
        invoice.setAi((String) row[6]);               // 6: ai
        invoice.setTaxValue(toDouble(row[7]));        // 7: tax_value
        invoice.setOrderVoucher((String) row[8]);     // 8: order_voucher
        invoice.setNote((String) row[9]);             // 9: noteinvoice
        invoice.setLookupCode((String) row[10]);       // 10: lk
        invoice.setRefId((String)(row[11]));            // 11: refId
        // Các trường 12 trở đi là của InvoiceDetail
        return invoice;
    }

    // Hàm hỗ trợ Mapping InvoiceDetail
    private InvoiceDetailDMS mapToInvoiceDetail(Object[] row) {
        InvoiceDetailDMS detail = new InvoiceDetailDMS();
        // Cần đảm bảo thứ tự các trường khớp với SQL (bắt đầu từ vị trí 12)
        detail.setProductCode((String) row[12]);     // 12: product_code
        detail.setQuantity((toDouble(row[13])));       // 13: qud
        detail.setUnitPrice((toDouble(row[14])));      // 14: unit_price
        detail.setTotalDetail((toDouble(row[15])));    // 15: ttd
        detail.setQuantityCarton((toDouble(row[16]))); // 16: qudthung
        detail.setBi(toInteger(row[17]));            // 17: bi
        detail.setCi((String) row[18]);              // 18: ci
        detail.setProductDhCode((String) row[19]);   // 19: pdcode
        return detail;
    }
//    public static BigDecimal doubleToBigDecimal(Double d) {
//        if (d == null || d.isNaN() || d.isInfinite()) {
//            return BigDecimal.ZERO;
//        }
//        
//        // Phương pháp an toàn nhất: chuyển Double thành String trước.
//        // Dùng Double.toString(d) để có biểu diễn chuỗi chính xác nhất của giá trị Double.
//        String s = Double.toString(d);
//        
//        // Sử dụng constructor string của BigDecimal để tạo số chính xác.
//        // Có thể thêm MathContext.DECIMAL64 để chuẩn hóa độ chính xác, tùy thuộc vào yêu cầu.
//        BigDecimal bd = new BigDecimal(s, MathContext.DECIMAL64);
//        return bd.setScale(2, RoundingMode.HALF_UP);
//    }
    // Hàm tiện ích chuyển đổi kiểu
    private double toDouble(Object obj) {
        if (obj instanceof Number) return ((Number) obj).doubleValue();
        return Math.round((obj != null ? Double.parseDouble(obj.toString()) : 0)*100)/100;
    }

    private Integer toInteger(Object obj) {
        if (obj instanceof Number) return ((Number) obj).intValue();
        return obj != null ? Integer.parseInt(obj.toString()) : null;
    }

    private Long toLong(Object obj) {
        if (obj instanceof Number) return ((Number) obj).longValue();
        return obj != null ? Long.parseLong(obj.toString()) : null;
    }
	@Override
	public List<Object[]> selectByInvoicesToDMS(long idInvoice) {
		try {
			StringBuilder sql = new StringBuilder();

			sql.append("SELECT ").append("i.id as idinvoice, ").append("'BL146' as mact, ")
					.append("i.voucher_code as vcinvoice, ").append("i.invoice_date as invDate, ")
					.append("c.customer_code as cusCode, ").append("ie.code AS codeie, ").append("'' as ai, ")
					.append("pd.product_code, ").append("ind.quantity as qud, ").append("ind.unit_price, ")
					.append("ind.total as ttd, ").append("i.tax_value, ").append("i.order_voucher, ")
					.append(" i.note AS noteinvoice, ").append("(ind.quantity /pd.specification) as qudthung, ")
					.append("0 as bi, ").append("'' as ci, ").append("i.lookup_code as lk, ")
					.append("ind.productdh_code as pdcode, i.refId as refId").append(" FROM ").append("invoice AS i ").append("JOIN ")
					.append("invoicedetail AS ind ON i.id = ind.invoice_id ").append("LEFT JOIN ")
					.append("iecategories AS ie ON ie.id = i.ie_categories_id ").append("LEFT JOIN ")
					.append("customer AS c ON c.id = i.customer_id ").append("LEFT JOIN ")
					.append("product AS pd ON pd.id = ind.product_id ").append("WHERE ")
					.append("i.id = :idInvoice");

			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("idInvoice", idInvoice);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectByInvoicesToExcel:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<Object[]> findByCustomer(Customer customer, Date sDate, Date eDate) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ").append("pd.product_code, ").append("pd.product_name, ")
					.append("Sum(ind.quantity) as qud, ").append("ind.unit_price, ").append("sum(ind.total) as ttd, ")
					.append("pd.unit ")

					.append(" FROM ").append("invoicedetail AS ind ").append("JOIN ")
					.append("invoice AS i ON i.id = ind.invoice_id ").append("LEFT JOIN ")
					.append("iecategories AS ie ON ie.id = i.ie_categories_id ").append("LEFT JOIN ")
					.append("customer AS c ON c.id = i.customer_id ").append("LEFT JOIN ")
					.append("product AS pd ON pd.id = ind.product_id ").append("LEFT JOIN ")
					.append("ProductType AS pdt ON pdt.id = pd.product_type_id ").append("WHERE ")
					.append("i.customer_id = :customer_id AND ")
					.append("i.invoice_date >= :sDate AND  i.invoice_date <= :eDate AND ")
					.append("ie.code not in :iecodes AND ").append("pdt.code != :pdtcode ")
					.append("GROUP BY pd.product_code, ind.unit_price ");

			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("customer_id", customer.getId());
			query.setParameter("sDate", sDate);
			query.setParameter("eDate", eDate);
			String[] iecodeStr = { "A", "P", "5", "Q", "0", "O", "M", "&", "+" };
			List<String> iecodes = new ArrayList<>(Arrays.asList(iecodeStr));
			query.setParameter("iecodes", iecodes);
			query.setParameter("pdtcode", "H");
			return query.getResultList();
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectByInvoicesToExcel:" + e.getMessage(), e);
		}
		return new ArrayList<Object[]>();
	}

	@Override
	public Object[] findByCustomerTTThue(Customer customer, Date sDate, Date eDate) {
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("select sum(v1.tongtien),sum(v1.thue) from ")
			.append("(SELECT DISTINCT i.id as idg, i.* ")
			.append("FROM ").append("invoice AS i ").append("LEFT JOIN ")
					.append("iecategories AS ie ON ie.id = i.ie_categories_id ").append("LEFT JOIN ")
					.append("invoicedetail AS idd ON idd.invoice_id = i.id ").append("LEFT JOIN 	")
					.append("product AS pd ON pd.id = idd.product_id ").append("LEFT JOIN 	")
					.append("ProductType AS pdt ON pdt.id = pd.product_type_id ").append("WHERE ")
					.append("i.customer_id = :customer_id AND ")
					.append("i.invoice_date >= :sDate AND  i.invoice_date <= :eDate AND ")
					.append("pdt.code != :pdtcode  AND ").append("ie.code not in (:iecodes) ) as v1 ")
					.append(" group by v1.customer_id ");

			Query query = em.createNativeQuery(sql.toString());
			query.setParameter("customer_id", customer.getId());
			query.setParameter("sDate", sDate);
			query.setParameter("eDate", eDate);
			String[] iecodeStr = { "A", "P", "5", "Q", "0", "O", "M", "&", "+" };
			List<String> iecodes = new ArrayList<>(Arrays.asList(iecodeStr));
			query.setParameter("iecodes", iecodes);
			query.setParameter("pdtcode", "H");
			List<Object[]> results = query.getResultList();
			if (results.size() != 0)
				return results.get(0);
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectByInvoicesToExcel:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int selectByInvoicesToExcel(List<Long> invoiceIds, List<Object[]> list) {
		int res = -1;
		try {
			if (invoiceIds.size() != 0) {
				StringBuilder sql = new StringBuilder();
				sql.append("SELECT ").append("'BL146', ").append("i.voucher_code, ").append("i.invoice_date, ")
						.append("c.customer_code, ").append("wa.code as codewa, ").append("ie.code as codeie, ")
						.append("cont.voucher_code as sohd, ").append("pd.product_code, ").append("pd.product_name, ")
						.append("ind.quantity, ").append("i.id as idinvoice, ").append("car.license_plate, ")
						.append("ind.unit_price, ").append("ind.total, ").append("ind.foreign_unit_price, ")
						.append("ind.foreign_unit_price * ind.quantity, ").append("i.tax_value, ")
						.append("i.invoice_serie, ").append("i.order_voucher, ").append("ind.productdh_code, ")
						.append("i.note as noteinvoice, ").append("ind.quantity * pd.factor, ")
						.append("ind.quantity / pd.specification, ").append("i.payment as ipayment, ")
						.append("ste.content, ").append("pay.payment, ").append("ste.id idbx, ")
						.append("del.place_code, ").append("i.created_by, ")
						.append("promo.program_code as promoCode, ").append("i.timeout, ")
						.append("ind.note_batch_code, ").append("ind.note as notedt, ").append("ord.id as idorder , ")
						.append("pri.program_code as priceCode, ").append("del.address, ").append("i.po_no, ")
						.append("c.customer_name, ").append("carr.carrier_name, ").append("pdcom.pcom_code, ")
						.append("pdcom.pcom_name, ").append("pdbrand.pbrand_code, ").append("pdbrand.pbrand_name, ")
						.append("cust.name as custname, ").append("cusch.name as cuschname, ")
						.append("i.delivery_date, ").append("i.sohoadongoc, ").append("i.lookup_code, ")
						.append("i.quydoi, ").append("carr.carrier_code, ").append("ind.id, ").append("pd.maspchinh, ")
						.append(" '', ").append("pdkm.product_code as mspkm, ").append("pdkm.factor as hsqdkm, ")
						.append("sto.stocker_name as stoName, ").append("promo.note, ")
						.append("ind.slpallet, ieiv.voucher_code as sohdtm,  nh.name as nhanhang, ").append("i.idhoadongoc, ")
						.append("area.area_code as makhuvuc, area.area_name as tenkhuvuc ");
				sql.append("FROM ").append("invoice AS i ").append("JOIN ")
						.append("invoicedetail AS ind ON i.id = ind.invoice_id ").append("LEFT JOIN ")
						.append("warehouse AS wa ON wa.id = i.warehouse_id ").append("LEFT JOIN ")
						.append("ieinvoice AS ieiv ON ieiv.id = i.idhoadonxk ").append("LEFT JOIN ")
						.append("iecategories AS ie ON ie.id = i.ie_categories_id ").append("LEFT JOIN ")
						.append("contract AS cont ON cont.id = i.contract_id ").append("LEFT JOIN ")
						.append("Car AS car ON car.id = i.car_id ").append("LEFT JOIN ")
						.append("stevedore AS ste ON ste.id = i.stevedore_id ").append("LEFT JOIN ")
						.append("paymentmethod AS pay ON pay.id = i.payment_method_id ").append("LEFT JOIN ")
						.append("deliverypricing AS del ON del.id = i.delivery_pricing_id ").append("LEFT JOIN ")
						.append("promotionprogram AS promo ON promo.id = i.promotion_program_id ").append("LEFT JOIN ")
						.append("orderlix AS ord ON ord.id = i.order_lix_id ").append("LEFT JOIN ")
						.append("pricingprogram AS pri ON pri.id = i.pricing_program_id ").append("LEFT JOIN ")
						.append("carrier AS carr ON carr.id = i.carrier_id ").append("LEFT JOIN ")
						.append("customer AS c ON c.id = i.customer_id ").append("LEFT JOIN ")
						.append("City AS city ON c.city_id = city.id ").append("LEFT JOIN ")
						.append("Area AS area ON city.area_id = area.id ").append("LEFT JOIN ")
						.append("stocker AS sto ON sto.id = i.stocker_id ").append("LEFT JOIN ")
						.append("customertypes AS cust ON cust.id = c.customer_types_id ").append("LEFT JOIN ")
						.append("customerchannel AS cusch ON cusch.id = cust.channal_id ").append("LEFT JOIN ")

						.append("product AS pd ON pd.id = ind.product_id ").append("LEFT JOIN ")
						.append("productcom AS pdcom ON pdcom.id = pd.product_com_id ").append("LEFT JOIN ")
						.append("productbrand AS pdbrand ON pdcom.product_brand_id = pdbrand.id ").append("LEFT JOIN ")
						.append("product AS pdkm ON pdkm.id = pd.promotion_product_id ").append("LEFT JOIN ")
						.append("nhanHang AS nh ON nh.id = pd.nhanHang_id ");
				sql.append("WHERE ").append("i.id IN (:idInvs) ");
				Query query = em.createNativeQuery(sql.toString());
				query.setParameter("idInvs", invoiceIds);
				list.addAll(query.getResultList());
				res = 0;
			}
		} catch (Exception e) {
			logger.error("InvoiceDetailService.selectByInvoicesToExcel:" + e.getMessage(), e);
		}
		return res;
	}


	public int capnhatdongiakm(long id, double dongia, String userAction) {
		int res = -1;
		try {
			Query query = em
					.createQuery("update InvoiceDetail set unit_price=:dongia, total=(:dongia*real_quantity), act='dongiaKM',useraction=:useraction where id =:id");
			query.setParameter("dongia", dongia);
			query.setParameter("id", id);
			query.setParameter("useraction", userAction);
			res = query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
