package lixco.com.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import lixco.com.commom_ejb.HolderParser;
import lixco.com.commom_ejb.JsonParserUtil;
import lixco.com.commom_ejb.MyMath;
import lixco.com.commom_ejb.MyUtilEJB;
import lixco.com.commom_ejb.ToolTimeCustomer;
import lixco.com.entity.Car;
import lixco.com.entity.Contract;
import lixco.com.entity.Customer;
import lixco.com.entity.CustomerChannel;
import lixco.com.entity.CustomerTypes;
import lixco.com.entity.DeliveryPricing;
import lixco.com.entity.IECategories;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.PaymentMethod;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.Product;
import lixco.com.entity.PromotionProgram;
import lixco.com.entity.Warehouse;
import lixco.com.entityapi.InvoiceApiDTO;
import lixco.com.entityapi.InvoiceAsyncDTO;
import lixco.com.entityapi.InvoiceDTO;
import lixco.com.entityapi.InvoiceDTO2;
import lixco.com.entityapi.InvoiceDetailDTO;
import lixco.com.entityapi.InvoiceDetailDTO2;
import lixco.com.interfaces.ICarService;
import lixco.com.interfaces.ICustomerPricingProgramService;
import lixco.com.interfaces.ICustomerPromotionProgramService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IDeliveryPricingService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IPaymentMethodService;
import lixco.com.interfaces.IPricingProgramDetailService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IStockerService;
import lixco.com.interfaces.IWarehouseService;
import lixco.com.reqInfo.CustomerPricingProgramReqInfo;
import lixco.com.reqInfo.CustomerPromotionProgramReqInfo;
import lixco.com.reqInfo.PricingProgramDetailReqInfo;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Stateless
@TransactionManagement(value = TransactionManagementType.BEAN)
public class InvoiceAPIService {
	@Inject
	private EntityManager em;
	@Resource(lookup = "java:/consumption")
	DataSource datasource;
	@Resource
	private UserTransaction ut;

	@Inject
	ICustomerService customerService;
	@Inject
	IPaymentMethodService paymentMethodService;
	@Inject
	ICarService carService;
	@Inject
	IDeliveryPricingService deliveryPricingService;
	@Inject
	IIECategoriesService ieCategoriesService;
	@Inject
	IWarehouseService warehouseService;
	@Inject
	IStockerService stockerService;

