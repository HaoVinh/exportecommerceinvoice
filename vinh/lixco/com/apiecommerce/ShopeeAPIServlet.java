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
import javax.ws.rs.GET;
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

import lixco.com.einvoice_service.EInvoiceService;
import lixco.com.entity.Customer;
import lixco.com.entity.EcomOrder;
import lixco.com.entity.EcomOrderDetail;
import lixco.com.entity.IECategories;
import lixco.com.entity.Invoice;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.PricingProgramDetail;
import lixco.com.entity.Product;
import lixco.com.entity.ShopeeToken;
import lixco.com.hddt.InvoiceToJson;
import lixco.com.hddt.MisaInvoice;
import lixco.com.hddt.ThongBao;
import lixco.com.interfaces.ICustomerPricingProgramService;
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
import lixco.com.service.ShopeeTokenService;
import lombok.Getter;
import lombok.Setter;

@Path("/shopee")
public class ShopeeAPIServlet {

	private static final int PARTNER_ID = 2030160;
	private static final String API_PARTNER_KEY = "shpk6b6e5843507275485253585072656c56587a6d6e4374477347647954714b";
	private static final long SHOP_ID = 773456943L;
	private static final Logger LOGGER = Logger.getLogger(ShopeeAPIServlet.class.getName());
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final String BASE_URL = "https://partner.shopeemobile.com";
	private static final Object PROCESS_LOCK = new Object();

	@Inject
	private EcomOrderService ecomOrderService;

	@Inject
	private ShopeeTokenManager shopeeTokenManager;

	@Inject
	private EcomOrderDetailService ecomOrderDetailService;

	@Inject
	private IIECategoriesService iieCategoriesService;

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
	private ICustomerPricingProgramService customerPricingProgramService;

	@Inject
	private IProductService productService;

	@Inject
	private IPricingProgramService priceProgramService;

	@Inject
	private ICustomerService customerService;

	@Inject
	private IPricingProgramDetailService pricingProgramDetailService;

	private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

