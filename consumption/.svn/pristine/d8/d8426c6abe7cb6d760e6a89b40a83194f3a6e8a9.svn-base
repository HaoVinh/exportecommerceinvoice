package vinh.lixco.com.apiecommerce;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lixco.com.entity.EcomOrder;
import lixco.com.entity.EcomOrderDetail;
import lixco.com.service.EcomOrderService;

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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@Path("lazada")
public class LazadaAPIServletCopy {
    private static final String APP_KEY = "133487";
    private static final String APP_SECRET = "RZTQdjz5VUdnpQ81koTwuQ0lrBJlnepC";
    private static final String API_URL = "https://api.lazada.vn/rest";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(LazadaAPIServletCopy.class.getName());
    private static final Map<String, OrderDTO> pendingUpdates = new HashMap<>();
    private static final String ACCESS_TOKEN = "50000501129yK3eZ0FYGhsirSGecfQ5lxwFghwK3D1169914bxVDealspK4Yk8Br";
    
    @Inject
    private EcomOrderService EcomOrderService;
    @Inject
    private lixco.com.service.EcomOrderDetailService EcomOrderDetailService;
    @POST
    @Path("/webhook/order")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    
  
    public Response webHookLazada(@Context HttpHeaders headers, String inputJson) {
        LOGGER.info("Nhận yêu cầu - Headers: " + headers.getRequestHeaders());
        LOGGER.info("Input JSON: " + inputJson);

        ObjectNode errorResponse = mapper.createObjectNode();

        try {
            // Xác thực chữ ký
            String authHeader = headers.getRequestHeader("Authorization") != null
                    && !headers.getRequestHeader("Authorization").isEmpty()
                    ? headers.getRequestHeader("Authorization").get(0)
                    : null;
            String computedSignature = SignatureUtil.getSignature(APP_KEY + inputJson, APP_SECRET);

            if (authHeader == null || !authHeader.equals(computedSignature)) {
                errorResponse.put("status", "error");
                errorResponse.put("message", "Header Authorization không hợp lệ hoặc thiếu");
//                LOGGER.warning("Xác thực thất bại - Kỳ vọng: " + computedSignature + ", Thực tế: " + authHeader);
                return Response.status(Response.Status.UNAUTHORIZED).entity(errorResponse)
                        .type(MediaType.APPLICATION_JSON).build();
            }

            // Phân tích JSON input
            JsonNode jsonNode = mapper.readTree(inputJson);
            long timestamp = System.currentTimeMillis();

            String sellerId = jsonNode.path("seller_id").asText(null);
            String site = jsonNode.path("site").asText("lazada_vn");
            int messageType = jsonNode.path("message_type").asInt(-1);

            
            JsonNode dataNode = jsonNode.path("data");
            if (dataNode.isMissingNode()) {
                throw new IllegalArgumentException("Thiếu data node trong JSON webhook");
            }

            String tradeOrderId = dataNode.path("trade_order_id").asText(null);
            if (tradeOrderId == null) {
                throw new IllegalArgumentException("Thiếu trade_order_id trong data webhook");
            }

            String orderStatus = dataNode.has("order_status") ? dataNode.path("order_status").asText(null)
                    : dataNode.path("status").asText(null);

            LOGGER.info("Xử lý webhook - trade_order_id: " + tradeOrderId + ", status: " + orderStatus);

            // Fetch dữ liệu từ API và lưu pendingUpdates
            OrderDTO orderDTO = fetchOrderData(tradeOrderId);
            List<OrderDetailDTO> orderDetailDTO = fetchOrderItems(tradeOrderId);
            if (orderDTO != null) {
                orderDTO.setOrder_status(orderStatus); // cập nhật trạng thái từ webhook
                synchronized (pendingUpdates) {
                    pendingUpdates.put(tradeOrderId, orderDTO);
                }
                saveOrUpdateOrder(orderDTO);
                LOGGER.info("Đã lưu OrderDTO vào pendingUpdates: " + tradeOrderId);
            } else {
                LOGGER.warning("Không thể lấy thông tin OrderDTO từ orderId: " + tradeOrderId);
            }

            // JSON phản hồi chuẩn
            ObjectNode responseJson = mapper.createObjectNode();
            responseJson.put("status", "success");
            responseJson.put("message", "Webhook được nhận và xử lý thành công");
            ObjectNode dataResp = mapper.createObjectNode();
            if (sellerId != null) dataResp.put("seller_id", sellerId);
            if (orderStatus != null) dataResp.put("order_status", orderStatus);
            dataResp.put("trade_order_id", tradeOrderId);
            responseJson.set("data", dataResp);
            responseJson.put("timestamp", timestamp);
            responseJson.put("site", site);
            responseJson.put("message_type", messageType);

            LOGGER.info("JSON phản hồi: " + mapper.writeValueAsString(responseJson));
            return Response.status(Response.Status.OK).entity(responseJson).type(MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            LOGGER.severe("Lỗi xử lý webhook: " + e.getMessage());
            errorResponse.put("status", "error");
            errorResponse.put("message", "Lỗi xử lý webhook: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse).type(MediaType.APPLICATION_JSON).build();
        }
    }
    public void saveOrUpdateOrder(OrderDTO dto) {
		EcomOrder existing = EcomOrderService.findByCode(dto.getOrderId());
		if (existing == null) {
			EcomOrder order = new EcomOrder();
			order.setOrderNumber(dto.getOrderId());
			order.setCustomerLastName(dto.getCustomerLastName());
			order.setCustomerFirstName(dto.getCustomerFirstName());
			order.setCreatedAt(dto.getCreatedAt());
			order.setUpdatedAt(dto.getUpdatedAt());
			order.setThoigiancapnhat(new Date());
			order.setPrice(dto.getPrice());
			order.setStatus(dto.getOrder_status());
			order.setLoaitmdt(dto.geteCommerceType());
			EcomOrderService.create(order);
			LOGGER.info("Lưu đơn hàng mới: " + dto.getOrderId());
		} else {
			existing.setStatus(dto.getOrder_status());
			existing.setUpdatedAt(dto.getUpdatedAt());
			existing.setThoigiancapnhat(new Date());
			EcomOrderService.update(existing);
			LOGGER.info("Cập nhật trạng thái đơn hàng: " + dto.getOrderId());
		}
	}
//    public void saveOrUpdateOrderDetail(OrderDetailDTO dtodt) {
//    	EcomOrderDetail existing = EcomOrderDetailService.findByCode(dtodt.getOrderItemId());
//    	if(existing == null) {
//    		EcomOrderDetail detail = new EcomOrderDetail();
//    		detail.setOrderItemNumber(dtodt.getOrderItemId());
//    		detail.setSku(dtodt.getSku());
//    		detail.setItemPrice(dtodt.getItemPrice());
//    		detail.setName(dtodt.getName());
//    		detail.setOrderId(dtodt.getOrderId());
//    		EcomOrderDetailService.create(detail);
//    	} else {
//    		EcomOrderDetailService.update(existing);
//    	}
//    }
    public OrderDTO fetchOrderData(String orderId) throws Exception {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("access_token", ACCESS_TOKEN);
        params.put("app_key", APP_KEY);
        params.put("order_id", orderId);
        params.put("sign_method", "sha256");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));

        String queryString = buildQueryString("/order/get", params);
        String sign = sign(queryString, APP_SECRET);
        params.put("sign", sign);

        String response = sendGetRequest(API_URL + "/order/get", params);
        JsonNode root = mapper.readTree(response);
        if (!"0".equals(root.path("code").asText())) return null;

        JsonNode data = root.path("data");

        String currentStatus = null;
        JsonNode statusesNode = data.path("statuses");
        if (statusesNode.isArray() && statusesNode.size() > 0) {
            currentStatus = statusesNode.get(statusesNode.size() - 1).asText();
        }

        OrderDTO dto = new OrderDTO();
        dto.setOrderId(data.path("order_id").asText(orderId));
        dto.setCustomerFirstName(data.path("customer_first_name").asText(null));
        dto.setCustomerLastName(data.path("customer_last_name").asText(null));
        dto.setCreatedAt(parseDate(data.path("created_at").asText()));
        dto.setUpdatedAt(parseDate(data.path("updated_at").asText()));
        dto.setPrice(data.path("price").asDouble(0.0));
        dto.seteCommerceType("Lazada");
        dto.setOrder_status(currentStatus); // ← cập nhật trạng thái hiện tại
        return dto;

    }
    public List<OrderDetailDTO> fetchOrderItems(String orderId) throws Exception {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("access_token", ACCESS_TOKEN);
        params.put("app_key", APP_KEY);
        params.put("order_id", orderId);
        params.put("sign_method", "sha256");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));

        String queryString = buildQueryString("/order/items/get", params);
        String sign = sign(queryString, APP_SECRET);
        params.put("sign", sign);

        String response = sendGetRequest(API_URL + "/order/items/get", params);
        JsonNode root = mapper.readTree(response);
        if (!"0".equals(root.path("code").asText())) {
            LOGGER.warning("Lỗi API /order/items/get - code: " + root.path("code").asText());
            return Collections.emptyList();
        }

        JsonNode data = root.path("data");
        if (!data.isArray()) return Collections.emptyList();

        List<OrderDetailDTO> result = new ArrayList<>();
        for (JsonNode itemNode : data) {
        	OrderDetailDTO item = new OrderDetailDTO();
            item.setSku(itemNode.path("sku").asText(null));
            item.setName(itemNode.path("name").asText(null));
            item.setItemPrice(itemNode.path("item_price").asDouble(0.0));
            item.setOrderItemId(itemNode.path("order_item_id").asText(null));
            result.add(item);
        }
        return result;
    }
    private Date parseDate(String dateStr) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        return sdf.parse(dateStr);
    }


    private String buildQueryString(String api, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(api);
        params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(e -> !e.getKey().equals("sign"))
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

    private String sendGetRequest(String urlStr, Map<String, String> params) throws Exception {
        StringBuilder urlWithParams = new StringBuilder(urlStr).append("?");
        for (Map.Entry<String, String> e : params.entrySet()) {
            urlWithParams.append(e.getKey()).append("=").append(e.getValue()).append("&");
        }
        urlWithParams.setLength(urlWithParams.length() - 1);

        URL url = new URL(urlWithParams.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }


    public static Map<String, OrderDTO> getPendingUpdates() {
        return pendingUpdates;
    }
    private static LazadaAPIServletCopy instance = new LazadaAPIServletCopy();

    public static LazadaAPIServletCopy getInstance() {
        return instance;
    }
    

}