	@Inject
	ICustomerPricingProgramService customerPricingProgramService;
	@Inject
	ICustomerPromotionProgramService customerPromotionProgramService;
	@Inject
	IProductService productService;
	@Inject
	TonKhoThucTeService tonKhoThucTeService;

//	public List<InvoiceDTO2> search(Date startDate, Date endDate, String maxuatnhap, String idloaibo, String statusXN) {
//		try {
//			CriteriaBuilder cb = em.getCriteriaBuilder();
//			CriteriaQuery<InvoiceDTO2> cq = cb.createQuery(InvoiceDTO2.class);
//			Root<Invoice> root_ = cq.from(Invoice.class);
//			Join<Invoice, Customer> customer_ = root_.join("customer", JoinType.LEFT);
//			Join<Invoice, IECategories> iECategories_ = root_.join("ie_categories", JoinType.LEFT);
//			Join<Invoice, Warehouse> warehouse_ = root_.join("warehouse", JoinType.LEFT);
//
//			Join<Invoice, Car> car_ = root_.join("car", JoinType.LEFT);
//			Join<Invoice, Contract> contract_ = root_.join("contract", JoinType.LEFT);
//
//			List<Predicate> predicates = new ArrayList<Predicate>();
//
//			predicates.add(cb.greaterThanOrEqualTo(root_.get("invoice_date"), startDate));
//			predicates.add(cb.lessThanOrEqualTo(root_.get("invoice_date"), endDate));
//
//			if (maxuatnhap != null && !"".equals(maxuatnhap.trim())) {
//				String maxnArray[] = maxuatnhap.split(";");
//				if (maxnArray.length != 0) {
//					List<String> maxns = Arrays.asList(maxnArray);
//					if ("0".equals(statusXN)) {
//						predicates.add(cb.in(iECategories_.get("code")).value(maxns));
//					} else if ("1".equals(statusXN)) {
//						predicates.add(cb.in(iECategories_.get("code")).value(maxns).not());
//					}
//				}
//			}
//			if (idloaibo != null && !"".equals(idloaibo.trim())) {
//				String idloaiboArray[] = idloaibo.split(";");
//				if (idloaiboArray.length != 0) {
//					List<Long> idlbs = new ArrayList<Long>();
//					for (int i = 0; i < idloaiboArray.length; i++) {
//						idlbs.add(Long.parseLong(idloaiboArray[i]));
//					}
//					predicates.add(cb.in(root_.get("id")).value(idlbs).not());
//				}
//			}
//			cq.select(
//					cb.construct(InvoiceDTO2.class, root_.get("id"), root_.get("voucher_code"),
//							root_.get("invoice_date"), customer_.get("customer_code"), customer_.get("customer_name"),
//							root_.get("order_voucher"), root_.get("tax_value"), warehouse_.get("code"),
//							iECategories_.get("code"), iECategories_.get("content"), root_.get("note"),
//							root_.get("invoice_serie"), root_.get("tongtien"), root_.get("thue"),
//							car_.get("license_plate"), contract_.get("voucher_code"), root_.get("po_no"),root_.get("lookup_code")))
//					.where(cb.and(predicates.toArray(new Predicate[0])))
//					.orderBy(cb.asc(root_.get("invoice_date")), cb.asc(root_.get("id")));
//
//			TypedQuery<InvoiceDTO2> query = em.createQuery(cq);
//
//			List<InvoiceDTO2> invoiceDTO2s = query.getResultList();
//			for (int i = 0; i < invoiceDTO2s.size(); i++) {
//				invoiceDTO2s.get(i).setInvoiceDetailDTO2s(findInvoice(invoiceDTO2s.get(i).getId()));
//			}
//			return invoiceDTO2s;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return new ArrayList<InvoiceDTO2>();
//	}
//	

	

	
	
	public List<InvoiceApiDTO> search(Date startDate, Date endDate, String maxuatnhap, String idloaibo,
			String statusXN) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("ive.id AS idInvoice, ");
		sql.append("ive.voucher_code as voucher_codeive, ");
		sql.append("ive.invoice_date as invoice_date, ");
		sql.append("ctum.customer_code AS customer_code, ");
		sql.append("ctum.customer_name AS customer_name, ");
		sql.append("ive.order_voucher AS order_voucher, ");
		sql.append("ive.tax_value AS tax_value, ");
		sql.append("war.code AS codeWarhouse, ");
		sql.append("ieCategories.code AS ieCategories, ");
		sql.append("ieCategories.content AS content, ");
		sql.append("ive.note AS note, ");
		sql.append("ive.invoice_serie AS invoice_serie, ");
		sql.append("ive.tongtien AS tongtien, ");
		sql.append("ive.thue AS thue, ");
		sql.append("c.license_plate AS license_plate, ");
		sql.append("cont.voucher_code AS voucher_codecont, ");
		sql.append("ive.po_no AS po_no, ");
		sql.append("ive.lookup_code AS lookup_code, ");
		sql.append("indl.id AS invoidetailid, ");
		sql.append("pro.product_code AS product_code, ");
		sql.append("pro.product_name AS product_name, ");
		sql.append("indl.quantity AS quantity, ");
		sql.append("indl.unit_price AS unit_price, ");
		sql.append("indl.total AS total, ");
		sql.append("indl.foreign_unit_price AS foreign_unit_price, ");
		sql.append("indl.total_foreign_amount AS total_foreign_amount, ");
		sql.append("indl.productdh_code AS productdh_code, ");
		sql.append("indlown.id AS idindlown ");
		sql.append("FROM consumption.invoice ive ");
		sql.append("LEFT JOIN invoicedetail indl ON ive.id = indl.invoice_id ");
		sql.append("LEFT JOIN IECategories ieCategories ON ieCategories.id = ive.ie_categories_id ");
		sql.append("LEFT JOIN Customer ctum ON ctum.id = ive.customer_id ");
		sql.append("LEFT JOIN product pro ON pro.id = indl.product_id ");
		sql.append("LEFT JOIN warehouse war ON war.id = ive.warehouse_id ");
		sql.append("LEFT JOIN car c ON c.id = ive.car_id ");
		sql.append("LEFT JOIN invoicedetail indlown ON indlown.id = indl.detail_own_id ");
		sql.append("LEFT JOIN contract cont ON cont.id = ive.contract_id ");
		sql.append("WHERE invoice_date BETWEEN :startDate AND :endDate ");

