package vinh.lixco.com.apiecommerce;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lazada.lazop.api.LazopClient;
import com.lazada.lazop.api.LazopRequest;
import com.lazada.lazop.api.LazopResponse;
import com.lazada.lazop.util.ApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TokenManager {
    private static final String APP_KEY = "133487";
    private static final String APP_SECRET = "RZTQdjz5VUdnpQ81koTwuQ0lrBJlnepC";
    private static final String AUTH_URL = "https://auth.lazada.com/rest";
    private static final String API_NAME_TOKEN = "/auth/token/create";
    private static final Logger LOGGER = Logger.getLogger(TokenManager.class.getName());
    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_DELAY_MS = 1000;
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String getAccessToken(String authCode) throws IOException {
        LazopClient client = new LazopClient(AUTH_URL, APP_KEY, APP_SECRET);
        LazopRequest request = new LazopRequest(API_NAME_TOKEN);
        request.setHttpMethod("GET");
        request.addApiParameter("app_key", APP_KEY);
        request.addApiParameter("code", authCode);
        request.addApiParameter("timestamp", String.valueOf(System.currentTimeMillis()));
        request.addApiParameter("sign_method", "sha256");

        Map<String, String> params = new HashMap<>();
        params.put("app_key", APP_KEY);
        params.put("code", authCode);
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("sign_method", "sha256");
        String sign = signApiRequest(params, null, APP_SECRET, "sha256", API_NAME_TOKEN);
        request.addApiParameter("sign", sign);

        LazopResponse response = executeWithRetry(client, request);
        if (!response.isSuccess() || !mapper.readTree(response.getBody()).has("code")
                || mapper.readTree(response.getBody()).get("code").asInt() != 0) {
            LOGGER.warning("Lỗi gọi /auth/token/create: " + response.getBody());
            throw new IOException("Không thể tạo access_token: " + response.getBody());
        }

        JsonNode dataNode = mapper.readTree(response.getBody()).get("data");
        if (dataNode != null && dataNode.has("access_token")) {
            return dataNode.get("access_token").asText();
        }
        throw new IOException("Không tìm thấy access_token trong response");
    }

    private static LazopResponse executeWithRetry(LazopClient client, LazopRequest request) throws IOException {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                LazopResponse response = client.execute(request);
                if (response.isSuccess()) {
                    return response;
                }
                LOGGER.warning("Lần thử " + (attempt + 1) + " thất bại: " + response.getBody());
                Thread.sleep(INITIAL_DELAY_MS * (long) Math.pow(2, attempt));
            } catch (ApiException | InterruptedException e) {
                LOGGER.severe("Lỗi: " + e.getMessage());
            }
            attempt++;
        }
        throw new IOException("Không thể tạo token sau " + MAX_RETRIES + " lần thử");
    }

    private static String signApiRequest(Map<String, String> params, String body, String appSecret, String signMethod, String apiName) throws IOException {
        String[] keys = params.keySet().toArray(new String[0]);
        java.util.Arrays.sort(keys);

        StringBuilder query = new StringBuilder();
        query.append(apiName);
        for (String key : keys) {
            String value = params.get(key);
            if (areNotEmpty(key, value) && !key.equals("sign") && !key.equals("partner_id")) {
                query.append(key).append(value);
            }
        }

        if (body != null) {
            query.append(body);
        }

        LOGGER.info("Chuỗi ký trước mã hóa: " + query.toString());
        byte[] bytes = encryptHMACSHA256(query.toString(), appSecret);
        String sign = byte2hex(bytes);
        LOGGER.info("Chữ ký sau mã hóa: " + sign);
        return sign;
    }

    private static byte[] encryptHMACSHA256(String data, String secret) throws IOException {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (java.security.GeneralSecurityException gse) {
            throw new IOException("Mã hóa HMAC-SHA256 thất bại: " + gse.getMessage());
        }
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    private static boolean areNotEmpty(String... values) {
        return java.util.Arrays.stream(values).allMatch(s -> s != null && !s.trim().isEmpty());
    }
    public static void main(String[] args) throws Exception {
//        generateAuthUrl(); 
        String[] tokens = getTokenShopLevel("79495757747479534d414964656f646f");
        System.out.println(tokens);
    }

    // Tạo URL để mở trình duyệt đăng nhập shop
    public static void generateAuthUrl() {
        long timestamp = System.currentTimeMillis() / 1000L;
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/shop/auth_partner";

        long partnerId = 2012115L;
        String partnerKey = "shpk76694e444a51764f786775544b7a64446577784f7652476b72556b485163";
        String redirectUrl = "https://dev1.lixco.vn/consumption/pages/caidat/donhangsan.jsf";

        String baseString = partnerId + path + timestamp;
        String sign = generateHmacSHA256(baseString, partnerKey);

        String url = host + path + "?partner_id=" + partnerId +
                "&timestamp=" + timestamp +
                "&sign=" + sign +
                "&redirect=" + redirectUrl;

        System.out.println("Auth URL: " + url);
    }

    // Nhận access_token và refresh_token lần đầu từ Shopee bằng mã AUTH_CODE
    public static String[] getTokenShopLevel(String authCode) throws Exception {
        String[] res = new String[2];

        long timestamp = System.currentTimeMillis() / 1000L;
        String host = "https://partner.shopeemobile.com";
        String path = "/api/v2/auth/token/get";

        long partnerId = 2012115L;
        String partnerKey = "shpk76694e444a51764f786775544b7a64446577784f7652476b72556b485163";
        long shopId = 779607831L;

        String baseString = partnerId + path + timestamp;
        String sign = generateHmacSHA256(baseString, partnerKey);

        String urlStr = host + path + "?partner_id=" + partnerId +
                "&timestamp=" + timestamp +
                "&sign=" + sign;

        // Tạo payload JSON
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("code", authCode);
        payloadMap.put("shop_id", shopId);
        payloadMap.put("partner_id", partnerId);
        String payload = mapper.writeValueAsString(payloadMap);

        // Gửi POST request
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes(StandardCharsets.UTF_8));
        }

        StringBuilder responseBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                responseBuilder.append(line);
            }
        }

        // Parse response JSON
        JsonNode json = mapper.readTree(responseBuilder.toString());
        res[0] = json.path("access_token").asText();
        res[1] = json.path("refresh_token").asText();

        System.out.println("Access Token: " + res[0]);
        System.out.println("Refresh Token: " + res[1]);

        return res;
    }

    // Hàm tạo chữ ký HMAC SHA256
    private static String generateHmacSHA256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] hashBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("HMAC SHA256 signature error", e);
        }
    }
}