	@POST
	@Path("/webhook/order")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response webhookShopee(@Context HttpHeaders headers, String inputJson) {
		ObjectNode resp = mapper.createObjectNode();
		resp.put("code", 0);
		resp.put("message", "OK");
		Response okResp = Response.ok(resp).build();

		// Lấy headers trước khi submit async (tránh mất contextual data)
		Map<String, List<String>> forwardedHeaders = new HashMap<>();
		headers.getRequestHeaders().forEach((key, values) -> {
			if (!key.equalsIgnoreCase("Host") && !key.equalsIgnoreCase("Content-Length")
					&& !key.equalsIgnoreCase("Connection")) {
				forwardedHeaders.put(key, new ArrayList<>(values)); // copy list để an toàn
			}
		});
		executorService.submit(() -> {
			try {
//	            String moduleAUrl = "http://192.168.0.83:8087/consumption/api/shopee/webhook/order";
				String moduleAUrl = "http://192.168.0.6:8980/consumption/api/shopee/webhook/order";

				LOGGER.info("Bắt đầu chuyển tiếp webhook sang module A: " + moduleAUrl);

				URL url = new URL(moduleAUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setConnectTimeout(15000);
				conn.setReadTimeout(30000);

				// Chuyển headers đã lưu
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
					// Đọc error nếu cần debug
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
				LOGGER.severe("Lỗi khi chuyển tiếp webhook Shopee sang module A: " + e.getMessage());
				e.printStackTrace();
			}
		});
		//Sau khi hoan tat chuyen webhook sang TTSP tiep tuc chuyen sang anh Trong
//		try {
//			webhookShopeeLocal(headers, inputJson);
//		}catch(Exception e) {
//			LOGGER.severe("Lỗi khi chuyển tiếp webhook từ trung gian sang module của anh Trọng: " + e.getMessage());
//		}

		return okResp;
	}
	//Ham day webhook qua anh Trong , code tuong tu ham nhan webhook shopee
	public Response webhookShopeeLocal(@Context HttpHeaders headers, String inputJson) {

		ObjectNode resp = mapper.createObjectNode();
		resp.put("code", 0);
		resp.put("message", "OK");
		Response okResp = Response.ok(resp).build();

		// Lấy headers trước khi submit async (tránh mất contextual data)
		Map<String, List<String>> forwardedHeaders = new HashMap<>();
		headers.getRequestHeaders().forEach((key, values) -> {
			if (!key.equalsIgnoreCase("Host") && !key.equalsIgnoreCase("Content-Length")
					&& !key.equalsIgnoreCase("Connection")) {
				forwardedHeaders.put(key, new ArrayList<>(values)); // copy list để an toàn
			}
		});

		executorService.submit(() -> {
			try {
//	            String moduleAUrl = "http://192.168.0.83:8087/consumption/api/shopee/webhook/order";
				String moduleAUrl = "http://192.168.0.226:63/consumption/api/shopee/webhook/order";

				LOGGER.info("Bắt đầu chuyển tiếp webhook sang module A: " + moduleAUrl);

				URL url = new URL(moduleAUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setConnectTimeout(15000);
				conn.setReadTimeout(30000);

				// Chuyển headers đã lưu
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
					LOGGER.info("Chuyển tiếp sang module anh Trong thành công - status: " + responseCode);
				} else {
					LOGGER.warning("Chuyển tiếp sang module anh Trong thất bại - status: " + responseCode);
					// Đọc error nếu cần debug
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
				LOGGER.severe("Lỗi khi chuyển tiếp webhook Shopee sang module A.Trong: " + e.getMessage());
			}
		});

		return okResp;
	}

	@POST
	@Path("/get/order")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchOrder(String requestBody) {
		try {
			JsonNode requestJson = mapper.readTree(requestBody);
			String orderSn = requestJson.path("orderSn").asText(null);

			if (orderSn == null || orderSn.trim().isEmpty()) {
				return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"orderSn is required\"}")
						.type(MediaType.APPLICATION_JSON).build();
			}

			LOGGER.info("Module B nhận yêu cầu fetch orderSn từ module A: " + orderSn);

			String accessToken = getAccessToken();
			long timestamp = System.currentTimeMillis() / 1000L;
			String path = "/api/v2/order/get_order_detail";
			String baseString = PARTNER_ID + path + timestamp + accessToken + SHOP_ID;
			String sign = generateHmacSHA256(baseString, API_PARTNER_KEY);

			String url = BASE_URL + path + "?partner_id=" + PARTNER_ID + "&timestamp=" + timestamp + "&access_token="
					+ accessToken + "&shop_id=" + SHOP_ID + "&sign=" + sign + "&order_sn_list=" + orderSn
					+ "&response_optional_fields=buyer_user_id,buyer_username,recipient_address,item_list,order_status,create_time,update_time,total_amount,payment_method,shipping_carrier";

			System.out.println(url);

			int maxRetries = 5;
			long delayMillis = 5000;
			IOException lastException = null;

			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(10000);
				conn.setRequestProperty("Accept", "application/json");

				int responseCode = conn.getResponseCode();
				InputStream inputStream = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();

				StringBuilder responseBuilder = new StringBuilder();
				try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
					String line;
					while ((line = in.readLine()) != null) {
						responseBuilder.append(line);
					}
				}

				String jsonResponse = responseBuilder.toString();
				if (responseCode != 200) {
					throw new IOException("Shopee HTTP error code: " + responseCode + ", Response: " + jsonResponse);
				}

				// Trả JSON từ Shopee trực tiếp về module A
				LOGGER.info("Module B gọi Shopee thành công cho orderSn: " + orderSn);
				return Response.ok(jsonResponse).type(MediaType.APPLICATION_JSON).build();

			} catch (IOException ex) {
				lastException = ex;

			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}

			throw new IOException("Failed to fetch order details after " + maxRetries + " attempts.", lastException);

		} catch (Exception e) {
			LOGGER.severe("Lỗi khi module B fetch order từ Shopee: " + e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"error\": \"" + e.getMessage() + "\"}").type(MediaType.APPLICATION_JSON).build();
		}
	}

	@POST
	@Path("/get/order_detail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchOrderDetail(String requestBody) {
		try {
			LOGGER.info("Module B nhận yêu cầu fetch order detail từ Module A: " + requestBody);

			JsonNode requestJson = mapper.readTree(requestBody);
			String orderSn = requestJson.path("orderSn").asText(null);

			if (orderSn == null || orderSn.trim().isEmpty()) {
				return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"orderSn is required\"}")
						.type(MediaType.APPLICATION_JSON).build();
			}

			String accessToken = getAccessToken();
			long timestamp = System.currentTimeMillis() / 1000L;
			String path = "/api/v2/payment/get_escrow_detail";

			String baseString = PARTNER_ID + path + timestamp + accessToken + SHOP_ID;
			String sign = generateHmacSHA256(baseString, API_PARTNER_KEY);

			String url = BASE_URL + path + "?partner_id=" + PARTNER_ID + "&timestamp=" + timestamp + "&access_token="
					+ accessToken + "&shop_id=" + SHOP_ID + "&sign=" + sign + "&order_sn=" + orderSn;

			System.out.println(url);
			int maxRetries = 5;
			long delayMillis = 5000;
			IOException lastException = null;

			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(10000);
				conn.setRequestProperty("Accept", "application/json");

				int responseCode = conn.getResponseCode();
				InputStream inputStream = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();

				StringBuilder responseBuilder = new StringBuilder();
				try (BufferedReader in = new BufferedReader(
						new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
					String line;
					while ((line = in.readLine()) != null) {
						responseBuilder.append(line);
					}
				}
				String jsonResponse = responseBuilder.toString();

				if (responseCode != 200) {
					throw new IOException("Shopee HTTP error code: " + responseCode + ", Response: " + jsonResponse);
				}

				LOGGER.info("Module B gọi Shopee thành công get_order_detail cho " + orderSn);
				return Response.ok(jsonResponse).type(MediaType.APPLICATION_JSON).build();

			} catch (IOException ex) {
				lastException = ex;
				LOGGER.warning("Attempt " + " failed for orderSn " + orderSn + ": " + ex.getMessage());

				Thread.sleep(delayMillis);

			} finally {
				if (conn != null)
					conn.disconnect();
			}

			throw new IOException("Failed after " + maxRetries + " attempts", lastException);

		} catch (Exception e) {
			LOGGER.severe("Lỗi xử lý /get/order_detail: " + e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}")
					.type(MediaType.APPLICATION_JSON).build();
		}
	}

