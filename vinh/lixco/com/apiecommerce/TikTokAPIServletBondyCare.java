package vinh.lixco.com.apiecommerce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.StaleStateException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import lixco.com.einvoice_service.EInvoiceService;
import lixco.com.entity.Customer;
import lixco.com.entity.EcomOrder;
import lixco.com.entity.EcomOrderDetail;
import lixco.com.entity.IECategories;
import lixco.com.entity.Invoice;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.PricingProgramDetail;
import lixco.com.entity.Product;
import lixco.com.entity.TikTokToken;
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
import lixco.com.service.TikTokTokenService;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okio.Buffer;

@Path("/tiktok2")
public class TikTokAPIServletBondyCare {
	private static final Logger LOGGER = Logger.getLogger(TikTokAPIServletBondyCare.class.getName());
	private static final String APP_KEY = "6jacsip4t547f";
	private static final String APP_SECRET = "45d6498f431acf4746c4c68854064b1eb88a66c6";
	private static final String SHOP_CIPHER = "ROW_pV9gNgAAAADct9_dRXml2qbuLcEOnpH4";
	private static final Object PROCESS_LOCK = new Object();
	private final okhttp3.OkHttpClient httpClient = new okhttp3.OkHttpClient();
	private final ObjectMapper mapper = new ObjectMapper();