		if (maxuatnhap != null && !maxuatnhap.trim().isEmpty()) {
			String[] maxnArray = maxuatnhap.split(";");
			if (maxnArray.length > 0) {
				if ("0".equals(statusXN)) {
					sql.append("AND ieCategories.code IN (:maxnArray) ");
				} else if ("1".equals(statusXN)) {
					sql.append("AND ieCategories.code NOT IN (:maxnArray) ");
				}
			}
		}

		sql.append("ORDER BY ive.invoice_date DESC, ive.id DESC");

		Query query = em.createNativeQuery(sql.toString());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if (maxuatnhap != null && !maxuatnhap.trim().isEmpty()) {
			query.setParameter("maxnArray", Arrays.asList(maxuatnhap.split(";")));
		}

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();

		// Chuyển đổi danh sách kết quả sang InvoiceApiDTO
		List<InvoiceApiDTO> results = resultList.stream().map(InvoiceApiDTO::new).collect(Collectors.toList());

		return results;
	}

	public List<InvoiceDetailDTO2> findInvoice(long idInvoice) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<InvoiceDetailDTO2> cq = cb.createQuery(InvoiceDetailDTO2.class);
			Root<InvoiceDetail> root_ = cq.from(InvoiceDetail.class);
			Join<InvoiceDetail, Product> product_ = root_.join("product", JoinType.LEFT);
			Join<InvoiceDetail, InvoiceDetail> ivdetail_ = root_.join("invoice_detail_own", JoinType.LEFT);
			cq.select(cb.construct(InvoiceDetailDTO2.class, root_.get("id"), product_.get("product_code"),
					product_.get("product_name"), root_.get("quantity"), root_.get("unit_price"), root_.get("total"),
					root_.get("foreign_unit_price"), root_.get("total_foreign_amount"), root_.get("productdh_code"),
					ivdetail_.get("id"))).where(cb.equal(root_.get("invoice").get("id"), idInvoice));
			TypedQuery<InvoiceDetailDTO2> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<InvoiceDetailDTO2>();
	}

	public int taoHoaDonAPI(InvoiceAsyncDTO invoiceAsyncDTO, StringBuilder errors, boolean kiemton) {
		InvoiceDTO invoiceDTO = invoiceAsyncDTO.getInvoiceDTO();
		List<InvoiceDetailDTO> invoiceDetailDTOs = invoiceDTO.getInvoiceDetailDTOs();
		int res = 1;
		Connection con = null;
		try {
			con = datasource.getConnection();
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			ut.begin();
			Date now = MyUtilEJB.loaibogio(new Date());
			// tạo hóa đơn thứ nhất
			Invoice invoice = new Invoice();
			invoice.setCreated_date(new Date());
			Customer cus = customerService.selectByCode(invoiceDTO.getMakhachhang());
			invoice.setCustomer(cus);
			invoice.setInvoice_date(now);
			invoice.setDelivery_date(now);
			// Chuyen khoan
			PaymentMethod payment = paymentMethodService.selectByCode("CK");
			invoice.setPayment_method(payment);
			Car car = carService.selectByCode(invoiceDTO.getSoxe());
			invoice.setCar(car);
			DeliveryPricing deliv = deliveryPricingService.selectByPlaceCode(invoiceDTO.getManoiden());
			invoice.setDelivery_pricing(deliv);
			// Xuat noi bo
			IECategories iecate = ieCategoriesService.selectByCode("Y");
			invoice.setIe_categories(iecate);
			// Kho san pham
			Warehouse warehouse = warehouseService.selectByCode("F");
			invoice.setWarehouse(warehouse);
			invoice.setTax_value(0.1);// Cai dat mac dinh 10%
			if (!"".equals(invoiceDTO.getMathukho()))
				invoice.setStocker(stockerService.selectByCode(invoiceDTO.getMathukho()));

			/*
			 * Chuong trinh don gia, khuyen mai
			 */
			JsonObject json = new JsonObject();
			json.addProperty("date", ToolTimeCustomer.convertDateToString(invoice.getInvoice_date(), "dd/MM/yyyy"));
			json.addProperty("customer_id", invoice.getCustomer().getId());
			CustomerPricingProgramReqInfo t = new CustomerPricingProgramReqInfo();
			customerPricingProgramService.selectForCustomer(JsonParserUtil.getGson().toJson(json), t);
			PricingProgram pricingProgramTrans = t.getCustomer_pricing_program() == null ? null
					: t.getCustomer_pricing_program().getPricing_program();
			CustomerPromotionProgramReqInfo t1 = new CustomerPromotionProgramReqInfo();
			customerPromotionProgramService.selectForCustomer(JsonParserUtil.getGson().toJson(json), t1);
			PromotionProgram promotionProgramTrans = t1.getCustomer_promotion_program() == null ? null
					: t1.getCustomer_promotion_program().getPromotion_program();

			invoice.setPricing_program(pricingProgramTrans);
			invoice.setPromotion_program(promotionProgramTrans);

			/*
			 * Cai dat don gia cho chi tiet
			 */
			for (int i = 0; i < invoiceDetailDTOs.size(); i++) {
				Product pd = productService.selectByCode(invoiceDetailDTOs.get(i).getProductCode());
				if (pd != null) {
					invoiceDetailDTOs.get(i).setProduct(pd);
					invoiceDetailDTOs.get(i)
							.setDongia(caidatdongia(pricingProgramTrans, invoice.getInvoice_date(), pd.getId()));
					invoiceDetailDTOs.get(i).setThanhtien(MyMath.roundCustom(
							invoiceDetailDTOs.get(i).getQuantity() * invoiceDetailDTOs.get(i).getDongia(), 0));
				} else {
					errors.append("Không tìm thấy mã sản phẩm: " + invoiceDetailDTOs.get(i).getProductCode());
					res = -1;
					ut.rollback();
					return res;
				}
			}

			invoice.setPo_no(invoiceDTO.getPo());
			invoice.setExported(true);
			initCodeVoucher(invoice);

			double totalevent = invoiceDetailDTOs.stream().mapToDouble(f -> f.getThanhtien()).sum();
			invoice.setTongtien(MyMath.round(totalevent));
			invoice.setThue(MyMath.round(totalevent * invoice.getTax_value()));

			em.persist(invoice);
			if (invoice.getId() == 0) {
				res = -1;
				ut.rollback();
				errors.append("Tạo hóa đơn thất bại!");
				return res;
			}
			// tạo chi tiết cho đơn hàng thứ nhất
			for (InvoiceDetailDTO p : invoiceDetailDTOs) {
				InvoiceDetail detail = new InvoiceDetail();
				detail.setInvoice(invoice);
				detail.setCreated_date(new Date());
				detail.setProduct(p.getProduct());
				detail.setQuantity(p.getQuantity());
				detail.setReal_quantity(detail.getQuantity());
				detail.setUnit_price(p.getDongia());
				detail.setTotal(MyMath.round(detail.getQuantity() * detail.getUnit_price()));
				detail.setNote_batch_code(p.getNote_batch_code());
				boolean dutonkho = true;
				double tonkho = 0;
				if (kiemton) {
					tonkho = tonKhoThucTeService.kiemton(detail.getProduct().getId());
					if (tonkho < detail.getQuantity()) {
						dutonkho = false;
					}
				}
				if (dutonkho) {
					if (detail.getQuantity() != 0) {
						em.persist(detail);
						if (detail.getId() == 0) {
							res = -1;
							errors.append("Tạo chi tiết hóa đơn thất bại!");
							ut.rollback();
							return res;
						}
					}
				} else {
					errors.append("Mã SP: " + detail.getProduct().getProduct_code() + " không đủ tồn kho ("
							+ MyUtilEJB.dinhdangso(tonkho / detail.getProduct().getSpecification()) + " thùng)");
					res = -1;
					ut.rollback();
					return res;
				}

			}
			ut.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}

	@Inject
	IPricingProgramDetailService pricingProgramDetailService;

	private double caidatdongia(PricingProgram pricingProgram, Date ngayhoadon, long idsanpham) {
		if (pricingProgram != null) {
			// Tải và cài đặt chương trình đơn giá
			boolean statusPrice = true;
			PricingProgramDetailReqInfo t1 = new PricingProgramDetailReqInfo();
			// Tìm cài đặt chương trình đơn giá con
			long idPriceSub = customerPricingProgramService.selectForCustomerSub(pricingProgram.getId(), ngayhoadon,
					idsanpham);
			if (idPriceSub != 0) {
				pricingProgramDetailService.findSettingPricing(idPriceSub, idsanpham, t1);

				if (t1.getPricing_program_detail() != null) {
					if (t1.getPricing_program_detail().getUnit_price() != 0) {
						return t1.getPricing_program_detail().getUnit_price();
					}
				}
			}
			// Nếu không có ct đơn giá con -> lấy chương trình đơn giá
			// cha
			if (statusPrice) {
				pricingProgramDetailService.findSettingPricing(pricingProgram.getId(), idsanpham, t1);
				if (t1.getPricing_program_detail() != null) {
					if (t1.getPricing_program_detail().getUnit_price() != 0) {
						return t1.getPricing_program_detail().getUnit_price();
					}
				}
			}
		}
		return 0.0;
	}

	private int initCodeVoucher(Invoice t) {
		int res = -1;
		try {
			Date date = t.getInvoice_date();
			int year = ToolTimeCustomer.getYearM(date);
			int month = ToolTimeCustomer.getMonthM(date);
			int day = ToolTimeCustomer.getDayM(date);

			String voucher = day + "" + month + "" + year + "/";
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<String> cq = cb.createQuery(String.class);
			Root<Invoice> root = cq.from(Invoice.class);
			cq.select(root.get("voucher_code"))
					.where(cb.equal(root.get("invoice_date"), ToolTimeCustomer.getFirstDateOfDay(date)))
					.orderBy(cb.desc(root.get("id")));
			TypedQuery<String> query = em.createQuery(cq);
			List<String> list = query.setMaxResults(1).getResultList();

			if (list.size() > 0) {
				String temp = list.get(0);
				if (temp != null) {
					int last = temp.lastIndexOf("/");
					String sub = temp.substring(last + 1);
					voucher = voucher + String.format("%02d", Integer.parseInt(sub) + 1);
					t.setVoucher_code(voucher);
				} else {
					t.setVoucher_code("001");
				}
			} else {
				voucher = voucher + String.format("%02d", 1);
				t.setVoucher_code(voucher);
			}
			res = 0;
		} catch (Exception e) {
			t.setVoucher_code("001");
			e.printStackTrace();
		}
		return res;
	}
}
