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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lixco.com.einvoice_service.EInvoiceService;
import lixco.com.entity.Customer;
import lixco.com.entity.EcomOrder;
import lixco.com.entity.EcomOrderDetail;
import lixco.com.entity.IECategories;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.PricingProgramDetail;
import lixco.com.entity.Product;
import lixco.com.entity.ShopeeToken;
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

@Path("/shopeeon1")
public class ShopeeAPIServletOn1 {

	private static final long PARTNER_ID = 2031029L;
	private static final String API_PARTNER_KEY = "shpk6c475559624c626d536770687454706c565542504f6873685144764d6144";
	private static final long SHOP_ID = 1516999140L;
	private static final Logger LOGGER = Logger.getLogger(ShopeeAPIServletOn1.class.getName());
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final String BASE_URL = "https://partner.shopeemobile.com";
	private static final Object PROCESS_LOCK = new Object();

	@Inject
	private EcomOrderService ecomOrderService;

	@Inject
	private ShopeeTokenManagerV2 shopeeTokenManager;

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
				forwardedHeaders.put(key, new ArrayList<>(values));
			}
		});

		executorService.submit(() -> {
			try {
//				String moduleAUrl = "http://192.168.0.83:8087/consumption/api/shopeeon1/webhook/order";
		            String moduleAUrl = "http://192.168.0.6:8980/consumption/api/shopeeon1/webhookshopee/ordershopee";

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

		return okResp;
	}

	@Getter
	@Setter
	boolean lamtron = true;
//	private void processShopeeWebhook(String orderSn, String status, long updateTime, String inputJson) {
//		synchronized (PROCESS_LOCK) {
//			long startTime = System.currentTimeMillis();
//			try {
//				LOGGER.info("Bắt đầu xử lý async cho Shopee orderSn: " + orderSn + " trên thread: "
//						+ Thread.currentThread().getName());
//
//				List<EcomOrder> existingOrders = ecomOrderService.findByOrderSn(orderSn);
//				if (!existingOrders.isEmpty()) {
//					for (EcomOrder order : existingOrders) {
//						order.setStatus(status);
//						order.setUpdatedAt(updateTime > 0 ? new Date(updateTime * 1000) : new Date());
//						order.setThoigiancapnhat(new Date());
//						EcomOrderUtils.setMyStatus(order);
//						ecomOrderService.update(order);
//
//						LOGGER.info("Updated status for existing orderSn: " + orderSn + ", orderType: "
//								+ order.getOrderType() + ", new status: " + status);
//
//						if ("TO_CONFIRM_RECEIVE".equalsIgnoreCase(order.getStatus())
//								|| "Đã giao đến người mua".equalsIgnoreCase(order.getMyStatus())) {
//
//							StringBuilder messages = new StringBuilder();
//							int result = processLogicInvoiceService.createInvoiceByOrderEcom(order, messages, false);
//
//							if (result == 0) {
//								List<Invoice> invoices = invoiceService.selectByOrderCode(order.getOrderId(),
//										order.getId());
//								if (invoices != null && !invoices.isEmpty()) {
//									ThongBao thongbao = InvoiceToJson.toJsonHoaDon2(invoices.get(0), eInvoiceService,
//											invoiceService, invoiceDetailService, ieInvoiceService, true, "HO CHI MINH",
//											"SYSTEM", productService);
////									ThongBao thongbao = InvoiceToJson.toJsonHoaDon(invoices.get(0), eInvoiceService,
////											invoiceService, invoiceDetailService, ieInvoiceService, lamtron,
////											"HO CHI MINH", null, productService, false);
//									if (!thongbao.isLoi()) {
//										String jsonInputString = thongbao.getDulieu();
//										LOGGER.info("JSON hóa đơn tạo thành công cho orderSn: " + orderSn);
//										invoices.get(0).setJsonhoadon(jsonInputString);
//										invoiceService.updateJsonHoaDon(invoices.get(0), null);
//										String resultInvoice = MisaInvoice.insertInvoiceTest(jsonInputString,
//												eInvoiceService);
////										String resultInvoice = MisaInvoice.insertInvoice(jsonInputString,
////												eInvoiceService);
//										LOGGER.info("Kết quả gửi e-Invoice cho orderSn: " + orderSn + ", result: "
//												+ resultInvoice);
//
//										if (resultInvoice != null && !resultInvoice.contains("\"success\":\"error\"")) {
//											LOGGER.info("Xuất hóa đơn điện tử thành công cho orderSn: " + orderSn);
//										} else {
//											LOGGER.warning("Xuất hóa đơn điện tử thất bại cho orderSn: " + orderSn
//													+ ", result: " + resultInvoice);
//										}
//									} else {
//										LOGGER.warning("Tạo JSON hóa đơn thất bại cho orderSn: " + orderSn + ", error: "
//												+ thongbao.getThongtinloi());
//									}
//								} else {
//									LOGGER.warning("Không tìm thấy Invoice cho orderSn: " + orderSn);
//								}
//							} else {
////								LOGGER.warning(
////										"Tạo hóa đơn Shopee chưa xuất HDDT: " + orderSn + ", messages: " + messages);
//							}
//						}
//					}
//
////					LOGGER.info("Kết thúc xử lý async cho Shopee orderSn: " + orderSn + ", thời gian: "
////							+ (System.currentTimeMillis() - startTime) + "ms");
//					return;
//				}
//
//				OrderDTO orderDTO = new OrderDTO();
//				orderDTO.setOrderId(orderSn);
//				orderDTO.setOrder_status(status);
//				orderDTO.setUpdatedAt(updateTime > 0 ? new Date(updateTime * 1000) : new Date());
//				orderDTO.seteCommerceType("Shopee");
//
//				try {
//					OrderDTO fetchedDTO = fetchOrder(orderSn);
//					if (fetchedDTO != null) {
//						orderDTO = fetchedDTO;
//						orderDTO.setOrder_status(status);
//						orderDTO.setUpdatedAt(updateTime > 0 ? new Date(updateTime * 1000) : new Date());
//						orderDTO.setDataJson(inputJson);
//					}
//				} catch (Exception e) {
//					LOGGER.warning("Failed to fetch order or escrow details for orderSn: " + orderSn + ", error: "
//							+ e.getMessage());
//				}
//
////				saveOrUpdateOrder(orderDTO);
//				LOGGER.info("Lưu đơn hàng Shopee mới thành công: " + orderSn);
//
//			} catch (Exception e) {
//				LOGGER.severe("Lỗi khi xử lý async Shopee orderSn: " + orderSn + " - " + e.getMessage());
//			}
//		}
//	}

	@POST
	@Path("/get/order")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchOrder(String requestBody) throws Exception {
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

		for (int attempt = 1; attempt <= maxRetries; attempt++) {
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
					throw new IOException("HTTP error code: " + responseCode + ", Response: " + jsonResponse);
				}

				LOGGER.info("Module B gọi Shopee thành công cho orderSn: " + orderSn);
				return Response.ok(jsonResponse).type(MediaType.APPLICATION_JSON).build();

			} catch (IOException ex) {
				lastException = ex;
				LOGGER.warning("Attempt " + attempt + " failed for orderSn " + orderSn + ": " + ex.getMessage());
				if (attempt < maxRetries) {
					Thread.sleep(delayMillis);
				}
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
		}

		throw new IOException("Failed to fetch order details after " + maxRetries + " attempts.", lastException);
	}

	
	
	@POST
	@Path("/get/order_detail") 
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchEscrowDetails(String requestBody) throws Exception {
		 try {
		        LOGGER.info("Module B nhận yêu cầu fetch order detail từ Module A: " + requestBody);

		        JsonNode requestJson = mapper.readTree(requestBody);
		        String orderSn = requestJson.path("orderSn").asText(null);

		        if (orderSn == null || orderSn.trim().isEmpty()) {
		            return Response.status(Response.Status.BAD_REQUEST)
		                    .entity("{\"error\": \"orderSn is required\"}")
		                    .type(MediaType.APPLICATION_JSON)
		                    .build();
		        }

		        String accessToken = getAccessToken();
		        long timestamp = System.currentTimeMillis() / 1000L;
		        String path = "/api/v2/payment/get_escrow_detail";

		        String baseString = PARTNER_ID + path + timestamp + accessToken + SHOP_ID;
		        String sign = generateHmacSHA256(baseString, API_PARTNER_KEY);

		        String url = BASE_URL + path + "?partner_id=" + PARTNER_ID 
		                   + "&timestamp=" + timestamp 
		                   + "&access_token=" + accessToken 
		                   + "&shop_id=" + SHOP_ID 
		                   + "&sign=" + sign 
		                   + "&order_sn=" + orderSn ;
		                 
		        System.out.println(url);
		        int maxRetries = 5;
		        long delayMillis = 5000;
		        IOException lastException = null;

		        for (int attempt = 1; attempt <= maxRetries; attempt++) {
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
		                try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
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
		                LOGGER.warning("Attempt " + attempt + " failed for orderSn " + orderSn + ": " + ex.getMessage());
		                if (attempt < maxRetries) {
		                    Thread.sleep(delayMillis);
		                }
		            } finally {
		                if (conn != null) conn.disconnect();
		            }
		        }

		        throw new IOException("Failed after " + maxRetries + " attempts", lastException);

		    } catch (Exception e) {
		        LOGGER.severe("Lỗi xử lý /get/order_detail: " + e.getMessage());
		        e.printStackTrace();
		        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
		                .entity("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}")
		                .type(MediaType.APPLICATION_JSON)
		                .build();
		    }
	}



	public void saveOrUpdateOrder(OrderDTO dto) {
		String orderNumber = dto.getOrderId();
		String platform = dto.geteCommerceType();
		String orderType = dto.getOrderType();
		String dataJSON = dto.getDataJson();
		EcomOrder existing = ecomOrderService.findByCodeAndPlatformAndOrderType(orderNumber, platform, orderType);
		EcomOrder order;

		if (existing == null) {
			order = new EcomOrder();
			String orderId = dto.getOrderId() + dto.getOrderType();
			order.setOrderId(orderId);
			order.setOrderNumber(orderNumber);
			order.setOrderType(orderType);
			order.setCustomerFirstName(dto.getCustomerFirstName());
			order.setCustomerLastName(dto.getCustomerLastName());
			order.setCreatedAt(dto.getCreatedAt());
			order.setLoaitmdt(platform);
			if (dataJSON != null) {
				order.setDataJson(dataJSON);
			} else {
				LOGGER.severe("Không thể lưu JSON:" + dataJSON);
			}
//			order.setDataJson(null);
		} else {
			order = existing;
		}

		order.setUpdatedAt(dto.getUpdatedAt());
		order.setPrice(dto.getPrice());
		order.setStatus(dto.getOrder_status());
		order.setThoigiancapnhat(new Date());
		order.setComboDiscount(dto.getComboDiscount());
		order.setShopeeDiscount(dto.getShopeeDiscount());
		order.setSellerDiscount(dto.getSellerDiscount());
		order.setDiscountedPrice(dto.getDiscountedPrice());
		order.setLastPrice(dto.getLastPrice());
		EcomOrderUtils.setMyStatus(order);
		Customer customer = customerService.selectByCode("OL248");
		order.setCustomer(customer);
		if (order.getOrderType() == "SALE") {
			IECategories ieCategories = iieCategoriesService.selectByCode("$");
			order.setIeCategories(ieCategories);
		} else if (order.getOrderType() == "PROMO") {
			IECategories ieCategories = iieCategoriesService.selectByCode("&");
			order.setIeCategories(ieCategories);
		}
		if (existing == null) {
			ecomOrderService.create(order);
		}
		List<EcomOrderDetail> newDetails = new ArrayList<>();
		List<EcomOrderDetail> oldDetails = ecomOrderDetailService.findByCodeAndPlatformAndOrderType(orderNumber,
				platform, orderType);
		for (EcomOrderDetail old : oldDetails) {
			ecomOrderDetailService.delete(old);
		}

		for (OrderDetailDTO splitDetail : dto.getOrderDetails()) {
			EcomOrderDetail orddetail = new EcomOrderDetail();
			orddetail.setOrder(order);
			orddetail.setOrderItemNumber(splitDetail.getOrderItemId());
			orddetail.setName(splitDetail.getName());
			orddetail.setSku(splitDetail.getSku());
			Product product = productService.selectByCode(orddetail.getSku());
			orddetail.setProduct(product);
			orddetail.setLoaitmdt(platform);
			orddetail.setVariant(splitDetail.isVariant());
			orddetail.setQuantity(splitDetail.getQuantity());
			orddetail.setItemPrice(splitDetail.getItemPrice());
			orddetail.setSplitPrice(splitDetail.getLastItemPrice());
			orddetail.setUnitPrice(splitDetail.getUnitPrice());
			orddetail.setOrderId(orderNumber);
			orddetail.setOrderType(splitDetail.getOrderDetailType());
			orddetail.setImageURL(splitDetail.getImageURL());
			ecomOrderDetailService.create(orddetail);
			newDetails.add(orddetail);
		}

		if ("SALE".equals(order.getOrderType()) && order.getSellerDiscount() != null
				&& order.getSellerDiscount() != 0) {
			EcomOrderDetail discountDetail = new EcomOrderDetail();
			discountDetail.setOrder(order);
			discountDetail.setOrderItemNumber("");
			Product discountProduct = productService.selectByCode("CKDH");
			if (discountProduct == null) {
				LOGGER.warning("Không tìm thấy sản phẩm CKDH cho chiết khấu đơn hàng: " + orderNumber);
			}
			discountDetail.setProduct(discountProduct);
			discountDetail.setName(discountProduct.getProduct_name());
			discountDetail.setLoaitmdt(platform);
			discountDetail.setVariant(false);
			discountDetail.setQuantity(0);
			discountDetail.setItemPrice(order.getSellerDiscount() / 1.08);
			discountDetail.setSplitPrice(order.getSellerDiscount() / 1.08);
			discountDetail.setUnitPrice(0.0);
			discountDetail.setOrderId(orderNumber);
			discountDetail.setOrderType("SALE");
			ecomOrderDetailService.create(discountDetail);
			newDetails.add(discountDetail);
			LOGGER.info("Lưu chi tiết chiết khấu: orderType=SALE, sku=CKDH, itemPrice=" + discountDetail.getItemPrice()
					+ ", lastItemPrice=" + discountDetail.getSplitPrice() + ", unitPrice="
					+ discountDetail.getUnitPrice() + ", quantity=" + discountDetail.getQuantity());
		}
		order.setOrderDetails(newDetails);
		ecomOrderService.update(order);
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
		newDetail.setItemPrice(itemPrice);

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

	private static ShopeeAPIServletOn1 instance = new ShopeeAPIServletOn1();

	public static ShopeeAPIServletOn1 getInstance() {
		return instance;
	}

	public void setShopeeTokenManager(ShopeeTokenManagerV2 manager) {
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

	private static final long EXPIRE_BUFFER_SECONDS = 1800;
	public synchronized String getAccessToken() {
        try {
            ShopeeToken tokenEntity = shopeeTokenService.findById(4);
            if (tokenEntity == null) throw new RuntimeException("Chưa có bản ghi Token ID=1 trong DB");

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
    @POST
	@Path("/warehouse")  
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JsonNode getWarehouseFilterConfig() throws Exception {
		String accessToken = getAccessToken();

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
    @POST
	@Path("/buyer_info")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getBuyerInfo(@Context HttpHeaders headers, String requestBody) throws Exception {
	    // 1. Lấy order_sn từ request của bạn (Giả sử bạn vẫn gửi đơn lẻ lên API của mình)
	    JsonNode requestJson = mapper.readTree(requestBody);
	    String orderSn = requestJson.path("order_sn").asText(null);

	    if (orderSn == null || orderSn.trim().isEmpty()) {
	        return "{\"error\": \"local_error\", \"message\": \"order_sn is required in your request body\"}";
	    }

	    // 2. Thông số hệ thống
	    String accessToken = getAccessToken();
	    long timestamp = System.currentTimeMillis() / 1000L;
	    String path = "/api/v2/order/get_buyer_invoice_info";

	    // 3. Tính SIGN (Vẫn là 5 tham số cơ bản)
	    String baseString = PARTNER_ID + path + timestamp + accessToken + SHOP_ID;
	    String sign = generateHmacSHA256(baseString, API_PARTNER_KEY);

	    // 4. URL
	    String urlStr = BASE_URL + path
	            + "?partner_id=" + PARTNER_ID
	            + "&timestamp=" + timestamp
	            + "&access_token=" + accessToken
	            + "&shop_id=" + SHOP_ID
	            + "&sign=" + sign;

	    HttpURLConnection conn = null;
	    try {
	        URL url = new URL(urlStr);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

	        // 5. TẠO PAYLOAD DẠNG QUERIES ARRAY (CHỖ NÀY QUAN TRỌNG)
	        ObjectNode finalPayload = mapper.createObjectNode();
	        ArrayNode queriesArray = finalPayload.putArray("queries");
	        ObjectNode orderNode = mapper.createObjectNode();
	        orderNode.put("order_sn", orderSn);
	        queriesArray.add(orderNode);

	        String jsonBody = mapper.writeValueAsString(finalPayload);
	        LOGGER.info("Shopee Request Payload: " + jsonBody);

	        try (OutputStream os = conn.getOutputStream()) {
	            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
	            os.flush();
	        }

	        // 6. ĐỌC PHẢN HỒI
	        int responseCode = conn.getResponseCode();
	        InputStream is = (responseCode >= 200 && responseCode < 300) 
	                         ? conn.getInputStream() 
	                         : conn.getErrorStream();

	        JsonNode result = mapper.readTree(is);
	        
	        // Trả về toàn bộ response từ Shopee để bạn xem cấu trúc
	        return mapper.writeValueAsString(result);

	    } finally {
	        if (conn != null) conn.disconnect();
	    }
	}
    
}