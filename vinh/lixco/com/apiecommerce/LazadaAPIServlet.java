package vinh.lixco.com.apiecommerce;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.jsonwebtoken.io.IOException;
import lixco.com.einvoice_service.EInvoiceService;
import lixco.com.entity.Customer;
import lixco.com.entity.EcomOrder;
import lixco.com.entity.EcomOrderDetail;
import lixco.com.entity.IECategories;
import lixco.com.entity.Invoice;
import lixco.com.entity.LazadaToken;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.PricingProgramDetail;
import lixco.com.entity.Product;
import lixco.com.hddt.InvoiceToJson;
import lixco.com.hddt.MisaInvoice;
import lixco.com.hddt.ThongBao;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IIEInvoiceService;
import lixco.com.interfaces.IInvoiceDetailService;
import lixco.com.interfaces.IInvoiceService;
import lixco.com.interfaces.IPricingProgramDetailService;
import lixco.com.interfaces.IPricingProgramService;
import lixco.com.interfaces.IProcessLogicInvoiceService;
import lixco.com.interfaces.IProductService;
import lixco.com.service.EcomOrderDetailService;
import lixco.com.service.EcomOrderService;
import lixco.com.service.LazadaTokenService;

@Path("lazada")
public class LazadaAPIServlet {
	private static final String APP_KEY = "133487";
	private static final String APP_SECRET = "RZTQdjz5VUdnpQ81koTwuQ0lrBJlnepC";
	private static final String API_URL = "https://api.lazada.vn/rest";
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final Logger LOGGER = Logger.getLogger(LazadaAPIServlet.class.getName());
	private static final String ACCESS_TOKEN = "50000101930fF3esqdeGEt6LwVlJlvH4gwyDgaETwf189d45beMu0QMtwiVyAV0q";
	private static final int CONNECTION_TIMEOUT = 60000;
	private static final Object PROCESS_LOCK = new Object();
	private static final String AUTH_URL = "https://auth.lazada.com/rest";
	private static final long EXPIRE_BUFFER_SECONDS = 3600;

	@Inject
	private LazadaTokenService lazadaTokenService;

	@Inject
	private EcomOrderService ecomOrderService;
	@Inject
	private EcomOrderDetailService ecomOrderDetailService;
	@Inject
	private ICustomerService customerService;
	@Inject
	private IProductService productService;
	@Inject
	private IPricingProgramService priceProgramService;
	@Inject
	private IPricingProgramDetailService pricingProgramDetailService;
	@Inject
	private IProcessLogicInvoiceService processLogicInvoiceService;
	@Inject
	private IInvoiceService invoiceService;
	@Inject
	private EInvoiceService eInvoiceService;
	@Inject
	private IInvoiceDetailService invoiceDetailService;
	@Inject
	private IIEInvoiceService ieInvoiceService;
	@Inject
	private IIECategoriesService iieCategoriesService;
	private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
	
