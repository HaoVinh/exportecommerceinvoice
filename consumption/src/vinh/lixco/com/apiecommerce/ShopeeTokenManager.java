
package vinh.lixco.com.apiecommerce;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lixco.com.entity.ShopeeToken;
import lixco.com.service.ShopeeTokenService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

@ApplicationScoped
public class ShopeeTokenManager {
	private static final Logger LOGGER = Logger.getLogger(ShopeeTokenManager.class.getName());
	private static final long PARTNER_ID = 2012115L;
	private static final String PARTNER_KEY = "shpk76694e444a51764f786775544b7a64446577784f7652476b72556b485163";
	private static final long SHOP_ID = 779607831L;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String BASE_URL = "https://partner.shopeemobile.com";

	@Inject
	private ShopeeTokenService shopeeTokenService;

	private ShopeeToken currentToken;

	public synchronized String getAccessToken() {
	    try {
	        long now = System.currentTimeMillis() / 1000L;
	        currentToken = shopeeTokenService.findById(1);

	        if (currentToken == null) {
	            throw new RuntimeException("ShopeeToken chưa được tạo trong DB.");
	        }

	        if (currentToken.getAccessToken() == null || currentToken.getAccessToken().trim().isEmpty()) {
	            throw new RuntimeException("AccessToken bị null hoặc rỗng trong ShopeeToken.");
	        }

	        if (now >= currentToken.getExpireTime()) {
	            LOGGER.info("AccessToken đã hết hạn. Làm mới...");
	            refreshToken();
	        }

	        return currentToken.getAccessToken();

	    } catch (Exception e) {
	        LOGGER.severe("Lỗi getAccessToken Shopee: " + e.getMessage());
	        throw new RuntimeException("Không thể lấy AccessToken", e);
	    }
	}



	private void refreshToken() {
	    try {
	        long timestamp = System.currentTimeMillis() / 1000L;
	        String path = "/api/v2/auth/access_token/get";
	        String sign = generateHmacSHA256(PARTNER_ID + path + timestamp, PARTNER_KEY);

	        String urlStr = BASE_URL + path + "?partner_id=" + PARTNER_ID + "&timestamp=" + timestamp + "&sign=" + sign;
	        Map<String, Object> payloadMap = new HashMap<>();
	        payloadMap.put("refresh_token", currentToken.getRefreshToken());
	        payloadMap.put("partner_id", PARTNER_ID);
	        payloadMap.put("shop_id", SHOP_ID);
	        String payload = MAPPER.writeValueAsString(payloadMap);
	        JsonNode json = sendPostRequest(urlStr, payload);

	        String newAccessToken = json.path("access_token").asText();
	        String newRefreshToken = json.path("refresh_token").asText();
	        long expireIn = json.path("expire_in").asLong();
	        long newExpireTime = timestamp + expireIn - 60; // Trừ 60 giây để an toàn

	        if (newAccessToken == null || newAccessToken.isEmpty()) {
	            throw new RuntimeException("Shopee không trả về access_token mới.");
	        }

	        ShopeeToken existingToken = shopeeTokenService.findById(1);
	        if (!newAccessToken.equals(existingToken.getAccessToken())) {
	            existingToken.setAccessToken(newAccessToken);
	            existingToken.setRefreshToken(newRefreshToken);
	            existingToken.setExpireTime(newExpireTime);
	            existingToken.setUsedAuthCode(true);
	            shopeeTokenService.update(existingToken);
	            this.currentToken = existingToken;
	            LOGGER.info("✅ Shopee token được cập nhật thành công.");
	        } else {
	            LOGGER.info("ℹ️ Shopee token chưa thay đổi, không cần cập nhật.");
	        }

	    } catch (Exception e) {
	        LOGGER.severe("Failed to refresh Shopee token: " + e.getMessage());
	        throw new RuntimeException("Refresh token failed", e);
	    }
	}

//	public synchronized void generateTokenFromAuthCode(String code) {
//		try {
//			// Check if token already exists and auth code was used
//			if (currentToken != null && currentToken.isUsedAuthCode()) {
//				LOGGER.info("Authorization code already used. Using existing token.");
//				return;
//			}
//
//			long timestamp = System.currentTimeMillis() / 1000L;
//			String path = "/api/v2/auth/token/get";
//			String sign = generateHmacSHA256(PARTNER_ID + path + timestamp, PARTNER_KEY);
//
//			String urlStr = BASE_URL + path + "?partner_id=" + PARTNER_ID + "&timestamp=" + timestamp + "&sign=" + sign;
//
//			Map<String, Object> payloadMap = new HashMap<>();
//			payloadMap.put("code", code);
//			payloadMap.put("shop_id", SHOP_ID);
//			payloadMap.put("partner_id", PARTNER_ID);
//			String payload = MAPPER.writeValueAsString(payloadMap);
//
//			System.out.println("Request URL: " + urlStr);
//			System.out.println("Payload: " + payload);
//
//			JsonNode json = sendPostRequest(urlStr, payload);
//			String accessToken = json.path("access_token").asText();
//			String refreshToken = json.path("refresh_token").asText();
//			long expireIn = json.path("expire_in").asLong();
//			long expireTime = timestamp + expireIn - 60;
//
//			ShopeeToken token = new ShopeeToken();
//			token.setAccessToken(accessToken);
//			token.setRefreshToken(refreshToken);
//			token.setExpireTime(expireTime);
//			token.setUsedAuthCode(true);
//
//			shopeeTokenService.update(token);
//			this.currentToken = token;
//
//			System.out.println("✅ Token mới đã tạo từ code auth: " + accessToken);
//		} catch (Exception e) {
//			throw new RuntimeException("❌ Lỗi tạo token từ code auth: " + e.getMessage(), e);
//		}
//	}

	private JsonNode sendPostRequest(String urlStr, String payload) throws IOException {
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

		return MAPPER.readTree(responseBuilder.toString());
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
//
//	public static void main(String[] args) {
//		ShopeeTokenManager tokenManager = new ShopeeTokenManager();
//		String authCode = "516f79697062534e4e6a506379644b6d";
//		tokenManager.generateTokenFromAuthCode(authCode);
//
//	}
}