//	private OrderDTO parseOrderDTO(JsonNode data, String orderSn, String jsonResponse) throws Exception {
//		OrderDTO saleDTO = new OrderDTO();
//		saleDTO.setOrderId(orderSn);
//		saleDTO.setOrder_status(data.path("order_status").asText());
//		saleDTO.setCreatedAt(new Date(data.path("create_time").asLong(0) * 1000));
//		saleDTO.setUpdatedAt(new Date(data.path("update_time").asLong(0) * 1000));
//		saleDTO.setPrice(data.path("total_amount").asDouble(0.0));
//		saleDTO.seteCommerceType("Shopee");
//		saleDTO.setOrderType("SALE");
//		saleDTO.setCustomerFirstName(data.path("recipient_address").path("name").asText(""));
//		saleDTO.setCustomerLastName("");
//		saleDTO.setDataJson(jsonResponse);
//		saleDTO.setDepartmentType("Marketing");
//		List<OrderDetailDTO> saleDetails = new ArrayList<>();
//		List<OrderDetailDTO> promoDetails = new ArrayList<>();
//		JsonNode itemList = data.path("item_list");
//		int index = 1;
//
//		for (JsonNode item : itemList) {
//			double apiItemPrice = item.path("model_discounted_price").asDouble(0.0);
//			String itemName = item.path("item_name").asText("");
//			String modelName = item.path("model_name").asText("");
//			if (!modelName.isEmpty()) {
//				itemName += " (" + modelName + ")";
//			}
//
//			OrderDetailDTO detail = new OrderDetailDTO();
//			detail.setOrderItemId(item.path("order_item_id").asText(""));
//			detail.setName(itemName);
//			String sku = item.path("model_sku").asText();
//			if (sku == null || sku.trim().isEmpty()) {
//				sku = item.path("item_sku").asText("");
//			}
//			detail.setSku(sku);
//			detail.setStt(index++);
//			detail.setQuantity(item.path("model_quantity_purchased").asInt(0));
//			detail.setOrderId(orderSn);
//			boolean isVariant = !item.path("main_item").asBoolean(true) && !item.path("model_sku").asText("").isEmpty();
//			detail.setVariant(isVariant);
//			detail.setLoaitmdt("Shopee");
//			detail.setPromotionType(item.path("promotion_type").asText(""));
//			detail.setItemPrice(apiItemPrice);
//			detail.setImageURL(item.path("image_info").path("image_url").asText());
//
//			saleDetails.add(detail); // tạm thêm vào saleDetails để sau gọi splitSku
//		}
//
//		// Gọi splitSku cho từng detail để tách combo và update tên từ productService
//		List<OrderDetailDTO> allSplitDetails = new ArrayList<>();
//		for (OrderDetailDTO detail : saleDetails) {
//			List<OrderDetailDTO> splitDetails = splitSku(detail);
//			allSplitDetails.addAll(splitDetails);
//		}
//
//		// Phân loại lại SALE và PROMO sau khi tách
//		saleDetails.clear();
//		promoDetails.clear();
//		for (OrderDetailDTO split : allSplitDetails) {
//			if ("SALE".equals(split.getOrderDetailType())) {
//				saleDetails.add(split);
//			} else if ("PROMO".equals(split.getOrderDetailType())) {
//				promoDetails.add(split);
//			}
//		}
//
//		// Cập nhật escrow cho đơn SALE
//		fetchEscrowDetails(saleDTO, saleDetails);
//
//		// Gán chi tiết cho đơn SALE
//		saleDTO.setOrderDetails(saleDetails);
//
//		// Tạo đơn PROMO riêng, copy cấu trúc và giá trị từ saleDTO (chia 1.08)
//		OrderDTO promoDTO = null;
//		if (!promoDetails.isEmpty()) {
//			promoDTO = new OrderDTO();
//			promoDTO.setOrderId(orderSn); // phân biệt nếu cần
//			promoDTO.setOrder_status(saleDTO.getOrder_status());
//			promoDTO.setCreatedAt(saleDTO.getCreatedAt());
//			promoDTO.setUpdatedAt(saleDTO.getUpdatedAt());
//			promoDTO.setPrice(saleDTO.getPrice());
//			promoDTO.seteCommerceType("Shopee");
//			promoDTO.setOrderType("PROMO");
//			promoDTO.setCustomerFirstName(saleDTO.getCustomerFirstName());
//			promoDTO.setCustomerLastName(saleDTO.getCustomerLastName());
//			promoDTO.setDataJson(jsonResponse);
//
//			promoDTO.setLastPrice(saleDTO.getLastPrice() != null ? saleDTO.getLastPrice() : 0.0);
//			promoDTO.setSellerDiscount(saleDTO.getSellerDiscount() != null ? saleDTO.getSellerDiscount() : 0.0);
//			promoDTO.setShopeeDiscount(saleDTO.getShopeeDiscount() != null ? saleDTO.getShopeeDiscount() : 0.0);
//			promoDTO.setComboDiscount(saleDTO.getComboDiscount() != null ? saleDTO.getComboDiscount() : 0.0);
//			promoDTO.setDiscountedPrice(saleDTO.getDiscountedPrice() != null ? saleDTO.getDiscountedPrice() : 0.0);
//			promoDTO.setDepartmentType("Marketing");
//			promoDTO.setOrderDetails(promoDetails);
//
//			saveOrUpdateOrder(promoDTO);
//		}
//
//		// Lưu đơn SALE
//		saveOrUpdateOrder(saleDTO);
//
//		return saleDTO;
//	}
//
//	public void saveOrUpdateOrder(OrderDTO dto) {
//		String orderNumber = dto.getOrderId();
//		String platform = dto.geteCommerceType();
//		String orderType = dto.getOrderType();
//		String dataJSON = dto.getDataJson();
//		String departmentType = dto.getDepartmentType();
//		EcomOrder existing = ecomOrderService.findByCodeAndPlatformAndOrderType(orderNumber, platform, orderType);
//		EcomOrder order;
//
//		if (existing == null) {
//			order = new EcomOrder();
//			String orderId = dto.getOrderId() + dto.getOrderType();
//			order.setOrderId(orderId);
//			order.setOrderNumber(orderNumber);
//			order.setOrderType(orderType);
//			order.setCustomerFirstName(dto.getCustomerFirstName());
//			order.setCustomerLastName(dto.getCustomerLastName());
//			order.setCreatedAt(dto.getCreatedAt());
//			order.setLoaitmdt(platform);
//			order.setDepartmentType(departmentType);
//			if (dataJSON != null) {
//				order.setDataJson(dataJSON);
//			} else {
//				LOGGER.severe("Không thể lưu JSON:" + dataJSON);
//			}
////			order.setDataJson(null);
//		} else {
//			order = existing;
//		}
//
//		order.setUpdatedAt(dto.getUpdatedAt());
//		order.setPrice(dto.getPrice());
//		order.setStatus(dto.getOrder_status());
//		order.setThoigiancapnhat(new Date());
//		order.setComboDiscount(dto.getComboDiscount());
//		order.setShopeeDiscount(dto.getShopeeDiscount());
//		order.setSellerDiscount(dto.getSellerDiscount());
//		order.setDiscountedPrice(dto.getDiscountedPrice());
//		order.setLastPrice(dto.getLastPrice());
//		
//		EcomOrderUtils.setMyStatus(order);
//		Customer customer = customerService.selectByCode("00073");
//		order.setCustomer(customer);
//		if (order.getOrderType() == "SALE") {
//			IECategories ieCategories = iieCategoriesService.selectByCode("$");
//			order.setIeCategories(ieCategories);
//		} else if (order.getOrderType() == "PROMO") {
//			IECategories ieCategories = iieCategoriesService.selectByCode("&");
//			order.setIeCategories(ieCategories);
//		}
//		if (existing == null) {
//			ecomOrderService.create(order);
//		}
//		List<EcomOrderDetail> newDetails = new ArrayList<>();
//		List<EcomOrderDetail> oldDetails = ecomOrderDetailService.findByCodeAndPlatformAndOrderType(orderNumber,
//				platform, orderType);
//		for (EcomOrderDetail old : oldDetails) {
//			ecomOrderDetailService.delete(old);
//		}
//
//		for (OrderDetailDTO splitDetail : dto.getOrderDetails()) {
//			EcomOrderDetail orddetail = new EcomOrderDetail();
//			orddetail.setOrder(order);
//			orddetail.setOrderItemNumber(splitDetail.getOrderItemId());
//			orddetail.setName(splitDetail.getName());
//			orddetail.setSku(splitDetail.getSku());
//			Product product = productService.selectByCode(orddetail.getSku());
//			orddetail.setProduct(product);
//			orddetail.setLoaitmdt(platform);
//			orddetail.setVariant(splitDetail.isVariant());
//			orddetail.setQuantity(splitDetail.getQuantity());
//			orddetail.setItemPrice(splitDetail.getItemPrice());
//			orddetail.setSplitPrice(splitDetail.getLastItemPrice());
//			orddetail.setUnitPrice(splitDetail.getUnitPrice());
//			orddetail.setOrderId(orderNumber);
//			orddetail.setOrderType(splitDetail.getOrderDetailType());
//			orddetail.setImageURL(splitDetail.getImageURL());
//			ecomOrderDetailService.create(orddetail);
//			newDetails.add(orddetail);
//		}
//
//		if ("SALE".equals(order.getOrderType()) && order.getSellerDiscount() != null
//				&& order.getSellerDiscount() != 0) {
//			EcomOrderDetail discountDetail = new EcomOrderDetail();
//			discountDetail.setOrder(order);
//			discountDetail.setOrderItemNumber("");
//			Product discountProduct = productService.selectByCode("CKDH");
//			if (discountProduct == null) {
//				LOGGER.warning("Không tìm thấy sản phẩm CKDH cho chiết khấu đơn hàng: " + orderNumber);
//			}
//			discountDetail.setProduct(discountProduct);
//			discountDetail.setName(discountProduct.getProduct_name());
//			discountDetail.setLoaitmdt(platform);
//			discountDetail.setVariant(false);
//			discountDetail.setQuantity(0);
//			discountDetail.setItemPrice(order.getSellerDiscount() / 1.08);
//			discountDetail.setSplitPrice(order.getSellerDiscount() / 1.08);
//			discountDetail.setUnitPrice(0.0);
//			discountDetail.setOrderId(orderNumber);
//			discountDetail.setOrderType("SALE");
//			ecomOrderDetailService.create(discountDetail);
//			newDetails.add(discountDetail);
//			LOGGER.info("Lưu chi tiết chiết khấu: orderType=SALE, sku=CKDH, itemPrice=" + discountDetail.getItemPrice()
//					+ ", lastItemPrice=" + discountDetail.getSplitPrice() + ", unitPrice="
//					+ discountDetail.getUnitPrice() + ", quantity=" + discountDetail.getQuantity());
//		}
//		order.setOrderDetails(newDetails);
//		ecomOrderService.update(order);
//	}

	private List<OrderDetailDTO> splitSku(OrderDetailDTO detail) {
		List<OrderDetailDTO> result = new ArrayList<>();

		String skuString = detail.getSku();
		if (skuString == null || skuString.trim().isEmpty()) {
			result.add(detail);
			return result;
		}
		skuString = skuString.trim().toUpperCase();

		// Pattern cho combo mua 1 tặng 1: "2C-A" (2 sản phẩm A)
		Pattern pattern = Pattern.compile("^(\\d+)C-(.+)$");
		Matcher matcher = pattern.matcher(skuString);

		if (matcher.matches()) {
			int comboMultiplier = Integer.parseInt(matcher.group(1)); // ví dụ 2
			String realSku = matcher.group(2).trim(); // A

			// Lấy tên sản phẩm từ productService (nghiệp vụ cũ)
			Product product = productService.selectByCode(realSku);
			String productName = (product != null && product.getProduct_name() != null) ? product.getProduct_name()
					: detail.getName();

			// Giá gốc từ detail (từ API Shopee)
			double originalItemPrice = detail.getItemPrice();
			double unitPriceAfterTax = originalItemPrice / 1.08;

			for (int i = 0; i < 2; i++) {
				OrderDetailDTO split = new OrderDetailDTO();
				split.setOrderItemId(detail.getOrderItemId());
				split.setName(productName);
				split.setSku(realSku);
				split.setStt(detail.getStt() + i);
				split.setQuantity(detail.getQuantity());
				split.setOrderId(detail.getOrderId());
				split.setVariant(detail.isVariant());
				split.setLoaitmdt(detail.getLoaitmdt());
				split.setPromotionType(detail.getPromotionType());
				split.setDiscountedPrice(detail.getDiscountedPrice());
				split.setImageURL(detail.getImageURL());
				split.setItemPrice(unitPriceAfterTax);
				split.setLastItemPrice(unitPriceAfterTax * detail.getQuantity());
				split.setUnitPrice(unitPriceAfterTax);
				split.setOrderDetailType(i == 0 ? "SALE" : "PROMO");

				result.add(split);
			}

			LOGGER.info(
					"Tách combo '" + skuString + "' → 2 dòng " + realSku + " (quantity mỗi dòng = 1): SALE unitPrice="
							+ unitPriceAfterTax + " | PROMO unitPrice=" + unitPriceAfterTax + ", lastItemPrice=0");
		} else {
			// SKU thường → giữ nguyên, dùng productService để update tên nếu có
			Product product = productService.selectByCode(skuString);
			if (product != null && product.getProduct_name() != null) {
				detail.setName(product.getProduct_name());
			}
			result.add(detail);
		}

		return result;
	}

	public JsonNode getWarehouseFilterConfig() throws Exception {
		String accessToken = getAccessToken();
		if (accessToken == null || accessToken.isEmpty()) {
			LOGGER.severe("Không lấy được access_token");
			return null;
		}

		long timestamp = System.currentTimeMillis() / 1000L;
		String path = "/api/v2/order/get_warehouse_filter_config";

		// Base string cho sign (cần access_token và shop_id)
		String baseString = PARTNER_ID + path + timestamp + accessToken + SHOP_ID;
		String sign = generateHmacSHA256(baseString, API_PARTNER_KEY);

		// Xây dựng URL (không cần thêm param warehouse_id hay warehouse_type)
		String urlStr = BASE_URL + path + "?partner_id=" + PARTNER_ID + "&timestamp=" + timestamp + "&access_token="
				+ accessToken + "&shop_id=" + SHOP_ID + "&sign=" + sign;

		LOGGER.info("Gọi API get_warehouse_filter_config: " + urlStr);

		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(urlStr).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(10000);
			conn.setRequestProperty("Accept", "application/json");

			int responseCode = conn.getResponseCode();
			InputStream inputStream = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();

			StringBuilder responseBuilder = new StringBuilder();
			try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
				String line;
				while ((line = in.readLine()) != null) {
					responseBuilder.append(line);
				}
			}

			String jsonResponse = responseBuilder.toString();
			JsonNode root = mapper.readTree(jsonResponse);

			if (responseCode != 200) {
				String errorMsg = root.has("message") ? root.path("message").asText() : "Không có message";
				LOGGER.severe("Lỗi gọi get_warehouse_filter_config: HTTP " + responseCode + " - " + errorMsg
						+ " - Response: " + jsonResponse);
				return null;
			}

			JsonNode responseNode = root.path("response");
			LOGGER.info("Thành công get_warehouse_filter_config: " + responseNode.toString());
			return responseNode;
		} catch (Exception e) {

			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	private String generateHmacSHA256(String data, String key) throws Exception {
		Mac mac = Mac.getInstance("HmacSHA256");
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		mac.init(secretKey);
		byte[] hashBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		for (byte b : hashBytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	private static ShopeeAPIServlet instance = new ShopeeAPIServlet();

	public static ShopeeAPIServlet getInstance() {
		return instance;
	}

	public void setShopeeTokenManager(ShopeeTokenManager manager) {
		this.shopeeTokenManager = manager;
	}

	@POST
	@Path("/token")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JsonNode sendPostRequest(String urlStr, String payload) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(10000);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");

		try (OutputStream os = conn.getOutputStream()) {
			os.write(payload.getBytes(StandardCharsets.UTF_8));
			os.flush();
		}

		int responseCode = conn.getResponseCode();
		if (responseCode != 200) {
			StringBuilder errorResponse = new StringBuilder();
			try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
				String line;
				while ((line = in.readLine()) != null) {
					errorResponse.append(line);
				}
			}
			throw new IOException("HTTP error code: " + responseCode + ", Response: " + errorResponse.toString());
		}

		StringBuilder responseBuilder = new StringBuilder();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			String line;
			while ((line = in.readLine()) != null) {
				responseBuilder.append(line);
			}
		}

		return mapper.readTree(responseBuilder.toString());
	}

	@Inject
	private ShopeeTokenService shopeeTokenService;

	private ShopeeToken currentToken;
	private static final long EXPIRE_BUFFER_SECONDS = 1800;

	public synchronized String getAccessToken() {
		try {
			ShopeeToken tokenEntity = shopeeTokenService.findById(1);
			if (tokenEntity == null)
				throw new RuntimeException("Chưa có bản ghi Token ID=1 trong DB");

			long now = System.currentTimeMillis() / 1000L;

			// Kiểm tra nếu token sắp hết hạn hoặc đã hết hạn
			if (now >= (tokenEntity.getExpireTime() - EXPIRE_BUFFER_SECONDS)) {
				LOGGER.info("Token sắp hết hạn hoặc đã hết hạn. Đang tiến hành làm mới...");
				return refreshToken(tokenEntity);
			}

			return tokenEntity.getAccessToken();
		} catch (Exception e) {
			LOGGER.severe("CRITICAL: Lỗi lấy AccessToken Shopee: " + e.getMessage());
			throw new RuntimeException("Service Unavailable: Shopee Token Issue", e);
		}
	}

	private String refreshToken(ShopeeToken currentToken) throws Exception {
		long timestamp = System.currentTimeMillis() / 1000L;
		String path = "/api/v2/auth/access_token/get";
		String sign = generateHmacSHA256(PARTNER_ID + path + timestamp, API_PARTNER_KEY);
		String urlStr = BASE_URL + path + "?partner_id=" + PARTNER_ID + "&timestamp=" + timestamp + "&sign=" + sign;

		Map<String, Object> payloadMap = new HashMap<>();
		payloadMap.put("refresh_token", currentToken.getRefreshToken());
		payloadMap.put("partner_id", PARTNER_ID);
		payloadMap.put("shop_id", SHOP_ID);

		String payload = mapper.writeValueAsString(payloadMap);

		try {
			JsonNode json = sendPostRequest(urlStr, payload);

			// Đọc dữ liệu từ response của Shopee
			String newAccessToken = json.path("access_token").asText();
			String newRefreshToken = json.path("refresh_token").asText();
			long expireInSeconds = json.path("expire_in").asLong();

			if (newAccessToken.isEmpty() || newRefreshToken.isEmpty()) {
				throw new RuntimeException("Shopee response missing tokens: " + json.toString());
			}

			// CẬP NHẬT LUÔN CẢ 2 VÀO DB - KHÔNG KIỂM TRA EQUALS
			currentToken.setAccessToken(newAccessToken);
			currentToken.setRefreshToken(newRefreshToken);
			currentToken.setExpireTime(timestamp + expireInSeconds);
			shopeeTokenService.update(currentToken);

			LOGGER.info("✅ Đã xoay vòng Token thành công. RefreshToken mới đã được lưu.");
			return newAccessToken;

		} catch (IOException e) {
			if (e.getMessage().contains("refresh_token_expired")) {
				// ĐÂY LÀ NƠI BẠN CẦN GỬI CẢNH BÁO (Telegram/Email)
				LOGGER.severe("ALERT: Refresh Token đã chết hẳn! Cần manual authorize lại gấp.");
			}
			throw e;
		}
	}

}