	@POST
	@Path("/webhook/order")  
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response webHookLazada(@Context HttpHeaders headers, String inputJson) {
	    ObjectNode errorResponse = mapper.createObjectNode();

	    try {
	        LOGGER.info("Lazada Webhook: Input JSON: " + inputJson);

	        // Verify signature NGAY TRONG MAIN THREAD (rất quan trọng để pass verify nhanh)
	        String authHeader = headers.getRequestHeader("Authorization") != null
	                && !headers.getRequestHeader("Authorization").isEmpty()
	                ? headers.getRequestHeader("Authorization").get(0)
	                : null;
	        String computedSignature = SignatureUtil.getSignature(APP_KEY + inputJson, APP_SECRET);
	        if (authHeader == null || !authHeader.equals(computedSignature)) {
	            errorResponse.put("status", "error");
	            errorResponse.put("message", "Header Authorization không hợp lệ hoặc thiếu");
	            LOGGER.warning("Xác thực chữ ký thất bại: authHeader=" + authHeader + ", computedSignature=" + computedSignature);
	            return Response.status(Response.Status.UNAUTHORIZED).entity(errorResponse)
	                    .type(MediaType.APPLICATION_JSON).build();
	        }

	        // Trả 200 OK NGAY LẬP TỨC để pass verify (không chờ xử lý logic)
	        ObjectNode resp = mapper.createObjectNode();
	        resp.put("status", "success");
	        resp.put("message", "OK");
	        Response okResp = Response.ok(resp).build();

	        // Lấy headers trước async (tránh mất contextual data)
	        Map<String, List<String>> forwardedHeaders = new HashMap<>();
	        headers.getRequestHeaders().forEach((key, values) -> {
	            if (!key.equalsIgnoreCase("Host") &&
	                !key.equalsIgnoreCase("Content-Length") &&
	                !key.equalsIgnoreCase("Connection")) {
	                forwardedHeaders.put(key, new ArrayList<>(values));
	            }
	        });

	        // Async chuyển tiếp toàn bộ inputJson + headers sang module A
	        executorService.submit(() -> {
	            try {
//	                String moduleAUrl = "http://192.168.0.83:8087/consumption/api/lazada/webhook/order";
	            	 String moduleAUrl = "http://192.168.0.6:8980/consumption/api/lazada/webhook/order";
	                LOGGER.info("Bắt đầu chuyển tiếp webhook Lazada sang module A: " + moduleAUrl);

	                URL url = new URL(moduleAUrl);
	                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	                conn.setRequestMethod("POST");
	                conn.setDoOutput(true);
	                conn.setConnectTimeout(15000);
	                conn.setReadTimeout(30000);

	                // Chuyển headers
	                forwardedHeaders.forEach((key, values) -> {
	                    for (String value : values) {
	                        conn.setRequestProperty(key, value);
	                    }
	                });

	                // Gửi body (inputJson)
	                try (OutputStream os = conn.getOutputStream()) {
	                    byte[] input = inputJson.getBytes(StandardCharsets.UTF_8);
	                    os.write(input, 0, input.length);
	                }

	                // Kiểm tra response từ module A
	                int responseCode = conn.getResponseCode();
	                if (responseCode >= 200 && responseCode < 300) {
	                    LOGGER.info("Chuyển tiếp sang module A thành công - status: " + responseCode);
	                } else {
	                    LOGGER.warning("Chuyển tiếp sang module A thất bại - status: " + responseCode);
	                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
	                        StringBuilder error = new StringBuilder();
	                        String line;
	                        while ((line = br.readLine()) != null) {
	                            error.append(line);
	                        }
	                        LOGGER.warning("Error body từ module A: " + error.toString());
	                    } catch (Exception ignore) {}
	                }

	                conn.disconnect();

	            } catch (Exception e) {
	                LOGGER.severe("Lỗi khi chuyển tiếp webhook Lazada sang module A: " + e.getMessage());
	                e.printStackTrace();
	            }
	        });
//	        try {
//	        	 executorService.submit(() -> {
//	 	            try {
////	 	                String moduleAUrl = "http://192.168.0.83:8087/consumption/api/lazada/webhook/order";
//	 	            	 String moduleAUrl = "http://192.168.0.226:63/consumption/api/lazada/webhook/order";
//	 	                LOGGER.info("Bắt đầu chuyển tiếp webhook Lazada sang module A: " + moduleAUrl);
//
//	 	                URL url = new URL(moduleAUrl);
//	 	                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//	 	                conn.setRequestMethod("POST");
//	 	                conn.setDoOutput(true);
//	 	                conn.setConnectTimeout(15000);
//	 	                conn.setReadTimeout(30000);
//
//	 	                // Chuyển headers
//	 	                forwardedHeaders.forEach((key, values) -> {
//	 	                    for (String value : values) {
//	 	                        conn.setRequestProperty(key, value);
//	 	                    }
//	 	                });
//
//	 	                // Gửi body (inputJson)
//	 	                try (OutputStream os = conn.getOutputStream()) {
//	 	                    byte[] input = inputJson.getBytes(StandardCharsets.UTF_8);
//	 	                    os.write(input, 0, input.length);
//	 	                }
//
//	 	                // Kiểm tra response từ module A
//	 	                int responseCode = conn.getResponseCode();
//	 	                if (responseCode >= 200 && responseCode < 300) {
//	 	                    LOGGER.info("Chuyển tiếp sang module A thành công - status: " + responseCode);
//	 	                } else {
//	 	                    LOGGER.warning("Chuyển tiếp sang module A thất bại - status: " + responseCode);
//	 	                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
//	 	                        StringBuilder error = new StringBuilder();
//	 	                        String line;
//	 	                        while ((line = br.readLine()) != null) {
//	 	                            error.append(line);
//	 	                        }
//	 	                        LOGGER.warning("Error body từ module A: " + error.toString());
//	 	                    } catch (Exception ignore) {}
//	 	                }
//
//	 	                conn.disconnect();
//
//	 	            } catch (Exception e) {
//	 	                LOGGER.severe("Lỗi khi chuyển tiếp webhook Lazada sang module A: " + e.getMessage());
//	 	                e.printStackTrace();
//	 	            }
//	 	        });
//			} catch (Exception e2) {
//				// TODO: handle exception
//			}

	        return okResp;

	    } catch (Exception e) {
	        LOGGER.severe("Lỗi xử lý webhook Lazada: " + e.getMessage());
	        e.printStackTrace();
	        errorResponse.put("status", "error");
	        errorResponse.put("message", "Lỗi xử lý webhook: " + e.getMessage());
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse)
	                .type(MediaType.APPLICATION_JSON).build();
	    }
	}
	private OrderDTO createPromoDTO(OrderDTO saleDTO) {
		if (saleDTO == null || saleDTO.getOrderDetails() == null) {
			LOGGER.info("saleDTO hoặc orderDetails null, không tạo promoDTO cho orderId: "
					+ (saleDTO != null ? saleDTO.getOrderId() : "null"));
			return null;
		}

		List<OrderDetailDTO> promoDetails = saleDTO.getOrderDetails().stream()
				.filter(detail -> "PROMO".equals(detail.getOrderDetailType())).collect(Collectors.toList());

		if (promoDetails.isEmpty()) {
			LOGGER.info("Không có chi tiết PROMO cho orderId: " + saleDTO.getOrderId());
			return null;
		}

		OrderDTO promoDTO = new OrderDTO();
		promoDTO.setOrderId(saleDTO.getOrderId());
		promoDTO.setOrder_status(saleDTO.getOrder_status());
		promoDTO.setCreatedAt(saleDTO.getCreatedAt());
		promoDTO.setUpdatedAt(saleDTO.getUpdatedAt());
		promoDTO.setPrice(0.0);
		promoDTO.setShippingFee(0.0);
		promoDTO.seteCommerceType("Lazada");
		promoDTO.setOrderType("PROMO");
		promoDTO.setCustomerFirstName(saleDTO.getCustomerFirstName());
		promoDTO.setCustomerLastName(saleDTO.getCustomerLastName());
		promoDTO.setLastPrice(0.0);
		promoDTO.setSellerDiscount(0.0);
		promoDTO.setTotalSellerDiscount(0.0);
		promoDTO.setDepartmentType("Marketings");
		for (OrderDetailDTO detail : promoDetails) {
			detail.setItemPrice(0.0);
			detail.setLastItemPrice(0.0);
			detail.setUnitPrice(0.0);
		}

		promoDTO.setOrderDetails(promoDetails);
		LOGGER.info("Created promoDTO with " + promoDetails.size() + " details for orderId: " + saleDTO.getOrderId());
		return promoDTO;
	}

	private void saveOrUpdateOrderDetail(List<OrderDetailDTO> orderDetailDTOs, String orderNumber, String platform,
			String orderType, EcomOrder order) {
		if (orderDetailDTOs == null || orderDetailDTOs.isEmpty()) {
			LOGGER.warning("orderDetailDTOs là null hoặc rỗng cho orderType: " + orderType);
			return;
		}
		for (OrderDetailDTO orddt : orderDetailDTOs) {
			if (orddt == null) {
				LOGGER.warning("OrderDetailDTO trong danh sách là null cho orderType: " + orderType);
				continue;
			}
			List<EcomOrderDetail> oldDetails = ecomOrderDetailService.findByCodeAndPlatformAndOrderTypeAndOrderItemId(
					orderNumber, platform, orderType, orddt.getOrderItemId());
			if (oldDetails != null && !oldDetails.isEmpty()) {
				LOGGER.info("Bỏ qua chi tiết đã tồn tại: orderId=" + orderNumber + ", orderItemId="
						+ orddt.getOrderItemId() + ", orderType=" + orderType + ", sku=" + orddt.getSku());
				continue;
			}

			EcomOrderDetail ecomOrddt = new EcomOrderDetail();
			ecomOrddt.setName(orddt.getName());
			ecomOrddt.setItemPrice(orddt.getItemPrice());
			ecomOrddt.setSku(orddt.getSku());
			Product product = productService.selectByCode(orddt.getSku());
			ecomOrddt.setProduct(product);
			ecomOrddt.setOrderItemNumber(orddt.getOrderItemId());
			ecomOrddt.setOrderId(orderNumber);
			ecomOrddt.setLoaitmdt(platform);
			ecomOrddt.setVariant(orddt.isVariant());
			ecomOrddt.setSplitPrice(orddt.getLastItemPrice());
			ecomOrddt.setUnitPrice(orddt.getUnitPrice());
			ecomOrddt.setQuantity(orddt.getQuantity() != 0 ? orddt.getQuantity() : 1);
			ecomOrddt.setOrderType(orddt.getOrderDetailType());
			ecomOrddt.setDataDetailJSON(orddt.getDataDetailJSON());
			ecomOrddt.setOrder(order);
			ecomOrddt.setImageURL(orddt.getImageURL());
			try {
				ecomOrderDetailService.create(ecomOrddt);
				LOGGER.info(
						"Created new order detail: orderId=" + orderNumber + ", orderItemId=" + orddt.getOrderItemId()
								+ ", orderType=" + orddt.getOrderDetailType() + ", sku=" + orddt.getSku()
								+ ", itemPrice=" + orddt.getItemPrice() + ", lastItemPrice=" + orddt.getLastItemPrice()
								+ ", unitPrice=" + orddt.getUnitPrice() + ", quantity=" + orddt.getQuantity());
			} catch (Exception e) {
				LOGGER.severe("Error saving new order detail: orderId=" + orderNumber + ", orderItemId="
						+ orddt.getOrderItemId() + ", orderType=" + orddt.getOrderDetailType() + ", error: "
						+ e.getMessage());
			}
		}
	}

	public void saveOrUpdateOrder(OrderDTO dto) {
		String orderNumber = dto.getOrderId();
		String platform = dto.geteCommerceType();
		String orderType = dto.getOrderType();
		String dataJSON = dto.getDataJson() != null ? dto.getDataJson() : "";

//		EcomOrder existing = ecomOrderService.findByCodeAndPlatformAndOrderType(orderNumber, platform, orderType);
//		if (existing != null) {
//			LOGGER.info(
//					"Đã tồn tại: orderNumber=" + orderNumber + ", platform=" + platform + ", orderType=" + orderType);
//			return;
//		}
//		if(existing == null) {
//			
//		}
		EcomOrder order = new EcomOrder();
		String orderId = dto.getOrderId() + dto.getOrderType();
		order.setOrderId(orderId);
		order.setOrderNumber(orderNumber);
		order.setOrderType(orderType);
		order.setCustomerFirstName(dto.getCustomerFirstName());
		order.setCustomerLastName(dto.getCustomerLastName());
		order.setCreatedAt(dto.getCreatedAt());
		order.setLoaitmdt(platform);
//		order.setDataJson(dataJSON);
		order.setDataJson(null);
		order.setUpdatedAt(dto.getUpdatedAt());
		order.setPrice(dto.getPrice());
		order.setStatus(dto.getOrder_status());
		order.setThoigiancapnhat(new Date());
		order.setComboDiscount(dto.getComboDiscount());
		order.setSellerDiscount(dto.getSellerDiscount());
		order.setDiscountedPrice(dto.getDiscountedPrice());
		order.setLastPrice(dto.getLastPrice());
		order.setShippingFee(dto.getShippingFee());
		order.setShippingDiscount(dto.getShippingDiscount());
		order.setTotalSellerDiscount(dto.getTotalSellerDiscount());
		order.setDepartmentType(dto.getDepartmentType());
		EcomOrderUtils.setMyStatus(order);
		Customer customer = new Customer();
		customer = customerService.selectByCode("00074");
		order.setCustomer(customer);
		if ("SALE".equals(order.getOrderType())) {
			IECategories ieCategories = iieCategoriesService.selectByCode("$");
			order.setIeCategories(ieCategories);
		} else if ("PROMO".equals(order.getOrderType())) {
			IECategories ieCategories = iieCategoriesService.selectByCode("&");
			order.setIeCategories(ieCategories);
		}
		try {
			ecomOrderService.create(order);

		} catch (Exception e) {
			LOGGER.severe("Lỗi lưu/cập nhật đơn hàng: " + orderNumber + ", error: " + e.getMessage());
			return;
		}

		List<EcomOrderDetail> newDetails = new ArrayList<>();
		List<OrderDetailDTO> saleDetails = dto.getOrderDetails().stream()
				.filter(d -> "SALE".equals(d.getOrderDetailType())).collect(Collectors.toList());
		List<OrderDetailDTO> promoDetails = dto.getOrderDetails().stream()
				.filter(d -> "PROMO".equals(d.getOrderDetailType())).collect(Collectors.toList());

		if (!saleDetails.isEmpty()) {
			saveOrUpdateOrderDetail(saleDetails, orderNumber, platform, "SALE", order);

			double totalDiscount = saleDetails.stream()
					.mapToDouble(item -> item.getVoucherSeller() != null ? item.getVoucherSeller() : 0.0).sum();
			if (totalDiscount != 0) {
				EcomOrderDetail discountDetail = new EcomOrderDetail();
				discountDetail.setOrder(order);
				discountDetail.setOrderItemNumber("");
				Product discountProduct = productService.selectByCode("CKDH");
				discountDetail
						.setName(discountProduct != null ? discountProduct.getProduct_name() : "Chiết khấu đơn hàng");
				discountDetail.setProduct(discountProduct);
				discountDetail.setLoaitmdt(platform);
				discountDetail.setVariant(false);
				discountDetail.setQuantity(0);
				double discountAmount = totalDiscount / 1.08;
				discountDetail.setItemPrice(-discountAmount);
				discountDetail.setSplitPrice(-discountAmount);
				discountDetail.setUnitPrice(0.0);
				discountDetail.setOrderId(orderNumber);
				discountDetail.setOrderType("SALE");
				try {
					ecomOrderDetailService.create(discountDetail);
					newDetails.add(discountDetail);
					LOGGER.info("Lưu chi tiết chiết khấu: orderType=SALE, sku=CKDH, itemPrice="
							+ discountDetail.getItemPrice() + ", lastItemPrice=" + discountDetail.getSplitPrice()
							+ ", unitPrice=" + discountDetail.getUnitPrice() + ", quantity="
							+ discountDetail.getQuantity());
				} catch (Exception e) {
					LOGGER.severe("Lỗi khi lưu chi tiết chiết khấu cho orderId=" + orderNumber + ": " + e.getMessage());
				}
			}
		}

		if (!promoDetails.isEmpty()) {
			saveOrUpdateOrderDetail(promoDetails, orderNumber, platform, "PROMO", order);
		}

		List<EcomOrderDetail> updatedDetails = ecomOrderDetailService
				.findByCodeAndPlatformAndOrderTypeAndOrderId(order.getId(), orderNumber, platform, orderType);
		newDetails.addAll(updatedDetails);
		order.setOrderDetails(newDetails);
		try {
			ecomOrderService.update(order);
			LOGGER.info("Cập nhật đơn hàng Lazada với chi tiết: " + orderNumber + " (Type: " + orderType + ")");
		} catch (Exception e) {
			LOGGER.severe("Lỗi cập nhật đơn hàng với chi tiết: " + orderNumber + ", error: " + e.getMessage());
		}
	}

	public OrderDTO fetchOrderData(String orderId) throws Exception {
		Map<String, String> params = new LinkedHashMap<>();
		String accessToken = getAccessToken();
		params.put("access_token", accessToken);
		params.put("app_key", APP_KEY);
		params.put("order_id", orderId);
		params.put("sign_method", "sha256");
		params.put("timestamp", String.valueOf(System.currentTimeMillis()));

		String queryString = buildQueryString("/order/get", params);
		String sign = sign(queryString, APP_SECRET);
		params.put("sign", sign);

		String response = sendGetResponse(API_URL + "/order/get", params);
		JsonNode root = mapper.readTree(response);
		LOGGER.info("Response từ API /order/get cho orderId " + orderId + ": " + response);

		if (!"0".equals(root.path("code").asText())) {
			LOGGER.severe("fetchOrderData thất bại cho orderId " + orderId + ", code: " + root.path("code").asText()
					+ ", message: " + root.path("message").asText());
			OrderDTO defaultDTO = createDefaultOrderDTO(orderId);
			defaultDTO.setDataJson(response);
			return defaultDTO;
		}

		JsonNode data = root.path("data");
		if (data.isMissingNode()) {
			LOGGER.warning("Dữ liệu missing cho orderId " + orderId);
			OrderDTO defaultDTO = createDefaultOrderDTO(orderId);
			defaultDTO.setDataJson(response);
			return defaultDTO;
		}

		String currentStatus = null;
		JsonNode statusesNode = data.path("statuses");
		if (statusesNode.isArray() && statusesNode.size() > 0) {
			currentStatus = statusesNode.get(statusesNode.size() - 1).asText();
		} else {
			LOGGER.warning("statusesNode không hợp lệ cho orderId " + orderId);
		}

		OrderDTO saleDTO = new OrderDTO();
		saleDTO.setOrderId(data.path("order_id").asText(orderId));
		saleDTO.setCustomerFirstName(data.path("customer_first_name").asText(null));
		saleDTO.setCustomerLastName(data.path("customer_last_name").asText(null));
		saleDTO.setCreatedAt(parseDate(data.path("created_at").asText()));
		saleDTO.setUpdatedAt(parseDate(data.path("updated_at").asText()));
		saleDTO.seteCommerceType("Lazada");
		saleDTO.setOrder_status(currentStatus);
		saleDTO.setPrice(data.path("price").asDouble(0.0));
		saleDTO.setOrderType("SALE");
		saleDTO.setDataJson(response);
		saleDTO.setDepartmentType("Marketing");
		double discountShippingFee = data.path("shipping_fee_discount_seller").asDouble(0.0);
		double discountShippingFeePlatform = data.path("shipping_fee_discount_platform").asDouble(0.0);
		double shippingFee = data.path("shipping_fee_original").asDouble(0.0);
		double lastShippingFee = discountShippingFee != 0.0 ? shippingFee - discountShippingFee : shippingFee;
		saleDTO.setShippingFee(lastShippingFee);
		saleDTO.setShippingDiscount(discountShippingFee);

		List<OrderDetailDTO> items = fetchOrderItems(orderId);

		if (items == null) {
			LOGGER.warning("fetchOrderItems trả về null cho orderId " + orderId);
			items = new ArrayList<>();
		}

		List<OrderDetailDTO> saleDetails = items.stream().filter(item -> "SALE".equals(item.getOrderDetailType()))
				.collect(Collectors.toList());

		if (saleDetails == null || saleDetails.isEmpty()) {
			LOGGER.warning("saleDetails is null or empty for orderId: " + orderId);
			for (OrderDetailDTO item : items) {
				if (item != null) {
					item.setLastItemPrice(0.0);
					item.setUnitPrice(0.0);
					item.setItemPrice(0.0);
				}
			}
			saleDTO.setLastPrice(0.0);
			return saleDTO;
		}

		double totalSellerDiscount = saleDetails.stream().filter(item -> item != null)
				.mapToDouble(OrderDetailDTO::getVoucherSeller).sum();

		double shippingFee2 = saleDTO.getShippingFee() != null ? saleDTO.getShippingFee() : 0.0;
		double totalPrice = data.path("price").asDouble(0.0);
		double priceToAllocate = (totalPrice + (shippingFee2 - discountShippingFee - discountShippingFeePlatform))
				/ 1.08;
		saleDTO.setLastPrice(priceToAllocate - totalSellerDiscount);

		if (!saleDetails.isEmpty() && priceToAllocate > 0) {

			double totalItemBase = saleDetails.stream().filter(d -> d != null && d.getItemPrice() != null)
					.mapToDouble(OrderDetailDTO::getItemPrice).sum();

			double allocatedSum = 0.0;
			for (int i = 0; i < saleDetails.size(); i++) {
				OrderDetailDTO detail = saleDetails.get(i);

				double allocatedPrice;
				if (i < saleDetails.size() - 1) {
					// phân bổ theo tỷ lệ itemPrice gốc
					allocatedPrice = (totalItemBase > 0) ? (detail.getItemPrice() / totalItemBase) * priceToAllocate
							: priceToAllocate / saleDetails.size();

					allocatedPrice = Math.round(allocatedPrice * 10000.0) / 10000.0;
					allocatedSum += allocatedPrice;
				} else {
					// dòng cuối = tổng - đã phân bổ
					allocatedPrice = priceToAllocate - allocatedSum;
				}

				// set giá đã phân bổ (theo từng dòng riêng biệt, không gộp trùng SKU)
				double lastItemPrice = allocatedPrice;
				detail.setLastItemPrice(lastItemPrice);
				detail.setUnitPrice(detail.getQuantity() > 0 ? lastItemPrice / detail.getQuantity() : lastItemPrice);
				detail.setItemPrice(detail.getUnitPrice());
			}
		} else {
			LOGGER.info("Không phân bổ priceToAllocate cho orderId " + orderId
					+ " do saleDetails rỗng hoặc priceToAllocate <= 0");
			for (OrderDetailDTO item : saleDetails) {
				if (item != null) {
					item.setLastItemPrice(0.0);
					item.setUnitPrice(0.0);
					item.setItemPrice(0.0);
				}
			}
		}
		saleDTO.setOrderDetails(saleDetails);

//		
//		double sumItemPrice = saleDetails.stream().filter(item -> item != null && item.getItemPrice() != null)
//				.mapToDouble(item -> item.getItemPrice() * item.getQuantity()).sum();
//
//		if (Math.abs(sumItemPrice - priceToAllocate) > 0.01) {
//			LOGGER.warning("⚠️ Tổng itemPrice KHÔNG KHỚP cho orderId " + orderId + " | sumItemPrice=" + sumItemPrice
//					+ ", priceToAllocate=" + priceToAllocate);
//		} else {
//			LOGGER.info("✅ Tổng itemPrice KHỚP cho orderId " + orderId + " | sumItemPrice=" + sumItemPrice
//					+ ", priceToAllocate=" + priceToAllocate);
//		}

		return saleDTO;
	}

	public List<OrderDetailDTO> fetchOrderItems(String orderId) throws Exception {
		Map<String, String> params = new LinkedHashMap<>();
		String accessToken = getAccessToken();
		params.put("access_token", accessToken);
		params.put("app_key", APP_KEY);
		params.put("order_id", orderId);
		params.put("sign_method", "sha256");
		params.put("timestamp", String.valueOf(System.currentTimeMillis()));

		String queryString = buildQueryString("/order/items/get", params);
		String sign = sign(queryString, APP_SECRET);
		params.put("sign", sign);

		String response = sendGetResponse(API_URL + "/order/items/get", params);
		JsonNode root = mapper.readTree(response);
		LOGGER.info("Response từ API /order/items/get cho orderId " + orderId + ": " + response);

		if (!"0".equals(root.path("code").asText())) {
			LOGGER.warning("Lỗi API /order/items/get - code: " + root.path("code").asText() + ", message: "
					+ root.path("message").asText());
			return Collections.emptyList();
		}

		JsonNode data = root.path("data");
		if (!data.isArray()) {
			LOGGER.warning("Dữ liệu không phải mảng cho orderId " + orderId);
			return Collections.emptyList();
		}

		List<OrderDetailDTO> result = new ArrayList<>();
		int index = 1;
		for (JsonNode itemNode : data) {
			double paidPrice = itemNode.path("paid_price").asDouble(0.0);
			boolean isPromo = paidPrice == 0.0;

			OrderDetailDTO item = new OrderDetailDTO();
			item.setSku(itemNode.path("sku").asText(null));
			item.setName(itemNode.path("name").asText(null));
			item.setItemPrice(isPromo ? 0.0 : itemNode.path("item_price").asDouble(0.0));
			item.setOrderItemId(itemNode.path("order_item_id").asText(null));
			item.setOrderId(orderId);
			item.setLoaitmdt("Lazada");
			item.setShippingDiscount(itemNode.path("shipping_fee_discount_seller").asDouble(0.0));
			item.setVoucherSeller(itemNode.path("voucher_seller").asDouble(0.0)
					+ itemNode.path("shipping_fee_discount_seller").asDouble(0.0));
			item.setOrderDetailType(isPromo ? "PROMO" : "SALE");
			item.setStt(index++);
			String variation = itemNode.path("variation").asText();
			item.setVariant(variation != null && !variation.trim().isEmpty());
			if (item.isVariant()) {
				item.setName(item.getName() + " - " + variation);
			}
			item.setQuantity(itemNode.path("quantity").asInt(1));
			item.setImageURL(itemNode.path("product_main_image").asText());
			List<OrderDetailDTO> processedItems = isPromo ? Collections.singletonList(item) : splitSku(item, response);
			result.addAll(processedItems);
			LOGGER.info("Xử lý item " + item.getOrderItemId() + ", orderDetailType=" + item.getOrderDetailType()
					+ ", số lượng mục sau khi xử lý: " + processedItems.size());
		}

		return result;
	}

	private List<OrderDetailDTO> splitSku(OrderDetailDTO detail, String response) {
	    List<OrderDetailDTO> result = new ArrayList<>();

	    String skuString = detail.getSku();
	    if (skuString == null || skuString.trim().isEmpty()) {
	        result.add(detail);
	        return result;
	    }
	    skuString = skuString.trim().toUpperCase();

	   
	    Pattern pattern = Pattern.compile("^(\\d+)C-(.+)$");
	    Matcher matcher = pattern.matcher(skuString);

	    if (matcher.matches()) {
	        int comboMultiplier = Integer.parseInt(matcher.group(1));
	        String realSku = matcher.group(2).trim(); 

	        
	        Product product = productService.selectByCode(realSku);
	        String productName = (product != null && product.getProduct_name() != null) 
	                             ? product.getProduct_name() 
	                             : detail.getName();

	       
	        double originalItemPrice = detail.getItemPrice();
	        double unitPriceAfterTax = originalItemPrice / 1.08; 

	       
	        int qtyPerLine = detail.getQuantity() / comboMultiplier;
	        if (qtyPerLine == 0) qtyPerLine = 1; 


	        OrderDetailDTO sale = new OrderDetailDTO();
	        sale.setOrderItemId(detail.getOrderItemId());
	        sale.setName(productName);
	        sale.setSku(realSku);
	        sale.setQuantity(qtyPerLine);
	        sale.setOrderId(detail.getOrderId());
	        sale.setVariant(detail.isVariant());
	        sale.setLoaitmdt(detail.getLoaitmdt());
	        sale.setPromotionType(detail.getPromotionType());
	        sale.setDiscountedPrice(detail.getDiscountedPrice());
	        sale.setImageURL(detail.getImageURL());
	        sale.setItemPrice(unitPriceAfterTax);
	        sale.setLastItemPrice(unitPriceAfterTax * qtyPerLine);
	        sale.setUnitPrice(unitPriceAfterTax);
	        sale.setOrderDetailType("SALE");
	        sale.setCombo(true);
	        result.add(sale);

	        
	        OrderDetailDTO promo = new OrderDetailDTO();
	        promo.setOrderItemId(detail.getOrderItemId());
	        promo.setName(productName);
	        promo.setSku(realSku);
	        promo.setQuantity(qtyPerLine);
	        promo.setOrderId(detail.getOrderId());
	        promo.setVariant(detail.isVariant());
	        promo.setLoaitmdt(detail.getLoaitmdt());
	        promo.setPromotionType(detail.getPromotionType());
	        promo.setDiscountedPrice(detail.getDiscountedPrice());
	        promo.setImageURL(detail.getImageURL());
	        promo.setItemPrice(unitPriceAfterTax);
	        promo.setLastItemPrice(unitPriceAfterTax * qtyPerLine);
	        promo.setUnitPrice(unitPriceAfterTax);
	        promo.setOrderDetailType("PROMO");
	        promo.setCombo(true);
	        result.add(promo);

	        LOGGER.info("Tách combo Lazada '" + skuString + "' → 2 dòng " + realSku 
	                    + " (quantity mỗi dòng = " + qtyPerLine + "): SALE unitPrice=" 
	                    + unitPriceAfterTax + " | PROMO unitPrice=0, lastItemPrice=0");
	    } else {
	        Product product = productService.selectByCode(skuString);
	        if (product != null && product.getProduct_name() != null) {
	            detail.setName(product.getProduct_name());
	        }
	        result.add(detail);
	    }

	    return result;
	}

	private String sendGetResponse(String urlStr, Map<String, String> params) throws Exception {
		StringBuilder urlWithParams = new StringBuilder(urlStr).append("?");
		for (Map.Entry<String, String> e : params.entrySet()) {
			urlWithParams.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		urlWithParams.setLength(urlWithParams.length() - 1);

		URL url = new URL(urlWithParams.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(CONNECTION_TIMEOUT);
		conn.setReadTimeout(CONNECTION_TIMEOUT);

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			return response.toString();
		} catch (Exception e) {
			LOGGER.severe("Lỗi kết nối API cho URL " + urlWithParams + ": " + e.getMessage());
			throw e;
		} finally {
			conn.disconnect();
		}
	}

	private Date parseDate(String dateStr) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		return sdf.parse(dateStr);
	}

	private String buildQueryString(String api, Map<String, String> params) {
		StringBuilder sb = new StringBuilder(api);
		params.entrySet().stream().sorted(Map.Entry.comparingByKey()).filter(e -> !e.getKey().equals("sign"))
				.forEach(e -> sb.append(e.getKey()).append(e.getValue()));
		return sb.toString();
	}

	private String sign(String data, String secret) throws Exception {
		Mac mac = Mac.getInstance("HmacSHA256");
		SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		mac.init(keySpec);
		byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		for (byte b : raw) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	private OrderDTO createDefaultOrderDTO(String orderId) {
		OrderDTO dto = new OrderDTO();
		dto.setOrderId(orderId);
		dto.seteCommerceType("Lazada");
		dto.setOrder_status("Unknown");
		dto.setPrice(0.0);
		dto.setShippingFee(0.0);
		dto.setSellerDiscount(0.0);
		dto.setTotalSellerDiscount(0.0);
		dto.setLastPrice(0.0);
		dto.setOrderDetails(new ArrayList<>());
		dto.setDataJson("");
		LOGGER.warning("Tạo DTO mặc định cho orderId " + orderId + " do API thất bại");
		return dto;
	}

	private OrderDetailDTO cloneDetail(OrderDetailDTO detail) {
		OrderDetailDTO clone = new OrderDetailDTO();
		clone.setOrderItemId(detail.getOrderItemId());
		clone.setSku(detail.getSku());
		clone.setStt(detail.getStt());
		clone.setQuantity(detail.getQuantity());
		clone.setOrderId(detail.getOrderId());
		clone.setVariant(detail.isVariant());
		clone.setLoaitmdt(detail.getLoaitmdt());
		clone.setName(detail.getName());
		clone.setItemPrice(detail.getItemPrice());
		clone.setLastItemPrice(detail.getLastItemPrice());
		clone.setUnitPrice(detail.getUnitPrice());
		clone.setVoucherSeller(detail.getVoucherSeller());
		clone.setShippingDiscount(detail.getShippingDiscount());
		clone.setOrderDetailType(detail.getOrderDetailType());
		return clone;
	}
	public synchronized String getAccessToken() {
		try {
			LazadaToken tokenEntity = lazadaTokenService.findById(2);
			if (tokenEntity == null) {
				throw new RuntimeException("Chưa có bản ghi token Lazada trong DB. Vui lòng authorize lại.");
			}

			long now = System.currentTimeMillis() / 1000L;

			// Nếu access_token sắp hết hạn → refresh
			if (now >= (tokenEntity.getExpireTime() - EXPIRE_BUFFER_SECONDS)) {
				LOGGER.info("Access token Lazada sắp hết hạn. Đang refresh token...");
				return refreshToken(tokenEntity);
			}

			return tokenEntity.getAccessToken();

		} catch (Exception e) {
			LOGGER.severe("Lỗi lấy AccessToken Lazada: " + e.getMessage());
			throw new RuntimeException("Service Unavailable: Lazada Token Issue", e);
		}
	}

	private String refreshToken(LazadaToken currentToken) throws Exception {
		long timestamp = System.currentTimeMillis();

		Map<String, String> params = new LinkedHashMap<>();
		params.put("app_key", APP_KEY);
		params.put("refresh_token", currentToken.getRefreshToken());
		params.put("sign_method", "sha256");
		params.put("timestamp", String.valueOf(timestamp));

		// Tạo sign (theo cách bạn đang dùng)
		String queryString = buildQueryString("/auth/token/refresh", params);
		String sign = sign(queryString, APP_SECRET);
		params.put("sign", sign);

		// Gọi API refresh (POST hoặc GET đều được, nhưng POST an toàn hơn)
		String responseStr = sendPostForToken(AUTH_URL + "/auth/token/refresh", params);

		JsonNode root = mapper.readTree(responseStr);

		if (!"0".equals(root.path("code").asText())) {
			String msg = root.path("message").asText();
			LOGGER.severe("Refresh token Lazada thất bại: " + msg);

			if (msg.contains("refresh_token") || msg.contains("invalid")) {
				LOGGER.severe("Refresh token Lazada đã hết hạn hoặc không hợp lệ! Cần authorize lại thủ công.");
			}
			throw new IOException("Khong the refresh token: " + msg);
		}

		// Cập nhật token mới
		String newAccessToken = root.path("access_token").asText();
		String newRefreshToken = root.path("refresh_token").asText(); // Lazada thường trả refresh_token mới
		long expiresIn = root.path("expires_in").asLong(); // giây
		long refreshExpiresIn = root.path("refresh_expires_in").asLong();

		if (newAccessToken.isEmpty()) {
			throw new RuntimeException("Không nhận được access_token mới từ Lazada");
		}

		// Cập nhật vào entity
		currentToken.setAccessToken(newAccessToken);
		if (!newRefreshToken.isEmpty()) {
			currentToken.setRefreshToken(newRefreshToken);
		}
		currentToken.setExpireTime(System.currentTimeMillis() / 1000L + expiresIn);
		currentToken.setRefreshTokenExpireTime(System.currentTimeMillis() / 1000L + refreshExpiresIn);

		lazadaTokenService.update(currentToken);

		LOGGER.info("✅ Refresh token Lazada thanh cong. Da cap nhat token Lazada");
		return newAccessToken;
	}

	private String sendPostForToken(String urlStr, Map<String, String> params) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setConnectTimeout(CONNECTION_TIMEOUT);
		conn.setReadTimeout(CONNECTION_TIMEOUT);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (postData.length() > 0) {
				postData.append("&");
			}
			postData.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=")
					.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}
		try (OutputStream os = conn.getOutputStream()) {
			os.write(postData.toString().getBytes(StandardCharsets.UTF_8));
		}

		int responseCode = conn.getResponseCode();
		InputStream is = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();

		StringBuilder response = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
		}

		conn.disconnect();

		if (responseCode != 200) {
			throw new IOException("HTTP " + responseCode + ": " + response.toString());
		}

		return response.toString();
	}

}