	@Inject
	private EcomOrderService ecomOrderService;
	@Inject
	private EcomOrderDetailService ecomOrderDetailService;
	@Inject
	private TikTokTokenManager tokenManager;
	@Inject
	private IPricingProgramService priceProgramService;
	@Inject
	private IPricingProgramDetailService pricingProgramDetailService;
	@Inject
	private IProductService productService;
	@Inject
	private ICustomerService customerService;
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
	@Path("/webhooktiktok/ordertiktok")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response receiveOrderWebhook(@Context HttpHeaders headers, String rawBody) {
		ObjectNode errorResponse = mapper.createObjectNode();

		try {
			LOGGER.info("TikTok Webhook: Input JSON: " + rawBody);

			if (rawBody == null || rawBody.trim().isEmpty()) {
				errorResponse.put("status", "error");
				errorResponse.put("message", "Empty or invalid payload");
				return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse)
						.type(MediaType.APPLICATION_JSON).build();
			}

			String authHeader = headers.getRequestHeader("Authorization") != null
					&& !headers.getRequestHeader("Authorization").isEmpty()
							? headers.getRequestHeader("Authorization").get(0)
							: null;

			String generatedSignature = generateWebhookSignature(rawBody, APP_SECRET, APP_KEY);

			if (authHeader == null || !generatedSignature.equals(authHeader)) {
				errorResponse.put("status", "error");
				errorResponse.put("message", "Invalid signature");
				LOGGER.warning("Xác thực chữ ký TikTok thất bại: authHeader=" + authHeader + ", generatedSignature="
						+ generatedSignature);
				return Response.status(Response.Status.UNAUTHORIZED).entity(errorResponse)
						.type(MediaType.APPLICATION_JSON).build();
			}

			ObjectNode resp = mapper.createObjectNode();
			resp.put("status", "success");
			resp.put("message", "OK");
			Response okResp = Response.ok(resp).build();

			Map<String, List<String>> forwardedHeaders = new HashMap<>();
			headers.getRequestHeaders().forEach((key, values) -> {
				if (!key.equalsIgnoreCase("Host") && !key.equalsIgnoreCase("Content-Length")
						&& !key.equalsIgnoreCase("Connection")) {
					forwardedHeaders.put(key, new ArrayList<>(values));
				}
			});

			// === ASYNC CHUYỂN TIẾP SANG MODULE A ===
			executorService.submit(() -> {
				try {
//					String moduleAUrl = "http://192.168.0.83:8087/consumption/api/tiktok2/webhooktiktok/ordertiktok";
					String moduleAUrl = "http://192.168.0.6:8980/consumption/api/tiktok2/webhooktiktok/ordertiktok";
					LOGGER.info("Bắt đầu chuyển tiếp webhook TikTok sang module A: " + moduleAUrl);

					URL url = new URL(moduleAUrl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setConnectTimeout(15000);
					conn.setReadTimeout(30000);

					forwardedHeaders.forEach((key, values) -> {
						for (String value : values) {
							conn.setRequestProperty(key, value);
						}
					});

					try (OutputStream os = conn.getOutputStream()) {
						byte[] input = rawBody.getBytes(StandardCharsets.UTF_8);
						os.write(input, 0, input.length);
					}

					int responseCode = conn.getResponseCode();
					if (responseCode >= 200 && responseCode < 300) {
						LOGGER.info("Chuyển tiếp webhook TikTok sang module A thành công - status: " + responseCode);
					} else {
						LOGGER.warning("Chuyển tiếp sang module A thất bại - status: " + responseCode);
						try (BufferedReader br = new BufferedReader(
								new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
							StringBuilder error = new StringBuilder();
							String line;
							while ((line = br.readLine()) != null) {
								error.append(line);
							}
							LOGGER.warning("Error body từ module A: " + error.toString());
						} catch (Exception ignore) {
						}
					}

					conn.disconnect();

				} catch (Exception e) {
					LOGGER.severe("Lỗi khi chuyển tiếp webhook TikTok sang module A: " + e.getMessage());
					e.printStackTrace();
				}
			});

			return okResp;

		} catch (Exception e) {
			LOGGER.severe("Lỗi xử lý webhook TikTok: " + e.getMessage());
			e.printStackTrace();
			errorResponse.put("status", "error");
			errorResponse.put("message", "Lỗi xử lý webhook: " + e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse)
					.type(MediaType.APPLICATION_JSON).build();
		}
	}
//	private OrderDTO createPromoDTO(OrderDTO saleDTO) {
//		List<OrderDetailDTO> promoDetails = saleDTO.getOrderDetails().stream()
//				.filter(detail -> "PROMO".equals(detail.getOrderDetailType())).collect(Collectors.toList());
//
//		if (promoDetails.isEmpty()) {
//			LOGGER.info("Không có chi tiết PROMO cho orderId: " + saleDTO.getOrderId());
//			return null;
//		}
//
//		OrderDTO promoDTO = new OrderDTO();
//		promoDTO.setOrderId(saleDTO.getOrderId());
//		promoDTO.setOrder_status(saleDTO.getOrder_status());
//		promoDTO.setCreatedAt(saleDTO.getCreatedAt());
//		promoDTO.setUpdatedAt(saleDTO.getUpdatedAt());
//		promoDTO.setPrice(0.0);
//		promoDTO.setShippingFee(0.0);
//		promoDTO.seteCommerceType("TikTok");
//		promoDTO.setOrderType("PROMO");
//		promoDTO.setCustomerFirstName(saleDTO.getCustomerFirstName());
//		promoDTO.setCustomerLastName(saleDTO.getCustomerLastName());
//		promoDTO.setLastPrice(0.0);
//		promoDTO.setSellerDiscount(0.0);
//		promoDTO.setShippingDiscount(0.0);
//		promoDTO.setTotalSellerDiscount(0.0);
//		promoDTO.setDataJson(saleDTO.getDataJson());
//		promoDTO.setPaymentDataJson("{}");
//
//		for (OrderDetailDTO detail : promoDetails) {
//			detail.setItemPrice(0.0);
//			detail.setLastItemPrice(0.0);
//			detail.setUnitPrice(0.0);
//		}
//
//		promoDTO.setOrderDetails(promoDetails);
//		LOGGER.info("Created promoDTO with " + promoDetails.size() + " details for orderId: " + saleDTO.getOrderId());
//		return promoDTO;
//	}

	@POST
	@Path("/get/price")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JsonNode fetchOrderPriceDetail(String requestBody) throws IOException {
		String accessToken =getAccessToken();
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		JsonNode requestJson = mapper.readTree(requestBody);
		String orderId = requestJson.path("orderId").asText(null); 
		
		String path = "finance/202309/orders/" + orderId + "/statement_transactions";

		HttpUrl.Builder urlBuilder = new HttpUrl.Builder().scheme("https").host("open-api.tiktokglobalshop.com")
				.addPathSegments(path).addQueryParameter("app_key", APP_KEY).addQueryParameter("timestamp", timestamp)
				.addQueryParameter("shop_cipher", SHOP_CIPHER).addQueryParameter("shop_id", "1730085241183701031")
				.addQueryParameter("version", "202309");

		// Build request không sign trước
		Request unsignedRequest = new Request.Builder().url(urlBuilder.build()).get()
				.header("x-tts-access-token", accessToken).header("Content-Type", "application/json").build();
		
		

		String sign = generateSignature(unsignedRequest, APP_SECRET);
		HttpUrl signedUrl = urlBuilder.addQueryParameter("sign", sign).build();
		Request signedRequest = unsignedRequest.newBuilder().url(signedUrl).build();
		int maxRetries = 5;
		long delayMillis = 5000;
		IOException lastException = null;
		
		
//		String path = "finance/202309/orders/" + orderId + "/statement_transactions";
//
//		HttpUrl.Builder urlBuilder = new HttpUrl.Builder().scheme("https").host("open-api.tiktokglobalshop.com")
//				.addPathSegments(path).addQueryParameter("app_key", APP_KEY).addQueryParameter("timestamp", timestamp)
//				.addQueryParameter("shop_cipher", SHOP_CIPHER).addQueryParameter("shop_id", "1730085241183701031")
//				.addQueryParameter("version", "202309");
//
//		// Build request không sign trước
//		Request unsignedRequest = new Request.Builder().url(urlBuilder.build()).get()
//				.header("x-tts-access-token", accessToken).header("Content-Type", "application/json").build();
//
//		// Tạo signature
//		String sign = generateSignature(unsignedRequest, APP_SECRET);
//
//		// Thêm sign vào URL
//		HttpUrl signedUrl = urlBuilder.addQueryParameter("sign", sign).build();
//		Request signedRequest = unsignedRequest.newBuilder().url(signedUrl).build();
//
//		int maxRetries = 5;
//		long delayMillis = 5000;
//		IOException lastException = null;


		for (int retry = 1; retry <= maxRetries; retry++) {
			okhttp3.Response response = null;
			try {
				response = httpClient.newCall(signedRequest).execute();
				if (!response.isSuccessful()) {
					String errorBody = response.body() != null ? response.body().string() : "";
					LOGGER.warning("HTTP error code: " + response.code() + ", Response: " + errorBody);
					throw new IOException("HTTP error code: " + response.code() + ", Response: " + errorBody);
				}

				String responseBody = response.body().string();
				JsonNode root = mapper.readTree(responseBody);
				if (!"0".equals(root.path("code").asText())) {
					LOGGER.warning("API /order/202407/orders/" + orderId + "/price_detail failed - code: "
							+ root.path("code").asText() + ", message: " + root.path("message").asText());
					throw new IOException("API error code: " + root.path("code").asText());
				}
				return root.path("data");
			} catch (IOException ex) {
				lastException = ex;
				LOGGER.warning("Attempt " + retry + " failed for price_detail API: " + ex.getMessage());
				if (retry < maxRetries) {
					try {
						Thread.sleep(delayMillis);
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
					}
				}
			} finally {
				if (response != null) {
					response.close();
				}
			}
		}
		throw new IOException("Lỗi fetch dữ liệu price_detail sau nhiều lần.", lastException);
	}

	@POST
	@Path("/get/order")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchOrder(String requestBody) throws Exception {
		JsonNode requestJson = mapper.readTree(requestBody);
		String orderId = requestJson.path("orderId").asText(null);
		if (orderId == null || orderId.trim().isEmpty()) {
			throw new IllegalArgumentException("orderId cannot be null or empty");
		}

		int maxTokenRefreshAttempts = 2;
		for (int attempt = 1; attempt <= maxTokenRefreshAttempts; attempt++) {
			String accessToken =getAccessToken();
			String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

			HttpUrl.Builder urlBuilder = new HttpUrl.Builder().scheme("https").host("open-api.tiktokglobalshop.com")
					.addPathSegments("order/202309/orders").addQueryParameter("app_key", APP_KEY)
					.addQueryParameter("timestamp", timestamp).addQueryParameter("shop_cipher", SHOP_CIPHER)
					.addQueryParameter("ids", orderId);

			Request unsignedRequest = new Request.Builder().url(urlBuilder.build()).get()
					.header("x-tts-access-token", accessToken).header("Content-Type", "application/json").build();

			String sign = generateSignature(unsignedRequest, APP_SECRET);
			HttpUrl signedUrl = urlBuilder.addQueryParameter("sign", sign).build();
			Request signedRequest = unsignedRequest.newBuilder().url(signedUrl).build();
			int maxRetries = 5;
			long delayMillis = 5000;
			IOException lastException = null;

			for (int retry = 1; retry <= maxRetries; retry++) {
				okhttp3.Response response = null;
				try {
					response = httpClient.newCall(signedRequest).execute();
					if (!response.isSuccessful()) {
						String errorBody = response.body() != null ? response.body().string() : "";
						LOGGER.warning("HTTP error code: " + response.code() + ", Response: " + errorBody);
						JsonNode errorJson = mapper.readTree(errorBody);
						if (errorJson.path("code").asInt() == 36009004 && attempt < maxTokenRefreshAttempts) {
							LOGGER.info("Token hết hạn. Đang tạo mới token.");
							tokenManager.getAccessToken();
							break;
						}
						throw new IOException("HTTP error code: " + response.code() + ", Response: " + errorBody);
					}
					int responseCode = response.code();
					String responseBody = response.body().string();
					if (responseCode != 200) {
						throw new IOException("HTTP error code: " + responseCode + ", Response: " + responseBody);
					}

					LOGGER.info("Module B gọi Tiktok thành công cho orderSn: " + orderId);
					return Response.ok(responseBody).type(MediaType.APPLICATION_JSON).build();
//					JsonNode root = mapper.readTree(responseBody);
//					JsonNode ordersNode = root.path("data").path("orders");
//
//					if (!ordersNode.isArray() || ordersNode.size() == 0) {
//						LOGGER.warning("TikTok returned empty orders for orderId: " + orderId);
//						return createDefaultOrderDTO(orderId);
//					}
//
//					JsonNode orderData = ordersNode.get(0);
//					JsonNode priceDetailData = fetchOrderPriceDetail(orderId, accessToken, timestamp);
//					OrderDTO dto = parseOrderDTO(orderData, priceDetailData, responseBody);
//					return dto;

				} catch (IOException ex) {
					lastException = ex;
					LOGGER.warning("Attempt " + retry + " failed: " + ex.getMessage());
					if (retry < maxRetries) {
						Thread.sleep(delayMillis);
					}
				} finally {
					if (response != null) {
						response.close();
					}
				}
			}
		}

		throw new IOException("Lỗi fetch dữ liệu order sau nhiều lần.");
	}
	private OrderDTO parseOrderDTO(JsonNode orderData, JsonNode priceDetailData, String responseBody) {
		OrderDTO saleDTO = new OrderDTO();
		saleDTO.setOrderId(orderData.path("id").asText());
		saleDTO.setOrder_status(orderData.path("status").asText(""));
		saleDTO.setCreatedAt(fromUnixTime(orderData.path("create_time").asLong(0)));
		saleDTO.setUpdatedAt(fromUnixTime(orderData.path("update_time").asLong(0)));
		saleDTO.setPrice(orderData.path("payment").path("original_total_product_price").asDouble(0.0));
		saleDTO.setShippingFee(orderData.path("payment").path("original_shipping_fee").asDouble(0.0)
				- orderData.path("payment").path("shipping_fee_platform_discount").asDouble(0.0));
		saleDTO.seteCommerceType("TikTok");
		saleDTO.setOrderType("SALE");
		saleDTO.setLastPrice(orderData.path("payment").path("original_total_product_price").asDouble(0.0)
				- orderData.path("payment").path("seller_discount").asDouble(0.0));
		JsonNode recipient = orderData.path("recipient_address");
		saleDTO.setCustomerFirstName(recipient.path("first_name").asText(""));
		saleDTO.setCustomerLastName(recipient.path("last_name").asText(""));
		saleDTO.setDataJson(responseBody);

		double voucherSeller = priceDetailData.path("voucher_deduction_seller").asDouble(0.0);
		double shippingFeeSellerDiscount = priceDetailData.path("shipping_fee_deduction_seller").asDouble(0.0);
		saleDTO.setSellerDiscount(voucherSeller);
		saleDTO.setShippingDiscount(shippingFeeSellerDiscount);
		saleDTO.setTotalSellerDiscount(-(voucherSeller + shippingFeeSellerDiscount));

		List<OrderDetailDTO> saleDetails = new ArrayList<>();
		List<OrderDetailDTO> promoDetails = new ArrayList<>();
		JsonNode items = orderData.path("line_items");
		int index = 1;

		for (JsonNode item : items) {
			double salePrice = item.path("original_price").asDouble(0.0) - item.path("seller_discount").asDouble(0.0);
			boolean isPromo = salePrice == 0.0;

			OrderDetailDTO detail = new OrderDetailDTO();
			detail.setOrderItemId(item.path("id").asText());
			detail.setName(item.path("product_name").asText(""));
			detail.setSku(item.path("seller_sku").asText(""));
			detail.setQuantity(item.path("quantity").asInt(1));
			detail.setOrderId(saleDTO.getOrderId());
			detail.setLoaitmdt("TikTok");
			detail.setVariant(item.has("combined_listing_skus") && item.path("combined_listing_skus").size() > 0);
			detail.setStt(index++);
			detail.setOrderDetailType(isPromo ? "PROMO" : "SALE");
			detail.setItemPrice(salePrice);
			detail.setShippingDiscount(item.path("shipping_fee_deduction_seller").asDouble(0.0));
			detail.setVoucherSeller(item.path("voucher_deduction_seller").asDouble(0.0));
			detail.setImageURL(item.path("sku_image").asText(""));
			List<OrderDetailDTO> processedItems = isPromo ? Collections.singletonList(detail) : splitSku(detail);
			if (isPromo) {
				detail.setLastItemPrice(0.0);
				detail.setUnitPrice(0.0);
				promoDetails.addAll(processedItems);
			} else {
				saleDetails.addAll(processedItems);
			}

		}

		List<OrderDetailDTO> allDetails = new ArrayList<>(saleDetails);
		allDetails.addAll(promoDetails);
		saleDTO.setOrderDetails(allDetails);

		allocateLastPrice(saleDTO);

		return saleDTO;
	}

	public void saveOrUpdateOrder(OrderDTO dto) {
		String orderNumber = dto.getOrderId().trim();
		String platform = dto.geteCommerceType().trim();
		String orderType = dto.getOrderType();
		String dataJSON = dto.getDataJson() != null ? dto.getDataJson() : "";

		EcomOrder existing = ecomOrderService.findByCodeAndPlatformAndOrderType(orderNumber, platform, orderType);
		if (existing != null) {
			LOGGER.info(
					"Đã tồn tại: orderNumber=" + orderNumber + ", platform=" + platform + ", orderType=" + orderType);
			return;
		}

		EcomOrder order = new EcomOrder();
		String orderId = dto.getOrderId() + dto.getOrderType();
		order.setOrderId(orderId);
		order.setOrderNumber(orderNumber);
		order.setOrderType(orderType);
		order.setLoaitmdt(platform);
		order.setCreatedAt(dto.getCreatedAt());
		order.setStatus(dto.getOrder_status());
		order.setPrice(dto.getPrice());
		order.setCustomerFirstName(dto.getCustomerFirstName());
		order.setCustomerLastName(dto.getCustomerLastName());
		order.setUpdatedAt(dto.getUpdatedAt());
		order.setThoigiancapnhat(new Date());
		order.setShippingFee(dto.getShippingFee());
		order.setLastPrice(dto.getLastPrice());
		order.setDataJson(dataJSON);
		order.setSellerDiscount(dto.getSellerDiscount());
		order.setShippingDiscount(dto.getShippingDiscount());
		order.setTotalSellerDiscount(dto.getTotalSellerDiscount());

		String paymentDataJson = dto.getPaymentDataJson();
		int maxLength = 255;
		if (paymentDataJson != null && paymentDataJson.length() > maxLength) {

			paymentDataJson = paymentDataJson.substring(0, maxLength);
		}
		order.setPaymentDataJson(paymentDataJson);

		Customer customer = customerService.selectByCode("OL249");
		order.setCustomer(customer);
		EcomOrderUtils.setMyStatus(order);
		if ("SALE".equals(order.getOrderType())) {
			IECategories ieCategories = iieCategoriesService.selectByCode("$");
			order.setIeCategories(ieCategories);
		} else if ("PROMO".equals(order.getOrderType())) {
			IECategories ieCategories = iieCategoriesService.selectByCode("&");
			order.setIeCategories(ieCategories);
		}

		try {
			ecomOrderService.create(order);
			LOGGER.info("Saved new TikTok order: " + orderNumber + " (Type: " + orderType + "), TotalSellerDiscount="
					+ order.getTotalSellerDiscount());
		} catch (StaleStateException e) {
			LOGGER.severe("Lỗi lưu/cập nhật đơn hàng: " + orderNumber + ", error: " + e.getMessage());
			return;
		}

		List<EcomOrderDetail> newDetails = new ArrayList<>();
		for (OrderDetailDTO orddt : dto.getOrderDetails()) {
			if (orddt == null) {
				LOGGER.warning("OrderDetailDTO trong danh sách là null cho orderType: " + orderType);
				continue;
			}

			EcomOrderDetail ecomOrddt = new EcomOrderDetail();
			ecomOrddt.setName(orddt.getName());
			ecomOrddt.setItemPrice(orddt.getItemPrice());
			ecomOrddt.setSku(orddt.getSku());
			Product product = productService.selectByCode(orddt.getSku());
			ecomOrddt.setProduct(product);
			ecomOrddt.setOrderItemNumber(orddt.getOrderItemId());
			ecomOrddt.setOrderId(orddt.getOrderId());
			ecomOrddt.setLoaitmdt("TikTok");
			ecomOrddt.setVariant(orddt.isVariant());
			ecomOrddt.setSplitPrice(orddt.getLastItemPrice());
			ecomOrddt.setUnitPrice(orddt.getUnitPrice());
			ecomOrddt.setQuantity(orddt.getQuantity() != 0 ? orddt.getQuantity() : 1);
			ecomOrddt.setOrderType(orderType);
			ecomOrddt.setImageURL(orddt.getImageURL());
			ecomOrddt.setOrder(order);

			try {
				ecomOrderDetailService.create(ecomOrddt);
				newDetails.add(ecomOrddt);
				LOGGER.info("Created new order detail: orderId=" + orderNumber + ", orderItemNumber="
						+ orddt.getOrderItemId() + ", orderType=" + orddt.getOrderDetailType() + ", sku="
						+ orddt.getSku() + ", itemPrice=" + orddt.getItemPrice() + ", lastItemPrice="
						+ orddt.getLastItemPrice() + ", unitPrice=" + orddt.getUnitPrice() + ", quantity="
						+ orddt.getQuantity());
			} catch (Exception e) {
				LOGGER.severe("Error saving new order detail: orderId=" + orderNumber + ", orderItemNumber="
						+ orddt.getOrderItemId() + ", orderType=" + orddt.getOrderDetailType() + ", error: "
						+ e.getMessage());
			}
		}

		order.setOrderDetails(newDetails);
		try {
			ecomOrderService.update(order);
			LOGGER.info("Cập nhật đơn hàng TikTok với chi tiết: " + orderNumber + " (Type: " + orderType
					+ "), TotalSellerDiscount=" + order.getTotalSellerDiscount());
		} catch (Exception e) {
			LOGGER.severe("Lỗi cập nhật đơn hàng với chi tiết: " + orderNumber + ", error: " + e.getMessage());
		}
	}

	private List<OrderDetailDTO> splitSku(OrderDetailDTO detail) {
		if (customerService == null) {
			List<OrderDetailDTO> result = new ArrayList<>();
			result.add(detail);
			return result;
		}

		Customer customer = null;
		try {
			customer = customerService.selectByCode("CO648");
		} catch (Exception e) {
			LOGGER.warning("Error fetching customer CO648: " + e.getMessage());
			List<OrderDetailDTO> result = new ArrayList<>();
			result.add(detail);
			return result;
		}
		if (customer == null) {
			LOGGER.warning("Không thấy mã KH CO648");
			List<OrderDetailDTO> result = new ArrayList<>();
			result.add(detail);
			return result;
		}

		PricingProgram pricingProgram = priceProgramService.findByCode("DG027968");
		if (pricingProgram == null) {
			LOGGER.warning("Pricing program DG027968 not found");
			List<OrderDetailDTO> result = new ArrayList<>();
			result.add(detail);
			return result;
		}

		List<PricingProgramDetail> pricingProgramDetails = pricingProgramDetailService
				.findAllByPricingProgram(pricingProgram.getId());

		List<OrderDetailDTO> result = new ArrayList<>();
		String skuString = detail.getSku();

		if (skuString == null || skuString.trim().isEmpty()) {
			result.add(detail);
			return result;
		}

		skuString = skuString.trim();
		int originalQuantity = detail.getQuantity();

		Pattern pattern = Pattern.compile("^(\\d+)C\\s*(?:\\+|\\-)\\s*(.+)$");
		Matcher matcher = pattern.matcher(skuString);

		if (matcher.matches()) {
			int prefixNumber = Integer.parseInt(matcher.group(1));
			String remainingSku = matcher.group(2).trim();
			String[] skuParts = remainingSku.split("\\s*\\+\\s*");

			if (skuParts.length >= 1) {
				if (skuParts.length == 1) {
					OrderDetailDTO newDetail = createDetailWithPricing(detail, skuParts[0].trim(),
							prefixNumber * originalQuantity, pricingProgramDetails);
					result.add(newDetail);
				} else if (skuParts.length == 2) {
					OrderDetailDTO detailA = createDetailWithPricing(detail, skuParts[0].trim(),
							prefixNumber * originalQuantity, pricingProgramDetails);
					detailA.setCombo(true);
					OrderDetailDTO detailB = createDetailWithPricing(detail, skuParts[1].trim(), 1 * originalQuantity,
							pricingProgramDetails);
					detailB.setCombo(true);
					result.add(detailA);
					result.add(detailB);
				} else if (skuParts.length >= 3) {
					OrderDetailDTO detailA = createDetailWithPricing(detail, skuParts[0].trim(),
							prefixNumber * originalQuantity, pricingProgramDetails);
					detailA.setCombo(true);
					result.add(detailA);
					for (int i = 1; i < skuParts.length; i++) {
						OrderDetailDTO newDetail = createDetailWithPricing(detail, skuParts[i].trim(),
								1 * originalQuantity, pricingProgramDetails);
						newDetail.setCombo(true);
						result.add(newDetail);
					}
				}
			}
		} else {
			String[] skuParts = skuString.split("\\s*\\+\\s*");
			if (skuParts.length > 1) {
				if (skuParts.length == 2) {
					OrderDetailDTO detailA = createDetailWithPricing(detail, skuParts[0].trim(), 1 * originalQuantity,
							pricingProgramDetails);
					OrderDetailDTO detailB = createDetailWithPricing(detail, skuParts[1].trim(), 1 * originalQuantity,
							pricingProgramDetails);
					detailA.setCombo(true);
					detailB.setCombo(true);
					result.add(detailA);
					result.add(detailB);
				} else {
					for (String part : skuParts) {
						if (!part.trim().isEmpty()) {
							OrderDetailDTO newDetail = createDetailWithPricing(detail, part.trim(),
									1 * originalQuantity, pricingProgramDetails);
							result.add(newDetail);
						}
					}
				}
			} else {
				OrderDetailDTO newDetail = createDetailWithPricing(detail, skuString, originalQuantity,
						pricingProgramDetails);
				result.add(newDetail);
			}
		}

		return result;
	}

	private OrderDetailDTO createDetailWithPricing(OrderDetailDTO originalDetail, String sku, int quantity,
			List<PricingProgramDetail> pricingDetails) {
		if (productService == null) {
			LOGGER.severe("productService is null, injection failed");
			return new OrderDetailDTO();
		}

		OrderDetailDTO newDetail = new OrderDetailDTO();
		newDetail.setOrderItemId(originalDetail.getOrderItemId());
		newDetail.setSku(sku);
		newDetail.setStt(originalDetail.getStt());
		newDetail.setQuantity(quantity);
		newDetail.setOrderId(originalDetail.getOrderId());
		newDetail.setVariant(originalDetail.isVariant());
		newDetail.setLoaitmdt(originalDetail.getLoaitmdt());
		newDetail.setPromotionType(originalDetail.getPromotionType());
		newDetail.setDiscountedPrice(originalDetail.getDiscountedPrice());
		newDetail.setItemPrice(originalDetail.getItemPrice());
		newDetail.setImageURL(originalDetail.getImageURL());
		boolean isPromo = sku.toUpperCase().contains("QT");
		newDetail.setOrderDetailType(isPromo ? "PROMO" : "SALE");

		Product product = productService.selectByCode(sku);
		if (product != null && product.getProduct_name() != null) {
			newDetail.setName(product.getProduct_name());
		} else {
			LOGGER.warning("Không tìm thấy sản phẩm: " + sku + ", trong chương trình TTSP");
			newDetail.setName(originalDetail.getName());
		}

		double itemPrice = findItemPriceForSku(sku, pricingDetails);
		newDetail.setUnitPrice(itemPrice);
		originalDetail.setUnitPrice(itemPrice);
		newDetail.setLastItemPrice(isPromo ? 0.0 : 0.0);
		newDetail.setUnitPrice(isPromo ? 0.0 : 0.0);

		return newDetail;
	}

	private double findItemPriceForSku(String sku, List<PricingProgramDetail> pricingDetails) {
		Product product = productService.selectByCode(sku);
		if (product == null || product.getProduct_code() == null) {
			LOGGER.warning("Product not found for SKU: " + sku);
			return 0.0;
		}

		return pricingDetails.stream().filter(item -> item.getProduct() != null)
				.filter(item -> product.getProduct_code().equals(item.getProduct().getProduct_code()))
				.mapToDouble(item -> item.getUnit_price()).findFirst().orElse(0.0);
	}

	private void allocateLastPrice(OrderDTO dto) {
		if (dto == null || dto.getOrderDetails() == null || dto.getOrderDetails().isEmpty()) {
			return;
		}

		// Tính priceToAllocate bằng tổng itemPrice của các chi tiết SALE
		List<OrderDetailDTO> allSaleSplitDetails = dto.getOrderDetails().stream()
				.filter(item -> "SALE".equals(item.getOrderDetailType())).collect(Collectors.toList());

		dto.setLastPrice((dto.getLastPrice() + dto.getShippingFee()) / 1.08); // Cập nhật lastPrice
		double priceToAllocate = dto.getLastPrice();
		// Kiểm tra có combo hay không
		boolean hasCombo = allSaleSplitDetails.stream().anyMatch(OrderDetailDTO::isCombo);

		if (!allSaleSplitDetails.isEmpty() && priceToAllocate > 0) {
			double allocatedSum = 0.0;
			if (hasCombo) {
				// Chỉ phân bổ giá cho các chi tiết combo trước
				List<OrderDetailDTO> comboDetails = allSaleSplitDetails.stream().filter(detail -> detail.isCombo()
						&& detail.getItemPrice() > 0 && "SALE".equals(detail.getOrderDetailType()))
						.collect(Collectors.toList());

				if (!comboDetails.isEmpty()) {
					// Tính tỷ lệ dựa trên itemPrice từ API
					double[] itemPricesArray = comboDetails.stream()
							.mapToDouble(item -> item.getUnitPrice() * item.getQuantity()).toArray();
					double[] ratios = calculateAllocationRatios(itemPricesArray);

					for (int i = 0; i < comboDetails.size(); i++) {
						OrderDetailDTO detail = comboDetails.get(i);
						double allocatedPrice = ratios[i] * priceToAllocate;
						detail.setLastItemPrice(allocatedPrice);
						detail.setUnitPrice(detail.getQuantity() > 0 ? allocatedPrice / detail.getQuantity() : 0.0);
						detail.setItemPrice(detail.getUnitPrice());
						allocatedSum += allocatedPrice;

					}
				} else {
					LOGGER.warning("Không có chi tiết combo hợp lệ để phân bổ priceToAllocate cho orderId "
							+ dto.getOrderId());
				}

				// Xử lý sản phẩm đơn: Gán phần còn lại
				List<OrderDetailDTO> singleDetails = allSaleSplitDetails.stream().filter(detail -> !detail.isCombo()
						&& "SALE".equals(detail.getOrderDetailType()) && detail.getItemPrice() > 0)
						.collect(Collectors.toList());

				if (!singleDetails.isEmpty()) {
					double remainingPrice = priceToAllocate - allocatedSum;
					double totalSingleItemPrice = singleDetails.stream()
							.mapToDouble(item -> item.getItemPrice() * item.getQuantity()).sum();

					for (int i = 0; i < singleDetails.size(); i++) {
						OrderDetailDTO detail = singleDetails.get(i);
						double allocatedPrice;
						if (i < singleDetails.size() - 1) {
							allocatedPrice = (totalSingleItemPrice > 0)
									? (detail.getItemPrice() * detail.getQuantity() / totalSingleItemPrice)
											* remainingPrice
									: remainingPrice / singleDetails.size();
							allocatedPrice = Math.round(allocatedPrice * 10000.0) / 10000.0;
							allocatedSum += allocatedPrice;
						} else {
							allocatedPrice = remainingPrice - (allocatedSum - priceToAllocate);
						}
						detail.setLastItemPrice(allocatedPrice);
						detail.setUnitPrice(detail.getQuantity() > 0 ? allocatedPrice / detail.getQuantity() : 0.0);
						detail.setItemPrice(detail.getUnitPrice());

					}
				}
			} else {
				// Sản phẩm đơn
				double totalItemPrice = allSaleSplitDetails.stream()
						.mapToDouble(item -> item.getItemPrice() * item.getQuantity()).sum();

				for (int i = 0; i < allSaleSplitDetails.size(); i++) {
					OrderDetailDTO detail = allSaleSplitDetails.get(i);
					if ("SALE".equals(detail.getOrderDetailType()) && detail.getItemPrice() > 0) {
						double allocatedPrice;
						if (i < allSaleSplitDetails.size() - 1) {
							allocatedPrice = (totalItemPrice > 0)
									? (detail.getItemPrice() * detail.getQuantity() / totalItemPrice) * priceToAllocate
									: priceToAllocate / allSaleSplitDetails.size();
							allocatedPrice = Math.round(allocatedPrice * 10000.0) / 10000.0;
							allocatedSum += allocatedPrice;
						} else {
							allocatedPrice = priceToAllocate - allocatedSum;
						}
						detail.setLastItemPrice(allocatedPrice);
						detail.setUnitPrice(detail.getQuantity() > 0 ? allocatedPrice / detail.getQuantity() : 0.0);
						detail.setItemPrice(detail.getUnitPrice());
						LOGGER.info("Single: orderId=" + dto.getOrderId() + ", sku=" + detail.getSku() + ", itemPrice="
								+ detail.getItemPrice() + ", lastItemPrice=" + detail.getLastItemPrice()
								+ ", unitPrice=" + detail.getUnitPrice() + ", quantity=" + detail.getQuantity());
					} else {
						detail.setLastItemPrice(0.0);
						detail.setUnitPrice(0.0);
						detail.setItemPrice(0.0);
					}
				}
			}

			// Kiểm tra cân bằng giá
			double sumItemPrice = allSaleSplitDetails.stream()
					.filter(item -> item != null && item.getLastItemPrice() != null)
					.mapToDouble(item -> item.getLastItemPrice() * item.getQuantity()).sum();

			if (Math.abs(sumItemPrice - priceToAllocate) > 0.01) {
				LOGGER.warning("⚠️ Tổng lastItemPrice KHÔNG KHỚP cho orderId " + dto.getOrderId() + " | sumItemPrice="
						+ sumItemPrice + ", priceToAllocate=" + priceToAllocate);
			} else {
				LOGGER.info("✅ Tổng lastItemPrice KHỚP cho orderId " + dto.getOrderId() + " | sumItemPrice="
						+ sumItemPrice + ", priceToAllocate=" + priceToAllocate);
			}
		}

		// Đặt giá PROMO về 0
		for (OrderDetailDTO item : dto.getOrderDetails()) {
			if ("PROMO".equals(item.getOrderDetailType())) {
				item.setLastItemPrice(0.0);
				item.setUnitPrice(0.0);
				item.setItemPrice(0.0);
				LOGGER.info(
						"Set promo item " + item.getOrderItemId() + " itemPrice, lastItemPrice, and unitPrice to 0");
			}
		}
	}

	private double[] calculateAllocationRatios(double[] itemPrices) {
		double totalPrice = Arrays.stream(itemPrices).sum();
		double[] ratios = new double[itemPrices.length];

		if (totalPrice <= 0) {
			Arrays.fill(ratios, 1.0 / itemPrices.length);
			return ratios;
		}

		for (int i = 0; i < itemPrices.length; i++) {
			ratios[i] = itemPrices[i] / totalPrice;
		}

		return ratios;
	}

	private Date fromUnixTime(long timestamp) {
		return timestamp <= 0 ? new Date() : new Date(timestamp * 1000);
	}

	private String generateSignature(Request request, String secret) {
		HttpUrl httpUrl = request.url();
		List<String> params = new ArrayList<>(httpUrl.queryParameterNames());
		params.removeIf(p -> p.equals("sign") || p.equals("access_token"));
		Collections.sort(params);

		StringBuilder paramStr = new StringBuilder(httpUrl.encodedPath());
		for (String param : params) {
			paramStr.append(param).append(httpUrl.queryParameter(param));
		}

		try {
			okhttp3.RequestBody body = request.body();
			if (body != null && !"multipart/form-data".equalsIgnoreCase(request.header("Content-Type"))) {
				Buffer buffer = new Buffer();
				body.writeTo(buffer);
				paramStr.append(buffer.readString(StandardCharsets.UTF_8));
			}
		} catch (Exception e) {
			throw new RuntimeException("Error reading request body", e);
		}

		return hmacSHA256(secret + paramStr.toString() + secret, secret);
	}

	private String generateWebhookSignature(String rawBody, String secret, String appKey) {
		return hmacSHA256(appKey + rawBody, secret);
	}

	private String hmacSHA256(String data, String key) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
			byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
			StringBuilder hex = new StringBuilder();
			for (byte b : hash) {
				hex.append(String.format("%02x", b));
			}
			return hex.toString();
		} catch (Exception e) {
			throw new RuntimeException("Error generating signature", e);
		}
	}

	private OrderDTO createDefaultOrderDTO(String orderId) {
		OrderDTO dto = new OrderDTO();
		dto.setOrderId(orderId);
		dto.seteCommerceType("TikTok");
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
		clone.setOrderDetailType(detail.getOrderDetailType());
		clone.setVoucherSeller(detail.getVoucherSeller());
		clone.setShippingDiscount(detail.getShippingDiscount());
		return clone;
	}

	private static TikTokAPIServletBondyCare instance = new TikTokAPIServletBondyCare();

	public static TikTokAPIServletBondyCare getInstance() {
		return instance;
	}

	public void setTikTokTokenManager(TikTokTokenManager manager) {
		this.tokenManager = manager;
	}

	@Inject
	private TikTokTokenService tikTokTokenService;

	private TikTokToken currentToken;
	// API URL
	private static final String TOKEN_URL = "https://auth.tiktok-shops.com/api/v2/token/refresh";
	private static final String INITIAL_TOKEN_URL = "https://auth.tiktok-shops.com/api/v2/token/get";
	private static final ObjectMapper MAPPER = new ObjectMapper();

	public synchronized String getAccessToken() {
		try {
			long now = System.currentTimeMillis() / 1000L;
			currentToken = tikTokTokenService.findById(2);

//		            if (currentToken == null) {
//		                initToken("ROW_SfT0ewAAAACcm1DPZz9DKY7JILx6Krt46WLaMizFTeokqFsRkgVQHYpprEPemjDUhs5ZTAlrXTuxeNwFfQBqMxTk-rKlBEbq");
//		            }

			if (now >= currentToken.getExpireTime() || isTokenExpiringSoon()) {
				refreshToken();
			}

			return currentToken.getAccessToken();

		} catch (Exception e) {
			LOGGER.severe("Error getting TikTok AccessToken: " + e.getMessage());
			throw new RuntimeException("Failed to get AccessToken", e);
		}
	}

	private void refreshToken() {
		try {
			String refreshToken = currentToken.getRefreshToken();
			if (refreshToken == null || refreshToken.trim().isEmpty()) {
				throw new RuntimeException("RefreshToken is null. Cần initToken(authCode) lại.");
			}

			String urlStr = TOKEN_URL + "?app_key=" + APP_KEY + "&app_secret=" + APP_SECRET + "&refresh_token="
					+ refreshToken + "&grant_type=refresh_token";

			LOGGER.info("Attempting to refresh token with URL: " + urlStr);

			JsonNode json = sendGetRequest(urlStr);

			JsonNode data = json.path("data");
			if (json.path("code").asInt() != 0 || data.isMissingNode()) {
				throw new RuntimeException("TikTok API error: " + json.path("message").asText());
			}

			String newAccessToken = data.path("access_token").asText();
			String newRefreshToken = data.path("refresh_token").asText();
			long expireIn = data.path("access_token_expire_in").asLong();
			long newExpireTime = System.currentTimeMillis() / 1000L + expireIn - 60;

			TikTokToken existingToken = tikTokTokenService.findById(2);
			if (existingToken == null) {
				throw new RuntimeException("TikTokToken not found in DB.");
			}

			existingToken.setAccessToken(newAccessToken);
			existingToken.setRefreshToken(newRefreshToken);
			existingToken.setExpireTime(newExpireTime);
			tikTokTokenService.update(existingToken);
			currentToken = existingToken;

			LOGGER.info("Token refreshed successfully.");

		} catch (Exception e) {
			LOGGER.severe("Failed to refresh TikTok token: " + e.getMessage());
			throw new RuntimeException("Refresh token failed", e);
		}
	}

	/**
	 * Check nếu token còn dưới 5 phút sẽ refresh
	 */
	private boolean isTokenExpiringSoon() {
		long currentTime = System.currentTimeMillis() / 1000L;
		return currentToken != null && currentToken.getExpireTime() - currentTime < 300;
	}

	private static JsonNode sendGetRequest(String urlStr) throws IOException {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(urlStr).openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setConnectTimeout(15000);
			conn.setReadTimeout(15000);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			int responseCode = conn.getResponseCode();
			InputStream inputStream = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();

			StringBuilder responseBuilder = new StringBuilder();
			try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
				String line;
				while ((line = in.readLine()) != null) {
					responseBuilder.append(line);
				}
			}

			return MAPPER.readTree(responseBuilder.toString());

		} finally {
			if (conn != null)
				conn.disconnect();
		}
	}
}